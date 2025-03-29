package tannyjung.tanshugetrees.procedures;

import tannyjung.tanshugetrees.network.TanshugetreesModVariables;

import net.minecraft.world.level.LevelAccessor;

public class COMMANDSeasonSetSpringProcedure {
	public static void execute(LevelAccessor world) {
		TanshugetreesModVariables.MapVariables.get(world).season = "Spring";
		TanshugetreesModVariables.MapVariables.get(world).syncData(world);
	}
}
