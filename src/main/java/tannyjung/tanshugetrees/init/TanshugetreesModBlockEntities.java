
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package tannyjung.tanshugetrees.init;

import tannyjung.tanshugetrees.block.entity.YokaiBlockEntity;
import tannyjung.tanshugetrees.block.entity.WhiteFairyBlockEntity;
import tannyjung.tanshugetrees.block.entity.WendyBlockEntity;
import tannyjung.tanshugetrees.block.entity.TheAspirantBlockEntity;
import tannyjung.tanshugetrees.block.entity.SnowlandBlockEntity;
import tannyjung.tanshugetrees.block.entity.SnowWhiteBlockEntity;
import tannyjung.tanshugetrees.block.entity.SkyIslandChainBlockEntity;
import tannyjung.tanshugetrees.block.entity.RustBlockEntity;
import tannyjung.tanshugetrees.block.entity.RedwoodBlockEntity;
import tannyjung.tanshugetrees.block.entity.RandomTreeBlockBlockEntity;
import tannyjung.tanshugetrees.block.entity.OldWitchBlockEntity;
import tannyjung.tanshugetrees.block.entity.MalusDomesticaBlockEntity;
import tannyjung.tanshugetrees.block.entity.LegionBlockEntity;
import tannyjung.tanshugetrees.block.entity.HalcyonBlockEntity;
import tannyjung.tanshugetrees.block.entity.GiantPumpkinBlockEntity;
import tannyjung.tanshugetrees.block.entity.FalconBlockEntity;
import tannyjung.tanshugetrees.block.entity.ChristmasTreeBlockEntity;
import tannyjung.tanshugetrees.block.entity.BlockPlacerTwigOuterBlockEntity;
import tannyjung.tanshugetrees.block.entity.BlockPlacerTwigInnerBlockEntity;
import tannyjung.tanshugetrees.block.entity.BlockPlacerTwigCoreBlockEntity;
import tannyjung.tanshugetrees.block.entity.BlockPlacerTrunkOuterBlockEntity;
import tannyjung.tanshugetrees.block.entity.BlockPlacerTrunkInnerBlockEntity;
import tannyjung.tanshugetrees.block.entity.BlockPlacerTrunkCoreBlockEntity;
import tannyjung.tanshugetrees.block.entity.BlockPlacerTertiaryRootOuterBlockEntity;
import tannyjung.tanshugetrees.block.entity.BlockPlacerTertiaryRootInnerBlockEntity;
import tannyjung.tanshugetrees.block.entity.BlockPlacerTertiaryRootCoreBlockEntity;
import tannyjung.tanshugetrees.block.entity.BlockPlacerTaprootOuterBlockEntity;
import tannyjung.tanshugetrees.block.entity.BlockPlacerTaprootInnerBlockEntity;
import tannyjung.tanshugetrees.block.entity.BlockPlacerTaprootCoreBlockEntity;
import tannyjung.tanshugetrees.block.entity.BlockPlacerSecondaryRootOuterBlockEntity;
import tannyjung.tanshugetrees.block.entity.BlockPlacerSecondaryRootInnerBlockEntity;
import tannyjung.tanshugetrees.block.entity.BlockPlacerSecondaryRootCoreBlockEntity;
import tannyjung.tanshugetrees.block.entity.BlockPlacerLeavesTwigOuterBlockEntity;
import tannyjung.tanshugetrees.block.entity.BlockPlacerLeavesTwigInnerBlockEntity;
import tannyjung.tanshugetrees.block.entity.BlockPlacerLeavesTwigCoreBlockEntity;
import tannyjung.tanshugetrees.block.entity.BlockPlacerLeavesBlockEntity;
import tannyjung.tanshugetrees.block.entity.BlockPlacerLeaves2BlockEntity;
import tannyjung.tanshugetrees.block.entity.BlockPlacerFineRootOuterBlockEntity;
import tannyjung.tanshugetrees.block.entity.BlockPlacerFineRootInnerBlockEntity;
import tannyjung.tanshugetrees.block.entity.BlockPlacerFineRootCoreBlockEntity;
import tannyjung.tanshugetrees.block.entity.BlockPlacerBranchOuterBlockEntity;
import tannyjung.tanshugetrees.block.entity.BlockPlacerBranchInnerBlockEntity;
import tannyjung.tanshugetrees.block.entity.BlockPlacerBranchCoreBlockEntity;
import tannyjung.tanshugetrees.block.entity.BeekeeperBlockEntity;
import tannyjung.tanshugetrees.block.entity.BeanstalkBlockEntity;
import tannyjung.tanshugetrees.block.entity.BaobabBlockEntity;
import tannyjung.tanshugetrees.block.entity.ArtOfVinesBlockEntity;
import tannyjung.tanshugetrees.block.entity.AgathosBlockEntity;
import tannyjung.tanshugetrees.TanshugetreesMod;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.Block;

public class TanshugetreesModBlockEntities {
	public static final DeferredRegister<BlockEntityType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, TanshugetreesMod.MODID);
	public static final RegistryObject<BlockEntityType<?>> RANDOM_TREE_BLOCK = register("random_tree_block", TanshugetreesModBlocks.RANDOM_TREE_BLOCK, RandomTreeBlockBlockEntity::new);
	public static final RegistryObject<BlockEntityType<?>> YOKAI = register("yokai", TanshugetreesModBlocks.YOKAI, YokaiBlockEntity::new);
	public static final RegistryObject<BlockEntityType<?>> FALCON = register("falcon", TanshugetreesModBlocks.FALCON, FalconBlockEntity::new);
	public static final RegistryObject<BlockEntityType<?>> WENDY = register("wendy", TanshugetreesModBlocks.WENDY, WendyBlockEntity::new);
	public static final RegistryObject<BlockEntityType<?>> SNOWLAND = register("snowland", TanshugetreesModBlocks.SNOWLAND, SnowlandBlockEntity::new);
	public static final RegistryObject<BlockEntityType<?>> ART_OF_VINES = register("art_of_vines", TanshugetreesModBlocks.ART_OF_VINES, ArtOfVinesBlockEntity::new);
	public static final RegistryObject<BlockEntityType<?>> BEANSTALK = register("beanstalk", TanshugetreesModBlocks.BEANSTALK, BeanstalkBlockEntity::new);
	public static final RegistryObject<BlockEntityType<?>> BEEKEEPER = register("beekeeper", TanshugetreesModBlocks.BEEKEEPER, BeekeeperBlockEntity::new);
	public static final RegistryObject<BlockEntityType<?>> CHRISTMAS_TREE = register("christmas_tree", TanshugetreesModBlocks.CHRISTMAS_TREE, ChristmasTreeBlockEntity::new);
	public static final RegistryObject<BlockEntityType<?>> MALUS_DOMESTICA = register("malus_domestica", TanshugetreesModBlocks.MALUS_DOMESTICA, MalusDomesticaBlockEntity::new);
	public static final RegistryObject<BlockEntityType<?>> RUST = register("rust", TanshugetreesModBlocks.RUST, RustBlockEntity::new);
	public static final RegistryObject<BlockEntityType<?>> THE_ASPIRANT = register("the_aspirant", TanshugetreesModBlocks.THE_ASPIRANT, TheAspirantBlockEntity::new);
	public static final RegistryObject<BlockEntityType<?>> LEGION = register("legion", TanshugetreesModBlocks.LEGION, LegionBlockEntity::new);
	public static final RegistryObject<BlockEntityType<?>> OLD_WITCH = register("old_witch", TanshugetreesModBlocks.OLD_WITCH, OldWitchBlockEntity::new);
	public static final RegistryObject<BlockEntityType<?>> SNOW_WHITE = register("snow_white", TanshugetreesModBlocks.SNOW_WHITE, SnowWhiteBlockEntity::new);
	public static final RegistryObject<BlockEntityType<?>> SKY_ISLAND_CHAIN = register("sky_island_chain", TanshugetreesModBlocks.SKY_ISLAND_CHAIN, SkyIslandChainBlockEntity::new);
	public static final RegistryObject<BlockEntityType<?>> HALCYON = register("halcyon", TanshugetreesModBlocks.HALCYON, HalcyonBlockEntity::new);
	public static final RegistryObject<BlockEntityType<?>> WHITE_FAIRY = register("white_fairy", TanshugetreesModBlocks.WHITE_FAIRY, WhiteFairyBlockEntity::new);
	public static final RegistryObject<BlockEntityType<?>> BLOCK_PLACER_TAPROOT_OUTER = register("block_placer_taproot_outer", TanshugetreesModBlocks.BLOCK_PLACER_TAPROOT_OUTER, BlockPlacerTaprootOuterBlockEntity::new);
	public static final RegistryObject<BlockEntityType<?>> BLOCK_PLACER_TAPROOT_INNER = register("block_placer_taproot_inner", TanshugetreesModBlocks.BLOCK_PLACER_TAPROOT_INNER, BlockPlacerTaprootInnerBlockEntity::new);
	public static final RegistryObject<BlockEntityType<?>> BLOCK_PLACER_TAPROOT_CORE = register("block_placer_taproot_core", TanshugetreesModBlocks.BLOCK_PLACER_TAPROOT_CORE, BlockPlacerTaprootCoreBlockEntity::new);
	public static final RegistryObject<BlockEntityType<?>> BLOCK_PLACER_SECONDARY_ROOT_CORE = register("block_placer_secondary_root_core", TanshugetreesModBlocks.BLOCK_PLACER_SECONDARY_ROOT_CORE, BlockPlacerSecondaryRootCoreBlockEntity::new);
	public static final RegistryObject<BlockEntityType<?>> BLOCK_PLACER_SECONDARY_ROOT_INNER = register("block_placer_secondary_root_inner", TanshugetreesModBlocks.BLOCK_PLACER_SECONDARY_ROOT_INNER, BlockPlacerSecondaryRootInnerBlockEntity::new);
	public static final RegistryObject<BlockEntityType<?>> BLOCK_PLACER_SECONDARY_ROOT_OUTER = register("block_placer_secondary_root_outer", TanshugetreesModBlocks.BLOCK_PLACER_SECONDARY_ROOT_OUTER, BlockPlacerSecondaryRootOuterBlockEntity::new);
	public static final RegistryObject<BlockEntityType<?>> BLOCK_PLACER_TERTIARY_ROOT_CORE = register("block_placer_tertiary_root_core", TanshugetreesModBlocks.BLOCK_PLACER_TERTIARY_ROOT_CORE, BlockPlacerTertiaryRootCoreBlockEntity::new);
	public static final RegistryObject<BlockEntityType<?>> BLOCK_PLACER_TERTIARY_ROOT_INNER = register("block_placer_tertiary_root_inner", TanshugetreesModBlocks.BLOCK_PLACER_TERTIARY_ROOT_INNER, BlockPlacerTertiaryRootInnerBlockEntity::new);
	public static final RegistryObject<BlockEntityType<?>> BLOCK_PLACER_TERTIARY_ROOT_OUTER = register("block_placer_tertiary_root_outer", TanshugetreesModBlocks.BLOCK_PLACER_TERTIARY_ROOT_OUTER, BlockPlacerTertiaryRootOuterBlockEntity::new);
	public static final RegistryObject<BlockEntityType<?>> BLOCK_PLACER_FINE_ROOT_CORE = register("block_placer_fine_root_core", TanshugetreesModBlocks.BLOCK_PLACER_FINE_ROOT_CORE, BlockPlacerFineRootCoreBlockEntity::new);
	public static final RegistryObject<BlockEntityType<?>> BLOCK_PLACER_FINE_ROOT_INNER = register("block_placer_fine_root_inner", TanshugetreesModBlocks.BLOCK_PLACER_FINE_ROOT_INNER, BlockPlacerFineRootInnerBlockEntity::new);
	public static final RegistryObject<BlockEntityType<?>> BLOCK_PLACER_FINE_ROOT_OUTER = register("block_placer_fine_root_outer", TanshugetreesModBlocks.BLOCK_PLACER_FINE_ROOT_OUTER, BlockPlacerFineRootOuterBlockEntity::new);
	public static final RegistryObject<BlockEntityType<?>> BLOCK_PLACER_TRUNK_CORE = register("block_placer_trunk_core", TanshugetreesModBlocks.BLOCK_PLACER_TRUNK_CORE, BlockPlacerTrunkCoreBlockEntity::new);
	public static final RegistryObject<BlockEntityType<?>> BLOCK_PLACER_TRUNK_INNER = register("block_placer_trunk_inner", TanshugetreesModBlocks.BLOCK_PLACER_TRUNK_INNER, BlockPlacerTrunkInnerBlockEntity::new);
	public static final RegistryObject<BlockEntityType<?>> BLOCK_PLACER_TRUNK_OUTER = register("block_placer_trunk_outer", TanshugetreesModBlocks.BLOCK_PLACER_TRUNK_OUTER, BlockPlacerTrunkOuterBlockEntity::new);
	public static final RegistryObject<BlockEntityType<?>> BLOCK_PLACER_BRANCH_CORE = register("block_placer_branch_core", TanshugetreesModBlocks.BLOCK_PLACER_BRANCH_CORE, BlockPlacerBranchCoreBlockEntity::new);
	public static final RegistryObject<BlockEntityType<?>> BLOCK_PLACER_BRANCH_INNER = register("block_placer_branch_inner", TanshugetreesModBlocks.BLOCK_PLACER_BRANCH_INNER, BlockPlacerBranchInnerBlockEntity::new);
	public static final RegistryObject<BlockEntityType<?>> BLOCK_PLACER_BRANCH_OUTER = register("block_placer_branch_outer", TanshugetreesModBlocks.BLOCK_PLACER_BRANCH_OUTER, BlockPlacerBranchOuterBlockEntity::new);
	public static final RegistryObject<BlockEntityType<?>> BLOCK_PLACER_TWIG_CORE = register("block_placer_twig_core", TanshugetreesModBlocks.BLOCK_PLACER_TWIG_CORE, BlockPlacerTwigCoreBlockEntity::new);
	public static final RegistryObject<BlockEntityType<?>> BLOCK_PLACER_TWIG_INNER = register("block_placer_twig_inner", TanshugetreesModBlocks.BLOCK_PLACER_TWIG_INNER, BlockPlacerTwigInnerBlockEntity::new);
	public static final RegistryObject<BlockEntityType<?>> BLOCK_PLACER_TWIG_OUTER = register("block_placer_twig_outer", TanshugetreesModBlocks.BLOCK_PLACER_TWIG_OUTER, BlockPlacerTwigOuterBlockEntity::new);
	public static final RegistryObject<BlockEntityType<?>> BLOCK_PLACER_LEAVES = register("block_placer_leaves", TanshugetreesModBlocks.BLOCK_PLACER_LEAVES, BlockPlacerLeavesBlockEntity::new);
	public static final RegistryObject<BlockEntityType<?>> BLOCK_PLACER_LEAVES_2 = register("block_placer_leaves_2", TanshugetreesModBlocks.BLOCK_PLACER_LEAVES_2, BlockPlacerLeaves2BlockEntity::new);
	public static final RegistryObject<BlockEntityType<?>> BLOCK_PLACER_LEAVES_TWIG_CORE = register("block_placer_leaves_twig_core", TanshugetreesModBlocks.BLOCK_PLACER_LEAVES_TWIG_CORE, BlockPlacerLeavesTwigCoreBlockEntity::new);
	public static final RegistryObject<BlockEntityType<?>> BLOCK_PLACER_LEAVES_TWIG_INNER = register("block_placer_leaves_twig_inner", TanshugetreesModBlocks.BLOCK_PLACER_LEAVES_TWIG_INNER, BlockPlacerLeavesTwigInnerBlockEntity::new);
	public static final RegistryObject<BlockEntityType<?>> BLOCK_PLACER_LEAVES_TWIG_OUTER = register("block_placer_leaves_twig_outer", TanshugetreesModBlocks.BLOCK_PLACER_LEAVES_TWIG_OUTER, BlockPlacerLeavesTwigOuterBlockEntity::new);
	public static final RegistryObject<BlockEntityType<?>> AGATHOS = register("agathos", TanshugetreesModBlocks.AGATHOS, AgathosBlockEntity::new);
	public static final RegistryObject<BlockEntityType<?>> REDWOOD = register("redwood", TanshugetreesModBlocks.REDWOOD, RedwoodBlockEntity::new);
	public static final RegistryObject<BlockEntityType<?>> BAOBAB = register("baobab", TanshugetreesModBlocks.BAOBAB, BaobabBlockEntity::new);
	public static final RegistryObject<BlockEntityType<?>> GIANT_PUMPKIN = register("giant_pumpkin", TanshugetreesModBlocks.GIANT_PUMPKIN, GiantPumpkinBlockEntity::new);

	// Start of user code block custom block entities
	// End of user code block custom block entities
	private static RegistryObject<BlockEntityType<?>> register(String registryname, RegistryObject<Block> block, BlockEntityType.BlockEntitySupplier<?> supplier) {
		return REGISTRY.register(registryname, () -> BlockEntityType.Builder.of(supplier, block.get()).build(null));
	}
}
