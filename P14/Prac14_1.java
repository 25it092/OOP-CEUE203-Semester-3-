
import java.util.ArrayList;
import java.util.Scanner;

class Item {
    private int id;
    private String name;

    public Item(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String toString() {
        return "ID: " + id + ", Name: " + name;
    }
}

class ItemService {
    private ArrayList<Item> items = new ArrayList<>();

    public void addItem(Item item) {
        if (item == null || item.getName() == null
                || item.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid item!");
        }

        items.add(item);
    }

    public int getCount() {
        return items.size();
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public void performAction(int action) {
        if (action < 1 || action > 3) {
            throw new IllegalArgumentException("Invalid action!");
        }

        if (action == 1) {
            System.out.println("Add item selected.");
        } else if (action == 2) {
            System.out.println("View items selected.");
        } else {
            System.out.println("Exit selected.");
        }
    }
}

public class Prac14_1 {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        ItemService service = new ItemService();

        while (true) {
            System.out.println("\n===== ITEM MANAGEMENT =====");
            System.out.println("1. Add Item");
            System.out.println("2. View Items");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");

            int choice;

            try {
                choice = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
                continue;
            }

            try {
                service.performAction(choice);

                if (choice == 1) {
                    System.out.print("Enter item ID: ");
                    int id = Integer.parseInt(sc.nextLine());

                    System.out.print("Enter item name: ");
                    String name = sc.nextLine();

                    service.addItem(new Item(id, name));

                    System.out.println("Item added successfully!");

                } else if (choice == 2) {

                    if (service.getCount() == 0) {
                        System.out.println("No items found.");
                    } else {
                        for (Item item : service.getItems()) {
                            System.out.println(item);
                        }

                        System.out.println("Total items: "
                                + service.getCount());
                    }

                } else {
                    System.out.println("Goodbye!");
                    break;
                }

            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

        sc.close();
    }
}
