package tannyjung.tanshugetrees_handcode.systems.tree_generator;

import tannyjung.tanshugetrees_core.FileManager;
import tannyjung.tanshugetrees_core.OutsideUtils;
import tannyjung.tanshugetrees.TanshugetreesMod;
import tannyjung.tanshugetrees_handcode.Handcode;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

public class PresetFixer {

    public static void start () {

        File template = new File(Handcode.path_config + "/#dev/temporary/preset_template.txt");

        if (template.exists() == true && template.isDirectory() == false) {

            File[] packs = new File(Handcode.path_config + "/custom_packs").listFiles();

            if (packs != null) {

                File[] presets = new File[0];

                for (File pack : packs) {

                    presets = new File(pack.getPath() + "/prestes").listFiles();

                    if (presets != null) {

                        for (File preset : presets) {

                            preset = new File(preset.getPath() + "/" + preset.getName() + ".txt");

                            if (preset.exists() == true && preset.isDirectory() == false) {

                                fix(template, preset.getPath());

                            }

                        }

                    }

                }

                TanshugetreesMod.LOGGER.info("Fixed all presets in all packs");

            }

        } else {

            TanshugetreesMod.LOGGER.error("Template not found");

        }

    }

    private static void fix (File template, String path) {

        File file = new File(path);
        Map<String, String> data = new HashMap<>();

        // Get Preset Data
        {

            try { BufferedReader buffered_reader = new BufferedReader(new FileReader(file), 65536); String read_all = ""; while ((read_all = buffered_reader.readLine()) != null) {

                {

                    if (read_all.startsWith(",   ") == true) {

                        data.put(read_all.substring(",   ".length(), read_all.indexOf(":")), read_all);

                    }

                }

            } buffered_reader.close(); } catch (Exception exception) { OutsideUtils.exception(new Exception(), exception); }

        }

        StringBuilder write = new StringBuilder();
        String name = "";

        // Read Template
        {

            try { BufferedReader buffered_reader = new BufferedReader(new FileReader(template), 65536); String read_all = ""; while ((read_all = buffered_reader.readLine()) != null) {

                {

                    if (read_all.startsWith(",") == true) {

                        name = read_all.substring(1, read_all.indexOf(":"));

                        if (data.containsKey(name) == true) {

                            write.append(data.get(name) +  "\n");
                            continue;

                        }

                    }

                    write.append(read_all + "\n");

                }

            } buffered_reader.close(); } catch (Exception exception) { OutsideUtils.exception(new Exception(), exception); }

        }

        FileManager.writeTXT(file.getPath(), write.toString(), false);

    }

}
