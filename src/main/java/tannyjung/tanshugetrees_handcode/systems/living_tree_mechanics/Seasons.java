package tannyjung.tanshugetrees_handcode.systems.living_tree_mechanics;

import net.minecraft.server.level.ServerLevel;
import tannyjung.tanshugetrees_core.Core;
import tannyjung.tanshugetrees_core.game.GameUtils;
import tannyjung.tanshugetrees.network.TanshugetreesModVariables;

public class Seasons {

    public static void get (ServerLevel level_server) {

        GameUtils.misc.sendChatMessage(level_server, "@a", "Current Season is " + TanshugetreesModVariables.MapVariables.get(level_server).season + " / gray");

    }

    public static void set (ServerLevel level_server, String season) {

        GameUtils.misc.sendChatMessage(level_server, "@a", "Set Season To " + season + " / gray");
        TanshugetreesModVariables.MapVariables.get(level_server).season = season;

    }

}
