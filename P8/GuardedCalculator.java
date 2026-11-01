import java.util.Scanner;

class DivideByZeroException extends Exception {
    public DivideByZeroException(String message) {
        super(message);
    }
}

public class GuardedCalculator {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean success = false;
        int attempt = 1;

        while (!success) {
            System.out.println("--- Attempt " + attempt + " ---");
            try {
                System.out.print("Enter first number: ");
                double num1 = Double.parseDouble(scanner.nextLine());

                System.out.print("Enter operator (+, -, *, /): ");
                String op = scanner.nextLine();

                System.out.print("Enter second number: ");
                double num2 = Double.parseDouble(scanner.nextLine());

                if (op.equals("/") && num2 == 0) {
                    throw new DivideByZeroException("Cannot divide by zero!");
                }

                double result = 0;
                if (op.equals("+")) {
                    result = num1 + num2;
                } else if (op.equals("-")) {
                    result = num1 - num2;
                } else if (op.equals("*")) {
                    result = num1 * num2;
                } else if (op.equals("/")) {
                    result = num1 / num2;
                } else {
                    System.out.println("Invalid operator!");
                    continue;
                }

                System.out.println("Result: " + result);
                success = true;

            } catch (NumberFormatException e) {
                System.out.println("Error: Invalid number input! Please enter valid numeric values.");
            } catch (DivideByZeroException e) {
                System.out.println("Error: " + e.getMessage());
            } finally {
                System.out.println("Logged attempt " + attempt + " completed.\n");
                attempt++;
            }
        }

        scanner.close();
    }
}
