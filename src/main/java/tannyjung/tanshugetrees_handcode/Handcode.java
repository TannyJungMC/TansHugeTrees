package tannyjung.tanshugetrees_handcode;

import tannyjung.tanshugetrees_core.Core;
import tannyjung.tanshugetrees_core.game.GameUtils;
import tannyjung.tanshugetrees_handcode.data.FileConfig;

public class Handcode {
    
    public static boolean compatibility_serene_seasons = false;

    public static void start () {

        Core.mod_name = "Tan's Huge Trees";
        Core.mod_id = "tanshugetrees";
        Core.mod_id_short = "T";

        Core.data_structure_version_core = 1;
        Core.data_structure_version_mod = "1.8.0";
        Core.data_structure_version_pack = "1.8.0";
        Core.tanny_pack_type = "Beta";

        Core.github_pack = "THT-tree_pack";
        Core.wiki = "https://sites.google.com/view/tannyjung/minecraft-mods/tans-huge-trees";

        Core.have_world_data_cleaner = true;

        compatibility_serene_seasons = FileConfig.compatibility_serene_seasons && GameUtils.Misc.isModLoaded("sereneseasons");

    }

}
