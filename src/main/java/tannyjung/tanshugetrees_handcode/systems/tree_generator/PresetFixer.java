package tannyjung.tanshugetrees_handcode.systems.tree_generator;

import tannyjung.core.FileManager;
import tannyjung.core.MiscUtils;
import tannyjung.tanshugetrees.TanshugetreesMod;
import tannyjung.tanshugetrees_handcode.Handcode;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

public class PresetFixer {

    public static void start () {

        File[] list_preset = null;

        for (File pack : new File(Handcode.directory_config + "/custom_packs").listFiles()) {

            if (pack.getName().equals(".organized") == false) {

                list_preset = new File(Handcode.directory_config + "/custom_packs/" + pack.getName() + "/presets").listFiles();

                if (list_preset != null && list_preset.length > 0) {

                    for (File species : list_preset) {

                        for (File preset : species.listFiles()) {

                            if (preset.isDirectory() == false && preset.getName().endsWith("_settings.txt") == false) {

                                fix(preset);

                            }

                        }

                    }

                }

            }

        }

        TanshugetreesMod.LOGGER.info("Fixed all presets in all packs");

    }

    private static void fix (File file) {

        File template = new File(Handcode.directory_config + "/custom_packs/TannyJung-Tree-Pack/.dev/preset_template.txt");

        if (template.exists() == true && template.isDirectory() == false) {

            Map<String, String> data = new HashMap<>();

            // Get Preset Data
            {

                try { BufferedReader buffered_reader = new BufferedReader(new FileReader(file), 65536); String read_all = ""; while ((read_all = buffered_reader.readLine()) != null) {

                    {

                        if (read_all.startsWith(",   ") == true) {

                            data.put(read_all.substring(",   ".length(), read_all.indexOf(":")), read_all);

                        }

                    }

                } buffered_reader.close(); } catch (Exception exception) { MiscUtils.exception(new Exception(), exception); }

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

                } buffered_reader.close(); } catch (Exception exception) { MiscUtils.exception(new Exception(), exception); }

            }

            FileManager.writeTXT(file.getPath(), write.toString(), false);

        }

    }

}
