package tannyjung.tanshugetrees_handcode.systems;

import tannyjung.tanshugetrees_core.Core;
import tannyjung.tanshugetrees_core.outside.CacheManager;
import tannyjung.tanshugetrees_core.outside.ConfigDynamic;
import tannyjung.tanshugetrees_core.outside.FileManager;
import tannyjung.tanshugetrees_core.outside.OutsideUtils;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.ShortBuffer;
import java.util.*;

public class Caches {

    private static void getTreeShape (String id) {

        if (CacheManager.Data.existMapStringArrayNumberShort("tree_shape_size", id) == false) {

            short[] data_size = new short[0];
            int[] data_block_count = new int[0];
            short[] data_shape = new short[0];

            // Get Data
            {

                String[] split = id.split("\\|");
                String path = "";

                try {

                    path = Core.path_config + "/dev/temporary/presets/" + split[0] + "/storage/" + split[1];

                } catch (Exception exception) {

                    OutsideUtils.exception(new Exception(), exception, "");

                }

                ByteBuffer buffer = FileManager.readBIN(path);

                if (buffer.remaining() > 0) {

                    // Size
                    {

                        int count = 6;
                        data_size = new short[count];

                        for (int number = 0; number < count; number++) {

                            data_size[number] = buffer.getShort();

                        }

                    }

                    // Block Count
                    {

                        int count = 6;
                        data_block_count = new int[count];

                        for (int number = 0; number < count; number++) {

                            data_block_count[number] = buffer.getInt();

                        }

                    }

                    // Shape
                    {

                        ShortBuffer buffer_convert = buffer.asShortBuffer();
                        data_shape = new short[buffer_convert.remaining()];
                        buffer_convert.get(data_shape);

                    }

                }

            }

            CacheManager.Data.setMapStringArrayNumberShort("tree_shape_size", id, data_size);
            CacheManager.Data.setMapStringArrayNumberInt("tree_shape_block_count", id, data_block_count);
            CacheManager.Data.setMapStringArrayNumberShort("tree_shape_data", id, data_shape);

        }

    }

    public static short[] getTreeShapeSize (String id) {

        getTreeShape(id);
        return CacheManager.Data.getMapStringArrayNumberShort("tree_shape_size").get(id);

    }

    public static int[] getTreeShapeBlockCount (String id) {

        getTreeShape(id);
        return CacheManager.Data.getMapStringArrayNumberInt("tree_shape_block_count").get(id);

    }

    public static short[] getTreeShapeData (String id) {

        getTreeShape(id);
        return CacheManager.Data.getMapStringArrayNumberShort("tree_shape_data").get(id);

    }

    public static List<String> getTreeSettings (String id) {

        if (CacheManager.Data.existMapTextListText("tree_settings", id) == false) {

            List<String> data = FileManager.readTXT(Core.path_config + "/dev/temporary/presets/" + id + ".txt");
            CacheManager.Data.setMapTextListText("tree_settings", id, data);

        }

        return CacheManager.Data.getMapTextListText("tree_settings").get(id);

    }

}
