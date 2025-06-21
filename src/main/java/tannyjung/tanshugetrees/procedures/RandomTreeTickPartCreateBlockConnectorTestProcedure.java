package tannyjung.tanshugetrees.procedures;

import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.context.CommandContext;

public class RandomTreeTickPartCreateBlockConnectorTestProcedure {
	public static void execute(CommandContext<CommandSourceStack> arguments, Entity entity) {
		if (entity == null)
			return;
		double posX = 0;
		double posY = 0;
		double posZ = 0;
		String pos = "";
		if (true) {
			try {
				posX = new Object() {
					public double getX() {
						try {
							return BlockPosArgument.getLoadedBlockPos(arguments, "pos").getX();
						} catch (CommandSyntaxException e) {
							e.printStackTrace();
							return 0;
						}
					}
				}.getX();
				posY = new Object() {
					public double getY() {
						try {
							return BlockPosArgument.getLoadedBlockPos(arguments, "pos").getY();
						} catch (CommandSyntaxException e) {
							e.printStackTrace();
							return 0;
						}
					}
				}.getY();
				posZ = new Object() {
					public double getZ() {
						try {
							return BlockPosArgument.getLoadedBlockPos(arguments, "pos").getZ();
						} catch (CommandSyntaxException e) {
							e.printStackTrace();
							return 0;
						}
					}
				}.getZ();
			} catch (Exception exception) {
				return;
			}
		}
		if (true) {
			posX = new Object() {
				public double getX() {
					try {
						return BlockPosArgument.getLoadedBlockPos(arguments, "pos").getX();
					} catch (CommandSyntaxException e) {
						e.printStackTrace();
						return 0;
					}
				}
			}.getX();
			posY = new Object() {
				public double getY() {
					try {
						return BlockPosArgument.getLoadedBlockPos(arguments, "pos").getY();
					} catch (CommandSyntaxException e) {
						e.printStackTrace();
						return 0;
					}
				}
			}.getY();
			posZ = new Object() {
				public double getZ() {
					try {
						return BlockPosArgument.getLoadedBlockPos(arguments, "pos").getZ();
					} catch (CommandSyntaxException e) {
						e.printStackTrace();
						return 0;
					}
				}
			}.getZ();
			if (Math.abs(Math.floor(entity.getY()) - Math.floor(posY)) <= 1 && Math.abs(Math.floor(entity.getX()) - Math.floor(posX)) <= 1 && Math.abs(Math.floor(entity.getZ()) - Math.floor(posZ)) <= 1) {
				if (Math.floor(entity.getY()) - Math.floor(posY) == 0) {
					if (Math.floor(entity.getX()) - Math.floor(posX) != 0 && Math.floor(entity.getZ()) - Math.floor(posZ) != 0) {
						if (Math.floor(entity.getX()) - Math.floor(posX) == 1 && Math.floor(entity.getZ()) - Math.floor(posZ) == 1) {
							if (entity.getX() - posX >= entity.getZ() - posZ) {
								posX = posX + 1;
							} else {
								posZ = posZ + 1;
							}
						} else if (Math.floor(entity.getX()) - Math.floor(posX) == 1 && Math.floor(entity.getZ()) - Math.floor(posZ) == -1) {
							if (entity.getX() - posX >= entity.getZ() - posZ) {
								posX = posX + 1;
							} else {
								posZ = posZ + -1;
							}
						} else if (Math.floor(entity.getX()) - Math.floor(posX) == -1 && Math.floor(entity.getZ()) - Math.floor(posZ) == 1) {
							if (entity.getX() - posX >= entity.getZ() - posZ) {
								posX = posX + -1;
							} else {
								posZ = posZ + 1;
							}
						} else {
							if (entity.getX() - posX >= entity.getZ() - posZ) {
								posX = posX + -1;
							} else {
								posZ = posZ + -1;
							}
						}
					}
				} else {
					if (Math.floor(entity.getX()) - Math.floor(posX) != 0 || Math.floor(entity.getZ()) - Math.floor(posZ) != 0) {
						if (Math.floor(entity.getY()) - Math.floor(posY) == 1) {
							posY = posY + 1;
						} else if (Math.floor(entity.getY()) - Math.floor(posY) == -1) {
							posY = posY + -1;
						}
					}
				}
				pos = (Math.floor(posX) + " " + Math.floor(posY) + " " + Math.floor(posZ)).replace(".0", "");
				if (!(pos).equals((Math.floor(new Object() {
					public double getX() {
						try {
							return BlockPosArgument.getLoadedBlockPos(arguments, "pos").getX();
						} catch (CommandSyntaxException e) {
							e.printStackTrace();
							return 0;
						}
					}
				}.getX()) + " " + Math.floor(new Object() {
					public double getY() {
						try {
							return BlockPosArgument.getLoadedBlockPos(arguments, "pos").getY();
						} catch (CommandSyntaxException e) {
							e.printStackTrace();
							return 0;
						}
					}
				}.getY()) + " " + Math.floor(new Object() {
					public double getZ() {
						try {
							return BlockPosArgument.getLoadedBlockPos(arguments, "pos").getZ();
						} catch (CommandSyntaxException e) {
							e.printStackTrace();
							return 0;
						}
					}
				}.getZ())).replace(".0", ""))) {
					if ((entity.getPersistentData().getString("place_block_command")).startsWith(" unless") || (entity.getPersistentData().getString("place_block_command")).startsWith(" if")) {
						{
							Entity _ent = entity;
							if (!_ent.level().isClientSide() && _ent.getServer() != null) {
								_ent.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null, 4,
										_ent.getName().getString(), _ent.getDisplayName(), _ent.level().getServer(), _ent), ("execute positioned " + pos + entity.getPersistentData().getString("place_block_command")));
							}
						}
					} else {
						if ((entity.getPersistentData().getString("place_block_command")).endsWith(" replace #tanshugetrees:passable_blocks")) {
							{
								Entity _ent = entity;
								if (!_ent.level().isClientSide() && _ent.getServer() != null) {
									_ent.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null,
											4, _ent.getName().getString(), _ent.getDisplayName(), _ent.level().getServer(), _ent), ("fill " + pos + " " + pos + entity.getPersistentData().getString("place_block_command")));
								}
							}
						} else {
							{
								Entity _ent = entity;
								if (!_ent.level().isClientSide() && _ent.getServer() != null) {
									_ent.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null,
											4, _ent.getName().getString(), _ent.getDisplayName(), _ent.level().getServer(), _ent), ("setblock " + pos + entity.getPersistentData().getString("place_block_command")));
								}
							}
						}
					}
				}
			}
		}
		if (true) {
			posX = new Object() {
				public double getX() {
					try {
						return BlockPosArgument.getLoadedBlockPos(arguments, "pos").getX();
					} catch (CommandSyntaxException e) {
						e.printStackTrace();
						return 0;
					}
				}
			}.getX();
			posY = new Object() {
				public double getY() {
					try {
						return BlockPosArgument.getLoadedBlockPos(arguments, "pos").getY();
					} catch (CommandSyntaxException e) {
						e.printStackTrace();
						return 0;
					}
				}
			}.getY();
			posZ = new Object() {
				public double getZ() {
					try {
						return BlockPosArgument.getLoadedBlockPos(arguments, "pos").getZ();
					} catch (CommandSyntaxException e) {
						e.printStackTrace();
						return 0;
					}
				}
			}.getZ();
			if (Math.abs(Math.floor(entity.getY()) - Math.floor(posY)) <= 1 && Math.abs(Math.floor(entity.getX()) - Math.floor(posX)) <= 1 && Math.abs(Math.floor(entity.getZ()) - Math.floor(posZ)) <= 1) {
				if (Math.floor(entity.getY()) - Math.floor(posY) != 0 && Math.floor(entity.getX()) - Math.floor(posX) != 0 && Math.floor(entity.getZ()) - Math.floor(posZ) != 0) {
					if (Math.floor(entity.getY()) - Math.floor(posY) == 1) {
						posY = posY + 1;
					} else if (Math.floor(entity.getY()) - Math.floor(posY) == -1) {
						posY = posY + -1;
					}
					if (Math.floor(entity.getX()) - Math.floor(posX) == 1 && Math.floor(entity.getZ()) - Math.floor(posZ) == 1) {
						if (entity.getX() - posX >= entity.getZ() - posZ) {
							posX = posX + 1;
						} else {
							posZ = posZ + 1;
						}
					} else if (Math.floor(entity.getX()) - Math.floor(posX) == 1 && Math.floor(entity.getZ()) - Math.floor(posZ) == -1) {
						if (entity.getX() - posX >= entity.getZ() - posZ) {
							posX = posX + 1;
						} else {
							posZ = posZ + -1;
						}
					} else if (Math.floor(entity.getX()) - Math.floor(posX) == -1 && Math.floor(entity.getZ()) - Math.floor(posZ) == 1) {
						if (entity.getX() - posX >= entity.getZ() - posZ) {
							posX = posX + -1;
						} else {
							posZ = posZ + 1;
						}
					} else {
						if (entity.getX() - Math.floor(posX) >= entity.getZ() - Math.floor(posZ)) {
							posX = posX + -1;
						} else {
							posZ = posZ + -1;
						}
					}
				}
				pos = (Math.floor(posX) + " " + Math.floor(posY) + " " + Math.floor(posZ)).replace(".0", "");
				if (!(pos).equals((Math.floor(new Object() {
					public double getX() {
						try {
							return BlockPosArgument.getLoadedBlockPos(arguments, "pos").getX();
						} catch (CommandSyntaxException e) {
							e.printStackTrace();
							return 0;
						}
					}
				}.getX()) + " " + Math.floor(new Object() {
					public double getY() {
						try {
							return BlockPosArgument.getLoadedBlockPos(arguments, "pos").getY();
						} catch (CommandSyntaxException e) {
							e.printStackTrace();
							return 0;
						}
					}
				}.getY()) + " " + Math.floor(new Object() {
					public double getZ() {
						try {
							return BlockPosArgument.getLoadedBlockPos(arguments, "pos").getZ();
						} catch (CommandSyntaxException e) {
							e.printStackTrace();
							return 0;
						}
					}
				}.getZ())).replace(".0", ""))) {
					if ((entity.getPersistentData().getString("place_block_command")).startsWith(" unless block ~ ~ ~ ")) {
						{
							Entity _ent = entity;
							if (!_ent.level().isClientSide() && _ent.getServer() != null) {
								_ent.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null, 4,
										_ent.getName().getString(), _ent.getDisplayName(), _ent.level().getServer(), _ent), ("execute positioned " + pos + entity.getPersistentData().getString("place_block_command")));
							}
						}
					} else {
						if ((entity.getPersistentData().getString("place_block_command")).endsWith(" replace #tanshugetrees:passable_blocks")) {
							{
								Entity _ent = entity;
								if (!_ent.level().isClientSide() && _ent.getServer() != null) {
									_ent.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null,
											4, _ent.getName().getString(), _ent.getDisplayName(), _ent.level().getServer(), _ent), ("fill " + pos + " " + pos + entity.getPersistentData().getString("place_block_command")));
								}
							}
						} else {
							{
								Entity _ent = entity;
								if (!_ent.level().isClientSide() && _ent.getServer() != null) {
									_ent.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null,
											4, _ent.getName().getString(), _ent.getDisplayName(), _ent.level().getServer(), _ent), ("setblock " + pos + entity.getPersistentData().getString("place_block_command")));
								}
							}
						}
					}
				}
			}
		}
	}
}
