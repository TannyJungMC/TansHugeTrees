package tannyjung.tanshugetrees_mcreator.procedures;

import tannyjung.tanshugetrees.server.world_gen.TreeLocation;

public class OVERLAYWorldGenBarProcedure {
	public static double execute() {
		return Math.round(((double) TreeLocation.world_gen_overlay_bar / 1024) * 16);
	}
}