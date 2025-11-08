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
import tannyjung.core.Utils;
import tannyjung.tanshugetrees.TanshugetreesMod;
import tannyjung.tanshugetrees_handcode.Handcode;
import tannyjung.tanshugetrees_handcode.config.ConfigMain;
import tannyjung.tanshugetrees_handcode.systems.Cache;

import java.io.*;
import java.util.*;

public class TreeLocation {

    private static final Map<String, List<String>> cache_write_tree_location = new HashMap<>();
    private static final Map<String, StringBuilder> cache_write_place = new HashMap<>();
    private static final Map<String, String> cache_dead_tree_auto_level = new HashMap<>();
    private static final Map<String, Boolean> cache_biome_test = new HashMap<>();
    public static int world_gen_overlay_animation = 0;
    public static int world_gen_overlay_bar = 0;
    public static String world_gen_overlay_details_biome = "";
    public static String world_gen_overlay_details_tree = "";

    public static void start (LevelAccessor level_accessor, String dimension, ChunkPos chunk_pos) {

        int region_posX = chunk_pos.x >> 5;
        int region_posZ = chunk_pos.z >> 5;
        File file_region = new File(Handcode.path_world_data + "/world_gen/regions/" + dimension + "/" + region_posX + "," + region_posZ + ".bin");

        if (file_region.exists() == false) {

            FileManager.writeBIN(file_region.getPath(), new ArrayList<>(), false);
            File file = new File(Handcode.path_config + "/config_world_gen.txt");

            if (file.exists() == true && file.isDirectory() == false) {

                TanshugetreesMod.LOGGER.info("Generating tree locations for a new region ({} -> {}/{})", dimension.replace("-", ":"), region_posX, region_posZ);
                world_gen_overlay_animation = 4;
                world_gen_overlay_bar = 0;
                scanning_overlay_loop();

                // Region Scanning
                {

                    String[] config_world_gen = FileManager.readTXT(file.getPath());

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

                    StringBuilder write = new StringBuilder();

                    for (Map.Entry<String, List<String>> entry : cache_write_tree_location.entrySet()) {

                        write = new StringBuilder();

                        for (String read_all : entry.getValue()) {

                            write.append(read_all).append("\n");

                        }

                        FileManager.writeTXT(Handcode.path_world_data + "/world_gen/tree_locations/" + dimension + "/" + entry.getKey() + ".txt", write.toString(), true);

                    }

                }

                // Write Place
                {

                    for (Map.Entry<String, StringBuilder> entry : cache_write_place.entrySet()) {


                        FileManager.writeTXT(Handcode.path_world_data + "/world_gen/place/" + dimension + "/" + entry.getKey() + ".txt", entry.getValue().toString(), true);

                    }

                }

            }

            cache_write_tree_location.clear();
            cache_write_place.clear();
            cache_dead_tree_auto_level.clear();
            cache_biome_test.clear();

        }

    }

    private static void scanning_overlay_loop () {

        TanshugetreesMod.queueServerWork(20, () -> {

            scanning_overlay_loop();

        });

        if (world_gen_overlay_animation != 0) {

            if (world_gen_overlay_animation < 4) {

                world_gen_overlay_animation = world_gen_overlay_animation + 1;

            } else {

                world_gen_overlay_animation = 1;

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
        boolean world_gen = false;
        String biome = "";
        String ground_block = "";
        double rarity = 0.0;
        int min_distance = 0;
        String group_size = "";
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
                                    world_gen_overlay_details_biome = Utils.biome.toID(biome_center);

                                }

                            }

                        } else {

                            if (skip == false) {

                                if (read_all.startsWith("world_gen = ") == true) {

                                    world_gen = Boolean.parseBoolean(read_all.replace("world_gen = ", ""));

                                } else if (read_all.startsWith("biome = ") == true) {

                                    biome = read_all.replace("biome = ", "");

                                } else if (read_all.startsWith("ground_block = ") == true) {

                                    ground_block = read_all.replace("ground_block = ", "");

                                } else if (read_all.startsWith("rarity = ") == true) {

                                    rarity = Double.parseDouble(read_all.replace("rarity = ", ""));

                                } else if (read_all.startsWith("min_distance = ") == true) {

                                    min_distance = Integer.parseInt(read_all.replace("min_distance = ", ""));

                                } else if (read_all.startsWith("group_size = ") == true) {

                                    group_size = read_all.replace("group_size = ", "");

                                } else if (read_all.startsWith("waterside_chance = ") == true) {

                                    waterside_chance = Double.parseDouble(read_all.replace("waterside_chance = ", "")) * ConfigMain.multiply_waterside_chance;

                                } else if (read_all.startsWith("dead_tree_chance = ") == true) {

                                    dead_tree_chance = Double.parseDouble(read_all.replace("dead_tree_chance = ", "")) * ConfigMain.multiply_dead_tree_chance;

                                } else if (read_all.startsWith("dead_tree_level = ") == true) {

                                    dead_tree_level = read_all.replace("dead_tree_level = ", "");

                                } else if (read_all.startsWith("start_height_offset = ") == true) {

                                    start_height_offset = read_all.replace("start_height_offset = ", "");

                                } else if (read_all.startsWith("rotation = ") == true) {

                                    rotation = read_all.replace("rotation = ", "");

                                } else if (read_all.startsWith("mirrored = ") == true) {

                                    mirrored = read_all.replace("mirrored = ", "");

                                    // End of ID
                                    {

                                        world_gen_overlay_details_tree = id;
                                        int group_size_get = 0;

                                        // Test All Data
                                        {

                                            if (world_gen == false) {

                                                continue;

                                            }

                                            // Rarity
                                            {

                                                rarity = (rarity * 0.01) * ConfigMain.multiply_rarity;

                                                if (Math.random() >= rarity) {

                                                    continue;

                                                }

                                            }

                                            // Biome
                                            {

                                                if (cache_biome_test.containsKey(Utils.biome.toID(biome_center) + "|" + id) == true) {

                                                    if (cache_biome_test.get(Utils.biome.toID(biome_center) + "|" + id) == false) {

                                                        continue;

                                                    }

                                                } else {

                                                    boolean result = Utils.outside.configTestBiome(biome_center, biome);
                                                    cache_biome_test.put(Utils.biome.toID(biome_center) + "|" + id, result);

                                                    if (result == false) {

                                                        continue;

                                                    }

                                                }

                                            }

                                            // Min Distance
                                            {

                                                min_distance = (int) Math.ceil(min_distance * ConfigMain.multiply_min_distance);

                                                if (min_distance > 0) {

                                                    if (testDistance(dimension, id, center_posX, center_posZ, min_distance) == false) {

                                                        continue;

                                                    }

                                                }

                                            }

                                            // Group Size
                                            {

                                                String[] get = group_size.split(" <> ");
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

                                                group_size_get = Mth.nextInt(RandomSource.create(), min, max);

                                            }

                                        }

                                        // Waterside Detection
                                        {

                                            if (testWaterSide(level_accessor, center_posX, center_posZ, waterside_chance) == false) {

                                                return;

                                            }

                                        }

                                        writeData(level_accessor, center_posX, center_posZ, id, ground_block, start_height_offset, rotation, mirrored, dead_tree_chance, dead_tree_level);

                                        // Group Spawning
                                        {

                                            if (group_size_get > 1) {

                                                while (group_size_get > 0) {

                                                    group_size_get = group_size_get - 1;
                                                    center_posX = center_posX + Mth.nextInt(RandomSource.create(), -(min_distance + 1), (min_distance + 1));
                                                    center_posZ = center_posZ + Mth.nextInt(RandomSource.create(), -(min_distance + 1), (min_distance + 1));

                                                    // Biome
                                                    {

                                                        biome_center = level_accessor.getBiome(new BlockPos(center_posX, level_accessor.getMaxBuildHeight(), center_posZ));

                                                        if (Utils.outside.configTestBiome(biome_center, biome) == false) {

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

                                                    writeData(level_accessor, center_posX, center_posZ, id, ground_block, start_height_offset, rotation, mirrored, dead_tree_chance, dead_tree_level);

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

        int center_chunkX = center_posX >> 4;
        int center_chunkZ = center_posZ >> 4;
        int center_regionX = center_chunkX >> 5;
        int center_regionZ = center_chunkZ >> 5;
        int testX = 0;
        int testZ = 0;
        int scanX = 0;
        int scanZ = 0;
        int step = 0;

        String[] get = null;
        String[] pos = null;
        int posX = 0;
        int posZ = 0;

        // Go next radius of scan size
        for (int distance = 0; distance <= (int) Math.ceil((min_distance / 16.0) / 32.0) * 32.0; distance = distance + 32) {

            step = 1;
            scanX = 0;
            scanZ = 0;

            // Go around like spiral
            while (true) {

                testX = ((center_chunkX - distance) + scanX) >> 5;
                testZ = ((center_chunkZ - distance) + scanZ) >> 5;

                // Read Data
                {

                    if (center_regionX == testX && center_regionZ == testZ) {

                        // Current Region
                        {

                            for (String read_all : cache_write_tree_location.getOrDefault(testX + "," + testZ, new ArrayList<>())) {

                                if (read_all.startsWith(id + "|") == true) {

                                    get = read_all.split("\\|");
                                    pos = get[1].split("/");
                                    posX = Integer.parseInt(pos[0]);
                                    posZ = Integer.parseInt(pos[1]);

                                }

                                if ((Math.abs(center_posX - posX) <= min_distance) && (Math.abs(center_posZ - posZ) <= min_distance)) {

                                    return false;

                                }

                            }

                        }

                    } else {

                        // Outside Region (Classic Testing)
                        {

                            for (String read_all : Cache.tree_location(dimension + "/" + testX + "," + testZ)) {

                                {

                                    if (read_all.startsWith(id + "|") == true) {

                                        get = read_all.split("\\|");
                                        pos = get[1].split("/");
                                        posX = Integer.parseInt(pos[0]);
                                        posZ = Integer.parseInt(pos[1]);

                                        if ((Math.abs(center_posX - posX) <= min_distance) && (Math.abs(center_posZ - posZ) <= min_distance)) {

                                            return false;

                                        }

                                    }

                                }

                            }

                        }

                    }

                }

                // Steps
                {

                    if (step == 1) {

                        scanX = scanX + 32;

                        if (scanX > (distance * 2)) {

                            step = 2;

                        }

                    } else if (step == 2) {

                        scanZ = scanZ + 32;

                        if (scanZ > (distance * 2)) {

                            step = 3;

                        }

                    } else if (step == 3) {

                        scanX = scanX - 32;

                        if (scanX <= 0) {

                            step = 4;

                        }

                    } else {

                        scanZ = scanZ - 32;

                        if (scanZ <= 0) {

                            break;

                        }

                    }

                }

            }

        }

        return true;

    }

    private static boolean testWaterSide (LevelAccessor level_accessor, int center_posX, int center_posZ, double waterside_chance) {

        boolean return_logic = true;

        {

            if (waterside_chance > 0) {

                if (ConfigMain.waterside_detection == false) {

                    return_logic = false;

                } else {

                    if (Math.random() < waterside_chance) {

                        boolean on_land = Utils.biome.isTaggedAs(level_accessor.getBiome(new BlockPos(center_posX, level_accessor.getMaxBuildHeight(), center_posZ)), "forge:is_water") == false;
                        int size = ConfigMain.surface_detection_size;
                        boolean waterside_test1 = Utils.biome.isTaggedAs(level_accessor.getBiome(new BlockPos(center_posX + size, level_accessor.getMaxBuildHeight(), center_posZ + size)), "forge:is_water");
                        boolean waterside_test2 = Utils.biome.isTaggedAs(level_accessor.getBiome(new BlockPos(center_posX + size, level_accessor.getMaxBuildHeight(), center_posZ - size)), "forge:is_water");
                        boolean waterside_test3 = Utils.biome.isTaggedAs(level_accessor.getBiome(new BlockPos(center_posX - size, level_accessor.getMaxBuildHeight(), center_posZ + size)), "forge:is_water");
                        boolean waterside_test4 = Utils.biome.isTaggedAs(level_accessor.getBiome(new BlockPos(center_posX - size, level_accessor.getMaxBuildHeight(), center_posZ - size)), "forge:is_water");

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

    private static void writeData (LevelAccessor level_accessor, int center_posX, int center_posZ, String id, String ground_block, String start_height_offset, String rotation, String mirrored, double dead_tree_chance, String dead_tree_level) {

        String path_storage = "";

        // Scan World Gen File
        {
            
            for (String read_all : Cache.world_gen_settings(id)) {
                
                {

                    if (read_all.startsWith("path_storage = ") == true) {

                        path_storage = read_all.replace("path_storage = ", "");
                        break;

                    }

                }

            }

        }

        File chosen = null;

        // Random Select File
        {

            String storage = Handcode.path_config + "/custom_packs/" + path_storage.replace("/", "/presets/") + "/storage";
            File[] list = new File(storage).listFiles();

            if (list != null) {

                chosen = new File(storage + "/" + list[(int) (Math.random() * list.length)].getName());

            }

        }

        if (chosen.exists() == true && chosen.isDirectory() == false) {

            short[] get = Cache.tree_shape(path_storage + "/" + chosen.getName(), 1);
            int sizeX = get[0];
            int sizeY = get[1];
            int sizeZ = get[2];
            int center_sizeX = get[3];
            int center_sizeY = get[4];
            int center_sizeZ = get[5];
            int count_trunk = get[6];
            int count_bough = get[6];
            int count_branch = get[7];
            int count_limb = get[8];
            int count_twig = get[9];
            int count_sprig = get[10];

            // Dead Tree
            {

                if (Math.random() >= dead_tree_chance) {

                    dead_tree_level = "0";

                } else {

                    if (dead_tree_level.startsWith("auto") == true) {

                        StringBuilder write = new StringBuilder();

                        if (cache_dead_tree_auto_level.containsKey(id) == true) {

                            write.append(cache_dead_tree_auto_level.get(id));

                        } else {

                            String is_pine = "0";

                            if (dead_tree_level.endsWith("pine") == true) {

                                is_pine = "1";

                            }

                            // Write Data
                            {

                                if (count_trunk > 0) {

                                    write.append("180 / 190 / 280 / 290");

                                }

                                if (count_bough > 0) {

                                    if (dead_tree_level.equals("") == false) {

                                        write.append(" / 160 / 170 / 260 / 270 / ");

                                    }

                                    write.append("1").append("5").append(is_pine).append(" / ").append("2").append("5").append(is_pine);

                                }

                                if (count_branch > 0) {

                                    if (dead_tree_level.equals("") == false) {

                                        write.append(" / ");

                                    }

                                    write.append("1").append("4").append(is_pine).append(" / ").append("2").append("4").append(is_pine);

                                }

                                if (count_limb > 0) {

                                    if (dead_tree_level.equals("") == false) {

                                        write.append(" / ");

                                    }

                                    write.append("1").append("3").append(is_pine).append(" / ").append("2").append("3").append(is_pine);

                                }

                                if (count_twig > 0) {

                                    if (dead_tree_level.equals("") == false) {

                                        write.append(" / ");

                                    }

                                    write.append("1").append("2").append(is_pine).append(" / ").append("2").append("2").append(is_pine);

                                }

                                if (count_sprig > 0) {

                                    if (dead_tree_level.equals("") == false) {

                                        write.append(" / ");

                                    }

                                    write.append("1").append("1").append(is_pine).append(" / ").append("2").append("1").append(is_pine);

                                }

                            }

                        }

                        dead_tree_level = write.toString();

                    }

                    String[] split = dead_tree_level.split(" / ");
                    dead_tree_level = split[(int) (Math.random() * (split.length - 1))];

                }

            }

            int start_height_offset_get = 0;

            // Height Offset
            {

                String[] offset_get = start_height_offset.split(" <> ");
                start_height_offset_get = Mth.nextInt(RandomSource.create(), Integer.parseInt(offset_get[0]), Integer.parseInt(offset_get[1]));

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

            // Coarse Woody Debris
            {

                if (dead_tree_level.startsWith("2") == true) {

                    int fall_direction = Mth.nextInt(RandomSource.create(center_posX * center_posZ), 1, 4);
                    int sizeX_save = sizeX;
                    int sizeY_save = sizeY;
                    int sizeZ_save = sizeZ;
                    int center_sizeX_save = center_sizeX;
                    int center_sizeY_save = center_sizeY;
                    int center_sizeZ_save = center_sizeZ;

                    if (fall_direction == 1) {

                        sizeY = sizeX_save;
                        sizeX = sizeY_save;
                        center_sizeY = sizeX_save - center_sizeX_save;
                        center_sizeX = center_sizeY_save;

                    } else if (fall_direction == 2) {

                        sizeY = sizeZ_save;
                        sizeZ = sizeY_save;
                        center_sizeY = sizeZ_save - center_sizeZ_save;
                        center_sizeZ = center_sizeY_save;

                    } else if (fall_direction == 3) {

                        sizeY = sizeX_save;
                        sizeX = sizeY_save;
                        center_sizeY = center_sizeX_save;
                        center_sizeX = sizeY_save - center_sizeY_save;

                    } else if (fall_direction == 4) {

                        sizeY = sizeZ_save;
                        sizeZ = sizeY_save;
                        center_sizeY = center_sizeZ_save;
                        center_sizeZ = sizeY_save - center_sizeY_save;

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

            int regionX = center_posX >> 9;
            int regionZ = center_posZ >> 9;
            String key = regionX + "," + regionZ;
            String value = id + "|" + center_posX + "/" + center_posZ;
            cache_write_tree_location.computeIfAbsent(key, test -> new ArrayList<>()).add(value);

            // Write Place
            {

                String data = from_chunkX + "/" + from_chunkZ + "/" + to_chunkX + "/" + to_chunkZ + "|" + id + "|" + chosen.getName() + "|" + center_posX + "/" + center_posZ + "|" + rotation + "/" + mirrored + "|" + start_height_offset_get + "|" + (sizeY - center_sizeY) + "|" + ground_block + "|" + dead_tree_level;
                int from_chunkX_test = from_chunkX >> 5;
                int from_chunkZ_test = from_chunkZ >> 5;
                int to_chunkX_test = to_chunkX >> 5;
                int to_chunkZ_test = to_chunkZ >> 5;

                for (int scanX = from_chunkX_test; scanX <= to_chunkX_test; scanX++) {

                    for (int scanZ = from_chunkZ_test; scanZ <= to_chunkZ_test; scanZ++) {

                        cache_write_place.computeIfAbsent(scanX + "," + scanZ, test -> new StringBuilder()).append(data).append("\n");

                    }

                }

            }

        }

    }

}