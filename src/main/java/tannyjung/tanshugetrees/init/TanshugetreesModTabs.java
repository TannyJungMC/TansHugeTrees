/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package tannyjung.tanshugetrees.init;

import tannyjung.tanshugetrees.TanshugetreesMod;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.network.chat.Component;
import net.minecraft.core.registries.Registries;

public class TanshugetreesModTabs {
	public static final DeferredRegister<CreativeModeTab> REGISTRY = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, TanshugetreesMod.MODID);
	public static final RegistryObject<CreativeModeTab> THT_TAB = REGISTRY.register("tht_tab",
			() -> CreativeModeTab.builder().title(Component.translatable("item_group.tanshugetrees.tht_tab")).icon(() -> new ItemStack(TanshugetreesModBlocks.YOKAI.get())).displayItems((parameters, tabData) -> {
				tabData.accept(TanshugetreesModBlocks.WAYPOINT_FLOWER.get().asItem());
				tabData.accept(TanshugetreesModItems.PRESET_FIXER.get());
				tabData.accept(TanshugetreesModBlocks.YOKAI.get().asItem());
				tabData.accept(TanshugetreesModBlocks.RANDOM_TREE.get().asItem());
				tabData.accept(TanshugetreesModBlocks.HALCYON.get().asItem());
			}).build());
}