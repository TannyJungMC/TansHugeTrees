package tannyjung.tanshugetrees.procedures;

import tannyjung.tanshugetrees_handcode.systems.tree_generator.TreeFunctionInLoadedChunk;

import tannyjung.tanshugetrees.TanshugetreesMod;

import net.minecraft.world.entity.Entity;

public class COMMANDTreeFunctionInLoadedChunkRunProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		if (false) {
			TanshugetreesMod.LOGGER.info(entity);
		}
		TreeFunctionInLoadedChunk.start(entity);
	}
}