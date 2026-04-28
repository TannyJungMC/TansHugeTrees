package tannyjung.tanshugetrees_handcode.systems;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.DatapackLoadFailureScreen;
import net.minecraft.client.gui.screens.LevelLoadingScreen;
import net.minecraft.client.gui.screens.Screen;
import tannyjung.tanshugetrees_core.Core;
import tannyjung.tanshugetrees_core.game.OverlayMaker;
import tannyjung.tanshugetrees_handcode.Handcode;
import tannyjung.tanshugetrees_handcode.systems.living_mechanics.LivingMechanics;
import tannyjung.tanshugetrees_handcode.systems.world_gen.TreeLocation;

public class Overlays {
    
    public static void eventMenu (Screen screen, GuiGraphics graphic, int screen_width, int screen_height) {

        if (screen instanceof LevelLoadingScreen == true) {

            {

                if (Handcode.Config.world_gen_icon == true) {

                    if (TreeLocation.world_gen_overlay_animation != 0) {

                        OverlayMaker.createImage(graphic, false, "tanshugetrees:textures/screens/overlay_region_gen.png", "", "", 8, 8, 64, 16, 4, 1, TreeLocation.world_gen_overlay_animation - 1);
                        OverlayMaker.createImage(graphic, false, "tanshugetrees:textures/screens/overlay_region_gen_bar.png", "", "", 27, 8, 17, 16, 17, 1, (int) Math.round(((double) TreeLocation.world_gen_overlay_bar / 1024) * 16));
                        OverlayMaker.createText(graphic, screen_width, screen_height, "top-left", 35, 8, 0.75, false, "§8Biome : " + TreeLocation.world_gen_overlay_details_biome);
                        OverlayMaker.createText(graphic, screen_width, screen_height, "top-left", 35, 18, 0.75, false, "§8Tree : " + TreeLocation.world_gen_overlay_details_tree);
                        OverlayMaker.createText(graphic, screen_width, screen_height, "top-left", 8, 38, 0.75, false, "§6Generating tree locations. This may take a while.");

                    }

                }

            }

        } else if (screen instanceof DatapackLoadFailureScreen == true) {

            {

                OverlayMaker.createText(graphic, screen_width, screen_height, "bottom-left", 8, 24, 0.75, false, "§cIf this is a world you played with Tan's Huge Trees mod version before 2025, then this is incompatible error.");
                OverlayMaker.createText(graphic, screen_width, screen_height, "bottom-left", 8, 16, 0.75, false, "§cI would recommended to go back to older version if you want to continue playing this world.");
                OverlayMaker.createText(graphic, screen_width, screen_height, "bottom-left", 8, 56, 1.0, false, "§fสวัสดีชาวโลก");
                OverlayMaker.createText(graphic, screen_width, screen_height, "bottom-left", 8, 48, 1.0, false, "§fฉันคือ มะนาวต่างดุด");

            }

        }

    }

    public static void eventInGame (GuiGraphics graphic, int screen_width, int screen_height) {

        // Developer Mode
        {

            if (Core.developer_mode == true) {

                OverlayMaker.createText(graphic, screen_width, screen_height, "top-left", 8, 68, 0.75, false, "§cTree Location = " + LivingMechanics.list_tree_location.size());
                OverlayMaker.createText(graphic, screen_width, screen_height, "top-left", 8, 78, 0.75, false, "§cFalling Leaf = " + LivingMechanics.list_falling_leaf.size());
                OverlayMaker.createText(graphic, screen_width, screen_height, "top-left", 8, 88, 0.75, false, "§cLeaf litter Remover = " + LivingMechanics.list_leaf_litter_remover.size());

            }

        }

        // World Gen Icon
        {

            if (Handcode.Config.world_gen_icon == true) {

                if (TreeLocation.world_gen_overlay_animation != 0) {

                    OverlayMaker.createImage(graphic, false, "tanshugetrees:textures/screens/overlay_region_gen.png", "", "", 8, 8, 64, 16, 4, 1, TreeLocation.world_gen_overlay_animation - 1);
                    OverlayMaker.createImage(graphic, false, "tanshugetrees:textures/screens/overlay_region_gen_bar.png", "", "", 27, 8, 17, 16, 17, 1, (int) Math.round(((double) TreeLocation.world_gen_overlay_bar / 1024) * 16));

                    if (Core.developer_mode == true) {

                        OverlayMaker.createText(graphic, screen_width, screen_height, "top-left", 35, 8, 0.75, false, "§8Biome : " + TreeLocation.world_gen_overlay_details_biome);
                        OverlayMaker.createText(graphic, screen_width, screen_height, "top-left", 35, 18, 0.75, false, "§8Tree : " + TreeLocation.world_gen_overlay_details_tree);

                    }

                }

            }

        }

    }

}
