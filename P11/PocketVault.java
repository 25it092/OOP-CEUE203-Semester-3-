
import java.io.*;

class StudentRecord implements Serializable {

    int id;
    String name;
    transient String password;

    StudentRecord(int id, String name, String password) {
        this.id = id;
        this.name = name;
        this.password = password;
    }

    void display() {
        System.out.println("ID: " + id);
        System.out.println("Name: " + name);
        System.out.println("Password: " + password);
        System.out.println();
    }
}

public class PocketVault {

    public static void main(String[] args) {

        StudentRecord[] students = {
            new StudentRecord(101, "Khushi", "abc123"),
            new StudentRecord(102, "Pal", "xyz456")
        };

        // Saving objects into file
        try {
            ObjectOutputStream out =
                new ObjectOutputStream(
                    new FileOutputStream("students.dat"));

            out.writeObject(students);
            out.close();

            System.out.println("Objects saved successfully!");

        } catch (IOException e) {
            System.out.println(e);
        }

        // Loading objects from file
        try {
            ObjectInputStream in =
                new ObjectInputStream(
                    new FileInputStream("students.dat"));

            StudentRecord[] loadedStudents =
                (StudentRecord[]) in.readObject();

            in.close();

            System.out.println("\nLoaded Student Records:");

            for (StudentRecord s : loadedStudents) {
                s.display();
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
