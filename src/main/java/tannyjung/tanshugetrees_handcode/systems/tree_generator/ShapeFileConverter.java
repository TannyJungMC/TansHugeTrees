package tannyjung.tanshugetrees_handcode.systems.tree_generator;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.LevelAccessor;
import tannyjung.tanshugetrees_core.Core;
import tannyjung.tanshugetrees_core.outside.FileManager;
import tannyjung.tanshugetrees_core.game.GameUtils;
import tannyjung.tanshugetrees.network.TanshugetreesModVariables;

import java.io.*;
import java.util.*;

public class ShapeFileConverter {

    public static LinkedHashMap<String, String> export_data = new LinkedHashMap<>();

    public static void start (LevelAccessor level_accessor, int count) {

        if (level_accessor instanceof ServerLevel level_server) {

            if (TanshugetreesModVariables.MapVariables.get(level_accessor).shape_file_converter == false) {

                GameUtils.Misc.sendChatMessage(level_server, "Turned ON / gray");

            }

            TanshugetreesModVariables.MapVariables.get(level_accessor).shape_file_converter_count = count;
            GameUtils.Misc.sendChatMessage(level_server, "Set loop to " + (int) TanshugetreesModVariables.MapVariables.get(level_accessor).shape_file_converter_count + " / gray");

            if (TanshugetreesModVariables.MapVariables.get(level_accessor).shape_file_converter == false) {

                TanshugetreesModVariables.MapVariables.get(level_accessor).shape_file_converter = true;
                summon(level_accessor, level_server);

            }

        }

    }

    public static void stop (LevelAccessor level_accessor) {

        if (level_accessor instanceof ServerLevel level_server) {

            if (TanshugetreesModVariables.MapVariables.get(level_accessor).shape_file_converter == true) {

                GameUtils.Misc.sendChatMessage(level_server, "Turned OFF / gray");
                TanshugetreesModVariables.MapVariables.get(level_accessor).shape_file_converter = false;

            } else {

                GameUtils.Misc.sendChatMessage(level_server, "Already Turned OFF / gray");

            }

        }

    }

    private static void summon (LevelAccessor level_accessor, ServerLevel level_server) {

        TanshugetreesModVariables.MapVariables.get(level_accessor).shape_file_converter_count = TanshugetreesModVariables.MapVariables.get(level_accessor).shape_file_converter_count - 1;
        Player player = level_server.getRandomPlayer();

        if (player == null) {

            return;

        }

        String path = "";

        // Get data
        {

            File file = new File(Core.path_config + "/dev/shape_file_converter/settings.txt");

            for (String scan : FileManager.readTXT(file.getPath())) {

                if (scan.startsWith("path_preset = ") == true) {

                    path = scan.substring("path_preset = ".length());
                    break;

                }

            }

        }

        Entity entity_summon = TreeGenerator.create(level_accessor, level_server, null, player.blockPosition().atY(1000), path);

        if (entity_summon == null) {

            GameUtils.Misc.sendChatMessage(level_server, "Can't start shape file converter because the file location you wrote cannot be found / red");
            TanshugetreesModVariables.MapVariables.get(level_accessor).shape_file_converter_count = 0;
            stop(level_accessor);

        }

    }

    public static void whenTreeStart (ServerLevel level_server, Entity entity) {

        GameUtils.Data.setEntityLogic(entity, "tree_generator_speed_global", false);
        GameUtils.Data.setEntityNumber(entity, "tree_generator_speed_tick", 1);
        GameUtils.Data.setEntityNumber(entity, "tree_generator_speed_repeat", 0);

        String name = GameUtils.Data.getEntityText(entity, "name");
        String time = new java.text.SimpleDateFormat("yyyyMMdd-HHmm-ss-SSS").format(Calendar.getInstance().getTime());
        String export_file_name = name + "_" + time + ".bin";
        GameUtils.Data.setEntityText(entity, "export_file_name", export_file_name);

        GameUtils.Misc.sendChatMessage(level_server, "Generating  / aqua | " + export_file_name + "  | [?] / dark_gray / From " + GameUtils.Data.getEntityText(entity, "path_type"));

        // Write Settings
        {

            StringBuilder write = new StringBuilder();

            {

                write
                        .append("type = ").append(GameUtils.Data.getEntityText(entity, "type")).append("\n")
                        .append("start_height = ").append((int) GameUtils.Data.getEntityNumber(entity, "start_height")).append("\n")
                        .append("can_disable_roots = ").append(GameUtils.Data.getEntityLogic(entity, "can_disable_roots")).append("\n")
                        .append("can_leaves_decay = ").append(GameUtils.Data.getEntityLogic(entity, "can_leaves_decay")).append("\n")
                        .append("can_leaves_drop = ").append(GameUtils.Data.getEntityLogic(entity, "can_leaves_drop")).append("\n")
                        .append("can_leaves_regrow = ").append(GameUtils.Data.getEntityLogic(entity, "can_leaves_regrow")).append("\n")
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
                        .append("Function fs 210 = ").append(GameUtils.Data.getEntityText(entity, "function_start")).append("\n")
                        .append("Function fe 220 = ").append(GameUtils.Data.getEntityText(entity, "function_end")).append("\n")
                        .append("Function f1 201 = ").append(GameUtils.Data.getEntityText(entity, "function_way1")).append("\n")
                        .append("Function f2 202 = ").append(GameUtils.Data.getEntityText(entity, "function_way2")).append("\n")
                        .append("Function f3 203 = ").append(GameUtils.Data.getEntityText(entity, "function_way3")).append("\n")
                        .append("Function f4 204 = ").append(GameUtils.Data.getEntityText(entity, "function_way4")).append("\n")
                        .append("Function f5 205 = ").append(GameUtils.Data.getEntityText(entity, "function_way5")).append("\n")
                        .append("Function f5 206 = ").append(GameUtils.Data.getEntityText(entity, "function_way6")).append("\n")
                        .append("Function f5 207 = ").append(GameUtils.Data.getEntityText(entity, "function_way7")).append("\n")
                        .append("Function f5 208 = ").append(GameUtils.Data.getEntityText(entity, "function_way8")).append("\n")
                        .append("Function f5 209 = ").append(GameUtils.Data.getEntityText(entity, "function_way9")).append("\n")
                ;

            }

            FileManager.writeTXT(Core.path_config + "/dev/shape_file_converter/" + name + "/" + name + "_settings.txt", write.toString(), false);

        }

    }

    private static String settingsWriteBlock (Entity entity, int id, String type) {

        String retuen_text = type.substring(0, 2);
        String keep = "";

        if (GameUtils.Data.getEntityLogic(entity, type + "_replace") == false) {

            keep = " keep";

        }

        if (type.equals("leaves") == false) {

            // General Blocks
            {

                String outer = GameUtils.Data.getEntityText(entity, type + "_outer") + keep;
                String inner = GameUtils.Data.getEntityText(entity, type + "_inner") + keep;
                String core = GameUtils.Data.getEntityText(entity, type + "_core") + keep;

                if (outer.equals("none" + keep) == true) {

                    outer = "none";

                }

                if (inner.equals("none" + keep) == true) {

                    inner = "none";

                }

                if (core.equals("none" + keep) == true) {

                    core = "none";

                }

                outer = "Block " + retuen_text + "o " + id + "1 = " + outer + "\n";
                inner = "Block " + retuen_text + "i " + id + "2 = " + inner + "\n";
                core = "Block " + retuen_text + "c " + id + "3 = " + core + "\n";
                retuen_text = outer + inner + core;

            }

        } else {

            // Leaves
            {

                String leaves1 = GameUtils.Data.getEntityText(entity, "leaves1") + keep;
                String leaves2 = GameUtils.Data.getEntityText(entity, "leaves2") + keep;

                if (leaves1.equals("none" + keep) == true) {

                    leaves1 = "none";

                }

                if (leaves2.equals("none" + keep) == true) {

                    leaves2 = "none";

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

                String[] pos = null;
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

                            if (min_sizeX >= posX) {

                                min_sizeX = posX;

                            }

                            if (min_sizeY >= posY) {

                                min_sizeY = posY;

                            }

                            if (min_sizeZ >= posZ) {

                                min_sizeZ = posZ;

                            }

                            if (max_sizeX <= posX) {

                                max_sizeX = posX;

                            }

                            if (max_sizeY <= posY) {

                                max_sizeY = posY;

                            }

                            if (max_sizeZ <= posZ) {

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

        String path = Core.path_config + "/dev/shape_file_converter/" + GameUtils.Data.getEntityText(entity, "name") + "/storage/" + GameUtils.Data.getEntityText(entity, "export_file_name");
        FileManager.writeBIN(path, start_data, false);
        FileManager.writeBIN(path, data, true);
        GameUtils.Misc.sendChatMessage(level_server, "Completed! / green");
        export_data.clear();

        Core.DelayedWork.create(false, 1, () -> {

            if (TanshugetreesModVariables.MapVariables.get(level_accessor).shape_file_converter_count > 0) {

                GameUtils.Misc.sendChatMessage(level_server, "Loop Left " + (int) TanshugetreesModVariables.MapVariables.get(level_accessor).shape_file_converter_count + " / gray");
                summon(level_accessor, level_server);

            } else {

                stop(level_accessor);

            }

        });

    }

}
