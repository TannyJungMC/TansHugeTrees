package tannyjung.tanshugetrees_handcode.config;

import net.minecraft.world.level.LevelAccessor;
import tannyjung.tanshugetrees_handcode.Handcode;
;
import tannyjung.core.FileManager;
import tannyjung.core.GameUtils;

public class ConfigRepairAll {

	public static void start (LevelAccessor level, boolean send_chat) {

		FileManager.createFolder(Handcode.directory_config + "/custom_packs/.organized");
		FileManager.createFolder(Handcode.directory_config + "/generated");

		ConfigMain.repair();
		ConfigAutoGen.repair();
		CustomPackOrganized.start(level);
		ConfigPlacement.start();

		if (send_chat == true) {

			GameUtils.misc.sendChatMessage(level, "@a", "gray", "THT : Repaired The Config");

		}

	}

}