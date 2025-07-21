package tannyjung.tanshugetrees_handcode.config;

import tannyjung.core.MiscUtils;
import tannyjung.tanshugetrees_handcode.Handcode;
import tannyjung.core.FileManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class ConfigWorldGen {

    public static void start () {

        createTemp();
        create();
        deleteTemp();

    }

    private static void createTemp () {

        Path from = Paths.get(Handcode.directory_config + "/config_world_gen.txt");
        Path to = Paths.get(Handcode.directory_config + "/config_world_gen_temp.txt");

        try {

            Files.walk(from).forEach(source -> {

                Path copy = to.resolve(from.relativize(source));

                try {

                    Files.copy(source, copy, StandardCopyOption.REPLACE_EXISTING);

                } catch (Exception exception) {

                    MiscUtils.exception(new Exception(), exception);

                }

            });

        } catch (Exception exception) {

            MiscUtils.exception(new Exception(), exception);

        }

    }

    private static void create () {

        File file_organized = new File(Handcode.directory_config + "/custom_packs/.organized/world_gen");
        File file = new File(Handcode.directory_config + "/config_world_gen.txt");

        // Re-Create The File
        {

            StringBuilder write = new StringBuilder();

            {

                write.append("""
                        Important Notes
                        
                        - No need to apply this config, as it automatic applying.
                        - To repair missing values, run this command [ /TANSHUGETREES config repair ] or restart the world.
                        - Very important! You must lock the trees you have edited, to mark it as don't reset. Do it by change "[]" at font of tree ID to "[LOCK]".
                        
                        Config Description
                        
                        - world_gen : Enable world generation for that tree by set to "true", or disable by "false".
                        - biome / ground_block : Change the biome and ground block that tree can place on. Supported both IDs and tags. These config supported multiple conditions, use "/" for "OR", use "," for "AND". For example, a tree that spawn in 2 main biomes. One is biomes tagged as forest, but not birch forest. Other one is taiga forest. It will be "#minecraft:is_forest, !minecraft:birch_forest / minecraft:taiga". Important note for ground block, it not works with trees bigger than about 80x80 blocks.
                        - rarity : Change how common of that tree. Lower means rarer. Only supported number between 0 and 100 (can be non integer).
                        - min_distance : Change distance of trees in the same species. This is distance in block, can be any number, but remember that higher can affect scan time.
                        - group_size : Use other placement system to spawn that tree in group style. To use this, set min and max count of trees per group, that upper than 1. For example, min 1 and max 5, will be "1 <> 5". Be careful to use this, as it can affect scan time a lot. This config also use other config in the system. Rarity will be how common of the group. Min distance is between trees, not groups. Waterside config will only detect once at the spawn location of that group.
                        - waterside_chance : Force that tree to only spawn near water. Be careful to use this, as it can affect scan time a lot.
                        - dead_tree_chance : Set how common of that tree to spawn as dead tree. Note that this config only for trees on surface, you may found some trees that become dead trees without this config, because that's by tree type inside tree settings. Land trees can't survive in water, etc.
                        - dead_tree_level : Make that tree more variety when it's dead tree. This config will be random select a number from the list. Only supported number from 1 to 9. For 12345 is no leaves, no sprig, no twig, no limb, and no branch. For 67 is only trunk with random length 50-100 percent, and hollowed. For 89 is only trunk with random length 10-50 percent, and hollowed.
                        - start_height_offset : Set spawn height from the ground. To use this, set min and max height from the ground.
                        - rotation : Set rotation of that tree. For random direction, use "random". For specific direction, use "north", "west", "east", or "south". Only supported one direction.
                        - mirrored : Set mirror effect for that tree. For random value, use "random". For specific value, use "true" or "false".
                        
                        """

                );

            }

            FileManager.writeTXT(file.toPath().toString(), write.toString(), false);

        }

        if (file_organized.exists() == true) {

            if (file_organized.listFiles() != null && file_organized.listFiles().length > 0) {

                // Scanning The Packs
                {

                    try {

                        Files.walk(file_organized.toPath()).forEach(source -> {

                            if (source.toFile().isDirectory() == false) {

                                String name_pack = source.getParent().getParent().toFile().getName();
                                String name_theme = source.getParent().toFile().getName();
                                String name_tree = source.toFile().getName().replace(".txt", "");

                                write(source, name_pack + " > " + name_theme + " > " + name_tree);

                            }

                        });

                    } catch (Exception exception) {

                        MiscUtils.exception(new Exception(), exception);

                    }

                }

            }

        } else {

            // Not found any pack installed
            {

                StringBuilder write = new StringBuilder();

                {

                    write.append("----------------------------------------------------------------------------------------------------");
                    write.append("\n");
                    write.append("\n");
                    write.append("Not found any pack installed");
                    write.append("\n");
                    write.append("\n");

                }

                FileManager.writeTXT(file.toPath().toString(), write.toString(), true);

            }

        }

        //  At the end of the file
        {

            StringBuilder write = new StringBuilder();

            {

                write.append("----------------------------------------------------------------------------------------------------");

            }

            FileManager.writeTXT(file.toPath().toString(), write.toString(), true);

        }

    }

    private static void write (Path source, String name) {

        boolean replace = true;
        String name_fix = name.replace("[INCOMPATIBLE] ", "");

        // Test Old File
        {

            File file = new File(Handcode.directory_config + "/config_world_gen_temp.txt");

            if (file.exists() == true && file.isDirectory() == false) {

                String read_all_fix = "";

                {

                    try { BufferedReader buffered_reader = new BufferedReader(new FileReader(file), 65536); String read_all = ""; while ((read_all = buffered_reader.readLine()) != null) {

                        {

                            if (read_all.startsWith("[") == true) {

                                if (read_all.endsWith("] " + name_fix) == true) {

                                    read_all_fix = read_all.replace("[INCOMPATIBLE] ", "");

                                    if (read_all_fix.startsWith("[LOCK]") == true) {

                                        replace = false;

                                    }

                                    break;

                                }

                            }

                        }

                    } buffered_reader.close(); } catch (Exception exception) { MiscUtils.exception(new Exception(), exception); }

                }

            }

        }

        File file = new File(Handcode.directory_config + "/config_world_gen.txt");

        if (file.exists() == true && file.isDirectory() == false) {

            {

                StringBuilder write = new StringBuilder();

                {

                    write.append("----------------------------------------------------------------------------------------------------");
                    write.append("\n");

                    // Incompatible and Lock
                    {

                        if (name.startsWith("[INCOMPATIBLE] ") == true || source.toFile().getName().startsWith("[INCOMPATIBLE] ") == true) {

                            write.append("[INCOMPATIBLE] ");

                        }

                        if (replace == false) {

                            write.append("[LOCK] ");

                        } else {

                            write.append("[] ");

                        }

                    }

                    write.append(name_fix);
                    write.append("\n");
                    write.append("----------------------------------------------------------------------------------------------------");
                    write.append("\n");

                    // Read and Write
                    {

                        String option = "";

                        try { BufferedReader buffered_reader = new BufferedReader(new FileReader(source.toFile())); String read_all = ""; while ((read_all = buffered_reader.readLine()) != null) {

                            {

                                if (read_all.equals("") == false) {

                                    if (read_all.startsWith("storage_directory = ") == false && read_all.startsWith("tree_settings = ") == false) {

                                        if (replace == true) {

                                            write.append(read_all);

                                        } else {

                                            // Get Old Value
                                            {

                                                File file_temp = new File(Handcode.directory_config + "/config_world_gen_temp.txt");
                                                boolean thisID = false;
                                                option = read_all.substring(0, read_all.indexOf(" = "));

                                                {

                                                    try { BufferedReader buffered_reader2 = new BufferedReader(new FileReader(file_temp)); String read_all2 = ""; while ((read_all2 = buffered_reader2.readLine()) != null) {

                                                        {

                                                            if (thisID == false) {

                                                                if (read_all2.startsWith("[") == true) {

                                                                    if (read_all2.endsWith(name_fix) == true) {

                                                                        thisID = true;

                                                                    }

                                                                }

                                                            } else {

                                                                if (read_all2.startsWith(option) == true) {

                                                                    write.append(read_all2);
                                                                    break;

                                                                } else if (read_all2.startsWith("[") == true) {

                                                                    // If not found this option in temp file
                                                                    write.append(read_all);
                                                                    break;

                                                                }

                                                            }

                                                        }

                                                    } buffered_reader2.close(); } catch (Exception exception) { MiscUtils.exception(new Exception(), exception); }

                                                }

                                            }

                                        }

                                        write.append("\n");

                                    }

                                }

                            }

                        } buffered_reader.close(); } catch (Exception exception) { MiscUtils.exception(new Exception(), exception); }

                    }

                }

                FileManager.writeTXT(file.toPath().toString(), write.toString(), true);

            }

        }

    }

    private static void deleteTemp () {

        File file = new File(Handcode.directory_config + "/config_world_gen_temp.txt");

        if (file.exists() == true && file.isDirectory() == false) {

            try {

                Files.delete(file.toPath());

            } catch (Exception exception) {

                MiscUtils.exception(new Exception(), exception);

            }

        }

    }

}