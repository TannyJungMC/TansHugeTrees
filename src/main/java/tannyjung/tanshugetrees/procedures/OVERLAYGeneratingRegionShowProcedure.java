package tannyjung.tanshugetrees.procedures;

import tannyjung.tanshugetrees_handcode.world_gen.TreeLocation;

public class OVERLAYGeneratingRegionShowProcedure {
	public static boolean execute() {
		return TreeLocation.generating_region != 0;
	}
}
