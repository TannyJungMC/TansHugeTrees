package tannyjung.tanshugetrees_handcode.systems;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LevelAccessor;
import tannyjung.tanshugetrees_core.game.GameUtils;
import tannyjung.tanshugetrees_core.outside.TXTFunction;
import tannyjung.tanshugetrees_handcode.Handcode;
import tannyjung.tanshugetrees_handcode.data.FileConfig;
import tannyjung.tanshugetrees_handcode.systems.compatibility.CompatibilitySereneSeasons;
import tannyjung.tanshugetrees_handcode.systems.living_mechanics.LivingMechanics;
import tannyjung.tanshugetrees_handcode.systems.living_mechanics.LivingMechanicsLeafDrop;
import tannyjung.tanshugetrees_handcode.systems.living_mechanics.LivingMechanicsLeafLitterRemover;
import tannyjung.tanshugetrees_handcode.systems.tree_generator.TreeGenerator;

import java.util.List;

public class Loops {

    private static boolean loop_tree_generator = false;
    private static boolean loop_tree_location = false;
    private static boolean loop_living_mechanics_leaf_drop = false;
    private static boolean loop_living_mechanics_leaf_litter_remover = false;
    private static int living_mechanics_tick = 0;

    public static void tick (LevelAccessor level_accessor, ServerLevel level_server) {

        if (loop_tree_generator == true) {

            // Tree Generator
            {

                if (FileConfig.tree_generator_speed_tick > 0) {

                    ServerPlayer player = level_server.getRandomPlayer();

                    if (player != null) {

                        for (Entity entity : GameUtils.Mob.getAtArea(level_server, player.position(), 2000, true, FileConfig.tree_generator_count_limit, "minecraft:marker", "TANSHUGETREES-tree_generator")) {

                            TreeGenerator.run(level_accessor, entity);

                        }

                    }

                }

            }

        }

        // Living Mechanics
        {

            if (loop_tree_location == true) {

                // Main
                {

                    if (FileConfig.living_mechanics == true && FileConfig.living_mechanics_tick > 0) {

                        living_mechanics_tick = living_mechanics_tick + 1;

                        if (living_mechanics_tick >= FileConfig.living_mechanics_tick) {

                            living_mechanics_tick = 0;

                            if (Math.random() < (double) GameUtils.Score.get(level_server, "TANSHUGETREES", "tree_location") / (double) FileConfig.living_mechanics_simulation) {

                                List<Entity> entities = GameUtils.Mob.getAtEverywhere(level_server, "minecraft:marker", "TANSHUGETREES-tree_location");

                                if (entities.isEmpty() == false) {

                                    LivingMechanics.start(entities.get(Mth.nextInt(RandomSource.create(), 0, entities.size() - 1)));

                                }

                            }

                        }

                    }

                }

            }

            if (loop_living_mechanics_leaf_drop == true) {

                for (Entity entity : GameUtils.Mob.getAtEverywhere(level_server, "minecraft:block_display", "TANSHUGETREES-leaf_drop")) {

                    LivingMechanicsLeafDrop.start(entity);

                }

            }

            if (loop_living_mechanics_leaf_litter_remover == true) {

                for (Entity entity : GameUtils.Mob.getAtEverywhere(level_server, "minecraft:marker", "TANSHUGETREES-leaf_litter_remover")) {

                    LivingMechanicsLeafLitterRemover.start(entity);

                }

            }

        }

    }

    public static void second (LevelAccessor level_accessor, ServerLevel level_server) {

        boolean loop_delayed_command = GameUtils.Mob.getAtEverywhere(level_server, "minecraft:marker", "TANSHUGETREES-delayed_command").isEmpty() == false;
        loop_tree_generator = GameUtils.Mob.getAtEverywhere(level_server, "minecraft:marker", "TANSHUGETREES-tree_generator").isEmpty() == false;
        loop_tree_location = GameUtils.Mob.getAtEverywhere(level_server, "minecraft:marker", "TANSHUGETREES-tree_location").isEmpty() == false;
        loop_living_mechanics_leaf_drop = GameUtils.Mob.getAtEverywhere(level_server, "minecraft:marker", "TANSHUGETREES-tree_location").isEmpty() == false;
        loop_living_mechanics_leaf_litter_remover = GameUtils.Mob.getAtEverywhere(level_server, "minecraft:marker", "TANSHUGETREES-leaf_litter_remover").isEmpty() == false;

        // Developer Mode
        {

            if (FileConfig.developer_mode == true) {

                for (Entity entity : GameUtils.Mob.getAtEverywhere(level_server, "", "TANSHUGETREES")) {

                    GameUtils.Misc.spawnParticle(level_server, entity.position(), 0, 0, 0, 0, 1, "minecraft:end_rod");

                }

                // Delayed Command
                {

                    if (loop_delayed_command == true) {

                        GameUtils.Score.set(level_server, "TANSHUGETREES", "delayed_command", GameUtils.Mob.getAtEverywhere(level_server, "minecraft:marker", "TANSHUGETREES-delayed_command").size());

                    }

                }

            }

        }

        // Tree Location
        {

            if (loop_tree_location == true) {

                GameUtils.Score.set(level_server, "TANSHUGETREES", "tree_location", GameUtils.Mob.getAtEverywhere(level_server, "minecraft:marker", "TANSHUGETREES-tree_location").size());

            }
        }

        // Living Mechanics
        {

            if (loop_living_mechanics_leaf_drop == true) {

                GameUtils.Score.set(level_server, "TANSHUGETREES", "leaf_drop", GameUtils.Mob.getAtEverywhere(level_server, "minecraft:block_display", "TANSHUGETREES-leaf_drop").size());

            }

            if (loop_living_mechanics_leaf_litter_remover == true) {

                GameUtils.Score.set(level_server, "TANSHUGETREES", "leaf_litter_remover", GameUtils.Mob.getAtEverywhere(level_server, "minecraft:marker", "TANSHUGETREES-leaf_litter_remover").size());

            }

        }

        // Delayed Command
        {

            if (loop_delayed_command == true) {

                for (Entity entity : GameUtils.Mob.getAtEverywhere(level_server, "", "TANSHUGETREES-delayed_command")) {

                    TXTFunction.runDelayedCommand(level_server, entity);

                }

            }

        }

    }

    public static void minute (LevelAccessor level_accessor, ServerLevel level_server) {

        if (Handcode.compatibility_serene_seasons == true) {

            CompatibilitySereneSeasons.loop(level_accessor, level_server);

        }

    }

}
