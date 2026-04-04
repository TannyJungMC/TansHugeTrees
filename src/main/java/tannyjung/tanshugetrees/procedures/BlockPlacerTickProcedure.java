package tannyjung.tanshugetrees.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.BlockPos;

public class BlockPlacerTickProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z) {
		tannyjung.tanshugetrees_handcode.systems.tree_generator.BlockPlacer.start(world, (ServerLevel) world, BlockPos.containing(x, y, z));
	}
}
