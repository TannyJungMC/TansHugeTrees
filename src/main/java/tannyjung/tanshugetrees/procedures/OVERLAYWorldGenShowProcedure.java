package tannyjung.tanshugetrees.procedures;

import tannyjung.tanshugetrees_handcode.server.world_gen.TreeLocation;

public class OVERLAYWorldGenShowProcedure {
	public static boolean execute() {
		return TreeLocation.world_gen_overlay_animation != 0;
	}
}