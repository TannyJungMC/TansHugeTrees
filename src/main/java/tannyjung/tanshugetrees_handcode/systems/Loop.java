package tannyjung.tanshugetrees_handcode.systems;

import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.fml.ModList;
import tannyjung.tanshugetrees.TanshugetreesMod;
import tannyjung.tanshugetrees.network.TanshugetreesModVariables;
import tannyjung.tanshugetrees.procedures.AutoGenLoopTickProcedure;
import tannyjung.tanshugetrees_handcode.Handcode;
import tannyjung.tanshugetrees_handcode.misc.GameUtils;
import tannyjung.tanshugetrees_handcode.systems.config.ConfigMain;

public class Loop {

    private static int second = 1;
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

        if (TanshugetreesModVariables.MapVariables.get(level).auto_gen == true) {

            GameUtils.runCommand(level, 0, 0, 0, "execute in tanshugetrees:dimension if entity @e[name=THT-random_tree] positioned 0 0 0 as @e[name=THT-random_tree,limit=1,distance=..1000] at @s run tanshugetrees dev random_tree run");

        }

        // Random Tree
        {

            if (GameUtils.commandResult(level, 0, 0, 0, "execute if entity @e[name=THT-random_tree]") == true) {

                if (TanshugetreesModVariables.MapVariables.get(level).auto_gen == false && ConfigMain.global_speed > 0) {

                    StringBuilder custom = new StringBuilder();

                    if (ConfigMain.count_limit > 0) {

                        custom.append(",sort=nearest,limit=" + ConfigMain.count_limit);

                    }

                    if (ConfigMain.distance_limit > 0) {

                        custom.append(",distance=.." + ConfigMain.distance_limit);

                    }

                    GameUtils.runCommand(level, 0, 0, 0, "execute at @p as @e[name=THT-random_tree" + custom + "] at @s run tanshugetrees dev random_tree run");

                }

            }

        }

    }

    private static void second (LevelAccessor level) {

        living_tree_mechanics_second(level);

        // Developer Mode
        {

            if (ConfigMain.developer_mode == true) {

                GameUtils.runCommand(level, 0, 0, 0, "execute at @a at @e[type=marker,tag=TANSHUGETREES,distance=..100] run particle end_rod ~ ~ ~ 0 0 0 0 1 force");

            }

        }

        // Auto Gen
        {

            if (TanshugetreesModVariables.MapVariables.get(level).auto_gen == true) {

                if (GameUtils.commandResult(level, 0, 0, 0, "execute in tanshugetrees:dimension positioned 0 0 0 unless entity @e[tag=THT-random_tree,distance=..1000]") == true) {

                    AutoGenLoopTickProcedure.execute(level, 0, 0, 0);

                }

            }

        }

    }

    private static void living_tree_mechanics_tick (LevelAccessor level) {

        if (ConfigMain.developer_mode == true) {

            // GameUtils.runCommand(level, 0, 0, 0, "execute as @e[name=TANSHUGETREES-leaf_drop] at @s if loaded ~ ~ ~ positioned as @a positioned ~ ~1.5 ~ facing entity @s feet run particle minecraft:soul_fire_flame ^ ^ ^10 0 0 0 0 1 force");
            // GameUtils.runCommand(level, 0, 0, 0, "execute as @e[name=TANSHUGETREES-leaf_litter_remover] at @s if loaded ~ ~ ~ positioned as @a positioned ~ ~1.5 ~ facing entity @s feet run particle minecraft:flame ^ ^ ^10 0 0 0 0 1 force");

        }

        // Main
        {

            if (ConfigMain.living_tree_mechanics_tick > 0) {

                living_tree_mechanics_tick = living_tree_mechanics_tick + 1;

                if (living_tree_mechanics_tick >= ConfigMain.living_tree_mechanics_tick) {

                    living_tree_mechanics_tick = 0;

                    if (Math.random() < (double) GameUtils.scoreGet(level, "tree_location") / (double) ConfigMain.living_tree_mechanics_simulation) {

                        if (GameUtils.commandResult(level, 0, 0, 0, "execute if entity @e[tag=TANSHUGETREES-tree_location]") == true) {

                            GameUtils.runCommand(level, 0, 0, 0, "execute as @e[tag=TANSHUGETREES-tree_location,limit=1,sort=random] at @s run TANSHUGETREES dev living_tree_mechanics loop_tick");

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
        GameUtils.scoreSet(level, "leaf_drop", 0);
        GameUtils.scoreSet(level, "leaf_litter_remover", 0);

        if (Handcode.version_1192 == false) {

            GameUtils.runCommand(level, 0, 0, 0, "execute at @e[tag=TANSHUGETREES-tree_location] if loaded ~ ~ ~ run scoreboard players add tree_location TANSHUGETREES 1");
            GameUtils.runCommand(level, 0, 0, 0, "execute at @e[name=TANSHUGETREES-leaf_drop] if loaded ~ ~ ~ run scoreboard players add leaf_drop TANSHUGETREES 1");
            GameUtils.runCommand(level, 0, 0, 0, "execute at @e[name=TANSHUGETREES-leaf_litter_remover] if loaded ~ ~ ~ run scoreboard players add leaf_litter_remover TANSHUGETREES 1");

        } else {

            GameUtils.runCommand(level, 0, 0, 0, "execute at @e[tag=TANSHUGETREES-tree_location] run scoreboard players add tree_location TANSHUGETREES 1");
            GameUtils.runCommand(level, 0, 0, 0, "execute at @e[name=TANSHUGETREES-leaf_drop] run scoreboard players add leaf_drop TANSHUGETREES 1");
            GameUtils.runCommand(level, 0, 0, 0, "execute at @e[name=TANSHUGETREES-leaf_litter_remover] run scoreboard players add leaf_litter_remover TANSHUGETREES 1");


        }

    }

}
