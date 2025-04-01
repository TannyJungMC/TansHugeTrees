package tannyjung.tanshugetrees_handcode.config;

import net.minecraft.world.level.LevelAccessor;
import tannyjung.tanshugetrees_handcode.Handcode;
import tannyjung.tanshugetrees_handcode.misc.FileManager;
import tannyjung.tanshugetrees_handcode.misc.Misc;

public class ConfigRepairAll {

	public static void start (LevelAccessor level) {

		Misc.sendChatMessage(level, "@a", "gray", "THT : Repaired The Config");

		FileManager.createFolder(Handcode.directory_config + "/custom_packs/.organized");
		FileManager.createFolder(Handcode.directory_config + "/generated");

		CustomPacksOrganized.clearFolder();
		TestIncompatibleCustomPack.scanMain();
		CustomPacksOrganized.start();
		TestIncompatibleCustomPack.scanOrganized();

		ConfigMain.repair();
		ConfigAutoGen.repair();
		ConfigPlacement.start();

	}

}