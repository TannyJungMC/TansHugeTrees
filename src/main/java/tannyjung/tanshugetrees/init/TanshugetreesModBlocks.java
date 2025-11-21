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
	public static final RegistryObject<Block> YOKAI;
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
	public static final RegistryObject<Block> RANDOM_TREE;
	public static final RegistryObject<Block> HALCYON;
	static {
		WAYPOINT_FLOWER = REGISTRY.register("waypoint_flower", WaypointFlowerBlock::new);
		YOKAI = REGISTRY.register("yokai", YokaiBlock::new);
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
		RANDOM_TREE = REGISTRY.register("random_tree", RandomTreeBlock::new);
		HALCYON = REGISTRY.register("halcyon", HalcyonBlock::new);
	}
	// Start of user code block custom blocks
	// End of user code block custom blocks
}