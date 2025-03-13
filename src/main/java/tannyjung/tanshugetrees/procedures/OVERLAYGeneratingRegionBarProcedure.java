package tannyjung.tanshugetrees.procedures;

import tannyjung.tanshugetrees_handcode.world_gen.TreeLocation;

public class OVERLAYGeneratingRegionBarProcedure {
	public static double execute() {
		return Math.round(((double) TreeLocation.generating_region_bar / 1024) * 16);
	}
}
