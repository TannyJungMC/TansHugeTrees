package tannyjung.tanshugetrees_handcode.core;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import tannyjung.tanshugetrees_core.game.CommandMaker;
import tannyjung.tanshugetrees_handcode.systems.living_mechanics.Seasons;
import tannyjung.tanshugetrees_handcode.systems.tree_generator.*;

public class Commands {

    public static void registry (Object event) {

        CommandMaker.create(event, 2, "command / preset_fixer", run.command::preset_fixer);
        CommandMaker.create(event, 2, "command / seasons / get", run.command.seasons::get);
        CommandMaker.create(event, 2, "command / seasons / set / autumn", run.command.seasons.set::autumn);
        CommandMaker.create(event, 2, "command / seasons / set / spring", run.command.seasons.set::spring);
        CommandMaker.create(event, 2, "command / seasons / set / summer", run.command.seasons.set::summer);
        CommandMaker.create(event, 2, "command / seasons / set / winter", run.command.seasons.set::winter);
        CommandMaker.create(event, 2, "command / shape_file_generator / start / <number>", run.command.shape_file_generator::start);
        CommandMaker.create(event, 2, "command / shape_file_generator / stop", run.command.shape_file_generator::stop);
        CommandMaker.create(event, 2, "command / summon_tree / <text>", run.command::summon_tree);
        CommandMaker.create(event, 2, "command / summon_sapling_trader", run.command::summon_sapling_trader);
        
    }

    private static class run {

        private static class command {

            private static void preset_fixer (CommandContext<CommandSourceStack> data) {

                ServerLevel level_server = data.getSource().getLevel();
                PresetFixer.start(level_server);

            }

            private static class seasons {

                private static void get (CommandContext<CommandSourceStack> data) {

                    ServerLevel level_server = data.getSource().getLevel();
                    Entity entity = data.getSource().getEntity();

                    if (entity == null) {

                        return;

                    }

                    Player player = null;

                    if (entity instanceof Player == true) {

                        player = (Player) entity;

                    }

                    Seasons.get(level_server, player);

                }

                private static class set {

                    private static void autumn (CommandContext<CommandSourceStack> data) {

                        ServerLevel level_server = data.getSource().getLevel();
                        Seasons.set(level_server, "Autumn");

                    }

                    private static void spring (CommandContext<CommandSourceStack> data) {

                        ServerLevel level_server = data.getSource().getLevel();
                        Seasons.set(level_server, "Spring");

                    }

                    private static void summer (CommandContext<CommandSourceStack> data) {

                        ServerLevel level_server = data.getSource().getLevel();
                        Seasons.set(level_server, "Summer");

                    }

                    private static void winter (CommandContext<CommandSourceStack> data) {

                        ServerLevel level_server = data.getSource().getLevel();
                        Seasons.set(level_server, "Winter");

                    }

                }

            }

            private static class shape_file_generator {

                private static void start (CommandContext<CommandSourceStack> data) {

                    ServerLevel level_server = data.getSource().getLevel();
                    int variable_number = CommandMaker.Argument.getNumber(data);
                    ShapeFileGenerator.start(level_server, variable_number);

                }

                private static void stop (CommandContext<CommandSourceStack> data) {

                    ServerLevel level_server = data.getSource().getLevel();
                    ShapeFileGenerator.stop(level_server);

                }

            }

            private static void summon_tree (CommandContext<CommandSourceStack> data) {

                ServerLevel level_server = data.getSource().getLevel();
                Entity entity = data.getSource().getEntity();
                Vec3 vec3 = data.getSource().getPosition();
                String variable_text = CommandMaker.Argument.getText(data);

                ServerPlayer player_server = null;

                if (entity instanceof ServerPlayer == true) {

                    player_server = (ServerPlayer) entity;

                }

                TreeGenerator.create(level_server, player_server, BlockPos.containing(vec3), variable_text);

            }

            private static void summon_sapling_trader (CommandContext<CommandSourceStack> data) {

                ServerLevel level_server = data.getSource().getLevel();
                Vec3 vec3 = data.getSource().getPosition();
                SaplingTrader.summonTrader(level_server, BlockPos.containing(vec3));

            }

        }

    }

}