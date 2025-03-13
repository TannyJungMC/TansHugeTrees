package tannyjung.tanshugetrees.procedures;

import tannyjung.tanshugetrees.network.TanshugetreesModVariables;

import net.minecraft.world.level.LevelAccessor;

public class RandomTreeDetectExistProcedure {
	public static void execute(LevelAccessor world) {
		TanshugetreesModVariables.MapVariables.get(world).detect_exist = true;
		TanshugetreesModVariables.MapVariables.get(world).syncData(world);
	}
}
