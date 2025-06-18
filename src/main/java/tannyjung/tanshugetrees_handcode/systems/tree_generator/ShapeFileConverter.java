package tannyjung.tanshugetrees_handcode.systems.tree_generator;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LevelAccessor;
import tannyjung.misc.FileManager;
import tannyjung.misc.GameUtils;
import tannyjung.misc.MiscUtils;
import tannyjung.tanshugetrees.TanshugetreesMod;
import tannyjung.tanshugetrees.network.TanshugetreesModVariables;
import tannyjung.tanshugetrees_handcode.Handcode;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Calendar;

public class ShapeFileConverter {

    public static void start (LevelAccessor level, Entity entity, int count) {

        if (Handcode.version_1192 == true) {

            GameUtils.misc.sendChatMessage(level, "@a", "red", "THT : Auto gen is not available on 1.19.2");

        } else {

            TanshugetreesModVariables.MapVariables.get(level).auto_gen_count = count;
            GameUtils.misc.sendChatMessage(level, "@a", "gray", "THT : Set loop to " + (int) TanshugetreesModVariables.MapVariables.get(level).auto_gen_count);

            if (TanshugetreesModVariables.MapVariables.get(level).auto_gen == false) {

                TanshugetreesModVariables.MapVariables.get(level).auto_gen = true;
                GameUtils.misc.sendChatMessage(level, "@a", "gray", "THT : Turned ON");
                TanshugetreesModVariables.MapVariables.get(level).auto_gen_teleport_player_back = entity.position().x + " " + entity.position().y + " " + entity.position().z;
                GameUtils.command.run(level, 0, 0, 0, "execute in tanshugetrees:dimension run forceload add -100 -100 100 100");
                GameUtils.command.runEntity(entity, "execute in tanshugetrees:dimension run tp @s 0 300 0");
                GameUtils.command.runEntity(entity, "gamemode spectator");
                GameUtils.command.runEntity(entity, "gamemode creative");
                summon(level);

            }

        }

    }

    public static void stop (LevelAccessor level) {

        TanshugetreesModVariables.MapVariables.get(level).auto_gen_count = 0;

        if (GameUtils.command.result(level, 0, 0, 0, "execute in tanshugetrees:dimension if entity @e[tag=TANSHUGETREES-tree_generator]") == true) {

            GameUtils.misc.sendChatMessage(level, "@a", "gray", "THT : Will turn OFF after this one");

        } else {

            TanshugetreesModVariables.MapVariables.get(level).auto_gen = false;
            GameUtils.misc.sendChatMessage(level, "@a", "gray", "THT : Turned OFF");

            GameUtils.command.run(level, 0, 0, 0, "execute in tanshugetrees:dimension run forceload remove all");
            GameUtils.command.run(level, 0, 0, 0, "execute as @a at @s if dimension tanshugetrees:dimension in minecraft:overworld run tp @s " + TanshugetreesModVariables.MapVariables.get(level).auto_gen_teleport_player_back);

        }

    }

    private static void summon (LevelAccessor level) {

        String file_location = "";
        int generate_speed_tick = 0;
        int generate_speed_repeat = 0;
        int generate_speed_tp = 0;
        int posY = 0;

        // Get data
        {

            File file = new File(Handcode.directory_config + "/generated/.shape_file_converter.txt");

            if (file.exists() == true && file.isDirectory() == false) {

                {

                    try { BufferedReader buffered_reader = new BufferedReader(new FileReader(file)); String read_all = ""; while ((read_all = buffered_reader.readLine()) != null) {

                        {

                            if (read_all.startsWith("file_location = ") == true) {

                                file_location = read_all.replace("file_location = ", "");

                            } else if (read_all.startsWith("generate_speed_tick = ") == true) {

                                generate_speed_tick = Integer.parseInt(read_all.replace("generate_speed_tick = ", ""));

                            } else if (read_all.startsWith("generate_speed_repeat = ") == true) {

                                generate_speed_repeat = Integer.parseInt(read_all.replace("generate_speed_repeat = ", ""));

                            } else if (read_all.startsWith("generate_speed_tp = ") == true) {

                                generate_speed_tp = Integer.parseInt(read_all.replace("generate_speed_tp = ", ""));

                            } else if (read_all.startsWith("posY = ") == true) {

                                posY = Integer.parseInt(read_all.replace("posY = ", ""));

                            }

                        }

                    } buffered_reader.close(); } catch (Exception e) { TanshugetreesMod.LOGGER.error(e.getMessage()); }

                }

            }

        }

        File file = new File(Handcode.directory_config + "/custom_packs/" + file_location);

        if (file.exists() == true && file.isDirectory() == false) {

            TanshugetreesModVariables.MapVariables.get(level).auto_gen_count = TanshugetreesModVariables.MapVariables.get(level).auto_gen_count - 1;

            // Summon
            {

                StringBuilder merge = new StringBuilder();

                // Get Preset
                {

                    try { BufferedReader buffered_reader = new BufferedReader(new FileReader(file)); String read_all = ""; while ((read_all = buffered_reader.readLine()) != null) {

                        {

                            merge.append(read_all);

                        }

                    } buffered_reader.close(); } catch (Exception e) { TanshugetreesMod.LOGGER.error(e.getMessage()); }

                }

                String data = merge.toString();
                data = data.substring(data.indexOf("ForgeData"), data.length() - 2);
                GameUtils.command.run(level, 0, 0, 0, "execute in tanshugetrees:dimension positioned 0 " + posY + " 0 run " + GameUtils.misc.summonEntity("marker", "TANSHUGETREES / TANSHUGETREES-tree_generator", "Tree Generator", data));
                String data_modify = "debug_mode:false,global_generate_speed:false,generate_speed_tick:" + generate_speed_tick + ",generate_speed_repeat:" + generate_speed_repeat + ",generate_speed_tp:" + generate_speed_tp;
                GameUtils.command.run(level, 0, 0, 0, "execute in tanshugetrees:dimension positioned 0 " + posY + " 0 run data merge entity @e[tag=TANSHUGETREES-tree_generator,limit=1,sort=nearest] {ForgeData:{" + data_modify + "}}");

            }

        } else {

            GameUtils.misc.sendChatMessage(level, "@a", "red", "THT : Can't start shape file converter because the file location you wrote cannot be found");

        }

    }

    public static void whenTreeStart (LevelAccessor level, Entity entity) {

        String name = GameUtils.NBT.entity.getText(entity, "name").toLowerCase();
        String time = new java.text.SimpleDateFormat("yyyyMMdd-HHmm-ss").format(Calendar.getInstance().getTime());
        GameUtils.NBT.entity.setText(entity, "export_file_name", name + "_" + time + " (generating).txt");
        GameUtils.command.run(level, 0, 0, 0, "tellraw @a [\"\",{\"text\":\"THT : Generating \",\"color\":\"aqua\"},{\"text\":\"" + GameUtils.NBT.entity.getText(entity, "export_file_name").replace(" (generating)", "") + "\",\"color\":\"white\"}]");

        // Write Settings
        {

            StringBuilder write = new StringBuilder();

            {

                // Settings
                {

                    write
                            .append("tree_type = ").append(GameUtils.NBT.entity.getText(entity, "tree_type")).append("\n")
                            .append("start_height = ").append((int) GameUtils.NBT.entity.getNumber(entity, "start_height")).append("\n")
                            .append("living_tree_mechanics = ").append(GameUtils.NBT.entity.getLogic(entity, "living_tree_mechanics")).append("\n")
                            .append("can_disable_roots = ").append(GameUtils.NBT.entity.getLogic(entity, "can_disable_roots")).append("\n")
                    ;

                }

                write.append("\n");

                // Blocks
                {

                    write
                            .append("Block tao = ").append(GameUtils.NBT.entity.getText(entity, "taproot" + "_outer")).append("\n")
                            .append("Block tai = ").append(GameUtils.NBT.entity.getText(entity, "taproot" + "_inner")).append("\n")
                            .append("Block tac = ").append(GameUtils.NBT.entity.getText(entity, "taproot" + "_core")).append("\n")
                            .append("Block seo = ").append(GameUtils.NBT.entity.getText(entity, "secondary_root" + "_outer")).append("\n")
                            .append("Block sei = ").append(GameUtils.NBT.entity.getText(entity, "secondary_root" + "_inner")).append("\n")
                            .append("Block sec = ").append(GameUtils.NBT.entity.getText(entity, "secondary_root" + "_core")).append("\n")
                            .append("Block teo = ").append(GameUtils.NBT.entity.getText(entity, "tertiary_root" + "_outer")).append("\n")
                            .append("Block tei = ").append(GameUtils.NBT.entity.getText(entity, "tertiary_root" + "_inner")).append("\n")
                            .append("Block tec = ").append(GameUtils.NBT.entity.getText(entity, "tertiary_root" + "_core")).append("\n")
                            .append("Block fio = ").append(GameUtils.NBT.entity.getText(entity, "fine_root" + "_outer")).append("\n")
                            .append("Block fii = ").append(GameUtils.NBT.entity.getText(entity, "fine_root" + "_inner")).append("\n")
                            .append("Block fic = ").append(GameUtils.NBT.entity.getText(entity, "fine_root" + "_core")).append("\n")
                            .append("Block tro = ").append(GameUtils.NBT.entity.getText(entity, "trunk" + "_outer")).append("\n")
                            .append("Block tri = ").append(GameUtils.NBT.entity.getText(entity, "trunk" + "_inner")).append("\n")
                            .append("Block trc = ").append(GameUtils.NBT.entity.getText(entity, "trunk" + "_core")).append("\n")
                            .append("Block boo = ").append(GameUtils.NBT.entity.getText(entity, "bough" + "_outer")).append("\n")
                            .append("Block boi = ").append(GameUtils.NBT.entity.getText(entity, "bough" + "_inner")).append("\n")
                            .append("Block boc = ").append(GameUtils.NBT.entity.getText(entity, "bough" + "_core")).append("\n")
                            .append("Block bro = ").append(GameUtils.NBT.entity.getText(entity, "branch" + "_outer")).append("\n")
                            .append("Block bri = ").append(GameUtils.NBT.entity.getText(entity, "branch" + "_inner")).append("\n")
                            .append("Block brc = ").append(GameUtils.NBT.entity.getText(entity, "branch" + "_core")).append("\n")
                            .append("Block lio = ").append(GameUtils.NBT.entity.getText(entity, "limb" + "_outer")).append("\n")
                            .append("Block lii = ").append(GameUtils.NBT.entity.getText(entity, "limb" + "_inner")).append("\n")
                            .append("Block lic = ").append(GameUtils.NBT.entity.getText(entity, "limb" + "_core")).append("\n")
                            .append("Block two = ").append(GameUtils.NBT.entity.getText(entity, "twig" + "_outer")).append("\n")
                            .append("Block twi = ").append(GameUtils.NBT.entity.getText(entity, "twig" + "_inner")).append("\n")
                            .append("Block twc = ").append(GameUtils.NBT.entity.getText(entity, "twig" + "_core")).append("\n")
                            .append("Block spo = ").append(GameUtils.NBT.entity.getText(entity, "sprig" + "_outer")).append("\n")
                            .append("Block spi = ").append(GameUtils.NBT.entity.getText(entity, "sprig" + "_inner")).append("\n")
                            .append("Block spc = ").append(GameUtils.NBT.entity.getText(entity, "sprig" + "_core")).append("\n")

                            .append("Block le1 = ").append(GameUtils.NBT.entity.getText(entity, "leaves1")).append("\n")
                            .append("Block le2 = ").append(GameUtils.NBT.entity.getText(entity, "leaves2")).append("\n")
                    ;

                }

                write.append("\n");

                // Functions
                {

                    write
                            .append("Function fs = ").append(GameUtils.NBT.entity.getText(entity, "function_start")).append("\n")
                            .append("Function fe = ").append(GameUtils.NBT.entity.getText(entity, "function_end")).append("\n")
                            .append("Function f1 = ").append(GameUtils.NBT.entity.getText(entity, "function_way1")).append("\n")
                            .append("Function f2 = ").append(GameUtils.NBT.entity.getText(entity, "function_way2")).append("\n")
                            .append("Function f3 = ").append(GameUtils.NBT.entity.getText(entity, "function_way3")).append("\n")
                    ;

                }

            }

            FileManager.writeTXT(Handcode.directory_config + "/generated/" + name + "_settings.txt", write.toString(), false);

        }

        // Write Shape File
        {

            StringBuilder write = new StringBuilder();

            {

                write
                        .append("Start Date : ").append(new java.text.SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTime())).append(" at ").append(new java.text.SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime())).append("\n")
                        .append("Complete Date : ###").append("\n")
                        .append("\n")
                        .append("sizeX = ###").append("\n")
                        .append("sizeY = ###").append("\n")
                        .append("sizeZ = ###").append("\n")
                        .append("center_sizeX = ###").append("\n")
                        .append("center_sizeY = ###").append("\n")
                        .append("center_sizeZ = ###").append("\n")
                        .append("\n")
                        .append("--------------------------------------------------").append("\n")
                        .append("\n")
                        .append("+b0/0/0tro").append("\n")
                ;

            }

            FileManager.writeTXT(Handcode.directory_config + "/generated/" + GameUtils.NBT.entity.getText(entity, "export_file_name"), write.toString(), false);

        }

    }

    public static void whenTreeEnd (LevelAccessor level, Entity entity) {

        TanshugetreesMod.queueServerWork(220, () -> {

            // Update Generated File
            {



            }

            // Rename Generated File
            {

                String folder = Handcode.directory_config + "/generated/";
                String file = GameUtils.NBT.entity.getText(entity, "export_file_name");
                new File(folder + "/" + file).renameTo(new File(folder + "/" + file.replace(" (generating)", "")));

            }

            GameUtils.misc.sendChatMessage(level, "@a", "green", "THT : Completed!");

            if (TanshugetreesModVariables.MapVariables.get(level).auto_gen_count > 0) {

                GameUtils.misc.sendChatMessage(level, "@a", "gray", "THT : Loop left " + (int) TanshugetreesModVariables.MapVariables.get(level).auto_gen_count);
                summon(level);

            } else {

                stop(level);

            }

        });

    }

}
