package tannyjung.tanshugetrees_handcode.data;

import tannyjung.tanshugetrees_core.Core;
import tannyjung.tanshugetrees_core.outside.ConfigDynamic;
import tannyjung.tanshugetrees_core.outside.CustomPackOrganizing;
import tannyjung.tanshugetrees_core.outside.FileManager;

public class DataRepair {

    public static void start () {

        CustomPackOrganizing.start("functions/presets/world_gen");
        FileManager.createEmptyFile(Core.path_config + "/#dev/shape_file_converter", true);
        ConfigDynamic.reorganize("worldgen", "world_gen");

        FileConfig.repair();
        FileConfig.apply();

    }

}
