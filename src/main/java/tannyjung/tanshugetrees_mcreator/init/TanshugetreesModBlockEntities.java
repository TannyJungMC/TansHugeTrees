/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package tannyjung.tanshugetrees_mcreator.init;

import tannyjung.tanshugetrees_mcreator.block.entity.YokaiBlockEntity;
import tannyjung.tanshugetrees_mcreator.block.entity.RandomTreeBlockEntity;
import tannyjung.tanshugetrees_mcreator.block.entity.HalcyonBlockEntity;
import tannyjung.tanshugetrees_mcreator.block.entity.BlockPlacerTwigOuterBlockEntity;
import tannyjung.tanshugetrees_mcreator.block.entity.BlockPlacerTwigInnerBlockEntity;
import tannyjung.tanshugetrees_mcreator.block.entity.BlockPlacerTwigCoreBlockEntity;
import tannyjung.tanshugetrees_mcreator.block.entity.BlockPlacerTrunkOuterBlockEntity;
import tannyjung.tanshugetrees_mcreator.block.entity.BlockPlacerTrunkInnerBlockEntity;
import tannyjung.tanshugetrees_mcreator.block.entity.BlockPlacerTrunkCoreBlockEntity;
import tannyjung.tanshugetrees_mcreator.block.entity.BlockPlacerTertiaryRootOuterBlockEntity;
import tannyjung.tanshugetrees_mcreator.block.entity.BlockPlacerTertiaryRootInnerBlockEntity;
import tannyjung.tanshugetrees_mcreator.block.entity.BlockPlacerTertiaryRootCoreBlockEntity;
import tannyjung.tanshugetrees_mcreator.block.entity.BlockPlacerTaprootOuterBlockEntity;
import tannyjung.tanshugetrees_mcreator.block.entity.BlockPlacerTaprootInnerBlockEntity;
import tannyjung.tanshugetrees_mcreator.block.entity.BlockPlacerTaprootCoreBlockEntity;
import tannyjung.tanshugetrees_mcreator.block.entity.BlockPlacerSprigOuterBlockEntity;
import tannyjung.tanshugetrees_mcreator.block.entity.BlockPlacerSprigInnerBlockEntity;
import tannyjung.tanshugetrees_mcreator.block.entity.BlockPlacerSprigCoreBlockEntity;
import tannyjung.tanshugetrees_mcreator.block.entity.BlockPlacerSecondaryRootOuterBlockEntity;
import tannyjung.tanshugetrees_mcreator.block.entity.BlockPlacerSecondaryRootInnerBlockEntity;
import tannyjung.tanshugetrees_mcreator.block.entity.BlockPlacerSecondaryRootCoreBlockEntity;
import tannyjung.tanshugetrees_mcreator.block.entity.BlockPlacerLimbOuterBlockEntity;
import tannyjung.tanshugetrees_mcreator.block.entity.BlockPlacerLimbInnerBlockEntity;
import tannyjung.tanshugetrees_mcreator.block.entity.BlockPlacerLimbCoreBlockEntity;
import tannyjung.tanshugetrees_mcreator.block.entity.BlockPlacerLeaves2BlockEntity;
import tannyjung.tanshugetrees_mcreator.block.entity.BlockPlacerLeaves1BlockEntity;
import tannyjung.tanshugetrees_mcreator.block.entity.BlockPlacerFineRootOuterBlockEntity;
import tannyjung.tanshugetrees_mcreator.block.entity.BlockPlacerFineRootInnerBlockEntity;
import tannyjung.tanshugetrees_mcreator.block.entity.BlockPlacerFineRootCoreBlockEntity;
import tannyjung.tanshugetrees_mcreator.block.entity.BlockPlacerBranchOuterBlockEntity;
import tannyjung.tanshugetrees_mcreator.block.entity.BlockPlacerBranchInnerBlockEntity;
import tannyjung.tanshugetrees_mcreator.block.entity.BlockPlacerBranchCoreBlockEntity;
import tannyjung.tanshugetrees_mcreator.block.entity.BlockPlacerBoughOuterBlockEntity;
import tannyjung.tanshugetrees_mcreator.block.entity.BlockPlacerBoughInnerBlockEntity;
import tannyjung.tanshugetrees_mcreator.block.entity.BlockPlacerBoughCoreBlockEntity;
import tannyjung.tanshugetrees_mcreator.TanshugetreesMod;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.Block;

public class TanshugetreesModBlockEntities {
	public static final DeferredRegister<BlockEntityType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, TanshugetreesMod.MODID);
	public static final RegistryObject<BlockEntityType<YokaiBlockEntity>> YOKAI = register("yokai", TanshugetreesModBlocks.YOKAI, YokaiBlockEntity::new);
	public static final RegistryObject<BlockEntityType<BlockPlacerTaprootOuterBlockEntity>> BLOCK_PLACER_TAPROOT_OUTER = register("block_placer_taproot_outer", TanshugetreesModBlocks.BLOCK_PLACER_TAPROOT_OUTER,
			BlockPlacerTaprootOuterBlockEntity::new);
	public static final RegistryObject<BlockEntityType<BlockPlacerTaprootInnerBlockEntity>> BLOCK_PLACER_TAPROOT_INNER = register("block_placer_taproot_inner", TanshugetreesModBlocks.BLOCK_PLACER_TAPROOT_INNER,
			BlockPlacerTaprootInnerBlockEntity::new);
	public static final RegistryObject<BlockEntityType<BlockPlacerTaprootCoreBlockEntity>> BLOCK_PLACER_TAPROOT_CORE = register("block_placer_taproot_core", TanshugetreesModBlocks.BLOCK_PLACER_TAPROOT_CORE, BlockPlacerTaprootCoreBlockEntity::new);
	public static final RegistryObject<BlockEntityType<BlockPlacerSecondaryRootCoreBlockEntity>> BLOCK_PLACER_SECONDARY_ROOT_CORE = register("block_placer_secondary_root_core", TanshugetreesModBlocks.BLOCK_PLACER_SECONDARY_ROOT_CORE,
			BlockPlacerSecondaryRootCoreBlockEntity::new);
	public static final RegistryObject<BlockEntityType<BlockPlacerSecondaryRootInnerBlockEntity>> BLOCK_PLACER_SECONDARY_ROOT_INNER = register("block_placer_secondary_root_inner", TanshugetreesModBlocks.BLOCK_PLACER_SECONDARY_ROOT_INNER,
			BlockPlacerSecondaryRootInnerBlockEntity::new);
	public static final RegistryObject<BlockEntityType<BlockPlacerSecondaryRootOuterBlockEntity>> BLOCK_PLACER_SECONDARY_ROOT_OUTER = register("block_placer_secondary_root_outer", TanshugetreesModBlocks.BLOCK_PLACER_SECONDARY_ROOT_OUTER,
			BlockPlacerSecondaryRootOuterBlockEntity::new);
	public static final RegistryObject<BlockEntityType<BlockPlacerTertiaryRootCoreBlockEntity>> BLOCK_PLACER_TERTIARY_ROOT_CORE = register("block_placer_tertiary_root_core", TanshugetreesModBlocks.BLOCK_PLACER_TERTIARY_ROOT_CORE,
			BlockPlacerTertiaryRootCoreBlockEntity::new);
	public static final RegistryObject<BlockEntityType<BlockPlacerTertiaryRootInnerBlockEntity>> BLOCK_PLACER_TERTIARY_ROOT_INNER = register("block_placer_tertiary_root_inner", TanshugetreesModBlocks.BLOCK_PLACER_TERTIARY_ROOT_INNER,
			BlockPlacerTertiaryRootInnerBlockEntity::new);
	public static final RegistryObject<BlockEntityType<BlockPlacerTertiaryRootOuterBlockEntity>> BLOCK_PLACER_TERTIARY_ROOT_OUTER = register("block_placer_tertiary_root_outer", TanshugetreesModBlocks.BLOCK_PLACER_TERTIARY_ROOT_OUTER,
			BlockPlacerTertiaryRootOuterBlockEntity::new);
	public static final RegistryObject<BlockEntityType<BlockPlacerFineRootCoreBlockEntity>> BLOCK_PLACER_FINE_ROOT_CORE = register("block_placer_fine_root_core", TanshugetreesModBlocks.BLOCK_PLACER_FINE_ROOT_CORE,
			BlockPlacerFineRootCoreBlockEntity::new);
	public static final RegistryObject<BlockEntityType<BlockPlacerFineRootInnerBlockEntity>> BLOCK_PLACER_FINE_ROOT_INNER = register("block_placer_fine_root_inner", TanshugetreesModBlocks.BLOCK_PLACER_FINE_ROOT_INNER,
			BlockPlacerFineRootInnerBlockEntity::new);
	public static final RegistryObject<BlockEntityType<BlockPlacerFineRootOuterBlockEntity>> BLOCK_PLACER_FINE_ROOT_OUTER = register("block_placer_fine_root_outer", TanshugetreesModBlocks.BLOCK_PLACER_FINE_ROOT_OUTER,
			BlockPlacerFineRootOuterBlockEntity::new);
	public static final RegistryObject<BlockEntityType<BlockPlacerTrunkCoreBlockEntity>> BLOCK_PLACER_TRUNK_CORE = register("block_placer_trunk_core", TanshugetreesModBlocks.BLOCK_PLACER_TRUNK_CORE, BlockPlacerTrunkCoreBlockEntity::new);
	public static final RegistryObject<BlockEntityType<BlockPlacerTrunkInnerBlockEntity>> BLOCK_PLACER_TRUNK_INNER = register("block_placer_trunk_inner", TanshugetreesModBlocks.BLOCK_PLACER_TRUNK_INNER, BlockPlacerTrunkInnerBlockEntity::new);
	public static final RegistryObject<BlockEntityType<BlockPlacerTrunkOuterBlockEntity>> BLOCK_PLACER_TRUNK_OUTER = register("block_placer_trunk_outer", TanshugetreesModBlocks.BLOCK_PLACER_TRUNK_OUTER, BlockPlacerTrunkOuterBlockEntity::new);
	public static final RegistryObject<BlockEntityType<BlockPlacerBranchCoreBlockEntity>> BLOCK_PLACER_BRANCH_CORE = register("block_placer_branch_core", TanshugetreesModBlocks.BLOCK_PLACER_BRANCH_CORE, BlockPlacerBranchCoreBlockEntity::new);
	public static final RegistryObject<BlockEntityType<BlockPlacerBranchInnerBlockEntity>> BLOCK_PLACER_BRANCH_INNER = register("block_placer_branch_inner", TanshugetreesModBlocks.BLOCK_PLACER_BRANCH_INNER, BlockPlacerBranchInnerBlockEntity::new);
	public static final RegistryObject<BlockEntityType<BlockPlacerBranchOuterBlockEntity>> BLOCK_PLACER_BRANCH_OUTER = register("block_placer_branch_outer", TanshugetreesModBlocks.BLOCK_PLACER_BRANCH_OUTER, BlockPlacerBranchOuterBlockEntity::new);
	public static final RegistryObject<BlockEntityType<BlockPlacerTwigCoreBlockEntity>> BLOCK_PLACER_TWIG_CORE = register("block_placer_twig_core", TanshugetreesModBlocks.BLOCK_PLACER_TWIG_CORE, BlockPlacerTwigCoreBlockEntity::new);
	public static final RegistryObject<BlockEntityType<BlockPlacerTwigInnerBlockEntity>> BLOCK_PLACER_TWIG_INNER = register("block_placer_twig_inner", TanshugetreesModBlocks.BLOCK_PLACER_TWIG_INNER, BlockPlacerTwigInnerBlockEntity::new);
	public static final RegistryObject<BlockEntityType<BlockPlacerTwigOuterBlockEntity>> BLOCK_PLACER_TWIG_OUTER = register("block_placer_twig_outer", TanshugetreesModBlocks.BLOCK_PLACER_TWIG_OUTER, BlockPlacerTwigOuterBlockEntity::new);
	public static final RegistryObject<BlockEntityType<BlockPlacerLeaves2BlockEntity>> BLOCK_PLACER_LEAVES_2 = register("block_placer_leaves_2", TanshugetreesModBlocks.BLOCK_PLACER_LEAVES_2, BlockPlacerLeaves2BlockEntity::new);
	public static final RegistryObject<BlockEntityType<BlockPlacerLeaves1BlockEntity>> BLOCK_PLACER_LEAVES_1 = register("block_placer_leaves_1", TanshugetreesModBlocks.BLOCK_PLACER_LEAVES_1, BlockPlacerLeaves1BlockEntity::new);
	public static final RegistryObject<BlockEntityType<BlockPlacerSprigCoreBlockEntity>> BLOCK_PLACER_SPRIG_CORE = register("block_placer_sprig_core", TanshugetreesModBlocks.BLOCK_PLACER_SPRIG_CORE, BlockPlacerSprigCoreBlockEntity::new);
	public static final RegistryObject<BlockEntityType<BlockPlacerSprigInnerBlockEntity>> BLOCK_PLACER_SPRIG_INNER = register("block_placer_sprig_inner", TanshugetreesModBlocks.BLOCK_PLACER_SPRIG_INNER, BlockPlacerSprigInnerBlockEntity::new);
	public static final RegistryObject<BlockEntityType<BlockPlacerSprigOuterBlockEntity>> BLOCK_PLACER_SPRIG_OUTER = register("block_placer_sprig_outer", TanshugetreesModBlocks.BLOCK_PLACER_SPRIG_OUTER, BlockPlacerSprigOuterBlockEntity::new);
	public static final RegistryObject<BlockEntityType<BlockPlacerBoughCoreBlockEntity>> BLOCK_PLACER_BOUGH_CORE = register("block_placer_bough_core", TanshugetreesModBlocks.BLOCK_PLACER_BOUGH_CORE, BlockPlacerBoughCoreBlockEntity::new);
	public static final RegistryObject<BlockEntityType<BlockPlacerBoughInnerBlockEntity>> BLOCK_PLACER_BOUGH_INNER = register("block_placer_bough_inner", TanshugetreesModBlocks.BLOCK_PLACER_BOUGH_INNER, BlockPlacerBoughInnerBlockEntity::new);
	public static final RegistryObject<BlockEntityType<BlockPlacerBoughOuterBlockEntity>> BLOCK_PLACER_BOUGH_OUTER = register("block_placer_bough_outer", TanshugetreesModBlocks.BLOCK_PLACER_BOUGH_OUTER, BlockPlacerBoughOuterBlockEntity::new);
	public static final RegistryObject<BlockEntityType<BlockPlacerLimbCoreBlockEntity>> BLOCK_PLACER_LIMB_CORE = register("block_placer_limb_core", TanshugetreesModBlocks.BLOCK_PLACER_LIMB_CORE, BlockPlacerLimbCoreBlockEntity::new);
	public static final RegistryObject<BlockEntityType<BlockPlacerLimbInnerBlockEntity>> BLOCK_PLACER_LIMB_INNER = register("block_placer_limb_inner", TanshugetreesModBlocks.BLOCK_PLACER_LIMB_INNER, BlockPlacerLimbInnerBlockEntity::new);
	public static final RegistryObject<BlockEntityType<BlockPlacerLimbOuterBlockEntity>> BLOCK_PLACER_LIMB_OUTER = register("block_placer_limb_outer", TanshugetreesModBlocks.BLOCK_PLACER_LIMB_OUTER, BlockPlacerLimbOuterBlockEntity::new);
	public static final RegistryObject<BlockEntityType<RandomTreeBlockEntity>> RANDOM_TREE = register("random_tree", TanshugetreesModBlocks.RANDOM_TREE, RandomTreeBlockEntity::new);
	public static final RegistryObject<BlockEntityType<HalcyonBlockEntity>> HALCYON = register("halcyon", TanshugetreesModBlocks.HALCYON, HalcyonBlockEntity::new);

	// Start of user code block custom block entities
	// End of user code block custom block entities
	private static <T extends BlockEntity> RegistryObject<BlockEntityType<T>> register(String registryname, RegistryObject<Block> block, BlockEntityType.BlockEntitySupplier<T> supplier) {
		return REGISTRY.register(registryname, () -> BlockEntityType.Builder.of(supplier, block.get()).build(null));
	}
}