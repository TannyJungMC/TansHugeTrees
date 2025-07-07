package tannyjung.tanshugetrees_handcode.config;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.LevelAccessor;
import tannyjung.tanshugetrees_handcode.Handcode;
;
import tannyjung.core.FileManager;
import tannyjung.core.GameUtils;

import java.util.concurrent.CompletableFuture;

public class ConfigRepairAll {

	public static void start (ServerLevel level_server, boolean send_chat) {

		// Global
		{

			FileManager.createFolder(Handcode.directory_config + "/custom_packs/.organized");
			ConfigMain.repair();
			ConfigWorldGen.start();
			CustomPackOrganized.start(level_server);

		}

		// World
		{

			if (level_server != null) {

				ConfigShapeFileConverter.repair();

			}

		}

		if (send_chat == true) {

			GameUtils.misc.sendChatMessage(level_server, "@a", "gray", "THT : Repaired The Config");

		}

	}

}