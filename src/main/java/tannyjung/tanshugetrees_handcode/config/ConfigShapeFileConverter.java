package tannyjung.tanshugetrees_handcode.config;

import tannyjung.tanshugetrees_handcode.Handcode;
import tannyjung.core.FileManager;

public class ConfigShapeFileConverter {

	public static void repair () {

		StringBuilder write = new StringBuilder();

		{

			write.append("""
					- This only for convert tree presets into tree shapes, to use in custom packs.
					- To use this. First, install dimension for tree generator. Set render distance and simulation distance to 32. Then write correct file location below and use command [ /TANSHUGETREES shape_file_converter start <loop> ].
					- Warning! This dimension Y height is only between 0 to 1024 blocks, so be careful about trees cut by world height. You can change Y position of it using [ posY ] option below.
					
					----------------------------------------------------------------------------------------------------
					
					file_location = #TannyJung-Main-Pack/presets/folder/file.txt
					posY = 300
					
					----------------------------------------------------------------------------------------------------
					"""

			);

		}

		FileManager.writeConfigTXT(Handcode.directory_config + "/.dev/shape_file_converter/#shape_file_converter.txt", write.toString());

	}

}