package tannyjung.tanshugetrees.procedures;

import tannyjung.tanshugetrees_handcode.systems.tree_generator.TreeSummonerStaff;

import tannyjung.tanshugetrees.world.inventory.TreeSummonerStaffGUIMenu;

import net.minecraft.network.chat.Component;
import net.minecraft.world.SimpleMenuProvider;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.core.BlockPos;

public class TreeSummonerStaffClickProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		if (entity.isShiftKeyDown() == false) {
			TreeSummonerStaff.click(entity);
		} else {
			if (entity instanceof ServerPlayer _ent) {
				BlockPos _bpos = BlockPos.containing(x, y, z);
				_ent.openMenu(new SimpleMenuProvider((syncId, inventory, player) -> new TreeSummonerStaffGUIMenu(syncId, inventory, _bpos), Component.literal("TreeSummonerStaffGUI")), buf -> buf.writeBlockPos(_bpos));
			}
			TreeSummonerStaffGUIWhenOpenProcedure.execute(entity);
		}
	}
}
