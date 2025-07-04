package tannyjung.tanshugetrees.procedures;

import net.minecraft.world.entity.Entity;

public class RandomTreeTickPartCreateCoreTestProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		String unless = "";
		String do_not_replace = "";
		String type_next = "";
		String type_pre = "";
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
		if (entity.getPersistentData().getDouble("part_create2") + 1 > entity.getPersistentData().getDouble("part_create")
				- (entity.getPersistentData().getDouble((entity.getPersistentData().getString("type") + "_outer_level")) + entity.getPersistentData().getDouble((entity.getPersistentData().getString("type") + "_inner_level")))) {
			if (entity.getPersistentData().getDouble("part_create") < 1) {
				entity.getPersistentData().putDouble("part_thickness_change", 1);
			} else {
				entity.getPersistentData().putDouble("part_thickness_change", 0.5);
			}
		} else {
			entity.getPersistentData().putDouble("part_thickness_change", 1);
		}
		if (entity.getPersistentData().getDouble("part_create2") >= entity.getPersistentData().getDouble("part_create") - entity.getPersistentData().getDouble((entity.getPersistentData().getString("type") + "_outer_level"))) {
			entity.getPersistentData().putString("block_placer", (entity.getPersistentData().getString("type") + "_outer"));
			entity.getPersistentData().putString("setblock_unless",
					((" unless block ~ ~ ~ tanshugetrees:block_placer_" + entity.getPersistentData().getString("type") + "_inner") + "" + (" unless block ~ ~ ~ tanshugetrees:block_placer_" + entity.getPersistentData().getString("type") + "_core")));
		} else if (entity.getPersistentData().getDouble("part_create2") >= entity.getPersistentData().getDouble("part_create")
				- (entity.getPersistentData().getDouble((entity.getPersistentData().getString("type") + "_outer_level")) + entity.getPersistentData().getDouble((entity.getPersistentData().getString("type") + "_inner_level")))) {
			entity.getPersistentData().putString("block_placer", (entity.getPersistentData().getString("type") + "_inner"));
			entity.getPersistentData().putString("setblock_unless", (" unless block ~ ~ ~ tanshugetrees:block_placer_" + entity.getPersistentData().getString("type") + "_core"));
		} else {
			entity.getPersistentData().putString("block_placer", (entity.getPersistentData().getString("type") + "_core"));
			entity.getPersistentData().putString("setblock_unless", "");
		}
		if (!((entity.getPersistentData().getString("type")).equals("taproot") || (entity.getPersistentData().getString("type")).equals("trunk") || (entity.getPersistentData().getString("type")).equals("secondary_root"))) {
			entity.getPersistentData().putString("setblock_unless", (entity.getPersistentData().getString("setblock_unless") + "" + (" unless block ~ ~ ~ #tanshugetrees:block_placer_blacklist_" + entity.getPersistentData().getString("type"))));
			if (!(entity.getPersistentData().getString("block_placer")).equals(entity.getPersistentData().getString("type") + "_core")) {
				entity.getPersistentData().putString("setblock_unless", (entity.getPersistentData().getString("setblock_unless") + "" + (" unless block ~ ~ ~ tanshugetrees:block_placer_" + (type_pre + "_outer"))
						+ (" unless block ~ ~ ~ tanshugetrees:block_placer_" + (type_pre + "_inner")) + (" unless block ~ ~ ~ tanshugetrees:block_placer_" + (type_pre + "_core"))));
			}
		}
	}
}
