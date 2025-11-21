package tannyjung.tanshugetrees.procedures;

import tannyjung.tanshugetrees_handcode.Handcode;
import tannyjung.tanshugetrees_handcode.server.living_tree_mechanics.LivingTreeMechanics;

import tannyjung.tanshugetrees.TanshugetreesMod;

import net.minecraft.world.entity.Entity;

public class COMMANDLivingTreeMechanicsMainProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		if (false) {
			TanshugetreesMod.LOGGER.info(entity);
		}
		LivingTreeMechanics.start(entity);
	}
}