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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

public class TreePlacer {

    public static void start (LevelAccessor level_accessor, ServerLevel level_server, ChunkGenerator chunk_generator, String dimension, ChunkPos chunk_pos) {

        // Read To Get Tree(s)
        {

            File file = new File(Handcode.directory_world_data + "/world_gen/place/" + dimension + "/" + (chunk_pos.x >> 5) + "," + (chunk_pos.z >> 5) + "/" + GameUtils.outside.quardtreeChunkToNode(chunk_pos.x, chunk_pos.z) + ".txt");

            if (file.exists() == true) {

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

                    // Structure Area
                    {

                        ChunkAccess chunk = level_accessor.getChunk(center_chunkX, center_chunkZ, ChunkStatus.STRUCTURE_REFERENCES, false);

                        if (chunk != null) {

                            Structure[] structures = chunk.getAllReferences().keySet().toArray(new Structure[0]);

                            for (Structure structure : structures) {

                                // structure.type().equals(StructureType.MINESHAFT) == false
                                // structure.terrainAdaptation().equals(TerrainAdjustment.NONE) == true

                                if (structure.step().equals(GenerationStep.Decoration.SURFACE_STRUCTURES) == true) {

                                    pass = false;
                                    break test;

                                }

                            }

                        }

                    }

                    // Ground Level
                    {

                        ChunkAccess chunk = level_accessor.getChunk(center_chunkX, center_chunkZ, ChunkStatus.SURFACE, false);

                        if (chunk != null) {

                            // Get Y Level
                            {

                                center_posY = chunk.getHeight(Heightmap.Types.OCEAN_FLOOR_WG, center_posX, center_posZ) + 1;

                            }

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

                        // Max Build Limit
                        {

                            if (center_posY + sizeY > level_accessor.getMaxBuildHeight()) {

                                pass = false;
                                break test;

                            }

                        }

                        // Max Height Spawn
                        {

                            if (center_posY + sizeY > level_accessor.getMaxBuildHeight() || center_posY > ConfigMain.max_height_spawn) {

                                pass = false;
                                break test;

                            }

                        }

                    }

                    // Surface Smoothness
                    {

                        if (ConfigMain.surrounding_area_detection == true && ConfigMain.surface_smoothness_detection == true) {

                            int size = ConfigMain.surrounding_area_detection_size;
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

                        if (Math.random() >= dead_tree_chance) {

                            if (tree_type.equals("adapt") == true) {

                                dead_tree_level = 0;

                            } else {

                                int highestY = chunk_generator.getBaseHeight(center_posX, center_posZ, Heightmap.Types.WORLD_SURFACE_WG, level_accessor, level_server.getChunkSource().randomState());

                                if ((tree_type.equals("land") == true && (originalY == highestY)) || (tree_type.equals("water") == true && (originalY < highestY))) {

                                    dead_tree_level = 0;

                                } else {

                                    if (Math.random() < ConfigMain.unviable_ecology_skip_chance) {

                                        pass = false;

                                    }

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

        String storage_directory = "";
        String tree_settings = "";

        // Scan "World Gen" File
        {

            File file = new File(Handcode.directory_config + "/custom_packs/.organized/world_gen/" + id + ".txt");

            if (file.exists() == true) {

                {

                    try { BufferedReader buffered_reader = new BufferedReader(new FileReader(file), 65536); String read_all = ""; while ((read_all = buffered_reader.readLine()) != null) {

                        {

                            if (read_all.startsWith("storage_directory = ") == true) {

                                storage_directory = read_all.replace("storage_directory = ", "");

                            } else if (read_all.startsWith("tree_settings = ") == true) {

                                tree_settings = read_all.replace("tree_settings = ", "");

                            } else {

                                break;

                            }

                        }

                    } buffered_reader.close(); } catch (Exception exception) { OutsideUtils.exception(new Exception(), exception); }

                }

            }

        }

        String location = storage_directory + "/" + chosen;
        File file_chosen = new File(Handcode.directory_config + "/custom_packs/" + location);

        if (file_chosen.exists() == true && file_chosen.isDirectory() == false) {

            Map<String, String> map_block = new HashMap<>();
            boolean can_disable_roots = false;
            boolean can_leaves_decay = false;
            boolean can_leaves_drop = false;
            boolean can_leaves_regrow = false;
            int[] leaves_type = new int[2];

            // Get Tree Settings
            {

                File file = new File(Handcode.directory_config + "/custom_packs/.organized/presets/" + tree_settings);

                if (file.exists() == true && file.isDirectory() == false) {

                    String get_short = "";
                    String get = "";
                    int leaves_type_test = 0;

                    // Scan
                    {

                        try { BufferedReader buffered_reader = new BufferedReader(new FileReader(file), 65536); String read_all = ""; while ((read_all = buffered_reader.readLine()) != null) {

                            {

                                if (read_all.startsWith("can_leaves_decay = ") == true) {

                                    {

                                        can_leaves_decay = Boolean.parseBoolean(read_all.replace("can_leaves_decay = ", ""));

                                    }

                                } else if (read_all.startsWith("can_leaves_drop = ") == true) {

                                    {

                                        can_leaves_drop = Boolean.parseBoolean(read_all.replace("can_leaves_drop = ", ""));

                                    }

                                } else if (read_all.startsWith("can_leaves_regrow = ") == true) {

                                    {

                                        can_leaves_regrow = Boolean.parseBoolean(read_all.replace("can_leaves_regrow = ", ""));

                                    }

                                } else if (read_all.startsWith("can_disable_roots") == true) {

                                    {

                                        can_disable_roots = Boolean.parseBoolean(read_all.replace("can_disable_roots = ", ""));

                                    }

                                } else if (read_all.startsWith("Block ") == true) {

                                    {

                                        get_short = read_all.substring("Block ".length(), "Block ###".length());
                                        get = read_all.substring("Block ### = ".length());
                                        map_block.put(get_short, get);

                                        // Leaves Types
                                        {

                                            if (get_short.startsWith("le") == true) {

                                                // Get ID
                                                {

                                                    if (get.endsWith(" keep") == true) {

                                                        get = get.substring(0, get.length() - (" keep").length());

                                                    }

                                                    if (get.endsWith("]") == true) {

                                                        get = get.substring(0, get.indexOf("["));

                                                    }

                                                }

                                                int number = Integer.parseInt(get_short.substring(2)) - 1;

                                                if (ConfigMain.deciduous_leaves_list.contains(get) == true) {

                                                    leaves_type[number] = 1;

                                                } else if (ConfigMain.coniferous_leaves_list.contains(get) == true) {

                                                    leaves_type[number] = 2;

                                                }

                                            }

                                        }

                                    }

                                } else if (read_all.startsWith("Function ") == true) {

                                    {

                                        get_short = read_all.substring("Function ".length(), "Function ##".length());
                                        get = read_all.substring("Function ## = ".length());
                                        map_block.put(get_short, get);

                                    }

                                }

                            }

                        } buffered_reader.close(); } catch (Exception exception) { OutsideUtils.exception(new Exception(), exception); }

                    }

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
                    double multiply = 0.0;

                    // Get Block Count
                    {

                        try { BufferedReader buffered_reader = new BufferedReader(new FileReader(file_chosen)); String read_all = ""; while ((read_all = buffered_reader.readLine()) != null) {

                            {

                                if (read_all.startsWith("---") == true) {

                                    break;

                                } else {

                                    if (dead_tree_level >= 60) {

                                        if (read_all.startsWith("block_count_trunk = ")) {

                                            {

                                                block_count_trunk = Integer.parseInt(read_all.replace("block_count_trunk = ", ""));

                                                if (dead_tree_level == 60 || dead_tree_level == 70) {

                                                    block_count_trunk = (int) (Mth.nextDouble(RandomSource.create(seed), 0.0, 0.5) * (double) block_count_trunk);

                                                } else if (dead_tree_level == 80 || dead_tree_level == 90) {

                                                    block_count_trunk = (int) (Mth.nextDouble(RandomSource.create(seed), 0.5, 1.0) * (double) block_count_trunk);

                                                }

                                                if (dead_tree_level == 70 || dead_tree_level == 90) {

                                                    hollowed = true;

                                                }

                                            }

                                        }

                                    } else {

                                        multiply = 1.0;

                                        if (read_all.startsWith("block_count_bough = ")) {

                                            {

                                                if (dead_tree_level == 50) {

                                                    multiply = Mth.nextDouble(RandomSource.create(seed + 5), 0.1, 0.5);

                                                }

                                                block_count_bough = (int) (Double.parseDouble(read_all.replace("block_count_bough = ", "")) * multiply);

                                            }

                                        } else if (read_all.startsWith("block_count_branch = ")) {

                                            {

                                                if (dead_tree_level > 49) {

                                                    continue;

                                                }

                                                if (dead_tree_level == 40) {

                                                    multiply = Mth.nextDouble(RandomSource.create(seed + 4), 0.1, 0.5);

                                                }

                                                block_count_branch = (int) (Double.parseDouble(read_all.replace("block_count_branch = ", "")) * multiply);

                                            }

                                        } else if (read_all.startsWith("block_count_limb = ")) {

                                            {

                                                if (dead_tree_level > 39) {

                                                    continue;

                                                }

                                                if (dead_tree_level == 30) {

                                                    multiply = Mth.nextDouble(RandomSource.create(seed + 3), 0.1, 0.5);

                                                }

                                                block_count_limb = (int) (Double.parseDouble(read_all.replace("block_count_limb = ", "")) * multiply);

                                            }

                                        } else if (read_all.startsWith("block_count_twig = ")) {

                                            {

                                                if (dead_tree_level > 29) {

                                                    continue;

                                                }

                                                if (dead_tree_level == 20) {

                                                    multiply = Mth.nextDouble(RandomSource.create(seed + 2), 0.1, 0.5);

                                                }

                                                block_count_twig = (int) (Double.parseDouble(read_all.replace("block_count_twig = ", "")) * multiply);

                                            }

                                        } else if (read_all.startsWith("block_count_sprig = ")) {

                                            {

                                                if (dead_tree_level > 19) {

                                                    continue;

                                                }

                                                if (dead_tree_level == 10) {

                                                    multiply = Mth.nextDouble(RandomSource.create(seed + 1), 0.1, 0.5);

                                                }

                                                block_count_sprig = (int) (Double.parseDouble(read_all.replace("block_count_sprig = ", "")) * multiply);

                                            }

                                        }

                                    }

                                }

                            }

                        } buffered_reader.close(); } catch (Exception exception) { OutsideUtils.exception(new Exception(), exception); }

                    }

                }

            }

            String get_short = "";
            String get = "";
            String get_pos = "";
            int[] get_pos_array = new int[0];
            int testX = 0;
            int testY = 0;
            int testZ = 0;

            boolean in_snowy_biome = GameUtils.biome.isTaggedAs(level_accessor.getBiome(new BlockPos(center_posX, center_posY, center_posZ)), "forge:is_snowy");
            boolean can_run_function = false;
            BlockState block = Blocks.AIR.defaultBlockState();
            BlockPos pos = null;
            double pre_leaf_litter_chance = 0.0;
            int height_motion_block = 0;

            // Read File
            {

                for (String read_all : Cache.tree_shape(location)) {

                    {

                        if (read_all.startsWith("+") == true) {

                            // Test "Block" or "Function" and get value from map
                            {

                                if (read_all.startsWith("+b") == true) {

                                    get_short = read_all.substring(read_all.length() - 3);

                                } else if (read_all.startsWith("+f") == true) {

                                    get_short = read_all.substring(read_all.length() - 2);

                                }

                                get = map_block.get(get_short);

                                // This fix the error >>> Cannot invoke "String.equals(Object)" because "get" is null
                                {

                                    try {

                                        if (get.equals("") == true) {

                                            continue;

                                        }

                                    } catch (Exception exception) {

                                        continue;

                                    }

                                }

                            }

                            if (read_all.startsWith("+b") == true) {

                                can_run_function = false;

                                // No Roots
                                {

                                    if (ConfigMain.world_gen_roots == false && can_disable_roots == true) {

                                        if (get_short.startsWith("se") == true || get_short.startsWith("te") == true || get_short.startsWith("fi") == true) {

                                            continue;

                                        }

                                    }

                                }

                                // Dead Tree
                                {

                                    if (dead_tree_level > 0) {

                                        // Basic Style
                                        {

                                            if (get_short.startsWith("le") == true) {

                                                continue;

                                            } else if (get_short.startsWith("sp") == true) {

                                                if (block_count_sprig > 0) {

                                                    block_count_sprig = block_count_sprig - 1;

                                                } else {

                                                    continue;

                                                }

                                            } else if (get_short.startsWith("tw") == true) {

                                                if (block_count_sprig == 0) {

                                                    if (block_count_twig > 0) {

                                                        block_count_twig = block_count_twig - 1;

                                                    } else {

                                                        continue;

                                                    }

                                                }

                                            } else if (get_short.startsWith("li") == true) {

                                                if (block_count_twig == 0) {

                                                    if (block_count_limb > 0) {

                                                        block_count_limb = block_count_limb - 1;

                                                    } else {

                                                        continue;

                                                    }

                                                }

                                            } else if (get_short.startsWith("br") == true) {

                                                if (block_count_limb == 0) {

                                                    if (block_count_branch > 0) {

                                                        block_count_branch = block_count_branch - 1;

                                                    } else {

                                                        continue;

                                                    }

                                                }

                                            } else if (get_short.startsWith("bo") == true) {

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

                                                if (get_short.startsWith("tr") == true) {

                                                    if (block_count_trunk > 0) {

                                                        block_count_trunk = block_count_trunk - 1;

                                                        if (hollowed == true) {

                                                            if (get_short.startsWith("trc") == true) {

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

                            // Get Pos
                            {

                                if (read_all.startsWith("+b") == true) {

                                    get_pos = read_all.substring(2, read_all.length() - 3);

                                } else if (read_all.startsWith("+f") == true) {

                                    get_pos = read_all.substring(2, read_all.length() - 2);

                                }

                                get_pos_array = GameUtils.outside.textPosConverter(get_pos, rotation, mirrored);

                                // Apply Block Pos With Real Pos
                                testX = center_posX + get_pos_array[0];
                                testY = center_posY + get_pos_array[1];
                                testZ = center_posZ + get_pos_array[2];

                            }

                            // Test is in current chunk
                            if (chunk_pos.equals(new ChunkPos(testX >> 4, testZ >> 4)) == true) {

                                pos = new BlockPos(testX, testY, testZ);

                                // Keep
                                {

                                    if (get.endsWith(" keep") == true) {

                                        get = get.replace(" keep", "");

                                        if (GameUtils.block.isTaggedAs(level_accessor.getBlockState(pos), "tanshugetrees:passable_blocks") == false || level_accessor.isWaterAt(pos) == true) {

                                            continue;

                                        }

                                    }

                                }

                                if (read_all.startsWith("+b") == true) {

                                    // Place Block
                                    {

                                        block = GameUtils.block.fromText(get);

                                        if (block != Blocks.AIR.defaultBlockState()) {

                                            // Leaves
                                            {

                                                if (get_short.startsWith("le") == true && GameUtils.block.isTaggedAs(block, "minecraft:leaves") == true) {

                                                    // Pre Leaves Drop
                                                    {

                                                        if (ConfigMain.leaf_litter == true && ConfigMain.pre_leaf_litter == true) {

                                                            if (can_leaves_drop == true) {

                                                                // Get "Chance" Value
                                                                {

                                                                    if ((get_short.endsWith("1") == true && leaves_type[0] == 2) || (get_short.endsWith("2") == true && leaves_type[1] == 2)) {

                                                                        pre_leaf_litter_chance = ConfigMain.pre_leaf_litter_chance_coniferous;

                                                                    } else {

                                                                        pre_leaf_litter_chance = ConfigMain.pre_leaf_litter_chance;

                                                                    }

                                                                }

                                                                if (Math.random() < pre_leaf_litter_chance) {

                                                                    height_motion_block = level_accessor.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, testX, testZ);

                                                                    if (height_motion_block < testY) {

                                                                        LeafLitter.start(level_accessor, testX, height_motion_block, testZ, block, false);

                                                                    }

                                                                }

                                                            }

                                                        }

                                                    }

                                                    // Abscission (World Gen)
                                                    {

                                                        if (ConfigMain.abscission_world_gen == true) {

                                                            if (in_snowy_biome == true) {

                                                                if ((get_short.endsWith("1") == true && leaves_type[0] == 1) || (get_short.endsWith("2") == true && leaves_type[1] == 1)) {

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

                                            level_accessor.setBlock(pos, block, 2);
                                            can_run_function = true;

                                        }

                                    }

                                } else if (read_all.startsWith("+f") == true) {

                                    // Run Function
                                    {

                                        // Separate like this because start and end function doesn't need to test "can_run_function"
                                        if (can_run_function == true || get_short.equals("fs") == true || get_short.equals("fe") == true) {

                                            TreeFunction.start(level_accessor, level_server, testX, testY, testZ, get, false);

                                        }

                                    }

                                }

                            }

                        } else if (read_all.startsWith("---") == true) {

                            // Summon Marker
                            {

                                if (chunk_pos.equals(new ChunkPos(center_posX >> 4, center_posZ >> 4)) == true) {

                                    if (ConfigMain.tree_location == true && dead_tree_level == 0) {

                                        if (can_leaves_decay == true || can_leaves_drop == true || can_leaves_regrow == true) {

                                            String marker_data = "ForgeData:{file:\"" + storage_directory + "\",settings:\"" + tree_settings + "\",rotation:" + rotation + ",mirrored:" + mirrored + "}";
                                            GameUtils.command.run(level_server, center_posX + 0.5, center_posY + 0.5, center_posZ + 0.5, GameUtils.entity.summonCommand("marker", "TANSHUGETREES / TANSHUGETREES-tree_location", id, marker_data));

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