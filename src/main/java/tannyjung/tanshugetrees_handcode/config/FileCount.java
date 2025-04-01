package tannyjung.tanshugetrees_handcode.config;

import net.minecraftforge.fml.loading.FMLPaths;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;
import tannyjung.tanshugetrees_handcode.Handcode;

import java.io.File;

public class FileCount {




    // --------------------------------------------------
    // Custom
    // --------------------------------------------------



	// Change to "FMLPaths.GAMEDIR.get().toString();" for game directory
    static String game_directory = FMLPaths.GAMEDIR.get().toString();
    
    static String folder_scan = game_directory + "/config/tanshugetrees/custom_packs";



    // --------------------------------------------------
    // Variables
    // --------------------------------------------------



    static File directory;
    static File[] list_pack;
    static File[] list_tree;
    static File[] list_file;

    static int count_tree = 0;
    static int count_file = 0;
    static String tree_name = "";
    static long file_size = 0;
    static double file_size_mb = 0;
    


    // --------------------------------------------------
    // Run System
    // --------------------------------------------------



    public static void start (LevelAccessor level, double x, double y, double z) {



	    count_tree = 0;
	    count_file = 0;
	    file_size = 0;

        try {
            scan_tree();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        file_size_mb = (double) (file_size  / (1024.0 * 1024.0));

        if (level instanceof ServerLevel _level) {
        	_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL,new Vec3(x, y, z), Vec2.ZERO, _level, 4, "",Component.literal(""), _level.getServer(), null).withSuppressedOutput(),("tellraw @a [{\"text\":\"There are now \",\"color\":\"white\"},{\"text\":\"" + count_tree + "\",\"color\":\"yellow\"},{\"text\":\" species in TannyJung's Tree Pack (" + Handcode.tanny_pack_version_name + "), and \",\"color\":\"white\"},{\"text\":\"" + count_file + "\",\"color\":\"yellow\"},{\"text\":\" different shapes in total! (\",\"color\":\"white\"},{\"text\":\"" + String.format("%.1f", file_size_mb) + " MB\",\"color\":\"yellow\"},{\"text\":\")\",\"color\":\"white\"}]"));
		}

    }



    // ----------------------------------------------------------------------------------------------------
    // Scan Tree
    // ----------------------------------------------------------------------------------------------------



    private static void scan_tree () throws Exception {
        
        directory = new File(folder_scan + "/TannyJung-Tree-Pack/presets");
        list_tree = directory.listFiles();

        if (list_tree != null) {for (int i_tree = 0; i_tree < list_tree.length;) {

            if (list_tree[i_tree].isDirectory()) {

                tree_name = list_tree[i_tree].getName();

                count_tree = count_tree + 1;

                scan_file();

            }

            i_tree = i_tree + 1;

        }}

    }



    // ----------------------------------------------------------------------------------------------------
    // Scan Storage
    // ----------------------------------------------------------------------------------------------------



    private static void scan_file () throws Exception {

        directory = new File(folder_scan + "/TannyJung-Tree-Pack/presets/" + tree_name + "/storage");
        list_file = directory.listFiles();

        if (list_file != null) {for (int i_file = 0; i_file < list_file.length;) {

            if (list_file[i_file].isFile() == true) {

                count_file = count_file + 1;

                file_size = file_size + list_file[i_file].length();

            }

            i_file = i_file + 1;

        }}

    }

}