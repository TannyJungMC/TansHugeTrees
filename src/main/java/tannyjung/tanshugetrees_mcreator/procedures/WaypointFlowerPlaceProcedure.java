package tannyjung.tanshugetrees_mcreator.procedures;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.core.BlockPos;

import java.io.File;

public class WaypointFlowerPlaceProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z) {
		File file = new File("");
		double merge_text_pos = 0;
		String merge_text = "";
		String file_name = "";
		String theme = "";
		{
			BlockPos _pos = BlockPos.containing(x, y, z);
			Block.dropResources(world.getBlockState(_pos), world, BlockPos.containing(x, y, z), null);
			world.destroyBlock(_pos, false);
		}
	}
}