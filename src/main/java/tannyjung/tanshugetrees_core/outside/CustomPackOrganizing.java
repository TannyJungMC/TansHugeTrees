package tannyjung.tanshugetrees_core.outside;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Blocks;
import tannyjung.tanshugetrees_core.Core;
import tannyjung.tanshugetrees_core.game.GameUtils;
import tannyjung.tanshugetrees_handcode.data.FileConfig;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class CustomPackOrganizing {

    private static final Map<String, String> cache_pack_ids = new HashMap<>();
    private static final Map<String, Map<String, List<String>>> cache_errors = new HashMap<>();

    public static void start (String pack_separate_multiple) {

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
            pack_separate_multiple = "/" + pack_separate_multiple + "/";

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

                if (file.listFiles() != null) {

                    try {

                        Files.walk(file.toPath()).forEach(source -> {

                            if (source.toFile().isDirectory() == false) {

                                Path path_to = file.toPath().relativize(source);
                                path_to = Path.of(Core.path_config + "/dev/temporary").resolve(path_to);

                                if (path_to.toFile().exists() == true) {

                                    // Edit
                                    {

                                        if (source.toString().endsWith(".txt") == true) {

                                            FileManager.mergeTXT(source.toFile(), path_to.toFile());

                                        } else {

                                            FileManager.copy(source.toString(), path_to.toString(), false);

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

        }

        FileManager.delete(Core.path_config + "/dev/temporary/pack_zip");
        testSettings();
        testWorldGen();

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
            String pack_id = "";
            List<String> pack_id_duplicated_test = new ArrayList<>();
            String data_structure_version = "";
            String required_packs = "";
            String required_mods = "";

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

                    pack_id = cache_pack_ids.get(pack.getName());

                    test:
                    {

                        // Pack ID
                        {

                            if (pack_id == null) {

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

                            if (pack_id_duplicated_test.contains(pack_id) == true) {

                                FileManager.rename(file.getPath(), "/[INCOMPATIBLE] " + file.getName());
                                addError("pack", "packs / duplicated pack ID. This will results skipping these packs. You can report this to the pack authors to help them fix it.", pack.getPath(), pack.getName() + " > " + pack_id);

                                while (cache_pack_ids.containsValue(pack_id) == true) {

                                    pack_id = pack_id + "X";

                                }

                                cache_pack_ids.put(pack.getName(), pack_id);
                                break test;

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

                    pack_id_duplicated_test.add(pack_id);

                } else {

                    FileManager.rename(file.getPath(), "/[INCOMPATIBLE] " + file.getName());
                    addError("pack", "packs / info file not found. This will results skipping these packs. Make sure you're using the version that includes info file.", pack.getPath(), pack.getName());

                }

            }

        }

    }

    private static void testSettings () {

        File file = new File(Core.path_config + "/dev/temporary/presets");

        if (file.exists() == true && file.isDirectory() == true) {

            try {

                Files.walk(file.toPath()).forEach(source -> {

                    File file_each = source.toFile();

                    if (file_each.getName().startsWith("[INCOMPATIBLE] ") == false && file_each.getName().endsWith("_settings.txt") == true) {

                        String name = Path.of(Core.path_config + "/dev/temporary/presets").relativize(file_each.toPath()).toString().replace("\\", "/");
                        String value = "";

                        for (String read_all : FileManager.readTXT(file_each.getPath())) {

                            if (read_all.contains(" = ") == true) {

                                if (read_all.startsWith("Block ") == true) {

                                    {

                                        value = read_all.substring(read_all.indexOf(" = ") + 3);

                                        if (value.isEmpty() == false) {

                                            if (GameUtils.Tile.fromText(value.replace(" keep", "")).getBlock() == Blocks.AIR) {

                                                addError("file", "settings file / unknown block IDs. This will results discontinue these trees and skip them in region pre-location.", file_each.getPath(), name + " > " + value);
                                                break;

                                            }

                                        }

                                    }

                                } else if (read_all.startsWith("Function ") == true) {

                                    {

                                        value = read_all.substring(read_all.indexOf(" = ") + 3);

                                        if (value.isEmpty() == false) {

                                            if (new File(Core.path_config + "/dev/temporary/functions/" + value + ".txt").exists() == false) {

                                                addError("file", "settings file / unknown functions. This will results skip these functions from running.", file_each.getPath(), name + " > " + value);
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

        File file = new File(Core.path_config + "/dev/temporary/world_gen");

        if (file.exists() == true && file.isDirectory() == true) {

            try {

                Files.walk(Path.of(Core.path_config + "/dev/temporary/world_gen")).forEach(source -> {

                    File file_each = source.toFile();

                    if (file_each.getName().startsWith("[INCOMPATIBLE] ") == false && file_each.getName().endsWith(".txt") == true) {

                        String name = Path.of(Core.path_config + "/dev/temporary/world_gen").relativize(file_each.toPath()).toString().replace("\\", "/");
                        File file_test = null;

                        for (String read_all : FileManager.readTXT(file_each.getPath())) {

                            if (read_all.contains(" = ") == true) {

                                if (read_all.startsWith("path_storage = ") == true) {

                                    {

                                        file_test = new File(Core.path_config + "/dev/temporary/presets/" + read_all.substring("path_storage = ".length()) + "/storage");

                                        if (file_test.exists() == true && file_test.isDirectory() == true) {

                                            if (file_test.listFiles() == null) {

                                                addError("file", "world gen files / empty storage. This will results discontinue these trees and skip them in world gen.", file_each.getPath(), name);
                                                break;

                                            }

                                        } else {

                                            addError("file", "world gen files / storage not found. This will results discontinue these trees and skip them in world gen.", file_each.getPath(), name);
                                            break;

                                        }

                                    }

                                } else if (read_all.startsWith("path_settings = ") == true) {

                                    {

                                        file_test = new File(Core.path_config + "/dev/temporary/presets/" + read_all.substring("path_settings = ".length()) + ".txt");

                                        if (file_test.exists() == false) {

                                            addError("file", "world gen files / settings not found. This will results skip them in world gen.", file_each.getPath(), name);
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

    private static void organize (File file_pack, String pack_separate_multiple) {

        boolean incompatible = file_pack.getName().startsWith("[INCOMPATIBLE] ") == true;

        // Get Real Pack Path
        {

            if (file_pack.getName().endsWith(".zip") == true) {

                file_pack = new File(Core.path_config + "/dev/temporary/pack_zip/" + file_pack.getName());

            }

        }

        File[] files = file_pack.listFiles();

        if (files != null) {

            File file_pack_final = file_pack;
            boolean is_separate_multiple = false;

            for (File file : files) {

                if (file.isDirectory() == true) {

                    is_separate_multiple = pack_separate_multiple.contains("/" + file.getName() + "/") == true;
                    boolean is_separate_multiple_final = is_separate_multiple;

                    try {

                        Files.walk(file.toPath()).forEach(source -> {

                            if (source.toFile().isDirectory() == false) {

                                Path path_copy_to = Path.of(Core.path_config + "/dev/temporary/" + file.getName());

                                // Convert Path
                                {

                                    if (is_separate_multiple_final == true) {

                                        path_copy_to = path_copy_to.resolve(file_pack_final.getName());

                                    }

                                    path_copy_to = path_copy_to.resolve(file_pack_final.toPath().resolve(file.getName()).relativize(source));

                                    if (incompatible == true) {

                                        path_copy_to = path_copy_to.getParent().resolve("[INCOMPATIBLE] " + path_copy_to.toFile().getName());

                                    }

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

    private static void addError (String type, String error, String path, String troublemaker) {

        cache_errors.computeIfAbsent(type, test -> new HashMap<>()).computeIfAbsent(error, test -> new ArrayList<>()).add(troublemaker);
        File file = new File(path);

        if (file.getName().startsWith("[INCOMPATIBLE] ") == false) {

            file.renameTo(new File(file.getParentFile().toPath() + "/[INCOMPATIBLE] " + file.getName()));

        }

    }

    public static void sendErrorMessage (ServerLevel level_server) {

        boolean to_chat = false;
        String message = "";
        String[] split = new String[0];

        for (Map.Entry<String, Map<String, List<String>>> entry1 : cache_errors.entrySet()) {

            to_chat = entry1.getKey().equals("pack") == true || FileConfig.developer_mode == true;

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
