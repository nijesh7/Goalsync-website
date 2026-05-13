import com.smartgoal.util.DBConnection;
import java.sql.Connection;

public class TestDB {
    public static void main(String[] args) {
        System.out.println("Testing Oracle Database Connection...");
        try {
            Connection con = DBConnection.getConnection();
            if (con != null) {
                System.out.println("SUCCESS! Connected to Oracle Database.");
                try (java.sql.Statement stmt = con.createStatement();
                     java.sql.ResultSet rs = stmt.executeQuery("SELECT * FROM users")) {
                    while (rs.next()) {
                        System.out.println("User in DB: id=" + rs.getInt("id") + ", email='" + rs.getString("email") + "', password='" + rs.getString("password") + "'");
                    }
                }
                con.close();
            } else {
                System.out.println("FAILED! Connection is null.");
            }
        } catch (Exception e) {
            System.out.println("ERROR: Could not connect to Oracle.");
            e.printStackTrace();
        }
    }
}
