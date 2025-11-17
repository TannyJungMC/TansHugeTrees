package tannyjung.tanshugetrees_mcreator.procedures;

import tannyjung.tanshugetrees.config.ConfigMain;

public class OVERLAYWorldGenShowDetailsProcedure {
	public static boolean execute() {
		return ConfigMain.developer_mode == true;
	}
}