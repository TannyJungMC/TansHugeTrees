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

                BlockPos pos_center = new BlockPos((int) GameUtils.NBT.block.getNumber(level, pos, "center_posX"), (int) GameUtils.NBT.block.getNumber(level, pos, "center_posY"), (int) GameUtils.NBT.block.getNumber(level, pos, "center_posZ"));
                String type_short = GameUtils.NBT.block.getText(level, pos, "type_short");

                String write = "+b" + (pos.getX() - pos_center.getX()) + "/" + (pos.getY() - pos_center.getY()) + "/" + (pos.getZ() - pos_center.getZ()) + type_short;
                FileManager.writeTXT(Handcode.directory_config + "/generated/" + GameUtils.NBT.block.getText(level, pos, "export_file_name"), write + "\n", true);
                level.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);

            }

        }

    }

}
