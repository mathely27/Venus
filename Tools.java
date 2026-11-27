import java.io.File;
import java.nio.file.Files;
public class Tools {
    public static String run(String name, String arg) {
        try {
            switch (name) {
                case "open_url":
                    return openUrl(arg);
                case "open_app":
                    return openApp(arg);
                case "close_app":
                    return closeApp(arg);
                case "create_file":
                    return createFile(arg);
                case "delete_file":
                    return deleteFile(arg);
                case "read_file":
                    return readFile(arg);
                case "say":
                    return sayText(arg);
                case "cmd":
                    return runCmd(arg);
                default:
                    return "Tool not found: " + name;
            }
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
    static String openUrl(String url) throws Exception {
        if (!url.startsWith("http")) {
            url = "https://" + url;
        }
        new ProcessBuilder("open", url).start();
        return "Opened " + url;
    }
    static String openApp(String app) throws Exception {
        Process p = new ProcessBuilder("open", "-a", app).start();
        if (p.waitFor() != 0) {
            new ProcessBuilder("open", "-a", app + " Browser").start();
        }
        return "Opened " + app;
    }
    static String closeApp(String app) throws Exception {
        new ProcessBuilder("pkill", "-f", "-i", app).start();
        return "Closed " + app;
    }
    static String createFile(String path) throws Exception {
        File file = new File(path);
        file.createNewFile();
        return "Created " + file.getName();
    }

    static String deleteFile(String path) throws Exception {
        File file = new File(path);
        if (file.delete()) {
            return "Deleted " + file.getName();
        }
        return "File not found";
    }

    static String readFile(String path) throws Exception {
        File file = new File(path);
        if (file.exists()) {
            return Files.readString(file.toPath());
        }
        return "File not found";
    }

    static String sayText(String text) throws Exception {
        new ProcessBuilder("say", text).start();
        return "Speaking: " + text;
    }
    static String runCmd(String cmd) throws Exception {
        Process p = new ProcessBuilder("zsh", "-c", cmd).start();
        return new String(p.getInputStream().readAllBytes()).trim();
    }
}
