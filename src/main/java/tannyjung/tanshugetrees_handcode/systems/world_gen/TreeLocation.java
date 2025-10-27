package tannyjung.tanshugetrees_handcode.systems.world_gen;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.*;
import tannyjung.core.FileManager;
import tannyjung.core.OutsideUtils;
import tannyjung.core.GameUtils;
import tannyjung.tanshugetrees.TanshugetreesMod;
import tannyjung.tanshugetrees_handcode.Handcode;
import tannyjung.tanshugetrees_handcode.config.ConfigMain;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;

public class TreeLocation {

    private static final Map<String, List<String>> cache_write_tree_location = new HashMap<>();
    private static final Map<String, String> cache_write_place = new HashMap<>();
    public static int world_gen_overlay_animation = 0;
    public static int world_gen_overlay_bar = 0;
    public static String world_gen_overlay_details_biome = "";
    public static String world_gen_overlay_details_tree = "";

    public static void start (LevelAccessor level_accessor, String dimension, ChunkPos chunk_pos) {

        int region_posX = chunk_pos.x >> 5;
        int region_posZ = chunk_pos.z >> 5;
        File region_folder = new File(Handcode.directory_world_data + "/world_gen/regions/" + dimension + "/" + region_posX + "," + region_posZ);

        if (region_folder.exists() == false) {

            FileManager.createFolder(region_folder.toPath().toString());
            File file = new File(Handcode.directory_config + "/config_world_gen.txt");

            if (file.exists() == true && file.isDirectory() == false) {

                TanshugetreesMod.LOGGER.info("Generating tree locations for a new region ({} -> {}/{})", dimension.replace("-", ":"), region_posX, region_posZ);
                String[] config_world_gen = FileManager.readTXT(file.getPath());

                // Overlay Loading Loop
                {

                    world_gen_overlay_animation = 4;
                    world_gen_overlay_bar = 0;

                    Handcode.thread.submit(() -> {

                        while (world_gen_overlay_animation != 0) {

                            if (world_gen_overlay_animation < 4) {

                                world_gen_overlay_animation = world_gen_overlay_animation + 1;

                            } else {

                                world_gen_overlay_animation = 1;

                            }

                            try {

                                Thread.sleep(1000);

                            } catch (InterruptedException exception) {

                                OutsideUtils.exception(new Exception(), exception);

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

                                getData(level_accessor, dimension, (region_posX * 32) + scanX, (region_posZ * 32) + scanZ, config_world_gen);

                            }

                        }

                    }

                }

                world_gen_overlay_animation = 0;
                TanshugetreesMod.LOGGER.info("Completed!");

                // Write Tree Location
                {

                    String folder_directory = Handcode.directory_world_data + "/world_gen/tree_locations/" + dimension + "/" + region_posX + "," + region_posZ;
                    StringBuilder write = new StringBuilder();

                    for (Map.Entry<String, List<String>> entry : cache_write_tree_location.entrySet()) {

                        for (String read_all : entry.getValue()) {

                            write.append(read_all).append("\n");

                        }

                        FileManager.writeTXT(folder_directory + "/" + entry.getKey() + ".txt", write.toString(), true);

                    }

                    cache_write_tree_location.clear();

                }

                // Write Place
                {

                    for (Map.Entry<String, String> entry : cache_write_place.entrySet()) {

                        FileManager.writeTXT(Handcode.directory_world_data + "/world_gen/place/" + dimension + "/" + entry.getKey() + ".txt", entry.getValue(), true);

                    }

                    cache_write_place.clear();

                }

            }

        }

    }

    private static void getData (LevelAccessor level_accessor, String dimension, int chunk_posX, int chunk_posZ, String[] config_world_gen) {

        boolean start_test = false;
        boolean skip = true;
        int center_posX = 0;
        int center_posZ = 0;
        Holder<Biome> biome_center = null;

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

        world_gen_overlay_details_tree = "No Matching";

        for (String read_all : config_world_gen) {

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

                                    skip = false;
                                    id = read_all.substring(read_all.indexOf("]") + 2).replace(" > ", "/");
                                    center_posX = (chunk_posX * 16) + (int) (Math.random() * 16);
                                    center_posZ = (chunk_posZ * 16) + (int) (Math.random() * 16);
                                    biome_center = level_accessor.getBiome(new BlockPos(center_posX, level_accessor.getMaxBuildHeight(), center_posZ));

                                    world_gen_overlay_details_biome = GameUtils.biome.toID(biome_center);

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

                                        if (GameUtils.outside.configTestBiome(biome_center, biome) == false) {

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

                                        world_gen_overlay_details_tree = id;

                                        // Waterside Detection
                                        {

                                            if (testWaterSide(level_accessor, center_posX, center_posZ, waterside_chance) == false) {

                                                return;

                                            }

                                        }

                                        String tree_data = id + "|" + ground_block + "|" + start_height_offset + "|" + rotation + "|" + mirrored + "|" + dead_tree_chance + "|" + dead_tree_level;
                                        readTreeFile(level_accessor, tree_data, center_posX, center_posZ);

                                        // Group Spawning
                                        {

                                            if (group_size > 1) {

                                                while (group_size > 0) {

                                                    group_size = group_size - 1;
                                                    center_posX = center_posX + Mth.nextInt(RandomSource.create(), -(min_distance + 1), (min_distance + 1));
                                                    center_posZ = center_posZ + Mth.nextInt(RandomSource.create(), -(min_distance + 1), (min_distance + 1));

                                                    // Biome
                                                    {

                                                        biome_center = level_accessor.getBiome(new BlockPos(center_posX, level_accessor.getMaxBuildHeight(), center_posZ));

                                                        if (GameUtils.outside.configTestBiome(biome_center, biome) == false) {

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

                                                    readTreeFile(level_accessor, tree_data, center_posX, center_posZ);

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

    private static boolean testDistance (String dimension, String id, int center_posX, int center_posZ, int min_distance) {

        boolean return_logic = true;

        {

            test:
            {

                File file = null;
                int scan_size = 32 >> 2;
                int center_chunk_posX = center_posX >> 4;
                int center_chunk_posZ = center_posZ >> 4;
                int center_region_posX = center_chunk_posX >> 5;
                int center_region_posZ = center_chunk_posZ >> 5;

                int start_chunk_posX = 0;
                int start_chunk_posZ = 0;
                int chunk_posX = 0;
                int chunk_posZ = 0;
                int scanX = 0;
                int scanZ = 0;
                String node = "";
                int step = 0;

                String[] get = null;
                String[] pos = null;
                int posX = 0;
                int posZ = 0;
                List<String> get_cache = new ArrayList<>();

                // Go next radius of scan size
                for (int distance = 0; distance <= (int) Math.ceil((min_distance / 16.0) / (double) scan_size) * scan_size; distance = distance + scan_size) {

                    start_chunk_posX = center_chunk_posX - distance;
                    start_chunk_posZ = center_chunk_posZ - distance;
                    step = 1;
                    scanX = 0;
                    scanZ = 0;

                    // Go around like spiral
                    while (true) {

                        chunk_posX = start_chunk_posX + scanX;
                        chunk_posZ = start_chunk_posZ + scanZ;

                        // Read Data
                        {

                            node = GameUtils.outside.quardtreeChunkToNode(chunk_posX, chunk_posZ);

                            if (center_region_posX == chunk_posX >> 5 && center_region_posZ == chunk_posZ >> 5) {

                                // Current Region
                                {

                                    if (cache_write_tree_location.containsKey(node) == true) {

                                        get_cache = cache_write_tree_location.get(node);

                                        for (String read_all : get_cache) {

                                            if (read_all.startsWith(id + "|") == true) {

                                                get = read_all.split("\\|");
                                                pos = get[1].split("/");
                                                posX = Integer.parseInt(pos[0]);
                                                posZ = Integer.parseInt(pos[1]);

                                            }

                                            if ((Math.abs(center_posX - posX) <= min_distance) && (Math.abs(center_posZ - posZ) <= min_distance)) {

                                                return_logic = false;
                                                break test;

                                            }

                                        }

                                    }

                                }

                            } else {

                                // Outside Region (Classic Testing)
                                {

                                    file = new File(Handcode.directory_world_data + "/world_gen/tree_locations/" + dimension + "/" + (chunk_posX >> 5) + "," + (chunk_posZ >> 5) + "/" + node + ".txt");

                                    if (file.exists() == true && file.isDirectory() == false) {

                                        {

                                            try { BufferedReader buffered_reader = new BufferedReader(new FileReader(file), 65536); String read_all = ""; while ((read_all = buffered_reader.readLine()) != null) {

                                                {

                                                    if (read_all.startsWith(id + "|") == true) {

                                                        get = read_all.split("\\|");
                                                        pos = get[1].split("/");
                                                        posX = Integer.parseInt(pos[0]);
                                                        posZ = Integer.parseInt(pos[1]);

                                                        if ((Math.abs(center_posX - posX) <= min_distance) && (Math.abs(center_posZ - posZ) <= min_distance)) {

                                                            return_logic = false;
                                                            break test;

                                                        }

                                                    }

                                                }

                                            } buffered_reader.close(); } catch (Exception exception) { OutsideUtils.exception(new Exception(), exception); }

                                        }

                                    }

                                }

                            }

                        }

                        // Steps
                        {

                            if (step == 1) {

                                scanX = scanX + scan_size;

                                if (scanX > (distance * 2)) {

                                    step = 2;

                                }

                            } else if (step == 2) {

                                scanZ = scanZ + scan_size;

                                if (scanZ > (distance * 2)) {

                                    step = 3;

                                }

                            } else if (step == 3) {

                                scanX = scanX - scan_size;

                                if (scanX <= 0) {

                                    step = 4;

                                }

                            } else {

                                scanZ = scanZ - scan_size;

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

    private static boolean testWaterSide (LevelAccessor level_accessor, int center_posX, int center_posZ, double waterside_chance) {

        boolean return_logic = true;

        {

            if (waterside_chance > 0) {

                if (ConfigMain.waterside_detection == false) {

                    return_logic = false;

                } else {

                    if (Math.random() < waterside_chance) {

                        boolean on_land = GameUtils.biome.isTaggedAs(level_accessor.getBiome(new BlockPos(center_posX, level_accessor.getMaxBuildHeight(), center_posZ)), "forge:is_water") == false;
                        int size = ConfigMain.surface_detection_size;
                        boolean waterside_test1 = GameUtils.biome.isTaggedAs(level_accessor.getBiome(new BlockPos(center_posX + size, level_accessor.getMaxBuildHeight(), center_posZ + size)), "forge:is_water");
                        boolean waterside_test2 = GameUtils.biome.isTaggedAs(level_accessor.getBiome(new BlockPos(center_posX + size, level_accessor.getMaxBuildHeight(), center_posZ - size)), "forge:is_water");
                        boolean waterside_test3 = GameUtils.biome.isTaggedAs(level_accessor.getBiome(new BlockPos(center_posX - size, level_accessor.getMaxBuildHeight(), center_posZ + size)), "forge:is_water");
                        boolean waterside_test4 = GameUtils.biome.isTaggedAs(level_accessor.getBiome(new BlockPos(center_posX - size, level_accessor.getMaxBuildHeight(), center_posZ - size)), "forge:is_water");

                        if (on_land == true) {

                            if (waterside_test1 == false && waterside_test2 == false && waterside_test3 == false && waterside_test4 == false) {

                                return_logic = false;

                            }

                        } else {

                            if (waterside_test1 == true && waterside_test2 == true && waterside_test3 == true && waterside_test4 == true) {

                                return_logic = false;

                            }

                        }

                    }

                }

            }

        }

        return return_logic;

    }

    private static void readTreeFile (LevelAccessor level_accessor, String tree_data, int center_posX, int center_posZ) {

        String id = "";
        String ground_block = "";
        String start_height_offset = "";
        String rotation = "";
        String mirrored = "";
        double dead_tree_chance = 0.0;
        String[] dead_tree_levels = new String[0];

        try {

            String[] get = tree_data.split("\\|");

            id = get[0];
            ground_block = get[1];
            start_height_offset = get[2];
            rotation = get[3];
            mirrored = get[4];
            dead_tree_chance = Double.parseDouble(get[5]);
            dead_tree_levels = get[6].split(" / ");

        } catch (Exception ignored) {

            return;

        }

        String path_storage = "";
        String path_tree_settings = "";

        // Scan "World Gen" File
        {

            File file = new File(Handcode.directory_config + "/#dev/custom_packs_organized/world_gen/" + id + ".txt");

            if (file.exists() == true && file.isDirectory() == false) {

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

        File chosen = null;

        // Random Select File
        {

            String storage = Handcode.directory_config + "/custom_packs/" + path_storage.replace("/", "/presets/") + "/storage";
            File[] list = new File(storage).listFiles();

            if (list != null) {

                chosen = new File(storage + "/" + list[(int) (Math.random() * list.length)].getName());

            }

        }

        if (chosen.exists() == true && chosen.isDirectory() == false) {

            short[] get = FileManager.readBIN(chosen.getPath(), 1, 6);
            int sizeX = get[0];
            int sizeY = get[1];
            int sizeZ = get[2];
            int center_sizeX = get[3];
            int center_sizeY = get[4];
            int center_sizeZ = get[5];

            sizeY = sizeY - center_sizeY;
            String tree_type = "";
            int start_height = 0;
            int dead_tree_level = 0;

            // Scan Tree Settings File
            {

                File file = new File(Handcode.directory_config + "/#dev/custom_packs_organized/presets/" + path_tree_settings + "_settings.txt");

                if (file.exists() == true && file.isDirectory() == false) {

                    {

                        try { BufferedReader buffered_reader = new BufferedReader(new FileReader(file), 65536); String read_all = ""; while ((read_all = buffered_reader.readLine()) != null) {

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

                        } buffered_reader.close(); } catch (Exception exception) { OutsideUtils.exception(new Exception(), exception); }

                    }

                }

            }

            // Random Start Height
            {

                String[] offset_get = start_height_offset.split(" <> ");
                int offset = Mth.nextInt(RandomSource.create(), Integer.parseInt(offset_get[0]), Integer.parseInt(offset_get[1]));
                start_height = start_height + offset;

            }

            // Dead Tree Level
            {

                dead_tree_level = Integer.parseInt(dead_tree_levels[(int) (Math.random() * (dead_tree_levels.length - 1))]);

                if (dead_tree_level > 0) {

                    if (dead_tree_level <= 5) {

                        dead_tree_level = dead_tree_level * 10;

                    } else if (dead_tree_level < 10) {

                        dead_tree_level = dead_tree_level * 10;

                    } else {

                        dead_tree_level = dead_tree_level + 1;

                    }

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

            int from_chunkX = center_posX - center_sizeX;
            int from_chunkZ = center_posZ - center_sizeZ;
            int to_chunkX = (from_chunkX + sizeX) >> 4;
            int to_chunkZ = (from_chunkZ + sizeZ) >> 4;
            from_chunkX = from_chunkX >> 4;
            from_chunkZ = from_chunkZ >> 4;

            // Test Exist Chunk
            {

                int scan_fromX = from_chunkX - 4;
                int scan_fromZ = from_chunkZ - 4;
                int scan_toX = to_chunkX + 4;
                int scan_toZ = to_chunkZ + 4;

                for (int scanX = scan_fromX ; scanX <= scan_toX; scanX++) {

                    for (int scanZ = scan_fromZ; scanZ <= scan_toZ; scanZ++) {

                        if (level_accessor.getChunk(scanX, scanZ, ChunkStatus.FEATURES, false) != null) {

                            return;

                        }

                    }

                }

            }

            String other_data = tree_type + "/" + start_height + "/" + sizeY + "/" + ground_block + "/" + dead_tree_chance + "/" + dead_tree_level;

            // Write Tree Location
            {

                String node = GameUtils.outside.quardtreeChunkToNode((center_posX >> 4), (center_posZ >> 4));
                cache_write_tree_location.computeIfAbsent(node, test -> new ArrayList<>()).add(id + "|" + center_posX + "/" + center_posZ);

            }

            // Write Place
            {

                String data = from_chunkX + "/" + from_chunkZ + "/" + to_chunkX + "/" + to_chunkZ + "|" + id + "|" + chosen.getName() + "|" + center_posX + "/" + center_posZ + "|" + rotation + "/" + mirrored + "|" + other_data + "\n";
                int size = 32 >> 2;
                int from_chunkX_test = (int) (Math.floor((double) from_chunkX / (double) size) * (double) size);
                int from_chunkZ_test = (int) (Math.floor((double) from_chunkZ / (double) size) * (double) size);
                int to_chunkX_test = (int) (Math.floor((double) to_chunkX / (double) size) * (double) size);
                int to_chunkZ_test = (int) (Math.floor((double) to_chunkZ / (double) size) * (double) size);
                String location = "";

                for (int scanX = from_chunkX_test; scanX <= to_chunkX_test; scanX = scanX + size) {

                    for (int scanZ = from_chunkZ_test; scanZ <= to_chunkZ_test; scanZ = scanZ + size) {

                        location = (scanX >> 5) + "," + (scanZ >> 5) + "/" + GameUtils.outside.quardtreeChunkToNode(scanX, scanZ);
                        cache_write_place.put(location, cache_write_place.getOrDefault(location, "") + data);

                    }

                }

            }

        }

    }

}