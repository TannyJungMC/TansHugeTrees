package tannyjung.tanshugetrees_handcode.systems.tree_generator;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LevelAccessor;
import tannyjung.tanshugetrees_core.Core;
import tannyjung.tanshugetrees_core.outside.FileManager;
import tannyjung.tanshugetrees_core.outside.OutsideUtils;
import tannyjung.tanshugetrees_core.game.GameUtils;
import tannyjung.tanshugetrees.network.TanshugetreesModVariables;

import java.io.*;
import java.util.*;

public class ShapeFileConverter {

    public static LinkedHashMap<String, String> export_data = new LinkedHashMap<>();

    public static void start (LevelAccessor level_accessor, int count) {

        if (level_accessor instanceof ServerLevel level_server) {

            if (TanshugetreesModVariables.MapVariables.get(level_accessor).shape_file_converter == false) {

                GameUtils.misc.sendChatMessage(level_server, null, "@a", "gray", Core.mod_id_short + " : Turned ON");

            }

            TanshugetreesModVariables.MapVariables.get(level_accessor).shape_file_converter_count = count;
            GameUtils.misc.sendChatMessage(level_server, null, "@a", "gray", Core.mod_id_short + " : Set loop to " + (int) TanshugetreesModVariables.MapVariables.get(level_accessor).shape_file_converter_count);

            if (TanshugetreesModVariables.MapVariables.get(level_accessor).shape_file_converter == false) {

                TanshugetreesModVariables.MapVariables.get(level_accessor).shape_file_converter = true;
                export_data.clear();
                summon(level_accessor, level_server);

            }

        }

    }

    public static void stop (LevelAccessor level_accessor) {

        if (level_accessor instanceof ServerLevel level_server) {

            if (TanshugetreesModVariables.MapVariables.get(level_accessor).shape_file_converter == true) {

                if (TanshugetreesModVariables.MapVariables.get(level_accessor).shape_file_converter_count > 0) {

                    TanshugetreesModVariables.MapVariables.get(level_accessor).shape_file_converter_count = 0;
                    GameUtils.misc.sendChatMessage(level_server, null, "@a", "gray", Core.mod_id_short + " : Will turn OFF after this one");

                } else {

                    GameUtils.misc.sendChatMessage(level_server, null, "@a", "gray", Core.mod_id_short + " : Turned OFF");
                    TanshugetreesModVariables.MapVariables.get(level_accessor).shape_file_converter = false;

                }

            } else {

                GameUtils.misc.sendChatMessage(level_server, null, "@a", "gray", Core.mod_id_short + " : Already Turned OFF");

            }

        }

    }

    private static void summon (LevelAccessor level_accessor, ServerLevel level_server) {

        String[] file_location = new String[0];

        // Get data
        {

            File file = new File(Core.path_config + "/#dev/shape_file_converter/#shape_file_converter.txt");

            if (file.exists() == true && file.isDirectory() == false) {

                {

                    try { BufferedReader buffered_reader = new BufferedReader(new FileReader(file), 65536); String read_all = ""; while ((read_all = buffered_reader.readLine()) != null) {

                        {

                            if (read_all.startsWith("file_location = ") == true) {

                                file_location = read_all.substring("file_location = ".length()).split("/");

                            }

                        }

                    } buffered_reader.close(); } catch (Exception exception) { OutsideUtils.exception(new Exception(), exception, ""); }

                }

            }

        }

        File file = new File(Core.path_config + "/#dev/temporary/presets/" + file_location[0] + "/" + file_location[1] + "/" + file_location[1] + ".txt");

        if (file.exists() == true && file.isDirectory() == false) {

            TanshugetreesModVariables.MapVariables.get(level_accessor).shape_file_converter_count = TanshugetreesModVariables.MapVariables.get(level_accessor).shape_file_converter_count - 1;

            // Summon
            {

                GameUtils.command.run(false, level_server, 0, 0, 0, "execute at @p positioned ~ 1000 ~ run " + GameUtils.command.summonEntity("marker", "TANSHUGETREES / TANSHUGETREES-tree_generator", "Tree Generator", GameUtils.misc.getForgeDataFromFile(file.getPath())));
                String data_modify = "debug_mode:false,tree_generator_speed_global:false,tree_generator_speed_tick:1,tree_generator_speed_repeat:0,name:\"" + file_location[1] + "\"";
                GameUtils.command.run(false, level_server, 0, 0, 0, "execute at @p positioned ~ 1000 ~ run data merge entity @e[tag=TANSHUGETREES-tree_generator,distance=..1,limit=1,sort=nearest] {ForgeData:{" + data_modify + "}}");

            }

        } else {

            GameUtils.misc.sendChatMessage(level_server, null, "@a", "red", Core.mod_id_short + " : Can't start shape file converter because the file location you wrote cannot be found");
            TanshugetreesModVariables.MapVariables.get(level_accessor).shape_file_converter_count = 0;
            stop(level_accessor);

        }

    }

    public static void whenTreeStart (ServerLevel level_server, Entity entity) {

        String name = GameUtils.nbt.entity.getText(entity, "name");
        String time = new java.text.SimpleDateFormat("yyyyMMdd-HHmm-ss-SSS").format(Calendar.getInstance().getTime());
        GameUtils.nbt.entity.setText(entity, "export_file_name", name + "_" + time + ".bin");
        GameUtils.command.run(false, level_server, 0, 0, 0, "tellraw @a [\"\",{\"text\":\"" + Core.mod_id_short + " : Generating \",\"color\":\"aqua\"},{\"text\":\"" + GameUtils.nbt.entity.getText(entity, "export_file_name").replace(" (generating)", "") + "\",\"color\":\"white\"}]");

        // Write Settings
        {

            StringBuilder write = new StringBuilder();

            {

                write
                        .append("tree_type = ").append(GameUtils.nbt.entity.getText(entity, "tree_type")).append("\n")
                        .append("start_height = ").append((int) GameUtils.nbt.entity.getNumber(entity, "start_height")).append("\n")
                        .append("can_disable_roots = ").append(GameUtils.nbt.entity.getLogic(entity, "can_disable_roots")).append("\n")
                        .append("can_leaves_decay = ").append(GameUtils.nbt.entity.getLogic(entity, "can_leaves_decay")).append("\n")
                        .append("can_leaves_drop = ").append(GameUtils.nbt.entity.getLogic(entity, "can_leaves_drop")).append("\n")
                        .append("can_leaves_regrow = ").append(GameUtils.nbt.entity.getLogic(entity, "can_leaves_regrow")).append("\n")
                        .append("\n")
                        .append(settingsWriteBlock(entity, 110, "taproot"))
                        .append(settingsWriteBlock(entity, 111, "secondary_root"))
                        .append(settingsWriteBlock(entity, 112, "tertiary_root"))
                        .append(settingsWriteBlock(entity, 113, "fine_root"))
                        .append(settingsWriteBlock(entity, 114, "trunk"))
                        .append(settingsWriteBlock(entity, 115, "bough"))
                        .append(settingsWriteBlock(entity, 116, "branch"))
                        .append(settingsWriteBlock(entity, 117, "limb"))
                        .append(settingsWriteBlock(entity, 118, "twig"))
                        .append(settingsWriteBlock(entity, 119, "sprig"))
                        .append(settingsWriteBlock(entity, 120, "leaves"))
                        .append("\n")
                        .append("Function fs 210 = ").append(GameUtils.nbt.entity.getText(entity, "function_start")).append("\n")
                        .append("Function fe 220 = ").append(GameUtils.nbt.entity.getText(entity, "function_end")).append("\n")
                        .append("Function f1 201 = ").append(GameUtils.nbt.entity.getText(entity, "function_way1")).append("\n")
                        .append("Function f2 202 = ").append(GameUtils.nbt.entity.getText(entity, "function_way2")).append("\n")
                        .append("Function f3 203 = ").append(GameUtils.nbt.entity.getText(entity, "function_way3")).append("\n")
                        .append("Function f4 204 = ").append(GameUtils.nbt.entity.getText(entity, "function_way4")).append("\n")
                        .append("Function f5 205 = ").append(GameUtils.nbt.entity.getText(entity, "function_way5")).append("\n")
                        .append("Function f5 206 = ").append(GameUtils.nbt.entity.getText(entity, "function_way6")).append("\n")
                        .append("Function f5 207 = ").append(GameUtils.nbt.entity.getText(entity, "function_way7")).append("\n")
                        .append("Function f5 208 = ").append(GameUtils.nbt.entity.getText(entity, "function_way8")).append("\n")
                        .append("Function f5 209 = ").append(GameUtils.nbt.entity.getText(entity, "function_way9")).append("\n")
                ;

            }

            FileManager.writeTXT(Core.path_config + "/#dev/shape_file_converter/" + name + "/" + name + "_settings.txt", write.toString(), false);

        }

    }

    private static String settingsWriteBlock (Entity entity, int id, String type) {

        String retuen_text = type.substring(0, 2);
        String keep = "";

        if (GameUtils.nbt.entity.getLogic(entity, type + "_replace") == false) {

            keep = " keep";

        }

        if (type.equals("leaves") == false) {

            // General Blocks
            {

                String outer = GameUtils.nbt.entity.getText(entity, type + "_outer") + keep;
                String inner = GameUtils.nbt.entity.getText(entity, type + "_inner") + keep;
                String core = GameUtils.nbt.entity.getText(entity, type + "_core") + keep;

                if (outer.equals(keep) == true) {

                    outer = "";

                }

                if (inner.equals(keep) == true) {

                    inner = "";

                }

                if (core.equals(keep) == true) {

                    core = "";

                }

                outer = "Block " + retuen_text + "o " + id + "1 = " + outer + "\n";
                inner = "Block " + retuen_text + "i " + id + "2 = " + inner + "\n";
                core = "Block " + retuen_text + "c " + id + "3 = " + core + "\n";
                retuen_text = outer + inner + core;

            }

        } else {

            // Leaves
            {

                String leaves1 = GameUtils.nbt.entity.getText(entity, "leaves1") + keep;
                String leaves2 = GameUtils.nbt.entity.getText(entity, "leaves2") + keep;

                if (leaves1.equals(keep) == true) {

                    leaves1 = "";

                }

                if (leaves2.equals(keep) == true) {

                    leaves2 = "";

                }

                leaves1 = "Block " + retuen_text + "1 " + id + "1 = " + leaves1 + "\n";
                leaves2 = "Block " + retuen_text + "2 " + id + "2 = " + leaves2 + "\n";
                retuen_text = leaves1 + leaves2;

            }

        }

        return retuen_text;

    }

    public static void whenTreeEnd (LevelAccessor level_accessor, ServerLevel level_server, Entity entity) {

        List<String> data = new ArrayList<>();
        int min_sizeX = 0;
        int min_sizeY = 0;
        int min_sizeZ = 0;
        int max_sizeX = 0;
        int max_sizeY = 0;
        int max_sizeZ = 0;
        int block_count_trunk = 0;
        int block_count_bough = 0;
        int block_count_branch = 0;
        int block_count_limb = 0;
        int block_count_twig = 0;
        int block_count_sprig = 0;

        // Scan and Convert
        {

            // Start Function
            data.add("s210");
            data.add("s0");
            data.add("s0");
            data.add("s0");

            // Blocks and Way Functions
            {

                String[] pos = new String[0];
                int posX = 0;
                int posY = 0;
                int posZ = 0;
                String type_short = "";
                int type = 0;

                for (Map.Entry<String, String> entry : export_data.entrySet()) {

                    pos = entry.getKey().substring(1).split("/");
                    posX = Integer.parseInt(pos[0]) - entity.getBlockX();
                    posY = Integer.parseInt(pos[1]) - entity.getBlockY();
                    posZ = Integer.parseInt(pos[2]) - entity.getBlockZ();
                    type_short = entry.getValue();

                    if (entry.getKey().startsWith("B") == true) {

                        // Blocks
                        {

                            if (type_short.startsWith("le") == true) {

                                type = 1200;

                                if (type_short.endsWith("1") == true) {

                                    type = type + 1;

                                } else if (type_short.endsWith("2") == true) {

                                    type = type + 2;

                                }

                            } else {

                                if (type_short.startsWith("ta") == true) {

                                    type = 1100;

                                } else if (type_short.startsWith("se") == true) {

                                    type = 1110;

                                } else if (type_short.startsWith("te") == true) {

                                    type = 1120;

                                } else if (type_short.startsWith("fi") == true) {

                                    type = 1130;

                                } else if (type_short.startsWith("tr") == true) {

                                    type = 1140;

                                } else if (type_short.startsWith("bo") == true) {

                                    type = 1150;

                                } else if (type_short.startsWith("br") == true) {

                                    type = 1160;

                                } else if (type_short.startsWith("li") == true) {

                                    type = 1170;

                                } else if (type_short.startsWith("tw") == true) {

                                    type = 1180;

                                } else if (type_short.startsWith("sp") == true) {

                                    type = 1190;

                                }

                                if (type_short.endsWith("o") == true) {

                                    type = type + 1;

                                } else if (type_short.endsWith("i") == true) {

                                    type = type + 2;

                                } else if (type_short.endsWith("c") == true) {

                                    type = type + 3;

                                }

                            }

                        }

                        // Get Size
                        {

                            if (min_sizeX > posX) {

                                min_sizeX = posX;

                            }

                            if (min_sizeY > posY) {

                                min_sizeY = posY;

                            }

                            if (min_sizeZ > posZ) {

                                min_sizeZ = posZ;

                            }

                            if (max_sizeX < posX) {

                                max_sizeX = posX;

                            }

                            if (max_sizeY < posY) {

                                max_sizeY = posY;

                            }

                            if (max_sizeZ < posZ) {

                                max_sizeZ = posZ;

                            }

                        }

                        // Get Block Count
                        {

                            if (type_short.startsWith("tr") == true) {

                                block_count_trunk = block_count_trunk + 1;

                            } else if (type_short.startsWith("bo") == true) {

                                block_count_bough = block_count_bough + 1;

                            } else if (type_short.startsWith("br") == true) {

                                block_count_branch = block_count_branch + 1;

                            } else if (type_short.startsWith("li") == true) {

                                block_count_limb = block_count_limb + 1;

                            } else if (type_short.startsWith("tw") == true) {

                                block_count_twig = block_count_twig + 1;

                            } else if (type_short.startsWith("sp") == true) {

                                block_count_sprig = block_count_sprig + 1;

                            }

                        }

                    } else if (entry.getKey().startsWith("F") == true) {

                        type = 200 + Integer.parseInt(type_short.substring(1));

                    }

                    data.add("s" + type);
                    data.add("s" + posX);
                    data.add("s" + posY);
                    data.add("s" + posZ);

                }

            }

            // End Function
            data.add("s220");
            data.add("s0");
            data.add("s0");
            data.add("s0");

        }

        List<String> start_data = new ArrayList<>();

        // Start Data
        {

            start_data.add("s" + (max_sizeX - min_sizeX));
            start_data.add("s" + (max_sizeY - min_sizeY));
            start_data.add("s" + (max_sizeZ - min_sizeZ));
            start_data.add("s" + -(min_sizeX));
            start_data.add("s" + -(min_sizeY));
            start_data.add("s" + -(min_sizeZ));
            start_data.add("i" + block_count_trunk);
            start_data.add("i" + block_count_bough);
            start_data.add("i" + block_count_branch);
            start_data.add("i" + block_count_limb);
            start_data.add("i" + block_count_twig);
            start_data.add("i" + block_count_sprig);

        }

        String path = Core.path_config + "/#dev/shape_file_converter/" + GameUtils.nbt.entity.getText(entity, "name") + "/storage/" + GameUtils.nbt.entity.getText(entity, "export_file_name");
        FileManager.writeBIN(path, start_data, false);
        FileManager.writeBIN(path, data, true);
        GameUtils.misc.sendChatMessage(level_server, null, "@a", "green", Core.mod_id_short + " : Completed!");
        export_data.clear();

        Core.delayed_works.create(true, 1, () -> {

            if (TanshugetreesModVariables.MapVariables.get(level_accessor).shape_file_converter_count > 0) {

                GameUtils.misc.sendChatMessage(level_server, null, "@a", "gray", Core.mod_id_short + " : Loop left " + (int) TanshugetreesModVariables.MapVariables.get(level_accessor).shape_file_converter_count);
                summon(level_accessor, level_server);

            } else {

                stop(level_accessor);

            }

        });

    }

}
