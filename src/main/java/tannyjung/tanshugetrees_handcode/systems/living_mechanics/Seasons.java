package tannyjung.tanshugetrees_handcode.systems.living_mechanics;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import tannyjung.tanshugetrees_core.game.GameUtils;
import tannyjung.tanshugetrees.network.TanshugetreesModVariables;

public class Seasons {

    public static void get (ServerLevel level_server, Entity entity) {

        GameUtils.Misc.sendChatMessagePrivate(entity, "Current Season is " + TanshugetreesModVariables.MapVariables.get(level_server).season + " / gray");

    }

    public static void set (ServerLevel level_server, String season) {

        GameUtils.Misc.sendChatMessage(level_server, "Set Season To " + season + " / gray");
        TanshugetreesModVariables.MapVariables.get(level_server).season = season;

    }

}
