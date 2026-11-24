import java.sql.*;
public class Database {
    static final String URL = "jdbc:sqlite:mars.db"; 
    static void init() {
        try (Connection c = DriverManager.getConnection(URL);
             Statement st = c.createStatement()) {
            st.execute("CREATE TABLE IF NOT EXISTS messages "
                     + "(id INTEGER PRIMARY KEY AUTOINCREMENT, text TEXT)");
        } catch (Exception e) {
            System.out.println("DB error: " + e.getMessage());
        }
    } 
    static void save(String text) {
        try (Connection c = DriverManager.getConnection(URL);
             PreparedStatement ps = c.prepareStatement("INSERT INTO messages(text) VALUES (?)")) {
            ps.setString(1, text);
            ps.executeUpdate();
        } catch (Exception e) {
            System.out.println("DB error: " + e.getMessage());
        }
    }
    static String lastMessages(int n) {
        StringBuilder sb = new StringBuilder();
        try (Connection c = DriverManager.getConnection(URL);
             PreparedStatement ps = c.prepareStatement(
                 "SELECT text FROM (SELECT id, text FROM messages ORDER BY id DESC LIMIT ?) ORDER BY id ASC")) {
            ps.setInt(1, n);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                sb.append("- ").append(rs.getString("text")).append(" ");
            }
        } catch (Exception e) {
            System.out.println("DB error: " + e.getMessage());
        }
        return sb.toString();
    }
}
