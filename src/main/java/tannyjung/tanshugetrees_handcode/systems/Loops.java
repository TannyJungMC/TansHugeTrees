package tannyjung.tanshugetrees_handcode.systems;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LevelAccessor;
import tannyjung.tanshugetrees_core.game.GameUtils;
import tannyjung.tanshugetrees_core.outside.TXTFunction;
import tannyjung.tanshugetrees_handcode.Handcode;
import tannyjung.tanshugetrees_handcode.systems.compatibility.CompatibilitySereneSeasons;
import tannyjung.tanshugetrees_handcode.systems.living_mechanics.LivingMechanics;
import tannyjung.tanshugetrees_handcode.systems.tree_generator.TreeGenerator;

public class Loops {

    public static boolean have_tree_generator = false;

    public static void tick (LevelAccessor level_accessor, ServerLevel level_server) {

        LivingMechanics.Loop.runTick();

        if (have_tree_generator == true) {

            // Tree Generator
            {

                if (Handcode.Config.tree_generator_speed_tick > 0) {

                    ServerPlayer player = level_server.getRandomPlayer();

                    if (player == null) {

                        return;

                    }

                    for (Entity entity : GameUtils.Mob.getAtArea(level_server, player.position(), 2000, true, Handcode.Config.tree_generator_count_limit, "minecraft:marker", "TANSHUGETREES-tree_generator")) {

                        TreeGenerator.run(level_accessor, entity);

                    }

                }

            }

        }

    }

    public static void second (LevelAccessor level_accessor, ServerLevel level_server) {

        have_tree_generator = GameUtils.Mob.getAtEverywhere(level_server, "minecraft:marker", "TANSHUGETREES-tree_generator").isEmpty() == false;
        LivingMechanics.Loop.runSecond(level_server);

    }

    public static void minute (LevelAccessor level_accessor, ServerLevel level_server) {

        CompatibilitySereneSeasons.loop(level_accessor, level_server);

    }

}
