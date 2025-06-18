package tannyjung.tanshugetrees_handcode.systems;

import net.minecraft.world.level.LevelAccessor;
import tannyjung.tanshugetrees.TanshugetreesMod;
import tannyjung.tanshugetrees.network.TanshugetreesModVariables;
import tannyjung.tanshugetrees_handcode.Handcode;
import tannyjung.misc.GameUtils;
import tannyjung.tanshugetrees_handcode.config.ConfigMain;
import tannyjung.tanshugetrees_handcode.config.UpdateRun;

public class Loop {

    private static int second = 1;
    private static int living_tree_mechanics_tick = 0;

    public static void start (LevelAccessor level) {

        if (GameUtils.misc.playerCount(level) > 0) {

            TanshugetreesMod.queueServerWork(1, () -> {

                start(level);

            });

            if (UpdateRun.install_pause_systems == false) {

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

    }

    private static void tick (LevelAccessor level) {

        // Tree Generator
        {

            if (GameUtils.command.result(level, 0, 0, 0, "execute if entity @e[tag=TANSHUGETREES-tree_generator]") == true) {

                if (TanshugetreesModVariables.MapVariables.get(level).auto_gen == false) {

                    // From Saplings
                    {

                        if (ConfigMain.global_speed > 0) {

                            StringBuilder custom = new StringBuilder();

                            if (ConfigMain.count_limit > 0) {

                                custom
                                        .append(",sort=nearest,limit=")
                                        .append(ConfigMain.count_limit)
                                ;

                            }

                            GameUtils.command.run(level, 0, 0, 0, "execute at @p as @e[tag=TANSHUGETREES-tree_generator" + custom + "] at @s run TANSHUGETREES dev tree_generator loop_tick");

                        }

                    }

                } else {

                    // From Converter
                    {

                        GameUtils.command.run(level, 0, 0, 0, "execute at @p in tanshugetrees:dimension positioned 0 0 0 as @e[tag=TANSHUGETREES-tree_generator,limit=1,sort=nearest] at @s run TANSHUGETREES dev tree_generator loop_tick");

                    }

                }

            }

        }

        // Living Tree Mechanics
        {

            // Main
            {

                if (ConfigMain.living_tree_mechanics_tick > 0) {

                    living_tree_mechanics_tick = living_tree_mechanics_tick + 1;

                    if (living_tree_mechanics_tick >= ConfigMain.living_tree_mechanics_tick) {

                        living_tree_mechanics_tick = 0;

                        if (Math.random() < (double) GameUtils.score.get(level, "TANSHUGETREES", "tree_location") / (double) ConfigMain.living_tree_mechanics_simulation) {

                            if (GameUtils.command.result(level, 0, 0, 0, "execute if entity @e[tag=TANSHUGETREES-tree_location]") == true) {

                                GameUtils.command.run(level, 0, 0, 0, "execute as @e[tag=TANSHUGETREES-tree_location,limit=1,sort=random] at @s run TANSHUGETREES dev living_tree_mechanics loop_tick");

                            }

                        }

                    }

                }

            }

            // Leaf Drop
            {

                if (GameUtils.command.result(level, 0, 0, 0, "execute if entity @e[tag=TANSHUGETREES-leaf_drop]") == true) {

                    GameUtils.command.run(level, 0, 0, 0, "execute as @e[tag=TANSHUGETREES-leaf_drop] at @s run TANSHUGETREES dev living_tree_mechanics leaf_drop_loop_tick");

                }

            }

            // Leaf Litter Remover
            {

                if (GameUtils.command.result(level, 0, 0, 0, "execute if entity @e[tag=TANSHUGETREES-leaf_litter_remover]") == true) {

                    GameUtils.command.run(level, 0, 0, 0, "execute as @e[tag=TANSHUGETREES-leaf_litter_remover] at @s run TANSHUGETREES dev living_tree_mechanics leaf_litter_remover_loop_tick");

                }

            }

        }

    }

    private static void second (LevelAccessor level) {

        // Developer Mode
        {

            if (ConfigMain.developer_mode == true) {

                GameUtils.command.run(level, 0, 0, 0, "execute at @e[tag=TANSHUGETREES-tree_location] run particle end_rod ~ ~ ~ 0 0 0 0 1 force");

            }

        }

        // Living Tree Mechanics
        {

            GameUtils.score.set(level, "TANSHUGETREES", "tree_location", 0);
            GameUtils.score.set(level, "TANSHUGETREES", "leaf_drop", 0);
            GameUtils.score.set(level, "TANSHUGETREES", "leaf_litter_remover", 0);

            if (Handcode.version_1192 == false) {

                GameUtils.command.run(level, 0, 0, 0, "execute at @e[tag=TANSHUGETREES-tree_location] if loaded ~ ~ ~ run scoreboard players add tree_location TANSHUGETREES 1");
                GameUtils.command.run(level, 0, 0, 0, "execute at @e[tag=TANSHUGETREES-leaf_drop] if loaded ~ ~ ~ run scoreboard players add leaf_drop TANSHUGETREES 1");
                GameUtils.command.run(level, 0, 0, 0, "execute at @e[tag=TANSHUGETREES-leaf_litter_remover] if loaded ~ ~ ~ run scoreboard players add leaf_litter_remover TANSHUGETREES 1");

            } else {

                GameUtils.command.run(level, 0, 0, 0, "execute at @e[tag=TANSHUGETREES-tree_location] run scoreboard players add tree_location TANSHUGETREES 1");
                GameUtils.command.run(level, 0, 0, 0, "execute at @e[tag=TANSHUGETREES-leaf_drop] run scoreboard players add leaf_drop TANSHUGETREES 1");
                GameUtils.command.run(level, 0, 0, 0, "execute at @e[tag=TANSHUGETREES-leaf_litter_remover] run scoreboard players add leaf_litter_remover TANSHUGETREES 1");


            }

        }

    }

}
