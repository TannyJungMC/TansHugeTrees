package tannyjung.tanshugetrees_handcode.systems;

import tannyjung.tanshugetrees_core.FileManager;
import tannyjung.tanshugetrees_core.OutsideUtils;
import tannyjung.tanshugetrees_handcode.Handcode;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.ShortBuffer;
import java.util.*;

public class Cache {

    private static final Map<String, Map<String, String>> cache_string = new HashMap<>();
    private static final Map<String, Map<String, String[]>> cache_string_list = new HashMap<>();
    private static final Map<String, Map<String, short[]>> cache_number_short_list = new HashMap<>();
    private static final Map<String, Map<String, int[]>> cache_number_int_list = new HashMap<>();

    public static double clear() {

        int size = 0;

        {

            size = size + OutsideUtils.cache.sizeMapText(cache_string);
            cache_string.clear();

            size = size + OutsideUtils.cache.sizeMapTextList(cache_string_list);
            cache_string_list.clear();

            size = size + OutsideUtils.cache.sizeMapNumberShort(cache_number_short_list);
            cache_number_short_list.clear();

            size = size + OutsideUtils.cache.sizeMapNumberInt(cache_number_int_list);
            cache_number_int_list.clear();

        }

        return Double.parseDouble(String.format(Locale.US, "%.2f", (double) size / (1024 * 1024)));

    }

    public static String getDictionary(String key, boolean id) {

        if (cache_string.containsKey("dictionary") == false) {

            cache_string.put("dictionary", new HashMap<>());

        }

        if (cache_string.get("dictionary").containsKey(key) == false) {

            String value_id = "";
            String value_text = "";

            // Get Data
            {

                String path = Handcode.path_world_data + "/dictionary.txt";
                String[] data = FileManager.readTXT(path);

                for (String read_all : data) {

                    if (id == true) {

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

                    if (id == false) {

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

        return cache_string.get("dictionary").get(key);

    }

    private static void getTreeShape(String id) {

        if (cache_number_short_list.containsKey("tree_shape_part1") == false) {

            cache_number_short_list.put("tree_shape_part1", new HashMap<>());
            cache_number_int_list.put("tree_shape_part2", new HashMap<>());
            cache_number_short_list.put("tree_shape_part3", new HashMap<>());

        }

        if (cache_number_short_list.get("tree_shape_part1").containsKey(id) == false) {

            short[] data1 = new short[0];
            int[] data2 = new int[0];
            short[] data3 = new short[0];

            get_data:
            {

                String[] split = id.split("/");
                String path = "";

                try {

                    path = Handcode.path_config + "/#dev/temporary/presets/" + split[0] + "/" + split[1] + "/storage/" + split[2];

                } catch (Exception exception) {

                    OutsideUtils.exception(new Exception(), exception, "");
                    break get_data;

                }

                ByteBuffer buffer = FileManager.readBIN(path);

                if (buffer.remaining() > 0) {

                    // Part 1
                    {

                        int count = 6;
                        data1 = new short[count];

                        for (int number = 0; number < count; number++) {

                            data1[number] = buffer.getShort();

                        }

                    }

                    // Part 2
                    {

                        int count = 6;
                        data2 = new int[count];

                        for (int number = 0; number < count; number++) {

                            data2[number] = buffer.getInt();

                        }

                    }

                    // Part 3
                    {

                        ShortBuffer buffer_convert = buffer.asShortBuffer();
                        data3 = new short[buffer_convert.remaining()];
                        buffer_convert.get(data3);

                    }

                }

            }

            cache_number_short_list.get("tree_shape_part1").put(id, data1);
            cache_number_int_list.get("tree_shape_part2").put(id, data2);
            cache_number_short_list.get("tree_shape_part3").put(id, data3);

        }

    }

    public static short[] getTreeShapePart1(String id) {

        getTreeShape(id);
        return cache_number_short_list.get("tree_shape_part1").get(id);

    }

    public static int[] getTreeShapePart2(String id) {

        getTreeShape(id);
        return cache_number_int_list.get("tree_shape_part2").get(id);

    }

    public static short[] getTreeShapePart3(String id) {

        getTreeShape(id);
        return cache_number_short_list.get("tree_shape_part3").get(id);

    }

    public static String[] getWorldGenSettings(String id) {

        if (cache_string_list.containsKey("world_gen_settings") == false) {

            cache_string_list.put("world_gen_settings", new HashMap<>());

        }

        if (cache_string_list.get("world_gen_settings").containsKey(id) == false) {

            String[] data = FileManager.readTXT(Handcode.path_config + "/#dev/temporary/world_gen/" + id + ".txt");
            cache_string_list.get("world_gen_settings").put(id, data);

        }

        return cache_string_list.get("world_gen_settings").get(id);

    }

    public static String[] getTreeSettings(String id) {

        if (cache_string_list.containsKey("tree_settings") == false) {

            cache_string_list.put("tree_settings", new HashMap<>());

        }

        if (cache_string_list.get("tree_settings").containsKey(id) == false) {

            String[] data = FileManager.readTXT(Handcode.path_config + "/#dev/temporary/presets/" + id + "_settings.txt");
            cache_string_list.get("tree_settings").put(id, data);

        }

        return cache_string_list.get("tree_settings").get(id);

    }

    public static String[] getFunction(String id) {

        if (cache_string_list.containsKey("functions") == false) {

            cache_string_list.put("functions", new HashMap<>());

        }

        if (cache_string_list.get("functions").containsKey(id) == false) {

            String[] data = FileManager.readTXT(Handcode.path_config + "/#dev/temporary/" + id + ".txt");
            cache_string_list.get("functions").put(id, data);

        }

        return cache_string_list.get("functions").get(id);

    }

    public static String[] getTreeDecorationList(String id) {

        if (cache_string_list.containsKey("tree_decoration") == false) {

            cache_string_list.put("tree_decoration", new HashMap<>());

        }

        if (cache_string_list.get("tree_decoration").containsKey(id) == false) {

            String[] data = new String[0];

            // Get Data
            {

                File[] packs = new File(Handcode.path_config + "/#dev/temporary/tree_decoration").listFiles();

                if (packs != null) {

                    List<String> names = new ArrayList<>();
                    File[] files = new File[0];
                    String path_prefix = "";

                    for (File pack : packs) {

                        if (id.equals("decay") == true) {

                            path_prefix = pack.getName() + "/decay";

                        } else {

                            path_prefix = pack.getName();

                        }

                        files = new File(Handcode.path_config + "/#dev/temporary/tree_decoration/" + path_prefix).listFiles();

                        if (files != null) {

                            for (File file : files) {

                                if (file.isDirectory() == false) {

                                    names.add(path_prefix + "/" + file.getName().replace(".txt", ""));

                                }

                            }

                        }

                    }

                    data = OutsideUtils.convertListToArray(names);

                }

            }

            cache_string_list.get("tree_decoration").put(id, data);

        }

        return cache_string_list.get("tree_decoration").get(id);

    }

}
