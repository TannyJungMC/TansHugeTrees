package tannyjung.tanshugetrees_core;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.levelgen.feature.Feature;
import org.apache.logging.log4j.Logger;
import tannyjung.tanshugetrees_core.game.GameUtils;
import tannyjung.tanshugetrees_core.outside.CacheManager;
import tannyjung.tanshugetrees_handcode.Handcode;
import tannyjung.tanshugetrees_handcode.data.DataMigration;
import tannyjung.tanshugetrees_handcode.data.DataRepair;
import tannyjung.tanshugetrees_handcode.data.FileConfig;

import java.util.*;
import java.util.concurrent.*;
import java.util.function.Supplier;

import org.apache.logging.log4j.LogManager;

/*
(1.20.1)
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.registries.DeferredRegister;
(1.21.1)
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.registries.DeferredRegister;
*/
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.registries.DeferredRegister;

public class Core {

    /*
    (1.20.1)
    ___ForgeData___
    (1.21.1) (1.21.8)
    ___NeoForgeData___

    NeoForgeData
    */
    
    public static String mod_name = "";
    public static String mod_id = "";
    public static String mod_id_big = "";
    public static String mod_id_short = "";

    public static String data_structure_version = "";
    public static String data_structure_version_pack = "";
    public static String tanny_pack_type = "";
    public static String tanny_pack_type_original = "";

    public static String github_pack = "";
    public static String wiki = "";

    public static Logger logger = null;
    public static final Object global_locking = new Object();
    public static final String path_game = FMLPaths.GAMEDIR.get().toString();
    public static String path_config = "";
    public static String path_world_data = "";
    public static final ExecutorService thread_main = Executors.newFixedThreadPool(1, name -> { Thread thread = new Thread(name); thread.setName("TannyJung - " + Core.mod_name); return thread; });
    
    public static void start (IEventBus bus) {

        Handcode.start();

        mod_id_big = mod_id.toUpperCase();
        tanny_pack_type_original = tanny_pack_type;
        path_config = path_game + "/config/" + mod_id;
        path_world_data = path_game + "/saves/" + mod_id + "-error";
        logger = LogManager.getLogger(mod_id);

        Registries.start(bus);







        DataMigration.run("config");
        restart(null, "config", true);

        if (FileConfig.auto_check_update == true) {

            // TannyPack.checkUpdate(null);

        }

    }

    public static void restart (ServerLevel level_server, String type, boolean detail_info) {

        Runnable runnable = () -> {

            synchronized (global_locking) {

                if (level_server != null && detail_info == true) {

                    GameUtils.misc.sendChatMessage(level_server, "@a", "Restarting... / gray");

                }

                double cache_size = CacheManager.clear();

                if (type.contains("config") == true) {

                    {

                        DataRepair.start();
                        DataRepair.messagePackErrors(level_server, "pack");

                        if (detail_info == true) {

                            DataRepair.messagePackErrors(level_server, "file");

                        }

                    }

                }

                if (type.contains("world") == true) {

                    {

                        if (level_server != null) {

                            GameUtils.score.create(level_server, mod_id_big);

                            if (detail_info == true) {

                                GameUtils.misc.sendChatMessage(level_server, "@a", "Restarted and cleared main caches (About " + cache_size + " MB) / gray");

                            }

                        }

                    }

                }

            }

        };

        if (level_server == null) {

            runnable.run();

        } else {

            Loops.restart = true;

            DelayedWorks.create(true, 20, () -> {

                runnable.run();
                Loops.restart = false;

            });

        }

    }

    public static class DelayedWorks {

        private static final Collection<AbstractMap.SimpleEntry<Runnable, Integer>> delayed_works = new ConcurrentLinkedQueue<>();
        private static final ScheduledExecutorService thread_delay = Executors.newScheduledThreadPool(1);

        public static void create (boolean async, int tick, Runnable work) {

            if (async == false) {

                delayed_works.add(new AbstractMap.SimpleEntry<>(work, tick));

            } else {

                thread_delay.schedule(work, tick * 50L, TimeUnit.MILLISECONDS);

            }

        }

        public static void runTick () {

            for (AbstractMap.SimpleEntry<Runnable, Integer> work : delayed_works) {

                work.setValue(work.getValue() - 1);

                if (work.getValue() == 0) {

                    work.getKey().run();
                    delayed_works.remove(work);

                }

            }

        }

    }

    public static class Loops {

        public static boolean restart = false;
        private static int second = 0;
        private static int minute = 0;

        public static void run (LevelAccessor level_accessor, ServerLevel level_server) {

            if (restart == false) {

                loopTick(level_accessor, level_server);

            }

        }

        private static void loopTick (LevelAccessor level_accessor, ServerLevel level_server) {

            tannyjung.tanshugetrees_handcode.systems.Loops.tick(level_accessor, level_server);
            second = second + 1;

            if (second > 20) {

                second = 0;
                loopSecond(level_accessor, level_server);

            }

        }

        private static void loopSecond (LevelAccessor level_accessor, ServerLevel level_server) {

            tannyjung.tanshugetrees_handcode.systems.Loops.second(level_server);
            minute = minute + 1;

            if (minute > 60) {

                minute = 0;
                loopMinute(level_accessor, level_server);

            }

        }

        private static void loopMinute (LevelAccessor level_accessor, ServerLevel level_server) {

            tannyjung.tanshugetrees_handcode.systems.Loops.minute(level_accessor, level_server);

        }

    }

    public static class Registries {

        public static Map<String, Supplier<Feature<?>>> features = new HashMap<>();

        public static void start (IEventBus bus) {

            Handcode.registry();

            // Feature
            {

                DeferredRegister<Feature<?>> deferred = DeferredRegister.create(net.minecraft.core.registries.Registries.FEATURE, mod_id);

                for (Map.Entry<String, Supplier<Feature<?>>> entry : features.entrySet()) {

                    deferred.register(entry.getKey(), entry.getValue());

                }

                deferred.register(bus);
                features.clear();

            }

        }

    }

}
