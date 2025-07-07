package tannyjung.tanshugetrees.procedures;

import tannyjung.tanshugetrees_handcode.systems.world_gen.TreeLocation;

public class OVERLAYWorldGenBarProcedure {
	public static double execute() {
		return Math.round(((double) TreeLocation.world_gen_overlay_bar / 1024) * 16);
	}
}