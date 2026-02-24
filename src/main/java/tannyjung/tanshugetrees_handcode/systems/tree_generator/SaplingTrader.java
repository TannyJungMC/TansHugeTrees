package tannyjung.tanshugetrees_handcode.systems.tree_generator;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.LevelAccessor;
import tannyjung.tanshugetrees_core.Core;
import tannyjung.tanshugetrees_core.outside.FileManager;
import tannyjung.tanshugetrees_core.game.GameUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SaplingTrader {

    public static void summonTrader (LevelAccessor level_accessor, ServerLevel level_server, int posX, int posY, int posZ) {

        File[] files = new File(Core.path_config + "/#dev/#temporary/sapling_trader").listFiles();
        StringBuilder data = new StringBuilder();
        data.append("{VillagerData:{level:99},CustomName:'{\"text\":\"Sapling Trader\"}',ArmorItems:[{},{},{},{id:\"tanshugetrees:sapling_yokai\",Count:1b}],DeathLootTable:\"minecraft:empty\",ArmorDropChances:[0.0f,0.0f,0.0f,0.0f],Offers:{Recipes:[");

        if (files != null) {

            // Get Data
            {

                List<String> get = new ArrayList<>();

                for (File file : files) {

                    get.clear();

                    for (String read_all : FileManager.readTXT(file.getPath())) {

                        if (read_all.isEmpty() == false) {

                            get.add(read_all);

                        }

                    }

                    data.append("{buy:{id:\"");
                    data.append(get.get(0));
                    data.append("\",tag:");
                    data.append(get.get(1));
                    data.append(",Count:");
                    data.append(get.get(2));
                    data.append("},buyB:{id:\"");
                    data.append(get.get(3));
                    data.append("\",tag:");
                    data.append(get.get(4));
                    data.append(",Count:");
                    data.append(get.get(5));
                    data.append("},sell:{id:\"");
                    data.append(get.get(6));
                    data.append("\",tag:");
                    data.append(get.get(7));
                    data.append(",Count:");
                    data.append(get.get(8));
                    data.append("},rewardExp:0b,maxUses:9999999},");

                }

            }

        }

        data.append("]}}");
        GameUtils.entity.summon(level_server, posX + 0.5, posY + 0.5, posZ + 0.5, "minecraft:wandering_trader", "Sapling Trader", "TANSHUGETREES-sapling_trader", data.toString());
        GameUtils.misc.spawnParticle(level_server, posX + 0.5, posY + 0.5, posZ + 0.5, 0, 0, 0, 0, 1, "minecraft:flash");
        GameUtils.misc.spawnParticle(level_server, posX + 0.5, posY + 0.5, posZ + 0.5, 0.5, 0.5, 0.5, 0.01, 20, "minecraft:campfire_signal_smoke");
        GameUtils.misc.playSound(level_server, posX + 0.5, posY + 0.5, posZ + 0.5, 2, 0, "minecraft:entity.illusioner.cast_spell");
        level_accessor.removeBlock(new BlockPos(posX, posY, posZ), false);

    }

}
