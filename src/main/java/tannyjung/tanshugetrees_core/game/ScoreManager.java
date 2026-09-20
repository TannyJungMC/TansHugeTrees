package tannyjung.tanshugetrees_core.game;

import net.minecraft.network.chat.Component;
import net.minecraft.server.ServerScoreboard;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.ScoreHolder;
import net.minecraft.world.scores.Scoreboard;
import net.minecraft.world.scores.criteria.ObjectiveCriteria;

public class ScoreManager {

    public static void create (ServerLevel level_server, String name) {

        Scoreboard scoreboard = level_server.getServer().getScoreboard();
        Objective objective = scoreboard.getObjective(name);

        if (objective != null) {

            return;

        }

			/*
            (1.20.1)
            scoreboard.addObjective(name, ObjectiveCriteria.DUMMY, Component.literal(name), ObjectiveCriteria.RenderType.INTEGER);
            (1.21.1)
            scoreboard.addObjective(name, ObjectiveCriteria.DUMMY, Component.literal(name), ObjectiveCriteria.RenderType.INTEGER, true, null);
            */
        scoreboard.addObjective(name, ObjectiveCriteria.DUMMY, Component.literal(name), ObjectiveCriteria.RenderType.INTEGER, true, null);

    }

    public static int get (ServerLevel level_server, String objective, String player) {

        ServerScoreboard score = level_server.getServer().getScoreboard();
        Objective objective_test = score.getObjective(objective);

        if (objective_test == null) {

            return 0;

        }

			/*
            (1.20.1)
            return score.getOrCreatePlayerScore(player, objective_test).getScore();
            (1.21.1)
            return score.getOrCreatePlayerScore(ScoreHolder.forNameOnly(player), objective_test, false).get();
            */
        return score.getOrCreatePlayerScore(ScoreHolder.forNameOnly(player), objective_test, false).get();

    }

    public static void set (ServerLevel level_server, String objective, String player, int value) {

        ServerScoreboard score = level_server.getServer().getScoreboard();
        Objective objective_test = score.getObjective(objective);

        if (objective_test == null) {

            return;

        }

			/*
            (1.20.1)
            score.getOrCreatePlayerScore(player, objective_test).setScore(value);
            (1.21.1)
            score.getOrCreatePlayerScore(ScoreHolder.forNameOnly(player), objective_test, false).set(value);
            */
        score.getOrCreatePlayerScore(ScoreHolder.forNameOnly(player), objective_test, false).set(value);

    }

    public static void add (ServerLevel level_server, String objective, String player, int value) {

        ServerScoreboard score = level_server.getServer().getScoreboard();
        Objective objective_test = score.getObjective(objective);

        if (objective_test == null) {

            return;

        }

        int old_value = get(level_server, objective, player);

            /*
            (1.20.1)
            score.getOrCreatePlayerScore(player, objective_test).setScore(old_value + value);
            (1.21.1)
            score.getOrCreatePlayerScore(ScoreHolder.forNameOnly(player), objective_test, false).set(old_value + value);
            */
        score.getOrCreatePlayerScore(ScoreHolder.forNameOnly(player), objective_test, false).set(old_value + value);

    }

}
