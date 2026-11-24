import java.net.URI;
import java.net.http.*;
import java.util.Scanner;

public class Venus {
    public static void main(String[] args)throws Exception {
        Scanner sc=new Scanner(System.in);
        HttpClient client=HttpClient.newHttpClient();
        while(true){
            System.out.print("You: ");
            String msg=sc.nextLine();
            if(msg.equalsIgnoreCase("bye")) break;
            // String SystemPrompot="You are Venus, create by Mathely .. you are an personal desktop ai assistant you asnwers in short and simple ";
            String SystemPrompot="You are Venus, created by Mathely. "
    + "You are a personal desktop AI assistant. Answer in short and simple words. "
    + "You can use this tool: open_url | url (opens a website in the browser). "
    + "If the user wants an action, reply with ONE line only like this: TOOL: tool_name | argument. "
    + "If no tool is needed, just answer normally in 1-2 short sentences. "
    + "Example: user says open youtube, you reply: TOOL: open_url | https://www.youtube.com";
            String json="{\"model\":\"qwen2.5:1.5b\","+"\"system\":\""+SystemPrompot+"\","+"\"prompt\":"
            + "\"" + msg.replace("\\","\\\\").replace("\"","\\\"")+"\",\"stream\":false}";
            // HttpRequest request=HttpRequest.newBuilder()
            //     .uri(URI.create())
            HttpRequest request=HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:11434/api/generate"))
                .header("Content-Type","application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
            HttpResponse<String>response=client.send(request,HttpResponse.BodyHandlers.ofString());
            String body=response.body();
            String key="\"response\":\"";
             int s=body.indexOf(key)+key.length();
            int e=body.indexOf("\",\"done\"",s);
            String answ=body.substring(s,e);
            String reply=answ.replace("\\n","\n").trim();
            if(reply.startsWith("TOOL")){
                String line=reply.substring(5).split("\n")[0];
                String[] parts=line.split("\\|",2);
                String name=parts[0].trim();
                String arg=parts.length>1 ? parts[1].trim() : "";
                System.out.println("Venus: "+Tools.run(name,arg));
            }else{System.out.println("Venus: "+reply);}


        }
            
        
    }
}
