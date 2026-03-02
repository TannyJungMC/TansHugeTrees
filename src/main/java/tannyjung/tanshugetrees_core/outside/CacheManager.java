package tannyjung.tanshugetrees_core.outside;

import tannyjung.tanshugetrees_core.Core;

import java.util.*;

public class CacheManager {

    public static final Object lock = new Object();
    public static final Map<String, Map<String, String>> cache_string = new HashMap<>();
    public static final Map<String, Map<String, String[]>> cache_string_list = new HashMap<>();
    public static final Map<String, Map<String, short[]>> cache_number_short_list = new HashMap<>();
    public static final Map<String, Map<String, int[]>> cache_number_int_list = new HashMap<>();

    public static double clear () {

        int size = 0;

        synchronized (lock) {

            {

                size = size + OutsideUtils.Cache.sizeMapText(cache_string);
                cache_string.clear();

                size = size + OutsideUtils.Cache.sizeMapTextList(cache_string_list);
                cache_string_list.clear();

                size = size + OutsideUtils.Cache.sizeMapNumberShort(cache_number_short_list);
                cache_number_short_list.clear();

                size = size + OutsideUtils.Cache.sizeMapNumberInt(cache_number_int_list);
                cache_number_int_list.clear();

            }

        }

        return Double.parseDouble(String.format(Locale.US, "%.2f", (double) size / (1024 * 1024)));

    }

    public static String getDictionary (String key, boolean is_number) {

        synchronized (lock) {

            if (cache_string.containsKey("dictionary") == false) {

                cache_string.put("dictionary", new HashMap<>());

            }

            if (cache_string.get("dictionary").containsKey(key) == false) {

                String value_id = "";
                String value_text = "";

                // Get Data
                {

                    String path = Core.path_world_mod + "/dictionary.txt";
                    String[] data = FileManager.readTXT(path);

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

                            value_id = String.valueOf(data.length + 1);
                            FileManager.writeTXT(path, value_id + "|" + value_text + "\n", true);

                        }

                    }

                }

                cache_string.get("dictionary").put(value_id, value_text);
                cache_string.get("dictionary").put(value_text, value_id);

            }

        }

        return cache_string.get("dictionary").get(key);

    }

    public static String[] getFunction (String id) {

        synchronized (lock) {

            if (cache_string_list.containsKey("functions") == false) {

                cache_string_list.put("functions", new HashMap<>());

            }

            if (cache_string_list.get("functions").containsKey(id) == false) {

                String[] data = FileManager.readTXT(Core.path_config + "/#dev/#temporary/" + id + ".txt");
                cache_string_list.get("functions").put(id, data);

            }

        }

        return cache_string_list.get("functions").get(id);

    }

}
