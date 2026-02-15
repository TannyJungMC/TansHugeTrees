package tannyjung.tanshugetrees_handcode.systems.world_gen;

import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.biome.Biome;
import tannyjung.tanshugetrees_core.Core;
import tannyjung.tanshugetrees_core.outside.CacheManager;
import tannyjung.tanshugetrees_core.outside.FileManager;
import tannyjung.tanshugetrees_core.outside.OutsideUtils;
import tannyjung.tanshugetrees_core.game.GameUtils;
import tannyjung.tanshugetrees_handcode.data.FileConfig;
import tannyjung.tanshugetrees_handcode.systems.Caches;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public class TreeLocation {

    private static final Map<String, List<String>> cache_write_tree_location = new HashMap<>();
    private static final Map<String, List<String>> cache_write_place = new HashMap<>();
    private static final Map<String, ByteBuffer> cache_other_region = new HashMap<>();
    private static final Map<String, List<String>> cache_dead_tree_auto_level = new HashMap<>();
    private static final Map<String, Boolean> cache_biome_test = new HashMap<>();
    public static int world_gen_overlay_animation = 0;
    public static int world_gen_overlay_bar = 0;
    public static String world_gen_overlay_details_biome = "";
    public static String world_gen_overlay_details_tree = "";

    public static void start (LevelAccessor level_accessor, ServerLevel level_server, String dimension, int chunkX, int chunkZ) {

        String[] config_world_gen = FileManager.readTXT(Core.path_config + "/config_worldgen.txt");

        if (config_world_gen.length > 0) {

            // Separate +-4 to fix tree cut-off by chunk status not fully update
            TreeLocation.run(level_accessor, level_server, dimension, chunkX + 4, chunkZ + 4, config_world_gen);
            TreeLocation.run(level_accessor, level_server, dimension, chunkX + 4, chunkZ - 4, config_world_gen);
            TreeLocation.run(level_accessor, level_server, dimension, chunkX - 4, chunkZ + 4, config_world_gen);
            TreeLocation.run(level_accessor, level_server, dimension, chunkX - 4, chunkZ - 4, config_world_gen);

            cache_dead_tree_auto_level.clear();
            cache_biome_test.clear();

        }

    }

    public static void run (LevelAccessor level_accessor, ServerLevel level_server, String dimension, int chunkX, int chunkZ, String[] config_world_gen) {

        int region_posX = chunkX >> 5;
        int region_posZ = chunkZ >> 5;
        File file_region = new File(Core.path_world_data + "/world_gen/#regions/" + dimension + "/" + region_posX + "," + region_posZ + ".bin");

        if (file_region.exists() == false) {

            FileManager.writeBIN(file_region.getPath(), new ArrayList<>(), false);

            Core.logger.info("Generating tree locations for a new region ({} -> {}/{})", dimension.replace("-", ":"), region_posX, region_posZ);
            RandomSource random = RandomSource.create(level_accessor.getServer().overworld().getSeed() ^ ((region_posX * 341873128712L) + (region_posZ * 132897987541L)));
            world_gen_overlay_animation = 4;
            world_gen_overlay_bar = 0;

            if (FileConfig.world_gen_icon == true) {

                CompletableFuture.runAsync(TreeLocation::scanning_overlay_loop);

            }

            // Region Scanning
            {

                int posX = region_posX * 32;
                int posZ = region_posZ * 32;

                for (int scanX = 0; scanX < 32; scanX++) {

                    for (int scanZ = 0; scanZ < 32; scanZ++) {

                        world_gen_overlay_bar = world_gen_overlay_bar + 1;

                        if (random.nextDouble() < FileConfig.region_scan_percent * 0.01) {

                            getData(level_accessor, level_server, dimension, random, posX + scanX, posZ + scanZ, config_world_gen);

                        }

                    }

                }

            }

            world_gen_overlay_animation = 0;
            Core.logger.info("Completed!");

            // Write File
            {

                for (Map.Entry<String, List<String>> entry : cache_write_tree_location.entrySet()) {

                    FileManager.writeBIN(Core.path_world_data + "/world_gen/tree_locations/" + dimension + "/" + entry.getKey() + ".bin", entry.getValue(), true);

                }

                for (Map.Entry<String, List<String>> entry : cache_write_place.entrySet()) {

                    FileManager.writeBIN(Core.path_world_data + "/world_gen/place/" + dimension + "/" + entry.getKey() + ".bin", entry.getValue(), true);

                }

            }

            cache_write_tree_location.clear();
            cache_write_place.clear();
            cache_other_region.clear();

        }

    }

    private static void scanning_overlay_loop () {

        if (world_gen_overlay_animation != 0) {

            if (world_gen_overlay_animation < 4) {

                world_gen_overlay_animation = world_gen_overlay_animation + 1;

            } else {

                world_gen_overlay_animation = 1;

            }

            Core.DelayedWorks.create(true, 20, TreeLocation::scanning_overlay_loop);

        }

    }

    private static void getData (LevelAccessor level_accessor, ServerLevel level_server, String dimension, RandomSource random, int chunk_posX, int chunk_posZ, String[] config_world_gen) {

        boolean start_test = false;
        boolean skip = true;
        int centerX = 0;
        int centerZ = 0;
        Holder<Biome> biome_center = null;
        String biome_center_id = "";

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

                if (read_all.isEmpty() == false) {

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
                                    centerX = (chunk_posX * 16) + random.nextInt(0, 16);
                                    centerZ = (chunk_posZ * 16) + random.nextInt(0, 16);
                                    biome_center = GameUtils.space.getBiomeAt(level_server, centerX, GameUtils.space.getBuildHeight(level_accessor, true), centerZ);
                                    biome_center_id = GameUtils.space.getBiomeID(biome_center);

                                    world_gen_overlay_details_biome = biome_center_id;

                                }

                            }

                        } else {

                            if (skip == false) {

                                if (read_all.startsWith("enable = ") == true) {

                                    world_gen = Boolean.parseBoolean(read_all.substring("enable = ".length()));

                                } else if (read_all.startsWith("biome = ") == true) {

                                    biome = read_all.substring("biome = ".length());

                                } else if (read_all.startsWith("ground_block = ") == true) {

                                    ground_block = read_all.substring("ground_block = ".length());

                                } else if (read_all.startsWith("rarity = ") == true) {

                                    rarity = Double.parseDouble(read_all.substring("rarity = ".length()));

                                } else if (read_all.startsWith("min_distance = ") == true) {

                                    min_distance = Integer.parseInt(read_all.substring("min_distance = ".length()));

                                } else if (read_all.startsWith("group_size = ") == true) {

                                    group_size = read_all.substring("group_size = ".length());

                                } else if (read_all.startsWith("waterside_chance = ") == true) {

                                    waterside_chance = Double.parseDouble(read_all.substring("waterside_chance = ".length())) * FileConfig.multiply_waterside_chance;

                                } else if (read_all.startsWith("dead_tree_chance = ") == true) {

                                    dead_tree_chance = Double.parseDouble(read_all.substring("dead_tree_chance = ".length())) * FileConfig.multiply_dead_tree_chance;

                                } else if (read_all.startsWith("dead_tree_level = ") == true) {

                                    dead_tree_level = read_all.substring("dead_tree_level = ".length());

                                } else if (read_all.startsWith("start_height_offset = ") == true) {

                                    start_height_offset = read_all.substring("start_height_offset = ".length());

                                } else if (read_all.startsWith("rotation = ") == true) {

                                    rotation = read_all.substring("rotation = ".length());

                                } else if (read_all.startsWith("mirrored = ") == true) {

                                    mirrored = read_all.substring("mirrored = ".length());

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

                                                rarity = (rarity * 0.01) * FileConfig.multiply_rarity;

                                                if (random.nextDouble() >= rarity) {

                                                    continue;

                                                }

                                            }

                                            // Min Distance
                                            {

                                                min_distance = (int) Math.ceil(min_distance * FileConfig.multiply_min_distance);

                                                if (min_distance > 0) {

                                                    if (testDistance(dimension, id, centerX, centerZ, min_distance) == false) {

                                                        continue;

                                                    }

                                                }

                                            }

                                            // Biome
                                            {

                                                String key = "|" + biome_center_id + "|" + id + "|";

                                                if (cache_biome_test.containsKey(key) == false) {

                                                    cache_biome_test.put(key, GameUtils.misc.testCustomBiome(biome_center, biome));

                                                }

                                                if (cache_biome_test.get(key) == false) {

                                                    continue;

                                                }

                                            }

                                            // Group Size
                                            {

                                                String[] get = group_size.split(" <> ");
                                                int min = Integer.parseInt(get[0]);
                                                int max = Integer.parseInt(get[1]);
                                                min = (int) Math.ceil(min * FileConfig.multiply_group_size);
                                                max = (int) Math.ceil(max * FileConfig.multiply_group_size);

                                                // Round if lower than 0
                                                {

                                                    if (min < 1) {

                                                        min = 1;

                                                    }

                                                    if (max < 1) {

                                                        max = 1;

                                                    }

                                                }

                                                group_size_get = Mth.nextInt(random, min, max);

                                            }

                                        }

                                        // Waterside Detection
                                        {

                                            if (testWaterSide(level_accessor, level_server, random, centerX, centerZ, waterside_chance) == false) {

                                                continue;

                                            }

                                        }

                                        writeData(level_accessor, random, centerX, centerZ, id, ground_block, start_height_offset, rotation, mirrored, dead_tree_chance, dead_tree_level);

                                        // Group Spawning
                                        {

                                            if (group_size_get > 1) {

                                                while (group_size_get > 0) {

                                                    group_size_get = group_size_get - 1;
                                                    centerX = centerX + random.nextInt(-(min_distance + 1), (min_distance + 1) + 1);
                                                    centerZ = centerZ + random.nextInt(-(min_distance + 1), (min_distance + 1) + 1);

                                                    // Min Distance
                                                    {

                                                        if (min_distance > 0) {

                                                            if (testDistance(dimension, id, centerX, centerZ, min_distance) == false) {

                                                                continue;

                                                            }

                                                        }

                                                    }

                                                    // Biome
                                                    {

                                                        biome_center = GameUtils.space.getBiomeAt(level_server, centerX, GameUtils.space.getBuildHeight(level_accessor, true), centerZ);
                                                        biome_center_id = GameUtils.space.getBiomeID(biome_center);
                                                        String key = "|" + biome_center_id + "|" + id + "|";

                                                        if (cache_biome_test.containsKey(key) == false) {

                                                            cache_biome_test.put(key, GameUtils.misc.testCustomBiome(biome_center, biome));

                                                        }

                                                        if (cache_biome_test.get(key) == false) {

                                                            continue;

                                                        }

                                                    }

                                                    writeData(level_accessor, random, centerX, centerZ, id, ground_block, start_height_offset, rotation, mirrored, dead_tree_chance, dead_tree_level);

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

    private static boolean testDistance (String dimension, String id, int centerX, int centerZ, int min_distance) {

        int center_regionX = centerX >> 9;
        int center_regionZ = centerZ >> 9;
        int scanX = 0;
        int scanZ = 0;
        List<String> already_tested_region = new ArrayList<>();

        int loop = 0;
        String value = "";
        String key = "";
        String test_id = "";
        int test_posX = 0;
        int test_posZ = 0;
        ByteBuffer buffer = null;
        String id_dictionary = CacheManager.getDictionary(id, false);

        for (int step = 1; step <= 9; step++) {

            // Get Region Pos
            {

                if (step == 1) {

                    scanX = centerX;
                    scanZ = centerZ;

                } else if (step == 2) {

                    scanX = centerX + min_distance;

                } else if (step == 3) {

                    scanX = centerX - min_distance;

                } else if (step == 4) {

                    scanZ = centerZ + min_distance;

                } else if (step == 5) {

                    scanZ = centerZ - min_distance;

                } else if (step == 6) {

                    scanX = centerX + min_distance;
                    scanZ = centerZ + min_distance;

                } else if (step == 7) {

                    scanX = centerX + min_distance;
                    scanZ = centerZ - min_distance;

                } else if (step == 8) {

                    scanX = centerX - min_distance;
                    scanZ = centerZ + min_distance;

                } else {

                    scanX = centerX - min_distance;
                    scanZ = centerZ - min_distance;

                }

                scanX = scanX >> 9;
                scanZ = scanZ >> 9;

            }

            if (already_tested_region.contains(scanX + "," + scanZ) == false) {

                already_tested_region.add(scanX + "," + scanZ);

                // Cache Test
                {

                    for (String read_all : cache_write_tree_location.getOrDefault(scanX + "," + scanZ, new ArrayList<>())) {

                        loop = loop + 1;

                        // Get Value
                        {

                            value = read_all.substring(1);

                            if (loop == 1) {

                                test_id = value;

                            } else if (loop == 2) {

                                test_posX = Integer.parseInt(value);

                            } else {

                                test_posZ = Integer.parseInt(value);

                            }

                        }

                        if (loop == 3) {

                            loop = 0;

                            // Test
                            {

                                if (centerX == test_posX && centerZ == test_posZ) {

                                    return false;

                                } else {

                                    if (id_dictionary.equals(test_id) == true) {

                                        if ((Math.abs(centerX - test_posX) <= min_distance) && (Math.abs(centerZ - test_posZ) <= min_distance)) {

                                            return false;

                                        }

                                    }

                                }

                            }

                        }

                    }

                }

                if (center_regionX != scanX || center_regionZ != scanZ) {

                    // BIN File Test
                    {

                        key = scanX + "," + scanZ;

                        if (cache_other_region.containsKey(key) == false) {

                            cache_other_region.put(key, FileManager.readBIN(Core.path_world_data + "/world_gen/tree_locations/" + dimension + "/" + key + ".bin"));

                        }

                        buffer = cache_other_region.get(key);

                        while (buffer.remaining() > 0) {

                            try {

                                test_id = String.valueOf(buffer.getShort());
                                test_posX = buffer.getInt();
                                test_posZ = buffer.getInt();

                            } catch (Exception exception) {

                                OutsideUtils.exception(new Exception(), exception, "");
                                return false;

                            }

                            if (centerX == test_posX && centerZ == test_posZ) {

                                return false;

                            } else {

                                if (id_dictionary.equals(test_id) == true) {

                                    if ((Math.abs(centerX - test_posX) <= min_distance) && (Math.abs(centerZ - test_posZ) <= min_distance)) {

                                        return false;

                                    }

                                }

                            }

                        }

                    }

                }

            }

        }

        return true;

    }

    private static boolean testWaterSide (LevelAccessor level_accessor, ServerLevel level_server, RandomSource random, int centerX, int centerZ, double waterside_chance) {

        boolean return_logic = true;

        {

            if (waterside_chance > 0) {

                if (FileConfig.waterside_detection == false) {

                    return_logic = false;

                } else {

                    if (random.nextDouble() < waterside_chance) {

                        int maxY = GameUtils.space.getBuildHeight(level_accessor, true);
                        boolean on_land = GameUtils.space.isBiomeTaggedAs(GameUtils.space.getBiomeAt(level_server, centerX, maxY, centerZ), "tanshugetrees:water_biomes") == false;
                        boolean waterside_test1 = GameUtils.space.isBiomeTaggedAs(GameUtils.space.getBiomeAt(level_server, centerX + 16, maxY, centerZ + 16), "tanshugetrees:water_biomes");
                        boolean waterside_test2 = GameUtils.space.isBiomeTaggedAs(GameUtils.space.getBiomeAt(level_server, centerX + 16, maxY, centerZ - 16), "tanshugetrees:water_biomes");
                        boolean waterside_test3 = GameUtils.space.isBiomeTaggedAs(GameUtils.space.getBiomeAt(level_server, centerX - 16, maxY, centerZ + 16), "tanshugetrees:water_biomes");
                        boolean waterside_test4 = GameUtils.space.isBiomeTaggedAs(GameUtils.space.getBiomeAt(level_server, centerX - 16, maxY, centerZ - 16), "tanshugetrees:water_biomes");

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

    private static void writeData (LevelAccessor level_accessor, RandomSource random, int centerX, int centerZ, String id, String ground_block, String start_height_offset, String rotation, String mirrored, double dead_tree_chance, String dead_tree_level) {

        String path_storage = "";

        // Scan World Gen File
        {
            
            for (String read_all : Caches.getWorldGenSettings(id)) {
                
                {

                    if (read_all.startsWith("path_storage = ") == true) {

                        path_storage = read_all.substring("path_storage = ".length());
                        break;

                    }

                }

            }

        }

        File chosen = new File(Core.path_config + "/#dev/#temporary/presets/" + path_storage + "/storage");

        // Random Select File
        {

            File[] list = chosen.listFiles();

            if (list != null) {

                chosen = new File(chosen.getPath() + "/" + list[random.nextInt(list.length)].getName());

            }

        }

        if (chosen.exists() == true && chosen.isDirectory() == false) {

            short[] get1 = Caches.getTreeShapePart1(path_storage + "/" + chosen.getName());
            int sizeX = 0;
            int sizeY = 0;
            int sizeZ = 0;
            int center_sizeX = 0;
            int center_sizeY = 0;
            int center_sizeZ = 0;
            int[] get2 = Caches.getTreeShapePart2(path_storage + "/" + chosen.getName());
            int count_trunk = 0;
            int count_bough = 0;
            int count_branch = 0;
            int count_limb = 0;
            int count_twig = 0;
            int count_sprig = 0;

            try {

                sizeX = get1[0];
                sizeY = get1[1];
                sizeZ = get1[2];
                center_sizeX = get1[3];
                center_sizeY = get1[4];
                center_sizeZ = get1[5];

                count_trunk = get2[0];
                count_bough = get2[1];
                count_branch = get2[2];
                count_limb = get2[3];
                count_twig = get2[4];
                count_sprig = get2[5];

            } catch (Exception exception) {

                OutsideUtils.exception(new Exception(), exception, "");
                return;

            }

            // Dead Tree
            {

                if (random.nextDouble() >= dead_tree_chance) {

                    dead_tree_level = "0";

                } else {

                    List<String> list = new ArrayList<>();

                    if (dead_tree_level.startsWith("auto") == false) {

                        list = Arrays.stream(dead_tree_level.split(" / ")).toList();

                    } else {

                        if (cache_dead_tree_auto_level.containsKey(id) == true) {

                            list = cache_dead_tree_auto_level.get(id);

                        } else {

                            String is_pine = "0";

                            if (dead_tree_level.endsWith("pine") == true) {

                                is_pine = "1";

                            }

                            // Write Data
                            {

                                if (count_trunk > 0) {

                                    if (FileConfig.dead_tree_auto_level.contains("18") == true) list.add("180");
                                    if (FileConfig.dead_tree_auto_level.contains("19") == true) list.add("190");
                                    if (FileConfig.dead_tree_auto_level.contains("28") == true) list.add("280");
                                    if (FileConfig.dead_tree_auto_level.contains("29") == true) list.add("290");
                                    if (FileConfig.dead_tree_auto_level.contains("38") == true) list.add("380");
                                    if (FileConfig.dead_tree_auto_level.contains("39") == true) list.add("390");

                                }

                                if (count_bough > 0) {

                                    if (FileConfig.dead_tree_auto_level.contains("16") == true) list.add("160");
                                    if (FileConfig.dead_tree_auto_level.contains("17") == true) list.add("170");
                                    if (FileConfig.dead_tree_auto_level.contains("26") == true) list.add("260");
                                    if (FileConfig.dead_tree_auto_level.contains("27") == true) list.add("270");
                                    if (FileConfig.dead_tree_auto_level.contains("36") == true) list.add("360");
                                    if (FileConfig.dead_tree_auto_level.contains("37") == true) list.add("370");
                                    if (FileConfig.dead_tree_auto_level.contains("15") == true) list.add("15" + is_pine);
                                    if (FileConfig.dead_tree_auto_level.contains("25") == true) list.add("25" + is_pine);
                                    if (FileConfig.dead_tree_auto_level.contains("35") == true) list.add("35" + is_pine);

                                }

                                if (count_branch > 0) {

                                    if (FileConfig.dead_tree_auto_level.contains("14") == true) list.add("14" + is_pine);
                                    if (FileConfig.dead_tree_auto_level.contains("24") == true) list.add("24" + is_pine);
                                    if (FileConfig.dead_tree_auto_level.contains("34") == true) list.add("34" + is_pine);

                                }

                                if (count_limb > 0) {

                                    if (FileConfig.dead_tree_auto_level.contains("13") == true) list.add("13" + is_pine);
                                    if (FileConfig.dead_tree_auto_level.contains("23") == true) list.add("23" + is_pine);
                                    if (FileConfig.dead_tree_auto_level.contains("33") == true) list.add("33" + is_pine);

                                }

                                if (count_twig > 0) {

                                    if (FileConfig.dead_tree_auto_level.contains("12") == true) list.add("12" + is_pine);
                                    if (FileConfig.dead_tree_auto_level.contains("22") == true) list.add("22" + is_pine);
                                    if (FileConfig.dead_tree_auto_level.contains("32") == true) list.add("32" + is_pine);

                                }

                                if (count_sprig > 0) {

                                    if (FileConfig.dead_tree_auto_level.contains("11") == true) list.add("11" + is_pine);
                                    if (FileConfig.dead_tree_auto_level.contains("21") == true) list.add("21" + is_pine);
                                    if (FileConfig.dead_tree_auto_level.contains("31") == true) list.add("31" + is_pine);

                                }

                            }

                            cache_dead_tree_auto_level.put(id, list);

                        }

                    }

                    dead_tree_level =  list.get(random.nextInt(list.size()));

                }

            }

            int start_height_offset_get = 0;

            // Height Offset
            {

                String[] offset_get = start_height_offset.split(" <> ");
                start_height_offset_get = random.nextInt(Integer.parseInt(offset_get[0]), Integer.parseInt(offset_get[1]) + 1);

            }

            // Rotation and Mirrored
            {

                // Apply Value
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

                        rotation = String.valueOf(random.nextInt(4) + 1);

                    }

                    if (mirrored.equals("random") == true) {

                        mirrored = String.valueOf(random.nextBoolean());

                    }

                }

                int[] convert = OutsideUtils.convertSizeRotationMirrored(Integer.parseInt(rotation), mirrored.equals("true"), sizeX, sizeZ, center_sizeX, center_sizeZ);
                sizeX = convert[0];
                sizeZ = convert[1];
                center_sizeX = convert[2];
                center_sizeZ = convert[3];

            }

            // Coarse Woody Debris
            {

                if (Integer.parseInt(dead_tree_level) > 200) {

                    int fallen_direction = RandomSource.create(level_accessor.getServer().overworld().getSeed() ^ ((centerX * 341873128712L) + (centerZ * 132897987541L))).nextInt(4) + 1;

                    if (fallen_direction > 0) {

                        int[] convert = OutsideUtils.convertSizeFallen(fallen_direction, sizeX, sizeY, sizeZ, center_sizeX, center_sizeY, center_sizeZ);
                        sizeX = convert[0];
                        sizeY = convert[1];
                        sizeZ = convert[2];
                        center_sizeX = convert[3];
                        center_sizeY = convert[4];
                        center_sizeZ = convert[5];

                    }

                }

            }

            int from_chunkX = centerX - center_sizeX;
            int from_chunkZ = centerZ - center_sizeZ;
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

                        if (GameUtils.space.testChunkStatus(level_accessor, scanX, scanZ, "minecraft:features") == true) {

                            return;

                        }

                    }

                }

            }

            // Everything Pass
            {

                // Write Tree Location
                {

                    List<String> write = new ArrayList<>();
                    write.add("s" + CacheManager.getDictionary(id, false));
                    write.add("i" + centerX);
                    write.add("i" + centerZ);
                    cache_write_tree_location.computeIfAbsent((centerX >> 9) + "," + (centerZ >> 9), test -> new ArrayList<>()).addAll(write);

                }

                // Write Place
                {

                    List<String> write = new ArrayList<>();
                    write.add("i" + from_chunkX);
                    write.add("i" + from_chunkZ);
                    write.add("i" + to_chunkX);
                    write.add("i" + to_chunkZ);
                    write.add("s" + CacheManager.getDictionary(id, false));
                    write.add("s" + CacheManager.getDictionary(chosen.getName(), false));
                    write.add("i" + centerX);
                    write.add("i" + centerZ);
                    write.add("b" + rotation);
                    write.add("l" + mirrored);
                    write.add("s" + start_height_offset_get);
                    write.add("s" + CacheManager.getDictionary(ground_block, false));
                    write.add("s" + dead_tree_level);

                    int from_chunkX_test = from_chunkX >> 5;
                    int from_chunkZ_test = from_chunkZ >> 5;
                    int to_chunkX_test = to_chunkX >> 5;
                    int to_chunkZ_test = to_chunkZ >> 5;

                    for (int scanX = from_chunkX_test; scanX <= to_chunkX_test; scanX++) {

                        for (int scanZ = from_chunkZ_test; scanZ <= to_chunkZ_test; scanZ++) {

                            cache_write_place.computeIfAbsent(scanX + "," + scanZ, test -> new ArrayList<>()).addAll(write);

                        }

                    }

                }

            }

        }

    }

}