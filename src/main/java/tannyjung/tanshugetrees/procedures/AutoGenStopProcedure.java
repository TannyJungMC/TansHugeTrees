package tannyjung.tanshugetrees.procedures;

import tannyjung.tanshugetrees.network.TanshugetreesModVariables;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;

public class AutoGenStopProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z) {
		if (TanshugetreesModVariables.MapVariables.get(world).auto_gen == true) {
			if (TanshugetreesModVariables.MapVariables.get(world).detect_exist == true) {
				if (world instanceof ServerLevel _level)
					_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
							"tellraw @a [\"\",{\"text\":\"THT : Auto gen will be stop after finish this one\",\"color\":\"gray\"}]");
			} else {
				if (world instanceof ServerLevel _level)
					_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
							"tellraw @a [\"\",{\"text\":\"THT : Plese wait a second and try again\",\"color\":\"gray\"}]");
			}
		} else {
			if (world instanceof ServerLevel _level)
				_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
						"tellraw @a [\"\",{\"text\":\"THT : Auto gen already turned OFF\",\"color\":\"gray\"}]");
		}
		TanshugetreesModVariables.MapVariables.get(world).auto_gen_count = 0;
		TanshugetreesModVariables.MapVariables.get(world).syncData(world);
	}
}
