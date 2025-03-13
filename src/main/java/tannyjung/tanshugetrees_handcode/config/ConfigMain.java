package tannyjung.tanshugetrees_handcode.config;

import tannyjung.tanshugetrees_handcode.Handcode;
import tannyjung.tanshugetrees_handcode.misc.FileManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class ConfigMain {

	public static boolean auto_check_update = false;
	public static boolean auto_update = false;
	public static boolean wip_version = false;

	public static double global_rarity = 0.0;
	public static boolean tree_location = false;
	public static boolean world_gen_roots = false;
	public static int surface_smoothness_detection_size = 0;
	public static int surface_smoothness_detection_height = 0;

	public static boolean rt_dynamic = false;
	public static int rt_dynamic_tick = 0;
	public static int rt_dynamic_process_limit = 0;
	public static int rt_dynamic_simulation = 0;
	public static boolean season_detector = false;
	public static boolean leaves_litter = false;
	public static double leaves_litter_remover_chance = 0.0;
	public static int leaves_litter_remover_count_limit = 0;
	public static double pre_leaves_litter_chance = 0.0;
	public static double pre_leaves_litter_coniferous_chance = 0.0;
	public static double leaves_drop_animation_chance = 0.0;
	public static int leaves_drop_animation_count_limit = 0;
	public static int leaves_light_level_detection = 0;

	public static double leaves_regrow_spring_chance = 0.0;
	public static double leaves_regrow_summer_chance = 0.0;
	public static double leaves_regrow_autumn_chance = 0;
	public static double leaves_regrow_winter_chance = 0;
	public static double leaves_drop_spring_chance = 0;
	public static double leaves_drop_summer_chance = 0.0;
	public static double leaves_drop_autumn_chance = 0.0;
	public static double leaves_drop_winter_chance = 0.0;
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
			- Apply the changes by run this command [ /THT config apply ] or restart the world
			- Repair missing values by run this command [ /THT config repair ] or restart the world
			----------------------------------------------------------------------------------------------------
			TannyJung's Tree Pack
			----------------------------------------------------------------------------------------------------
			
			auto_check_update = true
			// Check for the new update from GitHub every time the world starts
			// Default is true
			
			auto_update = true
			// Auto update the pack every time the world starts, if there's a new update from GitHub. To use this feature, the "auto_check_update" config must be enable.
			// Default is false
			
			wip_version = true
			// Use WIP version instead of release version. It's development version before full version, may contain new trees that still work in progress. Warning that not recommended for game play.
			// Default is false
			
			----------------------------------------------------------------------------------------------------
			Tree Placer & World Gen
			----------------------------------------------------------------------------------------------------
		
			global_rarity = 100
			// Change all trees rarity with one config. Only support number between 0 to 100. Set to 100 means make them can generate in every chunks, set to 50 for reduce by half, set to 0 to disable all.
			// Default is 100
			
			tree_location = true
			// This is for store some tree data and for some custom features. Disable this will remove tree location after world gen. Can reduce entity lag, but some features will not work such as RT dynamic, leaves drop and regrow.
			// Default is true
			
			world_gen_roots = true
			// Enable tree roots in world gen. Note that disable this feature will not affect to some trees and nature stuffs because roots is important part for them, also not affect to tree taproot part.
			// Default is true
			
			surface_smoothness_detection_size = 10
			// Set size of the detector, for all detectors at 8 directions. Set to 0 to disable this feature.
			// Default is 5
			
			surface_smoothness_detection_height = 5
			// Set height of the detector, this value for each up and down. If the detector detects that the surface is rough than this height, it will cancel the tree.
			// Default is 5
			
			----------------------------------------------------------------------------------------------------
			RT Dynamic
			----------------------------------------------------------------------------------------------------
			
			rt_dynamic = true
			// Enable some special features such as leaves drop and regrow, leaves litter, drop leaves if their twig is missing and deciduous leaves (drop leaves before winter).
			// Default is true
			
			rt_dynamic_tick = 5
			// How fast of RT dynamic system per tick. Set to 0 to temporary pause the tick.
			// Default is 5
			
			rt_dynamic_process_limit = 100
			// How many process for trees to run RT dynamic system. Set to 0 for one time process.
			// Default is 100
			
			rt_dynamic_simulation = 50
			// Simulate fake tree to slowdown tree process. For example, when I set tree speed for 100 trees. But it's only 1 tree in the area, it will drop and regrow leaves very fast because that's the speed for 100 trees. Set this config will simulate fake tree locations and make that 1 tree slowdown it process like it's 99 trees around it.
			// Default is 50
			
			season_detector = true
			// Use an area from world spawn to detect current season from Serene Seasons mod
			// Default is true
			
			leaves_litter = true
			// Create leaves block on the ground and on water. Disable leaves drop animation to make this instantly create leaves little instead of create when leaves drop animation touch the ground, also disable that will use full chance to be leaves litter.
			// Default is true
			
			leaves_litter_remover_chance = 0.001
			// Chance of leaves block on ground to disappear per process. Set to 0 to disable this feature.
			// Default is 0.001
			
			leaves_litter_remover_count_limit = 10
			// Count limit of the leaves litter remover
			// Default is 10
			
			pre_leaves_litter_chance = 0.25
			// Create leaves litter for normal leaves on the ground and on water while on worldgen
			// Default is 0.25
			
			pre_leaves_litter_coniferous_chance = 0.05
			// Create leaves litter for coniferous leaves on the ground and on water while on worldgen
			// Default is 0.05
			
			leaves_drop_animation_chance = 0.1
			// Chance of animation that will appear at leaves drop block on the tree
			// Default is 0.1
			
			leaves_drop_animation_count_limit = 50
			// Count limit of leaves drop animation
			// Default is 50
			
			leaves_light_level_detection = 7
			// Minimum light level that tree leaves can survive, leaves will not generate and drop self if light level is under this value. Set to 15 for only full bright level. Set to 0 for no light level affect.
			// Default is 7
			
			----------------------------------------------------------------------------------------------------
			RT Dynamic : Leaves Drop and Regrow
			----------------------------------------------------------------------------------------------------
			
			leaves_regrow_spring_chance = 0.01
			// Chance of deciduous leaves to regrow in spring
			// Default is 0.01
			
			leaves_regrow_summer_chance = 0.05
			// Chance of deciduous leaves to regrow in summer. Also use in normal leaves for all season.
			// Default is 0.05
			
			leaves_regrow_autumn_chance = 0
			// Chance of deciduous leaves to regrow in autumn
			// Default is 0
			
			leaves_regrow_winter_chance = 0
			// Chance of deciduous leaves to regrow in winter
			// Default is 0
			
			leaves_drop_spring_chance = 0
			// Chance of deciduous leaves to drop in spring
			// Default is 0
			
			leaves_drop_summer_chance = 0.01
			// Chance of deciduous leaves to drop in summer. Also use in normal leaves for all season.
			// Default is 0.01
			
			leaves_drop_autumn_chance = 0.05
			// Chance of deciduous leaves to drop in autumn
			// Default is 0.05
			
			leaves_drop_winter_chance = 0.1
			// Chance of deciduous leaves to drop in winter
			// Default is 0.1
			
			leaves_regrow_chance_coniferous = 0.005
			// Chance of coniferous leaves to regrow in all season
			// Default is 0.005
			
			leaves_drop_chance_coniferous = 0.001
			// Chance of coniferous leaves to drop in summer
			// Default is 0.001
			
			----------------------------------------------------------------------------------------------------
			Tree Generator : Performance
			----------------------------------------------------------------------------------------------------
			
			global_speed_enable = true
			// When true, it will change generator speed of all trees from saplings to the same.
			// Default is true
			
			global_speed = 1
			// How fast of generator speed in tick. Increase this will make it slower. Set to 0 for temporary pause all trees.
			// Default is 1
			
			global_speed_repeat = 1000
			// This make generator repeats it process in one time of it speed. Increase this will make it generate faster but also cause lag. Set to 0 for one time generation that can freeze the game.
			// Default is 10
			
			global_speed_tp = 0
			// This option change how many block the parts generator will teleport in a time. Increase this can cause lag because it teleports too fast. Set to 0 for no limit.
			// Default is 0
			
			count_limit = 1
			// How many trees will generate in the same time. Set to 0 for no limit.
			// Default is 1
			
			distance_limit = 0
			// How far the trees will process. Set to 0 for no limit.
			// Default is 500
			
			----------------------------------------------------------------------------------------------------
			Tree Generator = Quality
			----------------------------------------------------------------------------------------------------
			
			square_parts = false
			// Make wood of all trees from saplings and auto gen become cube, or that I called "Low Detail Mode". Make the generation more faster, but become unrealistic.
			// Default is false
			
			square_leaves = false
			// Make leaves of all trees from saplings and auto gen become cube, or that I called "Low Detail Mode". Make the generation more faster, but become unrealistic.
			// Default is false
			
			rt_roots = 100
			// Change percent of roots length for all trees from saplings and auto gen. Set to 0 will disable roots from generation.
			// Default is 100
			
			no_core = false
			// Disable core and inner for all trees from saplings and auto gen. Can increase speed of tree generation.
			// Default is false
			
			----------------------------------------------------------------------------------------------------
			Misc
			----------------------------------------------------------------------------------------------------
			
			developer_mode = false
			// Enable some features for debugging
			// Default is false
			
			fireworks = true
			// Summon fireworks when tree from sapling finishes generating
			// Default is true
			
			----------------------------------------------------------------------------------------------------
			""");

		}

		FileManager.writeConfigTXT(Handcode.directory_config + "/config.txt", write.toString());

	}

	public static void apply () {

		auto_check_update = Get.logic("auto_check_update");
		auto_update = Get.logic("auto_update");
		wip_version = Get.logic("wip_version");
		global_rarity = Get.numberDouble("global_rarity");

		tree_location = Get.logic("tree_location");
		world_gen_roots = Get.logic("config_world_gen_roots");
		surface_smoothness_detection_size = Get.numberInt("surface_smoothness_detection_size");
		surface_smoothness_detection_height = Get.numberInt("surface_smoothness_detection_height");

		rt_dynamic = Get.logic("rt_dynamic");
		rt_dynamic_tick = Get.numberInt("rt_dynamic_tick");
		rt_dynamic_process_limit = Get.numberInt("rt_dynamic_process_limit");
		rt_dynamic_simulation = Get.numberInt("rt_dynamic_simulation");
		season_detector = Get.logic("season_detector");
		leaves_litter = Get.logic("leaves_litter");
		leaves_litter_remover_chance = Get.numberDouble("leaves_litter_remover_chance");
		leaves_litter_remover_count_limit = Get.numberInt("leaves_litter_remover_count_limit");
		pre_leaves_litter_chance = Get.numberDouble("pre_leaves_litter_chance");
		pre_leaves_litter_coniferous_chance = Get.numberDouble("pre_leaves_litter_coniferous_chance");
		leaves_drop_animation_chance = Get.numberDouble("leaves_drop_animation_chance");
		leaves_drop_animation_count_limit = Get.numberInt("leaves_drop_animation_count_limit");
		leaves_light_level_detection = Get.numberInt("leaves_light_level_detection");

		leaves_regrow_spring_chance = Get.numberDouble("leaves_regrow_spring_chance");
		leaves_regrow_summer_chance = Get.numberDouble("leaves_regrow_summer_chance");
		leaves_regrow_autumn_chance = Get.numberDouble("leaves_regrow_autumn_chance");
		leaves_regrow_winter_chance = Get.numberDouble("leaves_regrow_winter_chance");
		leaves_drop_spring_chance = Get.numberDouble("leaves_drop_spring_chance");
		leaves_drop_summer_chance = Get.numberDouble("leaves_drop_summer_chance");
		leaves_drop_autumn_chance = Get.numberDouble("leaves_drop_autumn_chance");
		leaves_drop_winter_chance = Get.numberDouble("leaves_drop_winter_chance");
		leaves_regrow_chance_coniferous = Get.numberDouble("leaves_regrow_chance_coniferous");
		leaves_drop_chance_coniferous = Get.numberDouble("leaves_drop_chance_coniferous");

		global_speed_enable = Get.logic("global_speed_enable");
		global_speed = Get.numberInt("global_speed");
		global_speed_repeat = Get.numberInt("global_speed_repeat");
		global_speed_tp = Get.numberInt("global_speed_tp");
		count_limit = Get.numberInt("count_max");
		distance_limit = Get.numberInt("distance_max");

		square_parts = Get.logic("square_parts");
		square_leaves = Get.logic("square_leaves");
		rt_roots = Get.numberInt("rt_roots");
		no_core = Get.logic("no_core");

		developer_mode = Get.logic("developer_mode");
		fireworks = Get.logic("fireworks");

	}
	
	private static class Get {

		private static boolean logic (String test) {

			boolean return_logic = false;
			File file = new File(Handcode.directory_config + "/config.txt");
			test = test + " = ";

			try {

				BufferedReader buffered_reader = new BufferedReader(new FileReader(file));
				String read_all = "";

				while ((read_all = buffered_reader.readLine()) != null) {

					{

						if (read_all.startsWith(test) == true) {

							return_logic = Boolean.parseBoolean(read_all.replace(test, ""));
							break;

						}

					}

				}

				buffered_reader.close();

			} catch (Exception e) {

				e.printStackTrace();

			}

			return return_logic;

		}

		private static int numberInt(String test) {

			int return_number = 0;
			File file = new File(Handcode.directory_config + "/config.txt");
			test = test + " = ";

			try {

				BufferedReader buffered_reader = new BufferedReader(new FileReader(file));
				String read_all = "";

				while ((read_all = buffered_reader.readLine()) != null) {

					{

						if (read_all.startsWith(test) == true) {

							return_number = Integer.parseInt(read_all.replace(test, ""));
							break;

						}

					}

				}

				buffered_reader.close();

			} catch (Exception e) {

				e.printStackTrace();

			}

			return return_number;

		}

		private static double numberDouble(String test) {

			double return_number = 0;
			File file = new File(Handcode.directory_config + "/config.txt");
			test = test + " = ";

			try {

				BufferedReader buffered_reader = new BufferedReader(new FileReader(file));
				String read_all = "";

				while ((read_all = buffered_reader.readLine()) != null) {

					{

						if (read_all.startsWith(test) == true) {

							return_number = Double.parseDouble(read_all.replace(test, ""));
							break;

						}

					}

				}

				buffered_reader.close();

			} catch (Exception e) {

				e.printStackTrace();

			}

			return return_number;

		}

	}
	
}