
package tannyjung.tanshugetrees.client.screens;

import tannyjung.tanshugetrees.procedures.OVERLAYRegionGenTreeProcedure;
import tannyjung.tanshugetrees.procedures.OVERLAYRegionGenShowProcedure;
import tannyjung.tanshugetrees.procedures.OVERLAYRegionGenLoadingProcedure;
import tannyjung.tanshugetrees.procedures.OVERLAYRegionGenBiomeProcedure;
import tannyjung.tanshugetrees.procedures.OVERLAYRegionGenBarProcedure;
import tannyjung.tanshugetrees.procedures.DeveloperModeProcedure;

import org.checkerframework.checker.units.qual.h;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.api.distmarker.Dist;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.util.Mth;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.Minecraft;

@Mod.EventBusSubscriber({Dist.CLIENT})
public class OVERLAYRegionGenOverlay {
	@SubscribeEvent(priority = EventPriority.NORMAL)
	public static void eventHandler(RenderGuiEvent.Pre event) {
		int w = event.getWindow().getGuiScaledWidth();
		int h = event.getWindow().getGuiScaledHeight();
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

			if (DeveloperModeProcedure.execute())
				event.getGuiGraphics().drawString(Minecraft.getInstance().font,

						OVERLAYRegionGenBiomeProcedure.execute(), 6, 32, -13421773, false);
			if (DeveloperModeProcedure.execute())
				event.getGuiGraphics().drawString(Minecraft.getInstance().font,

						OVERLAYRegionGenTreeProcedure.execute(), 6, 44, -10066330, false);
		}
	}
}
