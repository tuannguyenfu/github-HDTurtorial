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
	public static List<String> jsonFF_Jobs() throws SQLException {
		List<String> ff_Jobs = new ArrayList<String>();
		Statement st = DB.createStatement();
		String sql = "Select * from FF_Booking";
		ResultSet rs = st.executeQuery(sql);
		while (rs.next()) {
			String jobType = JobType(rs.getInt("job_type_id"));
			String customerCode = getCustomerCode(rs.getInt("ff_booking_id"));
			String MAWB = getMAWB(rs.getString("MAWB"));
			ff_Jobs.add("CustomerCode=" + customerCode + "&" + "Job_ID=" + rs.getString("FF_Booking_Id") + "&"
					+ "JobNo=" + rs.getString("Booking_Value") + "&" + "JobType=" + jobType + "&" + "MAWB="
					+ rs.getString("MAWB"));
		}
		return ff_Jobs;
	}

	public static String getMAWB(String MAWB) {
		if (MAWB.equalsIgnoreCase("null")) {
			return "";
		} else
			return MAWB;
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

	public static String getCustomerCode(int ff_Booking_ID) throws SQLException {
		Map<Integer, String> mapCustomers = mapCustomer();
		Statement st = DB.createStatement();
		String sql = "select Customer_Code_ID from ff_bookingdetail where ff_booking_id = " + ff_Booking_ID;
		try {
			ResultSet rs = st.executeQuery(sql);
			while (rs.next()) {
				if (!rs.getString("Customer_Code_ID").equalsIgnoreCase("null")) {
					return mapCustomers.get(rs.getInt("Customer_Code_ID"));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "";
	}

	public static Map<Integer, String> mapCustomer() throws SQLException {
		List<Integer> customer_Codes = getDistinctCustomerCode();
		Map<Integer, String> mapCustomer = new HashMap<Integer, String>();
		for (Integer customer_Code : customer_Codes) {
			Statement st = DB.createStatement();
			String sql = "Select * from C_Bpartner where C_Bpartner_ID = " + customer_Code;
			ResultSet rs = st.executeQuery(sql);
			while (rs.next()) {
				mapCustomer.put(rs.getInt("C_Bpartner_ID"), rs.getString("name"));
			}
		}
		return mapCustomer;
	}

	public static List<Integer> getDistinctCustomerCode() {
		List<Integer> customer_Codes = new ArrayList<>();
		try {
			Statement st = DB.createStatement();
			String sql = "Select DISTINCT Customer_Code_ID from ff_bookingdetail";
			ResultSet rs = st.executeQuery(sql);
			while (rs.next()) {
				customer_Codes.add(rs.getInt("Customer_Code_ID"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return customer_Codes;
	}
}
