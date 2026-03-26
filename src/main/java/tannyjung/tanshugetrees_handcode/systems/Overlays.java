package tannyjung.tanshugetrees_handcode.systems;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.DatapackLoadFailureScreen;
import net.minecraft.client.gui.screens.LevelLoadingScreen;
import net.minecraft.client.gui.screens.Screen;
import tannyjung.tanshugetrees_core.game.OverlayMaker;
import tannyjung.tanshugetrees_handcode.data.FileConfig;
import tannyjung.tanshugetrees_handcode.systems.world_gen.TreeLocation;

public class Overlays {
    
    public static void eventMenu (Screen screen, GuiGraphics graphic, int screen_width, int screen_height) {

        if (screen instanceof LevelLoadingScreen) {

            {

                if (FileConfig.world_gen_icon == true) {

                    if (TreeLocation.world_gen_overlay_animation != 0) {

                        OverlayMaker.createImage(graphic, false, "tanshugetrees:textures/screens/overlay_region_gen.png", "", "", 8, 8, 64, 16, 4, 1, TreeLocation.world_gen_overlay_animation - 1);
                        OverlayMaker.createImage(graphic, false, "tanshugetrees:textures/screens/overlay_region_gen_bar.png", "", "", 27, 8, 17, 16, 17, 1, (int) Math.round(((double) TreeLocation.world_gen_overlay_bar / 1024) * 16));
                        OverlayMaker.createText(graphic, screen_width, screen_height, "top-left", 8, 32, 1.0, -10066330, false, "Biome : " + TreeLocation.world_gen_overlay_details_biome);
                        OverlayMaker.createText(graphic, screen_width, screen_height, "top-left", 8, 44, 1.0, -10066330, false, "Tree : " + TreeLocation.world_gen_overlay_details_tree);
                        OverlayMaker.createText(graphic, screen_width, screen_height, "top-left", 8, 64, 1.0, -3381760, false, "Generating tree locations. This may take a while.");

                    }

                }

            }

        } else if (screen instanceof DatapackLoadFailureScreen) {

            {

                OverlayMaker.createText(graphic, screen_width, screen_height, "bottom-left", 6, 24, 0.75, -3381760, false, "If this is a world you played with Tan's Huge Trees mod version before 2025, then this is incompatible error.");
                OverlayMaker.createText(graphic, screen_width, screen_height, "bottom-left", 6, 16, 0.75, -3381760, false, "I would recommended to go back to older version if you want to continue playing this world.");
                OverlayMaker.createText(graphic, screen_width, screen_height, "bottom-left", 6, 56, 1.0, -3381760, false, "สวัสดีชาวโลก");
                OverlayMaker.createText(graphic, screen_width, screen_height, "bottom-left", 6, 48, 1.0, -3381760, false, "ฉันคือ มะนาวต่างดุด");

            }

        }

    }

    public static void eventInGame (GuiGraphics graphic, int screen_width, int screen_height) {

        if (FileConfig.world_gen_icon == true) {

            if (TreeLocation.world_gen_overlay_animation != 0) {

                OverlayMaker.createImage(graphic, false, "tanshugetrees:textures/screens/overlay_region_gen.png", "", "", 8, 8, 64, 16, 4, 1, TreeLocation.world_gen_overlay_animation - 1);
                OverlayMaker.createImage(graphic, false, "tanshugetrees:textures/screens/overlay_region_gen_bar.png", "", "", 27, 8, 17, 16, 17, 1, (int) Math.round(((double) TreeLocation.world_gen_overlay_bar / 1024) * 16));

                if (FileConfig.developer_mode == true) {

                    OverlayMaker.createText(graphic, screen_width, screen_height, "top-left", 8, 32, 1.0, -10066330, false, "Biome : " + TreeLocation.world_gen_overlay_details_biome);
                    OverlayMaker.createText(graphic, screen_width, screen_height, "top-left", 8, 44, 1.0, -11908534, false, "Tree : " + TreeLocation.world_gen_overlay_details_tree);

                }

            }

        }

    }

}
