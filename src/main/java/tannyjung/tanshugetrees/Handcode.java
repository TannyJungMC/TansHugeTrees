package tannyjung.tanshugetrees;

import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.storage.LevelResource;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.level.ChunkEvent;
import net.minecraftforge.event.server.*;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.util.thread.SidedThreadGroups;
import net.minecraftforge.registries.DeferredRegister;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tannyjung.core.OutsideUtils;
import tannyjung.core.game.Utils;
import tannyjung.tanshugetrees.config.CustomPackIncompatible;
import tannyjung.tanshugetrees.config.PackCheckUpdate;
import tannyjung.tanshugetrees.config.ConfigMain;
import tannyjung.tanshugetrees.server.Cache;
import tannyjung.tanshugetrees.server.Loop;
import tannyjung.tanshugetrees.server.world_gen.FeatureAreaDirt;
import tannyjung.tanshugetrees.server.world_gen.FeatureAreaGrass;
import tannyjung.tanshugetrees.server.living_tree_mechanics.SeasonDetector;
import tannyjung.tanshugetrees.server.world_gen.WorldGenFull;
import tannyjung.tanshugetrees.server.world_gen.WorldGenBeforePlants;
import tannyjung.tanshugetrees_mcreator.init.TanshugetreesModBlockEntities;
import tannyjung.tanshugetrees_mcreator.init.TanshugetreesModBlocks;
import tannyjung.tanshugetrees_mcreator.init.TanshugetreesModItems;
import tannyjung.tanshugetrees_mcreator.init.TanshugetreesModTabs;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Mod("tanshugetrees")
@Mod.EventBusSubscriber
public class Handcode {

    public static final int DATA_STRUCTURE_VERSION = 20251023;
    public static final String TANNY_PACK_VERSION = "Alpha";

    public static final boolean VERSION_1192 = false;

    public static final Logger logger = LogManager.getLogger("TansHugeTrees");
    public static final String path_config = Utils.path_game + "/config/tanshugetrees";
    public static String path_world_data = Utils.path_game + "/saves/tanshugetrees-error";
    public static String tanny_pack_version_name = "";
    private static final Object lock = new Object();
    public static boolean system_pause = false;
    public static ExecutorService thread_main = Executors.newFixedThreadPool(1);
    private static final Collection<AbstractMap.SimpleEntry<Runnable, Integer>> delayed_works = new ConcurrentLinkedQueue<>();

    // ----------------------------------------------------------------------------------------------------

    public Handcode () {

        Handcode.logger.info("Starting...");

        // Thread Start
        {

            thread_main = Executors.newFixedThreadPool(1, name -> {
                Thread thread = new Thread(name);
                thread.setName("Tan's Huge Trees");
                return thread;
            });

        }

        runRegistries();
        runRestart(null, false);

    }

    private static void runRegistries () {

        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        // Features
        {

            DeferredRegister<Feature<?>> REGISTRY = DeferredRegister.create(Registries.FEATURE, "tanshugetrees");
            REGISTRY.register("world_gen_before_plants", WorldGenBeforePlants::new);
            REGISTRY.register("area_grass", FeatureAreaGrass::new);
            REGISTRY.register("area_dirt", FeatureAreaDirt::new);
            REGISTRY.register(bus);

        }

        // Things
        {

            TanshugetreesModBlocks.REGISTRY.register(bus);
            TanshugetreesModBlockEntities.REGISTRY.register(bus);
            TanshugetreesModItems.REGISTRY.register(bus);
            TanshugetreesModTabs.REGISTRY.register(bus);

        }

    }

    public static void runRestart (LevelAccessor level_accessor, boolean config_repair) {

        system_pause = true;

        Handcode.thread_main.submit(() -> {

            if (config_repair == false) {

                ConfigMain.repairAll(level_accessor);
                ConfigMain.apply();

            }

            double cache_size = Cache.clear();

            if (level_accessor != null) {

                if (level_accessor instanceof ServerLevel level_server) {

                    Utils.misc.sendChatMessage(level_server, "@a", "gray", "THT : Restarted and cleared main caches (About " + cache_size + " MB)");

                }

            }

            notifyWorldGen();

        });

    }

    public static void createDelayedWorks (int tick, Runnable works) {

        if (Thread.currentThread().getThreadGroup() == SidedThreadGroups.SERVER) {

            delayed_works.add(new AbstractMap.SimpleEntry<>(works, tick));

        }

    }

    public static void pauseWorldGen () {

        synchronized (lock) {

            while (Handcode.system_pause == true) {

                try {

                    lock.wait();

                } catch (Exception exception) {

                    OutsideUtils.exception(new Exception(), exception);

                }

            }

        }

    }

    public static void notifyWorldGen () {

        system_pause = false;

        synchronized (lock) {

            lock.notifyAll();

        }

    }

    // ----------------------------------------------------------------------------------------------------

    @SubscribeEvent
    public static void eventWorldAboutToStart (ServerAboutToStartEvent event) {

        path_world_data = event.getServer().getWorldPath(new LevelResource(".")) + "/data/tanshugetrees";
        runRestart(null, false);

    }

    @SubscribeEvent
    public static void eventWorldStarted (ServerStartedEvent event) {

        ServerLevel level_server = event.getServer().overworld();

        Handcode.createDelayedWorks(20, () -> {

            system_pause = true;

            Handcode.createDelayedWorks(20, () -> {

                Utils.command.run(level_server, 0, 0, 0, "scoreboard objectives add TANSHUGETREES dummy");
                Loop.start(level_server, level_server);

                if (ConfigMain.serene_seasons_compatibility == true && ModList.get().isLoaded("sereneseasons") == true) {

                    SeasonDetector.start(level_server, level_server);

                }

                Handcode.logger.info("Started World Systems");
                notifyWorldGen();

            });

        });

    }

    @SubscribeEvent
    public static void eventPlayerJoined (PlayerEvent.PlayerLoggedInEvent event) {

        if (Utils.misc.getPlayerCount() == 1) {

            Handcode.createDelayedWorks(100, () -> {

                if (ConfigMain.auto_check_update == true) {

                    LevelAccessor level_accessor = event.getEntity().level();
                    CustomPackIncompatible.scanMain(level_accessor);
                    PackCheckUpdate.start(level_accessor, false);

                }

            });

        }

    }

    @SubscribeEvent
    public static void eventChunkLoaded (ChunkEvent.Load event) {

        if (event.isNewChunk() == true) {

            WorldGenFull.start(event);

        }

    }

    @SubscribeEvent
    public static void eventTickServer (TickEvent.ServerTickEvent event) {

        if (event.phase == TickEvent.Phase.END) {

            List<AbstractMap.SimpleEntry<Runnable, Integer>> works = new ArrayList<>();

            delayed_works.forEach(work -> {

                work.setValue(work.getValue() - 1);

                if (work.getValue() == 0) {

                    works.add(work);

                }

            });

            works.forEach(e -> e.getKey().run());
            delayed_works.removeAll(works);

        }

    }

}
