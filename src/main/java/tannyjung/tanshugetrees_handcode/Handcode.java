package tannyjung.tanshugetrees_handcode;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.storage.LevelResource;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.server.ServerLifecycleEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.registries.DeferredRegister;
import tannyjung.core.GameUtils;
import tannyjung.tanshugetrees.TanshugetreesMod;
import tannyjung.tanshugetrees.network.TanshugetreesModVariables;
import tannyjung.tanshugetrees_handcode.config.CheckUpdateRun;
import tannyjung.tanshugetrees_handcode.config.ConfigMain;
import tannyjung.tanshugetrees_handcode.config.ConfigRepairAll;
import tannyjung.tanshugetrees_handcode.systems.Loop;
import tannyjung.tanshugetrees_handcode.systems.living_tree_mechanics.SeasonDetector;
import tannyjung.tanshugetrees_handcode.systems.world_gen.WorldGenFeature;

@Mod.EventBusSubscriber
public class Handcode {

	// ----------------------------------------------------------------------------------------------------

	public static double data_structure_version_pack = 1.3;
	public static String tanny_pack_version = "Alpha";

	public static boolean version_1192 = false;

	// ----------------------------------------------------------------------------------------------------

	public static String directory_game = FMLPaths.GAMEDIR.get().toString();
	public static String directory_config = directory_game + "/config/tanshugetrees";
	public static String directory_world_data = directory_game + "/saves/tanshugetrees-error/directory_world_data";
	public static String directory_world_generated = directory_game + "/saves/tanshugetrees-error/directory_world_generated";
	public static String tanny_pack_version_name = ""; // Make this because version can swap to "WIP" by config

	public Handcode () {}

	public static void startGame () {

		// // Basic Registries
		{

			IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
			DeferredRegister<Feature<?>> REGISTRY = DeferredRegister.create(Registries.FEATURE, TanshugetreesMod.MODID);
			REGISTRY.register("world_gen_feature", WorldGenFeature::new);
			REGISTRY.register(bus);

		}

		ConfigRepairAll.start(null, false);
		ConfigMain.apply(null);

	}

	@SubscribeEvent
	public static void startWorld (ServerLifecycleEvent event) {

		TanshugetreesModVariables.MapVariables.get(event.getServer().overworld()).version_1192 = version_1192;

		LevelAccessor level = event.getServer().overworld();
		String world_path = String.valueOf(event.getServer().getWorldPath(new LevelResource(".")));
		directory_world_data = world_path + "/data/tanshugetrees";
		directory_world_generated = world_path + "/generated/tanshugetrees";

		ConfigRepairAll.start(level, false);
		ConfigMain.apply(level);

	}

	@SubscribeEvent
	public static void playerJoin (PlayerEvent.PlayerLoggedInEvent event) {

		LevelAccessor level = event.getEntity().level();

		if (GameUtils.misc.playerCount(level) == 1) {

			Loop.start(level);
			GameUtils.command.run(level, 0, 0, 0, "scoreboard objectives add TANSHUGETREES dummy");

			// Season Detector
			{

				if (ConfigMain.serene_seasons_compatibility == true && ModList.get().isLoaded("sereneseasons") == true) {

					SeasonDetector.start(level);

				}

			}

			TanshugetreesMod.queueServerWork(100, () -> {

				if (ConfigMain.auto_check_update == true) {

					CheckUpdateRun.start(level);

				}

			});

		}

	}

}
