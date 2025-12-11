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
            int data_structure_version_config = Handcode.data_structure_version_config;
            boolean auto_update = FileConfig.auto_update;
            String command_update = "TANSHUGETREES tanny_pack update";
            TannyPackInstaller.checkUpdate(level_server, logger, path_config, id, pack_link, branch, wiki, data_structure_version_config, auto_update, command_update);

        }

        Handcode.system_pause.runContinue();

    }

    public static void reinstall (ServerLevel level_server, boolean by_player) {

        Handcode.system_pause.runPause();

        {

            Handcode.thread_main.submit(() -> {

                Logger logger = TanshugetreesMod.LOGGER;
                String path_config = Handcode.path_config;
                String id = "THT";
                String pack_link = "TannyJungMC/THT-tree_pack";
                String branch = Handcode.tanny_pack_type;

                if (TannyPackInstaller.reinstall(level_server, logger, path_config, id, pack_link, branch) == true) {

                    Handcode.restart(level_server, true, by_player);

                }

            });

        }

        Handcode.system_pause.runContinue();

    }

}