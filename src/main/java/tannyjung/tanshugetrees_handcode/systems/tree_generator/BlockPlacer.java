package tannyjung.tanshugetrees_handcode.systems.tree_generator;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import tannyjung.misc.GameUtils;
import tannyjung.tanshugetrees.network.TanshugetreesModVariables;

public class BlockPlacer {

    public static void start (LevelAccessor level, BlockPos pos) {

        BlockState block = GameUtils.block.fromText(GameUtils.NBT.block.getText(level, pos, "block"));

        if (TanshugetreesModVariables.MapVariables.get(level).auto_gen == false) {

            level.setBlock(pos, block, 2);

        }

    }

}
