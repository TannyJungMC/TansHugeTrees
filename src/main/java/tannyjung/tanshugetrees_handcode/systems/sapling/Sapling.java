package tannyjung.tanshugetrees_handcode.systems.sapling;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.LevelAccessor;
import tannyjung.core.GameUtils;
import tannyjung.core.MiscUtils;
import tannyjung.tanshugetrees.TanshugetreesMod;
import tannyjung.tanshugetrees.init.TanshugetreesModBlocks;
import tannyjung.tanshugetrees_handcode.Handcode;

import java.io.File;

public class Sapling {

    public static void start (LevelAccessor level_accessor, Entity entity, double posX, double posY, double posZ) {

        BlockPos pos = new BlockPos((int) posX, (int) posY, (int) posZ);

        if (GameUtils.nbt.block.getLogic(level_accessor, pos, "sapling_clickable") == false) {

            if (level_accessor instanceof ServerLevel level_server) {

                if (GameUtils.entity.itemGet(entity, EquipmentSlot.MAINHAND).is(Items.BONE_MEAL) == true) {

                    boolean pass = true;
                    boolean creative_mode = GameUtils.entity.isCreativeMode(entity);
                    GameUtils.command.run(level_server, pos.getX(), pos.getY(), pos.getZ(), "particle happy_villager ~0.5 ~0.5 ~0.5 0.25 0.25 0.25 0 10 force");

                    if (creative_mode == false) {

                        GameUtils.entity.itemCountAdd(entity, EquipmentSlot.MAINHAND, -1);
                        GameUtils.nbt.block.addNumber(level_accessor, pos, "bone_meal_usage", 1);

                    }

                    if (creative_mode == true || Math.random() < 0.05) {

                        // Test and Convert
                        {

                            if (level_accessor.getBlockState(pos).getBlock() == TanshugetreesModBlocks.RANDOM_TREE.get()) {

                                // Custom Tree
                                {

                                    if (GameUtils.nbt.block.getNumber(level_accessor, pos, "generate_speed_tick") == 0) {

                                        cancel(level_accessor, level_server, pos, "No Preset Inside");
                                        pass = false;

                                    }

                                }

                            } else {

                                // General
                                {

                                    String block = GameUtils.block.toTextID(level_accessor.getBlockState(pos));
                                    block = block.substring(block.indexOf(":") + 1);
                                    File file = new File(Handcode.directory_config + "/custom_packs/.organized/presets/TannyJung-Main-Pack/" + block + "/" + block + ".txt");

                                    if (file.exists() == false) {

                                        cancel(level_accessor, level_server, pos, "Preset Not Found");
                                        pass = false;

                                    } else {

                                        GameUtils.command.run(level_server, pos.getX(), pos.getY(), pos.getZ(), "data merge block ~ ~ ~ {" + MiscUtils.getForgeDataFromGiveFile(file.getPath()) + "}");

                                    }

                                }

                            }

                        }

                        if (pass == true) {

                            if (creative_mode == true) {

                                GameUtils.nbt.block.setNumber(level_accessor, pos, "countdown", -1);

                            }

                            GameUtils.nbt.block.setLogic(level_accessor, pos, "sapling_clickable", true);

                        }

                    }

                }

            }

        }

    }

    private static void cancel (LevelAccessor level_accessor, ServerLevel level_server, BlockPos pos, String message) {

        for (int test = (int) GameUtils.nbt.block.getNumber(level_accessor, pos, "bone_meal_usage"); test > 0; test--) {

            GameUtils.command.run(level_server, pos.getX(), pos.getY(), pos.getZ(), "execute positioned ~0.5 ~0.5 ~0.5 run " + GameUtils.misc.summonEntity("item", "", "", "Item:{id:\"minecraft:bone_meal\",Count:1b}"));

        }

        GameUtils.nbt.block.setNumber(level_accessor, pos, "bone_meal_usage", 0);

        // Display Text
        {

            GameUtils.command.run(level_server, pos.getX(), pos.getY(),  pos.getZ(), "execute positioned ~0.5 ~1.25 ~0.5 run " + GameUtils.misc.summonEntity("text_display",  "TANSHUGETREES / TANSHUGETREES-sapling_text", "Spaling Countdown", "see_through:1b,alignment:\"left\",brightness:{block:15},line_width:1000,transformation:{left_rotation:[0f,0f,0f,1f],right_rotation:[0f,0f,0f,1f],translation:[0f,0f,0f],scale:[1f,1f,1f]},billboard:vertical,text:'{\"text\":\"" + message + "\",\"color\":\"red\"}'"));

            TanshugetreesMod.queueServerWork(200, () -> {

                GameUtils.command.run(level_server, pos.getX(), pos.getY(),  pos.getZ(), "execute positioned ~0.5 ~1.25 ~0.5 run kill @e[tag=TANSHUGETREES-sapling_text,distance=..1]");

            });

        }

    }

}
