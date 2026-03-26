package tannyjung.tanshugetrees_handcode.data;

import tannyjung.tanshugetrees_core.Core;
import tannyjung.tanshugetrees_core.outside.ConfigDynamic;
import tannyjung.tanshugetrees_core.outside.CustomPackOrganizing;
import tannyjung.tanshugetrees_core.outside.FileManager;

public class DataRepair {

    public static void start () {

        CustomPackOrganizing.start("functions/presets/world_gen");

        // Custom
        {

            FileManager.createEmptyFile(Core.path_config + "/#dev/shape_file_converter", true);

            ConfigDynamic.reorganize("worldgen", "world_gen", """
                    - world_gen : Enable world generation for that tree by set to [ true ], or disable by [ false ].
                    - biome / ground_block : Change the biome and ground block that tree can place on. Supported both IDs and tags. These config supported multiple conditions, use [ / ] for [ OR ], use [ , ] for [ AND ]. For example, a tree that spawn in 2 main biomes. One is biomes tagged as forest, but not birch forest. Other one is taiga forest. It will be [ #minecraft:is_forest, !minecraft:birch_forest / minecraft:taiga ]. Important note for ground block, it not works with trees that one side farther than 48 blocks.
                    - rarity : Change how common of that tree. Lower means rarer. Only supported number between 0 and 100 (can be non-integer number).
                    - min_distance : Change distance of trees in the same species. This is distance in block with Y position ignored. Only supported number between 0 to 500.
                    - group_size : Spawn addition trees of the same species of it around the area. To use this, set min and max count of trees per group that upper than 1. For example, min 1 and max 5, will be [ 1 <> 5 ]. Be careful to use this, as it can affect scan time. This config also change the way other config options work. Rarity will be how common of the group. Min distance is between trees, not between groups. Waterside config will only detect once at spawn location of that group.
                    - waterside_chance : Force that tree to only spawn near water biomes. If this chance is not full, it will spawn like normal for that chance left. For example, set this to 0.75, it will spawn like normal tree for 0.25 chance. When use this option with group spawning, it will only detect once at spawn location of that group.
                    - dead_tree_chance : Set how common of that tree to spawn as dead tree
                    - dead_tree_level : Randomly select style of dead trees to make it looks more variety. This config will be randomly select a number from the list, or use "auto" and "auto_pine" for automatic selection. Only supported numbers 1XX/2XX/3XX with sub numbers 10/20/30/40/50/60/70/80/90 and 11/21/31/41/51. Set to 1XX for normal dead trees, 2XX and 3XX for coarse woody debris style but with and without roots. For sub numbers 10/20/30/40/50 is no leaves, no sprig, no twig, no limb, and no branch. With random decay 10-50%. For 11/21/31/41/51 is the same as previous but no random decay. For 60/70 is only trunk with random length 50-100% and hollowed. For 80/90 is only trunk with random length 0-50% and hollowed.
                    - start_height_offset : Randomly spawn that tree with custom height from the ground. To use this, set min and max height. For example, lowest -10 highest +10, will be [ -10 <> 10 ].
                    - rotation : Set rotation of that tree. For random direction, use [ random ]. For specific direction, use [ north ], [ west ], [ east ], or [ south ]. Only supported one value per tree.
                    - mirrored : Set mirror effect for that tree. For random value, use [ random ]. For specific value, use [ true ] or [ false ].
                    - path_storage : The part of tree shapes that will be used by this tree
                    - path_settings : The path of tree settings that will be applied into this tree
                    """);

        }

        FileConfig.repair();
        FileConfig.apply();

    }

}
