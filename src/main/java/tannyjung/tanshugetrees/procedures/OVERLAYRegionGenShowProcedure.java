package tannyjung.tanshugetrees.procedures;

import tannyjung.tanshugetrees_handcode.world_gen.TreeLocation;

public class OVERLAYRegionGenShowProcedure {
	public static boolean execute() {
		return TreeLocation.region_gen != 0;
	}
}
