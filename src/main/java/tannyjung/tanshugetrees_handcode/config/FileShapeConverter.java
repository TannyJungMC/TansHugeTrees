package tannyjung.tanshugetrees_handcode.config;

import tannyjung.tanshugetrees_core.Core;
import tannyjung.tanshugetrees_core.outside.ConfigMaker;

public class FileShapeConverter {

	public static void start () {

		String write = """
					- This only for convert tree presets into tree shapes, for use in custom packs.
					- To use this. Set render distance to 32 or depends on tree size. Then write correct file location below and use command [ /TANSHUGETREES command shape_file_converter start <loop> ].
					
					----------------------------------------------------------------------------------------------------
					
					file_location = pack/preset
					
					----------------------------------------------------------------------------------------------------
					""";

        ConfigMaker.repair(Core.path_config + "/#dev/shape_file_converter/#shape_file_converter.txt", write);

	}

}