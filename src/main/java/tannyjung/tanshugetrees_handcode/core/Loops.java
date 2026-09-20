package tannyjung.tanshugetrees_handcode.core;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import tannyjung.tanshugetrees_core.game.EntityManager;
import tannyjung.tanshugetrees_handcode.Handcode;
import tannyjung.tanshugetrees_handcode.systems.compatibility.CompatibilitySereneSeasons;
import tannyjung.tanshugetrees_handcode.systems.living_mechanics.LivingMechanics;
import tannyjung.tanshugetrees_handcode.systems.tree_generator.TreeGenerator;

public class Loops {

    public static boolean have_tree_generator = false;
    private static int loop_serene_season = 0;

    public static void tick (ServerLevel level_server) {

        LivingMechanics.Loop.runTick();

        if (have_tree_generator == true) {

            // Tree Generator
            {

                if (Handcode.Config.tree_generator_speed_tick > 0) {

                    ServerPlayer player_server = level_server.getRandomPlayer();

                    if (player_server == null) {

                        return;

                    }

                    for (Entity entity : EntityManager.Population.getArea(level_server, player_server.position(), 2000, "", "", new String[]{"TANSHUGETREES-tree_generator"})) {

                        TreeGenerator.run(level_server, entity);

                    }

                }

            }

        }

    }

    public static void second (ServerLevel level_server) {

        have_tree_generator = EntityManager.Population.getEverywhereStatic(level_server, "", "", new String[]{"TANSHUGETREES-tree_generator"}).isEmpty() == false;
        LivingMechanics.Loop.runSecond(level_server);

        if (loop_serene_season > 1) {

            loop_serene_season = loop_serene_season - 1;

        } else {

            loop_serene_season = 1200;
            CompatibilitySereneSeasons.loop(level_server);

        }

    }

}
