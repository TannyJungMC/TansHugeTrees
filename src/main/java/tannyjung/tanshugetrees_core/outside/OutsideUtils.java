package tannyjung.tanshugetrees_core.outside;

import tannyjung.tanshugetrees_core.Core;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OutsideUtils {

    public static void exception (Exception from, Exception exception, String details) {

        Core.logger.error("----------------------------------------------------------------------------------------------------");

        {

            StackTraceElement from_get = from.getStackTrace()[0];
            Core.logger.error("Found an error reported from {} -> {} -> {}", from_get.getClassName(), from_get.getMethodName(), from_get.getLineNumber());
            Core.logger.error(exception.getMessage());

            for (StackTraceElement get : exception.getStackTrace()) {

                if (get.toString().contains("tannyjung") == true) {

                    Core.logger.error(get);

                }

            }

            if (details.isEmpty() == false) {

                for (String get : details.split(" / ")) {

                    Core.logger.error(get);

                }

            }

        }

        Core.logger.error("----------------------------------------------------------------------------------------------------");

    }

    public static String testVersion (String version_main, String version) {

        String result = "";

        test:
        {

            if (Core.data_structure_version.equals(version) == true) {

                result = "same";

            } else {

                String[] main_split = version_main.split("\\.");
                String[] split = version.split("\\.");
                int main_split1 = 0;
                int main_split2 = 0;
                int main_split3 = 0;
                int split1 = 0;
                int split2 = 0;
                int split3 = 0;

                try {

                    main_split1 = Integer.parseInt(main_split[0]);
                    main_split2 = Integer.parseInt(main_split[1]);
                    main_split3 = Integer.parseInt(main_split[2]);
                    split1 = Integer.parseInt(split[0]);
                    split2 = Integer.parseInt(split[1]);
                    split3 = Integer.parseInt(split[2]);

                } catch (Exception ignored) {

                    result = "outdated";
                    break test;

                }

                if (main_split1 < split1 || main_split2 < split2 || main_split3 < split3) {

                    result = "outdated";

                } else {

                    result = "early";

                }

            }

        }

        System.out.println("+++ " + result + " <--- " + version_main + " / " + version);

        return result;

    }

    public static boolean isURLAvailable (String url) {

        try {

            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("HEAD");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            connection.connect();
            return true;

        } catch (Exception exception) {

            exception(new Exception(), exception, "");
            return false;

        }

    }

    public static boolean download (String url, String to) {

        if (OutsideUtils.isURLAvailable(url) == true) {

            FileManager.createEmptyFile(to, false);

            {

                try (FileOutputStream output = new FileOutputStream(to)) {

                    BufferedInputStream input = new BufferedInputStream(new URI(url).toURL().openStream());
                    byte[] buffer = new byte[1024];
                    int bytesRead;

                    while ((bytesRead = input.read(buffer, 0, 1024)) != -1) {

                        output.write(buffer, 0, bytesRead);

                    }

                } catch (Exception exception) {

                    OutsideUtils.exception(new Exception(), exception, "");

                    FileManager.delete(to);
                    return false;

                }

            }

        }

        return true;

    }

    public static int[] convertPosRotationMirrored (int rotation, boolean mirrored, int posX, int posZ) {

        int save_posX = posX;
        int save_posZ = posZ;

        if (mirrored == true) {

            posX = save_posX * (-1);

        }

        if (rotation == 2) {

            posX = save_posZ;
            posZ = save_posX * (-1);

        } else if (rotation == 3) {

            posX = save_posX * (-1);
            posZ = save_posZ * (-1);

        } else if (rotation == 4) {

            posX = save_posZ * (-1);
            posZ = save_posX;

        }

        return new int[]{posX, posZ};

    }

    public static int[] convertPosFallen (int fallen_direction, int posX, int posY, int posZ) {

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

        return new int[]{posX, posY, posZ};

    }

    public static int[] convertSizeRotationMirrored (int rotation, boolean mirrored, int sizeX, int sizeZ, int center_sizeX, int center_sizeZ) {

        int save_sizeX = sizeX;
        int save_sizeZ = sizeZ;
        int save_center_sizeX = center_sizeX;
        int save_center_sizeZ = center_sizeZ;

        if (mirrored == true) {

            center_sizeX = save_sizeX - save_center_sizeX;

        }

        if (rotation == 2) {

            sizeX = save_sizeZ;
            sizeZ = save_sizeX;
            center_sizeX = save_center_sizeZ;
            center_sizeZ = save_sizeX - save_center_sizeX;

        } else if (rotation == 3) {

            center_sizeX = save_sizeX - save_center_sizeX;
            center_sizeZ = save_sizeZ - save_center_sizeZ;

        } else if (rotation == 4) {

            sizeX = save_sizeZ;
            sizeZ = save_sizeX;
            center_sizeX = save_sizeZ - save_center_sizeZ;
            center_sizeZ = save_center_sizeX;

        }

        return new int[]{sizeX, sizeZ, center_sizeX, center_sizeZ};

    }

    public static int[] convertSizeFallen (int fallen_direction, int sizeX, int sizeY, int sizeZ, int center_sizeX, int center_sizeY, int center_sizeZ) {

        int save_sizeX = sizeX;
        int save_sizeY = sizeY;
        int save_sizeZ = sizeZ;
        int save_center_sizeX = center_sizeX;
        int save_center_sizeY = center_sizeY;
        int save_center_sizeZ = center_sizeZ;

        if (fallen_direction == 1) {

            sizeY = save_sizeX;
            sizeX = save_sizeY;
            center_sizeY = save_sizeX - save_center_sizeX;
            center_sizeX = save_center_sizeY;

        } else if (fallen_direction == 2) {

            sizeY = save_sizeZ;
            sizeZ = save_sizeY;
            center_sizeY = save_sizeZ - save_center_sizeZ;
            center_sizeZ = save_center_sizeY;

        } else if (fallen_direction == 3) {

            sizeY = save_sizeX;
            sizeX = save_sizeY;
            center_sizeY = save_center_sizeX;
            center_sizeX = save_sizeY - save_center_sizeY;

        } else if (fallen_direction == 4) {

            sizeY = save_sizeZ;
            sizeZ = save_sizeY;
            center_sizeY = save_center_sizeZ;
            center_sizeZ = save_sizeY - save_center_sizeY;

        }

        return new int[]{sizeX, sizeY, sizeZ, center_sizeX, center_sizeY, center_sizeZ};

    }

    public static String[] convertListToArray (List<String> list) {

        String[] array = new String[list.size()];

        for (int count = 0; count < list.size(); count++) {

            array[count] = list.get(count);

        }

        return array;

    }

    public static String getQuardtree (int level, int chunkX, int chunkZ) {

        StringBuilder return_text = new StringBuilder();

        {

            int localX = chunkX & 31;
            int localZ = chunkZ & 31;

            for (int step = 1; step <= level; step++) {

                int size = 32 >> step;
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

    public static String[] readOnlineTXT (String url) {

        List<String> data = new ArrayList<>();

        if (OutsideUtils.isURLAvailable(url) == true) {

            try {

                BufferedReader buffered_reader = new BufferedReader(new InputStreamReader(new URI(url).toURL().openStream()), 65536);
                String read_all = "";

                while ((read_all = buffered_reader.readLine()) != null) {

                    data.add(read_all);

                }

                buffered_reader.close();

            } catch (Exception exception) {

                OutsideUtils.exception(new Exception(), exception, "");

            }

        }

        return convertListToArray(data);

    }

    public static class cache {

        public static int sizeMapByteBuffer (Map<String, ByteBuffer> test) {

            int return_number = 0;

            for (Map.Entry<String, ByteBuffer> entry : test.entrySet()) {

                return_number = return_number + entry.getValue().capacity();

            }

            return return_number;

        }

        public static int sizeMapNumberShort (Map<String, Map<String, short[]>> test) {

            int return_number = 0;

            for (Map.Entry<String, Map<String, short[]>> entry1 : test.entrySet()) {

                for (Map.Entry<String, short[]> entry2 : entry1.getValue().entrySet()) {

                    return_number = return_number + entry2.getValue().length * Short.BYTES;

                }

            }

            return return_number;

        }

        public static int sizeMapNumberInt (Map<String, Map<String, int[]>> test) {

            int return_number = 0;

            for (Map.Entry<String, Map<String, int[]>> entry1 : test.entrySet()) {

                for (Map.Entry<String, int[]> entry2 : entry1.getValue().entrySet()) {

                    return_number = return_number + entry2.getValue().length * Integer.BYTES;

                }

            }

            return return_number;

        }

        public static int sizeMapText (Map<String, Map<String, String>> test) {

            int return_number = 0;

            for (Map.Entry<String, Map<String, String>> entry1 : test.entrySet()) {

                for (Map.Entry<String, String> entry2 : entry1.getValue().entrySet()) {

                    return_number = return_number + entry2.getValue().length() * Character.BYTES;

                }

            }

            return return_number;

        }

        public static int sizeMapTextList (Map<String, Map<String, String[]>> test) {

            int return_number = 0;

            for (Map.Entry<String, Map<String, String[]>> entry1 : test.entrySet()) {

                for (Map.Entry<String, String[]> entry2 : entry1.getValue().entrySet()) {

                    return_number = return_number + entry2.getValue().length * Integer.BYTES;

                }

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