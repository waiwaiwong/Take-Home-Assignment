package viki;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONObject;


public class Viki {
	public static void main(String[] args) throws Exception {
		
		int countHDTrue = 0;
		int countHDFalse = 0;
		int pageNumber = 1;
		boolean more = true;
		
		do {
			try {
				//Calls Viki URL
				String viki = readUrl("http://api.viki.io/v4/videos.json?app=100250a&per_page=10&page=" + pageNumber);
				JSONObject obj = new JSONObject(viki);
				
				//Gets the "more" field value
				more = obj.getBoolean("more");

				//Set array of Json Objects from response
				JSONArray array = obj.getJSONArray("response");
				
				//Look for a key called flags
                     //And a key called hd within flags
				for (int i = 0; i < array.length(); i++) {

					JSONObject flag = new JSONObject(array.getJSONObject(i).getString("flags"));
					String[] keys = JSONObject.getNames(flag);
					
					for (String key : keys) {
						if (key.equals("hd")) {
							Object value = flag.get(key);
							// Determine if hd is true or false 
							if (value.toString().equalsIgnoreCase("true")) {
								countHDTrue++;
							} else {
								countHDFalse++;
							}
						}
					}

				}

				pageNumber++;


			} catch (Exception e) {
				e.printStackTrace();
			}
		} while (more);
		
		System.out.println("True Count:" + countHDTrue);
		System.out.println("False Count:" + countHDFalse);
		System.out.println("Page Count:" + pageNumber);
	}
	
    /**
      This method return the url as text.
      @param urlString
      @return url as text
      @throws Exception
     */
	
	private static String readUrl(String urlString) throws Exception {
		BufferedReader reader = null;
		try {
			URL url = new URL(urlString);
			reader = new BufferedReader(new InputStreamReader(url.openStream()));
			StringBuffer buffer = new StringBuffer();
			int read;
			char[] chars = new char[1024];
			while ((read = reader.read(chars)) != -1)
				buffer.append(chars, 0, read);

			return buffer.toString();
		} finally {
			if (reader != null)
				reader.close();
		}
	}
}
