package tannyjung.tanshugetrees_handcode.systems.tree_generator;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import tannyjung.tanshugetrees_core.game.NBTManager;
import tannyjung.tanshugetrees_core.game.GameUtils;
import tannyjung.tanshugetrees.init.TanshugetreesModBlocks;
import tannyjung.tanshugetrees_handcode.Handcode;

import java.io.File;

public class Sapling {

    public static void click (LevelAccessor level_accessor, Entity entity, double posX, double posY, double posZ) {

        if (level_accessor instanceof ServerLevel level_server) {

            BlockPos pos = new BlockPos((int) posX, (int) posY, (int) posZ);

            if (NBTManager.block.getLogic(level_accessor, pos, "sapling_clickable") == false) {

                if (GameUtils.entity.getItemSlot(entity, EquipmentSlot.MAINHAND).is(Items.BONE_MEAL) == true) {

                    String error = "";
                    boolean creative_mode = GameUtils.entity.isCreativeMode(entity);
                    GameUtils.command.run(false, level_server, pos.getX(), pos.getY(), pos.getZ(), "particle happy_villager ~0.5 ~0.5 ~0.5 0.25 0.25 0.25 0 10 force");

                    if (creative_mode == false) {

                        GameUtils.entity.itemCountAdd(entity, EquipmentSlot.MAINHAND, -1);
                        NBTManager.block.addNumber(level_accessor, pos, "bone_meal_usage", 1);

                    }

                    if (creative_mode == true || Math.random() < 0.05) {

                        if (level_accessor.getBlockState(pos).getBlock() == TanshugetreesModBlocks.TREE_GENERATOR.get()) {

                            // Custom Sapling
                            {

                                if (NBTManager.block.getText(level_accessor, pos, "path").isEmpty() == true) {

                                    if (NBTManager.block.getNumber(level_accessor, pos, "tree_generator_speed_tick") == 0) {

                                        error = "Empty Sapling Preset";

                                    }

                                }

                            }

                        } else {

                            // General
                            {

                                String name = GameUtils.block.toTextID(level_accessor.getBlockState(pos)).substring("tanshugetrees:sapling_".length());
                                File file = new File(Handcode.path_config + "/#dev/temporary/presets/#TannyJung-Main-Pack/" + name + "/" + name + ".txt");

                                if (file.exists() == true && file.isDirectory() == false) {

                                    NBTManager.block.setText(level_accessor, pos, "path", "#TannyJung-Main-Pack/" + name);

                                } else {

                                    error = "Sapling Not Available Yet";

                                }

                            }

                        }

                        if (error.isEmpty() == false) {

                            // Error
                            {

                                for (int test = (int) NBTManager.block.getNumber(level_accessor, pos, "bone_meal_usage"); test > 0; test--) {

                                    GameUtils.command.run(false, level_server, pos.getX(), pos.getY(), pos.getZ(), "execute positioned ~0.5 ~0.5 ~0.5 run " + GameUtils.command.summonEntity("item", "", "", "Item:{id:\"minecraft:bone_meal\",Count:1b}"));

                                }

                                NBTManager.block.setNumber(level_accessor, pos, "bone_meal_usage", 0);

                                // Display Text
                                {

                                    GameUtils.command.run(false, level_server, pos.getX(), pos.getY(), pos.getZ(), "execute positioned ~0.5 ~1.25 ~0.5 run " + GameUtils.command.summonEntity("text_display", "TANSHUGETREES / TANSHUGETREES-sapling_text", "Spaling Countdown", "see_through:1b,alignment:\"left\",brightness:{block:15, sky:15},line_width:1000,transformation:{left_rotation:[0f,0f,0f,1f],right_rotation:[0f,0f,0f,1f],translation:[0f,0f,0f],scale:[1f,1f,1f]},billboard:vertical,text:'{\"text\":\"" + error + "\",\"color\":\"red\"}'"));

                                    Handcode.createDelayedWorks(false, 200, () -> {

                                        GameUtils.command.run(false, level_server, pos.getX(), pos.getY(), pos.getZ(), "execute positioned ~0.5 ~1.25 ~0.5 run kill @e[tag=TANSHUGETREES-sapling_text,distance=..1]");

                                    });

                                }

                            }

                        } else {

                            if (creative_mode == true) {

                                NBTManager.block.setNumber(level_accessor, pos, "countdown", -1);

                            }

                            NBTManager.block.setLogic(level_accessor, pos, "sapling_clickable", true);

                        }

                    }

                }

            }

        }

    }

    public static void tick (LevelAccessor level_accessor, double posX, double posY, double posZ) {

        if (level_accessor instanceof ServerLevel level_server) {

            BlockPos pos = new BlockPos((int) posX, (int) posY, (int) posZ);

            if (NBTManager.block.getLogic(level_accessor, pos, "sapling_clickable") == true) {

                int countdown = (int) NBTManager.block.getNumber(level_accessor, pos, "countdown");
                GameUtils.command.run(false, level_server, pos.getX(), pos.getY(),  pos.getZ(), "execute positioned ~0.5 ~1.25 ~0.5 run kill @e[tag=TANSHUGETREES-sapling_text,distance=..1]");

                if (countdown >= 0) {

                    GameUtils.command.run(false, level_server, pos.getX(), pos.getY(),  pos.getZ(), "execute positioned ~0.5 ~1.25 ~0.5 run " + GameUtils.command.summonEntity("text_display",  "TANSHUGETREES / TANSHUGETREES-sapling_text", "Spaling Countdown", "see_through:1b,alignment:\"left\",brightness:{block:15, sky:15},line_width:1000,transformation:{left_rotation:[0f,0f,0f,1f],right_rotation:[0f,0f,0f,1f],translation:[0f,0f,0f],scale:[1f,1f,1f]},billboard:vertical,text:'{\"text\":\"" + countdown + "\",\"color\":\"red\"}'"));

                    // If Break Sapling
                    {

                        Handcode.createDelayedWorks(false, 20, () -> {

                            if (level_accessor.getBlockState(pos) == Blocks.AIR.defaultBlockState()) {

                                GameUtils.command.run(false, level_server, pos.getX(), pos.getY(),  pos.getZ(), "execute positioned ~0.5 ~1.25 ~0.5 run kill @e[tag=TANSHUGETREES-sapling_text,distance=..1]");

                            }

                        });

                    }

                    NBTManager.block.addNumber(level_accessor, pos, "countdown", -1);

                } else {

                    if (NBTManager.block.getText(level_accessor, pos, "path").isEmpty() == false) {

                        TreeGenerator.create(level_server, null, pos.getX(), pos.getY(),  pos.getZ(), NBTManager.block.getText(level_accessor, pos, "path"));

                    } else {

                        GameUtils.command.run(false, level_server, pos.getX(), pos.getY(), pos.getZ(), "execute positioned ~0.5 ~0.5 ~0.5 run " + GameUtils.command.summonEntity("marker", "TANSHUGETREES / TANSHUGETREES-tree_generator", "Tree Generator", ""));
                        GameUtils.command.run(false, level_server, pos.getX(), pos.getY(), pos.getZ(), "data modify entity @e[tag=TANSHUGETREES-tree_generator,distance=..1,limit=1,sort=nearest] ForgeData set from block ~ ~ ~ ForgeData");

                    }

                    level_accessor.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);

                }

            }

        }

    }

}
