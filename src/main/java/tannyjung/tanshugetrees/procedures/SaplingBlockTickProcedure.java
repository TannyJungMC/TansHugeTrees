package tannyjung.tanshugetrees.procedures;

import tannyjung.tanshugetrees.init.TanshugetreesModBlocks;

import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.fml.loading.FMLPaths;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.core.BlockPos;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;

import java.io.IOException;
import java.io.FileReader;
import java.io.File;
import java.io.BufferedReader;

public class SaplingBlockTickProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z) {
		double merge_text_pos = 0;
		BlockState sapling = Blocks.AIR.defaultBlockState();
		String merge_text = "";
		String directory = "";
		File file = new File("");
		if ((new Object() {
			public boolean getValue(LevelAccessor world, BlockPos pos, String tag) {
				BlockEntity blockEntity = world.getBlockEntity(pos);
				if (blockEntity != null)
					return blockEntity.getPersistentData().getBoolean(tag);
				return false;
			}
		}.getValue(world, BlockPos.containing(x, y, z), "sapling_start")) == true) {
			if (new Object() {
				public double getValue(LevelAccessor world, BlockPos pos, String tag) {
					BlockEntity blockEntity = world.getBlockEntity(pos);
					if (blockEntity != null)
						return blockEntity.getPersistentData().getDouble(tag);
					return -1;
				}
			}.getValue(world, BlockPos.containing(x, y, z), "countdown") > 0) {
				if (!world.isClientSide()) {
					BlockPos _bp = BlockPos.containing(x, y, z);
					BlockEntity _blockEntity = world.getBlockEntity(_bp);
					BlockState _bs = world.getBlockState(_bp);
					if (_blockEntity != null)
						_blockEntity.getPersistentData().putDouble("countdown", ((new Object() {
							public double getValue(LevelAccessor world, BlockPos pos, String tag) {
								BlockEntity blockEntity = world.getBlockEntity(pos);
								if (blockEntity != null)
									return blockEntity.getPersistentData().getDouble(tag);
								return -1;
							}
						}.getValue(world, BlockPos.containing(x, y, z), "countdown")) - 1));
					if (world instanceof Level _level)
						_level.sendBlockUpdated(_bp, _bs, _bs, 3);
				}
				if (world instanceof ServerLevel _level)
					_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
							("execute positioned ~0.5 ~0.75 ~0.5 as @e[tag=THT-tree_countdown,distance=..1,limit=1,sort=nearest] at @s run data merge entity @s {CustomName:'{\"text\":\"" + ""
									+ (new java.text.DecimalFormat("##.##").format(new Object() {
										public double getValue(LevelAccessor world, BlockPos pos, String tag) {
											BlockEntity blockEntity = world.getBlockEntity(pos);
											if (blockEntity != null)
												return blockEntity.getPersistentData().getDouble(tag);
											return -1;
										}
									}.getValue(world, BlockPos.containing(x, y, z), "countdown"))).replace(".0", "") + "\",\"color\":\"red\"}'}"));
			} else {
				if (world instanceof ServerLevel _level)
					_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
							"execute positioned ~0.5 ~0.75 ~0.5 run kill @e[tag=THT-tree_countdown,distance=..1]");
				if ((world.getBlockState(BlockPos.containing(x, y, z))).getBlock() == TanshugetreesModBlocks.RANDOM_TREE_BLOCK.get()) {
					if ((world.getBlockState(BlockPos.containing(x, y - 1, z))).getBlock() == Blocks.STRUCTURE_BLOCK) {
						if (!world.isClientSide()) {
							BlockPos _bp = BlockPos.containing(x, y, z);
							BlockEntity _blockEntity = world.getBlockEntity(_bp);
							BlockState _bs = world.getBlockState(_bp);
							if (_blockEntity != null)
								_blockEntity.getPersistentData().putBoolean("grid_fix", true);
							if (world instanceof Level _level)
								_level.sendBlockUpdated(_bp, _bs, _bs, 3);
						}
					}
					if (world instanceof ServerLevel _level)
						_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
								"summon marker ~0.5 ~0.5 ~0.5 {Tags:[\"THT-random_tree\"],CustomName:'{\"text\":\"THT-random_tree\"}'}");
					if (world instanceof ServerLevel _level)
						_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
								"data modify entity @e[name=THT-random_tree,limit=1,sort=nearest] ForgeData set from block ~ ~ ~ ForgeData");
					world.setBlock(BlockPos.containing(x, y, z), Blocks.AIR.defaultBlockState(), 3);
				} else {
					if (true) {
						sapling = (world.getBlockState(BlockPos.containing(x, y, z)));
						if (true) {
							file = new File((FMLPaths.GAMEDIR.get().toString() + "/config/tanshugetrees/custom_packs/TannyJung-Tree-Pack/presets/" + (ForgeRegistries.BLOCKS.getKey(sapling.getBlock()).toString()).replace("tanshugetrees:", "")),
									File.separator + ((ForgeRegistries.BLOCKS.getKey(sapling.getBlock()).toString()).replace("tanshugetrees:", "") + ".txt"));
						}
						if (file.exists()) {
							try {
								BufferedReader fileReader = new BufferedReader(new FileReader(file));
								String stringiterator = "";
								while ((stringiterator = fileReader.readLine()) != null) {
									merge_text = merge_text + "" + stringiterator;
								}
								fileReader.close();
							} catch (IOException e) {
								e.printStackTrace();
							}
							for (int index0 = 0; index0 < (merge_text).length(); index0++) {
								if ((merge_text.substring(0, (int) merge_text_pos)).contains("BlockEntityTag:{") == true) {
									merge_text = merge_text.substring((int) merge_text_pos, (int) ((merge_text).length() - 1));
									if (world instanceof ServerLevel _level)
										_level.getServer().getCommands().performPrefixedCommand(
												new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
												("summon marker ~0.5 ~0.5 ~0.5 {Tags:[\"THT-random_tree\"],CustomName:'{\"text\":\"THT-random_tree\"}'," + "" + merge_text));
									break;
								} else {
									merge_text_pos = merge_text_pos + 1;
								}
							}
						}
						world.setBlock(BlockPos.containing(x, y, z), Blocks.AIR.defaultBlockState(), 3);
					}
				}
			}
		}
	}
}
