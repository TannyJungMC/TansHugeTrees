package tannyjung.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class OutsideUtils {

	public static void exception (Exception from, Exception exception) {

		Logger logger = LogManager.getLogger("TannyJung");
		logger.error("----------------------------------------------------------------------------------------------------");

		StackTraceElement from_get = from.getStackTrace()[0];

        logger.error("Found an error reported from {} -> {} -> {}", from_get.getClassName(), from_get.getMethodName(), from_get.getLineNumber());
		logger.error(exception.getMessage());

		for (StackTraceElement get : exception.getStackTrace()) {

			logger.error(get);

		}

		logger.error("----------------------------------------------------------------------------------------------------");

	}

	public static boolean isConnectedToInternet () {

		try {

			URL url = new URL("https://sites.google.com/view/tannyjung");
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("HEAD");
			connection.setConnectTimeout(5000);
			connection.setReadTimeout(5000);
			connection.connect();
			return true;

		} catch (Exception exception) {

			OutsideUtils.exception(new Exception(), exception);
			return false;

		}

	}

    public static short[] shortListToArray (List<Short> list) {

        short[] return_number = new short[list.size()];

        for (int count = 0; count < list.size(); count++) {

            return_number[count] = list.get(count);

        }

        return return_number;

    }

}