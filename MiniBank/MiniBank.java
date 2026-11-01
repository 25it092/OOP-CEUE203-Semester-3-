import java.util.Scanner;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Queue;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.lang.annotation.*;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;
import java.util.concurrent.*;
import static java.lang.System.out;

interface Transactable {
        void deposit(long amount);

        boolean withdraw(long amount);
}

interface InterestBearing {
        double interestRate();

        default double yearlyInterest() {
                return ((Account) this).getBalance() * interestRate() / 100;
        }
}

@FunctionalInterface
interface WithdrawRule {
        boolean allow(Account account, long amount);
}

interface Premium {
}

enum TransactionType {
        DEPOSIT, WITHDRAW, TRANSFER
}

record Command(TransactionType type, String accountNumber, long amount) {
}

record BankInfo(String bankName, String branch) {
}

enum MenuOption {
        OPEN, DEPOSIT, WITHDRAW, TRANSFER, EXIT
}

class Validator {
        static boolean isMobile(String mobile) {
                return mobile != null && Pattern.matches("[6-9][0-9]{9}", mobile);
        }

        static boolean isEmail(String email) {
                return email != null && Pattern.matches(
                                "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$", email);
        }

        static boolean isPAN(String pan) {
                return pan != null && Pattern.matches("[A-Z]{5}[0-9]{4}[A-Z]", pan);
        }

        
        static boolean isIFSC(String ifsc) {
                return ifsc != null && Pattern.matches("[A-Z]{4}0[A-Z0-9]{6}", ifsc);
        }

        static boolean isValidName(String name) {
                return name != null && name.trim().matches("[A-Za-z ]{2,50}");
        }

        static boolean isValidAmount(long amount) {
                return amount > 0;

        }
}

class CommandParser {
        static Command parse(String input) {
                String[] parts = input.trim().split("\\s+");

                if (parts.length != 3) {
                        throw new IllegalArgumentException(
                                        "Use: DEPOSIT accountNumber amount");
                }

                TransactionType type = TransactionType.valueOf(
                                parts[0].toUpperCase());

                if (type == TransactionType.TRANSFER) {
                        throw new IllegalArgumentException(
                                        "Use the banking menu for transfers.");
                }

                long amount = Long.parseLong(parts[2]);

                if (amount <= 0) {
                        throw new IllegalArgumentException(
                                        "Amount must be greater than zero.");
                }

                return new Command(type, parts[1], amount);
        }
}

class StatementFormatter {
        static String buildStatement(Account account) {
                return "Account Number: " + account.getAccountnum()
                                + "\nOwner: " + account.getOwner()
                                + "\nBalance: " + account.getBalance()
                                + "\nInterest Rate: " + account.interestRate() + "%"
                                + "\nYearly Interest: " + account.yearlyInterest()
                                + "\nActive: " + account.isActive();
        }
}

class Customer implements Cloneable {
        private static int customerCounter = 100;
        private final String customerId;
        private String name;
        private Address address;

        Customer(String name, Address address) {
                if (!Validator.isValidName(name)) {
                        throw new IllegalArgumentException("Invalid customer name.");
                }

                this.customerId = "CUST" + (++customerCounter);
                this.name = name.trim();
                this.address = address;
        }

        public String getCustomerId() {
                return customerId;
        }

        public String getName() {
                return name;
        }

        public Address getAddress() {
                return address;
        }

        @Override
        public Customer clone() {
                try {
                        Customer copy = (Customer) super.clone();
                        copy.address = address == null ? null : address.clone();
                        return copy;
                } catch (CloneNotSupportedException e) {
                        throw new AssertionError(e);
                }
        }

        static class Address implements Cloneable {
                private String city;
                private String state;

                Address(String city, String state) {
                        this.city = city;
                        this.state = state;
                }

                public String getCity() {
                        return city;
                }

                public String getState() {
                        return state;
                }

                @Override
                public Address clone() {
                        try {
                                return (Address) super.clone();
                        } catch (CloneNotSupportedException e) {
                                throw new AssertionError(e);
                        }
                }

                @Override
                public String toString() {
                        return city + ", " + state;
                }
        }

        @Override
        public String toString() {
                return "Customer ID: " + customerId
                                + "\nName: " + name
                                + "\nAddress: " + address;
        }
}

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@interface Id {
}

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@interface Positive {
        String message() default "must be greater than zero";
}

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@interface MaxLength {
        int value();
}

abstract class Account implements Transactable, InterestBearing {
        @Id
        private final String accountnum;

        @MaxLength(50)
        private String owner;

        @Positive
        private long balance;

        private boolean active;
        private static long accountCounter = 1000;

        private static synchronized String generateAccountNumber() {
                return String.format("AC%04d", ++accountCounter);
        }

        Account(String name, long balance) {
                if (!Validator.isValidName(name)) {
                        throw new IllegalArgumentException("Invalid account owner name.");
                }

                if (balance < 0) {
                        throw new IllegalArgumentException(
                                        "Opening balance cannot be negative.");
                }

                this.accountnum = generateAccountNumber();
                this.owner = name.trim();
                this.balance = balance;

                this.active = true;
        }

        Account(String name) {
                this(name, 0);
        }

        @Override
        public synchronized void deposit(long amount) {
                if (!active) {
                        throw new IllegalStateException("Account is inactive.");
                }

                if (amount <= 0) {
                        throw new IllegalArgumentException(
                                        "Amount must be greater than zero.");
                }

                balance = Math.addExact(balance, amount);
        }

        @Override
        public synchronized boolean withdraw(long amount) {
                if (!active || amount <= 0 || !canWithdraw(amount)) {
                        return false;
                }

                balance -= amount;
                return true;
        }

        public abstract boolean canWithdraw(long amount);

        public String getAccountnum() {
                return accountnum;
        }

        public String getOwner() {
                return owner;
        }

        public synchronized long getBalance() {

                return balance;
        }

        public boolean isActive() {
                return active;
        }

        public synchronized void setActive(boolean active) {
                this.active = active;
        }

        public boolean transferTo(Account target, long amount) {
                if (target == null || target == this || amount <= 0) {
                        return false;
                }

                Account first = accountnum.compareTo(target.accountnum) < 0
                                ? this
                                : target;
                Account second = first == this ? target : this;

                synchronized (first) {
                        synchronized (second) {
                                if (!active || !target.active || !canWithdraw(amount)) {
                                        return false;
                                }

                                long newTargetBalance = Math.addExact(
                                                target.balance, amount);

                                balance -= amount;
                                target.balance = newTargetBalance;
                                return true;
                        }
                }
        }

        @Override
        public String toString() {
                return "Account Number: " + accountnum
                                + "\nOwner: " + owner
                                + "\nBalance: " + getBalance()
                                + "\nActive: " + active;

        }

        @Override
        public boolean equals(Object obj) {
                if (this == obj) {
                        return true;
                }

                if (!(obj instanceof Account account)) {
                        return false;
                }

                return accountnum.equals(account.accountnum);
        }

        @Override
        public int hashCode() {
                return Objects.hash(accountnum);
        }
}

class SavingsAccount extends Account implements Premium {
        private final long minBalance;

        SavingsAccount(String name, long balance, long minBalance) {
                super(name, balance);

                if (minBalance < 0 || balance < minBalance) {
                        throw new IllegalArgumentException(
                                        "Opening balance must meet the minimum balance.");
                }

                this.minBalance = minBalance;
        }

        @Override
        public double interestRate() {
                return 4.0;
        }

        @Override
        public boolean canWithdraw(long amount) {

                return amount > 0 && getBalance() - amount >= minBalance;
        }
}

class CurrentAccount extends Account {
        private final long overdraftLimit;

        CurrentAccount(String name, long balance, long overdraftLimit) {
                super(name, balance);

                if (overdraftLimit < 0) {
                        throw new IllegalArgumentException(
                                        "Overdraft limit cannot be negative.");
                }

                this.overdraftLimit = overdraftLimit;
        }

        @Override
        public double interestRate() {
                return 0.0;
        }

        @Override
        public boolean canWithdraw(long amount) {
                return amount > 0
                                && getBalance() - amount >= -overdraftLimit;
        }
}

class FixedDepositAccount extends Account {
        FixedDepositAccount(String name, long balance) {
                super(name, balance);
        }

        @Override
        public double interestRate() {
                return 7.0;
        }

        @Override
        public boolean canWithdraw(long amount) {

                return false;
        }
}

class AnnotationValidator {
        static void validate(Object obj) {
                Class<?> type = obj.getClass();

                while (type != null) {
                        for (Field field : type.getDeclaredFields()) {
                                field.setAccessible(true);

                                try {
                                        Object value = field.get(obj);

                                        if (field.isAnnotationPresent(Id.class)) {
                                                out.println(field.getName() + " is an ID field.");
                                        }

                                        if (field.isAnnotationPresent(Positive.class)
                                                        && value instanceof Number number
                                                        && number.longValue() <= 0) {
                                                out.println(field.getName() + ": "
                                                                + field.getAnnotation(Positive.class)
                                                                                .message());
                                        }

                                        if (field.isAnnotationPresent(MaxLength.class)
                                                        && value instanceof String text
                                                        && text.length() > field.getAnnotation(
                                                                        MaxLength.class).value()) {
                                                out.println(field.getName()
                                                                + " exceeds maximum length.");
                                        }
                                } catch (IllegalAccessException e) {
                                        out.println("Unable to validate " + field.getName());
                                }
                        }

                        type = type.getSuperclass();
                }
        }

}

class BankException extends Exception {
        BankException(String message) {
                super(message);
        }
}

class InvalidAmountException extends BankException {
        InvalidAmountException(String message) {
                super(message);
        }
}

class InsufficientFundsException extends BankException {
        private final long shortfall;

        InsufficientFundsException(String message, long shortfall) {
                super(message);
                this.shortfall = shortfall;
        }

        public long getShortfall() {
                return shortfall;
        }
}

class AccountNotFoundException extends BankException {
        AccountNotFoundException(String message) {
                super(message);
        }
}

class BankOperations {
        static void deposit(Account account, long amount)
                        throws InvalidAmountException {
                if (account == null) {
                        throw new IllegalArgumentException("Account is required.");
                }

                if (amount <= 0) {
                        throw new InvalidAmountException(

                                        "Amount must be greater than zero.");
                }

                try {
                        account.deposit(amount);
                } catch (ArithmeticException e) {
                        throw new IllegalArgumentException("Amount is too large.");
                }
        }

        static void withdraw(Account account, long amount)
                        throws InvalidAmountException, InsufficientFundsException {
                if (account == null) {
                        throw new IllegalArgumentException("Account is required.");
                }

                if (amount <= 0) {
                        throw new InvalidAmountException(
                                        "Amount must be greater than zero.");
                }

                if (!account.withdraw(amount)) {
                        throw new InsufficientFundsException(
                                        "Withdrawal failed. Check balance, minimum balance, "
                                                        + "or account status.",
                                        Math.max(0, amount - account.getBalance()));
                }
        }

        static void transfer(Account from, Account to, long amount)
                        throws BankException {
                if (from == null || to == null) {
                        throw new AccountNotFoundException("Account not found.");
                }

                if (amount <= 0) {
                        throw new InvalidAmountException(
                                        "Amount must be greater than zero.");
                }

                try {
                        if (!from.transferTo(to, amount)) {

                                throw new InsufficientFundsException(
                                                "Transfer failed. Check account status and balance.",
                                                Math.max(0, amount - from.getBalance()));
                        }
                } catch (ArithmeticException e) {
                        throw new IllegalArgumentException(
                                        "Transfer amount is too large.");
                }
        }
}

class AccountWorker implements Runnable {
        private final Account account;
        private final int times;
        private final long amount;

        AccountWorker(Account account, int times, long amount) {
                this.account = account;
                this.times = times;
                this.amount = amount;
        }

        @Override
        public void run() {
                for (int i = 0; i < times; i++) {
                        account.deposit(amount);
                }

                out.println(Thread.currentThread().getName() + " completed.");
        }
}

class RaceConditionDemo {
        private static int unsafeBalance = 0;

        public static void main(String[] args) throws InterruptedException {
                Thread[] threads = new Thread[10];

                for (int i = 0; i < threads.length; i++) {
                        threads[i] = new Thread(() -> {
                                for (int j = 0; j < 1000; j++) {
                                        int value = unsafeBalance;

                                        Thread.yield();
                                        unsafeBalance = value + 1;
                                }
                        });

                        threads[i].start();
                }

                for (Thread thread : threads) {
                        thread.join();
                }

                out.println("Unsafe balance: " + unsafeBalance);

                Account account = new SavingsAccount("Test", 0, 0);
                Thread[] safeThreads = new Thread[10];

                for (int i = 0; i < safeThreads.length; i++) {
                        safeThreads[i] = new Thread(
                                        new AccountWorker(account, 1000, 1));
                        safeThreads[i].start();
                }

                for (Thread thread : safeThreads) {
                        thread.join();
                }

                out.println("Synchronized balance: " + account.getBalance());
        }
}

class TransactionProcessor {
        private final ExecutorService pool = Executors.newFixedThreadPool(4);

        void submit(Runnable task) {
                pool.submit(task);
        }

        void stop() {
                pool.shutdown();

                try {
                        if (!pool.awaitTermination(30, TimeUnit.SECONDS)) {
                                pool.shutdownNow();
                        }
                } catch (InterruptedException e) {
                        pool.shutdownNow();
                        Thread.currentThread().interrupt();
                }
        }
}

class TransactionProcessorDemo {
        public static void main(String[] args) {
                Account account = new SavingsAccount("Test", 0, 0);
                TransactionProcessor processor = new TransactionProcessor();

                for (int i = 0; i < 10; i++) {
                        processor.submit(() -> account.deposit(100));
                }

                processor.stop();
                out.println("Final balance: " + account.getBalance());
        }
}

class TransactionBuffer {
        private final Queue<Integer> buffer = new LinkedList<>();
        private final int capacity;
        private boolean finished;

        TransactionBuffer(int capacity) {
                this.capacity = capacity;
        }

        synchronized void produce(int value) throws InterruptedException {
                while (buffer.size() == capacity) {
                        wait();
                }

                buffer.add(value);
                out.println("Produced: " + value);
                notifyAll();

        }

        synchronized Integer consume() throws InterruptedException {
                while (buffer.isEmpty() && !finished) {
                        wait();
                }

                if (buffer.isEmpty() && finished) {
                        return null;
                }

                int value = buffer.remove();
                notifyAll();
                return value;
        }

        synchronized void finish() {
                finished = true;
                notifyAll();
        }
}

class ProducerConsumerDemo {
        public static void main(String[] args) {
                TransactionBuffer buffer = new TransactionBuffer(5);

                Thread producer = new Thread(() -> {
                        try {
                                for (int i = 1; i <= 10; i++) {
                                        buffer.produce(i);
                                }

                                buffer.finish();
                        } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                                buffer.finish();
                        }
                });

                Thread consumer = new Thread(() -> {
                        try {
                                Integer value;

                                while ((value = buffer.consume()) != null) {
                                        out.println("Consumed: " + value);
                                }
                        } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                        }
                });

                producer.start();
                consumer.start();

                try {
                        producer.join();
                        consumer.join();
                } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                }
        }
}

class DeadlockSafeTransferDemo {
        public static void main(String[] args) throws InterruptedException {
                Account first = new SavingsAccount("Asha", 5000, 0);
                Account second = new SavingsAccount("Riya", 5000, 0);

                Thread t1 = new Thread(() -> {
                        for (int i = 0; i < 100; i++) {
                                first.transferTo(second, 1);
                        }
                });

                Thread t2 = new Thread(() -> {
                        for (int i = 0; i < 100; i++) {
                                second.transferTo(first, 1);
                        }
                });

                t1.start();
                t2.start();
                t1.join();
                t2.join();

                out.println("First account: " + first.getBalance());
                out.println("Second account: " + second.getBalance());
                out.println("Total balance: "
                                + (first.getBalance() + second.getBalance()));
        }
}

class ExceptionHandlingDemo {
        public static void main(String[] args) {
                Account account = new SavingsAccount("Test", 5000, 500);

                try {
                        BankOperations.withdraw(account, 6000);
                } catch (BankException e) {
                        out.println(e.getMessage());
                } finally {
                        out.println("Transaction attempt completed.");
                }

                out.println("Final balance: " + account.getBalance());
        }
}

class Repository<T> {
        private final Map<String, T> records = new LinkedHashMap<>();
        private final Function<T, String> idGetter;

        Repository(Function<T, String> idGetter) {
                this.idGetter = idGetter;
        }

        void save(T item) {
                records.put(idGetter.apply(item), item);
        }

        T findById(String id) {
                return records.get(id);
        }

        boolean delete(String id) {
                return records.remove(id) != null;

        }

        List<T> findAll() {
                return new ArrayList<>(records.values());
        }

        int size() {
                return records.size();
        }
}

class StatePersister {
        private static final Path FILE = Path.of("minibank_accounts.txt");

        static void saveAccounts(Map<String, Account> accounts)
                        throws IOException {
                List<String> lines = new ArrayList<>();

                for (Account account : accounts.values()) {
                        lines.add(account.getAccountnum() + "|"
                                        + account.getOwner() + "|"
                                        + account.getBalance() + "|"
                                        + account.getClass().getSimpleName() + "|"
                                        + account.isActive());
                }

                Files.write(FILE, lines);
        }

        static void loadAccounts(Map<String, Account> accounts)
                        throws IOException {
                if (!Files.exists(FILE)) {
                        return;
                }

                for (String line : Files.readAllLines(FILE)) {
                        out.println(line);
                }

                out.println("Saved account records were read from the file.");
                out.println("This file is a text backup, not an automatic "
                                + "account-restoration file.");

        }
}

class TransactionLog {
        private static final Path FILE = Path.of("minibank_transactions.txt");

        static synchronized void log(String message) {
                try {
                        Files.writeString(FILE,
                                        java.time.LocalDateTime.now() + " | " + message
                                                        + System.lineSeparator(),
                                        java.nio.file.StandardOpenOption.CREATE,
                                        java.nio.file.StandardOpenOption.APPEND);
                } catch (IOException e) {
                        out.println("Unable to write transaction log: "
                                        + e.getMessage());
                }
        }
}

class ReportGenerator {
        static void printReport(Map<String, Account> accounts) {
                long total = 0;

                out.println("\n===== ACCOUNT REPORT =====");

                for (Account account : accounts.values()) {
                        out.println(account.getAccountnum() + " | "
                                        + account.getOwner() + " | "
                                        + account.getBalance() + " | "
                                        + account.getClass().getSimpleName());

                        total = Math.addExact(total, account.getBalance());
                }

                out.println("Number of accounts: " + accounts.size());
                out.println("Total balance: " + total);
        }
}

class BankService {
        private final Map<String, Account> accounts = new LinkedHashMap<>();

        private final Repository<Account> repository = new Repository<>(Account::getAccountnum);

        BankService() {
                addAccount(new SavingsAccount("Yashvi", 55000, 500));
                addAccount(new CurrentAccount("Rahul", 20000, 10000));
                addAccount(new FixedDepositAccount("Amit", 100000));
        }

        private void addAccount(Account account) {
                accounts.put(account.getAccountnum(), account);
                repository.save(account);
        }

        Account openSavingsAccount(String name, long balance)
                        throws InvalidAmountException {
                if (balance < 500) {
                        throw new InvalidAmountException(
                                        "Savings account needs at least 500 to open.");
                }

                Account account = new SavingsAccount(name, balance, 500);
                addAccount(account);
                TransactionLog.log("OPEN " + account.getAccountnum());
                save();
                return account;
        }

        Account findAccount(String number) throws AccountNotFoundException {
                Account account = repository.findById(number);

                if (account == null) {
                        throw new AccountNotFoundException("Account not found.");
                }

                return account;
        }

        void deposit(String number, long amount)
                        throws BankException {
                Account account = findAccount(number);
                BankOperations.deposit(account, amount);

                TransactionLog.log("DEPOSIT " + number + " " + amount);
                save();
        }

        void withdraw(String number, long amount)
                        throws BankException {
                Account account = findAccount(number);
                BankOperations.withdraw(account, amount);
                TransactionLog.log("WITHDRAW " + number + " " + amount);
                save();
        }

        void transfer(String fromNumber, String toNumber, long amount)
                        throws BankException {
                Account from = findAccount(fromNumber);
                Account to = findAccount(toNumber);
                BankOperations.transfer(from, to, amount);
                TransactionLog.log("TRANSFER " + fromNumber + " TO "
                                + toNumber + " " + amount);
                save();
        }

        void listAccounts() {
                for (Account account : repository.findAll()) {
                        out.println(account);
                        out.println();
                }
        }

        void printStatement(String number) throws BankException {
                out.println(StatementFormatter.buildStatement(
                                findAccount(number)));
        }

        void printReport() {
                ReportGenerator.printReport(accounts);
        }

        void save() {
                try {
                        StatePersister.saveAccounts(accounts);
                } catch (IOException e) {

                        out.println("Could not save account backup: "
                                        + e.getMessage());
                }
        }

        Map<String, Account> getAccounts() {
                return accounts;
        }
}

public class MiniBank {
        public static void main(String[] args) {
                BankInfo info = new BankInfo("MiniBank", "Main Branch");
                out.println(info.bankName() + " - " + info.branch());

                Customer customer = new Customer(
                                "Yashvi",
                                new Customer.Address("Vadodara", "Gujarat"));

                out.println(customer);
                out.println("Cloned customer:");
                out.println(customer.clone());

                BankService bank = new BankService();
                Scanner sc = new Scanner(System.in);

                Account sample = bank.getAccounts().values().iterator().next();

                WithdrawRule anonymousRule = new WithdrawRule() {
                        @Override
                        public boolean allow(Account account, long amount) {
                                return amount > 0 && account.canWithdraw(amount);
                        }
                };

                WithdrawRule lambdaRule = (account, amount) -> amount > 0 && account.canWithdraw(amount);

                out.println("Anonymous rule: "
                                + anonymousRule.allow(sample, 100));

                out.println("Lambda rule: "
                                + lambdaRule.allow(sample, 100));

                out.println("Premium account: "
                                + (sample instanceof Premium));

                out.println("Mobile valid: "
                                + Validator.isMobile("9876543210"));

                out.println("Email valid: "
                                + Validator.isEmail("abc@gmail.com"));

                out.println("PAN valid: "
                                + Validator.isPAN("ABCDE1234F"));

                out.println("IFSC valid: "
                                + Validator.isIFSC("SBIN0001234"));

                out.println("Annotation validation:");
                AnnotationValidator.validate(sample);

                while (true) {
                        out.println("\n===== MINIBANK MENU =====");
                        out.println("1. Open Savings Account");
                        out.println("2. Deposit");
                        out.println("3. Withdraw");
                        out.println("4. Transfer");
                        out.println("5. List Accounts");
                        out.println("6. Print Statement");
                        out.println("7. Account Report");
                        out.println("8. Save Account Backup");
                        out.println("9. Read Saved Backup");
                        out.println("10. Exit");
                        out.print("Enter choice: ");

                        try {
                                int choice = Integer.parseInt(sc.nextLine().trim());

                                switch (choice) {
                                        case 1 -> {
                                                out.print("Owner name: ");
                                                String name = sc.nextLine();

                                                out.print("Initial balance: ");
                                                long balance = Long.parseLong(sc.nextLine());

                                                Account account = bank.openSavingsAccount(name, balance);

                                                out.println("Account opened successfully.");
                                                out.println("Account number: "
                                                                + account.getAccountnum());
                                        }
                                        case 2 -> {
                                                out.print("Account number: ");
                                                String number = sc.nextLine();

                                                out.print("Deposit amount: ");
                                                long amount = Long.parseLong(sc.nextLine());

                                                bank.deposit(number, amount);
                                                out.println("Deposit successful.");
                                                out.println("Balance: "
                                                                + bank.findAccount(number).getBalance());
                                        }
                                        case 3 -> {
                                                out.print("Account number: ");
                                                String number = sc.nextLine();

                                                out.print("Withdrawal amount: ");
                                                long amount = Long.parseLong(sc.nextLine());

                                                bank.withdraw(number, amount);
                                                out.println("Withdrawal successful.");
                                                out.println("Balance: "
                                                                + bank.findAccount(number).getBalance());
                                        }
                                        case 4 -> {
                                                out.print("From account: ");
                                                String from = sc.nextLine();

                                                out.print("To account: ");
                                                String to = sc.nextLine();

                                                out.print("Transfer amount: ");
                                                long amount = Long.parseLong(sc.nextLine());

                                                bank.transfer(from, to, amount);
                                                out.println("Transfer successful.");
                                        }
                                        case 5 -> bank.listAccounts();
                                        case 6 -> {
                                                out.print("Account number: ");
                                                bank.printStatement(sc.nextLine());
                                        }
                                        case 7 -> bank.printReport();
                                        case 8 -> {
                                                bank.save();
                                                out.println("Account backup saved.");
                                        }
                                        case 9 -> StatePersister.loadAccounts(
                                                        bank.getAccounts());
                                        case 10 -> {
                                                bank.save();
                                                out.println("Thank you for using MiniBank.");
                                                sc.close();
                                                return;
                                        }
                                        default -> out.println("Invalid choice.");
                                }
                        } catch (BankException e) {
                                out.println("Transaction error: " + e.getMessage());
                        } catch (NumberFormatException e) {
                                out.println("Please enter a valid whole number.");
                        } catch (IllegalArgumentException | ArithmeticException e) {
                                out.println("Invalid operation: " + e.getMessage());
                        } catch (IOException e) {
                                out.println("File error: " + e.getMessage());
                        } finally {
                                out.println("Operation completed.");
                        }
                }
        }
}
