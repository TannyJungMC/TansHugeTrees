package tannyjung.tanshugetrees.procedures;

import tannyjung.tanshugetrees_handcode.config.ConfigMain;

public class OVERLAYWorldGenShowDetailsProcedure {
	public static boolean execute() {
		return ConfigMain.developer_mode == true;
	}
}
