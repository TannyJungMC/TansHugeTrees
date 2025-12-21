package tannyjung.tanshugetrees_handcode.data;

import net.minecraft.server.level.ServerLevel;

import org.apache.logging.log4j.Logger;
import tannyjung.tanshugetrees_core.TannyPackManager;
import tannyjung.tanshugetrees.TanshugetreesMod;
import tannyjung.tanshugetrees_handcode.Handcode;

public class TannyPack {

    public static void checkUpdate (ServerLevel level_server) {

        Logger logger = TanshugetreesMod.LOGGER;
        String path_config = Handcode.path_config;
        String id = "THT";
        String pack_link = "TannyJungMC/THT-tree_pack";
        String branch = Handcode.tanny_pack_type;
        String wiki = "https://sites.google.com/view/tannyjung/minecraft-projects/tans-huge-trees/installation";
        int data_structure_version = Handcode.data_structure_version_pack;
        boolean auto_update = FileConfig.auto_update;
        String command_update = "TANSHUGETREES tanny_pack update";

        if (TannyPackManager.checkUpdate(level_server, logger, path_config, id, pack_link, branch, wiki, data_structure_version, auto_update, command_update) == true) {

            reinstall(level_server);

        }

    }

    public static void reinstall (ServerLevel level_server) {

        Runnable runnable = () -> {

            Logger logger = TanshugetreesMod.LOGGER;
            String path_config = Handcode.path_config;
            String id = "THT";
            String pack_link = "TannyJungMC/THT-tree_pack";
            String branch = Handcode.tanny_pack_type;
            String wiki = "https://sites.google.com/view/tannyjung/minecraft-projects/tans-huge-trees/installation";

            Handcode.thread_locking.runPause();

            if (TannyPackManager.reinstall(level_server, logger, path_config, id, pack_link, branch, wiki) == true) {

                Handcode.thread_locking.runContinue();
                Handcode.restart(level_server, "config / world", true);

                if (level_server != null) {

                    message(level_server);

                }

            } else {

                Handcode.thread_locking.runContinue();

            }

        };

        if (level_server != null) {

            Handcode.thread_main.submit(runnable);

        } else {

            runnable.run();

        }

    }

    public static void message (ServerLevel level_server) {

        String pack_link = "TannyJungMC/THT-tree_pack";
        String branch = Handcode.tanny_pack_type;
        TannyPackManager.messageOnlineNews(level_server, pack_link, branch);

    }

}