package tannyjung.tanshugetrees_handcode.systems.config;

import net.minecraft.world.level.LevelAccessor;
import tannyjung.tanshugetrees_handcode.Handcode;
;
import tannyjung.tanshugetrees_handcode.misc.FileManager;
import tannyjung.tanshugetrees_handcode.misc.GameUtils;

public class ConfigRepairAll {

	public static void start (LevelAccessor level) {

		GameUtils.sendChatMessage(level, "@a", "gray", "THT : Repaired The Config");

		FileManager.createFolder(Handcode.directory_config + "/custom_packs/.organized");
		FileManager.createFolder(Handcode.directory_config + "/generated");

		CustomPackOrganized.clearFolder();
		CustomPackIncompatible.scanMain();
		CustomPackOrganized.start();
		CustomPackIncompatible.scanOrganized();

		ConfigMain.repair();
		ConfigAutoGen.repair();
		ConfigPlacement.start();

	}

}