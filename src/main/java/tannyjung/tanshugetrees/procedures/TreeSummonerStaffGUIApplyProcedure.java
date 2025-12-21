package tannyjung.tanshugetrees.procedures;

import tannyjung.tanshugetrees.init.TanshugetreesModMenus;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;

public class TreeSummonerStaffGUIApplyProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		if (((entity instanceof Player _entity0 && _entity0.containerMenu instanceof TanshugetreesModMenus.MenuAccessor _menu0) ? _menu0.getMenuState(0, "path", "") : "").equals("")) {
			if (entity instanceof Player _player && _player.containerMenu instanceof TanshugetreesModMenus.MenuAccessor _menu)
				_menu.sendMenuStateUpdate(_player, 0, "path", ((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getOrCreateTag().getString("path")), true);
		} else {
			(entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getOrCreateTag().putString("path",
					((entity instanceof Player _entity4 && _entity4.containerMenu instanceof TanshugetreesModMenus.MenuAccessor _menu4) ? _menu4.getMenuState(0, "path", "") : ""));
		}
	}
}