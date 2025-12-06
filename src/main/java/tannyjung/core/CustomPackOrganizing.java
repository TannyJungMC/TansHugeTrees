package tannyjung.core;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Blocks;
import tannyjung.core.game.GameUtils;
import tannyjung.tanshugetrees.TanshugetreesMod;
import tannyjung.tanshugetrees_handcode.Handcode;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class CustomPackOrganizing {

    private static final Map<String, String> cache_pack_id = new HashMap<>();
    private static final Map<String, Map<String, List<String>>> cache_error = new HashMap<>();

    public static void start (ServerLevel level_server, String path_config, String pack_separation_single, String pack_separation_multiple) {

        cache_error.clear();

        FileManager.delete(path_config + "/#dev/temporary");
        FileManager.createEmptyFile(path_config + "/#dev/temporary", true);
        FileManager.createEmptyFile(path_config + "/custom_packs", true);

        // Rename All Packs Back
        {

            File[] packs = new File(path_config + "/custom_packs").listFiles();

            if (packs != null) {

                for (File pack : packs) {

                    pack.renameTo(new File(pack.getParentFile().toPath() + "/" + pack.getName().replace("[INCOMPATIBLE] ", "")));

                }

            }

        }

        // Extract ZIP
        {

            File[] packs = new File(path_config + "/custom_packs").listFiles();

            if (packs != null) {

                for (File pack : packs) {

                    if (pack.getName().endsWith(".zip") == true) {

                        FileManager.extractZIP(pack.getPath(), path_config + "/#dev/temporary/pack_zip/" + pack.getName().replace(".zip", ""), false, "");

                    }

                }

            }

        }

        testInfo(path_config);

        // Organizing Info File
        {

            File[] packs = new File(path_config + "/custom_packs").listFiles();

            if (packs != null) {

                File file = null;

                for (File pack : packs) {

                    if (pack.getName().endsWith(".zip") == true) {

                        file = new File(path_config + "/#dev/temporary/pack_zip/" + pack.getName().replace(".zip", "") + "/info.txt");

                    } else {

                        file = new File(pack.getPath() + "/info.txt");

                    }

                    if (file.exists() == true && file.isDirectory() == false) {

                        FileManager.copy(file.getPath(), path_config + "/#dev/temporary/info/" + pack.getName() + ".txt", false);

                    }

                }

            }

        }

        // Organizing Data
        {

            File[] packs = new File(path_config + "/custom_packs").listFiles();

            if (packs != null) {

                File[] inside = new File[0];
                String pack_name = "";
                pack_separation_single = "/" + pack_separation_single + "/";
                pack_separation_multiple = "/" + pack_separation_multiple + "/";

                for (File pack : packs) {

                    boolean incompatible = pack.getName().startsWith("[INCOMPATIBLE] ");

                    if (incompatible == true) {

                        pack = new File(pack.getParent() + "/" + pack.getName().replace("[INCOMPATIBLE] ", ""));

                    }

                    pack_name = pack.getName();

                    if (pack.getName().endsWith(".zip") == true) {

                        pack = new File(path_config + "/#dev/temporary/pack_zip/" + pack.getName().replace(".zip", ""));

                    }

                    inside = pack.listFiles();

                    if (inside != null) {

                        String path_pack = pack.getPath();

                        for (File file : inside) {

                            if (file.isDirectory() == true) {

                                if (file.getName().equals("replace") == true) {

                                    // Replacement
                                    {

                                        if (incompatible == false) {

                                            {

                                                try {

                                                    Files.walk(file.toPath()).forEach(source -> {

                                                        {

                                                            if (source.toFile().isDirectory() == false) {

                                                                String replace_to = Path.of(path_config + "/#dev/temporary").resolve(file.toPath().relativize(source)).toString();

                                                                if (source.toString().endsWith(".txt") == true) {

                                                                    // Replace TXT with Mode
                                                                    {

                                                                        String[] data_old = FileManager.readTXT(replace_to);
                                                                        String[] data_new = FileManager.readTXT(source.toString());
                                                                        String[] data = data_new;
                                                                        boolean specific = false;

                                                                        // Get Mode
                                                                        {

                                                                            for (String read_all : data_new) {

                                                                                if (read_all.equals("# SPECIFIC") == true) {

                                                                                    specific = true;

                                                                                }

                                                                                break;

                                                                            }

                                                                        }

                                                                        if (specific == true) {

                                                                            data = data_old;
                                                                            int line = 0;
                                                                            String name = "";

                                                                            for (String read_all : data_new) {

                                                                                if (read_all.equals("") == false) {

                                                                                    if (read_all.contains(" = ") == true) {

                                                                                        name = read_all.substring(0, read_all.indexOf(" = ") + 3);

                                                                                        for (String read_all_old : data) {

                                                                                            if (read_all_old.startsWith(name) == true) {

                                                                                                data[line] = read_all;
                                                                                                break;

                                                                                            }

                                                                                            line = line + 1;

                                                                                        }

                                                                                    }

                                                                                }

                                                                            }

                                                                        }

                                                                        FileManager.writeTXT(replace_to, String.join("\n", data), false);

                                                                    }

                                                                } else {

                                                                    FileManager.copy(source.toString(), replace_to, false);

                                                                }

                                                            }

                                                        }

                                                    });

                                                } catch (Exception exception) {

                                                    OutsideUtils.exception(new Exception(), exception);

                                                }

                                            }

                                        }

                                    }

                                } else if (pack_separation_single.contains("/" + file.getName() + "/") == true) {

                                    // Single Separation
                                    {

                                        if (incompatible == false) {

                                            FileManager.copy(file.getPath(), path_config + "/#dev/temporary/" + file.getName(), true);

                                        }

                                    }

                                } else if (pack_separation_multiple.contains("/" + file.getName() + "/") == true) {

                                    // Multiple Separation
                                    {

                                        Path path_to = Path.of(path_config + "/#dev/temporary/" + file.getName() + "/" + cache_pack_id.get(pack_name));

                                        {

                                            try {

                                                Files.walk(file.toPath()).forEach(source -> {

                                                    if (source.toFile().isDirectory() == false) {

                                                        Path path_copy_to = path_to.resolve(Path.of(path_pack).resolve(file.getName()).relativize(source));

                                                        if (incompatible == true) {

                                                            path_copy_to = path_copy_to.getParent().resolve("[INCOMPATIBLE] " + path_copy_to.toFile().getName());

                                                        }

                                                        FileManager.copy(source.toString(), path_copy_to.toString(), false);

                                                    }

                                                });

                                            } catch (Exception exception) {

                                                OutsideUtils.exception(new Exception(), exception);

                                            }

                                        }

                                    }

                                }

                            }

                        }

                    }

                }

            }

        }

        FileManager.delete(path_config + "/#dev/temporary/pack_zip");
        testSettings(path_config);
        testWorldGen(path_config);

        sendErrorMessage(level_server, "pack");
        sendErrorMessage(level_server, "file");

        cache_pack_id.clear();

    }

    private static void addError (String type, String error, String path, String troublemaker) {

        cache_error.computeIfAbsent(type, test -> new HashMap<>()).computeIfAbsent(error, test -> new ArrayList<>()).add(troublemaker);
        File file = new File(path);

        if (file.getName().startsWith("[INCOMPATIBLE] ") == false) {

            file.renameTo(new File(file.getParentFile().toPath() + "/[INCOMPATIBLE] " + file.getName()));

        }

    }

    public static void sendErrorMessage (ServerLevel level_server, String type) {

        if (cache_error.containsKey(type) == true) {

            String message = "";
            String[] split = new String[0];

            for (Map.Entry<String, List<String>> entry : cache_error.get(type).entrySet()) {

                split = entry.getKey().split(" / ");
                message = "Detected incompatible " + split[0] + ". Caused by " + split[1] + ".";

                if (level_server != null) {

                    GameUtils.misc.sendChatMessage(level_server, "@a", "red", "THT : " + message);

                } else {

                    TanshugetreesMod.LOGGER.error(message);

                }

                for (String get : entry.getValue()) {

                    if (level_server != null) {

                        GameUtils.misc.sendChatMessage(level_server, "@a", "dark_gray", "THT : " + get);

                    } else {

                        TanshugetreesMod.LOGGER.error("- " + get);

                    }

                }

            }

        }

    }

    public static void testInfo (String path_config) {

        File[] packs = new File(path_config + "/custom_packs").listFiles();

        if (packs != null) {

            File file = null;
            String pack_id = "";

            // First Test and Pack ID
            {

                for (File pack : packs) {

                    if (pack.getName().endsWith(".zip") == true) {

                        file = new File(path_config + "/#dev/temporary/pack_zip/" + pack.getName().replace(".zip", "") + "/info.txt");

                    } else {

                        file = new File(pack.getPath() + "/info.txt");

                    }

                    if (file.exists() == true && file.isDirectory() == false) {

                        // Get Pack ID
                        {

                            for (String read_all : FileManager.readTXT(file.getPath())) {

                                if (read_all.startsWith("pack_id = ") == true) {

                                    pack_id = read_all.substring("pack_id = ".length());
                                    break;

                                }

                            }

                        }

                        // Test Pack ID
                        {

                            if (pack_id.equals("") == true) {

                                addError("pack", "pack / pack ID not found", pack.getPath(), pack.getName());
                                return;

                            }

                            if (cache_pack_id.containsValue(pack_id) == true) {

                                addError("pack", "pack / duplicated pack ID", pack.getPath(), pack.getName() + " > " + pack_id);
                                continue;

                            }

                        }

                        cache_pack_id.put(pack.getName(), pack_id);

                    } else {

                        addError("pack", "pack / info file not found", pack.getPath(), pack.getName());
                        return;

                    }

                }

            }

            int data_structure_version = 0;
            String required_packs = "";
            String required_mods = "";

            // Other Test
            {

                for (File pack : packs) {

                    if (pack.getName().endsWith(".zip") == true) {

                        file = new File(path_config + "/#dev/temporary/pack_zip/" + pack.getName().replace(".zip", "") + "/info.txt");

                    } else {

                        file = new File(pack.getPath() + "/info.txt");

                    }

                    // Get Other Data
                    {

                        for (String read_all : FileManager.readTXT(file.getPath())) {

                            if (read_all.startsWith("data_structure_version = ") == true) {

                                data_structure_version = Integer.parseInt(read_all.substring("data_structure_version = ".length()));

                            } else if (read_all.startsWith("required_packs = ")) {

                                required_packs = read_all.substring("required_packs = ".length());

                            } else if (read_all.startsWith("required_mods = ")) {

                                required_mods = read_all.substring("required_mods = ".length());

                            }

                        }

                    }

                    test:
                    {

                        // Test Mod Version
                        {

                            if (data_structure_version != Handcode.data_structure_version) {

                                addError("pack", "pack / unsupported mod version", pack.getPath(), pack.getName());
                                break test;

                            }

                        }

                        // Test Required Packs
                        {

                            if (required_packs.equals("") == false && required_packs.equals("none") == false) {

                                for (String value : required_packs.split(", ")) {

                                    if (cache_pack_id.containsValue(value) == false) {

                                        addError("pack", "pack / required pack not found", pack.getPath(), pack.getName() + " > " + value);
                                        break test;

                                    }

                                }

                            }

                        }

                        // Test Required Mods
                        {

                            if (required_mods.equals("") == false && required_mods.equals("none") == false) {

                                for (String value : required_mods.split(", ")) {

                                    if (GameUtils.misc.isModLoaded(value) == false) {

                                        addError("pack", "pack / required mod not found", pack.getPath(), pack.getName() + " > " + value);
                                        break test;

                                    }

                                }

                            }

                        }

                    }

                }

            }

        }

    }

    private static void testSettings (String path_config) {

        File file = new File(path_config + "/#dev/temporary/presets");

        if (file.exists() == true && file.isDirectory() == true) {

            try {

                Files.walk(file.toPath()).forEach(source -> {

                    File file_each = source.toFile();

                    if (file_each.getName().startsWith("[INCOMPATIBLE] ") == false && file_each.getName().endsWith("_settings.txt") == true) {

                        String name = Path.of(path_config + "/#dev/temporary/presets").relativize(file_each.toPath()).toString().replace("\\", "/");
                        String value = "";

                        for (String read_all : FileManager.readTXT(file_each.getPath())) {

                            if (read_all.contains(" = ") == true) {

                                if (read_all.startsWith("Block ") == true) {

                                    {

                                        value = read_all.substring(read_all.indexOf(" = ") + 3);

                                        if (value.equals("") == false) {

                                            if (GameUtils.block.fromText(value.replace(" keep", "")).getBlock() == Blocks.AIR) {

                                                addError("file", "settings file / unknown block ID", file_each.getPath(), name + " > " + value);
                                                break;

                                            }

                                        }

                                    }

                                } else if (read_all.startsWith("Function ") == true) {

                                    {

                                        value = read_all.substring(read_all.indexOf(" = ") + 3);

                                        if (value.equals("") == false) {

                                            if (new File(path_config + "/#dev/temporary/functions/" + value + ".txt").exists() == false) {

                                                addError("file", "settings file / unknown function", file_each.getPath(), name + " > " + value);
                                                break;

                                            }

                                        }

                                    }

                                }

                            }

                        }

                    }

                });

            } catch (Exception exception) {

                OutsideUtils.exception(new Exception(), exception);

            }

        }

    }

    private static void testWorldGen (String path_config) {

        File file = new File(path_config + "/#dev/temporary/world_gen");

        if (file.exists() == true && file.isDirectory() == true) {

            try {

                Files.walk(Path.of(path_config + "/#dev/temporary/world_gen")).forEach(source -> {

                    File file_each = source.toFile();

                    if (file_each.getName().startsWith("[INCOMPATIBLE] ") == false && file_each.getName().endsWith(".txt") == true) {

                        String name = Path.of(path_config + "/#dev/temporary/world_gen").relativize(file_each.toPath()).toString().replace("\\", "/");
                        File file_test = null;
                        String value = "";

                        for (String read_all : FileManager.readTXT(file_each.getPath())) {

                            if (read_all.contains(" = ") == true) {

                                if (read_all.startsWith("path_storage = ") == true) {

                                    {

                                        value = read_all.substring("path_storage = ".length());
                                        file_test = new File(path_config + "/#dev/temporary/presets/" + value + "/storage");

                                        if (file_test.exists() == true && file_test.isDirectory() == true) {

                                            if (file_test.listFiles() == null) {

                                                addError("file", "world gen file / empty storage", file_each.getPath(), name + " > " + value);
                                                break;

                                            }

                                        } else {

                                            addError("file", "world gen file / storage not found", file_each.getPath(), name + " > " + value);
                                            break;

                                        }

                                    }

                                } else if (read_all.startsWith("path_settings = ") == true) {

                                    {

                                        value = read_all.substring("path_settings = ".length());

                                        if (new File(path_config + "/#dev/temporary/presets/" + value + "_settings.txt").exists() == false) {

                                            addError("file", "world gen file / settings not found", file_each.getPath(), name + " > " + value);
                                            break;

                                        }

                                    }

                                }

                            }

                        }

                    }

                });

            } catch (Exception exception) {

                OutsideUtils.exception(new Exception(), exception);

            }

        }

    }

}
