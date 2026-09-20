package tannyjung.tanshugetrees_handcode.systems.living_mechanics;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import tannyjung.tanshugetrees_core.Core;
import tannyjung.tanshugetrees_core.game.*;
import tannyjung.tanshugetrees_core.outside.OutsideUtils;
import tannyjung.tanshugetrees.network.TanshugetreesModVariables;
import tannyjung.tanshugetrees_handcode.Handcode;
import tannyjung.tanshugetrees_handcode.systems.Caches;

import java.util.*;

public class LivingMechanics {

    public static List<Entity> list_tree_location = new ArrayList<>();
    public static List<Entity> list_falling_leaf = new ArrayList<>();
    public static List<Entity> list_leaf_litter_remover = new ArrayList<>();

    public static void start (Entity entity) {

        ServerLevel level_server = (ServerLevel) entity.level();
        BlockPos center_pos = new BlockPos(entity.getBlockX(), entity.getBlockY(), entity.getBlockZ());

        if (level_server.isLoaded(center_pos) == false) {

            return;

        }

        if (Core.developer_mode == true) {

            GameUtils.spawnParticle(level_server, entity.position(), 0, 0, 0, 0, 1, "minecraft:flash");
            GameUtils.spawnParticle(level_server, entity.position().add(0, 100, 0), 0, 25, 0, 0, 300, "minecraft:totem_of_undying");

        }

        int[] rotation_mirrored = new int[]{(int) NBTManager.Mob.getNumber(entity, "main", "rotation"), (int) NBTManager.Mob.getNumber(entity, "main", "mirrored")};
        boolean have_center_block = level_server.getBlockState(center_pos).isAir() == false;
        String path_palettes = NBTManager.Mob.getText(entity, "main", "tree_palettes");
        short[] leaves_types = Caches.TreePalettes.getLeavesType(path_palettes);
        Map<Short, BlockState> blocks = Caches.TreePalettes.getBlock(level_server, path_palettes);
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

            Map<String, String> data_normal = Caches.TreePalettes.getNormal(path_palettes);
            can_leaves_decay = data_normal.getOrDefault("can_leaves_decay", "false").equals("true");
            can_leaves_drop = data_normal.getOrDefault("can_leaves_drop", "false").equals("true");
            can_leaves_regrow = data_normal.getOrDefault("can_leaves_regrow", "false").equals("true");

        }

        int biome_type = 0;

        // Biome Type Test
        {

            Holder<Biome> biome = GameUtils.Environment.getAt(level_server, center_pos);

            if (GameUtils.Environment.test(biome, "#tanshugetrees:snowy_biomes") == true) {

                biome_type = 1;

            } else if (GameUtils.Environment.test(biome, "#tanshugetrees:tropical_biomes") == true) {

                biome_type = 2;

            }

        }

        String path_shapes = "";
        String chosen = "";

        // Get Path
        {

            String[] split = NBTManager.Mob.getText(entity, "main", "file").split("\\|");

            try {

                path_shapes = split[0];
                chosen = split[1];

            } catch (Exception exception) {

                OutsideUtils.exception(new Exception(), exception, "");
                return;

            }

        }

        short[] pre_block_data = new short[]{0, 0, 0, 0};

        // Get Pre Block Data
        {

            String[] split = NBTManager.Mob.getText(entity, "main", "pre_block").split("/");

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
            int process_save_min = (int) NBTManager.Mob.getNumber(entity, "main", "process_save");
            int process_save_max = process_save_min + Handcode.Config.living_mechanics_process_limit;
            ChunkPos chunk_pos = null;
            BlockPos pre_pos = null;
            BlockState pre_block = null;
            BlockPos pos = null;
            BlockState block = null;
            int leaves_type = 0;
            boolean break_by_process = false;

            Map<ChunkPos, Set<int[]>> shadow_reduction = new HashMap<>();

            int loop = 0;
            short type = 0;
            short posX = 0;
            short posY = 0;
            short posZ = 0;

            for (short scan : Caches.TreeShape.getTreeShapeData(path_shapes + "|" + chosen)) {

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

                            NBTManager.Mob.setNumber(entity, "main", "process_save", process, false);
                            break_by_process = true;
                            break;

                        }

                    }

                }

                if (OutsideUtils.Mathematic.isNumberStartWith(type, 1) == true) {

                    if (OutsideUtils.Mathematic.isNumberStartWith(type, 120) == false) {

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
                        chunk_pos = new ChunkPos(pos.getX() >> 4, pos.getZ() >> 4);

                        if (block == null) {

                            return;

                        }

                        // Only Loaded Chunks
                        {

                            if (level_server.isLoaded(pos) == false) {

                                return;

                            }

                        }

                        if (testForceDecay(level_server, pos, chunk_pos, pre_pos, pre_block, shadow_reduction, can_leaves_drop)) {

                            // Force Decay
                            {

                                if (can_leaves_decay == true) {

                                    if (level_server.getBlockState(pos).getBlock() == block.getBlock()) {

                                        if (BlockManager.test(block, "#minecraft:leaves") == true) {

                                            block = BlockManager.setPropertyLogic(block, "persistent", false);
                                            BlockManager.set(level_server, pos, block, false);

                                        } else {

                                            level_server.destroyBlock(pos, false);

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

                                update(level_server, entity, leaves, pos, block, leaves_type, biome_type, have_center_block, can_leaves_drop, can_leaves_regrow);

                            }

                        }

                    }

                }

            }

            if (break_by_process == true) {

                NBTManager.Mob.setText(entity, "main", "pre_block", pre_block_data[0] + "/" + pre_block_data[1] + "/" + pre_block_data[2] + "/" + pre_block_data[3], false);

            } else {

                // At the end of the file
                {

                    NBTManager.Mob.setNumber(entity, "main", "process_save", 0, false);

                    if (NBTManager.Mob.getLogic(entity, "main", "test_alive") == true) {

                        NBTManager.Mob.setLogic(entity, "main", "test_alive", false, false);

                    } else {

                        if (have_center_block == true) {

                            if (leaves_types[0] == 1 && leaves_types[1] == 1) {

                                if (TanshugetreesModVariables.MapVariables.get(level_server).season.equals("Summer") == true) {

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

    private static boolean testForceDecay (ServerLevel level_server, BlockPos pos, ChunkPos chunk_pos, BlockPos pre_pos, BlockState pre_block, Map<ChunkPos, Set<int[]>> shadow_reduction, boolean can_leaves_drop) {

        // Missing Twig
        {

            if (level_server.getBlockState(pre_pos).getBlock() != pre_block.getBlock()) {

                return true;

            }

        }

        // Shadow Reduction
        {

            if (Handcode.Config.shadow_reduction != 0) {

                if (can_leaves_drop == true) {

                    Set<int[]> shadow_reduction_data = shadow_reduction.get(chunk_pos);

                    if (shadow_reduction_data == null) {

                        shadow_reduction_data = new HashSet<>();
                        RandomSource random = RandomSource.create(level_server.getSeed() ^ ((pos.getX() * 341873128712L) + (pos.getZ() * 132897987541L)));

                        while (shadow_reduction_data.size() < Handcode.Config.shadow_reduction) {

                            shadow_reduction_data.add(new int[]{random.nextInt(16) + (chunk_pos.x * 16), random.nextInt(16) + (chunk_pos.z * 16)});

                        }

                        shadow_reduction.put(chunk_pos, shadow_reduction_data);

                    }

                    for (int[] test : shadow_reduction_data) {

                        if (test[0] == pos.getX() && test[1] == pos.getZ()) {

                            return true;

                        }

                    }

                }

            }

        }

        return false;

    }

    private static void update (ServerLevel level_server, Entity entity, Set<Block> leaves, BlockPos pos, BlockState block, int leaves_type, int biome_type, boolean have_center_block, boolean can_leaves_drop, boolean can_leaves_regrow) {

        BlockState block_test = level_server.getBlockState(pos);
        boolean is_leaves = leaves.contains(block_test.getBlock()) == true;
        boolean straighten = false;
        boolean can_pos_photosynthesis = false;

        // Straighten Test
        {

            if ((NBTManager.Mob.getNumber(entity, "main", "straighten_highestX") != pos.getX() || NBTManager.Mob.getNumber(entity, "main", "straighten_highestY") < pos.getY() || NBTManager.Mob.getNumber(entity, "main", "straighten_highestZ") != pos.getZ())) {

                NBTManager.Mob.setNumber(entity, "main", "straighten_highestX", pos.getX(), false);
                NBTManager.Mob.setNumber(entity, "main", "straighten_highestY", pos.getY(), false);
                NBTManager.Mob.setNumber(entity, "main", "straighten_highestZ", pos.getZ(), false);

            } else {

                straighten = true;

            }

        }

        // Light Level Detection
        {

            if (Handcode.Config.leaf_light_level_detection <= level_server.getBrightness(LightLayer.SKY, pos) + 1) {

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

                            if (leaves.contains(level_server.getBlockState(pos.atY((int) NBTManager.Mob.getNumber(entity, "main", "straighten_highestY"))).getBlock()) == false) {

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

                                        chance = switch (TanshugetreesModVariables.MapVariables.get(level_server).season) {
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

                                    if (TanshugetreesModVariables.MapVariables.get(level_server).season.equals("Summer") == true) {

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

                            BlockManager.remove(level_server, level_server, pos, false);

                            if (Handcode.Config.leaf_litter == true) {

                                if (Math.random() < Handcode.Config.falling_leaf_chance) {

                                    // Animation
                                    {

                                        if (list_falling_leaf.size() < Handcode.Config.falling_leaf_count_limit) {

                                            // Don't create animation, if there's a block below.
                                            if (BlockManager.test(level_server.getBlockState(pos.below()), "#tanshugetrees:passable_blocks") == true) {

                                                Entity entity_summon = DisplayManager.summonItem(level_server, pos.getCenter(), 0, 0, 1, false, "Falling Leaf", new String[]{"TANSHUGETREES-falling_leaf"}, BlockManager.toText(block)[0]);
                                                NBTManager.Mob.setText(entity_summon, "main", "block", BlockManager.toText(block)[0] + BlockManager.toText(block)[1], false);

                                            }

                                        }

                                    }

                                } else {

                                    // No Animation
                                    {

                                        int height_motion = level_server.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, pos.getX(), pos.getZ());

                                        if (height_motion != GameUtils.Space.getBuildHeight(level_server, false) && height_motion < pos.getY()) {

                                            LeafLitter.create(level_server, level_server, pos.atY(height_motion), block, false);

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

                            if (leaves.contains(level_server.getBlockState(new BlockPos(pos.getX(), pos.getY() + 1, pos.getZ()))) == true) {

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

                                        chance = switch (TanshugetreesModVariables.MapVariables.get(level_server).season) {
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

                        BlockManager.set(level_server, pos, block, false);

                    }

                }

            }

        }

        // Litter Remover
        {

            if (Handcode.Config.leaf_litter == true) {

                if (Math.random() < Handcode.Config.leaf_litter_remover_chance) {

                    if (list_leaf_litter_remover.size() < Handcode.Config.leaf_litter_remover_count_limit) {

                        EntityManager.summon(level_server, pos.getCenter(), false, "minecraft:marker", "Leaf Litter Remover", new String[]{"TANSHUGETREES-leaf_litter_remover"}, "{NeoForgeData:{tanshugetrees:{block:\"" + BlockManager.toText(block)[0] + "\"}}}");

                    }

                }

            }

        }

        if (have_center_block == true && is_leaves == true) {

            NBTManager.Mob.setLogic(entity, "main", "test_alive", true, false);

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
            boolean is_passable = BlockManager.isPassable(level_accessor, pos) == true || BlockManager.test(level_accessor.getBlockState(pos), "#tanshugetrees:passable_blocks") == true;

            if (is_passable == true && level_accessor.isWaterAt(pos) == false) {

                entity.setPos(entity.getX(), entity.getY() - 0.1, entity.getZ());

            } else {

                LeafLitter.create(level_accessor, level_server, pos.above(), BlockManager.fromText(level_server, NBTManager.Mob.getText(entity, "main", "block")), false);
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
            boolean is_passable = BlockManager.isPassable(level_accessor, pos) == true || BlockManager.test(level_accessor.getBlockState(pos), "#tanshugetrees:passable_blocks") == true;

            if (is_passable == true && level_accessor.isWaterAt(pos) == false) {

                entity.setPos(entity.getX(), entity.getY() - 1,  entity.getZ());

            } else {

                LeafLitter.create(level_accessor, level_server, pos.above(), BlockManager.fromText(level_server, NBTManager.Mob.getText(entity, "main", "block")), true);
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

                        CustomEntityUpdate.runDrop(entity);

                    }

                }

                if (list_leaf_litter_remover.isEmpty() == false) {

                    for (Entity entity : list_leaf_litter_remover) {

                        CustomEntityUpdate.runLitterRemover(entity);

                    }

                }

            }

        }

        public static void runSecond (ServerLevel level_server) {

            if (Handcode.Config.living_mechanics == true) {

                list_tree_location = EntityManager.Population.getEverywhereStatic(level_server, "", "", new String[]{"TANSHUGETREES-tree_location"});
                list_falling_leaf = EntityManager.Population.getEverywhereStatic(level_server, "", "", new String[]{"TANSHUGETREES-falling_leaf"});
                list_leaf_litter_remover = EntityManager.Population.getEverywhereStatic(level_server, "", "", new String[]{"TANSHUGETREES-leaf_litter_remover"});

            }

        }

    }

}