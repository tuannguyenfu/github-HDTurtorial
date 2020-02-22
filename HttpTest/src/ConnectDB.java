import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectDB {
	
	public static Connection conHD() throws SQLException {
		String url = "jdbc:postgresql://210.245.84.28/id41HD";
		String user = "adempiere";
		String pass = "hdsofterppassword@123$";
		Connection con = DriverManager.getConnection(url, user, pass);
		return con;
	}
	
}
