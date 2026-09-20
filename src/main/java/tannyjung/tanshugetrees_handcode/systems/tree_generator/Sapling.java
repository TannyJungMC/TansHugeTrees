package tannyjung.tanshugetrees_handcode.systems.tree_generator;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import tannyjung.tanshugetrees_core.Core;
import tannyjung.tanshugetrees_core.game.*;
import tannyjung.tanshugetrees.init.TanshugetreesModBlocks;
import tannyjung.tanshugetrees_core.outside.FileManager;

import java.io.File;
import java.nio.file.Path;

public class Sapling {

    public static void click (ServerLevel level_server, ServerPlayer player_server, BlockPos pos) {

        if (NBTManager.Tile.getLogic(level_server, pos, "main", "sapling_started") == false) {

            ItemStack item = player_server.getItemBySlot(EquipmentSlot.MAINHAND);

            if (item.getItem() == Items.BONE_MEAL) {

                String error = "";
                boolean creative_mode = player_server.isCreative() == true;
                GameUtils.spawnParticle(level_server, pos.getCenter(), 0, 0, 0, 0, 10, "minecraft:happy_villager");

                if (creative_mode == false) {

                    item.setCount(item.getCount() - 1);
                    NBTManager.Tile.setNumber(level_server, pos, "main", "bone_meal_usage", 1);

                }

                if (creative_mode == true || Math.random() < 0.05) {

                    if (level_server.getBlockState(pos).getBlock() == TanshugetreesModBlocks.CUSTOM_SAPLING.get()) {

                        // Custom Sapling
                        {

                            if (NBTManager.Tile.getText(level_server, pos, "main", "path").isEmpty() == true) {

                                if (NBTManager.Tile.getNumber(level_server, pos, "main", "tree_generator_speed_tick") == 0) {

                                    error = "§cEmpty Blueprint";

                                }

                            }

                        }

                    } else {

                        // General
                        {

                            String path = "";

                            // Get Path
                            {

                                String name = BlockManager.toText(level_server.getBlockState(pos))[0].substring("tanshugetrees:sapling_".length());

                                for (File scan : FileManager.getAllFiles(Core.path_config + "/dev/temporary/tree_blueprints/#main")) {

                                    if (scan.getName().equals(name + ".txt") == false) {

                                        continue;

                                    }

                                    path = Path.of(Core.path_config + "/dev/temporary/tree_blueprints").relativize(scan.toPath()).toString();
                                    path = path.replace("\\", "/");
                                    path = path.substring(0, path.length() - ".txt".length());
                                    break;

                                }

                            }

                            if (path.isEmpty() == true) {

                                error = "§cBlueprint Not Found";

                            } else {

                                NBTManager.Tile.setText(level_server, pos, "main", "path", path);

                            }

                        }

                    }

                    if (error.isEmpty() == false) {

                        // Error
                        {

                            for (int test = (int) NBTManager.Tile.getNumber(level_server, pos, "main", "bone_meal_usage"); test > 0; test--) {

                                EntityManager.summon(level_server, pos.getCenter(), false, "minecraft:item", "", new String[]{}, "{Item:{id:\"minecraft:bone_meal\",Count:1b}}");

                            }

                            NBTManager.Tile.setNumber(level_server, pos, "main", "bone_meal_usage", 0);

                            // Display Text
                            {

                                DisplayManager.summonTextTemporary(level_server, pos.getCenter().add(0, 1, 0), 1, new String[]{}, error);

                            }

                        }

                    } else {

                        if (creative_mode == true) {

                            NBTManager.Tile.setNumber(level_server, pos, "main", "countdown", -1);

                        }

                        NBTManager.Tile.setLogic(level_server, pos, "main", "sapling_started", true);
                        EntityManager.summon(level_server, pos.getCenter().add(0, 0.75, 0), false, "minecraft:text_display", "Sapling Countdown", new String[]{"TANSHUGETREES-sapling_countdown"}, "{see_through:1b,alignment:\"left\",brightness:{block:15, sky:15},line_width:1000,transformation:{left_rotation:[0f,0f,0f,1f],right_rotation:[0f,0f,0f,1f],translation:[0f,0f,0f],scale:[1f,1f,1f]},billboard:vertical}");

                    }

                }

            }

        }

    }

    public static void tick (ServerLevel level_server, BlockPos pos) {

        if (NBTManager.Tile.getLogic(level_server, pos, "main", "sapling_started") == true) {

            int countdown = (int) NBTManager.Tile.getNumber(level_server, pos, "main", "countdown");

            if (countdown >= 0) {

                // Update Countdown Number
                {

                    for (Entity entity_import : EntityManager.Population.getArea(level_server, pos.getCenter(), 1, "minecraft:text_display", "TANSHUGETREES-sapling_countdown", new String[]{})) {

                        GameUtils.runCommandEntity(entity_import, "data merge player_server @s {text:'{\"text\":\"" + countdown + "\",\"color\":\"red\"}'}");

                    }

                }

                // When Break Sapling
                {

                    Core.DelayedWork.createBasic(20, () -> {

                        for (Entity entity_import : EntityManager.Population.getArea(level_server, pos.getCenter(), 1, "minecraft:text_display", "TANSHUGETREES-sapling_countdown", new String[]{})) {

                            entity_import.discard();

                        }

                    });

                }

                NBTManager.Tile.addNumber(level_server, pos, "main", "countdown", -1);

            } else {

                // End
                {

                    for (Entity entity_import : EntityManager.Population.getArea(level_server, pos.getCenter(), 1, "minecraft:text_display", "TANSHUGETREES-sapling_countdown", new String[]{})) {

                        entity_import.discard();

                    }

                    String path = NBTManager.Tile.getText(level_server, pos, "main", "path");

                    if (path.isEmpty() == false) {

                        TreeGenerator.create(level_server, null, pos, path);

                    } else {

                        Entity entity_summon = EntityManager.summon(level_server, pos.getCenter(), false, "minecraft:marker", "Tree Generator", new String[]{"TANSHUGETREES-tree_generator"}, "");

                        if (entity_summon == null) {

                            return;

                        }

                        GameUtils.runCommandEntity(entity_summon, "data modify player_server @s NeoForgeData.tanshugetrees set from block ~ ~ ~ NeoForgeData.tanshugetrees");

                    }

                    level_server.removeBlock(new BlockPos(pos), false);

                }

            }

        }

    }

}
