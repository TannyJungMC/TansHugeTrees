package tannyjung.tanshugetrees_mcreator.procedures;

import tannyjung.core.game.TXTFunction;

import tannyjung.tanshugetrees.Handcode;
import tannyjung.tanshugetrees_mcreator.TanshugetreesMod;

import net.minecraft.world.entity.Entity;

public class COMMANDTreeFunctionInLoadedChunkRunProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		if (false) {
			Handcode.logger.info(entity);
		}
		TXTFunction.delayed_command(entity);
	}
}