package tannyjung.tanshugetrees_core;

import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.levelgen.feature.Feature;
import org.apache.logging.log4j.Logger;
import tannyjung.tanshugetrees_core.game.GameUtils;
import tannyjung.tanshugetrees_core.game.world_gen.FeatureAreaDirt;
import tannyjung.tanshugetrees_core.game.world_gen.FeatureAreaGrass;
import tannyjung.tanshugetrees_core.game.world_gen.WorldGenStepBeforePlants;
import tannyjung.tanshugetrees_core.game.world_gen.WorldGenStepLast;
import tannyjung.tanshugetrees_core.outside.*;
import tannyjung.tanshugetrees_handcode.Handcode;

import java.io.File;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.Supplier;

import org.apache.logging.log4j.LogManager;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.registries.DeferredRegister;
import tannyjung.tanshugetrees_handcode.systems.Loops;

public class Core {

    public static String mod_name = "";
    public static String mod_id = "";
    public static String mod_id_big = "";
    public static String mod_id_short = "";

    public static int data_structure_version_core = 0;
    public static String data_structure_version_mod = "";
    public static String data_structure_version_pack = "";
    public static String tanny_pack_type = "";
    public static String tanny_pack_type_original = "";

    public static String github_pack = "";
    public static String wiki = "";

    public static boolean have_world_data_cleaner = false;

    public static Logger logger = null;
    public static String path_game = FMLPaths.GAMEDIR.get().toString();
    public static String path_config = path_game + "/" + mod_id + "_error";
    public static String path_world_core = path_game + "/" + mod_id + "_error";
    public static String path_world_mod = path_game + "/" + mod_id + "_error";
    public static final ExecutorService thread_main = Executors.newFixedThreadPool(1, name -> { Thread thread = new Thread(name); thread.setName(Core.mod_name); return thread; });

    public static boolean in_restarting = false;

    public static void start (IEventBus bus) {

        Handcode.start();
        mod_id_big = mod_id.toUpperCase();
        tanny_pack_type_original = tanny_pack_type;
        logger = LogManager.getLogger(mod_id);
        path_config = path_game + "/config/" + mod_id;

        Registry.start(bus);
        DataMigration.run(false);
        restart(null, true);

    }

    public static void restart (ServerLevel level_server, boolean config) {

        Runnable runnable = () -> {

            if (config == true) {

                Handcode.repairData();
                Handcode.Config.repair();
                Handcode.Config.apply();

                if (Handcode.Config.wip_version == true) {

                    Core.tanny_pack_type = "WIP";

                } else {

                    Core.tanny_pack_type = Core.tanny_pack_type_original;

                }

                CustomPackOrganizing.sendErrorMessage(level_server);

            }

            if (level_server != null) {

                GameUtils.Score.create(level_server, mod_id_big);

            }

        };

        if (level_server == null) {

            CacheManager.clear();
            runnable.run();

        } else {

            thread_main.submit(() -> {

                GlobalLocking.test();
                GlobalLocking.lock();

                if (config == true) {

                    GameUtils.Misc.sendChatMessage(level_server, "Restarting the mod... / gray");

                }

                DelayedWork.create(true, 20, () -> {

                    String cache_size = "";

                    if (config == true) {

                        cache_size = CacheManager.clear();

                    }

                    runnable.run();

                    if (config == true) {

                        GameUtils.Misc.sendChatMessage(level_server, "Restarted and cleared main caches about " + cache_size + " / gray");

                    }

                    GlobalLocking.unlock();

                });

            });

        }

    }

    public static class Registry {

        public static Map<String, Supplier<Feature<?>>> features = new HashMap<>();

        public static void start (IEventBus bus) {

            features.put("world_gen_before_plants", WorldGenStepBeforePlants::new);
            features.put("world_gen_last", WorldGenStepLast::new);
            features.put("area_grass", FeatureAreaGrass::new);
            features.put("area_dirt", FeatureAreaDirt::new);

            // Feature
            {

                DeferredRegister<Feature<?>> deferred = DeferredRegister.create(Registries.FEATURE, mod_id);

                for (Map.Entry<String, Supplier<Feature<?>>> entry : features.entrySet()) {

                    deferred.register(entry.getKey(), entry.getValue());

                }

                deferred.register(bus);
                features.clear();

            }

        }

    }
    
    public static class GlobalLocking {

        private static final Object global_locking = new Object();

        public static void lock () {

            in_restarting = true;

        }

        public static void unlock () {

            synchronized (global_locking) {

                in_restarting = false;
                global_locking.notifyAll();

            }

        }

        public static void test () {

            synchronized (global_locking) {

                while (in_restarting == true) {

                    try {

                        global_locking.wait();

                    } catch (Exception exception) {

                        OutsideUtils.exception(new Exception(), exception, "");

                    }

                }

            }

        }

    }

    public static class DelayedWork {

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

    public static class Loop {

        private static int second = 0;
        private static int minute = 0;

        public static void loopTick (LevelAccessor level_accessor, ServerLevel level_server) {

            Loops.tick(level_accessor, level_server);
            second = second + 1;

            if (second > 20) {

                second = 0;
                loopSecond(level_accessor, level_server);

            }

        }

        private static void loopSecond (LevelAccessor level_accessor, ServerLevel level_server) {

            Loops.second(level_accessor, level_server);
            minute = minute + 1;

            if (minute > 60) {

                minute = 0;
                loopMinute(level_accessor, level_server);

            }

        }

        private static void loopMinute (LevelAccessor level_accessor, ServerLevel level_server) {

            Loops.minute(level_accessor, level_server);

        }

    }

    public static class DataMigration {

        public static void run(boolean is_world) {

            if (is_world == false) {

                String path = Core.path_config + "/dev/version.txt";
                File test_exist = new File(Core.path_config).getParentFile();
                String version = "";

                // Get Version
                {

                    if (test_exist.exists() == true) {

                        for (String read_all : FileManager.readTXT(path)) {

                            version = read_all;

                        }

                    } else {

                        version = "not found";

                    }

                }

                if (version.equals("not found") == false) {

                    Handcode.DataMigration.runConfig(version);

                }

                FileManager.writeTXT(path, Core.data_structure_version_mod, false);

            } else {

                String path = Core.path_world_mod + "/version.txt";
                File test_exist = new File(Core.path_world_mod).getParentFile();
                String version = "";

                // Get Version
                {

                    if (test_exist.exists() == true) {

                        for (String read_all : FileManager.readTXT(path)) {

                            version = read_all;

                        }

                    } else {

                        version = "not found";

                    }

                }

                if (version.equals("not found") == false) {

                    Handcode.DataMigration.runWorld(version);

                }

                FileManager.writeTXT(path, Core.data_structure_version_mod, false);

            }

        }

    }

}