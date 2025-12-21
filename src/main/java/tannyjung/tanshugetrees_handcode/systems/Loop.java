package tannyjung.tanshugetrees_handcode.systems;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.LevelAccessor;
import tannyjung.tanshugetrees_core.game.GameUtils;
import tannyjung.tanshugetrees_handcode.Handcode;
import tannyjung.tanshugetrees.network.TanshugetreesModVariables;

import tannyjung.tanshugetrees_handcode.data.FileConfig;

public class Loop {

    private static int second = 0;
    private static boolean loop_tree_generator = false;
    private static boolean loop_tree_location = false;
    private static boolean loop_living_tree_mechanics_leaf_drop = false;
    private static boolean loop_living_tree_mechanics_leaf_litter_remover = false;
    private static int living_tree_mechanics_tick = 0;

    public static void start (LevelAccessor level_accessor, ServerLevel level_server) {

        if (Handcode.restart_loop == false) {

            Handcode.createDelayedWorks(false, 1, () -> start(level_accessor, level_server));
            run(level_accessor, level_server);

        }

    }

    private static void run (LevelAccessor level_accessor, ServerLevel level_server) {

        tick(level_accessor, level_server);

        // Second Loop
        {

            second = second + 1;

            if (second > 20) {

                second = 0;
                second(level_server);

            }

        }

    }

    private static void tick (LevelAccessor level_accessor, ServerLevel level_server) {

        if (loop_tree_generator == true) {

            // Tree Generator
            {

                if (FileConfig.tree_generator_speed_tick > 0) {

                    StringBuilder custom = new StringBuilder();

                    if (TanshugetreesModVariables.MapVariables.get(level_accessor).shape_file_converter == false) {

                        if (FileConfig.tree_generator_count_limit > 0) {

                            custom
                                    .append(",sort=nearest,limit=")
                                    .append(FileConfig.tree_generator_count_limit)
                            ;

                        }

                    }

                    GameUtils.command.run(false, level_server, 0, 0, 0, "execute at @p as @e[tag=TANSHUGETREES-tree_generator" + custom + "] at @s run TANSHUGETREES dev tree_generator");

                }

            }

        }

        // Living Tree Mechanics
        {

            if (loop_tree_location == true) {

                // Main
                {

                    if (FileConfig.living_tree_mechanics == true && FileConfig.living_tree_mechanics_tick > 0) {

                        living_tree_mechanics_tick = living_tree_mechanics_tick + 1;

                        if (living_tree_mechanics_tick >= FileConfig.living_tree_mechanics_tick) {

                            living_tree_mechanics_tick = 0;

                            if (Math.random() < (double) GameUtils.score.get(level_server, "TANSHUGETREES", "tree_location") / (double) FileConfig.living_tree_mechanics_simulation) {

                                GameUtils.command.run(false, level_server, 0, 0, 0, "execute as @e[tag=TANSHUGETREES-tree_location,limit=1,sort=random] at @s run TANSHUGETREES dev living_tree_mechanics loop");

                            }

                        }

                    }

                }

            }

            if (loop_living_tree_mechanics_leaf_drop == true) {

                GameUtils.command.run(false, level_server, 0, 0, 0, "execute as @e[tag=TANSHUGETREES-leaf_drop] at @s run TANSHUGETREES dev living_tree_mechanics leaf_drop");

            }

            if (loop_living_tree_mechanics_leaf_litter_remover == true) {

                GameUtils.command.run(false, level_server, 0, 0, 0, "execute as @e[tag=TANSHUGETREES-leaf_litter_remover] at @s run TANSHUGETREES dev living_tree_mechanics leaf_litter_remover");

            }

        }

    }

    private static void second (ServerLevel level_server) {

        boolean loop_delayed_command = GameUtils.command.result(level_server, 0, 0, 0, "execute if entity @e[tag=TANNYJUNG-delayed_command]");

        loop_tree_generator = GameUtils.command.result(level_server, 0, 0, 0, "execute if entity @e[tag=TANSHUGETREES-tree_generator]");
        loop_tree_location = GameUtils.command.result(level_server, 0, 0, 0, "execute if entity @e[tag=TANSHUGETREES-tree_location]");
        loop_living_tree_mechanics_leaf_drop = GameUtils.command.result(level_server, 0, 0, 0, "execute if entity @e[tag=TANSHUGETREES-leaf_drop]");
        loop_living_tree_mechanics_leaf_litter_remover = GameUtils.command.result(level_server, 0, 0, 0, "execute if entity @e[tag=TANSHUGETREES-leaf_litter_remover]");

        // Developer Mode
        {

            if (FileConfig.developer_mode == true) {

                GameUtils.command.run(false, level_server, 0, 0, 0, "execute at @e[type=marker] run particle end_rod ~ ~ ~ 0 0 0 0 1 force");

                // Delayed Command
                {

                    GameUtils.score.set(level_server, "TANSHUGETREES", "delayed_command", 0);

                    if (loop_delayed_command == true) {

                        GameUtils.command.run(false, level_server, 0, 0, 0, "execute at @e[tag=TANNYJUNG-delayed_command] run scoreboard players add delayed_command TANSHUGETREES 1");

                    }

                }

            }

        }

        // Tree Location
        {

            GameUtils.score.set(level_server, "TANSHUGETREES", "tree_location", 0);

            if (loop_tree_location == true) {

                GameUtils.command.run(false, level_server, 0, 0, 0, "execute at @e[tag=TANSHUGETREES-tree_location] run scoreboard players add tree_location TANSHUGETREES 1");

            }
        }

        // Living Tree Mechanics
        {

            GameUtils.score.set(level_server, "TANSHUGETREES", "leaf_drop", 0);
            GameUtils.score.set(level_server, "TANSHUGETREES", "leaf_litter_remover", 0);

            if (loop_living_tree_mechanics_leaf_drop == true) {

                GameUtils.command.run(false, level_server, 0, 0, 0, "execute at @e[tag=TANSHUGETREES-leaf_drop] run scoreboard players add leaf_drop TANSHUGETREES 1");

            }

            if (loop_living_tree_mechanics_leaf_litter_remover == true) {

                GameUtils.command.run(false, level_server, 0, 0, 0, "execute at @e[tag=TANSHUGETREES-leaf_litter_remover] run scoreboard players add leaf_litter_remover TANSHUGETREES 1");

            }

        }

        // Delayed Command
        {

            if (loop_delayed_command == true) {

                GameUtils.command.run(false, level_server, 0, 0, 0, "execute as @e[tag=TANNYJUNG-delayed_command] at @s run TANSHUGETREES dev delayed_command");

            }

        }

    }

}
