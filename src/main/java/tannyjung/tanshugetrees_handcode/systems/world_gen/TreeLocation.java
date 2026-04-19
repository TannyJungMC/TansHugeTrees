package tannyjung.tanshugetrees_handcode.systems.world_gen;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.biome.Biome;
import tannyjung.tanshugetrees_core.Core;
import tannyjung.tanshugetrees_core.outside.CacheManager;
import tannyjung.tanshugetrees_core.outside.ConfigDynamic;
import tannyjung.tanshugetrees_core.outside.FileManager;
import tannyjung.tanshugetrees_core.outside.OutsideUtils;
import tannyjung.tanshugetrees_core.game.GameUtils;
import tannyjung.tanshugetrees_handcode.Handcode;
import tannyjung.tanshugetrees_handcode.systems.Caches;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public class TreeLocation {

    private static final Map<ChunkPos, Map<BlockPos, String>> cache_write_tree_location = new HashMap<>();
    private static final Map<String, List<String>> cache_write_place = new HashMap<>();
    private static final Map<String, Map<ChunkPos, Map<BlockPos, String>>> cache_other_region = new HashMap<>();
    private static final Map<ChunkPos, Holder<Biome>> cache_biome = new HashMap<>();
    public static int world_gen_overlay_animation = 0;
    public static int world_gen_overlay_bar = 0;
    public static String world_gen_overlay_details_biome = "";
    public static String world_gen_overlay_details_tree = "";

    public static void start (LevelAccessor level_accessor, String dimension, ChunkPos chunk_pos) {

        Core.GlobalLocking.test();
        Core.GlobalLocking.lock();

        Map<String, Map<String, String>> data = ConfigDynamic.getData("world_gen");

        if (data.isEmpty() == false) {

            // Separate +-4 to fix tree cut-off by chunk status not fully update
            TreeLocation.run(level_accessor, dimension, new ChunkPos(chunk_pos.x + 4, chunk_pos.z + 4), data);
            TreeLocation.run(level_accessor, dimension, new ChunkPos(chunk_pos.x + 4, chunk_pos.z - 4), data);
            TreeLocation.run(level_accessor, dimension, new ChunkPos(chunk_pos.x - 4, chunk_pos.z + 4), data);
            TreeLocation.run(level_accessor, dimension, new ChunkPos(chunk_pos.x - 4, chunk_pos.z - 4), data);

        }

        Core.GlobalLocking.unlock();

    }

    public static void run (LevelAccessor level_accessor, String dimension, ChunkPos chunk_pos, Map<String, Map<String, String>> data) {

        int regionX = chunk_pos.x >> 5;
        int regionZ = chunk_pos.z >> 5;
        File file_region = new File(Core.path_world_mod + "/world_gen/regions/" + dimension + "/" + regionX + "," + regionZ + ".bin");

        if (file_region.exists() == false) {

            FileManager.writeBIN(file_region.getPath(), new ArrayList<>(), false);
            Core.logger.info("Generating tree locations for a new region ({} -> {}/{})", dimension.replace("-", ":"), regionX, regionZ);
            world_gen_overlay_animation = 4;
            world_gen_overlay_bar = 0;

            if (Handcode.Config.world_gen_icon == true) {

                CompletableFuture.runAsync(TreeLocation::scanning_overlay_loop);

            }

            // Scanning
            {

                int posX = regionX * 32;
                int posZ = regionZ * 32;
                ChunkPos chunk_pos_scan = null;

                for (int scanX = 0; scanX < 32; scanX++) {

                    for (int scanZ = 0; scanZ < 32; scanZ++) {

                        world_gen_overlay_bar = world_gen_overlay_bar + 1;
                        chunk_pos_scan = new ChunkPos(posX + scanX, posZ + scanZ);
                        RandomSource random = RandomSource.create(level_accessor.getServer().overworld().getSeed() ^ ((chunk_pos_scan.x * 341873128712L) + (chunk_pos_scan.z * 132897987541L)));

                        if (random.nextDouble() < Handcode.Config.region_scan_percent * 0.01) {

                            getData(level_accessor, dimension, chunk_pos_scan, data);

                        }

                    }

                }

            }

            // Write Location
            {

                List<String> write = new ArrayList<>();

                for (Map.Entry<ChunkPos, Map<BlockPos, String>> entry1 : cache_write_tree_location.entrySet()) {

                    for (Map.Entry<BlockPos, String> entry2 : entry1.getValue().entrySet()) {

                        write.add("s" + entry2.getValue());
                        write.add("i" + entry2.getKey().getX());
                        write.add("i" + entry2.getKey().getZ());

                    }

                    FileManager.writeBIN(Core.path_world_mod + "/world_gen/tree_locations/" + dimension + "/" + (entry1.getKey().x >> 5) + "," + (entry1.getKey().z >> 5) + ".bin", write, true);
                    write.clear();

                }

            }

            // Write Place
            {

                for (Map.Entry<String, List<String>> entry : cache_write_place.entrySet()) {

                    FileManager.writeBIN(Core.path_world_mod + "/world_gen/place/" + dimension + "/" + entry.getKey() + ".bin", entry.getValue(), true);

                }

            }

            world_gen_overlay_animation = 0;
            Core.logger.info("Completed!");

            cache_write_tree_location.clear();
            cache_write_place.clear();
            cache_other_region.clear();
            cache_biome.clear();

            TreePlacer.Data.clear();

        }

    }

    private static void scanning_overlay_loop () {

        if (world_gen_overlay_animation != 0) {

            if (world_gen_overlay_animation < 4) {

                world_gen_overlay_animation = world_gen_overlay_animation + 1;

            } else {

                world_gen_overlay_animation = 1;

            }

            Core.DelayedWork.create(true, 20, TreeLocation::scanning_overlay_loop);

        }

    }

    private static void getData (LevelAccessor level_accessor, String dimension, ChunkPos chunk_pos, Map<String, Map<String, String>> data) {

        Holder<Biome> biome_center = getBiome(level_accessor, chunk_pos);
        String biome_id = GameUtils.Environment.toID(biome_center);
        world_gen_overlay_details_biome = biome_id;
        world_gen_overlay_details_tree = "No Matching";

        Map<String, String> config = null;
        boolean config_enable = false;
        String config_biome = "";
        String config_spawn_type = "";
        double config_rarity = 0.0;
        int config_min_distance = 0;
        int config_group_size = 0;
        Set<String> set_biome_tree = CacheManager.DataText.getSet("set_biome_tree").get(biome_id);

        // Get Set of Trees
        {

            if (set_biome_tree == null) {

                set_biome_tree = new HashSet<>();

                for (Map.Entry<String, Map<String, String>> entry : data.entrySet()) {

                    config_enable = entry.getValue().get("enable").equals("true");

                    if (config_enable == true) {

                        config_biome = entry.getValue().get("biome");

                        if (GameUtils.Environment.test(biome_center, config_biome) == true) {

                            set_biome_tree.add(entry.getKey());

                        }

                    }

                }

                CacheManager.DataText.setSet("set_biome_tree", biome_id, set_biome_tree);

            }

        }

        int center_posX = 0;
        int center_posZ = 0;
        String[] split = null;

        for (String scan : set_biome_tree) {

            config = data.get(scan);
            config_rarity = (Double.parseDouble(config.get("rarity")) * 0.01) * Handcode.Config.multiply_rarity;
            RandomSource random = RandomSource.create(level_accessor.getServer().overworld().getSeed() ^ ((chunk_pos.x * 341873128712L) + (chunk_pos.z * 132897987541L)) + scan.hashCode());

            if (random.nextDouble() < config_rarity) {

                center_posX = (chunk_pos.x * 16) + random.nextInt(0, 16);
                center_posZ = (chunk_pos.z * 16) + random.nextInt(0, 16);
                config_min_distance = (int) Math.ceil(Integer.parseInt(config.get("min_distance")) * Handcode.Config.multiply_min_distance);

                // Min Distance
                {

                    if (config_min_distance > 0) {

                        if (testDistance(dimension, scan, center_posX, center_posZ, config_min_distance) == false) {

                            continue;

                        }

                    }

                }

                // Shoreline
                {

                    if (config.get("spawn_type").equals("normal") == false) {

                        if (Handcode.Config.shoreline_detection == false) {

                            continue;

                        } else if (testShoreline(level_accessor, new ChunkPos(center_posX >> 4, center_posZ >> 4)) == false) {

                            continue;

                        }

                    }

                }

                world_gen_overlay_details_biome = GameUtils.Environment.toID(biome_center);
                world_gen_overlay_details_tree = scan;
                writeData(level_accessor, center_posX, center_posZ, scan, config);

                split = config.get("group_size").split(" <> ");
                config_group_size = (int) ((double) Mth.nextInt(random, Integer.parseInt(split[0]), Integer.parseInt(split[1])) * Handcode.Config.multiply_group_size);

                if (config_group_size > 1) {

                    // Group Spawning
                    {

                        config_spawn_type = config.get("spawn_type");
                        config_biome = config.get("biome");

                        if (config_spawn_type.equals("landside") == true) {

                            config_biome = "tanshugetrees:water_biomes";

                        } else if (config_spawn_type.equals("shoreline") == true) {

                            config_biome = config_biome + " / tanshugetrees:water_biomes";

                        }

                        while (config_group_size > 0) {

                            config_group_size = config_group_size - 1;
                            center_posX = center_posX + random.nextInt(-(config_min_distance + 1), (config_min_distance + 1) + 1);
                            center_posZ = center_posZ + random.nextInt(-(config_min_distance + 1), (config_min_distance + 1) + 1);

                            // Min Distance
                            {

                                if (config_min_distance > 0) {

                                    if (testDistance(dimension, scan, center_posX, center_posZ, config_min_distance) == false) {

                                        continue;

                                    }

                                }

                            }

                            // Biome
                            {

                                biome_center = getBiome(level_accessor, new ChunkPos(center_posX >> 4, center_posZ >> 4));

                                if (GameUtils.Environment.test(biome_center, config_biome) == false) {

                                    continue;

                                }

                            }

                            writeData(level_accessor, center_posX, center_posZ, scan, config);

                        }

                    }

                }

            }

        }

    }

    private static Holder<Biome> getBiome (LevelAccessor level_accessor, ChunkPos chunk_pos) {

        if (cache_biome.containsKey(chunk_pos) == false) {

            BlockPos pos = new BlockPos((chunk_pos.x * 16) + 7, GameUtils.Space.getBuildHeight(level_accessor, true), (chunk_pos.z * 16) + 7);
            cache_biome.put(chunk_pos, GameUtils.Environment.getAt(level_accessor, pos));

        }

        return cache_biome.get(chunk_pos);

    }

    private static boolean testDistance (String dimension, String id, int centerX, int centerZ, int min_distance) {

        BlockPos center_pos = new BlockPos(centerX, 0, centerZ);
        ChunkPos center_chunk = new ChunkPos(center_pos);
        String id_number = CacheManager.getDictionary(id, false);
        int scanX = 0;
        int scanZ = 0;
        ChunkPos scan_pos = null;
        int step = 0;
        int explorer_step = 0;
        boolean is_first = true;
        Map<BlockPos, String> data = new HashMap<>();

        ByteBuffer buffer = null;
        String key = "";
        String test_id = "";
        int test_posX = 0;
        int test_posZ = 0;

        for (int radius = 0; radius <= Math.ceil((double) min_distance / 16.0); radius++) {

            scanX = -radius;
            scanZ = -radius;
            step = 1;
            explorer_step = radius + radius;

            while (true) {

                // Get Data
                {

                    scan_pos = new ChunkPos(center_chunk.x + scanX, center_chunk.z + scanZ);

                    if (cache_write_tree_location.containsKey(scan_pos) == true) {

                        data = cache_write_tree_location.get(scan_pos);

                    } else {

                        key = (scan_pos.x >> 5) + "," + (scan_pos.z >> 5);

                        if (cache_other_region.containsKey(key) == false) {

                            cache_other_region.put(key, new HashMap<>());

                            // Load From BIN
                            {

                                buffer = FileManager.readBIN(Core.path_world_mod + "/world_gen/tree_locations/" + dimension + "/" + key + ".bin");

                                while (buffer.remaining() > 0) {

                                    try {

                                        test_id = String.valueOf(buffer.getShort());
                                        test_posX = buffer.getInt();
                                        test_posZ = buffer.getInt();

                                    } catch (Exception exception) {

                                        OutsideUtils.exception(new Exception(), exception, "");
                                        return false;

                                    }

                                    cache_other_region.computeIfAbsent(key, create -> new HashMap<>()).computeIfAbsent(new ChunkPos(test_posX >> 4, test_posZ >> 4), create -> new HashMap<>()).put(new BlockPos(test_posX, 0, test_posZ), test_id);

                                }

                            }

                        }

                        if (cache_other_region.get(key).containsKey(scan_pos) == true) {

                            data = cache_other_region.get(key).get(scan_pos);

                        } else {

                            data = new HashMap<>();

                        }

                    }

                }

                if (data.isEmpty() == false) {

                    // Test
                    {

                        for (Map.Entry<BlockPos, String> entry : data.entrySet()) {

                            if (entry.getKey() == center_pos) {

                                return false;

                            } else {

                                if (entry.getValue().equals(id_number) == true) {

                                    if ((Math.abs(centerX - entry.getKey().getX()) <= min_distance) && (Math.abs(centerZ - entry.getKey().getZ()) <= min_distance)) {

                                        return false;

                                    }

                                }

                            }

                        }

                    }

                }

                // Next Point
                {

                    if (step == 1) {

                        scanX = scanX + 1;

                    } else if (step == 2) {

                        scanZ = scanZ + 1;

                    } else if (step == 3) {

                        scanX = scanX - 1;

                    } else {

                        scanZ = scanZ - 1;

                    }

                }

                explorer_step = explorer_step - 1;

                if (explorer_step <= 0) {

                    // Next Step
                    {

                        if (is_first == true) {

                            is_first = false;
                            break;

                        }

                        if (step == 1) {

                            step = 2;

                        } else if (step == 2) {

                            step = 3;

                        } else if (step == 3) {

                            step = 4;

                        } else {

                            break;

                        }

                        explorer_step = radius + radius;

                    }

                }

            }

        }

        return true;

    }

    private static boolean testShoreline (LevelAccessor level_accessor, ChunkPos center_chunk_pos) {

        if (Handcode.Config.shoreline_detection == false) {

            return false;

        } else {

            Holder<Biome> biome_side1 = getBiome(level_accessor, new ChunkPos(center_chunk_pos.x + 1, center_chunk_pos.z + 1));
            Holder<Biome> biome_side2 = getBiome(level_accessor, new ChunkPos(center_chunk_pos.x + 1, center_chunk_pos.z - 1));
            Holder<Biome> biome_side3 = getBiome(level_accessor, new ChunkPos(center_chunk_pos.x - 1, center_chunk_pos.z + 1));
            Holder<Biome> biome_side4 = getBiome(level_accessor, new ChunkPos(center_chunk_pos.x - 1, center_chunk_pos.z - 1));
            boolean waterside_test1 = GameUtils.Environment.test(biome_side1, "#tanshugetrees:water_biomes");
            boolean waterside_test2 = GameUtils.Environment.test(biome_side2, "#tanshugetrees:water_biomes");
            boolean waterside_test3 = GameUtils.Environment.test(biome_side3, "#tanshugetrees:water_biomes");
            boolean waterside_test4 = GameUtils.Environment.test(biome_side4, "#tanshugetrees:water_biomes");
            return waterside_test1 == true || waterside_test2 == true || waterside_test3 == true || waterside_test4 == true;

        }

    }

    private static void writeData (LevelAccessor level_accessor, int centerX, int centerZ, String id, Map<String, String> data) {

        RandomSource random = RandomSource.create(level_accessor.getServer().overworld().getSeed() ^ ((centerX * 341873128712L) + (centerZ * 132897987541L)));
        String path_storage = data.get("path_storage");
        File chosen = new File(Core.path_config + "/dev/temporary/" + path_storage);

        // Random Select File
        {

            File[] list = chosen.listFiles();

            if (list == null) {

                return;

            }

            chosen = new File(chosen.getPath() + "/" + list[random.nextInt(list.length)].getName());

        }

        if (chosen.exists() == true && chosen.isDirectory() == false) {

            int sizeX = 0;
            int sizeY = 0;
            int sizeZ = 0;
            int center_sizeX = 0;
            int center_sizeY = 0;
            int center_sizeZ = 0;

            try {

                short[] size = Caches.TreeShape.getTreeShapeSize(path_storage + "|" + chosen.getName());
                sizeX = size[0];
                sizeY = size[1];
                sizeZ = size[2];
                center_sizeX = size[3];
                center_sizeY = size[4];
                center_sizeZ = size[5];

            } catch (Exception exception) {

                OutsideUtils.exception(new Exception(), exception, "");
                return;

            }

            // Convert Size
            {

                int[] rotation_mirrored = getRotationMirrored(level_accessor, centerX, centerZ, id);

                if (rotation_mirrored == null) {

                    return;

                }

                int[] convert = OutsideUtils.convertSizeRotationMirrored(rotation_mirrored, sizeX, sizeZ, center_sizeX, center_sizeZ);
                sizeX = convert[0];
                sizeZ = convert[1];
                center_sizeX = convert[2];
                center_sizeZ = convert[3];

            }

            int dead_tree_level = getDeadTreeLevel(level_accessor, id, path_storage + "|" + chosen.getName(), centerX, centerZ, false);

            // Coarse Woody Debris
            {

                if (dead_tree_level > 200) {

                    int fallen_direction = random.nextInt(4) + 1;

                    int[] convert = OutsideUtils.convertSizeFallen(fallen_direction, sizeX, sizeY, sizeZ, center_sizeX, center_sizeY, center_sizeZ);
                    sizeX = convert[0];
                    sizeY = convert[1];
                    sizeZ = convert[2];
                    center_sizeX = convert[3];
                    center_sizeY = convert[4];
                    center_sizeZ = convert[5];

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

            // Write Tree Location
            {

                ChunkPos chunk_pos = new ChunkPos(centerX >> 4,centerZ >> 4);
                BlockPos pos = new BlockPos(centerX, 0, centerZ);
                cache_write_tree_location.computeIfAbsent(chunk_pos, create -> new HashMap<>()).put(pos, CacheManager.getDictionary(id, false));

            }

            // Write Place
            {

                List<String> write = new ArrayList<>();
                write.add("s" + CacheManager.getDictionary(id, false));
                write.add("s" + CacheManager.getDictionary(chosen.getName(), false));
                write.add("i" + centerX);
                write.add("i" + centerZ);
                write.add("i" + from_chunkX);
                write.add("i" + from_chunkZ);
                write.add("i" + to_chunkX);
                write.add("i" + to_chunkZ);

                int from_chunkX_test = from_chunkX >> 5;
                int from_chunkZ_test = from_chunkZ >> 5;
                int to_chunkX_test = to_chunkX >> 5;
                int to_chunkZ_test = to_chunkZ >> 5;

                for (int scanX = from_chunkX_test; scanX <= to_chunkX_test; scanX++) {

                    for (int scanZ = from_chunkZ_test; scanZ <= to_chunkZ_test; scanZ++) {

                        cache_write_place.computeIfAbsent(scanX + "," + scanZ, create -> new ArrayList<>()).addAll(write);

                    }

                }

            }

        }

    }

    public static int[] getRotationMirrored (LevelAccessor level_accessor, int centerX, int centerZ, String id) {

        RandomSource random = RandomSource.create(level_accessor.getServer().overworld().getSeed() ^ ((centerX * 341873128712L) + (centerZ * 132897987541L)));
        String rotation = "";
        String mirrored = "";
        
        // Get Config Data
        {

            Map<String, String> data = ConfigDynamic.getData("world_gen").get(id);

            if (data == null) {

                return new int[0];

            }

            rotation = data.get("rotation");
            mirrored = data.get("mirrored");

        }
        
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

            } else if (mirrored.equals("random_x") == true) {

                if (random.nextBoolean() == true) {

                    mirrored = "1";

                } else {

                    mirrored = "0";

                }

            } else if (mirrored.equals("random_z") == true) {

                if (random.nextBoolean() == true) {

                    mirrored = "2";

                } else {

                    mirrored = "0";

                }

            } else {

                mirrored = "0";

            }

        }

        return new int[]{Integer.parseInt(rotation), Integer.parseInt(mirrored)};

    }

    public static int getDeadTreeLevel (LevelAccessor level_accessor, String id, String location, int centerX, int centerZ, boolean unviable_ecology) {

        RandomSource random = RandomSource.create(level_accessor.getServer().overworld().getSeed() ^ ((centerX * 341873128712L) + (centerZ * 132897987541L)));
        double chance = 0.0;
        String level = "";

        // Get Config Data
        {

            Map<String, String> data = ConfigDynamic.getData("world_gen").get(id);

            if (data == null) {

                return 0;

            }

            chance = Double.parseDouble(data.get("dead_tree_chance")) * Handcode.Config.multiply_dead_tree_chance;
            level = data.get("dead_tree_level");

        }

        if (unviable_ecology == true) {

            id = id + "_unviable_ecology";

        } else {

            if (random.nextDouble() >= chance) {

                return 0;

            }

        }

        short[] data = CacheManager.DataShort.getArray("dead_tree_level").get(id);

        if (data == null) {

            List<Short> list = new ArrayList<>();

            if (level.startsWith("auto") == false) {

                {

                    for (String scan : level.split(" / ")) {

                        if (unviable_ecology == false) {

                            if (scan.startsWith("3") == true) {

                                continue;

                            }

                        }

                        list.add(Short.parseShort(scan));

                    }

                }

            } else {

                {

                    short is_pine = 0;

                    if (level.equals("auto_pine") == true) {

                        is_pine = 1;

                    }

                    // Write Data
                    {

                        int count_trunk = 0;
                        int count_bough = 0;
                        int count_branch = 0;
                        int count_limb = 0;
                        int count_twig = 0;
                        int count_sprig = 0;

                        // Get Data
                        {

                            try {

                                int[] count = Caches.TreeShape.getTreeShapeBlockCount(location);
                                count_trunk = count[0];
                                count_bough = count[1];
                                count_branch = count[2];
                                count_limb = count[3];
                                count_twig = count[4];
                                count_sprig = count[5];

                            } catch (Exception exception) {

                                OutsideUtils.exception(new Exception(), exception, "");
                                return 0;

                            }

                        }

                        if (count_trunk > 0) {

                            if (Handcode.Config.dead_tree_auto_level.contains("18") == true) list.add((short) 180);
                            if (Handcode.Config.dead_tree_auto_level.contains("19") == true) list.add((short) 190);

                            if (unviable_ecology == false) {

                                if (Handcode.Config.dead_tree_auto_level.contains("28") == true) list.add((short) 280);
                                if (Handcode.Config.dead_tree_auto_level.contains("29") == true) list.add((short) 290);
                                if (Handcode.Config.dead_tree_auto_level.contains("38") == true) list.add((short) 380);
                                if (Handcode.Config.dead_tree_auto_level.contains("39") == true) list.add((short) 390);

                            }

                        }

                        if (count_bough > 0) {

                            if (Handcode.Config.dead_tree_auto_level.contains("16") == true) list.add((short) 160);
                            if (Handcode.Config.dead_tree_auto_level.contains("17") == true) list.add((short) 170);
                            if (Handcode.Config.dead_tree_auto_level.contains("15") == true) list.add((short) (150 + is_pine));

                            if (unviable_ecology == false) {

                                if (Handcode.Config.dead_tree_auto_level.contains("26") == true) list.add((short) 260);
                                if (Handcode.Config.dead_tree_auto_level.contains("27") == true) list.add((short) 270);
                                if (Handcode.Config.dead_tree_auto_level.contains("25") == true) list.add((short) (250 + is_pine));
                                if (Handcode.Config.dead_tree_auto_level.contains("36") == true) list.add((short) 360);
                                if (Handcode.Config.dead_tree_auto_level.contains("37") == true) list.add((short) 370);
                                if (Handcode.Config.dead_tree_auto_level.contains("35") == true) list.add((short) (350 + is_pine));

                            }

                        }

                        if (count_branch > 0) {

                            if (Handcode.Config.dead_tree_auto_level.contains("14") == true) list.add((short) (140 + is_pine));

                            if (unviable_ecology == false) {

                                if (Handcode.Config.dead_tree_auto_level.contains("24") == true) list.add((short) (240 + is_pine));
                                if (Handcode.Config.dead_tree_auto_level.contains("34") == true) list.add((short) (340 + is_pine));

                            }

                        }

                        if (count_limb > 0) {

                            if (Handcode.Config.dead_tree_auto_level.contains("13") == true) list.add((short) (130 + is_pine));

                            if (unviable_ecology == false) {

                                if (Handcode.Config.dead_tree_auto_level.contains("23") == true) list.add((short) (230 + is_pine));
                                if (Handcode.Config.dead_tree_auto_level.contains("33") == true) list.add((short) (330 + is_pine));

                            }

                        }

                        if (count_twig > 0) {

                            if (Handcode.Config.dead_tree_auto_level.contains("12") == true) list.add((short) (120 + is_pine));

                            if (unviable_ecology == false) {

                                if (Handcode.Config.dead_tree_auto_level.contains("22") == true) list.add((short) (220 + is_pine));
                                if (Handcode.Config.dead_tree_auto_level.contains("32") == true) list.add((short) (320 + is_pine));

                            }

                        }

                        if (count_sprig > 0) {

                            if (Handcode.Config.dead_tree_auto_level.contains("11") == true) list.add((short) (110 + is_pine));

                            if (unviable_ecology == false) {

                                if (Handcode.Config.dead_tree_auto_level.contains("21") == true) list.add((short) (210 + is_pine));
                                if (Handcode.Config.dead_tree_auto_level.contains("31") == true) list.add((short) (310 + is_pine));

                            }

                        }

                    }

                }

            }

            if (list.isEmpty() == true) {

                list.add((short) 0);

            }

            data = OutsideUtils.Data.convertListShortToArrayShort(list);
            CacheManager.DataShort.setArray("dead_tree_level", id, data);

        }

        return data[random.nextInt(data.length)];

    }

}