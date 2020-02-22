import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class FF_Conts {

	public static void addConts() throws IOException, InterruptedException {
		List<String> jsonFF_Jobs = jsonFF_Conts();
		for (String jsonBody : jsonFF_Jobs) {
			URL obj = new URL("https://jptmobile.bubbleapps.io/version-test/api/1.1/obj/job");
			HttpURLConnection postConnection = (HttpURLConnection) obj.openConnection();
			postConnection.setRequestMethod("POST");
			postConnection.setDoOutput(true);

			byte[] out = jsonBody.toString().getBytes(StandardCharsets.UTF_8);
			int length = out.length;
			System.out.println(jsonBody);

			postConnection.setFixedLengthStreamingMode(length);
			postConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
			postConnection.connect();
			try (OutputStream os = postConnection.getOutputStream()) {
				os.write(out);
			}
			Thread.sleep(200);
		}
	}

	public static List<String> jsonFF_Conts() {
		List<String> FF_Cont = new ArrayList<String>();
		try {
			Connection con = ConnectDB.conHD();
			Statement st = con.createStatement();
			String sql = "Select * from FF_Bookingdetail";
			ResultSet rs = st.executeQuery(sql);
			while (rs.next()) {
				FF_Cont.add("ContNo=" + rs.getString("Cont_Serial") + "&" + "InvoiceNo=" + rs.getString("Commerical")
						+ "&" + "Job_ID=" + rs.getString("FF_Booking_ID") + "&" + "JobNo="
						+ rs.getString("Booking_Value"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return FF_Cont;
	}

}
