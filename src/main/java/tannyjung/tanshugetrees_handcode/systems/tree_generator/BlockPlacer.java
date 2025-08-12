package tannyjung.tanshugetrees_handcode.systems.tree_generator;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import tannyjung.core.GameUtils;
import tannyjung.tanshugetrees.network.TanshugetreesModVariables;

public class BlockPlacer {

    public static void start (LevelAccessor level_accessor, BlockPos pos) {

        ServerLevel level_server = (ServerLevel) level_accessor;
        String function = GameUtils.nbt.block.getText(level_accessor, pos, "function");
        boolean function_pass = false;

        // Test Function
        {

            if (function.equals("") == false) {

                String[] styles = GameUtils.nbt.block.getText(level_accessor, pos, "function_style").split("/");

                for (String style : styles) {

                    if (style.equals("all") == true) {

                        function_pass = true;

                    } else if (style.equals("up") == true) {

                        if (level_accessor.getBlockState(new BlockPos(pos.getX(), pos.getY() + 1, pos.getZ())).isAir() == true) {

                            function_pass = true;

                        }

                    } else if (style.equals("down") == true) {

                        if (level_accessor.getBlockState(new BlockPos(pos.getX(), pos.getY() - 1, pos.getZ())).isAir() == true) {

                            function_pass = true;

                        }

                    } else if (style.equals("side") == true) {

                        if (level_accessor.getBlockState(new BlockPos(pos.getX() + 1, pos.getY(), pos.getZ())).isAir() == true) {

                            function_pass = true;

                        } else if (level_accessor.getBlockState(new BlockPos(pos.getX() - 1, pos.getY(), pos.getZ())).isAir() == true) {

                            function_pass = true;

                        } else if (level_accessor.getBlockState(new BlockPos(pos.getX(), pos.getY(), pos.getZ() + 1)).isAir() == true) {

                            function_pass = true;

                        } else if (level_accessor.getBlockState(new BlockPos(pos.getX(), pos.getY(), pos.getZ() - 1)).isAir() == true) {

                            function_pass = true;

                        }

                    }

                }

            }

        }

        if (TanshugetreesModVariables.MapVariables.get(level_accessor).shape_file_converter == false) {

            if (GameUtils.nbt.block.getLogic(level_accessor, pos, "delaying") == false) {

                GameUtils.nbt.block.setLogic(level_accessor, pos, "delaying", true);
                level_server.scheduleTick(pos, level_server.getBlockState(pos).getBlock(), 180);

            } else {

                level_accessor.setBlock(pos, GameUtils.block.fromText(GameUtils.nbt.block.getText(level_accessor, pos, "block")), 2);

                if (function_pass == true) {

                    TreeFunction.start(level_server, level_server, pos.getX(), pos.getY(), pos.getZ(), function, false);

                }

            }

        } else {

            // Shape File Converter
            {

                BlockPos pos_center = new BlockPos((int) GameUtils.nbt.block.getNumber(level_accessor, pos, "center_posX"), (int) GameUtils.nbt.block.getNumber(level_accessor, pos, "center_posY"), (int) GameUtils.nbt.block.getNumber(level_accessor, pos, "center_posZ"));
                String write_pos = (pos.getX() - pos_center.getX()) + "/" + (pos.getY() - pos_center.getY()) + "/" + (pos.getZ() - pos_center.getZ());
                ShapeFileConverter.export_data.append("+b").append(write_pos).append(GameUtils.nbt.block.getText(level_accessor, pos, "type_short")).append("\n");

                if (function_pass == true) {

                    ShapeFileConverter.export_data.append("+f").append(write_pos).append(GameUtils.nbt.block.getText(level_accessor, pos, "function_short")).append("\n");

                }

                level_accessor.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);

            }

        }

    }

}
