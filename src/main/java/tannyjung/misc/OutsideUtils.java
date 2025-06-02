package tannyjung.misc;

import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

public class OutsideUtils {

	public static boolean isConnectedToInternet () {

		try {

			URL test = new URI("https://www.google.com/").toURL();
			HttpURLConnection connection = (HttpURLConnection) test.openConnection();
			connection.setRequestMethod("HEAD");
			connection.setConnectTimeout(3000);
			connection.setReadTimeout(3000);
			int responseCode = connection.getResponseCode();
			// return (200 <= responseCode && responseCode < 400);

		} catch (Exception e) {

			return false;

		}

		return true;

	}

}