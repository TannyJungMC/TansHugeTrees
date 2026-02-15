package tannyjung.tanshugetrees_core.outside;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Blocks;
import tannyjung.tanshugetrees_core.Core;
import tannyjung.tanshugetrees_core.game.GameUtils;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class CustomPackOrganizing {

    private static final Map<String, Map<String, List<String>>> cache_errors = new HashMap<>();
    private static final Map<String, String> cache_pack_ids = new HashMap<>();

    public static Map<String, Map<String, List<String>>> start (String pack_separation_single, String pack_separation_multiple) {

        FileManager.delete(Core.path_config + "/#dev/#temporary");
        FileManager.createEmptyFile(Core.path_config + "/#dev/#temporary", true);
        FileManager.createEmptyFile(Core.path_config + "/custom_packs", true);

        // Rename All Packs Back
        {

            File[] packs = new File(Core.path_config + "/custom_packs").listFiles();

            if (packs != null) {

                for (File pack : packs) {

                    pack.renameTo(new File(pack.getParentFile().toPath() + "/" + pack.getName().replace("[INCOMPATIBLE] ", "")));

                }

            }

        }

        // Extract ZIP
        {

            File[] packs = new File(Core.path_config + "/custom_packs").listFiles();

            if (packs != null) {

                for (File pack : packs) {

                    if (pack.getName().endsWith(".zip") == true) {

                        FileManager.extractZIP(pack.getPath(), Core.path_config + "/#dev/#temporary/pack_zip/" + pack.getName().replace(".zip", ""), false, "");

                    }

                }

            }

        }

        // Organizing Info
        {

            File[] packs = new File(Core.path_config + "/custom_packs").listFiles();

            if (packs != null) {

                File file = null;

                for (File pack : packs) {

                    if (pack.getName().endsWith(".zip") == true) {

                        file = new File(Core.path_config + "/#dev/#temporary/pack_zip/" + pack.getName().replace(".zip", "") + "/info.txt");

                    } else {

                        file = new File(pack.getPath() + "/info.txt");

                    }

                    if (file.exists() == true && file.isDirectory() == false) {

                        FileManager.copy(file.getPath(), Core.path_config + "/#dev/#temporary/info/" + pack.getName() + ".txt", false);

                    }

                }

            }

        }

        getPackID();
        testInfo();
        pack_separation_single = "/" + pack_separation_single + "/";
        pack_separation_multiple = "/" + pack_separation_multiple + "/";

        // Organizing Data
        {

            File[] packs = new File(Core.path_config + "/custom_packs").listFiles();

            if (packs != null) {

                File tanny_pack = TannyPackManager.getCurrentFile();

                if (tanny_pack.exists() == true) {

                    organizingData(tanny_pack, pack_separation_single, pack_separation_multiple);

                }

                for (File pack : packs) {

                    if (pack != tanny_pack) {

                        organizingData(pack, pack_separation_single, pack_separation_multiple);

                    }

                }

            }

        }

        // Organizing Replacement
        {

            File[] packs = new File(Core.path_config + "/custom_packs").listFiles();

            if (packs != null) {

                for (File pack : packs) {

                    if (pack.getName().startsWith("[INCOMPATIBLE] ") == false) {

                        if (pack.getName().endsWith(".zip") == true) {

                            pack = new File(Core.path_config + "/#dev/#temporary/pack_zip/" + pack.getName().replace(".zip", ""));

                        }

                        File file = new File(pack.getPath() + "/replace");

                        if (file.exists() == true && file.isDirectory() == true) {

                            {

                                try {

                                    Files.walk(file.toPath()).forEach(source -> {

                                        {

                                            if (source.toFile().isDirectory() == false) {

                                                String replace_to = Path.of(Core.path_config + "/#dev/#temporary").resolve(file.toPath().relativize(source)).toString();

                                                if (source.toString().endsWith(".txt") == true) {

                                                    // Replace TXT with Mode
                                                    {

                                                        String[] data_old = FileManager.readTXT(replace_to);
                                                        String[] data_new = FileManager.readTXT(source.toString());
                                                        String[] data = new String[0];
                                                        boolean specific = false;

                                                        // Get Mode
                                                        {

                                                            for (String read_all : data_new) {

                                                                if (read_all.equals("# SPECIFIC") == true) {

                                                                    specific = true;
                                                                    break;

                                                                }

                                                            }

                                                        }

                                                        if (specific == false) {

                                                            data = data_new;

                                                        } else {

                                                            data = data_old;
                                                            int line = 0;
                                                            String name = "";

                                                            for (String read_all : data_new) {

                                                                if (read_all.isEmpty() == false) {

                                                                    if (read_all.contains(" = ") == true) {

                                                                        name = read_all.substring(0, read_all.indexOf(" = ") + 3);
                                                                        line = 0;

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

                                    OutsideUtils.exception(new Exception(), exception, "");

                                }

                            }

                        }

                    }

                }

            }

        }

        // Organizing Dev
        {

            File[] packs = new File(Core.path_config + "/custom_packs").listFiles();

            if (packs != null) {

                for (File pack : packs) {

                    if (pack.getName().endsWith(".zip") == true) {

                        pack = new File(Core.path_config + "/#dev/#temporary/pack_zip/" + pack.getName().replace(".zip", ""));

                    }

                    File file = new File(pack.getPath() + "/#dev");

                    if (file.exists() == true && file.isDirectory() == true) {

                        {

                            try {

                                Files.walk(file.toPath()).forEach(source -> {

                                    {

                                        if (source.toFile().isDirectory() == false) {

                                            String replace_to = Path.of(Core.path_config + "/#dev/#temporary").resolve(file.toPath().relativize(source)).toString();
                                            FileManager.copy(source.toString(), replace_to, false);

                                        }

                                    }

                                });

                            } catch (Exception exception) {

                                OutsideUtils.exception(new Exception(), exception, "");

                            }

                        }

                    }

                }

            }

        }

        FileManager.delete(Core.path_config + "/#dev/#temporary/pack_zip");
        testSettings();
        testWorldGen();

        Map<String, Map<String, List<String>>> errors = new HashMap<>(cache_errors);
        cache_errors.clear();
        cache_pack_ids.clear();
        return errors;

    }

    private static void getPackID () {

        File[] files = new File(Core.path_config + "/#dev/#temporary/info").listFiles();

        if (files != null) {

            for (File file : files) {

                if (file.exists() == true && file.isDirectory() == false) {

                    for (String read_all : FileManager.readTXT(file.getPath())) {

                        if (read_all.startsWith("pack_id = ") == true) {

                            cache_pack_ids.put(file.getName().substring(0, file.getName().length() - ".txt".length()), read_all.substring("pack_id = ".length()));
                            break;

                        }

                    }

                }

            }

        }

    }

    public static void testInfo () {

        File[] packs = new File(Core.path_config + "/custom_packs").listFiles();

        if (packs != null) {

            File file = null;
            String pack_id = "";
            List<String> pack_id_duplicated_test = new ArrayList<>();
            String data_structure_version = "";
            String required_packs = "";
            String required_mods = "";

            for (File pack : packs) {

                file = new File(Core.path_config + "/#dev/#temporary/info/" + pack.getName() + ".txt");

                if (file.exists() == true && file.isDirectory() == false) {

                    // Get Data
                    {

                        for (String read_all : FileManager.readTXT(file.getPath())) {

                            if (read_all.startsWith("data_structure_version = ") == true) {

                                data_structure_version = read_all.substring("data_structure_version = ".length());

                            } else if (read_all.startsWith("required_packs = ")) {

                                required_packs = read_all.substring("required_packs = ".length());

                            } else if (read_all.startsWith("required_mods = ")) {

                                required_mods = read_all.substring("required_mods = ".length());

                            }

                        }

                    }

                    pack_id = cache_pack_ids.get(pack.getName());

                    test:
                    {

                        // Pack ID
                        {

                            if (pack_id == null) {

                                FileManager.rename(file.getPath(), "/[INCOMPATIBLE] " + file.getName());
                                addError("pack", "pack / pack ID not found", pack.getPath(), pack.getName());
                                break test;

                            }

                        }

                        // Data Structure Version
                        {

                            if (Core.data_structure_version_pack.equals(data_structure_version) == false) {

                                FileManager.rename(file.getPath(), "/[INCOMPATIBLE] " + file.getName());
                                addError("pack", "pack / unsupported mod version", pack.getPath(), pack.getName());
                                break test;

                            }

                        }

                        // Duplicated Pack ID
                        {

                            if (pack_id_duplicated_test.contains(pack_id) == true) {

                                FileManager.rename(file.getPath(), "/[INCOMPATIBLE] " + file.getName());
                                addError("pack", "pack / duplicated pack ID", pack.getPath(), pack.getName() + " > " + pack_id);

                                while (cache_pack_ids.containsValue(pack_id) == true) {

                                    pack_id = pack_id + "X";

                                }

                                cache_pack_ids.put(pack.getName(), pack_id);
                                break test;

                            }

                        }

                        // Required Packs
                        {

                            if (required_packs.isEmpty() == false && required_packs.equals("none") == false) {

                                for (String value : required_packs.split(" / ")) {

                                    if (cache_pack_ids.containsValue(value) == false) {

                                        FileManager.rename(file.getPath(), "/[INCOMPATIBLE] " + file.getName());
                                        addError("pack", "pack / required pack not found", pack.getPath(), pack.getName() + " > " + value);
                                        break test;

                                    }

                                }

                            }

                        }

                        // Required Mods
                        {

                            if (required_mods.isEmpty() == false && required_mods.equals("none") == false) {

                                for (String value : required_mods.split(" / ")) {

                                    if (GameUtils.misc.isModLoaded(value) == false) {

                                        FileManager.rename(file.getPath(), "/[INCOMPATIBLE] " + file.getName());
                                        addError("pack", "pack / required mod not found", pack.getPath(), pack.getName() + " > " + value);
                                        break test;

                                    }

                                }

                            }

                        }

                    }

                    pack_id_duplicated_test.add(pack_id);

                } else {

                    FileManager.rename(file.getPath(), "/[INCOMPATIBLE] " + file.getName());
                    addError("pack", "pack / info file not found", pack.getPath(), pack.getName());

                }

            }

        }

    }

    private static void testSettings () {

        File file = new File(Core.path_config + "/#dev/#temporary/presets");

        if (file.exists() == true && file.isDirectory() == true) {

            try {

                Files.walk(file.toPath()).forEach(source -> {

                    File file_each = source.toFile();

                    if (file_each.getName().startsWith("[INCOMPATIBLE] ") == false && file_each.getName().endsWith("_settings.txt") == true) {

                        String name = Path.of(Core.path_config + "/#dev/#temporary/presets").relativize(file_each.toPath()).toString().replace("\\", "/");
                        String value = "";

                        for (String read_all : FileManager.readTXT(file_each.getPath())) {

                            if (read_all.contains(" = ") == true) {

                                if (read_all.startsWith("Block ") == true) {

                                    {

                                        value = read_all.substring(read_all.indexOf(" = ") + 3);

                                        if (value.isEmpty() == false) {

                                            if (GameUtils.block.fromText(value.replace(" keep", "")).getBlock() == Blocks.AIR) {

                                                addError("file", "settings file / unknown block ID", file_each.getPath(), name + " > " + value);
                                                break;

                                            }

                                        }

                                    }

                                } else if (read_all.startsWith("Function ") == true) {

                                    {

                                        value = read_all.substring(read_all.indexOf(" = ") + 3);

                                        if (value.isEmpty() == false) {

                                            if (new File(Core.path_config + "/#dev/#temporary/functions/" + value + ".txt").exists() == false) {

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

                OutsideUtils.exception(new Exception(), exception, "");

            }

        }

    }

    private static void testWorldGen () {

        File file = new File(Core.path_config + "/#dev/#temporary/world_gen");

        if (file.exists() == true && file.isDirectory() == true) {

            try {

                Files.walk(Path.of(Core.path_config + "/#dev/#temporary/world_gen")).forEach(source -> {

                    File file_each = source.toFile();

                    if (file_each.getName().startsWith("[INCOMPATIBLE] ") == false && file_each.getName().endsWith(".txt") == true) {

                        String name = Path.of(Core.path_config + "/#dev/#temporary/world_gen").relativize(file_each.toPath()).toString().replace("\\", "/");
                        File file_test = null;

                        for (String read_all : FileManager.readTXT(file_each.getPath())) {

                            if (read_all.contains(" = ") == true) {

                                if (read_all.startsWith("path_storage = ") == true) {

                                    {

                                        file_test = new File(Core.path_config + "/#dev/#temporary/presets/" + read_all.substring("path_storage = ".length()) + "/storage");

                                        if (file_test.exists() == true && file_test.isDirectory() == true) {

                                            if (file_test.listFiles() == null) {

                                                addError("file", "world gen file / empty storage", file_each.getPath(), name);
                                                break;

                                            }

                                        } else {

                                            addError("file", "world gen file / storage not found", file_each.getPath(), name);
                                            break;

                                        }

                                    }

                                } else if (read_all.startsWith("path_settings = ") == true) {

                                    {

                                        file_test = new File(Core.path_config + "/#dev/#temporary/presets/" + read_all.substring("path_settings = ".length()) + "_settings.txt");

                                        if (file_test.exists() == false) {

                                            addError("file", "world gen file / settings not found", file_each.getPath(), name);
                                            break;

                                        }

                                    }

                                }

                            }

                        }

                    }

                });

            } catch (Exception exception) {

                OutsideUtils.exception(new Exception(), exception, "");

            }

        }

    }

    private static void organizingData (File pack, String pack_separation_single, String pack_separation_multiple) {

        boolean incompatible = pack.getName().startsWith("[INCOMPATIBLE] ");

        if (incompatible == true) {

            pack = new File(pack.getParent() + "/" + pack.getName().replace("[INCOMPATIBLE] ", ""));

        }

        String pack_name = pack.getName();

        if (pack.getName().endsWith(".zip") == true) {

            pack = new File(Core.path_config + "/#dev/#temporary/pack_zip/" + pack.getName().replace(".zip", ""));

        }

        File[] inside = pack.listFiles();

        if (inside != null) {

            String path_pack = pack.getPath();

            for (File file : inside) {

                if (pack_separation_single.contains("/" + file.getName() + "/") == true) {

                    // Single Separation
                    {

                        if (incompatible == false) {

                            FileManager.copy(file.getPath(), Core.path_config + "/#dev/#temporary/" + file.getName(), true);

                        }

                    }

                } else if (pack_separation_multiple.contains("/" + file.getName() + "/") == true) {

                    // Multiple Separation
                    {

                        Path path_to = Path.of(Core.path_config + "/#dev/#temporary/" + file.getName() + "/" + cache_pack_ids.get(pack_name));

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

                                OutsideUtils.exception(new Exception(), exception, "");

                            }

                        }

                    }

                }

            }

        }

    }

    private static void addError (String type, String error, String path, String troublemaker) {

        cache_errors.computeIfAbsent(type, test -> new HashMap<>()).computeIfAbsent(error, test -> new ArrayList<>()).add(troublemaker);
        File file = new File(path);

        if (file.getName().startsWith("[INCOMPATIBLE] ") == false) {

            file.renameTo(new File(file.getParentFile().toPath() + "/[INCOMPATIBLE] " + file.getName()));

        }

    }

    public static void sendErrorMessage (ServerLevel level_server, Map<String, Map<String, List<String>>> errors, String type) {

        if (errors.containsKey(type) == true) {

            String message = "";
            String[] split = new String[0];

            for (Map.Entry<String, List<String>> entry : errors.get(type).entrySet()) {

                split = entry.getKey().split(" / ");
                message = "Detected incompatible " + split[0] + ". Caused by " + split[1] + ".";

                if (level_server != null) {

                    GameUtils.misc.sendChatMessage(level_server, "@a", message + " / red");

                } else {

                    Core.logger.error(message);

                }

                for (String get : entry.getValue()) {

                    if (level_server != null) {

                        GameUtils.misc.sendChatMessage(level_server, "@a", get + " / dark_gray");

                    } else {

                        Core.logger.error("- {}", get);

                    }

                }

            }

        }

    }

}
