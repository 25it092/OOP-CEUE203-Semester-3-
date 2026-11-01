import java.util.*;

class VisitorTracker {
    public static void main(String[] args) {

        ArrayList<String> visitors = new ArrayList<>();

        visitors.add("Aarav");
        visitors.add("Diya");
        visitors.add("Kabir");
        visitors.add("Aarav");
        visitors.add("Meera");
        visitors.add("Diya");
        visitors.add("Rohan");

        HashSet<String> unique = new HashSet<>(visitors);

        LinkedHashSet<String> ordered = new LinkedHashSet<>(visitors);

        System.out.println("Unique Visitors Count: " + unique.size());

        System.out.println("First Three Unique Visitors:");

        int count = 0;

        for (String name : ordered) {
            if (count == 3) {
                break;
            }

            System.out.println(name);
            count++;
        }
    }
}
