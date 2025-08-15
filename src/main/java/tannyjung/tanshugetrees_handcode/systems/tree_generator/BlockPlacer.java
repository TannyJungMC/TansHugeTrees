package tannyjung.tanshugetrees_handcode.systems.tree_generator;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import tannyjung.core.GameUtils;
import tannyjung.tanshugetrees.TanshugetreesMod;
import tannyjung.tanshugetrees.network.TanshugetreesModVariables;

public class BlockPlacer {

    public static void start (LevelAccessor level_accessor, BlockPos pos) {

        ServerLevel level_server = (ServerLevel) level_accessor;
        boolean is_shape_file_converter = TanshugetreesModVariables.MapVariables.get(level_accessor).shape_file_converter;
        String function = GameUtils.nbt.block.getText(level_accessor, pos, "function");

        if (GameUtils.nbt.block.getLogic(level_accessor, pos, "delay1") == false) {

            GameUtils.nbt.block.setLogic(level_accessor, pos, "delay1", true);

            if (is_shape_file_converter == false) {

                level_server.scheduleTick(pos, level_server.getBlockState(pos).getBlock(), 100);

            }

            // Test Function
            {

                if (function.equals("") == false) {

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

            if (is_shape_file_converter == false) {

                // Normal
                {

                    if (GameUtils.nbt.block.getLogic(level_accessor, pos, "delay2") == false) {

                        GameUtils.nbt.block.setLogic(level_accessor, pos, "delay2", true);
                        level_server.scheduleTick(pos, level_server.getBlockState(pos).getBlock(), 100);

                    } else {

                        level_accessor.setBlock(pos, GameUtils.block.fromText(GameUtils.nbt.block.getText(level_accessor, pos, "block")), 2);

                        if (function.equals("") == false) {

                            TanshugetreesMod.queueServerWork(20, () -> {

                                TreeFunction.start(level_server, level_server, pos.getX(), pos.getY(), pos.getZ(), function, false);

                            });

                        }

                    }

                }

            } else {

                // Shape File Converter
                {

                    BlockPos pos_center = new BlockPos((int) GameUtils.nbt.block.getNumber(level_accessor, pos, "center_posX"), (int) GameUtils.nbt.block.getNumber(level_accessor, pos, "center_posY"), (int) GameUtils.nbt.block.getNumber(level_accessor, pos, "center_posZ"));
                    String write_pos = (pos.getX() - pos_center.getX()) + "/" + (pos.getY() - pos_center.getY()) + "/" + (pos.getZ() - pos_center.getZ());
                    ShapeFileConverter.export_data.append("+b").append(write_pos).append(GameUtils.nbt.block.getText(level_accessor, pos, "type_short")).append("\n");

                    if (function.equals("") == false) {

                        ShapeFileConverter.export_data.append("+f").append(write_pos).append(GameUtils.nbt.block.getText(level_accessor, pos, "function_short")).append("\n");

                    }

                    level_accessor.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);

                }

            }

        }

    }

}
