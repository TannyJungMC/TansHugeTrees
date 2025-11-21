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
	public static final RegistryObject<CreativeModeTab> TAB = REGISTRY.register("tab",
			() -> CreativeModeTab.builder().title(Component.translatable("item_group.tanshugetrees.tab")).icon(() -> new ItemStack(TanshugetreesModBlocks.SAPLING_YOKAI.get())).displayItems((parameters, tabData) -> {
				tabData.accept(TanshugetreesModBlocks.WAYPOINT_FLOWER.get().asItem());
				tabData.accept(TanshugetreesModBlocks.TREE_GENERATOR.get().asItem());
				tabData.accept(TanshugetreesModBlocks.SAPLING_HALCYON.get().asItem());
				tabData.accept(TanshugetreesModBlocks.SAPLING_YOKAI.get().asItem());
				tabData.accept(TanshugetreesModBlocks.SAPLING_REDWOOD.get().asItem());
				tabData.accept(TanshugetreesModBlocks.SAPLING_MALUS_DOMESTICA.get().asItem());
				tabData.accept(TanshugetreesModBlocks.SAPLING_WENDY.get().asItem());
				tabData.accept(TanshugetreesModBlocks.SAPLING_MANGROVE.get().asItem());
				tabData.accept(TanshugetreesModBlocks.SAPLING_FALCON.get().asItem());
				tabData.accept(TanshugetreesModBlocks.SAPLING_SKY_ISLAND_CHAIN.get().asItem());
			}).build());
}