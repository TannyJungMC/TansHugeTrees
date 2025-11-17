package tannyjung.tanshugetrees.server.tree_generator;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import tannyjung.core.game.NBTManager;
import tannyjung.core.game.TXTFunction;
import tannyjung.core.game.Utils;
import tannyjung.tanshugetrees.Handcode;

public class BlockPlacer {

    public static void start (LevelAccessor level_accessor, BlockPos pos) {

        ServerLevel level_server = (ServerLevel) level_accessor;
        String function = NBTManager.block.getText(level_accessor, pos, "function");

        if (NBTManager.block.getLogic(level_accessor, pos, "delay1") == false) {

            NBTManager.block.setLogic(level_accessor, pos, "delay1", true);
            level_server.scheduleTick(pos, level_server.getBlockState(pos).getBlock(), 100);

            // Test Function
            {

                if (function.equals("") == false) {

                    String[] styles = NBTManager.block.getText(level_accessor, pos, "function_style").split("/");
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

                        NBTManager.block.setText(level_accessor, pos, "function", "");

                    }

                }

            }

        } else {

            // Normal
            {

                if (NBTManager.block.getLogic(level_accessor, pos, "delay2") == false) {

                    NBTManager.block.setLogic(level_accessor, pos, "delay2", true);
                    level_server.scheduleTick(pos, level_server.getBlockState(pos).getBlock(), 100);

                } else {

                    level_accessor.setBlock(pos, Utils.block.fromText(NBTManager.block.getText(level_accessor, pos, "block")), 2);

                    if (function.equals("") == false) {

                        Handcode.createDelayedWorks(20, () -> {

                            TXTFunction.start(level_server, level_server, RandomSource.create(), pos.getX(), pos.getY(), pos.getZ(), function);

                        });

                    }

                }

            }

        }

    }

}
