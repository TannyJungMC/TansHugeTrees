package tannyjung.tanshugetrees.procedures;

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

public class RandomTreeTickCreateProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		String type_next = "";
		String type_pre = "";
		double variable_number = 0;
		double variable_number2 = 0;
		double variable_number3 = 0;
		double part_size = 0;
		if (!("").equals("pre-next")) {
			type_pre = "";
			type_next = "";
			if (true) {
				if ((entity.getPersistentData().getString("type")).equals("secondary_root")) {
					type_pre = "taproot";
				} else if ((entity.getPersistentData().getString("type")).equals("tertiary_root")) {
					type_pre = "secondary_root";
				} else if ((entity.getPersistentData().getString("type")).equals("fine_root")) {
					type_pre = "tertiary_root";
				}
				if ((entity.getPersistentData().getString("type")).equals("taproot")) {
					type_next = "secondary_root";
				} else if ((entity.getPersistentData().getString("type")).equals("secondary_root")) {
					type_next = "tertiary_root";
				} else if ((entity.getPersistentData().getString("type")).equals("tertiary_root")) {
					type_next = "fine_root";
				}
			}
			if (true) {
				if ((entity.getPersistentData().getString("type")).equals("branch")) {
					type_pre = "trunk";
				} else if ((entity.getPersistentData().getString("type")).equals("twig")) {
					type_pre = "branch";
				} else if ((entity.getPersistentData().getString("type")).equals("leaves_twig")) {
					type_pre = "twig";
				} else if ((entity.getPersistentData().getString("type")).equals("leaves")) {
					type_pre = "leaves_twig";
				}
				if ((entity.getPersistentData().getString("type")).equals("trunk")) {
					type_next = "branch";
				} else if ((entity.getPersistentData().getString("type")).equals("branch")) {
					type_next = "twig";
				} else if ((entity.getPersistentData().getString("type")).equals("twig")) {
					type_next = "leaves_twig";
				} else if ((entity.getPersistentData().getString("type")).equals("leaves_twig")) {
					type_next = "leaves";
				}
			}
		}
		entity.getPersistentData().putString("up_down", "");
		if (entity.getPersistentData().getDouble((type_next + "_count")) > entity.getPersistentData().getDouble((type_next + "_last_count_min"))
				&& entity.getPersistentData().getDouble((entity.getPersistentData().getString("type") + "_length")) < entity.getPersistentData().getDouble((entity.getPersistentData().getString("type") + "_length_save"))
						* (1 - entity.getPersistentData().getDouble((type_next + "_random_percent")) * 0.01)
				&& (Mth.nextDouble(RandomSource.create(), 0, 99) < entity.getPersistentData().getDouble((type_next + "_random"))
						|| entity.getPersistentData().getBoolean((type_next + "_random_auto")) == true && entity.getPersistentData().getDouble((type_next + "_distance")) <= 0)
				|| entity.getPersistentData().getDouble((entity.getPersistentData().getString("type") + "_length")) <= 0) {
			if (entity.getPersistentData().getDouble((type_next + "_count")) > 0) {
				if (entity.getPersistentData().getBoolean((type_next + "_random_auto")) == true) {
					entity.getPersistentData().putDouble((type_next + "_distance"),
							((entity.getPersistentData().getDouble((entity.getPersistentData().getString("type") + "_length_save")) * (1 - entity.getPersistentData().getDouble((type_next + "_random_percent")) * 0.01))
									/ (entity.getPersistentData().getDouble((type_next + "_count_save")) - entity.getPersistentData().getDouble((type_next + "_last_count_min")))));
				} else {
					entity.getPersistentData().putDouble((type_next + "_distance"), (entity.getPersistentData().getDouble((type_next + "_distance_min"))));
				}
				if ((entity.getPersistentData().getString("type")).equals("taproot")) {
					entity.getPersistentData().putString("type", "secondary_root");
				} else if ((entity.getPersistentData().getString("type")).equals("secondary_root")) {
					entity.getPersistentData().putString("type", "tertiary_root");
				} else if ((entity.getPersistentData().getString("type")).equals("tertiary_root")) {
					entity.getPersistentData().putString("type", "fine_root");
				}
				if ((entity.getPersistentData().getString("type")).equals("trunk")) {
					entity.getPersistentData().putString("type", "branch");
				} else if ((entity.getPersistentData().getString("type")).equals("branch")) {
					entity.getPersistentData().putString("type", "twig");
				} else if ((entity.getPersistentData().getString("type")).equals("twig")) {
					entity.getPersistentData().putString("type", "leaves_twig");
				} else if ((entity.getPersistentData().getString("type")).equals("leaves_twig")) {
					entity.getPersistentData().putString("type", "leaves");
				}
				entity.getPersistentData().putString("step", "summon");
				entity.getPersistentData().putString("up_down", "up");
			} else {
				if (world instanceof ServerLevel _level)
					_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
							("kill @e[name=THT-tree_" + (entity.getPersistentData().getString("type") + "_" + entity.getPersistentData().getString("tree_id")) + "]"));
				if ((entity.getPersistentData().getString("type")).equals("taproot")) {
					if (entity.getPersistentData().getDouble((entity.getPersistentData().getString("type") + "_length")) <= 0 && entity.getPersistentData().getDouble((entity.getPersistentData().getString("type") + "_count")) <= 0) {
						entity.getPersistentData().putString("step", "summon");
						entity.getPersistentData().putString("type", "trunk");
					}
				} else if ((entity.getPersistentData().getString("type")).equals("secondary_root")) {
					entity.getPersistentData().putString("type", "taproot");
				} else if ((entity.getPersistentData().getString("type")).equals("tertiary_root")) {
					entity.getPersistentData().putString("type", "secondary_root");
				} else if ((entity.getPersistentData().getString("type")).equals("fine_root")) {
					entity.getPersistentData().putString("type", "tertiary_root");
				}
				if ((entity.getPersistentData().getString("type")).equals("trunk")) {
					if (entity.getPersistentData().getDouble((entity.getPersistentData().getString("type") + "_length")) <= 0 && entity.getPersistentData().getDouble((entity.getPersistentData().getString("type") + "_count")) <= 0) {
						entity.getPersistentData().putString("step", "end");
					} else {
						entity.getPersistentData().putString("step", "summon");
					}
				} else if ((entity.getPersistentData().getString("type")).equals("branch")) {
					entity.getPersistentData().putString("type", "trunk");
				} else if ((entity.getPersistentData().getString("type")).equals("twig")) {
					entity.getPersistentData().putString("type", "branch");
				} else if ((entity.getPersistentData().getString("type")).equals("leaves_twig")) {
					entity.getPersistentData().putString("type", "twig");
				} else if ((entity.getPersistentData().getString("type")).equals("leaves")) {
					entity.getPersistentData().putString("type", "leaves_twig");
				}
				entity.getPersistentData().putString("up_down", "down");
			}
		} else {
			if (true) {
				if (true) {
					if (entity.getPersistentData().getDouble((entity.getPersistentData().getString("type") + "_curvature_horizontal")) != 0
							|| entity.getPersistentData().getDouble((entity.getPersistentData().getString("type") + "_curvature_vertical")) != 0) {
						if (world instanceof ServerLevel _level)
							_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
									("execute as @e[name=THT-tree_" + (entity.getPersistentData().getString("type") + "_" + entity.getPersistentData().getString("tree_id")) + "] at @s run tp @s ~ ~ ~ ~"
											+ Mth.nextDouble(RandomSource.create(), entity.getPersistentData().getDouble((entity.getPersistentData().getString("type") + "_curvature_horizontal")) * (-1),
													entity.getPersistentData().getDouble((entity.getPersistentData().getString("type") + "_curvature_horizontal")))
											+ " ~" + Mth.nextDouble(RandomSource.create(), entity.getPersistentData().getDouble((entity.getPersistentData().getString("type") + "_curvature_vertical")) * (-1),
													entity.getPersistentData().getDouble((entity.getPersistentData().getString("type") + "_curvature_vertical")))));
					}
					if (entity.getPersistentData().getDouble((entity.getPersistentData().getString("type") + "_crawling")) != 0) {
						if (world instanceof ServerLevel _level)
							_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
									("execute as @e[name=THT-tree_" + (entity.getPersistentData().getString("type") + "_" + entity.getPersistentData().getString("tree_id")) + "] at @s positioned ^ ^ ^"
											+ entity.getPersistentData().getDouble((entity.getPersistentData().getString("type") + "_thickness")) + " if block ^ ^"
											+ entity.getPersistentData().getDouble((entity.getPersistentData().getString("type") + "_crawling_air_height")) + " ^"
											+ entity.getPersistentData().getDouble((entity.getPersistentData().getString("type") + "_crawling_air_forward")) + " #tanshugetrees:passable_blocks unless block ^ ^"
											+ entity.getPersistentData().getDouble((entity.getPersistentData().getString("type") + "_crawling_height")) + " ^"
											+ entity.getPersistentData().getDouble((entity.getPersistentData().getString("type") + "_crawling_forward")) + " #tanshugetrees:passable_blocks run tp @s ~ ~ ~ ~ ~"
											+ entity.getPersistentData().getDouble((entity.getPersistentData().getString("type") + "_crawling"))));
						if (world instanceof ServerLevel _level)
							_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
									("execute as @e[name=THT-tree_" + (entity.getPersistentData().getString("type") + "_" + entity.getPersistentData().getString("tree_id")) + "] at @s positioned ^ ^ ^"
											+ entity.getPersistentData().getDouble((entity.getPersistentData().getString("type") + "_thickness")) + " if block ^ ^"
											+ entity.getPersistentData().getDouble((entity.getPersistentData().getString("type") + "_crawling_air_height")) * (-1) + " ^"
											+ entity.getPersistentData().getDouble((entity.getPersistentData().getString("type") + "_crawling_air_forward")) + " #tanshugetrees:passable_blocks unless block ^ ^"
											+ entity.getPersistentData().getDouble((entity.getPersistentData().getString("type") + "_crawling_height")) * (-1) + " ^"
											+ entity.getPersistentData().getDouble((entity.getPersistentData().getString("type") + "_crawling_forward")) + " #tanshugetrees:passable_blocks run tp @s ~ ~ ~ ~ ~"
											+ entity.getPersistentData().getDouble((entity.getPersistentData().getString("type") + "_crawling")) * (-1)));
					}
					if (entity.getPersistentData().getDouble((entity.getPersistentData().getString("type") + "_dodging")) != 0) {
						if (world instanceof ServerLevel _level)
							_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
									("execute as @e[name=THT-tree_" + (entity.getPersistentData().getString("type") + "_" + entity.getPersistentData().getString("tree_id")) + "] at @s positioned ^ ^ ^"
											+ entity.getPersistentData().getDouble((entity.getPersistentData().getString("type") + "_thickness")) + "] at @s unless block ^"
											+ entity.getPersistentData().getDouble((entity.getPersistentData().getString("type") + "_dodging_sideward")) + " ^ ^"
											+ entity.getPersistentData().getDouble((entity.getPersistentData().getString("type") + "_dodging_forward")) + " #tanshugetrees:passable_blocks run tp @s ~ ~ ~ ~"
											+ entity.getPersistentData().getDouble((entity.getPersistentData().getString("type") + "_dodging")) + " ~"));
						if (world instanceof ServerLevel _level)
							_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
									("execute as @e[name=THT-tree_" + (entity.getPersistentData().getString("type") + "_" + entity.getPersistentData().getString("tree_id")) + "] at @s positioned ^ ^ ^"
											+ entity.getPersistentData().getDouble((entity.getPersistentData().getString("type") + "_thickness")) + "] at @s unless block ^"
											+ entity.getPersistentData().getDouble((entity.getPersistentData().getString("type") + "_dodging_sideward")) * (-1) + " ^ ^"
											+ entity.getPersistentData().getDouble((entity.getPersistentData().getString("type") + "_dodging_forward")) + " #tanshugetrees:passable_blocks run tp @s ~ ~ ~ ~"
											+ entity.getPersistentData().getDouble((entity.getPersistentData().getString("type") + "_dodging")) * (-1) + " ~"));
					}
					if (entity.getPersistentData().getDouble((entity.getPersistentData().getString("type") + "_gravity_weightiness")) != 0) {
						if (world instanceof ServerLevel _level)
							_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
									("execute as @e[name=THT-tree_" + (entity.getPersistentData().getString("type") + "_" + entity.getPersistentData().getString("tree_id")) + ",x_rotation="
											+ entity.getPersistentData().getDouble((entity.getPersistentData().getString("type") + "_gravity_min")) + "..90] at @s unless entity @s[x_rotation=90] run tp @s ~ ~ ~ ~ ~-"
											+ entity.getPersistentData().getDouble((entity.getPersistentData().getString("type") + "_gravity_weightiness"))));
						if (world instanceof ServerLevel _level)
							_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
									("execute as @e[name=THT-tree_" + (entity.getPersistentData().getString("type") + "_" + entity.getPersistentData().getString("tree_id")) + ",x_rotation=-90.."
											+ entity.getPersistentData().getDouble((entity.getPersistentData().getString("type") + "_gravity_max")) + "] at @s unless entity @s[x_rotation=-90] run tp @s ~ ~ ~ ~ ~"
											+ entity.getPersistentData().getDouble((entity.getPersistentData().getString("type") + "_gravity_weightiness"))));
					}
				}
				if (world instanceof ServerLevel _level)
					_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
							("execute as @e[name=THT-tree_" + (entity.getPersistentData().getString("type") + "_" + entity.getPersistentData().getString("tree_id")) + "] at @s run tp @s ^ ^ ^" + 1));
				entity.getPersistentData().putDouble((entity.getPersistentData().getString("type") + "_length"), (entity.getPersistentData().getDouble((entity.getPersistentData().getString("type") + "_length")) - 1));
				if (true) {
					variable_number = entity.getPersistentData().getDouble((entity.getPersistentData().getString("type") + "_thickness"))
							- (entity.getPersistentData().getDouble((entity.getPersistentData().getString("type") + "_thickness_max")) - entity.getPersistentData().getDouble((entity.getPersistentData().getString("type") + "_thickness_min")))
									/ entity.getPersistentData().getDouble((entity.getPersistentData().getString("type") + "_length_save"));
					if (variable_number >= 0) {
						entity.getPersistentData().putDouble((entity.getPersistentData().getString("type") + "_thickness"), variable_number);
					}
				}
				entity.getPersistentData().putDouble((type_next + "_distance"), (entity.getPersistentData().getDouble((type_next + "_distance")) - 1));
				entity.getPersistentData().putDouble("generate_speed_tp_test", (entity.getPersistentData().getDouble("generate_speed_tp_test") - 1));
				if (TanshugetreesModVariables.MapVariables.get(world).auto_gen == false) {
					if (world instanceof ServerLevel _level)
						_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
								("execute at @e[name=THT-tree_" + (entity.getPersistentData().getString("type") + "_" + entity.getPersistentData().getString("tree_id")) + "] run kill @e[type=item,distance=.."
										+ (entity.getPersistentData().getDouble((entity.getPersistentData().getString("type") + "_thickness")) + 5) + "]"));
				} else {
					if (world instanceof ServerLevel _level)
						_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
								("execute if dimension tanshugetrees:dimension at @e[name=THT-tree_" + (entity.getPersistentData().getString("type") + "_" + entity.getPersistentData().getString("tree_id")) + "] run forceload add ~" + 100 + " ~" + 100
										+ " ~-" + 100 + " ~-" + 100));
				}
				entity.getPersistentData().putString("step", "part_create");
			}
		}
	}
}
