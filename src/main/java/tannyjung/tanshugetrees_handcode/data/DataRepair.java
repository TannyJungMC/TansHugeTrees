package tannyjung.tanshugetrees_handcode.data;

import net.minecraft.server.level.ServerLevel;
import tannyjung.core.CustomPackOrganizing;
import tannyjung.core.FileManager;
import tannyjung.tanshugetrees_handcode.Handcode;
import tannyjung.tanshugetrees_handcode.data.migration.DataMigration;

public class DataRepair {

    public static void start (ServerLevel level_server) {

        DataMigration.start(level_server != null);
        FileManager.createEmptyFile(Handcode.path_config + "/#dev/shape_file_converter", true);

        String pack_separation_single = "leaf_litter";
        String pack_separation_multiple = "functions/presets/world_gen";
        CustomPackOrganizing.start(level_server, Handcode.path_config, pack_separation_single, pack_separation_multiple);





        FileConfig.repair();
        FileConfigWorldGen.start();
        FileShapeConverter.start();

        FileConfig.apply();

    }

}
