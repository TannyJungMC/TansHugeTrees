package tannyjung.tanshugetrees.procedures;

import tannyjung.tanshugetrees_handcode.config.TestIncompatibleCustomPack;
import tannyjung.tanshugetrees_handcode.config.CustomPacksOrganized;
import tannyjung.tanshugetrees_handcode.config.ConfigRepairPlacement;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;

public class ConfigRepairProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z) {
		if (world instanceof ServerLevel _level)
			_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(), "");
		tannyjung.tanshugetrees_handcode.config.Config.repair();
		try {
			TestIncompatibleCustomPack.start(world, x, y, z);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String[] custom_args = {};
		try {
			CustomPacksOrganized.start(custom_args);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			ConfigRepairPlacement.start(world, x, y, z);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
