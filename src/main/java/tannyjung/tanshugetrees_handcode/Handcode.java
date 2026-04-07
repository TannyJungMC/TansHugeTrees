package tannyjung.tanshugetrees_handcode;

import tannyjung.tanshugetrees_core.Core;
import tannyjung.tanshugetrees_core.game.GameUtils;
import tannyjung.tanshugetrees_core.outside.*;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Handcode {

    public static boolean compatibility_serene_seasons = false;

    public static void start () {

        Core.data_structure_version_core = 1;
        Core.data_structure_version_mod = "1.8.0";
        Core.data_structure_version_pack = "1.8.0";
        Core.tanny_pack_type = "Beta";

        Core.mod_name = "Tan's Huge Trees";
        Core.mod_id = "tanshugetrees";
        Core.mod_id_short = "T";
        Core.github_pack = "THT-tree_pack";
        Core.wiki = "https://sites.google.com/view/tannyjung/minecraft-mods/tans-huge-trees";

        Core.have_world_data_cleaner = true;

        compatibility_serene_seasons = Config.compatibility_serene_seasons && GameUtils.Misc.isModLoaded("sereneseasons");

    }

    public static void repairData () {

        FileManager.createEmptyFile(Core.path_config + "/dev/shape_file_converter", true);
        CustomPackOrganizing.start("functions / presets / world_gen", "presets < _settings / world_gen", "functions / leaf_litter / tree_decoration");

        ConfigDynamic.reorganize("world_gen", "world_gen", """
                enable = false
                # Enable world generation for that tree by set to [ true ] or disable by [ false ].
                spawn_type = none
                # Set how that tree spawn in the world. Set to [ normal ] for spawn by biome like normal. Set to [ waterside ] for only spawn if detects water biomes nearby, but their group can't be spawned in water biomes. Set to [ landside ] for only spawn if detects water biomes nearby, but their group can be spawned in water biomes. Set to [ shoreline ] for only spawn if detects water biomes nearby, but their group can be spawned in both their biomes and water biomes.
                biome = none
                # Change biome of that tree can place on. Supported both IDs and tags. These config supported multiple conditions, use [ / ] for [ OR ], use [ , ] for [ AND ]. For example, a tree that spawn in 2 type of biomes. One is biomes tagged as forest, but not birch forest. Other one is taiga forest. It will be [ #minecraft:is_forest, !minecraft:birch_forest / minecraft:taiga ].
                ground_block = none
                # Change ground block of that tree can place on. Supported both IDs and tags. These config supported multiple conditions, use [ / ] for [ OR ], use [ , ] for [ AND ]. For example, a tree that spawn on 2 type of blocks. One is any of blocks tagged as dirt but not grass. Other one is sand block. It will be [ #minecraft:dirt, !minecraft:grass / minecraft:sand ]. Important note, it may not works with trees that one side farther than 32 blocks.
                rarity = 0
                # Change how common of that tree. Lower means rarer. Only supported number between 0 and 100 (can be non-integer number).
                min_distance = 0
                # Change distance of trees in the same species. This is distance in block with Y position ignored. Only supported number between 0 to 500.
                group_size = 1 <> 1
                # Spawn addition trees of the same species of it around the area. To use this, set min and max count of trees per group that upper than 1. For example, min 1 and max 5, will be [ 1 <> 5 ]. Be careful to use this, as it can affect scan time. This config also change the way other config options work. Rarity will be how common of the group. Min distance is between trees, not between groups. Waterside config will only detect once at spawn location of that group.
                dead_tree_chance = 0.0
                # Set how common of that tree to spawn as a dead tree. Note that the trees can still be dead trees when spawn in unviable ecology, such as land trees in water.
                dead_tree_level = auto
                # Randomly select style of dead trees to make it looks more variety. This config will be randomly select a number from the list, or use "auto" and "auto_pine" for automatic selection. Only supported numbers 1XX/2XX/3XX with sub numbers 10/20/30/40/50/60/70/80/90 and 11/21/31/41/51. Set to 1XX for normal dead trees, 2XX and 3XX for coarse woody debris style but with and without roots. For sub numbers 10/20/30/40/50 is no leaves, no sprig, no twig, no limb, and no branch. With random decay 10-50%. For 11/21/31/41/51 is the same as previous but no random decay. For 60/70 is only trunk with random length 50-100% and hollowed. For 80/90 is only trunk with random length 0-50% and hollowed.
                start_height_offset = 0 <> 0
                # Randomly spawn that tree with custom height from the ground. To use this, set min and max height. For example, lowest -10 highest +10, will be [ -10 <> 10 ].
                rotation = random
                # Set rotation of that tree. For random direction, use [ random ]. For specific direction, use [ north ], [ west ], [ east ], or [ south ]. Only supported one value per tree.
                mirrored = random
                # Set mirror effect for that tree. For random value, use [ random ]. For specific value with 50% random, use [ random_x ] or [ random_z ]. Use [ off ] to disable mirror effect.
                path_storage = none
                # The part of tree shapes that will be used by that tree
                path_settings = none
                # The path of tree settings that will be applied into that tree
                """);

        ConfigClassic.repair(Core.path_config + "/dev/shape_file_converter/settings.txt", """
                - This only for convert tree presets into tree shapes, for use in custom packs. Not a place for people to run around.
                - To use this. Set render distance to 32 or depends on tree size. Then write correct file location below and use command [ /TANSHUGETREES command shape_file_converter start <loop> ].
                
                ----------------------------------------------------------------------------------------------------
                
                path_preset = none
                | Path of the preset you will convert. The part will be something like [ presets/#TannyJung-Main-Pack/redwood/redwood ]. This path will go to extracted packs first, then go to unextracted packs if failed.
                
                ----------------------------------------------------------------------------------------------------
                """);

    }

    public static class Config {

        public static boolean auto_check_update = false;
        public static boolean wip_version = false;

        public static int region_scan_percent = 0;
        public static double multiply_rarity = 0.0;
        public static double multiply_min_distance = 0.0;
        public static double multiply_group_size = 0.0;
        public static double multiply_dead_tree_chance = 0.0;
        public static boolean tree_location = false;
        public static boolean world_gen_roots = false;
        public static int max_height_spawn = 0;
        public static double unviable_ecology_skip_chance = 0.0;
        public static boolean leaf_litter_world_gen = false;
        public static double leaf_litter_world_gen_chance = 0.0;
        public static double leaf_litter_world_gen_chance_coniferous = 0.0;
        public static boolean abscission_world_gen = false;
        public static Set<String> dead_tree_auto_level = new HashSet<>();
        public static boolean tree_decorations = false;
        public static double tree_decorations_normal_chance = 0.0;
        public static double tree_decorations_decay_chance = 0.0;

        public static boolean shoreline_detection = false;
        public static boolean surface_smoothness_detection = false;
        public static int surface_smoothness_detection_percent = 0;
        public static int surface_smoothness_detection_height_up = 0;
        public static int surface_smoothness_detection_height_down = 0;
        public static int structure_detection_size = 0;

        public static boolean living_mechanics = false;
        public static int living_mechanics_tick = 0;
        public static int living_mechanics_process_limit = 0;
        public static int living_mechanics_simulation = 0;
        public static boolean leaf_litter = false;
        public static boolean leaf_litter_classic = false;
        public static boolean leaf_litter_classic_only = false;
        public static double leaf_litter_remover_chance = 0.0;
        public static int leaf_litter_remover_count_limit = 0;
        public static double falling_leaf_chance = 0.0;
        public static int falling_leaf_count_limit = 0;
        public static int leaf_light_level_detection = 0;
        public static double dead_leaf_drop_chance = 0.0;

        public static boolean compatibility_serene_seasons = false;
        public static double leaf_drop_chance_spring = 0;
        public static double leaf_drop_chance_summer = 0.0;
        public static double leaf_drop_chance_autumn = 0.0;
        public static double leaf_drop_chance_winter = 0.0;
        public static double leaf_regrowth_chance_spring = 0.0;
        public static double leaf_regrowth_chance_summer = 0.0;
        public static double leaf_regrowth_chance_autumn = 0;
        public static double leaf_regrowth_chance_winter = 0;
        public static double leaf_drop_chance_coniferous = 0.0;
        public static double leaf_regrowth_chance_coniferous = 0.0;
        public static Set<String> deciduous_leaves_list = null;
        public static Set<String> coniferous_leaves_list = null;

        public static boolean tree_generator_speed_global = false;
        public static int tree_generator_speed_tick = 0;
        public static int tree_generator_speed_repeat = 0;
        public static int tree_generator_count_limit = 0;

        public static boolean developer_mode = false;
        public static boolean world_gen_icon = false;

        public static void repair () {

            String write = """
                    
                    ----------------------------------------------------------------------------------------------------
                    TannyJung's Main Pack
                    ----------------------------------------------------------------------------------------------------
                    
                    auto_check_update = true
                    | Check for new update from GitHub every time the world starts
                    
                    wip_version = false
                    | Use development version of the pack instead of release version. Not recommended for game play, as it's still in development, it might unstable. Sometimes it needed development version of the mod.
                    
                    ----------------------------------------------------------------------------------------------------
                    World Generation
                    ----------------------------------------------------------------------------------------------------
                    
                    region_scan_percent = 100
                    | Set percent of chunk scan per region, from region pre-location system. One region contains 32x32 chunks, or 1,024 chunks. Lower this can reduce scan time, also lower the chance of all trees.
                    
                    multiply_rarity = 1.0
                    multiply_min_distance = 1.0
                    multiply_group_size = 1.0
                    multiply_dead_tree_chance = 1.0
                    | These number will be multiplied to all tree config in these types
                    
                    tree_location = true
                    | Enable marker entity for tree location to store some tree data and for some custom features. Disable this can reduce number of entities, but some features will not work such as living mechanics.
                    
                    world_gen_roots = true
                    | Enable tree roots when generate in world gen. Note that disable this will no affect to some trees, because roots is important part for them. Also will no affect to taproot part.
                    
                    max_height_spawn = 0
                    | Cancel the trees when their spawn center is above this Y level. As some world gen mods such as ReTerraForged, replacing mountain block and my trees can't detect those new block, make them spawn on blocks that not in the list. Set to 0 to disable this.
                    
                    unviable_ecology_skip_chance = 0.0
                    | Skip trees that generate in unviable ecosystems. For example, land trees that generate in water. This config only affect to dead trees, as normal trees already skip generate in unviable ecosystems.
                    
                    leaf_litter_world_gen = true
                    leaf_litter_world_gen_chance = 0.1
                    leaf_litter_world_gen_chance_coniferous = 0.05
                    | Create leaf litter on ground and water, while in world gen. Leaf litter config must be enable to allow this.
                    
                    abscission_world_gen = true
                    | Make all deciduous trees generate with all leaves dropped to the ground when they're in snowy biomes
                    
                    dead_tree_auto_level = 11 / 12 / 13 / 14 / 15 / 16 / 17 / 18 / 19 / 21 / 22 / 23 / 24 / 25 / 26 / 27 / 28 / 29 / 31 / 32 / 33 / 34 / 35 / 36 / 37 / 38 / 39
                    | Randomly pick these number for trees that set dead tree level as "auto" and "auto_pine". 1X is normal dead trees. 2X is fallen trees with roots. 3X is fallen trees without roots. X1 X2 X3 X4 X5 is no leaves, no sprig, no twig, no limb, no branch. X6 X7 is only trunk 50-100% and hollowed. X8 X9 is only trunk 10-50% and hollowed.
                    
                    tree_decorations = true
                    tree_decorations_normal_chance = 0.01
                    tree_decorations_decay_chance = 0.5
                    | Run decoration functions while generate trees. Disable these or reduce their chances can improve world gen speed.
                    
                    ----------------------------------------------------------------------------------------------------
                    World Generation : Surrounding Area Detection
                    ----------------------------------------------------------------------------------------------------
                    
                    shoreline_detection = true
                    | Enable shoreline system for trees that use this feature. If disable this, all trees that spawn at waterside, landside, and shoreline will be skipped and not spawn anywhere.
                    
                    surface_smoothness_detection = true
                    | Force the trees to only spawn on good areas. Note that this system only detects 4 points around tree center, so it's not 100% perfect.
                    
                    surface_smoothness_detection_percent = 50
                    | How far detection point is at. Set to 100 for same as tree size.
                    
                    surface_smoothness_detection_height_up = 50
                    | Set height up of surface smoothness. Set to 100 for same as Y size of the tree above its center.
                    
                    surface_smoothness_detection_height_down = 25
                    | Set height down of surface smoothness. Set to 100 for same as Y size of the tree below its center.
                    
                    structure_detection_size = 0
                    | Cancel trees if they detect structure in their area based from their size. This number will be plus with their size, higher number bigger distance. Note that this feature is not perfect, trees with long size might not be canceled. Only support number between is 0 to 9. Set to 0 for only chunks that marked as having structures. Set to -1 to disable this feature.
                    
                    ----------------------------------------------------------------------------------------------------
                    Living Mechanics
                    ----------------------------------------------------------------------------------------------------
                    
                    living_mechanics = true
                    | Enable some custom systems to make the trees from this mod feel more alive. Such as leaf drop and regrowth, leaf decay, leaf litter, and abscission.
                    
                    living_mechanics_tick = 5
                    | How fast in tick of living mechanics system. Set to 0 to temporary pause the tick.
                    
                    living_mechanics_process_limit = 500
                    | How many process for trees to run this system per time. Set to 0 for one time process.
                    
                    living_mechanics_simulation = 100
                    | Simulate fake trees to slowdown the process. For example, when I set tree speed for 100 trees. But there's only 1 tree in the area, it will drop and regrow leaves very fast because that's the speed for 100 trees. Set this config will simulate fake trees and make that 1 tree slowdown it process like there's 99 trees around it.
                    
                    leaf_litter = true
                    | Create leaf litter on ground and water
                    
                    leaf_litter_classic = true
                    | Use classic style for leaf litter when that leaves block have no custom style. Classic style will use block of itself as litter block.
                    
                    leaf_litter_classic_only = false
                    | Only use classic style for all leaf litters
                    
                    leaf_litter_remover_chance = 0.001
                    | Chance of leaf litter on the ground to disappear per process
                    
                    leaf_litter_remover_count_limit = 100
                    | Count limit of the leaf litter remover
                    
                    falling_leaf_chance = 1.0
                    | Chance of falling leaf that will appear at leaves blocks when leaf drop system pick that block. When this leaf animation touch the ground or water, it will create leaf litter there. Other than this chance will be use instant drop without animation.
                    
                    falling_leaf_count_limit = 500
                    | Count limit of falling leaf
                    
                    leaf_light_level_detection = 7
                    | Minimum light level of leaves can survive. Leaves will drop themselves if light level is under this value. Set to 15 for only full bright level. Set to 0 for no light level affect.
                    
                    dead_leaf_drop_chance = 0.001
                    | Chance of leaves to drop themselves when the tree is dead by light level and missing center block
                    
                    ----------------------------------------------------------------------------------------------------
                    Living Mechanics : Leaf Cycle and Seasons
                    ----------------------------------------------------------------------------------------------------
                    
                    compatibility_serene_seasons = true
                    | Sync seasons to Serene Seasons mod by using area at world spawn, run the test every one minute.
                    
                    leaf_drop_chance_spring = 0.0
                    leaf_drop_chance_summer = 0.005
                    leaf_drop_chance_autumn = 0.01
                    leaf_drop_chance_winter = 0.01
                    leaf_regrowth_chance_spring = 0.01
                    leaf_regrowth_chance_summer = 0.01
                    leaf_regrowth_chance_autumn = 0.0
                    leaf_regrowth_chance_winter = 0.0
                    | Chance of deciduous leaves to drop and regrow based on seasons. But note that it will only use summer value when in tropical biomes, and for other leaves that not marked as deciduous and coniferous.
                    
                    leaf_drop_chance_coniferous = 0.0001
                    leaf_regrowth_chance_coniferous = 0.005
                    | Chance of coniferous leaves to drop in summer and regrow in any season
                    
                    deciduous_leaves_list = minecraft:oak_leaves / minecraft:birch_leaves
                    coniferous_leaves_list = minecraft:spruce_leaves
                    | List of deciduous and coniferous leaves blocks. Deciduous is oak trees and similar. They will drop their leaves before winter, but note that they will not do that in tropical biomes. Coniferous is pine trees. They will drop their leaves only in summer and almost very rare.
                    
                    ----------------------------------------------------------------------------------------------------
                    Tree Generator
                    ----------------------------------------------------------------------------------------------------
                    
                    tree_generator_speed_global = true
                    | When true, it will use same speed for all generators.
                    
                    tree_generator_speed_tick = 1
                    | How fast of generators in tick. Increase this will make them slower. Set to 0 for temporary pause all generators.
                    
                    tree_generator_speed_repeat = 100
                    | How many processes the generators run in a time. Increase this will make them generate faster but also can cause lag. Set to 0 for one time generation that can freeze the game.
                    
                    tree_generator_count_limit = 1
                    | How many generators will generate in the same time. Set to 0 for no limit.
                    
                    ----------------------------------------------------------------------------------------------------
                    Miscellaneous
                    ----------------------------------------------------------------------------------------------------
                    
                    developer_mode = false
                    | Enable some features for debugging such as detailed error messages, info overlay in-game, etc.
                    
                    world_gen_icon = true
                    | Enable little icon at top-left showing everytime the mod generate new region. This config only affect on singleplayer.
                    
                    ----------------------------------------------------------------------------------------------------
                    """;

            ConfigClassic.repair(Core.path_config + "/config.txt", write);

        }

        public static void apply () {

            Map<String, String> data = ConfigClassic.getValues(Core.path_config + "/config.txt");

            auto_check_update = Boolean.parseBoolean(data.get("auto_check_update"));
            wip_version = Boolean.parseBoolean(data.get("wip_version"));

            region_scan_percent = Integer.parseInt(data.get("region_scan_percent"));
            multiply_rarity = Double.parseDouble(data.get("multiply_rarity"));
            multiply_min_distance = Double.parseDouble(data.get("multiply_min_distance"));
            multiply_group_size = Double.parseDouble(data.get("multiply_group_size"));
            multiply_dead_tree_chance = Double.parseDouble(data.get("multiply_dead_tree_chance"));
            tree_location = Boolean.parseBoolean(data.get("tree_location"));
            world_gen_roots = Boolean.parseBoolean(data.get("world_gen_roots"));
            max_height_spawn = Integer.parseInt(data.get("max_height_spawn"));
            unviable_ecology_skip_chance = Double.parseDouble(data.get("unviable_ecology_skip_chance"));
            leaf_litter_world_gen = Boolean.parseBoolean(data.get("leaf_litter_world_gen"));
            leaf_litter_world_gen_chance = Double.parseDouble(data.get("leaf_litter_world_gen_chance"));
            leaf_litter_world_gen_chance_coniferous = Double.parseDouble(data.get("leaf_litter_world_gen_chance_coniferous"));
            abscission_world_gen = Boolean.parseBoolean(data.get("abscission_world_gen"));
            dead_tree_auto_level = new HashSet<>(List.of(data.get("dead_tree_auto_level").split(" / ")));
            tree_decorations = Boolean.parseBoolean(data.get("tree_decorations"));
            tree_decorations_normal_chance = Double.parseDouble(data.get("tree_decorations_normal_chance"));
            tree_decorations_decay_chance = Double.parseDouble(data.get("tree_decorations_decay_chance"));

            shoreline_detection = Boolean.parseBoolean(data.get("shoreline_detection"));
            surface_smoothness_detection = Boolean.parseBoolean(data.get("surface_smoothness_detection"));
            surface_smoothness_detection_percent = Integer.parseInt(data.get("surface_smoothness_detection_percent"));
            surface_smoothness_detection_height_up = Integer.parseInt(data.get("surface_smoothness_detection_height_up"));
            surface_smoothness_detection_height_down = Integer.parseInt(data.get("surface_smoothness_detection_height_down"));
            structure_detection_size = Integer.parseInt(data.get("structure_detection_size"));

            living_mechanics = Boolean.parseBoolean(data.get("living_mechanics"));
            living_mechanics_tick = Integer.parseInt(data.get("living_mechanics_tick"));
            living_mechanics_process_limit = Integer.parseInt(data.get("living_mechanics_process_limit"));
            living_mechanics_simulation = Integer.parseInt(data.get("living_mechanics_simulation"));
            leaf_litter = Boolean.parseBoolean(data.get("leaf_litter"));
            leaf_litter_classic = Boolean.parseBoolean(data.get("leaf_litter_classic"));
            leaf_litter_classic_only = Boolean.parseBoolean(data.get("leaf_litter_classic_only"));
            leaf_litter_remover_chance = Double.parseDouble(data.get("leaf_litter_remover_chance"));
            leaf_litter_remover_count_limit = Integer.parseInt(data.get("leaf_litter_remover_count_limit"));
            falling_leaf_chance = Double.parseDouble(data.get("falling_leaf_chance"));
            falling_leaf_count_limit = Integer.parseInt(data.get("falling_leaf_count_limit"));
            leaf_light_level_detection = Integer.parseInt(data.get("leaf_light_level_detection"));
            dead_leaf_drop_chance = Double.parseDouble(data.get("dead_leaf_drop_chance"));

            compatibility_serene_seasons = Boolean.parseBoolean(data.get("compatibility_serene_seasons"));
            leaf_drop_chance_spring = Double.parseDouble(data.get("leaf_drop_chance_spring"));
            leaf_drop_chance_summer = Double.parseDouble(data.get("leaf_drop_chance_summer"));
            leaf_drop_chance_autumn = Double.parseDouble(data.get("leaf_drop_chance_autumn"));
            leaf_drop_chance_winter = Double.parseDouble(data.get("leaf_drop_chance_winter"));
            leaf_regrowth_chance_spring = Double.parseDouble(data.get("leaf_regrowth_chance_spring"));
            leaf_regrowth_chance_summer = Double.parseDouble(data.get("leaf_regrowth_chance_summer"));
            leaf_regrowth_chance_autumn = Double.parseDouble(data.get("leaf_regrowth_chance_autumn"));
            leaf_regrowth_chance_winter = Double.parseDouble(data.get("leaf_regrowth_chance_winter"));
            leaf_drop_chance_coniferous = Double.parseDouble(data.get("leaf_drop_chance_coniferous"));
            leaf_regrowth_chance_coniferous = Double.parseDouble(data.get("leaf_regrowth_chance_coniferous"));
            deciduous_leaves_list = new HashSet<>(List.of(data.get("deciduous_leaves_list").split(" / ")));
            coniferous_leaves_list = new HashSet<>(List.of(data.get("coniferous_leaves_list").split(" / ")));

            tree_generator_speed_global = Boolean.parseBoolean(data.get("tree_generator_speed_global"));
            tree_generator_speed_tick = Integer.parseInt(data.get("tree_generator_speed_tick"));
            tree_generator_speed_repeat = Integer.parseInt(data.get("tree_generator_speed_repeat"));
            tree_generator_count_limit = Integer.parseInt(data.get("tree_generator_count_limit"));

            developer_mode = Boolean.parseBoolean(data.get("developer_mode"));
            world_gen_icon = Boolean.parseBoolean(data.get("world_gen_icon"));

        }

    }

    public static class DataMigration {

        public static void runConfig (String version) {

            if (version.isEmpty() == true) {

                {

                    Core.logger.info("Running config data migration for failed condition");
                    FileManager.delete(Core.path_config + "/#dev");
                    FileManager.rename(Core.path_config + "/custom_packs/THT-tree_pack-main", "#TannyJung-Main-Pack");
                    FileManager.rename(Core.path_config + "/custom_packs/TannyJung-Main-Pack", "#TannyJung-Main-Pack");

                }

            }

            if (OutsideUtils.testVersion("1.8.0", version).equals("outdated") == true) {

                {

                    Core.logger.info("Running config data migration for 1.8.0");
                    FileManager.rename(Core.path_config + "/config_worldgen.txt", "config_world_gen.txt");

                }

            }

        }

        public static void runWorld (String version) {

            if (version.isEmpty() == true) {

                {

                    Core.logger.info("Running world data migration for failed condition");
                    FileManager.delete(Core.path_world_mod);

                }

            }

            if (OutsideUtils.testVersion("1.8.0", version).equals("outdated") == true) {

                {

                    Core.logger.info("Running world data migration for 1.8.0");
                    FileManager.rename(Core.path_world_mod + "/world_gen/#regions", "regions");
                    FileManager.delete(Core.path_world_mod + "/world_gen/tree_location");
                    FileManager.delete(Core.path_world_mod + "/world_gen/place");

                }

            }

        }

    }

}
