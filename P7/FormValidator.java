import java.lang.annotation.*;
import java.lang.reflect.*;
import java.util.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@interface NotBlank {
}

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@interface MaxLength {
    int value();
}

class SignupForm {
    @NotBlank
    String name;

    @NotBlank
    @MaxLength(20)
    String username;

    @NotBlank
    @MaxLength(30)
    String password;

    SignupForm(String name, String username, String password) {
        this.name = name;
        this.username = username;
        this.password = password;
    }
}

class Checker {
    static List<String> check(Object obj) {
        List<String> errors = new ArrayList<>();
        Field[] fields = obj.getClass().getDeclaredFields();

        for (Field f : fields) {
            f.setAccessible(true);

            try {
                Object val = f.get(obj);
                String str = val == null ? "" : val.toString();

                // checking blank values
                if (f.isAnnotationPresent(NotBlank.class)) {
                    if (str.trim().isEmpty())
                        errors.add(f.getName() + " cannot be blank");
                }

                // checking max length
                if (f.isAnnotationPresent(MaxLength.class)) {
                    MaxLength max = f.getAnnotation(MaxLength.class);

                    if (str.length() > max.value())
                        errors.add(f.getName() + " exceeds " + max.value() + " characters");
                }
            } catch (IllegalAccessException e) {
                errors.add("Cannot access " + f.getName());
            }
        }

        return errors;
    }
}

public class FormValidator {
    public static void main(String[] args) {
        SignupForm form = new SignupForm("", "yashvi123", "thispasswordiswaytoolong");

        List<String> errors = Checker.check(form);

        if (errors.isEmpty()) {
            System.out.println("Form is valid");
        } else {
            for (String err : errors)
                System.out.println(err);
        }
    }
}
