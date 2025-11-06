package tannyjung.tanshugetrees_handcode.config;

import tannyjung.tanshugetrees_handcode.Handcode;
import tannyjung.core.FileManager;

public class ConfigShapeFileConverter {

	public static void repair () {

		StringBuilder write = new StringBuilder();

		{

			write.append("""
					- This only for convert tree presets into tree shapes, for use in custom packs.
					- To use this. Set render distance to 32. Then write correct file location below and use command [ /TANSHUGETREES command shape_file_converter start <loop> ].
					
					----------------------------------------------------------------------------------------------------
					
					file_location = pack/preset
					
					----------------------------------------------------------------------------------------------------
					"""

			);

		}

		FileManager.writeConfigTXT(Handcode.path_config + "/#dev/shape_file_converter/#shape_file_converter.txt", write.toString());

	}

}