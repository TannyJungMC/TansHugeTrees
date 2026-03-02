package tannyjung.tanshugetrees_handcode.systems.tree_generator;

import net.minecraft.server.level.ServerLevel;
import tannyjung.tanshugetrees_core.Core;
import tannyjung.tanshugetrees_core.outside.FileManager;
import tannyjung.tanshugetrees_core.game.GameUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SaplingTrader {

    public static void summonTrader (ServerLevel level_server, int posX, int posY, int posZ) {

        File[] files = new File(Core.path_config + "/#dev/#temporary/sapling_trader").listFiles();
        StringBuilder write = new StringBuilder();

        // Generate Data
        {

            write.append("{ArmorItems:[{},{},{},{id:\"tanshugetrees:sapling_yokai\",Count:1b}],DeathLootTable:\"minecraft:empty\",ArmorDropChances:[0.0f,0.0f,0.0f,0.0f],VillagerData:{level:99},Offers:{Recipes:[");

            if (files != null) {

                // Get Data
                {

                    List<String> data = new ArrayList<>();

                    for (File file : files) {

                        if (file.isDirectory() == false) {

                            data.clear();

                            // Get Data
                            {

                                for (String read_all : FileManager.readTXT(file.getPath())) {

                                    if (read_all.isEmpty() == false) {

                                        if (read_all.equals("-") == true) {

                                            data.add("");

                                        } else {

                                            data.add(read_all);

                                        }

                                    }

                                }

                            }

                            // Edit
                            {

                                if (data.get(4).isEmpty() == false) {

                                    data.set(4, "path:\"" + data.get(4) + "\"");

                                }

                                if (data.get(10).isEmpty() == false) {

                                    data.set(10, "path:\"" + data.get(10) + "\"");

                                }

                                if (data.get(16).isEmpty() == false) {

                                    data.set(16, "path:\"" + data.get(16) + "\"");

                                }

                            }

                            // Write
                            {

                                /*
                                (1.20.1)
                                write.append("{buy:{id:\"").append(data.get(0)).append("\",tag:{").append(GameUtils.Data.createItem(data.get(1), data.get(2), data.get(3), data.get(4))).append("},Count:").append(data.get(5));
                                write.append("},buyB:{id:\"").append(data.get(6)).append("\",tag:{").append(GameUtils.Data.createItem(data.get(7), data.get(8), data.get(9), data.get(10))).append("},Count:").append(data.get(11));
                                write.append("},sell:{id:\"").append(data.get(12)).append("\",tag:{").append(GameUtils.Data.createItem(data.get(13), data.get(14), data.get(15), data.get(16))).append("},Count:").append(data.get(17));
                                (1.21.1)
                                write.append("{buy:{id:\"").append(data.get(0)).append("\",components:{").append(GameUtils.Data.createItem(data.get(1), data.get(2), data.get(3), data.get(4))).append("},count:").append(data.get(5));
                                write.append("},buyB:{id:\"").append(data.get(6)).append("\",components:{").append(GameUtils.Data.createItem(data.get(7), data.get(8), data.get(9), data.get(10))).append("},count:").append(data.get(11));
                                write.append("},sell:{id:\"").append(data.get(12)).append("\",components:{").append(GameUtils.Data.createItem(data.get(13), data.get(14), data.get(15), data.get(16))).append("},count:").append(data.get(17));
                                */
                                write.append("{buy:{id:\"").append(data.get(0)).append("\",tag:{").append(GameUtils.Data.createItem(data.get(1), data.get(2), data.get(3), data.get(4))).append("},Count:").append(data.get(5));
                                write.append("},buyB:{id:\"").append(data.get(6)).append("\",tag:{").append(GameUtils.Data.createItem(data.get(7), data.get(8), data.get(9), data.get(10))).append("},Count:").append(data.get(11));
                                write.append("},sell:{id:\"").append(data.get(12)).append("\",tag:{").append(GameUtils.Data.createItem(data.get(13), data.get(14), data.get(15), data.get(16))).append("},Count:").append(data.get(17));

                            }

                            write.append("},rewardExp:0b,maxUses:9999999},");

                        }

                    }

                }

            }

            write.append("]}}");

        }

        GameUtils.Mob.summon(level_server, posX + 0.5, posY + 0.5, posZ + 0.5, "minecraft:wandering_trader", "Sapling Trader / dark_green", "TANSHUGETREES-sapling_trader", write.toString());
        GameUtils.Misc.spawnParticle(level_server, posX + 0.5, posY + 0.5, posZ + 0.5, 0, 0, 0, 0, 1, "minecraft:flash");
        GameUtils.Misc.spawnParticle(level_server, posX + 0.5, posY + 0.5, posZ + 0.5, 0.5, 0.5, 0.5, 0.01, 20, "minecraft:campfire_signal_smoke");
        GameUtils.Misc.playSound(level_server, posX + 0.5, posY + 0.5, posZ + 0.5, 2, 0, "minecraft:entity.illusioner.cast_spell");

    }

}
