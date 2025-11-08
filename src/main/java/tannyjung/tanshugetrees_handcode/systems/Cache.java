package tannyjung.tanshugetrees_handcode.systems;

import tannyjung.core.FileManager;
import tannyjung.tanshugetrees.TanshugetreesMod;
import tannyjung.tanshugetrees_handcode.Handcode;

import java.nio.ShortBuffer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Cache {

    private static final Map<String, short[]> tree_shape_part1 = new HashMap<>();
    private static final Map<String, short[]> tree_shape_part2 = new HashMap<>();
    private static final Map<String, String[]> world_gen_settings = new HashMap<>();
    private static final Map<String, String[]> tree_settings = new HashMap<>();
    private static final Map<String, String[]> functions = new HashMap<>();
    private static final Map<String, String[]> leaf_litter = new HashMap<>();
    private static final Map<String, String[]> tree_location = new HashMap<>();

    public static void clear () {

        TanshugetreesMod.LOGGER.info("Cleared All Main Caches");
        tree_shape_part1.clear();
        tree_shape_part2.clear();
        world_gen_settings.clear();
        tree_settings.clear();
        functions.clear();
        leaf_litter.clear();
        tree_location.clear();

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

    public static String[] tree_location (String id) {

        if (tree_location.containsKey(id) == false) {

            tree_location.put(id, FileManager.readTXT(Handcode.path_world_data + "/world_gen/tree_locations/" + id + ".txt"));

        }

        return tree_location.getOrDefault(id, new String[0]);

    }

    public static short idTextNumber (String text) {

        short return_number = 0;

        {



        }

        return return_number;

    }

}
