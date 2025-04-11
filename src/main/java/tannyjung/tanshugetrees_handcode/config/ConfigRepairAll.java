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
		FileManager.createFolder(Handcode.directory_world_data + "/data_folder_cleaner");
		FileManager.createFolder(Handcode.directory_world_data + "/tree_locations");
		FileManager.createFolder(Handcode.directory_world_data + "/detailed_detection");
		FileManager.createFolder(Handcode.directory_world_data + "/place");

		CustomPackOrganized.clearFolder();
		CustomPackIncompatible.scanMain();
		CustomPackOrganized.start();
		CustomPackIncompatible.scanOrganized();

		ConfigMain.repair();
		ConfigAutoGen.repair();
		ConfigPlacement.start();

	}

}