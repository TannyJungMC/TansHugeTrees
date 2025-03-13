
package tannyjung.tanshugetrees.client.screens;

import tannyjung.tanshugetrees.procedures.OVERLAYGeneratingRegionShowProcedure;
import tannyjung.tanshugetrees.procedures.OVERLAYGeneratingRegionLoadingProcedure;
import tannyjung.tanshugetrees.procedures.OVERLAYGeneratingRegionBarProcedure;

import org.checkerframework.checker.units.qual.h;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.api.distmarker.Dist;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.util.Mth;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.gui.screens.LevelLoadingScreen;
import net.minecraft.client.Minecraft;

@Mod.EventBusSubscriber({Dist.CLIENT})
public class OVERLAYGeneratingRegionMenuOverlay {
	@SubscribeEvent(priority = EventPriority.NORMAL)
	public static void eventHandler(ScreenEvent.Render.Post event) {
		if (event.getScreen() instanceof LevelLoadingScreen) {
			int w = event.getScreen().width;
			int h = event.getScreen().height;
			Level world = null;
			double x = 0;
			double y = 0;
			double z = 0;
			Player entity = Minecraft.getInstance().player;
			if (entity != null) {
				world = entity.level();
				x = entity.getX();
				y = entity.getY();
				z = entity.getZ();
			}
			if (OVERLAYGeneratingRegionShowProcedure.execute()) {

				event.getGuiGraphics().blit(new ResourceLocation("tanshugetrees:textures/screens/overlay_generating_region.png"), w / 2 + 65, h / 2 + -8, Mth.clamp((int) OVERLAYGeneratingRegionLoadingProcedure.execute() * 16, 0, 48), 0, 16, 16, 64,
						16);

				event.getGuiGraphics().blit(new ResourceLocation("tanshugetrees:textures/screens/overlay_generating_region_bar.png"), w / 2 + 81, h / 2 + -8, Mth.clamp((int) OVERLAYGeneratingRegionBarProcedure.execute() * 1, 0, 16), 0, 1, 16, 17,
						16);

			}
		}
	}
}
