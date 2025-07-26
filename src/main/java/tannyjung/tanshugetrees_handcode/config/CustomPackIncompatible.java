package tannyjung.tanshugetrees_handcode.config;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.fml.ModList;
import tannyjung.core.FileManager;
import tannyjung.core.OutsideUtils;
import tannyjung.tanshugetrees.TanshugetreesMod;
import tannyjung.tanshugetrees_handcode.Handcode;
import tannyjung.core.GameUtils;

import java.io.File;
import java.nio.file.Files;

public class CustomPackIncompatible {

    public static void scanMain (LevelAccessor level_accessor) {

        for (File pack : new File(Handcode.directory_config + "/custom_packs").listFiles()) {

            if (pack.getName().equals(".organized") == false) {

                if (testVersion(level_accessor, pack + "/version.txt") == false) {

                    break;

                }

                if (testDependencies(level_accessor, pack + "/dependencies.txt") == false) {

                    break;

                }

            }

        }

    }

    public static void scanOrganized () {

        // Tree Settings
        {

            File file = new File(Handcode.directory_config + "/custom_packs/.organized/world_gen");

            {

                try {

                    Files.walk(file.toPath()).forEach(source -> {

                        if (source.toFile().isDirectory() == false && source.toFile().getName().startsWith("[INCOMPATIBLE] ") == false) {

                            testTreeSettings(source.toFile());

                        }

                    });

                } catch (Exception exception) {

                    OutsideUtils.exception(new Exception(), exception);

                }

            }

        }

    }

    private static void rename (String path, boolean pass) {

        File file = new File(path);

        if (pass == true) {

            file.renameTo(new File(file.getParentFile().toPath() + "/" + file.getName().replace("[INCOMPATIBLE] ", "")));

        } else {

            if (file.getName().startsWith("[INCOMPATIBLE]") == false) {

                file.renameTo(new File(file.getParentFile().toPath() + "/[INCOMPATIBLE] " + file.getName()));

            }

        }

    }

    private static boolean testVersion (LevelAccessor level_accessor, String path) {

        boolean pass = true;
        String message = "";
        File file = new File(path);
        String pack_name = file.getParentFile().getName().replace("[INCOMPATIBLE] ", "");

        if (file.exists() == true && file.isDirectory() == false) {

            for (String read_all : FileManager.fileToStringArray(file.getPath())) {

                {

                    if (read_all.startsWith("data_structure_version = ")) {

                        if (Double.parseDouble(read_all.replace("data_structure_version = ", "")) != Handcode.data_structure_version_pack) {

                            pass = false;
                            message = "Detected incompatible pack. Caused by unsupported mod version. [ " + pack_name + " ]";
                            break;

                        }

                    }

                }

            }

        } else {

            pass = false;
            message = "Detected incompatible pack. Caused by no version file. [ " + pack_name + " ]";

        }

        if (message.equals("") == false) {

            if (level_accessor instanceof ServerLevel level_server) {

                GameUtils.misc.sendChatMessage(level_server, "@a", "red", "THT : " + message);

            }

        }

        rename(file.getParentFile().getPath(), pass);
        return pass;

    }

    private static boolean testDependencies (LevelAccessor level_accessor, String path) {

        boolean pass = true;
        String message = "";
        File file = new File(path);
        String name_pack = file.getParentFile().getName().replace("[INCOMPATIBLE] ", "");

        if (file.exists() == true && file.isDirectory() == false) {

            String get = "";

            for (String read_all : FileManager.fileToStringArray(file.getPath())) {

                {

                    if (read_all.startsWith("required_packs = ")) {

                        {

                            get = read_all.replace("required_packs = ", "");

                            if (get.equals("none") == false) {

                                for (String test : get.split(", ")) {

                                    if (new File(Handcode.directory_config + "/custom_packs/" + test).exists() == false) {

                                        pass = false;
                                        message = "Detected incompatible pack. Caused by required pack not found. [ " + name_pack + " > " + test + " ]";
                                        break;

                                    }

                                }

                            }

                        }

                    } else if (read_all.startsWith("required_mods = ")) {

                        {

                            get = read_all.replace("required_mods = ", "");

                            if (get.equals("none") == false) {

                                for (String test : get.split(", ")) {

                                    if (ModList.get().isLoaded(test) == false) {

                                        pass = false;
                                        message = "Detected incompatible pack. Caused by required mod not found. [ " + name_pack + " > " + test + " ]";
                                        break;

                                    }

                                }

                            }

                        }

                    }

                }

            }

        }

        if (message.equals("") == false) {

            if (level_accessor instanceof ServerLevel level_server) {

                GameUtils.misc.sendChatMessage(level_server, "@a", "red", "THT : " + message);

            }

        }

        rename(file.getParentFile().getPath(), pass);
        return pass;

    }

    private static void testTreeSettings (File file) {

        boolean pass = true;
        String message = "";
        String tree_settings = "";
        String name_pack = file.getParentFile().getParentFile().getName().replace("[INCOMPATIBLE] ", "");
        String name_theme = file.getParentFile().getName();
        String name_tree = file.getName().replace("[INCOMPATIBLE] ", "");

        // Read "World Gen" File
        {

            for (String read_all : FileManager.fileToStringArray(file.getPath())) {

                {

                    if (read_all.startsWith("tree_settings = ")) {

                        tree_settings = read_all.replace("tree_settings = ", "");
                        break;

                    }

                }

            }

        }

        File file_settings = new File(Handcode.directory_config + "/custom_packs/.organized/presets/" + tree_settings);
        File file_settings_incompatible = new File(Handcode.directory_config + "/custom_packs/.organized/presets/" + "[INCOMPATIBLE] " + tree_settings);

        if (file_settings_incompatible.exists() == false) {

            if (file_settings.exists() == true && file_settings.isDirectory() == false) {

                // Read "Tree Settings" File
                {

                    for (String read_all : FileManager.fileToStringArray(file_settings.getPath())) {

                        {

                            if (read_all.startsWith("Block ")) {

                                String id = read_all.substring(read_all.indexOf(" = ") + 3).replace(" keep", "");

                                if (id.equals("") == false) {

                                    if (GameUtils.block.fromText(id).getBlock() == Blocks.AIR) {

                                        pass = false;
                                        message = "Detected incompatible tree. Caused by unknown block ID. [ " + name_pack + " > " + name_tree + " > " + id + " ]";
                                        break;

                                    }

                                }

                            }

                        }

                    }

                }

            } else {

                pass = false;
                message = "Detected incompatible tree. Caused by tree settings not found. [ " + name_pack + " > " + name_theme + " > " + name_tree + " ]";

            }

        }

        if (message.equals("") == false) {

            TanshugetreesMod.LOGGER.error(message);

        }

        rename(file.getPath(), pass);

    }

}