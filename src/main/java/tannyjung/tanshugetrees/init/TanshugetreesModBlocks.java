/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package tannyjung.tanshugetrees.init;

import tannyjung.tanshugetrees.block.*;
import tannyjung.tanshugetrees.TanshugetreesMod;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;

import net.minecraft.world.level.block.Block;

public class TanshugetreesModBlocks {
	public static final DeferredRegister<Block> REGISTRY = DeferredRegister.create(ForgeRegistries.BLOCKS, TanshugetreesMod.MODID);
	public static final RegistryObject<Block> WAYPOINT_FLOWER;
	public static final RegistryObject<Block> BLOCK_PLACER_TAPROOT_OUTER;
	public static final RegistryObject<Block> BLOCK_PLACER_TAPROOT_INNER;
	public static final RegistryObject<Block> BLOCK_PLACER_TAPROOT_CORE;
	public static final RegistryObject<Block> BLOCK_PLACER_SECONDARY_ROOT_CORE;
	public static final RegistryObject<Block> BLOCK_PLACER_SECONDARY_ROOT_INNER;
	public static final RegistryObject<Block> BLOCK_PLACER_SECONDARY_ROOT_OUTER;
	public static final RegistryObject<Block> BLOCK_PLACER_TERTIARY_ROOT_CORE;
	public static final RegistryObject<Block> BLOCK_PLACER_TERTIARY_ROOT_INNER;
	public static final RegistryObject<Block> BLOCK_PLACER_TERTIARY_ROOT_OUTER;
	public static final RegistryObject<Block> BLOCK_PLACER_FINE_ROOT_CORE;
	public static final RegistryObject<Block> BLOCK_PLACER_FINE_ROOT_INNER;
	public static final RegistryObject<Block> BLOCK_PLACER_FINE_ROOT_OUTER;
	public static final RegistryObject<Block> BLOCK_PLACER_TRUNK_CORE;
	public static final RegistryObject<Block> BLOCK_PLACER_TRUNK_INNER;
	public static final RegistryObject<Block> BLOCK_PLACER_TRUNK_OUTER;
	public static final RegistryObject<Block> BLOCK_PLACER_BRANCH_CORE;
	public static final RegistryObject<Block> BLOCK_PLACER_BRANCH_INNER;
	public static final RegistryObject<Block> BLOCK_PLACER_BRANCH_OUTER;
	public static final RegistryObject<Block> BLOCK_PLACER_TWIG_CORE;
	public static final RegistryObject<Block> BLOCK_PLACER_TWIG_INNER;
	public static final RegistryObject<Block> BLOCK_PLACER_TWIG_OUTER;
	public static final RegistryObject<Block> BLOCK_PLACER_LEAVES_2;
	public static final RegistryObject<Block> BLOCK_PLACER_LEAVES_1;
	public static final RegistryObject<Block> BLOCK_PLACER_SPRIG_CORE;
	public static final RegistryObject<Block> BLOCK_PLACER_SPRIG_INNER;
	public static final RegistryObject<Block> BLOCK_PLACER_SPRIG_OUTER;
	public static final RegistryObject<Block> BLOCK_PLACER_BOUGH_CORE;
	public static final RegistryObject<Block> BLOCK_PLACER_BOUGH_INNER;
	public static final RegistryObject<Block> BLOCK_PLACER_BOUGH_OUTER;
	public static final RegistryObject<Block> BLOCK_PLACER_LIMB_CORE;
	public static final RegistryObject<Block> BLOCK_PLACER_LIMB_INNER;
	public static final RegistryObject<Block> BLOCK_PLACER_LIMB_OUTER;
	public static final RegistryObject<Block> SAPLING_HALCYON;
	public static final RegistryObject<Block> SAPLING_YOKAI;
	public static final RegistryObject<Block> TREE_GENERATOR;
	public static final RegistryObject<Block> SAPLING_REDWOOD;
	public static final RegistryObject<Block> SAPLING_MALUS_DOMESTICA;
	public static final RegistryObject<Block> SAPLING_WENDY;
	public static final RegistryObject<Block> SAPLING_MANGROVE;
	public static final RegistryObject<Block> SAPLING_FALCON;
	public static final RegistryObject<Block> SAPLING_SKY_ISLAND_CHAIN;
	static {
		WAYPOINT_FLOWER = REGISTRY.register("waypoint_flower", WaypointFlowerBlock::new);
		BLOCK_PLACER_TAPROOT_OUTER = REGISTRY.register("block_placer_taproot_outer", BlockPlacerTaprootOuterBlock::new);
		BLOCK_PLACER_TAPROOT_INNER = REGISTRY.register("block_placer_taproot_inner", BlockPlacerTaprootInnerBlock::new);
		BLOCK_PLACER_TAPROOT_CORE = REGISTRY.register("block_placer_taproot_core", BlockPlacerTaprootCoreBlock::new);
		BLOCK_PLACER_SECONDARY_ROOT_CORE = REGISTRY.register("block_placer_secondary_root_core", BlockPlacerSecondaryRootCoreBlock::new);
		BLOCK_PLACER_SECONDARY_ROOT_INNER = REGISTRY.register("block_placer_secondary_root_inner", BlockPlacerSecondaryRootInnerBlock::new);
		BLOCK_PLACER_SECONDARY_ROOT_OUTER = REGISTRY.register("block_placer_secondary_root_outer", BlockPlacerSecondaryRootOuterBlock::new);
		BLOCK_PLACER_TERTIARY_ROOT_CORE = REGISTRY.register("block_placer_tertiary_root_core", BlockPlacerTertiaryRootCoreBlock::new);
		BLOCK_PLACER_TERTIARY_ROOT_INNER = REGISTRY.register("block_placer_tertiary_root_inner", BlockPlacerTertiaryRootInnerBlock::new);
		BLOCK_PLACER_TERTIARY_ROOT_OUTER = REGISTRY.register("block_placer_tertiary_root_outer", BlockPlacerTertiaryRootOuterBlock::new);
		BLOCK_PLACER_FINE_ROOT_CORE = REGISTRY.register("block_placer_fine_root_core", BlockPlacerFineRootCoreBlock::new);
		BLOCK_PLACER_FINE_ROOT_INNER = REGISTRY.register("block_placer_fine_root_inner", BlockPlacerFineRootInnerBlock::new);
		BLOCK_PLACER_FINE_ROOT_OUTER = REGISTRY.register("block_placer_fine_root_outer", BlockPlacerFineRootOuterBlock::new);
		BLOCK_PLACER_TRUNK_CORE = REGISTRY.register("block_placer_trunk_core", BlockPlacerTrunkCoreBlock::new);
		BLOCK_PLACER_TRUNK_INNER = REGISTRY.register("block_placer_trunk_inner", BlockPlacerTrunkInnerBlock::new);
		BLOCK_PLACER_TRUNK_OUTER = REGISTRY.register("block_placer_trunk_outer", BlockPlacerTrunkOuterBlock::new);
		BLOCK_PLACER_BRANCH_CORE = REGISTRY.register("block_placer_branch_core", BlockPlacerBranchCoreBlock::new);
		BLOCK_PLACER_BRANCH_INNER = REGISTRY.register("block_placer_branch_inner", BlockPlacerBranchInnerBlock::new);
		BLOCK_PLACER_BRANCH_OUTER = REGISTRY.register("block_placer_branch_outer", BlockPlacerBranchOuterBlock::new);
		BLOCK_PLACER_TWIG_CORE = REGISTRY.register("block_placer_twig_core", BlockPlacerTwigCoreBlock::new);
		BLOCK_PLACER_TWIG_INNER = REGISTRY.register("block_placer_twig_inner", BlockPlacerTwigInnerBlock::new);
		BLOCK_PLACER_TWIG_OUTER = REGISTRY.register("block_placer_twig_outer", BlockPlacerTwigOuterBlock::new);
		BLOCK_PLACER_LEAVES_2 = REGISTRY.register("block_placer_leaves_2", BlockPlacerLeaves2Block::new);
		BLOCK_PLACER_LEAVES_1 = REGISTRY.register("block_placer_leaves_1", BlockPlacerLeaves1Block::new);
		BLOCK_PLACER_SPRIG_CORE = REGISTRY.register("block_placer_sprig_core", BlockPlacerSprigCoreBlock::new);
		BLOCK_PLACER_SPRIG_INNER = REGISTRY.register("block_placer_sprig_inner", BlockPlacerSprigInnerBlock::new);
		BLOCK_PLACER_SPRIG_OUTER = REGISTRY.register("block_placer_sprig_outer", BlockPlacerSprigOuterBlock::new);
		BLOCK_PLACER_BOUGH_CORE = REGISTRY.register("block_placer_bough_core", BlockPlacerBoughCoreBlock::new);
		BLOCK_PLACER_BOUGH_INNER = REGISTRY.register("block_placer_bough_inner", BlockPlacerBoughInnerBlock::new);
		BLOCK_PLACER_BOUGH_OUTER = REGISTRY.register("block_placer_bough_outer", BlockPlacerBoughOuterBlock::new);
		BLOCK_PLACER_LIMB_CORE = REGISTRY.register("block_placer_limb_core", BlockPlacerLimbCoreBlock::new);
		BLOCK_PLACER_LIMB_INNER = REGISTRY.register("block_placer_limb_inner", BlockPlacerLimbInnerBlock::new);
		BLOCK_PLACER_LIMB_OUTER = REGISTRY.register("block_placer_limb_outer", BlockPlacerLimbOuterBlock::new);
		SAPLING_HALCYON = REGISTRY.register("sapling_halcyon", SaplingHalcyonBlock::new);
		SAPLING_YOKAI = REGISTRY.register("sapling_yokai", SaplingYokaiBlock::new);
		TREE_GENERATOR = REGISTRY.register("tree_generator", TreeGeneratorBlock::new);
		SAPLING_REDWOOD = REGISTRY.register("sapling_redwood", SaplingRedwoodBlock::new);
		SAPLING_MALUS_DOMESTICA = REGISTRY.register("sapling_malus_domestica", SaplingMalusDomesticaBlock::new);
		SAPLING_WENDY = REGISTRY.register("sapling_wendy", SaplingWendyBlock::new);
		SAPLING_MANGROVE = REGISTRY.register("sapling_mangrove", SaplingMangroveBlock::new);
		SAPLING_FALCON = REGISTRY.register("sapling_falcon", SaplingFalconBlock::new);
		SAPLING_SKY_ISLAND_CHAIN = REGISTRY.register("sapling_sky_island_chain", SaplingSkyIslandChainBlock::new);
	}
	// Start of user code block custom blocks
	// End of user code block custom blocks
}