import java.net.URI;
import java.net.http.*;
import java.util.Scanner;
public class Venus {
    HttpClient client = HttpClient.newHttpClient();
    String ask(String msg) throws Exception {
        String history = Database.lastMessages(10).replace("\\", "\\\\").replace("\"", "\\\"");
        Database.save(msg);
        String sys = "You are Venus, a desktop assistant created by Mathely. Answer in short and simple words. "
            + "When user wants an action or task, reply with ONLY ONE LINE in format: TOOL: tool_name | argument. "
            + "Tools: "
            + "open_app | app_name (opens Mac app) "
            + "close_app | app_name (closes Mac app) "
            + "open_url | url (opens website in browser) "
            + "create_file | path or path|content (creates file) "
            + "delete_file | path (deletes file) "
            + "read_file | path (reads file content) "
            + "say | text (speaks text out loud) "
            + "cmd | command (runs shell command) "
            + "Examples: "
            + "User: open brave -> TOOL: open_app | Brave "
            + "User: close brave -> TOOL: close_app | Brave "
            + "User: say hello -> TOOL: say | hello "
            + "User: say i am ok -> TOOL: say | I am ok "
            + "User: open youtube -> TOOL: open_url | https://youtube.com "
            + "User: open settings -> TOOL: open_app | System Settings "
            + "User: create file test.txt -> TOOL: create_file | Desktop/test.txt "
            + "For normal conversation, reply in 1-2 friendly sentences without any TOOL format. "
            + "Old chat: " + history;
        String json = "{\"model\":\"qwen2.5:1.5b\"," + "\"system\":\"" + sys + "\"," + "\"prompt\":"
            + "\"" + msg.replace("\\", "\\\\").replace("\"", "\\\"") + "\",\"stream\":false}";
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:11434/api/generate"))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(json))
            .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String body = response.body();
        String key = "\"response\":\"";
        int s = body.indexOf(key) + key.length();
        int e = body.indexOf("\",\"done\"", s);
        return body.substring(s, e).replace("\\n", "\n").trim();
    }
    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        Venus bot = new Venus();
        Database.init();
        while (true) {
            System.out.print("You: ");
            String msg = sc.nextLine();
            if (msg.equalsIgnoreCase("bye")) break;
            String reply = bot.ask(msg);
            String r = reply.toLowerCase();
            if (r.startsWith("tool") && reply.contains(":")) {
                String clean = reply.substring(reply.indexOf(":") + 1).trim();
                String[] parts = clean.split("\\|", 2);
                System.out.println("Venus: " + Tools.run(parts[0].trim(), parts.length > 1 ? parts[1].trim() : ""));
            } else {
                System.out.println("Venus: " + reply.replace("normal_chat |", "").trim());
            }
        }
    }
}
