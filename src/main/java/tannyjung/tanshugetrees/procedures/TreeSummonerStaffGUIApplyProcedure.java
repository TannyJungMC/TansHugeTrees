package tannyjung.tanshugetrees.procedures;

import tannyjung.tanshugetrees_handcode.systems.tree_generator.TreeSummonerStaff;

import tannyjung.tanshugetrees.TanshugetreesMod;

import net.minecraft.world.entity.Entity;

public class TreeSummonerStaffGUIApplyProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		if (false) {
			TanshugetreesMod.LOGGER.info(entity);
		}
		TreeSummonerStaff.apply(entity);
	}
}