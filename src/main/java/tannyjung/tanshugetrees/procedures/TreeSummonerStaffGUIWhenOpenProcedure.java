package tannyjung.tanshugetrees.procedures;

import tannyjung.tanshugetrees.init.TanshugetreesModMenus;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;

public class TreeSummonerStaffGUIWhenOpenProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		if (((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getOrCreateTag().getString("path")).isEmpty() == false) {
			if (entity instanceof Player _player && _player.containerMenu instanceof TanshugetreesModMenus.MenuAccessor _menu)
				_menu.sendMenuStateUpdate(_player, 0, "path", ((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getOrCreateTag().getString("path")), true);
		}
	}
}