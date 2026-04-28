package tannyjung.tanshugetrees_handcode.systems.living_mechanics;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import tannyjung.tanshugetrees_core.Core;
import tannyjung.tanshugetrees_core.outside.OutsideUtils;
import tannyjung.tanshugetrees_core.game.GameUtils;
import tannyjung.tanshugetrees.network.TanshugetreesModVariables;
import tannyjung.tanshugetrees_handcode.Handcode;
import tannyjung.tanshugetrees_handcode.systems.Caches;

import java.util.*;

public class LivingMechanics {

    public static List<Entity> list_tree_location = new ArrayList<>();
    public static List<Entity> list_falling_leaf = new ArrayList<>();
    public static List<Entity> list_leaf_litter_remover = new ArrayList<>();

    public static void start (Entity entity) {

        LevelAccessor level_accessor = entity.level();
        ServerLevel level_server = (ServerLevel) level_accessor;
        BlockPos center_pos = new BlockPos(entity.getBlockX(), entity.getBlockY(), entity.getBlockZ());

        if (level_server.isLoaded(center_pos) == false) {

            return;

        }

        if (Core.developer_mode == true) {

            GameUtils.Misc.spawnParticle(level_server, entity.position(), 0, 0, 0, 0, 1, "minecraft:flash");
            GameUtils.Misc.spawnParticle(level_server, entity.position().add(0, 100, 0), 0, 25, 0, 0, 300, "minecraft:totem_of_undying");

        }

        int[] rotation_mirrored = new int[]{(int) GameUtils.Data.getEntityNumber(entity, "rotation"), (int) GameUtils.Data.getEntityNumber(entity, "mirrored")};
        boolean have_center_block = level_accessor.getBlockState(center_pos).isAir() == false;
        String path_settings = GameUtils.Data.getEntityText(entity, "tree_settings");
        short[] leaves_types = Caches.TreeSettings.getLeavesType(path_settings);
        Map<Short, BlockState> blocks = Caches.TreeSettings.getBlock(level_server, path_settings);
        Set<Block> leaves = new HashSet<>();

        // Get Leaves
        {

            BlockState get = null;
            get = blocks.get((short) 1201);

            if (get != null && get.isAir() == false) {

                leaves.add(get.getBlock());

            }

            get = blocks.get((short) 1202);

            if (get != null && get.isAir() == false) {

                leaves.add(get.getBlock());

            }

        }

        boolean can_leaves_decay = false;
        boolean can_leaves_drop = false;
        boolean can_leaves_regrow = false;

        // Get Data
        {

            Map<String, String> data_normal = Caches.TreeSettings.getNormal(path_settings);
            can_leaves_decay = data_normal.getOrDefault("can_leaves_decay", "false").equals("true");
            can_leaves_drop = data_normal.getOrDefault("can_leaves_drop", "false").equals("true");
            can_leaves_regrow = data_normal.getOrDefault("can_leaves_regrow", "false").equals("true");

        }

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

        String path_storage = "";
        String chosen = "";

        // Get Path
        {

            String[] split = GameUtils.Data.getEntityText(entity, "file").split("\\|");

            try {

                path_storage = split[0];
                chosen = split[1];

            } catch (Exception exception) {

                OutsideUtils.exception(new Exception(), exception, "");
                return;

            }

        }

        short[] pre_block_data = new short[]{0, 0, 0, 0};

        // Get Pre Block Data
        {

            String[] split = GameUtils.Data.getEntityText(entity, "pre_block").split("/");

            if (split.length > 1) {

                try {

                    pre_block_data[0] = Short.parseShort(split[0]);
                    pre_block_data[1] = Short.parseShort(split[1]);
                    pre_block_data[2] = Short.parseShort(split[2]);
                    pre_block_data[3] = Short.parseShort(split[3]);

                } catch (Exception exception) {

                    OutsideUtils.exception(new Exception(), exception, "");

                }

            }

        }

        // Scan
        {

            int process = 0;
            int process_save_min = (int) GameUtils.Data.getEntityNumber(entity, "process_save");
            int process_save_max = process_save_min + Handcode.Config.living_mechanics_process_limit;
            BlockPos pre_pos = null;
            BlockState pre_block = null;
            BlockPos pos = null;
            BlockState block = null;
            int leaves_type = 0;
            boolean break_by_process = false;

            int loop = 0;
            short type = 0;
            short posX = 0;
            short posY = 0;
            short posZ = 0;

            for (short scan : Caches.TreeShape.getTreeShapeData(path_storage + "|" + chosen)) {

                // Loop Skip
                {

                    loop = loop + 1;

                    if (loop == 1) {

                        type = scan;

                    } else if (loop == 2) {

                        posX = scan;

                    } else if (loop == 3) {

                        posY = scan;

                    } else {

                        posZ = scan;
                        loop = 0;

                    }

                    if (loop > 0) {

                        continue;

                    }

                }

                // Process Limit
                {

                    if (Handcode.Config.living_mechanics_process_limit > 0) {

                        process = process + 1;

                        if (process < process_save_min) {

                            continue;

                        } else if (process_save_max <= process) {

                            GameUtils.Data.setEntityNumber(entity, "process_save", process);
                            break_by_process = true;
                            break;

                        }

                    }

                }

                if (OutsideUtils.Mathematics.isNumberStartWith(type, 1) == true) {

                    if (OutsideUtils.Mathematics.isNumberStartWith(type, 120) == false) {

                        pre_block_data[0] = type;
                        pre_block_data[1] = posX;
                        pre_block_data[2] = posY;
                        pre_block_data[3] = posZ;

                    } else {

                        // Get Previous Block Data
                        {

                            pre_pos = new BlockPos(pre_block_data[1], pre_block_data[2], pre_block_data[3]);
                            pre_pos = OutsideUtils.convertPosRotationMirrored(pre_pos, rotation_mirrored);
                            pre_pos = pre_pos.offset(entity.getBlockX(), entity.getBlockY(), entity.getBlockZ());
                            pre_block = blocks.get(pre_block_data[0]);

                            if (pre_block == null) {

                                return;

                            }

                            // Only Loaded Chunks
                            {

                                if (level_server.isLoaded(pre_pos) == false) {

                                    return;

                                }

                            }

                        }

                        pos = new BlockPos(posX, posY, posZ);
                        pos = OutsideUtils.convertPosRotationMirrored(pos, rotation_mirrored);
                        pos = pos.offset(entity.getBlockX(), entity.getBlockY(), entity.getBlockZ());
                        block = blocks.get(type);

                        if (block == null) {

                            return;

                        }

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

                                if (type == 1201) {

                                    leaves_type = 1;

                                } else {

                                    leaves_type = 2;

                                }

                                update(level_accessor, level_server, entity, leaves, pos, block, leaves_type, biome_type, have_center_block, can_leaves_drop, can_leaves_regrow);

                            }

                        }

                    }

                }

            }

            if (break_by_process == true) {

                GameUtils.Data.setEntityText(entity, "pre_block", pre_block_data[0] + "/" + pre_block_data[1] + "/" + pre_block_data[2] + "/" + pre_block_data[3]);

            } else {

                // At the end of the file
                {

                    GameUtils.Data.setEntityNumber(entity, "process_save", 0);

                    if (GameUtils.Data.getEntityLogic(entity, "test_alive") == true) {

                        GameUtils.Data.setEntityLogic(entity, "test_alive", false);

                    } else {

                        if (have_center_block == true) {

                            if (leaves_types[0] == 1 && leaves_types[1] == 1) {

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

    }

    private static void update (LevelAccessor level_accessor, ServerLevel level_server, Entity entity, Set<Block> leaves, BlockPos pos, BlockState block, int leaves_type, int biome_type, boolean have_center_block, boolean can_leaves_drop, boolean can_leaves_regrow) {

        BlockState block_test = level_accessor.getBlockState(pos);
        boolean is_leaves = leaves.contains(block_test.getBlock()) == true;
        boolean straighten = false;
        boolean can_pos_photosynthesis = false;

        // Straighten Test
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

            // Drop
            {

                if (can_leaves_drop == true) {

                    double chance = 0.0;

                    if (straighten == true) {

                        // Straighten
                        {

                            if (leaves.contains(level_accessor.getBlockState(pos.atY((int) GameUtils.Data.getEntityNumber(entity, "straighten_highestY"))).getBlock()) == false) {

                                chance = 1.0;

                            }

                        }

                    } else if (can_pos_photosynthesis == false || have_center_block == false) {

                        chance = Handcode.Config.dead_leaf_drop_chance;

                    } else {

                        // General
                        {

                            if (leaves_type == 1) {

                                if (biome_type == 0) {

                                    // By Seasons
                                    {

                                        chance = switch (TanshugetreesModVariables.MapVariables.get(level_accessor).season) {
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

                                    if (TanshugetreesModVariables.MapVariables.get(level_accessor).season.equals("Summer") == true) {

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

                                if (Math.random() < Handcode.Config.falling_leaf_chance) {

                                    // Animation
                                    {

                                        if (list_falling_leaf.size() < Handcode.Config.falling_leaf_count_limit) {

                                            // Don't create animation, if there's a block below.
                                            if (GameUtils.Tile.test(level_accessor.getBlockState(pos.below()), "#tanshugetrees:passable_blocks") == true) {

                                                Entity entity_summon = GameUtils.Misc.summonBlock(level_server, pos.getCenter(), "Falling Leaf", "TANSHUGETREES-falling_leaf", 0, 0, 0, 1, 1, 1, 0, 0, GameUtils.Tile.toText(block)[0]);
                                                GameUtils.Data.setEntityText(entity_summon, "block", GameUtils.Tile.toText(block)[0] + GameUtils.Tile.toText(block)[1]);

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

        } else if (have_center_block == true && block_test.isAir() == true) {

            // Regrowth
            {

                if (can_leaves_regrow == true) {

                    double chance = 0.0;

                    if (straighten == true) {

                        // Cancel By Straighten (if no block above)
                        {

                            if (leaves.contains(level_accessor.getBlockState(new BlockPos(pos.getX(), pos.getY() + 1, pos.getZ()))) == true) {

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

                                        chance = switch (TanshugetreesModVariables.MapVariables.get(level_accessor).season) {
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

                        GameUtils.Tile.set(level_accessor, pos, block, false);

                    }

                }

            }

        }

        // Litter Remover
        {

            if (Handcode.Config.leaf_litter == true) {

                if (Math.random() < Handcode.Config.leaf_litter_remover_chance) {

                    if (list_leaf_litter_remover.size() < Handcode.Config.leaf_litter_remover_count_limit) {

                        GameUtils.Mob.summon(level_server, pos.getCenter(), "minecraft:marker", "Leaf Litter Remover", "TANSHUGETREES-leaf_litter_remover", "{ForgeData:{tanshugetrees:{block:\"" + GameUtils.Tile.toText(block)[0] + "\"}}}");

                    }

                }

            }

        }

        if (have_center_block == true && is_leaves == true) {

            GameUtils.Data.setEntityLogic(entity, "test_alive", true);

        }

    }

    private static class CustomEntityUpdate {

        public static void runDrop (Entity entity) {

            LevelAccessor level_accessor = entity.level();
            ServerLevel level_server = (ServerLevel) level_accessor;

            if (level_server.isLoaded(entity.blockPosition()) == false) {

                return;

            }

            BlockPos pos = BlockPos.containing(entity.position().add(0, -0.6, 0));
            boolean is_passable = GameUtils.Tile.isPassable(level_accessor, pos) == true || GameUtils.Tile.test(level_accessor.getBlockState(pos), "#tanshugetrees:passable_blocks") == true;

            if (is_passable == true && level_accessor.isWaterAt(pos) == false) {

                entity.setPos(entity.getX(), entity.getY() - 0.1, entity.getZ());

            } else {

                LeafLitter.create(level_server, level_server, pos.above(), GameUtils.Tile.fromText(level_server, GameUtils.Data.getEntityText(entity, "block")), false);
                entity.discard();

            }

        }

        public static void runLitterRemover (Entity entity) {

            LevelAccessor level_accessor = entity.level();
            ServerLevel level_server = (ServerLevel) level_accessor;

            if (level_server.isLoaded(entity.blockPosition()) == false) {

                return;

            }

            BlockPos pos = entity.blockPosition().below();
            boolean is_passable = GameUtils.Tile.isPassable(level_accessor, pos) == true || GameUtils.Tile.test(level_accessor.getBlockState(pos), "#tanshugetrees:passable_blocks") == true;

            if (is_passable == true && level_accessor.isWaterAt(pos) == false) {

                entity.setPos(entity.getX(), entity.getY() - 1,  entity.getZ());

            } else {

                LeafLitter.create(level_server, level_server, pos.above(), GameUtils.Tile.fromText(level_server, GameUtils.Data.getEntityText(entity, "block")), true);
                entity.discard();

            }

        }

    }

    public static class Loop {

        private static int tick = 0;

        public static void runTick () {

            if (Handcode.Config.living_mechanics == true) {

                if (list_tree_location.isEmpty() == false) {

                    // Main
                    {

                        tick = tick + 1;

                        if (tick >= Handcode.Config.living_mechanics_tick) {

                            tick = 0;

                            if (Math.random() < (double) list_tree_location.size() / (double) Handcode.Config.living_mechanics_simulation) {

                                LivingMechanics.start(list_tree_location.get(Mth.nextInt(RandomSource.create(), 0, list_tree_location.size() - 1)));

                            }

                        }

                    }

                }

                if (list_falling_leaf.isEmpty() == false) {

                    for (Entity entity : list_falling_leaf) {

                        if (entity.isRemoved() == true) {

                            continue;

                        }

                        CustomEntityUpdate.runDrop(entity);

                    }

                }

                if (list_leaf_litter_remover.isEmpty() == false) {

                    for (Entity entity : list_leaf_litter_remover) {

                        if (entity.isRemoved() == true) {

                            continue;

                        }

                        CustomEntityUpdate.runLitterRemover(entity);

                    }

                }

            }

        }

        public static void runSecond (ServerLevel level_server) {

            if (Handcode.Config.living_mechanics == true) {

                list_tree_location = GameUtils.Mob.getAtEverywhere(level_server, "minecraft:marker", "TANSHUGETREES-tree_location");
                list_falling_leaf = GameUtils.Mob.getAtEverywhere(level_server, "minecraft:block_display", "TANSHUGETREES-falling_leaf");
                list_leaf_litter_remover = GameUtils.Mob.getAtEverywhere(level_server, "minecraft:marker", "TANSHUGETREES-leaf_litter_remover");

            }

        }

    }

}