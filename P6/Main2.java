@FunctionalInterface
interface Notifier {
    void send(String msg);
}

// Marker interface
interface Urgent {
}

class UrgentSMS implements Notifier, Urgent {
    public void send(String msg) {
        System.out.println("SMS: " + msg);
    }
}

public class Main2 {
    public static void main(String[] args) {
        Notifier email = msg ->
                System.out.println("Email: " + msg);

        Notifier sms = new UrgentSMS();
        Notifier[] arr = {email, sms};

        String msg = "Your class starts at 9 AM";

        // sending to all
        for (Notifier n : arr) {
            n.send(msg);
        }

        System.out.println("Urgent messages:");

        // sending urgent notifications twice
        for (Notifier n : arr) {
            if (n instanceof Urgent) {
                n.send(msg);
                n.send(msg);
            }
        }
    }
}
