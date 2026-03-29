package tannyjung.tanshugetrees_core.outside;


import tannyjung.tanshugetrees_core.Core;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class ConfigDynamic {

    public static void reorganize (String name, String scan_at, String options) {

        LinkedHashMap<String, String> description = new LinkedHashMap<>();

        // Get Data
        {

            List<String> data = new ArrayList<>();
            String[] split = new String[0];

            for (String read_all : options.split("\n")) {

                if (read_all.startsWith("# ") == false) {

                    data.add(read_all);
                    split = read_all.split(" = ");

                } else {

                    description.put(split[0], read_all.substring("# ".length()));

                }

            }

            CacheManager.Data.setMapTextListText("config_dynamic_default", name, data);

        }

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

                Map<String, Map<String, String>> temp = getData(name);

                try {

                    Files.walk(file_organized.toPath()).forEach(source -> {

                        if (source.toFile().isDirectory() == false) {

                            write.append(write(name, scan_at, temp, source));

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

    private static String write (String name, String scan_at, Map<String, Map<String, String>> temp, Path source) {

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
        String[] split = new String[0];

        for (String scan : CacheManager.Data.getMapTextListText("config_dynamic_default").get(name)) {

            split = scan.split(" = ");

            if (options.containsKey(split[0]) == true) {

                if (temp.containsKey(id) == true && temp.get(id).containsKey(split[0]) == true) {

                    if (lock == true) {

                        write.append(split[0]).append(" = ").append(temp.get(id).get(split[0]));

                    } else {

                        write.append(split[0]).append(" = ").append(options.get(split[0]));

                    }

                    write.append("\n");
                    continue;

                }

            }

            write.append(split[0]).append(" = ").append(split[1]);
            write.append("\n");

        }

        return write.toString();

    }

    public static Map<String, Map<String, String>> getData (String name) {

        if (CacheManager.Data.existMapTextLogic("create_config_" + name, "") == false) {

            CacheManager.Data.setMapTextLogic("create_config_" + name, "", true);

            Map<String, String> default_values = new HashMap<>();

            // Get Default Values
            {

                String[] split = new String[0];

                for (String scan : CacheManager.Data.getMapTextListText("config_dynamic_default").get(name)) {

                    split = scan.split(" = ");
                    default_values.put(split[0], split[1]);

                }

            }

            Map<String, Map<String, String>> data = new HashMap<>();
            String[] value = new String[0];
            String id = "";
            boolean skip = true;

            if (CacheManager.Data.existMapTextListText("config_dynamic", name) == false) {

                CacheManager.Data.setMapTextListText("config_dynamic", name, FileManager.readTXT(Core.path_config + "/config_" + name + ".txt"));

            }

            for (String read_all : CacheManager.Data.getMapTextListText("config_dynamic").get(name)) {

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
                                data.computeIfAbsent(id, test -> new HashMap<>()).put("lock", String.valueOf(read_all.startsWith("[LOCK] ") == true));

                            }

                        } else if (read_all.contains(" = ") == true) {

                            if (skip == false) {

                                value = read_all.split(" = ");
                                data.computeIfAbsent(id, test -> new HashMap<>()).put(value[0], value[1]);

                            } else {

                                value = read_all.split(" = ");

                                if (default_values.containsKey(value[0]) == true) {

                                    value[1] = default_values.get(value[0]);
                                    data.computeIfAbsent(id, test -> new HashMap<>()).put(value[0], value[1]);

                                }

                            }

                        }

                    }

                }

            }

            for (Map.Entry<String, Map<String, String>> entry : data.entrySet()) {

                CacheManager.Data.setMapTextMapTextText("config_" + name, entry.getKey(), entry.getValue());

            }

        }

        return CacheManager.Data.getMapTextMapTextText("config_" + name);

    }

}