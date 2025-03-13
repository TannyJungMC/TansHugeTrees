package tannyjung.tanshugetrees.procedures;

import tannyjung.tanshugetrees.network.TanshugetreesModVariables;

import net.minecraft.world.level.LevelAccessor;

public class SeasonSetSummerProcedure {
	public static void execute(LevelAccessor world) {
		TanshugetreesModVariables.MapVariables.get(world).season = "Summer";
		TanshugetreesModVariables.MapVariables.get(world).syncData(world);
	}
}
