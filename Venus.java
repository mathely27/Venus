import java.net.URI;
import java.net.http.*;
import java.util.Scanner;

public class Venus {
    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        HttpClient client = HttpClient.newHttpClient();
        Database.init();
        while (true) {
            System.out.print("You: ");
            String msg = sc.nextLine();
            if (msg.equalsIgnoreCase("bye")) break;
            String history = Database.lastMessages(10).replace("\\", "\\\\").replace("\"", "\\\"");
            Database.save(msg);
            String sys = "You are Venus, created by Mathely. You are a personal desktop AI assistant. Answer in short and simple words. "
                + "Tools: "
                + "open_url | url (opens website in browser) "
                + "open_app | app_name (opens macOS app like Calculator, Safari, Notes) "
                + "close_app | app_name (closes macOS app) "
                + "create_file | path or path|content (creates file at path) "
                + "delete_file | path (deletes file) "
                + "read_file | path (reads file content) "
                + "say | text (speaks text out loud) "
                + "cmd | command (runs terminal shell command) "
                + "If user wants an action, reply with ONE line only: TOOL: tool_name | argument. "
                + "If no tool needed, reply normally in 1-2 short sentences. "
                + "Chat memory: " + history;
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
            String answ = body.substring(s, e);
            String reply = answ.replace("\\n", "\n").trim();
            if (reply.startsWith("TOOL")) {
                String line = reply.substring(reply.indexOf("TOOL") + 4).replaceFirst("^:", "").trim().split("\n")[0];
                String[] parts = line.split("\\|", 2);
                String name = parts[0].trim();
                String arg = parts.length > 1 ? parts[1].trim() : "";
                System.out.println("Venus: " + Tools.run(name, arg));
            } else {
                System.out.println("Venus: " + reply);
            }
        }
    }
}
