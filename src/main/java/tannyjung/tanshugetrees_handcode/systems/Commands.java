package tannyjung.tanshugetrees_handcode.systems;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.Vec3;
import tannyjung.tanshugetrees_core.game.CommandMaker;
import tannyjung.tanshugetrees_handcode.systems.living_mechanics.Seasons;
import tannyjung.tanshugetrees_handcode.systems.tree_generator.*;

public class Commands {

    public static void registry (Object event) {

        CommandMaker.create(event, 2, "TANSHUGETREES / command / preset_fixer", run.command::preset_fixer);
        CommandMaker.create(event, 2, "TANSHUGETREES / command / seasons / get", run.command.seasons::get);
        CommandMaker.create(event, 2, "TANSHUGETREES / command / seasons / set / autumn", run.command.seasons.set::autumn);
        CommandMaker.create(event, 2, "TANSHUGETREES / command / seasons / set / spring", run.command.seasons.set::spring);
        CommandMaker.create(event, 2, "TANSHUGETREES / command / seasons / set / summer", run.command.seasons.set::summer);
        CommandMaker.create(event, 2, "TANSHUGETREES / command / seasons / set / winter", run.command.seasons.set::winter);
        CommandMaker.create(event, 2, "TANSHUGETREES / command / shape_file_converter / start / <number>", run.command.shape_file_converter::start);
        CommandMaker.create(event, 2, "TANSHUGETREES / command / shape_file_converter / stop", run.command.shape_file_converter::stop);
        CommandMaker.create(event, 2, "TANSHUGETREES / command / summon_tree / <text>", run.command::summon_tree);
        CommandMaker.create(event, 2, "TANSHUGETREES / command / summon_sapling_trader", run.command::summon_sapling_trader);
        
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
                    Seasons.get(level_server, entity);

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

            private static class shape_file_converter {

                private static void start (CommandContext<CommandSourceStack> data) {

                    LevelAccessor level_accessor = data.getSource().getLevel();
                    int variable_number = CommandMaker.Argument.getNumber(data);
                    ShapeFileConverter.start(level_accessor, variable_number);

                }

                private static void stop (CommandContext<CommandSourceStack> data) {

                    LevelAccessor level_accessor = data.getSource().getLevel();
                    ShapeFileConverter.stop(level_accessor);

                }

            }

            private static void summon_tree (CommandContext<CommandSourceStack> data) {

                ServerLevel level_server = data.getSource().getLevel();
                Vec3 vec3 = data.getSource().getPosition();
                String variable_text = CommandMaker.Argument.getText(data);
                Entity entity = data.getSource().getEntity();
                TreeGenerator.create(level_server, entity, BlockPos.containing(vec3), variable_text);

            }

            private static void summon_sapling_trader (CommandContext<CommandSourceStack> data) {

                ServerLevel level_server = data.getSource().getLevel();
                Vec3 vec3 = data.getSource().getPosition();
                SaplingTrader.summonTrader(level_server, BlockPos.containing(vec3));

            }

        }

    }

}