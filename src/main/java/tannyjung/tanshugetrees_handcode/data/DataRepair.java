package tannyjung.tanshugetrees_handcode.data;

import net.minecraft.server.level.ServerLevel;
import tannyjung.tanshugetrees.TanshugetreesMod;
import tannyjung.tanshugetrees_core.CustomPackOrganizing;
import tannyjung.tanshugetrees_core.FileManager;
import tannyjung.tanshugetrees_handcode.Handcode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataRepair {

    private static Map<String, Map<String, List<String>>> cache_pack_errors = new HashMap<>();

    public static void start () {

        String pack_separation_single = "leaf_litter";
        String pack_separation_multiple = "functions/presets/world_gen";
        cache_pack_errors = CustomPackOrganizing.start(Handcode.path_config, pack_separation_single, pack_separation_multiple, Handcode.data_structure_version_config);

        FileManager.createEmptyFile(Handcode.path_config + "/#dev/shape_file_converter", true);
        FileConfig.repair();
        FileConfigWorldGen.start();
        FileShapeConverter.start();
        FileConfig.apply();

    }

    public static void messagePackErrors (ServerLevel level_server, String type) {

        if (type.contains("pack") == true) CustomPackOrganizing.sendErrorMessage(level_server, TanshugetreesMod.LOGGER, "THT", cache_pack_errors, "pack");
        if (type.contains("file") == true) CustomPackOrganizing.sendErrorMessage(level_server, TanshugetreesMod.LOGGER, "THT", cache_pack_errors, "file");

    }

}
