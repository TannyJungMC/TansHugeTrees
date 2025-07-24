package tannyjung.tanshugetrees_handcode.systems;

import tannyjung.core.MiscUtils;
import tannyjung.tanshugetrees_handcode.Handcode;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public class Cache {

    private static final Map<String, String[]> cache_shape = new HashMap<>();

    public static String[] tree_shape (String location) {

        if (cache_shape.containsKey(location) == false) {

            File file = new File(Handcode.directory_config + "/custom_packs/" + location);

            try {

                cache_shape.put(location, Files.readAllLines(file.toPath(), StandardCharsets.UTF_8).toArray(new String[0]));

            } catch (Exception exception) {

                MiscUtils.exception(new Exception(), exception);

            }

        }

        return cache_shape.getOrDefault(location, new String[0]);

    }

}
