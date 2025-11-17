package tannyjung.tanshugetrees_mcreator.procedures;

import tannyjung.tanshugetrees.server.world_gen.TreeLocation;

public class OVERLAYWorldGenShowProcedure {
	public static boolean execute() {
		return TreeLocation.world_gen_overlay_animation != 0;
	}
}