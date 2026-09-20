package tannyjung.tanshugetrees_handcode.core;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.DatapackLoadFailureScreen;
import net.minecraft.client.gui.screens.LevelLoadingScreen;
import tannyjung.tanshugetrees_core.Core;
import tannyjung.tanshugetrees_core.game.screen.ScreenDrawing;
import tannyjung.tanshugetrees_handcode.Handcode;
import tannyjung.tanshugetrees_handcode.systems.living_mechanics.LivingMechanics;
import tannyjung.tanshugetrees_handcode.systems.world_gen.TreeLocation;

public class Overlays {
    
    public static void eventMenu () {

        if (Minecraft.getInstance().screen instanceof LevelLoadingScreen == true) {

            {

                if (Handcode.Config.world_gen_icon == true) {

                    if (TreeLocation.overlay_percent != 0) {

                        ScreenDrawing.Position.set(ScreenDrawing.Anchor.Type.TOP_LEFT, 10, 10);
                        ScreenDrawing.ComponentOverlay.drawTextBasic(1, "§8Percent : " + TreeLocation.overlay_percent);
                        ScreenDrawing.ComponentOverlay.drawTextBasic(1, "§8Biome : " + TreeLocation.overlay_biome);
                        ScreenDrawing.ComponentOverlay.drawTextBasic(1, "§8Tree : " + TreeLocation.overlay_tree);
                        ScreenDrawing.ComponentOverlay.drawTextBasic(1, "§6Generating tree locations. This may take a while.");

                    }

                }

            }

        } else if (Minecraft.getInstance().screen instanceof DatapackLoadFailureScreen == true) {

            {

                ScreenDrawing.Position.set(ScreenDrawing.Anchor.Type.TOP_LEFT, 10, 10);
                ScreenDrawing.ComponentOverlay.drawTextBasic(1, "§cIf this is a world you played with Tan's Huge Trees mod version before 2025, then this is incompatible error.");
                ScreenDrawing.ComponentOverlay.drawTextBasic(1, "§cI would recommended to go back to older version if you want to continue playing this world.");
                ScreenDrawing.ComponentOverlay.drawTextBasic(1, "§fสวัสดีชาวโลก");
                ScreenDrawing.ComponentOverlay.drawTextBasic(1, "§fฉันคือ มะนาวต่างดุด");

            }

        }

    }

    public static void eventInGame () {

        // Developer Mode
        {

            if (Core.developer_mode == true) {

                ScreenDrawing.Position.set(ScreenDrawing.Anchor.Type.CENTER_LEFT, 10, 0);
                ScreenDrawing.ComponentOverlay.drawTextBasic(0.5, "§fTree Location = " + LivingMechanics.list_tree_location.size());
                ScreenDrawing.Position.addZ(2);
                ScreenDrawing.ComponentOverlay.drawTextBasic(0.5, "§fFalling Leaf = " + LivingMechanics.list_falling_leaf.size());
                ScreenDrawing.Position.addZ(2);
                ScreenDrawing.ComponentOverlay.drawTextBasic(0.5, "§fLeaf litter Remover = " + LivingMechanics.list_leaf_litter_remover.size());

            }

        }

        // World Gen Icon
        {

            if (Handcode.Config.world_gen_icon == true) {

                if (TreeLocation.overlay_percent != 0) {

                    if (Core.developer_mode == true) {

                        ScreenDrawing.Position.set(ScreenDrawing.Anchor.Type.TOP_LEFT, 10, 10);
                        ScreenDrawing.ComponentOverlay.drawTextBasic(1, "§8Percent : " + TreeLocation.overlay_percent);
                        ScreenDrawing.ComponentOverlay.drawTextBasic(1, "§8Biome : " + TreeLocation.overlay_biome);
                        ScreenDrawing.ComponentOverlay.drawTextBasic(1, "§8Tree : " + TreeLocation.overlay_tree);

                    }

                }

            }

        }

    }

}
