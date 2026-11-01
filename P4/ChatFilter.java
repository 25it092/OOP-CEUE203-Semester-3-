import java.util.Scanner;
public class ChatFilter {
    public static String filterLogs(String[] logs, String keyword){
        int count=0;
        StringBuilder report = new StringBuilder();
        String lowerKeyword = keyword.toLowerCase();

        for(String line : logs){
            String[] parts = line.split(" ", 3);
            if(parts.length < 3){
                continue;
            }
            String time = parts[0];
            String username = parts[1];
            String message = parts[2];
            if(message.toLowerCase().contains(lowerKeyword)){
                count++;
                report.append(time).append(" ").append(username).append(": ").append(message).append("\n");
            }
        }
        return ("Matches: " + count + "\n " + report.toString());
    }
    public static void main(String[] args){
        
        String[] Logs = {
            "10:05 Benjamin hey there wassup!",
            "10:06 Sherin good morning!",
            "10:07 idontknow",
            "10:08 Jonathan adios amigo"
        };
        Scanner sc = new Scanner(System.in);
        System.out.print("\n Enter search keyword: ");
        String keyword = sc.nextLine();

        String result = ChatFilter.filterLogs(Logs, keyword);
        System.out.println(result);

        sc.close();
    }
}
