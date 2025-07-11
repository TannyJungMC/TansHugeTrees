package tannyjung.tanshugetrees_handcode.config;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import tannyjung.core.MiscUtils;
import tannyjung.tanshugetrees.TanshugetreesMod;
import tannyjung.tanshugetrees_handcode.Handcode;
import tannyjung.core.GameUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Path;

public class CustomPackIncompatible {

    public static void scanMain (LevelAccessor level_accessor) {

        for (File pack : new File(Handcode.directory_config + "/custom_packs").listFiles()) {

            if (pack.getName().equals(".organized") == false) {

                testVersion(level_accessor, new File(pack.toPath() + "/version.txt"));

            }

        }

    }

    public static void scanOrganized () {

        File file = new File(Handcode.directory_config + "/custom_packs/.organized");

        for (File category : file.listFiles()) {

            if (category.getName().equals("world_gen") == true) {

                {

                    try {

                        Files.walk(category.toPath()).forEach(source -> {

                            if (source.toFile().isDirectory() == false) {

                                testTreeSettings(source);

                            }

                        });

                    } catch (Exception exception) {

                        MiscUtils.exception(new Exception(), exception);

                    }

                }

            }

        }

    }

    private static void testVersion (LevelAccessor level_accessor, File file) {

        boolean incompatible = false;

        if (file.exists() == true && file.isDirectory() == false) {

            {

                try { BufferedReader buffered_reader = new BufferedReader(new FileReader(file), 65536); String read_all = ""; while ((read_all = buffered_reader.readLine()) != null) {

                    {

                        if (read_all.startsWith("data_structure_version = ")) {

                            if (Double.parseDouble(read_all.replace("data_structure_version = ", "")) != Handcode.data_structure_version_pack) {

                                if (level_accessor instanceof ServerLevel level_server) {

                                    GameUtils.misc.sendChatMessage(level_server, "@a", "red", "THT : Detected incompatible pack. Caused by unsupported mod version. [ " + file.getParentFile().getName().replace("[INCOMPATIBLE] ", "") + " ]");

                                }

                                incompatible = true;
                                break;

                            }

                            break;

                        }

                    }

                } buffered_reader.close(); } catch (Exception exception) { MiscUtils.exception(new Exception(), exception); }

            }

        } else {

            incompatible = true;

            if (level_accessor instanceof ServerLevel level_server) {

                GameUtils.misc.sendChatMessage(level_server, "@a", "red", "THT : Detected incompatible pack. Caused by no version file. [ " + file.getParentFile().getName().replace("[INCOMPATIBLE] ", "") + " ]");

            }

        }

        rename(new File(file.getParentFile().getPath()), incompatible);

    }

    private static void testTreeSettings (Path source) {

        boolean incompatible = false;
        String tree_settings = "";

        // Read "World Gen" File
        {

            try { BufferedReader buffered_reader = new BufferedReader(new FileReader(source.toFile())); String read_all = ""; while ((read_all = buffered_reader.readLine()) != null) {

                {

                    if (read_all.startsWith("tree_settings = ")) {

                        tree_settings = read_all.replace("tree_settings = ", "");
                        break;

                    }

                }

            } buffered_reader.close(); } catch (Exception exception) { MiscUtils.exception(new Exception(), exception); }

        }

        File file = new File(Handcode.directory_config + "/custom_packs/.organized/presets/" + tree_settings.replace("[INCOMPATIBLE] ", ""));

        if (file.exists() == true && file.isDirectory() == false) {

            // Read "Tree Settings" File
            {

                try { BufferedReader buffered_reader = new BufferedReader(new FileReader(file), 65536); String read_all = ""; while ((read_all = buffered_reader.readLine()) != null) {

                    {

                        if (read_all.startsWith("Block ")) {

                            String id = read_all.substring(read_all.indexOf(" = ") + 3).replace(" keep", "");

                            if (id.equals("") == false) {

                                if (GameUtils.block.fromText(id).getBlock() == Blocks.AIR) {

                                    incompatible = true;
                                    TanshugetreesMod.LOGGER.error("Detected incompatible tree. Caused by unknown block ID. [ " + source.toFile().getParentFile().getName().replace("[INCOMPATIBLE] ", "") + " > " + source.toFile().getName().replace("[INCOMPATIBLE] ", "") + " ]");
                                    break;

                                }

                            }

                        }

                    }

                } buffered_reader.close(); } catch (Exception exception) { MiscUtils.exception(new Exception(), exception); }

            }

        } else {

            incompatible = true;
            TanshugetreesMod.LOGGER.error("Detected incompatible tree. Caused by no tree settings. [ " + source.toFile().getParentFile().getParentFile().getName() + " > " + source.toFile().getParentFile().getName().replace("[INCOMPATIBLE] ", "") + " > " + source.toFile().getName().replace("[INCOMPATIBLE] ", "") + " ]");

        }

        rename(source.toFile(), incompatible);

    }

    private static void rename (File file, boolean incompatible) {

        if (incompatible == false) {

            file.renameTo(new File(file.getParentFile().toPath() + "/" + file.getName().replace("[INCOMPATIBLE] ", "")));

        } else {

            if (file.getName().startsWith("[INCOMPATIBLE]") == false) {

                file.renameTo(new File(file.getParentFile().toPath() + "/[INCOMPATIBLE] " + file.getName()));

            }

        }

    }

}