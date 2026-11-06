package tannyjung.tanshugetrees_core.outside.config;

import net.minecraft.server.level.ServerLevel;
import tannyjung.tanshugetrees_core.Core;
import tannyjung.tanshugetrees_core.game.BlockManager;
import tannyjung.tanshugetrees_core.game.GameUtils;
import tannyjung.tanshugetrees_core.outside.FileManager;
import tannyjung.tanshugetrees_core.outside.OutsideUtils;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.file.Path;
import java.util.*;

public class CustomPack {

    private static final Map<String, String> pack_ids = new HashMap<>();
    private static final Map<String, List<String>> cache_errors = new HashMap<>();

    public static void organize (String[] folder_unique, String[] folder_fusion, String[] folder_index_bin) {

        pack_ids.clear();

        FileManager.delete(Core.path_config + "/dev/temporary");
        FileManager.createEmptyFile(Core.path_config + "/dev/temporary", true);
        File[] packs = new File(Core.path_config + "/custom_packs").listFiles();

        if (packs == null) {

            return;

        }

        Map<String, Map<String, String>> info_data = getInfoData(packs);
        Map<String, String[]> zip_index_bin = new HashMap<>();

        // Get ZIP Index
        {

            for (File pack : packs) {

                if (pack.isDirectory() == false) {

                    zip_index_bin.put(pack.getName(), FileManager.ZIP.getIndex(pack.getPath()));

                }

            }

        }

        for (String folder : folder_unique) {

            organizeUnique(packs, zip_index_bin, folder);

        }

        for (String folder : folder_fusion) {

            organizeFusion(packs, zip_index_bin, folder);

        }

        for (String folder : folder_index_bin) {

            organizeIndexBIN(packs, zip_index_bin, folder);

        }

        replace(packs, zip_index_bin);

        Status.start(info_data);

        cache_errors.clear();








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

    public static Map<String, String> convertDataToMap (String[] data) {

        Map<String, String> convert = new HashMap<>();
        String[] split = null;

        for (String scan : data) {

            if (scan.isEmpty() == true) {

                continue;

            }

            if (scan.contains(" = ") == false) {

                continue;

            }

            split = scan.split(" = ");

            if (split[1].equals("none") == true) {

                split[1] = "";

            }

            convert.put(split[0], split[1]);

        }

        return convert;

    }

    private static Map<String, Map<String, String>> getInfoData (File[] packs) {

        Map<String, Map<String, String>> data = new HashMap<>();
        Map<String, String> info_data = new HashMap<>();
        String destination = "";

        for (File pack : packs) {

            destination = Core.path_config + "/dev/temporary/info/" + pack.getName() + ".txt";

            if (pack.isDirectory() == true) {

                FileManager.copyFile(pack.getPath() + "/info.txt", destination);

            } else {

                FileManager.ZIP.extractSpecific(pack.getPath(), "info.txt", destination);

            }

            info_data = convertDataToMap(FileManager.readTXT(destination));
            data.put(pack.getName(), info_data);
            pack_ids.put(pack.getName(), info_data.getOrDefault("pack_id", ""));

        }

        return data;

    }

    private static void organizeUnique (File[] packs, Map<String, String[]> zip_index, String folder) {

        for (File pack : packs) {

            if (pack.isDirectory() == true) {

                FileManager.copyFolder(pack.getPath() + "/" + folder, Core.path_config + "/dev/temporary/" + folder + "/" + pack_ids.get(pack.getName()));

            } else {

                for (String scan : zip_index.get(pack.getName())) {

                    if (scan.startsWith(folder + "/") == false) {

                        continue;

                    }

                    FileManager.ZIP.extractSpecific(pack.getPath(), scan, Core.path_config + "/dev/temporary/" + folder + "/" + pack_ids.get(pack.getName()) + "/" + scan.substring((folder + "/").length()));

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

                    if (scan.startsWith(folder + "/") == false) {

                        continue;

                    }

                    FileManager.ZIP.extractSpecific(pack.getPath(), scan, Core.path_config + "/dev/temporary/" + folder + "/" + scan.substring((folder + "/").length()));

                }

            }

        }

    }

    private static void organizeIndexBIN (File[] packs, Map<String, String[]> zip_index, String folder) {

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

                    if (scan.startsWith(folder + "/") == false) {

                        continue;

                    }

                    path = scan.substring((folder + "/").length());
                    map.put(Core.path_config + "/dev/temporary/" + folder + "/" + pack_id + "/" + path + ".txt", pack.getName() + "|" + folder + "/" + path);

                }

            }

            for (Map.Entry<String, String> entry : map.entrySet()) {

                FileManager.writeTXT(entry.getKey(), entry.getValue(), false);

            }

            map.clear();

        }

    }

    public static ByteBuffer readIndexBIN (String path_file_index) {

        String[] list = FileManager.readTXT(path_file_index);

        if (list.length == 0) {

            return ByteBuffer.allocate(0);

        }

        String[] split = list[0].split("\\|");

        if (split[0].endsWith(".zip") == true) {

            return FileManager.ZIP.readBIN(Core.path_config + "/custom_packs/" + split[0], split[1]);

        } else {

            return FileManager.readBIN(Core.path_config + "/custom_packs/" + split[0] + "/" + split[1]);

        }

    }

    private static void replace (File[] packs, Map<String, String[]> zip_index) {

        Map<String, Map<String, String[]>> replace = new HashMap<>();

        // Collect Replace Data
        {

            for (File pack : packs) {

                if (pack.isDirectory() == true) {

                    for (File scan : FileManager.getAllFiles(pack.getPath() + "/replace")) {

                        replace.computeIfAbsent(pack.getName(), create -> new HashMap<>()).put(Core.path_config + "/dev/temporary/" + Path.of(Core.path_config + "/custom_packs/" + pack.getName() + "/replace").relativize(scan.toPath()), FileManager.readTXT(scan.getPath()));

                    }

                } else {

                    for (String scan : zip_index.get(pack.getName())) {

                        if (scan.startsWith("replace/") == false) {

                            continue;

                        }

                        replace.computeIfAbsent(pack.getName(), create -> new HashMap<>()).put(Core.path_config + "/dev/temporary/" + scan.substring("replace/".length()), FileManager.ZIP.readTXT(pack.getPath(), scan));

                    }

                }

            }

        }

        List<String> data_before = new ArrayList<>();
        List<String> data_before_reorganize = new ArrayList<>();
        StringBuilder data_after = new StringBuilder();
        String[] split = new String[]{};
        int skip = 0;
        String mode = "";

        String option_prefix = "";
        String option_name = "";
        String option_value_old = "";
        String option_value_new = "";

        for (Map.Entry<String, Map<String, String[]>> entry1 : replace.entrySet()) {

            for (Map.Entry<String, String[]> entry2 : entry1.getValue().entrySet()) {

                if (entry2.getValue().length == 0) {

                    continue;

                }

                mode = entry2.getValue()[0];

                if (mode.equals("# ALL") == true) {

                    {

                        data_after.append("REPLACED ALL BY [ ").append(entry1.getKey()).append(" ]");
                        data_after.append("\n");
                        skip = 2;

                        for (String scan : entry2.getValue()) {

                            if (skip > 0) {

                                skip = skip - 1;
                                continue;

                            }

                            data_after.append(scan);
                            data_after.append("\n");

                        }

                    }

                } else if (mode.equals("# SPECIFIC") == true) {

                    {

                        data_before = Arrays.stream(FileManager.readTXT(entry2.getKey())).toList();

                        for (String scan : entry2.getValue()) {

                            split = scan.split(" = ");

                            if (split.length == 1) {

                                continue;

                            }

                            option_value_new = split[1];
                            split = split[0].split(" -> ");

                            if (split.length == 1) {

                                continue;

                            }

                            option_prefix = split[0];
                            option_name = split[1];

                            for (String scan_old : data_before) {

                                split = scan_old.split(" = ");

                                if (split.length == 2 && split[0].equals(option_name) == true) {

                                    option_value_old = split[1];

                                    if (option_prefix.equals("SET") == true) {

                                        {

                                            data_before_reorganize.add(option_name + " = " + option_value_new);
                                            data_before_reorganize.add("> EDITED BY [ " + entry1.getKey() + " ] SET VALUE FROM PREVIOUS [ " + option_value_old + " ]");
                                            continue;

                                        }

                                    } else if (option_prefix.equals("ADD") == true) {

                                        {

                                            data_before_reorganize.add(option_name + " = " + option_value_old + " / " + option_value_new);
                                            data_before_reorganize.add("> EDITED BY [ " + entry1.getKey() + " ] ADDED VALUE [ " + option_value_new + " ]");
                                            continue;

                                        }

                                    } else if (option_prefix.equals("REPLACE") == true) {

                                        {

                                            split = option_value_new.split(" -> ");

                                            if (split.length == 2 && option_value_old.contains(split[0]) == true) {

                                                data_before_reorganize.add(option_name + " = " + option_value_old.replace(split[0], split[1]));
                                                data_before_reorganize.add("> EDITED BY [ " + entry1.getKey() + " ] REPLACED WORD [ " + split[0] + " ] TO [ " + split[1] + " ]");
                                                continue;

                                            }

                                        }

                                    }

                                }

                                data_before_reorganize.add(scan_old);

                            }

                            data_before = new ArrayList<>(data_before_reorganize);
                            data_before_reorganize.clear();

                        }

                        for (String scan : data_before) {

                            data_after.append(scan);
                            data_after.append("\n");

                        }

                    }

                }

                if (data_after.isEmpty() == true) {

                    continue;

                }

                FileManager.writeTXT(entry2.getKey(), data_after.toString(), false);
                data_after.setLength(0);

            }

        }

    }

    private static class Status {

        private static void start (Map<String, Map<String, String>> info_data) {

            Status.checkInfo(info_data);

            if (cache_errors.isEmpty() == true) {

                return;

            }

            StringBuilder builder = new StringBuilder();
            List<String> list = new ArrayList<>();

            builder.append("Here is all errors detected in custom packs");
            builder.append("\n");
            builder.append("\n");
            builder.append("----------------------------------------------------------------------------------------------------");
            builder.append("\n");
            builder.append("Hard Errors");
            builder.append("\n");
            builder.append("----------------------------------------------------------------------------------------------------");
            builder.append("\n");
            builder.append("\n");

            list = cache_errors.getOrDefault("hard", new ArrayList<>());

            if (list.isEmpty() == true) {

                builder.append("It looks fine here");
                builder.append("\n");

            } else {

                for (String error : list) {

                    builder.append("- ").append(error);
                    builder.append("\n");

                }

            }

            builder.append("\n");
            builder.append("----------------------------------------------------------------------------------------------------");
            builder.append("\n");
            builder.append("Soft Errors");
            builder.append("\n");
            builder.append("----------------------------------------------------------------------------------------------------");
            builder.append("\n");
            builder.append("\n");

            list = cache_errors.getOrDefault("soft", new ArrayList<>());

            if (list.isEmpty() == true) {

                builder.append("It looks fine here");
                builder.append("\n");

            } else {

                for (String error : list) {

                    builder.append("- ").append(error);
                    builder.append("\n");

                }

            }

            builder.append("\n");
            builder.append("----------------------------------------------------------------------------------------------------");

            FileManager.writeTXT(Core.path_config + "/status.txt", builder.toString(), false);

        }

        private static void addError (String error) {

            cache_errors.computeIfAbsent("hard", create -> new ArrayList<>()).add(error);

        }

        private static void checkInfo (Map<String, Map<String, String>> info_data) {

            String value = "";
            boolean test = false;

            for (Map.Entry<String, Map<String, String>> entry : info_data.entrySet()) {

                if (entry.getValue().isEmpty() == true) {

                    Status.addError(entry.getKey() + " -> Not detected info file or it has empty info file");
                    continue;

                }

                // Pack ID
                {

                    value = entry.getValue().get("pack_id");

                    if (value == null) {

                        Status.addError(entry.getKey() + " -> Not detected option name [ pack_id ] in info file of the pack");

                    } else {

                        for (Map.Entry<String, String> scan : pack_ids.entrySet()) {

                            if (scan.getKey().equals(entry.getKey()) == true) {

                                continue;

                            }

                            if (scan.getValue().equals(value) == true) {

                                Status.addError(entry.getKey() + " -> Detected duplicate pack ID [ " + value + " ] same as pack [ " + scan.getKey() + " ]");
                                break;

                            }

                        }

                    }

                }

                // Required Packs
                {

                    value = entry.getValue().get("required_packs");

                    if (value == null) {

                        Status.addError(entry.getKey() + " -> Not detected option name [ required_packs ] in info file of the pack");

                    } else if (value.equals("none") == false) {

                        for (String scan : value.split(", ")) {

                            if (pack_ids.containsValue(scan) == false) {

                                Status.addError(entry.getKey() + " -> Not detected pack ID [ " + scan + " ] which is required");

                            }

                        }

                    }

                }

                // Required Mods
                {

                    value = entry.getValue().get("required_mods");

                    if (value == null) {

                        Status.addError(entry.getKey() + " -> Not detected option name [ required_mods ] in info file of the pack");

                    } else if (value.equals("none") == false) {

                        for (String scan : value.split(", ")) {

                            if (GameUtils.isModLoaded(scan) == false) {

                                Status.addError(entry.getKey() + " -> Not detected mod ID [ " + scan + " ] which is required");

                            }

                        }

                    }

                }

                // Data Structure Version
                {

                    value = entry.getValue().get("data_structure_version");

                    if (value == null) {

                        Status.addError(entry.getKey() + " -> Not detected option name [ data_structure_version ] in info file of the pack");

                    } else {

                        test = false;

                        for (String scan : value.split(" / ")) {

                            if (Core.data_structure_version_pack.equals(scan) == true) {

                                test = true;
                                break;

                            }

                        }

                        if (test == false) {

                            Status.addError(entry.getKey() + " -> Unmatched data structure version [ " + value + " ] but the mod needs [ " + Core.data_structure_version_pack + " ]");

                        }

                    }

                }

            }

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

                pack_ids.put(entry.getKey(), convertDataToMap(FileManager.readTXT(entry.getValue())).get("pack_id"));

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

                Error2.add("pack", "packs / info file not found. This will results skipping these packs. Make sure you use the version that includes info file.", pack.getPath(), pack.getName());
                pass = false;

            }

            data = convertDataToMap(FileManager.readTXT(location));

            // Pack ID
            {

                pack_id = pack_ids.get(pack.getName());

                if (pack_id == null) {

                    Error2.add("pack", "packs / pack ID not found. This will results skipping these packs. Make sure you use the version that includes pack ID.", pack.getPath(), pack.getName());
                    pass = false;

                } else {

                    test_duplicated_pack.computeIfAbsent(pack_id, create -> new ArrayList<>()).add(1);

                    if (test_duplicated_pack.get(pack_id).size() > 1) {

                        Error2.add("pack", "packs / duplicated pack IDs. This will results skipping these packs. You can report this to the pack authors to help them fix it.", pack.getPath(), pack.getName() + " > " + pack_id);
                        pass = false;

                        pack_ids.replace(pack.getName(), pack_id + "_" + test_duplicated_pack.get(pack_id).size());

                    }

                }

            }

            // Data Structure Version
            {

                data_structure_version = data.get("data_structure_version");

                if (data_structure_version == null) {

                    Error2.add("pack", "packs / missing data_structure_version", pack.getPath(), pack.getName() + " > " + data_structure_version);
                    pass = false;

                } else {

                    if (Core.data_structure_version_pack.equals(data_structure_version) == false) {

                        Error2.add("pack", "packs / unsupported data structure version. This will results skipping these packs. Your version is " + Core.data_structure_version_pack + " but these packs require a different version.", pack.getPath(), pack.getName() + " > " + data_structure_version);
                        pass = false;

                    }

                }

            }

            // Required Packs
            {

                required_packs = data.get("required_packs");

                if (required_packs == null) {

                    Error2.add("pack", "packs / missing required_packs", pack.getPath(), pack.getName() + " > " + data_structure_version);
                    pass = false;

                } else {

                    if (required_packs.isEmpty() == false) {

                        for (String scan : required_packs.split(" / ")) {

                            if (pack_ids.containsValue(scan) == false) {

                                Error2.add("pack", "packs / required packs not found. This will results skipping these packs. Make sure you install required packs to allow these packs to work.", pack.getPath(), pack.getName() + " > " + scan);
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

                    Error2.add("pack", "packs / missing required_mods", pack.getPath(), pack.getName() + " > " + data_structure_version);
                    pass = false;

                } else {

                    if (required_mods.isEmpty() == false) {

                        for (String scan : required_mods.split(" / ")) {

                            if (GameUtils.isModLoaded(scan) == false) {

                                Error2.add("pack", "packs / required mods not found. This will results skipping these packs. Make sure you install required mods to allow these packs to work.", pack.getPath(), pack.getName() + " > " + scan);
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
                            Error2.add("file", "setting files / option parsing error. This will results skipping them in mod systems.", file.getPath(), id + " > " + Arrays.toString(split));
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

                                                Error2.add("file", "settings files / empty " + option + " folder. This will results skipping them in mod systems.", file.getPath(), id);
                                                pass = false;

                                            }

                                        } else {

                                            file_test = new File(file_test.getPath() + ".txt");

                                            if (file_test.exists() == false) {

                                                Error2.add("file", "world gen files / " + option + " path not found. This will results skipping them in mod systems.", file.getPath(), id + " > " + value);
                                                pass = false;

                                            }

                                        }

                                    }

                                } else if (option.startsWith("Block ") == true) {

                                    {

                                        if (level_server != null) {

                                            value = value.replace(" keep", "");

                                            if (BlockManager.fromText(level_server, value).isAir() == true) {

                                                Error2.add("file", "settings file / unknown block IDs. This will results skipping them in mod systems.", file.getPath(), id + " > " + value);
                                                pass = false;

                                            }

                                        }

                                    }

                                } else if (option.startsWith("Function ") == true) {

                                    {

                                        file_test = new File(Core.path_config + "/dev/temporary/" + value + ".txt");


                                        if (file_test.exists() == false) {

                                            Error2.add("file", "settings file / unknown functions. This will results skipping them in mod systems.", file.getPath(), id + " > " + value);
                                            pass = false;

                                        } else {

                                            if (testParse(level_server, file_test, true) == false) {

                                                Error2.add("file", "settings files / functions is mark as incompatible. This will results skipping them in mod systems.", file.getPath(), id);
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

    public static class Error2 {

        private static void add (String type, String error, String path, String troublemaker) {

            // errors.computeIfAbsent(type, create -> new HashMap<>()).computeIfAbsent(error, create -> new HashSet<>()).add(troublemaker);
            // cache_error_files.add(path);

        }

        public static void sendMessage (ServerLevel level_server) {

            boolean to_chat = false;
            String message = "";
            String[] split = null;
            boolean first = true;

            /*

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

             */

        }

    }

}
