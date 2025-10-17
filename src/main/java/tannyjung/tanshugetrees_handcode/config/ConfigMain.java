package tannyjung.tanshugetrees_handcode.config;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.LevelAccessor;
import tannyjung.tanshugetrees_handcode.Handcode;
import tannyjung.core.FileManager;
import tannyjung.core.GameUtils;

import java.util.*;

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
	public static int max_height_spawn = 0;
	public static double unviable_ecology_skip_chance = 0.0;
	public static boolean leaf_litter_world_gen = false;
	public static double leaf_litter_world_gen_chance = 0.0;
	public static double leaf_litter_world_gen_chance_coniferous = 0.0;
	public static boolean abscission_world_gen = false;

	public static boolean surrounding_area_detection = false;
	public static int surrounding_area_detection_size = 0;
	public static boolean waterside_detection = false;
	public static boolean surface_smoothness_detection = false;
	public static int surface_smoothness_detection_height = 0;

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

	public static boolean tree_generator_speed_global = false;
	public static int tree_generator_speed_tick = 0;
	public static int tree_generator_speed_repeat = 0;
	public static int tree_generator_count_limit = 0;

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
					| Check for new update from GitHub every time the world starts
					| Default is [ true ]
					
					auto_update = false
					| Auto update the pack every time the world starts, if there's new update from GitHub. To use this feature, the "auto_check_update" config must be enable.
					| Default is [ false ]
					
					wip_version = false
					| Use development version of the pack, instead of release version. Not recommended for game play, as it's still in development, it might unstable. Sometimes it needed development version of the mod.
					| Default is [ false ]
					
					----------------------------------------------------------------------------------------------------
					World Generation
					----------------------------------------------------------------------------------------------------
					
					region_scan_chance = 1.0
					| Set chance of chunk scan per region, from region pre-location system. One region contains 32x32 chunks, or 1,024 chunks. Lower this can reduce scan time, also lower the chance of all trees.
					| Default is [ 1.0 ]
					
					multiply_rarity = 1.0
					multiply_min_distance = 1.0
					multiply_group_size = 1.0
					multiply_waterside_chance = 1.0
					multiply_dead_tree_chance = 1.0
					| These number will be multiplied to all tree config in these types
					| Default is [ 1.0 For All ]
					
					tree_location = true
					| Enable marker entity for tree location to store some tree data and for some custom features. Disable this can reduce number of entities, but some features will not work, such as living tree mechanics.
					| Default is [ true ]
					
					world_gen_roots = true
					| Enable tree roots when generate in world gen. Note that disable this will no affect to some trees, because roots is important part for them. Also will no affect to taproot part.
					| Default is [ true ]
					
					max_height_spawn = 0
					| Cancel the trees when their spawn center is above this Y level. As some world gen mods such as ReTerraForged, replacing mountain block and my trees can't detect those new block, make them spawn on blocks that not in the list. Set to 0 to disable this.
					| Default is [ 0 ]
					
					unviable_ecology_skip_chance = 0.75
					| Skip trees that generate in unviable ecosystems. For example, land trees that generate in water.
					| Default is [ 0.75 ]
					
					leaf_litter_world_gen = true
					leaf_litter_world_gen_chance = 0.1
					leaf_litter_world_gen_chance_coniferous = 0.05
					| Create leaf litter on ground and water, while in world gen. Leaf litter config must be enable to allow this.
					| Default is [ true ] [ 0.1 ] [ 0.05 ]
					
					abscission_world_gen = true
					| Make all deciduous trees generate with all leaves dropped to the ground, when they are in snowy biomes.
					| Default is [ true ]
					
					----------------------------------------------------------------------------------------------------
					World Generation : Surrounding Area Detection
					----------------------------------------------------------------------------------------------------
					
					surrounding_area_detection = true
					| Enable some custom systems to detect area around that tree before placing it. Such as smoothness surface and near water/land biomes.
					| Default is [ true ]
					
					surrounding_area_detection_size = 8
					| Set size of detectors, for all detectors at 8 directions.
					| Default is [ 8 ]
					
					waterside_detection = true
					| Enable waterside system for trees that use this feature. If disable this, all waterside trees will be skipped and not spawn anywhere.
					| Default is [ true ]
					
					surface_smoothness_detection = true
					| Force the trees to only spawn on flat areas. Note that this system only detects 8 points around that tree, so it's not 100% perfect. Surrounding area detection must be enable to use this feature.
					| Default is [ true ]
					
					surface_smoothness_detection_height = 8
					| Set height of surface smoothness. This value for each up and down. If the detector detects that the surface is rough than this height, it will cancel that tree.
					| Default is [ 8 ]
					
					----------------------------------------------------------------------------------------------------
					Living Tree Mechanics
					----------------------------------------------------------------------------------------------------
					
					living_tree_mechanics = true
					| Enable some custom systems to make the trees from this mod feel more alive. Such as leaf drop and regrowth, leaf decay, leaf litter, and abscission.
					| Default is [ true ]
					
					living_tree_mechanics_tick = 5
					| How fast in tick of living tree mechanics system. Set to 0 to temporary pause the tick.
					| Default is [ 5 ]
					
					living_tree_mechanics_process_limit = 100
					| How many process for trees to run this system per time. Set to 0 for one time process.
					| Default is [ 100 ]
					
					living_tree_mechanics_simulation = 100
					| Simulate fake trees to slowdown the process. For example, when I set tree speed for 100 trees. But there's only 1 tree in the area, it will drop and regrow leaves very fast because that's the speed for 100 trees. Set this config will simulate fake trees and make that 1 tree slowdown it process like there's 99 trees around it.
					| Default is [ 100 ]
					
					leaf_litter = true
					| Create leaf litter on ground and water
					| Default is [ true ]
					
					leaf_litter_classic = true
					| Use classic style for leaf litter when that leaves block have no custom style. Classic style will use full leaves block, not like slab or carpet.
					| Default is [ true ]
					
					leaf_litter_classic_only = false
					| Only use classic style for all leaf litters
					| Default is [ false ]
					
					leaf_litter_remover_chance = 0.001
					| Chance of leaf litter on the ground to disappear per process
					| Default is [ 0.001 ]
					
					leaf_litter_remover_count_limit = 100
					| Count limit of the leaf litter remover
					| Default is [ 100 ]
					
					leaf_drop_animation_chance = 1.0
					| Chance of animation that will appear on the trees. When this leaf animation touch the ground or water, it will create leaf litter there. Other than this chance will be use instant drop without animation.
					| Default is [ 1.0 ]
					
					leaf_drop_animation_count_limit = 500
					| Count limit of leaf drop animation
					| Default is [ 500 ]
					
					leaf_light_level_detection = 7
					| Minimum light level of leaves can survive. Leaves will drop themselves if light level is under this value. Set to 15 for only full bright level. Set to 0 for no light level affect.
					| Default is [ 7 ]
					
					leaf_light_level_detection_drop_chance = 0.1
					| Chance of leaves to drop themselves when light level is under the config
					| Default is [ 0.1 ]
					
					deciduous_leaves_list = minecraft:oak_leaves / minecraft:birch_leaves
					coniferous_leaves_list = minecraft:spruce_leaves
					| List of deciduous and coniferous leaves blocks. Deciduous is oak trees and similar. They will drop their leaves before winter, but note that they will not do that in tropical biomes. Coniferous is pine trees. They will drop their leaves only in summer and almost very rare.
					| Default is [ minecraft:oak_leaves / minecraft:birch_leaves ] [ minecraft:spruce_leaves ]
					
					----------------------------------------------------------------------------------------------------
					Living Tree Mechanics : Season Settings
					----------------------------------------------------------------------------------------------------
					
					serene_seasons_compatibility = true
					| Sync the mod seasons to the same from Serene Seasons mod. Using area at world spawn to detect current season.
					| Default is [ true ]
					
					leaf_drop_chance_spring = 0.0
					leaf_drop_chance_summer = 0.05
					leaf_drop_chance_autumn = 0.1
					leaf_drop_chance_winter = 0.1
					leaf_regrowth_chance_spring = 0.05
					leaf_regrowth_chance_summer = 0.1
					leaf_regrowth_chance_autumn = 0.0
					leaf_regrowth_chance_winter = 0.0
					| Chance of deciduous leaves to drop and regrow based on seasons. But note that it will only use summer value when in tropical biomes, and for other leaves that not marked as deciduous and coniferous.
					| Default is [ 0.0 ] [ 0.05 ] [ 0.1 ] [ 0.1 ] [ 0.05 ] [ 0.1 ] [ 0.0 ] [ 0.0 ]
					
					leaf_drop_chance_coniferous = 0.001
					leaf_regrowth_chance_coniferous = 0.005
					| Chance of coniferous leaves to drop in summer and regrow in any season
					| Default is [ 0.005 ] [ 0.001 ]
					
					----------------------------------------------------------------------------------------------------
					Tree Generator
					----------------------------------------------------------------------------------------------------
					
					tree_generator_speed_global = true
					| When true, it will use same speed for all generators.
					| Default is [ true ]
					
					tree_generator_speed_tick = 1
					| How fast of generators in tick. Increase this will make them slower. Set to 0 for temporary pause all generators.
					| Default is [ 1 ]
					
					tree_generator_speed_repeat = 1000
					| How many processes the generators run in a time. Increase this will make them generate faster but also can cause lag. Set to 0 for one time generation that can freeze the game.
					| Default is [ 1000 ]
					
					tree_generator_count_limit = 1
					| How many generators will generate in the same time. Set to 0 for no limit.
					| Default is [ 1 ]
					
					----------------------------------------------------------------------------------------------------
					Misc
					----------------------------------------------------------------------------------------------------
					
					developer_mode = false
					| Enable some features for debugging. Such as region pre-location info in-game, to see what tree slowdown the pre-location. Tracking the trees to see what tree running the living tree mechanics.
					| Default is [ false ]
					
					----------------------------------------------------------------------------------------------------
					"""

			);

		}

		FileManager.writeConfigTXT(Handcode.directory_config + "/config.txt", write.toString());

	}

	public static void apply (LevelAccessor level_accessor) {

		Map<String, String> data = new HashMap<>();

		// Get Data
		{

			int index = 0;

			for (String read_all : FileManager.fileToStringArray(Handcode.directory_config + "/config.txt")) {

				{

					if (read_all.equals("") == false) {

						if (read_all.contains(" = ") == true) {

							index = read_all.indexOf(" = ");
							data.put(read_all.substring(0, index), read_all.substring(index + 3));

						}

					}

				}

			}

		}

		auto_check_update = Boolean.parseBoolean(data.get("auto_check_update"));
		auto_update = Boolean.parseBoolean(data.get("auto_update"));
		wip_version = Boolean.parseBoolean(data.get("wip_version"));

		region_scan_chance = Double.parseDouble(data.get("region_scan_chance"));
		multiply_rarity = Double.parseDouble(data.get("multiply_rarity"));
		multiply_min_distance = Double.parseDouble(data.get("multiply_min_distance"));
		multiply_group_size = Double.parseDouble(data.get("multiply_group_size"));
		multiply_waterside_chance = Double.parseDouble(data.get("multiply_waterside_chance"));
		multiply_dead_tree_chance = Double.parseDouble(data.get("multiply_dead_tree_chance"));
		tree_location = Boolean.parseBoolean(data.get("tree_location"));
		world_gen_roots = Boolean.parseBoolean(data.get("world_gen_roots"));
		max_height_spawn = Integer.parseInt(data.get("max_height_spawn"));
		unviable_ecology_skip_chance = Double.parseDouble(data.get("unviable_ecology_skip_chance"));
		leaf_litter_world_gen = Boolean.parseBoolean(data.get("leaf_litter_world_gen"));
		leaf_litter_world_gen_chance = Double.parseDouble(data.get("leaf_litter_world_gen_chance"));
		leaf_litter_world_gen_chance_coniferous = Double.parseDouble(data.get("leaf_litter_world_gen_chance_coniferous"));
		abscission_world_gen = Boolean.parseBoolean(data.get("abscission_world_gen"));

		surrounding_area_detection = Boolean.parseBoolean(data.get("surrounding_area_detection"));
		surrounding_area_detection_size = Integer.parseInt(data.get("surrounding_area_detection_size"));
		waterside_detection = Boolean.parseBoolean(data.get("waterside_detection"));
		surface_smoothness_detection = Boolean.parseBoolean(data.get("surface_smoothness_detection"));
		surface_smoothness_detection_height = Integer.parseInt(data.get("surface_smoothness_detection_height"));

		living_tree_mechanics = Boolean.parseBoolean(data.get("living_tree_mechanics"));
		living_tree_mechanics_tick = Integer.parseInt(data.get("living_tree_mechanics_tick"));
		living_tree_mechanics_process_limit = Integer.parseInt(data.get("living_tree_mechanics_process_limit"));
		living_tree_mechanics_simulation = Integer.parseInt(data.get("living_tree_mechanics_simulation"));
		leaf_litter = Boolean.parseBoolean(data.get("leaf_litter"));
		leaf_litter_classic = Boolean.parseBoolean(data.get("leaf_litter_classic"));
		leaf_litter_classic_only = Boolean.parseBoolean(data.get("leaf_litter_classic_only"));
		leaf_litter_remover_chance = Double.parseDouble(data.get("leaf_litter_remover_chance"));
		leaf_litter_remover_count_limit = Integer.parseInt(data.get("leaf_litter_remover_count_limit"));
		leaf_drop_animation_chance = Double.parseDouble(data.get("leaf_drop_animation_chance"));
		leaf_drop_animation_count_limit = Integer.parseInt(data.get("leaf_drop_animation_count_limit"));
		leaf_light_level_detection = Integer.parseInt(data.get("leaf_light_level_detection"));
		leaf_light_level_detection_drop_chance = Double.parseDouble(data.get("leaf_light_level_detection_drop_chance"));
		deciduous_leaves_list = new HashSet<>(List.of(data.get("deciduous_leaves_list").split(" / ")));
		coniferous_leaves_list = new HashSet<>(List.of(data.get("coniferous_leaves_list").split(" / ")));

		serene_seasons_compatibility = Boolean.parseBoolean(data.get("serene_seasons_compatibility"));
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

		tree_generator_speed_global = Boolean.parseBoolean(data.get("tree_generator_speed_global"));
		tree_generator_speed_tick = Integer.parseInt(data.get("tree_generator_speed_tick"));
		tree_generator_speed_repeat = Integer.parseInt(data.get("tree_generator_speed_repeat"));
		tree_generator_count_limit = Integer.parseInt(data.get("tree_generator_count_limit"));

		developer_mode = Boolean.parseBoolean(data.get("developer_mode"));

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

		FileManager.createFolder(Handcode.directory_config + "/#dev");
		FileManager.createFolder(Handcode.directory_config + "/#dev/custom_packs_organized");
		FileManager.createFolder(Handcode.directory_config + "/#dev/shape_file_converter");
		FileManager.createFolder(Handcode.directory_config + "/custom_packs");

		CustomPackOrganized.start(level_accessor);
		ConfigMain.repair();
		ConfigWorldGen.start();
		ConfigShapeFileConverter.repair();

		if (level_accessor instanceof ServerLevel level_server) {

			GameUtils.misc.sendChatMessage(level_server, "@a", "gray", "THT : Repaired The Config");

		}

	}
	
}