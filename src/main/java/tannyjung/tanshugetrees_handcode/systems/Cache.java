package tannyjung.tanshugetrees_handcode.systems;

import tannyjung.core.FileManager;
import tannyjung.core.OutsideUtils;
import tannyjung.tanshugetrees_handcode.Handcode;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Cache {

    private static final Map<String, short[]> cache_tree_shape = new HashMap<>();

    public static short[] tree_shape (String location) {

        if (cache_tree_shape.containsKey(location) == false) {

            cache_tree_shape.put(location, FileManager.readBIN(Handcode.directory_config + "/custom_packs/" + location, 13, 0));

        }

        return cache_tree_shape.getOrDefault(location, new short[0]);

    }

}
