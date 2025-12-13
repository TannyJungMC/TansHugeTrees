package tannyjung.tanshugetrees_handcode.data;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.DatapackLoadFailureScreen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import tannyjung.tanshugetrees.TanshugetreesMod;
import tannyjung.tanshugetrees_core.FileManager;
import tannyjung.tanshugetrees_core.game.OverlayMaker;
import tannyjung.tanshugetrees_handcode.Handcode;

import java.io.*;

@Mod.EventBusSubscriber({Dist.CLIENT})
public class DataMigration {

    public static class config {

        public static void run () {

            File folder = new File(Handcode.path_config);
            File file = new File(Handcode.path_config + "/#dev/version.txt");
            int previous_version = 0;

            {

                if (folder.exists() == false) {

                    previous_version = -1;

                } else {

                    for (String read_all : FileManager.readTXT(file.getPath())) {

                        previous_version = Integer.parseInt(read_all);

                    }

                }

                FileManager.writeTXT(file.getPath(), String.valueOf(Handcode.data_structure_version), false);

            }

            if (previous_version >= 0 && Handcode.data_structure_version != previous_version) {

                if (previous_version == 0) versions.before160();
                if (previous_version < 20250000) TanshugetreesMod.LOGGER.info("Data Migration : Config Test");;

            }

        }

        private static class versions {

            private static void before160 () {

                FileManager.rename(Handcode.path_config + "/custom_packs/THT-tree_pack-main", "#TannyJung-Main-Pack");
                FileManager.rename(Handcode.path_config + "/custom_packs/TannyJung-Main-Pack", "#TannyJung-Main-Pack");
                FileManager.rename(Handcode.path_config + "/config_world_gen.txt", "config_worldgen.txt");

                TanshugetreesMod.LOGGER.info("Data Migration : Config Before 1.6.0");

            }

        }

    }

    public static class world_data {

        public static void run () {

            File folder = new File(Handcode.path_world_data);
            File file = new File(Handcode.path_world_data + "/version.txt");
            int previous_version = 0;

            {

                if (folder.exists() == false) {

                    previous_version = -1;

                } else {

                    for (String read_all : FileManager.readTXT(file.getPath())) {

                        previous_version = Integer.parseInt(read_all);

                    }

                }

                FileManager.writeTXT(file.getPath(), String.valueOf(Handcode.data_structure_version), false);

            }

            if (previous_version >= 0 && Handcode.data_structure_version != previous_version) {

                if (previous_version == 0) versions.before160();
                if (previous_version < 20250000) TanshugetreesMod.LOGGER.info("Data Migration : World Test");;

            }

        }

        private static class versions {

            private static void before160 () {

                TanshugetreesMod.LOGGER.info("Data Migration : World Before 1.6.0");

            }

        }

    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void overlayMissingDimension (ScreenEvent.Render.Post event) {

        GuiGraphics graphic = event.getGuiGraphics();
        int width = event.getScreen().width;
        int height = event.getScreen().height;

        if (event.getScreen() instanceof DatapackLoadFailureScreen) {

            OverlayMaker.text(graphic, width, height, "bottom-left", 6, 24, 0.75, -3381760, false, "If this is a world you played with Tan's Huge Trees mod version before 2025, then this is incompatible error.");
            OverlayMaker.text(graphic, width, height, "bottom-left", 6, 16, 0.75, -3381760, false, "I would recommended to go back to older version if you want to continue playing this world.");

            OverlayMaker.text(graphic, width, height, "bottom-left", 6, 56, 1.0, -3381760, false, "สวัสดีชาวโลก");
            OverlayMaker.text(graphic, width, height, "bottom-left", 6, 48, 1.0, -3381760, false, "ฉันคือ มะนาวต่างดุด");

        }

    }

}
