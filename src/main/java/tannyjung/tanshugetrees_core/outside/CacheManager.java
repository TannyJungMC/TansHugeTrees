package tannyjung.tanshugetrees_core.outside;

import tannyjung.tanshugetrees_core.Core;

import java.nio.ByteBuffer;
import java.util.*;

public class CacheManager {

    private static final Object lock = new Object();
    private static final Map<String, Map<String, String>> cache_map_text_text = new HashMap<>();
    private static final Map<String, Map<String, Map<String, String>>> cache_map_text_map_text_text = new HashMap<>();
    private static final Map<String, Map<String, List<String>>> cache_map_text_list_text = new HashMap<>();
    private static final Map<String, Map<String, short[]>> cache_map_text_array_number_short = new HashMap<>();
    private static final Map<String, Map<String, int[]>> cache_map_text_array_number_int = new HashMap<>();
    private static final Map<String, Map<String, Boolean>> cache_map_text_logic = new HashMap<>();

    public static String clear () {

        String convert = "";
        int size = 0;

        synchronized (lock) {

            // Map Text Text
            {

                for (Map.Entry<String, Map<String, String>> entry1 : cache_map_text_text.entrySet()) {

                    for (Map.Entry<String, String> entry2 : entry1.getValue().entrySet()) {

                        size = size + (entry2.getValue().length() * Character.BYTES);

                    }

                }

                cache_map_text_text.clear();

            }

            // Map Text Map Text Text
            {

                for (Map.Entry<String, Map<String, Map<String, String>>> entry1 : cache_map_text_map_text_text.entrySet()) {

                    for (Map.Entry<String, Map<String, String>> entry2 : entry1.getValue().entrySet()) {

                        for (Map.Entry<String, String> entry3 : entry2.getValue().entrySet()) {

                            size = size + (entry3.getValue().length() * Character.BYTES);

                        }

                    }

                }

                cache_map_text_map_text_text.clear();

            }

            // Map Text List Test
            {

                for (Map.Entry<String, Map<String, List<String>>> entry1 : cache_map_text_list_text.entrySet()) {

                    for (Map.Entry<String, List<String>> entry2 : entry1.getValue().entrySet()) {

                        for (String read_all : entry2.getValue()) {

                            size = size + (read_all.length() * Character.BYTES);

                        }

                    }

                }

                cache_map_text_list_text.clear();

            }

            // Map Text Array Number (Short)
            {

                for (Map.Entry<String, Map<String, short[]>> entry1 : cache_map_text_array_number_short.entrySet()) {

                    for (Map.Entry<String, short[]> entry2 : entry1.getValue().entrySet()) {

                        size = size + (entry2.getValue().length * Short.BYTES);

                    }

                }

                cache_map_text_array_number_short.clear();

            }

            // Map Text Array Number (Int)
            {

                for (Map.Entry<String, Map<String, int[]>> entry1 : cache_map_text_array_number_int.entrySet()) {

                    for (Map.Entry<String, int[]> entry2 : entry1.getValue().entrySet()) {

                        size = size + (entry2.getValue().length * Integer.BYTES);

                    }

                }

                cache_map_text_array_number_int.clear();

            }

            // Map Text Logic
            {

                for (Map.Entry<String, Map<String, Boolean>> entry1 : cache_map_text_logic.entrySet()) {

                    size = size + entry1.getValue().size();

                }

                cache_map_text_logic.clear();

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

    public static class Data {

        public static boolean existMapTextListText (String name, String key) {

            synchronized (lock) {

                return cache_map_text_list_text.containsKey(name) == true && cache_map_text_list_text.get(name).containsKey(key) == true;

            }

        }

        public static Map<String, List<String>> getMapTextListText (String name) {

            synchronized (lock) {

                return cache_map_text_list_text.get(name);

            }

        }

        public static void setMapTextListText (String name, String key, List<String> value) {

            synchronized (lock) {

                cache_map_text_list_text.computeIfAbsent(name, test -> new HashMap<>()).put(key, value);

            }

        }

        public static boolean existMapTextText (String name, String key) {

            synchronized (lock) {

                return cache_map_text_text.containsKey(name) == true && cache_map_text_text.get(name).containsKey(key) == true;

            }

        }

        public static Map<String, String> getMapTextText (String name) {

            synchronized (lock) {

                return cache_map_text_text.get(name);

            }

        }

        public static void setMapTextText (String name, String key, String value) {

            synchronized (lock) {

                cache_map_text_text.computeIfAbsent(name, test -> new HashMap<>()).put(key, value);

            }

        }

        public static boolean existMapTextMapTextText (String name, String key) {

            synchronized (lock) {

                return cache_map_text_map_text_text.containsKey(name) == true && cache_map_text_map_text_text.get(name).containsKey(key) == true;

            }

        }

        public static Map<String, Map<String, String>> getMapTextMapTextText (String name) {

            synchronized (lock) {

                return cache_map_text_map_text_text.get(name);

            }

        }

        public static void setMapTextMapTextText (String name, String key, Map<String, String> value) {

            synchronized (lock) {

                cache_map_text_map_text_text.computeIfAbsent(name, test -> new HashMap<>()).put(key, value);

            }

        }

        public static boolean existMapStringArrayNumberShort (String name, String key) {

            synchronized (lock) {

                return cache_map_text_array_number_short.containsKey(name) == true && cache_map_text_array_number_short.get(name).containsKey(key) == true;

            }

        }

        public static Map<String, short[]> getMapStringArrayNumberShort (String name) {

            synchronized (lock) {

                return cache_map_text_array_number_short.get(name);

            }

        }

        public static void setMapStringArrayNumberShort (String name, String key, short[] value) {

            synchronized (lock) {

                cache_map_text_array_number_short.computeIfAbsent(name, test -> new HashMap<>()).put(key, value);

            }

        }

        public static boolean existMapStringArrayNumberInt (String name, String key) {

            synchronized (lock) {

                return cache_map_text_array_number_int.containsKey(name) == true && cache_map_text_array_number_int.get(name).containsKey(key) == true;

            }

        }

        public static Map<String, int[]> getMapStringArrayNumberInt (String name) {

            synchronized (lock) {

                return cache_map_text_array_number_int.get(name);

            }

        }

        public static void setMapStringArrayNumberInt (String name, String key, int[] value) {

            synchronized (lock) {

                cache_map_text_array_number_int.computeIfAbsent(name, test -> new HashMap<>()).put(key, value);

            }

        }

        public static boolean existMapTextLogic (String name, String key) {

            synchronized (lock) {

                return cache_map_text_logic.containsKey(name) == true && cache_map_text_logic.get(name).containsKey(key) == true;

            }

        }

        public static Map<String, Boolean> getMapTextLogic (String name) {

            synchronized (lock) {

                return cache_map_text_logic.get(name);

            }

        }

        public static void setMapTextLogic (String name, String key, boolean value) {

            synchronized (lock) {

                cache_map_text_logic.computeIfAbsent(name, test -> new HashMap<>()).put(key, value);

            }

        }

    }

    public static List<String> getFunction (String path) {

        synchronized (lock) {

            if (cache_map_text_list_text.containsKey("functions") == false) {

                cache_map_text_list_text.put("functions", new HashMap<>());

            }

            if (cache_map_text_list_text.get("functions").containsKey(path) == false) {

                List<String> data = FileManager.readTXT(Core.path_config + "/dev/temporary/" + path + ".txt");
                cache_map_text_list_text.get("functions").put(path, data);

            }

        }

        return cache_map_text_list_text.get("functions").get(path);

    }

    public static String getDictionary (String key, boolean is_number) {

        synchronized (lock) {

            if (cache_map_text_text.containsKey("dictionary") == false) {

                cache_map_text_text.put("dictionary", new HashMap<>());

            }

            if (cache_map_text_text.get("dictionary").containsKey(key) == false) {

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

                cache_map_text_text.get("dictionary").put(value_id, value_text);
                cache_map_text_text.get("dictionary").put(value_text, value_id);

            }

        }

        return cache_map_text_text.get("dictionary").get(key);

    }

}
