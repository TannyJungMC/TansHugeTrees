package tannyjung.tanshugetrees.procedures;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;

public class RandomTreeTickPartCreateBlockConnectorProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		if (!(entity.getPersistentData().getString("block_connect_previous")).equals("")) {
			if (!(entity.getPersistentData().getString("up_down")).equals("up")) {
				entity.getPersistentData().putString("block_connect_previous", (entity.getPersistentData().getString(("block_connect_previous_" + entity.getPersistentData().getString("type")))));
			}
			if (true) {
				RandomTreeTickWayFunctionProcedure.execute(world, entity);
				entity.getPersistentData().putString("place_block_command", ((entity.getPersistentData().getString("place_block_command")).replace("\"}}", "\",function:\"" + "" + entity.getPersistentData().getString("function") + "\"}}")));
				{
					Entity _ent = entity;
					if (!_ent.level().isClientSide() && _ent.getServer() != null) {
						_ent.getServer().getCommands().performPrefixedCommand(
								new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null, 4, _ent.getName().getString(), _ent.getDisplayName(),
										_ent.level().getServer(), _ent),
								("data modify entity @e[name=THT-tree_part_create_" + entity.getPersistentData().getString("tree_id") + ",limit=1] ForgeData.place_block_command set from entity @s ForgeData.place_block_command"));
					}
				}
			}
			if (world instanceof ServerLevel _level)
				_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
						("execute as @e[name=THT-tree_part_create_" + entity.getPersistentData().getString("tree_id") + "] at @s run TANSHUGETREES dev tree_generator block_connector " + entity.getPersistentData().getString("block_connect_previous")));
		}
		if (true) {
			{
				Entity _ent = entity;
				if (!_ent.level().isClientSide() && _ent.getServer() != null) {
					_ent.getServer().getCommands().performPrefixedCommand(
							new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null, 4, _ent.getName().getString(), _ent.getDisplayName(),
									_ent.level().getServer(), _ent),
							("data modify entity @s ForgeData.block_connect_previous_posX set from entity @e[name=THT-tree_part_create_" + entity.getPersistentData().getString("tree_id") + ",limit=1] Pos[0]"));
				}
			}
			{
				Entity _ent = entity;
				if (!_ent.level().isClientSide() && _ent.getServer() != null) {
					_ent.getServer().getCommands().performPrefixedCommand(
							new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null, 4, _ent.getName().getString(), _ent.getDisplayName(),
									_ent.level().getServer(), _ent),
							("data modify entity @s ForgeData.block_connect_previous_posY set from entity @e[name=THT-tree_part_create_" + entity.getPersistentData().getString("tree_id") + ",limit=1] Pos[1]"));
				}
			}
			{
				Entity _ent = entity;
				if (!_ent.level().isClientSide() && _ent.getServer() != null) {
					_ent.getServer().getCommands().performPrefixedCommand(
							new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null, 4, _ent.getName().getString(), _ent.getDisplayName(),
									_ent.level().getServer(), _ent),
							("data modify entity @s ForgeData.block_connect_previous_posZ set from entity @e[name=THT-tree_part_create_" + entity.getPersistentData().getString("tree_id") + ",limit=1] Pos[2]"));
				}
			}
		}
		entity.getPersistentData().putString("block_connect_previous", ((Math.floor(entity.getPersistentData().getDouble("block_connect_previous_posX")) + " " + Math.floor(entity.getPersistentData().getDouble("block_connect_previous_posY")) + " "
				+ Math.floor(entity.getPersistentData().getDouble("block_connect_previous_posZ"))).replace(".0", "")));
		entity.getPersistentData().putString("block_connect_previous_type", (entity.getPersistentData().getString("type")));
		entity.getPersistentData().putString(("block_connect_previous_" + entity.getPersistentData().getString("block_connect_previous_type")), (entity.getPersistentData().getString("block_connect_previous")));
	}
}
