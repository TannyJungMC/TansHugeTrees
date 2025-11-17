package tannyjung.tanshugetrees_mcreator.procedures;

import tannyjung.tanshugetrees.Handcode;
import tannyjung.tanshugetrees.server.tree_generator.ShapeFileConverter;

import tannyjung.tanshugetrees_mcreator.TanshugetreesMod;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;

public class COMMANDShapeFileConverterStopProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		if (false) {
			if (world instanceof ServerLevel _level)
				_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(), "");
			Handcode.logger.info(entity);
		}
		ShapeFileConverter.stop(world);
	}
}