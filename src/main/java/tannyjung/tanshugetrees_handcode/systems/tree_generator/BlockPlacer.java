package tannyjung.tanshugetrees_handcode.systems.tree_generator;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import tannyjung.tanshugetrees_core.Core;
import tannyjung.tanshugetrees_core.game.BlockManager;
import tannyjung.tanshugetrees_core.game.NBTManager;
import tannyjung.tanshugetrees_core.outside.TXTFunction;

public class BlockPlacer {

    public static void start (ServerLevel level_server, BlockPos pos) {

        String function = NBTManager.Tile.getText(level_server, pos, "main", "function");

        if (NBTManager.Tile.getLogic(level_server, pos, "main", "delay1") == false) {

            NBTManager.Tile.setLogic(level_server, pos, "main", "delay1", true);
            BlockManager.setScheduleTick(level_server, pos, 100);

            // Test Function
            {

                if (function.isEmpty() == false) {

                    String[] styles = NBTManager.Tile.getText(level_server, pos, "main", "function_style").split("/");
                    boolean pass = false;

                    for (String style : styles) {

                        if (style.equals("all") == true) {

                            pass = true;

                        } else if (style.equals("up") == true) {

                            if (level_server.getBlockState(pos.above()).isAir() == true) {

                                pass = true;

                            }

                        } else if (style.equals("down") == true) {

                            if (level_server.getBlockState(pos.below()).isAir() == true) {

                                pass = true;

                            }

                        } else if (style.equals("side") == true) {

                            if (level_server.getBlockState(pos.north()).isAir() == true) {

                                pass = true;

                            } else if (level_server.getBlockState(pos.west()).isAir() == true) {

                                pass = true;

                            } else if (level_server.getBlockState(pos.east()).isAir() == true) {

                                pass = true;

                            } else if (level_server.getBlockState(pos.south()).isAir() == true) {

                                pass = true;

                            }

                        }

                    }

                    if (pass == false) {

                        NBTManager.Tile.setText(level_server, pos, "main", "function", "");

                    }

                }

            }

        } else {

            if (NBTManager.Tile.getLogic(level_server, pos, "main", "delay2") == false) {

                NBTManager.Tile.setLogic(level_server, pos, "main", "delay2", true);
                BlockManager.setScheduleTick(level_server, pos, 100);

            } else {

                BlockManager.set(level_server, pos, BlockManager.fromText(level_server, NBTManager.Tile.getText(level_server, pos, "main", "block")), false);

                if (function.isEmpty() == false) {

                    Core.DelayedWork.createBasic(20, () -> {

                        TXTFunction.run(level_server, level_server, pos, function, true);

                    });

                }

            }

        }

    }

}
