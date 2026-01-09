package tannyjung.tanshugetrees_handcode.data;

import net.minecraft.server.level.ServerLevel;
import tannyjung.tanshugetrees.TanshugetreesMod;
import tannyjung.tanshugetrees_core.Core;
import tannyjung.tanshugetrees_core.outside.CustomPackOrganizing;
import tannyjung.tanshugetrees_core.outside.FileManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataRepair {

    private static Map<String, Map<String, List<String>>> pack_errors = new HashMap<>();

    public static void start () {

        String pack_separation_single = "leaf_litter/sapling_trader";
        String pack_separation_multiple = "functions/presets/tree_decoration/world_gen";
        pack_errors = CustomPackOrganizing.start(Core.path_config, pack_separation_single, pack_separation_multiple, Core.data_structure_version_pack);

        FileManager.createEmptyFile(Core.path_config + "/#dev/shape_file_converter", true);
        FileConfig.repair();
        FileConfigWorldGen.start();
        FileShapeConverter.start();
        FileConfig.apply();

    }

    public static void messagePackErrors (ServerLevel level_server, String type) {

        if (type.contains("pack") == true) CustomPackOrganizing.sendErrorMessage(level_server, TanshugetreesMod.LOGGER, Core.mod_id_short + "", pack_errors, "pack");
        if (type.contains("file") == true) CustomPackOrganizing.sendErrorMessage(level_server, TanshugetreesMod.LOGGER, Core.mod_id_short + "", pack_errors, "file");

    }

}
