package tannyjung.tanshugetrees_handcode.systems.tree_generator;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import tannyjung.core.FileManager;
import tannyjung.core.GameUtils;
import tannyjung.tanshugetrees.network.TanshugetreesModVariables;
import tannyjung.tanshugetrees_handcode.Handcode;

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

                String path = Handcode.directory_world_generated + "/" + GameUtils.nbt.block.getText(level_accessor, pos, "export_file_name");
                BlockPos pos_center = new BlockPos((int) GameUtils.nbt.block.getNumber(level_accessor, pos, "center_posX"), (int) GameUtils.nbt.block.getNumber(level_accessor, pos, "center_posY"), (int) GameUtils.nbt.block.getNumber(level_accessor, pos, "center_posZ"));
                String write_pos = (pos.getX() - pos_center.getX()) + "/" + (pos.getY() - pos_center.getY()) + "/" + (pos.getZ() - pos_center.getZ());
                FileManager.writeTXT(path, "+b" + write_pos + GameUtils.nbt.block.getText(level_accessor, pos, "type_short") + "\n", true);

                // Function
                {

                    if (GameUtils.nbt.block.getText(level_accessor, pos, "function_short").equals("") == false) {

                        FileManager.writeTXT(path, "+f" + write_pos + GameUtils.nbt.block.getText(level_accessor, pos, "function_short") + "\n", true);

                    }

                }

                level_accessor.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);

            }

        }

    }

}
