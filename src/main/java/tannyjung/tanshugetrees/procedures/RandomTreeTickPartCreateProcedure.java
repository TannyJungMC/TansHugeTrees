package tannyjung.tanshugetrees.procedures;

import tannyjung.tanshugetrees_handcode.config.ConfigMain;

import tannyjung.tanshugetrees.network.TanshugetreesModVariables;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Mth;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;

import java.io.File;

public class RandomTreeTickPartCreateProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		double leaves_straighten = 0;
		double leaves_straighten_place = 0;
		File file = new File("");
		boolean end = false;
		String block_keep = "";
		String variable_text = "";
		if (entity.getPersistentData().getBoolean("part_create_true") == false) {
			entity.getPersistentData().putBoolean("part_create_true", true);
			if (true) {
				if ((entity.getPersistentData().getString("type")).equals("leaves")) {
					entity.getPersistentData().putDouble("part_create", (entity.getPersistentData().getDouble("leaves_size")));
				} else {
					entity.getPersistentData().putDouble("part_create", (entity.getPersistentData().getDouble((entity.getPersistentData().getString("type") + "_thickness"))));
				}
				if (!(entity.getPersistentData().getString("type")).equals("leaves")
						&& (ConfigMain.no_core == true || (entity.getPersistentData().getString((entity.getPersistentData().getString("type") + "_core"))).equals("") || entity.getPersistentData().getDouble("part_create") == 0)) {
					entity.getPersistentData().putDouble("part_create2", (entity.getPersistentData().getDouble("part_create")));
				} else {
					entity.getPersistentData().putDouble("part_create2", 0);
				}
				entity.getPersistentData().putDouble("part_create3", 0);
				entity.getPersistentData().putDouble("part_create4", 90);
				if (true) {
					if ((entity.getPersistentData().getString((entity.getPersistentData().getString("type") + "_generator_type"))).equals("sphere_zone") && (entity.getPersistentData().getString("type")).equals("leaves")) {
						variable_text = "execute at @e[name=THT-tree_" + ("leaves_twig" + "_" + entity.getPersistentData().getString("tree_id")) + "] run summon marker ^ ^ ^-"
								+ entity.getPersistentData().getDouble("part_create") * (100 - entity.getPersistentData().getDouble("leaves_sphere_zone_radius_percent") * 0.5) * 0.01;
						if (world instanceof ServerLevel _level)
							_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
									("execute as @e[name=THT-tree_" + ("leaves_twig" + "_" + entity.getPersistentData().getString("tree_id")) + ",x_rotation=" + entity.getPersistentData().getDouble("leaves_sphere_zone_pitch_min")
											+ "..90] at @s run tp @s ~ ~ ~ ~ " + entity.getPersistentData().getDouble("leaves_sphere_zone_pitch_min")));
					} else {
						variable_text = "execute at @e[name=THT-tree_" + (entity.getPersistentData().getString("type") + "_" + entity.getPersistentData().getString("tree_id")) + "] run summon marker ~ ~ ~";
					}
					if (world instanceof ServerLevel _level)
						_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
								(variable_text + "" + " {Tags:[\"THT\",\"THT-random_tree\"],CustomName:'{\"text\":\"THT-tree_part_create_" + entity.getPersistentData().getString("tree_id") + "\"}',Rotation:[0f,90f]}"));
				}
			}
			if ((entity.getPersistentData().getString((entity.getPersistentData().getString("type") + "_generator_type"))).equals("sphere_zone")) {
				{
					Entity _ent = entity;
					if (!_ent.level().isClientSide() && _ent.getServer() != null) {
						_ent.getServer().getCommands().performPrefixedCommand(
								new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null, 4, _ent.getName().getString(), _ent.getDisplayName(),
										_ent.level().getServer(), _ent),
								("data modify entity @s ForgeData.part_directionXZ set from entity @e[name=THT-tree_" + ("leaves_twig" + "_" + entity.getPersistentData().getString("tree_id")) + ",limit=1] Rotation[0]"));
					}
				}
				{
					Entity _ent = entity;
					if (!_ent.level().isClientSide() && _ent.getServer() != null) {
						_ent.getServer().getCommands().performPrefixedCommand(
								new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null, 4, _ent.getName().getString(), _ent.getDisplayName(),
										_ent.level().getServer(), _ent),
								("data modify entity @s ForgeData.part_directionYY set from entity @e[name=THT-tree_" + ("leaves_twig" + "_" + entity.getPersistentData().getString("tree_id")) + ",limit=1] Rotation[1]"));
					}
				}
				if (entity.getPersistentData().getDouble("part_directionXZ") < 0) {
					entity.getPersistentData().putDouble("part_directionXZ", (entity.getPersistentData().getDouble("part_directionXZ") + 360));
				}
				entity.getPersistentData().putDouble("part_directionXZ", (entity.getPersistentData().getDouble("part_directionXZ") * (Math.PI / 180)));
				entity.getPersistentData().putDouble("part_directionYY", ((entity.getPersistentData().getDouble("part_directionYY") + 90) * (Math.PI / 180)));
				if (true) {
					entity.getPersistentData().putDouble("part_directionX", (90 * Math.cos(entity.getPersistentData().getDouble("part_directionXZ")) * Math.sin(entity.getPersistentData().getDouble("part_directionYY"))));
					entity.getPersistentData().putDouble("part_directionY", (90 * Math.sin(entity.getPersistentData().getDouble("part_directionXZ")) * Math.sin(entity.getPersistentData().getDouble("part_directionYY"))));
					entity.getPersistentData().putDouble("part_directionZ", (90 * Math.cos(entity.getPersistentData().getDouble("part_directionYY"))));
				}
			}
		}
		RandomTreeTickWayFunctionProcedure.execute(world, entity);
		if ((entity.getPersistentData().getString((entity.getPersistentData().getString("type") + "_generator_type"))).equals("square") || (entity.getPersistentData().getString("type")).equals("leaves") && ConfigMain.square_leaves == true
				|| !(entity.getPersistentData().getString("type")).equals("leaves") && ConfigMain.square_parts == true) {
			if (true) {
				if (entity.getPersistentData().getBoolean((entity.getPersistentData().getString("type") + "_replace")) == true) {
					block_keep = "";
				} else {
					block_keep = " replace #tanshugetrees:passable_blocks";
				}
				if ((entity.getPersistentData().getString("type")).equals("leaves")) {
					if (entity.getPersistentData().getDouble("leaves2_chance") > Math.random()) {
						entity.getPersistentData().putString("block", (entity.getPersistentData().getString("leaves2")));
						entity.getPersistentData().putString("type_short", (entity.getPersistentData().getString(("leaves2" + "_short"))));
						entity.getPersistentData().putString("block_placer", "leaves_2");
					} else {
						entity.getPersistentData().putString("block", (entity.getPersistentData().getString("leaves")));
						entity.getPersistentData().putString("type_short", (entity.getPersistentData().getString(("leaves" + "_short"))));
						entity.getPersistentData().putString("block_placer", "leaves");
					}
				} else {
					entity.getPersistentData().putString("block", (entity.getPersistentData().getString((entity.getPersistentData().getString("type") + "_outer"))));
					entity.getPersistentData().putString("type_short", (entity.getPersistentData().getString(((entity.getPersistentData().getString("type") + "_outer") + "_short"))));
					entity.getPersistentData().putString("block_placer", (entity.getPersistentData().getString("type") + "_outer"));
				}
				if (TanshugetreesModVariables.MapVariables.get(world).version_1192 == true) {
					{
						Entity _ent = entity;
						if (!_ent.level().isClientSide() && _ent.getServer() != null) {
							_ent.getServer().getCommands().performPrefixedCommand(
									new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null, 4, _ent.getName().getString(), _ent.getDisplayName(),
											_ent.level().getServer(), _ent),
									("execute at @e[name=THT-tree_" + (entity.getPersistentData().getString("type") + "_" + entity.getPersistentData().getString("tree_id"))
											+ "] positioned ~ 0 ~ unless entity @e[name=THT-forceload,distance=..8] run forceload add ~-8 ~-8 ~8 ~8"));
						}
					}
					{
						Entity _ent = entity;
						if (!_ent.level().isClientSide() && _ent.getServer() != null) {
							_ent.getServer().getCommands().performPrefixedCommand(
									new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null, 4, _ent.getName().getString(), _ent.getDisplayName(),
											_ent.level().getServer(), _ent),
									("execute at @e[name=THT-tree_" + (entity.getPersistentData().getString("type") + "_" + entity.getPersistentData().getString("tree_id"))
											+ "] positioned ~ 0 ~ unless entity @e[name=THT-forceload,distance=..8] run summon marker ~ ~ ~ {Tags:[\"THT\"],CustomName:'{\"text\":\"THT-forceload\"}'}"));
						}
					}
				} else {
					{
						Entity _ent = entity;
						if (!_ent.level().isClientSide() && _ent.getServer() != null) {
							_ent.getServer().getCommands().performPrefixedCommand(
									new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null, 4, _ent.getName().getString(), _ent.getDisplayName(),
											_ent.level().getServer(), _ent),
									("execute at @e[name=THT-tree_" + (entity.getPersistentData().getString("type") + "_" + entity.getPersistentData().getString("tree_id"))
											+ "] unless loaded ~ ~ ~ positioned ~ 0 ~ unless entity @e[name=THT-forceload,distance=..8] run forceload add ~-8 ~-8 ~8 ~8"));
						}
					}
					{
						Entity _ent = entity;
						if (!_ent.level().isClientSide() && _ent.getServer() != null) {
							_ent.getServer().getCommands().performPrefixedCommand(
									new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null, 4, _ent.getName().getString(), _ent.getDisplayName(),
											_ent.level().getServer(), _ent),
									("execute at @e[name=THT-tree_" + (entity.getPersistentData().getString("type") + "_" + entity.getPersistentData().getString("tree_id"))
											+ "] unless loaded ~ ~ ~ positioned ~ 0 ~ unless entity @e[name=THT-forceload,distance=..8] run summon marker ~ ~ ~ {Tags:[\"THT\"],CustomName:'{\"text\":\"THT-forceload\"}'}"));
						}
					}
				}
				entity.getPersistentData().putString("place_block_command",
						(("tanshugetrees:block_placer_" + entity.getPersistentData().getString("block_placer") + "{ForgeData:{" + "file_name:\"" + entity.getPersistentData().getString("file_name") + "\""
								+ (",RT_posX:" + entity.getX() + ",RT_posY:" + entity.getY() + ",RT_posZ:" + entity.getZ()) + ",type_short:\"" + entity.getPersistentData().getString("type_short") + "\",block:\""
								+ entity.getPersistentData().getString("block") + "\",function:\"" + entity.getPersistentData().getString("function") + "\",auto_gen:" + TanshugetreesModVariables.MapVariables.get(world).auto_gen + "}}") + ""
								+ block_keep));
				if (world instanceof ServerLevel _level)
					_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
							("execute at @e[name=THT-tree_" + (entity.getPersistentData().getString("type") + "_" + entity.getPersistentData().getString("tree_id")) + "] run fill "
									+ "~x ~x ~x ~-x ~-x ~-x".replace("x", "" + entity.getPersistentData().getDouble("part_create") * 0.5) + " " + entity.getPersistentData().getString("place_block_command")));
				if (entity.getPersistentData().getDouble("part_create") < 1) {
					RandomTreeTickPartCreateBlockConnectorProcedure.execute(world, x, y, z, entity);
				}
			}
			end = true;
		} else if ((entity.getPersistentData().getString((entity.getPersistentData().getString("type") + "_generator_type"))).equals("sphere")
				|| (entity.getPersistentData().getString((entity.getPersistentData().getString("type") + "_generator_type"))).equals("sphere_zone")) {
			if (entity.getPersistentData().getDouble("part_create4") >= -90) {
				if (entity.getPersistentData().getDouble("part_create3") < 360) {
					if (TanshugetreesModVariables.MapVariables.get(world).version_1192 == true) {
						{
							Entity _ent = entity;
							if (!_ent.level().isClientSide() && _ent.getServer() != null) {
								_ent.getServer().getCommands().performPrefixedCommand(
										new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null, 4, _ent.getName().getString(),
												_ent.getDisplayName(), _ent.level().getServer(), _ent),
										("execute at @e[name=THT-tree_" + (entity.getPersistentData().getString("type") + "_" + entity.getPersistentData().getString("tree_id")) + "] positioned ^ ^ ^"
												+ entity.getPersistentData().getDouble("part_create2")
												+ " positioned ~ 0 ~ unless entity @e[name=THT-forceload,distance=..16] run summon marker ~ ~ ~ {Tags:[\"THT\"],CustomName:'{\"text\":\"THT-forceload\"}'}"));
							}
						}
						{
							Entity _ent = entity;
							if (!_ent.level().isClientSide() && _ent.getServer() != null) {
								_ent.getServer().getCommands().performPrefixedCommand(
										new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null, 4, _ent.getName().getString(),
												_ent.getDisplayName(), _ent.level().getServer(), _ent),
										("execute at @e[name=THT-tree_" + (entity.getPersistentData().getString("type") + "_" + entity.getPersistentData().getString("tree_id")) + "] positioned ^ ^ ^"
												+ entity.getPersistentData().getDouble("part_create2") + " run forceload add ~ ~ ~ ~"));
							}
						}
					} else {
						{
							Entity _ent = entity;
							if (!_ent.level().isClientSide() && _ent.getServer() != null) {
								_ent.getServer().getCommands().performPrefixedCommand(
										new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null, 4, _ent.getName().getString(),
												_ent.getDisplayName(), _ent.level().getServer(), _ent),
										("execute at @e[name=THT-tree_" + (entity.getPersistentData().getString("type") + "_" + entity.getPersistentData().getString("tree_id")) + "] positioned ^ ^ ^"
												+ entity.getPersistentData().getDouble("part_create2")
												+ " unless loaded ~ ~ ~ positioned ~ 0 ~ unless entity @e[name=THT-forceload,distance=..16] run summon marker ~ ~ ~ {Tags:[\"THT\"],CustomName:'{\"text\":\"THT-forceload\"}'}"));
							}
						}
						{
							Entity _ent = entity;
							if (!_ent.level().isClientSide() && _ent.getServer() != null) {
								_ent.getServer().getCommands().performPrefixedCommand(
										new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null, 4, _ent.getName().getString(),
												_ent.getDisplayName(), _ent.level().getServer(), _ent),
										("execute at @e[name=THT-tree_" + (entity.getPersistentData().getString("type") + "_" + entity.getPersistentData().getString("tree_id")) + "] positioned ^ ^ ^"
												+ entity.getPersistentData().getDouble("part_create2") + " unless loaded ~ ~ ~ run forceload add ~ ~ ~ ~"));
							}
						}
					}
					if (entity.getPersistentData().getBoolean((entity.getPersistentData().getString("type") + "_replace")) == true) {
						block_keep = "";
					} else {
						block_keep = " if block ~ ~ ~ #tanshugetrees:passable_blocks";
					}
					if ((entity.getPersistentData().getString("type")).equals("leaves")) {
						entity.getPersistentData().putDouble("part_thickness_change", 1);
						if ((entity.getPersistentData().getString((entity.getPersistentData().getString("type") + "_generator_type"))).equals("sphere_zone")) {
							entity.getPersistentData().putDouble("part_directionXZ_test", (entity.getPersistentData().getDouble("part_create3") * (Math.PI / 180)));
							entity.getPersistentData().putDouble("part_directionYY_test", ((entity.getPersistentData().getDouble("part_create4") + 90) * (Math.PI / 180)));
							if (true) {
								entity.getPersistentData().putDouble("part_directionX_test", (90 * Math.cos(entity.getPersistentData().getDouble("part_directionXZ_test")) * Math.sin(entity.getPersistentData().getDouble("part_directionYY_test"))));
								entity.getPersistentData().putDouble("part_directionY_test", (90 * Math.sin(entity.getPersistentData().getDouble("part_directionXZ_test")) * Math.sin(entity.getPersistentData().getDouble("part_directionYY_test"))));
								entity.getPersistentData().putDouble("part_directionZ_test", (90 * Math.cos(entity.getPersistentData().getDouble("part_directionYY_test"))));
							}
						}
						if ((entity.getPersistentData().getString((entity.getPersistentData().getString("type") + "_generator_type"))).equals("sphere")
								|| (entity.getPersistentData().getString((entity.getPersistentData().getString("type") + "_generator_type"))).equals("sphere_zone")
										&& Math.sqrt(Math.pow(entity.getPersistentData().getDouble("part_directionX") - entity.getPersistentData().getDouble("part_directionX_test"), 2)
												+ Math.pow(entity.getPersistentData().getDouble("part_directionZ") - entity.getPersistentData().getDouble("part_directionZ_test"), 2)
												+ Math.pow(entity.getPersistentData().getDouble("part_directionY") - entity.getPersistentData().getDouble("part_directionY_test"), 2)) <= entity.getPersistentData().getDouble("leaves_sphere_zone_size")
										&& entity.getPersistentData().getDouble("part_create2") >= entity.getPersistentData().getDouble("part_create") * (100 - entity.getPersistentData().getDouble("leaves_sphere_zone_radius_percent")) * 0.01) {
							if (Mth.nextDouble(RandomSource.create(), 1, 100) <= entity.getPersistentData().getDouble("leaves_density")) {
								leaves_straighten = 0;
								leaves_straighten_place = 0;
								if (entity.getPersistentData().getDouble("leaves_straighten_chance") > Math.random()) {
									leaves_straighten = Mth.nextInt(RandomSource.create(), (int) entity.getPersistentData().getDouble("leaves_straighten_min"), (int) entity.getPersistentData().getDouble("leaves_straighten_max"));
								}
								for (int index0 = 0; index0 < (int) (leaves_straighten + 1); index0++) {
									if (entity.getPersistentData().getDouble("leaves2_chance") > Math.random()) {
										entity.getPersistentData().putString("block", (entity.getPersistentData().getString("leaves2")));
										entity.getPersistentData().putString("block_placer", "leaves_2");
										entity.getPersistentData().putString("type_short", (entity.getPersistentData().getString(("leaves2" + "_short"))));
									} else {
										entity.getPersistentData().putString("block", (entity.getPersistentData().getString("leaves")));
										entity.getPersistentData().putString("block_placer", "leaves");
										entity.getPersistentData().putString("type_short", (entity.getPersistentData().getString(("leaves" + "_short"))));
									}
									entity.getPersistentData().putString("place_block_command",
											((block_keep + " run setblock ~ ~ ~ ") + ""
													+ ("tanshugetrees:block_placer_" + entity.getPersistentData().getString("block_placer") + "{ForgeData:{" + "file_name:\"" + entity.getPersistentData().getString("file_name") + "\""
															+ (",RT_posX:" + entity.getX() + ",RT_posY:" + entity.getY() + ",RT_posZ:" + entity.getZ()) + ",type_short:\"" + entity.getPersistentData().getString("type_short") + "\",block:\""
															+ entity.getPersistentData().getString("block") + "\",function:\"" + entity.getPersistentData().getString("function") + "\",auto_gen:"
															+ TanshugetreesModVariables.MapVariables.get(world).auto_gen + "}}")));
									if (world instanceof ServerLevel _level)
										_level.getServer().getCommands().performPrefixedCommand(
												new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
												("execute at @e[name=THT-tree_part_create_" + entity.getPersistentData().getString("tree_id") + "] positioned ^ ^ ^" + entity.getPersistentData().getDouble("part_create2") + " positioned ~ ~-"
														+ leaves_straighten_place + " ~" + entity.getPersistentData().getString("place_block_command")));
									if (entity.getPersistentData().getDouble("part_create") < 1 && leaves_straighten_place == 0) {
										RandomTreeTickPartCreateBlockConnectorProcedure.execute(world, x, y, z, entity);
									}
									leaves_straighten_place = leaves_straighten_place + 1;
								}
							}
						}
					} else {
						if (true) {
							if (entity.getPersistentData().getDouble("part_create3") == 0 && entity.getPersistentData().getDouble("part_create4") == 90) {
								RandomTreeTickPartCreateCoreTestProcedure.execute(entity);
							}
							entity.getPersistentData().putString("type_short", (entity.getPersistentData().getString((entity.getPersistentData().getString("block_placer") + "_short"))));
							entity.getPersistentData().putString("block", (entity.getPersistentData().getString((entity.getPersistentData().getString("block_placer")))));
							entity.getPersistentData().putString("place_block_command", (((" unless block ~ ~ ~ " + ("tanshugetrees:block_placer_" + entity.getPersistentData().getString("block_placer"))) + ""
									+ entity.getPersistentData().getString("setblock_unless") + block_keep + " run setblock ~ ~ ~ ")
									+ ""
									+ ("tanshugetrees:block_placer_" + entity.getPersistentData().getString("block_placer") + "{ForgeData:{" + "file_name:\"" + entity.getPersistentData().getString("file_name") + "\""
											+ (",RT_posX:" + entity.getX() + ",RT_posY:" + entity.getY() + ",RT_posZ:" + entity.getZ()) + ",type_short:\"" + entity.getPersistentData().getString("type_short") + "\",block:\""
											+ entity.getPersistentData().getString("block") + "\",function:\"" + entity.getPersistentData().getString("function") + "\",auto_gen:" + TanshugetreesModVariables.MapVariables.get(world).auto_gen + "}}")));
							if (world instanceof ServerLevel _level)
								_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
										("execute at @e[name=THT-tree_part_create_" + entity.getPersistentData().getString("tree_id") + "] positioned ^ ^ ^" + entity.getPersistentData().getDouble("part_create2")
												+ entity.getPersistentData().getString("place_block_command")));
							if (entity.getPersistentData().getDouble("part_create") < 1) {
								RandomTreeTickPartCreateBlockConnectorProcedure.execute(world, x, y, z, entity);
							}
						}
					}
					entity.getPersistentData().putDouble("part_create3", (entity.getPersistentData().getDouble("part_create3") + 360 / (2 * Math.PI * entity.getPersistentData().getDouble("part_create2"))));
				} else {
					entity.getPersistentData().putDouble("part_create3", 0);
					entity.getPersistentData().putDouble("part_create4", (entity.getPersistentData().getDouble("part_create4") - 180 / (2 * Math.PI * entity.getPersistentData().getDouble("part_create2"))));
				}
			} else {
				if (entity.getPersistentData().getDouble("part_create2") != entity.getPersistentData().getDouble("part_create")) {
					entity.getPersistentData().putDouble("part_create3", 0);
					entity.getPersistentData().putDouble("part_create4", 90);
					entity.getPersistentData().putDouble("part_create2", (entity.getPersistentData().getDouble("part_create2") + entity.getPersistentData().getDouble("part_thickness_change")));
					if (entity.getPersistentData().getDouble("part_create2") > entity.getPersistentData().getDouble("part_create")) {
						entity.getPersistentData().putDouble("part_create2", (entity.getPersistentData().getDouble("part_create")));
					}
				} else {
					end = true;
				}
			}
			if (world instanceof ServerLevel _level)
				_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
						("execute as @e[name=THT-tree_part_create_" + entity.getPersistentData().getString("tree_id") + "] at @s run tp @s ~ ~ ~ " + entity.getPersistentData().getDouble("part_create3") + " "
								+ entity.getPersistentData().getDouble("part_create4")));
		} else {
			end = true;
		}
		if (end == true) {
			entity.getPersistentData().putString("step", "create");
			entity.getPersistentData().putBoolean("part_create_true", false);
			if (world instanceof ServerLevel _level)
				_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
						("kill @e[name=THT-tree_part_create_" + entity.getPersistentData().getString("tree_id") + "]"));
		}
	}
}
