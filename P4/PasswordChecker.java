import java.util.Scanner;
public class PasswordChecker{
    //first rule check:
    public static boolean length(String password){
        if(password.length() >= 8){
            return true;
        }
        else{
            System.out.println("\n -Rule Not satisfied: Password Length.");
            return false;
        }
    }
    //second rule check
    public static boolean upper(String password){
        if(password.matches(".*[A-Z].*")){
            return true;
        }
        else{
            System.out.println(" -Rule Not satisfied: Atleast one Uppercase Letter.");
            return false;
        }
    }
    //third rule check
    public static boolean digits(String password){
        if(password.matches(".*[0-9].*")){
            return true;
        }
        else{
            System.out.println(" -Rule Not satisfied: Atleast one Digit.");
            return false;
        }
    }
    //fourth rule check
    public static boolean special(String password){
        if(password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*")){
            return true;
        }
        else{
            System.out.println(" -Rule Not satisfied: Atleast one Special Character.");
            return false;
        }
    }
    public static String strength(String password){
        int count = 0;
        if(length(password)){
            count++;
        }
        if(upper(password)){
            count++;
        }
        if(digits(password)){
            count++;
        }
        if(special(password)){
            count++;
        }

        if(count<=1){
            return "Weak!";
        }
        else if(count<=3){
            return "Medium!";
        }
        else{
            return "Strong!";
        }
    }
    

    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        //for p1
        System.out.print("\n Enter password 1 for checking: ");
        String pass1 = sc.next();
        System.out.println("\n ->Password strength for p1: " + PasswordChecker.strength(pass1));
        //for p2
        System.out.print("\n Enter password 2 for checking:  ");
        String pass2 = sc.next();
        System.out.println("\n ->Password strength for p2: " + PasswordChecker.strength(pass2));
        //for p3
        System.out.print("\n Enter password 3 for checking:  ");
        String pass3 = sc.next();
        System.out.println("\n ->Password strength for p3: " + PasswordChecker.strength(pass3));

        sc.close();
    }
}
