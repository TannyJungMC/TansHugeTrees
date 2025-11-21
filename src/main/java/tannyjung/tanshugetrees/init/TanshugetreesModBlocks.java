/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package tannyjung.tanshugetrees.init;

import tannyjung.tanshugetrees.block.YokaiBlock;
import tannyjung.tanshugetrees.block.WaypointFlowerBlock;
import tannyjung.tanshugetrees.block.RandomTreeBlock;
import tannyjung.tanshugetrees.block.HalcyonBlock;
import tannyjung.tanshugetrees.block.BlockPlacerTwigOuterBlock;
import tannyjung.tanshugetrees.block.BlockPlacerTwigInnerBlock;
import tannyjung.tanshugetrees.block.BlockPlacerTwigCoreBlock;
import tannyjung.tanshugetrees.block.BlockPlacerTrunkOuterBlock;
import tannyjung.tanshugetrees.block.BlockPlacerTrunkInnerBlock;
import tannyjung.tanshugetrees.block.BlockPlacerTrunkCoreBlock;
import tannyjung.tanshugetrees.block.BlockPlacerTertiaryRootOuterBlock;
import tannyjung.tanshugetrees.block.BlockPlacerTertiaryRootInnerBlock;
import tannyjung.tanshugetrees.block.BlockPlacerTertiaryRootCoreBlock;
import tannyjung.tanshugetrees.block.BlockPlacerTaprootOuterBlock;
import tannyjung.tanshugetrees.block.BlockPlacerTaprootInnerBlock;
import tannyjung.tanshugetrees.block.BlockPlacerTaprootCoreBlock;
import tannyjung.tanshugetrees.block.BlockPlacerSprigOuterBlock;
import tannyjung.tanshugetrees.block.BlockPlacerSprigInnerBlock;
import tannyjung.tanshugetrees.block.BlockPlacerSprigCoreBlock;
import tannyjung.tanshugetrees.block.BlockPlacerSecondaryRootOuterBlock;
import tannyjung.tanshugetrees.block.BlockPlacerSecondaryRootInnerBlock;
import tannyjung.tanshugetrees.block.BlockPlacerSecondaryRootCoreBlock;
import tannyjung.tanshugetrees.block.BlockPlacerLimbOuterBlock;
import tannyjung.tanshugetrees.block.BlockPlacerLimbInnerBlock;
import tannyjung.tanshugetrees.block.BlockPlacerLimbCoreBlock;
import tannyjung.tanshugetrees.block.BlockPlacerLeaves2Block;
import tannyjung.tanshugetrees.block.BlockPlacerLeaves1Block;
import tannyjung.tanshugetrees.block.BlockPlacerFineRootOuterBlock;
import tannyjung.tanshugetrees.block.BlockPlacerFineRootInnerBlock;
import tannyjung.tanshugetrees.block.BlockPlacerFineRootCoreBlock;
import tannyjung.tanshugetrees.block.BlockPlacerBranchOuterBlock;
import tannyjung.tanshugetrees.block.BlockPlacerBranchInnerBlock;
import tannyjung.tanshugetrees.block.BlockPlacerBranchCoreBlock;
import tannyjung.tanshugetrees.block.BlockPlacerBoughOuterBlock;
import tannyjung.tanshugetrees.block.BlockPlacerBoughInnerBlock;
import tannyjung.tanshugetrees.block.BlockPlacerBoughCoreBlock;
import tannyjung.tanshugetrees.TanshugetreesMod;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;

import net.minecraft.world.level.block.Block;

public class TanshugetreesModBlocks {
	public static final DeferredRegister<Block> REGISTRY = DeferredRegister.create(ForgeRegistries.BLOCKS, TanshugetreesMod.MODID);
	public static final RegistryObject<Block> WAYPOINT_FLOWER = REGISTRY.register("waypoint_flower", () -> new WaypointFlowerBlock());
	public static final RegistryObject<Block> YOKAI = REGISTRY.register("yokai", () -> new YokaiBlock());
	public static final RegistryObject<Block> BLOCK_PLACER_TAPROOT_OUTER = REGISTRY.register("block_placer_taproot_outer", () -> new BlockPlacerTaprootOuterBlock());
	public static final RegistryObject<Block> BLOCK_PLACER_TAPROOT_INNER = REGISTRY.register("block_placer_taproot_inner", () -> new BlockPlacerTaprootInnerBlock());
	public static final RegistryObject<Block> BLOCK_PLACER_TAPROOT_CORE = REGISTRY.register("block_placer_taproot_core", () -> new BlockPlacerTaprootCoreBlock());
	public static final RegistryObject<Block> BLOCK_PLACER_SECONDARY_ROOT_CORE = REGISTRY.register("block_placer_secondary_root_core", () -> new BlockPlacerSecondaryRootCoreBlock());
	public static final RegistryObject<Block> BLOCK_PLACER_SECONDARY_ROOT_INNER = REGISTRY.register("block_placer_secondary_root_inner", () -> new BlockPlacerSecondaryRootInnerBlock());
	public static final RegistryObject<Block> BLOCK_PLACER_SECONDARY_ROOT_OUTER = REGISTRY.register("block_placer_secondary_root_outer", () -> new BlockPlacerSecondaryRootOuterBlock());
	public static final RegistryObject<Block> BLOCK_PLACER_TERTIARY_ROOT_CORE = REGISTRY.register("block_placer_tertiary_root_core", () -> new BlockPlacerTertiaryRootCoreBlock());
	public static final RegistryObject<Block> BLOCK_PLACER_TERTIARY_ROOT_INNER = REGISTRY.register("block_placer_tertiary_root_inner", () -> new BlockPlacerTertiaryRootInnerBlock());
	public static final RegistryObject<Block> BLOCK_PLACER_TERTIARY_ROOT_OUTER = REGISTRY.register("block_placer_tertiary_root_outer", () -> new BlockPlacerTertiaryRootOuterBlock());
	public static final RegistryObject<Block> BLOCK_PLACER_FINE_ROOT_CORE = REGISTRY.register("block_placer_fine_root_core", () -> new BlockPlacerFineRootCoreBlock());
	public static final RegistryObject<Block> BLOCK_PLACER_FINE_ROOT_INNER = REGISTRY.register("block_placer_fine_root_inner", () -> new BlockPlacerFineRootInnerBlock());
	public static final RegistryObject<Block> BLOCK_PLACER_FINE_ROOT_OUTER = REGISTRY.register("block_placer_fine_root_outer", () -> new BlockPlacerFineRootOuterBlock());
	public static final RegistryObject<Block> BLOCK_PLACER_TRUNK_CORE = REGISTRY.register("block_placer_trunk_core", () -> new BlockPlacerTrunkCoreBlock());
	public static final RegistryObject<Block> BLOCK_PLACER_TRUNK_INNER = REGISTRY.register("block_placer_trunk_inner", () -> new BlockPlacerTrunkInnerBlock());
	public static final RegistryObject<Block> BLOCK_PLACER_TRUNK_OUTER = REGISTRY.register("block_placer_trunk_outer", () -> new BlockPlacerTrunkOuterBlock());
	public static final RegistryObject<Block> BLOCK_PLACER_BRANCH_CORE = REGISTRY.register("block_placer_branch_core", () -> new BlockPlacerBranchCoreBlock());
	public static final RegistryObject<Block> BLOCK_PLACER_BRANCH_INNER = REGISTRY.register("block_placer_branch_inner", () -> new BlockPlacerBranchInnerBlock());
	public static final RegistryObject<Block> BLOCK_PLACER_BRANCH_OUTER = REGISTRY.register("block_placer_branch_outer", () -> new BlockPlacerBranchOuterBlock());
	public static final RegistryObject<Block> BLOCK_PLACER_TWIG_CORE = REGISTRY.register("block_placer_twig_core", () -> new BlockPlacerTwigCoreBlock());
	public static final RegistryObject<Block> BLOCK_PLACER_TWIG_INNER = REGISTRY.register("block_placer_twig_inner", () -> new BlockPlacerTwigInnerBlock());
	public static final RegistryObject<Block> BLOCK_PLACER_TWIG_OUTER = REGISTRY.register("block_placer_twig_outer", () -> new BlockPlacerTwigOuterBlock());
	public static final RegistryObject<Block> BLOCK_PLACER_LEAVES_2 = REGISTRY.register("block_placer_leaves_2", () -> new BlockPlacerLeaves2Block());
	public static final RegistryObject<Block> BLOCK_PLACER_LEAVES_1 = REGISTRY.register("block_placer_leaves_1", () -> new BlockPlacerLeaves1Block());
	public static final RegistryObject<Block> BLOCK_PLACER_SPRIG_CORE = REGISTRY.register("block_placer_sprig_core", () -> new BlockPlacerSprigCoreBlock());
	public static final RegistryObject<Block> BLOCK_PLACER_SPRIG_INNER = REGISTRY.register("block_placer_sprig_inner", () -> new BlockPlacerSprigInnerBlock());
	public static final RegistryObject<Block> BLOCK_PLACER_SPRIG_OUTER = REGISTRY.register("block_placer_sprig_outer", () -> new BlockPlacerSprigOuterBlock());
	public static final RegistryObject<Block> BLOCK_PLACER_BOUGH_CORE = REGISTRY.register("block_placer_bough_core", () -> new BlockPlacerBoughCoreBlock());
	public static final RegistryObject<Block> BLOCK_PLACER_BOUGH_INNER = REGISTRY.register("block_placer_bough_inner", () -> new BlockPlacerBoughInnerBlock());
	public static final RegistryObject<Block> BLOCK_PLACER_BOUGH_OUTER = REGISTRY.register("block_placer_bough_outer", () -> new BlockPlacerBoughOuterBlock());
	public static final RegistryObject<Block> BLOCK_PLACER_LIMB_CORE = REGISTRY.register("block_placer_limb_core", () -> new BlockPlacerLimbCoreBlock());
	public static final RegistryObject<Block> BLOCK_PLACER_LIMB_INNER = REGISTRY.register("block_placer_limb_inner", () -> new BlockPlacerLimbInnerBlock());
	public static final RegistryObject<Block> BLOCK_PLACER_LIMB_OUTER = REGISTRY.register("block_placer_limb_outer", () -> new BlockPlacerLimbOuterBlock());
	public static final RegistryObject<Block> RANDOM_TREE = REGISTRY.register("random_tree", () -> new RandomTreeBlock());
	public static final RegistryObject<Block> HALCYON = REGISTRY.register("halcyon", () -> new HalcyonBlock());
	// Start of user code block custom blocks
	// End of user code block custom blocks
}