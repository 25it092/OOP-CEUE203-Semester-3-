import java.lang.annotation.*;
import java.lang.reflect.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@interface Run {
}

class MyTests {
    @Run
    public void testAdd() {
        System.out.println("Addition test passed");
    }

    @Run
    public void testSub() {
        System.out.println("Subtraction test passed");
    }

    public void normalMethod() {
        System.out.println("This method will not run");
    }

    @Run
    public void testMsg() {
        System.out.println("Message test passed");
    }
}

public class MiniTestRunner {
    public static void main(String[] args) {
        MyTests obj = new MyTests();
        int count = 0;

        // finding methods with @Run
        for (Method m : obj.getClass().getDeclaredMethods()) {
            if (m.isAnnotationPresent(Run.class) && m.getParameterCount() == 0) {
                try {
                    m.invoke(obj);
                    count++;
                } catch (Exception e) {
                    System.out.println("Test failed: " + m.getName());
                }
            }
        }

        System.out.println("Tests run: " + count);
    }
}
