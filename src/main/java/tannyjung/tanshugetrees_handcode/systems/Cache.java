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

            File file = new File(Handcode.directory_config + "/custom_packs/" + location);

            if (file.exists() == true && file.isDirectory() == false) {

                try {

                    DataInputStream file_bin = new DataInputStream(new FileInputStream(Handcode.directory_config + "/#dev/shape_file_converter/test.bin"));

                    {

                        // while (file_bin.available() > 0) {

                            // cache_tree_shape.put(location, ) file_bin.readShort();

                        // }

                    }

                    file_bin.close();

                } catch (Exception exception) {

                    OutsideUtils.exception(new Exception(), exception);

                }

            }

        }

        return cache_tree_shape.getOrDefault(location, new short[0]);

    }

}
