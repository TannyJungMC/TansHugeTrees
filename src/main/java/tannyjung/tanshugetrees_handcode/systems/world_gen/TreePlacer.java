package tannyjung.tanshugetrees_handcode.systems.world_gen;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
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
        int chunk_posX = context.origin().getX() >> 4;
        int chunk_posZ = context.origin().getZ() >> 4;
        String dimension = GameUtils.getCurrentDimensionID(world_gen.getLevel()).replace(":", "-");

        // Read To Get Tree(s)
        {

            File file = new File(Handcode.directory_world_data + "/place/" + dimension + "/" + (chunk_posX >> 5) + "," + (chunk_posZ >> 5) + "/" + FileManager.quardtreeChunkToNode(chunk_posX, chunk_posZ) + ".txt");

            if (file.exists() == true) {

                String[] array_text = null;

                String[] from_chunk = null;
                String[] to_chunk = null;
                int from_chunk_posX = 0;
                int from_chunk_posZ = 0;
                int to_chunk_posX = 0;
                int to_chunk_posZ = 0;

                String id = "";
                String chosen = "";

                String[] center_pos = null;
                int center_posX = 0;
                int center_posY = 0;
                int center_posZ = 0;

                String[] rotation_mirrored = null;
                int rotation = 0;
                boolean mirrored = false;

                String[] other_data = null;
                int original_height = 0;
                String ground_block = "";
                int dead_tree_level = 0;

                // Reading
                {

                    try { BufferedReader buffered_reader = new BufferedReader(new FileReader(file)); String read_all = ""; while ((read_all = buffered_reader.readLine()) != null) {

                        {

                            if (read_all.equals("") == false) {

                                array_text = read_all.split("\\|");

                                // Get Tree Chunk Pos
                                {

                                    try {

                                        from_to_chunk = array_text[0].split("/");
                                        from_chunk_posX = Integer.parseInt(from_to_chunk[0]);
                                        from_chunk_posZ = Integer.parseInt(from_to_chunk[1]);
                                        to_chunk_posX = Integer.parseInt(from_to_chunk[2]);
                                        to_chunk_posZ = Integer.parseInt(from_to_chunk[3]);

                                    } catch (Exception ignored) {

                                    }

                                }

                                if ((from_chunk_posX <= chunk_posX && chunk_posX <= to_chunk_posX) && (from_chunk_posZ <= chunk_posZ && chunk_posZ <= to_chunk_posZ)) {

                                    // Get Tree Data
                                    {

                                        try {

                                            id = array_text[1];
                                            chosen = array_text[2];

                                            center_pos = array_text[3].split("/");
                                            center_posX = Integer.parseInt(center_pos[0]);
                                            center_posY = Integer.parseInt(center_pos[1]);
                                            center_posZ = Integer.parseInt(center_pos[2]);

                                            rotation_mirrored = array_text[4].split("/");
                                            rotation = Integer.parseInt(rotation_mirrored[0]);
                                            mirrored = Boolean.parseBoolean(rotation_mirrored[1]);

                                            other_data = array_text[5].split("(?<! )/(?! )");
                                            original_height = Integer.parseInt(other_data[0]);
                                            ground_block = other_data[1];
                                            dead_tree_level = Integer.parseInt(other_data[2]);

                                        } catch (Exception ignored) {

                                        }

                                    }

                                    // Detailed Detection
                                    {

                                        if (detailed_detection(world_gen, dimension, center_posX, center_posZ, original_height, ground_block) == false) {

                                            continue;

                                        }

                                    }

                                    getData(world_gen, chunk_posX, chunk_posZ, id, chosen, center_posX, center_posY, center_posZ, rotation, mirrored, dead_tree_level);

                                }

                            }

                        }

                    } buffered_reader.close(); } catch (Exception e) { TanshugetreesMod.LOGGER.error(e.getMessage()); }

                }

            }

        }

    }

    private static boolean detailed_detection (WorldGenLevel world_gen, String dimension, int center_posX, int center_posZ, int original_height, String ground_block) {

        boolean return_logic = true;
        boolean already_tested = false;

        File file = new File(Handcode.directory_world_data + "/detailed_detection/" + dimension + "/" + (center_posX >> 9) + "," + (center_posZ >> 9) + "/" + FileManager.quardtreeChunkToNode((center_posX >> 4), (center_posZ >> 4)) + ".txt");

        // Create Empty File
        {

            if (file.exists() == false) {

                FileManager.writeTXT(file.toPath().toString(), "", true);

            }

        }

        // Read to test is it already tested
        {

            try { BufferedReader buffered_reader = new BufferedReader(new FileReader(file)); String read_all = ""; while ((read_all = buffered_reader.readLine()) != null) {

                if (read_all.startsWith(center_posX + "/" + center_posZ) == true) {

                    already_tested = true;

                    if (read_all.endsWith("true") == false) {

                        return_logic = false;

                    }

                    break;

                }

            } buffered_reader.close(); } catch (Exception e) { TanshugetreesMod.LOGGER.error(e.getMessage()); }

        }

        if (already_tested == false) {

            int center_chunk_posX = center_posX >> 4;
            int center_chunk_posZ = center_posZ >> 4;

            if (world_gen.hasChunk(center_chunk_posX, center_chunk_posZ) == true) {

                ChunkAccess center_chunk = world_gen.getChunk(center_chunk_posX, center_chunk_posZ);

                test:
                {

                    // Test Structure Area
                    {

                        if (center_chunk.getStatus().isOrAfter(ChunkStatus.STRUCTURE_REFERENCES) == true) {

                            Structure[] structures = center_chunk.getAllReferences().keySet().toArray(new Structure[0]);

                            for (Structure structure : structures) {

                                // structure.type().equals(StructureType.MINESHAFT) == false
                                // structure.terrainAdaptation().equals(TerrainAdjustment.NONE) == true

                                if (structure.step().equals(GenerationStep.Decoration.SURFACE_STRUCTURES) == true) {

                                    return_logic = false;
                                    break test;

                                }

                            }

                        }

                    }

                    // Test Ground Block
                    {

                        if (center_chunk.getStatus().isOrAfter(ChunkStatus.SURFACE) == true) {

                            if (testGroundBlock(world_gen.getBlockState(new BlockPos(center_posX, original_height - 1, center_posZ)), ground_block) == false) {

                                return_logic = false;

                            }

                            break test;

                        }

                    }

                }

            }

            // Write the file to mark as "already tested"
            {

                StringBuilder write = new StringBuilder();

                {

                    write
                            .append(center_posX)
                            .append("/")
                            .append(center_posZ)
                            .append("|")
                            .append(return_logic)
                    ;

                    write.append("\n");

                }

                FileManager.writeTXT(file.toPath().toString(), write.toString(), true);

            }

        }

        return return_logic;

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

    private static void getData (WorldGenLevel world_gen, int chunk_posX, int chunk_posZ, String id, String chosen, int center_posX, int center_posY, int center_posZ, int rotation, boolean mirrored, int dead_tree_level) {

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

                        trunk_count = (int) (Mth.nextDouble(RandomSource.create(center_posX + center_posY + center_posZ), 0.1, 0.5) * trunk_count);

                    } else {

                        trunk_count = (int) (Mth.nextDouble(RandomSource.create(center_posX + center_posY + center_posZ), 0.5, 1) * trunk_count);

                    }

                    if (dead_tree_level == 5 || dead_tree_level == 7) {

                        hollowed = true;

                    }

                }

            }

            ChunkPos chunk_pos = new ChunkPos(chunk_posX, chunk_posZ);

            String get_short = "";
            String get = "";
            String get_pos = "";
            int[] get_pos_array = new int[0];
            int test_posX = 0;
            int test_posY = 0;
            int test_posZ = 0;

            boolean in_snowy_biome = GameUtils.isBiomeTaggedAs(world_gen.getBiome(new BlockPos(center_posX, center_posY, center_posZ)), "forge:is_snowy");
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

                            get_short = "";
                            get = "";

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
                                test_posX = center_posX + get_pos_array[0];
                                test_posY = center_posY + get_pos_array[1];
                                test_posZ = center_posZ + get_pos_array[2];

                            }

                            // Test is in Current Chunk
                            if (chunk_pos.equals(new ChunkPos(test_posX >> 4, test_posZ >> 4)) == true) {

                                pos = new BlockPos(test_posX, test_posY, test_posZ);

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

                                                if (living_tree_mechanics == true) {

                                                    if (get_short.startsWith("le") == true && GameUtils.isBlockTaggedAs(block, "minecraft:leaves") == true) {

                                                        // Pre Leaves Drop
                                                        {

                                                            if (ConfigMain.pre_leaves_litter == true) {

                                                                // Get "Chance" Value
                                                                {

                                                                     if ((get_short.endsWith("1") == true && leaves1_type == 2) || (get_short.endsWith("2") == true && leaves2_type == 2)) {

                                                                         pre_leaf_litter_chance = ConfigMain.pre_leaves_litter_chance_coniferous;

                                                                    } else {

                                                                        pre_leaf_litter_chance = ConfigMain.pre_leaves_litter_chance;

                                                                    }

                                                                }

                                                                if (Math.random() < pre_leaf_litter_chance) {

                                                                    height_motion_block = world_gen.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, test_posX, test_posZ);

                                                                    if (height_motion_block < test_posY) {

                                                                        LeafLitter.run(world_gen, test_posX, height_motion_block, test_posZ, block, false);

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

                                            if (ConfigMain.tree_location == true) {

                                                if (ConfigMain.living_tree_mechanics == true && living_tree_mechanics == true) {

                                                    if (read_all.equals("+b0^0^0tro") == true) {

                                                        if (dead_tree_level == 0) {

                                                            marker_data = "ForgeData:{file:\"" + storage_directory + "/" + chosen + "\",settings:\"" + tree_settings + "\",rotation:" + rotation + ",mirrored:" + mirrored + "}";
                                                            GameUtils.runCommand(world_gen.getLevel(), center_posX + 0.5, center_posY + 0.5, center_posZ + 0.5, GameUtils.summonEntity("marker", "tree_location", id, "white", marker_data));

                                                        }

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

                                            TreeFunction.run(world_gen, get, test_posX, test_posY, test_posZ);

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