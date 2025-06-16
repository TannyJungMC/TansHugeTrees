package tannyjung.tanshugetrees_handcode.systems.tree_generator;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import tannyjung.misc.FileManager;
import tannyjung.misc.GameUtils;
import tannyjung.tanshugetrees.network.TanshugetreesModVariables;
import tannyjung.tanshugetrees_handcode.Handcode;

public class BlockPlacer {

    public static void start (LevelAccessor level, BlockPos pos) {

        if (TanshugetreesModVariables.MapVariables.get(level).auto_gen == false) {

            BlockState block = GameUtils.block.fromText(GameUtils.NBT.block.getText(level, pos, "block"));
            level.setBlock(pos, block, 2);

        } else {

            // Shape File Converter
            {

                String type = GameUtils.NBT.block.getText(level, pos, "type");
                String block_original = GameUtils.NBT.block.getText(level, pos, "block_original");
                String type_short = "";

                if (type.equals("leaves") == false) {

                    type_short = block_original.substring(0, 2);

                } else {

                    type_short = "le" + block_original.substring(6);

                }

                String write = "+b" + pos.getX() + "/" + pos.getY() + "/" + pos.getZ() + type_short;
                String file_name = GameUtils.NBT.block.getText(level, pos, "export_file_name") + " (generating)" + ".txt";
                FileManager.writeTXT(Handcode.directory_config + "/generated/" + file_name, write + "\n", true);
                level.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);

            }

        }

    }

}
