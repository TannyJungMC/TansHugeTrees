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

        if (TanshugetreesModVariables.MapVariables.get(level_accessor).shape_file_converter == false) {

            // Function
            {

                if (GameUtils.nbt.block.getText(level_accessor, pos, "function").equals("") == false) {

                    TreeFunction.start(level_server, level_server, pos.getX(), pos.getY(), pos.getZ(), GameUtils.nbt.block.getText(level_accessor, pos, "function"), false);

                }

            }

            level_accessor.setBlock(pos, GameUtils.block.fromText(GameUtils.nbt.block.getText(level_accessor, pos, "block")), 2);

        } else {

            // Shape File Converter
            {

                BlockPos pos_center = new BlockPos((int) GameUtils.nbt.block.getNumber(level_accessor, pos, "center_posX"), (int) GameUtils.nbt.block.getNumber(level_accessor, pos, "center_posY"), (int) GameUtils.nbt.block.getNumber(level_accessor, pos, "center_posZ"));
                String write_pos = (pos.getX() - pos_center.getX()) + "/" + (pos.getY() - pos_center.getY()) + "/" + (pos.getZ() - pos_center.getZ());
                ShapeFileConverter.shape_file_converter_export_data.append("+b").append(write_pos).append(GameUtils.nbt.block.getText(level_accessor, pos, "type_short")).append("\n");

                // Function
                {

                    if (GameUtils.nbt.block.getText(level_accessor, pos, "function_short").equals("") == false) {

                        ShapeFileConverter.shape_file_converter_export_data.append("+f").append(write_pos).append(GameUtils.nbt.block.getText(level_accessor, pos, "function_short")).append("\n");

                    }

                }

                level_accessor.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);

            }

        }

    }

}
