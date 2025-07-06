package tannyjung.tanshugetrees_handcode.systems.tree_generator;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import tannyjung.core.FileManager;
import tannyjung.core.GameUtils;
import tannyjung.tanshugetrees.network.TanshugetreesModVariables;
import tannyjung.tanshugetrees_handcode.Handcode;

public class BlockPlacer {

    public static void start (LevelAccessor level, BlockPos pos) {

        if (TanshugetreesModVariables.MapVariables.get(level).auto_gen == false) {

            // Function
            {

                if (GameUtils.NBT.block.getText(level, pos, "function").equals("") == false) {

                    TreeFunction.start(level, pos.getX(), pos.getY(), pos.getZ(), GameUtils.NBT.block.getText(level, pos, "function"));

                }

            }

            level.setBlock(pos, GameUtils.block.fromText(GameUtils.NBT.block.getText(level, pos, "block")), 2);

        } else {

            // Shape File Converter
            {

                String path = Handcode.directory_world_generated + "/" + GameUtils.NBT.block.getText(level, pos, "export_file_name");
                BlockPos pos_center = new BlockPos((int) GameUtils.NBT.block.getNumber(level, pos, "center_posX"), (int) GameUtils.NBT.block.getNumber(level, pos, "center_posY"), (int) GameUtils.NBT.block.getNumber(level, pos, "center_posZ"));
                String write_pos = (pos.getX() - pos_center.getX()) + "/" + (pos.getY() - pos_center.getY()) + "/" + (pos.getZ() - pos_center.getZ());
                FileManager.writeTXT(path, "+b" + write_pos + GameUtils.NBT.block.getText(level, pos, "type_short") + "\n", true);

                // Function
                {

                    if (GameUtils.NBT.block.getText(level, pos, "function_short").equals("") == false) {

                        FileManager.writeTXT(path, "+f" + write_pos + GameUtils.NBT.block.getText(level, pos, "function_short") + "\n", true);

                    }

                }

                level.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);

            }

        }

    }

}
