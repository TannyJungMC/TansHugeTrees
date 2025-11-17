package tannyjung.tanshugetrees_mcreator.procedures;

import tannyjung.tanshugetrees.Handcode;
import tannyjung.tanshugetrees.server.living_tree_mechanics.LivingTreeMechanics;

import tannyjung.tanshugetrees_mcreator.TanshugetreesMod;

import net.minecraft.world.entity.Entity;

public class COMMANDLivingTreeMechanicsMainProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		if (false) {
			Handcode.logger.info(entity);
		}
		LivingTreeMechanics.start(entity);
	}
}