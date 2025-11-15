package tannyjung.tanshugetrees_handcode.config;


import tannyjung.core.Utils;
import tannyjung.tanshugetrees_handcode.Handcode;
import tannyjung.core.FileManager;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class ConfigWorldGen {

    public static void start () {

        createTemp();
        create();
        deleteTemp();

    }

    private static void createTemp () {

        File from = new File(Handcode.path_config + "/config_world_gen.txt");
        File to = new File(Handcode.path_config + "/config_world_gen_temp.txt");

        if (from.exists() == true && from.isDirectory() == false) {

            try {

                Files.copy(from.toPath(), to.toPath(), StandardCopyOption.REPLACE_EXISTING);

            } catch (Exception exception) {

                Utils.misc.exception(new Exception(), exception);

            }

        }

    }

    private static void create () {

        File file = new File(Handcode.path_config + "/config_world_gen.txt");
        File file_organized = new File(Handcode.path_config + "/#dev/custom_packs_organized/world_gen");

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
                        
                        - world_gen : Enable world generation for that tree by set to [ true ], or disable by [ false ].
                        - biome / ground_block : Change the biome and ground block that tree can place on. Supported both IDs and tags. These config supported multiple conditions, use [ / ] for [ OR ], use [ , ] for [ AND ]. For example, a tree that spawn in 2 main biomes. One is biomes tagged as forest, but not birch forest. Other one is taiga forest. It will be [ #minecraft:is_forest, !minecraft:birch_forest / minecraft:taiga ]. Important note for ground block, it not works with trees bigger than about 80x80 blocks.
                        - rarity : Change how common of that tree. Lower means rarer. Only supported number between 0 and 100 (can be non-integer number).
                        - min_distance : Change distance of trees in the same species. This is distance in block with Y position ignored. Only supported number between 0 to 500.
                        - group_size : Use other placement system to spawn that tree in group style. To use this, set min and max count of trees per group that upper than 1. For example, min 1 and max 5, will be [ 1 <> 5 ]. Be careful to use this, as it can affect scan time. This config also change the way other config options work. Rarity will be how common of the group. Min distance is between trees, not between groups. Waterside config will only detect once at spawn location of that group.
                        - waterside_chance : Force that tree to only spawn near water biomes. If this chance is not full, it will spawn like normal for that chance left. For example, set this to 0.75, it will spawn like normal tree for 0.25 chance. When use this option with group spawning, it will only detect once at spawn location of that group.
                        - dead_tree_chance : Set how common of that tree to spawn as dead tree. Note that this config only affect trees in their viable ecosystems, you may found some trees that become dead trees without this config, because that's by tree type inside tree settings. Land trees can't survive in water, etc.
                        - dead_tree_level : Randomly select style of dead trees, make that tree looks more variants when it's dead tree. This config will be random select a number from the list, or use "auto" and "auto_pine" for automatic selection. Only supported numbers 1XX/2XX/3XX with sub numbers 10/20/30/40/50/60/70/80/90 and 11/21/31/41/51. Set to 1XX for normal dead trees, 2XX and 3XX for coarse woody debris style but with and without roots. For sub numbers 10/20/30/40/50 is no leaves, no sprig, no twig, no limb, and no branch. With random decay 10-50%. For 11/21/31/41/51 is the same as previous but no random decay. For 60/70 is only trunk with random length 50-100% and hollowed. For 80/90 is only trunk with random length 0-50% and hollowed.
                        - start_height_offset : Randomly spawn that tree with custom height from the ground. To use this, set min and max height. For example, lowest -10 highest +10, will be [ -10 <> 10 ].
                        - rotation : Set rotation of that tree. For random direction, use [ random ]. For specific direction, use [ north ], [ west ], [ east ], or [ south ]. Only supported one value per tree.
                        - mirrored : Set mirror effect for that tree. For random value, use [ random ]. For specific value, use [ true ] or [ false ]. Only supported one value per tree.
                        
                        """

                );

            }

            FileManager.writeTXT(file.toPath().toString(), write.toString(), false);

        }

        if (file_organized.exists() == false) {

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

        } else {

            // Scan Packs
            {

                try {

                    Files.walk(file_organized.toPath()).forEach(source -> {

                        if (source.toFile().isDirectory() == false) {

                            write(source);

                        }

                    });

                } catch (Exception exception) {

                    Utils.misc.exception(new Exception(), exception);

                }

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

    private static void write (Path source) {

        String name_pack = source.getParent().getParent().toFile().getName();
        String name_theme = source.getParent().toFile().getName();
        String name_tree = source.toFile().getName().replace(".txt", "");
        String name = name_pack + " > " + name_theme + " > " + name_tree;
        boolean incompatible = false;

        if (name.contains("[INCOMPATIBLE] ") == true) {

            name = name.replace("[INCOMPATIBLE] ", "");
            incompatible = true;

        }

        boolean replace = true;

        // Test is it locked
        {

            for (String read_all : FileManager.readTXT(Handcode.path_config + "/config_world_gen_temp.txt")) {

                {

                    if (read_all.startsWith("[") == true && read_all.endsWith("] " + name) == true) {

                        if (read_all.replace("[INCOMPATIBLE] ", "").startsWith("[LOCK] ") == true) {

                            replace = false;

                        }

                        break;

                    }

                }

            }

        }

        StringBuilder write = new StringBuilder();

        // Write
        {

            // Name
            {

                write.append("----------------------------------------------------------------------------------------------------");
                write.append("\n");

                if (incompatible == true) {

                    write.append("[INCOMPATIBLE] ");

                }

                if (replace == false) {

                    write.append("[LOCK] ");

                } else {

                    write.append("[] ");

                }

                write.append(name);
                write.append("\n");
                write.append("----------------------------------------------------------------------------------------------------");
                write.append("\n");

            }

            String option = "";

            for (String read_all : FileManager.readTXT(source.toString())) {

                {

                    if (read_all.equals("") == false) {

                        if (read_all.startsWith("path_") == false) {

                            if (replace == true) {

                                write.append(read_all);

                            } else {

                                // Get Old Value
                                {

                                    File file_temp = new File(Handcode.path_config + "/config_world_gen_temp.txt");
                                    boolean thisID = false;
                                    option = read_all.substring(0, read_all.indexOf(" = "));

                                    for (String read_all_temp : FileManager.readTXT(file_temp.getPath())) {

                                        {

                                            if (thisID == false) {

                                                if (read_all_temp.startsWith("[") == true) {

                                                    if (read_all_temp.endsWith(name) == true) {

                                                        thisID = true;

                                                    }

                                                }

                                            } else {

                                                if (read_all_temp.startsWith(option) == true) {

                                                    write.append(read_all_temp);
                                                    break;

                                                } else if (read_all_temp.startsWith("[") == true) {

                                                    // If not found this option in temp file
                                                    write.append(read_all_temp);
                                                    break;

                                                }

                                            }

                                        }

                                    }

                                }

                            }

                            write.append("\n");

                        }

                    }

                }

            }

        }

        FileManager.writeTXT(Handcode.path_config + "/config_world_gen.txt", write.toString(), true);

    }

    private static void deleteTemp () {

        File file = new File(Handcode.path_config + "/config_world_gen_temp.txt");

        if (file.exists() == true && file.isDirectory() == false) {

            try {

                Files.delete(file.toPath());

            } catch (Exception exception) {

                Utils.misc.exception(new Exception(), exception);

            }

        }

    }

}