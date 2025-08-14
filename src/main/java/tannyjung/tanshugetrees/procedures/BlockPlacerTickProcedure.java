package tannyjung.tanshugetrees.procedures;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.core.BlockPos;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;

import java.io.File;

public class BlockPlacerTickProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z) {
		File file = new File("");
		String function = "";
		if (false) {
			if (world instanceof ServerLevel _level)
				_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(), "");
		}
		BlockPos pos = new BlockPos((int) x, (int) y, (int) z);
		tannyjung.tanshugetrees_handcode.systems.tree_generator.BlockPlacer.start(world, pos);
	}
}