package tannyjung.tanshugetrees_handcode.config;

import tannyjung.tanshugetrees_handcode.Handcode;
import tannyjung.tanshugetrees_handcode.misc.FileManager;

public class ConfigAutoGen {

	public static void repair () {

		StringBuilder write = new StringBuilder();

		{

			write.append("""
			- It will use my void dimension to generate trees so it not affect to normal world. When start, you will teleport to this dimension and teleport you back when finished.
			- To use this, write correct file location, then use command [ /THT auto_gen start <loop> ].
			- Warning! This dimension height is 0 to 1024 blocks, so be careful about world high/low cut the trees. You can change the Y position of trees using [ posY ] option.
			
			----------------------------------------------------------------------------------------------------
			
			file_location = THT-tree_pack-main/presets/folder/file.txt
			
			generate_speed = 1
			generate_speed_repeat = 10000
			generate_speed_tp = 0
			
			posY = 300
			chat_message = true
			
			----------------------------------------------------------------------------------------------------
			""");

		}

		FileManager.writeConfigTXT(Handcode.directory_config + "/generated/.auto_gen.txt", write.toString());

	}

}