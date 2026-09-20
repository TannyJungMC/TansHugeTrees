package tannyjung.tanshugetrees_core.game;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import tannyjung.tanshugetrees_core.outside.CacheManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BlockManager {

    public static boolean test (BlockState block, String test) {

        if (test.isEmpty() == true) {

            return false;

        } else if (test.equals("all") == true) {

            return true;

        } else {

            String key = block + " -> " + test;

            if (CacheManager.DataLogic.existNormal("test_block", key) == false) {

                boolean result = false;

                {

                    String[] data = toText(block);
                    String block_id = data[0];
                    List<String> properties = new ArrayList<>();

                    if (data[1].isEmpty() == false) {

                        properties = Arrays.stream(data[1].substring(1, data[1].length() - 1).split(",")).toList();

                    }

                    String value = "";
                    int index = 0;
                    String block_test = "";

                    for (String split : test.split(" / ")) {

                        result = true;

                        for (String split2 : split.split(", ")) {

                            value = split2.replaceAll("[#!]", "");

                            {

                                if (split2.startsWith("#") == true || split2.startsWith("!#") == true) {

                                    try {

                                        if (block.is(BlockTags.create(ResourceLocation.parse(value))) == false) {

                                            result = false;

                                        }

                                    } catch (Exception ignored) {

                                        result = false;

                                    }

                                } else {

                                    index = value.indexOf("[");

                                    if (index == -1) {

                                        block_test = value;

                                        if (block_id.equals(block_test) == false) {

                                            result = false;

                                        }

                                    } else {

                                        block_test = value.substring(0, index);

                                        if (block_id.equals(block_test) == false) {

                                            result = false;

                                        } else {

                                            for (String property : value.substring(index + 1, value.length() - 1).split(",")) {

                                                if (properties.contains(property) == false) {

                                                    result = false;
                                                    break;

                                                }

                                            }

                                        }

                                    }

                                }

                                if (split2.startsWith("!") == true) {

                                    result = !result;

                                }

                            }

                            if (result == false) {

                                break;

                            }

                        }

                        if (result == true) {

                            break;

                        }

                    }

                }

                CacheManager.DataLogic.setNormal("test_block", key, result);

            }

            return CacheManager.DataLogic.getNormal("test_block").get(key);

        }

    }

    public static void set (LevelAccessor level_accessor, BlockPos pos, BlockState block, boolean is_world_gen) {

        // World Height Limit
        {

            if (GameUtils.Space.getBuildHeight(level_accessor, false) > pos.getY()) {

                return;

            } else if (GameUtils.Space.getBuildHeight(level_accessor, true) < pos.getY()) {

                return;

            }

        }

        // Waterlogged
        {

            if (level_accessor.isWaterAt(pos) == true) {

                block = setPropertyLogic(block, "waterlogged", true);

            }

        }

        int type = 0;

        if (is_world_gen == false) {

            type = 2;

        }

        level_accessor.setBlock(pos, block, type);

    }

    public static void remove (LevelAccessor level_accessor, ServerLevel level_server, BlockPos pos, boolean is_world_gen) {

        // World Height Limit
        {

            if (GameUtils.Space.getBuildHeight(level_accessor, false) > pos.getY()) {

                return;

            } else if (GameUtils.Space.getBuildHeight(level_accessor, true) < pos.getY()) {

                return;

            }

        }

        BlockState block = null;

        if (level_accessor.isWaterAt(pos) == true) {

            block = Blocks.WATER.defaultBlockState();

        } else {

            block = Blocks.AIR.defaultBlockState();

        }

        set(level_accessor, pos, block, is_world_gen);

        if (is_world_gen == false) {

            level_server.neighborChanged(pos.above(), level_server.getBlockState(pos.above()).getBlock(), pos);

        }

    }

    public static void removeDrop (LevelAccessor level_accessor, ServerLevel level_server, BlockPos pos) {

        ItemManager.spawn(level_server, pos.getCenter(), level_accessor.getBlockState(pos).getBlock().asItem().getDefaultInstance());
        remove(level_accessor, level_server, pos, false);

    }

    public static BlockState fromText (LevelAccessor level_accessor, String data) {

        BlockState block = null;

        // Get Block
        {

            Block get = null;
            String id = data;

            if (id.endsWith("}") == true) {

                id = id.substring(0, id.indexOf("{"));

            }

            if (id.endsWith("]") == true) {

                id = id.substring(0, id.indexOf("["));

            }

            get = level_accessor.registryAccess().registryOrThrow(Registries.BLOCK).get(ResourceLocation.parse(id));

            if (get == null) {

                return Blocks.AIR.defaultBlockState();

            }

            block = get.defaultBlockState();

        }

        if (data.endsWith("}") == true) {

            data = data.substring(0, data.indexOf("{"));
            // TODO -> Put "{...}" from data into the block

        }

        if (data.endsWith("]") == true) {

            {

                String[] properties = data.substring(data.indexOf("[") + 1, data.length() - 1).split(",");

                for (String scan : properties) {

                    String[] get = scan.split("=");
                    Property<?> test = block.getBlock().getStateDefinition().getProperty(get[0]);

                    if (test instanceof BooleanProperty == true) {

                        block = setPropertyLogic(block, get[0], Boolean.parseBoolean(get[1]));

                    } else if (test instanceof IntegerProperty == true) {

                        block = setPropertyNumber(block, get[0], Integer.parseInt(get[1]));

                    } else if (test instanceof EnumProperty<?> == true) {

                        block = setPropertyCustom(block, get[0], get[1]);

                    }

                }

            }

        }

        return block;

    }

    public static String[] toText (BlockState block) {

        String[] split = block.toString().substring("Block{".length()).split("}");

        if (split.length == 1) {

            split = new String[]{split[0], ""};

        }

        return split;

    }

    public static BlockState randomRotation (BlockState block) {

        if (Math.random() < 0.25) {

            return setPropertyCustom(block, "facing", "north");

        } else if (Math.random() < 0.25) {

            return setPropertyCustom(block, "facing", "west");

        } else if (Math.random() < 0.25) {

            return setPropertyCustom(block, "facing", "east");

        } else {

            return setPropertyCustom(block, "facing", "south");

        }

    }

    public static void setScheduleTick (ServerLevel level_server, BlockPos pos, int value) {

        level_server.scheduleTick(pos, level_server.getBlockState(pos).getBlock(), value);

    }

    public static boolean isPassable (LevelAccessor level_accessor, BlockPos pos) {

        return level_accessor.getBlockState(pos).getCollisionShape(level_accessor, pos).isEmpty() == true;

    }

    public static boolean getPropertyLogic (BlockState block, String name) {

        Property<?> property = block.getBlock().getStateDefinition().getProperty(name);

        if (property instanceof BooleanProperty == true) {

            return Boolean.parseBoolean(block.getValue(property).toString());

        }

        return false;

    }

    public static int getPropertyNumber (BlockState block, String name) {

        Property<?> property = block.getBlock().getStateDefinition().getProperty(name);

        if (property instanceof IntegerProperty == true) {

            return Integer.parseInt(block.getValue(property).toString());

        }

        return 0;

    }

    public static String getPropertyCustom (BlockState block, String name) {

        Property<?> property = block.getBlock().getStateDefinition().getProperty(name);

        if (property instanceof EnumProperty<?> == true) {

            return block.getValue(property).toString();

        }

        return "";

    }

    public static BlockState setPropertyLogic (BlockState block, String name, boolean value) {

        Property<?> property = block.getBlock().getStateDefinition().getProperty(name);

        if (block.hasProperty(property) == true) {

            if (property instanceof BooleanProperty property_instance) {

                if (property_instance.getValue(String.valueOf(value)).isPresent() == true) {

                    block = block.setValue(property_instance, value);

                }

            }

        }

        return block;

    }

    public static BlockState setPropertyNumber (BlockState block, String name, int value) {

        Property<?> property = block.getBlock().getStateDefinition().getProperty(name);

        if (block.hasProperty(property) == true) {

            if (property instanceof IntegerProperty property_instance) {

                if (property_instance.getValue(String.valueOf(value)).isPresent() == true) {

                    block = block.setValue(property_instance, value);

                }

            }

        }

        return block;

    }

    public static BlockState setPropertyCustom (BlockState block, String name, String value) {

        Property<?> property = block.getBlock().getStateDefinition().getProperty(name);

        if (property instanceof EnumProperty property_instance) {

            if (property_instance.getValue(value).isPresent() == true) {

                block = block.setValue(property_instance, (Enum) property_instance.getValue(value).get());

            }

        }

        return block;

    }
    
}
