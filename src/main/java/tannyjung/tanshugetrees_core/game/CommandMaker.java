package tannyjung.tanshugetrees_core.game;

import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.MessageArgument;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.LevelAccessor;
import tannyjung.tanshugetrees_core.Core;
import tannyjung.tanshugetrees_core.outside.OutsideUtils;

import java.util.function.Consumer;

/*
(1.20.1)
import net.minecraftforge.event.RegisterCommandsEvent;
(1.21.1)
import net.neoforged.neoforge.event.RegisterCommandsEvent;
*/
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import tannyjung.tanshugetrees_core.outside.TannyPackManager;

public class CommandMaker {

    public static void create (Object event_object, int permission, String structure, Consumer<CommandContext<CommandSourceStack>> consumer) {

        RegisterCommandsEvent event = (RegisterCommandsEvent) event_object;
        String[] split = structure.split(" / ");
        String structure_short = "";

        // Convert
        {

            StringBuilder convert = new StringBuilder();

            for (String get : split) {

                convert.append("/");

                if (get.startsWith("<") == false) {

                    convert.append("#");

                } else {

                    {

                        if (get.equals("<number>") == true) {

                            convert.append("N");

                        } else if (get.equals("<text>") == true) {

                            convert.append("T");

                        }

                    }

                }

            }

            structure_short = convert.substring(1);

        }

        // Registry
        {

            if (structure_short.equals("#/#") == true) {

                {

                    event.getDispatcher().register(Commands.literal(split[0]).requires(test -> test.hasPermission(0)).then(Commands.literal(split[1]).executes(arguments -> {

                        if (CommandMaker.testPermission(arguments, permission) == true) {

                            consumer.accept(arguments);

                        }

                        return 0;

                    })));

                }

            } else if (structure_short.equals("#/#/#") == true) {

                {

                    event.getDispatcher().register(Commands.literal(split[0]).requires(test -> test.hasPermission(0)).then(Commands.literal(split[1]).then(Commands.literal(split[2]).executes(arguments -> {

                        if (CommandMaker.testPermission(arguments, permission) == true) {

                            consumer.accept(arguments);

                        }

                        return 0;

                    }))));

                }

            } else if (structure_short.equals("#/#/#/#") == true) {

                {

                    event.getDispatcher().register(Commands.literal(split[0]).requires(test -> test.hasPermission(0)).then(Commands.literal(split[1]).then(Commands.literal(split[2]).then(Commands.literal(split[3]).executes(arguments -> {

                        if (CommandMaker.testPermission(arguments, permission) == true) {

                            consumer.accept(arguments);

                        }

                        return 0;

                    })))));

                }

            } else if (structure_short.equals("#/#/#/#/#") == true) {

                {

                    event.getDispatcher().register(Commands.literal(split[0]).requires(test -> test.hasPermission(0)).then(Commands.literal(split[1]).then(Commands.literal(split[2]).then(Commands.literal(split[3]).then(Commands.literal(split[4]).executes(arguments -> {

                        if (CommandMaker.testPermission(arguments, permission) == true) {

                            consumer.accept(arguments);

                        }

                        return 0;

                    }))))));

                }

            } else if (structure_short.equals("#/#/#/#/N") == true) {

                {

                    event.getDispatcher().register(Commands.literal(split[0]).requires(s -> s.hasPermission(0)).then(Commands.literal(split[1]).then(Commands.literal(split[2]).then(Commands.literal(split[3]).then(Commands.argument("number", DoubleArgumentType.doubleArg()).executes(arguments -> {

                        if (CommandMaker.testPermission(arguments, permission) == true) {

                            consumer.accept(arguments);

                        }

                        return 0;

                    }))))));

                }

            } else if (structure_short.equals("#/#/#/T") == true) {

                {

                    event.getDispatcher().register(Commands.literal(split[0]).requires(s -> s.hasPermission(0)).then(Commands.literal(split[1]).then(Commands.literal(split[2]).then(Commands.argument("text", MessageArgument.message()).executes(arguments -> {

                        if (CommandMaker.testPermission(arguments, permission) == true) {

                            consumer.accept(arguments);

                        }

                        return 0;

                    })))));

                }

            }

        }

    }

    public static class argument {

        public static int getNumber (CommandContext<CommandSourceStack> data) {

            return (int) DoubleArgumentType.getDouble(data, "number");

        }

        public static String getText (CommandContext<CommandSourceStack> data) {

            String return_text = "";

            return_text = (new Object() {

                public String get () {

                    try {

                        return MessageArgument.getMessage(data, "text").getString();

                    } catch (Exception exception) {

                        OutsideUtils.exception(new Exception(), exception, "");

                    }

                    return "";

                }

            }).get();

            return return_text;

        }

    }

    public static boolean testPermission (CommandContext<CommandSourceStack> data, int permission) {

        Entity entity = data.getSource().getEntity();

        if (entity instanceof Player player) {

            if (player.hasPermissions(permission) == false) {

                GameUtils.misc.sendChatMessage(data.getSource().getLevel(), "@s", "You must have server permission minimum level " + permission + " to use this command. If you're in singleplayer, try enable cheat mode or temporary open LAN. If you're in multiplayer, try give yourself OP or contact server admin. / red");
                return false;

            }

        }

        return true;

    }

    public static class builtin_command {

        public static void registry (Object event_object) {

            CommandMaker.create(event_object, 2, Core.mod_id_big + " / command / txt_function / <text>", run.command::txt_function);
            CommandMaker.create(event_object, 2, Core.mod_id_big + " / restart", run::restart);
            CommandMaker.create(event_object, 2, Core.mod_id_big + " / tanny_pack / check_update", run.tanny_pack::check_update);
            CommandMaker.create(event_object, 2, Core.mod_id_big + " / tanny_pack / update", run.tanny_pack::update);

        }

        private static class run {

            private static class command {

                private static void txt_function (CommandContext<CommandSourceStack> data) {

                    LevelAccessor level_accessor = data.getSource().getLevel();
                    ServerLevel level_server = data.getSource().getLevel();
                    int posX = (int) Math.floor(data.getSource().getPosition().x());
                    int posY = (int) Math.floor(data.getSource().getPosition().y());
                    int posZ = (int) Math.floor(data.getSource().getPosition().z());
                    String variable_text = CommandMaker.argument.getText(data);
                    TXTFunction.run(level_accessor, level_server, posX, posY, posZ, variable_text, true);

                }

            }

            private static void restart (CommandContext<CommandSourceStack> data) {

                ServerLevel level_server = data.getSource().getLevel();
                Entity entity = data.getSource().getEntity();

                Core.thread_main.submit(() -> {

                    Core.restart(level_server, "config / world", entity != null);

                });

            }

            private static class tanny_pack {

                private static void check_update (CommandContext<CommandSourceStack> data) {

                    ServerLevel level_server = data.getSource().getLevel();
                    TannyPackManager.checkUpdate(level_server);

                }

                private static void update (CommandContext<CommandSourceStack> data) {

                    ServerLevel level_server = data.getSource().getLevel();
                    TannyPackManager.reinstall(level_server);

                }

            }

        }

    }

}
