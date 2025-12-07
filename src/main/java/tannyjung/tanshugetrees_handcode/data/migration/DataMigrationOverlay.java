package tannyjung.tanshugetrees_handcode.data.migration;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.DatapackLoadFailureScreen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import tannyjung.tanshugetrees_core.game.OverlayMaker;

@Mod.EventBusSubscriber({Dist.CLIENT})
public class DataMigrationOverlay {

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void eventMenu (ScreenEvent.Render.Post event) {

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
