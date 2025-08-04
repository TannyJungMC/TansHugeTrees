package tannyjung.tanshugetrees_handcode.systems;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.LevelAccessor;
import tannyjung.tanshugetrees.TanshugetreesMod;
import tannyjung.tanshugetrees.network.TanshugetreesModVariables;
import tannyjung.tanshugetrees_handcode.Handcode;
import tannyjung.core.GameUtils;
import tannyjung.tanshugetrees_handcode.config.ConfigMain;
import tannyjung.tanshugetrees_handcode.config.PackUpdate;

public class Loop {

    private static int second = 0;
    private static boolean loop_tree_generator = false;
    private static boolean loop_tree_location = false;
    private static boolean loop_living_tree_mechanics_leaf_drop = false;
    private static boolean loop_living_tree_mechanics_leaf_litter_remover = false;
    private static int living_tree_mechanics_tick = 0;

    public static void start (LevelAccessor level_accessor, ServerLevel level_server) {

        if (Handcode.world_active == true) {

            TanshugetreesMod.queueServerWork(1, () -> {

                start(level_accessor, level_server);

            });

            if (PackUpdate.install_pause_systems == false) {

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

        }

    }

    private static void tick (LevelAccessor level_accessor, ServerLevel level_server) {

        if (loop_tree_generator == true) {

            // Tree Generator
            {

                    if (TanshugetreesModVariables.MapVariables.get(level_accessor).shape_file_converter == false) {

                        // From Saplings
                        {

                            if (ConfigMain.tree_generator_speed_tick > 0) {

                                StringBuilder custom = new StringBuilder();

                                if (ConfigMain.tree_generator_count_limit > 0) {

                                    custom
                                            .append(",sort=nearest,limit=")
                                            .append(ConfigMain.tree_generator_count_limit)
                                    ;

                                }

                                GameUtils.command.run(level_server, 0, 0, 0, "execute at @p as @e[tag=TANSHUGETREES-tree_generator" + custom + "] at @s run TANSHUGETREES dev tree_generator");

                            }

                        }

                    } else {

                        // From Converter
                        {

                            GameUtils.command.run(level_server, 0, 0, 0, "execute at @p in tanshugetrees:tanshugetrees_dimension positioned 0 0 0 as @e[tag=TANSHUGETREES-tree_generator,limit=1,sort=nearest] at @s run TANSHUGETREES dev tree_generator");

                        }

                    }

                }

        }

        // Living Tree Mechanics
        {

            if (loop_tree_location == true) {

                // Main
                {

                    if (ConfigMain.living_tree_mechanics_tick > 0) {

                        living_tree_mechanics_tick = living_tree_mechanics_tick + 1;

                        if (living_tree_mechanics_tick >= ConfigMain.living_tree_mechanics_tick) {

                            living_tree_mechanics_tick = 0;

                            if (Math.random() < (double) GameUtils.score.get(level_server, "TANSHUGETREES", "tree_location") / (double) ConfigMain.living_tree_mechanics_simulation) {

                                if (GameUtils.command.result(level_server, 0, 0, 0, "execute if entity @e[tag=TANSHUGETREES-tree_location]") == true) {

                                    GameUtils.command.run(level_server, 0, 0, 0, "execute as @e[tag=TANSHUGETREES-tree_location,limit=1,sort=random] at @s run TANSHUGETREES dev living_tree_mechanics main");

                                }

                            }

                        }

                    }

                }

            }

            if (loop_living_tree_mechanics_leaf_drop == true) {

                GameUtils.command.run(level_server, 0, 0, 0, "execute as @e[tag=TANSHUGETREES-leaf_drop] at @s run TANSHUGETREES dev living_tree_mechanics leaf_drop");

            }

            if (loop_living_tree_mechanics_leaf_litter_remover == true) {

                GameUtils.command.run(level_server, 0, 0, 0, "execute as @e[tag=TANSHUGETREES-leaf_litter_remover] at @s run TANSHUGETREES dev living_tree_mechanics leaf_litter_remover");

            }

        }

    }

    private static void second (ServerLevel level_server) {

        // Developer Mode
        {

            if (ConfigMain.developer_mode == true) {

                GameUtils.command.run(level_server, 0, 0, 0, "execute at @e[tag=TANSHUGETREES] run particle end_rod ~ ~ ~ 0 0 0 0 1 force");

            }

        }

        // Tree Function
        {

            if (GameUtils.command.result(level_server, 0, 0, 0, "execute if entity @e[tag=TANSHUGETREES-tree_function_in_loaded_chunk]") == true) {

                if (Handcode.version_1192 == false) {

                    GameUtils.command.run(level_server, 0, 0, 0, "execute as @e[tag=TANSHUGETREES-tree_function_in_loaded_chunk] at @s if loaded ~ ~ ~ run TANSHUGETREES dev tree_function_in_loaded_chunk");

                } else {

                    GameUtils.command.run(level_server, 0, 0, 0, "execute as @e[tag=TANSHUGETREES-tree_function_in_loaded_chunk] at @s run TANSHUGETREES dev tree_function_in_loaded_chunk");

                }

            }

        }

        loop_tree_generator = GameUtils.command.result(level_server, 0, 0, 0, "execute if entity @e[tag=TANSHUGETREES-tree_generator]");
        loop_tree_location = GameUtils.command.result(level_server, 0, 0, 0, "execute if entity @e[tag=TANSHUGETREES-tree_location]");
        loop_living_tree_mechanics_leaf_drop = GameUtils.command.result(level_server, 0, 0, 0, "execute if entity @e[tag=TANSHUGETREES-leaf_drop]");
        loop_living_tree_mechanics_leaf_litter_remover = GameUtils.command.result(level_server, 0, 0, 0, "execute if entity @e[tag=TANSHUGETREES-leaf_litter_remover]");

        if (loop_tree_location == true) {

            // Tree Location
            {

                GameUtils.score.set(level_server, "TANSHUGETREES", "tree_location", 0);

                if (Handcode.version_1192 == false) {

                    GameUtils.command.run(level_server, 0, 0, 0, "execute at @e[tag=TANSHUGETREES-tree_location] if loaded ~ ~ ~ run scoreboard players add tree_location TANSHUGETREES 1");

                } else {

                    GameUtils.command.run(level_server, 0, 0, 0, "execute at @e[tag=TANSHUGETREES-tree_location] run scoreboard players add tree_location TANSHUGETREES 1");


                }

            }

        }

        // Living Tree Mechanics
        {

            GameUtils.score.set(level_server, "TANSHUGETREES", "leaf_drop", 0);
            GameUtils.score.set(level_server, "TANSHUGETREES", "leaf_litter_remover", 0);

            if (loop_living_tree_mechanics_leaf_drop == true) {

                // Leaf Drop
                {

                    if (Handcode.version_1192 == false) {

                        GameUtils.command.run(level_server, 0, 0, 0, "execute at @e[tag=TANSHUGETREES-leaf_drop] if loaded ~ ~ ~ run scoreboard players add leaf_drop TANSHUGETREES 1");

                    } else {

                        GameUtils.command.run(level_server, 0, 0, 0, "execute at @e[tag=TANSHUGETREES-leaf_drop] run scoreboard players add leaf_drop TANSHUGETREES 1");


                    }

                }

            }

            if (loop_living_tree_mechanics_leaf_litter_remover == true) {

                // Leaf Litter Remover
                {

                    if (Handcode.version_1192 == false) {

                        GameUtils.command.run(level_server, 0, 0, 0, "execute at @e[tag=TANSHUGETREES-leaf_litter_remover] if loaded ~ ~ ~ run scoreboard players add leaf_litter_remover TANSHUGETREES 1");

                    } else {

                        GameUtils.command.run(level_server, 0, 0, 0, "execute at @e[tag=TANSHUGETREES-leaf_litter_remover] run scoreboard players add leaf_litter_remover TANSHUGETREES 1");


                    }

                }

            }

        }

    }

}
