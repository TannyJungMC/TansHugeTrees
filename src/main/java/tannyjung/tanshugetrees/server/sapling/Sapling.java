package tannyjung.tanshugetrees.server.sapling;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.LevelAccessor;
import tannyjung.core.game.NBTManager;
import tannyjung.core.game.Utils;
import tannyjung.tanshugetrees_mcreator.init.TanshugetreesModBlocks;
import tannyjung.tanshugetrees.Handcode;

import java.io.File;

public class Sapling {

    public static void start (LevelAccessor level_accessor, Entity entity, double posX, double posY, double posZ) {

        BlockPos pos = new BlockPos((int) posX, (int) posY, (int) posZ);

        if (NBTManager.Block.getLogic(level_accessor, pos, "sapling_clickable") == false) {

            if (level_accessor instanceof ServerLevel level_server) {

                if (Utils.entity.itemGet(entity, EquipmentSlot.MAINHAND).is(Items.BONE_MEAL) == true) {

                    boolean pass = true;
                    boolean creative_mode = Utils.entity.isCreativeMode(entity);
                    Utils.command.run(level_server, pos.getX(), pos.getY(), pos.getZ(), "particle happy_villager ~0.5 ~0.5 ~0.5 0.25 0.25 0.25 0 10 force");

                    if (creative_mode == false) {

                        Utils.entity.itemCountAdd(entity, EquipmentSlot.MAINHAND, -1);
                        NBTManager.Block.addNumber(level_accessor, pos, "bone_meal_usage", 1);

                    }

                    if (creative_mode == true || Math.random() < 0.05) {

                        // Test and Convert
                        {

                            if (level_accessor.getBlockState(pos).getBlock() == TanshugetreesModBlocks.RANDOM_TREE.get()) {

                                // Custom Tree
                                {

                                    if (NBTManager.Block.getNumber(level_accessor, pos, "tree_generator_speed_tick") == 0) {

                                        cancel(level_accessor, level_server, pos, "No Preset Inside");
                                        pass = false;

                                    }

                                }

                            } else {

                                // General
                                {

                                    String block = Utils.block.toTextID(level_accessor.getBlockState(pos));
                                    block = block.substring(block.indexOf(":") + 1);
                                    File file = new File(Handcode.path_config + "/#dev/custom_packs_organized/presets/#TannyJung-Main-Pack/" + block + "/" + block + ".txt");

                                    if (file.exists() == false) {

                                        cancel(level_accessor, level_server, pos, "Preset Not Found");
                                        pass = false;

                                    } else {

                                        Utils.command.run(level_server, pos.getX(), pos.getY(), pos.getZ(), "data merge block ~ ~ ~ {" + Utils.misc.getForgeDataFromGiveFile(file.getPath()) + "}");

                                    }

                                }

                            }

                        }

                        if (pass == true) {

                            if (creative_mode == true) {

                                NBTManager.Block.setNumber(level_accessor, pos, "countdown", -1);

                            }

                            NBTManager.Block.setLogic(level_accessor, pos, "sapling_clickable", true);

                        }

                    }

                }

            }

        }

    }

    private static void cancel (LevelAccessor level_accessor, ServerLevel level_server, BlockPos pos, String message) {

        for (int test = (int) NBTManager.Block.getNumber(level_accessor, pos, "bone_meal_usage"); test > 0; test--) {

            Utils.command.run(level_server, pos.getX(), pos.getY(), pos.getZ(), "execute positioned ~0.5 ~0.5 ~0.5 run " + Utils.command.summonEntity("item", "", "", "Item:{id:\"minecraft:bone_meal\",Count:1b}"));

        }

        NBTManager.Block.setNumber(level_accessor, pos, "bone_meal_usage", 0);

        // Display Text
        {

            Utils.command.run(level_server, pos.getX(), pos.getY(),  pos.getZ(), "execute positioned ~0.5 ~1.25 ~0.5 run " + Utils.command.summonEntity("text_display",  "TANSHUGETREES / TANSHUGETREES-sapling_text", "Spaling Countdown", "see_through:1b,alignment:\"left\",brightness:{block:15},line_width:1000,transformation:{left_rotation:[0f,0f,0f,1f],right_rotation:[0f,0f,0f,1f],translation:[0f,0f,0f],scale:[1f,1f,1f]},billboard:vertical,text:'{\"text\":\"" + message + "\",\"color\":\"red\"}'"));

            Handcode.createDelayedWorks(200, () -> {

                Utils.command.run(level_server, pos.getX(), pos.getY(),  pos.getZ(), "execute positioned ~0.5 ~1.25 ~0.5 run kill @e[tag=TANSHUGETREES-sapling_text,distance=..1]");

            });

        }

    }

}
