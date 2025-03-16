package tannyjung.tanshugetrees_handcode.world_gen;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraftforge.registries.ForgeRegistries;
import tannyjung.tanshugetrees_handcode.Handcode;
import tannyjung.tanshugetrees_handcode.config.ConfigMain;
import tannyjung.tanshugetrees_handcode.misc.FileManager;
import tannyjung.tanshugetrees_handcode.misc.Misc;
import tannyjung.tanshugetrees_handcode.misc.TreeFunction;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

public class TreePlacer {

    public static void start (FeaturePlaceContext<NoneFeatureConfiguration> context) {

        LevelAccessor level = context.level();
        int chunk_posX = context.origin().getX() >> 4;
        int chunk_posZ = context.origin().getZ() >> 4;

        // Read To Get Tree(s)
        {

            File file = new File(Handcode.directory_world_data + "/place/" + (chunk_posX >> 5) + "," + (chunk_posZ >> 5) + ".txt");

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

                try { BufferedReader buffered_reader = new BufferedReader(new FileReader(file)); String read_all = ""; while ((read_all = buffered_reader.readLine()) != null) {

                    {

                        if (read_all.equals("") == false) {

                            // Get Tree Chunk Pos
                            {

                                array_text = read_all.split("\\|");

                                from_chunk = array_text[0].split("/");
                                to_chunk = array_text[1].split("/");
                                from_chunk_posX = Integer.parseInt(from_chunk[0]);
                                from_chunk_posZ = Integer.parseInt(from_chunk[1]);
                                to_chunk_posX = Integer.parseInt(to_chunk[0]);
                                to_chunk_posZ = Integer.parseInt(to_chunk[1]);

                            }

                            if ((from_chunk_posX <= chunk_posX && chunk_posX <= to_chunk_posX) && (from_chunk_posZ <= chunk_posZ && chunk_posZ <= to_chunk_posZ)) {

                                // Get Tree Data
                                {

                                    id = array_text[2];
                                    chosen = array_text[3];

                                    center_pos = array_text[4].split("/");
                                    center_posX = Integer.parseInt(center_pos[0]);
                                    center_posY = Integer.parseInt(center_pos[1]);
                                    center_posZ = Integer.parseInt(center_pos[2]);

                                    rotation_mirrored = array_text[5].split("/");
                                    rotation = Integer.parseInt(rotation_mirrored[0]);
                                    mirrored = Boolean.parseBoolean(rotation_mirrored[1]);

                                    other_data = array_text[6].split("(?<! )/(?! )");
                                    original_height = Integer.parseInt(other_data[0]);
                                    ground_block = other_data[1];
                                    dead_tree_level = Integer.parseInt(other_data[2]);

                                }

                                // Detailed Detection
                                {

                                    if (detailed_detection(level, center_posX, original_height, center_posZ, ground_block) == false) {

                                        continue;

                                    }

                                }

                                place(context, id, chosen, center_posX, center_posY, center_posZ, rotation, mirrored, dead_tree_level);

                            }

                        }

                    }

                } buffered_reader.close(); } catch (Exception e) { e.printStackTrace(); }

            }

        }

    }

    private static boolean detailed_detection (LevelAccessor level, int center_posX, int original_height, int center_posZ, String ground_block) {

        boolean return_logic = true;
        boolean already_tested = false;

        File file = new File(Handcode.directory_world_data + "/detailed_detection/" + (center_posX >> 9) + "," + (center_posZ >> 9) + ".txt");

        // Create Empty File
        {

            if (file.exists() == false) {

                StringBuilder write = new StringBuilder();

                {

                    write.append("");

                }

                FileManager.writeTXT(file.toPath().toString(), write.toString(), true);

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

            } buffered_reader.close(); } catch (Exception e) { e.printStackTrace(); }

        }

        if (already_tested == false) {

            int center_chunk_posX = center_posX >> 4;
            int center_chunk_posZ = center_posZ >> 4;

            if (level.hasChunk(center_chunk_posX, center_chunk_posZ) == true) {

                ChunkAccess center_chunk = level.getChunk(center_chunk_posX, center_chunk_posZ);

                test:
                while (true) {

                    // Test Structure Area
                    {

                        if (center_chunk.getStatus().isOrAfter(ChunkStatus.STRUCTURE_REFERENCES) == true) {

                            Structure[] structures = center_chunk.getAllReferences().keySet().toArray(new Structure[0]);

                            for (Structure structure : structures) {

                                if (structure.type().equals(StructureType.MINESHAFT) == false) {

                                    return_logic = false;
                                    break test;

                                }

                            }

                        }

                    }

                    // Test Ground Block
                    {

                        if (center_chunk.getStatus().isOrAfter(ChunkStatus.SURFACE) == true) {

                            boolean test = false;
                            BlockState ground_block_get = level.getBlockState(new BlockPos(center_posX, original_height - 1, center_posZ));

                            for (String split : ground_block.split(" / ")) {

                                test = true;

                                for (String split2 : split.split(" & ")) {

                                    String split_get = split2.replaceAll("[#!]", "");

                                    {

                                        if (split2.contains("#") == false) {

                                            if (ForgeRegistries.BLOCKS.getKey(ground_block_get.getBlock()).toString().equals(split_get) == false) {

                                                test = false;

                                            }

                                        } else {

                                            if (Misc.isBlockTaggedAs(ground_block_get, split_get) == false) {

                                                test = false;

                                            }

                                        }

                                        if (split2.contains("!") == true) {

                                            test = !test;

                                        }

                                    }

                                    if (test == false) {

                                        break;

                                    }

                                }

                                if (test == true) {

                                    break;

                                }

                            }

                            if (test == false) {

                                return_logic = false;
                                break test;

                            }

                        }

                    }

                    break;

                }

            }

            // Write
            {

                StringBuilder write = new StringBuilder();

                {

                    write.append(center_posX + "/" + center_posZ + "|" + return_logic);
                    write.append("\n");

                }

                FileManager.writeTXT(file.toPath().toString(), write.toString(), true);

            }

        }

        return return_logic;

    }

    private static void place (FeaturePlaceContext <NoneFeatureConfiguration> context, String id, String chosen, int center_posX, int center_posY, int center_posZ, int rotation, boolean mirrored, int dead_tree_level) {

        String storage_directory = "";
        String tree_settings = "";

        Map<String, String> map_block = new HashMap<>();
        boolean tree_dynamic = false;

        // Scan "World Gen" File
        {

            File file = new File(Handcode.directory_game + "/config/tanshugetrees/custom_packs/.organized/" + id.replace("#", "world_gen") + ".txt");

            if (file.exists() == true) {

                try {

                    BufferedReader buffered_reader = new BufferedReader(new FileReader(file));
                    String read_all = "";

                    while ((read_all = buffered_reader.readLine()) != null) {

                        if (read_all.startsWith("storage_directory = ") == true) {

                            storage_directory = read_all.replace("storage_directory = ", "");

                        } else if (read_all.startsWith("tree_settings = ") == true) {

                            tree_settings = read_all.replace("tree_settings = ", "");

                        } else {

                            break;

                        }

                    }

                    buffered_reader.close();

                } catch (Exception e) {

                    e.printStackTrace();

                }

            }

        }

        // Scan "Tree Settings" File
        {

            File file = new File(Handcode.directory_game + "/config/tanshugetrees/custom_packs/" + tree_settings);

            if (file.exists() == true && file.isDirectory() == false) {

                String get_short = "";
                String get = "";

                try { BufferedReader buffered_reader = new BufferedReader(new FileReader(file)); String read_all = ""; while ((read_all = buffered_reader.readLine()) != null) {

                    {

                        if (read_all.startsWith("rt_dynamic = ") == true) {

                            {

                                if (read_all.replace("rt_dynamic = ", "").equals("true")) {

                                    tree_dynamic = true;

                                }

                            }

                        } else if (read_all.startsWith("Block ") == true) {

                            {

                                get_short = read_all.substring("Block ".length(), "Block ###".length());
                                get = read_all.substring("Block ### = ".length());
                                map_block.put(get_short, get);

                            }

                        } else if (read_all.startsWith("Function ") == true) {

                            {

                                get_short = read_all.substring("Function ".length(), "Function ##".length());
                                get = read_all.substring("Function ## = ".length());
                                map_block.put(get_short, get);

                            }

                        }

                    }

                } buffered_reader.close(); } catch (Exception e) { e.printStackTrace(); }

            }

        }

        // Placing
        {

            File file = new File(Handcode.directory_game + "/config/tanshugetrees/custom_packs/" + storage_directory + "/" + chosen);

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
                                e.printStackTrace();
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

                LevelAccessor level = context.level();
                ServerLevel world = context.level().getLevel();
                WorldGenLevel world_gen_level = context.level();
                ChunkPos chunk_pos = new ChunkPos(context.origin().getX() >> 4, context.origin().getZ() >> 4);
                ChunkAccess chunk = context.level().getChunk(chunk_pos.x, chunk_pos.z);

                String get_short = "";
                String get = "";

                String test = "";
                int split = 0;
                int split2 = 0;
                int posX = 0;
                int posY = 0;
                int posZ = 0;
                int test_posX = 0;
                int test_posY = 0;
                int test_posZ = 0;

                boolean keep = false;
                BlockState block = Blocks.AIR.defaultBlockState();
                BlockPos pos = null;
                double pre_leaves_litter_chance = 0.0;
                boolean can_run_function = false;

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

                                if (get.equals("") == true) {

                                    continue;

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

                                // Convert To Pos
                                {

                                    if (read_all.startsWith("+b") == true) {

                                        test = read_all.substring(2, read_all.length() - 3);

                                    } else if (read_all.startsWith("+f") == true) {

                                        test = read_all.substring(2, read_all.length() - 2);

                                    }

                                    split = test.indexOf("^");
                                    split2 = test.indexOf("^", split + 1);
                                    posX = Integer.parseInt(test.substring(0, split));
                                    posY = Integer.parseInt(test.substring(split + 1, split2));
                                    posZ = Integer.parseInt(test.substring(split2 + 1));

                                }

                                // Rotation & Mirrored
                                {

                                    if (mirrored == true) {

                                        posX = posX * (-1);

                                    }

                                    if (rotation == 2) {

                                        int posX_save = posX;
                                        posX = posZ;
                                        posZ = posX_save * (-1);

                                    } else if (rotation == 3) {

                                        posX = posX * (-1);
                                        posZ = posZ * (-1);

                                    } else if (rotation == 4) {

                                        int posX_save = posX;
                                        int posZ_save = posZ;
                                        posX = posZ_save * (-1);
                                        posZ = posX_save;

                                    }

                                }

                                // Apply Block Pos With Real Pos
                                test_posX = center_posX + posX;
                                test_posY = center_posY + posY;
                                test_posZ = center_posZ + posZ;

                            }

                            // Test is in Current Chunk
                            if (chunk_pos.equals(new ChunkPos(test_posX >> 4, test_posZ >> 4)) == true) {

                                pos = new BlockPos(test_posX, test_posY, test_posZ);

                                // Keep
                                {

                                    if (get.endsWith(" keep") == true) {

                                        get = get.replace(" keep", "");

                                        if (Misc.isBlockTaggedAs(chunk.getBlockState(pos), "tanshugetrees:passable_blocks") == false || Misc.isBlockTaggedAs(chunk.getBlockState(pos), "tanshugetrees:fluid_blocks") == true) {

                                            continue;

                                        }

                                    }

                                }

                                if (read_all.startsWith("+b")) {

                                    // Place Block
                                    {

                                        block = Misc.textToBlock(get);

                                        if (block != Blocks.AIR.defaultBlockState()) {

                                            chunk.setBlockState(pos, block, false);
                                            can_run_function = true;

                                            // Pre Leaves Drop
                                            {

                                                if (tree_dynamic == true && get_short.startsWith("le") == true) {

                                                    if (Misc.isBlockTaggedAs(block, "tanshugetrees:deciduous_leaves_blocks") == true) {

                                                        pre_leaves_litter_chance = ConfigMain.pre_leaves_litter_chance;

                                                    } else if (Misc.isBlockTaggedAs(block, "tanshugetrees:coniferous_leaves_blocks") == true) {

                                                        pre_leaves_litter_chance = ConfigMain.pre_leaves_litter_coniferous_chance;

                                                    }

                                                    if (Math.random() < pre_leaves_litter_chance) {

                                                        pre_leaves_drop(chunk, pos, block);

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

                                            TreeFunction.run(level, world, world_gen_level, get, test_posX, test_posY, test_posZ);

                                        }

                                    }

                                }

                            }

                        }

                    }

                } buffered_reader.close(); } catch (Exception e) { e.printStackTrace(); }

            }

        }

    }

    private static void pre_leaves_drop (ChunkAccess chunk, BlockPos pos, BlockState block) {

        int height = chunk.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, pos.getX(), pos.getZ()) + 1;

        if (height < pos.getY()) {

            // Not The Same Block
            if (chunk.getBlockState(new BlockPos(pos.getX(), height, pos.getZ())).getBlock() != block.getBlock()) {

                // If Found Water
                if (Misc.isBlockTaggedAs(chunk.getBlockState(new BlockPos(pos.getX(), height - 1, pos.getZ())), "tanshugetrees:fluid_blocks") == true) {

                    block = (block.getBlock().getStateDefinition().getProperty("waterlogged") instanceof BooleanProperty property ? block.setValue(property, true) : block);
                    height = height - 1;

                }

                chunk.setBlockState(new BlockPos(pos.getX(), height, pos.getZ()), block, false);

            }

        }

    }

}