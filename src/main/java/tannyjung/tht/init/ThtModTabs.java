/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package tannyjung.tht.init;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.CreativeModeTab;

public class ThtModTabs {
	public static CreativeModeTab TAB_THT_TAB;

	public static void load() {
		TAB_THT_TAB = new CreativeModeTab("tab_tht_tab") {
			@Override
			public ItemStack makeIcon() {
				return new ItemStack(ThtModBlocks.YOKAI.get());
			}

			@Override
			public boolean hasSearchBar() {
				return false;
			}
		};
	}
}
