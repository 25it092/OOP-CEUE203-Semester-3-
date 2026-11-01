import java.util.Scanner;
public class VendingMachine {
    enum Coin {
        ONE, TWO, FIVE, TEN
    }
    public static void main(String[] args) {
        int price = 15;
        int total = 0;
        Scanner scanner = new Scanner(System.in);
        while (total < price) {
            System.out.println();
            System.out.print("Enter coin (ONE, TWO, FIVE, TEN): ");
            String input = scanner.next().toUpperCase();

            Coin coin = Coin.valueOf(input);
            int value = 0;
            switch (coin) {
                case ONE:
                    value = 1;
                    break;
                case TWO:
                    value = 2;
                    break;
                case FIVE:
                    value = 5;
                    break;
                case TEN:
                    value = 10;
                    break;
            }
            total = total + value;
            System.out.println("Total so far: " + total);
        }
        int change = total - price;
        System.out.println("Paid. Change: " + change);

        scanner.close();
    }
}
