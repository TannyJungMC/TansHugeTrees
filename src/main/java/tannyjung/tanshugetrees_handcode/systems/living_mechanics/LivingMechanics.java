package tannyjung.tanshugetrees_handcode.systems.living_mechanics;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import tannyjung.tanshugetrees_core.Core;
import tannyjung.tanshugetrees_core.outside.OutsideUtils;
import tannyjung.tanshugetrees_core.game.GameUtils;
import tannyjung.tanshugetrees.network.TanshugetreesModVariables;
import tannyjung.tanshugetrees_handcode.Handcode;
import tannyjung.tanshugetrees_handcode.systems.Caches;

import java.io.File;
import java.util.*;

public class LivingMechanics {

    public static void start (Entity entity) {

        LevelAccessor level_accessor = entity.level();
        ServerLevel level_server = (ServerLevel) level_accessor;
        BlockPos center_pos = new BlockPos(entity.getBlockX(), entity.getBlockY(), entity.getBlockZ());

        if (level_server.isLoaded(center_pos) == false) {

            return;

        }

        Map<String, BlockState> map_block = new HashMap<>();
        int[] leaves_type = new int[2];
        boolean can_leaves_decay = false;
        boolean can_leaves_drop = false;
        boolean can_leaves_regrow = false;

        // Read Settings
        {

            List<String> tree_settings = Caches.getTreeSettings(GameUtils.Data.getEntityText(entity, "tree_settings"));

            if (tree_settings.isEmpty() == true) {

                return;

            } else {

                String get_short = "";
                String get = "";

                for (String read_all : tree_settings) {

                    {

                        if (read_all.startsWith("can_leaves_decay = ") == true) {

                            can_leaves_decay = Boolean.parseBoolean(read_all.substring("can_leaves_decay = ".length()));

                        } else if (read_all.startsWith("can_leaves_drop = ") == true) {

                            can_leaves_drop = Boolean.parseBoolean(read_all.substring("can_leaves_drop = ".length()));

                        } else if (read_all.startsWith("can_leaves_regrow = ") == true) {

                            can_leaves_regrow = Boolean.parseBoolean(read_all.substring("can_leaves_regrow = ".length()));

                        } else if (read_all.startsWith("Block ") == true) {

                            {

                                get_short = read_all.substring(("Block ### ").length(), ("Block ### ####").length());
                                get = read_all.substring(("Block ### #### = ").length());

                                if (get.endsWith(" keep") == true) {

                                    get = get.substring(0, get.length() - (" keep").length());

                                }

                                map_block.put(get_short, GameUtils.Tile.fromText(get));

                                if (get_short.startsWith("120") == true) {

                                    // Leaves Types
                                    {

                                        if (get.endsWith("]") == true) {

                                            get = get.substring(0, get.indexOf("["));

                                        }

                                        int number = Integer.parseInt(get_short.substring(3)) - 1;

                                        if (Handcode.Config.deciduous_leaves_list.contains(get) == true) {

                                            leaves_type[number] = 1;

                                        } else if (Handcode.Config.coniferous_leaves_list.contains(get) == true) {

                                            leaves_type[number] = 2;

                                        }

                                    }

                                }

                            }

                        }

                    }

                }

            }

        }

        String[] file_path_data = GameUtils.Data.getEntityText(entity, "file").split("\\|");
        String path_storage = file_path_data[0];
        String chosen = file_path_data[1];
        File file = new File(Core.path_config + "/dev/temporary/presets/" + path_storage + "/storage/" + chosen);

        if (file.exists() == true && file.isDirectory() == false) {

            if (Handcode.Config.developer_mode == true) {

                GameUtils.Misc.spawnParticle(level_server, entity.position(), 0, 0, 0, 0, 1, "minecraft:flash");
                GameUtils.Misc.spawnParticle(level_server, entity.position().add(0, 100, 0), 0, 25, 0, 0, 300, "minecraft:totem_of_undying");

            }

            boolean have_center_block = level_accessor.getBlockState(center_pos).isAir() == false;
            int rotation = (int) GameUtils.Data.getEntityNumber(entity, "rotation");
            int mirrored = (int) GameUtils.Data.getEntityNumber(entity, "mirrored");
            int biome_type = 0;

            // Biome Type Test
            {

                Holder<Biome> biome = GameUtils.Environment.getAt(level_accessor, center_pos);

                if (GameUtils.Environment.test(biome, "#tanshugetrees:snowy_biomes") == true) {

                    biome_type = 1;

                } else if (GameUtils.Environment.test(biome, "#tanshugetrees:tropical_biomes") == true) {

                    biome_type = 2;

                }

            }

            int process = 0;
            int loop = 0;
            String type = "";
            int posX = 0;
            int posY = 0;
            int posZ = 0;
            int[] pos_converted = new int[0];
            String get = "";
            BlockPos pre_pos = new BlockPos(0, 0, 0);
            BlockState pre_block = Blocks.AIR.defaultBlockState();
            BlockPos pos = new BlockPos(0, 0, 0);
            BlockState block = Blocks.AIR.defaultBlockState();
            String[] pre_block_data = new String[0];

            for (short read_all : Caches.getTreeShapeData(path_storage + "|" + chosen)) {

                // Loop and Get Data
                {

                    loop = loop + 1;

                    if (loop == 1) {

                        type = String.valueOf(read_all);

                    } else if (loop == 2) {

                        posX = read_all;

                    } else if (loop == 3) {

                        posY = read_all;

                    } else {

                        posZ = read_all;
                        loop = 0;

                    }

                }

                if (loop == 0) {

                    process = process + 1;

                    // Skipping Conditions
                    {

                        // Out of Save
                        {

                            if (process < GameUtils.Data.getEntityNumber(entity, "process_save")) {

                                continue;

                            }

                        }

                        // Out of Process Limit
                        {

                            if (Handcode.Config.living_mechanics_process_limit > 0) {

                                if (GameUtils.Data.getEntityNumber(entity, "process_save") + Handcode.Config.living_mechanics_process_limit <= process) {

                                    GameUtils.Data.setEntityNumber(entity, "process_save", process);
                                    return;

                                }

                            }

                        }

                    }

                    if (type.startsWith("1") == true) {

                        if (type.startsWith("120") == false) {

                            GameUtils.Data.setEntityText(entity, "pre_block", type + "/" + posX + "/" + posY + "/" + posZ);

                        } else {

                            // Get Previous Block Data
                            {

                                pre_block_data = GameUtils.Data.getEntityText(entity, "pre_block").split("/");
                                pos_converted = OutsideUtils.convertPosRotationMirrored(rotation, mirrored, Integer.parseInt(pre_block_data[1]), Integer.parseInt(pre_block_data[3]));
                                pre_pos = new BlockPos(entity.getBlockX() + pos_converted[0], entity.getBlockY() + Integer.parseInt(pre_block_data[2]), entity.getBlockZ() + pos_converted[1]);

                                // Only Loaded Chunks
                                {

                                    if (level_server.isLoaded(pre_pos) == false) {

                                        return;

                                    }

                                }

                                pre_block = map_block.getOrDefault(pre_block_data[0], Blocks.AIR.defaultBlockState());

                            }

                            pos_converted = OutsideUtils.convertPosRotationMirrored(rotation, mirrored, posX, posZ);
                            pos = new BlockPos(entity.getBlockX() + pos_converted[0], entity.getBlockY() + posY, entity.getBlockZ() + pos_converted[1]);
                            block = map_block.get(type);

                            // Only Loaded Chunks
                            {

                                if (level_server.isLoaded(pos) == false) {

                                    return;

                                }

                            }

                            if (level_accessor.getBlockState(pre_pos).getBlock() != pre_block.getBlock()) {

                                // Missing Twig
                                {

                                    if (can_leaves_decay == true) {

                                        if (level_accessor.getBlockState(pos).getBlock() == block.getBlock()) {

                                            if (GameUtils.Tile.test(block, "#minecraft:leaves") == true) {

                                                block = GameUtils.Tile.setPropertyLogic(block, "persistent", false);
                                                GameUtils.Tile.set(level_accessor, pos, block, false);

                                            } else {

                                                level_accessor.destroyBlock(pos, false);

                                            }

                                        }

                                    }

                                }

                            } else {

                                if (can_leaves_drop == true || can_leaves_regrow == true) {

                                    updating(level_accessor, level_server, entity, pos, map_block, block, leaves_type[Integer.parseInt(type.substring(3)) - 1], biome_type, have_center_block, can_leaves_drop, can_leaves_regrow);

                                }

                            }

                        }

                    }

                }

            }

            // At the end of the file
            {

                GameUtils.Data.setEntityNumber(entity, "process_save", 0);

                if (GameUtils.Data.getEntityLogic(entity, "test_alive") == true) {

                    GameUtils.Data.setEntityLogic(entity, "test_alive", false);

                } else {

                    if (have_center_block == true) {

                        if (leaves_type[0] == 1 && leaves_type[1] == 1) {

                            if (TanshugetreesModVariables.MapVariables.get(level_accessor).season.equals("Summer") == true) {

                                entity.discard();

                            }

                        } else if (Math.random() < 0.1) {

                            entity.discard();

                        }

                    } else {

                        entity.discard();

                    }

                }

            }

        }

    }

    private static void updating (LevelAccessor level_accessor, ServerLevel level_server, Entity entity, BlockPos pos, Map<String, BlockState> map_block, BlockState block, int leaves_type, int biome_type, boolean have_center_block, boolean can_leaves_drop, boolean can_leaves_regrow) {

        boolean is_leaves = level_accessor.getBlockState(pos).getBlock() == block.getBlock();
        String current_season = TanshugetreesModVariables.MapVariables.get(level_accessor).season;
        boolean straighten = false;
        boolean can_pos_photosynthesis = false;

        // Leaves Straighten Test
        {

            if ((GameUtils.Data.getEntityNumber(entity, "straighten_highestX") != pos.getX() || GameUtils.Data.getEntityNumber(entity, "straighten_highestY") < pos.getY() || GameUtils.Data.getEntityNumber(entity, "straighten_highestZ") != pos.getZ())) {

                GameUtils.Data.setEntityNumber(entity, "straighten_highestX", pos.getX());
                GameUtils.Data.setEntityNumber(entity, "straighten_highestY", pos.getY());
                GameUtils.Data.setEntityNumber(entity, "straighten_highestZ", pos.getZ());

            } else {

                straighten = true;

            }

        }

        // Light Level Detection
        {

            if (Handcode.Config.leaf_light_level_detection <= level_accessor.getBrightness(LightLayer.SKY, pos) + 1) {

                can_pos_photosynthesis = true;

            }

        }

        if (is_leaves == true) {

            // Leaf Drop
            {

                if (can_leaves_drop == true) {

                    double chance = 0.0;

                    if (straighten == true) {

                        // Straighten
                        {

                            BlockState test = level_accessor.getBlockState(new BlockPos(pos.getX(), (int) GameUtils.Data.getEntityNumber(entity, "straighten_highestY"), pos.getZ()));

                            if (map_block.get("1201").getBlock() != test.getBlock() && map_block.get("1202").getBlock() != test.getBlock()) {

                                chance = 1.0;

                            }

                        }

                    } else if (can_pos_photosynthesis == false || have_center_block == false) {

                        chance = Handcode.Config.leaf_dead_drop_chance;

                    } else {

                        // General
                        {

                            if (leaves_type == 1) {

                                if (biome_type == 0) {

                                    // By Seasons
                                    {

                                        chance = switch (current_season) {
                                            case "Spring" -> Handcode.Config.leaf_drop_chance_spring;
                                            case "Summer" -> Handcode.Config.leaf_drop_chance_summer;
                                            case "Autumn" -> Handcode.Config.leaf_drop_chance_autumn;
                                            case "Winter" -> Handcode.Config.leaf_drop_chance_winter;
                                            default -> chance;
                                        };

                                    }

                                } else if (biome_type == 1) {

                                    chance = Handcode.Config.leaf_drop_chance_winter;

                                } else if (biome_type == 2) {

                                    chance = Handcode.Config.leaf_drop_chance_summer;

                                }

                            } else if (leaves_type == 2) {

                                // Only drop coniferous leaves in summer
                                {

                                    if (current_season.equals("Summer")) {

                                        chance = Handcode.Config.leaf_drop_chance_coniferous;

                                    }

                                }

                            } else {

                                chance = Handcode.Config.leaf_drop_chance_summer;

                            }

                        }

                    }

                    if (Math.random() < chance) {

                        {

                            GameUtils.Tile.remove(level_accessor, level_server, pos, false);

                            if (Handcode.Config.leaf_litter == true) {

                                if (Math.random() < Handcode.Config.leaf_drop_animation_chance) {

                                    // Animation
                                    {

                                        if (GameUtils.Score.get(level_server, "TANSHUGETREES", "leaf_drop") < Handcode.Config.leaf_drop_animation_count_limit) {

                                            // Don't create animation, if there's a block below.
                                            if (GameUtils.Tile.test(level_accessor.getBlockState(pos.below()), "#tanshugetrees:passable_blocks") == true) {

                                                Entity entity_summon = GameUtils.Misc.summonBlock(level_server, pos.getCenter(), "Falling Leaf", "TANSHUGETREES-leaf_drop", 0, 0, 0, 1, 1, 1, 0, 0, GameUtils.Tile.toText(block)[0]);
                                                GameUtils.Data.setEntityText(entity_summon, "block", GameUtils.Tile.toText(block)[0]);
                                                GameUtils.Score.add(level_server, "TANSHUGETREES", "leaf_drop", 1);

                                            }

                                        }

                                    }

                                } else {

                                    // No Animation
                                    {

                                        int height_motion = GameUtils.Space.getHeight(level_accessor, pos.getX(), pos.getZ(), "MOTION_BLOCKING_NO_LEAVES");

                                        if (height_motion != GameUtils.Space.getBuildHeight(level_accessor, false) && height_motion < pos.getY()) {

                                            LeafLitter.create(level_accessor, level_server, pos.atY(height_motion), block, false);

                                        }

                                    }

                                }

                            }

                        }

                    }

                }

            }

        } else if (have_center_block == true && level_accessor.getBlockState(pos).isAir() == true) {

            // Leaf Regrowth
            {

                if (can_leaves_regrow == true) {

                    double chance = 0.0;

                    if (straighten == true) {

                        // Cancel By Straighten (if no block above)
                        {

                            BlockState test = level_accessor.getBlockState(new BlockPos(pos.getX(), pos.getY() + 1, pos.getZ()));

                            if (map_block.get("1201").getBlock() != test.getBlock() && map_block.get("1202").getBlock() != test.getBlock()) {

                                return;

                            } else {

                                chance = 1.0;

                            }

                        }

                    } else if (can_pos_photosynthesis == true) {

                        // General
                        {

                            if (leaves_type == 1) {

                                if (biome_type == 0) {

                                    // By Seasons
                                    {

                                        chance = switch (current_season) {
                                            case "Spring" -> Handcode.Config.leaf_regrowth_chance_spring;
                                            case "Summer" -> Handcode.Config.leaf_regrowth_chance_summer;
                                            case "Autumn" -> Handcode.Config.leaf_regrowth_chance_autumn;
                                            case "Winter" -> Handcode.Config.leaf_regrowth_chance_winter;
                                            default -> chance;
                                        };

                                    }

                                } else if (biome_type == 1) {

                                    chance = Handcode.Config.leaf_regrowth_chance_winter;

                                } else if (biome_type == 2) {

                                    chance = Handcode.Config.leaf_regrowth_chance_summer;

                                }

                            } else if (leaves_type == 2) {

                                chance = Handcode.Config.leaf_regrowth_chance_coniferous;

                            } else {

                                chance = Handcode.Config.leaf_regrowth_chance_summer;

                            }

                        }

                    }

                    if (Math.random() < chance) {

                        {

                            block = GameUtils.Tile.setPropertyLogic(block, "persistent", true);
                            GameUtils.Tile.set(level_accessor, pos, block, false);

                        }

                    }

                }

            }

        }

        // Leaf Litter Remover
        {

            if (Handcode.Config.leaf_litter == true) {

                if (Math.random() < Handcode.Config.leaf_litter_remover_chance) {

                    if (GameUtils.Score.get(level_server, "TANSHUGETREES", "leaf_litter_remover") < Handcode.Config.leaf_litter_remover_count_limit) {

                        GameUtils.Mob.summon(level_server, pos.getCenter(), "minecraft:marker", "Leaf Litter Remover", "TANSHUGETREES-leaf_litter_remover", "{ForgeData:{tanshugetrees:{block:\"" + GameUtils.Tile.toText(block)[0] + "\"}}}");
                        GameUtils.Score.add(level_server, "TANSHUGETREES", "leaf_litter_remover", 1);

                    }

                }

            }

        }

        if (have_center_block == true && is_leaves == true) {

            GameUtils.Data.setEntityLogic(entity, "test_alive", true);

        }

    }

}