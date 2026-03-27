package tannyjung.tanshugetrees_handcode.systems.world_gen;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
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

    private static final Object lock = new Object();
    private static final Map<String, List<String>> cache_write_tree_location = new HashMap<>();
    private static final Map<String, List<String>> cache_write_place = new HashMap<>();
    private static final Map<String, ByteBuffer> cache_other_region = new HashMap<>();
    private static final Map<ChunkPos, Holder<Biome>> cache_biome = new HashMap<>();
    private static final Map<String, Boolean> cache_biome_test = new HashMap<>();
    public static int world_gen_overlay_animation = 0;
    public static int world_gen_overlay_bar = 0;
    public static String world_gen_overlay_details_biome = "";
    public static String world_gen_overlay_details_tree = "";

    public static void start (LevelAccessor level_accessor, ServerLevel level_server, String dimension, ChunkPos chunk_pos) {

        synchronized (lock) {

            Map<String, Map<String, String>> config = Caches.getConfigWorldGen();

            if (config.isEmpty() == false) {

                // Separate +-4 to fix tree cut-off by chunk status not fully update
                TreeLocation.run(level_accessor, level_server, dimension, new ChunkPos(chunk_pos.x + 4, chunk_pos.z + 4), config);
                TreeLocation.run(level_accessor, level_server, dimension, new ChunkPos(chunk_pos.x + 4, chunk_pos.z - 4), config);
                TreeLocation.run(level_accessor, level_server, dimension, new ChunkPos(chunk_pos.x - 4, chunk_pos.z + 4), config);
                TreeLocation.run(level_accessor, level_server, dimension, new ChunkPos(chunk_pos.x - 4, chunk_pos.z - 4), config);

                cache_write_tree_location.clear();
                cache_write_place.clear();
                cache_other_region.clear();
                cache_biome.clear();
                cache_biome_test.clear();

            }

        }

    }

    public static void run (LevelAccessor level_accessor, ServerLevel level_server, String dimension, ChunkPos chunk_pos, Map<String, Map<String, String>> config) {

        int regionX = chunk_pos.x >> 5;
        int regionZ = chunk_pos.z >> 5;
        File file_region = new File(Core.path_world_mod + "/world_gen/regions/" + dimension + "/" + regionX + "," + regionZ + ".bin");

        if (file_region.exists() == false) {

            FileManager.writeBIN(file_region.getPath(), new ArrayList<>(), false);
            Core.logger.info("Generating tree locations for a new region ({} -> {}/{})", dimension.replace("-", ":"), regionX, regionZ);
            world_gen_overlay_animation = 4;
            world_gen_overlay_bar = 0;

            if (FileConfig.world_gen_icon == true) {

                CompletableFuture.runAsync(TreeLocation::scanning_overlay_loop);

            }

            // Scanning
            {

                RandomSource random = null;
                int posX = regionX * 32;
                int posZ = regionZ * 32;
                ChunkPos chunk_pos_scan = null;

                for (int scanX = 0; scanX < 32; scanX++) {

                    for (int scanZ = 0; scanZ < 32; scanZ++) {

                        world_gen_overlay_bar = world_gen_overlay_bar + 1;
                        chunk_pos_scan = new ChunkPos(posX + scanX, posZ + scanZ);
                        random = RandomSource.create(level_accessor.getServer().overworld().getSeed() ^ ((chunk_pos_scan.x * 341873128712L) + (chunk_pos_scan.z * 132897987541L)));

                        if (random.nextDouble() < FileConfig.region_scan_percent * 0.01) {

                            getData(level_accessor, level_server, dimension, chunk_pos_scan, config);

                        }

                    }

                }

            }

            // Write File
            {

                for (Map.Entry<String, List<String>> entry : cache_write_tree_location.entrySet()) {

                    FileManager.writeBIN(Core.path_world_mod + "/world_gen/tree_locations/" + dimension + "/" + entry.getKey() + ".bin", entry.getValue(), true);

                }

                for (Map.Entry<String, List<String>> entry : cache_write_place.entrySet()) {

                    FileManager.writeBIN(Core.path_world_mod + "/world_gen/place/" + dimension + "/" + entry.getKey() + ".bin", entry.getValue(), true);

                }

            }

            world_gen_overlay_animation = 0;
            Core.logger.info("Completed!");

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

    private static void getData (LevelAccessor level_accessor, ServerLevel level_server, String dimension, ChunkPos chunk_pos, Map<String, Map<String, String>> config) {

        String biome = "";
        Holder<Biome> biome_center = null;
        String biome_center_id = "";
        int group_size = 0;
        int center_posX = 0;
        int center_posZ = 0;
        int min_distance = 0;
        String key = "";
        String[] split = new String[0];
        RandomSource random = null;

        world_gen_overlay_details_tree = "No Matching";

        for (Map.Entry<String, Map<String, String>> entry : config.entrySet()) {

            if (entry.getValue().get("enable").equals("true") == true) {

                random = RandomSource.create(level_accessor.getServer().overworld().getSeed() ^ ((chunk_pos.x * 341873128712L) + (chunk_pos.z * 132897987541L)) + entry.getKey().hashCode());

                if (random.nextDouble() < (Double.parseDouble(entry.getValue().get("rarity")) * 0.01) * FileConfig.multiply_rarity) {

                    center_posX = (chunk_pos.x * 16) + random.nextInt(0, 16);
                    center_posZ = (chunk_pos.z * 16) + random.nextInt(0, 16);
                    min_distance = (int) Math.ceil(Integer.parseInt(entry.getValue().get("min_distance")) * FileConfig.multiply_min_distance);

                    // Min Distance
                    {

                        if (min_distance > 0) {

                            if (testDistance(dimension, entry.getKey(), center_posX, center_posZ, min_distance) == false) {

                                continue;

                            }

                        }

                    }

                    biome = entry.getValue().get("biome");

                    // Biome
                    {

                        biome_center = getBiome(level_accessor, level_server, chunk_pos);
                        biome_center_id = GameUtils.Space.getBiomeID(biome_center);
                        key = biome_center_id + "/" + entry.getKey();

                        if (cache_biome_test.containsKey(key) == false) {

                            cache_biome_test.put(key, GameUtils.Misc.testBiome(biome_center, biome));

                        }

                        if (cache_biome_test.get(key) == false) {

                            continue;

                        }

                    }

                    // Shoreline
                    {

                        if (entry.getValue().get("spawn_type").equals("normal") == false) {

                            if (FileConfig.shoreline_detection == false) {

                                continue;

                            } else if (testShoreline(level_accessor, level_server, new ChunkPos(center_posX >> 4, center_posZ >> 4)) == false) {

                                continue;

                            }

                        }

                    }

                    split = entry.getValue().get("group_size").split(" <> ");
                    group_size = (int) ((double) Mth.nextInt(random, Integer.parseInt(split[0]), Integer.parseInt(split[1])) * FileConfig.multiply_group_size);
                    world_gen_overlay_details_biome = biome_center_id;
                    world_gen_overlay_details_tree = entry.getKey();
                    writeData(level_accessor, center_posX, center_posZ, entry.getKey(), entry.getValue());

                    if (group_size > 1) {

                        // Group Spawning
                        {

                            if (entry.getValue().get("spawn_type").equals("landside") == true) {

                                biome = "tanshugetrees:water_biomes";

                            } else if (entry.getValue().get("spawn_type").equals("shoreline") == true) {

                                biome = biome + " / tanshugetrees:water_biomes";

                            }

                            while (group_size > 0) {

                                group_size = group_size - 1;
                                center_posX = center_posX + random.nextInt(-(min_distance + 1), (min_distance + 1) + 1);
                                center_posZ = center_posZ + random.nextInt(-(min_distance + 1), (min_distance + 1) + 1);

                                // Min Distance
                                {

                                    if (min_distance > 0) {

                                        if (testDistance(dimension, entry.getKey(), center_posX, center_posZ, min_distance) == false) {

                                            continue;

                                        }

                                    }

                                }

                                // Biome
                                {

                                    biome_center = getBiome(level_accessor, level_server, chunk_pos);
                                    biome_center_id = GameUtils.Space.getBiomeID(biome_center);
                                    key = biome_center_id + "/" + entry.getKey();

                                    if (cache_biome_test.containsKey(key) == false) {

                                        cache_biome_test.put(key, GameUtils.Misc.testBiome(biome_center, biome));

                                    }

                                    if (cache_biome_test.get(key) == false) {

                                        continue;

                                    }

                                }

                                writeData(level_accessor, center_posX, center_posZ, entry.getKey(), entry.getValue());

                            }

                        }

                    }

                }

            }

        }

    }

    private static Holder<Biome> getBiome (LevelAccessor level_accessor, ServerLevel level_server, ChunkPos chunk_pos) {

        if (cache_biome.containsKey(chunk_pos) == false) {

            BlockPos pos = new BlockPos((chunk_pos.x * 16) + 7, GameUtils.Space.getBuildHeight(level_accessor, true), (chunk_pos.z * 16) + 7);
            cache_biome.put(chunk_pos, GameUtils.Space.getBiomeAt(level_accessor, level_server, pos));

        }

        return cache_biome.get(chunk_pos);

    }

    private static boolean testDistance (String dimension, String id, int centerX, int centerZ, int min_distance) {

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

            // Get Surrounding Pos
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

                if (cache_write_tree_location.containsKey(scanX + "," + scanZ) == true) {

                    // From Cache
                    {

                        for (String read_all : cache_write_tree_location.get(scanX + "," + scanZ)) {

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

                } else {

                    // From BIN
                    {

                        key = scanX + "," + scanZ;

                        if (cache_other_region.containsKey(key) == false) {

                            cache_other_region.put(key, FileManager.readBIN(Core.path_world_mod + "/world_gen/tree_locations/" + dimension + "/" + key + ".bin"));

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

    private static boolean testShoreline (LevelAccessor level_accessor, ServerLevel level_server, ChunkPos center_chunk_pos) {

        if (FileConfig.shoreline_detection == false) {

            return false;

        } else {

            Holder<Biome> biome_side1 = getBiome(level_accessor, level_server, new ChunkPos(center_chunk_pos.x + 1, center_chunk_pos.z + 1));
            Holder<Biome> biome_side2 = getBiome(level_accessor, level_server, new ChunkPos(center_chunk_pos.x + 1, center_chunk_pos.z - 1));
            Holder<Biome> biome_side3 = getBiome(level_accessor, level_server, new ChunkPos(center_chunk_pos.x - 1, center_chunk_pos.z + 1));
            Holder<Biome> biome_side4 = getBiome(level_accessor, level_server, new ChunkPos(center_chunk_pos.x - 1, center_chunk_pos.z - 1));
            boolean waterside_test1 = GameUtils.Space.isBiomeTaggedAs(biome_side1, "tanshugetrees:water_biomes");
            boolean waterside_test2 = GameUtils.Space.isBiomeTaggedAs(biome_side2, "tanshugetrees:water_biomes");
            boolean waterside_test3 = GameUtils.Space.isBiomeTaggedAs(biome_side3, "tanshugetrees:water_biomes");
            boolean waterside_test4 = GameUtils.Space.isBiomeTaggedAs(biome_side4, "tanshugetrees:water_biomes");
            return waterside_test1 == true || waterside_test2 == true || waterside_test3 == true || waterside_test4 == true;

        }

    }

    private static void writeData (LevelAccessor level_accessor, int centerX, int centerZ, String id, Map<String, String> data) {

        String path_storage = data.get("path_storage");
        File chosen = new File(Core.path_config + "/dev/temporary/presets/" + path_storage + "/storage");
        RandomSource random = RandomSource.create(level_accessor.getServer().overworld().getSeed() ^ ((centerX * 341873128712L) + (centerZ * 132897987541L)));

        // Random Select File
        {

            File[] list = chosen.listFiles();

            if (list != null) {

                chosen = new File(chosen.getPath() + "/" + list[random.nextInt(list.length)].getName());

            }

        }

        if (chosen.exists() == true && chosen.isDirectory() == false) {

            short[] get1 = Caches.getTreeShapeSize(path_storage + "|" + chosen.getName());
            int sizeX = 0;
            int sizeY = 0;
            int sizeZ = 0;
            int center_sizeX = 0;
            int center_sizeY = 0;
            int center_sizeZ = 0;

            try {

                sizeX = get1[0];
                sizeY = get1[1];
                sizeZ = get1[2];
                center_sizeX = get1[3];
                center_sizeY = get1[4];
                center_sizeZ = get1[5];

            } catch (Exception exception) {

                OutsideUtils.exception(new Exception(), exception, "");
                return;

            }

            int dead_tree_level = getDeadTreeLevel(data, random, path_storage + "|" + chosen.getName(), id, false);
            int start_height_offset = 0;

            // Height Offset
            {

                String[] offset_get = data.get("start_height_offset").split(" <> ");
                start_height_offset = random.nextInt(Integer.parseInt(offset_get[0]), Integer.parseInt(offset_get[1]) + 1);

            }

            String rotation = data.get("rotation");
            String mirrored = data.get("mirrored");

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

                        mirrored = String.valueOf(random.nextInt(3));

                    } else if (mirrored.equals("off") == true) {

                        mirrored = "0";

                    } else {

                        if (random.nextBoolean() == true) {

                            if (mirrored.equals("random_x") == true) {

                                mirrored = "1";

                            } else if (mirrored.equals("random_z") == true) {

                                mirrored = "2";

                            } else {

                                mirrored = "0";

                            }

                        } else {

                            mirrored = "0";

                        }

                    }

                }

                int[] convert = OutsideUtils.convertSizeRotationMirrored(Integer.parseInt(rotation), Integer.parseInt(mirrored), sizeX, sizeZ, center_sizeX, center_sizeZ);
                sizeX = convert[0];
                sizeZ = convert[1];
                center_sizeX = convert[2];
                center_sizeZ = convert[3];

            }

            // Coarse Woody Debris
            {

                if (dead_tree_level > 200) {

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

                        if (GameUtils.Space.testChunkStatus(level_accessor, new ChunkPos(scanX, scanZ), "features") == true) {

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
                    write.add("b" + mirrored);
                    write.add("s" + start_height_offset);
                    write.add("s" + CacheManager.getDictionary(data.get("ground_block"), false));
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

    public static int getDeadTreeLevel (Map<String, String> data, RandomSource random, String location, String id, boolean unviable_ecology) {

        if (unviable_ecology == false) {

            if (random.nextDouble() >= Double.parseDouble(data.get("dead_tree_chance"))) {

                return 0;

            }

        } else {

            id = id + "_unviable_ecology";

        }

        List<Integer> list = new ArrayList<>();
        String dead_tree_level = data.get("dead_tree_level");

        if (dead_tree_level.startsWith("auto") == false) {

            {

                for (String read_all : dead_tree_level.split(" / ")) {

                    if (unviable_ecology == false && read_all.startsWith("3")) {

                        continue;

                    }

                    list.add(Integer.parseInt(read_all));

                }

                return list.get(random.nextInt(list.size()));

            }

        } else {

            {

                int[] convert = new int[0];

                if (CacheManager.Data.existTextList("dead_tree_auto_level", id) == false) {

                    int is_pine = 0;

                    if (dead_tree_level.equals("auto_pine") == true) {

                        is_pine = 1;

                    }

                    // Write Data
                    {

                        int[] get2 = Caches.getTreeShapeBlockCount(location);
                        int count_trunk = 0;
                        int count_bough = 0;
                        int count_branch = 0;
                        int count_limb = 0;
                        int count_twig = 0;
                        int count_sprig = 0;

                        try {

                            count_trunk = get2[0];
                            count_bough = get2[1];
                            count_branch = get2[2];
                            count_limb = get2[3];
                            count_twig = get2[4];
                            count_sprig = get2[5];

                        } catch (Exception exception) {

                            OutsideUtils.exception(new Exception(), exception, "");

                        }

                        if (count_trunk > 0) {

                            if (FileConfig.dead_tree_auto_level.contains("18") == true) list.add(180);
                            if (FileConfig.dead_tree_auto_level.contains("19") == true) list.add(190);
                            if (FileConfig.dead_tree_auto_level.contains("28") == true) list.add(280);
                            if (FileConfig.dead_tree_auto_level.contains("29") == true) list.add(290);

                            if (unviable_ecology == false) {

                                if (FileConfig.dead_tree_auto_level.contains("38") == true) list.add(380);
                                if (FileConfig.dead_tree_auto_level.contains("39") == true) list.add(390);

                            }

                        }

                        if (count_bough > 0) {

                            if (FileConfig.dead_tree_auto_level.contains("16") == true) list.add(160);
                            if (FileConfig.dead_tree_auto_level.contains("17") == true) list.add(170);
                            if (FileConfig.dead_tree_auto_level.contains("26") == true) list.add(260);
                            if (FileConfig.dead_tree_auto_level.contains("27") == true) list.add(270);
                            if (FileConfig.dead_tree_auto_level.contains("15") == true) list.add(150 + is_pine);
                            if (FileConfig.dead_tree_auto_level.contains("25") == true) list.add(250 + is_pine);

                            if (unviable_ecology == false) {

                                if (FileConfig.dead_tree_auto_level.contains("36") == true) list.add(360);
                                if (FileConfig.dead_tree_auto_level.contains("37") == true) list.add(370);
                                if (FileConfig.dead_tree_auto_level.contains("35") == true) list.add(350 + is_pine);

                            }

                        }

                        if (count_branch > 0) {

                            if (FileConfig.dead_tree_auto_level.contains("14") == true) list.add(140 + is_pine);
                            if (FileConfig.dead_tree_auto_level.contains("24") == true) list.add(240 + is_pine);

                            if (unviable_ecology == false) {

                                if (FileConfig.dead_tree_auto_level.contains("34") == true) list.add(340 + is_pine);

                            }

                        }

                        if (count_limb > 0) {

                            if (FileConfig.dead_tree_auto_level.contains("13") == true) list.add(130 + is_pine);
                            if (FileConfig.dead_tree_auto_level.contains("23") == true) list.add(230 + is_pine);

                            if (unviable_ecology == false) {

                                if (FileConfig.dead_tree_auto_level.contains("33") == true) list.add(330 + is_pine);

                            }

                        }

                        if (count_twig > 0) {

                            if (FileConfig.dead_tree_auto_level.contains("12") == true) list.add(120 + is_pine);
                            if (FileConfig.dead_tree_auto_level.contains("22") == true) list.add(220 + is_pine);

                            if (unviable_ecology == false) {

                                if (FileConfig.dead_tree_auto_level.contains("32") == true) list.add(320 + is_pine);

                            }

                        }

                        if (count_sprig > 0) {

                            if (FileConfig.dead_tree_auto_level.contains("11") == true) list.add(110 + is_pine);
                            if (FileConfig.dead_tree_auto_level.contains("21") == true) list.add(210 + is_pine);

                            if (unviable_ecology == false) {

                                if (FileConfig.dead_tree_auto_level.contains("31") == true) list.add(310 + is_pine);

                            }

                        }

                    }

                    convert = new int[list.size()];

                    for (int scan = 0; scan < convert.length; scan++) {

                        convert[scan] = list.get(scan);

                    }

                    CacheManager.Data.setNumberIntArray("dead_tree_auto_level", id, convert);

                }

                convert = CacheManager.Data.getNumberIntArray("dead_tree_auto_level", id);

                if (convert.length > 0) {

                    return convert[random.nextInt(convert.length)];

                } else {

                    return 0;

                }

            }

        }

    }

}