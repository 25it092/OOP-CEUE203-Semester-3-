import java.util.Scanner;
public class TollBooth {
    record Vehicle(String number, String type) {}
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int totalToll = 0;
        int bikeCount = 0;
        int carCount = 0;
        int truckCount = 0;

        while (true) {
            System.out.println();
            System.out.print("Enter vehicle number (or 'done' to stop): ");
            String number = scanner.next();
            if (number.equalsIgnoreCase("done")) {
                break;
            }
            System.out.print("Enter vehicle type (bike, car, truck): ");
            String type = scanner.next().toLowerCase();
            Vehicle vehicle = new Vehicle(number, type);
            int toll = 0;
            switch (vehicle.type()) {
                case "bike":
                    toll = 20;
                    bikeCount++;
                    break;
                case "car":
                    toll = 50;
                    carCount++;
                    break;
                case "truck":
                    toll = 150;
                    truckCount++;
                    break;
                default:
                    System.out.println("Unknown vehicle type. Skipping...");
                    continue;
            }
            totalToll += toll;
        }
        String mostFrequent = "none";
        if (bikeCount > carCount && bikeCount > truckCount && bikeCount > 0) {
            mostFrequent = "bike";
        } else if (carCount > bikeCount && carCount > truckCount) {
            mostFrequent = "car";
        } else if (truckCount > 0 && truckCount > carCount && truckCount > bikeCount) {
            mostFrequent = "truck";
        }
        System.out.println("Total toll: " + totalToll);
        System.out.println("Most frequent: " + mostFrequent);
        scanner.close();
    }
}
