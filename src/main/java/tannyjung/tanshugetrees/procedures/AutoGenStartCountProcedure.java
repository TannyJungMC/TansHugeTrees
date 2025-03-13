package tannyjung.tanshugetrees.procedures;

import tannyjung.tanshugetrees.network.TanshugetreesModVariables;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.commands.CommandSourceStack;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.arguments.DoubleArgumentType;

public class AutoGenStartCountProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, CommandContext<CommandSourceStack> arguments, Entity entity) {
		if (entity == null)
			return;
		TanshugetreesModVariables.MapVariables.get(world).auto_gen_count = DoubleArgumentType.getDouble(arguments, "loop");
		TanshugetreesModVariables.MapVariables.get(world).syncData(world);
		AutoGenStartProcedure.execute(world, x, y, z, entity);
	}
}
