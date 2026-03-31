package tannyjung.tanshugetrees_core.outside;


import tannyjung.tanshugetrees_core.Core;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class ConfigDynamic {

    public static void reorganize (String name, String scan_at, String options) {

        LinkedHashMap<String, String> default_values = new LinkedHashMap<>();
        LinkedHashMap<String, String> description = new LinkedHashMap<>();

        // Get Default and Description
        {

            String[] split = new String[0];
            String option_name = "";

            for (String read_all : options.split("\n")) {

                if (read_all.startsWith("# ") == false) {

                    split = read_all.split(" = ");
                    default_values.put(split[0], split[1]);
                    option_name = split[0];

                } else {

                    description.put(option_name, read_all.substring("# ".length()));

                }

            }

        }

        // Generate
        {

            StringBuilder write = new StringBuilder();
            write.append("Important Notes");
            write.append("\n");
            write.append("\n");
            write.append("- To apply this config and repair missing values, run this command [ /").append(Core.mod_id_big).append(" restart ] or restart the world.");
            write.append("\n");
            write.append("- Very important! You must lock the settings you have edited to prevent them from resetting. Do it by change [] at font of ID to [LOCK]. This is for keeping config values you haven't changed to always up to date.");
            write.append("\n");
            write.append("\n");
            write.append("Config Description");
            write.append("\n");
            write.append("\n");

            // Write Description
            {

                for (Map.Entry<String, String> entry : description.entrySet()) {

                    write.append("- ").append(entry.getKey()).append(" : ").append(description.get(entry.getKey()));
                    write.append("\n");

                }

            }

            write.append("\n");
            File file_organized = new File(Core.path_config + "/dev/temporary/" + scan_at);

            if (file_organized.exists() == false) {

                write.append("----------------------------------------------------------------------------------------------------");
                write.append("\n");
                write.append("\n");
                write.append("Nothing to show here. Try create a world and join in first.");
                write.append("\n");
                write.append("\n");

            } else {

                // Scan Packs
                {

                    Map<String, Map<String, String>> temp = read(default_values, name);

                    try {

                        Files.walk(file_organized.toPath()).forEach(source -> {

                            if (source.toFile().isDirectory() == false) {

                                write.append(write(default_values, temp, scan_at, source));

                            }

                        });

                    } catch (Exception exception) {

                        OutsideUtils.exception(new Exception(), exception, "");

                    }

                }

            }

            write.append("----------------------------------------------------------------------------------------------------");
            FileManager.writeTXT(Core.path_config + "/config_" + name + ".txt", write.toString(), false);

        }

        // Apply
        {

            Map<String, Map<String, String>> data = read(default_values, name);

            if (data.isEmpty() == true) {

                CacheManager.Data.setMapTextMapTextText("config_" + name, "", null);

            } else {

                for (Map.Entry<String, Map<String, String>> entry : data.entrySet()) {

                    CacheManager.Data.setMapTextMapTextText("config_" + name, entry.getKey(), entry.getValue());

                }

            }

        }

    }

    private static String write (LinkedHashMap<String, String> default_values, Map<String, Map<String, String>> temp, String scan_at, Path source) {

        String id = Path.of(Core.path_config + "/dev/temporary/" + scan_at).relativize(source).toString().replace("\\", "/").replace(".txt", "");;
        boolean incompatible = false;

        if (id.contains("[INCOMPATIBLE] ") == true) {

            id = id.replace("[INCOMPATIBLE] ", "");
            incompatible = true;

        }

        boolean lock = false;

        if (temp.containsKey(id) == true) {

            lock = temp.get(id).get("lock").equals("true") == true;

        }

        StringBuilder write = new StringBuilder();

        // Name
        {

            write.append("----------------------------------------------------------------------------------------------------");
            write.append("\n");

            if (incompatible == true) {

                write.append("[INCOMPATIBLE] ");

            }

            if (lock == true) {

                write.append("[LOCK] ");

            } else {

                write.append("[] ");

            }

            write.append(id.replace("/", " > "));
            write.append("\n");
            write.append("----------------------------------------------------------------------------------------------------");
            write.append("\n");

        }

        Map<String, String> options = FileManager.convertFileToDataMap(source.toString());

        for (Map.Entry<String, String> entry : default_values.entrySet()) {

            if (options.containsKey(entry.getKey()) == true) {

                if (temp.containsKey(id) == true && temp.get(id).containsKey(entry.getKey()) == true) {

                    if (lock == true) {

                        write.append(entry.getKey()).append(" = ").append(temp.get(id).get(entry.getKey()));

                    } else {

                        write.append(entry.getKey()).append(" = ").append(options.get(entry.getKey()));

                    }

                } else {

                    write.append(entry.getKey()).append(" = ").append(options.get(entry.getKey()));

                }

            } else {

                write.append(entry.getKey()).append(" = ").append(entry.getValue());

            }

            write.append("\n");

        }

        return write.toString();

    }

    private static Map<String, Map<String, String>> read (Map<String, String> default_values, String name) {

        Map<String, Map<String, String>> data = new HashMap<>();
        String[] value = new String[0];
        String id = "";
        boolean skip = true;

        for (String read_all : FileManager.readTXT(Core.path_config + "/config_" + name + ".txt")) {

            {

                if (read_all.isEmpty() == false) {

                    if (read_all.startsWith("[") == true) {

                        // First Data
                        {

                            if (read_all.startsWith("[INCOMPATIBLE] ") == true) {

                                skip = true;
                                read_all = read_all.substring("[INCOMPATIBLE] ".length());

                            } else {

                                skip = false;

                            }

                            id = read_all.substring(read_all.indexOf("]") + 2).replace(" > ", "/");
                            data.computeIfAbsent(id, create -> new HashMap<>()).put("lock", String.valueOf(read_all.startsWith("[LOCK] ") == true));

                        }

                    } else if (read_all.contains(" = ") == true) {

                        if (skip == false) {

                            value = read_all.split(" = ");
                            data.computeIfAbsent(id, create -> new HashMap<>()).put(value[0], value[1]);

                        } else {

                            value = read_all.split(" = ");

                            if (default_values.containsKey(value[0]) == true) {

                                value[1] = default_values.get(value[0]);
                                data.computeIfAbsent(id, create -> new HashMap<>()).put(value[0], value[1]);

                            }

                        }

                    }

                }

            }

        }

        return data;

    }

    public static Map<String, Map<String, String>> getData (String name) {

        return CacheManager.Data.getMapTextMapTextText("config_" + name);

    }

}