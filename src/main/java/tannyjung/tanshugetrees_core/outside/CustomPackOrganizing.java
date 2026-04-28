package tannyjung.tanshugetrees_core.outside;

import net.minecraft.server.level.ServerLevel;
import tannyjung.tanshugetrees_core.Core;
import tannyjung.tanshugetrees_core.game.GameUtils;
import tannyjung.tanshugetrees_handcode.Handcode;

import java.io.File;
import java.nio.file.Path;
import java.util.*;

public class CustomPackOrganizing {

    private static final Map<String, String> cache_pack_ids = new HashMap<>();
    private static final Set<String> cache_error_files = new HashSet<>();
    private static final Map<String, Map<String, Set<String>>> errors = new HashMap<>();

    public static void start (ServerLevel level_server, String pack_separate_multiple, String folder_settings, String folder_functions) {

        errors.clear();

        FileManager.delete(Core.path_config + "/dev/temporary");
        FileManager.createEmptyFile(Core.path_config + "/dev/temporary", true);
        FileManager.createEmptyFile(Core.path_config + "/custom_packs", true);
        File[] packs = new File(Core.path_config + "/custom_packs").listFiles();

        if (packs == null) {

            return;

        }

        // Rename All Back
        {

            for (File pack : packs) {

                FileManager.rename(pack.getPath(), pack.getName().replace("[INCOMPATIBLE] ", ""));

            }

        }

        packs = new File(Core.path_config + "/custom_packs").listFiles();

        if (packs == null) {

            return;

        }

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

        // Get Pack ID
        {

            File[] files = new File(Core.path_config + "/dev/temporary/info").listFiles();

            if (files == null) {

                return;

            }

            for (File file : files) {

                if (file.exists() == true) {

                    for (String scan : FileManager.readTXT(file.getPath())) {

                        if (scan.startsWith("pack_id = ") == true) {

                            cache_pack_ids.put(file.getName().substring(0, file.getName().length() - ".txt".length()), scan.substring("pack_id = ".length()));
                            break;

                        }

                    }

                }

            }

        }

        testInfo();
        pack_separate_multiple = " / " + pack_separate_multiple + " / ";

        // Organizing Data
        {

            File main_pack = TannyPackManager.getCurrentFile();

            if (main_pack.exists() == true) {

                organize(main_pack, pack_separate_multiple);

            }

            for (File pack : packs) {

                if (pack.getName().equals(main_pack.getName()) == false) {

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

        test(level_server, folder_settings, false);
        test(level_server, folder_functions, true);
        FileManager.delete(Core.path_config + "/dev/temporary/pack_zip");

        // Rename Incompatible Files
        {

            for (String scan : cache_error_files) {

                FileManager.rename(scan, "[INCOMPATIBLE] " + new File(scan).getName());

            }

        }

        cache_pack_ids.clear();
        cache_error_files.clear();

    }

    private static void test (ServerLevel level_server, String folders, boolean is_function) {

        for (String folder : folders.split(" / ")) {

            String suffix = "";

            if (folder.contains(" < ") == true) {

                String[] split = folder.split(" < ");
                folder = split[0];
                suffix = split[1];

            }

            File file = new File(Core.path_config + "/dev/temporary/" + folder);

            if (file.exists() == true) {

                for (File scan : FileManager.getAllFiles(file.getPath())) {

                    {

                        if (scan.getName().startsWith("[INCOMPATIBLE] ") == false && scan.getName().endsWith(suffix + ".txt") == true) {

                            Option.testParse(level_server, scan, is_function);

                        }

                    }

                }

            }

        }

    }

    private static void testInfo () {

        File[] packs = new File(Core.path_config + "/custom_packs").listFiles();

        if (packs == null) {

            return;

        }

        boolean pass = false;
        File file = null;
        String data_structure_version = "";
        String required_packs = "none";
        String required_mods = "none";
        List<String> pack_id_scan = new ArrayList<>();

        for (File pack : packs) {

            pass = true;
            file = new File(Core.path_config + "/dev/temporary/info/" + pack.getName() + ".txt");

            if (file.exists() == true) {

                // Get Data
                {

                    for (String scan : FileManager.readTXT(file.getPath())) {

                        if (scan.startsWith("data_structure_version = ") == true) {

                            data_structure_version = scan.substring("data_structure_version = ".length());

                        } else if (scan.startsWith("required_packs = ")) {

                            required_packs = scan.substring("required_packs = ".length());

                        } else if (scan.startsWith("required_mods = ")) {

                            required_mods = scan.substring("required_mods = ".length());

                        }

                    }

                }

                // Pack ID
                {

                    if (cache_pack_ids.containsKey(pack.getName()) == false) {

                        pass = false;
                        Error.add("pack", "packs / pack ID not found. This will results skipping these packs. Make sure you use the version that includes pack ID.", pack.getPath(), pack.getName());

                    }

                }

                // Data Structure Version
                {

                    if (Core.data_structure_version_pack.equals(data_structure_version) == false) {

                        pass = false;
                        Error.add("pack", "packs / unsupported data structure version. This will results skipping these packs. Your version is " + Core.data_structure_version_pack + " but these packs require a different version.", pack.getPath(), pack.getName() + " > " + data_structure_version);

                    }

                }

                // Duplicated Pack ID
                {

                    pack_id_scan.clear();

                    for (String id : cache_pack_ids.values()) {

                        if (pack_id_scan.contains(id) == false) {

                            pack_id_scan.add(id);

                        } else {

                            pass = false;
                            Error.add("pack", "packs / duplicated pack IDs. This will results skipping these packs. You can report this to the pack authors to help them fix it.", pack.getPath(), pack.getName() + " > " + id);
                            break;

                        }

                    }

                }

                // Required Packs
                {

                    if (required_packs.equals("none") == false) {

                        for (String value : required_packs.split(" / ")) {

                            if (cache_pack_ids.containsValue(value) == false) {

                                pass = false;
                                Error.add("pack", "packs / required packs not found. This will results skipping these packs. Make sure you install required packs to allow these packs to work.", pack.getPath(), pack.getName() + " > " + value);

                            }

                        }

                    }

                }

                // Required Mods
                {

                    if (required_mods.equals("none") == false) {

                        for (String value : required_mods.split(" / ")) {

                            if (GameUtils.Misc.isModLoaded(value) == false) {

                                pass = false;
                                Error.add("pack", "packs / required mods not found. This will results skipping these packs. Make sure you install required mods to allow these packs to work.", pack.getPath(), pack.getName() + " > " + value);

                            }

                        }

                    }

                }

            } else {

                pass = false;
                Error.add("pack", "packs / info file not found. This will results skipping these packs. Make sure you use the version that includes info file.", pack.getPath(), pack.getName());

            }

            if (pass == false) {

                FileManager.rename(file.getPath(), "/[INCOMPATIBLE] " + file.getName());

            }

        }

    }

    private static void organize (File pack, String pack_separate_multiple) {

        boolean incompatible = cache_error_files.contains(pack.getPath()) == true;

        // Get Real Pack Path
        {

            if (pack.getName().endsWith(".zip") == true) {

                pack = new File(Core.path_config + "/dev/temporary/pack_zip/" + pack.getName());

            }

        }

        File[] files = pack.listFiles();

        if (files == null) {

            return;

        }

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

    public static class Option {

        private static boolean testParse (ServerLevel level_server, File file, boolean is_function) {

            boolean pass = true;
            String id = Path.of(Core.path_config + "/dev/temporary/").relativize(file.toPath()).toString().replace("\\", "/");
            String[] split = null;
            String option = "";
            String value = "";
            File file_test = null;

            for (String scan : FileManager.readTXT(file.getPath())) {

                if (scan.contains(" = ") == true) {

                    split = scan.split(" = ");

                    // Parsing
                    {

                        try {

                            option = split[0];
                            value = split[1];

                        } catch (Exception exception) {

                            OutsideUtils.exception(new Exception(), exception, "");
                            Error.add("file", "setting files / option parsing error. This will results skipping them in mod systems.", file.getPath(), id + " > " + Arrays.toString(split));
                            pass = false;

                        }

                    }

                    if (is_function == true) {

                        // TODO

                    } else {

                        {

                            if (value.equals("none") == false) {

                                if (option.startsWith("path_") == true) {

                                    {

                                        option = option.substring("path_".length()).replace("_", " ");
                                        file_test = new File(Core.path_config + "/dev/temporary/" + value);

                                        if (file_test.exists() == true) {

                                            if (file_test.listFiles() == null) {

                                                Error.add("file", "settings files / empty " + option + " folder. This will results skipping them in mod systems.", file.getPath(), id);
                                                pass = false;

                                            }

                                        } else {

                                            file_test = new File(file_test.getPath() + ".txt");

                                            if (file_test.exists() == false) {

                                                Error.add("file", "world gen files / " + option + " path not found. This will results skipping them in mod systems.", file.getPath(), id + " > " + value);
                                                pass = false;

                                            }

                                        }

                                    }

                                } else if (option.startsWith("Block ") == true) {

                                    {

                                        if (level_server != null) {

                                            value = value.replace(" keep", "");

                                            if (GameUtils.Tile.fromText(level_server, value).isAir() == true) {

                                                Error.add("file", "settings file / unknown block IDs. This will results skipping them in mod systems.", file.getPath(), id + " > " + value);
                                                pass = false;

                                            }

                                        }

                                    }

                                } else if (option.startsWith("Function ") == true) {

                                    {

                                        file_test = new File(Core.path_config + "/dev/temporary/" + value + ".txt");


                                        if (file_test.exists() == false) {

                                            Error.add("file", "settings file / unknown functions. This will results skipping them in mod systems.", file.getPath(), id + " > " + value);
                                            pass = false;

                                        } else {

                                            if (testParse(level_server, file_test, true) == false) {

                                                Error.add("file", "settings files / functions is mark as incompatible. This will results skipping them in mod systems.", file.getPath(), id);
                                                pass = false;

                                            }

                                        }

                                    }

                                }

                            }

                        }

                    }

                }

            }

            return pass;

        }

    }

    public static class Error {

        private static void add (String type, String error, String path, String troublemaker) {

            errors.computeIfAbsent(type, create -> new HashMap<>()).computeIfAbsent(error, create -> new HashSet<>()).add(troublemaker);
            cache_error_files.add(path);

        }

        public static void sendMessage (ServerLevel level_server) {

            boolean to_chat = false;
            String message = "";
            String[] split = null;
            boolean first = true;

            for (Map.Entry<String, Map<String, Set<String>>> entry1 : errors.entrySet()) {

                to_chat = level_server != null && (entry1.getKey().equals("pack") == true || Core.developer_mode == true);

                // First Line
                {

                    if (first == true) {

                        first = false;

                        if (to_chat == true) {

                            Core.logger.error("----------------------------------------------------------------------------------------------------");

                        }

                    }

                }

                for (Map.Entry<String, Set<String>> entry2 : entry1.getValue().entrySet()) {

                    {

                        split = entry2.getKey().split(" / ");
                        message = "Detected incompatible " + split[0] + ", caused by " + split[1];

                        if (to_chat == true) {

                            GameUtils.Misc.sendChatMessage(level_server, message + " / red");

                        } else {

                            Core.logger.error(message);

                        }

                        for (String get : entry2.getValue()) {

                            if (to_chat == true) {

                                GameUtils.Misc.sendChatMessage(level_server, get + " / dark_gray");

                            } else {

                                Core.logger.error("- {}", get);

                            }

                        }

                        if (to_chat == true) {

                            Core.logger.error("----------------------------------------------------------------------------------------------------");

                        }

                    }

                }

            }

        }

    }

}
