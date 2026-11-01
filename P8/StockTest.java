class OutOfStockException extends Exception {
    int shortfall;

    public OutOfStockException(int shortfall) {
        super("Not enough stock! Shortfall by " + shortfall + " units.");
        this.shortfall = shortfall;
    }
}

class InvalidQuantityException extends Exception {
    public InvalidQuantityException(String message) {
        super(message);
    }
}

class Warehouse {
    private int availableStock = 10;

    public void issue(String item, int qty) throws OutOfStockException, InvalidQuantityException {
        if (qty <= 0) {
            throw new InvalidQuantityException("Invalid quantity (" + qty + ")! Quantity must be greater than 0.");
        }

        if (qty > availableStock) {
            int shortfall = qty - availableStock;
            throw new OutOfStockException(shortfall);
        }

        availableStock -= qty;
        System.out.println("Successfully issued " + qty + " of " + item + ". Remaining stock: " + availableStock);
    }
}

class Request {
    String item;
    int qty;

    public Request(String item, int qty) {
        this.item = item;
        this.qty = qty;
    }
}

public class StockTest {
    public static void main(String[] args) {
        System.out.println("\n");
        Warehouse warehouse = new Warehouse();

        Request[] requests = {
            new Request("Laptops", 3),
            new Request("Laptops", -2),
            new Request("Laptops", 0),
            new Request("Laptops", 10),
            new Request("Laptops", 4)
        };

        for (int i = 0; i < requests.length; i++) {
            Request req = requests[i];
            System.out.println(" --- Processing Request " + (i + 1) + ": " + req.qty + " " + req.item + " ---");

            try {
                warehouse.issue(req.item, req.qty);
            } catch (InvalidQuantityException e) {
                System.out.println("Error: " + e.getMessage());
            } catch (OutOfStockException e) {
                System.out.println("Error: " + e.getMessage());
            }

            System.out.println("Request " + (i + 1) + " processed.\n");
        }
    }
}
