package tannyjung.tanshugetrees.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;

public class COMMANDTreeGeneratorLoopTickProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		RandomTreeTickProcedure.execute(world, x, y, z, entity);
	}
}
