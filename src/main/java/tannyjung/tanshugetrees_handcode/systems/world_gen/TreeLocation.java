package tannyjung.tanshugetrees_handcode.systems.world_gen;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.Main;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.*;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import tannyjung.tanshugetrees.network.TanshugetreesModVariables;
import tannyjung.tanshugetrees_handcode.misc.FileManager;
import tannyjung.tanshugetrees_handcode.misc.GameUtils;
import tannyjung.tanshugetrees.TanshugetreesMod;
import tannyjung.tanshugetrees_handcode.Handcode;
import tannyjung.tanshugetrees_handcode.systems.config.ConfigMain;

import java.io.*;
import java.util.concurrent.*;

public class TreeLocation {

    public static int world_gen_overlay_animation = 0;
    public static int world_gen_overlay_bar = 0;
    public static String world_gen_overlay_details_biome = "";
    public static String world_gen_overlay_details_tree = "";

    public static void run (FeaturePlaceContext <NoneFeatureConfiguration> context) {

        WorldGenLevel world_gen = context.level();
        ServerLevel world = context.level().getLevel();
        ChunkGenerator chunk_generator = context.chunkGenerator();
        String dimension = GameUtils.getCurrentDimensionID(world).replace(":", "-");

        int region_posX = context.origin().getX() >> 9;
        int region_posZ = context.origin().getZ() >> 9;
        File file = new File(Handcode.directory_world_data + "/regions/" + dimension + "/" + region_posX + "," + region_posZ + ".txt");

        if (file.exists() == false) {

            // Create Empty File
            FileManager.writeTXT(file.toPath().toString(), "", true);

            int chunk_posX = 0;
            int chunk_posZ = 0;
            int center_posX = 0;
            int center_posZ = 0;

            world_gen_overlay_animation = 4;
            world_gen_overlay_bar = 0;
            TanshugetreesMod.LOGGER.info("Generating Region (" + region_posX + "/" + region_posZ + ")");

            // Overlay Loading Loop
            {

                CompletableFuture.runAsync(() -> {

                    while (world_gen_overlay_animation != 0) {

                        if (world_gen_overlay_animation < 4) {

                            world_gen_overlay_animation = world_gen_overlay_animation + 1;

                        } else {

                            world_gen_overlay_animation = 1;

                        }

                        try {

                            Thread.sleep(1000);

                        } catch (InterruptedException e) {

                            TanshugetreesMod.LOGGER.error(e.getMessage());

                        }

                    }

                });

            }

            // Region Scanning
            {

                for (int scanX = 0; scanX < 32; scanX++) {

                    for (int scanZ = 0; scanZ < 32; scanZ++) {

                        world_gen_overlay_bar = world_gen_overlay_bar + 1;

                        if (Math.random() < ConfigMain.region_scan_chance) {

                            chunk_posX = (region_posX * 32) + scanX;
                            chunk_posZ = (region_posZ * 32) + scanZ;

                            if (world_gen.hasChunk(chunk_posX, chunk_posZ) == false || (world_gen.hasChunk(chunk_posX, chunk_posZ) == true && world_gen.getChunk(chunk_posX, chunk_posZ).getStatus().isOrAfter(ChunkStatus.FULL)) == false) {

                                center_posX = (chunk_posX * 16) + (int) (Math.random() * 16);
                                center_posZ = (chunk_posZ * 16) + (int) (Math.random() * 16);
                                getData(world_gen, world, chunk_generator, dimension, center_posX, center_posZ);

                            }

                        }

                    }

                }

            }

            world_gen_overlay_animation = 0;
            TanshugetreesMod.LOGGER.info("Completed!");

        }

    }

    private static void getData (WorldGenLevel world_gen, ServerLevel world, ChunkGenerator chunk_generator, String dimension, int center_posX, int center_posZ) {

        File file = new File(Handcode.directory_config + "/config_placement.txt");

        if (file.exists() == true) {

            boolean start_test = false;
            boolean skip = true;
            Holder<Biome> biome_center = world_gen.getBiome(new BlockPos(center_posX, world_gen.getMaxBuildHeight(), center_posZ));

            String id = "";
            String biome = "";
            String ground_block = "";
            double rarity = 0.0;
            int min_distance = 0;
            int group_size = 0;
            double waterside_chance = 0.0;
            double dead_tree_chance = 0.0;
            String dead_tree_level = "";
            String start_height_offset = "";
            String rotation = "";
            String mirrored = "";

            world_gen_overlay_details_biome = GameUtils.biomeToBiomeID(biome_center);

            // Read Placement Config
            {

                try { BufferedReader buffered_reader = new BufferedReader(new FileReader(file)); String read_all = ""; while ((read_all = buffered_reader.readLine()) != null) {

                    {

                        if (read_all.equals("") == false) {

                            if (start_test == false) {

                                if (read_all.startsWith("---") == true) {

                                    start_test = true;

                                }

                            } else {

                                if (read_all.startsWith("[") == true) {

                                    // Reset The Test
                                    {

                                        if (read_all.startsWith("[INCOMPATIBLE] ") == true) {

                                            skip = true;

                                        } else {

                                            id = read_all.substring(read_all.indexOf("]") + 2).replace(" > ", "/");
                                            skip = false;

                                            world_gen_overlay_details_tree = id;

                                        }

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

                                                biome = read_all.replace("biome = ", "");

                                                if (testBiome(biome_center, biome) == false) {

                                                    skip = true;

                                                }

                                            }

                                        } else if (read_all.startsWith("ground_block = ") == true) {

                                            {

                                                ground_block = read_all.replace("ground_block = ", "");

                                            }

                                        } else if (read_all.startsWith("rarity = ") == true) {

                                            {

                                                rarity = Double.parseDouble(read_all.replace("rarity = ", ""));
                                                rarity = (rarity * 0.01) * ConfigMain.multiply_rarity;

                                                if (Math.random() >= rarity) {

                                                    skip = true;

                                                }

                                            }

                                        } else if (read_all.startsWith("min_distance = ") == true) {

                                            {

                                                min_distance = Integer.parseInt(read_all.replace("min_distance = ", ""));
                                                min_distance = (int) Math.ceil(min_distance * ConfigMain.multiply_min_distance);

                                                if (min_distance > 0) {

                                                    if (testDistance(dimension, id, center_posX, center_posZ, min_distance) == false) {

                                                        skip = true;

                                                    }

                                                }

                                            }

                                        } else if (read_all.startsWith("group_size = ") == true) {

                                            {

                                                String[] get = read_all.replace("group_size = ", "").split(" <> ");
                                                int min = Integer.parseInt(get[0]);
                                                int max = Integer.parseInt(get[1]);
                                                min = (int) Math.ceil(min * ConfigMain.multiply_group_size);
                                                max = (int) Math.ceil(max * ConfigMain.multiply_group_size);

                                                // Round if lower than 0
                                                {

                                                    if (min < 1) {

                                                        min = 1;

                                                    }

                                                    if (max < 1) {

                                                        max = 1;

                                                    }

                                                }

                                                group_size = Mth.nextInt(RandomSource.create(), min, max);

                                            }

                                        } else if (read_all.startsWith("waterside_chance = ") == true) {

                                            {

                                                waterside_chance = Double.parseDouble(read_all.replace("waterside_chance = ", ""));
                                                waterside_chance = waterside_chance * ConfigMain.multiply_waterside_chance;

                                            }

                                        } else if (read_all.startsWith("dead_tree_chance = ") == true) {

                                            {

                                                dead_tree_chance = Double.parseDouble(read_all.replace("dead_tree_chance = ", ""));
                                                dead_tree_chance = dead_tree_chance * ConfigMain.multiply_dead_tree_chance;

                                            }

                                        } else if (read_all.startsWith("dead_tree_level = ") == true) {

                                            {

                                                dead_tree_level = read_all.replace("dead_tree_level = ", "");

                                            }

                                        } else if (read_all.startsWith("start_height_offset = ") == true) {

                                            {

                                                start_height_offset = read_all.replace("start_height_offset = ", "");

                                            }

                                        } else if (read_all.startsWith("rotation = ") == true) {

                                            {

                                                rotation = read_all.replace("rotation = ", "");

                                            }

                                        } else if (read_all.startsWith("mirrored = ") == true) {

                                            {

                                                mirrored = read_all.replace("mirrored = ", "");

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

                } buffered_reader.close(); } catch (Exception e) { TanshugetreesMod.LOGGER.error(e.getMessage()); }

            }

            if (skip == false) {

                int center_posY = chunk_generator.getBaseHeight(center_posX, center_posZ, Heightmap.Types.OCEAN_FLOOR_WG, world_gen, world.getChunkSource().randomState());

                // Surface Smoothness Detector & Waterside Chance
                {

                    if (testSurroundingArea(world_gen, world, chunk_generator, center_posX, center_posY, center_posZ, true, waterside_chance) == false) {

                        return;

                    }

                }

                String tree_data = id + "|" + ground_block + "|" + start_height_offset + "|" + rotation + "|" + mirrored + "|" + dead_tree_chance + "|" + dead_tree_level;
                readTreeFile(world_gen, world, chunk_generator, dimension, tree_data, center_posX, center_posY, center_posZ);

                // Group Spawning
                {

                    if (group_size > 1) {

                        while (group_size > 0) {

                            group_size = group_size - 1;
                            center_posX = center_posX + Mth.nextInt(RandomSource.create(), -(min_distance + 1), (min_distance + 1));
                            center_posZ = center_posZ + Mth.nextInt(RandomSource.create(), -(min_distance + 1), (min_distance + 1));
                            center_posY = chunk_generator.getBaseHeight(center_posX, center_posZ, Heightmap.Types.OCEAN_FLOOR_WG, world_gen, world.getChunkSource().randomState());

                            // Biome
                            {

                                biome_center = world_gen.getBiome(new BlockPos(center_posX, world_gen.getMaxBuildHeight(), center_posZ));

                                if (testBiome(biome_center, biome) == false) {

                                    continue;

                                }

                            }

                            // Min Distance
                            {

                                if (min_distance > 0) {

                                    if (testDistance(dimension, id, center_posX, center_posZ, min_distance) == false) {

                                        continue;

                                    }

                                }

                            }

                            // Surface Smoothness Detector
                            {

                                if (testSurroundingArea(world_gen, world, chunk_generator, center_posX, center_posY, center_posZ, false, waterside_chance) == false) {

                                    return;

                                }

                            }

                            readTreeFile(world_gen, world, chunk_generator, dimension, tree_data, center_posX, center_posY, center_posZ);

                        }

                    }

                }

            }

        }

    }

    private static boolean testBiome (Holder<Biome> biome_center, String config_value) {

        boolean return_logic = false;

        {

            String biome_centerID = GameUtils.biomeToBiomeID(biome_center);

            for (String split : config_value.split(" / ")) {

                return_logic = true;

                for (String split2 : split.split(", ")) {

                    String split_get = split2.replaceAll("[#!]", "");

                    {

                        if (split2.contains("#") == false) {

                            if (biome_centerID.equals(split_get) == false) {

                                return_logic = false;

                            }

                        } else {

                            if (GameUtils.isBiomeTaggedAs(biome_center, split_get) == false) {

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

    private static boolean testDistance (String dimension, String id, int center_posX, int center_posZ, int min_distance) {

        boolean return_logic = true;

        {

            test:
            {

                int size = 32 >> Handcode.quadtree_level;

                int center_chunk_posX = center_posX >> 4;
                int center_chunk_posZ = center_posZ >> 4;
                int start_chunk_posX = 0;
                int start_chunk_posZ = 0;
                int chunk_posX = 0;
                int chunk_posZ = 0;
                int scanX = 0;
                int scanZ = 0;
                int step = 0;

                for (int distance = 0; distance <= (int) Math.ceil((min_distance / 16.0) / (double) size) * size; distance = distance + size) {

                    start_chunk_posX = center_chunk_posX - distance;
                    start_chunk_posZ = center_chunk_posZ - distance;
                    step = 1;
                    scanX = 0;
                    scanZ = 0;

                    while (true) {

                        chunk_posX = start_chunk_posX + scanX;
                        chunk_posZ = start_chunk_posZ + scanZ;

                        {

                            File file = file = new File(Handcode.directory_world_data + "/tree_locations/" + dimension + "/" + (chunk_posX >> 5) + "," + (chunk_posZ >> 5) + "/" + FileManager.quardtreeChunkToNode(chunk_posX, chunk_posZ) + ".txt");
                            String[] get = null;
                            String[] pos = null;
                            int posX = 0;
                            int posZ = 0;

                            if (file.exists() == true && file.isDirectory() == false) {

                                {

                                    try { BufferedReader buffered_reader = new BufferedReader(new FileReader(file)); String read_all = ""; while ((read_all = buffered_reader.readLine()) != null) {

                                        {

                                            if (read_all.startsWith(id + "|") == true) {

                                                get = read_all.split("\\|");
                                                pos = get[1].split("/");
                                                posX = Integer.parseInt(pos[0]);
                                                posZ = Integer.parseInt(pos[2]);

                                                if ((Math.abs(center_posX - posX) <= min_distance) && (Math.abs(center_posZ - posZ) <= min_distance)) {

                                                    return_logic = false;
                                                    break test;

                                                }

                                            }

                                        }

                                    } buffered_reader.close(); } catch (Exception e) { TanshugetreesMod.LOGGER.error(e.getMessage()); }

                                }

                            }

                        }

                        // Steps
                        {

                            if (step == 1) {

                                scanX = scanX + size;

                                if (scanX > (distance * 2)) {

                                    step = 2;

                                }

                            } else if (step == 2) {

                                scanZ = scanZ + size;

                                if (scanZ > (distance * 2)) {

                                    step = 3;

                                }

                            } else if (step == 3) {

                                scanX = scanX - size;

                                if (scanX <= 0) {

                                    step = 4;

                                }

                            } else if (step == 4) {

                                scanZ = scanZ - size;

                                if (scanZ <= 0) {

                                    break;

                                }

                            }

                        }

                    }

                }

            }

        }

        return return_logic;

    }

    private static boolean testSurroundingArea (WorldGenLevel world_gen, ServerLevel world, ChunkGenerator chunk_generator, int center_posX, int center_posY, int center_posZ, boolean waterside_detection, double waterside_chance) {

        boolean return_logic = true;

        {

            test:
            if (ConfigMain.surface_smoothness_detection == true || ConfigMain.waterside_detection == true) {

                int size = ConfigMain.surrounding_area_detection_size;
                int ocean_floor1 = chunk_generator.getBaseHeight(center_posX + size, center_posZ + size, Heightmap.Types.OCEAN_FLOOR_WG, world_gen, world.getChunkSource().randomState());
                int ocean_floor2 = chunk_generator.getBaseHeight(center_posX + size, center_posZ - size, Heightmap.Types.OCEAN_FLOOR_WG, world_gen, world.getChunkSource().randomState());
                int ocean_floor3 = chunk_generator.getBaseHeight(center_posX - size, center_posZ + size, Heightmap.Types.OCEAN_FLOOR_WG, world_gen, world.getChunkSource().randomState());
                int ocean_floor4 = chunk_generator.getBaseHeight(center_posX - size, center_posZ - size, Heightmap.Types.OCEAN_FLOOR_WG, world_gen, world.getChunkSource().randomState());

                if (ConfigMain.surface_smoothness_detection == true) {

                    int height = ConfigMain.surface_smoothness_detection_height;

                    if ((Math.abs(center_posY - ocean_floor1) > height) || (Math.abs(center_posY - ocean_floor2) > height) || (Math.abs(center_posY - ocean_floor3) > height) || (Math.abs(center_posY - ocean_floor4) > height)) {

                        return_logic = false;
                        break test;

                    }

                }

                if (ConfigMain.waterside_detection == true) {

                    if (waterside_detection == true) {

                        if (Math.random() < waterside_chance) {

                            int surface1 = chunk_generator.getBaseHeight(center_posX + size, center_posZ + size, Heightmap.Types.WORLD_SURFACE_WG, world_gen, world.getChunkSource().randomState());
                            int surface2 = chunk_generator.getBaseHeight(center_posX + size, center_posZ - size, Heightmap.Types.WORLD_SURFACE_WG, world_gen, world.getChunkSource().randomState());
                            int surface3 = chunk_generator.getBaseHeight(center_posX - size, center_posZ + size, Heightmap.Types.WORLD_SURFACE_WG, world_gen, world.getChunkSource().randomState());
                            int surface4 = chunk_generator.getBaseHeight(center_posX - size, center_posZ - size, Heightmap.Types.WORLD_SURFACE_WG, world_gen, world.getChunkSource().randomState());

                            if (surface1 == ocean_floor1 && surface2 == ocean_floor2 && surface3 == ocean_floor3 && surface4 == ocean_floor4) {

                                return_logic = false;
                                break test;

                            }

                        }

                    }

                } else {

                    return_logic = false;
                    break test;

                }

            }

        }

        return return_logic;

    }

    private static void readTreeFile (WorldGenLevel world_gen, ServerLevel world, ChunkGenerator chunk_generator, String dimension, String tree_data, int center_posX, int center_posY, int center_posZ) {

        String storage_directory = "";
        String tree_settings = "";

        String[] get = tree_data.split("\\|");
        String id = get[0];
        String ground_block = get[1];
        String start_height_offset = get[2];
        String rotation = get[3];
        String mirrored = get[4];
        double dead_tree_chance = Double.parseDouble(get[5]);
        String dead_tree_level_all = get[6];

        // Scan "World Gen" File
        {

            File file = new File(Handcode.directory_config + "/custom_packs/.organized/world_gen/" + id + ".txt");

            if (file.exists() == true && file.isDirectory() == false) {

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

        File chosen = new File(Handcode.directory_config + "/custom_packs/" + storage_directory);

        // Random Select File
        {

            File[] list = chosen.listFiles();

            if (list != null) {

                chosen = new File(chosen.toPath() + "/" + list[(int) (Math.random() * list.length)].getName());

            }

        }

        if (chosen.exists() == true && chosen.isDirectory() == false) {

            String tree_type = "";
            int start_height = 0;
            int dead_tree_level = 0;
            int original_height = center_posY;

            // Scan "Tree Settings" File
            {

                File file = new File(Handcode.directory_config + "/custom_packs/.organized/presets/" + tree_settings);

                if (file.exists() == true && file.isDirectory() == false) {

                    {

                        try { BufferedReader buffered_reader = new BufferedReader(new FileReader(file)); String read_all = ""; while ((read_all = buffered_reader.readLine()) != null) {

                            {

                                if (read_all.startsWith("tree_type = ") == true) {

                                    {

                                        tree_type = read_all.replace("tree_type = ", "");

                                    }

                                } else if (read_all.startsWith("start_height = ") == true) {

                                    {

                                        start_height = Integer.parseInt(read_all.replace("start_height = ", ""));

                                    }

                                }

                            }

                        } buffered_reader.close(); } catch (Exception e) { TanshugetreesMod.LOGGER.error(e.getMessage()); }

                    }

                }

            }

            int sizeX = 0;
            int sizeY = 0;
            int sizeZ = 0;
            int center_sizeX = 0;
            int center_sizeZ = 0;

            // Get Values
            {

                try { BufferedReader buffered_reader = new BufferedReader(new FileReader(chosen)); String read_all = ""; while ((read_all = buffered_reader.readLine()) != null) {

                    {

                        if (read_all.equals("") == false) {

                            if (read_all.equals("--------------------------------------------------") == true) {

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

                                    } else if (read_all.startsWith("center_sizeZ = ")) {

                                        center_sizeZ = Integer.parseInt(read_all.replace("center_sizeZ = ", ""));

                                    }

                                }

                            }

                        }

                    }

                } buffered_reader.close(); } catch (Exception e) { TanshugetreesMod.LOGGER.error(e.getMessage()); }

            }

            // Start Height
            {

                int offset = 0;

                // Offset
                {

                    String[] offset_get = start_height_offset.split(" <> ");
                    int offset_min = Integer.parseInt(offset_get[0]);
                    int offset_max = Integer.parseInt(offset_get[1]);
                    offset = Mth.nextInt(RandomSource.create(), offset_min, offset_max);

                }

                center_posY = center_posY + start_height + offset;

            }

            // Test World Height
            {

                if (center_posY + sizeY > world_gen.getMaxBuildHeight()) {

                    return;

                }

            }

            // Dead Tree
            {

                boolean dead_tree = false;

                if (Math.random() < dead_tree_chance) {

                    dead_tree = true;

                } else {

                    int highest_point = chunk_generator.getBaseHeight(center_posX, center_posZ, Heightmap.Types.WORLD_SURFACE_WG, world_gen, world.getChunkSource().randomState());

                    if (
                        (tree_type.equals("land") == true && (original_height < highest_point))
                        ||
                        (tree_type.equals("water") == true && (original_height == highest_point))
                    ) {

                        dead_tree = true;

                    }

                }

                if (dead_tree == true) {

                    String[] levels = dead_tree_level_all.split(" / ");
                    dead_tree_level = Integer.parseInt(levels[(int) (Math.random() * (levels.length - 1))]);

                }

            }

            // Rotation & Mirrored
            {

                // Convert
                {

                    if (rotation.equals("north") == true) {

                        rotation = "1";

                    } else if (rotation.equals("west") == true) {

                        rotation = "4";

                    } else if (rotation.equals("east") == true) {

                        rotation = "2";

                    } else if (rotation.equals("south") == true) {

                        rotation = "3";

                    } else {

                        rotation = String.valueOf((int) (Math.random() * 4) + 1);

                    }

                    if (mirrored.equals("false") == true) {

                        mirrored = "false";

                    } else if (mirrored.equals("true") == true) {

                        mirrored = "true";

                    } else {

                        mirrored = String.valueOf((Math.random() < 0.5));

                    }

                }

                // Applying
                {

                    if (mirrored.equals("true") == true) {

                        center_sizeX = sizeX - center_sizeX;

                    }

                    if (rotation.equals("2") == true) {

                        int center_sizeX_save = center_sizeX;
                        center_sizeX = center_sizeZ;
                        center_sizeZ = sizeX - center_sizeX_save;

                        int sizeX_save = sizeX;
                        sizeX = sizeZ;
                        sizeZ = sizeX_save;

                    } else if (rotation.equals("3") == true) {

                        center_sizeX = sizeX - center_sizeX;
                        center_sizeZ = sizeZ - center_sizeZ;

                    } else if (rotation.equals("4") == true) {

                        int center_sizeX_save = center_sizeX;
                        center_sizeX = sizeZ - center_sizeZ;
                        center_sizeZ = center_sizeX_save;

                        int sizeX_save = sizeX;
                        sizeX = sizeZ;
                        sizeZ = sizeX_save;

                    }

                }

            }

            // Calculation
            {

                int from_chunkX = center_posX - center_sizeX;
                int from_chunkZ = center_posZ - center_sizeZ;
                int to_chunkX = (from_chunkX + sizeX) >> 4;
                int to_chunkZ = (from_chunkZ + sizeZ) >> 4;
                from_chunkX = from_chunkX >> 4;
                from_chunkZ = from_chunkZ >> 4;

                // Test Exist Chunk
                {

                    for (int scanX = from_chunkX; scanX <= to_chunkX; scanX++) {

                        for (int scanZ = from_chunkZ; scanZ <= to_chunkZ; scanZ++) {

                            if (world_gen.hasChunk(scanX, scanZ) == true && world_gen.getChunk(scanX, scanZ).getStatus().isOrAfter(ChunkStatus.FEATURES) == true) {

                                return;

                            }

                        }

                    }

                }

                writeLocationFile(dimension, id, center_posX, center_posY, center_posZ);
                String other_data = original_height + "/" + ground_block + "/" + dead_tree_level;
                writePlaceFile(dimension, from_chunkX, from_chunkZ, to_chunkX, to_chunkZ, id, chosen, center_posX, center_posY, center_posZ, rotation, mirrored, other_data);

            }

        }

    }

    private static void writeLocationFile(String dimension, String id, int center_posX, int center_posY, int center_posZ) {

        {

            StringBuilder write = new StringBuilder();

            {

                write
                        .append(id)
                        .append("|")
                        .append(center_posX)
                        .append("/")
                        .append(center_posY)
                        .append("/")
                        .append(center_posZ)
                ;

                write.append("\n");

            }

            String folder = Handcode.directory_world_data + "/tree_locations/" + dimension + "/" + (center_posX >> 9) + "," + (center_posZ >> 9);
            FileManager.createFolder(folder);
            FileManager.writeTXT(folder + "/" + FileManager.quardtreeChunkToNode((center_posX >> 4), (center_posZ >> 4)) + ".txt", write.toString(), true);

        }

    }

    private static void writePlaceFile(String dimension, int from_chunkX, int from_chunkZ, int to_chunkX, int to_chunkZ, String id, File chosen, int center_posX, int center_posY, int center_posZ, String rotation, String mirrored, String other_data) {

        {

            StringBuilder write = new StringBuilder();

            {

                write
                        .append(from_chunkX)
                        .append("/")
                        .append(from_chunkZ)
                        .append("|")
                        .append(to_chunkX)
                        .append("/")
                        .append(to_chunkZ)
                        .append("|")
                        .append(id)
                        .append("|")
                        .append(chosen.getName())
                        .append("|")
                        .append(center_posX)
                        .append("/")
                        .append(center_posY)
                        .append("/")
                        .append(center_posZ)
                        .append("|")
                        .append(rotation)
                        .append("/")
                        .append(mirrored)
                        .append("|")
                        .append(other_data)
                ;

                write.append("\n");

            }

            int size = 32 >> Handcode.quadtree_level;
            int to_chunkX_test = ((int) Math.ceil(to_chunkX / (double) size) * size) + size;
            int to_chunkZ_test = ((int) Math.ceil(to_chunkZ / (double) size) * size) + size;
            String folder = "";

            for (int scanX = from_chunkX; scanX < to_chunkX_test; scanX = scanX + size) {

                for (int scanZ = from_chunkZ; scanZ < to_chunkZ_test; scanZ = scanZ + size) {

                    folder = Handcode.directory_world_data + "/place/" + dimension + "/" + (scanX >> 5) + "," + (scanZ >> 5);
                    FileManager.createFolder(folder);
                    FileManager.writeTXT(folder + "/" + FileManager.quardtreeChunkToNode(scanX, scanZ) + ".txt", write.toString(), true);

                }

            }

        }

    }

}