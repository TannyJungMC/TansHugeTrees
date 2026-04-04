package tannyjung.tanshugetrees.procedures;

import tannyjung.tanshugetrees_handcode.systems.tree_generator.TreeSummonerStaff;

import net.minecraft.world.entity.Entity;

public class TreeSummonerStaffGUIWhenOpenProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		TreeSummonerStaff.restore(entity);
	}
}
