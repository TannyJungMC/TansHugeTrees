package tannyjung.tanshugetrees_handcode.config;

import java.io.FileWriter;
import java.io.FileReader;
import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import net.minecraftforge.fml.loading.FMLPaths;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;

import tannyjung.tanshugetrees.TanshugetreesMod;

import java.util.concurrent.CompletableFuture;

public class ConfigRepairPlacement {

    

    // ----------------------------------------------------------------------------------------------------
    // Custom
    // ----------------------------------------------------------------------------------------------------

    

    //    FMLPaths.GAMEDIR.get().toString();
    //    "C:/Users/acer/Desktop/Minecraft Projects/Mod MCreator/THT/THT/run";
    static String game_directory = FMLPaths.GAMEDIR.get().toString();

    static String directory_file = game_directory + "/config/tanshugetrees/config_placement.txt";
    static String directory_file_temp = game_directory + "/config/tanshugetrees/config_placement_temp.txt";
    static String directory_pack = game_directory + "/config/tanshugetrees/custom_packs/.organized";



    // ----------------------------------------------------------------------------------------------------
    // Class Global Variables
    // ----------------------------------------------------------------------------------------------------


    
    static LevelAccessor world;
    static double x = 0;
    static double y = 0;
    static double z = 0;

    

    // ----------------------------------------------------------------------------------------------------
    // Run System
    // ----------------------------------------------------------------------------------------------------



    public static void start (LevelAccessor import_world, double import_x, double import_y, double import_z) {

        world = import_world;
        x = import_x;
        y = import_y;
        z = import_z;

        CompletableFuture.runAsync(() -> {

            try {

                create_temp();

                /*

                // --------------------------------------------------
                //    Delete this after NeoForge
                // --------------------------------------------------
                if (Paths.get(directory_file).toFile().exists() == true) {
                    
                    convert();
                    create_temp();



                    String[] custom_args = {};
                    try {
                    CustomPacksOrganized.start(custom_args);
                    } catch (Exception e) {
                    e.printStackTrace();
                    }
                // --------------------------------------------------

                }

                 */
                
                create();
                scan_write();
                delete_temp();

            }  catch (Exception e) {

                TanshugetreesMod.LOGGER.error(e);
                
            }

        });

    }



    // ----------------------------------------------------------------------------------------------------
    // Convert Old To New
    // ----------------------------------------------------------------------------------------------------

    /*

    public static void convert () throws Exception {

        FileWriter writer;
        BufferedWriter buffered_writer;
        BufferedReader buffered_reader;
        
        String read_all = "";
        boolean convert_true = false;

        buffered_reader = new BufferedReader(new FileReader(directory_file_temp));

        while ((read_all = buffered_reader.readLine()) != null) {

            if (read_all.startsWith("- ") == true) {

                convert_true = true;

            }

            break;

        } buffered_reader.close();



        if (convert_true == true) {

            //    Create Folder

            writer = new FileWriter(directory_file, false);
            buffered_writer = new BufferedWriter(writer);
            buffered_writer.close();
            writer.close();

            if (world instanceof ServerLevel _level) {
                _level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL,new Vec3(x, y, z), Vec2.ZERO, _level, 4, "",Component.literal(""), _level.getServer(), null).withSuppressedOutput(),("execute if entity @e[type=player,distance=..0.01] run tellraw @a [{\"text\":\"THT : Converted \",\"color\":\"green\"},{\"text\":\"config_placement.txt\",\"color\":\"white\"}]"));
            }




            //    Copy Old Folder

            Path from = Paths.get(game_directory + "/config/tanshugetrees/custom/tree_packs");
            Path to = Paths.get(game_directory + "/config/tanshugetrees/custom_packs");
    
            try {
    
                Files.walk(from).forEach(source -> {
    
                    Path copy = to.resolve(from.relativize(source));
    
                    try {

                        Files.createDirectories(copy.getParent());
                        Files.copy(source, copy, StandardCopyOption.REPLACE_EXISTING);
    
                    } catch (Exception e) {

                        TanshugetreesMod.LOGGER.error("Error copying file : " + e);
    
                    }
    
                });
    
            } catch (Exception e) {

                TanshugetreesMod.LOGGER.error("Error walking the source directory : " + e);
    
            }





            //    Delete Old Folders

            Files.walk(Paths.get(game_directory + "/config/tanshugetrees/custom")).sorted(java.util.Comparator.reverseOrder()).forEach(source -> {

                try {
    
                    Files.delete(source);
    
                } catch (Exception e) {

                    TanshugetreesMod.LOGGER.error("Error to delete old folder (converter)! " + e);
    
                }
                
            });





            //    Write

            try {

                String font_text = "";
                String name = "";
                boolean lock = false;

                writer = new FileWriter(directory_file, true);
                buffered_writer = new BufferedWriter(writer);

                buffered_reader = new BufferedReader(new FileReader(directory_file_temp));

                while ((read_all = buffered_reader.readLine()) != null) {

                    if (read_all.startsWith("lock : ") == true) {

                        if (read_all.endsWith("true") == true) {

                            lock = true;

                        } else {

                            lock = false;

                        }

                    }

                    if (read_all.contains("biome : ") == true) {

                        font_text = read_all.substring(0, read_all.indexOf("biome : "));
                        name = font_text.replace(" ", "").replace(">", " > ").replace("/", " > ").replace("|", "");

                        buffered_writer.newLine();
                        buffered_writer.write("----------------------------------------------------------------------------------------------------");
                        buffered_writer.newLine();

                        if (lock == true) {

                            buffered_writer.write("[LOCK] ");

                        } else {

                            buffered_writer.write("[] ");

                        }

                        buffered_writer.write(name);
                        buffered_writer.newLine();
                        buffered_writer.write("----------------------------------------------------------------------------------------------------");

                    }

                    if (read_all.contains("    |    ") == true) {

                        buffered_writer.newLine();
                        buffered_writer.write(read_all.replace(font_text, ""));

                    }
        
                } buffered_reader.close();

            } catch (Exception e) {

                TanshugetreesMod.LOGGER.error(e);
                
            }

            buffered_writer.close();
            writer.close();

        }

    }

     */



    // ----------------------------------------------------------------------------------------------------
    // Create Temp File
    // ----------------------------------------------------------------------------------------------------



    private static void create_temp () throws Exception {

        Path from = Paths.get(directory_file);
        Path to = Paths.get(directory_file_temp);

        try {

            Files.walk(from).forEach(source -> {

                Path copy = to.resolve(from.relativize(source));

                try {

                    Files.copy(source, copy, StandardCopyOption.REPLACE_EXISTING);

                } catch (Exception e) {

                    TanshugetreesMod.LOGGER.error("Error copying file : " + e.getMessage());

                }

            });

        } catch (Exception e) {

            TanshugetreesMod.LOGGER.error("Error walking the source directory : " + e.getMessage());

        }

    }



    // ----------------------------------------------------------------------------------------------------
    // Create
    // ----------------------------------------------------------------------------------------------------



    private static void create () throws Exception {

        if (Paths.get(directory_file).toFile().exists() == false) {

            if (world instanceof ServerLevel _level) {
                _level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL,new Vec3(x, y, z), Vec2.ZERO, _level, 4, "",Component.literal(""), _level.getServer(), null).withSuppressedOutput(),("execute if entity @e[type=player,distance=..0.01] run tellraw @a [{\"text\":\"THT : Repaired \",\"color\":\"green\"},{\"text\":\"config_placement.txt\",\"color\":\"white\"}]"));
            }

        }

        FileWriter writer = new FileWriter(directory_file, false);
        BufferedWriter buffered_writer = new BufferedWriter(writer);

            try {

                buffered_writer.write("This config is auto apply the changes. Repair missing values by command [/THT config repair] or restart the world.");
                buffered_writer.newLine();
                buffered_writer.newLine();
                buffered_writer.write("IMPORTANT!");
                buffered_writer.newLine();
                buffered_writer.write("- If you change something in this config, you need to lock your settings, by write [LOCK] at font of the name of it.");
                buffered_writer.newLine();
                buffered_writer.newLine();
                buffered_writer.write("SETTINGS INFO");
                buffered_writer.newLine();
                buffered_writer.write("- [biome] is where biome the tree can spawn in, supported both multiple biomes and biome tags.");
                buffered_writer.newLine();
                buffered_writer.write("    - Use [@all] for all biomes");
                buffered_writer.newLine();
                buffered_writer.write("    - Use [@vanilla] for vanilla biomes");
                buffered_writer.newLine();
                buffered_writer.write("    - Add [!] for if-not");
                buffered_writer.newLine();
                buffered_writer.write("    - Add [&] for compulsion (the value must be true, so can spawn that tree.)");
                buffered_writer.newLine();
                buffered_writer.write("- [ground_block] is block the tree can spawn on, supported both multiple blocks and block tags.");
                buffered_writer.newLine();
                buffered_writer.write("    - Use [@all] for all blocks");
                buffered_writer.newLine();
                buffered_writer.write("    - Add [!] for if-not");
                buffered_writer.newLine();
                buffered_writer.write("    - Add [&] for compulsion (the value must be true, so can spawn that tree.)");
                buffered_writer.newLine();
                buffered_writer.write("- [rarity] is how common of the tree. Only supported number between 0 to 100 (Higher make it more common). Set to 0 to disable it.");
                buffered_writer.newLine();
                buffered_writer.write("- [min_distance] is min distance in blocks between trees in the same species. May not work in some cases, cause by unloaded chunks.");
                buffered_writer.newLine();
                buffered_writer.write("- [group_chance] is a chance to spawn near others in the same species. Work good with trees that have low rarity. Only supported number between 0 to 1");
                buffered_writer.newLine();
                buffered_writer.write("- [waterside_chance] is a chance to spawn near water. Set to 1 for only waterside. Only supported number between 0 to 1");
                buffered_writer.newLine();
                buffered_writer.write("- [dead_tree_chance] is a chance to spawn as dead tree, no leaves and sometimes no twig or even become hollowed tree. Only supported number between 0 to 1");
                buffered_writer.newLine();
                buffered_writer.write("- [dead_tree_level_min] is max level of dead tree. Useful for some trees such as coconut tree that use branch part as leaves. Only supported number between 1 to 7");
                buffered_writer.newLine();
                buffered_writer.newLine();
                buffered_writer.newLine();
                buffered_writer.newLine();
                buffered_writer.newLine();

            } catch (Exception e) {

                TanshugetreesMod.LOGGER.error(e);
                
            }

        buffered_writer.close();
        writer.close();

    }



    // ----------------------------------------------------------------------------------------------------
    // Scan & Write
    // ----------------------------------------------------------------------------------------------------



    private static void scan_write () {

        try {

            Files.walk(Paths.get(directory_pack)).forEach(source -> {

                String name_pack = "";
                
                if (source.toFile().isDirectory() == true) {

                    if (source.toFile().getName().equals(".organized") == false) {

                        String name_pack_test = source.getName(Paths.get(directory_pack).getNameCount()).toString();

                        if (name_pack.equals(name_pack_test) == false) {

                            name_pack = name_pack_test;

                        }

                    }

                } else {

                    if (source.toString().contains("\\world_gen\\") == true) {

                        name_pack = source.getName(source.getNameCount() - 4).toString();
                        
                        String name_theme = source.getName(source.getNameCount() - 2).toString();
                        String name_tree = source.getName(source.getNameCount() - 1).toString().replace(".txt", "");

                        String name = name_pack + " > " + name_theme + " > " + name_tree;

                        write(source, name, test_exists(name) == true);

                    }

                }

            });

        } catch (Exception e) {

            TanshugetreesMod.LOGGER.error(e);
            
        }




        //  At the end of the file
        try {

            FileWriter writer = new FileWriter(directory_file, true);
            BufferedWriter buffered_writer = new BufferedWriter(writer);

            try {

                buffered_writer.newLine();
                buffered_writer.write("----------------------------------------------------------------------------------------------------");

            } catch (Exception e) {

                TanshugetreesMod.LOGGER.error(e);
                
            }

            buffered_writer.close();
            writer.close();
            
        } catch (Exception e) {

            TanshugetreesMod.LOGGER.error(e);

        }

    }



    // ----------------------------------------------------------------------------------------------------
    // Test Exists
    // ----------------------------------------------------------------------------------------------------



    private static boolean test_exists (String name) {

        Boolean exists = false;

        try {

            BufferedReader buffered_reader = new BufferedReader(new FileReader(directory_file_temp));
            String read_all = "";

            while ((read_all = buffered_reader.readLine()) != null) {

                if (read_all.endsWith("[LOCK] " + name) == true) {

                    exists = true;

                    break;

                }
    
            } buffered_reader.close();

        } catch (Exception e) {

            TanshugetreesMod.LOGGER.error(e);
            
        }

        return exists;

    }



    // ----------------------------------------------------------------------------------------------------
    // Write
    // ----------------------------------------------------------------------------------------------------



    private static void write (Path source, String name, boolean exists) {

        try {

            FileWriter writer = new FileWriter(directory_file, true);
            BufferedWriter buffered_writer = new BufferedWriter(writer);

            try {

                buffered_writer.newLine();
                buffered_writer.write("----------------------------------------------------------------------------------------------------");
                buffered_writer.newLine();

                if (name.startsWith("[INCOMPATIBLE] ") == true) {

                    buffered_writer.write("[INCOMPATIBLE] ");

                }

                if (exists == true) {

                    buffered_writer.write("[LOCK] ");

                } else {

                    buffered_writer.write("[] ");

                }
                
                buffered_writer.write(name.replace("[INCOMPATIBLE] ", ""));
                buffered_writer.newLine();
                buffered_writer.write("----------------------------------------------------------------------------------------------------");                



                BufferedReader buffered_reader = new BufferedReader(new FileReader(source.toFile()));
                String read_all = "";
                boolean repair = false;

                while ((read_all = buffered_reader.readLine()) != null) {

                    if (

                        read_all.equals("") == false
                        &&
                        (
                            read_all.startsWith("storage_directory") == false
                            &&
                            read_all.startsWith("tree_settings") == false
                        )

                    ) {

                        if (exists == false) {

                            buffered_writer.newLine();
                            buffered_writer.write(read_all);

                        } else {

                            BufferedReader file_buffered_reader2 = new BufferedReader(new FileReader(Paths.get(directory_file_temp).toFile()));
                            String read_all2 = "";
                            boolean skip = false;

                            while ((read_all2 = file_buffered_reader2.readLine()) != null) {

                                if (skip == false) {

                                    if (read_all2.endsWith("[LOCK] " + name) == true) {

                                        skip = true;
            
                                    }
                                    
                                } else {

                                    if (read_all2.startsWith("[")) {

                                        buffered_writer.newLine();
                                        buffered_writer.write(read_all);   //  Write New Value

                                        repair = true;

                                        break;
                                    
                                    }

                                    if (read_all2.indexOf(" = ") != -1) {

                                        if (read_all.startsWith(read_all2.substring(0, read_all2.indexOf(" = "))) == true) {

                                            buffered_writer.newLine();
                                            buffered_writer.write(read_all2);   //  Write Old Value

                                            break;

                                        }
                                        
                                    }

                                }
                    
                            } file_buffered_reader2.close();

                        }

                    }
        
                } buffered_reader.close();

                if (repair == true) {

                    String tree_name = name;

                    tree_name = tree_name.substring(tree_name.indexOf(" > ") + 3);
                    tree_name = tree_name.substring(tree_name.indexOf(" > ") + 3);

                    String from = name.replace(" > " + tree_name, "");

                    if (world instanceof ServerLevel _level) {
                        _level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL,new Vec3(x, y, z), Vec2.ZERO, _level, 4, "",Component.literal(""), _level.getServer(), null).withSuppressedOutput(),("execute if entity @e[type=player,distance=..0.01] run tellraw @a [{\"text\":\"THT : Repaired \",\"color\":\"gray\"},{\"text\":\"" + tree_name + " \",\"color\":\"white\"},{\"text\":\"[?]\",\"color\":\"dark_gray\",\"hoverEvent\":{\"action\":\"show_item\",\"contents\":{\"id\":\"minecraft:air\",\"count\":1,\"tag\":\"{display:{Name:'" + "\\" + "\"" + from + "\\" + "\"'}}\"}}}]"));
                    }

                }

            } catch (Exception e) {

                TanshugetreesMod.LOGGER.error(e);
                
            }

            buffered_writer.close();
            writer.close();

        } catch (Exception e) {

            TanshugetreesMod.LOGGER.error(e);
            
        }

    }



    // ----------------------------------------------------------------------------------------------------
    // Delete Temp
    // ----------------------------------------------------------------------------------------------------



    private static void delete_temp () {

        try {

            Files.delete(Paths.get(directory_file_temp));
            
        } catch (Exception e) {

            TanshugetreesMod.LOGGER.error(e);

        }

    }

}