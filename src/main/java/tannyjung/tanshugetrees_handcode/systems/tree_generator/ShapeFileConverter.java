package tannyjung.tanshugetrees_handcode.systems.tree_generator;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LevelAccessor;
import tannyjung.misc.GameUtils;
import tannyjung.tanshugetrees.TanshugetreesMod;
import tannyjung.tanshugetrees.network.TanshugetreesModVariables;
import tannyjung.tanshugetrees_handcode.Handcode;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class ShapeFileConverter {

    public static void start (LevelAccessor level, Entity entity) {

        if (Handcode.version_1192 == true) {

            GameUtils.misc.sendChatMessage(level, "@a", "red", "THT : Auto gen is not available on 1.19.2");

        } else {

            if (TanshugetreesModVariables.MapVariables.get(level).auto_gen == false) {

                TanshugetreesModVariables.MapVariables.get(level).auto_gen = true;
                GameUtils.misc.sendChatMessage(level, "@a", "gray", "THT : Auto gen now turned ON");
                GameUtils.misc.sendChatMessage(level, "@a", "gray", "THT : Auto gen set loop to " + TanshugetreesModVariables.MapVariables.get(level).auto_gen_count);

                GameUtils.command.run(level, 0, 0, 0, "execute in tanshugetrees:dimension run forceload add -100 -100 100 100");
                TanshugetreesModVariables.MapVariables.get(level).auto_gen_teleport_player_back = entity.position().x + " " + entity.position().y + " " + entity.position().z;
                GameUtils.command.runEntity(entity, "execute in tanshugetrees:dimension run tp @s 0 300 0");
                GameUtils.command.runEntity(entity, "gamemode spectator");
                GameUtils.command.runEntity(entity, "gamemode creative");

            }

        }























        if (GameUtils.command.result(level, 0, 0, 0, "execute in tanshugetrees:dimension positioned 0 0 0 unless entity @e[tag=TANSHUGETREES-tree_generator,distance=..1000]") == true) {

            TanshugetreesModVariables.MapVariables.get(level).auto_gen_cooldown = TanshugetreesModVariables.MapVariables.get(level).auto_gen_cooldown + 1;

            if (TanshugetreesModVariables.MapVariables.get(level).auto_gen_cooldown > 10) {

                TanshugetreesModVariables.MapVariables.get(level).auto_gen_cooldown = 0;
                getData(level);

            }

        }

    }

    private static void getData (LevelAccessor level) {

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

            if (TanshugetreesModVariables.MapVariables.get(level).auto_gen_count > 0) {

                // Summon
                {

                    TanshugetreesModVariables.MapVariables.get(level).auto_gen_count = TanshugetreesModVariables.MapVariables.get(level).auto_gen_count - 1;
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

                // End
                {

                    GameUtils.misc.sendChatMessage(level, "@a", "gray", "THT : Auto gen now turned OFF");
                    TanshugetreesModVariables.MapVariables.get(level).auto_gen = false;
                    GameUtils.command.run(level, 0, 0, 0, "execute in tanshugetrees:dimension run forceload remove all");
                    GameUtils.command.run(level, 0, 0, 0, "execute as @a at @s if dimension tanshugetrees:dimension in minecraft:overworld run tp @s " + TanshugetreesModVariables.MapVariables.get(level).auto_gen_teleport_player_back);

                }

            }

        } else {

            GameUtils.misc.sendChatMessage(level, "@a", "red", "THT : Can't start shape file converter because the file location you wrote cannot be found");

        }

    }

}
