package tannyjung.tanshugetrees_handcode.systems;

import net.minecraft.world.level.block.state.BlockState;
import tannyjung.tanshugetrees_core.Core;
import tannyjung.tanshugetrees_core.game.GameUtils;
import tannyjung.tanshugetrees_core.outside.CacheManager;
import tannyjung.tanshugetrees_core.outside.FileManager;
import tannyjung.tanshugetrees_core.outside.OutsideUtils;
import tannyjung.tanshugetrees_handcode.Handcode;

import java.nio.ByteBuffer;
import java.nio.ShortBuffer;
import java.util.*;

public class Caches {

    public static class TreeShape {

        private static void getTreeShape (String id) {

            short[] data_size = null;
            int[] data_block_count = null;
            short[] data_shape = null;

            // Get Data
            {

                String path = "";

                // Get path
                {

                    try {

                        String[] split = id.split("\\|");
                        path = Core.path_config + "/dev/temporary/" + split[0] + "/" + split[1];

                    } catch (Exception exception) {

                        OutsideUtils.exception(new Exception(), exception, "");
                        return;

                    }

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

            if (data_size == null) data_size = new short[0];
            if (data_block_count == null) data_block_count = new int[0];
            if (data_shape == null) data_shape = new short[0];

            CacheManager.DataShort.setArray("tree_shape_size", id, data_size);
            CacheManager.DataInt.setArray("tree_shape_block_count", id, data_block_count);
            CacheManager.DataShort.setArray("tree_shape_data", id, data_shape);

        }

        public static short[] getTreeShapeSize (String id) {

            short[] data = CacheManager.DataShort.getArray("tree_shape_size").get(id);

            if (data == null) {

                getTreeShape(id);
                data = CacheManager.DataShort.getArray("tree_shape_size").get(id);

                if (data == null) {

                    data = new short[0];

                }

            }

            return data;

        }

        public static int[] getTreeShapeBlockCount (String id) {

            int[] data = CacheManager.DataInt.getArray("tree_shape_block_count").get(id);

            if (data == null) {

                getTreeShape(id);
                data = CacheManager.DataInt.getArray("tree_shape_block_count").get(id);

                if (data == null) {

                    data = new int[0];

                }

            }

            return data;

        }

        public static short[] getTreeShapeData (String id) {

            short[] data = CacheManager.DataShort.getArray("tree_shape_data").get(id);

            if (data == null) {

                getTreeShape(id);
                data = CacheManager.DataShort.getArray("tree_shape_data").get(id);

                if (data == null) {

                    data = new short[0];

                }

            }

            return data;

        }

    }

    public static class TreeSettings {

        private static void get (String id) {

            Map<String, String> data_normal = new HashMap<>();
            Map<String, String> data_block = new HashMap<>();
            Map<String, String> data_function = new HashMap<>();
            Set<Short> data_keep = new HashSet<>();
            short[] data_leaves_type = new short[2];

            // Get Data
            {

                String[] split = null;
                String key = "";
                String value = "";
                byte leaves_type = 0;

                for (String scan : FileManager.readTXT(Core.path_config + "/dev/temporary/" + id + ".txt")) {

                    if (scan.isEmpty() == false) {

                        try {

                            split = scan.split(" = ");
                            key = split[0];
                            value = split[1];

                        } catch (Exception exception) {

                            OutsideUtils.exception(new Exception(), exception, "");
                            break;

                        }

                        if (key.startsWith("Block ") == true) {

                            {

                                key = key.substring("Block ### ".length());

                                if (value.endsWith(" keep") == true) {

                                    value = value.substring(0, value.length() - " keep".length());
                                    data_keep.add(Short.parseShort(key));

                                }

                                data_block.put(key, value);

                                if (key.startsWith("120") == true) {

                                    if (value.endsWith("]") == true) {

                                        value = value.substring(0, value.indexOf("["));

                                    }

                                    leaves_type = Byte.parseByte(key.substring("120".length()));

                                    if (Handcode.Config.deciduous_leaves_list.contains(value) == true) {

                                        data_leaves_type[leaves_type] = 1;

                                    } else if (Handcode.Config.coniferous_leaves_list.contains(value) == true) {

                                        data_leaves_type[leaves_type] = 2;

                                    }

                                }

                            }

                        } else if (key.startsWith("Function ") == true) {

                            key = key.substring("Function ## ".length());
                            data_function.put(key, value);

                        } else {

                            data_normal.put(key, value);

                        }

                    }

                }

            }

            CacheManager.DataText.setMap("tree_settings_normal", id, data_normal);
            CacheManager.DataText.setMap("tree_settings_block", id, data_block);
            CacheManager.DataText.setMap("tree_settings_function", id, data_function);
            CacheManager.DataShort.setSet("tree_settings_keep", id, data_keep);
            CacheManager.DataShort.setArray("tree_settings_leaves_type", id, data_leaves_type);

        }

        public static Map<String, String> getNormal (String id) {

            Map<String, String> data = CacheManager.DataText.getMap("tree_settings_normal").get(id);

            if (data == null) {

                get(id);
                data = CacheManager.DataText.getMap("tree_settings_normal").get(id);

                if (data == null) {

                    data = new  HashMap<>();

                }

            }

            return data;

        }

        public static Map<Short, BlockState> getBlock (String id) {

            Map<String, String> data = CacheManager.DataText.getMap("tree_settings_block").get(id);

            if (data == null) {

                get(id);
                data = CacheManager.DataText.getMap("tree_settings_block").get(id);

                if (data == null) {

                    data = new HashMap<>();

                }

            }

            Map<Short, BlockState> convert = new HashMap<>();

            for (Map.Entry<String, String> entry : data.entrySet()) {

                convert.put(Short.parseShort(entry.getKey()), GameUtils.Tile.fromText(entry.getValue()));

            }

            return convert;

        }

        public static Map<Short, String> getFunction (String id) {

            Map<String, String> data = CacheManager.DataText.getMap("tree_settings_function").get(id);

            if (data == null) {

                get(id);
                data = CacheManager.DataText.getMap("tree_settings_function").get(id);

                if (data == null) {

                    data = new HashMap<>();

                }

            }

            Map<Short, String> convert = new HashMap<>();

            for (Map.Entry<String, String> entry : data.entrySet()) {

                convert.put(Short.parseShort(entry.getKey()), entry.getValue());

            }

            return convert;

        }

        public static Set<Short> getKeep (String id) {

            Set<Short> data = CacheManager.DataShort.getSet("tree_settings_keep").get(id);

            if (data == null) {

                get(id);
                data = CacheManager.DataShort.getSet("tree_settings_keep").get(id);

                if (data == null) {

                    data = new HashSet<>();

                }

            }

            return data;

        }

        public static short[] getLeavesType (String id) {

            short[] data = CacheManager.DataShort.getArray("tree_settings_leaves_type").get(id);

            if (data == null) {

                get(id);
                data = CacheManager.DataShort.getArray("tree_settings_leaves_type").get(id);

                if (data == null) {

                    data = new short[0];

                }

            }

            return data;

        }

    }

}
