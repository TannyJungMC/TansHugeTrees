package tannyjung.tanshugetrees_core.game;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.*;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import tannyjung.tanshugetrees_core.Core;
import tannyjung.tanshugetrees_core.game.screen.GUIScreen;
import tannyjung.tanshugetrees_core.game.screen.ScreenDrawing;
import tannyjung.tanshugetrees_core.outside.FileManager;
import tannyjung.tanshugetrees_core.outside.NetworkManager;
import tannyjung.tanshugetrees_core.outside.OutsideUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class NBTManager {

    public static CompoundTag convertJSONToTag (String data) {

        try {

            return TagParser.parseTag(data);

        } catch (Exception ignored) {

            return new CompoundTag();

        }

    }

    public static MutableComponent convertJSONToComponent (String data) {

        try {

            /*
            (1.20.1)
            return Component.Serializer.fromJson(data);
            (1.21.1)
            return Component.Serializer.fromJson(data, RegistryAccess.EMPTY);
            */
            return Component.Serializer.fromJson(data, RegistryAccess.EMPTY);

        } catch (Exception exception) {

            OutsideUtils.exception(new Exception(), exception, "");

        }

        return null;

    }

    public static String convertFileToForgeData (String path) {

        StringBuilder data = new StringBuilder();
        String[] split = null;
        Set<String> normal = new HashSet<>();

        // Add Normal
        {

            normal.add("-");
            normal.add("0");
            normal.add("1");
            normal.add("2");
            normal.add("3");
            normal.add("4");
            normal.add("5");
            normal.add("6");
            normal.add("7");
            normal.add("8");
            normal.add("9");

        }

        for (String scan : FileManager.readTXT(path)) {

            if (scan.isEmpty() == false && scan.startsWith("---") == false) {

                split = scan.split(" = ");

                if (split[1].isEmpty() == true || split[1].equals("none") == true) {

                    continue;

                } else if (normal.contains(split[1].substring(0, 1)) == true) {

                    data.append(split[0]).append(":").append(split[1]);

                } else if (split[1].equals("true") == true || split[1].equals("false") == true) {

                    data.append(split[0]).append(":").append(split[1]);

                } else {

                    data.append(split[0]).append(":\"").append(split[1]).append("\"");

                }

                data.append(",");

            }

        }

        return "{NeoForgeData:{" + Core.mod_id + ":{" + data + "}}}";

    }

    public static String createText (String data) {

        StringBuilder convert = new StringBuilder();
        String[] split = null;
        boolean first = false;

        for (String scan : data.split(" \\| ")) {

            if (first == false) {

                first = true;

            } else {

                convert.append(",");

            }

            split = scan.split(" / ");

            if (split.length == 1) {

                convert.append("{\"text\":\"");
                convert.append(split[0]);
                convert.append("\",\"color\":\"white\"");

            } else {

                convert.append("{\"text\":\"");
                convert.append(split[0]);
                convert.append("\",\"color\":\"");
                convert.append(split[1]);
                convert.append("\"");

                if (split.length == 3) {

                    if (split[2].startsWith("https") == true) {

                        convert.append(",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"");
                        convert.append(split[2]);
                        convert.append("\"}");

                    } else if (split[2].startsWith("/") == true) {

                        convert.append(",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"");
                        convert.append(split[2]);
                        convert.append("\"}");

                    }

                    convert.append(",\"hoverEvent\":{\"action\":\"show_text\",\"contents\":\"");
                    convert.append(split[2]);
                    convert.append("\"}");

                }

            }

            convert.append("}");

        }

        return convert.toString();

    }

    public static String createTextDoubleBackslash (String data) {

        return createText(data).replace("\"", "\\\"");

    }

    public static String createItem (String name, String lore, String custom_data, String forge_data) {

        /*
        (1.20.1)
        String part_name = "display:{Name:\"" + createTextDoubleBackslash(name) + "\"},";
        String part_lore = "Lore:[\"" + createTextDoubleBackslash(lore) + "\"],";
        String part_custom_data = "tag:{" + Core.mod_id + ":{" + custom_data + "}},";
        String part_forge_data = "BlockEntityData:{NeoForgeData:{" + Core.mod_id + ":{" + forge_data + "}}},";
        (1.21.1)
        String part_name = "custom_name:\"" + createTextDoubleBackslash(name) + "\",";
        String part_lore = "lore:[\"" + createTextDoubleBackslash(lore) + "\"],";
        String part_custom_data = "custom_data:{" + custom_data + "},";
        String part_forge_data = "entity_block_data:{id:\"\",NeoForgeData:{" + Core.mod_id + ":{" + forge_data + "}}},";
        */
        String part_name = "custom_name:\"" + createTextDoubleBackslash(name) + "\",";
        String part_lore = "lore:[\"" + createTextDoubleBackslash(lore) + "\"],";
        String part_custom_data = "custom_data:{" + custom_data + "},";
        String part_forge_data = "entity_block_data:{id:\"\",NeoForgeData:{" + Core.mod_id + ":{" + forge_data + "}}},";

        StringBuilder write = new StringBuilder();
        if (name.isEmpty() == false) write.append(part_name);
        if (lore.isEmpty() == false) write.append(part_lore);
        if (custom_data.isEmpty() == false) write.append(part_custom_data);
        if (forge_data.isEmpty() == false) write.append(part_forge_data);
        return write.toString();

    }

    public static class PutInTag {

        private static CompoundTag putType (String type, CompoundTag tag) {

            CompoundTag tag1 = new CompoundTag();
            tag1.put(type, tag);
            CompoundTag tag2 = new CompoundTag();
            tag2.put(Core.mod_id, tag1);
            return tag2;

        }

        public static CompoundTag fromText (String type, String name, String value) {

            CompoundTag tag = new CompoundTag();
            tag.putString(name, value);
            return putType(type, tag);

        }

        public static CompoundTag fromLogic (String type, String name, boolean value) {

            CompoundTag tag = new CompoundTag();
            tag.putBoolean(name, value);
            return putType(type, tag);

        }

        public static CompoundTag fromNumber (String type, String name, double value) {

            CompoundTag tag = new CompoundTag();
            tag.putDouble(name, value);
            return putType(type, tag);

        }

        public static CompoundTag fromList (String type, String name, ListTag value) {

            CompoundTag tag = new CompoundTag();
            tag.put(name, value);
            return putType(type, tag);

        }

    }

    public static class Mob {

        private static void sync (ServerPlayer player_server, CompoundTag tag) {

            if (tag.sizeInBytes() / 1024 < 900) {

                NetworkManager.runClientCore(player_server, "nbt", "sync", tag);
                ScreenDrawing.refresh_server = true;

            } else {

                Core.logger.error("Detected too big player NBT size sending to client, over 900 MB. Skip sending and reset that server player data.");

                CompoundTag tag_new = new CompoundTag();
                tag_new.put(Core.mod_id, new CompoundTag());
                merge(player_server, tag_new, true);

            }

        }

        public static void merge (Entity entity, CompoundTag tag, boolean should_sync) {

            entity.getPersistentData().merge(tag);

            if (should_sync == true) {

                if (entity instanceof ServerPlayer player_server) {

                    sync(player_server, tag);

                }

            }

        }

        public static CompoundTag getAll (Entity entity) {

            return entity.getPersistentData().getCompound(Core.mod_id);

        }

        public static CompoundTag getAllForMerge (Entity entity) {

            CompoundTag tag = new CompoundTag();
            tag.put(Core.mod_id, getAll(entity));
            return tag;

        }

        public static String getText (Entity entity, String type, String name) {

            CompoundTag tag = getAll(entity).getCompound(type);

            /*
            (1.20.1) (1.21.1)
            return tag.getString(name);
            (1.21.8)
            return tag.getString(name).get();
            */
            return tag.getString(name);

        }

        public static void setText (Entity entity, String type, String name, String value, boolean should_sync) {

            merge(entity, NBTManager.PutInTag.fromText(type, name, value), should_sync);

        }

        public static boolean getLogic (Entity entity, String type, String name) {

            CompoundTag tag = getAll(entity).getCompound(type);

            /*
            (1.20.1) (1.21.1)
            return tag.getBoolean(name);
            (1.21.8)
            return tag.getBoolean(name).get();
            */
            return tag.getBoolean(name);

        }

        public static void setLogic (Entity entity, String type, String name, boolean value, boolean should_sync) {

            merge(entity, NBTManager.PutInTag.fromLogic(type, name, value), should_sync);

        }

        public static double getNumber (Entity entity, String type, String name) {

            CompoundTag tag = getAll(entity).getCompound(type);

            /*
            (1.20.1) (1.21.1)
            return tag.getDouble(name);
            (1.21.8)
            return tag.getDouble(name).get();
            */
            return tag.getDouble(name);

        }

        public static void setNumber (Entity entity, String type, String name, double value, boolean should_sync) {

            merge(entity, NBTManager.PutInTag.fromNumber(type, name, value), should_sync);

        }

        public static void addNumber (Entity entity, String type, String name, double value, boolean should_sync) {

            value = getNumber(entity, type, name) + value;
            setNumber(entity, type, name, value, should_sync);

        }

        public static ListTag getList (Entity entity, String type, String name) {

            /*
            (1.20.1) (1.21.1)
            return getAll(entity).getCompound(type).getList(name, Tag.TAG_STRING);
            (1.21.8)
            return getAll(entity).getCompound(type).getList(name).get();
            */
            return getAll(entity).getCompound(type).getList(name, Tag.TAG_STRING);

        }

        public static List<String> getListAsList (Entity entity, String type, String name) {

            ListTag list = getList(entity, type, name);
            List<String> convert = new ArrayList<>();

            for (int number = 0; number < list.size(); number++) {

                    /*
                    (1.20.1) (1.21.1)
                    convert.add(list.getString(number));
                    (1.21.8)
                    convert.add(list.getString(number)).get();
                    */
                convert.add(list.getString(number));

            }

            return convert;

        }

        public static void setList (Entity entity, String type, String name, ListTag value, boolean should_sync) {

            merge(entity, NBTManager.PutInTag.fromList(type, name, value), should_sync);

        }

        public static void setListAlternate (Entity entity, String type, String name, List<String> value, boolean should_sync) {

            ListTag list = new ListTag();

            for (String scan : value) {

                list.add(StringTag.valueOf(scan));

            }

            setList(entity, type, name, list, should_sync);

        }

        public static void addList (Entity entity, String type, String name, ListTag value, boolean should_sync) {

            ListTag list = getList(entity, type, name);
            list.add(value);

            merge(entity, NBTManager.PutInTag.fromList(type, name, list), should_sync);

        }

        public static void addListAlternate (Entity entity, String type, String name, String value, boolean should_sync) {

            ListTag list = new ListTag();
            list.add(StringTag.valueOf(value));
            addList(entity, type, name, list, should_sync);

        }

        public static void addListMultiple (Entity entity, String type, String name, List<String> value, boolean should_sync) {

            ListTag list = getList(entity, type, name);

            for (String scan : value) {

                list.add(StringTag.valueOf(scan));

            }

            merge(entity, NBTManager.PutInTag.fromList(type, name, list), should_sync);

        }

    }

    public static class Tile {

        public static boolean getLogic (ServerLevel level_server, BlockPos pos, String type, String name) {

            return new Object() {

                public boolean getValue (LevelAccessor level_accessor, BlockPos pos, String name) {

                    BlockEntity entity = level_accessor.getBlockEntity(pos);

                    if (entity != null) {

                        CompoundTag tag = entity.getPersistentData().getCompound(Core.mod_id);

                        if (type.isEmpty() == false) {

                            tag = tag.getCompound(type);

                        }

                        /*
                        (1.20.1) (1.21.1)
                        return tag.getBoolean(name);
                        (1.21.8)
                        return tag.getBoolean(name).get();
                        */
                        return tag.getBoolean(name);

                    }

                    return false;

                }

            }.getValue(level_server, pos, name);

        }

        public static String getText (ServerLevel level_server, BlockPos pos, String type, String name) {

            return new Object() {

                public String getValue (LevelAccessor level_accessor, BlockPos pos, String name) {

                    BlockEntity entity = level_accessor.getBlockEntity(pos);

                    if (entity != null) {

                        CompoundTag tag = entity.getPersistentData().getCompound(Core.mod_id);

                        if (type.isEmpty() == false) {

                            tag = tag.getCompound(type);

                        }

                        /*
                        (1.20.1) (1.21.1)
                        return tag.getString(name);
                        (1.21.8)
                        return tag.getString(name).get();
                        */
                        return tag.getString(name);

                    }
                    
                    return "";

                }

            }.getValue(level_server, pos, name);

        }

        public static double getNumber (ServerLevel level_server, BlockPos pos, String type, String name) {

            return new Object() {

                public double getValue (LevelAccessor level_accessor, BlockPos pos, String name) {

                    BlockEntity entity = level_accessor.getBlockEntity(pos);

                    if (entity != null) {

                        CompoundTag tag = entity.getPersistentData().getCompound(Core.mod_id);

                        if (type.isEmpty() == false) {

                            tag = tag.getCompound(type);

                        }

                        /*
                        (1.20.1) (1.21.1)
                        return tag.getString(name);
                        (1.21.8)
                        return tag.getString(name).get();
                        */
                        return tag.getDouble(name);

                    }
                    
                    return 0.0;

                }

            }.getValue(level_server, pos, name);

        }
        
        private static void set (ServerLevel level_server, BlockPos pos, BlockEntity entity_block, String type, CompoundTag tag_value) {

            CompoundTag tag = new CompoundTag();

            if (type.isEmpty() == false) {

                CompoundTag tag_type = new CompoundTag();
                tag_type.put(type, tag_value);
                tag.put(Core.mod_id, tag_type);

            } else {

                tag.put(Core.mod_id, tag_value);

            }

            entity_block.getPersistentData().merge(tag);
            BlockState block = level_server.getBlockState(pos);
            level_server.sendBlockUpdated(pos, block, block, 2);
            
        }

        public static void setLogic (ServerLevel level_server, BlockPos pos, String type, String name, boolean value) {

            BlockEntity entity_block = level_server.getBlockEntity(pos);

            if (entity_block != null) {

                CompoundTag tag = new CompoundTag();
                tag.putBoolean(name, value);
                set(level_server, pos, entity_block, type, tag);

            }

        }

        public static void setText (ServerLevel level_server, BlockPos pos, String type, String name, String value) {

            BlockEntity entity_block = level_server.getBlockEntity(pos);

            if (entity_block != null) {

                CompoundTag tag = new CompoundTag();
                tag.putString(name, value);
                set(level_server, pos, entity_block, type, tag);

            }

        }

        public static void setNumber (ServerLevel level_server, BlockPos pos, String type, String name, double value) {

            BlockEntity entity_block = level_server.getBlockEntity(pos);

            if (entity_block != null) {

                CompoundTag tag = new CompoundTag();
                tag.putDouble(name, value);
                set(level_server, pos, entity_block, type, tag);

            }

        }

        public static void addNumber (ServerLevel level_server, BlockPos pos, String type, String name, double value) {

            value = getNumber(level_server, pos, type, name) + value;
            setNumber(level_server, pos, type, name, value);

        }

    }

    public static class Item {

        public static CompoundTag getAll (ItemStack item) {

            /*
            (1.20.1)
            return item.getOrCreateTag().getCompound(Core.mod_id);
            (1.21.1)
            return item.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getCompound(Core.mod_id);
            */
            return item.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getCompound(Core.mod_id);

        }

        private static void merge (ItemStack item, CompoundTag tag) {

            /*
            (1.20.1)
            item.getOrCreateTag().merge(tag);
            (1.21.1)
            CustomData.update(DataComponents.CUSTOM_DATA, item, original -> original.merge(tag));
            */
            CustomData.update(DataComponents.CUSTOM_DATA, item, original -> original.merge(tag));

            ScreenDrawing.refresh_server = true;

        }

        public static boolean getLogic (ItemStack item, String type, String name) {

            CompoundTag tag = getAll(item).getCompound(type);
            return tag.getBoolean(name);

        }

        public static void setLogic (ItemStack item, String type, String name, boolean value) {

            merge(item, NBTManager.PutInTag.fromLogic(type, name, value));

        }

        public static String getText (ItemStack item, String type, String name) {

            CompoundTag tag = getAll(item).getCompound(type);
            return tag.getString(name);

        }

        public static void setText (ItemStack item, String type, String name, String value) {

            merge(item, NBTManager.PutInTag.fromText(type, name, value));

        }

        public static double getNumber (ItemStack item, String type, String name) {

            CompoundTag tag = getAll(item).getCompound(type);
            return tag.getDouble(name);

        }

        public static void setNumber (ItemStack item, String type, String name, double value) {

            merge(item, NBTManager.PutInTag.fromNumber(type, name, value));

        }

        public static void addNumber (ItemStack item, String type, String name, double value) {

            value = getNumber(item, type, name) + value;
            setNumber(item, type, name, value);

        }

        public static ListTag getList (ItemStack item, String type, String name) {

            CompoundTag tag = getAll(item).getCompound(type);
            return tag.getList(name, Tag.TAG_STRING);

        }

        public static List<String> getListAsList (ItemStack item, String type, String name) {

            ListTag list = getList(item, type, name);
            List<String> convert = new ArrayList<>();

            for (int number = 0; number < list.size(); number++) {

                    /*
                    (1.20.1) (1.21.1)
                    convert.add(list.getString(number));
                    (1.21.8)
                    convert.add(list.getString(number)).get();
                    */
                convert.add(list.getString(number));

            }

            return convert;

        }

        public static void setList (ItemStack item, String type, String name, ListTag value) {

            merge(item, NBTManager.PutInTag.fromList(type, name, value));

        }

        public static void setListAlternate (ItemStack item, String type, String name, List<String> value) {

            ListTag list = new ListTag();

            for (String scan : value) {

                list.add(StringTag.valueOf(scan));

            }

            setList(item, type, name, list);

        }

        public static void addList (ItemStack item, String type, String name, ListTag value) {

            ListTag list = getList(item, type, name);
            list.add(value);

            merge(item, NBTManager.PutInTag.fromList(type, name, list));

        }

        public static void addListAlternate (ItemStack item, String type, String name, String value) {

            ListTag list = new ListTag();
            list.add(StringTag.valueOf(value));
            addList(item, type, name, list);

        }

        public static void addListMultiple (ItemStack item, String type, String name, List<String> value) {

            ListTag list = getList(item, type, name);

            for (String scan : value) {

                list.add(StringTag.valueOf(scan));

            }

            merge(item, NBTManager.PutInTag.fromList(type, name, list));

        }

    }

}
