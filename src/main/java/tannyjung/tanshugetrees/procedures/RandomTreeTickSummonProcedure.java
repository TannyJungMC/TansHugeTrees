package tannyjung.tanshugetrees.procedures;

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

public class RandomTreeTickSummonProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		double direction_below = 0;
		double direction_above = 0;
		double center_direction_vertical = 0;
		double center_direction_horizontal = 0;
		double center_direction_forward = 0;
		double center_direction_height = 0;
		double count = 0;
		double loop = 0;
		double percent = 0;
		String type_next = "";
		String type_pre = "";
		String type = "";
		String step = "";
		String above_below = "";
		if (!("Get Type Pre - Next").isEmpty()) {
			type_pre = RandomTreeGetPreNextProcedure.execute(entity, "previous");
			type_next = RandomTreeGetPreNextProcedure.execute(entity, "next");
		}
		entity.getPersistentData().putDouble((entity.getPersistentData().getString("type") + "_count"), (entity.getPersistentData().getDouble((entity.getPersistentData().getString("type") + "_count")) - 1));
		if (!(entity.getPersistentData().getString("type")).equals("leaves")) {
			entity.getPersistentData().putDouble((entity.getPersistentData().getString("type") + "_thickness"), (entity.getPersistentData().getDouble((entity.getPersistentData().getString("type") + "_thickness_max"))));
			entity.getPersistentData().putDouble((entity.getPersistentData().getString("type") + "_length"), (Mth.nextInt(RandomSource.create(),
					(int) entity.getPersistentData().getDouble((entity.getPersistentData().getString("type") + "_length_min")), (int) entity.getPersistentData().getDouble((entity.getPersistentData().getString("type") + "_length_max")))));
			entity.getPersistentData().putDouble((type_next + "_count"),
					(Mth.nextInt(RandomSource.create(), (int) entity.getPersistentData().getDouble((type_next + "_count_min")), (int) entity.getPersistentData().getDouble((type_next + "_count_max")))));
			while (loop < 3) {
				loop = loop + 1;
				if (loop == 1) {
					step = "length";
				} else if (loop == 2) {
					step = "thickness";
				} else if (loop == 3) {
					step = "count";
				}
				if ((step).equals("count")) {
					type = type_next;
				} else {
					type = entity.getPersistentData().getString("type");
				}
				if (entity.getPersistentData().getDouble((entity.getPersistentData().getString((type + "_" + step + "_reduce_from")) + "_length_save")) > 0) {
					if (!((step).equals("count") && ((entity.getPersistentData().getString("type")).equals("taproot") || (entity.getPersistentData().getString("type")).equals("fine_root")
							|| (entity.getPersistentData().getString("type")).equals("trunk") || (entity.getPersistentData().getString("type")).equals("leaves_twig")))) {
						if (entity.getPersistentData().getDouble((entity.getPersistentData().getString((type + "" + ("_" + step + "_reduce_from"))) + "_length")) > entity.getPersistentData()
								.getDouble((entity.getPersistentData().getString((type + "" + ("_" + step + "_reduce_from"))) + "_length_save")) * (1 - entity.getPersistentData().getDouble((type + "_" + step + "_reduce_center")) * 0.01)) {
							above_below = "below";
							percent = (entity.getPersistentData().getDouble((entity.getPersistentData().getString((type + "" + ("_" + step + "_reduce_from"))) + "_length_save"))
									- entity.getPersistentData().getDouble((entity.getPersistentData().getString((type + "" + ("_" + step + "_reduce_from"))) + "_length")))
									/ (entity.getPersistentData().getDouble((entity.getPersistentData().getString((type + "" + ("_" + step + "_reduce_from"))) + "_length_save"))
											- entity.getPersistentData().getDouble((entity.getPersistentData().getString((type + "" + ("_" + step + "_reduce_from"))) + "_length_save"))
													* (1 - entity.getPersistentData().getDouble((type + "_" + step + "_reduce_center")) * 0.01));
						} else {
							above_below = "above";
							if (entity.getPersistentData().getDouble((entity.getPersistentData().getString((type + "" + ("_" + step + "_reduce_from"))) + "_length")) == 0
									&& entity.getPersistentData().getDouble((entity.getPersistentData().getString((type + "" + ("_" + step + "_reduce_from"))) + "_length_save"))
											* (1 - entity.getPersistentData().getDouble((type + "_" + step + "_reduce_center")) * 0.01) == 0) {
								percent = 1;
							} else {
								percent = entity.getPersistentData().getDouble((entity.getPersistentData().getString((type + "" + ("_" + step + "_reduce_from"))) + "_length"))
										/ (entity.getPersistentData().getDouble((entity.getPersistentData().getString((type + "" + ("_" + step + "_reduce_from"))) + "_length_save"))
												* (1 - entity.getPersistentData().getDouble((type + "_" + step + "_reduce_center")) * 0.01));
							}
						}
						percent = percent + (1 - percent) * entity.getPersistentData().getDouble((type + "" + ("_" + step + "_reduce_" + above_below))) * 0.01;
						if ((step).equals("length")) {
							entity.getPersistentData().putDouble((type + "_length"), Math.ceil(percent * entity.getPersistentData().getDouble((type + "_length"))));
						} else if ((step).equals("thickness")) {
							entity.getPersistentData().putDouble((type + "_thickness"), (percent * entity.getPersistentData().getDouble((type + "_thickness_max"))));
						} else if ((step).equals("count")) {
							entity.getPersistentData().putDouble((type + "_count"), Math.ceil(percent * entity.getPersistentData().getDouble((type + "_count"))));
						}
					}
				}
			}
			entity.getPersistentData().putDouble((entity.getPersistentData().getString("type") + "_length_save"), (entity.getPersistentData().getDouble((entity.getPersistentData().getString("type") + "_length"))));
			entity.getPersistentData().putDouble((type_next + "_count_save"), (entity.getPersistentData().getDouble((type_next + "_count"))));
		} else {
			if (entity.getPersistentData().getDouble((entity.getPersistentData().getString("leaves_size_reduce_from") + "_length")) > entity.getPersistentData()
					.getDouble((entity.getPersistentData().getString("leaves_size_reduce_from") + "_length_save")) * (1 - entity.getPersistentData().getDouble("leaves_size_reduce_center") * 0.01)) {
				percent = (entity.getPersistentData().getDouble((entity.getPersistentData().getString("leaves_size_reduce_from") + "_length_save"))
						- entity.getPersistentData().getDouble((entity.getPersistentData().getString("leaves_size_reduce_from") + "_length")))
						/ (entity.getPersistentData().getDouble((entity.getPersistentData().getString("leaves_size_reduce_from") + "_length_save"))
								- entity.getPersistentData().getDouble((entity.getPersistentData().getString("leaves_size_reduce_from") + "_length_save")) * (1 - entity.getPersistentData().getDouble("leaves_size_reduce_center") * 0.01));
				percent = percent + (1 - percent) * entity.getPersistentData().getDouble("leaves_size_reduce_below") * 0.01;
			} else {
				if (entity.getPersistentData().getDouble((entity.getPersistentData().getString("leaves_size_reduce_from") + "_length")) <= 0
						&& !(entity.getPersistentData().getDouble((entity.getPersistentData().getString("leaves_size_reduce_from") + "_length_save")) * (1 - entity.getPersistentData().getDouble("leaves_size_reduce_center") * 0.01) > 0)) {
					percent = 1;
				} else {
					percent = entity.getPersistentData().getDouble((entity.getPersistentData().getString("leaves_size_reduce_from") + "_length"))
							/ (entity.getPersistentData().getDouble((entity.getPersistentData().getString("leaves_size_reduce_from") + "_length_save")) * (1 - entity.getPersistentData().getDouble("leaves_size_reduce_center") * 0.01));
				}
				percent = percent + (1 - percent) * entity.getPersistentData().getDouble("leaves_size_reduce_above") * 0.01;
			}
			entity.getPersistentData().putDouble("leaves_size", Math.ceil(percent * Mth.nextInt(RandomSource.create(), (int) entity.getPersistentData().getDouble("leaves_size_min"), (int) entity.getPersistentData().getDouble("leaves_size_max"))));
		}
		if ((entity.getPersistentData().getString("type")).equals("taproot") || (entity.getPersistentData().getString("type")).equals("trunk")) {
			if (world instanceof ServerLevel _level)
				_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
						("summon marker ~ ~ ~ {Tags:[\"THT\",\"THT-random_tree\",\"THT-tree_" + "" + entity.getPersistentData().getString("tree_id") + "\"],CustomName:'{\"text\":\"THT-tree_"
								+ (entity.getPersistentData().getString("type") + "_" + entity.getPersistentData().getString("tree_id")) + "\"}'}"));
			if (world instanceof ServerLevel _level)
				_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
						("execute as @e[name=THT-tree_" + (entity.getPersistentData().getString("type") + "_" + entity.getPersistentData().getString("tree_id")) + "] at @s run tp @s ~ ~ ~ "
								+ Mth.nextDouble(RandomSource.create(), entity.getPersistentData().getDouble((entity.getPersistentData().getString("type") + "_start_direction_min")),
										entity.getPersistentData().getDouble((entity.getPersistentData().getString("type") + "_start_direction_max")))
								+ " " + Mth.nextDouble(RandomSource.create(), entity.getPersistentData().getDouble((entity.getPersistentData().getString("type") + "_start_gravity_max")),
										entity.getPersistentData().getDouble((entity.getPersistentData().getString("type") + "_start_gravity_min")))));
		} else {
			if (entity.getPersistentData().getBoolean((entity.getPersistentData().getString("type") + "_center_direction")) == false) {
				center_direction_horizontal = Mth.nextDouble(RandomSource.create(), entity.getPersistentData().getDouble((entity.getPersistentData().getString("type") + "_start_horizontal")) * (-1),
						entity.getPersistentData().getDouble((entity.getPersistentData().getString("type") + "_start_horizontal")));
				center_direction_vertical = Mth.nextDouble(RandomSource.create(), entity.getPersistentData().getDouble((entity.getPersistentData().getString("type") + "_start_vertical")) * (-1),
						entity.getPersistentData().getDouble((entity.getPersistentData().getString("type") + "_start_vertical")));
				center_direction_forward = Mth.nextDouble(RandomSource.create(), entity.getPersistentData().getDouble((entity.getPersistentData().getString("type") + "_start_forward_min")),
						entity.getPersistentData().getDouble((entity.getPersistentData().getString("type") + "_start_forward_max")));
				center_direction_height = Mth.nextDouble(RandomSource.create(), entity.getPersistentData().getDouble((entity.getPersistentData().getString("type") + "_start_height_min")),
						entity.getPersistentData().getDouble((entity.getPersistentData().getString("type") + "_start_height_max")));
			} else {
				if (entity.getPersistentData()
						.getDouble((entity.getPersistentData().getString((entity.getPersistentData().getString("type") + "_center_direction_from")) + "_length")) > entity.getPersistentData()
								.getDouble((entity.getPersistentData().getString((entity.getPersistentData().getString("type") + "_center_direction_from")) + "_length_save"))
								* (1 - entity.getPersistentData().getDouble((entity.getPersistentData().getString("type") + "_center_direction_center")) * 0.01)) {
					direction_below = (entity.getPersistentData().getDouble((entity.getPersistentData().getString((entity.getPersistentData().getString("type") + "_center_direction_from")) + "_length_save"))
							- entity.getPersistentData().getDouble((entity.getPersistentData().getString((entity.getPersistentData().getString("type") + "_center_direction_from")) + "_length")))
							/ (entity.getPersistentData().getDouble((entity.getPersistentData().getString((entity.getPersistentData().getString("type") + "_center_direction_from")) + "_length_save"))
									- entity.getPersistentData().getDouble((entity.getPersistentData().getString((entity.getPersistentData().getString("type") + "_center_direction_from")) + "_length_save"))
											* (1 - entity.getPersistentData().getDouble((entity.getPersistentData().getString("type") + "_center_direction_center")) * 0.01));
					if (true) {
						center_direction_vertical = entity.getPersistentData().getDouble((entity.getPersistentData().getString("type") + "_center_direction_vertical_below")) * direction_below;
						center_direction_horizontal = entity.getPersistentData().getDouble((entity.getPersistentData().getString("type") + "_center_direction_horizontal_below")) * direction_below;
						center_direction_forward = entity.getPersistentData().getDouble((entity.getPersistentData().getString("type") + "_center_direction_forward_below")) * (1 - direction_below);
						center_direction_height = entity.getPersistentData().getDouble((entity.getPersistentData().getString("type") + "_center_direction_height_below")) * (1 - direction_below);
					}
				} else {
					direction_above = entity.getPersistentData().getDouble((entity.getPersistentData().getString((entity.getPersistentData().getString("type") + "_center_direction_from")) + "_length"))
							/ (entity.getPersistentData().getDouble((entity.getPersistentData().getString((entity.getPersistentData().getString("type") + "_center_direction_from")) + "_length_save"))
									* (1 - entity.getPersistentData().getDouble((entity.getPersistentData().getString("type") + "_center_direction_center")) * 0.01));
					if (true) {
						center_direction_vertical = entity.getPersistentData().getDouble((entity.getPersistentData().getString("type") + "_center_direction_vertical_above")) * direction_above;
						center_direction_horizontal = entity.getPersistentData().getDouble((entity.getPersistentData().getString("type") + "_center_direction_horizontal_above")) * direction_above;
						center_direction_forward = entity.getPersistentData().getDouble((entity.getPersistentData().getString("type") + "_center_direction_forward_above")) * (1 - direction_above);
						center_direction_height = entity.getPersistentData().getDouble((entity.getPersistentData().getString("type") + "_center_direction_height_above")) * (1 - direction_above);
					}
				}
				center_direction_horizontal = Mth.nextDouble(RandomSource.create(), center_direction_horizontal * (-1), center_direction_horizontal);
				center_direction_vertical = Mth.nextDouble(RandomSource.create(), center_direction_vertical * (-1), center_direction_vertical);
			}
			if (true) {
				for (int index1 = 0; index1 < 1; index1++) {
					{
						Entity _ent = entity;
						if (!_ent.level().isClientSide() && _ent.getServer() != null) {
							_ent.getServer().getCommands().performPrefixedCommand(
									new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null, 4, _ent.getName().getString(), _ent.getDisplayName(),
											_ent.level().getServer(), _ent),
									("execute at @e[name=THT-tree_" + (type_pre + "_" + entity.getPersistentData().getString("tree_id")) + "] positioned ^" + center_direction_horizontal + " ^" + center_direction_vertical + " ^"
											+ center_direction_forward + " positioned ~ ~" + center_direction_height + " ~ run summon marker ~ ~ ~ {Tags:[\"THT\",\"THT-random_tree\",\"THT-tree_" + entity.getPersistentData().getString("tree_id")
											+ "\"],CustomName:'{\"text\":\"THT-tree_" + (entity.getPersistentData().getString("type") + "_" + entity.getPersistentData().getString("tree_id")) + "\"}'}"));
						}
					}
					if (CommandResultEntityProcedure.execute(entity, "execute if entity @e[name=THT-tree_" + (entity.getPersistentData().getString("type") + "_" + entity.getPersistentData().getString("tree_id")) + "]")) {
						break;
					}
				}
			}
			if (world instanceof ServerLevel _level)
				_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
						("execute as @e[name=THT-tree_" + (entity.getPersistentData().getString("type") + "_" + entity.getPersistentData().getString("tree_id")) + "] at @s facing entity @e[name=THT-tree_"
								+ (type_pre + "_" + entity.getPersistentData().getString("tree_id")) + ",limit=1] feet positioned as @e[name=THT-tree_" + (type_pre + "_" + entity.getPersistentData().getString("tree_id"))
								+ ",limit=1] run tp @s ~ ~ ~ facing ^ ^ ^-1"));
			if ((entity.getPersistentData().getString("type")).equals("leaves_twig")) {
				entity.getPersistentData().putDouble("leaves_count", 1);
				entity.getPersistentData().putDouble("leaves_count_save", 1);
			}
		}
		entity.getPersistentData().putString("step", "part_create");
	}
}
