package tannyjung.tanshugetrees_handcode.config;

import net.minecraftforge.fml.loading.FMLPaths;
import tannyjung.tanshugetrees.TanshugetreesMod;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Comparator;

public class CustomPacksOrganized {

    

    // ----------------------------------------------------------------------------------------------------
    // Custom
    // ----------------------------------------------------------------------------------------------------

    

    // Change to "FMLPaths.GAMEDIR.get().toString();" for game directory
    static String game_directory = FMLPaths.GAMEDIR.get().toString();

    static String folder_pack = game_directory + "/config/tanshugetrees/custom_packs";
    static String folder_pack_organized = game_directory + "/config/tanshugetrees/custom_packs/.organized";



    // ----------------------------------------------------------------------------------------------------
    // Class Global Variables
    // ----------------------------------------------------------------------------------------------------


    
    

    

    // ----------------------------------------------------------------------------------------------------
    // Run System
    // ----------------------------------------------------------------------------------------------------



    public static void start (String[] args) {

        delete();
        create_organized_folder();
        copy();
        replace();

    }



    // ----------------------------------------------------------------------------------------------------
    // Delete
    // ----------------------------------------------------------------------------------------------------



    private static void delete () {

        Path delete = Paths.get(folder_pack_organized);

        if (delete.toFile().exists() == true) {

            try {

                Files.walk(delete).sorted(Comparator.reverseOrder()).forEach(path -> {

                    try {

                        Files.delete(path);

                    } catch (Exception e) {

                        TanshugetreesMod.LOGGER.error("Error to delete folders in custom_packs_organized!");

                    }

                });

            } catch (Exception e) {

                e.printStackTrace();

            }

        }

    }



    // ----------------------------------------------------------------------------------------------------
    // Create Organized Folder
    // ----------------------------------------------------------------------------------------------------



    private static void create_organized_folder () {

        File create;

        create = new File(folder_pack_organized);
        create.mkdirs();

    }
    


    // ----------------------------------------------------------------------------------------------------
    // Copy
    // ----------------------------------------------------------------------------------------------------



    private static void copy () {
        
        Path from = Paths.get(folder_pack);
        Path to = Paths.get(folder_pack_organized);

        try {

            Files.walk(from).forEach(source -> {

                if (

                    source.toFile().isDirectory() == false
                    &&
                    source.toString().contains("[INCOMPATIBLE] ") == false
                    &&
                    (
                        source.toString().contains("\\.organized\\") == false
                        &&
                        source.toString().contains("\\replace\\") == false
                        &&
                        source.toString().contains("\\storage\\") == false
                    )
                    &&
                    (
                        source.toString().contains("\\functions\\") == true
                        ||
                        source.toString().contains("\\presets\\") == true
                        ||
                        source.toString().contains("\\world_gen\\") == true
                    )

                ) {

                    Path copy = to.resolve(from.relativize(source));

                    try {

                        Files.createDirectories(copy.getParent());
                        Files.copy(source, copy, StandardCopyOption.REPLACE_EXISTING);

                    } catch (Exception e) {

                        TanshugetreesMod.LOGGER.error("Error to copying " + e.getMessage());

                    }

                }

            });

        } catch (Exception e) {

            TanshugetreesMod.LOGGER.error("Error walking the source directory : " + e.getMessage());

        }

    }



    // ----------------------------------------------------------------------------------------------------
    // Replace
    // ----------------------------------------------------------------------------------------------------



    private static void replace () {

        File directory_scan = new File(folder_pack);
        File [] file_list = directory_scan.listFiles();

        if (file_list != null) {for (int i = 0; i < file_list.length;) {

            if (file_list[i].getName().equals(".organized") == false) {

                Path from = Paths.get(folder_pack + "/" + file_list[i].getName() + "/replace");
                Path to = Paths.get(folder_pack_organized);

                if (from.toFile().exists() == true) {

	                try {
	
	                    Files.walk(from).forEach(source -> {
	
	                        if (source.toFile().isDirectory() == false) {
	
	                            Path copy = to.resolve(from.relativize(source));
	
	                            try {
	
	                                Files.createDirectories(copy.getParent());
	                                Files.copy(source, copy, StandardCopyOption.REPLACE_EXISTING);
	
	                            } catch (Exception e) {

                                    TanshugetreesMod.LOGGER.error("Error to replacing " + e.getMessage());
	
	                            }
	
	                        }
	
	                    });
	
	                } catch (Exception e) {

                        TanshugetreesMod.LOGGER.error("Error walking the source directory (replacing) : " + e.getMessage());
	
	                }

                }

            }

            i = i + 1;

        }}

    }

}