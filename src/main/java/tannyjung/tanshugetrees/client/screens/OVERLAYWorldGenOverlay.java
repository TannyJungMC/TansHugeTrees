package tannyjung.tanshugetrees.client.screens;

import tannyjung.tanshugetrees.procedures.OVERLAYWorldGenShowProcedure;
import tannyjung.tanshugetrees.procedures.OVERLAYWorldGenShowDetailsProcedure;
import tannyjung.tanshugetrees.procedures.OVERLAYWorldGenDetailsTreeProcedure;
import tannyjung.tanshugetrees.procedures.OVERLAYWorldGenDetailsBiomeProcedure;
import tannyjung.tanshugetrees.procedures.OVERLAYWorldGenBarProcedure;
import tannyjung.tanshugetrees.procedures.OVERLAYWorldGenAnimationProcedure;

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
public class OVERLAYWorldGenOverlay {
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
		if (OVERLAYWorldGenShowProcedure.execute()) {

			event.getGuiGraphics().blit(ResourceLocation.parse("tanshugetrees:textures/screens/overlay_region_gen.png"), 6, 8, Mth.clamp((int) OVERLAYWorldGenAnimationProcedure.execute() * 16, 0, 48), 0, 16, 16, 64, 16);

			event.getGuiGraphics().blit(ResourceLocation.parse("tanshugetrees:textures/screens/overlay_region_gen_bar.png"), 22, 8, Mth.clamp((int) OVERLAYWorldGenBarProcedure.execute() * 1, 0, 16), 0, 1, 16, 17, 16);

			if (OVERLAYWorldGenShowDetailsProcedure.execute())
				event.getGuiGraphics().drawString(Minecraft.getInstance().font,

						OVERLAYWorldGenDetailsBiomeProcedure.execute(), 6, 32, -10066330, false);
			if (OVERLAYWorldGenShowDetailsProcedure.execute())
				event.getGuiGraphics().drawString(Minecraft.getInstance().font,

						OVERLAYWorldGenDetailsTreeProcedure.execute(), 6, 44, -11908534, false);
		}
	}
}