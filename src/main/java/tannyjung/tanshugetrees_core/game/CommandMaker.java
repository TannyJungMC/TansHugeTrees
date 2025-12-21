package tannyjung.tanshugetrees_core.game;

import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.MessageArgument;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.RegisterCommandsEvent;
import tannyjung.tanshugetrees_core.OutsideUtils;

import java.util.function.Consumer;

public class CommandMaker {

    public static void create (RegisterCommandsEvent event, String structure, Consumer<CommandContext<CommandSourceStack>> data) {

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

                        data.accept(arguments);
                        return 0;

                    })));

                }

            } else if (structure_short.equals("#/#/#") == true) {

                {

                    event.getDispatcher().register(Commands.literal(split[0]).requires(test -> test.hasPermission(0)).then(Commands.literal(split[1]).then(Commands.literal(split[2]).executes(arguments -> {

                        data.accept(arguments);
                        return 0;

                    }))));

                }

            } else if (structure_short.equals("#/#/#/#") == true) {

                {

                    event.getDispatcher().register(Commands.literal(split[0]).requires(test -> test.hasPermission(0)).then(Commands.literal(split[1]).then(Commands.literal(split[2]).then(Commands.literal(split[3]).executes(arguments -> {

                        data.accept(arguments);
                        return 0;

                    })))));

                }

            } else if (structure_short.equals("#/#/#/#/#") == true) {

                {

                    event.getDispatcher().register(Commands.literal(split[0]).requires(test -> test.hasPermission(0)).then(Commands.literal(split[1]).then(Commands.literal(split[2]).then(Commands.literal(split[3]).then(Commands.literal(split[4]).executes(arguments -> {

                        data.accept(arguments);
                        return 0;

                    }))))));

                }

            } else if (structure_short.equals("#/#/#/#/N") == true) {

                {

                    event.getDispatcher().register(Commands.literal(split[0]).requires(s -> s.hasPermission(0)).then(Commands.literal(split[1]).then(Commands.literal(split[2]).then(Commands.literal(split[3]).then(Commands.argument("number", DoubleArgumentType.doubleArg()).executes(arguments -> {

                        data.accept(arguments);
                        return 0;

                    }))))));

                }

            } else if (structure_short.equals("#/#/#/T") == true) {

                {

                    event.getDispatcher().register(Commands.literal(split[0]).requires(s -> s.hasPermission(0)).then(Commands.literal(split[1]).then(Commands.literal(split[2]).then(Commands.argument("text", MessageArgument.message()).executes(arguments -> {

                        data.accept(arguments);
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

    public static boolean testPermission (CommandContext<CommandSourceStack> data, String prefix, int permission) {

        Entity entity = data.getSource().getEntity();

        if (entity instanceof Player player) {

            if (player.hasPermissions(permission) == false) {

                GameUtils.misc.sendChatMessage(data.getSource().getLevel(), player, "@s", "red", prefix + " : You must have server permission minimum level " + permission + " to use this command. If you're in singleplayer, try enable cheat mode or temporary open LAN. If you're in multiplayer, try give yourself OP or contact server admin.");
                return false;

            }

        }

        return true;

    }

}
