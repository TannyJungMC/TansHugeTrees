package tannyjung.tanshugetrees_handcode.systems.sapling;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.LevelAccessor;
import tannyjung.core.GameUtils;
import tannyjung.tanshugetrees.init.TanshugetreesModBlocks;
import tannyjung.tanshugetrees_handcode.Handcode;

import java.io.File;

public class Sapling {

    public static void click (LevelAccessor level_accessor, ServerLevel level_server, Entity entity, double posX, double posY, double posZ) {

        BlockPos pos = new BlockPos((int) posX, (int) posY, (int) posZ);

        if (GameUtils.nbt.block.getLogic(level_accessor, pos, "start") == false) {

            if (GameUtils.entity.itemGet(entity, EquipmentSlot.MAINHAND).is(Items.BONE_MEAL) == true) {

                if (GameUtils.entity.isCreativeMode(entity) == false) {

                    GameUtils.entity.itemCountAdd(entity, EquipmentSlot.MAINHAND, -1);

                }

                System.out.println("particle happy_villager ~0.5 ~0.5 ~0.5 0.25 0.25 0.25 0 10 force");
                System.out.println(level_server);

                GameUtils.command.run(level_server, pos.getX(), pos.getY(), pos.getZ(), "particle happy_villager ~0.5 ~0.5 ~0.5 0.25 0.25 0.25 0 10 force");

                // Checking Preset Inside
                {

                    if (level_accessor.getBlockState(pos).getBlock() == TanshugetreesModBlocks.RANDOM_TREE_BLOCK.get()) {

                        // Custom Tree
                        {

                            if (GameUtils.nbt.block.getNumber(level_accessor, pos, "generate_speed_tick") == 0) {

                                cancel();

                            }

                        }

                    } else {

                        // General
                        {

                            String blockID = GameUtils.block.toTextID(level_accessor.getBlockState(pos));
                            File file = new File(Handcode.directory_config + "/custom_packs/.organized/presets/TannyJung-Tree-Pack/" + blockID.substring(blockID.indexOf(":") + 1) + ".txt");

                            if (file.exists() == false) {

                                cancel();

                            } else {



                            }

                        }

                    }

                }

            }

        }

    }

    private static void cancel () {



    }

}
