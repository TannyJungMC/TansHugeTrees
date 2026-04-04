package tannyjung.tanshugetrees_core.outside;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Blocks;
import tannyjung.tanshugetrees_core.Core;
import tannyjung.tanshugetrees_core.game.GameUtils;
import tannyjung.tanshugetrees_handcode.Handcode;

import java.io.File;
import java.nio.file.Path;
import java.util.*;

public class CustomPackOrganizing {

    private static final Map<String, String> cache_pack_ids = new HashMap<>();
    private static final Map<String, Map<String, List<String>>> cache_errors = new HashMap<>();

    public static void start (String pack_separate_multiple, String folder_settings, String folder_functions) {

        cache_errors.clear();

        FileManager.delete(Core.path_config + "/dev/temporary");
        FileManager.createEmptyFile(Core.path_config + "/dev/temporary", true);
        FileManager.createEmptyFile(Core.path_config + "/custom_packs", true);

        // Rename All Back
        {

            File[] packs = new File(Core.path_config + "/custom_packs").listFiles();

            if (packs != null) {

                for (File pack : packs) {

                    FileManager.rename(pack.getPath(), pack.getName().replace("[INCOMPATIBLE] ", ""));

                }

            }

        }

        File[] packs = new File(Core.path_config + "/custom_packs").listFiles();

        if (packs != null) {

            // Extract ZIP
            {

                for (File pack : packs) {

                    if (pack.getName().endsWith(".zip") == true) {

                        FileManager.extractZIP(pack.getPath(), Core.path_config + "/dev/temporary/pack_zip/" + pack.getName(), false, "");

                    }

                }

            }

            // Organize Info
            {

                File file = null;

                for (File pack : packs) {

                    if (pack.getName().endsWith(".zip") == true) {

                        file = new File(Core.path_config + "/dev/temporary/pack_zip/" + pack.getName() + "/info.txt");

                    } else {

                        file = new File(pack.getPath() + "/info.txt");

                    }

                    if (file.exists() == true) {

                        FileManager.copy(file.getPath(), Core.path_config + "/dev/temporary/info/" + pack.getName() + ".txt", false);

                    }

                }

            }

            getPackID();
            testInfo();
            pack_separate_multiple = " / " + pack_separate_multiple + " / ";

            // Organizing Data
            {

                File tanny_pack = TannyPackManager.getCurrentFile();

                if (tanny_pack.exists() == true) {

                    organize(tanny_pack, pack_separate_multiple);

                }

                for (File pack : packs) {

                    if (pack.getName().equals(tanny_pack.getName()) == false) {

                        organize(pack, pack_separate_multiple);

                    }

                }

            }

            // Edit
            {

                File file = new File(Core.path_config + "/dev/temporary/edit");

                if (file.exists() == true) {

                    for (File scan : FileManager.getAllFiles(file.getPath())) {

                        {

                            Path path_to = file.toPath().relativize(scan.toPath());
                            path_to = Path.of(Core.path_config + "/dev/temporary").resolve(path_to);

                            if (path_to.toFile().exists() == true) {

                                // Edit
                                {

                                    if (scan.getName().endsWith(".txt") == true) {

                                        FileManager.mergeTXT(scan, path_to.toFile());

                                    } else {

                                        FileManager.copy(scan.getPath(), path_to.toString(), false);

                                    }

                                }

                            }

                        }

                    }

                }

            }

        }

        test(folder_settings, false);
        test(folder_functions, true);
        FileManager.delete(Core.path_config + "/dev/temporary/pack_zip");
        cache_pack_ids.clear();

    }

    private static void getPackID () {

        File[] files = new File(Core.path_config + "/dev/temporary/info").listFiles();

        if (files != null) {

            for (File file : files) {

                if (file.exists() == true) {

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
            String data_structure_version = "";
            String required_packs = "";
            String required_mods = "";
            List<String> pack_id_scan = new ArrayList<>();

            for (File pack : packs) {

                file = new File(Core.path_config + "/dev/temporary/info/" + pack.getName() + ".txt");

                if (file.exists() == true) {

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

                    test:
                    {

                        // Pack ID
                        {

                            if (cache_pack_ids.containsKey(pack.getName()) == false) {

                                FileManager.rename(file.getPath(), "/[INCOMPATIBLE] " + file.getName());
                                addError("pack", "packs / pack ID not found. This will results skipping these packs. Make sure you're using the version that includes pack ID.", pack.getPath(), pack.getName());
                                break test;

                            }

                        }

                        // Data Structure Version
                        {

                            if (Core.data_structure_version_pack.equals(data_structure_version) == false) {

                                FileManager.rename(file.getPath(), "/[INCOMPATIBLE] " + file.getName());
                                addError("pack", "packs / unsupported data structure version. This will results skipping these packs. Your version is " + Core.data_structure_version_pack + " but these packs require a different version.", pack.getPath(), pack.getName() + " > " + data_structure_version);
                                break test;

                            }

                        }

                        // Duplicated Pack ID
                        {

                            pack_id_scan.clear();

                            for (String id : cache_pack_ids.values()) {

                                if (pack_id_scan.contains(id) == false) {

                                    pack_id_scan.add(id);

                                } else {

                                    FileManager.rename(file.getPath(), "/[INCOMPATIBLE] " + file.getName());
                                    addError("pack", "packs / duplicated pack IDs. This will results skipping these packs. You can report this to the pack authors to help them fix it.", pack.getPath(), pack.getName() + " > " + id);
                                    break test;

                                }

                            }

                        }

                        // Required Packs
                        {

                            if (required_packs.equals("none") == false) {

                                for (String value : required_packs.split(" / ")) {

                                    if (cache_pack_ids.containsValue(value) == false) {

                                        FileManager.rename(file.getPath(), "/[INCOMPATIBLE] " + file.getName());
                                        addError("pack", "packs / required packs not found. This will results skipping these packs. Make sure you're using required packs to allow these packs to work.", pack.getPath(), pack.getName() + " > " + value);
                                        break test;

                                    }

                                }

                            }

                        }

                        // Required Mods
                        {

                            if (required_mods.equals("none") == false) {

                                for (String value : required_mods.split(" / ")) {

                                    if (GameUtils.Misc.isModLoaded(value) == false) {

                                        FileManager.rename(file.getPath(), "/[INCOMPATIBLE] " + file.getName());
                                        addError("pack", "packs / required mods not found. This will results skipping these packs. Make sure you're using required mods to allow these packs to work.", pack.getPath(), pack.getName() + " > " + value);
                                        break test;

                                    }

                                }

                            }

                        }

                    }

                } else {

                    FileManager.rename(file.getPath(), "/[INCOMPATIBLE] " + file.getName());
                    addError("pack", "packs / info file not found. This will results skipping these packs. Make sure you're using the version that includes info file.", pack.getPath(), pack.getName());

                }

            }

        }

    }

    private static void test (String folders, boolean is_function) {

        for (String folder : folders.split(" / ")) {

            String suffix = "";

            if (folder.contains(" + ") == true) {

                String[] split = folder.split(" + ");
                folder = split[0];
                suffix = split[1];

            }

            File file = new File(Core.path_config + "/dev/temporary/" + folder);

            if (file.exists() == true) {

                for (File scan : FileManager.getAllFiles(file.getPath())) {

                    {

                        if (scan.getName().endsWith(".txt") == true) {

                            if (scan.getName().startsWith("[INCOMPATIBLE] ") == false && scan.getName().endsWith(suffix) == true) {

                                testFile(scan.getPath(), is_function);

                            }

                        }

                    }

                }

            }

        }

    }

    private static boolean testFile (String path, boolean is_function) {

        File file = new File(path);
        String id = Path.of(Core.path_config + "/dev/temporary/").relativize(file.toPath()).toString().replace("\\", "/");
        String[] split = new String[0];
        File file_test = null;

        for (String read_all : FileManager.readTXT(file.getPath())) {

            if (read_all.contains(" = ") == true) {

                split = read_all.split(" = ");

                if (split.length == 2 && split[1].isEmpty() == false) {

                    if (is_function == false) {

                        {

                            if (split[0].equals("path_storage = ") == true) {

                                {

                                    file_test = new File(Core.path_config + "/dev/temporary/presets/" + read_all.substring("path_storage = ".length()) + "/storage");

                                    if (file_test.exists() == true) {

                                        if (file_test.listFiles() == null) {

                                            addError("file", "world gen files / empty storage. This will results skipping them in mod systems.", file.getPath(), id);
                                            return false;

                                        }

                                    } else {

                                        addError("file", "world gen files / storage not found. This will results skipping them in mod systems.", file.getPath(), id);
                                        return false;

                                    }

                                }

                            } else if (split[0].equals("path_settings = ") == true) {

                                {

                                    file_test = new File(Core.path_config + "/dev/temporary/presets/" + read_all.substring("path_settings = ".length()) + ".txt");

                                    if (file_test.exists() == false) {

                                        addError("file", "world gen files / settings not found. This will results skipping them in mod systems.", file.getPath(), id);
                                        return false;

                                    } else {

                                        if (testFile(file_test.getPath(), false) == false) {

                                            addError("file", "world gen files / settings is mark as incompatible. This will results skipping them in mod systems.", file.getPath(), id);
                                            return false;

                                        }

                                    }

                                }

                            } else if (split[0].startsWith("Block ") == true) {

                                {

                                    split[1] = split[1].replace(" keep", "");

                                    if (GameUtils.Tile.fromText(split[1]).getBlock() == Blocks.AIR) {

                                        addError("file", "settings file / unknown block IDs. This will results skipping them in mod systems.", file.getPath(), id + " > " + split[1]);
                                        return false;

                                    }

                                }

                            } else if (split[0].startsWith("Function ") == true) {

                                {

                                    file_test = new File(Core.path_config + "/dev/temporary/functions/" + split[1] + ".txt");


                                    if (file_test.exists() == false) {

                                        addError("file", "settings file / unknown functions. This will results skipping them in mod systems.", file.getPath(), id + " > " + split[1]);
                                        return false;

                                    } else {

                                        if (testFile(file_test.getPath(), true) == false) {

                                            addError("file", "settings files / functions is mark as incompatible. This will results skipping them in mod systems.", file.getPath(), id);
                                            return false;

                                        }

                                    }

                                }

                            }

                        }

                    }

                }

            }

        }

        return true;

    }

    private static void organize (File pack, String pack_separate_multiple) {

        boolean incompatible = pack.getName().startsWith("[INCOMPATIBLE] ") == true;

        // Get Real Pack Path
        {

            if (pack.getName().endsWith(".zip") == true) {

                pack = new File(Core.path_config + "/dev/temporary/pack_zip/" + pack.getName());

            }

        }

        File[] files = pack.listFiles();

        if (files != null) {

            boolean is_separate_multiple = false;

            for (File file : files) {

                if (file.isDirectory() == true) {

                    is_separate_multiple = pack_separate_multiple.contains(" / " + file.getName() + " / ") == true;
                    boolean is_separate_multiple_final = is_separate_multiple;

                    for (File scan : FileManager.getAllFiles(file.getPath())) {

                        {

                            Path path_copy_to = Path.of(Core.path_config + "/dev/temporary/" + file.getName());

                            // Convert Path
                            {

                                if (is_separate_multiple_final == true) {

                                    path_copy_to = path_copy_to.resolve(cache_pack_ids.get(pack.getName()));

                                }

                                path_copy_to = path_copy_to.resolve(pack.toPath().resolve(file.getName()).relativize(scan.toPath()));

                                if (incompatible == true) {

                                    path_copy_to = path_copy_to.getParent().resolve("[INCOMPATIBLE] " + path_copy_to.toFile().getName());

                                }

                            }

                            FileManager.copy(scan.getPath(), path_copy_to.toString(), false);

                        }

                    }

                }

            }

        }

    }

    private static void addError (String type, String error, String path, String troublemaker) {

        cache_errors.computeIfAbsent(type, create -> new HashMap<>()).computeIfAbsent(error, create -> new ArrayList<>()).add(troublemaker);
        File file = new File(path);

        if (file.getName().startsWith("[INCOMPATIBLE] ") == false) {

            FileManager.rename(path, "[INCOMPATIBLE] " + file.getName());

        }

    }

    public static void sendErrorMessage (ServerLevel level_server) {

        boolean to_chat = false;
        String message = "";
        String[] split = new String[0];

        for (Map.Entry<String, Map<String, List<String>>> entry1 : cache_errors.entrySet()) {

            to_chat = entry1.getKey().equals("pack") == true || Handcode.Config.developer_mode == true;

            for (Map.Entry<String, List<String>> entry2 : entry1.getValue().entrySet()) {

                {

                    split = entry2.getKey().split(" / ");
                    message = "Detected incompatible " + split[0] + ", caused by " + split[1];

                    if (level_server != null && to_chat == true) {

                        GameUtils.Misc.sendChatMessage(level_server, message + " / red");

                    } else {

                        Core.logger.error(message);

                    }

                    for (String get : entry2.getValue()) {

                        if (level_server != null && to_chat == true) {

                            GameUtils.Misc.sendChatMessage(level_server, get + " / dark_gray");

                        } else {

                            Core.logger.error("- {}", get);

                        }

                    }

                }

            }

        }



    }

}
