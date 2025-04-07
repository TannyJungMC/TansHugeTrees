package tannyjung.tanshugetrees_handcode.config;

import tannyjung.tanshugetrees.TanshugetreesMod;
import tannyjung.tanshugetrees_handcode.Handcode;
import tannyjung.tanshugetrees_handcode.misc.FileManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class ConfigPlacement {

    public static void start () {

        createTemp();
        create();
        deleteTemp();

    }

    private static void createTemp () {

        Path from = Paths.get(Handcode.directory_config + "/config_placement.txt");
        Path to = Paths.get(Handcode.directory_config + "/config_placement_temp.txt");

        try {

            Files.walk(from).forEach(source -> {

                Path copy = to.resolve(from.relativize(source));

                try {

                    Files.copy(source, copy, StandardCopyOption.REPLACE_EXISTING);

                } catch (Exception e) {

                    TanshugetreesMod.LOGGER.error(e.getMessage());

                }

            });

        } catch (Exception e) {

            TanshugetreesMod.LOGGER.error(e.getMessage());

        }

    }

    private static void create () {

        File file_organized = new File(Handcode.directory_config + "/custom_packs/.organized/world_gen");
        File file = new File(Handcode.directory_config + "/config_placement.txt");

        // Re-Create The File
        {

            StringBuilder write = new StringBuilder();

            {

                write.append("""
                - No need to apply this config, as it automatic applying.
                - To repair missing values, run this command [ /tanshugetrees config repair ] or restart the world.
                - Very important! You must lock the trees you have edited, to mark it as don't reset. Do it by change "[]" at font of tree ID to "[LOCK]".
                
                multiply_rarity = 1.0
                multiply_min_distance = 1.0
                multiply_group_size = 1.0
                multiply_waterside_chance = 1.0
                multiply_dead_tree_chance = 1.0
                
                """);

            }

            FileManager.writeConfigTXT(file.toPath().toString(), write.toString());

        }

        if (file_organized.exists() == true) {

            if (file_organized.listFiles().length > 0) {

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

                    } catch (Exception e) {

                        TanshugetreesMod.LOGGER.error(e.getMessage());

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

        // Test Locked
        {

            File file = new File(Handcode.directory_config + "/config_placement_temp.txt");

            if (file.exists() == true && file.isDirectory() == false) {

                {

                    try { BufferedReader buffered_reader = new BufferedReader(new FileReader(file)); String read_all = ""; while ((read_all = buffered_reader.readLine()) != null) {

                        {

                            if (read_all.startsWith("[") == true) {

                                if (read_all.endsWith("[LOCK] " + name.replace("[INCOMPATIBLE] ", "")) == true) {

                                    replace = false;
                                    break;

                                }

                            }

                        }

                    } buffered_reader.close(); } catch (Exception e) { TanshugetreesMod.LOGGER.error(e.getMessage()); }

                }

            }

        }

        File file = new File(Handcode.directory_config + "/config_placement.txt");

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

                    write.append(name.replace("[INCOMPATIBLE] ", ""));
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

                                                File file_temp = new File(Handcode.directory_config + "/config_placement_temp.txt");
                                                boolean thisID = false;
                                                option = read_all.substring(0, read_all.indexOf(" = "));

                                                {

                                                    try { BufferedReader buffered_reader2 = new BufferedReader(new FileReader(file_temp)); String read_all2 = ""; while ((read_all2 = buffered_reader2.readLine()) != null) {

                                                        {

                                                            if (thisID == false) {

                                                                if (read_all2.startsWith("[") == true) {

                                                                    if (read_all2.endsWith(name.replace("[INCOMPATIBLE] ", "")) == true) {

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

                                                    } buffered_reader2.close(); } catch (Exception e) { TanshugetreesMod.LOGGER.error(e.getMessage()); }

                                                }

                                            }

                                        }

                                        write.append("\n");

                                    }

                                }

                            }

                        } buffered_reader.close(); } catch (Exception e) { TanshugetreesMod.LOGGER.error(e.getMessage()); }

                    }

                }

                FileManager.writeTXT(file.toPath().toString(), write.toString(), true);

            }

        }

    }

    private static void deleteTemp () {

        File file = new File(Handcode.directory_config + "/config_placement_temp.txt");

        if (file.exists() == true && file.isDirectory() == false) {

            try {

                Files.delete(file.toPath());

            } catch (Exception e) {

                TanshugetreesMod.LOGGER.error(e.getMessage());

            }

        }

    }

}