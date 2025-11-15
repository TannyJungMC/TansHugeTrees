package tannyjung.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class Utils {

	public static class misc {

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

                misc.exception(new Exception(), exception);
                return false;

            }

        }

		public static int[] convertPosRotationMirrored (int posX, int posY, int posZ, int rotation, boolean mirrored, int fallen_direction) {

            {

                // General
                {

                    if (mirrored == true) {

                        posX = posX * (-1);

                    }

                    if (rotation == 2) {

                        int posX_save = posX;
                        posX = posZ;
                        posZ = posX_save * (-1);

                    } else if (rotation == 3) {

                        posX = posX * (-1);
                        posZ = posZ * (-1);

                    } else if (rotation == 4) {

                        int posX_save = posX;
                        int posZ_save = posZ;
                        posX = posZ_save * (-1);
                        posZ = posX_save;

                    }

                }

                // Fallen
                {

                    if (fallen_direction > 0) {

                        int posX_save = posX;
                        int posY_save = posY;
                        int posZ_save = posZ;

                        if (fallen_direction == 1) {

                            posY = posX_save;
                            posX = posY_save;

                        } else if (fallen_direction == 2) {

                            posY = posZ_save;
                            posZ = posY_save;

                        } else if (fallen_direction == 3) {

                            posY = posX_save;
                            posX = -posY_save;

                        } else if (fallen_direction == 4) {

                            posY = posZ_save;
                            posZ = -posY_save;

                        }

                    }

                }

            }

			return new int[]{posX, posY, posZ};

		}

        public static short[] shortListToArray (List<Short> list) {

            short[] return_number = new short[list.size()];

            for (int count = 0; count < list.size(); count++) {

                return_number[count] = list.get(count);

            }

            return return_number;

        }

        public static String quardtree (int chunkX, int chunkZ) {

            StringBuilder return_text = new StringBuilder();

            {

                int localX = chunkX & 31;
                int localZ = chunkZ & 31;

                for (int level = 1; level <= 2; level++) {

                    int size = 32 >> level;
                    int posX = (localX / size) % 2;
                    int posZ = (localZ / size) % 2;

                    if (posX == 0 && posZ == 0) return_text.append("-NW");
                    else if (posX == 1 && posZ == 0) return_text.append("-NE");
                    else if (posX == 0) return_text.append("-SW");
                    else return_text.append("-SE");

                }

            }

            return return_text.substring(1);

        }

	}

    public static class size {

        public static int sizeMapText (Map<String, String> test) {

            int return_number = 0;

            for (Map.Entry<String, String> entry : test.entrySet()) {

                return_number = return_number + entry.getValue().length() * Character.BYTES;

            }

            return return_number;

        }

        public static int sizeMapTextList(Map<String, String[]> test) {

            int return_number = 0;

            for (Map.Entry<String, String[]> entry : test.entrySet()) {

                return_number = return_number + entry.getValue().length * Integer.BYTES;

            }

            return return_number;

        }

        public static int sizeMapNumber (Map<String, short[]> test) {

            int return_number = 0;

            for (Map.Entry<String, short[]> entry : test.entrySet()) {

                return_number = return_number + entry.getValue().length * Short.BYTES;

            }

            return return_number;

        }

        public static int sizeArrayText (String[] test) {

            int return_number = 0;

            for (String get : test) {

                return_number = return_number + get.length() * Integer.BYTES;

            }

            return return_number;

        }

    }

}