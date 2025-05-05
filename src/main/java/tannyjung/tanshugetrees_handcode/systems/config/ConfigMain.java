package tannyjung.tanshugetrees_handcode.systems.config;

import net.minecraft.world.level.LevelAccessor;
import tannyjung.tanshugetrees.TanshugetreesMod;
import tannyjung.tanshugetrees_handcode.Handcode;
import tannyjung.tanshugetrees_handcode.misc.FileManager;
import tannyjung.tanshugetrees_handcode.misc.GameUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class ConfigMain {

	public static boolean auto_check_update = false;
	public static boolean auto_update = false;
	public static boolean wip_version = false;

	public static double region_scan_percentage = 0.0;
	public static boolean tree_location = false;
	public static boolean world_gen_roots = false;
	public static boolean surface_smoothness_detection = false;
	public static boolean waterside_detection = false;
	public static int surrounding_area_detection_size = 0;
	public static int surface_smoothness_detection_height = 0;
	public static boolean pre_leaves_litter = false;
	public static double pre_leaves_litter_chance = 0.0;
	public static double pre_leaves_litter_chance_coniferous = 0.0;
	public static boolean pre_leaves_litter_classic = false;
	public static boolean pre_leaves_litter_classic_only = false;
	public static boolean abscission_world_gen = false;

	public static boolean living_tree_mechanics = false;
	public static int living_tree_mechanics_tick = 0;
	public static int living_tree_mechanics_process_limit = 0;
	public static int living_tree_mechanics_simulation = 0;
	public static boolean season_detector = false;
	public static boolean leaf_litter = false;
	public static double leaf_litter_remover_chance = 0.0;
	public static int leaf_litter_remover_count_limit = 0;
	public static double leaf_drop_animation_chance = 0.0;
	public static int leaf_drop_animation_count_limit = 0;
	public static int leaf_light_level_detection = 0;

	public static double leaves_drop_chance_spring = 0;
	public static double leaves_drop_chance_summer = 0.0;
	public static double leaves_drop_chance_autumn = 0.0;
	public static double leaves_drop_chance_winter = 0.0;
	public static double leaves_regrow_chance_spring = 0.0;
	public static double leaves_regrow_chance_summer = 0.0;
	public static double leaves_regrow_chance_autumn = 0;
	public static double leaves_regrow_chance_winter = 0;
	public static double leaves_regrow_chance_coniferous = 0.0;
	public static double leaves_drop_chance_coniferous = 0.0;

	public static boolean global_speed_enable = false;
	public static int global_speed = 0;
	public static int global_speed_repeat = 0;
	public static int global_speed_tp = 0;
	public static int count_limit = 0;
	public static int distance_limit = 0;

	public static boolean square_parts = false;
	public static boolean square_leaves = false;
	public static int rt_roots = 0;
	public static boolean no_core = false;

	public static boolean developer_mode = false;
	public static boolean fireworks = false;

	public static void repair () {

		StringBuilder write = new StringBuilder();

		{

			write.append("""
					- To apply this config, run this command [ /TANSHUGETREES config apply ] or restart the world.
					- To repair missing values, run this command [ /TANSHUGETREES config repair ] or restart the world.
					
					----------------------------------------------------------------------------------------------------
					TannyJung's Tree Pack
					----------------------------------------------------------------------------------------------------
					
					auto_check_update = true
					| Check for the new update from GitHub every time the world starts
					| Default is true
					
					auto_update = false
					| Auto update the pack every time the world starts, if there's a new update from GitHub. To use this feature, the "auto_check_update" config must be enable.
					| Default is false
					
					wip_version = false
					| Use WIP version instead of release version. It's development version before full version, may contain new trees that still work in progress. Warning that not recommended for game play.
					| Default is false
					
					----------------------------------------------------------------------------------------------------
					World Generation
					----------------------------------------------------------------------------------------------------
					
					region_scan_percentage = 100
					| Only support number between 0 to 100. Set to 100 means make them can generate in every chunks, set to 50 for reduce by half, set to 0 to disable all.
					| Default is 100
					
					tree_location = true
					| This is for store some tree data and for some custom features. Disable this will remove tree location after world gen. Can reduce entity lag, but some features will not work such as RT dynamic, leaves drop and regrow.
					| Default is true
					
					world_gen_roots = true
					| Enable tree roots in world gen. Note that disable this feature will not affect to some trees and nature stuffs because roots is important part for them, also not affect to tree taproot part.
					| Default is true
					
					surface_smoothness_detection = true
					| aaa
					| Default is true
					
					waterside_detection = true
					| aaa
					| Default is true
					
					surrounding_area_detection_size = 10
					| Set size of the detectors, for all detectors at 8 directions. Set to 0 will disable this feature.
					| Default is 10
					
					surface_smoothness_detection_height = 10
					| Set height of the detector, this value for each up and down. If the detector detects that the surface is rough than this height, it will cancel the tree.
					| Default is 10
					
					pre_leaf_litter = true
					| aaa
					| Default is true
					
					pre_leaf_litter_chance = 0.25
					| Create leaf litter for normal leaves on the ground or water while world generation
					| Default is 0.25
					
					pre_leaf_litter_chance_coniferous = 0.05
					| Create leaf litter for coniferous leaves on the ground or water while world generation
					| Default is 0.1
					
					pre_leaf_litter_classic = true
					| aaa
					| Default is true
					
					pre_leaf_litter_classic_only = false
					| aaa
					| Default is false
					
					abscission_world_gen = true
					| aaa
					| Default is true
					
					
					----------------------------------------------------------------------------------------------------
					Living Tree Mechanics
					----------------------------------------------------------------------------------------------------
					
					living_tree_mechanics = true
					| Enable some special features such as leaves drop and regrow, leaves litter, drop leaves if their twig is missing, and abscission.
					| Default is true
					
					living_tree_mechanics_tick = 5
					| How fast of RT dynamic system per tick. Set to 0 to temporary pause the tick.
					| Default is 5
					
					living_tree_mechanics_process_limit = 100
					| How many process for trees to run RT dynamic system. Set to 0 for one time process.
					| Default is 100
					
					living_tree_mechanics_simulation = 100
					| Simulate fake tree to slowdown tree process. For example, when I set tree speed for 100 trees. But it's only 1 tree in the area, it will drop and regrow leaves very fast because that's the speed for 100 trees. Set this config will simulate fake tree locations and make that 1 tree slowdown it process like it's 99 trees around it.
					| Default is 100
					
					season_detector = true
					| Use an area from world spawn to detect current season from Serene Seasons mod
					| Default is true
					
					abscission = true
					| wadaw
					| Default is true
					
					leaf_litter = true
					| Create leaves block on the ground and on water. Disable leaves drop animation to make this instantly create leaves little instead of create when leaves drop animation touch the ground, also disable that will use full chance to be leaves litter.
					| Default is true
					
					leaf_litter_remover_chance = 0.001
					| Chance of leaves block on ground to disappear per process. Set to 0 to disable this feature.
					| Default is 0.001
					
					leaf_litter_remover_count_limit = 100
					| Count limit of the leaves litter remover
					| Default is 10
					
					leaf_drop_animation_chance = 0.1
					| Chance of animation that will appear at leaves drop block on the tree
					| Default is 0.1
					
					leaf_drop_animation_count_limit = 500
					| Count limit of leaves drop animation
					| Default is 50
					
					leaf_light_level_detection = 7
					| Minimum light level that tree leaves can survive, leaves will drop itself if light level is under this value. Set to 15 for only full bright level. Set to 0 for no light level affect.
					| Default is 7
					
					----------------------------------------------------------------------------------------------------
					Living Tree Mechanics : Leaves Drop and Regrow
					----------------------------------------------------------------------------------------------------
					
					leaves_drop_chance_spring = 0
					| Chance of deciduous leaves to drop in spring
					| Default is 0
					
					leaves_drop_chance_summer = 0.01
					| Chance of deciduous leaves to drop in summer. Also use in normal leaves for all season.
					| Default is 0.01
					
					leaves_drop_chance_autumn = 0.05
					| Chance of deciduous leaves to drop in autumn
					| Default is 0.05
					
					leaves_drop_chance_winter = 0.1
					| Chance of deciduous leaves to drop in winter
					| Default is 0.1
					
					leaves_regrow_chance_spring = 0.01
					| Chance of deciduous leaves to regrow in spring
					| Default is 0.01
					
					leaves_regrow_chance_summer = 0.05
					| Chance of deciduous leaves to regrow in summer. Also use in normal leaves for all season.
					| Default is 0.05
					
					leaves_regrow_chance_autumn = 0
					| Chance of deciduous leaves to regrow in autumn
					| Default is 0
					
					leaves_regrow_chance_winter = 0
					| Chance of deciduous leaves to regrow in winter
					| Default is 0
					
					leaves_regrow_chance_coniferous = 0.005
					| Chance of coniferous leaves to regrow in all season
					| Default is 0.005
					
					leaves_drop_chance_coniferous = 0.001
					| Chance of coniferous leaves to drop in summer
					| Default is 0.001
					
					----------------------------------------------------------------------------------------------------
					Tree Generator : Performance
					----------------------------------------------------------------------------------------------------
					
					global_speed_enable = true
					| When true, it will change generator speed of all trees from saplings to the same.
					| Default is true
					
					global_speed = 1
					| How fast of generator speed in tick. Increase this will make it slower. Set to 0 for temporary pause all trees.
					| Default is 1
					
					global_speed_repeat = 1000
					| This make generator repeats it process in one time of it speed. Increase this will make it generate faster but also cause lag. Set to 0 for one time generation that can freeze the game.
					| Default is 10
					
					global_speed_tp = 0
					| This option change how many block the parts generator will teleport in a time. Increase this can cause lag because it teleports too fast. Set to 0 for no limit.
					| Default is 0
					
					count_limit = 1
					| How many trees will generate in the same time. Set to 0 for no limit.
					| Default is 1
					
					distance_limit = 0
					| How far the trees will process. Set to 0 for no limit.
					| Default is 500
					
					----------------------------------------------------------------------------------------------------
					Tree Generator = Quality
					----------------------------------------------------------------------------------------------------
					
					square_parts = false
					| Make wood of all trees from saplings and auto gen become cube, or that I called "Low Detail Mode". Make the generation more faster, but become unrealistic.
					| Default is false
					
					square_leaves = false
					| Make leaves of all trees from saplings and auto gen become cube, or that I called "Low Detail Mode". Make the generation more faster, but become unrealistic.
					| Default is false
					
					rt_roots = 100
					| Change percent of roots length for all trees from saplings and auto gen. Set to 0 will disable roots from generation.
					| Default is 100
					
					no_core = false
					| Disable core and inner for all trees from saplings and auto gen. Can increase speed of tree generation.
					| Default is false
					
					----------------------------------------------------------------------------------------------------
					Misc
					----------------------------------------------------------------------------------------------------
					
					developer_mode = false
					| Enable some features for debugging
					| Default is false
					
					fireworks = true
					| Summon fireworks when tree from sapling finishes generating
					| Default is true
					
					----------------------------------------------------------------------------------------------------
					"""

			);

		}

		FileManager.writeConfigTXT(Handcode.directory_config + "/config.txt", write.toString());

	}

	public static void apply (LevelAccessor level) {

		GameUtils.sendChatMessage(level, "@a", "gray", "THT : Applied The Config");

		auto_check_update = Get.logic("auto_check_update");
		auto_update = Get.logic("auto_update");
		wip_version = Get.logic("wip_version");

		// WIP Version Applying
		{

			if (wip_version == true) {

				Handcode.tanny_pack_version_name = "WIP";

			} else {

				Handcode.tanny_pack_version_name = Handcode.tanny_pack_version;

			}

		}

		region_scan_percentage = Get.numberDouble("region_scan_percentage");
		tree_location = Get.logic("tree_location");
		world_gen_roots = Get.logic("config_world_gen_roots");
		surface_smoothness_detection = Get.logic("surface_smoothness_detection");
		waterside_detection = Get.logic("waterside_detection");
		surrounding_area_detection_size = Get.numberInt("surrounding_area_detection_size");
		surface_smoothness_detection_height = Get.numberInt("surface_smoothness_detection_height");
		pre_leaves_litter = Get.logic("pre_leaf_litter");
		pre_leaves_litter_chance = Get.numberDouble("pre_leaf_litter_chance");
		pre_leaves_litter_chance_coniferous = Get.numberDouble("pre_leaf_litter_chance_coniferous");
		pre_leaves_litter_classic = Get.logic("pre_leaf_litter_classic");
		pre_leaves_litter_classic_only = Get.logic("pre_leaf_litter_classic_only");
		abscission_world_gen = Get.logic("abscission_world_gen");

		living_tree_mechanics = Get.logic("living_tree_mechanics");
		living_tree_mechanics_tick = Get.numberInt("living_tree_mechanics_tick");
		living_tree_mechanics_process_limit = Get.numberInt("living_tree_mechanics_process_limit");
		living_tree_mechanics_simulation = Get.numberInt("living_tree_mechanics_simulation");
		season_detector = Get.logic("season_detector");
		leaf_litter = Get.logic("leaf_litter");
		leaf_litter_remover_chance = Get.numberDouble("leaf_litter_remover_chance");
		leaf_litter_remover_count_limit = Get.numberInt("leaf_litter_remover_count_limit");
		leaf_drop_animation_chance = Get.numberDouble("leaf_drop_animation_chance");
		leaf_drop_animation_count_limit = Get.numberInt("leaf_drop_animation_count_limit");
		leaf_light_level_detection = Get.numberInt("leaf_light_level_detection");

		leaves_drop_chance_spring = Get.numberDouble("leaves_drop_chance_spring");
		leaves_drop_chance_summer = Get.numberDouble("leaves_drop_chance_summer");
		leaves_drop_chance_autumn = Get.numberDouble("leaves_drop_chance_autumn");
		leaves_drop_chance_winter = Get.numberDouble("leaves_drop_chance_winter");
		leaves_regrow_chance_spring = Get.numberDouble("leaves_regrow_chance_spring");
		leaves_regrow_chance_summer = Get.numberDouble("leaves_regrow_chance_summer");
		leaves_regrow_chance_autumn = Get.numberDouble("leaves_regrow_chance_autumn");
		leaves_regrow_chance_winter = Get.numberDouble("leaves_regrow_chance_winter");
		leaves_regrow_chance_coniferous = Get.numberDouble("leaves_regrow_chance_coniferous");
		leaves_drop_chance_coniferous = Get.numberDouble("leaves_drop_chance_coniferous");

		global_speed_enable = Get.logic("global_speed_enable");
		global_speed = Get.numberInt("global_speed");
		global_speed_repeat = Get.numberInt("global_speed_repeat");
		global_speed_tp = Get.numberInt("global_speed_tp");
		count_limit = Get.numberInt("count_limit");
		distance_limit = Get.numberInt("distance_limit");

		square_parts = Get.logic("square_parts");
		square_leaves = Get.logic("square_leaves");
		rt_roots = Get.numberInt("rt_roots");
		no_core = Get.logic("no_core");

		developer_mode = Get.logic("developer_mode");
		fireworks = Get.logic("fireworks");

	}
	
	private static class Get {

		private static boolean logic (String name) {

			boolean return_logic = false;
			String get = read(name);

			if (get.equals("") == false) {

				return_logic = Boolean.parseBoolean(get);

			}

			return return_logic;

		}

		private static int numberInt (String name) {

			int return_number = 0;
			String get = read(name);

			if (get.equals("") == false) {

				return_number = Integer.parseInt(get);

			}

			return return_number;

		}

		private static double numberDouble (String name) {

			double return_number = 0.0;
			String get = read(name);

			if (get.equals("") == false) {

				return_number = Double.parseDouble(get);

			}

			return return_number;

		}

		private static String read (String name) {

			String return_text = "";
			name = name + " = ";

			{

				File file = new File(Handcode.directory_config + "/config.txt");

				{

					try { BufferedReader buffered_reader = new BufferedReader(new FileReader(file)); String read_all = ""; while ((read_all = buffered_reader.readLine()) != null) {

						{

							if (read_all.startsWith("|") == false) {

								if (read_all.startsWith(name) == true) {

									return_text = read_all.replace(name, "");
									break;

								}

							}

						}

					} buffered_reader.close(); } catch (Exception e) { TanshugetreesMod.LOGGER.error(e.getMessage()); }

				}

			}

			return return_text;

		}

	}
	
}