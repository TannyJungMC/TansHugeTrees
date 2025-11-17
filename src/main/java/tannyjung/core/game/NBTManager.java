package tannyjung.core.game;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.TagParser;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class NBTManager {

    public static class entity {

        public static String getText (Entity entity, String name) {

            return entity.getPersistentData().getString(name);

        }

        public static Boolean getLogic (Entity entity, String name) {

            return entity.getPersistentData().getBoolean(name);

        }

        public static double getNumber (Entity entity, String name) {

            return entity.getPersistentData().getDouble(name);

        }

        public static double[] getListNumber (Entity entity, String name) {

            ListTag list = entity.getPersistentData().getList(name, Tag.TAG_DOUBLE);
            double[] convert = new double[list.size()];

            for (int count = 0; count <= list.size() - 1; count++) {

                convert[count] = list.getDouble(count);

            }

            return convert;

        }

        public static double[] getListNumberFloat (Entity entity, String name) {

            ListTag list = entity.getPersistentData().getList(name, Tag.TAG_FLOAT);
            double[] convert = new double[list.size()];

            for (int count = 0; count <= list.size() - 1; count++) {

                convert[count] = list.getFloat(count);

            }

            return convert;

        }

        public static void setText (Entity entity, String name, String value) {

            entity.getPersistentData().putString(name, value);

        }

        public static void setLogic (Entity entity, String name, boolean value) {

            entity.getPersistentData().putBoolean(name, value);

        }

        public static void setNumber (Entity entity, String name, double value) {

            entity.getPersistentData().putDouble(name, value);

        }

        public static void addNumber (Entity entity, String name, double value) {

            entity.getPersistentData().putDouble(name, entity.getPersistentData().getDouble(name) + value);

        }

    }

    public static class block {

        public static String getText (LevelAccessor level_accessor, BlockPos pos, String name) {

            return new Object() {

                public String getValue (LevelAccessor level_accessor, BlockPos pos, String name) {

                    BlockEntity blockEntity = level_accessor.getBlockEntity(pos);

                    if (blockEntity != null) {

                        return blockEntity.getPersistentData().getString(name);

                    }

                    return "";

                }

            }.getValue(level_accessor, pos, name);

        }

        public static double getNumber (LevelAccessor level_accessor, BlockPos pos, String name) {

            return new Object() {

                public double getValue (LevelAccessor level_accessor, BlockPos pos, String name) {

                    BlockEntity blockEntity = level_accessor.getBlockEntity(pos);

                    if (blockEntity != null) {

                        return blockEntity.getPersistentData().getDouble(name);

                    }

                    return 0.0;

                }

            }.getValue(level_accessor, pos, name);

        }

        public static boolean getLogic (LevelAccessor level_accessor, BlockPos pos, String name) {

            return new Object() {

                public boolean getValue (LevelAccessor level_accessor, BlockPos pos, String name) {

                    BlockEntity blockEntity = level_accessor.getBlockEntity(pos);

                    if (blockEntity != null) {

                        return blockEntity.getPersistentData().getBoolean(name);

                    }

                    return false;

                }

            }.getValue(level_accessor, pos, name);

        }

        public static void setText (LevelAccessor level_accessor, BlockPos pos, String name, String value) {

            BlockEntity block_entity = level_accessor.getBlockEntity(pos);

            if (block_entity != null) {

                block_entity.getPersistentData().putString(name, value);
                BlockState block = level_accessor.getBlockState(pos);

                if (level_accessor instanceof ServerLevel level_server) {

                    level_server.sendBlockUpdated(pos, block, block, 2);

                }

            }

        }

        public static void setNumber (LevelAccessor level_accessor, BlockPos pos, String name, double value) {

            BlockEntity block_entity = level_accessor.getBlockEntity(pos);

            if (block_entity != null) {

                block_entity.getPersistentData().putDouble(name, value);
                BlockState block = level_accessor.getBlockState(pos);

                if (level_accessor instanceof ServerLevel level_server) {

                    level_server.sendBlockUpdated(pos, block, block, 2);

                }

            }

        }

        public static void setLogic (LevelAccessor level_accessor, BlockPos pos, String name, boolean value) {

            BlockEntity block_entity = level_accessor.getBlockEntity(pos);

            if (block_entity != null) {

                block_entity.getPersistentData().putBoolean(name, value);
                BlockState block = level_accessor.getBlockState(pos);

                if (level_accessor instanceof ServerLevel level_server) {

                    level_server.sendBlockUpdated(pos, block, block, 2);

                }

            }

        }

        public static void addNumber (LevelAccessor level_accessor, BlockPos pos, String name, double value) {

            BlockEntity block_entity = level_accessor.getBlockEntity(pos);

            if (block_entity != null) {

                block_entity.getPersistentData().putDouble(name, block_entity.getPersistentData().getDouble(name) + value);
                BlockState block = level_accessor.getBlockState(pos);

                if (level_accessor instanceof ServerLevel level_server) {

                    level_server.sendBlockUpdated(pos, block, block, 2);

                }

            }

        }

    }

    public static CompoundTag textToCompoundTag (String id) {

        CompoundTag return_NBT = new CompoundTag();

        {

            try {

                return_NBT = TagParser.parseTag(id.substring(id.indexOf("{")));

            } catch (Exception ignored) {



            }

        }

        return return_NBT;

    }

}
