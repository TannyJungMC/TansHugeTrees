package tannyjung.tanshugetrees_handcode.data;

import net.minecraft.server.level.ServerLevel;
import tannyjung.tanshugetrees_core.Core;
import tannyjung.tanshugetrees_core.outside.CustomPackOrganizing;
import tannyjung.tanshugetrees_core.outside.FileManager;

import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class DataRepair {

    private static Map<String, Map<String, List<String>>> pack_errors = new HashMap<>();

    public static void start () {

        String pack_separation_single = "leaf_litter/sapling_trader";
        String pack_separation_multiple = "functions/presets/tree_decoration/world_gen";
        pack_errors = CustomPackOrganizing.start(pack_separation_single, pack_separation_multiple);

        FileManager.createEmptyFile(Core.path_config + "/#dev/shape_file_converter", true);
        FileConfig.repair();
        FileConfigWorldGen.start();
        FileShapeConverter.start();
        FileConfig.apply();























        try {

            InputStream stream = Core.class.getResourceAsStream("/data/" + Core.mod_id + "/#TannyJung-Main-Pack.zip");

            if (stream != null) {

                Files.copy(stream, Path.of(Core.path_config + "/#dev/#temporary/#TannyJung-Main-Pack.zip"), StandardCopyOption.REPLACE_EXISTING);
                stream.close();

            }

        } catch (Exception e) {

            e.getStackTrace();

        }












    }

    public static void messagePackErrors (ServerLevel level_server) {

        CustomPackOrganizing.sendErrorMessage(level_server, pack_errors, "pack");

        if (FileConfig.developer_mode == true) {

            CustomPackOrganizing.sendErrorMessage(level_server, pack_errors, "file");

        } else {

            CustomPackOrganizing.sendErrorMessage(null, pack_errors, "file");

        }

    }

}
