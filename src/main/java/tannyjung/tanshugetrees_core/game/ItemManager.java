package tannyjung.tanshugetrees_core.game;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class ItemManager {

    public static void spawn (ServerLevel level_server, Vec3 vec3, ItemStack item) {

        level_server.addFreshEntity(new ItemEntity(level_server, vec3.x, vec3.y, vec3.z, item));

    }

    public static String toID (ItemStack item) {

        return String.valueOf(item.getItem());

    }

    public static ItemStack fromID (ServerLevel level_server, String id) {

        Item item = level_server.registryAccess().registryOrThrow(Registries.ITEM).get(ResourceLocation.parse(id));

        if (item == null) {

            return ItemStack.EMPTY;

        }

        return item.getDefaultInstance();

    }

    public static boolean isTaggedAs (ItemStack item, String tag) {

        return item.is(ItemTags.create(ResourceLocation.parse(tag))) == true;

    }

    public static void give (ServerPlayer player_server, ItemStack item) {

        player_server.getInventory().setItem(player_server.getInventory().getFreeSlot(), item);

    }

    public static void removeFromBody (ServerPlayer player_server, Item item) {

        player_server.getInventory().clearOrCountMatchingItems(scan -> scan.is(item) == true, 3, player_server.inventoryMenu.getCraftSlots());

    }

    public static void setCooldown (Entity entity, ItemStack item, int tick) {

        if (entity instanceof ServerPlayer player_server) {

            player_server.getCooldowns().addCooldown(item.getItem(), tick);

        }

    }

    public static void addDurability (ItemStack item, int value) {

        int damage = item.getDamageValue() - value;

        if (damage < 0) {

            damage = 0;

        }

        item.setDamageValue(damage);

    }

    public static void reduceDurability (ItemStack item, int value) {

        int damage = item.getDamageValue() + value;
        item.setDamageValue(damage);

        if (item.getMaxDamage() < damage) {

            item.shrink(1);

        }

    }

}
