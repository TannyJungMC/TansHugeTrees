package tannyjung.tanshugetrees.procedures;

import tannyjung.tanshugetrees_handcode.systems.living_tree_mechanics.LivingTreeMechanicsLeafLitterRemoverTickLoop;

import tannyjung.tanshugetrees.TanshugetreesMod;

import net.minecraft.world.entity.Entity;

public class COMMANDLivingTreeMechanicsLeafLitterRemoverLoopTickProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		if (false) {
			TanshugetreesMod.LOGGER.info(entity);
		}
		LivingTreeMechanicsLeafLitterRemoverTickLoop.start(entity);
	}
}
