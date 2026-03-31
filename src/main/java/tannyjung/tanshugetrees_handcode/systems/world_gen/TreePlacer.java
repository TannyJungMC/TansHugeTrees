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

    private static final Object lock = new Object();
    public static final Map<String, Map<ChunkPos, ByteArrayOutputStream>> cache_bin_convert = new HashMap<>();
    private static final Map<ChunkPos, Map<BlockPos, List<String>>> cache_functions = new HashMap<>();

    public static void start (LevelAccessor level_accessor, ServerLevel level_server, ChunkGenerator chunk_generator, String dimension, ChunkPos chunk_pos) {

        Core.GlobalLocking.test();

        {

            ByteBuffer data = null;
            int from_chunkX = 0;
            int from_chunkZ = 0;
            int to_chunkX = 0;
            int to_chunkZ = 0;
            String id = "";
            String chosen = "";
            int centerX = 0;
            int centerZ = 0;
            int rotation = 0;
            int mirrored = 0;
            int start_height_offset = 0;
            String ground_block = "";
            int dead_tree_level = 0;

            String key = (chunk_pos.x >> 5) + "," + (chunk_pos.z >> 5);

            if (cache_bin_convert.containsKey(key) == false) {

                cache_bin_convert.put(key, new HashMap<>());

                // Convert
                {

                    ByteArrayOutputStream data_convert = new ByteArrayOutputStream();
                    data = FileManager.readBIN(Core.path_world_mod + "/world_gen/place/" + dimension + "/" + key + ".bin");

                    while (data.remaining() > 0) {

                        try {

                            // Read
                            {

                                from_chunkX = data.getInt();
                                from_chunkZ = data.getInt();
                                to_chunkX = data.getInt();
                                to_chunkZ = data.getInt();
                                id = String.valueOf(data.getShort());
                                chosen = String.valueOf(data.getShort());
                                centerX = data.getInt();
                                centerZ = data.getInt();
                                rotation = data.get();
                                mirrored = data.get();
                                start_height_offset = data.getShort();
                                ground_block = String.valueOf(data.getShort());
                                dead_tree_level = data.getShort();

                                data_convert.reset();
                                data_convert.write(OutsideUtils.ByteConverter.fromInt(from_chunkX));
                                data_convert.write(OutsideUtils.ByteConverter.fromInt(from_chunkZ));
                                data_convert.write(OutsideUtils.ByteConverter.fromInt(to_chunkX));
                                data_convert.write(OutsideUtils.ByteConverter.fromInt(to_chunkZ));
                                data_convert.write(OutsideUtils.ByteConverter.fromShort(Short.parseShort(id)));
                                data_convert.write(OutsideUtils.ByteConverter.fromShort(Short.parseShort(chosen)));
                                data_convert.write(OutsideUtils.ByteConverter.fromInt(centerX));
                                data_convert.write(OutsideUtils.ByteConverter.fromInt(centerZ));
                                data_convert.write((byte) rotation);
                                data_convert.write((byte) mirrored);
                                data_convert.write(OutsideUtils.ByteConverter.fromShort((short) start_height_offset));
                                data_convert.write(OutsideUtils.ByteConverter.fromShort(Short.parseShort(ground_block)));
                                data_convert.write(OutsideUtils.ByteConverter.fromShort((short) dead_tree_level));

                            }

                            for (int scanX = from_chunkX; scanX <= to_chunkX; scanX++) {

                                for (int scanZ = from_chunkZ; scanZ <= to_chunkZ; scanZ++) {

                                    if (key.equals((scanX >> 5) + "," + (scanZ >> 5)) == true) {

                                        cache_bin_convert.get(key).computeIfAbsent(new ChunkPos(scanX, scanZ), create -> new ByteArrayOutputStream()).write(data_convert.toByteArray());

                                    }

                                }

                            }

                        } catch (Exception exception) {

                            OutsideUtils.exception(new Exception(), exception, "");
                            return;

                        }

                    }

                }

            }

            if (cache_bin_convert.get(key).containsKey(chunk_pos) == true) {

                data = ByteBuffer.wrap(cache_bin_convert.get(key).get(chunk_pos).toByteArray());
                cache_bin_convert.get(key).remove(chunk_pos);

                while (data.remaining() > 0) {

                    // Get data
                    {

                        try {

                            from_chunkX = data.getInt();
                            from_chunkZ = data.getInt();
                            to_chunkX = data.getInt();
                            to_chunkZ = data.getInt();
                            id = CacheManager.getDictionary(String.valueOf(data.getShort()), true);
                            chosen = CacheManager.getDictionary(String.valueOf(data.getShort()), true);
                            centerX = data.getInt();
                            centerZ = data.getInt();
                            rotation = data.get();
                            mirrored = data.get();
                            start_height_offset = data.getShort();
                            ground_block = CacheManager.getDictionary(String.valueOf(data.getShort()), true);
                            dead_tree_level = data.getShort();

                        } catch (Exception exception) {

                            OutsideUtils.exception(new Exception(), exception, "");
                            return;

                        }

                    }

                    test(level_accessor, level_server, chunk_generator, dimension, chunk_pos, from_chunkX, from_chunkZ, to_chunkX, to_chunkZ, id, chosen, centerX, centerZ, rotation, mirrored, start_height_offset, ground_block, dead_tree_level);

                }

                functionRun(level_accessor, level_server, chunk_pos);

            }

        }

    }

    private static void test (LevelAccessor level_accessor, ServerLevel level_server, ChunkGenerator chunk_generator, String dimension, ChunkPos chunk_pos, int from_chunkX, int from_chunkZ, int to_chunkX, int to_chunkZ, String id, String chosen, int centerX, int centerZ, int rotation, int mirrored, int start_height_offset, String ground_block, int dead_tree_level) {

        Map<String, Map<String, String>> config = ConfigDynamic.getData("world_gen");
        String path_storage = "";
        String path_settings = "";

        if (config.containsKey(id) == false) {

            return;

        } else {

            path_storage = config.get(id).get("path_storage");
            path_settings = config.get(id).get("path_settings");

        }

        boolean already_tested = false;
        boolean pass = false;
        int centerY = 0;
        int fallen_direction = 0;
        String location = path_storage + "|" + chosen;

        if (dead_tree_level > 200) {

            fallen_direction = RandomSource.create(level_server.getSeed() ^ ((centerX * 341873128712L) + (centerZ * 132897987541L))).nextInt(4) + 1;

        }

        synchronized (lock) {

            // get Data
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
                        get_posY = data.getInt();
                        get_posZ = data.getInt();
                        get_dead_tree_level = data.getShort();

                    } catch (Exception exception) {

                        OutsideUtils.exception(new Exception(), exception, "");
                        return;

                    }

                    if (centerX == get_posX && centerZ == get_posZ) {

                        already_tested = true;
                        pass = get_pass;
                        centerY = get_posY;
                        dead_tree_level = get_dead_tree_level;
                        break;

                    }

                }

            }

            if (already_tested == false) {

                String type = "";
                int start_height = 0;

                // Scan Tree Settings File
                {

                    List<String> tree_settings = Caches.getTreeSettings(path_settings);

                    if (tree_settings.isEmpty() == true) {

                        return;

                    } else {

                        for (String read_all : tree_settings) {

                            {

                                if (read_all.startsWith("type = ") == true) {

                                    type = read_all.substring("type = ".length());

                                } else if (read_all.startsWith("start_height = ") == true) {

                                    start_height = Integer.parseInt(read_all.substring("start_height = ".length()));
                                    break;

                                }

                            }

                        }

                    }

                }

                test:
                {

                    BlockPos pos_original = new BlockPos(centerX, GameUtils.Space.getHeightWorldGen(level_accessor, level_server, chunk_generator, centerX, centerZ, "OCEAN_FLOOR", "OCEAN_FLOOR_WG"), centerZ);

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

                    // Tree Type
                    {

                        if (type.equals("special") == false && type.equals("emergent") == false) {

                            int highestY = GameUtils.Space.getHeightWorldGen(level_accessor, level_server, chunk_generator, centerX, centerZ, "WORLD_SURFACE", "WORLD_SURFACE_WG");

                            if ((type.equals("terrestrial") == true && (pos_original.getY() < highestY)) || (type.equals("aquatic") == true && (pos_original.getY() == highestY))) {

                                RandomSource random = RandomSource.create(level_server.getSeed() ^ ((centerX * 341873128712L) + (centerZ * 132897987541L)));

                                if (random.nextDouble() < Handcode.Config.unviable_ecology_skip_chance) {

                                    break test;

                                }

                                if (dead_tree_level == 0) {

                                    Map<String, String> data = ConfigDynamic.getData("world_gen").get(id);
                                    dead_tree_level = TreeLocation.getDeadTreeLevel(data, random, location, id, true);

                                }

                            }

                        }

                    }

                    int center_sizeX = 0;
                    int center_sizeY = 0;
                    int center_sizeZ = 0;
                    int sizeX = 0;
                    int sizeY = 0;
                    int sizeZ = 0;

                    // Size Convert
                    {

                        short[] size_data = Caches.getTreeShapeSize(location);

                        try {

                            sizeX = size_data[0];
                            sizeY = size_data[1];
                            sizeZ = size_data[2];
                            center_sizeX = size_data[3];
                            center_sizeY = size_data[4];
                            center_sizeZ = size_data[5];

                        } catch (Exception exception) {

                            OutsideUtils.exception(new Exception(), exception, "This is normal error when a tree shape is no longer in your world. Here is that shape ID \"" + location + "\"");
                            break test;

                        }

                        // Rotation and Mirrored
                        {

                            int[] convert = OutsideUtils.convertSizeRotationMirrored(rotation, mirrored, sizeX, sizeZ, center_sizeX, center_sizeZ);
                            sizeX = convert[0];
                            sizeZ = convert[1];
                            center_sizeX = convert[2];
                            center_sizeZ = convert[3];

                        }

                        // Fallen
                        {

                            if (fallen_direction > 0) {

                                int[] convert = OutsideUtils.convertSizeFallen(fallen_direction, sizeX, sizeY, sizeZ, center_sizeX, center_sizeY, center_sizeZ);
                                sizeX = convert[0];
                                sizeY = convert[1];
                                sizeZ = convert[2];
                                center_sizeX = convert[3];
                                center_sizeY = convert[4];
                                center_sizeZ = convert[5];

                            }

                        }

                    }

                    centerY = pos_original.getY();

                    if (dead_tree_level < 300) {

                        centerY = centerY + start_height + start_height_offset;

                    }

                    // Build Height Limit
                    {

                        if ((sizeY - center_sizeY) + centerY >= level_accessor.getMaxBuildHeight()) {

                            break test;

                        }

                        if (pos_original.getY() == GameUtils.Space.getBuildHeight(level_accessor, false)) {

                            break test;

                        }

                    }

                    // Max Height Spawn
                    {

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

                                            for (Structure structure : references.keySet().toArray(new Structure[0])) {

                                                if (structure.step().equals(GenerationStep.Decoration.SURFACE_STRUCTURES) == true) {

                                                    break test;

                                                }

                                            }

                                        }

                                    }

                                }

                            }

                        }

                    }

                    if (testSurfaceSmoothness(level_accessor, level_server, chunk_generator, location, centerX, centerY, centerZ, sizeX, sizeZ, center_sizeX, center_sizeY, center_sizeZ, rotation, mirrored, fallen_direction, dead_tree_level, pos_original) == false) {

                        break test;

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
                    write.add("i" + centerX);
                    write.add("i" + centerY);
                    write.add("i" + centerZ);
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

            BlockPos pos_center = new BlockPos(centerX, centerY, centerZ);
            place(level_accessor, level_server, chunk_pos, id, location, path_settings, pos_center, rotation, mirrored, dead_tree_level, fallen_direction);

        }

    }

    private static boolean testSurfaceSmoothness (LevelAccessor level_accessor, ServerLevel level_server, ChunkGenerator chunk_generator, String location, int centerX, int centerY, int centerZ, int sizeX, int sizeZ, int center_sizeX, int center_sizeY, int center_sizeZ, int rotation, int mirrored, int fallen_direction, int dead_tree_level, BlockPos pos_original) {

        if (Handcode.Config.surface_smoothness_detection == true) {

            if (sizeX != 0 || sizeZ != 0) {

                if (dead_tree_level > 200) {

                    {

                        short[] shape = Caches.getTreeShapeData(location);

                        if (shape.length > 0) {

                            boolean pass = false;

                            int loop = 0;
                            String dead_type = "";
                            int posX = 0;
                            int posY = 0;
                            int posZ = 0;

                            int[] reduce_parts = getPartReduce(level_server, location, centerX, centerZ, dead_tree_level);
                            int reduce_trunk = reduce_parts[0];
                            int reduce_bough = reduce_parts[1];
                            int reduce_branch = reduce_parts[2];
                            int reduce_limb = reduce_parts[3];
                            int reduce_twig = reduce_parts[4];
                            int reduce_sprig = reduce_parts[5];
                            int total_block_count = reduce_trunk + reduce_bough + reduce_branch + reduce_limb + reduce_twig + reduce_sprig;
                            int dead_tree_level_test = Integer.parseInt(String.valueOf(dead_tree_level).substring(1));
                            int left_before_test = (int) (total_block_count * 0.75);
                            int distance_skip = (int) Math.floor(left_before_test / 16.0);
                            int distance_skip_test = 0;
                            Map<String, Integer> previousY = new HashMap<>();
                            int testY = 0;
                            int[] pos_converted = new int[0];

                            for (short read_all : shape) {

                                // Get Data
                                {

                                    loop = loop + 1;

                                    if (loop == 1) {

                                        dead_type = String.valueOf(read_all);

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

                                    if (dead_type.startsWith("1") == true) {

                                        // Skip Roots
                                        {

                                            if (dead_type.startsWith("110") == true || dead_type.startsWith("111") == true || dead_type.startsWith("112") == true || dead_type.startsWith("113") == true) {

                                                continue;

                                            }

                                        }

                                        // Dead Tree Reduction
                                        {

                                            if (dead_tree_level_test > 0) {

                                                // Basic Style
                                                {

                                                    if (dead_type.startsWith("120") == true) {

                                                        continue;

                                                    } else if (dead_type.startsWith("119") == true) {

                                                        if (reduce_sprig > 0) {

                                                            reduce_sprig = reduce_sprig - 1;

                                                        } else {

                                                            continue;

                                                        }

                                                    } else if (dead_type.startsWith("118") == true) {

                                                        if (reduce_sprig == 0) {

                                                            if (reduce_twig > 0) {

                                                                reduce_twig = reduce_twig - 1;

                                                            } else {

                                                                continue;

                                                            }

                                                        }

                                                    } else if (dead_type.startsWith("117") == true) {

                                                        if (reduce_twig == 0) {

                                                            if (reduce_limb > 0) {

                                                                reduce_limb = reduce_limb - 1;

                                                            } else {

                                                                continue;

                                                            }

                                                        }

                                                    } else if (dead_type.startsWith("116") == true) {

                                                        if (reduce_limb == 0) {

                                                            if (reduce_branch > 0) {

                                                                reduce_branch = reduce_branch - 1;

                                                            } else {

                                                                continue;

                                                            }

                                                        }

                                                    } else if (dead_type.startsWith("115") == true) {

                                                        if (reduce_branch == 0) {

                                                            if (reduce_bough > 0) {

                                                                reduce_bough = reduce_bough - 1;

                                                            } else {

                                                                continue;

                                                            }

                                                        }

                                                    }

                                                }

                                                if (dead_tree_level_test >= 60) {

                                                    // Only Trunk
                                                    {

                                                        if (dead_type.startsWith("114") == true) {

                                                            if (reduce_trunk > 0) {

                                                                reduce_trunk = reduce_trunk - 1;

                                                            } else {

                                                                continue;

                                                            }

                                                        }

                                                    }

                                                }

                                            }

                                        }

                                        if (left_before_test > 0) {

                                            left_before_test = left_before_test - 1;

                                        } else {

                                            if (distance_skip > 0) {

                                                if (distance_skip_test > 0) {

                                                    distance_skip_test = distance_skip_test - 1;
                                                    continue;

                                                } else {

                                                    distance_skip_test = distance_skip;

                                                }

                                            }

                                            pos_converted = OutsideUtils.convertPosRotationMirrored(rotation, mirrored, posX, posZ);
                                            pos_converted = OutsideUtils.convertPosFallen(fallen_direction, pos_converted[0], posY, pos_converted[1]);
                                            posX = centerX + pos_converted[0];
                                            posY = centerY + pos_converted[1];
                                            posZ = centerZ + pos_converted[2];

                                            if (previousY.containsKey(posX + "/" + posZ) == true) {

                                                if (previousY.get(posX + "/" + posZ) <= posY) {

                                                    continue;

                                                }

                                            }

                                            previousY.put(posX + "/" + posZ, posY);
                                            testY = GameUtils.Space.getHeightWorldGen(level_accessor, level_server, chunk_generator, posX, posZ, "OCEAN_FLOOR", "OCEAN_FLOOR_WG");

                                            if (testY >= posY) {

                                                pass = true;
                                                break;

                                            }

                                        }

                                    }


                                }

                            }

                            return pass == true;

                        }

                    }

                } else {

                    {

                        int height_up = (int) Math.ceil(pos_original.getY() + ((sizeX - center_sizeX) * (Handcode.Config.surface_smoothness_detection_height_up * 0.01)));
                        int height_down = (int) Math.ceil(pos_original.getY() - (center_sizeY * (Handcode.Config.surface_smoothness_detection_height_down * 0.01)));
                        double distance_multiply = Handcode.Config.surface_smoothness_detection_percent * 0.01;

                        int test_center_sizeX = (int) Math.ceil(center_sizeX * distance_multiply);
                        int test_center_sizeZ = (int) Math.ceil(center_sizeZ * distance_multiply);
                        int test_sizeX = (int) Math.ceil((sizeX - center_sizeX) * distance_multiply);
                        int test_sizeZ = (int) Math.ceil((sizeZ - center_sizeZ) * distance_multiply);
                        int pos1 = GameUtils.Space.getHeightWorldGen(level_accessor, level_server, chunk_generator, centerX - test_center_sizeX, centerZ - test_center_sizeZ, "OCEAN_FLOOR", "OCEAN_FLOOR_WG");
                        int pos2 = GameUtils.Space.getHeightWorldGen(level_accessor, level_server, chunk_generator, centerX - test_center_sizeX, centerZ + test_sizeZ, "OCEAN_FLOOR", "OCEAN_FLOOR_WG");
                        int pos3 = GameUtils.Space.getHeightWorldGen(level_accessor, level_server, chunk_generator, centerX + test_sizeX, centerZ - test_center_sizeZ, "OCEAN_FLOOR", "OCEAN_FLOOR_WG");
                        int pos4 = GameUtils.Space.getHeightWorldGen(level_accessor, level_server, chunk_generator, centerX + test_sizeX, centerZ + test_sizeZ, "OCEAN_FLOOR", "OCEAN_FLOOR_WG");
                        boolean test1 = (pos_original.getY() < pos1 && height_up > pos1) || (pos_original.getY() >= pos1 && pos1 > height_down);
                        boolean test2 = (pos_original.getY() < pos2 && height_up > pos2) || (pos_original.getY() >= pos2 && pos2 > height_down);
                        boolean test3 = (pos_original.getY() < pos3 && height_up > pos3) || (pos_original.getY() >= pos3 && pos3 > height_down);
                        boolean test4 = (pos_original.getY() < pos4 && height_up > pos4) || (pos_original.getY() >= pos4 && pos4 > height_down);

                        return test1 == true && test2 == true && test3 == true && test4 == true;

                    }

                }

            }

        }

        return true;

    }

    private static int[] getPartReduce (ServerLevel level_server, String location, int centerX, int centerZ, int dead_tree_level) {

        int[] reduce_parts = new int[6];
        RandomSource random = RandomSource.create(level_server.getSeed() ^ ((centerX * 341873128712L) + (centerZ * 132897987541L)));
        dead_tree_level = Integer.parseInt(String.valueOf(dead_tree_level).substring(1));

        int[] block_count = Caches.getTreeShapeBlockCount(location);
        int reduce_trunk = 0;
        int reduce_bough = 0;
        int reduce_branch = 0;
        int reduce_limb = 0;
        int reduce_twig = 0;
        int reduce_sprig = 0;

        {

            reduce_trunk = block_count[0];

            if (dead_tree_level >= 60) {

                // Only Trunk
                {

                    if (dead_tree_level == 60 || dead_tree_level == 70) {

                        reduce_trunk = (int) (Mth.nextDouble(random, 0.5, 1.0) * (double) reduce_trunk);

                    } else if (dead_tree_level == 80 || dead_tree_level == 90) {

                        reduce_trunk = (int) (Mth.nextDouble(random, 0.1, 0.5) * (double) reduce_trunk);

                    }

                }

            } else {

                // General
                {

                    reduce_bough = block_count[1];

                    if (dead_tree_level == 50) {

                        reduce_bough = (int) (random.nextDouble() * reduce_bough);

                    }

                    if (dead_tree_level < 50) {

                        reduce_branch = block_count[2];

                        if (dead_tree_level == 40) {

                            reduce_branch = (int) (random.nextDouble() * reduce_branch);

                        }

                    }

                    if (dead_tree_level < 40) {

                        reduce_limb = block_count[3];

                        if (dead_tree_level == 30 && reduce_limb > 0) {

                            reduce_limb = (int) (random.nextDouble() * reduce_limb);

                        }

                    }

                    if (dead_tree_level < 30) {

                        reduce_twig = block_count[4];

                        if (dead_tree_level == 20 && reduce_twig > 0) {

                            reduce_twig = (int) (random.nextDouble() * reduce_twig);

                        }

                    }

                    if (dead_tree_level < 20) {

                        reduce_sprig = block_count[5];

                        if (dead_tree_level == 10 && reduce_sprig > 0) {

                            reduce_sprig = (int) (random.nextDouble() * reduce_sprig);

                        }

                    }

                }

            }

            reduce_parts[0] = reduce_trunk;
            reduce_parts[1] = reduce_bough;
            reduce_parts[2] = reduce_branch;
            reduce_parts[3] = reduce_limb;
            reduce_parts[4] = reduce_twig;
            reduce_parts[5] = reduce_sprig;

        }

        return reduce_parts;

    }

    private static void place (LevelAccessor level_accessor, ServerLevel level_server, ChunkPos chunk_pos, String id, String location, String path_settings, BlockPos pos_center, int rotation, int mirrored, int dead_tree_level, int fallen_direction) {

        short[] shape = Caches.getTreeShapeData(location);

        if (shape.length > 0) {

            Map<String, BlockState> map_block = new HashMap<>();
            Map<String, Boolean> map_block_keep = new HashMap<>();
            Map<String, String> map_function = new HashMap<>();
            boolean can_disable_roots = false;
            boolean can_leaves_decay = false;
            boolean can_leaves_drop = false;
            boolean can_leaves_regrow = false;
            int[] leaves_type = new int[2];

            // Get Tree Settings File
            {

                String type_id = "";
                String value = "";

                for (String read_all : Caches.getTreeSettings(path_settings)) {

                    {

                        if (read_all.startsWith("can_leaves_decay = ") == true) {

                            can_leaves_decay = Boolean.parseBoolean(read_all.substring("can_leaves_decay = ".length()));

                        } else if (read_all.startsWith("can_leaves_drop = ") == true) {

                            can_leaves_drop = Boolean.parseBoolean(read_all.substring("can_leaves_drop = ".length()));

                        } else if (read_all.startsWith("can_leaves_regrow = ") == true) {

                            can_leaves_regrow = Boolean.parseBoolean(read_all.substring("can_leaves_regrow = ".length()));

                        } else if (read_all.startsWith("can_disable_roots") == true) {

                            can_disable_roots = Boolean.parseBoolean(read_all.substring("can_disable_roots = ".length()));

                        } else if (read_all.startsWith("Block ") == true) {

                            {

                                type_id = read_all.substring("Block ### ".length(), "Block ### ####".length());
                                value = read_all.substring("Block ### #### = ".length());

                                if (value.endsWith(" keep") == true) {

                                    map_block_keep.put(type_id, true);
                                    value = value.replace(" keep", "");

                                } else {

                                    map_block_keep.put(type_id, false);

                                }

                                map_block.put(type_id, GameUtils.Tile.fromText(value));

                                // Test Leaves Types
                                {

                                    if (type_id.startsWith("120") == true) {

                                        // Get ID
                                        {

                                            if (value.endsWith(" keep") == true) {

                                                value = value.substring(0, value.length() - (" keep").length());

                                            }

                                            if (value.endsWith("]") == true) {

                                                value = value.substring(0, value.indexOf("["));

                                            }

                                        }

                                        int number = Integer.parseInt(type_id.substring(2)) - 1;

                                        if (Handcode.Config.deciduous_leaves_list.contains(value) == true) {

                                            leaves_type[number] = 1;

                                        } else if (Handcode.Config.coniferous_leaves_list.contains(value) == true) {

                                            leaves_type[number] = 2;

                                        }

                                    }

                                }

                            }

                        } else if (read_all.startsWith("Function ") == true) {

                            {

                                type_id = read_all.substring("Function ## ".length(), "Function ## ###".length());
                                value = read_all.substring("Function ## ### = ".length());
                                map_function.put(type_id, value);

                            }

                        }

                    }

                }

            }

            boolean in_snowy_biome = GameUtils.Environment.test(GameUtils.Environment.getAt(level_accessor, pos_center), "#tanshugetrees:snowy_biomes");
            boolean no_roots = Handcode.Config.world_gen_roots == false && can_disable_roots == true;
            boolean coarse_woody_debris = false;
            boolean hollowed = false;
            int reduce_trunk = 0;
            int reduce_bough = 0;
            int reduce_branch = 0;
            int reduce_limb = 0;
            int reduce_twig = 0;
            int reduce_sprig = 0;

            // Dead Tree Level Convert
            {

                if (dead_tree_level > 0) {

                    int[] reduce_parts = getPartReduce(level_server, location, pos_center.getX(), pos_center.getZ(), dead_tree_level);
                    reduce_trunk = reduce_parts[0];
                    reduce_bough = reduce_parts[1];
                    reduce_branch = reduce_parts[2];
                    reduce_limb = reduce_parts[3];
                    reduce_twig = reduce_parts[4];
                    reduce_sprig = reduce_parts[5];

                    if (dead_tree_level > 100 && dead_tree_level < 199) {

                        dead_tree_level = dead_tree_level - 100;

                    } else if (dead_tree_level > 200 && dead_tree_level < 299) {

                        dead_tree_level = dead_tree_level - 200;
                        coarse_woody_debris = true;

                    } else if (dead_tree_level > 300 && dead_tree_level < 399) {

                        dead_tree_level = dead_tree_level - 300;
                        coarse_woody_debris = true;
                        no_roots = true;

                    }

                    if (dead_tree_level == 70 || dead_tree_level == 90) {

                        hollowed = true;

                    }

                }

            }

            // Read File
            {

                RandomSource random = RandomSource.create(level_server.getSeed() ^ ((chunk_pos.x * 341873128712L) + (chunk_pos.z * 132897987541L)));

                int loop = 0;
                String type = "";
                int posX = 0;
                int posY = 0;
                int posZ = 0;

                int[] pos_converted = new int[0];
                boolean can_run_function = false;
                BlockState block = null;
                String function = "";
                BlockPos pos = null;
                double leaf_litter_world_gen_chance = 0.0;
                int height_motion = 0;

                List<String> tree_decoration = new ArrayList<>();

                for (short read_all : shape) {

                    // Get Data
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

                        if (type.startsWith("1") == true) {

                            can_run_function = false;

                            // Dead Tree Reduction
                            {

                                if (dead_tree_level > 0) {

                                    // Basic Style
                                    {

                                        if (type.startsWith("120") == true) {

                                            continue;

                                        } else if (type.startsWith("119") == true) {

                                            if (reduce_sprig > 0) {

                                                reduce_sprig = reduce_sprig - 1;

                                            } else {

                                                continue;

                                            }

                                        } else if (type.startsWith("118") == true) {

                                            if (reduce_sprig == 0) {

                                                if (reduce_twig > 0) {

                                                    reduce_twig = reduce_twig - 1;

                                                } else {

                                                    continue;

                                                }

                                            }

                                        } else if (type.startsWith("117") == true) {

                                            if (reduce_twig == 0) {

                                                if (reduce_limb > 0) {

                                                    reduce_limb = reduce_limb - 1;

                                                } else {

                                                    continue;

                                                }

                                            }

                                        } else if (type.startsWith("116") == true) {

                                            if (reduce_limb == 0) {

                                                if (reduce_branch > 0) {

                                                    reduce_branch = reduce_branch - 1;

                                                } else {

                                                    continue;

                                                }

                                            }

                                        } else if (type.startsWith("115") == true) {

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

                                            if (type.startsWith("114") == true) {

                                                if (reduce_trunk > 0) {

                                                    reduce_trunk = reduce_trunk - 1;

                                                    if (hollowed == true) {

                                                        if (type.equals("1143") == true) {

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

                        pos_converted = OutsideUtils.convertPosRotationMirrored(rotation, mirrored, posX, posZ);
                        pos_converted = OutsideUtils.convertPosFallen(fallen_direction, pos_converted[0], posY, pos_converted[1]);
                        pos = pos_center.offset(pos_converted[0], pos_converted[1], pos_converted[2]);

                        if (chunk_pos.x == pos.getX() >> 4 && chunk_pos.z == pos.getZ() >> 4) {

                            if (type.startsWith("1") == true) {

                                block = map_block.get(type);

                                // Test and Place Block
                                {

                                    // Keep
                                    {

                                        if (map_block_keep.get(type) == true) {

                                            if (level_accessor.getBlockState(pos).isAir() == false) {

                                                continue;

                                            }

                                        }

                                    }

                                    // No Roots
                                    {

                                        if (coarse_woody_debris == false) {

                                            if (no_roots == true) {

                                                if (type.startsWith("111") == true || type.startsWith("112") == true || type.startsWith("113") == true) {

                                                    continue;

                                                }

                                            }

                                        } else {

                                            if (type.startsWith("112") == true || type.startsWith("113") == true) {

                                                continue;

                                            }

                                            if (no_roots == true) {

                                                if (type.startsWith("110") == true || type.startsWith("111") == true) {

                                                    continue;

                                                }

                                            }

                                        }

                                    }

                                    // Leaves
                                    {

                                        if (type.startsWith("120") == true && GameUtils.Tile.test(block, "#minecraft:leaves") == true) {

                                            // Pre Leaves Drop
                                            {

                                                if (Handcode.Config.leaf_litter == true && Handcode.Config.leaf_litter_world_gen == true) {

                                                    if (can_leaves_drop == true) {

                                                        if ((type.endsWith("1") == true && leaves_type[0] == 2) || (type.endsWith("2") == true && leaves_type[1] == 2)) {

                                                            leaf_litter_world_gen_chance = Handcode.Config.leaf_litter_world_gen_chance_coniferous;

                                                        } else {

                                                            leaf_litter_world_gen_chance = Handcode.Config.leaf_litter_world_gen_chance;

                                                        }

                                                        if (Math.random() < leaf_litter_world_gen_chance) {

                                                            height_motion = GameUtils.Space.getHeight(level_accessor, pos.getX(), pos.getZ(), "MOTION_BLOCKING_NO_LEAVES");

                                                            if (height_motion < pos.getY()) {

                                                                if (height_motion != GameUtils.Space.getBuildHeight(level_accessor, false)) {

                                                                    LeafLitter.create(level_accessor, level_server, pos.atY(height_motion), block, false);

                                                                }

                                                            }

                                                        }

                                                    }

                                                }

                                            }

                                            // Abscission (World Gen)
                                            {

                                                if (Handcode.Config.abscission_world_gen == true) {

                                                    if (in_snowy_biome == true) {

                                                        if ((type.endsWith("1") == true && leaves_type[0] == 1) || (type.endsWith("2") == true && leaves_type[1] == 1)) {

                                                            continue;

                                                        }

                                                    }

                                                }

                                            }

                                        }

                                    }

                                    GameUtils.Tile.set(level_accessor, pos, block, true);

                                }

                                // Summon Marker
                                {

                                    // At Center
                                    if (posX == 0 && posY == 0 && posZ == 0) {

                                        if (Handcode.Config.tree_location == true && dead_tree_level == 0) {

                                            if (can_leaves_decay == true || can_leaves_drop == true || can_leaves_regrow == true) {

                                                String marker_data = "{ForgeData:{tanshugetrees:{file:\"" + location + "\",tree_settings:\"" + path_settings + "\",rotation:" + rotation + ",mirrored:" + mirrored + "}}}";
                                                GameUtils.Mob.summonWorldGen(level_server, pos.getCenter(), "minecraft:marker", id, "TANSHUGETREES-tree_location", marker_data);
                                                
                                            }

                                        }

                                    }

                                }

                                // Tree Decoration
                                {

                                    if (Handcode.Config.tree_decorations_normal == true && random.nextDouble() < Handcode.Config.tree_decorations_normal_chance) {

                                        if (type.startsWith("120") == false) {

                                            if (CacheManager.Data.existMapTextListText("tree_decoration", "normal") == false) {

                                                {

                                                    String[] names = new File(Core.path_config + "/dev/temporary/tree_decoration").list();
                                                    List<String> data = new ArrayList<>();

                                                    if (names != null) {

                                                        for (String name : names) {

                                                            if (name.endsWith(".txt") == true) {

                                                                data.add(name.replace(".txt", ""));

                                                            }

                                                        }

                                                    }

                                                    CacheManager.Data.setMapTextListText("tree_decoration", "normal", data);

                                                }

                                            }

                                            tree_decoration = CacheManager.Data.getMapTextListText("tree_decoration").get("normal");

                                            if (tree_decoration.isEmpty() == false) {

                                                functionAdd(chunk_pos, pos, "tree_decoration/" + tree_decoration.get(random.nextInt(tree_decoration.size())));

                                            }

                                        }

                                    }

                                    if (dead_tree_level > 0) {

                                        if (Handcode.Config.tree_decorations_decay == true && random.nextDouble() < Handcode.Config.tree_decorations_decay_chance) {

                                            if (CacheManager.Data.existMapTextListText("tree_decoration", "decay") == false) {

                                                {

                                                    String[] names = new File(Core.path_config + "/dev/temporary/tree_decoration/decay").list();
                                                    List<String> data = new ArrayList<>();

                                                    if (names != null) {

                                                        for (String name : names) {

                                                            if (name.endsWith(".txt") == true) {

                                                                data.add(name.replace(".txt", ""));

                                                            }

                                                        }

                                                    }

                                                    CacheManager.Data.setMapTextListText("tree_decoration", "decay", data);

                                                }

                                            }

                                            tree_decoration = CacheManager.Data.getMapTextListText("tree_decoration").get("decay");

                                            if (tree_decoration.isEmpty() == false) {

                                                functionAdd(chunk_pos, pos, "tree_decoration/decay/" + tree_decoration.get(random.nextInt(tree_decoration.size())));

                                            }

                                        }

                                    }

                                }

                                can_run_function = true;

                            } else if (type.startsWith("2") == true) {

                                // Function
                                {

                                    // Separate like this because start and end function doesn't need to test "can_run_function"
                                    if (can_run_function == true || (type.equals("210") == true || type.equals("220") == true)) {

                                        function = map_function.getOrDefault(type, "");

                                        if (function.isEmpty() == false) {

                                            functionAdd(chunk_pos, pos, "functions/" + function);

                                        }

                                    }

                                }

                            }

                        }

                    }

                }

            }

        }

    }

    private static void functionAdd (ChunkPos chunk_pos, BlockPos pos, String path) {

        cache_functions.computeIfAbsent(chunk_pos, create -> new HashMap<>()).computeIfAbsent(pos, create -> new ArrayList<>()).add(path);

    }

    private static void functionRun (LevelAccessor level_accessor, ServerLevel level_server, ChunkPos chunk_pos) {

        if (cache_functions.containsKey(chunk_pos) == true) {

            for (Map.Entry<BlockPos, List<String>> entry : cache_functions.get(chunk_pos).entrySet()) {

                for (String get : entry.getValue()) {

                    TXTFunction.run(level_accessor, level_server, entry.getKey(), get, false);

                }

            }

            cache_functions.remove(chunk_pos);

        }

    }

}