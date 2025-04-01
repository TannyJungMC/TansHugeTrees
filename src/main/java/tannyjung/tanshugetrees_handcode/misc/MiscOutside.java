package tannyjung.tanshugetrees_handcode.misc;

import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

public class MiscOutside {

	public static boolean isConnectedToInternet () {

		boolean return_logic = true;

		{

			try {

				URL test = new URI("https://www.google.com/").toURL();
				HttpURLConnection connection = (HttpURLConnection) test.openConnection();
				connection.setRequestMethod("HEAD");
				connection.setConnectTimeout(3000);
				connection.setReadTimeout(3000);
				int responseCode = connection.getResponseCode();
				// return (200 <= responseCode && responseCode < 400);

			} catch (Exception e) {

				return_logic = false;

			}

		}

		return return_logic;

	}

}