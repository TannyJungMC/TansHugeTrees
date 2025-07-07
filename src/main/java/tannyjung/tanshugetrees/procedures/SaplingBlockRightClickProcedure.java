package tannyjung.tanshugetrees.procedures;

import tannyjung.tanshugetrees_handcode.systems.sapling.Sapling;

import tannyjung.tanshugetrees.TanshugetreesMod;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;

import java.io.File;

public class SaplingBlockRightClickProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		File file = new File("");
		String file_name = "";
		String cancel = "";
		BlockState sapling = Blocks.AIR.defaultBlockState();
		if (false) {
			if (world instanceof ServerLevel _level)
				_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(), "");
			TanshugetreesMod.LOGGER.info(entity);
		}
		if (world instanceof ServerLevel level_server) {
			Sapling.click(world, level_server, entity, x, y, z);
		}
	}
}