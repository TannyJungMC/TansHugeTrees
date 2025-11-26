package tannyjung.tanshugetrees_handcode.data;

import tannyjung.core.FileManager;
import tannyjung.tanshugetrees_handcode.Handcode;

import java.io.File;

public class DataMigration {

    public static void start () {

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

            if (Handcode.DATA_STRUCTURE_VERSION != previous_version) {

                FileManager.writeTXT(path, String.valueOf(Handcode.DATA_STRUCTURE_VERSION), false);
                config.test(previous_version);

            }

        }

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

            if (Handcode.DATA_STRUCTURE_VERSION != previous_version) {

                FileManager.writeTXT(path, String.valueOf(Handcode.DATA_STRUCTURE_VERSION), false);
                world_data.test(previous_version);

            }

        }

    }

    private static class config {

        private static void test (int previous_version) {

            if (previous_version == 0) {

                // Hello Humans

            }

        }

        private static class versions {



        }

    }

    private static class world_data {

        private static void test (int previous_version) {

            if (previous_version == 0) {

                // Hello Humans

            }

        }

        private static class versions {



        }

    }

}
