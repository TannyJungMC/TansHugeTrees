
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package tannyjung.tanshugetrees.init;

import tannyjung.tanshugetrees.block.YokaiBlock;
import tannyjung.tanshugetrees.block.WhiteFairyBlock;
import tannyjung.tanshugetrees.block.WendyBlock;
import tannyjung.tanshugetrees.block.WaypointFlowerBlock;
import tannyjung.tanshugetrees.block.TheAspirantBlock;
import tannyjung.tanshugetrees.block.SnowlandBlock;
import tannyjung.tanshugetrees.block.SnowWhiteBlock;
import tannyjung.tanshugetrees.block.SkyIslandChainBlock;
import tannyjung.tanshugetrees.block.RustBlock;
import tannyjung.tanshugetrees.block.RedwoodBlock;
import tannyjung.tanshugetrees.block.RandomTreeBlockBlock;
import tannyjung.tanshugetrees.block.OldWitchBlock;
import tannyjung.tanshugetrees.block.MalusDomesticaBlock;
import tannyjung.tanshugetrees.block.LegionBlock;
import tannyjung.tanshugetrees.block.HalcyonBlock;
import tannyjung.tanshugetrees.block.GiantPumpkinBlock;
import tannyjung.tanshugetrees.block.FalconBlock;
import tannyjung.tanshugetrees.block.ChristmasTreeBlock;
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
import tannyjung.tanshugetrees.block.BlockPlacerSecondaryRootOuterBlock;
import tannyjung.tanshugetrees.block.BlockPlacerSecondaryRootInnerBlock;
import tannyjung.tanshugetrees.block.BlockPlacerSecondaryRootCoreBlock;
import tannyjung.tanshugetrees.block.BlockPlacerLeavesTwigOuterBlock;
import tannyjung.tanshugetrees.block.BlockPlacerLeavesTwigInnerBlock;
import tannyjung.tanshugetrees.block.BlockPlacerLeavesTwigCoreBlock;
import tannyjung.tanshugetrees.block.BlockPlacerLeavesBlock;
import tannyjung.tanshugetrees.block.BlockPlacerLeaves2Block;
import tannyjung.tanshugetrees.block.BlockPlacerFineRootOuterBlock;
import tannyjung.tanshugetrees.block.BlockPlacerFineRootInnerBlock;
import tannyjung.tanshugetrees.block.BlockPlacerFineRootCoreBlock;
import tannyjung.tanshugetrees.block.BlockPlacerBranchOuterBlock;
import tannyjung.tanshugetrees.block.BlockPlacerBranchInnerBlock;
import tannyjung.tanshugetrees.block.BlockPlacerBranchCoreBlock;
import tannyjung.tanshugetrees.block.BeekeeperBlock;
import tannyjung.tanshugetrees.block.BeanstalkBlock;
import tannyjung.tanshugetrees.block.BaobabBlock;
import tannyjung.tanshugetrees.block.ArtOfVinesBlock;
import tannyjung.tanshugetrees.block.AgathosBlock;
import tannyjung.tanshugetrees.TanshugetreesMod;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;

import net.minecraft.world.level.block.Block;

public class TanshugetreesModBlocks {
	public static final DeferredRegister<Block> REGISTRY = DeferredRegister.create(ForgeRegistries.BLOCKS, TanshugetreesMod.MODID);
	public static final RegistryObject<Block> RANDOM_TREE_BLOCK = REGISTRY.register("random_tree_block", () -> new RandomTreeBlockBlock());
	public static final RegistryObject<Block> WAYPOINT_FLOWER = REGISTRY.register("waypoint_flower", () -> new WaypointFlowerBlock());
	public static final RegistryObject<Block> YOKAI = REGISTRY.register("yokai", () -> new YokaiBlock());
	public static final RegistryObject<Block> FALCON = REGISTRY.register("falcon", () -> new FalconBlock());
	public static final RegistryObject<Block> WENDY = REGISTRY.register("wendy", () -> new WendyBlock());
	public static final RegistryObject<Block> SNOWLAND = REGISTRY.register("snowland", () -> new SnowlandBlock());
	public static final RegistryObject<Block> ART_OF_VINES = REGISTRY.register("art_of_vines", () -> new ArtOfVinesBlock());
	public static final RegistryObject<Block> BEANSTALK = REGISTRY.register("beanstalk", () -> new BeanstalkBlock());
	public static final RegistryObject<Block> BEEKEEPER = REGISTRY.register("beekeeper", () -> new BeekeeperBlock());
	public static final RegistryObject<Block> CHRISTMAS_TREE = REGISTRY.register("christmas_tree", () -> new ChristmasTreeBlock());
	public static final RegistryObject<Block> MALUS_DOMESTICA = REGISTRY.register("malus_domestica", () -> new MalusDomesticaBlock());
	public static final RegistryObject<Block> RUST = REGISTRY.register("rust", () -> new RustBlock());
	public static final RegistryObject<Block> THE_ASPIRANT = REGISTRY.register("the_aspirant", () -> new TheAspirantBlock());
	public static final RegistryObject<Block> LEGION = REGISTRY.register("legion", () -> new LegionBlock());
	public static final RegistryObject<Block> OLD_WITCH = REGISTRY.register("old_witch", () -> new OldWitchBlock());
	public static final RegistryObject<Block> SNOW_WHITE = REGISTRY.register("snow_white", () -> new SnowWhiteBlock());
	public static final RegistryObject<Block> SKY_ISLAND_CHAIN = REGISTRY.register("sky_island_chain", () -> new SkyIslandChainBlock());
	public static final RegistryObject<Block> HALCYON = REGISTRY.register("halcyon", () -> new HalcyonBlock());
	public static final RegistryObject<Block> WHITE_FAIRY = REGISTRY.register("white_fairy", () -> new WhiteFairyBlock());
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
	public static final RegistryObject<Block> BLOCK_PLACER_LEAVES = REGISTRY.register("block_placer_leaves", () -> new BlockPlacerLeavesBlock());
	public static final RegistryObject<Block> BLOCK_PLACER_LEAVES_2 = REGISTRY.register("block_placer_leaves_2", () -> new BlockPlacerLeaves2Block());
	public static final RegistryObject<Block> BLOCK_PLACER_LEAVES_TWIG_CORE = REGISTRY.register("block_placer_leaves_twig_core", () -> new BlockPlacerLeavesTwigCoreBlock());
	public static final RegistryObject<Block> BLOCK_PLACER_LEAVES_TWIG_INNER = REGISTRY.register("block_placer_leaves_twig_inner", () -> new BlockPlacerLeavesTwigInnerBlock());
	public static final RegistryObject<Block> BLOCK_PLACER_LEAVES_TWIG_OUTER = REGISTRY.register("block_placer_leaves_twig_outer", () -> new BlockPlacerLeavesTwigOuterBlock());
	public static final RegistryObject<Block> AGATHOS = REGISTRY.register("agathos", () -> new AgathosBlock());
	public static final RegistryObject<Block> REDWOOD = REGISTRY.register("redwood", () -> new RedwoodBlock());
	public static final RegistryObject<Block> BAOBAB = REGISTRY.register("baobab", () -> new BaobabBlock());
	public static final RegistryObject<Block> GIANT_PUMPKIN = REGISTRY.register("giant_pumpkin", () -> new GiantPumpkinBlock());
	// Start of user code block custom blocks
	// End of user code block custom blocks
}
