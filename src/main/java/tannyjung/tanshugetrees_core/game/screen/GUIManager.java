package tannyjung.tanshugetrees_core.game.screen;

import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import tannyjung.tanshugetrees_core.Core;
import tannyjung.tanshugetrees_core.game.NBTManager;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class GUIManager {

    public static void create (String group, String name, Runnable runnable) {

        Storage.map.computeIfAbsent(group, create -> new LinkedHashMap<>()).put(name, runnable);

    }

    public static void open (ServerPlayer player_server, String type, String group, String name) {

        MenuProvider provider = new MenuProvider() {

            @Override
            public Component getDisplayName () {

                return Component.literal(Core.mod_name);

            }

            @Override
            public boolean shouldTriggerClientSideContainerClosingOnOpen () {

                return false;

            }

            @Override
            public AbstractContainerMenu createMenu (int id, Inventory inventory, Player player) {

                return new GUIContainer(id, inventory, new FriendlyByteBuf(Unpooled.buffer()).writeBlockPos(player.blockPosition()));

            }

        };

        if (Storage.getNumber(group, name) == 0) {

            return;

        }

        GUIScreen.player_server_watching.add(player_server);
        NBTManager.Mob.setText(player_server, "gui", "nbt_type", type, true);
        NBTManager.Mob.setText(player_server, "gui", "group", group, true);
        NBTManager.Mob.setText(player_server, "gui", "name", name, true);
        player_server.openMenu(provider, player_server.blockPosition());

    }

    public static void close (Player player) {

        player.closeContainer();

    }

    public static class Storage {

        private static final Map<String, LinkedHashMap<String, Runnable>> map = new HashMap<>();

        public static void draw (String group, String name) {

            Map<String, Runnable> data = map.get(group);

            if (data == null) {

                return;

            }

            Runnable runnable = data.get(name);

            if (runnable == null) {

                return;

            }

            runnable.run();

        }

        public static int getNumber (String group, String name) {

            LinkedHashMap<String, Runnable> data = map.get(group);

            if (data == null) {

                return 0;

            }

            int number = 1;

            for (Map.Entry<String, Runnable> entry : data.entrySet()) {

                if (entry.getKey().equals(name) == true) {

                    return number;

                }

                number = number + 1;

            }

            return 0;

        }

        public static String getNameByNumber (String group, int number) {

            LinkedHashMap<String, Runnable> data = map.get(group);

            if (data == null) {

                return "";

            }

            for (Map.Entry<String, Runnable> entry : data.entrySet()) {

                if (number <= 1) {

                    return entry.getKey();

                }

                number = number - 1;

            }

            return "";

        }

        public static String getNamePrevious (String group, String name) {

            LinkedHashMap<String, Runnable> data = map.get(group);

            if (data == null) {

                return "";

            }

            String previous = "";

            for (Map.Entry<String, Runnable> entry : data.entrySet()) {

                if (entry.getKey().equals(name) == false) {

                    previous = entry.getKey();

                } else {

                    return previous;

                }

            }

            return "";

        }

        public static String getNameNext (String group, String name) {

            LinkedHashMap<String, Runnable> data = map.get(group);

            if (data == null) {

                return "";

            }

            boolean test = false;

            for (Map.Entry<String, Runnable> entry : data.entrySet()) {

                if (test == true) {

                    return entry.getKey();

                }

                if (entry.getKey().equals(name) == true) {

                    test = true;

                }

            }

            return "";

        }

    }

}
