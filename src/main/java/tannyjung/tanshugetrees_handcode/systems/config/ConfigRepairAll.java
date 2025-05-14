package tannyjung.tanshugetrees_handcode.systems.config;

import net.minecraft.world.level.LevelAccessor;
import tannyjung.tanshugetrees_handcode.Handcode;
;
import tannyjung.tanshugetrees_handcode.misc.FileManager;
import tannyjung.tanshugetrees_handcode.misc.GameUtils;

import java.util.concurrent.CompletableFuture;

public class ConfigRepairAll {

	public static void start (LevelAccessor level) {

		FileManager.createFolder(Handcode.directory_config + "/custom_packs/.organized");
		FileManager.createFolder(Handcode.directory_config + "/generated");

		CustomPackOrganized.clearFolder();
		CustomPackIncompatible.scanMain();
		CustomPackOrganized.start();
		CustomPackIncompatible.scanOrganized();

		ConfigMain.repair();
		ConfigAutoGen.repair();
		ConfigPlacement.start();

		GameUtils.sendChatMessage(level, "@a", "gray", "THT : Repaired The Config");

	}

}