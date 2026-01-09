package tannyjung.tanshugetrees_handcode.data;

import tannyjung.tanshugetrees.TanshugetreesMod;
import tannyjung.tanshugetrees_core.Core;
import tannyjung.tanshugetrees_core.outside.FileManager;

import java.io.*;

public class DataMigration {

    public static void run (String type) {

        if (type.contains("config") == true) {

            String path = Core.path_config + "/#dev/version.txt";
            File test_exist = new File(Core.path_config).getParentFile();
            int version = 0;

            // Get Version
            {

                if (test_exist.exists() == false) {

                    version = -1;

                } else {

                    for (String read_all : FileManager.readTXT(path)) {

                        version = Integer.parseInt(read_all);

                    }

                }

            }

            if (version >= 0 && Core.data_structure_version != version) {

                config.test(version);

            }

            FileManager.writeTXT(path, String.valueOf(Core.data_structure_version), false);

        }

        if (type.contains("world") == true) {

            String path = Core.path_world_data + "/version.txt";
            File test_exist = new File(Core.path_world_data).getParentFile();
            int version = 0;

            // Get Version
            {

                if (test_exist.exists() == false) {

                    version = -1;

                } else {

                    for (String read_all : FileManager.readTXT(path)) {

                        version = Integer.parseInt(read_all);

                    }

                }

            }

            if (version >= 0 && Core.data_structure_version != version) {

                world.test(version);

            }

            FileManager.writeTXT(path, String.valueOf(Core.data_structure_version), false);

        }

    }

    private static class config {

        private static void test (int version) {

            if (version == 0) DataMigration.config.run.failed();
            if (version < 20260107) TanshugetreesMod.LOGGER.info("Data Migration : Config Test");

        }

        private static class run {

            private static void failed () {

                TanshugetreesMod.LOGGER.info("Running config data migration for failed condition");

                // Rename "tanshugetrees" to "tannyjung_tanshugetrees"
                {

                    File file = new File(new File(Core.path_config).getParentFile().getPath() + "/tanshugetrees");

                    if (file.exists() == true && file.isDirectory() == true) {

                        FileManager.rename(file.getPath(), "tannyjung_tanshugetrees");

                    }

                }

                FileManager.delete(Core.path_config + "/#dev/custom_packs_organized");
                FileManager.rename(Core.path_config + "/custom_packs/THT-tree_pack-main", "#TannyJung-Main-Pack");
                FileManager.rename(Core.path_config + "/custom_packs/TannyJung-Main-Pack", "#TannyJung-Main-Pack");
                FileManager.rename(Core.path_config + "/config_world_gen.txt", "config_worldgen.txt");

            }

        }

    }

    private static class world {

        private static void test (int version) {

            if (version == 0) run.failed();
            if (version < 20260107) TanshugetreesMod.LOGGER.info("Data Migration : World Test");

        }

        private static class run {

            private static void failed () {

                TanshugetreesMod.LOGGER.info("Running world data migration for failed condition");

                // Rename "tanshugetrees" to "tannyjung_tanshugetrees"
                {

                    File file = new File(new File(Core.path_world_data).getParentFile().getPath() + "/tanshugetrees");

                    if (file.exists() == true && file.isDirectory() == true) {

                        FileManager.rename(file.getPath(), "tannyjung_tanshugetrees");

                    }

                }

            }

        }

    }

}
