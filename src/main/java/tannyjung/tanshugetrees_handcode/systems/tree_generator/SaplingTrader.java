package tannyjung.tanshugetrees_handcode.systems.tree_generator;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import tannyjung.tanshugetrees_core.Core;
import tannyjung.tanshugetrees_core.outside.FileManager;
import tannyjung.tanshugetrees_core.game.GameUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SaplingTrader {

    public static void summonTrader (LevelAccessor level_accessor, ServerLevel level_server, int posX, int posY, int posZ) {

        File[] files = new File(Core.path_config + "/#dev/temporary/sapling_trader").listFiles();
        StringBuilder data = new StringBuilder();
        data.append("VillagerData:{level:99},CustomName:'{\"text\":\"Sapling Trader\"}',ArmorItems:[{},{},{},{id:\"tanshugetrees:sapling_yokai\",Count:1b}],DeathLootTable:\"minecraft:empty\",ArmorDropChances:[0.0f,0.0f,0.0f,0.0f],Offers:{Recipes:[");

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

        data.append("]}");
        GameUtils.command.run(false, level_server, posX, posY, posZ, "execute positioned ~0.5 ~0.5 ~0.5 run " + GameUtils.command.summonEntity("wandering_trader", "TANSHUGETREES", "Sapling Trader", data.toString()));
        GameUtils.command.run(false, level_server, posX, posY, posZ, "execute positioned ~0.5 ~0.5 ~0.5 run particle minecraft:flash ~ ~ ~ 0 0 0 0 1 force");
        GameUtils.command.run(false, level_server, posX, posY, posZ, "execute positioned ~0.5 ~0.5 ~0.5 run particle minecraft:campfire_signal_smoke ~ ~ ~ 0.5 0.5 0.5 0.01 20 force");
        GameUtils.command.run(false, level_server, posX, posY, posZ, "execute positioned ~0.5 ~0.5 ~0.5 run playsound minecraft:entity.illusioner.cast_spell ambient @a[distance=..100] ~ ~ ~ 2 0 0.025");
        level_accessor.setBlock(new BlockPos(posX, posY, posZ), Blocks.AIR.defaultBlockState(), 2);

    }

}
