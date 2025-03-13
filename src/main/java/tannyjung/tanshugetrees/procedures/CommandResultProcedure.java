package tannyjung.tanshugetrees.procedures;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;

public class CommandResultProcedure {
	public static boolean execute(LevelAccessor world, double x, double y, double z, String command) {
		if (command == null)
			return false;
		boolean variable_logic = false;
		if ((new Object() {
			public String getResult(LevelAccessor world, Vec3 pos, String _command) {
				StringBuilder _result = new StringBuilder();
				if (world instanceof ServerLevel _level) {
					CommandSource _dataConsumer = new CommandSource() {
						@Override
						public void sendSystemMessage(Component message) {
							_result.append(message.getString());
						}

						@Override
						public boolean acceptsSuccess() {
							return true;
						}

						@Override
						public boolean acceptsFailure() {
							return true;
						}

						@Override
						public boolean shouldInformAdmins() {
							return false;
						}
					};
					_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(_dataConsumer, pos, Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null), _command);
				}
				return _result.toString();
			}
		}.getResult(world, new Vec3(x, y, z), command)).startsWith("Test passed")) {
			variable_logic = true;
		}
		return variable_logic;
	}
}
