package tannyjung.tanshugetrees_handcode.systems;

import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.fml.ModList;
import tannyjung.tanshugetrees.TanshugetreesMod;
import tannyjung.tanshugetrees_handcode.Handcode;
import tannyjung.tanshugetrees_handcode.misc.GameUtils;
import tannyjung.tanshugetrees_handcode.systems.config.ConfigMain;

import java.lang.reflect.Method;

public class Loop {

    private static int second = 1;

    private static int season_detector_tick = 0;
    private static int living_tree_mechanics_tick = 0;

    public static void start (LevelAccessor level) {

        if (GameUtils.playerCount(level) > 0) {

            TanshugetreesMod.queueServerWork(1, () -> {

                start(level);

            });

            tick(level);

            // Second Loop
            {

                second = second + 1;

                if (second > 20) {

                    second = 0;
                    second(level);

                }

            }

        }

    }

    private static void tick (LevelAccessor level) {

        living_tree_mechanics_tick(level);

    }

    private static void second (LevelAccessor level) {

        season_detector(level);
        living_tree_mechanics_second(level);

    }

    private static void season_detector (LevelAccessor level) {

        TanshugetreesMod.LOGGER.info(season_detector_tick);

        if (season_detector_tick == 0) {

            if (ConfigMain.season_detector == true && ModList.get().isLoaded("sereneseasons") == true) {

                season_detector_tick = 1;

            } else {

                return;

            }

        }

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

            TanshugetreesMod.LOGGER.info(">>>");
            TanshugetreesMod.LOGGER.info("setblock ~ ~ ~ command_block[facing=up]{Command:\"TANSHUGETREES command season set " + season + "\"}");
            TanshugetreesMod.LOGGER.info("setblock ~ ~1 ~ sereneseasons:season_sensor[season=" + season_number + "]");


            GameUtils.runCommand(level, posX, posY, posZ, "setblock ~ ~ ~ command_block[facing=up]{Command:\"TANSHUGETREES command season set " + season + "\"}");
            GameUtils.runCommand(level, posX, posY, posZ, "setblock ~ ~1 ~ sereneseasons:season_sensor[season=" + season_number + "]");

        }

        season_detector_tick = season_detector_tick + 1;

    }

    private static void living_tree_mechanics_tick (LevelAccessor level) {

        // Main
        {

            if (ConfigMain.rt_dynamic_tick > 0) {

                living_tree_mechanics_tick = living_tree_mechanics_tick + 1;

                if (living_tree_mechanics_tick >= ConfigMain.rt_dynamic_tick) {

                    living_tree_mechanics_tick = 0;

                    {

                        boolean pass = false;

                        if (ConfigMain.rt_dynamic_simulation > GameUtils.scoreGet(level, "tree_location")) {

                            int random = ConfigMain.rt_dynamic_simulation - GameUtils.scoreGet(level, "tree_location");
                            pass = Mth.nextInt(RandomSource.create(), 0, random) == 0;

                        } else {

                            pass = true;

                        }

                        if (pass == true) {

                            if (GameUtils.commandResult(level, 0, 0, 0, "execute if entity @e[tag=TANSHUGETREES-tree_location]") == true) {

                                GameUtils.runCommand(level, 0, 0, 0, "execute as @e[tag=TANSHUGETREES-tree_location,limit=1,sort=random] at @s run TANSHUGETREES dev living_tree_mechanics loop_tick");

                            }

                        }

                    }

                }

            }

        }

        // Leaf Drop
        {

            if (GameUtils.commandResult(level, 0, 0, 0, "execute if entity @e[name=TANSHUGETREES-leaf_drop]") == true) {

                GameUtils.runCommand(level, 0, 0, 0, "execute as @e[name=TANSHUGETREES-leaf_drop] at @s run TANSHUGETREES dev living_tree_mechanics leaf_drop_loop_tick");

            }

        }

        // Leaf Litter Remover
        {

            if (GameUtils.commandResult(level, 0, 0, 0, "execute if entity @e[name=TANSHUGETREES-leaf_litter_remover]") == true) {

                GameUtils.runCommand(level, 0, 0, 0, "execute as @e[name=TANSHUGETREES-leaf_litter_remover] at @s run TANSHUGETREES dev living_tree_mechanics leaf_litter_remover_loop_tick");

            }

        }

    }

    private static void living_tree_mechanics_second (LevelAccessor level) {

        GameUtils.scoreSet(level, "tree_location", 0);
        GameUtils.runCommand(level, 0, 0, 0, "execute at @e[tag=TANSHUGETREES-tree_location] run scoreboard players add tree_location TANSHUGETREES 1");

        GameUtils.scoreSet(level, "leaf_drop", 0);
        GameUtils.runCommand(level, 0, 0, 0, "execute at @e[name=TANSHUGETREES-leaf_drop] run scoreboard players add leaf_drop TANSHUGETREES 1");
        GameUtils.scoreSet(level, "leaf_litter_remover", 0);
        GameUtils.runCommand(level, 0, 0, 0, "execute at @e[name=TANSHUGETREES-leaf_litter_remover] run scoreboard players add leaf_litter_remover TANSHUGETREES 1");

    }

}
