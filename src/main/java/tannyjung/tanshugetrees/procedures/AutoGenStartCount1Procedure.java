package tannyjung.tanshugetrees.procedures;

import tannyjung.tanshugetrees.network.TanshugetreesModVariables;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;

public class AutoGenStartCount1Procedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		TanshugetreesModVariables.MapVariables.get(world).auto_gen_count = 1;
		TanshugetreesModVariables.MapVariables.get(world).syncData(world);
		AutoGenStartProcedure.execute(world, x, y, z, entity);
	}
}
