package tannyjung.tanshugetrees_handcode.world_gen;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.*;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import tannyjung.tanshugetrees.TanshugetreesMod;
import tannyjung.tanshugetrees.network.TanshugetreesModVariables;
import tannyjung.tanshugetrees_handcode.Handcode;
import tannyjung.tanshugetrees_handcode.config.ConfigMain;
import tannyjung.tanshugetrees_handcode.misc.FileManager;
import tannyjung.tanshugetrees_handcode.misc.Misc;

import java.io.*;
import java.util.concurrent.*;

public class TreeLocation {

    public static int generating_region = 0;
    public static int generating_region_bar = 0;

    public static void start (FeaturePlaceContext <NoneFeatureConfiguration> context) {

        int region_posX = context.origin().getX() >> 9;
        int region_posZ = context.origin().getZ() >> 9;
        File file = new File(Handcode.directory_world_data + "/tree_locations/" + region_posX + "," + region_posZ + ".txt");

        if (file.exists() == false) {

            LevelAccessor level = context.level();
            ServerLevel world = context.level().getLevel();
            ChunkGenerator chunk_generator = context.chunkGenerator();
            int chunk_posX = 0;
            int chunk_posZ = 0;
            int center_posX = 0;
            int center_posZ = 0;
            int center_posY = 0;
            String tree_data = "";

            generating_region = 4;
            generating_region_bar = 0;
            TanshugetreesMod.LOGGER.info("Generating Region (" + region_posX + "/" + region_posZ + ")");

            // Overlay Loading Loop
            {

                CompletableFuture.runAsync(() -> {

                    while (generating_region != 0) {

                        if (generating_region < 4) {

                            generating_region = generating_region + 1;

                        } else {

                            generating_region = 1;

                        }

                        try {

                            Thread.sleep(1000);

                        } catch (InterruptedException e) {

                            e.printStackTrace();

                        }

                    }

                });

            }

            // Create Empty File
            {

                StringBuilder write = new StringBuilder();

                {

                    write.append("");

                }

                FileManager.writeTXT(file.toPath().toString(), write.toString(), true);

            }

            // Region Scanning
            {

                for (int scanX = 0; scanX < 32; scanX++) {

                    for (int scanZ = 0; scanZ < 32; scanZ++) {

                        generating_region_bar = generating_region_bar + 1;

                        // Left here before I finish config reader
                        if (Math.random() < ConfigMain.global_rarity * 0.01) {

                            chunk_posX = (region_posX * 32) + scanX;
                            chunk_posZ = (region_posZ * 32) + scanZ;

                            if (level.hasChunk(chunk_posX, chunk_posZ) == false || (level.hasChunk(chunk_posX, chunk_posZ) == true && level.getChunk(chunk_posX, chunk_posZ).getStatus().isOrAfter(ChunkStatus.FULL)) == false) {

                                // Read "config_placement.txt"
                                {

                                    File config_placement = new File(Handcode.directory_config + "/config_placement.txt");

                                    if (config_placement.exists() == true) {

                                        center_posX = (chunk_posX * 16) + (int) (Math.random() * 16);
                                        center_posZ = (chunk_posZ * 16) + (int) (Math.random() * 16);
                                        center_posY = chunk_generator.getBaseHeight(center_posX, center_posZ, Heightmap.Types.OCEAN_FLOOR_WG, level, world.getChunkSource().randomState());

                                        tree_data = getTreeData(level, center_posX, center_posY, center_posZ);

                                        if (tree_data.equals("") == false) {

                                            // Surface Smoothness Detector
                                            {

                                                if (ConfigMain.surface_smoothness_detection_size != 0) {

                                                    int surface_size = ConfigMain.surface_smoothness_detection_size;
                                                    int surface_height = ConfigMain.surface_smoothness_detection_height;

                                                    if (
                                                        (Math.abs(center_posY - chunk_generator.getBaseHeight(center_posX + surface_size, center_posZ + surface_size, Heightmap.Types.OCEAN_FLOOR_WG, level, world.getChunkSource().randomState())) > surface_height)
                                                        ||
                                                        (Math.abs(center_posY - chunk_generator.getBaseHeight(center_posX + surface_size, center_posZ - surface_size, Heightmap.Types.OCEAN_FLOOR_WG, level, world.getChunkSource().randomState())) > surface_height)
                                                        ||
                                                        (Math.abs(center_posY - chunk_generator.getBaseHeight(center_posX - surface_size, center_posZ + surface_size, Heightmap.Types.OCEAN_FLOOR_WG, level, world.getChunkSource().randomState())) > surface_height)
                                                        ||
                                                        (Math.abs(center_posY - chunk_generator.getBaseHeight(center_posX - surface_size, center_posZ - surface_size, Heightmap.Types.OCEAN_FLOOR_WG, level, world.getChunkSource().randomState())) > surface_height)
                                                    ) {

                                                        continue;

                                                    }

                                                }

                                            }

                                            readTreeFile(level, world, chunk_generator, tree_data, center_posX, center_posY, center_posZ);

                                        }

                                    }

                                }

                            }

                        }

                    }

                }

            }

            generating_region = 0;
            TanshugetreesMod.LOGGER.info("Completed!");

        }

    }

    private static String getTreeData (LevelAccessor level, int center_posX, int center_posY, int center_posZ) {

        String return_text = "";
        File file = new File(Handcode.directory_config + "/config_placement.txt");

        if (file.exists() == true) {

            boolean skip = true;

            String id = "";
            int rotation = 0;
            boolean mirrored = false;

            String ground_block = "";
            int start_height_offset = 0;
            boolean dead_tree = false;
            int dead_tree_level = 0;

            // Read File
            {

                try { BufferedReader buffered_reader = new BufferedReader(new FileReader(file)); String read_all = ""; while ((read_all = buffered_reader.readLine()) != null) {

                    {

                        if (read_all.equals("") == false && read_all.startsWith("// ") == false) {

                            if (read_all.startsWith("[INCOMPATIBLE] ") == false) {

                                if (read_all.startsWith("[") == true) {

                                    // Reset The Test
                                    {

                                        id = read_all.substring(read_all.indexOf("]") + 2);
                                        int test = id.indexOf(" > ");
                                        id = id.substring(0, test) + "/#/" + id.substring(test + 3).replace(" > ", "/");

                                        skip = false;

                                    }

                                } else {

                                    if (skip == false) {

                                        if (read_all.startsWith("world_gen = ") == true) {

                                            {

                                                if (read_all.replace("world_gen = ", "").equals("true") == false) {

                                                    skip = true;

                                                }

                                            }

                                        } else if (read_all.startsWith("biome = ") == true) {

                                            {

                                                boolean test = false;
                                                Holder<Biome> biome_get = level.getBiome(new BlockPos(center_posX, center_posY - 1, center_posZ));

                                                for (String split : read_all.replace("biome = ", "").split(" / ")) {

                                                    test = true;

                                                    for (String split2 : split.split(" & ")) {

                                                        String split_get = split2.replaceAll("[#!]", "");

                                                        {

                                                            if (split2.contains("#") == false) {

                                                                String variable_text = biome_get.toString().replace("Reference{ResourceKey[minecraft:worldgen/biome / ", "");
                                                                variable_text = variable_text.substring(0, variable_text.indexOf("]"));

                                                                if (variable_text.equals(split_get) == false) {

                                                                    test = false;

                                                                }

                                                            } else {

                                                                if (Misc.isBiomeTaggedAs(biome_get, split_get) == false) {

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

                                                    skip = true;

                                                }

                                            }

                                        } else if (read_all.startsWith("ground_block = ") == true) {

                                            {

                                                ground_block = read_all.replace("ground_block = ", "");

                                            }

                                        } else if (read_all.startsWith("rarity = ") == true) {

                                            {

                                                if (Math.random() >= Double.parseDouble(read_all.replace("rarity = ", "")) * 0.01) {

                                                    skip = true;

                                                }

                                            }

                                        } else if (read_all.startsWith("min_distance = ") == true) {

                                            {

                                                int min_distance = Integer.parseInt(read_all.replace("min_distance = ", ""));

                                                if (min_distance > 0) {

                                                    int region_posX = center_posX >> 9;
                                                    int region_posZ = center_posZ >> 9;

                                                    if (testDistance(region_posX, region_posZ, id, center_posX, center_posZ, min_distance) == true) {

                                                        skip = true;

                                                    } else {

                                                        int test_region_posX = 0;
                                                        int test_region_posZ = 0;

                                                        scan:
                                                        for (int scanX = -1; scanX < 1; scanX++) {

                                                            for (int scanZ = -1; scanZ < 1; scanZ++) {

                                                                test_region_posX = region_posX + scanX;
                                                                test_region_posZ = region_posZ + scanZ;

                                                                // Don't scan the center, as I already scanned it at above.
                                                                if ((region_posX != test_region_posX) || (region_posZ != test_region_posZ)) {

                                                                    if (testDistance(test_region_posX, test_region_posZ, id, center_posX, center_posZ, min_distance) == true) {

                                                                        skip = true;
                                                                        break scan;

                                                                    }

                                                                }

                                                            }

                                                        }

                                                    }

                                                }

                                            }

                                        } else if (read_all.startsWith("group_chance = ") == true) {



                                        } else if (read_all.startsWith("waterside_chance = ") == true) {



                                        } else if (read_all.startsWith("dead_tree_chance = ") == true) {

                                            {

                                                dead_tree = Math.random() < Double.parseDouble(read_all.replace("dead_tree_chance = ", ""));

                                            }

                                        } else if (read_all.startsWith("dead_tree_level = ") == true) {

                                            {

                                                String[] array = read_all.replace("dead_tree_level = ", "").split(" / ");
                                                int chosen = (int) (Math.random() * (array.length - 1));
                                                dead_tree_level = Integer.parseInt(array[chosen]);

                                            }

                                        } else if (read_all.startsWith("start_height_offset = ") == true) {

                                            {

                                                String[] offset = read_all.replace("start_height_offset = ", "").split(" <> ");
                                                int offset_min = Integer.parseInt(offset[0]);
                                                int offset_max = Integer.parseInt(offset[1]);
                                                start_height_offset = offset_min + (int) (Math.random() * Math.abs(offset_max - offset_min));

                                            }

                                        } else if (read_all.startsWith("rotation = ") == true) {

                                            {

                                                String test = read_all.replace("rarity = ", "");

                                                if (test.equals("north") == true) {

                                                    rotation = 1;

                                                } else if (test.equals("west") == true) {

                                                    rotation = 4;

                                                } else if (test.equals("east") == true) {

                                                    rotation = 2;

                                                } else if (test.equals("south") == true) {

                                                    rotation = 3;

                                                } else {

                                                    rotation = (int) (Math.random() * 4) + 1;

                                                }

                                            }

                                        } else if (read_all.startsWith("mirrored = ") == true) {

                                            {

                                                String test = read_all.replace("mirrored = ", "");

                                                if (test.equals("false") == true) {

                                                    mirrored = false;

                                                } else if (test.equals("true") == true) {

                                                    mirrored = true;

                                                } else {

                                                    mirrored = !(Math.random() < 0.5);

                                                }

                                            }

                                            // If it not skips that tree to the end of test, it will run this.
                                            if (skip == false) {

                                                break;

                                            }

                                        }

                                    }

                                }

                            }

                        }

                    }

                } buffered_reader.close(); } catch (Exception e) { e.printStackTrace(); }

            }

            if (skip == false) {

                return_text = id + "|" + rotation + "|" + mirrored + "|" + start_height_offset + "|" + ground_block + "|" + dead_tree + "|" + dead_tree_level;

            }

        }

        return return_text;

    }

    private static boolean testDistance (int region_posX, int region_posZ, String id, int center_posX, int center_posZ, int min_distance) {

        boolean return_logic = false;

        File file = file = new File(Handcode.directory_world_data + "/tree_locations/" + region_posX + "," + region_posZ + ".txt");
        String[] array = null;
        String[] pos = null;
        int posX = 0;
        int posZ = 0;

        if (file.exists() == true) {

            try { BufferedReader buffered_reader = new BufferedReader(new FileReader(file)); String read_all = ""; while ((read_all = buffered_reader.readLine()) != null) {

                {

                    if (read_all.startsWith(id + "|") == true) {

                        array = read_all.split("\\|");
                        pos = array[1].split("/");
                        posX = Integer.parseInt(pos[0]);
                        posZ = Integer.parseInt(pos[2]);

                        if ((Math.abs(center_posX - posX) <= min_distance) && (Math.abs(center_posZ - posZ) <= min_distance)) {

                            return_logic = true;
                            break;

                        }

                    }

                }

            } buffered_reader.close(); } catch (Exception e) { e.printStackTrace(); }

        }

        return return_logic;

    }

    private static void readTreeFile (LevelAccessor level, ServerLevel world, ChunkGenerator chunk_generator, String tree_data, int center_posX, int center_posY, int center_posZ) {

        String storage_directory = "";
        String tree_settings = "";

        String[] array = tree_data.split("\\|");
        String id = array[0];
        int rotation = Integer.parseInt(array[1]);
        boolean mirrored = Boolean.parseBoolean(array[2]);
        int start_height_offset = Integer.parseInt(array[3]);
        String ground_block = array[4];
        boolean dead_tree = Boolean.parseBoolean(array[5]);
        int dead_tree_level = Integer.parseInt(array[6]);

        int original_height = center_posY;

        // Scan "World Gen" File
        {

            File file = new File(Handcode.directory_config + "/custom_packs/.organized/" + id.replace("#", "world_gen") + ".txt");

            if (file.exists() == true) {

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

                } buffered_reader.close(); } catch (Exception e) { e.printStackTrace(); }

            }

        }

        // Scan "Tree Settings" File
        {

            File file = new File(Handcode.directory_game + "/config/tanshugetrees/custom_packs/" + tree_settings);

            if (file.exists() == true && file.isDirectory() == false) {

                // Read File
                {

                    try { BufferedReader buffered_reader = new BufferedReader(new FileReader(file)); String read_all = ""; while ((read_all = buffered_reader.readLine()) != null) {

                        {

                            if (read_all.startsWith("tree_type = ") == true) {

                                {

                                    String tree_type = read_all.replace("tree_type = ", "");

                                    if (dead_tree == false) {

                                        int highest_point = chunk_generator.getBaseHeight(center_posX, center_posZ, Heightmap.Types.WORLD_SURFACE_WG, level, world.getChunkSource().randomState());

                                        if (
                                            (tree_type.equals("land") == true && (center_posY < highest_point))
                                            ||
                                            (tree_type.equals("water") == true && (center_posY == highest_point))
                                        ) {

                                            dead_tree = true;

                                        }

                                    }

                                }

                            } else if (read_all.startsWith("start_height = ") == true) {

                                {

                                    int start_height = Integer.parseInt(read_all.replace("start_height = ", ""));
                                    center_posY = center_posY + start_height + start_height_offset;

                                }

                            }

                        }

                    } buffered_reader.close(); } catch (Exception e) { e.printStackTrace(); }

                }

            }

        }

        File file = new File(Handcode.directory_config + "/custom_packs/" + storage_directory);
        String chosen = "";

        // Random Select File
        {

            File[] list = file.listFiles();

            if (list != null) {

                chosen = list[(int) (Math.random() * list.length)].getName();
                file = new File(file.toPath() + "/" + chosen);

            }

        }

        if (file.exists() == true && file.isDirectory() == false) {

            int sizeX = 0;
            int sizeY = 0;
            int sizeZ = 0;
            int center_sizeX = 0;
            int center_sizeY = 0;
            int center_sizeZ = 0;

            // Get Values
            {

                try { BufferedReader buffered_reader = new BufferedReader(new FileReader(file)); String read_all = ""; while ((read_all = buffered_reader.readLine()) != null) {

                    {

                        if (read_all.equals("") == false) {

                            if (read_all.equals("--------------------------------------------------")) {

                                break;

                            } else {

                                {

                                    if (read_all.startsWith("sizeX = ")) {

                                        sizeX = Integer.parseInt(read_all.replace("sizeX = ", ""));

                                    } else if (read_all.startsWith("sizeY = ")) {

                                        sizeY = Integer.parseInt(read_all.replace("sizeY = ", ""));

                                    } else if (read_all.startsWith("sizeZ = ")) {

                                        sizeZ = Integer.parseInt(read_all.replace("sizeZ = ", ""));

                                    } else if (read_all.startsWith("center_sizeX = ")) {

                                        center_sizeX = Integer.parseInt(read_all.replace("center_sizeX = ", ""));

                                    } else if (read_all.startsWith("center_sizeY = ")) {

                                        center_sizeY = Integer.parseInt(read_all.replace("center_sizeY = ", ""));

                                    } else if (read_all.startsWith("center_sizeZ = ")) {

                                        center_sizeZ = Integer.parseInt(read_all.replace("center_sizeZ = ", ""));

                                    }

                                }

                            }

                        }

                    }

                } buffered_reader.close(); } catch (Exception e) { e.printStackTrace(); }

            }

            // Test World Height
            {

                if (center_posY + sizeY > level.getMaxBuildHeight()) {

                    return;

                }

            }

            // Calculation What Chunks Have This Tree + Test Exist Chunks
            {

                // Rotation & Mirrored
                {

                    if (mirrored == true) {

                        center_sizeX = sizeX - center_sizeX;

                    }

                    if (rotation == 2) {

                        int center_sizeX_save = center_sizeX;
                        center_sizeX = center_sizeZ;
                        center_sizeZ = sizeX - center_sizeX_save;

                        int sizeX_save = sizeX;
                        sizeX = sizeZ;
                        sizeZ = sizeX_save;

                    } else if (rotation == 3) {

                        center_sizeX = sizeX - center_sizeX;
                        center_sizeZ = sizeZ - center_sizeZ;

                    } else if (rotation == 4) {

                        int center_sizeX_save = center_sizeX;
                        center_sizeX = sizeZ - center_sizeZ;
                        center_sizeZ = center_sizeX_save;

                        int sizeX_save = sizeX;
                        sizeX = sizeZ;
                        sizeZ = sizeX_save;

                    }

                }

                int pointX = center_posX - center_sizeX;
                int pointZ = center_posZ - center_sizeZ;

                if ((center_posX > pointX && center_posZ > pointZ) && (center_posX < center_posX + center_sizeX && center_posZ < center_posZ + center_sizeZ)) {

                    boolean check_loaded_chunks = true;

                    for (int test = 0; test < 2; test++) {

                        if (check_loaded_chunks == true) {

                            for (int scanX = pointX >> 4; scanX <= Math.nextUp((pointX + sizeX) / 16.0); scanX++) {

                                for (int scanZ = pointZ >> 4; scanZ <= Math.nextUp((pointZ + sizeZ) / 16.0); scanZ++) {

                                    if (level.hasChunk(scanX, scanZ) == true && level.getChunk(scanX, scanZ).getStatus().isOrAfter(ChunkStatus.FEATURES) == true) {

                                        return;

                                    }

                                }

                            }

                            check_loaded_chunks = false;
                            writeLocationFile(id, center_posX, center_posY, center_posZ);

                        } else {

                            String other_data = "";

                            // Compress "Other Data" Variable
                            {

                                if (dead_tree == false) {

                                    dead_tree_level = 0;

                                }

                                other_data = original_height + "/" + ground_block + "/" + dead_tree_level;

                            }

                            writePlaceFile(pointX >> 4, pointZ >> 4, (pointX + sizeX) >> 4, (pointZ + sizeZ) >> 4, id, chosen, center_posX, center_posY, center_posZ, rotation, mirrored, other_data);

                        }

                    }

                }

            }

        }

    }

    private static void writeLocationFile(String id, int center_posX, int center_posY, int center_posZ) {

        StringBuilder write = new StringBuilder();

        {

            write.append(id + "|" + center_posX + "/" + center_posY + "/" + center_posZ);
            write.append("\n");

        }

        FileManager.writeTXT(Handcode.directory_world_data + "/tree_locations/" + (center_posX >> 9) + "," + (center_posZ >> 9) + ".txt", write.toString(), true);

    }

    private static void writePlaceFile(int from_chunk_posX, int from_chunk_posZ, int to_chunk_posX, int to_chunk_posZ, String id, String chosen, int center_posX, int center_posY, int center_posZ, int rotation, boolean mirrored, String other_data) {

        StringBuilder write = new StringBuilder();

        {

            write.append(from_chunk_posX + "/" + from_chunk_posZ + "|" + to_chunk_posX + "/" + to_chunk_posZ + "|" + id + "|" + chosen + "|" + center_posX + "/" + center_posY + "/" + center_posZ + "|" + rotation + "/" + mirrored + "|" + other_data);
            write.append("\n");

        }

        for (int scanX = from_chunk_posX >> 5; scanX <= Math.nextUp(to_chunk_posX / 32.0); scanX++) {

            for (int scanZ = from_chunk_posZ >> 5; scanZ <= Math.nextUp(to_chunk_posZ / 32.0); scanZ++) {

                FileManager.writeTXT(Handcode.directory_world_data + "/place/" + scanX + "," + scanZ + ".txt", write.toString(), true);

            }

        }

    }

}