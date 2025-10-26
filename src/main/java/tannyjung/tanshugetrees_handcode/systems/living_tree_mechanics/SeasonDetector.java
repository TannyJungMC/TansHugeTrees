package tannyjung.tanshugetrees_handcode.systems.living_tree_mechanics;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.LevelAccessor;
import tannyjung.tanshugetrees.TanshugetreesMod;
import tannyjung.core.GameUtils;

public class SeasonDetector {

    private static int season_detector_tick = 0;

    public static void start (LevelAccessor level_accessor) {

        if (level_accessor instanceof ServerLevel level_server) {

            TanshugetreesMod.queueServerWork(20, () -> {

                start(level_server);

            });

            int posX = level_server.getLevelData().getXSpawn();
            int posZ = level_server.getLevelData().getZSpawn();
            int posY = level_server.getMinBuildHeight() + 1;

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
                    season_number = 0;

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

    }

}
