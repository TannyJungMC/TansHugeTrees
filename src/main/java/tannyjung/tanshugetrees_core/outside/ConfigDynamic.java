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

            String[] split = null;
            String option_name = "";

            for (String scan : options.split("\n")) {

                if (scan.startsWith("# ") == false) {

                    split = scan.split(" = ");
                    default_values.put(split[0], split[1]);
                    option_name = split[0];

                } else {

                    description.put(option_name, scan.substring("# ".length()));

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

            for (Map.Entry<String, Map<String, String>> entry : data.entrySet()) {

                CacheManager.DataText.setMap("config_" + name, entry.getKey(), entry.getValue());

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

        Map<String, String> options = OutsideUtils.convertFileToDataMap(source.toString());
        String value = "";

        for (Map.Entry<String, String> entry : default_values.entrySet()) {

            if (options.containsKey(entry.getKey()) == true) {

                value = options.get(entry.getKey());

                if (value.isEmpty() == true) {

                    value = "none";

                }

                if (temp.containsKey(id) == true && temp.get(id).containsKey(entry.getKey()) == true) {

                    if (lock == true) {

                        write.append(entry.getKey()).append(" = ").append(temp.get(id).get(entry.getKey()));

                    } else {

                        write.append(entry.getKey()).append(" = ").append(value);

                    }

                } else {

                    write.append(entry.getKey()).append(" = ").append(value);

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
        String[] split = null;
        String id = "";
        boolean skip = true;

        for (String scan : FileManager.readTXT(Core.path_config + "/config_" + name + ".txt")) {

            {

                if (scan.isEmpty() == false) {

                    if (scan.startsWith("[") == true) {

                        // First Data
                        {

                            if (scan.startsWith("[INCOMPATIBLE] ") == true) {

                                skip = true;
                                scan = scan.substring("[INCOMPATIBLE] ".length());

                            } else {

                                skip = false;

                            }

                            id = scan.substring(scan.indexOf("]") + 2).replace(" > ", "/");
                            data.computeIfAbsent(id, create -> new HashMap<>()).put("lock", String.valueOf(scan.startsWith("[LOCK] ") == true));

                        }

                    } else if (scan.contains(" = ") == true) {

                        split = scan.split(" = ");

                        if (skip == false) {

                            if (split[1].equals("none") == true) {

                                split[1] = "";

                            }

                            data.computeIfAbsent(id, create -> new HashMap<>()).put(split[0], split[1]);

                        } else {

                            if (default_values.containsKey(split[0]) == true) {

                                split[1] = default_values.get(split[0]);
                                data.computeIfAbsent(id, create -> new HashMap<>()).put(split[0], split[1]);

                            }

                        }

                    }

                }

            }

        }

        return data;

    }

    public static Map<String, Map<String, String>> getData (String name) {

        return CacheManager.DataText.getMap("config_" + name);

    }

}