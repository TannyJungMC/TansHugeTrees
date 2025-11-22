package tannyjung.tanshugetrees_handcode.systems;

import tannyjung.core.FileManager;
import tannyjung.core.OutsideUtils;
import tannyjung.tanshugetrees_handcode.Handcode;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.ShortBuffer;
import java.util.*;

public class Cache {

    private static final Map<String, String> dictionary = new HashMap<>();
    private static final Map<String, short[]> tree_shape_part1 = new HashMap<>();
    private static final Map<String, int[]> tree_shape_part2 = new HashMap<>();
    private static final Map<String, short[]> tree_shape_part3 = new HashMap<>();
    private static final Map<String, String[]> world_gen_settings = new HashMap<>();
    private static final Map<String, String[]> tree_settings = new HashMap<>();
    private static final Map<String, String[]> functions = new HashMap<>();
    private static String[] functions_tree_decoration = new String[0];
    private static String[] functions_tree_decoration_decay = new String[0];
    private static final Map<String, String[]> leaf_litter = new HashMap<>();

    public static double clear () {

        double size = 0;

        {

            size = size + OutsideUtils.cache.sizeMapText(dictionary);
            size = size + OutsideUtils.cache.sizeMapNumberShort(tree_shape_part1);
            size = size + OutsideUtils.cache.sizeMapNumberInt(tree_shape_part2);
            size = size + OutsideUtils.cache.sizeMapNumberShort(tree_shape_part3);
            size = size + OutsideUtils.cache.sizeMapTextList(world_gen_settings);
            size = size + OutsideUtils.cache.sizeMapTextList(tree_settings);
            size = size + OutsideUtils.cache.sizeMapTextList(functions);
            size = size + OutsideUtils.cache.sizeArrayText(functions_tree_decoration);
            size = size + OutsideUtils.cache.sizeArrayText(functions_tree_decoration_decay);
            size = size + OutsideUtils.cache.sizeMapTextList(leaf_litter);

            dictionary.clear();
            tree_shape_part1.clear();
            tree_shape_part2.clear();
            tree_shape_part3.clear();
            world_gen_settings.clear();
            tree_settings.clear();
            functions.clear();
            functions_tree_decoration = new String[0];
            functions_tree_decoration_decay = new String[0];
            leaf_litter.clear();

        }

        return Double.parseDouble(String.format("%.2f", size / (1024 * 1024)));

    }

    public static String getDictionary (String key, boolean id) {

        if (key.equals("") == false) {

            if (dictionary.containsKey(key) == false) {

                {

                    String path = Handcode.path_world_data + "/dictionary.txt";
                    String[] data = FileManager.readTXT(path);
                    String value_id = "";
                    String value_text = "";

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

                    if (value_id.equals("") == true && value_text.equals("") == true) {

                        if (id == false) {

                            value_text = key;

                        }

                        value_id = String.valueOf(data.length + 1);
                        FileManager.writeTXT(path, value_id + "|" + value_text + "\n", true);

                    }

                    dictionary.put(value_id, value_text);
                    dictionary.put(value_text, value_id);

                }

            }

        }

        return dictionary.getOrDefault(key, "");

    }

    private static void getTreeShape (String id) {

        if (tree_shape_part1.containsKey(id) == false) {

            String[] split = id.split("/");
            ByteBuffer buffer = FileManager.readBIN(Handcode.path_config + "/custom_packs/" + split[0] + "/presets/" + split[1] + "/storage/" + split[2]);

            if (buffer.remaining() > 0) {

                // Part 1
                {

                    int count = 6;
                    short[] data = new short[count];

                    for (int number = 0; number < count; number++) {

                        data[number] = buffer.getShort();

                    }

                    tree_shape_part1.put(id, data);

                }

                // Part 2
                {

                    int count = 6;
                    int[] data = new int[count];

                    for (int number = 0; number < count; number++) {

                        data[number] = buffer.getInt();

                    }

                    tree_shape_part2.put(id, data);

                }

                // Part 3
                {

                    ShortBuffer buffer_convert = buffer.asShortBuffer();
                    short[] data = new short[buffer_convert.remaining()];
                    buffer_convert.get(data);
                    tree_shape_part3.put(id, data);

                }

            }

        }

    }

    public static short[] getTreeShapePart1 (String id) {

        getTreeShape(id);
        return tree_shape_part1.getOrDefault(id, new short[0]);

    }

    public static int[] getTreeShapePart2 (String id) {

        getTreeShape(id);
        return tree_shape_part2.getOrDefault(id, new int[0]);

    }

    public static short[] getTreeShapePart3 (String id) {

        getTreeShape(id);
        return tree_shape_part3.getOrDefault(id, new short[0]);

    }

    public static String[] getWorldGenSettings (String id) {

        if (world_gen_settings.containsKey(id) == false) {

            world_gen_settings.put(id, FileManager.readTXT(Handcode.path_config + "/#dev/custom_packs_organized/world_gen/" + id + ".txt"));

        }

        return world_gen_settings.getOrDefault(id, new String[0]);

    }

    public static String[] getTreeSettings (String id) {

        if (tree_settings.containsKey(id) == false) {

            tree_settings.put(id, FileManager.readTXT(Handcode.path_config + "/#dev/custom_packs_organized/presets/" + id + "_settings.txt"));

        }

        return tree_settings.getOrDefault(id, new String[0]);

    }

    public static String[] getFunction (String id) {

        if (functions.containsKey(id) == false) {

            functions.put(id, FileManager.readTXT(Handcode.path_config + "/#dev/custom_packs_organized/functions/" + id + ".txt"));

        }

        return functions.getOrDefault(id, new String[0]);

    }

    public static String[] getFunctionTreeDecoration () {

        if (functions_tree_decoration.length == 0) {

            String[] list = new File(Handcode.path_config + "/#dev/custom_packs_organized/functions/#TannyJung-Main-Pack/tree_decoration").list();

            if (list != null) {

                String[] convert = new String[list.length];
                int loop = 0;

                while (loop < list.length) {

                    convert[loop] = list[loop].replace(".txt", "");
                    loop = loop + 1;

                }

                functions_tree_decoration = convert;

            }

        }

        return functions_tree_decoration;

    }

    public static String[] getFunctionTreeDecorationDecay () {

        if (functions_tree_decoration_decay.length == 0) {

            String[] list = new File(Handcode.path_config + "/#dev/custom_packs_organized/functions/#TannyJung-Main-Pack/tree_decoration_decay").list();

            if (list != null) {

                String[] convert = new String[list.length];
                int loop = 0;

                while (loop < list.length) {

                    convert[loop] = list[loop].replace(".txt", "");
                    loop = loop + 1;

                }

                functions_tree_decoration_decay = convert;

            }

        }

        return functions_tree_decoration_decay;

    }

    public static String[] getLeafLitter (String id) {

        if (leaf_litter.containsKey(id) == false) {

            leaf_litter.put(id, FileManager.readTXT(Handcode.path_config + "/#dev/custom_packs_organized/leaf_litter/" + id + ".txt"));

        }

        return leaf_litter.getOrDefault(id, new String[0]);

    }

}
