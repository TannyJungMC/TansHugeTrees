package tannyjung.tanshugetrees_handcode.systems.world_gen;

import it.unimi.dsi.fastutil.longs.LongSet;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.structure.Structure;
import tannyjung.tanshugetrees_core.Core;
import tannyjung.tanshugetrees_core.outside.*;
import tannyjung.tanshugetrees_core.game.GameUtils;
import tannyjung.tanshugetrees_handcode.Handcode;
import tannyjung.tanshugetrees_handcode.systems.Caches;
import tannyjung.tanshugetrees_handcode.systems.living_mechanics.LeafLitter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.ByteBuffer;
import java.util.*;

public class TreePlacer {

    public static void start (LevelAccessor level_accessor, ServerLevel level_server, ChunkGenerator chunk_generator, String dimension, ChunkPos chunk_pos) {

        Core.GlobalLocking.test();

        ByteBuffer data = Data.get(dimension, chunk_pos);
        String id = "";
        String chosen = "";
        int centerX = 0;
        int centerZ = 0;
        int from_chunkX = 0;
        int from_chunkZ = 0;
        int to_chunkX = 0;
        int to_chunkZ = 0;

        while (data.remaining() > 0) {

            // Get data
            {

                try {

                    id = CacheManager.getDictionary(String.valueOf(data.getShort()), true);
                    chosen = CacheManager.getDictionary(String.valueOf(data.getShort()), true);
                    centerX = data.getInt();
                    centerZ = data.getInt();
                    from_chunkX = data.getInt();
                    from_chunkZ = data.getInt();
                    to_chunkX = data.getInt();
                    to_chunkZ = data.getInt();

                } catch (Exception exception) {

                    OutsideUtils.exception(new Exception(), exception, "");
                    return;

                }

            }

            DetailedDetection.test(level_accessor, level_server, chunk_generator, dimension, chunk_pos, from_chunkX, from_chunkZ, to_chunkX, to_chunkZ, id, chosen, centerX, centerZ);

        }

        LeafLitterGeneration.place(level_accessor, level_server, chunk_generator, chunk_pos);
        Function.run(level_accessor, level_server, chunk_pos);
        Data.clearChunk(dimension, chunk_pos);

    }

    private static int[] getPartReduce (LevelAccessor level_accessor, String location, int centerX, int centerZ, int dead_tree_level) {

        RandomSource random = RandomSource.create(level_accessor.getServer().overworld().getSeed() ^ ((centerX * 341873128712L) + (centerZ * 132897987541L)));
        int count_trunk = 0;
        int count_bough = 0;
        int count_branch = 0;
        int count_limb = 0;
        int count_twig = 0;
        int count_sprig = 0;

        // Get Data
        {

            try {

                int[] data = Caches.TreeShape.getTreeShapeBlockCount(location);
                count_trunk = data[0];
                count_bough = data[1];
                count_branch = data[2];
                count_limb = data[3];
                count_twig = data[4];
                count_sprig = data[5];

            } catch (Exception exception) {

                OutsideUtils.exception(new Exception(), exception, "");
                return new int[0];

            }

        }

        // Convert Dead Level
        {

            if (dead_tree_level < 200) {

                dead_tree_level = dead_tree_level - 100;

            } else if (dead_tree_level < 300) {

                dead_tree_level = dead_tree_level - 200;

            } else if (dead_tree_level < 400) {

                dead_tree_level = dead_tree_level - 300;

            }

        }

        if (dead_tree_level >= 60) {

            // Only Trunk
            {

                count_bough = 0;
                count_branch = 0;
                count_limb = 0;
                count_twig = 0;
                count_sprig = 0;

                if (dead_tree_level == 60 || dead_tree_level == 70) {

                    count_trunk = (int) (Mth.nextDouble(random, 0.5, 1.0) * (double) count_trunk);

                } else if (dead_tree_level == 80 || dead_tree_level == 90) {

                    count_trunk = (int) (Mth.nextDouble(random, 0.1, 0.5) * (double) count_trunk);

                }

            }

        } else {

            // General
            {

                if (dead_tree_level == 50) {

                    count_bough = (int) (random.nextDouble() * count_bough);

                }

                if (dead_tree_level < 50) {

                    if (dead_tree_level == 40) {

                        count_branch = (int) (random.nextDouble() * count_branch);

                    }

                } else {

                    count_branch = 0;

                }

                if (dead_tree_level < 40) {

                    if (dead_tree_level == 30 && count_limb > 0) {

                        count_limb = (int) (random.nextDouble() * count_limb);

                    }

                } else {

                    count_limb = 0;

                }

                if (dead_tree_level < 30) {

                    if (dead_tree_level == 20 && count_twig > 0) {

                        count_twig = (int) (random.nextDouble() * count_twig);

                    }

                } else {

                    count_twig = 0;

                }

                if (dead_tree_level < 20) {

                    if (dead_tree_level == 10 && count_sprig > 0) {

                        count_sprig = (int) (random.nextDouble() * count_sprig);

                    }

                } else {

                    count_sprig = 0;

                }

            }

        }

        return new int[]{count_trunk, count_bough, count_branch, count_limb, count_twig, count_sprig};

    }

    private static void place (LevelAccessor level_accessor, ServerLevel level_server, ChunkPos chunk_pos, String id, String location, String path_settings, BlockPos pos_center, int[] rotation_mirrored, int dead_tree_level, int fallen_direction) {

        boolean can_disable_roots = false;
        boolean can_leaves_decay = false;
        boolean can_leaves_drop = false;
        boolean can_leaves_regrow = false;

        // Get Data
        {

            Map<String, String> data_normal = Caches.TreeSettings.getNormal(path_settings);
            can_disable_roots = data_normal.getOrDefault("can_disable_roots", "").equals("true");
            can_leaves_decay = data_normal.getOrDefault("can_leaves_decay", "").equals("true");
            can_leaves_drop = data_normal.getOrDefault("can_leaves_drop", "").equals("true");
            can_leaves_regrow = data_normal.getOrDefault("can_leaves_regrow", "").equals("true");

        }

        Map<Short, BlockState> blocks = Caches.TreeSettings.getBlock(level_server, path_settings);
        Set<Short> keep = Caches.TreeSettings.getKeep(path_settings);
        short[] leaves_type = Caches.TreeSettings.getLeavesType(path_settings);
        Map<Short, String> functions = Caches.TreeSettings.getFunction(path_settings);
        List<String> tree_decoration_normal = getTreeDecoration("normal");
        List<String> tree_decoration_decay = getTreeDecoration("decay");

        boolean coarse_woody_debris = false;
        boolean no_roots = false;
        boolean hollowed = false;
        boolean abscission = false;
        int reduce_trunk = 0;
        int reduce_bough = 0;
        int reduce_branch = 0;
        int reduce_limb = 0;
        int reduce_twig = 0;
        int reduce_sprig = 0;

        if (dead_tree_level == 0) {

            {

                no_roots = Handcode.Config.world_gen_roots == false && can_disable_roots == true;

                if (Handcode.Config.abscission_world_gen == true) {

                    if (leaves_type[0] == 1 || leaves_type[1] == 1) {

                        if (GameUtils.Environment.test(GameUtils.Environment.getAt(level_accessor, pos_center), "#tanshugetrees:snowy_biomes") == true) {

                            abscission = true;

                        }

                    }

                }

            }

        } else {

            {

                // Get Reduce
                {

                    try {

                        int[] data = getPartReduce(level_accessor, location, pos_center.getX(), pos_center.getZ(), dead_tree_level);
                        reduce_trunk = data[0];
                        reduce_bough = data[1];
                        reduce_branch = data[2];
                        reduce_limb = data[3];
                        reduce_twig = data[4];
                        reduce_sprig = data[5];

                    } catch (Exception exception) {

                        OutsideUtils.exception(new Exception(), exception, "");
                        return;

                    }

                }

                boolean force_no_roots = false;

                if (dead_tree_level < 200) {

                    dead_tree_level = dead_tree_level - 100;

                } else if (dead_tree_level < 300) {

                    dead_tree_level = dead_tree_level - 200;
                    coarse_woody_debris = true;

                } else if (dead_tree_level < 400) {

                    dead_tree_level = dead_tree_level - 300;
                    coarse_woody_debris = true;
                    force_no_roots = true;

                }

                if (dead_tree_level == 70 || dead_tree_level == 90) {

                    hollowed = true;

                }

                if (force_no_roots == true) {

                    no_roots = true;

                }

            }

        }

        boolean can_run_function = false;
        BlockState block = null;
        String function = "";
        BlockPos pos = null;
        boolean is_leaves = false;
        boolean is_function = false;
        double leaf_litter_chance = 0.0;

        int loop = 0;
        short type = 0;
        short posX = 0;
        short posY = 0;
        short posZ = 0;

        for (short scan : Caches.TreeShape.getTreeShapeData(location)) {

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

            is_leaves = OutsideUtils.Math.isNumberStartWith(type, 120) == true;
            is_function = OutsideUtils.Math.isNumberStartWith(type, 2) == true;

            if (is_function == false) {

                can_run_function = false;

                // Dead Tree Reduction
                {

                    if (dead_tree_level > 0) {

                        if (is_leaves == true) {

                            continue;

                        } else {

                            // Basic Style
                            {

                                if (OutsideUtils.Math.isNumberStartWith(type, 119) == true) {

                                    if (reduce_sprig > 0) {

                                        reduce_sprig = reduce_sprig - 1;

                                    } else {

                                        continue;

                                    }

                                } else if (OutsideUtils.Math.isNumberStartWith(type, 118) == true) {

                                    if (reduce_sprig == 0) {

                                        if (reduce_twig > 0) {

                                            reduce_twig = reduce_twig - 1;

                                        } else {

                                            continue;

                                        }

                                    }

                                } else if (OutsideUtils.Math.isNumberStartWith(type, 117) == true) {

                                    if (reduce_twig == 0) {

                                        if (reduce_limb > 0) {

                                            reduce_limb = reduce_limb - 1;

                                        } else {

                                            continue;

                                        }

                                    }

                                } else if (OutsideUtils.Math.isNumberStartWith(type, 116) == true) {

                                    if (reduce_limb == 0) {

                                        if (reduce_branch > 0) {

                                            reduce_branch = reduce_branch - 1;

                                        } else {

                                            continue;

                                        }

                                    }

                                } else if (OutsideUtils.Math.isNumberStartWith(type, 115) == true) {

                                    if (reduce_branch == 0) {

                                        if (reduce_bough > 0) {

                                            reduce_bough = reduce_bough - 1;

                                        } else {

                                            continue;

                                        }

                                    }

                                }

                            }

                            if (dead_tree_level >= 60) {

                                // Only Trunk
                                {

                                    if (OutsideUtils.Math.isNumberStartWith(type, 114) == true) {

                                        if (reduce_trunk > 0) {

                                            reduce_trunk = reduce_trunk - 1;

                                            if (hollowed == true) {

                                                if (type == 1143) {

                                                    continue;

                                                }

                                            }

                                        } else {

                                            continue;

                                        }

                                    }

                                }

                            }

                        }

                    }

                }

            }

            pos = new BlockPos(posX, posY, posZ);
            pos = OutsideUtils.convertPosRotationMirrored(pos, rotation_mirrored);
            pos = OutsideUtils.convertPosFallen(pos, fallen_direction);
            pos = pos.offset(pos_center.getX(), pos_center.getY(), pos_center.getZ());

            if (chunk_pos.x == pos.getX() >> 4 && chunk_pos.z == pos.getZ() >> 4) {

                if (is_function == false) {

                    block = blocks.get(type);

                    if (block == null) {

                        continue;

                    }

                    // Keep
                    {

                        if (keep.contains(type) == true) {

                            if (level_accessor.getBlockState(pos).isAir() == false) {

                                continue;

                            }

                        }

                    }

                    // No Roots
                    {

                        if (coarse_woody_debris == false) {

                            if (no_roots == true) {

                                if (OutsideUtils.Math.isNumberStartWith(type, 111) == true || OutsideUtils.Math.isNumberStartWith(type, 112) == true || OutsideUtils.Math.isNumberStartWith(type, 113) == true) {

                                    continue;

                                }

                            }

                        } else {

                            if (OutsideUtils.Math.isNumberStartWith(type, 112) == true || OutsideUtils.Math.isNumberStartWith(type, 113) == true) {

                                continue;

                            }

                            if (no_roots == true) {

                                if (OutsideUtils.Math.isNumberStartWith(type, 110) == true || OutsideUtils.Math.isNumberStartWith(type, 111) == true) {

                                    continue;

                                }

                            }

                        }

                    }

                    RandomSource random = RandomSource.create(level_accessor.getServer().overworld().getSeed() ^ ((pos.getX() * 341873128712L) + (pos.getZ() * 132897987541L)) * pos.getY());

                    if (is_leaves == true) {

                        // Leaf Litter
                        {

                            if (can_leaves_drop == true) {

                                if (Handcode.Config.leaf_litter == true && Handcode.Config.leaf_litter_world_gen == true) {

                                    if ((OutsideUtils.Math.isNumberEndWith(type, 1) == true && leaves_type[0] == 2) || (OutsideUtils.Math.isNumberEndWith(type, 2) == true && leaves_type[1] == 2)) {

                                        leaf_litter_chance = Handcode.Config.leaf_litter_world_gen_chance_coniferous;

                                    } else {

                                        leaf_litter_chance = Handcode.Config.leaf_litter_world_gen_chance;

                                    }

                                    if (random.nextDouble() < leaf_litter_chance) {

                                        LeafLitterGeneration.add(chunk_pos, pos, block);

                                    }

                                }
                            }

                        }

                        // Abscission
                        {

                            if (abscission == true) {

                                continue;

                            }

                        }

                    }

                    GameUtils.Tile.set(level_accessor, pos, block, true);

                    // Tree Decoration
                    {

                        if (Handcode.Config.tree_decorations == true) {

                            if (is_leaves == false) {

                                if (random.nextDouble() < Handcode.Config.tree_decorations_normal_chance) {

                                    if (tree_decoration_normal.isEmpty() == false) {

                                        Function.add(chunk_pos, pos, tree_decoration_normal.get(random.nextInt(tree_decoration_normal.size())));

                                    }

                                }

                            }

                            if (dead_tree_level > 0) {

                                if (random.nextDouble() < Handcode.Config.tree_decorations_decay_chance) {

                                    if (tree_decoration_decay.isEmpty() == false) {

                                        Function.add(chunk_pos, pos, tree_decoration_decay.get(random.nextInt(tree_decoration_decay.size())));

                                    }

                                }

                            }

                        }

                    }

                    // Summon Marker
                    {

                        // At Center
                        if (posX == 0 && posY == 0 && posZ == 0) {

                            if (Handcode.Config.tree_location == true && dead_tree_level == 0) {

                                if (can_leaves_decay == true || can_leaves_drop == true || can_leaves_regrow == true) {

                                    String marker_data = "{ForgeData:{tanshugetrees:{file:\"" + location + "\",tree_settings:\"" + path_settings + "\",rotation:" + rotation_mirrored[0] + ",mirrored:" + rotation_mirrored[1] + "}}}";
                                    GameUtils.Mob.summonWorldGen(level_server, pos.getCenter(), "minecraft:marker", id, "TANSHUGETREES-tree_location", marker_data);

                                }

                            }

                        }

                    }

                    can_run_function = true;

                } else {

                    // Function
                    {

                        // Separate because start and end function no need to test "can run here?"
                        if (can_run_function == true || type == 210 || type == 220) {

                            function = functions.get(type);

                            if (function == null) {

                                continue;

                            }

                            Function.add(chunk_pos, pos, function);

                        }

                    }

                }

            }

        }

    }

    private static List<String> getTreeDecoration (String type) {

        List<String> data = CacheManager.DataText.getList("tree_decorations").get(type);

        if (data == null) {

            data = new ArrayList<>();
            String[] names = null;

            // Get List of Names
            {

                if (type.equals("normal") == true) {

                    names = new File(Core.path_config + "/dev/temporary/tree_decorations").list();

                } else {

                    names = new File(Core.path_config + "/dev/temporary/tree_decorations/decay").list();

                }

                if (names == null) {

                    names = new String[0];

                }

            }

            for (String name : names) {

                if (name.endsWith(".txt") == true) {

                    name = name.substring(0, name.length() - ".txt".length());

                    if (type.equals("normal") == true) {

                        name = "tree_decorations/" + name;

                    } else {

                        name = "tree_decorations/decay/" + name;

                    }

                    data.add(name);

                }

            }

            CacheManager.DataText.setList("tree_decorations", type, data);

        }

        return data;

    }

    public static class Data {

        private static final Object lock = new Object();
        private static final Map<String, Map<ChunkPos, ByteArrayOutputStream>> bin_convert = new HashMap<>();

        public static void clear () {

            synchronized (lock) {

                bin_convert.clear();

            }

        }

        private static void clearChunk (String dimension, ChunkPos chunk_pos) {

            int regionX = chunk_pos.x >> 5;
            int regionZ = chunk_pos.z >> 5;
            String key = dimension + "/" + regionX + "," + regionZ;

            synchronized (lock) {

                Map<ChunkPos, ByteArrayOutputStream> data = bin_convert.get(key);

                if (data != null) {

                    data.remove(chunk_pos);

                }

            }

        }

        private static ByteBuffer get (String dimension, ChunkPos chunk_pos) {

            int regionX = chunk_pos.x >> 5;
            int regionZ = chunk_pos.z >> 5;
            String key = dimension + "/" + regionX + "," + regionZ;

            synchronized (lock) {

                Map<ChunkPos, ByteArrayOutputStream> data = bin_convert.get(key);

                if (data == null) {

                    bin_convert.put(key, new HashMap<>());

                    // Convert Region
                    {

                        ByteBuffer bin = FileManager.readBIN(Core.path_world_mod + "/world_gen/place/" + key + ".bin");
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        int from_chunkX = 0;
                        int from_chunkZ = 0;
                        int to_chunkX = 0;
                        int to_chunkZ = 0;
                        short id = 0;
                        short chosen = 0;
                        int centerX = 0;
                        int centerZ = 0;

                        while (bin.remaining() > 0) {

                            // Get Data
                            {

                                try {

                                    id = bin.getShort();
                                    chosen = bin.getShort();
                                    centerX = bin.getInt();
                                    centerZ = bin.getInt();
                                    from_chunkX = bin.getInt();
                                    from_chunkZ = bin.getInt();
                                    to_chunkX = bin.getInt();
                                    to_chunkZ = bin.getInt();

                                    stream.reset();
                                    stream.write(OutsideUtils.Data.convertShortToArrayByte(id));
                                    stream.write(OutsideUtils.Data.convertShortToArrayByte(chosen));
                                    stream.write(OutsideUtils.Data.convertIntToArrayByte(centerX));
                                    stream.write(OutsideUtils.Data.convertIntToArrayByte(centerZ));
                                    stream.write(OutsideUtils.Data.convertIntToArrayByte(from_chunkX));
                                    stream.write(OutsideUtils.Data.convertIntToArrayByte(from_chunkZ));
                                    stream.write(OutsideUtils.Data.convertIntToArrayByte(to_chunkX));
                                    stream.write(OutsideUtils.Data.convertIntToArrayByte(to_chunkZ));

                                    for (int scanX = from_chunkX; scanX <= to_chunkX; scanX++) {

                                        for (int scanZ = from_chunkZ; scanZ <= to_chunkZ; scanZ++) {

                                            if (regionX == scanX >> 5 && regionZ == scanZ >> 5) {

                                                bin_convert.get(key).computeIfAbsent(new ChunkPos(scanX, scanZ), create -> new ByteArrayOutputStream()).write(stream.toByteArray());

                                            }

                                        }

                                    }

                                } catch (Exception exception) {

                                    OutsideUtils.exception(new Exception(), exception, "");
                                    return ByteBuffer.allocate(0);

                                }

                            }

                        }

                    }

                    data = bin_convert.get(key);

                }

                ByteArrayOutputStream stream = data.get(chunk_pos);

                if (stream == null) {

                    return ByteBuffer.allocate(0);

                } else {

                    return ByteBuffer.wrap(stream.toByteArray());

                }

            }

        }

    }

    private static class DetailedDetection {

        private static final Object lock = new Object();

        private static void test (LevelAccessor level_accessor, ServerLevel level_server, ChunkGenerator chunk_generator, String dimension, ChunkPos chunk_pos, int from_chunkX, int from_chunkZ, int to_chunkX, int to_chunkZ, String id, String chosen, int centerX, int centerZ) {

            String location = "";
            String path_settings = "";
            String ground_block = "";
            String[] start_height_offset = null;

            // Get Config Data
            {

                Map<String, String> config = ConfigDynamic.getData("world_gen").get(id);

                if (config == null) {

                    return;

                }

                location = config.get("path_storage") + "|" + chosen;
                path_settings = config.get("path_settings");
                ground_block = config.get("ground_block");
                start_height_offset = config.get("start_height_offset").split(" <> ");

            }

            int dead_tree_level = TreeLocation.getDeadTreeLevel(level_accessor, id, location, centerX, centerZ, false);
            int fallen_direction = 0;

            if (dead_tree_level > 200) {

                fallen_direction = TreeLocation.getFallenDirection(level_accessor, centerX, centerZ);

            }

            int[] rotation_mirrored = TreeLocation.getRotationMirrored(level_accessor, centerX, centerZ, id);

            if (rotation_mirrored == null) {

                return;

            }

            BlockPos pos_center = new BlockPos(centerX, 0, centerZ);
            boolean pass = false;

            synchronized (lock) {

                boolean already_tested = false;

                // Is Already Tested
                {

                    ByteBuffer data = FileManager.readBIN(Core.path_world_mod + "/world_gen/detailed_detection/" + dimension + "/" + (chunk_pos.x >> 5) + "," + (chunk_pos.z >> 5) + ".bin");
                    boolean get_pass = false;
                    int get_posX = 0;
                    int get_posY = 0;
                    int get_posZ = 0;
                    int get_dead_tree_level = 0;

                    while (data.remaining() > 0) {

                        try {

                            get_pass = data.get() == 1;
                            get_posX = data.getInt();
                            get_posY = data.getShort();
                            get_posZ = data.getInt();
                            get_dead_tree_level = data.getShort();

                        } catch (Exception exception) {

                            OutsideUtils.exception(new Exception(), exception, "");
                            return;

                        }

                        if (centerX == get_posX && centerZ == get_posZ) {

                            already_tested = true;
                            pass = get_pass;
                            pos_center = pos_center.atY(get_posY);
                            dead_tree_level = get_dead_tree_level;
                            break;

                        }

                    }

                }

                if (already_tested == false) {

                    String type = "";
                    int start_height = 0;

                    // Scan Tree Settings
                    {

                        Map<String, String> tree_settings = Caches.TreeSettings.getNormal(path_settings);
                        type = tree_settings.getOrDefault("type", "");
                        start_height = Integer.parseInt(tree_settings.getOrDefault("start_height", "0"));

                    }

                    test:
                    {

                        BlockPos pos_original = new BlockPos(centerX, GameUtils.Space.getHeightWorldGen(level_accessor, level_server, chunk_generator, centerX, centerZ, "OCEAN_FLOOR_WG", "OCEAN_FLOOR_WG"), centerZ);

                        // Ground Level
                        {

                            if (GameUtils.Space.testChunkStatus(level_accessor, new ChunkPos(pos_original), "carvers") == true) {

                                BlockState block = level_accessor.getBlockState(pos_original.below());

                                if (block.canBeReplaced() == true) {

                                    break test;

                                } else if (GameUtils.Tile.test(block, ground_block) == false) {

                                    break test;

                                }

                            }

                        }

                        RandomSource random = RandomSource.create(level_accessor.getServer().overworld().getSeed() ^ ((centerX * 341873128712L) + (centerZ * 132897987541L)));

                        // Tree Type
                        {

                            if (type.equals("special") == false && type.equals("emergent") == false) {

                                int highestY = GameUtils.Space.getHeightWorldGen(level_accessor, level_server, chunk_generator, centerX, centerZ, "WORLD_SURFACE_WG", "WORLD_SURFACE_WG");

                                if ((type.equals("terrestrial") == true && (pos_original.getY() < highestY)) || (type.equals("aquatic") == true && (pos_original.getY() == highestY))) {

                                    if (random.nextDouble() < Handcode.Config.unviable_ecology_skip_chance) {

                                        break test;

                                    }

                                    if (dead_tree_level == 0) {

                                        dead_tree_level = TreeLocation.getDeadTreeLevel(level_accessor, id, location, centerX, centerZ, true);

                                    }

                                }

                            }

                        }

                        // Height Offset
                        {

                            int offsetY = pos_original.getY() + start_height;

                            if (dead_tree_level < 200) {

                                offsetY = offsetY + random.nextInt(Integer.parseInt(start_height_offset[0]), Integer.parseInt(start_height_offset[1]) + 1);

                            }

                            pos_center = pos_center.atY(pos_center.getY() + offsetY);

                        }

                        int center_sizeX = 0;
                        int center_sizeY = 0;
                        int center_sizeZ = 0;
                        int sizeX = 0;
                        int sizeY = 0;
                        int sizeZ = 0;

                        // Get Size
                        {

                            try {

                                short[] size_data = Caches.TreeShape.getTreeShapeSize(location);
                                sizeX = size_data[0];
                                sizeY = size_data[1];
                                sizeZ = size_data[2];
                                center_sizeX = size_data[3];
                                center_sizeY = size_data[4];
                                center_sizeZ = size_data[5];

                            } catch (Exception exception) {

                                OutsideUtils.exception(new Exception(), exception, "This is normal error when a tree shape is no longer in your world. Here is that shape ID [ " + location + " ].");
                                break test;

                            }

                        }

                        // Size Convert
                        {

                            int[] convert = OutsideUtils.convertSizeRotationMirrored(rotation_mirrored, sizeX, sizeZ, center_sizeX, center_sizeZ);
                            sizeX = convert[0];
                            sizeZ = convert[1];
                            center_sizeX = convert[2];
                            center_sizeZ = convert[3];

                            if (fallen_direction > 0) {

                                convert = OutsideUtils.convertSizeFallen(fallen_direction, sizeX, sizeY, sizeZ, center_sizeX, center_sizeY, center_sizeZ);
                                sizeX = convert[0];
                                sizeY = convert[1];
                                sizeZ = convert[2];
                                center_sizeX = convert[3];
                                center_sizeY = convert[4];
                                center_sizeZ = convert[5];

                            }

                        }

                        // Height Y Test
                        {

                            if ((sizeY - center_sizeY) + pos_center.getY() >= level_accessor.getMaxBuildHeight()) {

                                break test;

                            }

                            if (pos_original.getY() == GameUtils.Space.getBuildHeight(level_accessor, false)) {

                                break test;

                            }

                            if (Handcode.Config.max_height_spawn != 0) {

                                if (pos_original.getY() > Handcode.Config.max_height_spawn) {

                                    break test;

                                }

                            }

                        }

                        // Structure Detection
                        {

                            int size = Handcode.Config.structure_detection_size;
                            ChunkPos chunk_pos_test = null;

                            if (size >= 0) {

                                Map<Structure, LongSet> references = new HashMap<>();

                                for (int scanX = from_chunkX - size; scanX <= to_chunkX + size; scanX++) {

                                    for (int scanZ = from_chunkZ - size; scanZ <= to_chunkZ + size; scanZ++) {

                                        chunk_pos_test = new ChunkPos(scanX, scanZ);

                                        if (GameUtils.Space.testChunkStatus(level_accessor, chunk_pos_test, "structure_references") == true) {

                                            references = level_accessor.getChunk(chunk_pos_test.x, chunk_pos_test.z).getAllReferences();

                                            if (references.size() > 0) {

                                                for (Structure structure : references.keySet()) {

                                                    if (structure.step() == GenerationStep.Decoration.SURFACE_STRUCTURES) {

                                                        break test;

                                                    }

                                                }

                                            }

                                        }

                                    }

                                }

                            }

                        }

                        if (sizeX != 0 || sizeZ != 0) {

                            if (dead_tree_level < 200) {

                                if (testSurfaceSmoothness(level_accessor, level_server, chunk_generator, pos_center, sizeX, sizeY, sizeZ, center_sizeX, center_sizeY, center_sizeZ, pos_original) == false) {

                                    break test;

                                }

                            } else {

                                if (testFallenArea(level_accessor, level_server, chunk_generator, location, pos_center, rotation_mirrored, fallen_direction, dead_tree_level) == false) {

                                    break test;

                                }

                            }

                        }

                        pass = true;

                    }

                    // Write File
                    {

                        int from_regionX = from_chunkX >> 5;
                        int from_regionZ = from_chunkZ >> 5;
                        int to_regionX = to_chunkX >> 5;
                        int to_regionZ = to_chunkZ >> 5;

                        List<String> write = new ArrayList<>();
                        write.add("l" + pass);
                        write.add("i" + pos_center.getX());
                        write.add("s" + pos_center.getY());
                        write.add("i" + pos_center.getZ());
                        write.add("s" + dead_tree_level);

                        for (int scanX = from_regionX; scanX <= to_regionX; scanX++) {

                            for (int scanZ = from_regionZ; scanZ <= to_regionZ; scanZ++) {

                                FileManager.writeBIN(Core.path_world_mod + "/world_gen/detailed_detection/" + dimension + "/" + scanX + "," + scanZ + ".bin", write, true);

                            }

                        }

                    }

                }

            }

            if (pass == true) {

                place(level_accessor, level_server, chunk_pos, id, location, path_settings, pos_center, rotation_mirrored, dead_tree_level, fallen_direction);

            }

        }

        private static boolean testSurfaceSmoothness (LevelAccessor level_accessor, ServerLevel level_server, ChunkGenerator chunk_generator, BlockPos pos_center, int sizeX, int sizeY, int sizeZ, int center_sizeX, int center_sizeY, int center_sizeZ, BlockPos pos_original) {

            if (Handcode.Config.surface_smoothness_detection == true) {

                double distance_multiply = Handcode.Config.surface_smoothness_detection_percent * 0.01;
                int test_center_sizeX = (int) Math.ceil(center_sizeX * distance_multiply);
                int test_center_sizeZ = (int) Math.ceil(center_sizeZ * distance_multiply);
                int test_sizeX = (int) Math.ceil((sizeX - center_sizeX) * distance_multiply);
                int test_sizeZ = (int) Math.ceil((sizeZ - center_sizeZ) * distance_multiply);
                int pos1 = GameUtils.Space.getHeightWorldGen(level_accessor, level_server, chunk_generator, pos_center.getX() - test_center_sizeX, pos_center.getZ() - test_center_sizeZ, "OCEAN_FLOOR", "OCEAN_FLOOR_WG");
                int pos2 = GameUtils.Space.getHeightWorldGen(level_accessor, level_server, chunk_generator, pos_center.getX() - test_center_sizeX, pos_center.getZ() + test_sizeZ, "OCEAN_FLOOR", "OCEAN_FLOOR_WG");
                int pos3 = GameUtils.Space.getHeightWorldGen(level_accessor, level_server, chunk_generator, pos_center.getX() + test_sizeX, pos_center.getZ() - test_center_sizeZ, "OCEAN_FLOOR", "OCEAN_FLOOR_WG");
                int pos4 = GameUtils.Space.getHeightWorldGen(level_accessor, level_server, chunk_generator, pos_center.getX() + test_sizeX, pos_center.getZ() + test_sizeZ, "OCEAN_FLOOR", "OCEAN_FLOOR_WG");

                int height_up = (sizeY - center_sizeY) + Math.abs(pos_center.getY() - pos_original.getY());
                height_up = pos_original.getY() + (int) Math.ceil(height_up * Handcode.Config.surface_smoothness_detection_height_up * 0.01);
                int height_down = center_sizeY + Math.abs(pos_center.getY() - pos_original.getY());
                height_down = pos_original.getY() - (int) Math.ceil(height_down * Handcode.Config.surface_smoothness_detection_height_down * 0.01);
                boolean test1 = (pos_original.getY() < pos1 && height_up > pos1) || (pos_original.getY() >= pos1 && pos1 > height_down);
                boolean test2 = (pos_original.getY() < pos2 && height_up > pos2) || (pos_original.getY() >= pos2 && pos2 > height_down);
                boolean test3 = (pos_original.getY() < pos3 && height_up > pos3) || (pos_original.getY() >= pos3 && pos3 > height_down);
                boolean test4 = (pos_original.getY() < pos4 && height_up > pos4) || (pos_original.getY() >= pos4 && pos4 > height_down);

                return test1 == true && test2 == true && test3 == true && test4 == true;

            }

            return true;

        }

        private static boolean testFallenArea (LevelAccessor level_accessor, ServerLevel level_server, ChunkGenerator chunk_generator, String location, BlockPos pos_center, int[] rotation_mirrored, int fallen_direction, int dead_tree_level) {

            int reduce_trunk = 0;
            int reduce_bough = 0;
            int reduce_branch = 0;
            int reduce_limb = 0;
            int reduce_twig = 0;
            int reduce_sprig = 0;

            // Get Reduce
            {

                try {

                    int[] data = getPartReduce(level_accessor, location, pos_center.getX(), pos_center.getZ(), dead_tree_level);
                    reduce_trunk = data[0];
                    reduce_bough = data[1];
                    reduce_branch = data[2];
                    reduce_limb = data[3];
                    reduce_twig = data[4];
                    reduce_sprig = data[5];

                } catch (Exception exception) {

                    OutsideUtils.exception(new Exception(), exception, "");
                    return false;

                }

            }

            int left_before_test = 0;

            // Get Left
            {

                double total = reduce_trunk + reduce_bough + reduce_branch + reduce_limb + reduce_twig + reduce_sprig;
                left_before_test = (int) Math.ceil(total * 0.5);

            }

            boolean is_only_trunk = dead_tree_level >= 60;
            int distance_skip = (int) Math.floor(left_before_test / 16.0);
            int distance_skip_test = 0;
            BlockPos pos = null;

            int loop = 0;
            short type = 0;
            short posX = 0;
            short posY = 0;
            short posZ = 0;

            for (short scan : Caches.TreeShape.getTreeShapeData(location)) {

                // Get Data
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

                }

                if (loop == 0) {

                    if (OutsideUtils.Math.isNumberStartWith(type, 1) == true) {

                        // Skip Roots
                        {

                            if (OutsideUtils.Math.isNumberStartWith(type, 110) == true || OutsideUtils.Math.isNumberStartWith(type, 111) == true || OutsideUtils.Math.isNumberStartWith(type, 112) == true || OutsideUtils.Math.isNumberStartWith(type, 113) == true) {

                                continue;

                            }

                        }

                        // Dead Tree Reduction
                        {

                            // Basic Style
                            {

                                if (OutsideUtils.Math.isNumberStartWith(type, 120) == true) {

                                    continue;

                                } else if (OutsideUtils.Math.isNumberStartWith(type, 119) == true) {

                                    if (reduce_sprig > 0) {

                                        reduce_sprig = reduce_sprig - 1;

                                    } else {

                                        continue;

                                    }

                                } else if (OutsideUtils.Math.isNumberStartWith(type, 118) == true) {

                                    if (reduce_sprig == 0) {

                                        if (reduce_twig > 0) {

                                            reduce_twig = reduce_twig - 1;

                                        } else {

                                            continue;

                                        }

                                    }

                                } else if (OutsideUtils.Math.isNumberStartWith(type, 117) == true) {

                                    if (reduce_twig == 0) {

                                        if (reduce_limb > 0) {

                                            reduce_limb = reduce_limb - 1;

                                        } else {

                                            continue;

                                        }

                                    }

                                } else if (OutsideUtils.Math.isNumberStartWith(type, 116) == true) {

                                    if (reduce_limb == 0) {

                                        if (reduce_branch > 0) {

                                            reduce_branch = reduce_branch - 1;

                                        } else {

                                            continue;

                                        }

                                    }

                                } else if (OutsideUtils.Math.isNumberStartWith(type, 115) == true) {

                                    if (reduce_branch == 0) {

                                        if (reduce_bough > 0) {

                                            reduce_bough = reduce_bough - 1;

                                        } else {

                                            continue;

                                        }

                                    }

                                }

                            }

                            if (is_only_trunk == true) {

                                // Only Trunk
                                {

                                    if (OutsideUtils.Math.isNumberStartWith(type, 114) == true) {

                                        if (reduce_trunk > 0) {

                                            reduce_trunk = reduce_trunk - 1;

                                        } else {

                                            continue;

                                        }

                                    }

                                }

                            }

                        }

                        if (left_before_test > 0) {

                            left_before_test = left_before_test - 1;

                        } else {

                            // Distance Skip
                            {

                                if (distance_skip > 0) {

                                    if (distance_skip_test > 0) {

                                        distance_skip_test = distance_skip_test - 1;
                                        continue;

                                    } else {

                                        distance_skip_test = distance_skip;

                                    }

                                }

                            }

                            pos = new BlockPos(posX, posY, posZ);
                            pos = OutsideUtils.convertPosRotationMirrored(pos, rotation_mirrored);
                            pos = OutsideUtils.convertPosFallen(pos, fallen_direction);
                            pos = pos.offset(pos_center.getX(), pos_center.getY(), pos_center.getZ());

                            if (pos.getY() <= GameUtils.Space.getHeightWorldGen(level_accessor, level_server, chunk_generator, pos.getX(), pos.getZ(), "OCEAN_FLOOR_WG", "OCEAN_FLOOR_WG")) {

                                return true;

                            }

                        }

                    }

                }

            }

            return false;

        }

    }

    private static class LeafLitterGeneration {

        private static final Object lock = new Object();
        private static final Map<ChunkPos, Map<BlockPos, BlockState>> cache_locations = new HashMap<>();

        private static void add (ChunkPos chunk_pos, BlockPos pos, BlockState block) {

            synchronized (lock) {

                cache_locations.computeIfAbsent(chunk_pos, create -> new HashMap<>()).put(pos, block);

            }

        }

        private static void  place (LevelAccessor level_accessor, ServerLevel level_server, ChunkGenerator chunk_generator, ChunkPos chunk_pos) {

            synchronized (lock) {

                Map<BlockPos, BlockState> data = cache_locations.get(chunk_pos);

                if (data == null) {

                    return;

                }

                int height_motion = 0;

                for (Map.Entry<BlockPos, BlockState> entry : data.entrySet()) {

                    height_motion = GameUtils.Space.getHeightWorldGen(level_accessor, level_server, chunk_generator, entry.getKey().getX(), entry.getKey().getZ(), "MOTION_BLOCKING_NO_LEAVES", "WORLD_SURFACE_WG");

                    if (height_motion < entry.getKey().getY()) {

                        LeafLitter.create(level_accessor, level_server, entry.getKey().atY(height_motion), entry.getValue(), false);

                    }

                }

                cache_locations.remove(chunk_pos);

            }

        }

    }

    private static class Function {

        private static final Object lock = new Object();
        private static final Map<ChunkPos, Map<BlockPos, List<String>>> cache_functions = new HashMap<>();

        private static void add (ChunkPos chunk_pos, BlockPos pos, String path) {

            synchronized (lock) {

                cache_functions.computeIfAbsent(chunk_pos, create -> new HashMap<>()).computeIfAbsent(pos, create -> new ArrayList<>()).add(path);

            }

        }

        private static void run (LevelAccessor level_accessor, ServerLevel level_server, ChunkPos chunk_pos) {

            synchronized (lock) {

                Map<BlockPos, List<String>> data = cache_functions.get(chunk_pos);

                if (data == null) {

                    return;

                }

                for (Map.Entry<BlockPos, List<String>> entry : data.entrySet()) {

                    for (String get : entry.getValue()) {

                        TXTFunction.run(level_accessor, level_server, entry.getKey(), get, false);

                    }

                }

                cache_functions.remove(chunk_pos);

            }

        }

    }

}