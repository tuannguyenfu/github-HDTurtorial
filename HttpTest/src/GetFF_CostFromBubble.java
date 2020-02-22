import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;



public class GetFF_CostFromBubble {

	public static void main(String[] args) throws IOException{
		
		URL url = new URL("https://jptmobile.bubbleapps.io/version-test/api/1.1/obj/costtype/");
		HttpURLConnection getConnection = (HttpURLConnection) url.openConnection();
		getConnection.setRequestMethod("GET");
		StringBuilder result;
		
		try (BufferedReader in = new BufferedReader(new InputStreamReader(getConnection.getInputStream()))) {
			
			String line;
			result = new StringBuilder();
			
			while ((line = in.readLine()) != null) {
				
				result.append(line);
				result.append(System.lineSeparator());
				
			}
			
			System.out.println(result.toString());
			
		}	
		
	}
	
}
