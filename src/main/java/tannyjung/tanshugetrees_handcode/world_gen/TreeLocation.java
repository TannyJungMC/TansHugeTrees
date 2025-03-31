package tannyjung.tanshugetrees_handcode.world_gen;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
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
        File file = new File(Handcode.directory_world_data + "/regions/" + region_posX + "," + region_posZ + ".txt");

        if (file.exists() == false) {

            // Write The File
            {

                StringBuilder write = new StringBuilder();

                {

                    write.append("");

                }

                FileManager.writeTXT(file.toPath().toString(), write.toString(), true);

            }

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

            // Region Scanning
            {

                for (int scanX = 0; scanX < 32; scanX++) {

                    for (int scanZ = 0; scanZ < 32; scanZ++) {

                        generating_region_bar = generating_region_bar + 1;

                        if (Math.random() < ConfigMain.global_rarity * 0.01) {

                            chunk_posX = (region_posX * 32) + scanX;
                            chunk_posZ = (region_posZ * 32) + scanZ;

                            if (level.hasChunk(chunk_posX, chunk_posZ) == false || (level.hasChunk(chunk_posX, chunk_posZ) == true && level.getChunk(chunk_posX, chunk_posZ).getStatus().isOrAfter(ChunkStatus.FULL)) == false) {

                                center_posX = (chunk_posX * 16) + (int) (Math.random() * 16);
                                center_posZ = (chunk_posZ * 16) + (int) (Math.random() * 16);
                                center_posY = chunk_generator.getBaseHeight(center_posX, center_posZ, Heightmap.Types.OCEAN_FLOOR_WG, level, world.getChunkSource().randomState());

                                getTreeData(level, world, chunk_generator, center_posX, center_posY, center_posZ);

                            }

                        }

                    }

                }

            }

            generating_region = 0;
            TanshugetreesMod.LOGGER.info("Completed!");

        }

    }

    private static void getTreeData (LevelAccessor level, ServerLevel world, ChunkGenerator chunk_generator, int center_posX, int center_posY, int center_posZ) {

        File file = new File(Handcode.directory_config + "/config_placement.txt");

        if (file.exists() == true) {

            boolean skip = true;
            String id = "";
            String ground_block = "";
            int min_distance = 0;
            int group_size = 0;
            double waterside_chance = 0.0;
            String start_height_offset = "";
            String rotation = "";
            String mirrored = "";
            double dead_tree_chance = 0.0;
            String dead_tree_level = "";

            // Read, Test, and Get Values
            {

                try { BufferedReader buffered_reader = new BufferedReader(new FileReader(file)); String read_all = ""; while ((read_all = buffered_reader.readLine()) != null) {

                    {

                        if (read_all.equals("") == false) {

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

                                                min_distance = Integer.parseInt(read_all.replace("min_distance = ", ""));

                                                if (min_distance > 0) {

                                                    if (testDistance(id, center_posX, center_posZ, min_distance) == false) {

                                                        skip = true;

                                                    }

                                                }

                                            }

                                        } else if (read_all.startsWith("group_size = ") == true) {

                                            {

                                                String[] get = read_all.replace("group_size = ", "").split(" <> ");
                                                int min = Integer.parseInt(get[0]);
                                                int max = Integer.parseInt(get[1]);
                                                group_size = Mth.nextInt(RandomSource.create(), min, max);

                                            }

                                        } else if (read_all.startsWith("waterside_chance = ") == true) {

                                            {

                                                waterside_chance = Double.parseDouble(read_all.replace("waterside_chance = ", ""));

                                            }

                                        } else if (read_all.startsWith("dead_tree_chance = ") == true) {

                                            {

                                                dead_tree_chance = Double.parseDouble(read_all.replace("dead_tree_chance = ", ""));

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

                } buffered_reader.close(); } catch (Exception e) { e.printStackTrace(); }

            }

            if (skip == false) {

                String tree_data = id + "|" + ground_block + "|" + start_height_offset + "|" + waterside_chance + "|" + rotation + "|" + mirrored + "|" + dead_tree_chance + "|" + dead_tree_level;
                readTreeFile(level, world, chunk_generator, tree_data, center_posX, center_posY, center_posZ);

                // Group Spawning
                {

                    if (group_size > 1) {

                        int min_distance_plus = min_distance + 1;

                        for (int loop = group_size; loop > 0; loop--) {

                            center_posX = center_posX + Mth.nextInt(RandomSource.create(), -min_distance_plus, min_distance_plus);
                            center_posZ = center_posZ + Mth.nextInt(RandomSource.create(), -min_distance_plus, min_distance_plus);

                            if (testDistance(id, center_posX, center_posZ, min_distance) == false) {

                                continue;

                            }

                            center_posY = chunk_generator.getBaseHeight(center_posX, center_posZ, Heightmap.Types.OCEAN_FLOOR_WG, level, world.getChunkSource().randomState());
                            readTreeFile(level, world, chunk_generator, tree_data, center_posX, center_posY, center_posZ);

                        }

                    }

                }

            }

        }

    }

    private static boolean testDistance (String id, int center_posX, int center_posZ, int min_distance) {

        boolean return_logic = true;

        int region_posX = center_posX >> 9;
        int region_posZ = center_posZ >> 9;

        if (testDistanceRun(id, region_posX, region_posZ, center_posX, center_posZ, min_distance) == false) {

            return_logic = false;

        } else if (
            testDistanceRun(id, region_posX + 1, region_posZ + 0, center_posX, center_posZ, min_distance) == false
            ||
            testDistanceRun(id, region_posX - 1, region_posZ + 0, center_posX, center_posZ, min_distance) == false
            ||
            testDistanceRun(id, region_posX + 0, region_posZ + 1, center_posX, center_posZ, min_distance) == false
            ||
            testDistanceRun(id, region_posX + 0, region_posZ - 1, center_posX, center_posZ, min_distance) == false
        ) {

            return_logic = false;

        } else if (
            testDistanceRun(id, region_posX + 1, region_posZ + 1, center_posX, center_posZ, min_distance) == false
            ||
            testDistanceRun(id, region_posX + 1, region_posZ - 1, center_posX, center_posZ, min_distance) == false
            ||
            testDistanceRun(id, region_posX - 1, region_posZ + 1, center_posX, center_posZ, min_distance) == false
            ||
            testDistanceRun(id, region_posX - 1, region_posZ - 1, center_posX, center_posZ, min_distance) == false
        ) {

            return_logic = false;

        }

        return return_logic;

    }

    private static boolean testDistanceRun (String id, int region_posX, int region_posZ, int center_posX, int center_posZ, int min_distance) {

        boolean return_logic = true;

        File file = file = new File(Handcode.directory_world_data + "/tree_locations/" + region_posX + "," + region_posZ + ".txt");
        String[] array = null;
        String[] pos = null;
        int posX = 0;
        int posZ = 0;

        if (file.exists() == true) {

            {

                try { BufferedReader buffered_reader = new BufferedReader(new FileReader(file)); String read_all = ""; while ((read_all = buffered_reader.readLine()) != null) {

                    {

                        if (read_all.startsWith(id + "|") == true) {

                            array = read_all.split("\\|");
                            pos = array[1].split("/");
                            posX = Integer.parseInt(pos[0]);
                            posZ = Integer.parseInt(pos[2]);

                            if ((Math.abs(center_posX - posX) <= min_distance) && (Math.abs(center_posZ - posZ) <= min_distance)) {

                                return_logic = false;
                                break;

                            }

                        }

                    }

                } buffered_reader.close(); } catch (Exception e) { e.printStackTrace(); }

            }

        }

        return return_logic;

    }

    private static void readTreeFile (LevelAccessor level, ServerLevel world, ChunkGenerator chunk_generator, String tree_data, int center_posX, int center_posY, int center_posZ) {

        String storage_directory = "";
        String tree_settings = "";

        String[] get = tree_data.split("\\|");
        String id = get[0];
        String ground_block = get[1];
        String start_height_offset = get[2];
        double waterside_chance = Double.parseDouble(get[3]);
        String rotation = get[4];
        String mirrored = get[5];
        double dead_tree_chance = Double.parseDouble(get[6]);
        String dead_tree_level_all = get[7];

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

        File chosen = new File(Handcode.directory_config + "/custom_packs/" + storage_directory);

        // Random Select File
        {

            File[] list = chosen.listFiles();

            if (list != null) {

                chosen = new File(chosen.toPath() + "/" + list[(int) (Math.random() * list.length)].getName());

            }

        }

        if (chosen.exists() == true && chosen.isDirectory() == false) {

            // Surface Smoothness Detector & Waterside Chance
            {

                if (ConfigMain.surrounding_area_detection_size != 0) {

                    int size = ConfigMain.surrounding_area_detection_size;
                    int ocean_floor1 = chunk_generator.getBaseHeight(center_posX + size, center_posZ + size, Heightmap.Types.OCEAN_FLOOR_WG, level, world.getChunkSource().randomState());
                    int ocean_floor2 = chunk_generator.getBaseHeight(center_posX + size, center_posZ - size, Heightmap.Types.OCEAN_FLOOR_WG, level, world.getChunkSource().randomState());
                    int ocean_floor3 = chunk_generator.getBaseHeight(center_posX - size, center_posZ + size, Heightmap.Types.OCEAN_FLOOR_WG, level, world.getChunkSource().randomState());
                    int ocean_floor4 = chunk_generator.getBaseHeight(center_posX - size, center_posZ - size, Heightmap.Types.OCEAN_FLOOR_WG, level, world.getChunkSource().randomState());

                    if (ConfigMain.surface_smoothness_detection == true) {

                        int height = ConfigMain.surface_smoothness_detection_height;

                        if ((Math.abs(center_posY - ocean_floor1) > height) || (Math.abs(center_posY - ocean_floor2) > height) || (Math.abs(center_posY - ocean_floor3) > height) || (Math.abs(center_posY - ocean_floor4) > height)) {

                            return;

                        }

                    }

                    if (ConfigMain.waterside_detection == true) {

                        if (Math.random() < waterside_chance) {

                            int world_surface1 = chunk_generator.getBaseHeight(center_posX + size, center_posZ + size, Heightmap.Types.WORLD_SURFACE_WG, level, world.getChunkSource().randomState());
                            int world_surface2 = chunk_generator.getBaseHeight(center_posX + size, center_posZ - size, Heightmap.Types.WORLD_SURFACE_WG, level, world.getChunkSource().randomState());
                            int world_surface3 = chunk_generator.getBaseHeight(center_posX - size, center_posZ + size, Heightmap.Types.WORLD_SURFACE_WG, level, world.getChunkSource().randomState());
                            int world_surface4 = chunk_generator.getBaseHeight(center_posX - size, center_posZ - size, Heightmap.Types.WORLD_SURFACE_WG, level, world.getChunkSource().randomState());

                            if ((world_surface1 == ocean_floor1) && (world_surface2 == ocean_floor2) && (world_surface3 == ocean_floor3) && (world_surface4 == ocean_floor4)) {

                                return;

                            }

                        }

                    }

                }

            }

            String tree_type = "";
            int start_height = 0;
            int dead_tree_level = 0;
            int original_height = center_posY;

            // Scan "Tree Settings" File
            {

                File file = new File(Handcode.directory_config + "/custom_packs/" + tree_settings);

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

                        } buffered_reader.close(); } catch (Exception e) { e.printStackTrace(); }

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

                } buffered_reader.close(); } catch (Exception e) { e.printStackTrace(); }

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

                if (center_posY + sizeY > level.getMaxBuildHeight()) {

                    return;

                }

            }

            // Dead Tree
            {

                boolean dead_tree = false;

                if (Math.random() < dead_tree_chance) {

                    dead_tree = true;

                } else {

                    int highest_point = chunk_generator.getBaseHeight(center_posX, center_posZ, Heightmap.Types.WORLD_SURFACE_WG, level, world.getChunkSource().randomState());

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

                // Affect
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

            // Calculation what chunks have this tree + test exist chunks
            {

                int pointX = center_posX - center_sizeX;
                int pointZ = center_posZ - center_sizeZ;

                if (true
                    //(center_posX > pointX && center_posZ > pointZ)
                    //&&
                    //(center_posX < center_posX + center_sizeX && center_posZ < center_posZ + center_sizeZ)
                ) {

                    for (int scanX = pointX >> 4; scanX <= Math.nextUp((pointX + sizeX) / 16.0); scanX++) {

                        for (int scanZ = pointZ >> 4; scanZ <= Math.nextUp((pointZ + sizeZ) / 16.0); scanZ++) {

                            if (level.hasChunk(scanX, scanZ) == true && level.getChunk(scanX, scanZ).getStatus().isOrAfter(ChunkStatus.FEATURES) == true) {

                                return;

                            }

                        }

                    }

                }

                writeLocationFile(id, center_posX, center_posY, center_posZ);
                String other_data = original_height + "/" + ground_block + "/" + dead_tree_level;
                writePlaceFile(pointX >> 4, pointZ >> 4, (pointX + sizeX) >> 4, (pointZ + sizeZ) >> 4, id, chosen, center_posX, center_posY, center_posZ, rotation, mirrored, other_data);

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

    private static void writePlaceFile(int from_chunk_posX, int from_chunk_posZ, int to_chunk_posX, int to_chunk_posZ, String id, File chosen, int center_posX, int center_posY, int center_posZ, String rotation, String mirrored, String other_data) {

        StringBuilder write = new StringBuilder();

        {

            write.append(from_chunk_posX + "/" + from_chunk_posZ + "|" + to_chunk_posX + "/" + to_chunk_posZ + "|" + id + "|" + chosen.getName() + "|" + center_posX + "/" + center_posY + "/" + center_posZ + "|" + rotation + "/" + mirrored + "|" + other_data);
            write.append("\n");

        }

        for (int scanX = from_chunk_posX >> 5; scanX <= Math.nextUp(to_chunk_posX / 32.0); scanX++) {

            for (int scanZ = from_chunk_posZ >> 5; scanZ <= Math.nextUp(to_chunk_posZ / 32.0); scanZ++) {

                FileManager.writeTXT(Handcode.directory_world_data + "/place/" + scanX + "," + scanZ + ".txt", write.toString(), true);

            }

        }

    }

}