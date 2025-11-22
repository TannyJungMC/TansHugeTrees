package tannyjung.tanshugetrees_handcode.systems;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.LevelLoadingScreen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import tannyjung.core.game.OverlayMaker;
import tannyjung.tanshugetrees_handcode.config.ConfigMain;
import tannyjung.tanshugetrees_handcode.systems.world_gen.TreeLocation;

@Mod.EventBusSubscriber({Dist.CLIENT})
public class Overlay {

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void eventMenu (ScreenEvent.Render.Post event) {

        GuiGraphics graphic = event.getGuiGraphics();

        if (event.getScreen() instanceof LevelLoadingScreen) {

            // World Gen (Out-Game)
            {

                if (ConfigMain.world_gen_icon == true) {

                    if (TreeLocation.world_gen_overlay_animation != 0) {

                        OverlayMaker.image(graphic, "inside", "", "tanshugetrees:textures/screens/overlay_region_gen.png", 6, 8, 64, 16, 4, 1, TreeLocation.world_gen_overlay_animation - 1);
                        OverlayMaker.image(graphic, "inside", "", "tanshugetrees:textures/screens/overlay_region_gen_bar.png", 22, 8, 17, 16, 17, 1, (int) Math.round(((double) TreeLocation.world_gen_overlay_bar / 1024) * 16));
                        OverlayMaker.text(graphic, "Biome : " + TreeLocation.world_gen_overlay_details_biome, 6, 32, 1.0f, 1.0f, -1, false);
                        OverlayMaker.text(graphic, "Tree : " + TreeLocation.world_gen_overlay_details_tree, 6, 44, 1.0f, 1.0f, -3355444, false);
                        OverlayMaker.text(graphic, "Generating tree locations. This may take a while.", 6, 64, 1.0f, 1.0f, -3381760, false);

                    }

                }

            }

        }

    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void eventInGame (RenderGuiEvent.Post event) {

        GuiGraphics graphic = event.getGuiGraphics();

        // // World Gen (In-Game)
        {

            if (ConfigMain.world_gen_icon == true) {

                if (TreeLocation.world_gen_overlay_animation != 0) {

                    OverlayMaker.image(graphic, "inside", "", "tanshugetrees:textures/screens/overlay_region_gen.png", 6, 8, 64, 16, 4, 1, TreeLocation.world_gen_overlay_animation - 1);
                    OverlayMaker.image(graphic, "inside", "", "tanshugetrees:textures/screens/overlay_region_gen_bar.png", 22, 8, 17, 16, 17, 1, (int) Math.round(((double) TreeLocation.world_gen_overlay_bar / 1024) * 16));

                    if (ConfigMain.developer_mode == true) {

                        OverlayMaker.text(graphic, "Biome : " + TreeLocation.world_gen_overlay_details_biome, 6, 32, 1.0f, 1.0f, -10066330, false);
                        OverlayMaker.text(graphic, "Tree : " + TreeLocation.world_gen_overlay_details_tree, 6, 44, 1.0f, 1.0f, -11908534, false);

                    }

                }

            }

        }

    }

}
