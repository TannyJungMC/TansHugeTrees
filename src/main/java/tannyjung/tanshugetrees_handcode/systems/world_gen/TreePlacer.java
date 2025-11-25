package tannyjung.tanshugetrees_handcode.systems.world_gen;

import it.unimi.dsi.fastutil.longs.LongSet;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.Structure;
import tannyjung.core.FileManager;
import tannyjung.core.OutsideUtils;
import tannyjung.core.game.GameUtils;
import tannyjung.tanshugetrees_handcode.Handcode;
import tannyjung.tanshugetrees_handcode.config.ConfigMain;
import tannyjung.tanshugetrees_handcode.systems.Cache;
import tannyjung.tanshugetrees_handcode.systems.living_tree_mechanics.LeafLitter;

import java.nio.ByteBuffer;
import java.util.*;

public class TreePlacer {

    public static void start (LevelAccessor level_accessor, ServerLevel level_server, ChunkGenerator chunk_generator, String dimension, ChunkPos chunk_pos) {

        ByteBuffer get = FileManager.readBIN(Handcode.path_world_data + "/world_gen/place/" + dimension + "/" + (chunk_pos.x >> 5) + "," + (chunk_pos.z >> 5) + ".bin");

        while (get.remaining() > 0) {

            int from_chunkX = 0;
            int from_chunkZ = 0;
            int to_chunkX = 0;
            int to_chunkZ = 0;
            String id = "";
            String chosen = "";
            int center_posX = 0;
            int center_posZ = 0;
            int rotation = 0;
            boolean mirrored = false;
            int start_height_offset = 0;
            String ground_block = "";
            int dead_tree_level = 0;

            try {

                from_chunkX = get.getInt();
                from_chunkZ = get.getInt();
                to_chunkX = get.getInt();
                to_chunkZ = get.getInt();
                id = Cache.getDictionary(String.valueOf(get.getShort()), true);
                chosen = Cache.getDictionary(String.valueOf(get.getShort()), true);
                center_posX = get.getInt();
                center_posZ = get.getInt();
                rotation = get.get();
                mirrored = get.get() == 1;
                start_height_offset = get.getShort();
                ground_block = Cache.getDictionary(String.valueOf(get.getShort()), true);
                dead_tree_level = get.getShort();

            } catch (Exception ignored) {

                return;

            }

            if ((from_chunkX <= chunk_pos.x && chunk_pos.x <= to_chunkX) && (from_chunkZ <= chunk_pos.z && chunk_pos.z <= to_chunkZ)) {

                testDetailedDetection(level_accessor, level_server, chunk_generator, dimension, chunk_pos, from_chunkX, from_chunkZ, to_chunkX, to_chunkZ, id, chosen, center_posX, center_posZ, rotation, mirrored, start_height_offset, ground_block, dead_tree_level);

            }

        }

    }

    private static void testDetailedDetection (LevelAccessor level_accessor, ServerLevel level_server, ChunkGenerator chunk_generator, String dimension, ChunkPos chunk_pos, int from_chunkX, int from_chunkZ, int to_chunkX, int to_chunkZ, String id, String chosen, int center_posX, int center_posZ, int rotation, boolean mirrored, int start_height_offset, String ground_block, int dead_tree_level) {

        boolean already_tested = false;
        boolean pass = false;
        int center_posY = 0;
        int center_chunkX = center_posX >> 4;
        int center_chunkZ = center_posZ >> 4;

        // Already Tested
        {

            ByteBuffer detailed_detection = FileManager.readBIN(Handcode.path_world_data + "/world_gen/detailed_detection/" + dimension + "/" + (chunk_pos.x >> 5) + "," + (chunk_pos.z >> 5) + ".bin");
            boolean get_pass = false;
            int get_posX = 0;
            int get_posY = 0;
            int get_posZ = 0;
            int get_dead_tree_level = 0;

            while (detailed_detection.remaining() > 0) {

                try {

                    get_pass = detailed_detection.get() == 1;
                    get_posX = detailed_detection.getInt();
                    get_posY = detailed_detection.getInt();
                    get_posZ = detailed_detection.getInt();
                    get_dead_tree_level = detailed_detection.getShort();

                } catch (Exception ignored) {

                    return;

                }

                if (center_posX == get_posX && center_posZ == get_posZ) {

                    already_tested = true;
                    pass = get_pass;
                    center_posY = get_posY;
                    dead_tree_level = get_dead_tree_level;
                    break;

                }

            }

        }

        if (already_tested == false || pass == true) {

            String path_storage = "";
            String path_tree_settings = "";

            // Scan World Gen File
            {

                String[] world_gen_settings = Cache.getWorldGenSettings(id);

                if (world_gen_settings.length != 0) {

                    for (String read_all : world_gen_settings) {

                        {

                            if (read_all.startsWith("path_storage = ") == true) {

                                path_storage = read_all.replace("path_storage = ", "");

                            } else if (read_all.startsWith("path_tree_settings = ") == true) {

                                path_tree_settings = read_all.replace("path_tree_settings = ", "");
                                break;

                            }

                        }

                    }

                }

            }

            boolean coarse_woody_debris = dead_tree_level > 200;
            int fallen_direction = 0;

            if (coarse_woody_debris == true) {

                fallen_direction = RandomSource.create(level_server.getSeed() ^ ((center_posX * 341873128712L) + (center_posZ * 132897987541L))).nextInt(4) + 1;

            }

            if (already_tested == false) {

                String tree_type = "";
                int start_height = 0;

                // Scan Tree Settings File
                {

                    String[] tree_settings = Cache.getTreeSettings(path_tree_settings);

                    if (tree_settings.length > 0) {

                        for (String read_all : tree_settings) {

                            {

                                if (read_all.startsWith("tree_type = ") == true) {

                                    tree_type = read_all.replace("tree_type = ", "");

                                } else if (read_all.startsWith("start_height = ") == true) {

                                    start_height = Integer.parseInt(read_all.replace("start_height = ", ""));
                                    break;

                                }

                            }

                        }

                    }

                }

                int up_sizeY = 0;

                // Get Size Y
                {

                    short[] data = Cache.getTreeShapePart1(path_storage + "/" + chosen);

                    if (data.length > 0) {

                        up_sizeY = data[1] - data[4];

                    }

                }

                test:
                {

                    // Structure Detection
                    {

                        int radius = ConfigMain.structure_detection_size;

                        if (radius > 0) {

                            ChunkAccess chunk = null;
                            Map<Structure, LongSet> references = new HashMap<>();

                            for (int scanX = -radius; scanX <= radius; scanX++) {

                                for (int scanZ = -radius; scanZ <= radius; scanZ++) {

                                    chunk = level_accessor.getChunk(center_chunkX + scanX, center_chunkZ + scanZ, ChunkStatus.STRUCTURE_REFERENCES, false);

                                    if (chunk != null) {

                                        references = chunk.getAllReferences();

                                        if (references != null) {

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

                    int originalY = chunk_generator.getBaseHeight(center_posX, center_posZ, Heightmap.Types.OCEAN_FLOOR_WG, level_accessor, level_server.getChunkSource().randomState());

                    // Ground Level
                    {

                        ChunkAccess chunk = level_accessor.getChunk(center_chunkX, center_chunkZ, ChunkStatus.SURFACE, false);

                        if (chunk != null) {

                            originalY = chunk.getHeight(Heightmap.Types.OCEAN_FLOOR, center_posX, center_posZ) + 1;

                            // Ground Block
                            {

                                if (GameUtils.misc.testCustomBlock(chunk.getBlockState(new BlockPos(center_posX, originalY - 1, center_posZ)), ground_block) == false) {

                                    break test;

                                }

                            }

                        }

                    }

                    if (coarse_woody_debris == false) {

                        center_posY = originalY + start_height + start_height_offset;

                    } else {

                        center_posY = originalY;

                    }

                    // Y Level Test
                    {

                        // Build Height Limit
                        {

                            if (center_posY + up_sizeY >= level_accessor.getMaxBuildHeight()) {

                                break test;

                            }

                            if (originalY == level_accessor.getMinBuildHeight()) {

                                break test;

                            }

                        }

                        // Max Height Spawn
                        {

                            if (ConfigMain.max_height_spawn != 0) {

                                if (originalY > ConfigMain.max_height_spawn) {

                                    break test;

                                }

                            }

                        }

                    }

                    // Surface Smoothness
                    {

                        if (ConfigMain.surface_smoothness_detection_size > 0) {

                            // Basic Test
                            {

                                int testY = chunk_generator.getBaseHeight(center_posX, center_posZ, Heightmap.Types.WORLD_SURFACE_WG, level_accessor, level_server.getChunkSource().randomState());
                                int size = ConfigMain.surface_smoothness_detection_size;
                                int height_up = testY + ConfigMain.surface_smoothness_detection_height_up;
                                int height_down = testY - ConfigMain.surface_smoothness_detection_height_down;

                                int pos1 = chunk_generator.getBaseHeight(center_posX + size, center_posZ + size, Heightmap.Types.WORLD_SURFACE_WG, level_accessor, level_server.getChunkSource().randomState());
                                int pos2 = chunk_generator.getBaseHeight(center_posX + size, center_posZ - size, Heightmap.Types.WORLD_SURFACE_WG, level_accessor, level_server.getChunkSource().randomState());
                                int pos3 = chunk_generator.getBaseHeight(center_posX - size, center_posZ + size, Heightmap.Types.WORLD_SURFACE_WG, level_accessor, level_server.getChunkSource().randomState());
                                int pos4 = chunk_generator.getBaseHeight(center_posX - size, center_posZ - size, Heightmap.Types.WORLD_SURFACE_WG, level_accessor, level_server.getChunkSource().randomState());
                                boolean test1 = (testY < pos1 && height_up > pos1) || (testY >= pos1 && pos1 > height_down);
                                boolean test2 = (testY < pos2 && height_up > pos2) || (testY >= pos2 && pos2 > height_down);
                                boolean test3 = (testY < pos3 && height_up > pos3) || (testY >= pos3 && pos3 > height_down);
                                boolean test4 = (testY < pos4 && height_up > pos4) || (testY >= pos4 && pos4 > height_down);

                                if (test1 == false || test2 == false || test3 == false || test4 == false) {

                                    break test;

                                }

                            }

                            if (coarse_woody_debris == true) {

                                {

                                    short[] shape = Cache.getTreeShapePart3(path_storage + "/" + chosen);

                                    if (shape.length > 0) {

                                        boolean test_pass = false;

                                        int loop = 0;
                                        String type = "";
                                        int posX = 0;
                                        int posY = 0;
                                        int posZ = 0;

                                        int[] reduce_parts = getPartReduce(level_server, center_posX, center_posZ, path_storage, chosen, dead_tree_level);
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
                                        int[] pos_converted = new int[0];
                                        int testY = 0;
                                        Map<String, Integer> previousY = new HashMap<>();

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

                                                    // Skip Roots
                                                    {

                                                        if (type.startsWith("110") == true || type.startsWith("111") == true || type.startsWith("112") == true || type.startsWith("113") == true) {

                                                            continue;

                                                        }

                                                    }

                                                    // Dead Tree Reduction
                                                    {

                                                        if (dead_tree_level_test > 0) {

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

                                                            if (dead_tree_level_test >= 60) {

                                                                // Only Trunk
                                                                {

                                                                    if (type.startsWith("114") == true) {

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

                                                        pos_converted = OutsideUtils.convertPosRotationMirrored(posX, posY, posZ, rotation, mirrored);
                                                        pos_converted = OutsideUtils.convertPosFallen(pos_converted[0], pos_converted[1], pos_converted[2], fallen_direction);
                                                        posX = center_posX + pos_converted[0];
                                                        posY = center_posY + pos_converted[1];
                                                        posZ = center_posZ + pos_converted[2];

                                                        if (previousY.containsKey(posX + "/" + posZ) == true) {

                                                            if (previousY.get(posX + "/" + posZ) <= posY) {

                                                                continue;

                                                            }

                                                        }

                                                        previousY.put(posX + "/" + posZ, posY);
                                                        testY = chunk_generator.getBaseHeight(posX, posZ, Heightmap.Types.OCEAN_FLOOR_WG, level_accessor, level_server.getChunkSource().randomState());

                                                        if (testY >= posY) {

                                                            test_pass = true;
                                                            break;

                                                        }

                                                    }

                                                }


                                            }

                                        }

                                        if (test_pass == false) {

                                            break test;

                                        }

                                    }

                                }

                            }

                        }

                    }

                    // Tree Type
                    {

                        if (tree_type.equals("adapt") == false) {

                            int highestY = chunk_generator.getBaseHeight(center_posX, center_posZ, Heightmap.Types.WORLD_SURFACE_WG, level_accessor, level_server.getChunkSource().randomState());

                            if ((tree_type.equals("land") == true && (originalY < highestY)) || (tree_type.equals("water") == true && (originalY == highestY))) {

                                if (dead_tree_level == 0) {

                                    break test;

                                } else {

                                    RandomSource random = RandomSource.create(level_server.getSeed() ^ ((center_posX * 341873128712L) + (center_posZ * 132897987541L) + center_posY));

                                    if (random.nextDouble() < ConfigMain.unviable_ecology_skip_chance) {

                                        break test;

                                    }

                                }

                            }

                        }

                    }

                    pass = true;

                }

                // Write File
                {

                    int from_chunkX_test = from_chunkX >> 5;
                    int from_chunkZ_test = from_chunkZ >> 5;
                    int to_chunkX_test = to_chunkX >> 5;
                    int to_chunkZ_test = to_chunkZ >> 5;

                    List<String> write = new ArrayList<>();
                    write.add("l" + pass);
                    write.add("i" + center_posX);
                    write.add("i" + center_posY);
                    write.add("i" + center_posZ);
                    write.add("s" + dead_tree_level);

                    for (int scanX = from_chunkX_test; scanX <= to_chunkX_test; scanX++) {

                        for (int scanZ = from_chunkZ_test; scanZ <= to_chunkZ_test; scanZ++) {

                            FileManager.writeBIN(Handcode.path_world_data + "/world_gen/detailed_detection/" + dimension + "/" + scanX + "," + scanZ + ".bin", write, true);

                        }

                    }

                }

            }

            if (pass == true) {

                place(level_accessor, level_server, chunk_pos, id, chosen, path_storage, path_tree_settings, center_posX, center_posY, center_posZ, rotation, mirrored, dead_tree_level, fallen_direction);

            }

        }

    }

    private static int[] getPartReduce (ServerLevel level_server, int center_posX, int center_posZ, String path_storage, String chosen, int dead_tree_level) {

        int[] reduce_parts = new int[6];
        RandomSource random = RandomSource.create(level_server.getSeed() ^ ((center_posX * 341873128712L) + (center_posZ * 132897987541L)));
        int[] block_count = Cache.getTreeShapePart2(path_storage + "/" + chosen);
        int reduce_trunk = 0;
        int reduce_bough = 0;
        int reduce_branch = 0;
        int reduce_limb = 0;
        int reduce_twig = 0;
        int reduce_sprig = 0;
        dead_tree_level = Integer.parseInt(String.valueOf(dead_tree_level).substring(1));

        {

            reduce_trunk = block_count[0];

            if (dead_tree_level >= 60) {

                // Only Trunk
                {

                    if (dead_tree_level == 60 || dead_tree_level == 70) {

                        reduce_trunk = (int) (Mth.nextDouble(random, 0.5, 1.0) * (double) reduce_trunk);

                    } else if (dead_tree_level == 80 || dead_tree_level == 90) {

                        reduce_trunk = (int) (Mth.nextDouble(random, 0.0, 0.5) * (double) reduce_trunk);

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

    private static void place (LevelAccessor level_accessor, ServerLevel level_server, ChunkPos chunk_pos, String id, String chosen, String path_storage, String path_tree_settings, int center_posX, int center_posY, int center_posZ, int rotation, boolean mirrored, int dead_tree_level, int fallen_direction) {

        short[] shape = Cache.getTreeShapePart3(path_storage + "/" + chosen);

        if (shape.length > 0) {

            Map<String, String> map_block = new HashMap<>();
            boolean can_disable_roots = false;
            boolean can_leaves_decay = false;
            boolean can_leaves_drop = false;
            boolean can_leaves_regrow = false;
            int[] leaves_type = new int[2];

            // Get Tree Settings File
            {

                String[] tree_settings = Cache.getTreeSettings(path_tree_settings);

                if (tree_settings.length > 0) {

                    String type_id = "";
                    String value = "";

                    for (String read_all : tree_settings) {

                        {

                            if (read_all.startsWith("can_leaves_decay = ") == true) {

                                can_leaves_decay = Boolean.parseBoolean(read_all.replace("can_leaves_decay = ", ""));

                            } else if (read_all.startsWith("can_leaves_drop = ") == true) {

                                can_leaves_drop = Boolean.parseBoolean(read_all.replace("can_leaves_drop = ", ""));

                            } else if (read_all.startsWith("can_leaves_regrow = ") == true) {

                                can_leaves_regrow = Boolean.parseBoolean(read_all.replace("can_leaves_regrow = ", ""));

                            } else if (read_all.startsWith("can_disable_roots") == true) {

                                can_disable_roots = Boolean.parseBoolean(read_all.replace("can_disable_roots = ", ""));

                            } else if (read_all.startsWith("Block ") == true) {

                                {

                                    type_id = read_all.substring("Block ### ".length(), "Block ### ####".length());
                                    value = read_all.substring("Block ### #### = ".length());
                                    map_block.put(type_id, value);

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

                                            if (ConfigMain.deciduous_leaves_list.contains(value) == true) {

                                                leaves_type[number] = 1;

                                            } else if (ConfigMain.coniferous_leaves_list.contains(value) == true) {

                                                leaves_type[number] = 2;

                                            }

                                        }

                                    }

                                }

                            } else if (read_all.startsWith("Function ") == true) {

                                {

                                    type_id = read_all.substring("Function ## ".length(), "Function ## ###".length());
                                    value = read_all.substring("Function ## ### = ".length());
                                    map_block.put(type_id, value);

                                }

                            }

                        }

                    }

                }

            }

            boolean in_snowy_biome = GameUtils.biome.isTaggedAs(level_accessor.getBiome(new BlockPos(center_posX, center_posY, center_posZ)), "forge:is_snowy");
            boolean no_roots = (ConfigMain.world_gen_roots == false && can_disable_roots);
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

                    int[] reduce_parts = getPartReduce(level_server, center_posX, center_posZ, path_storage, chosen, dead_tree_level);
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

                int loop = 0;
                String type = "";
                int posX = 0;
                int posY = 0;
                int posZ = 0;

                int[] pos_converted = new int[0];
                String get = "";
                boolean can_run_function = false;
                BlockState block = Blocks.AIR.defaultBlockState();
                BlockPos pos = null;
                double leaf_litter_world_gen_chance = 0.0;
                int height_motion = 0;

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

                        pos_converted = OutsideUtils.convertPosRotationMirrored(posX, posY, posZ, rotation, mirrored);
                        pos_converted = OutsideUtils.convertPosFallen(pos_converted[0], pos_converted[1], pos_converted[2], fallen_direction);
                        pos = new BlockPos(center_posX + pos_converted[0], center_posY + pos_converted[1], center_posZ + pos_converted[2]);

                        if (chunk_pos.x == pos.getX() >> 4 && chunk_pos.z == pos.getZ() >> 4) {

                            // Get Block or Function
                            {

                                get = map_block.getOrDefault(type, "");

                                if (get.equals("") == true) {

                                    continue;

                                }

                            }

                            if (type.startsWith("1") == true) {

                                // Test and Place Block
                                {

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

                                    // Keep
                                    {

                                        if (get.endsWith(" keep") == true) {

                                            get = get.replace(" keep", "");

                                            if (level_accessor.getBlockState(pos).isAir() == false) {

                                                continue;

                                            }

                                        }

                                    }

                                    block = GameUtils.block.fromText(get);

                                    // Leaves
                                    {

                                        if (type.startsWith("120") == true && GameUtils.block.isTaggedAs(block, "minecraft:leaves") == true) {

                                            // Pre Leaves Drop
                                            {

                                                if (ConfigMain.leaf_litter == true && ConfigMain.leaf_litter_world_gen == true) {

                                                    if (can_leaves_drop == true) {

                                                        // Get "Chance" Value
                                                        {

                                                            if ((type.endsWith("1") == true && leaves_type[0] == 2) || (type.endsWith("2") == true && leaves_type[1] == 2)) {

                                                                leaf_litter_world_gen_chance = ConfigMain.leaf_litter_world_gen_chance_coniferous;

                                                            } else {

                                                                leaf_litter_world_gen_chance = ConfigMain.leaf_litter_world_gen_chance;

                                                            }

                                                        }

                                                        if (Math.random() < leaf_litter_world_gen_chance) {

                                                            height_motion = level_accessor.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, pos.getX(), pos.getZ());

                                                            if (height_motion != level_accessor.getMinBuildHeight() && height_motion < pos.getY()) {

                                                                LeafLitter.start(level_accessor, pos.getX(), height_motion, pos.getZ(), block, false);

                                                            }

                                                        }

                                                    }

                                                }

                                            }

                                            // Abscission (World Gen)
                                            {

                                                if (ConfigMain.abscission_world_gen == true) {

                                                    if (in_snowy_biome == true) {

                                                        if ((type.endsWith("1") == true && leaves_type[0] == 1) || (type.endsWith("2") == true && leaves_type[1] == 1)) {

                                                            continue;

                                                        }

                                                    }

                                                }

                                            }

                                        }

                                    }

                                    // Automatic Waterlogged
                                    {

                                        if (level_accessor.isWaterAt(pos) == true) {

                                            block = GameUtils.block.propertyBooleanSet(block, "waterlogged", true);

                                        }

                                    }

                                    level_accessor.setBlock(pos, block, 0);

                                }

                                // Summon Marker
                                {

                                    // At Center
                                    if (posX == 0 && posY == 0 && posZ == 0) {

                                        if (ConfigMain.tree_location == true && dead_tree_level == 0) {

                                            if (can_leaves_decay == true || can_leaves_drop == true || can_leaves_regrow == true) {

                                                String marker_data = "ForgeData:{file:\"" + path_storage + "|" + chosen + "\",tree_settings:\"" + path_tree_settings + "\",rotation:" + rotation + ",mirrored:" + mirrored + "}";
                                                GameUtils.command.run(level_server, center_posX + 0.5, center_posY + 0.5, center_posZ + 0.5, GameUtils.command.summonEntity("marker", "TANSHUGETREES / TANSHUGETREES-tree_location", id, marker_data));

                                            }

                                        }

                                    }

                                }

                                // Tree Decoration
                                {

                                    if (type.startsWith("120") == false) {

                                        if (dead_tree_level == 0) {

                                            for (String name : Cache.getFunctionTreeDecoration()) {

                                                WorldGenBeforePlants.functionAdd(chunk_pos, pos, "#TannyJung-Main-Pack/tree_decoration/" + name);

                                            }

                                        } else {

                                            for (String name : Cache.getFunctionTreeDecorationDecay()) {

                                                WorldGenBeforePlants.functionAdd(chunk_pos, pos, "#TannyJung-Main-Pack/tree_decoration_decay/" + name);

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

                                        WorldGenBeforePlants.functionAdd(chunk_pos, pos, get);

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