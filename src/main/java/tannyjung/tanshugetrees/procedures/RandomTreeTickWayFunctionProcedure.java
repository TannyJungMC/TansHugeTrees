package tannyjung.tanshugetrees.procedures;

import tannyjung.tanshugetrees.network.TanshugetreesModVariables;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;

import java.io.File;

public class RandomTreeTickWayFunctionProcedure {
	public static void execute(LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		File file = new File("");
		double way_function = 0;
		double length = 0;
		String type = "";
		entity.getPersistentData().putString("function", "");
		way_function = 1;
		for (int index0 = 0; index0 < 3; index0++) {
			if (!(entity.getPersistentData().getString(("function_way" + ("" + way_function).replace(".0", "")))).equals("")) {
				if ((entity.getPersistentData().getString(("function_way" + ("" + way_function).replace(".0", "") + "_type"))).equals(entity.getPersistentData().getString("type"))) {
					if (entity.getPersistentData().getDouble(("function_way" + ("" + way_function).replace(".0", "") + "_chance")) > Math.random()
							&& (entity.getPersistentData().getDouble(("function_way" + ("" + way_function).replace(".0", "") + "_max")) > 0
									|| entity.getPersistentData().getDouble(("function_way" + ("" + way_function).replace(".0", "") + "_max")) == 0)) {
						if ((entity.getPersistentData().getString("type")).equals("leaves")) {
							type = "leaves_twig";
						} else {
							type = entity.getPersistentData().getString("type");
						}
						if (entity.getPersistentData().getDouble((type + "_length_save")) == 0) {
							length = 100;
						} else {
							length = 100 - (entity.getPersistentData().getDouble((type + "_length")) / entity.getPersistentData().getDouble((type + "_length_save"))) * 100;
						}
						if (Math.floor(length) >= entity.getPersistentData().getDouble(("function_way" + ("" + way_function).replace(".0", "") + "_range_min"))
								&& Math.floor(length) <= entity.getPersistentData().getDouble(("function_way" + ("" + way_function).replace(".0", "") + "_range_max"))) {
							if (entity.getPersistentData().getDouble(("function_way" + ("" + way_function).replace(".0", "") + "_max")) > 1) {
								entity.getPersistentData().putDouble(("function_way" + ("" + way_function).replace(".0", "") + "_max"), (entity.getPersistentData().getDouble(("function_way" + ("" + way_function).replace(".0", "") + "_max")) - 1));
							} else if (entity.getPersistentData().getDouble(("function_way" + ("" + way_function).replace(".0", "") + "_max")) == 1) {
								entity.getPersistentData().putDouble(("function_way" + ("" + way_function).replace(".0", "") + "_max"), (-1));
							}
							if (TanshugetreesModVariables.MapVariables.get(world).auto_gen == false) {
								entity.getPersistentData().putString("function", (entity.getPersistentData().getString(("function_way" + ("" + way_function).replace(".0", "")))));
							} else {
								entity.getPersistentData().putString("function", ("f" + ("" + way_function).replace(".0", "")));
							}
							break;
						}
					}
				}
			}
			way_function = way_function + 1;
		}
	}
}
