package tannyjung.tanshugetrees.procedures;

import tannyjung.tanshugetrees_handcode.world_gen.TreeLocation;

public class OVERLAYRegionGenBarProcedure {
	public static double execute() {
		return Math.round(((double) TreeLocation.region_gen_bar / 1024) * 16);
	}
}
