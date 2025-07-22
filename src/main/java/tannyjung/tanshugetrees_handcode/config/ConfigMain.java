package tannyjung.tanshugetrees_handcode.config;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.LevelAccessor;
import tannyjung.tanshugetrees_handcode.Handcode;
import tannyjung.core.FileManager;
import tannyjung.core.GameUtils;

import java.util.*;
import java.util.concurrent.CompletableFuture;

public class ConfigMain {

	// ----------------------------------------------------------------------------------------------------

	public static boolean auto_check_update = false;
	public static boolean auto_update = false;
	public static boolean wip_version = false;

	public static double region_scan_chance = 0.0;
	public static double multiply_rarity = 0.0;
	public static double multiply_min_distance = 0.0;
	public static double multiply_group_size = 0.0;
	public static double multiply_waterside_chance = 0.0;
	public static double multiply_dead_tree_chance = 0.0;
	public static boolean tree_location = false;
	public static boolean world_gen_roots = false;
	public static boolean surrounding_area_detection = false;
	public static int surrounding_area_detection_size = 0;
	public static boolean waterside_detection = false;
	public static boolean surface_smoothness_detection = false;
	public static int surface_smoothness_detection_height = 0;
	public static boolean pre_leaf_litter = false;
	public static double pre_leaf_litter_chance = 0.0;
	public static double pre_leaf_litter_chance_coniferous = 0.0;
	public static boolean abscission_world_gen = false;

	public static boolean living_tree_mechanics = false;
	public static int living_tree_mechanics_tick = 0;
	public static int living_tree_mechanics_process_limit = 0;
	public static int living_tree_mechanics_simulation = 0;
	public static boolean leaf_litter = false;
	public static boolean leaf_litter_classic = false;
	public static boolean leaf_litter_classic_only = false;
	public static double leaf_litter_remover_chance = 0.0;
	public static int leaf_litter_remover_count_limit = 0;
	public static double leaf_drop_animation_chance = 0.0;
	public static int leaf_drop_animation_count_limit = 0;
	public static int leaf_light_level_detection = 0;
	public static double leaf_light_level_detection_drop_chance = 0.0;
	public static Set<String> deciduous_leaves_list = null;
	public static Set<String> coniferous_leaves_list = null;

	public static boolean serene_seasons_compatibility = false;
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

	public static boolean global_speed_enable = false;
	public static int global_speed = 0;
	public static int global_speed_repeat = 0;
	public static int global_speed_tp = 0;
	public static int count_limit = 0;

	public static boolean square_parts = false;
	public static boolean square_leaves = false;
	public static int rt_roots = 0;
	public static boolean no_core = false;

	public static boolean developer_mode = false;

	// ----------------------------------------------------------------------------------------------------
	
	public static void repair () {

		StringBuilder write = new StringBuilder();

		{

			write.append("""
					Important Notes
					
					- To apply this config, run this command [ /TANSHUGETREES config apply ] or restart the world.
					- To repair missing values, run this command [ /TANSHUGETREES config repair ] or restart the world.
					
					----------------------------------------------------------------------------------------------------
					TannyJung's Main Pack
					----------------------------------------------------------------------------------------------------
					
					auto_check_update = true
					| Check for the new update from GitHub every time the world starts
					| Default is [ true ]
					
					auto_update = false
					| Auto update the pack every time the world starts, if there's a new update from GitHub. To use this feature, the "auto_check_update" config must be enable.
					| Default is [ false ]
					
					wip_version = false
					| Use WIP version instead of release version. Not recommended for game play, as it might contains new trees that still work in progress, also might needed development version of the mod.
					| Default is [ false ]
					
					----------------------------------------------------------------------------------------------------
					World Generation
					----------------------------------------------------------------------------------------------------
					
					region_scan_chance = 1.0
					| Set chance of chunk scan per region, for region pre-location system. One region contains 32x32 chunks, or 1,024 chunks. Lower this can reduce scan time, also lower the chance of all trees.
					| Default is [ 1.0 ]
					
					multiply_rarity = 1.0
					multiply_min_distance = 1.0
					multiply_group_size = 1.0
					multiply_waterside_chance = 1.0
					multiply_dead_tree_chance = 1.0
					| These number will be multiplied to all tree config in their types
					| Default is [ 1.0 For All ]
					
					tree_location = true
					| Store some tree data and for some custom features. Disable this can reduce number of entities, but some features will not work such as living tree mechanics.
					| Default is [ true ]
					
					world_gen_roots = true
					| Enable tree roots in world gen. Note that disable this feature will not affect to some trees, because roots is important part for them. Also not affect to taproot part.
					| Default is [ true ]
					
					surrounding_area_detection = true
					| ###
					| Default is [ true ]
					
					surrounding_area_detection_size = 8
					| Set size of the detectors, for all detectors at 8 directions.
					| Default is [ 8 ]
					
					waterside_detection = true
					| ###
					| Default is [ true ]
					
					surface_smoothness_detection = true
					| Control the trees to only spawn on flat areas. Surrounding area detection must be enable to use this feature.
					| Default is [ true ]
					
					surface_smoothness_detection_height = 8
					| Set height of surface smoothness detection. This value for each up and down. If the detector detects that the surface is rough than this height, it will cancel that tree.
					| Default is [ 8 ]
					
					pre_leaf_litter = true
					pre_leaf_litter_chance = 0.1
					pre_leaf_litter_chance_coniferous = 0.05
					| Create leaf litter on ground and water while on world generation. Leaf litter config must be enable to allow this.
					| Default is [ true ] [ 0.25 ] [ 0.1 ]
					
					abscission_world_gen = true
					| ###
					| Default is [ true ]
					
					----------------------------------------------------------------------------------------------------
					Living Tree Mechanics
					----------------------------------------------------------------------------------------------------
					
					living_tree_mechanics = true
					| Enable some special features such as leaf drop and regrowth, leaves litter, drop leaves if their twig is missing, and abscission.
					| Default is [ true ]
					
					living_tree_mechanics_tick = 5
					| How fast in tick of living tree mechanics system. Set to 0 to temporary pause the tick.
					| Default is [ 5 ]
					
					living_tree_mechanics_process_limit = 100
					| How many process for trees to run living tree mechanics system per time. Set to 0 for one time process.
					| Default is [ 100 ]
					
					living_tree_mechanics_simulation = 100
					| Simulate fake tree to slowdown tree process. For example, when I set tree speed for 100 trees. But it's only 1 tree in the area, it will drop and regrow leaves very fast because that's the speed for 100 trees. Set this config will simulate fake tree locations and make that 1 tree slowdown it process like it's 99 trees around it.
					| Default is [ 100 ]
					
					leaf_litter = true
					| Create leaves block on the ground and on water. Disable leaf drop animation to make this instantly create leaves litter instead of create when leaf drop animation touch the ground.
					| Default is [ true ]
					
					leaf_litter_classic = true
					| ###
					| Default is [ true ]
					
					leaf_litter_classic_only = false
					| ###
					| Default is [ false ]
					
					leaf_litter_remover_chance = 0.001
					| Chance of leaves block on ground to disappear per process
					| Default is [ 0.001 ]
					
					leaf_litter_remover_count_limit = 100
					| Count limit of the leaves litter remover
					| Default is [ 100 ]
					
					leaf_drop_animation_chance = 1.0
					| Chance of animation that will appear at leaf drop block on the trees. Other than this chance will be use fast drop without animation.
					| Default is [ 1.0 ]
					
					leaf_drop_animation_count_limit = 500
					| Count limit of leaf drop animation
					| Default is [ 500 ]
					
					leaf_light_level_detection = 7
					| Minimum light level that tree leaves can survive, leaves will drop themselves if light level is under this value. Set to 15 for only full bright level. Set to 0 for no light level affect.
					| Default is [ 7 ]
					
					leaf_light_level_detection_drop_chance = 0.1
					| Chance of leaves to drop when light level is lower than config
					| Default is [ 0.1 ]
					
					deciduous_leaves_list = minecraft:oak_leaves / minecraft:birch_leaves
					coniferous_leaves_list = minecraft:spruce_leaves
					| List of deciduous and coniferous leaves blocks. Deciduous is oak trees, and similar. They will drop their leaves before winter, but note that they won't do in tropical biomes. Coniferous is pine trees. They will drop their leaves only in summer, and almost rare.
					| Default is [ minecraft:oak_leaves / minecraft:birch_leaves ] [ minecraft:spruce_leaves ]
					
					----------------------------------------------------------------------------------------------------
					Living Tree Mechanics : Season Settings
					----------------------------------------------------------------------------------------------------
					
					serene_seasons_compatibility = true
					| Sync the mod seasons with Serene Seasons mod. Using area at world spawn to detect current season.
					| Default is [ true ]
					
					leaf_drop_chance_spring = 0.0
					leaf_drop_chance_summer = 0.05
					leaf_drop_chance_autumn = 0.1
					leaf_drop_chance_winter = 0.1
					leaf_regrowth_chance_spring = 0.05
					leaf_regrowth_chance_summer = 0.1
					leaf_regrowth_chance_autumn = 0.0
					leaf_regrowth_chance_winter = 0.0
					| Chance of deciduous leaves to drop and regrow based on seasons. But note that it will only use summer value when in tropical biomes. For general leaves that not marked as deciduous, will use summer value.
					| Default is [ 0.0 ] [ 0.05 ] [ 0.1 ] [ 0.1 ] [ 0.05 ] [ 0.1 ] [ 0.0 ] [ 0.0 ]
					
					leaf_drop_chance_coniferous = 0.001
					leaf_regrowth_chance_coniferous = 0.005
					| Chance of coniferous leaves to drop in summer and regrow in any season
					| Default is [ 0.005 ] [ 0.001 ]
					
					----------------------------------------------------------------------------------------------------
					Tree Generator : Performance
					----------------------------------------------------------------------------------------------------
					
					global_speed_enable = true
					| When true, it will change generator speed of all trees from saplings to the same.
					| Default is [ true ]
					
					global_speed = 1
					| How fast of generator speed in tick. Increase this will make it slower. Set to 0 for temporary pause all trees.
					| Default is [ 1 ]
					
					global_speed_repeat = 1000
					| This make generator repeats it process in one time of it speed. Increase this will make it generate faster but also cause lag. Set to 0 for one time generation that can freeze the game.
					| Default is [ 1000 ]
					
					global_speed_tp = 0
					| This option change how many block the parts generator will teleport in a time. Increase this can cause lag because it teleports too fast. Set to 0 for no limit.
					| Default is [ 0 ]
					
					count_limit = 1
					| How many trees will generate in the same time. Set to 0 for no limit.
					| Default is [ 1 ]
					
					----------------------------------------------------------------------------------------------------
					Tree Generator : Quality
					----------------------------------------------------------------------------------------------------
					
					square_parts = false
					| Make wood of all trees from saplings and auto gen become cube, or that I called "Low Detail Mode". Make the generation more faster, but become unrealistic.
					| Default is [ false ]
					
					square_leaves = false
					| Make leaves of all trees from saplings and auto gen become cube, or that I called "Low Detail Mode". Make the generation more faster, but become unrealistic.
					| Default is [ false ]
					
					rt_roots = 100
					| Change percent of roots length for all trees from saplings and auto gen. Set to 0 will disable roots from generation.
					| Default is [ 100 ]
					
					no_core = false
					| Disable core and inner for all trees from saplings and auto gen. Can increase speed of tree generation.
					| Default is [ false ]
					
					----------------------------------------------------------------------------------------------------
					Misc
					----------------------------------------------------------------------------------------------------
					
					developer_mode = false
					| Enable some features for debugging
					| Default is [ false ]
					
					----------------------------------------------------------------------------------------------------
					"""

			);

		}

		FileManager.writeConfigTXT(Handcode.directory_config + "/config.txt", write.toString());

	}

	public static void apply (LevelAccessor level_accessor) {

		String path = Handcode.directory_config + "/config.txt";

		auto_check_update = FileManager.GetConfigValue.logic(path, "auto_check_update");
		auto_update = FileManager.GetConfigValue.logic(path, "auto_update");
		wip_version = FileManager.GetConfigValue.logic(path, "wip_version");

		region_scan_chance = FileManager.GetConfigValue.numberDouble(path, "region_scan_chance");
		multiply_rarity = FileManager.GetConfigValue.numberDouble(path, "multiply_rarity");
		multiply_min_distance = FileManager.GetConfigValue.numberDouble(path, "multiply_min_distance");
		multiply_group_size = FileManager.GetConfigValue.numberDouble(path, "multiply_group_size");
		multiply_waterside_chance = FileManager.GetConfigValue.numberDouble(path, "multiply_waterside_chance");
		multiply_dead_tree_chance = FileManager.GetConfigValue.numberDouble(path, "multiply_dead_tree_chance");
		tree_location = FileManager.GetConfigValue.logic(path, "tree_location");
		world_gen_roots = FileManager.GetConfigValue.logic(path, "world_gen_roots");
		surrounding_area_detection = FileManager.GetConfigValue.logic(path, "surrounding_area_detection");
		surrounding_area_detection_size = FileManager.GetConfigValue.numberInt(path, "surrounding_area_detection_size");
		waterside_detection = FileManager.GetConfigValue.logic(path, "waterside_detection");
		surface_smoothness_detection = FileManager.GetConfigValue.logic(path, "surface_smoothness_detection");
		surface_smoothness_detection_height = FileManager.GetConfigValue.numberInt(path, "surface_smoothness_detection_height");
		pre_leaf_litter = FileManager.GetConfigValue.logic(path, "pre_leaf_litter");
		pre_leaf_litter_chance = FileManager.GetConfigValue.numberDouble(path, "pre_leaf_litter_chance");
		pre_leaf_litter_chance_coniferous = FileManager.GetConfigValue.numberDouble(path, "pre_leaf_litter_chance_coniferous");
		abscission_world_gen = FileManager.GetConfigValue.logic(path, "abscission_world_gen");

		living_tree_mechanics = FileManager.GetConfigValue.logic(path, "living_tree_mechanics");
		living_tree_mechanics_tick = FileManager.GetConfigValue.numberInt(path, "living_tree_mechanics_tick");
		living_tree_mechanics_process_limit = FileManager.GetConfigValue.numberInt(path, "living_tree_mechanics_process_limit");
		living_tree_mechanics_simulation = FileManager.GetConfigValue.numberInt(path, "living_tree_mechanics_simulation");
		leaf_litter = FileManager.GetConfigValue.logic(path, "leaf_litter");
		leaf_litter_classic = FileManager.GetConfigValue.logic(path, "leaf_litter_classic");
		leaf_litter_classic_only = FileManager.GetConfigValue.logic(path, "leaf_litter_classic_only");
		leaf_litter_remover_chance = FileManager.GetConfigValue.numberDouble(path, "leaf_litter_remover_chance");
		leaf_litter_remover_count_limit = FileManager.GetConfigValue.numberInt(path, "leaf_litter_remover_count_limit");
		leaf_drop_animation_chance = FileManager.GetConfigValue.numberDouble(path, "leaf_drop_animation_chance");
		leaf_drop_animation_count_limit = FileManager.GetConfigValue.numberInt(path, "leaf_drop_animation_count_limit");
		leaf_light_level_detection = FileManager.GetConfigValue.numberInt(path, "leaf_light_level_detection");
		leaf_light_level_detection_drop_chance = FileManager.GetConfigValue.numberDouble(path, "leaf_light_level_detection_drop_chance");
		deciduous_leaves_list = new HashSet<>(Arrays.asList(FileManager.GetConfigValue.text(path, "deciduous_leaves_list").split(" / ")));
		coniferous_leaves_list = new HashSet<>(Arrays.asList(FileManager.GetConfigValue.text(path, "coniferous_leaves_list").split(" / ")));

		serene_seasons_compatibility = FileManager.GetConfigValue.logic(path, "serene_seasons_compatibility");
		leaf_drop_chance_spring = FileManager.GetConfigValue.numberDouble(path, "leaf_drop_chance_spring");
		leaf_drop_chance_summer = FileManager.GetConfigValue.numberDouble(path, "leaf_drop_chance_summer");
		leaf_drop_chance_autumn = FileManager.GetConfigValue.numberDouble(path, "leaf_drop_chance_autumn");
		leaf_drop_chance_winter = FileManager.GetConfigValue.numberDouble(path, "leaf_drop_chance_winter");
		leaf_regrowth_chance_spring = FileManager.GetConfigValue.numberDouble(path, "leaf_regrowth_chance_spring");
		leaf_regrowth_chance_summer = FileManager.GetConfigValue.numberDouble(path, "leaf_regrowth_chance_summer");
		leaf_regrowth_chance_autumn = FileManager.GetConfigValue.numberDouble(path, "leaf_regrowth_chance_autumn");
		leaf_regrowth_chance_winter = FileManager.GetConfigValue.numberDouble(path, "leaf_regrowth_chance_winter");
		leaf_drop_chance_coniferous = FileManager.GetConfigValue.numberDouble(path, "leaf_drop_chance_coniferous");
		leaf_regrowth_chance_coniferous = FileManager.GetConfigValue.numberDouble(path, "leaf_regrowth_chance_coniferous");

		global_speed_enable = FileManager.GetConfigValue.logic(path, "global_speed_enable");
		global_speed = FileManager.GetConfigValue.numberInt(path, "global_speed");
		global_speed_repeat = FileManager.GetConfigValue.numberInt(path, "global_speed_repeat");
		global_speed_tp = FileManager.GetConfigValue.numberInt(path, "global_speed_tp");
		count_limit = FileManager.GetConfigValue.numberInt(path, "count_limit");

		square_parts = FileManager.GetConfigValue.logic(path, "square_parts");
		square_leaves = FileManager.GetConfigValue.logic(path, "square_leaves");
		rt_roots = FileManager.GetConfigValue.numberInt(path, "rt_roots");
		no_core = FileManager.GetConfigValue.logic(path, "no_core");

		developer_mode = FileManager.GetConfigValue.logic(path, "developer_mode");

		// After Applying
		{

			if (wip_version == true) {

				Handcode.tanny_pack_version_name = "WIP";

			} else {

				Handcode.tanny_pack_version_name = Handcode.tanny_pack_version;

			}

		}

		if (level_accessor instanceof ServerLevel level_server) {

			GameUtils.misc.sendChatMessage(level_server, "@a", "gray", "THT : Applied The Config");

		}

	}

	public static void repairAll (LevelAccessor level_accessor) {

		FileManager.createFolder(Handcode.directory_config + "/custom_packs/.organized");
		CustomPackOrganized.start(level_accessor);
		ConfigMain.repair();
		ConfigWorldGen.start();

		if (Handcode.world_active == true) {

			ConfigShapeFileConverter.repair();

		}

		if (level_accessor instanceof ServerLevel level_server) {

			GameUtils.misc.sendChatMessage(level_server, "@a", "gray", "THT : Repaired The Config");

		}

	}
	
}