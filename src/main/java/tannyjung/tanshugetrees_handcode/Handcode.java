package tannyjung.tanshugetrees_handcode;

import tannyjung.tanshugetrees_core.Core;
import tannyjung.tanshugetrees_core.game.GameUtils;
import tannyjung.tanshugetrees_handcode.data.FileConfig;
import tannyjung.tanshugetrees_handcode.systems.world_gen.FeatureAreaDirt;
import tannyjung.tanshugetrees_handcode.systems.world_gen.FeatureAreaGrass;

public class Handcode {
    
    public static boolean compatibility_serene_seasons = false;

    public static void start () {

        Core.data_structure_version_core = 1;
        Core.data_structure_version_mod = "1.8.0";
        Core.data_structure_version_pack = "1.8.0";
        Core.tanny_pack_type = "WIP";

        Core.mod_name = "Tan's Huge Trees";
        Core.mod_id = "tanshugetrees";
        Core.mod_id_short = "T";

        Core.github_pack = "THT-tree_pack";
        Core.wiki = "https://sites.google.com/view/tannyjung/minecraft-mods/tans-huge-trees";

        compatibility_serene_seasons = FileConfig.compatibility_serene_seasons && GameUtils.Misc.isModLoaded("sereneseasons");

    }

    public static void registry () {

        Core.Registries.features.put("area_grass", FeatureAreaGrass::new);
        Core.Registries.features.put("area_dirt", FeatureAreaDirt::new);

    }

}
