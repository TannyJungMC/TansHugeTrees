package tannyjung.tanshugetrees.procedures;

import net.minecraft.world.entity.Entity;

public class RandomTreeGetPreNextProcedure {
	public static String execute(Entity entity, String get) {
		if (entity == null || get == null)
			return "";
		String return_text = "";
		if ((get).equals("previous")) {
			if ((entity.getPersistentData().getString("type")).equals("secondary_root")) {
				return_text = "taproot";
			} else if ((entity.getPersistentData().getString("type")).equals("tertiary_root")) {
				return_text = "secondary_root";
			} else if ((entity.getPersistentData().getString("type")).equals("fine_root")) {
				return_text = "tertiary_root";
			}
			if ((entity.getPersistentData().getString("type")).equals("branch")) {
				return_text = "trunk";
			} else if ((entity.getPersistentData().getString("type")).equals("twig")) {
				return_text = "branch";
			} else if ((entity.getPersistentData().getString("type")).equals("leaves_twig")) {
				return_text = "twig";
			} else if ((entity.getPersistentData().getString("type")).equals("leaves")) {
				return_text = "leaves_twig";
			}
		} else if ((get).equals("next")) {
			if ((entity.getPersistentData().getString("type")).equals("taproot")) {
				return_text = "secondary_root";
			} else if ((entity.getPersistentData().getString("type")).equals("secondary_root")) {
				return_text = "tertiary_root";
			} else if ((entity.getPersistentData().getString("type")).equals("tertiary_root")) {
				return_text = "fine_root";
			}
			if ((entity.getPersistentData().getString("type")).equals("trunk")) {
				return_text = "branch";
			} else if ((entity.getPersistentData().getString("type")).equals("branch")) {
				return_text = "twig";
			} else if ((entity.getPersistentData().getString("type")).equals("twig")) {
				return_text = "leaves_twig";
			} else if ((entity.getPersistentData().getString("type")).equals("leaves_twig")) {
				return_text = "leaves";
			}
		}
		return return_text;
	}
}
