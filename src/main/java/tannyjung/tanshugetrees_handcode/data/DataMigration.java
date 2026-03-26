package tannyjung.tanshugetrees_handcode.data;

import tannyjung.tanshugetrees_core.Core;
import tannyjung.tanshugetrees_core.outside.FileManager;
import tannyjung.tanshugetrees_core.outside.OutsideUtils;

import java.io.*;

public class DataMigration {

    public static void run (String type) {

        if (type.contains("config") == true) {

            String path = Core.path_config + "/#dev/version.txt";
            File test_exist = new File(Core.path_config).getParentFile();
            String version = "";

            // Get Version
            {

                if (test_exist.exists() == true) {

                    for (String read_all : FileManager.readTXT(path)) {

                        version = read_all;

                    }

                }

            }

            config.test(version);
            FileManager.writeTXT(path, Core.data_structure_version_mod, false);

        }

        if (type.contains("world") == true) {

            String path = Core.path_world_mod + "/version.txt";
            File test_exist = new File(Core.path_world_mod).getParentFile();
            String version = "";

            // Get Version
            {

                if (test_exist.exists() == true) {

                    for (String read_all : FileManager.readTXT(path)) {

                        version = read_all;

                    }

                }

            }

            world.test(version);
            FileManager.writeTXT(path, Core.data_structure_version_mod, false);

        }

    }

    private static class config {

        private static void test (String version) {

            if (version.isEmpty() == true) DataMigration.config.run.failed();
            if (OutsideUtils.testVersion("1.8.0", version).equals("outdated") == true) run.before_1_8_0();

        }

        private static class run {

            private static void failed () {

                Core.logger.info("Running config data migration for failed condition");
                FileManager.delete(Core.path_config + "/#dev");
                FileManager.rename(Core.path_config + "/custom_packs/THT-tree_pack-main", "#TannyJung-Main-Pack");
                FileManager.rename(Core.path_config + "/custom_packs/TannyJung-Main-Pack", "#TannyJung-Main-Pack");
                FileManager.rename(Core.path_config + "/config_world_gen.txt", "config_worldgen.txt");

            }

            private static void before_1_8_0 () {

                Core.logger.info("Running config data migration for 1.8.0");
                FileManager.rename(Core.path_config + "/#dev/temporary", "#temporary");

            }

        }

    }

    private static class world {

        private static void test (String version) {

            if (version.isEmpty() == true) run.failed();
            if (OutsideUtils.testVersion("1.8.0", version).equals("outdated") == true) run.before_1_8_0();

        }

        private static class run {

            private static void failed () {

                Core.logger.info("Running world data migration for failed condition");
                FileManager.delete(Core.path_world_mod);

            }

            private static void before_1_8_0 () {

                Core.logger.info("Running world data migration for 1.8.0");
                FileManager.delete(Core.path_world_mod + "/world_gen/detailed_detection");

            }

        }

    }

}
