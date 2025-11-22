package tannyjung.tanshugetrees_handcode.systems.living_tree_mechanics;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.LevelAccessor;
import tannyjung.core.game.GameUtils;
import tannyjung.tanshugetrees_handcode.Handcode;
import tannyjung.tanshugetrees.network.TanshugetreesModVariables;

public class Seasons {

    private static int season_detector_tick = 0;

    public static void startDetector (LevelAccessor level_accessor, ServerLevel level_server) {

        level_server.getServer().execute(() -> {

            loopDetector(level_accessor, level_server);

        });

    }

    private static void loopDetector (LevelAccessor level_accessor, ServerLevel level_server) {

        Handcode.createDelayedWorks(200, () -> {

            if (Handcode.system_pause == false) {

                loopDetector(level_accessor, level_server);

            }

        });

        int posX = level_accessor.getLevelData().getXSpawn();
        int posZ = level_accessor.getLevelData().getZSpawn();
        int posY = level_accessor.getMinBuildHeight() + 1;

        if (season_detector_tick == 1 || season_detector_tick == 3 || season_detector_tick == 5 || season_detector_tick == 7 || season_detector_tick == 9) {

            GameUtils.command.run(level_server, posX, posY, posZ, "fill ~ ~ ~ ~ ~1 ~ air");

            if (season_detector_tick == 9) {

                season_detector_tick = 0;
                return;

            }

        } else {

            String season = "";
            int season_number = 0;

            if (season_detector_tick == 2) {

                season = "spring";

            } else if (season_detector_tick == 4) {

                season = "summer";
                season_number = 1;

            } else if (season_detector_tick == 6) {

                season = "autumn";
                season_number = 2;

            } else if (season_detector_tick == 8) {

                season = "winter";
                season_number = 3;

            }

            if (season.equals("") == false) {

                GameUtils.command.run(level_server, posX, posY, posZ, "setblock ~ ~ ~ command_block{Command:\"TANSHUGETREES command season set " + season + "\"}");
                GameUtils.command.run(level_server, posX, posY, posZ, "setblock ~ ~1 ~ sereneseasons:season_sensor[season=" + season_number + "]");

            }

        }

        season_detector_tick = season_detector_tick + 1;

    }

    public static void get (ServerLevel level_server) {

        GameUtils.misc.sendChatMessage(level_server, "@a", "gray", "THT : Current Season is " + TanshugetreesModVariables.MapVariables.get(level_server).season);

    }

    public static void set (ServerLevel level_server, String season) {

        GameUtils.misc.sendChatMessage(level_server, "@a", "gray", "THT : Set Season To " + season);
        TanshugetreesModVariables.MapVariables.get(level_server).season = season;

    }

}
