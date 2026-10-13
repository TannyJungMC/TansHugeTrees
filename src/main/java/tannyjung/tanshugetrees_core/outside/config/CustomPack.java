package tannyjung.tanshugetrees_core.outside.config;

import net.minecraft.server.level.ServerLevel;
import tannyjung.tanshugetrees_core.Core;
import tannyjung.tanshugetrees_core.game.BlockManager;
import tannyjung.tanshugetrees_core.game.GameUtils;
import tannyjung.tanshugetrees_core.outside.FileManager;
import tannyjung.tanshugetrees_core.outside.OutsideUtils;
import tannyjung.tanshugetrees_handcode.Handcode;

import java.io.File;
import java.nio.file.Path;
import java.util.*;

public class CustomPack {

    private static final Map<String, String> pack_ids = new HashMap<>();
    private static final Map<String, Map<String, Set<String>>> errors = new HashMap<>();

    public static void organize (String[] folder_unique, String[] folder_fusion, String[] folder_index) {

        pack_ids.clear();
        errors.clear();

        FileManager.delete(Core.path_config + "/dev/temporary");
        FileManager.createEmptyFile(Core.path_config + "/dev/temporary", true);
        File[] packs = new File(Core.path_config + "/custom_packs").listFiles();

        if (packs == null) {

            return;

        }

        Map<String, String[]> zip_index = new HashMap<>();

        // Get ZIP Index
        {

            for (File pack : packs) {

                if (pack.isDirectory() == false) {

                    zip_index.put(pack.getName(), FileManager.ZIP.getIndex(pack.getPath()));

                }

            }

        }

        readPackInfo(packs);

        for (String folder : folder_unique) {

            organizeUnique(packs, zip_index, folder);

        }

        for (String folder : folder_fusion) {

            organizeFusion(packs, zip_index, folder);

        }

        for (String folder : folder_index) {

            organizeIndex(packs, zip_index, folder);

        }








        /*

        // Organizing Data
        {

            Map<String, Map<String, String>> config = ConfigDynamic.readTest("world_gen");
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

                                    FileManager.copyFile(scan.getPath(), path_to.toString());

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

         */

    }

    private static void readPackInfo (File[] packs) {

        String[] data = new String[]{};

        for (File pack : packs) {

            if (pack.isDirectory() == true) {

                data = FileManager.readTXT(pack.getPath() + "/info.txt");

            } else {

                data = FileManager.ZIP.readTXT(pack.getPath(), "info.txt");

            }

            for (String scan : data) {

                if (scan.startsWith("pack_id = ") == true) {

                    pack_ids.put(pack.getName(), scan.substring("pack_id = ".length()));

                }

            }

        }

    }

    private static void organizeUnique (File[] packs, Map<String, String[]> zip_index, String folder) {

        for (File pack : packs) {

            if (pack.isDirectory() == true) {

                FileManager.copyFolder(pack.getPath() + "/" + folder, Core.path_config + "/dev/temporary/" + folder + "/" + pack_ids.get(pack.getName()));

            } else {

                for (String scan : zip_index.get(pack.getName())) {

                    if (scan.startsWith(folder + "/") == true) {

                        FileManager.ZIP.extractSpecific(pack.getPath(), scan, Core.path_config + "/dev/temporary/" + folder + "/" + pack_ids.get(pack.getName()) + "/" + scan.substring((folder + "/").length()));

                    }

                }

            }

        }

    }

    private static void organizeFusion (File[] packs, Map<String, String[]> zip_index, String folder) {

        for (File pack : packs) {

            if (pack.isDirectory() == true) {

                FileManager.copyFolder(pack.getPath() + "/" + folder, Core.path_config + "/dev/temporary/" + folder);

            } else {

                for (String scan : zip_index.get(pack.getName())) {

                    if (scan.startsWith(folder + "/") == true) {

                        FileManager.ZIP.extractSpecific(pack.getPath(), scan, Core.path_config + "/dev/temporary/" + folder + "/" + scan.substring((folder + "/").length()));

                    }

                }

            }

        }

    }

    private static void organizeIndex (File[] packs, Map<String, String[]> zip_index, String folder) {

        Map<String, String> map = new HashMap<>();
        String pack_id = "";
        String path = "";

        for (File pack : packs) {

            pack_id = pack_ids.get(pack.getName());

            if (pack.isDirectory() == true) {

                for (File scan : FileManager.getAllFiles(pack.getPath() + "/" + folder)) {

                    path = Path.of(Core.path_config + "/custom_packs/" + pack.getName()).relativize(scan.toPath()).toString();
                    path = path.substring((folder + "/").length());
                    path = path.replace("\\", "/");
                    map.put(Core.path_config + "/dev/temporary/" + folder + "/" + pack_id + "/" + path + ".txt", pack.getName() + "|" + folder + "/" + path);

                }

            } else {

                for (String scan : zip_index.get(pack.getName())) {

                    if (scan.startsWith(folder + "/") == true) {

                        path = scan.substring((folder + "/").length());
                        map.put(Core.path_config + "/dev/temporary/" + folder + "/" + pack_id + "/" + path + ".txt", pack.getName() + "|" + folder + "/" + path);

                    }

                }

            }

            for (Map.Entry<String, String> entry : map.entrySet()) {

                FileManager.writeTXT(entry.getKey(), entry.getValue(), false);

            }

            map.clear();

        }

    }















    private static void organizeInfo (File[] packs, Map<String, String> pack_ids) {

        Map<String, String> locations = new HashMap<>();
        File file = null;

        for (File pack : packs) {

            if (pack.getName().endsWith(".zip") == true) {

                file = new File(Core.path_config + "/dev/temporary/pack_zip/" + pack.getName() + "/info.txt");

            } else {

                file = new File(pack.getPath() + "/info.txt");

            }

            if (file.exists() == true) {

                FileManager.copyFile(file.getPath(), Core.path_config + "/dev/temporary/info/" + pack.getName() + ".txt");
                locations.put(pack.getName(), file.getPath());

            }

        }

        // Get Pack ID
        {

            for (Map.Entry<String, String> entry : locations.entrySet()) {

                pack_ids.put(entry.getKey(), OutsideUtils.convertFileToDataMap(entry.getValue()).get("pack_id"));

            }

        }

        boolean pass = false;
        Map<String, String> data = null;
        String location = "";
        String pack_id = "";
        String data_structure_version = "";
        String required_packs = "";
        String required_mods = "";
        Map<String, List<Integer>> test_duplicated_pack = new HashMap<>();

        for (File pack : packs) {

            pass = true;
            location = locations.get(pack.getName());

            if (location == null) {

                Error.add("pack", "packs / info file not found. This will results skipping these packs. Make sure you use the version that includes info file.", pack.getPath(), pack.getName());
                pass = false;

            }

            data = OutsideUtils.convertFileToDataMap(location);

            // Pack ID
            {

                pack_id = pack_ids.get(pack.getName());

                if (pack_id == null) {

                    Error.add("pack", "packs / pack ID not found. This will results skipping these packs. Make sure you use the version that includes pack ID.", pack.getPath(), pack.getName());
                    pass = false;

                } else {

                    test_duplicated_pack.computeIfAbsent(pack_id, create -> new ArrayList<>()).add(1);

                    if (test_duplicated_pack.get(pack_id).size() > 1) {

                        Error.add("pack", "packs / duplicated pack IDs. This will results skipping these packs. You can report this to the pack authors to help them fix it.", pack.getPath(), pack.getName() + " > " + pack_id);
                        pass = false;

                        pack_ids.replace(pack.getName(), pack_id + "_" + test_duplicated_pack.get(pack_id).size());

                    }

                }

            }

            // Data Structure Version
            {

                data_structure_version = data.get("data_structure_version");

                if (data_structure_version == null) {

                    Error.add("pack", "packs / missing data_structure_version", pack.getPath(), pack.getName() + " > " + data_structure_version);
                    pass = false;

                } else {

                    if (Core.data_structure_version_pack.equals(data_structure_version) == false) {

                        Error.add("pack", "packs / unsupported data structure version. This will results skipping these packs. Your version is " + Core.data_structure_version_pack + " but these packs require a different version.", pack.getPath(), pack.getName() + " > " + data_structure_version);
                        pass = false;

                    }

                }

            }

            // Required Packs
            {

                required_packs = data.get("required_packs");

                if (required_packs == null) {

                    Error.add("pack", "packs / missing required_packs", pack.getPath(), pack.getName() + " > " + data_structure_version);
                    pass = false;

                } else {

                    if (required_packs.isEmpty() == false) {

                        for (String scan : required_packs.split(" / ")) {

                            if (pack_ids.containsValue(scan) == false) {

                                Error.add("pack", "packs / required packs not found. This will results skipping these packs. Make sure you install required packs to allow these packs to work.", pack.getPath(), pack.getName() + " > " + scan);
                                pass = false;

                            }

                        }

                    }

                }

            }

            // Required Mods
            {

                required_mods = data.get("required_mods");

                if (required_mods == null) {

                    Error.add("pack", "packs / missing required_mods", pack.getPath(), pack.getName() + " > " + data_structure_version);
                    pass = false;

                } else {

                    if (required_mods.isEmpty() == false) {

                        for (String scan : required_mods.split(" / ")) {

                            if (GameUtils.isModLoaded(scan) == false) {

                                Error.add("pack", "packs / required mods not found. This will results skipping these packs. Make sure you install required mods to allow these packs to work.", pack.getPath(), pack.getName() + " > " + scan);
                                pass = false;

                            }

                        }

                    }

                }

            }

        }

    }

    private static void test (ServerLevel level_server, String[] folders, boolean is_function) {

        for (String folder : folders) {

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

    private static void organize (File pack, String[] pack_separate_multiple) {

        boolean incompatible = pack.getName().startsWith("[INCOMPATIBLE] ") == true;

        // Get ZIP Pack Path
        {

            if (pack.getName().endsWith(".zip") == true) {

                pack = new File(Core.path_config + "/dev/temporary/pack_zip/" + pack.getName());

            }

        }

        boolean is_separate_multiple = false;
        File[] files = pack.listFiles();

        if (files == null) {

            return;

        }

        for (File file : files) {

            if (file.isDirectory() == true) {

                is_separate_multiple = false;

                for (String scan : pack_separate_multiple) {

                    if (file.getName().equals(scan) == true) {

                        is_separate_multiple = true;
                        break;

                    }

                }

                for (File scan : FileManager.getAllFiles(file.getPath())) {

                    {

                        Path path_copy_to = Path.of(Core.path_config + "/dev/temporary/" + file.getName());

                        // Convert Path
                        {

                            if (is_separate_multiple == true) {

                                // path_copy_to = path_copy_to.resolve(cache_pack_ids.get(pack.getName()));

                            }

                            path_copy_to = path_copy_to.resolve(pack.toPath().resolve(file.getName()).relativize(scan.toPath()));

                            if (incompatible == true) {

                                path_copy_to = path_copy_to.getParent().resolve("[INCOMPATIBLE] " + path_copy_to.toFile().getName());

                            }

                        }

                        FileManager.copyFile(scan.getPath(), path_copy_to.toString());

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

                                // TODO

                                if (false     &&     option.startsWith("path_") == true) {

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

                                            if (BlockManager.fromText(level_server, value).isAir() == true) {

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
            // cache_error_files.add(path);

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

                        if (to_chat == false) {

                            Core.logger.error("----------------------------------------------------------------------------------------------------");

                        }

                    }

                }

                for (Map.Entry<String, Set<String>> entry2 : entry1.getValue().entrySet()) {

                    {

                        split = entry2.getKey().split(" / ");
                        message = "Detected incompatible " + split[0] + ", caused by " + split[1];

                        if (to_chat == true) {

                            GameUtils.sendChatMessage(level_server, message + " / red");

                        } else {

                            Core.logger.error(message);

                        }

                        for (String get : entry2.getValue()) {

                            if (to_chat == true) {

                                GameUtils.sendChatMessage(level_server, get + " / dark_gray");

                            } else {

                                Core.logger.error("- {}", get);

                            }

                        }

                        if (to_chat == false) {

                            Core.logger.error("----------------------------------------------------------------------------------------------------");

                        }

                    }

                }

            }

        }

    }

}
