package tannyjung.tanshugetrees_handcode.data;

import tannyjung.tanshugetrees_handcode.Handcode;
import tannyjung.tanshugetrees_core.FileManager;

public class FileShapeConverter {

	public static void start () {

		StringBuilder write = new StringBuilder();

		{

			write.append("""
					- This only for convert tree presets into tree shapes, for use in custom packs.
					- To use this. Set render distance to 32 or depends on tree size. Then write correct file location below and use command [ /TANSHUGETREES commands shape_file_converter start <loop> ].
					
					----------------------------------------------------------------------------------------------------
					
					file_location = pack/preset
					
					----------------------------------------------------------------------------------------------------
					"""

			);

		}

		FileManager.writeTXTConfig(Handcode.path_config + "/#dev/shape_file_converter/#shape_file_converter.txt", write.toString());

	}

}