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

    public static void click (LevelAccessor level_accessor, Entity entity, BlockPos pos) {

        if (level_accessor instanceof ServerLevel level_server) {

            if (GameUtils.Data.getBlockLogic(level_accessor, pos, "sapling_started") == false) {

                if (GameUtils.Item.getSlot(entity, EquipmentSlot.MAINHAND).is(Items.BONE_MEAL) == true) {

                    String error = "";
                    boolean creative_mode = GameUtils.Mob.isCreativeMode(entity);
                    GameUtils.Misc.spawnParticle(level_server, pos.getCenter(), 0, 0, 0, 0, 10, "minecraft:happy_villager");

                    if (creative_mode == false) {

                        GameUtils.Item.addCount(entity, EquipmentSlot.MAINHAND, -1);
                        GameUtils.Data.addBlockNumber(level_accessor, level_server, pos, "bone_meal_usage", 1);

                    }

                    if (creative_mode == true || Math.random() < 0.05) {

                        if (level_accessor.getBlockState(pos).getBlock() == TanshugetreesModBlocks.TREE_GENERATOR.get()) {

                            // Custom Sapling
                            {

                                if (GameUtils.Data.getBlockText(level_accessor, pos, "path").isEmpty() == true) {

                                    if (GameUtils.Data.getBlockNumber(level_accessor, pos, "tree_generator_speed_tick") == 0) {

                                        error = "Empty Sapling Preset";

                                    }

                                }

                            }

                        } else {

                            // General
                            {

                                String name = GameUtils.Tile.toText(level_accessor.getBlockState(pos))[0].substring("tanshugetrees:sapling_".length());
                                File file = new File(Core.path_config + "/dev/temporary/presets/#TannyJung-Main-Pack/" + name + "/" + name + ".txt");

                                if (file.exists() == true && file.isDirectory() == false) {

                                    GameUtils.Data.setBlockText(level_accessor, level_server, pos, "path", "#TannyJung-Main-Pack/" + name);

                                } else {

                                    error = "Sapling Not Available Yet";

                                }

                            }

                        }

                        if (error.isEmpty() == false) {

                            // Error
                            {

                                for (int test = (int) GameUtils.Data.getBlockNumber(level_accessor, pos, "bone_meal_usage"); test > 0; test--) {

                                    GameUtils.Mob.summon(level_server, pos.getCenter(), "minecraft:item", "", "", "{Item:{id:\"minecraft:bone_meal\",Count:1b}}");

                                }

                                GameUtils.Data.setBlockNumber(level_accessor, level_server, pos, "bone_meal_usage", 0);

                                // Display Text
                                {

                                    GameUtils.Mob.summon(level_server, pos.getCenter().add(0, 0.75, 0), "minecraft:text_display", "Sapling Error", "TANSHUGETREES-sapling_error", "{see_through:1b,alignment:\"left\",brightness:{block:15, sky:15},line_width:1000,transformation:{left_rotation:[0f,0f,0f,1f],right_rotation:[0f,0f,0f,1f],translation:[0f,0f,0f],scale:[1f,1f,1f]},billboard:vertical,text:'{\"text\":\"" + error + "\",\"color\":\"red\"}'}");

                                    Core.DelayedWorks.create(false, 200, () -> {

                                        for (Entity entity_import : GameUtils.Mob.getAtArea(level_server, pos.getCenter().add(0, 0.75, 0), 1, true, 0, "minecraft:text_display", "TANSHUGETREES-sapling_error")) {

                                            entity_import.discard();
                                            
                                        }
                                        
                                    });

                                }

                            }

                        } else {

                            if (creative_mode == true) {

                                GameUtils.Data.setBlockNumber(level_accessor, level_server, pos, "countdown", -1);

                            }

                            GameUtils.Data.setBlockLogic(level_accessor, level_server, pos, "sapling_started", true);
                            GameUtils.Mob.summon(level_server, pos.getCenter().add(0, 0.75, 0), "minecraft:text_display", "Sapling Countdown", "TANSHUGETREES-sapling_countdown", "{see_through:1b,alignment:\"left\",brightness:{block:15, sky:15},line_width:1000,transformation:{left_rotation:[0f,0f,0f,1f],right_rotation:[0f,0f,0f,1f],translation:[0f,0f,0f],scale:[1f,1f,1f]},billboard:vertical}");

                        }

                    }

                }

            }

        }

    }

    public static void tick (LevelAccessor level_accessor, BlockPos pos) {

        if (level_accessor instanceof ServerLevel level_server) {

            if (GameUtils.Data.getBlockLogic(level_accessor, pos, "sapling_started") == true) {

                int countdown = (int) GameUtils.Data.getBlockNumber(level_accessor, pos, "countdown");

                if (countdown >= 0) {

                    // Update Countdown Number
                    {

                        for (Entity entity_import : GameUtils.Mob.getAtArea(level_server, pos.getCenter(), 1, true, 0, "minecraft:text_display", "TANSHUGETREES-sapling_countdown")) {

                            GameUtils.Command.runEntity(entity_import, "data merge entity @s {text:'{\"text\":\"" + countdown + "\",\"color\":\"red\"}'}");

                        }

                    }

                    // When Break Sapling
                    {

                        Core.DelayedWorks.create(false, 20, () -> {

                            for (Entity entity_import : GameUtils.Mob.getAtArea(level_server, pos.getCenter(), 1, true, 0, "minecraft:text_display", "TANSHUGETREES-sapling_countdown")) {

                                entity_import.discard();

                            }

                        });

                    }

                    GameUtils.Data.addBlockNumber(level_accessor, level_server, pos, "countdown", -1);

                } else {

                    // End
                    {

                        if (GameUtils.Data.getBlockText(level_accessor, pos, "path").isEmpty() == false) {

                            TreeGenerator.create(level_server, null, pos, GameUtils.Data.getBlockText(level_accessor, pos, "path"));

                        } else {

                            Entity entity_summon = GameUtils.Mob.summon(level_server, pos.getCenter(), "minecraft:marker", "Tree Generator", "TANSHUGETREES-tree_generator", "");
                            GameUtils.Command.runEntity(entity_summon, "data modify entity @s NeoForgeData.tanshugetrees set from block ~ ~ ~ NeoForgeData.tanshugetrees");

                        }

                        level_accessor.removeBlock(new BlockPos(pos), false);

                    }

                }

            }

        }

    }

}
