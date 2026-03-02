package tannyjung.tanshugetrees_handcode.systems.tree_generator;

import net.minecraft.server.level.ServerLevel;
import tannyjung.tanshugetrees_core.Core;
import tannyjung.tanshugetrees_core.game.GameUtils;
import tannyjung.tanshugetrees_core.outside.FileManager;
import tannyjung.tanshugetrees_core.outside.OutsideUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class PresetFixer {

    public static void start (ServerLevel level_server) {

        File template = new File(Core.path_config + "/#dev/#temporary/preset_template.txt");

        if (template.exists() == true && template.isDirectory() == false) {

            GameUtils.Misc.sendChatMessage(level_server, "@a", "Start fixing all tree presets from all extracted packs... / gray");
            boolean fix_at_least_one = false;
            File[] packs = new File(Core.path_config + "/custom_packs").listFiles();

            if (packs != null) {

                File[] presets = new File[0];

                for (File pack : packs) {

                    if (pack.getName().endsWith(".zip") == false) {

                        presets = new File(pack.getPath() + "/presets").listFiles();

                        if (presets != null) {

                            for (File preset : presets) {

                                preset = new File(preset.getPath() + "/" + preset.getName() + ".txt");

                                if (preset.exists() == true && preset.isDirectory() == false) {

                                    if (fix(level_server, template, preset) == true) {

                                        fix_at_least_one = true;

                                    }

                                }

                            }

                        }

                    }

                }

            }

            if (fix_at_least_one == true) {

                GameUtils.Misc.sendChatMessage(level_server, "@a", "Completed! / gray");

            } else {

                GameUtils.Misc.sendChatMessage(level_server, "@a", "There's nothing changed / gray");

            }

        } else {

            GameUtils.Misc.sendChatMessage(level_server, "@a", "Template not found / red");

        }

    }

    private static boolean fix (ServerLevel level_server, File template, File file) {

        boolean fix_at_least_one = false;

        String id = Path.of(Core.path_config + "/custom_packs").relativize(file.toPath()).toString().replace("\\", "/").replace("/presets/", "/");
        Map<String, String> data_unlock = new HashMap<>();
        Map<String, String> data_lock = new HashMap<>();
        int index = 0;

        // Read Old File
        {

            try { BufferedReader buffered_reader = new BufferedReader(new FileReader(file), 65536); String read_all = ""; while ((read_all = buffered_reader.readLine()) != null) {

                {

                    if (read_all.isEmpty() == false && read_all.startsWith("-") == false) {

                        index = read_all.indexOf(" = ");

                        if (index > 0) {

                            if (read_all.startsWith("    ") == true) {

                                data_lock.put(read_all.substring("    ".length(), index), read_all);

                            } else {

                                data_unlock.put(read_all.substring(0, index), read_all);

                            }

                        }

                    }

                }

            } buffered_reader.close(); } catch (Exception exception) { OutsideUtils.exception(new Exception(), exception, ""); }

        }

        StringBuilder write = new StringBuilder();
        String name = "";
        String value_old = "";
        String value_new = "";

        // Update
        {

            try { BufferedReader buffered_reader = new BufferedReader(new FileReader(template), 65536); String read_all = ""; while ((read_all = buffered_reader.readLine()) != null) {

                {

                    if (read_all.isEmpty() == false && read_all.startsWith("-") == false) {

                        index = read_all.indexOf(" = ");

                        if (index > 0) {

                            name = read_all.substring(0, index);

                            if (data_lock.containsKey(name) == true) {

                                // Lock
                                {

                                    write.append(data_lock.get(name)).append("\n");

                                }

                            } else if (data_unlock.containsKey(name) == true) {

                                // Update
                                {

                                    if (data_unlock.get(name).equals(read_all) == false) {

                                        fix_at_least_one = true;
                                        value_old = data_unlock.get(name).substring(index + " = ".length()).replace("\"", "\\\"");
                                        value_new = read_all.substring(index + " = ".length()).replace("\"", "\\\"");
                                        GameUtils.Misc.sendChatMessage(level_server, "@a", "Updated " + id + " > " + name + " > " + value_old + " > " + value_new + " / dark_gray");

                                    }

                                    write.append(read_all).append("\n");

                                }

                            } else {

                                // Missing
                                {

                                    fix_at_least_one = true;
                                    write.append(read_all).append("\n");
                                    GameUtils.Misc.sendChatMessage(level_server, "@a", "Added " + id + " > " + name + " / dark_gray");

                                }

                            }

                        }

                    } else {

                        write.append(read_all).append("\n");

                    }

                }

            } buffered_reader.close(); } catch (Exception exception) { OutsideUtils.exception(new Exception(), exception, ""); }

        }

        FileManager.writeTXT(file.getPath(), write.toString(), false);
        return fix_at_least_one;

    }

}
