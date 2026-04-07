package tannyjung.tanshugetrees_core.game;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.storage.LevelResource;

import tannyjung.tanshugetrees_core.Core;
import tannyjung.tanshugetrees_core.game.world_gen.WorldGenStepEnd;
import tannyjung.tanshugetrees_core.outside.CustomPackOrganizing;
import tannyjung.tanshugetrees_core.outside.TannyPackManager;
import tannyjung.tanshugetrees_handcode.Handcode;
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
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.api.distmarker.Dist;
(1.21.1)
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.level.ChunkEvent;
import net.neoforged.neoforge.event.server.ServerAboutToStartEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.neoforge.event.server.ServerStoppingEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
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
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.api.distmarker.Dist;

public class EventCenter {
    
    @EventBusSubscriber({Dist.CLIENT})
    public static class Client {

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
    
    @EventBusSubscriber
    public static class Server {

        private static boolean first_player_joined = false;

        @SubscribeEvent
        public static void eventWorldAboutToStart (ServerAboutToStartEvent event) {

            String path_world = event.getServer().getWorldPath(new LevelResource(".")).toString();
            Core.path_world_core = path_world + "/data/tannyjung/" + Core.data_structure_version_core;
            Core.path_world_mod = path_world + "/data/" + Core.mod_id;

            Core.DataMigration.run(true);
            Core.restart(null, false, true);

        }

        @SubscribeEvent
        public static void eventWorldStarted (ServerStartedEvent event) {

            ServerLevel level_server = event.getServer().overworld();
            Core.restart(level_server, true, false);

        }

        @SubscribeEvent
        public static void eventWorldStopping (ServerStoppingEvent event) {

            first_player_joined = false;

        }

        @SubscribeEvent
        public static void eventChunkLoaded (ChunkEvent.Load event) {

            if (event.isNewChunk() == true) {

                LevelAccessor level_accessor = event.getLevel();
                ServerLevel level_server = (ServerLevel) level_accessor;
                String dimension = GameUtils.Space.getDimensionID(level_server).replace(":", "-");
                ChunkPos chunk_pos = event.getChunk().getPos();

                WorldGenStepEnd.start(dimension, chunk_pos);

            }

        }

        @SubscribeEvent
        public static void eventPlayerJoined (PlayerEvent.PlayerLoggedInEvent event) {

            Entity entity = event.getEntity();
            ServerLevel level_server = (ServerLevel) entity.level();

            if (first_player_joined == false) {

                first_player_joined = true;

                Core.DelayedWork.create(true, 100, () -> {

                    Core.thread_main.submit(() -> {

                        CustomPackOrganizing.Error.sendMessage(level_server);

                        if (Handcode.Config.auto_check_update == true) {

                            TannyPackManager.runCheckUpdate(level_server);

                        }

                    });

                });

            }

            Events.eventPlayerJoined();

        }

        @SubscribeEvent
        public static void eventRegisterCommand (RegisterCommandsEvent event) {

            CommandMaker.BuiltinCommands.registry(event);
            Commands.registry(event);

        }

        @SubscribeEvent

        /*
        (1.20.1)
        public static void eventTickServer (TickEvent.ServerTickEvent event) {
        (1.21.1)
        public static void eventTickServer (ServerTickEvent.Post event) {
        */
        public static void eventTickServer (TickEvent.ServerTickEvent event) {

            if (Core.global_locking == false) {

                /*
                (1.20.1)
                if (event.phase == TickEvent.Phase.START) return;
                (1.21.1)
                ### Nothing ###
                */
                if (event.phase == TickEvent.Phase.START) return;

                LevelAccessor level_accessor = event.getServer().overworld();
                ServerLevel level_server = event.getServer().overworld();

                Core.DelayedWork.runTick();
                Core.Loop.loopTick(level_accessor, level_server);

            }

        }

    }

}
