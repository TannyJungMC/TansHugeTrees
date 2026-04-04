/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package tannyjung.tanshugetrees.init;

import tannyjung.tanshugetrees.block.entity.*;
import tannyjung.tanshugetrees.TanshugetreesMod;

import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.Block;

public class TanshugetreesModBlockEntities {
	public static final DeferredRegister<BlockEntityType<?>> REGISTRY = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, TanshugetreesMod.MODID);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockPlacerTaprootOuterBlockEntity>> BLOCK_PLACER_TAPROOT_OUTER = register("block_placer_taproot_outer", TanshugetreesModBlocks.BLOCK_PLACER_TAPROOT_OUTER,
			BlockPlacerTaprootOuterBlockEntity::new);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockPlacerTaprootInnerBlockEntity>> BLOCK_PLACER_TAPROOT_INNER = register("block_placer_taproot_inner", TanshugetreesModBlocks.BLOCK_PLACER_TAPROOT_INNER,
			BlockPlacerTaprootInnerBlockEntity::new);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockPlacerTaprootCoreBlockEntity>> BLOCK_PLACER_TAPROOT_CORE = register("block_placer_taproot_core", TanshugetreesModBlocks.BLOCK_PLACER_TAPROOT_CORE, BlockPlacerTaprootCoreBlockEntity::new);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockPlacerSecondaryRootCoreBlockEntity>> BLOCK_PLACER_SECONDARY_ROOT_CORE = register("block_placer_secondary_root_core", TanshugetreesModBlocks.BLOCK_PLACER_SECONDARY_ROOT_CORE,
			BlockPlacerSecondaryRootCoreBlockEntity::new);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockPlacerSecondaryRootInnerBlockEntity>> BLOCK_PLACER_SECONDARY_ROOT_INNER = register("block_placer_secondary_root_inner", TanshugetreesModBlocks.BLOCK_PLACER_SECONDARY_ROOT_INNER,
			BlockPlacerSecondaryRootInnerBlockEntity::new);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockPlacerSecondaryRootOuterBlockEntity>> BLOCK_PLACER_SECONDARY_ROOT_OUTER = register("block_placer_secondary_root_outer", TanshugetreesModBlocks.BLOCK_PLACER_SECONDARY_ROOT_OUTER,
			BlockPlacerSecondaryRootOuterBlockEntity::new);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockPlacerTertiaryRootCoreBlockEntity>> BLOCK_PLACER_TERTIARY_ROOT_CORE = register("block_placer_tertiary_root_core", TanshugetreesModBlocks.BLOCK_PLACER_TERTIARY_ROOT_CORE,
			BlockPlacerTertiaryRootCoreBlockEntity::new);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockPlacerTertiaryRootInnerBlockEntity>> BLOCK_PLACER_TERTIARY_ROOT_INNER = register("block_placer_tertiary_root_inner", TanshugetreesModBlocks.BLOCK_PLACER_TERTIARY_ROOT_INNER,
			BlockPlacerTertiaryRootInnerBlockEntity::new);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockPlacerTertiaryRootOuterBlockEntity>> BLOCK_PLACER_TERTIARY_ROOT_OUTER = register("block_placer_tertiary_root_outer", TanshugetreesModBlocks.BLOCK_PLACER_TERTIARY_ROOT_OUTER,
			BlockPlacerTertiaryRootOuterBlockEntity::new);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockPlacerFineRootCoreBlockEntity>> BLOCK_PLACER_FINE_ROOT_CORE = register("block_placer_fine_root_core", TanshugetreesModBlocks.BLOCK_PLACER_FINE_ROOT_CORE,
			BlockPlacerFineRootCoreBlockEntity::new);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockPlacerFineRootInnerBlockEntity>> BLOCK_PLACER_FINE_ROOT_INNER = register("block_placer_fine_root_inner", TanshugetreesModBlocks.BLOCK_PLACER_FINE_ROOT_INNER,
			BlockPlacerFineRootInnerBlockEntity::new);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockPlacerFineRootOuterBlockEntity>> BLOCK_PLACER_FINE_ROOT_OUTER = register("block_placer_fine_root_outer", TanshugetreesModBlocks.BLOCK_PLACER_FINE_ROOT_OUTER,
			BlockPlacerFineRootOuterBlockEntity::new);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockPlacerTrunkCoreBlockEntity>> BLOCK_PLACER_TRUNK_CORE = register("block_placer_trunk_core", TanshugetreesModBlocks.BLOCK_PLACER_TRUNK_CORE, BlockPlacerTrunkCoreBlockEntity::new);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockPlacerTrunkInnerBlockEntity>> BLOCK_PLACER_TRUNK_INNER = register("block_placer_trunk_inner", TanshugetreesModBlocks.BLOCK_PLACER_TRUNK_INNER, BlockPlacerTrunkInnerBlockEntity::new);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockPlacerTrunkOuterBlockEntity>> BLOCK_PLACER_TRUNK_OUTER = register("block_placer_trunk_outer", TanshugetreesModBlocks.BLOCK_PLACER_TRUNK_OUTER, BlockPlacerTrunkOuterBlockEntity::new);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockPlacerBranchCoreBlockEntity>> BLOCK_PLACER_BRANCH_CORE = register("block_placer_branch_core", TanshugetreesModBlocks.BLOCK_PLACER_BRANCH_CORE, BlockPlacerBranchCoreBlockEntity::new);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockPlacerBranchInnerBlockEntity>> BLOCK_PLACER_BRANCH_INNER = register("block_placer_branch_inner", TanshugetreesModBlocks.BLOCK_PLACER_BRANCH_INNER, BlockPlacerBranchInnerBlockEntity::new);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockPlacerBranchOuterBlockEntity>> BLOCK_PLACER_BRANCH_OUTER = register("block_placer_branch_outer", TanshugetreesModBlocks.BLOCK_PLACER_BRANCH_OUTER, BlockPlacerBranchOuterBlockEntity::new);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockPlacerTwigCoreBlockEntity>> BLOCK_PLACER_TWIG_CORE = register("block_placer_twig_core", TanshugetreesModBlocks.BLOCK_PLACER_TWIG_CORE, BlockPlacerTwigCoreBlockEntity::new);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockPlacerTwigInnerBlockEntity>> BLOCK_PLACER_TWIG_INNER = register("block_placer_twig_inner", TanshugetreesModBlocks.BLOCK_PLACER_TWIG_INNER, BlockPlacerTwigInnerBlockEntity::new);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockPlacerTwigOuterBlockEntity>> BLOCK_PLACER_TWIG_OUTER = register("block_placer_twig_outer", TanshugetreesModBlocks.BLOCK_PLACER_TWIG_OUTER, BlockPlacerTwigOuterBlockEntity::new);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockPlacerLeaves2BlockEntity>> BLOCK_PLACER_LEAVES_2 = register("block_placer_leaves_2", TanshugetreesModBlocks.BLOCK_PLACER_LEAVES_2, BlockPlacerLeaves2BlockEntity::new);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockPlacerLeaves1BlockEntity>> BLOCK_PLACER_LEAVES_1 = register("block_placer_leaves_1", TanshugetreesModBlocks.BLOCK_PLACER_LEAVES_1, BlockPlacerLeaves1BlockEntity::new);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockPlacerSprigCoreBlockEntity>> BLOCK_PLACER_SPRIG_CORE = register("block_placer_sprig_core", TanshugetreesModBlocks.BLOCK_PLACER_SPRIG_CORE, BlockPlacerSprigCoreBlockEntity::new);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockPlacerSprigInnerBlockEntity>> BLOCK_PLACER_SPRIG_INNER = register("block_placer_sprig_inner", TanshugetreesModBlocks.BLOCK_PLACER_SPRIG_INNER, BlockPlacerSprigInnerBlockEntity::new);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockPlacerSprigOuterBlockEntity>> BLOCK_PLACER_SPRIG_OUTER = register("block_placer_sprig_outer", TanshugetreesModBlocks.BLOCK_PLACER_SPRIG_OUTER, BlockPlacerSprigOuterBlockEntity::new);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockPlacerBoughCoreBlockEntity>> BLOCK_PLACER_BOUGH_CORE = register("block_placer_bough_core", TanshugetreesModBlocks.BLOCK_PLACER_BOUGH_CORE, BlockPlacerBoughCoreBlockEntity::new);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockPlacerBoughInnerBlockEntity>> BLOCK_PLACER_BOUGH_INNER = register("block_placer_bough_inner", TanshugetreesModBlocks.BLOCK_PLACER_BOUGH_INNER, BlockPlacerBoughInnerBlockEntity::new);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockPlacerBoughOuterBlockEntity>> BLOCK_PLACER_BOUGH_OUTER = register("block_placer_bough_outer", TanshugetreesModBlocks.BLOCK_PLACER_BOUGH_OUTER, BlockPlacerBoughOuterBlockEntity::new);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockPlacerLimbCoreBlockEntity>> BLOCK_PLACER_LIMB_CORE = register("block_placer_limb_core", TanshugetreesModBlocks.BLOCK_PLACER_LIMB_CORE, BlockPlacerLimbCoreBlockEntity::new);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockPlacerLimbInnerBlockEntity>> BLOCK_PLACER_LIMB_INNER = register("block_placer_limb_inner", TanshugetreesModBlocks.BLOCK_PLACER_LIMB_INNER, BlockPlacerLimbInnerBlockEntity::new);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockPlacerLimbOuterBlockEntity>> BLOCK_PLACER_LIMB_OUTER = register("block_placer_limb_outer", TanshugetreesModBlocks.BLOCK_PLACER_LIMB_OUTER, BlockPlacerLimbOuterBlockEntity::new);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SaplingHalcyonBlockEntity>> SAPLING_HALCYON = register("sapling_halcyon", TanshugetreesModBlocks.SAPLING_HALCYON, SaplingHalcyonBlockEntity::new);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SaplingYokaiBlockEntity>> SAPLING_YOKAI = register("sapling_yokai", TanshugetreesModBlocks.SAPLING_YOKAI, SaplingYokaiBlockEntity::new);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TreeGeneratorBlockEntity>> TREE_GENERATOR = register("tree_generator", TanshugetreesModBlocks.TREE_GENERATOR, TreeGeneratorBlockEntity::new);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SaplingRedwoodBlockEntity>> SAPLING_REDWOOD = register("sapling_redwood", TanshugetreesModBlocks.SAPLING_REDWOOD, SaplingRedwoodBlockEntity::new);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SaplingMalusDomesticaBlockEntity>> SAPLING_MALUS_DOMESTICA = register("sapling_malus_domestica", TanshugetreesModBlocks.SAPLING_MALUS_DOMESTICA, SaplingMalusDomesticaBlockEntity::new);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SaplingWendyBlockEntity>> SAPLING_WENDY = register("sapling_wendy", TanshugetreesModBlocks.SAPLING_WENDY, SaplingWendyBlockEntity::new);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SaplingMangroveBlockEntity>> SAPLING_MANGROVE = register("sapling_mangrove", TanshugetreesModBlocks.SAPLING_MANGROVE, SaplingMangroveBlockEntity::new);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SaplingFalconBlockEntity>> SAPLING_FALCON = register("sapling_falcon", TanshugetreesModBlocks.SAPLING_FALCON, SaplingFalconBlockEntity::new);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SaplingSkyIslandChainBlockEntity>> SAPLING_SKY_ISLAND_CHAIN = register("sapling_sky_island_chain", TanshugetreesModBlocks.SAPLING_SKY_ISLAND_CHAIN, SaplingSkyIslandChainBlockEntity::new);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SaplingChristmasTreeBlockEntity>> SAPLING_CHRISTMAS_TREE = register("sapling_christmas_tree", TanshugetreesModBlocks.SAPLING_CHRISTMAS_TREE, SaplingChristmasTreeBlockEntity::new);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SaplingBaobabBlockEntity>> SAPLING_BAOBAB = register("sapling_baobab", TanshugetreesModBlocks.SAPLING_BAOBAB, SaplingBaobabBlockEntity::new);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SaplingLegionBlockEntity>> SAPLING_LEGION = register("sapling_legion", TanshugetreesModBlocks.SAPLING_LEGION, SaplingLegionBlockEntity::new);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SaplingOldWitchBlockEntity>> SAPLING_OLD_WITCH = register("sapling_old_witch", TanshugetreesModBlocks.SAPLING_OLD_WITCH, SaplingOldWitchBlockEntity::new);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SaplingGiantPumpkinBlockEntity>> SAPLING_GIANT_PUMPKIN = register("sapling_giant_pumpkin", TanshugetreesModBlocks.SAPLING_GIANT_PUMPKIN, SaplingGiantPumpkinBlockEntity::new);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SaplingRustBlockEntity>> SAPLING_RUST = register("sapling_rust", TanshugetreesModBlocks.SAPLING_RUST, SaplingRustBlockEntity::new);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SaplingTheAspirantBlockEntity>> SAPLING_THE_ASPIRANT = register("sapling_the_aspirant", TanshugetreesModBlocks.SAPLING_THE_ASPIRANT, SaplingTheAspirantBlockEntity::new);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SaplingWhiteFairyBlockEntity>> SAPLING_WHITE_FAIRY = register("sapling_white_fairy", TanshugetreesModBlocks.SAPLING_WHITE_FAIRY, SaplingWhiteFairyBlockEntity::new);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SaplingAgathosBlockEntity>> SAPLING_AGATHOS = register("sapling_agathos", TanshugetreesModBlocks.SAPLING_AGATHOS, SaplingAgathosBlockEntity::new);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SaplingPalmBlockEntity>> SAPLING_PALM = register("sapling_palm", TanshugetreesModBlocks.SAPLING_PALM, SaplingPalmBlockEntity::new);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SaplingCoconutTreeBlockEntity>> SAPLING_COCONUT_TREE = register("sapling_coconut_tree", TanshugetreesModBlocks.SAPLING_COCONUT_TREE, SaplingCoconutTreeBlockEntity::new);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SaplingBeanStalkBlockEntity>> SAPLING_BEAN_STALK = register("sapling_bean_stalk", TanshugetreesModBlocks.SAPLING_BEAN_STALK, SaplingBeanStalkBlockEntity::new);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SaplingBeeKeeperBlockEntity>> SAPLING_BEE_KEEPER = register("sapling_bee_keeper", TanshugetreesModBlocks.SAPLING_BEE_KEEPER, SaplingBeeKeeperBlockEntity::new);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SaplingWalkingTreeBlockEntity>> SAPLING_WALKING_TREE = register("sapling_walking_tree", TanshugetreesModBlocks.SAPLING_WALKING_TREE, SaplingWalkingTreeBlockEntity::new);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SaplingCatalystBlockEntity>> SAPLING_CATALYST = register("sapling_catalyst", TanshugetreesModBlocks.SAPLING_CATALYST, SaplingCatalystBlockEntity::new);

	// Start of user code block custom block entities
	// End of user code block custom block entities
	private static <T extends BlockEntity> DeferredHolder<BlockEntityType<?>, BlockEntityType<T>> register(String registryname, DeferredHolder<Block, Block> block, BlockEntityType.BlockEntitySupplier<T> supplier) {
		return REGISTRY.register(registryname, () -> BlockEntityType.Builder.of(supplier, block.get()).build(null));
	}
}