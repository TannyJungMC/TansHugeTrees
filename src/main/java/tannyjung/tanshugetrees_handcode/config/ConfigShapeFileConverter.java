package tannyjung.tanshugetrees_handcode.config;

import tannyjung.tanshugetrees_handcode.Handcode;
import tannyjung.core.FileManager;

public class ConfigShapeFileConverter {

	public static void repair () {

		StringBuilder write = new StringBuilder();

		{

			write.append("""
					- It will use my void dimension to generate trees so it not affect to normal world. When start, you will teleport to this dimension and teleport you back when finished.
					- To use this, write correct file location, then use command [ /THT shape_file_converter start <loop> ].
					- Warning! This dimension height is 0 to 1024 blocks, so be careful about world high/low cut the trees. You can change the Y position of trees using [ posY ] option.
					
					----------------------------------------------------------------------------------------------------
					
					file_location = #TannyJung-Main-Pack/folder/file.txt
					posY = 300
					
					----------------------------------------------------------------------------------------------------
					"""

			);

		}

		FileManager.writeConfigTXT(Handcode.directory_config + "/.dev/shape_file_converter/#shape_file_converter.txt", write.toString());

	}

}