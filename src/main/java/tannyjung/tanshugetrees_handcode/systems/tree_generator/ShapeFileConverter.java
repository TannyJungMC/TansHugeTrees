package tannyjung.tanshugetrees_handcode.systems.tree_generator;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LevelAccessor;
import tannyjung.core.FileManager;
import tannyjung.core.GameUtils;
import tannyjung.core.OutsideUtils;
import tannyjung.tanshugetrees.network.TanshugetreesModVariables;
import tannyjung.tanshugetrees_handcode.Handcode;

import java.io.*;
import java.util.*;

public class ShapeFileConverter {

    public static LinkedHashMap<String, String> export_data = new LinkedHashMap<>();

    public static void start (LevelAccessor level_accessor, Entity entity, int count) {

        if (level_accessor instanceof ServerLevel level_server) {

            if (TanshugetreesModVariables.MapVariables.get(level_accessor).shape_file_converter == false) {

                GameUtils.misc.sendChatMessage(level_server, "@a", "gray", "THT : Turned ON");

            }

            TanshugetreesModVariables.MapVariables.get(level_accessor).shape_file_converter_count = count;
            GameUtils.misc.sendChatMessage(level_server, "@a", "gray", "THT : Set loop to " + (int) TanshugetreesModVariables.MapVariables.get(level_accessor).shape_file_converter_count);

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
                    GameUtils.misc.sendChatMessage(level_server, "@a", "gray", "THT : Will turn OFF after this one");

                } else {

                    GameUtils.misc.sendChatMessage(level_server, "@a", "gray", "THT : Turned OFF");
                    TanshugetreesModVariables.MapVariables.get(level_accessor).shape_file_converter = false;

                }

            } else {

                GameUtils.misc.sendChatMessage(level_server, "@a", "gray", "THT : Already Turned OFF");

            }

        }

    }

    private static void summon (LevelAccessor level_accessor, ServerLevel level_server) {

        String file_location = "";

        // Get data
        {

            File file = new File(Handcode.directory_config + "/#dev/shape_file_converter/#shape_file_converter.txt");

            if (file.exists() == true && file.isDirectory() == false) {

                {

                    try { BufferedReader buffered_reader = new BufferedReader(new FileReader(file), 65536); String read_all = ""; while ((read_all = buffered_reader.readLine()) != null) {

                        {

                            if (read_all.startsWith("file_location = ") == true) {

                                file_location = read_all.replace("file_location = ", "");

                            }

                        }

                    } buffered_reader.close(); } catch (Exception exception) { OutsideUtils.exception(new Exception(), exception); }

                }

            }

        }

        File file = new File(Handcode.directory_config + "/custom_packs/" + file_location);

        if (file.exists() == true && file.isDirectory() == false) {

            TanshugetreesModVariables.MapVariables.get(level_accessor).shape_file_converter_count = TanshugetreesModVariables.MapVariables.get(level_accessor).shape_file_converter_count - 1;

            // Summon
            {

                GameUtils.command.run(level_server, 0, 0, 0, "execute at @p positioned ~ 1000 ~ run " + GameUtils.entity.summonCommand("marker", "TANSHUGETREES / TANSHUGETREES-tree_generator", "Tree Generator", GameUtils.outside.getForgeDataFromGiveFile(file.getPath())));
                String data_modify = "debug_mode:false,tree_generator_speed_global:false,tree_generator_speed_tick:1,tree_generator_speed_repeat:0";
                GameUtils.command.run(level_server, 0, 0, 0, "execute at @p positioned ~ 1000 ~ run data merge entity @e[tag=TANSHUGETREES-tree_generator,distance=..1,limit=1,sort=nearest] {ForgeData:{" + data_modify + "}}");

            }

        } else {

            GameUtils.misc.sendChatMessage(level_server, "@a", "red", "THT : Can't start shape file converter because the file location you wrote cannot be found");
            TanshugetreesModVariables.MapVariables.get(level_accessor).shape_file_converter_count = 0;
            stop(level_accessor);

        }

    }

    public static void whenTreeStart (ServerLevel level_server, Entity entity) {

        String name = GameUtils.nbt.entity.getText(entity, "name").toLowerCase();
        String time = new java.text.SimpleDateFormat("yyyyMMdd-HHmm-ss").format(Calendar.getInstance().getTime());
        GameUtils.nbt.entity.setText(entity, "export_file_name", name + "_" + time + ".bin");
        GameUtils.command.run(level_server, 0, 0, 0, "tellraw @a [\"\",{\"text\":\"THT : Generating \",\"color\":\"aqua\"},{\"text\":\"" + GameUtils.nbt.entity.getText(entity, "export_file_name").replace(" (generating)", "") + "\",\"color\":\"white\"}]");

        // Write Settings
        {

            StringBuilder write = new StringBuilder();

            {

                // Settings
                {

                    write
                            .append("tree_type = ").append(GameUtils.nbt.entity.getText(entity, "tree_type")).append("\n")
                            .append("start_height = ").append((int) GameUtils.nbt.entity.getNumber(entity, "start_height")).append("\n")
                            .append("can_disable_roots = ").append(GameUtils.nbt.entity.getLogic(entity, "can_disable_roots")).append("\n")
                            .append("can_leaves_decay = ").append(GameUtils.nbt.entity.getLogic(entity, "can_leaves_decay")).append("\n")
                            .append("can_leaves_drop = ").append(GameUtils.nbt.entity.getLogic(entity, "can_leaves_drop")).append("\n")
                            .append("can_leaves_regrow = ").append(GameUtils.nbt.entity.getLogic(entity, "can_leaves_regrow")).append("\n")
                    ;

                }

                write.append("\n");

                // Blocks
                {

                    write.append(settingsWriteBlock(entity, "taproot"));
                    write.append(settingsWriteBlock(entity, "secondary_root"));
                    write.append(settingsWriteBlock(entity, "tertiary_root"));
                    write.append(settingsWriteBlock(entity, "fine_root"));
                    write.append(settingsWriteBlock(entity, "trunk"));
                    write.append(settingsWriteBlock(entity, "bough"));
                    write.append(settingsWriteBlock(entity, "branch"));
                    write.append(settingsWriteBlock(entity, "limb"));
                    write.append(settingsWriteBlock(entity, "twig"));
                    write.append(settingsWriteBlock(entity, "sprig"));
                    write.append(settingsWriteBlock(entity, "leaves"));

                }

                write.append("\n");

                // Functions
                {

                    write
                            .append("Function fs = ").append(GameUtils.nbt.entity.getText(entity, "function_start")).append("\n")
                            .append("Function fe = ").append(GameUtils.nbt.entity.getText(entity, "function_end")).append("\n")
                            .append("Function f1 = ").append(GameUtils.nbt.entity.getText(entity, "function_way1")).append("\n")
                            .append("Function f2 = ").append(GameUtils.nbt.entity.getText(entity, "function_way2")).append("\n")
                            .append("Function f3 = ").append(GameUtils.nbt.entity.getText(entity, "function_way3")).append("\n")
                    ;

                }

            }

            FileManager.writeTXT(Handcode.directory_config + "/#dev/shape_file_converter/" + name + "_settings.txt", write.toString(), false);

        }

    }

    private static String settingsWriteBlock (Entity entity, String type) {

        String retuen_text = "";
        String keep = "";

        if (GameUtils.nbt.entity.getLogic(entity, type + "_replace") == false) {

            keep = " keep";

        }

        if (type.equals("leaves") == false) {

            // General Blocks
            {

                String type_short = type.substring(0, 2);
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

                retuen_text = "Block " + type_short + "o = " + outer + "\n" + "Block " + type_short + "i = " + inner + "\n" + "Block " + type_short + "c = " + core + "\n";

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

                retuen_text = "Block le1 = " + leaves1 + "\n" + "Block le2 = " + leaves2 + "\n";

            }

        }

        return retuen_text;

    }

    public static void whenTreeEnd (LevelAccessor level_accessor, ServerLevel level_server, Entity entity) {

        List<Short> short_converted = new ArrayList<>();

        {

            // Start Function
            short_converted.add((short) 21000);
            short_converted.add((short) 0);
            short_converted.add((short) 0);
            short_converted.add((short) 0);

            // Blocks and Way Functions
            {

                String[] pos = new String[0];
                int type = 0;

                for (Map.Entry<String, String> entry : ShapeFileConverter.export_data.entrySet()) {

                    if (entry.getKey().startsWith("B") == true) {

                        {

                            if (entry.getValue().startsWith("le") == true) {

                                type = 1200;

                            } else {

                                if (entry.getValue().startsWith("ta") == true) {

                                    type = 1100;

                                } else if (entry.getValue().startsWith("se") == true) {

                                    type = 1110;

                                } else if (entry.getValue().startsWith("tr") == true) {

                                    type = 1120;

                                } else if (entry.getValue().startsWith("fi") == true) {

                                    type = 1130;

                                } else if (entry.getValue().startsWith("tr") == true) {

                                    type = 1140;

                                } else if (entry.getValue().startsWith("bo") == true) {

                                    type = 1150;

                                } else if (entry.getValue().startsWith("br") == true) {

                                    type = 1160;

                                } else if (entry.getValue().startsWith("li") == true) {

                                    type = 1170;

                                } else if (entry.getValue().startsWith("tw") == true) {

                                    type = 1180;

                                } else if (entry.getValue().startsWith("sp") == true) {

                                    type = 1190;

                                }

                            }

                            if (entry.getValue().endsWith("o") == true) {

                                type = type + 1;

                            } else if (entry.getValue().endsWith("i") == true) {

                                type = type + 2;

                            } else if (entry.getValue().endsWith("c") == true) {

                                type = type + 3;

                            }

                        }

                    }

                    short_converted.add((short) type);
                    pos = entry.getKey().substring(1).split("/");
                    short_converted.add(Short.parseShort(pos[0]));
                    short_converted.add(Short.parseShort(pos[1]));
                    short_converted.add(Short.parseShort(pos[2]));

                }

            }

            // End Function
            short_converted.add((short) 22000);
            short_converted.add((short) 0);
            short_converted.add((short) 0);
            short_converted.add((short) 0);

        }

        FileManager.writeBIN(Handcode.directory_config + "/#dev/shape_file_converter/" + GameUtils.nbt.entity.getText(entity, "export_file_name"), OutsideUtils.shortListToArray(short_converted), true);
        export_data.clear();










        /*

        // Update Generated File
        {

            StringBuilder data = new StringBuilder();

            int min_sizeX = 0;
            int min_sizeY = 0;
            int min_sizeZ = 0;
            int max_sizeX = 0;
            int max_sizeY = 0;
            int max_sizeZ = 0;
            int sizeX = 0;
            int sizeY = 0;
            int sizeZ = 0;
            int center_sizeX = 0;
            int center_sizeY = 0;
            int center_sizeZ = 0;

            int block_count_trunk = 0;
            int block_count_bough = 0;
            int block_count_branch = 0;
            int block_count_limb = 0;
            int block_count_twig = 0;
            int block_count_sprig = 0;

            // Scanning
            {

                boolean start = false;
                String type_short = "";
                String[] pos = new String[0];
                int posX = 0;
                int posY = 0;
                int posZ = 0;

                {

                    try {

                        DataOutputStream file_bin = new DataOutputStream(new FileOutputStream(Handcode.directory_config + "/#dev/shape_file_converter/test.bin", false));

                        for (String read_all : FileManager.readTXT(file.getPath())) {

                            {

                                if (start == false) {

                                    if (read_all.startsWith("---") == true) {

                                        start = true;

                                    }

                                } else {

                                    data.append(read_all).append("\n");

                                    if (read_all.startsWith("+b") == true) {

                                        // Size
                                        {

                                            pos = read_all.substring(2, read_all.length() - 3).split("/");
                                            posX = Integer.parseInt(pos[0]);
                                            posY = Integer.parseInt(pos[1]);
                                            posZ = Integer.parseInt(pos[2]);

                                            // Get Min and Max
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

                                        }

                                        // Block Count
                                        {

                                            type_short = read_all.substring(read_all.length() - 3);

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








                                        String[] get = read_all.substring(2, read_all.length() - 3).split("/");
                                        int x = Integer.parseInt(get[0]);
                                        int y = Integer.parseInt(get[1]);
                                        int z = Integer.parseInt(get[2]);

                                        file_bin.writeShort(122);
                                        file_bin.writeShort(x);
                                        file_bin.writeShort(y);
                                        file_bin.writeShort(z);

                                    }

                                }

                            }

                        }

                        file_bin.close();

                    } catch (Exception exception) {

                        OutsideUtils.exception(new Exception(), exception);

                    }

                }

            }

            // Size Calculation
            {

                sizeX = max_sizeX - min_sizeX;
                sizeY = max_sizeY - min_sizeY;
                sizeZ = max_sizeZ - min_sizeZ;
                center_sizeX = -(min_sizeX);
                center_sizeY = -(min_sizeY);
                center_sizeZ = -(min_sizeZ);

            }

            String file_new = file.getParentFile().getPath() + "/" + file.getName().replace(" (generating)", " (updating)");

            // Updating
            {

                try { BufferedReader buffered_reader = new BufferedReader(new FileReader(file), 65536); String read_all = ""; while ((read_all = buffered_reader.readLine()) != null) {

                    {

                        if (read_all.startsWith("---") == false) {

                            if (read_all.startsWith("Complete Date : ") == true) {

                                read_all = read_all.replace("###", complete_date);

                            } else if (read_all.startsWith("sizeX = ") == true) {

                                read_all = read_all.replace("###", "" + sizeX);

                            } else if (read_all.startsWith("sizeY = ") == true) {

                                read_all = read_all.replace("###", "" + sizeY);

                            } else if (read_all.startsWith("sizeZ = ") == true) {

                                read_all = read_all.replace("###", "" + sizeZ);

                            } else if (read_all.startsWith("center_sizeX = ") == true) {

                                read_all = read_all.replace("###", "" + center_sizeX);

                            } else if (read_all.startsWith("center_sizeY = ") == true) {

                                read_all = read_all.replace("###", "" + center_sizeY);

                            } else if (read_all.startsWith("center_sizeZ = ") == true) {

                                read_all = read_all.replace("###", "" + center_sizeZ);

                            } else if (read_all.startsWith("block_count_trunk = ") == true) {

                                read_all = read_all.replace("###", "" + block_count_trunk);

                            } else if (read_all.startsWith("block_count_bough = ") == true) {

                                read_all = read_all.replace("###", "" + block_count_bough);

                            } else if (read_all.startsWith("block_count_branch = ") == true) {

                                read_all = read_all.replace("###", "" + block_count_branch);

                            } else if (read_all.startsWith("block_count_limb = ") == true) {

                                read_all = read_all.replace("###", "" + block_count_limb);

                            } else if (read_all.startsWith("block_count_twig = ") == true) {

                                read_all = read_all.replace("###", "" + block_count_twig);

                            } else if (read_all.startsWith("block_count_sprig = ") == true) {

                                read_all = read_all.replace("###", "" + block_count_sprig);

                            }

                            FileManager.writeTXT(file_new, read_all + "\n", true);

                        } else {

                            FileManager.writeTXT(file_new, read_all + "\n" + "\n", true);
                            break;

                        }

                    }

                } buffered_reader.close(); } catch (Exception exception) { OutsideUtils.exception(new Exception(), exception); }

            }

            FileManager.writeTXT(file_new, data.toString(), true);
            file.delete();
            new File(file_new).renameTo(new File(file.getParentFile().getPath() + "/" + file.getName().replace(" (generating)", "")));

        }

         */








        GameUtils.misc.sendChatMessage(level_server, "@a", "green", "THT : Completed!");

        if (TanshugetreesModVariables.MapVariables.get(level_accessor).shape_file_converter_count > 0) {

            GameUtils.misc.sendChatMessage(level_server, "@a", "gray", "THT : Loop left " + (int) TanshugetreesModVariables.MapVariables.get(level_accessor).shape_file_converter_count);
            summon(level_accessor, level_server);

        } else {

                stop(level_accessor);

        }

    }

}
