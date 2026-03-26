package tannyjung.tanshugetrees_core.outside;


import tannyjung.tanshugetrees_core.Core;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;

public class ConfigDynamic {

    public static void reorganize (String name, String scan_at, String description) {

        File file = new File(Core.path_config + "/config_" + name + ".txt");
        File file_temp = new File(Core.path_config + "/config_" + name + "_temp.txt");

        // Create Temp
        {

            if (file.exists() == true) {

                try {

                    Files.copy(file.toPath(), file_temp.toPath(), StandardCopyOption.REPLACE_EXISTING);

                } catch (Exception exception) {

                    OutsideUtils.exception(new Exception(), exception, "");

                }

            }

        }

        create(name, scan_at, description);

        // Delete Temp
        {

            if (file_temp.exists() == true) {

                try {

                    Files.delete(file_temp.toPath());

                } catch (Exception exception) {

                    OutsideUtils.exception(new Exception(), exception, "");

                }

            }

        }

    }

    private static void create (String name, String scan_at, String description) {

        File file_organized = new File(Core.path_config + "/#dev/temporary/" + scan_at);

        if (file_organized.exists() == true && file_organized.isDirectory() == true) {

            File file = new File(Core.path_config + "/config_" + name + ".txt");

            // Re-Create The File
            {

                StringBuilder write = new StringBuilder();

                {

                    write.append("Important Notes");
                    write.append("\n");
                    write.append("\n");
                    write.append("- To apply this config and repair missing values, run this command [ /").append(Core.mod_id_big).append(" restart ] or restart the world.");
                    write.append("\n");
                    write.append("- Very important! You must lock the trees you have edited to prevent them from resetting. Do it by change \"[]\" at font of ID to \"[LOCK]\". This is for keeping config values you haven't changed to always up to date.");
                    write.append("\n");
                    write.append("\n");
                    write.append("Config Description");
                    write.append("\n");
                    write.append("\n");
                    write.append(description);
                    write.append("\n");

                }

                FileManager.writeTXT(file.toPath().toString(), write.toString(), false);

            }

            // Scan Packs
            {

                try {

                    Files.walk(file_organized.toPath()).forEach(source -> {

                        if (source.toFile().isDirectory() == false) {

                            write(name, scan_at, source);

                        }

                    });

                } catch (Exception exception) {

                    OutsideUtils.exception(new Exception(), exception, "");

                }

            }

            FileManager.writeTXT(file.toPath().toString(), "----------------------------------------------------------------------------------------------------", true);

        }

    }

    private static void write (String name, String scan_at, Path source) {

        String path = Path.of(Core.path_config + "/#dev/temporary/" + scan_at).relativize(source).toString().replace("\\", " > ").replace(".txt", "");;
        boolean incompatible = false;

        if (path.contains("[INCOMPATIBLE] ") == true) {

            path = path.replace("[INCOMPATIBLE] ", "");
            incompatible = true;

        }

        boolean replace = true;

        // Test is it locked
        {

            for (String read_all : FileManager.readTXT(Core.path_config + "/config_" + name + "_temp.txt")) {

                {

                    if (read_all.startsWith("[") == true && read_all.endsWith("] " + path) == true) {

                        if (read_all.replace("[INCOMPATIBLE]", "").startsWith("[LOCK]") == true) {

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

                write.append(path);
                write.append("\n");
                write.append("----------------------------------------------------------------------------------------------------");
                write.append("\n");

            }

            String option = "";

            for (String read_all : FileManager.readTXT(source.toString())) {

                {

                    if (read_all.isEmpty() == false) {

                        if (replace == true) {

                            write.append(read_all);

                        } else {

                            // Get Old Value
                            {

                                File file_temp = new File(Core.path_config + "/config_" + name + "_temp.txt");
                                boolean thisID = false;
                                option = read_all.substring(0, read_all.indexOf(" = "));

                                for (String read_all_temp : FileManager.readTXT(file_temp.getPath())) {

                                    {

                                        if (thisID == false) {

                                            if (read_all_temp.startsWith("[") == true) {

                                                if (read_all_temp.endsWith(path) == true) {

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

        FileManager.writeTXT(Core.path_config + "/config_" + name + ".txt", write.toString(), true);

    }

    public static Map<String, Map<String, String>> getData (String name, String default_values) {

        if (CacheManager.Data.existTextText(name) == false) {

            Map<String, Map<String, String>> data = new HashMap<>();
            Map<String, String> settings = new HashMap<>();
            String[] value = new String[0];
            String id = "";
            boolean skip = true;
            boolean after_this = false;
            String[] split = new String[0];

            if (CacheManager.Data.existTextList("config_dynamic", name) == false) {

                CacheManager.Data.setTextList("config_dynamic", name, FileManager.readTXT(Core.path_config + "/config_" + name + ".txt"));

            }

            for (String read_all : CacheManager.Data.getTextList("config_dynamic", name)) {

                {

                    if (read_all.isEmpty() == false) {

                        if (read_all.startsWith("[") == true) {

                            if (read_all.startsWith("[INCOMPATIBLE]") == true) {

                                skip = true;

                            } else {

                                skip = false;
                                id = read_all.substring(read_all.indexOf("]") + 2).replace(" > ", "/");

                            }

                        } else {

                            if (skip == false) {

                                if (read_all.contains(" = ") == true) {

                                    after_this = true;
                                    value = read_all.split(" = ");
                                    settings.put(value[0], value[1]);

                                } else if (after_this == true) {

                                    after_this = false;

                                    // Test Non-Exist Options
                                    {

                                        for (String option : default_values.split(" / ")) {

                                            split = option.split(" -> ");

                                            if (settings.containsKey(split[0]) == false) {

                                                settings.put(split[0], split[1]);

                                            }

                                        }

                                    }

                                    data.put(id, new HashMap<>(settings));
                                    settings.clear();

                                }

                            }

                        }

                    }

                }

            }

            CacheManager.Data.setTextText(name, data);

        }

        return CacheManager.Data.getTextText(name);

    }

}