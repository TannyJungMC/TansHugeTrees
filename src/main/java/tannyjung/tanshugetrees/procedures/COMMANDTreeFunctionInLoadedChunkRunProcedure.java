package tannyjung.tanshugetrees.procedures;

import tannyjung.core.game.TXTFunction;

import tannyjung.tanshugetrees.TanshugetreesMod;

import net.minecraft.world.entity.Entity;

public class COMMANDTreeFunctionInLoadedChunkRunProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		if (false) {
			TanshugetreesMod.LOGGER.info(entity);
		}
		TXTFunction.delayed_command(entity);
	}
}