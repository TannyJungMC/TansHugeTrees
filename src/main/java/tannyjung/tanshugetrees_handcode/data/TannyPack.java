package tannyjung.tanshugetrees_handcode.data;

import net.minecraft.server.level.ServerLevel;

import org.apache.logging.log4j.Logger;
import tannyjung.tanshugetrees_core.TannyPackInstaller;
import tannyjung.tanshugetrees.TanshugetreesMod;
import tannyjung.tanshugetrees_handcode.Handcode;

public class TannyPack {

    public static void checkUpdate (ServerLevel level_server) {

        Handcode.system_pause.runPause();

        {

            Logger logger = TanshugetreesMod.LOGGER;
            String path_config = Handcode.path_config;
            String id = "THT";
            String pack_link = "TannyJungMC/THT-tree_pack";
            String branch = Handcode.tanny_pack_type;
            String wiki = "https://sites.google.com/view/tannyjung/minecraft-projects/tans-huge-trees/installation";
            int data_structure_version = Handcode.data_structure_version_pack;
            boolean auto_update = FileConfig.auto_update;
            String command_update = "TANSHUGETREES tanny_pack update";

            if (TannyPackInstaller.checkUpdate(level_server, logger, path_config, id, pack_link, branch, wiki, data_structure_version, auto_update, command_update) == true) {

                reinstall(level_server, false);

            }

        }

        Handcode.system_pause.runContinue();

    }

    public static void reinstall (ServerLevel level_server, boolean by_player) {

        Handcode.system_pause.runPause();

        {

            Runnable runnable = () -> {

                Logger logger = TanshugetreesMod.LOGGER;
                String path_config = Handcode.path_config;
                String id = "THT";
                String pack_link = "TannyJungMC/THT-tree_pack";
                String branch = Handcode.tanny_pack_type;

                if (TannyPackInstaller.reinstall(level_server, logger, path_config, id, pack_link, branch) == true) {

                    Handcode.restartConfig(level_server, by_player);
                    Handcode.restartWorld(level_server, true);

                }

            };

            if (by_player == false) {

                runnable.run();

            } else {

                Handcode.thread_main.submit(runnable);

            }

        }

        Handcode.system_pause.runContinue();

    }

}