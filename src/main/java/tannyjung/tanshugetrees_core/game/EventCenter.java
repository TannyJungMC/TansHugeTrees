package tannyjung.tanshugetrees_core.game;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.storage.LevelResource;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import tannyjung.tanshugetrees_core.Core;
import tannyjung.tanshugetrees_handcode.Handcode;
import tannyjung.tanshugetrees_handcode.data.DataMigration;
import tannyjung.tanshugetrees_handcode.data.DataRepair;
import tannyjung.tanshugetrees_handcode.data.FileConfig;
import tannyjung.tanshugetrees_handcode.data.TannyPack;
import tannyjung.tanshugetrees_handcode.systems.Commands;
import tannyjung.tanshugetrees_handcode.systems.Events;
import tannyjung.tanshugetrees_handcode.systems.Overlay;

/*
(Forge)
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.level.ChunkEvent;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.api.distmarker.Dist;
(NeoForge)
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.level.ChunkEvent;
import net.neoforged.neoforge.event.server.ServerAboutToStartEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.neoforge.event.server.ServerStoppingEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.api.distmarker.Dist;
*/
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.level.ChunkEvent;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.api.distmarker.Dist;

public class EventCenter {

    /*
    (Forge)
    @Mod.EventBusSubscriber({Dist.CLIENT})
    (NeoForge)
    @EventBusSubscriber({Dist.CLIENT})
    */
    @Mod.EventBusSubscriber({Dist.CLIENT})
    public static class client {

        @SubscribeEvent(priority = EventPriority.NORMAL)
        public static void eventMenu (ScreenEvent.Render.Post event) {

            Screen screen = event.getScreen();
            GuiGraphics graphic = event.getGuiGraphics();
            int screen_width = event.getScreen().width;
            int screen_height = event.getScreen().height;Overlay.eventMenu(screen, graphic, screen_width, screen_height);

        }

        @SubscribeEvent(priority = EventPriority.NORMAL)
        public static void eventInGame (RenderGuiEvent.Post event) {

            GuiGraphics graphic = event.getGuiGraphics();
            int screen_width = event.getWindow().getWidth();
            int screen_height = event.getWindow().getHeight();
            Overlay.eventInGame(graphic, screen_width, screen_height);

        }

    }

    /*
    (Forge)
    @Mod.EventBusSubscriber
    (NeoForge)
    @EventBusSubscriber
    */
    @Mod.EventBusSubscriber
    public static class server {

        private static boolean first_player_joined = false;

        @SubscribeEvent
        public static void eventWorldAboutToStart(ServerAboutToStartEvent event) {

            Core.path_world_data = event.getServer().getWorldPath(new LevelResource(".")) + "/data/tannyjung_" + Core.mod_id;
            DataMigration.run("world");
            Handcode.restart(null, "config", true);

        }

        @SubscribeEvent
        public static void eventWorldStarted(ServerStartedEvent event) {

            ServerLevel level_server = event.getServer().overworld();
            Handcode.restart(level_server, "world", false);

        }

        @SubscribeEvent
        public static void eventWorldStopping(ServerStoppingEvent event) {

            first_player_joined = false;
            Core.loop.restart = true;

        }

        @SubscribeEvent
        public static void eventChunkLoaded(ChunkEvent.Load event) {

            if (event.isNewChunk() == true) {

                String dimension = GameUtils.misc.getCurrentDimensionID((Level) event.getLevel()).replace(":", "-");
                ChunkPos chunk_pos = event.getChunk().getPos();
                Events.eventChunkLoaded(dimension, chunk_pos);

            }

        }

        @SubscribeEvent
        public static void eventPlayerJoined(PlayerEvent.PlayerLoggedInEvent event) {

            Entity entity = event.getEntity();
            ServerLevel level_server = (ServerLevel) entity.level();

            if (first_player_joined == false) {

                first_player_joined = true;

                Core.delayed_works.create(true, 20, () -> {

                    DataRepair.messagePackErrors(level_server, "pack");

                    if (FileConfig.auto_check_update == true) {

                        TannyPack.checkUpdate(level_server);

                    }

                });

            }

            Events.eventPlayerJoined();

        }

        @SubscribeEvent
        public static void eventRegisterCommand(RegisterCommandsEvent event) {

            Commands.registry(event);

        }

        /*
        (Forge)
        public static void eventTickServer (TickEvent.ServerTickEvent event) {
        (NeoForge)
        public static void eventTickServer (ServerTickEvent.Post event) {
        */
        @SubscribeEvent
        public static void eventTickServer(TickEvent.ServerTickEvent event) {

            LevelAccessor level_accessor = event.getServer().overworld();
            ServerLevel level_server = event.getServer().overworld();
            
            Core.delayed_works.runTick();
            Core.loop.run(level_accessor, level_server);
                    
        }

    }

}
