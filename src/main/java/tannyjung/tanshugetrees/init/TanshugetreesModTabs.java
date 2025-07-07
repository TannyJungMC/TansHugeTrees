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
				tabData.accept(TanshugetreesModBlocks.RANDOM_TREE_BLOCK.get().asItem());
				tabData.accept(TanshugetreesModBlocks.WAYPOINT_FLOWER.get().asItem());
				tabData.accept(TanshugetreesModItems.PRESET_FIXER.get());
				tabData.accept(TanshugetreesModBlocks.AGATHOS.get().asItem());
				tabData.accept(TanshugetreesModBlocks.ART_OF_VINES.get().asItem());
				tabData.accept(TanshugetreesModBlocks.BAOBAB.get().asItem());
				tabData.accept(TanshugetreesModBlocks.BEANSTALK.get().asItem());
				tabData.accept(TanshugetreesModBlocks.BEEKEEPER.get().asItem());
				tabData.accept(TanshugetreesModBlocks.CHRISTMAS_TREE.get().asItem());
				tabData.accept(TanshugetreesModBlocks.FALCON.get().asItem());
				tabData.accept(TanshugetreesModBlocks.GIANT_PUMPKIN.get().asItem());
				tabData.accept(TanshugetreesModBlocks.HALCYON.get().asItem());
				tabData.accept(TanshugetreesModBlocks.LEGION.get().asItem());
				tabData.accept(TanshugetreesModBlocks.MALUS_DOMESTICA.get().asItem());
				tabData.accept(TanshugetreesModBlocks.OLD_WITCH.get().asItem());
				tabData.accept(TanshugetreesModBlocks.REDWOOD.get().asItem());
				tabData.accept(TanshugetreesModBlocks.RUST.get().asItem());
				tabData.accept(TanshugetreesModBlocks.SKY_ISLAND_CHAIN.get().asItem());
				tabData.accept(TanshugetreesModBlocks.SNOW_WHITE.get().asItem());
				tabData.accept(TanshugetreesModBlocks.SNOWLAND.get().asItem());
				tabData.accept(TanshugetreesModBlocks.THE_ASPIRANT.get().asItem());
				tabData.accept(TanshugetreesModBlocks.WENDY.get().asItem());
				tabData.accept(TanshugetreesModBlocks.WHITE_FAIRY.get().asItem());
				tabData.accept(TanshugetreesModBlocks.YOKAI.get().asItem());
			}).build());
}