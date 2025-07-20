package tannyjung.tanshugetrees_handcode;

import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.storage.LevelResource;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.server.*;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.registries.DeferredRegister;
import tannyjung.core.GameUtils;
import tannyjung.tanshugetrees.TanshugetreesMod;
import tannyjung.tanshugetrees_handcode.config.CustomPackIncompatible;
import tannyjung.tanshugetrees_handcode.config.PackCheckUpdate;
import tannyjung.tanshugetrees_handcode.config.ConfigMain;
import tannyjung.tanshugetrees_handcode.systems.Loop;
import tannyjung.tanshugetrees_handcode.systems.living_tree_mechanics.SeasonDetector;
import tannyjung.tanshugetrees_handcode.systems.world_gen.WorldGenFeature;

import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber
public class Handcode {

	// ----------------------------------------------------------------------------------------------------

	public static double data_structure_version_pack = 1.4;
	public static String tanny_pack_version = "Alpha";

	public static boolean version_1192 = false;

	// ----------------------------------------------------------------------------------------------------

	public static String directory_game = FMLPaths.GAMEDIR.get().toString();
	public static String directory_config = directory_game + "/config/tanshugetrees";
	public static String directory_world_data = directory_game + "/saves/tanshugetrees-error/directory_world_data";
	public static String directory_world_generated = directory_game + "/saves/tanshugetrees-error/directory_world_generated";
	public static String tanny_pack_version_name = ""; // Make this because version can swap to "WIP" by config

	public static boolean world_active = false;

	public Handcode () {}

	public static void startGame () {

		TanshugetreesMod.LOGGER.info("Loading mod registries and config");

		// // Basic Registries
		{

			IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
			DeferredRegister<Feature<?>> REGISTRY = DeferredRegister.create(Registries.FEATURE, TanshugetreesMod.MODID);
			REGISTRY.register("world_gen_feature", WorldGenFeature::new);
			REGISTRY.register(bus);

		}

		CompletableFuture.runAsync(() -> {

			ConfigMain.repairAll(null);
			ConfigMain.apply(null);

		});

	}

	@SubscribeEvent
	public static void worldAboutToStart (ServerAboutToStartEvent event) {

		world_active = true;
		TanshugetreesMod.LOGGER.info("Turned ON world systems");

		String world_path = String.valueOf(event.getServer().getWorldPath(new LevelResource(".")));
		directory_world_data = world_path + "/data/tanshugetrees";
		directory_world_generated = world_path + "/generated/tanshugetrees";

		ConfigMain.repairAll(null);
		ConfigMain.apply(null);

	}

	@SubscribeEvent
	public static void worldStarted (ServerStartedEvent event) {

		LevelAccessor level_accessor = event.getServer().overworld();

		if (level_accessor instanceof ServerLevel level_server) {

			GameUtils.command.run(level_server, 0, 0, 0, "scoreboard objectives add TANSHUGETREES dummy");

			// Season Detector
			{

				if (ConfigMain.serene_seasons_compatibility == true && ModList.get().isLoaded("sereneseasons") == true) {

					SeasonDetector.start(level_server);

				}

			}

			Loop.start(level_accessor, level_server);

		}

	}

	@SubscribeEvent
	public static void worldStopped (ServerStoppingEvent event) {

		world_active = false;
		TanshugetreesMod.LOGGER.info("Turned OFF world systems");

	}

	@SubscribeEvent
	public static void playerJoined (PlayerEvent.PlayerLoggedInEvent event) {

		if (GameUtils.misc.playerCount() == 1) {

			TanshugetreesMod.queueServerWork(100, () -> {

				if (ConfigMain.auto_check_update == true) {

					LevelAccessor level_accessor = event.getEntity().level();
					PackCheckUpdate.start(level_accessor);
					CustomPackIncompatible.scanMain(level_accessor);

				}

			});

		}

	}

}
