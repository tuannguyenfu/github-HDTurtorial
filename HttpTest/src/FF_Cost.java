
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

public class FF_Cost {

	public static void addFFCost() throws IOException {
		List<String> jsonFF_Cost = jsonFF_Cost();
		for (String jsonBody : jsonFF_Cost) {
			URL obj = new URL("https://jptmobile.bubbleapps.io/version-test/api/1.1/obj/costtype");
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
		}

	}

	public static List<String> jsonFF_Cost() {
		List<String> ff_Cost = new ArrayList<>();
		try {
			Connection con = ConnectDB.conHD();
			String sql = "select * from FF_CostType";
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(sql);
			while (rs.next()) {
				if (rs.getString("isactive").equalsIgnoreCase("Y")) {
					ff_Cost.add("CostType_Name=" + rs.getString("value") + "_" + rs.getString("name") + "&CostType_ID="
							+ rs.getString("ff_costtype_id"));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ff_Cost;
	}

}
