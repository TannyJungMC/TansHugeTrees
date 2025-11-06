package tannyjung.tanshugetrees_handcode.systems;

import tannyjung.core.FileManager;
import tannyjung.tanshugetrees.TanshugetreesMod;
import tannyjung.tanshugetrees_handcode.Handcode;

import java.util.HashMap;
import java.util.Map;

public class Cache {

    private static final Map<String, short[]> tree_shape = new HashMap<>();
    private static final Map<String, String[]> world_gen_settings = new HashMap<>();
    private static final Map<String, String[]> tree_settings = new HashMap<>();
    private static final Map<String, String[]> functions = new HashMap<>();
    private static final Map<String, String[]> leaf_litter = new HashMap<>();
    private static final Map<String, String[]> tree_location = new HashMap<>();
    private static final Map<String, String[]> world_gen_place = new HashMap<>();

    public static void clear () {

        TanshugetreesMod.LOGGER.info("Cleared All Main Caches");
        tree_shape.clear();

    }

    public static short[] tree_shape (String id) {

        if (tree_shape.containsKey(id) == false) {

            String[] split = id.split("/");
            tree_shape.put(id, FileManager.readBIN(Handcode.path_config + "/custom_packs/" + split[0] + "/presets/" + split[1] + "/storage/" + split[2], 13, 0));

        }

        return tree_shape.getOrDefault(id, new short[0]);

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

    public static String[] world_gen_place (String id) {

        if (world_gen_place.containsKey(id) == false) {

            world_gen_place.put(id, FileManager.readTXT(Handcode.path_world_data + "/world_gen/place/" + id + ".txt"));

        }

        return world_gen_place.getOrDefault(id, new String[0]);

    }

}
