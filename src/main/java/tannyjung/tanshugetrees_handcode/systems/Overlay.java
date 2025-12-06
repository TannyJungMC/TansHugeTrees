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
import tannyjung.tanshugetrees_handcode.data.FileConfig;
import tannyjung.tanshugetrees_handcode.systems.world_gen.TreeLocation;

@Mod.EventBusSubscriber({Dist.CLIENT})
public class Overlay {

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void eventMenu (ScreenEvent.Render.Post event) {

        GuiGraphics graphic = event.getGuiGraphics();
        int width = event.getScreen().width;
        int height = event.getScreen().height;

        if (event.getScreen() instanceof LevelLoadingScreen) {

            // World Gen (Out-Game)
            {

                if (FileConfig.world_gen_icon == true) {

                    if (TreeLocation.world_gen_overlay_animation != 0) {

                        OverlayMaker.image(graphic, "inside", "", "tanshugetrees:textures/screens/overlay_region_gen.png", 6, 8, 64, 16, 4, 1, TreeLocation.world_gen_overlay_animation - 1);
                        OverlayMaker.image(graphic, "inside", "", "tanshugetrees:textures/screens/overlay_region_gen_bar.png", 22, 8, 17, 16, 17, 1, (int) Math.round(((double) TreeLocation.world_gen_overlay_bar / 1024) * 16));
                        OverlayMaker.text(graphic, width, height, "top-left", 6, 32, 1.0, -1, false, "Biome : " + TreeLocation.world_gen_overlay_details_biome);
                        OverlayMaker.text(graphic, width, height, "top-left", 6, 44, 1.0, -3355444, false, "Tree : " + TreeLocation.world_gen_overlay_details_tree);
                        OverlayMaker.text(graphic, width, height, "top-left", 6, 64, 1.0, -3381760, false, "Generating tree locations. This may take a while.");

                    }

                }

            }

        }

    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void eventInGame (RenderGuiEvent.Post event) {

        GuiGraphics graphic = event.getGuiGraphics();
        int width = event.getWindow().getWidth();
        int height = event.getWindow().getHeight();

        // // World Gen (In-Game)
        {

            if (FileConfig.world_gen_icon == true) {

                if (TreeLocation.world_gen_overlay_animation != 0) {

                    OverlayMaker.image(graphic, "inside", "", "tanshugetrees:textures/screens/overlay_region_gen.png", 6, 8, 64, 16, 4, 1, TreeLocation.world_gen_overlay_animation - 1);
                    OverlayMaker.image(graphic, "inside", "", "tanshugetrees:textures/screens/overlay_region_gen_bar.png", 22, 8, 17, 16, 17, 1, (int) Math.round(((double) TreeLocation.world_gen_overlay_bar / 1024) * 16));

                    if (FileConfig.developer_mode == true) {

                        OverlayMaker.text(graphic, width, height, "top-left", 6, 32, 1.0, -10066330, false, "Biome : " + TreeLocation.world_gen_overlay_details_biome);
                        OverlayMaker.text(graphic, width, height, "top-left", 6, 44, 1.0, -11908534, false, "Tree : " + TreeLocation.world_gen_overlay_details_tree);

                    }

                }

            }

        }

    }

}
