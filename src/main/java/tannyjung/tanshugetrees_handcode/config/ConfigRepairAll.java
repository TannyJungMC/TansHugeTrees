package tannyjung.tanshugetrees_handcode.config;

import net.minecraft.world.level.LevelAccessor;
import tannyjung.tanshugetrees_handcode.Handcode;
import tannyjung.tanshugetrees_handcode.misc.FileManager;

public class ConfigRepairAll {

	public static void start (LevelAccessor level) {

		FileManager.createFolder(Handcode.directory_config + "/custom_packs/.organized");
		FileManager.createFolder(Handcode.directory_config + "/generated");

		{

			String[] custom_args = {};

			try {
				CustomPacksOrganized.start(custom_args);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		TestIncompatibleCustomPack.start();
		ConfigMain.repair();
		ConfigAutoGen.repair();
		ConfigPlacement.start();

	}

}