package tannyjung.tanshugetrees_handcode;

import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.storage.LevelResource;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.level.ChunkEvent;
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
import tannyjung.tanshugetrees_handcode.systems.world_gen.FeatureGrassArea;
import tannyjung.tanshugetrees_handcode.systems.living_tree_mechanics.SeasonDetector;
import tannyjung.tanshugetrees_handcode.systems.world_gen.WorldGenFull;
import tannyjung.tanshugetrees_handcode.systems.world_gen.WorldGenBeforePlants;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Mod.EventBusSubscriber
public class Handcode {

	// ----------------------------------------------------------------------------------------------------

	public static int data_structure_version = 20251023;
	public static String tanny_pack_version = "Alpha";

	public static boolean version_1192 = false;

	// ----------------------------------------------------------------------------------------------------

	public static String directory_game = FMLPaths.GAMEDIR.get().toString();
	public static String directory_config = directory_game + "/config/tanshugetrees";
	public static String directory_world_data = directory_game + "/saves/tanshugetrees-error/directory_world_data";
	public static String tanny_pack_version_name = ""; // Make this because version can swap to "WIP" by config
	public static boolean world_active = false;
    private static int thread_count_name = 1;
    public static ExecutorService thread = Executors.newFixedThreadPool(1);

    // ----------------------------------------------------------------------------------------------------

	public Handcode () {}

	public static void startGame () {

		TanshugetreesMod.LOGGER.info("Loading mod registries and config");

		// Basic Registries
		{

			IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

			DeferredRegister<Feature<?>> REGISTRY = DeferredRegister.create(Registries.FEATURE, TanshugetreesMod.MODID);
			REGISTRY.register("world_gen_before_plants", WorldGenBeforePlants::new);
			REGISTRY.register("grass_area", FeatureGrassArea::new);

			REGISTRY.register(bus);

		}

        Handcode.thread.submit(() -> {

			ConfigMain.repairAll(null);
			ConfigMain.apply(null);

		});

	}

	@SubscribeEvent
	public static void worldAboutToStart (ServerAboutToStartEvent event) {

		world_active = true;
		TanshugetreesMod.LOGGER.info("Turned ON world systems");

        // Thread Start
        {

            thread_count_name = 1;

            thread = Executors.newFixedThreadPool(4, name -> {
                Thread thread = new Thread(name);
                thread.setName("Tan's Huge Trees (" + thread_count_name + ")");
                thread_count_name = thread_count_name + 1;
                return thread;
            });

        }

		String world_path = String.valueOf(event.getServer().getWorldPath(new LevelResource(".")));
		directory_world_data = world_path + "/data/tanshugetrees";
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
        thread.shutdownNow();

	}

	@SubscribeEvent
	public static void playerJoined (PlayerEvent.PlayerLoggedInEvent event) {

		if (GameUtils.misc.playerCount() == 1) {

			TanshugetreesMod.queueServerWork(100, () -> {

				if (ConfigMain.auto_check_update == true) {

					LevelAccessor level_accessor = event.getEntity().level();
					CustomPackIncompatible.scanMain(level_accessor);
					PackCheckUpdate.start(level_accessor, false);

				}

			});

		}

	}

	@SubscribeEvent
	public static void chunkLoaded (ChunkEvent.Load event) {

		if (event.isNewChunk() == true) {

			WorldGenFull.start(event);

		}

	}

}
