package tannyjung.tanshugetrees_handcode.systems.world_gen;

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
import tannyjung.core.GameUtils;
import tannyjung.tanshugetrees_handcode.Handcode;
import tannyjung.tanshugetrees_handcode.config.ConfigMain;
import tannyjung.tanshugetrees_handcode.systems.Cache;
import tannyjung.tanshugetrees_handcode.systems.living_tree_mechanics.LeafLitter;
import tannyjung.tanshugetrees_handcode.systems.tree_generator.TreeFunction;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class TreePlacer {

    public static void start (LevelAccessor level_accessor, ServerLevel level_server, ChunkGenerator chunk_generator, String dimension, ChunkPos chunk_pos) {

        File file = new File(Handcode.directory_world_data + "/world_gen/place/" + dimension + "/" + (chunk_pos.x >> 5) + "," + (chunk_pos.z >> 5) + "/" + GameUtils.outside.quardtreeChunkToNode(chunk_pos.x, chunk_pos.z) + ".txt");

        if (file.exists() == true && file.isDirectory() == false) {

            String[] array_text = null;
            String[] from_to_chunk_get = null;
            int[] from_to_chunk = new int[4];
            String id = "";
            String chosen = "";
            String[] center_pos = null;
            int center_posX = 0;
            int center_posZ = 0;
            String[] rotation_mirrored = null;
            int rotation = 0;
            boolean mirrored = false;
            String[] other_data = null;

            // Reading
            {

                try { BufferedReader buffered_reader = new BufferedReader(new FileReader(file), 65536); String read_all = ""; while ((read_all = buffered_reader.readLine()) != null) {

                    {

                        if (read_all.equals("") == false) {

                            array_text = read_all.split("\\|");

                            // Get Tree Chunk Pos
                            {

                                try {

                                    from_to_chunk_get = array_text[0].split("/");
                                    from_to_chunk[0] = Integer.parseInt(from_to_chunk_get[0]);
                                    from_to_chunk[1] = Integer.parseInt(from_to_chunk_get[1]);
                                    from_to_chunk[2] = Integer.parseInt(from_to_chunk_get[2]);
                                    from_to_chunk[3] = Integer.parseInt(from_to_chunk_get[3]);

                                } catch (Exception ignored) {

                                    return;

                                }

                            }

                            if ((from_to_chunk[0] <= chunk_pos.x && chunk_pos.x <= from_to_chunk[2]) && (from_to_chunk[1] <= chunk_pos.z && chunk_pos.z <= from_to_chunk[3])) {

                                // Get Tree Data
                                {

                                    try {

                                        id = array_text[1];
                                        chosen = array_text[2];
                                        center_pos = array_text[3].split("/");
                                        center_posX = Integer.parseInt(center_pos[0]);
                                        center_posZ = Integer.parseInt(center_pos[1]);
                                        rotation_mirrored = array_text[4].split("/");
                                        rotation = Integer.parseInt(rotation_mirrored[0]);
                                        mirrored = Boolean.parseBoolean(rotation_mirrored[1]);
                                        other_data = array_text[5].split("(?<! )/(?! )");

                                    } catch (Exception ignored) {

                                        return;

                                    }

                                }

                                detailed_detection(level_accessor, level_server, chunk_generator, dimension, chunk_pos, from_to_chunk, id, chosen, center_posX, center_posZ, rotation, mirrored, other_data);

                            }

                        }

                    }

                } buffered_reader.close(); } catch (Exception exception) { OutsideUtils.exception(new Exception(), exception); }

            }

        }

    }

    private static void detailed_detection (LevelAccessor level_accessor, ServerLevel level_server, ChunkGenerator chunk_generator, String dimension, ChunkPos chunk_pos, int[] from_to_chunk, String id, String chosen, int center_posX, int center_posZ, int rotation, boolean mirrored, String[] other_data) {

        boolean already_tested = false;
        boolean pass = true;
        int center_posY = 0;
        int dead_tree_level = 0;

        // Already Tested
        {

            File file = new File(Handcode.directory_world_data + "/world_gen/detailed_detection/" + dimension + "/" + (chunk_pos.x >> 5) + "," + (chunk_pos.z >> 5) + "/" + GameUtils.outside.quardtreeChunkToNode(chunk_pos.x, chunk_pos.z) + ".txt");

            if (file.exists() == true && file.isDirectory() == false) {

                String[] get = new String[0];

                {

                    try { BufferedReader buffered_reader = new BufferedReader(new FileReader(file), 65536); String read_all = ""; while ((read_all = buffered_reader.readLine()) != null) {

                        {

                            if (read_all.startsWith(id + "|" + center_posX + "/" + center_posZ + "|") == true) {

                                already_tested = true;

                                try {

                                    get = read_all.split("\\|");
                                    pass = Boolean.parseBoolean(get[2]);
                                    center_posY = Integer.parseInt(get[3]);
                                    dead_tree_level = Integer.parseInt(get[4]);

                                } catch (Exception ignored) {

                                    return;

                                }

                                break;

                            }

                        }

                    } buffered_reader.close(); } catch (Exception exception) { OutsideUtils.exception(new Exception(), exception); }

                }

            }

        }

        if (already_tested == false) {

            {

                String tree_type = "";
                int start_height = 0;
                int sizeY = 0;
                String ground_block = "";
                double dead_tree_chance = 0.0;

                try {

                    tree_type = other_data[0];
                    start_height = Integer.parseInt(other_data[1]);
                    sizeY = Integer.parseInt(other_data[2]);
                    ground_block = other_data[3];
                    dead_tree_chance = Double.parseDouble(other_data[4]);
                    dead_tree_level = Integer.parseInt(other_data[5]);

                } catch (Exception ignored) {

                    return;

                }

                int center_chunkX = center_posX >> 4;
                int center_chunkZ = center_posZ >> 4;
                int originalY = chunk_generator.getBaseHeight(center_posX, center_posZ, Heightmap.Types.OCEAN_FLOOR_WG, level_accessor, level_server.getChunkSource().randomState());
                center_posY = originalY;

                test:
                {

                    // Structure Detection
                    {

                        int radius = ConfigMain.structure_detection_size;

                        if (radius > 0) {

                            for (int scanX = -radius; scanX < radius; scanX++) {

                                for (int scanZ = -radius; scanZ < radius; scanZ++) {

                                    ChunkAccess chunk = level_accessor.getChunk(center_chunkX + scanX, center_chunkZ + scanZ, ChunkStatus.STRUCTURE_REFERENCES, false);

                                    if (chunk != null) {

                                        for (Structure structure : chunk.getAllReferences().keySet().toArray(new Structure[0])) {

                                            // structure.type().equals(StructureType.MINESHAFT) == false

                                            if (structure.step().equals(GenerationStep.Decoration.SURFACE_STRUCTURES) == true || structure.step().equals(GenerationStep.Decoration.STRONGHOLDS) == true) {

                                                pass = false;
                                                break test;

                                            }

                                        }

                                    }

                                }

                            }

                        }

                    }

                    // Ground Level
                    {

                        ChunkAccess chunk = level_accessor.getChunk(center_chunkX, center_chunkZ, ChunkStatus.SURFACE, false);

                        if (chunk != null) {

                            // Get Y Level
                            center_posY = chunk.getHeight(Heightmap.Types.OCEAN_FLOOR_WG, center_posX, center_posZ) + 1;

                            // Ground Block
                            {

                                if (GameUtils.outside.configTestBlock(chunk.getBlockState(new BlockPos(center_posX, center_posY - 1, center_posZ)), ground_block) == false) {

                                    pass = false;
                                    break test;

                                }

                            }

                        }

                    }

                    center_posY = center_posY + start_height;

                    // Y Level Test
                    {

                        // Build Height Limit
                        {

                            if (center_posY + sizeY >= level_accessor.getMaxBuildHeight()) {

                                pass = false;
                                break test;

                            }

                            if (originalY == level_accessor.getMinBuildHeight()) {

                                pass = false;
                                break test;

                            }

                        }

                        // Max Height Spawn
                        {

                            if (ConfigMain.max_height_spawn != 0) {

                                if (originalY > ConfigMain.max_height_spawn) {

                                    pass = false;
                                    break test;

                                }

                            }

                        }

                    }

                    // Surface Smoothness
                    {

                        if (ConfigMain.surface_smoothness_detection == true) {

                            int size = ConfigMain.surface_detection_size;
                            int height = ConfigMain.surface_smoothness_detection_height;
                            int pos1 = chunk_generator.getBaseHeight(center_posX + size, center_posZ + size, Heightmap.Types.OCEAN_FLOOR_WG, level_accessor, level_server.getChunkSource().randomState());
                            int pos2 = chunk_generator.getBaseHeight(center_posX + size, center_posZ - size, Heightmap.Types.OCEAN_FLOOR_WG, level_accessor, level_server.getChunkSource().randomState());
                            int pos3 = chunk_generator.getBaseHeight(center_posX - size, center_posZ + size, Heightmap.Types.OCEAN_FLOOR_WG, level_accessor, level_server.getChunkSource().randomState());
                            int pos4 = chunk_generator.getBaseHeight(center_posX - size, center_posZ - size, Heightmap.Types.OCEAN_FLOOR_WG, level_accessor, level_server.getChunkSource().randomState());

                            if ((Math.abs(originalY - pos1) > height) || (Math.abs(originalY - pos2) > height) || (Math.abs(originalY - pos3) > height) || (Math.abs(originalY - pos4) > height)) {

                                pass = false;
                                break test;

                            }

                        }

                    }

                    // Dead Tree and Tree Type
                    {

                        if (tree_type.equals("adapt") == true) {

                            dead_tree_level = 0;

                        } else {

                            int highestY = chunk_generator.getBaseHeight(center_posX, center_posZ, Heightmap.Types.WORLD_SURFACE_WG, level_accessor, level_server.getChunkSource().randomState());

                            if ((tree_type.equals("land") == true && (originalY == highestY)) || (tree_type.equals("water") == true && (originalY < highestY))) {

                                if (Math.random() >= dead_tree_chance) {

                                    dead_tree_level = 0;

                                }

                            } else {

                                if (Math.random() < ConfigMain.unviable_ecology_skip_chance) {

                                    pass = false;

                                }

                            }

                        }

                    }

                }

                // Write File
                {

                    int from_chunkX = from_to_chunk[0];
                    int from_chunkZ = from_to_chunk[1];
                    int to_chunkX = from_to_chunk[2];
                    int to_chunkZ = from_to_chunk[3];
                    StringBuilder write = new StringBuilder();

                    {

                        write
                                .append(id)
                                .append("|")
                                .append(center_posX)
                                .append("/")
                                .append(center_posZ)
                                .append("|")
                                .append(pass)
                                .append("|")
                                .append(center_posY)
                                .append("|")
                                .append(dead_tree_level)
                        ;

                        write.append("\n");

                    }

                    int size = 32 >> 2;
                    int from_chunkX_test = (int) (Math.floor((double) from_chunkX / (double) size) * size);
                    int from_chunkZ_test = (int) (Math.floor((double) from_chunkZ / (double) size) * size);
                    int to_chunkX_test = (int) (Math.floor((double) to_chunkX / (double) size) * size);
                    int to_chunkZ_test = (int) (Math.floor((double) to_chunkZ / (double) size) * size);

                    for (int scanX = from_chunkX_test; scanX <= to_chunkX_test; scanX = scanX + size) {

                        for (int scanZ = from_chunkZ_test; scanZ <= to_chunkZ_test; scanZ = scanZ + size) {

                            FileManager.writeTXT(Handcode.directory_world_data + "/world_gen/detailed_detection/" + dimension + "/" + (scanX >> 5) + "," + (scanZ >> 5) + "/" + GameUtils.outside.quardtreeChunkToNode(scanX, scanZ) + ".txt", write.toString(), true);

                        }

                    }

                }

            }

        }

        if (pass == true) {

            getData(level_accessor, level_server, chunk_pos, id, chosen, center_posX, center_posY, center_posZ, rotation, mirrored, dead_tree_level);

        }

    }

    private static void getData (LevelAccessor level_accessor, ServerLevel level_server, ChunkPos chunk_pos, String id, String chosen, int center_posX, int center_posY, int center_posZ, int rotation, boolean mirrored, int dead_tree_level) {

        String path_storage = "";
        String path_tree_settings = "";

        // Scan "World Gen" File
        {

            File file = new File(Handcode.directory_config + "/#dev/custom_packs_organized/world_gen/" + id + ".txt");

            if (file.exists() == true) {

                {

                    try { BufferedReader buffered_reader = new BufferedReader(new FileReader(file), 65536); String read_all = ""; while ((read_all = buffered_reader.readLine()) != null) {

                        {

                            if (read_all.startsWith("path_storage = ") == true) {

                                path_storage = read_all.replace("path_storage = ", "");

                            } else if (read_all.startsWith("path_tree_settings = ") == true) {

                                path_tree_settings = read_all.replace("path_tree_settings = ", "");

                            } else {

                                break;

                            }

                        }

                    } buffered_reader.close(); } catch (Exception exception) { OutsideUtils.exception(new Exception(), exception); }

                }

            }

        }

        File file = new File(Handcode.directory_config + "/custom_packs/" + path_storage.replace("/", "/presets/") + "/storage/" + chosen);

        if (file.exists() == true && file.isDirectory() == false) {

            short[] shape = Cache.tree_shape(path_storage + "/" + chosen);

            if (shape != null) {

                Map<String, String> map_block = new HashMap<>();
                boolean can_disable_roots = false;
                boolean can_leaves_decay = false;
                boolean can_leaves_drop = false;
                boolean can_leaves_regrow = false;
                int[] leaves_type = new int[2];

                // Get Tree Settings
                {

                    File file_test = new File(Handcode.directory_config + "/#dev/custom_packs_organized/presets/" + path_tree_settings + "_settings.txt");

                    if (file_test.exists() == true && file_test.isDirectory() == false) {

                        String type_id = "";
                        String value = "";

                        // Scan
                        {

                            try { BufferedReader buffered_reader = new BufferedReader(new FileReader(file_test), 65536); String read_all = ""; while ((read_all = buffered_reader.readLine()) != null) {

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

                            } buffered_reader.close(); } catch (Exception exception) { OutsideUtils.exception(new Exception(), exception); }

                        }

                    } else {

                        return;

                    }

                }

                int block_count_trunk = 0;
                int block_count_bough = 0;
                int block_count_branch = 0;
                int block_count_limb = 0;
                int block_count_twig = 0;
                int block_count_sprig = 0;
                boolean hollowed = false;

                // Dead Tree Calculation
                {

                    if (dead_tree_level > 0) {

                        int seed = center_posX + center_posY + center_posZ;
                        short[] block_count = FileManager.readBIN(file.getPath(), 7, 12);

                        if (dead_tree_level >= 60) {

                            // Only Trunk
                            {

                                block_count_trunk = block_count[0];

                                if (dead_tree_level == 60 || dead_tree_level == 70) {

                                    block_count_trunk = (int) (Mth.nextDouble(RandomSource.create(seed), 0.0, 0.5) * (double) block_count_trunk);

                                } else if (dead_tree_level == 80 || dead_tree_level == 90) {

                                    block_count_trunk = (int) (Mth.nextDouble(RandomSource.create(seed), 0.5, 1.0) * (double) block_count_trunk);

                                }

                                if (dead_tree_level == 70 || dead_tree_level == 90) {

                                    hollowed = true;

                                }

                            }

                        } else {

                            // General
                            {

                                block_count_bough = block_count[1];

                                if (dead_tree_level == 50) {

                                    block_count_bough = (int) ((double) block_count_bough * Mth.nextDouble(RandomSource.create(seed + 1), 0.1, 0.9));

                                }

                                if (dead_tree_level < 50) {

                                    block_count_branch = block_count[2];

                                    if (dead_tree_level == 40) {

                                        block_count_branch = (int) ((double) block_count_branch * Mth.nextDouble(RandomSource.create(seed + 2), 0.1, 0.9));

                                    }

                                }

                                if (dead_tree_level < 40) {

                                    block_count_limb = block_count[3];

                                    if (dead_tree_level == 30) {

                                        block_count_limb = (int) ((double) block_count_limb * Mth.nextDouble(RandomSource.create(seed + 3), 0.1, 0.9));

                                    }

                                }

                                if (dead_tree_level < 30) {

                                    block_count_twig = block_count[4];

                                    if (dead_tree_level == 20) {

                                        block_count_twig = (int) ((double) block_count_twig * Mth.nextDouble(RandomSource.create(seed + 4), 0.1, 0.9));

                                    }

                                }

                                if (dead_tree_level < 20) {

                                    block_count_sprig = block_count[5];

                                    if (dead_tree_level == 10) {

                                        block_count_sprig = (int) ((double) block_count_sprig * Mth.nextDouble(RandomSource.create(seed + 5), 0.1, 0.9));

                                    }

                                }

                            }

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

                    boolean in_snowy_biome = GameUtils.biome.isTaggedAs(level_accessor.getBiome(new BlockPos(center_posX, center_posY, center_posZ)), "forge:is_snowy");
                    boolean can_run_function = false;
                    BlockState block = Blocks.AIR.defaultBlockState();
                    BlockPos pos = null;
                    double leaf_litter_world_gen_chance = 0.0;
                    int height_motion = 0;

                    for (short read_all : shape) {

                        // Loop and Get Data
                        {

                            loop = loop + 1;

                            if (loop > 4) {

                                loop = 1;

                            }

                            if (loop == 1) {

                                type = String.valueOf(read_all);

                            } else if (loop == 2) {

                                posX = read_all;

                            } else if (loop == 3) {

                                posY = read_all;

                            } else {

                                posZ = read_all;

                            }

                        }

                        if (loop == 4) {

                            // Dead Tree Reduction
                            {

                                if (dead_tree_level > 0) {

                                    // Basic Style
                                    {

                                        if (type.startsWith("120") == true) {

                                            continue;

                                        } else if (type.startsWith("119") == true) {

                                            if (block_count_sprig > 0) {

                                                block_count_sprig = block_count_sprig - 1;

                                            } else {

                                                continue;

                                            }

                                        } else if (type.startsWith("118") == true) {

                                            if (block_count_sprig == 0) {

                                                if (block_count_twig > 0) {

                                                    block_count_twig = block_count_twig - 1;

                                                } else {

                                                    continue;

                                                }

                                            }

                                        } else if (type.startsWith("117") == true) {

                                            if (block_count_twig == 0) {

                                                if (block_count_limb > 0) {

                                                    block_count_limb = block_count_limb - 1;

                                                } else {

                                                    continue;

                                                }

                                            }

                                        } else if (type.startsWith("116") == true) {

                                            if (block_count_limb == 0) {

                                                if (block_count_branch > 0) {

                                                    block_count_branch = block_count_branch - 1;

                                                } else {

                                                    continue;

                                                }

                                            }

                                        } else if (type.startsWith("115") == true) {

                                            if (block_count_branch == 0) {

                                                if (block_count_bough > 0) {

                                                    block_count_bough = block_count_bough - 1;

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

                                                if (block_count_trunk > 0) {

                                                    block_count_trunk = block_count_trunk - 1;

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

                            // Placing
                            {

                                pos_converted = GameUtils.outside.posRotationMirrored(posX, posZ, rotation, mirrored);
                                pos = new BlockPos(center_posX + pos_converted[0], center_posY + posY, center_posZ + pos_converted[1]);

                                if (chunk_pos.x == pos.getX() >> 4 && chunk_pos.z == pos.getZ() >> 4) {

                                    get = map_block.getOrDefault(type, "");

                                    if (get.equals("") == false) {

                                        if (type.startsWith("1") == true) {

                                            can_run_function = false;

                                            // Block
                                            {

                                                // No Roots
                                                {

                                                    if (ConfigMain.world_gen_roots == false && can_disable_roots == true) {

                                                        if (type.startsWith("110") == true || type.startsWith("111") == true || type.startsWith("112") == true) {

                                                            continue;

                                                        }

                                                    }

                                                }

                                                // Keep
                                                {

                                                    if (get.endsWith(" keep") == true) {

                                                        get = get.replace(" keep", "");

                                                        if (GameUtils.block.isTaggedAs(level_accessor.getBlockState(pos), "tanshugetrees:passable_blocks") == false || level_accessor.isWaterAt(pos) == true) {

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
                                                can_run_function = true;

                                            }

                                            // Summon Marker
                                            {

                                                if (posY == 0 && pos_converted[0] == 0 && pos_converted[1] == 0) {

                                                    if (ConfigMain.tree_location == true && dead_tree_level == 0) {

                                                        if (can_leaves_decay == true || can_leaves_drop == true || can_leaves_regrow == true) {

                                                            String marker_data = "ForgeData:{file:\"" + path_storage + "|" + chosen + "\",tree_settings:\"" + path_tree_settings + "\",rotation:" + rotation + ",mirrored:" + mirrored + "}";
                                                            GameUtils.command.run(level_server, center_posX + 0.5, center_posY + 0.5, center_posZ + 0.5, GameUtils.entity.summonCommand("marker", "TANSHUGETREES / TANSHUGETREES-tree_location", id, marker_data));

                                                        }

                                                    }

                                                }

                                            }

                                        } else if (type.startsWith("2") == true) {

                                            // Function
                                            {

                                                // Separate like this because start and end function doesn't need to test "can_run_function"
                                                if (can_run_function == true || type.equals("210") == true || type.equals("220") == true) {

                                                    TreeFunction.start(level_accessor, level_server, pos.getX(), pos.getY(), pos.getZ(), get, false);

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

        }

    }

}