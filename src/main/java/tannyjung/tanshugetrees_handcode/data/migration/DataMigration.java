package tannyjung.tanshugetrees_handcode.data.migration;

import tannyjung.core.FileManager;
import tannyjung.tanshugetrees_handcode.Handcode;

import java.io.*;

public class DataMigration {

    public static void start (boolean in_world) {

        // Config
        {

            String path = Handcode.path_config + "/#dev/version.txt";
            int previous_version = 0;

            // Read File
            {

                File file = new File(path);

                if (file.exists() == true && file.isDirectory() == false) {

                    for (String read_all : FileManager.readTXT(path)) {

                        previous_version = Integer.parseInt(read_all);

                    }

                }

            }

            if (Handcode.data_structure_version_config != previous_version) {

                if (config.test(previous_version) == true) {

                    FileManager.writeTXT(path, String.valueOf(Handcode.data_structure_version_config), false);

                }

            }

        }

        if (in_world == true) {

            // World Data
            {

                String path = Handcode.path_world_data + "/version.txt";
                int previous_version = 0;

                // Read File
                {

                    File file = new File(path);

                    if (file.exists() == true && file.isDirectory() == false) {

                        for (String read_all : FileManager.readTXT(path)) {

                            previous_version = Integer.parseInt(read_all);

                        }

                    }

                }

                if (Handcode.data_structure_version_world != previous_version) {

                    if (world_data.test(previous_version) == true) {

                        FileManager.writeTXT(path, String.valueOf(Handcode.data_structure_version_world), false);

                    }

                }

            }

        }

    }

    private static class config {

        private static boolean test (int previous_version) {

            if (previous_version == 0) return versions.before160();

            return true;

        }

        private static class versions {

            private static boolean before160 () {

                FileManager.rename(Handcode.path_config + "/custom_packs/THT-tree_pack-main", "#TannyJung-Main-Pack");
                FileManager.rename(Handcode.path_config + "/custom_packs/#TansHugeTrees-Main-Pack", "#TannyJung-Main-Pack");
                FileManager.rename(Handcode.path_config + "/config_world_gen.txt", "config_worldgen.txt");
                return true;

            }

        }

    }

    private static class world_data {

        private static boolean test (int previous_version) {

            if (previous_version == 0) return versions.before160();

            return true;

        }

        private static class versions {

            private static boolean before160 () {

                return true;

            }

        }

    }

}
