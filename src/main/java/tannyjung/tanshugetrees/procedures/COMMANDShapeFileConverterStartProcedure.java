package tannyjung.tanshugetrees.procedures;

import tannyjung.tanshugetrees_handcode.systems.tree_generator.ShapeFileConverter;

import tannyjung.tanshugetrees.network.TanshugetreesModVariables;
import tannyjung.tanshugetrees.TanshugetreesMod;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.arguments.DoubleArgumentType;

public class COMMANDShapeFileConverterStartProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, CommandContext<CommandSourceStack> arguments, Entity entity) {
		if (entity == null)
			return;
		if (false) {
			if (world instanceof ServerLevel _level)
				_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(), "");
			TanshugetreesMod.LOGGER.info(entity);
		}
		TanshugetreesModVariables.MapVariables.get(world).auto_gen_count = DoubleArgumentType.getDouble(arguments, "count");
		TanshugetreesModVariables.MapVariables.get(world).syncData(world);
		ShapeFileConverter.start(world, entity);
	}
}
