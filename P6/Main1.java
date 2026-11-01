interface Switchable {
    void on();
    void off();

    default void toggle() {
        off();
        on();
    }
}

class Fan implements Switchable {
    public void on() {
        System.out.println("Fan is ON");
    }

    public void off() {
        System.out.println("Fan is OFF");
    }
}

class Light implements Switchable {
    public void on() {
        System.out.println("Light is ON");
    }

    public void off() {
        System.out.println("Light is OFF");
    }
}

interface SwitchRule {
    boolean canSwitchOn(Switchable dev, int hr);
}

public class Main1 {
    public static void main(String[] args) {
        Switchable[] arr = {new Fan(), new Light()};

        // toggling each device
        for (Switchable dev : arr) {
            dev.toggle();
        }

        // using anonymous class
        SwitchRule r1 = new SwitchRule() {
            public boolean canSwitchOn(Switchable dev, int hr) {
                return hr >= 6 && hr <= 22;
            }
        };

        // using lambda
        SwitchRule r2 = (dev, hr) -> hr >= 6 && hr <= 22;

        System.out.println("Anonymous class: "
                + r1.canSwitchOn(arr[0], 10));
        System.out.println("Lambda: "
                + r2.canSwitchOn(arr[1], 23));
    }
}
