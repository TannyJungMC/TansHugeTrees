package tannyjung.tanshugetrees_handcode.systems;

import tannyjung.core.FileManager;
import tannyjung.tanshugetrees.TanshugetreesMod;
import tannyjung.tanshugetrees_handcode.Handcode;

import java.nio.ShortBuffer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Cache {

    private static final Map<String, String> dictionary = new HashMap<>();
    private static final Map<String, short[]> tree_shape_part1 = new HashMap<>();
    private static final Map<String, short[]> tree_shape_part2 = new HashMap<>();
    private static final Map<String, String[]> world_gen_settings = new HashMap<>();
    private static final Map<String, String[]> tree_settings = new HashMap<>();
    private static final Map<String, String[]> functions = new HashMap<>();
    private static final Map<String, String[]> leaf_litter = new HashMap<>();

    public static void clear () {

        dictionary.clear();
        tree_shape_part1.clear();
        tree_shape_part2.clear();
        world_gen_settings.clear();
        tree_settings.clear();
        functions.clear();
        leaf_litter.clear();
        TanshugetreesMod.LOGGER.info("Cleared All Main Caches");

    }

    public static String dictionary (String key, boolean id) {

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

        return dictionary.getOrDefault(key, "");

    }

    public static short[] tree_shape (String id, int part) {

        if (tree_shape_part1.containsKey(id) == false) {

            String[] split = id.split("/");
            ShortBuffer buffer = FileManager.readBIN(Handcode.path_config + "/custom_packs/" + split[0] + "/presets/" + split[1] + "/storage/" + split[2]).asShortBuffer();
            short[] data = new short[buffer.remaining()];
            buffer.get(data);

            tree_shape_part1.put(id, Arrays.copyOfRange(data, 0, 12));
            tree_shape_part2.put(id, Arrays.copyOfRange(data, 12, data.length));

        }

        if (part == 1) {

           return tree_shape_part1.getOrDefault(id, new short[0]);

        } else if (part == 2) {

            return tree_shape_part2.getOrDefault(id, new short[0]);

        }

        return new short[0];

    }

    public static String[] world_gen_settings (String id) {

        if (world_gen_settings.containsKey(id) == false) {

            world_gen_settings.put(id, FileManager.readTXT(Handcode.path_config + "/#dev/custom_packs_organized/world_gen/" + id + ".txt"));

        }

        return world_gen_settings.getOrDefault(id, new String[0]);

    }

    public static String[] tree_settings (String id) {

        if (tree_settings.containsKey(id) == false) {

            tree_settings.put(id, FileManager.readTXT(Handcode.path_config + "/#dev/custom_packs_organized/presets/" + id + "_settings.txt"));

        }

        return tree_settings.getOrDefault(id, new String[0]);

    }

    public static String[] functions (String id) {

        if (functions.containsKey(id) == false) {

            functions.put(id, FileManager.readTXT(Handcode.path_config + "/#dev/custom_packs_organized/functions/" + id + ".txt"));

        }

        return functions.getOrDefault(id, new String[0]);

    }

    public static String[] leaf_litter (String id) {

        if (leaf_litter.containsKey(id) == false) {

            leaf_litter.put(id, FileManager.readTXT(Handcode.path_config + "/#dev/custom_packs_organized/leaf_litter/" + id + ".txt"));

        }

        return leaf_litter.getOrDefault(id, new String[0]);

    }

}
