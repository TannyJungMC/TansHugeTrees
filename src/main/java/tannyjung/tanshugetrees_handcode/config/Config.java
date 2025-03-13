package tannyjung.tanshugetrees_handcode.config;

import tannyjung.tanshugetrees_handcode.Handcode;
import tannyjung.tanshugetrees_handcode.misc.FileManager;

public class Config {

	public static void repair () {

		FileManager.createFolder(Handcode.directory_config + "/custom_packs/.organized");
		FileManager.createFolder(Handcode.directory_config + "/generated");

		ConfigMain.repair();
		ConfigAutoGen.repair();
		// ConfigRepairPlacement.start();

	}

}