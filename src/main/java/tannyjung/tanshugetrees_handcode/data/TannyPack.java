package tannyjung.tanshugetrees_handcode.data;

import net.minecraft.server.level.ServerLevel;

import org.apache.logging.log4j.Logger;
import tannyjung.tanshugetrees_core.Core;
import tannyjung.tanshugetrees_core.outside.TannyPackManager;
import tannyjung.tanshugetrees.TanshugetreesMod;
import tannyjung.tanshugetrees_handcode.Handcode;

public class TannyPack {

    public static void checkUpdate (ServerLevel level_server) {

        String pack_link = "TannyJungMC/THT-tree_pack";
        String wiki = "https://sites.google.com/view/tannyjung/minecraft-projects/tans-huge-trees/installation";

        boolean auto_update = FileConfig.auto_update; // Remove Auto Update ?

        String command_update = "TANSHUGETREES tanny_pack update";

        if (TannyPackManager.checkUpdate(level_server, pack_link, wiki, auto_update, command_update) == true) {

            reinstall(level_server);

        }

    }

    public static void reinstall (ServerLevel level_server) {

        Runnable runnable = () -> {

            synchronized (Core.global_locking) {

                String pack_link = "TannyJungMC/THT-tree_pack";
                String wiki = "https://sites.google.com/view/tannyjung/minecraft-projects/tans-huge-trees/installation";

                if (TannyPackManager.reinstall(level_server, pack_link, wiki) == true) {

                    Handcode.restart(level_server, "config / world", true);

                }

            }

        };

        if (level_server != null) {

            Core.thread_main.submit(runnable);

        } else {

            runnable.run();

        }

    }

}