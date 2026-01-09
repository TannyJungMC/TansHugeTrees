package tannyjung.tanshugetrees_handcode.systems.tree_generator;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.LevelAccessor;
import tannyjung.tanshugetrees_core.Core;
import tannyjung.tanshugetrees_core.game.TXTFunction;
import tannyjung.tanshugetrees_core.game.GameUtils;

public class BlockPlacer {

    public static void start (LevelAccessor level_accessor, BlockPos pos) {

        ServerLevel level_server = (ServerLevel) level_accessor;
        String function = GameUtils.nbt.block.getText(level_accessor, pos, "function");

        if (GameUtils.nbt.block.getLogic(level_accessor, pos, "delay1") == false) {

            GameUtils.nbt.block.setLogic(level_accessor, pos, "delay1", true);
            level_server.scheduleTick(pos, level_server.getBlockState(pos).getBlock(), 100);

            // Test Function
            {

                if (function.isEmpty() == false) {

                    String[] styles = GameUtils.nbt.block.getText(level_accessor, pos, "function_style").split("/");
                    boolean pass = false;

                    for (String style : styles) {

                        if (style.equals("all") == true) {

                            pass = true;

                        } else if (style.equals("up") == true) {

                            if (level_accessor.getBlockState(new BlockPos(pos.getX(), pos.getY() + 1, pos.getZ())).isAir() == true) {

                                pass = true;

                            }

                        } else if (style.equals("down") == true) {

                            if (level_accessor.getBlockState(new BlockPos(pos.getX(), pos.getY() - 1, pos.getZ())).isAir() == true) {

                                pass = true;

                            }

                        } else if (style.equals("side") == true) {

                            if (level_accessor.getBlockState(new BlockPos(pos.getX() + 1, pos.getY(), pos.getZ())).isAir() == true) {

                                pass = true;

                            } else if (level_accessor.getBlockState(new BlockPos(pos.getX() - 1, pos.getY(), pos.getZ())).isAir() == true) {

                                pass = true;

                            } else if (level_accessor.getBlockState(new BlockPos(pos.getX(), pos.getY(), pos.getZ() + 1)).isAir() == true) {

                                pass = true;

                            } else if (level_accessor.getBlockState(new BlockPos(pos.getX(), pos.getY(), pos.getZ() - 1)).isAir() == true) {

                                pass = true;

                            }

                        }

                    }

                    if (pass == false) {

                        GameUtils.nbt.block.setText(level_accessor, pos, "function", "");

                    }

                }

            }

        } else {

            // Normal
            {

                if (GameUtils.nbt.block.getLogic(level_accessor, pos, "delay2") == false) {

                    GameUtils.nbt.block.setLogic(level_accessor, pos, "delay2", true);
                    level_server.scheduleTick(pos, level_server.getBlockState(pos).getBlock(), 100);

                } else {

                    level_accessor.setBlock(pos, GameUtils.block.fromText(GameUtils.nbt.block.getText(level_accessor, pos, "block")), 2);

                    if (function.isEmpty() == false) {

                        Core.delayed_works.create(false, 20, () -> {

                            TXTFunction.run(level_server, level_server, pos.getX(), pos.getY(), pos.getZ(), "functions/" + function, true);

                        });

                    }

                }

            }

        }

    }

}
