package tannyjung.tanshugetrees.procedures;

import tannyjung.tanshugetrees_handcode.server.world_gen.TreeLocation;

public class OVERLAYWorldGenAnimationProcedure {
	public static double execute() {
		return TreeLocation.world_gen_overlay_animation - 1;
	}
}