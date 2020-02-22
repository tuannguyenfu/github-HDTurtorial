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

public class FF_Jobs {

	public static void addJobs() throws IOException, InterruptedException {
		List<String> jsonFF_Jobs = jsonFF_Jobs();
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

	public static List<String> jsonFF_Jobs() {
		List<String> ff_Jobs = new ArrayList<String>();
		int count = 0;
		try {
			Connection con = ConnectDB.conHD();
			Statement st = con.createStatement();
			String sql = "Select * from FF_Booking";
			ResultSet rs = st.executeQuery(sql);
			while (rs.next()) {
				System.err.println(count);
				String jobType = JobType(rs.getInt("job_type_id"));
				String customerCode = getCustomerCode(rs.getString("Booking_Value"));
				ff_Jobs.add("CustomerCode=" + customerCode + "&" + "Job_ID=" + rs.getString("FF_Booking_Id") + "&" + "JobNo=" + rs.getString("Booking_Value")
						+ "&" + "JobType=" + jobType + "&" + "MAWB=" + rs.getString("MAWB"));
				count = count + 1;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ff_Jobs;
	}

	public static String JobType(int job_Type_ID) {
		if (job_Type_ID == 1000000) {
			return "Air Import";
		} else if (job_Type_ID == 1000001) {
			return "Air Export";
		} else if (job_Type_ID == 1000002) {
			return "Sea Import";
		} else if (job_Type_ID == 1000003) {
			return "Sea Export";
		} else if (job_Type_ID == 1000004) {
			return "Domestic";
		} else if (job_Type_ID == 1000005) {
			return "Cross Border";
		} else
			return "";
	}

	public static String getCustomerCode(String bookingValue) {
		try {
			Connection con = ConnectDB.conHD();
			Statement st = con.createStatement();
			String sql = "select transfer_place from ff_bookingdetail where booking_value = '" + bookingValue + "'";
			ResultSet rs = st.executeQuery(sql);
			while (rs.next()) {
				return rs.getString("transfer_place");
			}
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
}
