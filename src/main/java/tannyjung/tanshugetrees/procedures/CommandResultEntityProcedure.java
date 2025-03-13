package tannyjung.tanshugetrees.procedures;

import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;

public class CommandResultEntityProcedure {
	public static boolean execute(Entity entity, String command) {
		if (entity == null || command == null)
			return false;
		boolean variable_logic = false;
		if ((new Object() {
			public String getResult(Entity _ent, String _command) {
				StringBuilder _result = new StringBuilder();
				if (!_ent.level().isClientSide() && _ent.getServer() != null) {
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
					_ent.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(_dataConsumer, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null, 4,
							_ent.getName().getString(), _ent.getDisplayName(), _ent.level().getServer(), _ent), _command);
				}
				return _result.toString();
			}
		}.getResult(entity, command)).startsWith("Test passed")) {
			variable_logic = true;
		}
		return variable_logic;
	}
}
