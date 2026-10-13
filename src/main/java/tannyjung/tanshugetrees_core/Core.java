package tannyjung.tanshugetrees_core;

import net.minecraft.server.level.ServerLevel;
import org.apache.logging.log4j.Logger;
import tannyjung.tanshugetrees_core.game.*;
import tannyjung.tanshugetrees_core.outside.*;
import tannyjung.tanshugetrees_core.outside.CacheManager;
import tannyjung.tanshugetrees_core.outside.config.ConfigClassic;
import tannyjung.tanshugetrees_core.outside.config.CustomPack;
import tannyjung.tanshugetrees_handcode.Handcode;
import tannyjung.tanshugetrees_handcode.core.GUIs;
import tannyjung.tanshugetrees_handcode.core.KeyBindings;
import tannyjung.tanshugetrees_handcode.core.Loops;

import java.io.File;
import java.util.*;
import java.util.concurrent.*;

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

public class Core {

    /*

    Use replace-all tool to replace these words, without "___" by the way. Note that you need to enable match cases and match words as well.

    (1.20.1)
    ___ForgeData___
    (1.21.1) (1.21.8)
    ___NeoForgeData___

    */

    public static Logger logger = null;
    public static ExecutorService thread = null;
    public static boolean global_locking = false;
    public static String path_config = "";
    public static String path_world_core = "";
    public static String path_world_mod = "";

    public static String mod_name = "";
    public static String mod_id = "";
    public static String mod_id_big = "";
    public static String mod_id_short = "";
    public static String github_pack = "";
    public static String wiki = "";
    public static boolean have_custom_pack = false;
    public static boolean have_world_data_cleaner = false;

    public static int data_structure_version_core = 0;
    public static String data_structure_version_data = "";
    public static String data_structure_version_pack = "";
    public static String main_pack_type = "";
    public static String main_pack_type_original = "";

    public static boolean auto_check_update = false;
    public static boolean wip_version = false;
    public static boolean developer_mode = false;

    public static void sync (

            String mod_name,
            String mod_id,
            String mod_id_short,
            String github_pack,
            String wiki,
            boolean have_custom_pack,
            boolean have_world_data_cleaner,

            int data_structure_version_core,
            String data_structure_version_data,
            String data_structure_version_pack,
            String main_pack_type

    ) {

        Core.mod_name = mod_name;
        Core.mod_id = mod_id;
        Core.mod_id_big = mod_id.toUpperCase();
        Core.mod_id_short = mod_id_short;
        Core.github_pack = github_pack;
        Core.wiki = wiki;
        Core.have_custom_pack = have_custom_pack;
        Core.have_world_data_cleaner = have_world_data_cleaner;

        Core.data_structure_version_core = data_structure_version_core;
        Core.data_structure_version_data = data_structure_version_data;
        Core.data_structure_version_pack = data_structure_version_pack;
        Core.main_pack_type = main_pack_type;
        Core.main_pack_type_original = main_pack_type;

    }

    public static void start (IEventBus bus) {

        Handcode.sync();
        logger = LogManager.getLogger(mod_id);
        path_config = FMLPaths.GAMEDIR.get().toString() + "/config/" + mod_id;

        thread = Executors.newFixedThreadPool(1, runnable -> {

            Thread thread = new Thread(runnable);
            thread.setName(mod_id_big + "-thread");
            return thread;

        });

        DataMigration.runConfig();
        DataManagement.regenerateOverall(bus);
        DataManagement.restart(null, true, true);

    }

    public static class DataManagement {

        public static void restart (ServerLevel level_server, boolean message, boolean config) {

            Runnable runnable = () -> {

                // Start Message
                {

                    if (message == true && config == true) {

                        if (level_server != null) {

                            GameUtils.sendChatMessage(level_server, "Restarting the mod... / gray");

                        }

                    }

                }

                String cache_size = "";

                if (config == true) {

                    cache_size = CacheManager.clear();
                    repairConfig(level_server);

                }

                // End Message
                {

                    if (message == true && config == true) {

                        CustomPack.Error.sendMessage(level_server);

                        if (level_server != null) {

                            GameUtils.sendChatMessage(level_server, "Restarted and cleared main caches about " + cache_size + " / gray");

                        }

                    }

                }

            };

            if (level_server == null) {

                runnable.run();

            } else {

                thread.submit(() -> {

                    GlobalLocking.test();
                    GlobalLocking.lock();

                    DelayedWork.createAsync(20, () -> {

                        runnable.run();

                        EntityManager.Population.refresh();

                        GlobalLocking.unlock();

                    });

                });

            }

        }

        public static void regenerateOverall (IEventBus bus) {

            EventCenter.register(bus);
            GUIs.add();
            KeyBindings.add();

        }

        public static void regenerateWorld (ServerLevel level_server) {

            ScoreManager.create(level_server, mod_id_big);

        }

        private static void repairConfig (ServerLevel level_server) {

            FileManager.createEmptyFile(path_config + "/custom_packs", true);

            // Main Config
            {

                Handcode.Config.repair("""
                    ----------------------------------------------------------------------------------------------------
                    Main Pack
                    ----------------------------------------------------------------------------------------------------
                    
                    auto_check_update = true
                    | Check for new update from GitHub every time the world starts
                    
                    wip_version = false
                    | Use development version of the pack instead of release version. Not recommended for game play, as it's still in development, it might unstable. Sometimes it needed development version of the mod.
                    
                    """, """
                    
                    developer_mode = false
                    | Enable some features for debugging such as detailed error messages, info overlay in-game, etc.
                    
                    ----------------------------------------------------------------------------------------------------
                    """);

                Map<String, String> data = ConfigClassic.getValues(path_config + "/config.txt");
                Handcode.Config.apply(data);

                auto_check_update = Boolean.parseBoolean(data.get("auto_check_update"));
                wip_version = Boolean.parseBoolean(data.get("wip_version"));
                developer_mode = Boolean.parseBoolean(data.get("developer_mode"));

                if (wip_version == true) {

                    main_pack_type = "wip";

                } else {

                    main_pack_type = main_pack_type_original;

                }

            }

            Handcode.organizeCustomPack();
            Handcode.repairData(level_server);

        }

    }

    public static class DataMigration {

        public static void runConfig () {

            String path = path_config + "/dev/version.txt";
            File test_exist = new File(path_config);
            String version = "";

            // Get Version
            {

                if (test_exist.exists() == true) {

                    for (String scan : FileManager.readTXT(path)) {

                        version = scan;

                    }

                } else {

                    version = "not found";

                }

            }

            if (version.equals("not found") == false) {

                Handcode.DataMigration.runConfig(version);

            }

            FileManager.writeTXT(path, data_structure_version_data, false);

        }

        public static void runWorld () {

            String path = path_world_mod + "/version.txt";
            File test_exist = new File(path_world_mod);
            String version = "";

            // Get Version
            {

                if (test_exist.exists() == true) {

                    for (String scan : FileManager.readTXT(path)) {

                        version = scan;

                    }

                } else {

                    version = "not found";

                }

            }

            if (version.equals("not found") == false) {

                Handcode.DataMigration.runWorld(version);

            }

            FileManager.writeTXT(path, data_structure_version_data, false);

        }

    }

    public static class GlobalLocking {

        private static final Object lock = new Object();

        public static void lock () {

            global_locking = true;

        }

        public static void unlock () {

            synchronized (lock) {

                global_locking = false;
                lock.notifyAll();

            }

        }

        public static void test () {

            synchronized (lock) {

                while (global_locking == true) {

                    try {

                        lock.wait();

                    } catch (Exception exception) {

                        OutsideUtils.exception(new Exception(), exception, "");
                        return;

                    }

                }

            }

        }

    }

    public static class DelayedWork {

        private static final Collection<AbstractMap.SimpleEntry<Runnable, Integer>> delayed_works = new ConcurrentLinkedQueue<>();
        private static final ScheduledExecutorService thread_delay = Executors.newScheduledThreadPool(1);

        public static void createBasic (int tick, Runnable work) {

            if (tick == 0) {

                work.run();

            } else {

                delayed_works.add(new AbstractMap.SimpleEntry<>(work, tick));

            }

        }

        public static void createAsync (int tick, Runnable work) {

            if (tick == 0) {

                work.run();

            } else {

                thread_delay.schedule(work, tick * 50L, TimeUnit.MILLISECONDS);

            }

        }

        public static void tick () {

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

        public static void
        loopTick (ServerLevel level_server) {

            Loops.tick(level_server);

            if (second > 1) {

                second = second - 1;

            } else {

                second = 20;
                loopSecond(level_server);

            }

        }

        private static void loopSecond (ServerLevel level_server) {

            TXTFunction.loop(level_server);
            Loops.second(level_server);

        }

    }

}