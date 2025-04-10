
package tannyjung.tanshugetrees.client.screens;

import tannyjung.tanshugetrees.procedures.OVERLAYRegionGenTreeProcedure;
import tannyjung.tanshugetrees.procedures.OVERLAYRegionGenShowProcedure;
import tannyjung.tanshugetrees.procedures.OVERLAYRegionGenLoadingProcedure;
import tannyjung.tanshugetrees.procedures.OVERLAYRegionGenBiomeProcedure;
import tannyjung.tanshugetrees.procedures.OVERLAYRegionGenBarProcedure;

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
import net.minecraft.network.chat.Component;
import net.minecraft.client.gui.screens.LevelLoadingScreen;
import net.minecraft.client.Minecraft;

@Mod.EventBusSubscriber({Dist.CLIENT})
public class OVERLAYRegionGenMenuOverlay {
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
			if (OVERLAYRegionGenShowProcedure.execute()) {

				event.getGuiGraphics().blit(new ResourceLocation("tanshugetrees:textures/screens/overlay_region_gen.png"), 6, 8, Mth.clamp((int) OVERLAYRegionGenLoadingProcedure.execute() * 16, 0, 48), 0, 16, 16, 64, 16);

				event.getGuiGraphics().blit(new ResourceLocation("tanshugetrees:textures/screens/overlay_region_gen_bar.png"), 22, 8, Mth.clamp((int) OVERLAYRegionGenBarProcedure.execute() * 1, 0, 16), 0, 1, 16, 17, 16);

				event.getGuiGraphics().drawString(Minecraft.getInstance().font,

						OVERLAYRegionGenBiomeProcedure.execute(), 6, 32, -1, false);
				event.getGuiGraphics().drawString(Minecraft.getInstance().font,

						OVERLAYRegionGenTreeProcedure.execute(), 6, 44, -3355444, false);
				event.getGuiGraphics().drawString(Minecraft.getInstance().font, Component.translatable("gui.tanshugetrees.overlay_region_gen_menu.label_tanss_huge_trees_generating_t"), 6, 64, -3381760, false);
			}
		}
	}
}
