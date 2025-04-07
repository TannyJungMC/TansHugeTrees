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

	public static String quardtreeChunkToNode (int chunkX, int chunkZ) {

		StringBuilder return_text = new StringBuilder();

		{

			for (int loop = 1; loop <= 3; loop++) {

				int posX = (chunkX / (loop * 2)) % 2;
				int posZ = (chunkZ / (loop * 2)) % 2;

				if (posX == 0 && posZ == 0) {

					return_text.append("-NW");

				} else if (posX == 1 && posZ == 0) {

					return_text.append("-NE");

				} else if (posX == 0 && posZ == 1) {

					return_text.append("-SW");

				} else {

					return_text.append("-SE");

				}

			}

		}

		return return_text.substring(1);

	}

}