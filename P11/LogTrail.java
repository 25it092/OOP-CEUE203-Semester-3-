
import java.nio.file.*;
import java.util.*;
import java.io.*;

public class LogTrail {

    public static void main(String[] args) throws IOException {

        // Creating text files
        Files.write(Path.of("log1.txt"), Arrays.asList(
            "System started",
            "ERROR: File not found",
            "Login successful"
        ));

        Files.write(Path.of("log2.txt"), Arrays.asList(
            "User logged in",
            "ERROR: Connection failed",
            "System stopped"
        ));

        Files.write(Path.of("log3.txt"), Arrays.asList(
            "Application opened",
            "Login successful"
        ));

        Path[] files = {
            Path.of("log1.txt"),
            Path.of("log2.txt"),
            Path.of("log3.txt")
        };

        int totalLines = 0;
        int errorCount = 0;

        // Reading each file
        for (Path file : files) {

            System.out.println("\nFile: " + file.getFileName());

            List<String> lines = Files.readAllLines(file);

            System.out.println("File Size: " + Files.size(file) + " bytes");

            for (String line : lines) {

                System.out.println(line);

                totalLines++;

                if (line.contains("ERROR")) {
                    errorCount++;
                }
            }
        }

        // Displaying final results
        System.out.println("\nTotal Lines: " + totalLines);
        System.out.println("ERROR Lines: " + errorCount);
    }
}
