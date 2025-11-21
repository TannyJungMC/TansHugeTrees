package tannyjung.tanshugetrees_handcode.common;

import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LevelAccessor;
import tannyjung.core.game.CommandMaker;
import tannyjung.core.game.TXTFunction;
import tannyjung.tanshugetrees_handcode.Handcode;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import tannyjung.tanshugetrees_handcode.common.config.PackCheckUpdate;
import tannyjung.tanshugetrees_handcode.common.config.PackUpdate;
import tannyjung.tanshugetrees_handcode.server.living_tree_mechanics.LivingTreeMechanics;
import tannyjung.tanshugetrees_handcode.server.living_tree_mechanics.LivingTreeMechanicsLeafDrop;
import tannyjung.tanshugetrees_handcode.server.living_tree_mechanics.LivingTreeMechanicsLeafLitterRemover;
import tannyjung.tanshugetrees_handcode.server.living_tree_mechanics.Seasons;
import tannyjung.tanshugetrees_handcode.server.tree_generator.PresetFixer;
import tannyjung.tanshugetrees_handcode.server.tree_generator.ShapeFileConverter;
import tannyjung.tanshugetrees_handcode.server.tree_generator.TreeGenerator;

@Mod.EventBusSubscriber
public class Commands {

    @SubscribeEvent
    public static void start (RegisterCommandsEvent event) {

        CommandMaker.registry(event, 0, "TANSHUGETREES / custom_packs / check_update_main", run.custom_packs::check_update_main);
        CommandMaker.registry(event, 0, "TANSHUGETREES / custom_packs / update_main", run.custom_packs::update_main);
        CommandMaker.registry(event, 0, "TANSHUGETREES / dev / delayed_command", run.dev::detailed_command);
        CommandMaker.registry(event, 0, "TANSHUGETREES / dev / living_tree_mechanics / leaf_drop", run.dev.living_tree_mechanics::leaf_drop);
        CommandMaker.registry(event, 0, "TANSHUGETREES / dev / living_tree_mechanics / leaf_litter_remover", run.dev.living_tree_mechanics::leaf_litter_remover);
        CommandMaker.registry(event, 0, "TANSHUGETREES / dev / living_tree_mechanics / loop", run.dev.living_tree_mechanics::loop);
        CommandMaker.registry(event, 0, "TANSHUGETREES / dev / tree_generator", run.dev::tree_generator);
        CommandMaker.registry(event, 0, "TANSHUGETREES / general / preset_fixer", run.general::preset_fixer);
        CommandMaker.registry(event, 0, "TANSHUGETREES / general / shape_file_converter / start / <number>", run.general.shape_file_converter::start);
        CommandMaker.registry(event, 0, "TANSHUGETREES / general / shape_file_converter / stop", run.general.shape_file_converter::stop);
        CommandMaker.registry(event, 0, "TANSHUGETREES / general / seasons / get", run.general.seasons::get);
        CommandMaker.registry(event, 0, "TANSHUGETREES / general / seasons / set / autumn", run.general.seasons.set::autumn);
        CommandMaker.registry(event, 0, "TANSHUGETREES / general / seasons / set / spring", run.general.seasons.set::spring);
        CommandMaker.registry(event, 0, "TANSHUGETREES / general / seasons / set / summer", run.general.seasons.set::summer);
        CommandMaker.registry(event, 0, "TANSHUGETREES / general / seasons / set / winter", run.general.seasons.set::winter);
        CommandMaker.registry(event, 0, "TANSHUGETREES / restart", run::restart);

    }

    private static class run {

        private static class custom_packs {

            private static void check_update_main(CommandContext<CommandSourceStack> data) {

                LevelAccessor level_accessor = data.getSource().getLevel();
                PackCheckUpdate.start(level_accessor, true);

            }

            private static void update_main(CommandContext<CommandSourceStack> data) {

                LevelAccessor level_accessor = data.getSource().getLevel();
                PackUpdate.start(level_accessor);

            }

        }

        private static class dev {

            private static void detailed_command(CommandContext<CommandSourceStack> data) {

                Entity entity = data.getSource().getEntity();

                if (entity == null) {

                    return;

                }

                TXTFunction.runDelayedCommand(entity);

            }

            private static class living_tree_mechanics {

                private static void leaf_drop(CommandContext<CommandSourceStack> data) {

                    Entity entity = data.getSource().getEntity();

                    if (entity == null) {

                        return;

                    }

                    LivingTreeMechanicsLeafDrop.start(entity);

                }

                private static void leaf_litter_remover(CommandContext<CommandSourceStack> data) {

                    Entity entity = data.getSource().getEntity();

                    if (entity == null) {

                        return;

                    }

                    LivingTreeMechanicsLeafLitterRemover.start(entity);

                }

                private static void loop(CommandContext<CommandSourceStack> data) {

                    Entity entity = data.getSource().getEntity();

                    if (entity == null) {

                        return;

                    }

                    LivingTreeMechanics.start(entity);

                }

            }

            private static void tree_generator(CommandContext<CommandSourceStack> data) {

                LevelAccessor level_accessor = data.getSource().getLevel();
                Entity entity = data.getSource().getEntity();

                if (entity == null) {

                    return;

                }

                TreeGenerator.start(level_accessor, entity);

            }

        }

        private static class general {

            private static void preset_fixer(CommandContext<CommandSourceStack> data) {

                PresetFixer.start();

            }

            private static class shape_file_converter {

                private static void start(CommandContext<CommandSourceStack> data) {

                    LevelAccessor level_accessor = data.getSource().getLevel();
                    int count = (int) DoubleArgumentType.getDouble(data, "count");
                    ShapeFileConverter.start(level_accessor, count);

                }

                private static void stop(CommandContext<CommandSourceStack> data) {

                    LevelAccessor level_accessor = data.getSource().getLevel();
                    ShapeFileConverter.stop(level_accessor);

                }

            }

            private static class seasons {

                private static void get(CommandContext<CommandSourceStack> data) {

                    ServerLevel level_server = data.getSource().getLevel();
                    Seasons.get(level_server);

                }

                private static class set {

                    private static void autumn(CommandContext<CommandSourceStack> data) {

                        ServerLevel level_server = data.getSource().getLevel();
                        Seasons.set(level_server, "Autumn");

                    }

                    private static void spring(CommandContext<CommandSourceStack> data) {

                        ServerLevel level_server = data.getSource().getLevel();
                        Seasons.set(level_server, "Spring");

                    }

                    private static void summer(CommandContext<CommandSourceStack> data) {

                        ServerLevel level_server = data.getSource().getLevel();
                        Seasons.set(level_server, "Summer");

                    }

                    private static void winter(CommandContext<CommandSourceStack> data) {

                        ServerLevel level_server = data.getSource().getLevel();
                        Seasons.set(level_server, "Winter");

                    }

                }

            }

        }

        private static void restart(CommandContext<CommandSourceStack> data) {

            LevelAccessor level_accessor = data.getSource().getLevel();
            Handcode.runRestart(level_accessor, true);

        }

    }

}