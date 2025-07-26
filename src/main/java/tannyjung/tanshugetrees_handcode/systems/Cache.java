package tannyjung.tanshugetrees_handcode.systems;

import tannyjung.core.FileManager;
import tannyjung.tanshugetrees_handcode.Handcode;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class Cache {

    private static final Map<String, String[]> cache_tree_shape = new HashMap<>();

    public static String[] tree_shape (String location) {

        if (cache_tree_shape.containsKey(location) == false) {

            File file = new File(Handcode.directory_config + "/custom_packs/" + location);

            if (file.exists() == true && file.isDirectory() == false) {

                cache_tree_shape.put(location, FileManager.fileToStringArray(file.getPath()));

            }

        }

        return cache_tree_shape.getOrDefault(location, new String[0]);

    }

}
