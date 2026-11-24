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
            String SystemPrompot="You are Venus, create by Mathely .. you are an personal desktop ai assistant you asnwers in short and simple ";
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
            System.out.println("Venus: "+answ.replace("\\n", "\n"));


        }
            
        
    }
}
