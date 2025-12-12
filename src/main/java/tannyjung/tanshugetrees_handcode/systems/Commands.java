package tannyjung.tanshugetrees_handcode.systems;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LevelAccessor;
import tannyjung.tanshugetrees_core.game.CommandMaker;
import tannyjung.tanshugetrees_core.game.TXTFunction;
import tannyjung.tanshugetrees_handcode.Handcode;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import tannyjung.tanshugetrees_handcode.data.TannyPack;
import tannyjung.tanshugetrees_handcode.systems.living_tree_mechanics.LivingTreeMechanics;
import tannyjung.tanshugetrees_handcode.systems.living_tree_mechanics.LivingTreeMechanicsLeafDrop;
import tannyjung.tanshugetrees_handcode.systems.living_tree_mechanics.LivingTreeMechanicsLeafLitterRemover;
import tannyjung.tanshugetrees_handcode.systems.living_tree_mechanics.Seasons;
import tannyjung.tanshugetrees_handcode.systems.tree_generator.PresetFixer;
import tannyjung.tanshugetrees_handcode.systems.tree_generator.ShapeFileConverter;
import tannyjung.tanshugetrees_handcode.systems.tree_generator.TreeGenerator;

@Mod.EventBusSubscriber
public class Commands {

    @SubscribeEvent
    public static void start (RegisterCommandsEvent event) {

        CommandMaker.create(event, "TANSHUGETREES / command / seasons / get", run.command.seasons::get);
        CommandMaker.create(event, "TANSHUGETREES / command / seasons / set / autumn", run.command.seasons.set::autumn);
        CommandMaker.create(event, "TANSHUGETREES / command / seasons / set / spring", run.command.seasons.set::spring);
        CommandMaker.create(event, "TANSHUGETREES / command / seasons / set / summer", run.command.seasons.set::summer);
        CommandMaker.create(event, "TANSHUGETREES / command / seasons / set / winter", run.command.seasons.set::winter);
        CommandMaker.create(event, "TANSHUGETREES / dev / delayed_command", run.dev::detailed_command);
        CommandMaker.create(event, "TANSHUGETREES / dev / living_tree_mechanics / leaf_drop", run.dev.living_tree_mechanics::leaf_drop);
        CommandMaker.create(event, "TANSHUGETREES / dev / living_tree_mechanics / leaf_litter_remover", run.dev.living_tree_mechanics::leaf_litter_remover);
        CommandMaker.create(event, "TANSHUGETREES / dev / living_tree_mechanics / loop", run.dev.living_tree_mechanics::loop);
        CommandMaker.create(event, "TANSHUGETREES / dev / preset_fixer", run.dev::preset_fixer);
        CommandMaker.create(event, "TANSHUGETREES / dev / shape_file_converter / start / <number>", run.dev.shape_file_converter::start);
        CommandMaker.create(event, "TANSHUGETREES / dev / shape_file_converter / stop", run.dev.shape_file_converter::stop);
        CommandMaker.create(event, "TANSHUGETREES / dev / tree_generator", run.dev::tree_generator);
        CommandMaker.create(event, "TANSHUGETREES / dev / txt_function / <text>", run.dev::txt_function);
        CommandMaker.create(event, "TANSHUGETREES / restart", run::restart);
        CommandMaker.create(event, "TANSHUGETREES / tanny_pack / check_update", run.tanny_pack::check_update);
        CommandMaker.create(event, "TANSHUGETREES / tanny_pack / update", run.tanny_pack::update);

    }

    private static class run {

        private static class command {

            private static class seasons {

                private static void get (CommandContext<CommandSourceStack> data) {

                    if (CommandMaker.testPermission(data, "THT", 2) == true) {

                        ServerLevel level_server = data.getSource().getLevel();
                        Seasons.get(level_server);

                    }

                }

                private static class set {

                    private static void autumn (CommandContext<CommandSourceStack> data) {

                        if (CommandMaker.testPermission(data, "THT", 2) == true) {

                            ServerLevel level_server = data.getSource().getLevel();
                            Seasons.set(level_server, "Autumn");

                        }

                    }

                    private static void spring (CommandContext<CommandSourceStack> data) {

                        if (CommandMaker.testPermission(data, "THT", 2) == true) {

                            ServerLevel level_server = data.getSource().getLevel();
                            Seasons.set(level_server, "Spring");

                        }

                    }

                    private static void summer (CommandContext<CommandSourceStack> data) {

                        if (CommandMaker.testPermission(data, "THT", 2) == true) {

                            ServerLevel level_server = data.getSource().getLevel();
                            Seasons.set(level_server, "Summer");

                        }

                    }

                    private static void winter (CommandContext<CommandSourceStack> data) {

                        if (CommandMaker.testPermission(data, "THT", 2) == true) {

                            ServerLevel level_server = data.getSource().getLevel();
                            Seasons.set(level_server, "Winter");

                        }

                    }

                }

            }

        }

        private static class dev {

            private static void detailed_command (CommandContext<CommandSourceStack> data) {

                if (CommandMaker.testPermission(data, "THT", 2) == true) {

                    Entity entity = data.getSource().getEntity();

                    if (entity == null) {

                        return;

                    }

                    TXTFunction.runDelayedCommand(entity);

                }

            }

            private static class living_tree_mechanics {

                private static void leaf_drop (CommandContext<CommandSourceStack> data) {

                    if (CommandMaker.testPermission(data, "THT", 2) == true) {

                        Entity entity = data.getSource().getEntity();

                        if (entity == null) {

                            return;

                        }

                        LivingTreeMechanicsLeafDrop.start(entity);

                    }

                }

                private static void leaf_litter_remover (CommandContext<CommandSourceStack> data) {

                    if (CommandMaker.testPermission(data, "THT", 2) == true) {

                        Entity entity = data.getSource().getEntity();

                        if (entity == null) {

                            return;

                        }

                        LivingTreeMechanicsLeafLitterRemover.start(entity);

                    }

                }

                private static void loop (CommandContext<CommandSourceStack> data) {

                    if (CommandMaker.testPermission(data, "THT", 2) == true) {

                        Entity entity = data.getSource().getEntity();

                        if (entity == null) {

                            return;

                        }

                        LivingTreeMechanics.start(entity);

                    }

                }

            }

            private static void preset_fixer (CommandContext<CommandSourceStack> data) {

                if (CommandMaker.testPermission(data, "THT", 2) == true) {

                    PresetFixer.start();

                }

            }

            private static class shape_file_converter {

                private static void start (CommandContext<CommandSourceStack> data) {

                    if (CommandMaker.testPermission(data, "THT", 2) == true) {

                        LevelAccessor level_accessor = data.getSource().getLevel();
                        int number = CommandMaker.argument.getNumber(data);
                        ShapeFileConverter.start(level_accessor, number);

                    }

                }

                private static void stop (CommandContext<CommandSourceStack> data) {

                    if (CommandMaker.testPermission(data, "THT", 2) == true) {

                        LevelAccessor level_accessor = data.getSource().getLevel();
                        ShapeFileConverter.stop(level_accessor);

                    }

                }

            }

            private static void tree_generator (CommandContext<CommandSourceStack> data) {

                if (CommandMaker.testPermission(data, "THT", 2) == true) {

                    LevelAccessor level_accessor = data.getSource().getLevel();
                    Entity entity = data.getSource().getEntity();

                    if (entity == null) {

                        return;

                    }

                    TreeGenerator.start(level_accessor, entity);

                }

            }

            private static void txt_function (CommandContext<CommandSourceStack> data) {

                if (CommandMaker.testPermission(data, "THT", 2) == true) {

                    LevelAccessor level_accessor = data.getSource().getLevel();
                    ServerLevel level_server = data.getSource().getLevel();
                    int posX = (int) data.getSource().getPosition().x();
                    int posY = (int) data.getSource().getPosition().y();
                    int posZ = (int) data.getSource().getPosition().z();
                    String text = CommandMaker.argument.getText(data);
                    TXTFunction.start(level_accessor, level_server, posX, posY, posZ, text, true);

                }

            }

        }

        private static void restart (CommandContext<CommandSourceStack> data) {

            if (CommandMaker.testPermission(data, "THT", 2) == true) {

                ServerLevel level_server = data.getSource().getLevel();
                Entity entity = data.getSource().getEntity();

                Handcode.thread_main.submit(() -> {

                    Handcode.restart(level_server, true, entity != null);

                });

            }

        }

        private static class tanny_pack {

            private static void check_update (CommandContext<CommandSourceStack> data) {

                if (CommandMaker.testPermission(data, "THT", 2) == true) {

                    ServerLevel level_server = data.getSource().getLevel();
                    TannyPack.checkUpdate(level_server);

                }

            }

            private static void update (CommandContext<CommandSourceStack> data) {

                if (CommandMaker.testPermission(data, "THT", 2) == true) {

                    ServerLevel level_server = data.getSource().getLevel();
                    Entity entity = data.getSource().getEntity();
                    TannyPack.reinstall(level_server, entity != null);

                }

            }

        }

    }

}