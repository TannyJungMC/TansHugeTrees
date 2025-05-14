package tannyjung.tanshugetrees_handcode.systems.living_tree_mechanics;

import net.minecraft.world.level.LevelAccessor;
import tannyjung.tanshugetrees.TanshugetreesMod;
import tannyjung.tanshugetrees_handcode.misc.GameUtils;

public class SeasonDetector {

    private static int season_detector_tick = 0;

    public static void start (LevelAccessor level) {

        TanshugetreesMod.queueServerWork(20, () -> {

           start(level);

        });

        int posX = level.getLevelData().getXSpawn();
        int posZ = level.getLevelData().getZSpawn();
        int posY = level.getMinBuildHeight() + 1;

        if (season_detector_tick == 1 || season_detector_tick == 3 || season_detector_tick == 5 || season_detector_tick == 7 || season_detector_tick == 9) {

            GameUtils.runCommand(level, posX, posY, posZ, "fill ~ ~ ~ ~ ~1 ~ air");

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

                GameUtils.runCommand(level, posX, posY, posZ, "setblock ~ ~ ~ command_block[facing=up]{Command:\"TANSHUGETREES command season set " + season + "\"}");
                GameUtils.runCommand(level, posX, posY, posZ, "setblock ~ ~1 ~ sereneseasons:season_sensor[season=" + season_number + "]");

            }

        }

        season_detector_tick = season_detector_tick + 1;

    }

}
