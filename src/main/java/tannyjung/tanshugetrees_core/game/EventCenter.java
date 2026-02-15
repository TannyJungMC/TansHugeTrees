package tannyjung.tanshugetrees_core.game;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.storage.LevelResource;

import tannyjung.tanshugetrees_core.Core;
import tannyjung.tanshugetrees_core.outside.TannyPackManager;
import tannyjung.tanshugetrees_handcode.data.DataMigration;
import tannyjung.tanshugetrees_handcode.data.DataRepair;
import tannyjung.tanshugetrees_handcode.data.FileConfig;
import tannyjung.tanshugetrees_handcode.systems.Commands;
import tannyjung.tanshugetrees_handcode.systems.Events;
import tannyjung.tanshugetrees_handcode.systems.Overlays;

/*
(1.20.1)
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.level.ChunkEvent;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.client.event.ScreenEvent;
(1.21.1)
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.level.ChunkEvent;
import net.neoforged.neoforge.event.server.ServerAboutToStartEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.neoforge.event.server.ServerStoppingEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;
import net.neoforged.bus.api.EventPriority;
*/
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.level.ChunkEvent;
import net.neoforged.neoforge.event.server.ServerAboutToStartEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.neoforge.event.server.ServerStoppingEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;
import net.neoforged.bus.api.EventPriority;

public class EventCenter {

    /*
    (1.20.1)
    @Mod.EventBusSubscriber({Dist.CLIENT})
    (1.21.1)
    @EventBusSubscriber({Dist.CLIENT})
    */
    @EventBusSubscriber({Dist.CLIENT})
    public static class client {

        @SubscribeEvent(priority = EventPriority.NORMAL)
        public static void eventMenu (ScreenEvent.Render.Post event) {

            Screen screen = event.getScreen();
            GuiGraphics graphic = event.getGuiGraphics();
            int screen_width = event.getScreen().width;
            int screen_height = event.getScreen().height;
            Overlays.eventMenu(screen, graphic, screen_width, screen_height);

        }

        @SubscribeEvent(priority = EventPriority.NORMAL)
        public static void eventInGame (RenderGuiEvent.Post event) {

            GuiGraphics graphic = event.getGuiGraphics();
            int screen_width = event.getGuiGraphics().guiWidth();
            int screen_height = event.getGuiGraphics().guiHeight();
            Overlays.eventInGame(graphic, screen_width, screen_height);

        }

    }

    /*
    (1.20.1)
    @Mod.EventBusSubscriber
    (1.21.1)
    @EventBusSubscriber
    */
    @EventBusSubscriber
    public static class server {

        private static boolean first_player_joined = false;

        @SubscribeEvent
        public static void eventWorldAboutToStart (ServerAboutToStartEvent event) {

            Core.path_world_data = event.getServer().getWorldPath(new LevelResource(".")) + "/data/tannyjung_" + Core.mod_id;
            DataMigration.run("world");
            Core.restart(null, "config", true);

        }

        @SubscribeEvent
        public static void eventWorldStarted (ServerStartedEvent event) {

            ServerLevel level_server = event.getServer().overworld();
            Core.restart(level_server, "world", false);

        }

        @SubscribeEvent
        public static void eventWorldStopping (ServerStoppingEvent event) {

            first_player_joined = false;
            Core.Loops.restart = true;

        }

        @SubscribeEvent
        public static void eventChunkLoaded (ChunkEvent.Load event) {

            if (event.isNewChunk() == true) {

                ServerLevel level_server = (ServerLevel) event.getLevel();
                String dimension = GameUtils.space.getDimensionID(level_server).replace(":", "-");
                ChunkPos chunk_pos = event.getChunk().getPos();
                Events.eventChunkLoaded(dimension, chunk_pos);

            }

        }

        @SubscribeEvent
        public static void eventPlayerJoined (PlayerEvent.PlayerLoggedInEvent event) {

            Entity entity = event.getEntity();
            ServerLevel level_server = (ServerLevel) entity.level();

            if (first_player_joined == false) {

                first_player_joined = true;

                Core.DelayedWorks.create(true, 20, () -> {

                    DataRepair.messagePackErrors(level_server, "pack");

                    if (FileConfig.auto_check_update == true) {

                        TannyPackManager.checkUpdate(level_server);

                    }

                });

            }

            Events.eventPlayerJoined();

        }

        @SubscribeEvent
        public static void eventRegisterCommand (RegisterCommandsEvent event) {

            CommandMaker.builtin_command.registry(event);
            Commands.registry(event);

        }

        @SubscribeEvent

        /*
        (1.20.1)
        public static void eventTickServer (TickEvent.ServerTickEvent event) {
        (1.21.1)
        public static void eventTickServer (ServerTickEvent.Post event) {
        */
        public static void eventTickServer (ServerTickEvent.Post event) {

            LevelAccessor level_accessor = event.getServer().overworld();
            ServerLevel level_server = event.getServer().overworld();
            
            Core.DelayedWorks.runTick();
            Core.Loops.run(level_accessor, level_server);
                    
        }

    }

}
