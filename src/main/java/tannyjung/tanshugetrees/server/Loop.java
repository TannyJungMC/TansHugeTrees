package tannyjung.tanshugetrees.server;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.LevelAccessor;
import tannyjung.core.game.Utils;
import tannyjung.tanshugetrees_mcreator.network.TanshugetreesModVariables;
import tannyjung.tanshugetrees.Handcode;

import tannyjung.tanshugetrees.config.ConfigMain;

public class Loop {

    private static int second = 0;
    private static boolean loop_tree_generator = false;
    private static boolean loop_tree_location = false;
    private static boolean loop_living_tree_mechanics_leaf_drop = false;
    private static boolean loop_living_tree_mechanics_leaf_litter_remover = false;
    private static int living_tree_mechanics_tick = 0;

    public static void start (LevelAccessor level_accessor, ServerLevel level_server) {

        level_server.getServer().execute(() -> {

            run(level_accessor, level_server);

        });

    }

    private static void run (LevelAccessor level_accessor, ServerLevel level_server) {

        Handcode.createDelayedWorks(1, () -> {

            if (Handcode.system_pause == false) {

                run(level_accessor, level_server);

            }

        });

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

                if (ConfigMain.tree_generator_speed_tick > 0) {

                    StringBuilder custom = new StringBuilder();

                    if (TanshugetreesModVariables.MapVariables.get(level_accessor).shape_file_converter == false) {

                        if (ConfigMain.tree_generator_count_limit > 0) {

                            custom
                                    .append(",sort=nearest,limit=")
                                    .append(ConfigMain.tree_generator_count_limit)
                            ;

                        }

                    }

                    Utils.command.run(level_server, 0, 0, 0, "execute at @p as @e[tag=TANSHUGETREES-tree_generator" + custom + "] at @s run TANSHUGETREES dev tree_generator");

                }

            }

        }

        // Living Tree Mechanics
        {

            if (loop_tree_location == true) {

                // Main
                {

                    if (ConfigMain.living_tree_mechanics == true && ConfigMain.living_tree_mechanics_tick > 0) {

                        living_tree_mechanics_tick = living_tree_mechanics_tick + 1;

                        if (living_tree_mechanics_tick >= ConfigMain.living_tree_mechanics_tick) {

                            living_tree_mechanics_tick = 0;

                            if (Math.random() < (double) Utils.score.get(level_server, "TANSHUGETREES", "tree_location") / (double) ConfigMain.living_tree_mechanics_simulation) {

                                Utils.command.run(level_server, 0, 0, 0, "execute as @e[tag=TANSHUGETREES-tree_location,limit=1,sort=random] at @s run TANSHUGETREES dev living_tree_mechanics main");

                            }

                        }

                    }

                }

            }

            if (loop_living_tree_mechanics_leaf_drop == true) {

                Utils.command.run(level_server, 0, 0, 0, "execute as @e[tag=TANSHUGETREES-leaf_drop] at @s run TANSHUGETREES dev living_tree_mechanics leaf_drop");

            }

            if (loop_living_tree_mechanics_leaf_litter_remover == true) {

                Utils.command.run(level_server, 0, 0, 0, "execute as @e[tag=TANSHUGETREES-leaf_litter_remover] at @s run TANSHUGETREES dev living_tree_mechanics leaf_litter_remover");

            }

        }

    }

    private static void second (ServerLevel level_server) {

        boolean loop_delayed_command = Utils.command.result(level_server, 0, 0, 0, "execute if entity @e[tag=TANNYJUNG-delayed_command]");

        loop_tree_generator = Utils.command.result(level_server, 0, 0, 0, "execute if entity @e[tag=TANSHUGETREES-tree_generator]");
        loop_tree_location = Utils.command.result(level_server, 0, 0, 0, "execute if entity @e[tag=TANSHUGETREES-tree_location]");
        loop_living_tree_mechanics_leaf_drop = Utils.command.result(level_server, 0, 0, 0, "execute if entity @e[tag=TANSHUGETREES-leaf_drop]");
        loop_living_tree_mechanics_leaf_litter_remover = Utils.command.result(level_server, 0, 0, 0, "execute if entity @e[tag=TANSHUGETREES-leaf_litter_remover]");

        // Developer Mode
        {

            if (ConfigMain.developer_mode == true) {

                Utils.command.run(level_server, 0, 0, 0, "execute at @e[type=marker] run particle end_rod ~ ~ ~ 0 0 0 0 1 force");

                // Delayed Command
                {

                    Utils.score.set(level_server, "TANSHUGETREES", "delayed_command", 0);

                    if (loop_delayed_command == true) {

                        Utils.command.run(level_server, 0, 0, 0, "execute at @e[tag=TANNYJUNG-delayed_command] run scoreboard players add delayed_command TANSHUGETREES 1");

                    }

                }

            }

        }

        // Tree Location
        {

            Utils.score.set(level_server, "TANSHUGETREES", "tree_location", 0);

            if (loop_tree_location == true) {

                Utils.command.run(level_server, 0, 0, 0, "execute at @e[tag=TANSHUGETREES-tree_location] run scoreboard players add tree_location TANSHUGETREES 1");

            }
        }

        // Living Tree Mechanics
        {

            Utils.score.set(level_server, "TANSHUGETREES", "leaf_drop", 0);
            Utils.score.set(level_server, "TANSHUGETREES", "leaf_litter_remover", 0);

            if (loop_living_tree_mechanics_leaf_drop == true) {

                // Leaf Drop
                {

                    if (Handcode.VERSION_1192 == false) {

                        Utils.command.run(level_server, 0, 0, 0, "execute at @e[tag=TANSHUGETREES-leaf_drop] if loaded ~ ~ ~ run scoreboard players add leaf_drop TANSHUGETREES 1");

                    } else {

                        Utils.command.run(level_server, 0, 0, 0, "execute at @e[tag=TANSHUGETREES-leaf_drop] run scoreboard players add leaf_drop TANSHUGETREES 1");

                    }

                }

            }

            if (loop_living_tree_mechanics_leaf_litter_remover == true) {

                // Leaf Litter Remover
                {

                    if (Handcode.VERSION_1192 == false) {

                        Utils.command.run(level_server, 0, 0, 0, "execute at @e[tag=TANSHUGETREES-leaf_litter_remover] if loaded ~ ~ ~ run scoreboard players add leaf_litter_remover TANSHUGETREES 1");

                    } else {

                        Utils.command.run(level_server, 0, 0, 0, "execute at @e[tag=TANSHUGETREES-leaf_litter_remover] run scoreboard players add leaf_litter_remover TANSHUGETREES 1");

                    }

                }

            }

        }

        // Delayed Command
        {

            if (loop_delayed_command == true) {

                Utils.command.run(level_server, 0, 0, 0, "execute as @e[tag=TANNYJUNG-delayed_command] at @s run TANSHUGETREES dev tree_function_in_loaded_chunk");

            }

        }

    }

}
