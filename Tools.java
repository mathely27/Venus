import java.io.File;
import java.nio.file.Files;

public class Tools {
    public static String run(String name, String arg) {
        try {
            switch (name) {
                case "open_url":
                    if (!arg.startsWith("http")) arg = "https://" + arg;
                    new ProcessBuilder("open", arg).start();
                    return "Opened " + arg;
                case "open_app":
                    new ProcessBuilder("open", "-a", arg).start();
                    return "Opened " + arg;
                case "close_app":
                    new ProcessBuilder("osascript", "-e", "tell application \"" + arg + "\" to quit").start();
                    return "Closed " + arg;
                case "create_file":
                    String[] fp = arg.split("\\|", 2);
                    File cf = new File(fp[0].trim().replace("~", System.getProperty("user.home")));
                    if (cf.getParentFile() != null) cf.getParentFile().mkdirs();
                    if (fp.length > 1) Files.writeString(cf.toPath(), fp[1].trim());
                    else cf.createNewFile();
                    return "Created " + cf.getPath();
                case "delete_file":
                    File df = new File(arg.trim().replace("~", System.getProperty("user.home")));
                    return df.delete() ? "Deleted " + df.getPath() : "File not found";
                case "read_file":
                    File rf = new File(arg.trim().replace("~", System.getProperty("user.home")));
                    return Files.readString(rf.toPath());
                case "say":
                    new ProcessBuilder("say", arg).start();
                    return "Said: " + arg;
                case "cmd":
                    Process p = new ProcessBuilder("zsh", "-c", arg).start();
                    return new String(p.getInputStream().readAllBytes()).trim();
                default:
                    return "i dont have a tool";
            }
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}
