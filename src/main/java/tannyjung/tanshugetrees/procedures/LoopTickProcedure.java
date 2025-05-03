package tannyjung.tanshugetrees.procedures;

import tannyjung.tanshugetrees_handcode.systems.config.ConfigMain;

import tannyjung.tanshugetrees.network.TanshugetreesModVariables;
import tannyjung.tanshugetrees.TanshugetreesMod;

import net.minecraftforge.server.ServerLifecycleHooks;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Mth;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;
import net.minecraft.client.Minecraft;

public class LoopTickProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z) {
		double world_spawn_posX = 0;
		double world_spawn_posY = 0;
		double world_spawn_posZ = 0;
		String variable_text = "";
		TanshugetreesMod.queueServerWork(1, () -> {
			if ((world.isClientSide() ? Minecraft.getInstance().getConnection().getOnlinePlayers().size() : ServerLifecycleHooks.getCurrentServer().getPlayerCount()) > 0) {
				LoopTickProcedure.execute(world, x, y, z);
			}
		});
		if (TanshugetreesModVariables.MapVariables.get(world).loop_second < 20) {
			TanshugetreesModVariables.MapVariables.get(world).loop_second = TanshugetreesModVariables.MapVariables.get(world).loop_second + 1;
			TanshugetreesModVariables.MapVariables.get(world).syncData(world);
		} else {
			TanshugetreesModVariables.MapVariables.get(world).loop_second = 1;
			TanshugetreesModVariables.MapVariables.get(world).syncData(world);
			if (true) {
				if (!("").equals("Developer Mode")) {
					if (ConfigMain.developer_mode == true) {
						if (world instanceof ServerLevel _level)
							_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
									"execute at @a at @e[type=marker,tag=TANSHUGETREES,distance=..100] run particle end_rod ~ ~ ~ 0 0 0 0 1 force");
					}
				}
				if (!("").equals("Living Tree Mechanics")) {
					if (ConfigMain.rt_dynamic == true && ConfigMain.tree_location == true) {
						if (!("").equals("Leaves Drop")) {
							if (CommandResultProcedure.execute(world, x, y, z, "execute if entity @e[name=THT-leaves_drop]")) {
								if (world instanceof ServerLevel _level)
									_level.getServer().getCommands().performPrefixedCommand(
											new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
											"execute as @e[name=THT-leaves_drop] at @s unless block ~ ~1.3 ~ #tanshugetrees:passable_blocks run tanshugetrees dev rt_dynamic leaves_drop");
								if (world instanceof ServerLevel _level)
									_level.getServer().getCommands().performPrefixedCommand(
											new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
											"execute as @e[name=THT-leaves_drop] at @s if block ~ ~2 ~ #tanshugetrees:leaves_blocks[waterlogged=true] run kill @s");
								if (world instanceof ServerLevel _level)
									_level.getServer().getCommands().performPrefixedCommand(
											new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
											"execute as @e[name=THT-leaves_drop] at @s if block ~ ~2 ~ water run tanshugetrees dev rt_dynamic leaves_drop");
							}
						}
						if (!("").equals("Leaves Litter")) {
							if (CommandResultProcedure.execute(world, x, y, z, "execute if entity @e[name=THT-leaves_litter_remover]")) {
								if (world instanceof ServerLevel _level)
									_level.getServer().getCommands().performPrefixedCommand(
											new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
											"execute at @e[name=THT-leaves_litter_remover] unless block ~ ~ ~ #tanshugetrees:passable_blocks if block ~ ~1 ~ #tanshugetrees:leaves_blocks run setblock ~ ~1 ~ air");
								if (world instanceof ServerLevel _level)
									_level.getServer().getCommands().performPrefixedCommand(
											new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
											"execute as @e[name=THT-leaves_litter_remover] at @s unless block ~ ~ ~ #tanshugetrees:passable_blocks run kill @s");
								if (world instanceof ServerLevel _level)
									_level.getServer().getCommands().performPrefixedCommand(
											new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
											"execute at @e[name=THT-leaves_litter_remover] if block ~ ~ ~ #tanshugetrees:leaves_blocks[waterlogged=true] run setblock ~ ~ ~ water");
								if (world instanceof ServerLevel _level)
									_level.getServer().getCommands().performPrefixedCommand(
											new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
											"execute as @e[name=THT-leaves_litter_remover] at @s if block ~ ~ ~ water run kill @s");
							}
						}
					}
				}
				if (!("").equals("Auto Gen")) {
					if (TanshugetreesModVariables.MapVariables.get(world).auto_gen == true) {
						if (CommandResultProcedure.execute(world, x, y, z, "execute in tanshugetrees:dimension positioned 0 0 0 unless entity @e[tag=THT-random_tree,distance=..1000]")) {
							AutoGenLoopTickProcedure.execute(world, x, y, z);
						}
					}
				}
			}
		}
		if (!("").equals("Living Tree Mechanics")) {
			if (ConfigMain.rt_dynamic_tick > 0) {
				if (TanshugetreesModVariables.MapVariables.get(world).rt_dynamic_tick < ConfigMain.rt_dynamic_tick) {
					TanshugetreesModVariables.MapVariables.get(world).rt_dynamic_tick = TanshugetreesModVariables.MapVariables.get(world).rt_dynamic_tick + 1;
					TanshugetreesModVariables.MapVariables.get(world).syncData(world);
				} else {
					TanshugetreesModVariables.MapVariables.get(world).rt_dynamic_tick = 1;
					TanshugetreesModVariables.MapVariables.get(world).syncData(world);
					if (Mth.nextInt(RandomSource.create(), 1, (int) ConfigMain.rt_dynamic_simulation) == 1) {
						if (CommandResultProcedure.execute(world, x, y, z, "execute if entity @e[tag=TANSHUGETREES-tree_location]")) {
							if (TanshugetreesModVariables.MapVariables.get(world).version_1192 == true) {
								if (world instanceof ServerLevel _level)
									_level.getServer().getCommands().performPrefixedCommand(
											new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
											"execute as @e[tag=TANSHUGETREES-tree_location,limit=1,sort=random] at @s run TANSHUGETREES dev living_tree_mechanics loop_tick");
							} else {
								if (world instanceof ServerLevel _level)
									_level.getServer().getCommands().performPrefixedCommand(
											new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
											"execute as @e[tag=TANSHUGETREES-tree_location,limit=1,sort=random] at @s if loaded ~ ~ ~ run TANSHUGETREES dev living_tree_mechanics loop_tick");
							}
						}
					}
				}
				if (CommandResultProcedure.execute(world, x, y, z, "execute if entity @e[name=TANSHUGETREES-leaf_drop]")) {
					if (TanshugetreesModVariables.MapVariables.get(world).version_1192 == true) {
						if (world instanceof ServerLevel _level)
							_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
									"execute as @e[name=TANSHUGETREES-leaf_drop] at @s run TANSHUGETREES dev living_tree_mechanics leaf_drop_loop_tick");
					} else {
						if (world instanceof ServerLevel _level)
							_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
									"execute as @e[name=TANSHUGETREES-leaf_drop] at @s if loaded ~ ~ ~ run TANSHUGETREES dev living_tree_mechanics leaf_drop_loop_tick");
					}
				}
				if (world instanceof ServerLevel _level)
					_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
							"execute if entity @e[name=THT-leaves_litter_remover] as @e[name=THT-leaves_litter_remover] at @s if block ~ ~ ~ #tanshugetrees:passable_blocks unless block ~ ~ ~ water unless block ~ ~ ~ #tanshugetrees:leaves_blocks[waterlogged=true] run tp @s ~ ~-1 ~");
			}
		}
		if (!("").equals("Auto Gen")) {
			if (TanshugetreesModVariables.MapVariables.get(world).auto_gen == true) {
				if (world instanceof ServerLevel _level)
					_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
							"execute in tanshugetrees:dimension if entity @e[name=THT-random_tree] positioned 0 0 0 as @e[name=THT-random_tree,limit=1,distance=..1000] at @s run tanshugetrees dev random_tree run");
			}
		}
		if (!("").equals("Random Tree")) {
			if (CommandResultProcedure.execute(world, x, y, z, "execute if entity @e[name=THT-random_tree]")) {
				if (TanshugetreesModVariables.MapVariables.get(world).auto_gen == false && ConfigMain.global_speed != 0) {
					variable_text = "";
					if (ConfigMain.count_limit != 0) {
						variable_text = ",sort=nearest,limit=" + (new java.text.DecimalFormat("##.##").format(ConfigMain.count_limit)).replace(".0", "");
					}
					if (ConfigMain.distance_limit != 0) {
						variable_text = variable_text + "" + (",distance=.." + (new java.text.DecimalFormat("##.##").format(ConfigMain.distance_limit)).replace(".0", ""));
					}
					if (CommandResultProcedure.execute(world, x, y, z, "execute in tanshugetrees:dimension if dimension tanshugetrees:dimension")) {
						if (world instanceof ServerLevel _level)
							_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
									("execute at @p as @e[name=THT-random_tree" + variable_text + "] at @s run tanshugetrees dev random_tree run"));
					}
				}
			}
		}
	}
}
