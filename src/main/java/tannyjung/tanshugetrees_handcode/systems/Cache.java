package tannyjung.tanshugetrees_handcode.systems;

import tannyjung.tanshugetrees_core.Core;
import tannyjung.tanshugetrees_core.outside.CacheManager;
import tannyjung.tanshugetrees_core.outside.FileManager;
import tannyjung.tanshugetrees_core.outside.OutsideUtils;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.ShortBuffer;
import java.util.*;

public class Cache {

    private static void getTreeShape (String id) {

        if (CacheManager.cache_number_short_list.containsKey("tree_shape_part1") == false) {

            CacheManager.cache_number_short_list.put("tree_shape_part1", new HashMap<>());
            CacheManager.cache_number_int_list.put("tree_shape_part2", new HashMap<>());
            CacheManager.cache_number_short_list.put("tree_shape_part3", new HashMap<>());

        }

        if (CacheManager.cache_number_short_list.get("tree_shape_part1").containsKey(id) == false) {

            short[] data1 = new short[0];
            int[] data2 = new int[0];
            short[] data3 = new short[0];

            get_data:
            {

                String[] split = id.split("/");
                String path = "";

                try {

                    path = Core.path_config + "/#dev/temporary/presets/" + split[0] + "/" + split[1] + "/storage/" + split[2];

                } catch (Exception exception) {

                    OutsideUtils.exception(new Exception(), exception, "");
                    break get_data;

                }

                ByteBuffer buffer = FileManager.readBIN(path);

                if (buffer.remaining() > 0) {

                    // Part 1
                    {

                        int count = 6;
                        data1 = new short[count];

                        for (int number = 0; number < count; number++) {

                            data1[number] = buffer.getShort();

                        }

                    }

                    // Part 2
                    {

                        int count = 6;
                        data2 = new int[count];

                        for (int number = 0; number < count; number++) {

                            data2[number] = buffer.getInt();

                        }

                    }

                    // Part 3
                    {

                        ShortBuffer buffer_convert = buffer.asShortBuffer();
                        data3 = new short[buffer_convert.remaining()];
                        buffer_convert.get(data3);

                    }

                }

            }

            CacheManager.cache_number_short_list.get("tree_shape_part1").put(id, data1);
            CacheManager.cache_number_int_list.get("tree_shape_part2").put(id, data2);
            CacheManager.cache_number_short_list.get("tree_shape_part3").put(id, data3);

        }

    }

    public static short[] getTreeShapePart1 (String id) {

        getTreeShape(id);
        return CacheManager.cache_number_short_list.get("tree_shape_part1").get(id);

    }

    public static int[] getTreeShapePart2 (String id) {

        getTreeShape(id);
        return CacheManager.cache_number_int_list.get("tree_shape_part2").get(id);

    }

    public static short[] getTreeShapePart3 (String id) {

        getTreeShape(id);
        return CacheManager.cache_number_short_list.get("tree_shape_part3").get(id);

    }

    public static String[] getWorldGenSettings (String id) {

        if (CacheManager.cache_string_list.containsKey("world_gen_settings") == false) {

            CacheManager.cache_string_list.put("world_gen_settings", new HashMap<>());

        }

        if (CacheManager.cache_string_list.get("world_gen_settings").containsKey(id) == false) {

            String[] data = FileManager.readTXT(Core.path_config + "/#dev/temporary/world_gen/" + id + ".txt");
            CacheManager.cache_string_list.get("world_gen_settings").put(id, data);

        }

        return CacheManager.cache_string_list.get("world_gen_settings").get(id);

    }

    public static String[] getTreeSettings (String id) {

        if (CacheManager.cache_string_list.containsKey("tree_settings") == false) {

            CacheManager.cache_string_list.put("tree_settings", new HashMap<>());

        }

        if (CacheManager.cache_string_list.get("tree_settings").containsKey(id) == false) {

            String[] data = FileManager.readTXT(Core.path_config + "/#dev/temporary/presets/" + id + "_settings.txt");
            CacheManager.cache_string_list.get("tree_settings").put(id, data);

        }

        return CacheManager.cache_string_list.get("tree_settings").get(id);

    }

    public static String[] getTreeDecorationList (String id) {

        if (CacheManager.cache_string_list.containsKey("tree_decoration") == false) {

            CacheManager.cache_string_list.put("tree_decoration", new HashMap<>());

        }

        if (CacheManager.cache_string_list.get("tree_decoration").containsKey(id) == false) {

            String[] data = new String[0];

            // Get Data
            {

                File[] packs = new File(Core.path_config + "/#dev/temporary/tree_decoration").listFiles();

                if (packs != null) {

                    List<String> names = new ArrayList<>();
                    File[] files = new File[0];
                    String path_prefix = "";

                    for (File pack : packs) {

                        if (id.equals("decay") == true) {

                            path_prefix = pack.getName() + "/decay";

                        } else {

                            path_prefix = pack.getName();

                        }

                        files = new File(Core.path_config + "/#dev/temporary/tree_decoration/" + path_prefix).listFiles();

                        if (files != null) {

                            for (File file : files) {

                                if (file.isDirectory() == false) {

                                    names.add(path_prefix + "/" + file.getName().replace(".txt", ""));

                                }

                            }

                        }

                    }

                    data = OutsideUtils.convertListToArray(names);

                }

            }

            CacheManager.cache_string_list.get("tree_decoration").put(id, data);

        }

        return CacheManager.cache_string_list.get("tree_decoration").get(id);

    }

}
