package tannyjung.tanshugetrees_core.outside;

import tannyjung.tanshugetrees_core.Core;

import java.nio.ByteBuffer;
import java.util.*;

public class CacheManager {

    private static final Object lock = new Object();
    private static final Map<String, Map<String, String>> cache_text = new HashMap<>();
    private static final Map<String, Map<String, Map<String, String>>> cache_text_text = new HashMap<>();
    private static final Map<String, Map<String, List<String>>> cache_text_list = new HashMap<>();
    private static final Map<String, Map<String, short[]>> cache_number_short_array = new HashMap<>();
    private static final Map<String, Map<String, int[]>> cache_number_int_array = new HashMap<>();
    private static final Map<String, Map<String, Boolean>> cache_logic = new HashMap<>();

    public static String clear () {

        String convert = "";
        int size = 0;

        synchronized (lock) {

            {

                size = size + SizeCalculation.getMapText(cache_text);
                cache_text.clear();

                size = size + SizeCalculation.getMapTextText(cache_text_text);
                cache_text_text.clear();

                size = size + SizeCalculation.getMapTextList(cache_text_list);
                cache_text_list.clear();

                size = size + SizeCalculation.getMapNumberShortArray(cache_number_short_array);
                cache_number_short_array.clear();

                size = size + SizeCalculation.getMapNumberIntArray(cache_number_int_array);
                cache_number_int_array.clear();

                size = size + SizeCalculation.getMapLogic(cache_logic);
                cache_logic.clear();

            }

        }

        if (size < 1024) {

            convert = size + " B";

        } else if (size < 1048576) {

            convert = String.format(Locale.US, "%.2f", (double) size / 1024.0) + " KB";

        } else {

            convert = String.format(Locale.US, "%.2f", (double) size / 1048576.0) + " MB";

        }

        return convert;

    }

    public static class SizeCalculation {

        public static int getMapByteBuffer (Map<String, ByteBuffer> test) {

            int size = 0;

            for (Map.Entry<String, ByteBuffer> entry : test.entrySet()) {

                size = size + entry.getValue().capacity();

            }

            return size;

        }

        public static int getMapText (Map<String, Map<String, String>> test) {

            int size = 0;

            for (Map.Entry<String, Map<String, String>> entry1 : test.entrySet()) {

                for (Map.Entry<String, String> entry2 : entry1.getValue().entrySet()) {

                    size = size + (entry2.getValue().length() * Character.BYTES);

                }

            }

            return size;

        }

        public static int getMapTextText (Map<String, Map<String, Map<String, String>>> test) {

            int size = 0;

            for (Map.Entry<String, Map<String, Map<String, String>>> entry1 : test.entrySet()) {

                for (Map.Entry<String, Map<String, String>> entry2 : entry1.getValue().entrySet()) {

                    for (Map.Entry<String, String> entry3 : entry2.getValue().entrySet()) {

                        size = size + (entry3.getValue().length() * Character.BYTES);

                    }

                }

            }

            return size;

        }

        public static int getMapTextList (Map<String, Map<String, List<String>>> test) {

            int size = 0;

            for (Map.Entry<String, Map<String, List<String>>> entry1 : test.entrySet()) {

                for (Map.Entry<String, List<String>> entry2 : entry1.getValue().entrySet()) {

                    for (String read_all : entry2.getValue()) {

                        size = size + (read_all.length() * Character.BYTES);

                    }

                }

            }

            return size;

        }

        public static int getMapNumberShortArray (Map<String, Map<String, short[]>> test) {

            int size = 0;

            for (Map.Entry<String, Map<String, short[]>> entry1 : test.entrySet()) {

                for (Map.Entry<String, short[]> entry2 : entry1.getValue().entrySet()) {

                    size = size + (entry2.getValue().length * Short.BYTES);

                }

            }

            return size;

        }

        public static int getMapNumberIntArray (Map<String, Map<String, int[]>> test) {

            int size = 0;

            for (Map.Entry<String, Map<String, int[]>> entry1 : test.entrySet()) {

                for (Map.Entry<String, int[]> entry2 : entry1.getValue().entrySet()) {

                    size = size + (entry2.getValue().length * Integer.BYTES);

                }

            }

            return size;

        }

        public static int getMapLogic (Map<String, Map<String, Boolean>> test) {

            int size = 0;

            for (Map.Entry<String, Map<String, Boolean>> entry1 : test.entrySet()) {

                size = size + entry1.getValue().size();

            }

            return size;

        }

    }

    public static List<String> getFunction (String path) {

        synchronized (lock) {

            if (cache_text_list.containsKey("functions") == false) {

                cache_text_list.put("functions", new HashMap<>());

            }

            if (cache_text_list.get("functions").containsKey(path) == false) {

                List<String> data = FileManager.readTXT(Core.path_config + "/#dev/temporary/" + path + ".txt");
                cache_text_list.get("functions").put(path, data);

            }

        }

        return cache_text_list.get("functions").get(path);

    }

    public static String getDictionary (String key, boolean is_number) {

        synchronized (lock) {

            if (cache_text.containsKey("dictionary") == false) {

                cache_text.put("dictionary", new HashMap<>());

            }

            if (cache_text.get("dictionary").containsKey(key) == false) {

                String value_id = "";
                String value_text = "";

                // Get Data
                {

                    String path = Core.path_world_mod + "/dictionary.txt";
                    List<String> data = FileManager.readTXT(path);

                    for (String read_all : data) {

                        if (is_number == true) {

                            if (read_all.startsWith(key + "|") == true) {

                                value_id = key;
                                value_text = read_all.substring(read_all.indexOf("|") + 1);
                                break;

                            }

                        } else {

                            if (read_all.endsWith("|" + key) == true) {

                                value_id = read_all.substring(0, read_all.indexOf("|"));
                                value_text = key;
                                break;

                            }

                        }

                    }

                    if (value_id.isEmpty() == true && value_text.isEmpty() == true) {

                        if (is_number == false) {

                            value_text = key;

                        }

                        if (value_text.isEmpty() == false) {

                            value_id = String.valueOf(data.size() + 1);
                            FileManager.writeTXT(path, value_id + "|" + value_text + "\n", true);

                        }

                    }

                }

                cache_text.get("dictionary").put(value_id, value_text);
                cache_text.get("dictionary").put(value_text, value_id);

            }

        }

        return cache_text.get("dictionary").get(key);

    }

    public static class Data {

        public static boolean existLogic (String name, String key) {

            synchronized (lock) {

                return cache_logic.containsKey(name) == true && cache_logic.get(name).containsKey(key) == true;

            }

        }

        public static boolean getLogic (String name, String key) {

            synchronized (lock) {

                return cache_logic.get(name).get(key);

            }

        }

        public static void setLogic (String name, String key, boolean value) {

            synchronized (lock) {

                cache_logic.computeIfAbsent(name, test -> new HashMap<>()).put(key, value);

            }

        }

        public static boolean existTextList (String name, String key) {

            synchronized (lock) {

                return cache_text_list.containsKey(name) == true && cache_text_list.get(name).containsKey(key) == true;

            }

        }

        public static List<String> getTextList (String name, String key) {

            synchronized (lock) {

                return cache_text_list.get(name).get(key);

            }

        }

        public static void setTextList (String name, String key, List<String> value) {

            synchronized (lock) {

                cache_text_list.computeIfAbsent(name, test -> new HashMap<>()).put(key, value);

            }

        }

        public static boolean existTextText (String name) {

            synchronized (lock) {

                return cache_text_text.containsKey(name) == true;

            }

        }

        public static Map<String, Map<String, String>> getTextText (String name) {

            synchronized (lock) {

                return cache_text_text.get(name);

            }

        }

        public static void setTextText (String name, Map<String, Map<String, String>> value) {

            synchronized (lock) {

                cache_text_text.put(name, value);

            }

        }

        public static boolean existNumberShortArray (String name, String key) {

            synchronized (lock) {

                return cache_number_short_array.containsKey(name) == true && cache_number_short_array.get(name).containsKey(key) == true;

            }

        }

        public static short[] getNumberShortArray (String name, String key) {

            synchronized (lock) {

                return cache_number_short_array.get(name).get(key);

            }

        }

        public static void setNumberShortArray (String name, String key, short[] value) {

            synchronized (lock) {

                cache_number_short_array.computeIfAbsent(name, test -> new HashMap<>()).put(key, value);

            }

        }

        public static boolean existNumberIntArray (String name, String key) {

            synchronized (lock) {

                return cache_number_int_array.containsKey(name) == true && cache_number_int_array.get(name).containsKey(key) == true;

            }

        }

        public static int[] getNumberIntArray (String name, String key) {

            synchronized (lock) {

                return cache_number_int_array.get(name).get(key);

            }

        }

        public static void setNumberIntArray (String name, String key, int[] value) {

            synchronized (lock) {

                cache_number_int_array.computeIfAbsent(name, test -> new HashMap<>()).put(key, value);

            }

        }

    }

}
