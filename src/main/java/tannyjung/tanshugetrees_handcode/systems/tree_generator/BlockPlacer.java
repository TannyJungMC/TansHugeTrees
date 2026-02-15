package tannyjung.tanshugetrees_handcode.systems.tree_generator;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.LevelAccessor;
import tannyjung.tanshugetrees_core.Core;
import tannyjung.tanshugetrees_core.game.TXTFunction;
import tannyjung.tanshugetrees_core.game.GameUtils;

public class BlockPlacer {

    public static void start (LevelAccessor level_accessor, ServerLevel level_server, int posX, int posY, int posZ) {

        String function = GameUtils.nbt.block.getText(level_accessor, posX, posY, posZ, "function");

        if (GameUtils.nbt.block.getLogic(level_accessor, posX, posY, posZ, "delay1") == false) {

            GameUtils.nbt.block.setLogic(level_accessor, level_server, posX, posY, posZ, "delay1", true);
            GameUtils.block.setScheduleTick(level_server, posX, posY, posZ, 100);

            // Test Function
            {

                if (function.isEmpty() == false) {

                    String[] styles = GameUtils.nbt.block.getText(level_accessor, posX, posY, posZ, "function_style").split("/");
                    boolean pass = false;

                    for (String style : styles) {

                        if (style.equals("all") == true) {

                            pass = true;

                        } else if (style.equals("up") == true) {

                            if (GameUtils.block.getAt(level_accessor, posX, posY + 1, posZ).isAir() == true) {

                                pass = true;

                            }

                        } else if (style.equals("down") == true) {

                            if (GameUtils.block.getAt(level_accessor, posX, posY - 1, posZ).isAir() == true) {

                                pass = true;

                            }

                        } else if (style.equals("side") == true) {

                            if (GameUtils.block.getAt(level_accessor, posX + 1, posY, posZ).isAir() == true) {

                                pass = true;

                            } else if (GameUtils.block.getAt(level_accessor, posX - 1, posY, posZ).isAir() == true) {

                                pass = true;

                            } else if (GameUtils.block.getAt(level_accessor, posX, posY, posZ + 1).isAir() == true) {

                                pass = true;

                            } else if (GameUtils.block.getAt(level_accessor, posX, posY, posZ - 1).isAir() == true) {

                                pass = true;

                            }

                        }

                    }

                    if (pass == false) {

                        GameUtils.nbt.block.setText(level_accessor, level_server, posX, posY, posZ, "function", "");

                    }

                }

            }

        } else {

            // Normal
            {

                if (GameUtils.nbt.block.getLogic(level_accessor, posX, posY, posZ, "delay2") == false) {

                    GameUtils.nbt.block.setLogic(level_accessor, level_server, posX, posY, posZ, "delay2", true);
                    GameUtils.block.setScheduleTick(level_server, posX, posY, posZ, 100);

                } else {

                    GameUtils.block.setAt(level_accessor, posX, posY, posZ, GameUtils.block.fromText(GameUtils.nbt.block.getText(level_accessor, posX, posY, posZ, "block")), false);

                    if (function.isEmpty() == false) {

                        Core.DelayedWorks.create(false, 20, () -> {

                            TXTFunction.run(level_accessor, level_server, posX, posY, posZ, "functions/" + function, true);

                        });

                    }

                }

            }

        }

    }

}
