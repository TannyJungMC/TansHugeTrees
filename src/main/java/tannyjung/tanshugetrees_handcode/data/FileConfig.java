package tannyjung.tanshugetrees_handcode.data;

import tannyjung.tanshugetrees_core.Core;
import tannyjung.tanshugetrees_core.outside.ConfigClassic;


import java.util.*;

public class FileConfig {

	public static boolean auto_check_update = false;
	public static boolean wip_version = false;

	public static double region_scan_percent = 0.0;
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

	public static boolean waterside_detection = false;
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
	public static double leaf_drop_animation_chance = 0.0;
	public static int leaf_drop_animation_count_limit = 0;
	public static int leaf_light_level_detection = 0;
	public static double leaf_dead_drop_chance = 0.0;

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
                | Use development version of the pack, instead of release version. Not recommended for game play, as it's still in development, it might unstable. Sometimes it needed development version of the mod.
                
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
                
                ----------------------------------------------------------------------------------------------------
                World Generation : Surrounding Area Detection
                ----------------------------------------------------------------------------------------------------
                
                waterside_detection = true
                | Enable waterside system for trees that use this feature. If disable this, all waterside trees will be skipped and not spawn anywhere.
                
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
                
                leaf_litter_remover_chance = 0.0001
                | Chance of leaf litter on the ground to disappear per process
                
                leaf_litter_remover_count_limit = 100
                | Count limit of the leaf litter remover
                
                leaf_drop_animation_chance = 1.0
                | Chance of animation that will appear on the trees. When this leaf animation touch the ground or water, it will create leaf litter there. Other than this chance will be use instant drop without animation.
                
                leaf_drop_animation_count_limit = 500
                | Count limit of leaf drop animation
                
                leaf_light_level_detection = 7
                | Minimum light level of leaves can survive. Leaves will drop themselves if light level is under this value. Set to 15 for only full bright level. Set to 0 for no light level affect.
                
                leaf_dead_drop_chance = 0.001
                | Chance of leaves to drop themselves when they're dead by light level and missing center block
                
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
                | Enable some features for debugging. Such as detailed error messages, region pre-location details in-game, etc.
                
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

        region_scan_percent = Double.parseDouble(data.get("region_scan_percent"));
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

		waterside_detection = Boolean.parseBoolean(data.get("waterside_detection"));
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
		leaf_drop_animation_chance = Double.parseDouble(data.get("leaf_drop_animation_chance"));
		leaf_drop_animation_count_limit = Integer.parseInt(data.get("leaf_drop_animation_count_limit"));
		leaf_light_level_detection = Integer.parseInt(data.get("leaf_light_level_detection"));
		leaf_dead_drop_chance = Double.parseDouble(data.get("leaf_dead_drop_chance"));

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

        if (wip_version == true) {

			Core.tanny_pack_type = "WIP";

        } else {

			Core.tanny_pack_type = Core.tanny_pack_type_original;

        }

	}
	
}