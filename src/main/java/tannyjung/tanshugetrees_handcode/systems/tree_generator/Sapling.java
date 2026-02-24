package tannyjung.tanshugetrees_handcode.systems.tree_generator;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.LevelAccessor;
import tannyjung.tanshugetrees_core.Core;
import tannyjung.tanshugetrees_core.game.GameUtils;
import tannyjung.tanshugetrees.init.TanshugetreesModBlocks;

import java.io.File;

public class Sapling {

    public static void click (LevelAccessor level_accessor, Entity entity, int posX, int posY, int posZ) {

        if (level_accessor instanceof ServerLevel level_server) {

            if (GameUtils.nbt.block.getLogic(level_accessor, posX, posY, posZ, "sapling_started") == false) {

                if (GameUtils.entity.getItemSlot(entity, EquipmentSlot.MAINHAND).is(Items.BONE_MEAL) == true) {

                    String error = "";
                    boolean creative_mode = GameUtils.entity.isCreativeMode(entity);
                    GameUtils.misc.spawnParticle(level_server, posX + 0.5, posY + 0.5, posZ + 0.5, 0, 0, 0, 0, 10, "minecraft:happy_villager");

                    if (creative_mode == false) {

                        GameUtils.entity.itemCountAdd(entity, EquipmentSlot.MAINHAND, -1);
                        GameUtils.nbt.block.addNumber(level_accessor, level_server, posX, posY, posZ, "bone_meal_usage", 1);

                    }

                    if (creative_mode == true || Math.random() < 0.05) {

                        if (level_accessor.getBlockState(new BlockPos(posX, posY, posZ)).getBlock() == TanshugetreesModBlocks.TREE_GENERATOR.get()) {

                            // Custom Sapling
                            {

                                if (GameUtils.nbt.block.getText(level_accessor, posX, posY, posZ, "path").isEmpty() == true) {

                                    if (GameUtils.nbt.block.getNumber(level_accessor, posX, posY, posZ, "tree_generator_speed_tick") == 0) {

                                        error = "Empty Sapling Preset";

                                    }

                                }

                            }

                        } else {

                            // General
                            {

                                String name = GameUtils.block.toTextID(level_accessor.getBlockState(new BlockPos(posX, posY, posZ))).substring("tanshugetrees:sapling_".length());
                                File file = new File(Core.path_config + "/#dev/#temporary/presets/#TannyJung-Main-Pack/" + name + "/" + name + ".txt");

                                if (file.exists() == true && file.isDirectory() == false) {

                                    GameUtils.nbt.block.setText(level_accessor, level_server, posX, posY, posZ, "path", "#TannyJung-Main-Pack/" + name);

                                } else {

                                    error = "Sapling Not Available Yet";

                                }

                            }

                        }

                        if (error.isEmpty() == false) {

                            // Error
                            {

                                for (int test = (int) GameUtils.nbt.block.getNumber(level_accessor, posX, posY, posZ, "bone_meal_usage"); test > 0; test--) {

                                    GameUtils.entity.summon(level_server, posX + 0.5, posY + 0.5, posZ + 0.5, "minecraft:item", "", "", "{Item:{id:\"minecraft:bone_meal\",Count:1b}}");

                                }

                                GameUtils.nbt.block.setNumber(level_accessor, level_server, posX, posY, posZ, "bone_meal_usage", 0);

                                // Display Text
                                {

                                    GameUtils.entity.summon(level_server, posX + 0.5, posY + 1.25, posZ + 0.5, "minecraft:text_display", "Sapling Error", "TANSHUGETREES-sapling_error", "{see_through:1b,alignment:\"left\",brightness:{block:15, sky:15},line_width:1000,transformation:{left_rotation:[0f,0f,0f,1f],right_rotation:[0f,0f,0f,1f],translation:[0f,0f,0f],scale:[1f,1f,1f]},billboard:vertical,text:'{\"text\":\"" + error + "\",\"color\":\"red\"}'}");

                                    Core.DelayedWorks.create(false, 200, () -> {

                                        for (Entity entity_import : GameUtils.entity.getAtArea(level_server, posX, posY, posZ, 1, true, 0, "minecraft:text_display", "TANSHUGETREES-sapling_error")) {

                                            entity_import.discard();
                                            
                                        }
                                        
                                    });

                                }

                            }

                        } else {

                            if (creative_mode == true) {

                                GameUtils.nbt.block.setNumber(level_accessor, level_server, posX, posY, posZ, "countdown", -1);

                            }

                            GameUtils.nbt.block.setLogic(level_accessor, level_server, posX, posY, posZ, "sapling_started", true);
                            GameUtils.entity.summon(level_server, posX + 0.5, posY + 1.25, posZ + 0.5, "minecraft:text_display", "Sapling Countdown", "TANSHUGETREES-sapling_countdown", "{see_through:1b,alignment:\"left\",brightness:{block:15, sky:15},line_width:1000,transformation:{left_rotation:[0f,0f,0f,1f],right_rotation:[0f,0f,0f,1f],translation:[0f,0f,0f],scale:[1f,1f,1f]},billboard:vertical}");

                        }

                    }

                }

            }

        }

    }

    public static void tick (LevelAccessor level_accessor, int posX, int posY, int posZ) {

        if (level_accessor instanceof ServerLevel level_server) {

            if (GameUtils.nbt.block.getLogic(level_accessor, posX, posY, posZ, "sapling_started") == true) {

                int countdown = (int) GameUtils.nbt.block.getNumber(level_accessor, posX, posY, posZ, "countdown");

                if (countdown >= 0) {

                    // Update Countdown Number
                    {

                        for (Entity entity_import : GameUtils.entity.getAtArea(level_server, posX, posY, posZ, 1, true, 0, "minecraft:text_display", "TANSHUGETREES-sapling_countdown")) {

                            GameUtils.command.runEntity(entity_import, "data merge entity @s {text:'{\"text\":\"" + countdown + "\",\"color\":\"red\"}'}");

                        }

                    }

                    // When Break Sapling
                    {

                        Core.DelayedWorks.create(false, 20, () -> {

                            for (Entity entity_import : GameUtils.entity.getAtArea(level_server, posX, posY, posZ, 1, true, 0, "minecraft:text_display", "TANSHUGETREES-sapling_countdown")) {

                                entity_import.discard();

                            }

                        });

                    }

                    GameUtils.nbt.block.addNumber(level_accessor, level_server, posX, posY, posZ, "countdown", -1);

                } else {

                    // End
                    {

                        if (GameUtils.nbt.block.getText(level_accessor, posX, posY, posZ, "path").isEmpty() == false) {

                            TreeGenerator.create(level_server, posX, posY, posZ, GameUtils.nbt.block.getText(level_accessor, posX, posY, posZ, "path"));

                        } else {

                            Entity entity_summon = GameUtils.entity.summon(level_server, posX + 0.5, posY + 0.5, posZ + 0.5, "minecraft:marker", "Tree Generator", "TANSHUGETREES-tree_generator", "");
                            GameUtils.command.runEntity(entity_summon, "data modify entity @s NeoForgeData.tanshugetrees set from block ~ ~ ~ NeoForgeData.tanshugetrees");

                        }

                        level_accessor.removeBlock(new BlockPos(posX, posY, posZ), false);

                    }

                }

            }

        }

    }

}
