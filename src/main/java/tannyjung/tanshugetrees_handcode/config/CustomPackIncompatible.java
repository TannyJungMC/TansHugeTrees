package tannyjung.tanshugetrees_handcode.config;

import net.minecraft.world.level.block.Blocks;
import tannyjung.tanshugetrees_handcode.Handcode;
import tannyjung.tanshugetrees_handcode.misc.Misc;
import tannyjung.tanshugetrees_handcode.misc.MiscOutside;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Path;

public class CustomPackIncompatible {

    public static void start () {

        scanMain();
        scanOrganized();

    }

    public static void scanMain () {

        for (File pack : new File(Handcode.directory_config + "/custom_packs").listFiles()) {

            if (pack.getName().equals(".organized") == false) {

                testVersion(new File(pack.toPath() + "/version.txt"));

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

                    } catch (Exception e) {

                        e.printStackTrace();

                    }

                }

            }

        }

    }

    private static void testVersion (File file) {

        boolean incompatible = false;

        if (file.exists() == true && file.isDirectory() == false) {

            {

                try { BufferedReader buffered_reader = new BufferedReader(new FileReader(file)); String read_all = ""; while ((read_all = buffered_reader.readLine()) != null) {

                    {

                        if (read_all.startsWith("mod_version = ")) {

                            if (Double.parseDouble(read_all.replace("mod_version = ", "")) != Handcode.mod_version) {

                                incompatible = true;
                                break;

                            }

                            break;

                        }

                    }

                } buffered_reader.close(); } catch (Exception e) { e.printStackTrace(); }

            }

        } else {

            incompatible = true;

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

            } buffered_reader.close(); } catch (Exception e) { e.printStackTrace(); }

        }

        File file = new File(Handcode.directory_config + "/custom_packs/.organized/presets/" + tree_settings);

        if (file.exists() == true && file.isDirectory() == false) {

            // Read "Tree Settings" File
            {

                try { BufferedReader buffered_reader = new BufferedReader(new FileReader(file)); String read_all = ""; while ((read_all = buffered_reader.readLine()) != null) {

                    {

                        if (read_all.startsWith("Block ")) {

                            String id = read_all.substring(read_all.indexOf(" = ") + 3).replace(" keep", "");

                            if (id.equals("") == false) {

                                if (Misc.textToBlock(id).getBlock() == Blocks.AIR) {

                                    incompatible = true;
                                    break;

                                }

                            }

                        }

                    }

                } buffered_reader.close(); } catch (Exception e) { e.printStackTrace(); }

            }

        } else {

            incompatible = true;

        }

        rename(source.toFile(), incompatible);

    }

    private static void rename (File file, boolean incompatible) {

        if (incompatible == true) {

            if (file.getName().startsWith("[INCOMPATIBLE]") == false) {

                file.renameTo(new File(file.getParentFile().toPath() + "/[INCOMPATIBLE] " + file.getName()));

            }

        } else {

            file.renameTo(new File(file.getParentFile().toPath() + "/" + file.getName().replace("[INCOMPATIBLE] ", "")));

        }

    }

}