package tannyjung.tanshugetrees.procedures;

import tannyjung.tanshugetrees_handcode.systems.config.ConfigMain;

import tannyjung.tanshugetrees.network.TanshugetreesModVariables;

import net.minecraftforge.fml.loading.FMLPaths;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.core.BlockPos;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;

import java.io.IOException;
import java.io.FileWriter;
import java.io.File;
import java.io.BufferedWriter;

public class RandomTreeTickProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		double variable_number = 0;
		double variable_number2 = 0;
		File file = new File("");
		String variable_text2 = "";
		String variable_text = "";
		String type_next = "";
		String type_pre = "";
		String pos = "";
		{
			Entity _ent = entity;
			if (!_ent.level().isClientSide() && _ent.getServer() != null) {
				_ent.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null, 4,
						_ent.getName().getString(), _ent.getDisplayName(), _ent.level().getServer(), _ent), "particle composter ~ ~ ~ 0 0 0 0 1 force");
			}
		}
		if (true) {
			{
				Entity _ent = entity;
				if (!_ent.level().isClientSide() && _ent.getServer() != null) {
					_ent.getServer().getCommands().performPrefixedCommand(
							new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null, 4, _ent.getName().getString(), _ent.getDisplayName(),
									_ent.level().getServer(), _ent),
							("execute at @e[name=THT-tree_" + (entity.getPersistentData().getString("type") + "_" + entity.getPersistentData().getString("tree_id")) + "] run particle flash ~ ~ ~ 0 0 0 0 1 force"));
				}
			}
			{
				Entity _ent = entity;
				if (!_ent.level().isClientSide() && _ent.getServer() != null) {
					_ent.getServer().getCommands().performPrefixedCommand(
							new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null, 4, _ent.getName().getString(), _ent.getDisplayName(),
									_ent.level().getServer(), _ent),
							("execute at @e[name=THT-tree_" + (entity.getPersistentData().getString("type") + "_" + entity.getPersistentData().getString("tree_id")) + "] run particle end_rod ~ ~ ~ 0 0 50 0 50 force"));
				}
			}
			{
				Entity _ent = entity;
				if (!_ent.level().isClientSide() && _ent.getServer() != null) {
					_ent.getServer().getCommands().performPrefixedCommand(
							new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null, 4, _ent.getName().getString(), _ent.getDisplayName(),
									_ent.level().getServer(), _ent),
							("execute at @e[name=THT-tree_" + (entity.getPersistentData().getString("type") + "_" + entity.getPersistentData().getString("tree_id")) + "] run particle end_rod ~ ~ ~ 50 0 0 0 50 force"));
				}
			}
			{
				Entity _ent = entity;
				if (!_ent.level().isClientSide() && _ent.getServer() != null) {
					_ent.getServer().getCommands().performPrefixedCommand(
							new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null, 4, _ent.getName().getString(), _ent.getDisplayName(),
									_ent.level().getServer(), _ent),
							("execute at @e[name=THT-tree_" + (entity.getPersistentData().getString("type") + "_" + entity.getPersistentData().getString("tree_id")) + "] run particle end_rod ~ ~ ~ 0 50 0 0 50 force"));
				}
			}
		}
		if (entity.getPersistentData().getBoolean("start_tp") == false) {
			entity.getPersistentData().putBoolean("start_tp", true);
			if (entity.getPersistentData().getBoolean("can_disable_roots") == true && (world.getBlockState(BlockPos.containing(entity.getX(), entity.getY() - 1, entity.getZ()))).getBlock() == Blocks.SCAFFOLDING) {
				entity.getPersistentData().putBoolean("no_roots", true);
			}
			if (TanshugetreesModVariables.MapVariables.get(world).auto_gen == false) {
				{
					Entity _ent = entity;
					if (!_ent.level().isClientSide() && _ent.getServer() != null) {
						_ent.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null, 4,
								_ent.getName().getString(), _ent.getDisplayName(), _ent.level().getServer(), _ent), ("tp ~ ~" + entity.getPersistentData().getDouble("start_height") + " ~"));
					}
				}
			}
		}
		if (true) {
			if (TanshugetreesModVariables.MapVariables.get(world).auto_gen == false && ConfigMain.global_speed_enable == true && entity.getPersistentData().getBoolean("global_generate_speed") == true) {
				entity.getPersistentData().putDouble("generate_speed", ConfigMain.global_speed);
				entity.getPersistentData().putDouble("generate_speed_repeat", ConfigMain.global_speed_repeat);
				entity.getPersistentData().putDouble("generate_speed_tp", ConfigMain.global_speed_tp);
			}
			if (entity.getPersistentData().getDouble("generate_speed_repeat") > 0) {
				entity.getPersistentData().putDouble("repeat", (entity.getPersistentData().getDouble("generate_speed_repeat")));
			} else {
				entity.getPersistentData().putDouble("repeat", 1);
			}
		}
		if (entity.getPersistentData().getBoolean("complete") == false) {
			if (entity.getPersistentData().getDouble("generate_speed_test") > 0) {
				entity.getPersistentData().putDouble("generate_speed_test", (entity.getPersistentData().getDouble("generate_speed_test") - 1));
			}
			if (entity.getPersistentData().getDouble("generate_speed_test") <= 0) {
				entity.getPersistentData().putDouble("generate_speed_test", (entity.getPersistentData().getDouble("generate_speed")));
				if (entity.getPersistentData().getBoolean("start") == false) {
					entity.getPersistentData().putBoolean("start", true);
					{
						Entity _ent = entity;
						if (!_ent.level().isClientSide() && _ent.getServer() != null) {
							_ent.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null, 4,
									_ent.getName().getString(), _ent.getDisplayName(), _ent.level().getServer(), _ent), "kill @e[tag=THT-tree_countdown,distance=..1,limit=1,sort=nearest]");
						}
					}
					RandomTreeTickStartProcedure.execute(world, entity);
				} else {
					entity.getPersistentData().putDouble("generate_speed_repeat_test", (entity.getPersistentData().getDouble("generate_speed_repeat")));
					entity.getPersistentData().putDouble("generate_speed_tp_test", (entity.getPersistentData().getDouble("generate_speed_tp")));
					while ((entity.getPersistentData().getDouble("generate_speed_repeat_test") > 0 || entity.getPersistentData().getDouble("generate_speed_repeat") == 0)
							&& (entity.getPersistentData().getDouble("generate_speed_tp_test") > 0 || entity.getPersistentData().getDouble("generate_speed_tp") == 0)) {
						entity.getPersistentData().putDouble("generate_speed_repeat_test", (entity.getPersistentData().getDouble("generate_speed_repeat_test") - 1));
						entity.getPersistentData().putDouble("status_step", (entity.getPersistentData().getDouble("status_step") + 1));
						if ((entity.getPersistentData().getString("step")).equals("summon")) {
							RandomTreeTickSummonProcedure.execute(world, x, y, z, entity);
						} else if ((entity.getPersistentData().getString("step")).equals("create")) {
							RandomTreeTickCreateProcedure.execute(world, x, y, z, entity);
						} else if ((entity.getPersistentData().getString("step")).equals("part_create")) {
							RandomTreeTickPartCreateProcedure.execute(world, x, y, z, entity);
						} else if ((entity.getPersistentData().getString("step")).equals("end")) {
							entity.getPersistentData().putBoolean("complete", true);
							entity.getPersistentData().putDouble("finish_cooldown", 220);
							{
								Entity _ent = entity;
								if (!_ent.level().isClientSide() && _ent.getServer() != null) {
									_ent.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null,
											4, _ent.getName().getString(), _ent.getDisplayName(), _ent.level().getServer(), _ent), "kill @e[tag=THT-tree_status,distance=..1]");
								}
							}
							break;
						}
					}
					if (true) {
						{
							Entity _ent = entity;
							if (!_ent.level().isClientSide() && _ent.getServer() != null) {
								_ent.getServer().getCommands().performPrefixedCommand(
										new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null, 4, _ent.getName().getString(),
												_ent.getDisplayName(), _ent.level().getServer(), _ent),
										("data merge entity @e[tag=THT-tree_status1,distance=..1,limit=1] {CustomName:'{\"text\":\"" + ""
												+ ("Total Process : " + (new java.text.DecimalFormat("##.##").format(entity.getPersistentData().getDouble("status_step"))).replace(".0", "")) + "\",\"color\":\"aqua\"}'}"));
							}
						}
						{
							Entity _ent = entity;
							if (!_ent.level().isClientSide() && _ent.getServer() != null) {
								_ent.getServer().getCommands().performPrefixedCommand(
										new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null, 4, _ent.getName().getString(),
												_ent.getDisplayName(), _ent.level().getServer(), _ent),
										("data merge entity @e[tag=THT-tree_status2,distance=..1,limit=1] {CustomName:'{\"text\":\"" + ""
												+ ("Generating : " + entity.getPersistentData().getString("type") + " | Step : " + entity.getPersistentData().getString("step")) + "\",\"color\":\"aqua\"}'}"));
							}
						}
						{
							Entity _ent = entity;
							if (!_ent.level().isClientSide() && _ent.getServer() != null) {
								_ent.getServer().getCommands().performPrefixedCommand(
										new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null, 4, _ent.getName().getString(),
												_ent.getDisplayName(), _ent.level().getServer(), _ent),
										("data merge entity @e[tag=THT-tree_status3,distance=..1,limit=1] {CustomName:'{\"text\":\"" + ""
												+ ("Count : " + (new java.text.DecimalFormat("##.##").format(entity.getPersistentData().getDouble((entity.getPersistentData().getString("type") + "_count")))).replace(".0", "") + " | Length : "
														+ (new java.text.DecimalFormat("##.##").format(entity.getPersistentData().getDouble((entity.getPersistentData().getString("type") + "_length")))).replace(".0", "") + " | Thickness : "
														+ (new java.text.DecimalFormat("##.##").format(entity.getPersistentData().getDouble((entity.getPersistentData().getString("type") + "_thickness")))).replace(".0", ""))
												+ "\",\"color\":\"aqua\"}'}"));
							}
						}
					}
				}
			}
		} else {
			if (entity.getPersistentData().getDouble("finish_cooldown") > 0) {
				entity.getPersistentData().putDouble("finish_cooldown", (entity.getPersistentData().getDouble("finish_cooldown") - 1));
			} else {
				if (entity.getPersistentData().getDouble("function_end_chance") > Math.random()) {
					if (TanshugetreesModVariables.MapVariables.get(world).auto_gen == false) {
						{
							Entity _ent = entity;
							if (!_ent.level().isClientSide() && _ent.getServer() != null) {
								_ent.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null, 4,
										_ent.getName().getString(), _ent.getDisplayName(), _ent.level().getServer(), _ent), (entity.getPersistentData().getString("function_end")));
							}
						}
					} else {
						if (true) {
							file = new File((FMLPaths.GAMEDIR.get().toString() + "/config/tanshugetrees/generated"), File.separator + (entity.getPersistentData().getString("file_name")));
							try {
								FileWriter filewriter = new FileWriter(file, true);
								BufferedWriter filebw = new BufferedWriter(filewriter);
								if (true) {
									{
										filebw.write("+f0^0^0fe");
										filebw.newLine();
									}
								}
								filebw.close();
								filewriter.close();
							} catch (IOException exception) {
								exception.printStackTrace();
							}
						}
					}
				}
				if (TanshugetreesModVariables.MapVariables.get(world).auto_gen == true) {
					AutoGenWhenTreeFinishProcedure.execute(world, x, y, z, entity);
				}
				if (TanshugetreesModVariables.MapVariables.get(world).auto_gen == true) {
					if (world instanceof ServerLevel _level)
						_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
								"execute if entity @p[distance=..200] run summon firework_rocket ~20 ~10 ~20 {LifeTime:40,FireworksItem:{id:firework_rocket,Count:1,tag:{Fireworks:{Flight:2,Explosions:[{Type:4,Flicker:1,Trail:1,Colors:[I;3887386,4312372],FadeColors:[I;3887386,4312372]}]}}}}");
					if (world instanceof ServerLevel _level)
						_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
								"execute if entity @p[distance=..200] run summon firework_rocket ~20 ~10 ~-20 {LifeTime:40,FireworksItem:{id:firework_rocket,Count:1,tag:{Fireworks:{Flight:2,Explosions:[{Type:4,Flicker:1,Trail:1,Colors:[I;3887386,4312372],FadeColors:[I;3887386,4312372]}]}}}}");
					if (world instanceof ServerLevel _level)
						_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
								"execute if entity @p[distance=..200] run summon firework_rocket ~-20 ~10 ~20 {LifeTime:40,FireworksItem:{id:firework_rocket,Count:1,tag:{Fireworks:{Flight:2,Explosions:[{Type:4,Flicker:1,Trail:1,Colors:[I;3887386,4312372],FadeColors:[I;3887386,4312372]}]}}}}");
					if (world instanceof ServerLevel _level)
						_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
								"execute if entity @p[distance=..200] run summon firework_rocket ~-20 ~10 ~-20 {LifeTime:40,FireworksItem:{id:firework_rocket,Count:1,tag:{Fireworks:{Flight:2,Explosions:[{Type:4,Flicker:1,Trail:1,Colors:[I;3887386,4312372],FadeColors:[I;3887386,4312372]}]}}}}");
				}
				if (world instanceof ServerLevel _level)
					_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
							("kill @e[tag=THT-tree_" + entity.getPersistentData().getString("tree_id") + "]"));
				{
					Entity _ent = entity;
					if (!_ent.level().isClientSide() && _ent.getServer() != null) {
						_ent.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null, 4,
								_ent.getName().getString(), _ent.getDisplayName(), _ent.level().getServer(), _ent), "kill @s");
					}
				}
			}
		}
	}
}
