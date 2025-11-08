package tannyjung.tanshugetrees_handcode.systems.sapling;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import tannyjung.core.Utils;
import tannyjung.tanshugetrees.TanshugetreesMod;

public class SaplingTick {

    public static void start (LevelAccessor level_accessor, double posX, double posY, double posZ) {

        BlockPos pos = new BlockPos((int) posX, (int) posY, (int) posZ);

        if (Utils.nbt.block.getLogic(level_accessor, pos, "sapling_clickable") == true) {

            if (level_accessor instanceof ServerLevel level_server) {

                int countdown = (int) Utils.nbt.block.getNumber(level_accessor, pos, "countdown");
                Utils.command.run(level_server, pos.getX(), pos.getY(),  pos.getZ(), "execute positioned ~0.5 ~1.25 ~0.5 run kill @e[tag=TANSHUGETREES-sapling_text,distance=..1]");

                if (countdown >= 0) {

                    Utils.command.run(level_server, pos.getX(), pos.getY(),  pos.getZ(), "execute positioned ~0.5 ~1.25 ~0.5 run " + Utils.entity.summonCommand("text_display",  "TANSHUGETREES / TANSHUGETREES-sapling_text", "Spaling Countdown", "see_through:1b,alignment:\"left\",brightness:{block:15},line_width:1000,transformation:{left_rotation:[0f,0f,0f,1f],right_rotation:[0f,0f,0f,1f],translation:[0f,0f,0f],scale:[1f,1f,1f]},billboard:vertical,text:'{\"text\":\"" + countdown + "\",\"color\":\"red\"}'"));

                    // If Break Sapling
                    {

                        TanshugetreesMod.queueServerWork(20, () -> {

                            if (level_accessor.getBlockState(pos) == Blocks.AIR.defaultBlockState()) {

                                Utils.command.run(level_server, pos.getX(), pos.getY(),  pos.getZ(), "execute positioned ~0.5 ~1.25 ~0.5 run kill @e[tag=TANSHUGETREES-sapling_text,distance=..1]");

                            }

                        });

                    }

                    Utils.nbt.block.addNumber(level_accessor, pos, "countdown", -1);

                } else {

                    Utils.command.run(level_server, pos.getX(), pos.getY(),  pos.getZ(), "execute positioned ~0.5 ~0.5 ~0.5 run " + Utils.entity.summonCommand("marker", "TANSHUGETREES / TANSHUGETREES-tree_generator", "Tree Generator", ""));
                    Utils.command.run(level_server, pos.getX(), pos.getY(),  pos.getZ(), "data modify entity @e[tag=TANSHUGETREES-tree_generator,distance=..1,limit=1,sort=nearest] ForgeData set from block ~ ~ ~ ForgeData");
                    level_accessor.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);

                }

            }

        }

    }

}
