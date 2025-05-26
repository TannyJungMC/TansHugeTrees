package tannyjung.tanshugetrees_handcode.systems.world_gen;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraftforge.registries.ForgeRegistries;
import tannyjung.tanshugetrees_handcode.misc.FileManager;
import tannyjung.tanshugetrees_handcode.misc.GameUtils;
import tannyjung.tanshugetrees.TanshugetreesMod;
import tannyjung.tanshugetrees_handcode.Handcode;
import tannyjung.tanshugetrees_handcode.systems.config.ConfigMain;
import tannyjung.tanshugetrees_handcode.systems.LeafLitter;
import tannyjung.tanshugetrees_handcode.systems.TreeFunction;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

public class TreePlacer {

    public static void run (FeaturePlaceContext<NoneFeatureConfiguration> context) {

        WorldGenLevel world_gen = context.level();
        ServerLevel world = context.level().getLevel();
        ChunkGenerator chunk_generator = context.chunkGenerator();
        ChunkPos chunk_pos = new ChunkPos(context.origin().getX() >> 4, context.origin().getZ() >> 4);
        String dimension = GameUtils.getCurrentDimensionID(world).replace(":", "-");

        // Read To Get Tree(s)
        {

            File file = new File(Handcode.directory_world_data + "/place/" + dimension + "/" + (chunk_pos.x >> 5) + "," + (chunk_pos.z >> 5) + "/" + FileManager.quardtreeChunkToNode(chunk_pos.x, chunk_pos.z) + ".txt");

            if (file.exists() == true) {

                String[] array_text = null;
                String[] from_to_chunk_get = null;
                int[] from_to_chunk = new int[4];
                String id = "";
                String chosen = "";
                String[] center_pos = null;
                int centerX = 0;
                int centerZ = 0;
                String[] rotation_mirrored = null;
                int rotation = 0;
                boolean mirrored = false;
                String[] other_data = null;

                // Reading
                {

                    try { BufferedReader buffered_reader = new BufferedReader(new FileReader(file)); String read_all = ""; while ((read_all = buffered_reader.readLine()) != null) {

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
                                            centerX = Integer.parseInt(center_pos[0]);
                                            centerZ = Integer.parseInt(center_pos[1]);
                                            rotation_mirrored = array_text[4].split("/");
                                            rotation = Integer.parseInt(rotation_mirrored[0]);
                                            mirrored = Boolean.parseBoolean(rotation_mirrored[1]);

                                            other_data = array_text[5].split("(?<! )/(?! )");

                                        } catch (Exception ignored) {

                                            return;

                                        }

                                    }

                                    testing(world_gen, world, chunk_generator, dimension, chunk_pos, from_to_chunk, id, chosen, centerX, centerZ, rotation, mirrored, other_data);

                                }

                            }

                        }

                    } buffered_reader.close(); } catch (Exception e) { TanshugetreesMod.LOGGER.error(e.getMessage()); }

                }

            }

        }

    }

    private static void testing (WorldGenLevel world_gen, ServerLevel world, ChunkGenerator chunk_generator, String dimension, ChunkPos chunk_pos, int[] from_to_chunk, String id, String chosen, int centerX, int centerZ, int rotation, boolean mirrored, String[] other_data) {

        boolean already_tested = false;
        boolean pass = true;
        int centerY = 0;
        int dead_tree_level = 0;

        // Already Tested
        {

            File file = new File(Handcode.directory_world_data + "/detailed_detection/" + dimension + "/" + (chunk_pos.x >> 5) + "," + (chunk_pos.z >> 5) + "/" + FileManager.quardtreeChunkToNode(chunk_pos.x, chunk_pos.z) + ".txt");

            if (file.exists() == true && file.isDirectory() == false) {

                String[] get = new String[0];

                {

                    try { BufferedReader buffered_reader = new BufferedReader(new FileReader(file)); String read_all = ""; while ((read_all = buffered_reader.readLine()) != null) {

                        {

                            if (read_all.startsWith(id + "|" + centerX + "/" + centerZ + "|") == true) {

                                already_tested = true;

                                try {

                                    get = read_all.split("\\|");
                                    pass = Boolean.parseBoolean(get[2]);
                                    centerY = Integer.parseInt(get[3]);
                                    dead_tree_level = Integer.parseInt(get[4]);

                                } catch (Exception e) {

                                    return;

                                }

                                break;

                            }

                        }

                    } buffered_reader.close(); } catch (Exception e) { TanshugetreesMod.LOGGER.error(e.getMessage()); }

                }

            }

        }

        if (already_tested == false) {

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

            } catch (Exception e) {

                return;

            }

            ChunkAccess center_chunk = null;
            int center_chunkX = centerX >> 4;
            int center_chunkZ = centerZ >> 4;
            int originalY = chunk_generator.getBaseHeight(centerX, centerZ, Heightmap.Types.OCEAN_FLOOR_WG, world_gen, world.getChunkSource().randomState());

            // Center Chunk Loaded
            {

                if (world_gen.hasChunk(center_chunkX, center_chunkZ) == false) {

                    centerY = originalY;

                } else {

                    center_chunk = world_gen.getChunk(center_chunkX, center_chunkZ);

                    if (center_chunk.getStatus().isOrAfter(ChunkStatus.SURFACE) == true) {

                        centerY = center_chunk.getHeight(Heightmap.Types.OCEAN_FLOOR_WG, centerX, centerZ);

                    }

                }

            }

            centerY = centerY + start_height;

            test:
            {

                if (center_chunk != null) {

                    // Structure Area
                    {

                        if (center_chunk.getStatus().isOrAfter(ChunkStatus.STRUCTURE_REFERENCES) == true) {

                            Structure[] structures = center_chunk.getAllReferences().keySet().toArray(new Structure[0]);

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

                    // Ground Block
                    {

                        if (center_chunk.getStatus().isOrAfter(ChunkStatus.SURFACE) == true) {

                            if (testGroundBlock(center_chunk.getBlockState(new BlockPos(centerX, originalY - 1, centerZ)), ground_block) == false) {

                                pass = false;
                                break test;

                            }

                        }

                    }

                    // Surface Smoothness
                    {

                        int size = ConfigMain.surrounding_area_detection_size;
                        int height = ConfigMain.surface_smoothness_detection_height;
                        int pos1 = chunk_generator.getBaseHeight(centerX + size, centerZ + size, Heightmap.Types.OCEAN_FLOOR_WG, world_gen, world.getChunkSource().randomState());
                        int pos2 = chunk_generator.getBaseHeight(centerX + size, centerZ - size, Heightmap.Types.OCEAN_FLOOR_WG, world_gen, world.getChunkSource().randomState());
                        int pos3 = chunk_generator.getBaseHeight(centerX - size, centerZ + size, Heightmap.Types.OCEAN_FLOOR_WG, world_gen, world.getChunkSource().randomState());
                        int pos4 = chunk_generator.getBaseHeight(centerX - size, centerZ - size, Heightmap.Types.OCEAN_FLOOR_WG, world_gen, world.getChunkSource().randomState());

                        if ((Math.abs(originalY - pos1) > height) || (Math.abs(originalY - pos2) > height) || (Math.abs(originalY - pos3) > height) || (Math.abs(originalY - pos4) > height)) {

                            pass = false;
                            break test;

                        }

                    }

                }

                // World Height
                {

                    if (centerY + sizeY > world_gen.getMaxBuildHeight()) {

                        pass = false;
                        break test;

                    }

                }

                // Dead Tree
                {

                    if (Math.random() >= dead_tree_chance) {

                        if (tree_type.equals("adapt") == true) {

                            dead_tree_level = 0;

                        } else {

                            int highestY = chunk_generator.getBaseHeight(centerX, centerZ, Heightmap.Types.WORLD_SURFACE_WG, world_gen, world.getChunkSource().randomState());

                            if ((tree_type.equals("land") == true && (originalY == highestY)) || (tree_type.equals("water") == true && (originalY < highestY))) {

                                dead_tree_level = 0;

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
                            .append(centerX)
                            .append("/")
                            .append(centerZ)
                            .append("|")
                            .append(pass)
                            .append("|")
                            .append(centerY)
                            .append("|")
                            .append(dead_tree_level)
                    ;

                    write.append("\n");

                }

                int size = 32 >> Handcode.quadtree_level;
                int to_chunkX_test = ((int) Math.ceil(to_chunkX / (double) size) * size) + size;
                int to_chunkZ_test = ((int) Math.ceil(to_chunkZ / (double) size) * size) + size;

                for (int scanX = from_chunkX; scanX < to_chunkX_test; scanX = scanX + size) {

                    for (int scanZ = from_chunkZ; scanZ < to_chunkZ_test; scanZ = scanZ + size) {

                        FileManager.writeTXT(Handcode.directory_world_data + "/detailed_detection/" + dimension + "/" + (scanX >> 5) + "," + (scanZ >> 5) + "/" + FileManager.quardtreeChunkToNode(scanX, scanZ) + ".txt", write.toString(), true);

                    }

                }

            }

        }

        if (pass == true) {

            getData(world_gen, world, chunk_pos, id, chosen, centerX, centerY, centerZ, rotation, mirrored, dead_tree_level);

        }

    }

    private static boolean testGroundBlock (BlockState ground_block, String config_value) {

        boolean return_logic = false;

        {

            for (String split : config_value.split(" / ")) {

                return_logic = true;

                for (String split2 : split.split(", ")) {

                    String split_get = split2.replaceAll("[#!]", "");

                    {

                        if (split2.contains("#") == false) {

                            if (ForgeRegistries.BLOCKS.getKey(ground_block.getBlock()).toString().equals(split_get) == false) {

                                return_logic = false;

                            }

                        } else {

                            if (GameUtils.isBlockTaggedAs(ground_block, split_get) == false) {

                                return_logic = false;

                            }

                        }

                        if (split2.contains("!") == true) {

                            return_logic = !return_logic;

                        }

                    }

                    if (return_logic == false) {

                        break;

                    }

                }

                if (return_logic == true) {

                    break;

                }

            }

        }

        return return_logic;

    }

    private static void getData (WorldGenLevel world_gen, ServerLevel world, ChunkPos chunk_pos, String id, String chosen, int centerX, int centerY, int centerZ, int rotation, boolean mirrored, int dead_tree_level) {

        String storage_directory = "";
        String tree_settings = "";

        // Scan "World Gen" File
        {

            File file = new File(Handcode.directory_config + "/custom_packs/.organized/world_gen/" + id + ".txt");

            if (file.exists() == true) {

                {

                    try { BufferedReader buffered_reader = new BufferedReader(new FileReader(file)); String read_all = ""; while ((read_all = buffered_reader.readLine()) != null) {

                        {

                            if (read_all.startsWith("storage_directory = ") == true) {

                                storage_directory = read_all.replace("storage_directory = ", "");

                            } else if (read_all.startsWith("tree_settings = ") == true) {

                                tree_settings = read_all.replace("tree_settings = ", "");

                            } else {

                                break;

                            }

                        }

                    } buffered_reader.close(); } catch (Exception e) { TanshugetreesMod.LOGGER.error(e.getMessage()); }

                }

            }

        }

        Map<String, String> map_block = new HashMap<>();
        boolean living_tree_mechanics = false;
        boolean can_disable_roots = false;
        int leaves1_type = 0;
        int leaves2_type = 0;

        // Scan "Tree Settings" File
        {

            File file = new File(Handcode.directory_config + "/custom_packs/.organized/presets/" + tree_settings);

            if (file.exists() == true && file.isDirectory() == false) {

                String get_short = "";
                String get = "";
                int leaves_type_test = 0;

                // Scan
                {

                    try { BufferedReader buffered_reader = new BufferedReader(new FileReader(file)); String read_all = ""; while ((read_all = buffered_reader.readLine()) != null) {

                        {

                            if (read_all.startsWith("living_tree_mechanics = ") == true) {

                                {

                                    living_tree_mechanics = Boolean.parseBoolean(read_all.replace("living_tree_mechanics = ", ""));

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

                                    // Test Leaves Type
                                    {

                                        if (get_short.startsWith("le") == true) {

                                            if (get.endsWith(" keep") == true) {

                                                get = get.substring(0, get.length() - (" keep").length());

                                            }

                                            if (get.endsWith("]") == true) {

                                                get = get.substring(0, get.indexOf("["));

                                            }

                                            if (ConfigMain.deciduous_leaves_list.contains(get) == true) {

                                                leaves_type_test = 1;

                                            } else if (ConfigMain.coniferous_leaves_list.contains(get) == true) {

                                                leaves_type_test = 2;

                                            }

                                            if (get_short.endsWith("1") == true) {

                                                leaves1_type = leaves_type_test;

                                            } else if (get_short.endsWith("2") == true) {

                                                leaves2_type = leaves_type_test;

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

                    } buffered_reader.close(); } catch (Exception e) { TanshugetreesMod.LOGGER.error(e.getMessage()); }

                }

            }

        }

        File file = new File(Handcode.directory_config + "/custom_packs/" + storage_directory + "/" + chosen);

        if (file.exists() == true && file.isDirectory() == false) {

            int trunk_count = 0;
            boolean hollowed = false;

            // Dead Tree Level 4 - 7
            {

                if (dead_tree_level >= 4) {

                    boolean skip = false;

                    // Get Trunk Count
                    {

                        try {
                            BufferedReader buffered_reader = new BufferedReader(new FileReader(file));
                            String read_all = "";
                            while ((read_all = buffered_reader.readLine()) != null) {

                                {

                                    if (skip == false) {

                                        if (read_all.equals("--------------------------------------------------")) {

                                            skip = true;

                                        }

                                    } else {

                                        if (read_all.startsWith("+b")) {

                                            if (read_all.substring(read_all.length() - 3, read_all.length() - 1).equals("tr") == true) {

                                                trunk_count = trunk_count + 1;

                                            }

                                        }

                                    }

                                }

                            }
                            buffered_reader.close();
                        } catch (Exception e) {
                            TanshugetreesMod.LOGGER.error(e.getMessage());
                        }

                    }

                    if (dead_tree_level >= 6) {

                        trunk_count = (int) (Mth.nextDouble(RandomSource.create(centerX + centerY + centerZ), 0.1, 0.5) * trunk_count);

                    } else {

                        trunk_count = (int) (Mth.nextDouble(RandomSource.create(centerX + centerY + centerZ), 0.5, 1) * trunk_count);

                    }

                    if (dead_tree_level == 5 || dead_tree_level == 7) {

                        hollowed = true;

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

            boolean in_snowy_biome = GameUtils.isBiomeTaggedAs(world_gen.getBiome(new BlockPos(centerX, centerY, centerZ)), "forge:is_snowy");
            boolean can_run_function = false;
            BlockState block = Blocks.AIR.defaultBlockState();
            BlockPos pos = null;
            double pre_leaf_litter_chance = 0.0;
            int height_motion_block = 0;
            boolean summon_marker = false;
            String marker_data = "";

            // Read File
            {

                try { BufferedReader buffered_reader = new BufferedReader(new FileReader(file)); String read_all = ""; while ((read_all = buffered_reader.readLine()) != null) {

                    {

                        if (read_all.startsWith("+") == true) {

                            // Test "Block" or "Function" and get value from map
                            {

                                if (read_all.startsWith("+b") == true) {

                                    get_short = read_all.substring(read_all.length() - 3);
                                    can_run_function = false;

                                } else if (read_all.startsWith("+f") == true) {

                                    get_short = read_all.substring(read_all.length() - 2);

                                }

                                get = map_block.get(get_short);

                                // This fix the error >>> Cannot invoke "String.equals(Object)" because "get" is null
                                try {

                                    if (get.equals("") == true) {

                                        continue;

                                    }

                                } catch (Exception e) {

                                    continue;

                                }

                            }

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

                                if (dead_tree_level != 0) {

                                    if (dead_tree_level >= 1) {

                                        if (get_short.startsWith("le") == true) {

                                            continue;

                                        }

                                    }

                                    if (dead_tree_level >= 2) {

                                        if (get_short.startsWith("lt") == true) {

                                            continue;

                                        }

                                    }

                                    if (dead_tree_level >= 3) {

                                        if (get_short.startsWith("tw") == true) {

                                            continue;

                                        }

                                    }

                                    if (dead_tree_level >= 4) {

                                        if (get_short.startsWith("br") == true) {

                                            continue;

                                        } else if (get_short.startsWith("tr") == true) {

                                            if (trunk_count > 0) {

                                                trunk_count = trunk_count - 1;

                                            } else {

                                                break;

                                            }

                                            if (hollowed == true) {

                                                if (get_short.startsWith("trc") == true) {

                                                    continue;

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

                                get_pos_array = FileManager.textPosConverter(get_pos, rotation, mirrored);

                                // Apply Block Pos With Real Pos
                                testX = centerX + get_pos_array[0];
                                testY = centerY + get_pos_array[1];
                                testZ = centerZ + get_pos_array[2];

                            }

                            // Test is in current chunk
                            if (chunk_pos.equals(new ChunkPos(testX >> 4, testZ >> 4)) == true) {

                                pos = new BlockPos(testX, testY, testZ);

                                // Keep
                                {

                                    if (get.endsWith(" keep") == true) {

                                        get = get.replace(" keep", "");

                                        if (GameUtils.isBlockTaggedAs(world_gen.getBlockState(pos), "tanshugetrees:passable_blocks") == false || world_gen.isWaterAt(pos) == true) {

                                            continue;

                                        }

                                    }

                                }

                                if (read_all.startsWith("+b")) {

                                    // Place Block
                                    {

                                        block = GameUtils.textToBlock(get);

                                        if (block != Blocks.AIR.defaultBlockState()) {

                                            // Leaves
                                            {

                                                if (get_short.startsWith("le") == true && GameUtils.isBlockTaggedAs(block, "minecraft:leaves") == true) {

                                                    // Pre Leaves Drop
                                                    {

                                                        if (ConfigMain.leaf_litter == true && ConfigMain.pre_leaf_litter == true) {

                                                            // Get "Chance" Value
                                                            {

                                                                if ((get_short.endsWith("1") == true && leaves1_type == 2) || (get_short.endsWith("2") == true && leaves2_type == 2)) {

                                                                    pre_leaf_litter_chance = ConfigMain.pre_leaf_litter_chance_coniferous;

                                                                } else {

                                                                    pre_leaf_litter_chance = ConfigMain.pre_leaf_litter_chance;

                                                                }

                                                            }

                                                            if (Math.random() < pre_leaf_litter_chance) {

                                                                height_motion_block = world_gen.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, testX, testZ);

                                                                if (height_motion_block < testY) {

                                                                    LeafLitter.run(world_gen, testX, height_motion_block, testZ, block, false);

                                                                }

                                                            }

                                                        }

                                                    }

                                                    // Abscission (World Gen)
                                                    {

                                                        if (ConfigMain.abscission_world_gen == true) {

                                                            if ((get_short.endsWith("1") == true && leaves1_type == 1) || (get_short.endsWith("2") == true && leaves2_type == 1)) {

                                                                if (in_snowy_biome == true) {

                                                                    continue;

                                                                }

                                                            }

                                                        }

                                                    }

                                                }

                                            }

                                            // Automatic Waterlogged
                                            {

                                                if (world_gen.isWaterAt(pos) == true) {

                                                    block = GameUtils.blockPropertyBooleanSet(block, "waterlogged", true);

                                                }

                                            }

                                            world_gen.setBlock(pos, block, 2);
                                            can_run_function = true;

                                        }

                                    }

                                    // Summon Marker
                                    {

                                        if (summon_marker == false) {

                                            summon_marker = true;

                                            if (dead_tree_level == 0) {

                                                if (ConfigMain.tree_location == true && ConfigMain.living_tree_mechanics == true && living_tree_mechanics == true) {

                                                    if (read_all.equals("+b0^0^0tro") == true) {

                                                        marker_data = "ForgeData:{file:\"" + storage_directory + "/" + chosen + "\",settings:\"" + tree_settings + "\",rotation:" + rotation + ",mirrored:" + mirrored + "}";
                                                        GameUtils.runCommand(world, centerX + 0.5, centerY + 0.5, centerZ + 0.5, GameUtils.summonEntity("marker", "tree_location", id, "white", marker_data));

                                                    }

                                                }

                                            }

                                        }

                                    }

                                } else if (read_all.startsWith("+f")) {

                                    // Run Function
                                    {

                                        // Separate like this because start and end function doesn't need to test "can_run_function"
                                        if (can_run_function == true || get_short.equals("fs") == true || get_short.equals("fe") == true) {

                                            TreeFunction.run(world_gen, get, testX, testY, testZ);

                                        }

                                    }

                                }

                            }

                        }

                    }

                } buffered_reader.close(); } catch (Exception e) { TanshugetreesMod.LOGGER.error(e.getMessage()); }

            }

        }

    }

}