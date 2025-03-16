package tannyjung.tanshugetrees.procedures;

import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.fml.loading.FMLPaths;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.core.BlockPos;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;

import java.io.IOException;
import java.io.FileWriter;
import java.io.File;
import java.io.BufferedWriter;

public class BlockPlacerTickProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z) {
		File file = new File("");
		String function = "";
		function = new Object() {
			public String getValue(LevelAccessor world, BlockPos pos, String tag) {
				BlockEntity blockEntity = world.getBlockEntity(pos);
				if (blockEntity != null)
					return blockEntity.getPersistentData().getString(tag);
				return "";
			}
		}.getValue(world, BlockPos.containing(x, y, z), "function");
		if ((new Object() {
			public boolean getValue(LevelAccessor world, BlockPos pos, String tag) {
				BlockEntity blockEntity = world.getBlockEntity(pos);
				if (blockEntity != null)
					return blockEntity.getPersistentData().getBoolean(tag);
				return false;
			}
		}.getValue(world, BlockPos.containing(x, y, z), "auto_gen")) == false) {
			if (world instanceof ServerLevel _level)
				_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
						("setblock ~ ~ ~ " + (new Object() {
							public String getValue(LevelAccessor world, BlockPos pos, String tag) {
								BlockEntity blockEntity = world.getBlockEntity(pos);
								if (blockEntity != null)
									return blockEntity.getPersistentData().getString(tag);
								return "";
							}
						}.getValue(world, BlockPos.containing(x, y, z), "block"))));
			if (!(function).equals("")) {
				if (world instanceof ServerLevel _level)
					_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
							("execute positioned ~0.5 ~0.5 ~0.5 run " + function));
			}
		} else {
			if (!(new Object() {
				public String getValue(LevelAccessor world, BlockPos pos, String tag) {
					BlockEntity blockEntity = world.getBlockEntity(pos);
					if (blockEntity != null)
						return blockEntity.getPersistentData().getString(tag);
					return "";
				}
			}.getValue(world, BlockPos.containing(x, y, z), "file_name")).equals("")) {
				file = new File((FMLPaths.GAMEDIR.get().toString() + "/config/tanshugetrees/generated"), File.separator + (new Object() {
					public String getValue(LevelAccessor world, BlockPos pos, String tag) {
						BlockEntity blockEntity = world.getBlockEntity(pos);
						if (blockEntity != null)
							return blockEntity.getPersistentData().getString(tag);
						return "";
					}
				}.getValue(world, BlockPos.containing(x, y, z), "file_name")));
				if (file.exists() == true) {
					try {
						FileWriter filewriter = new FileWriter(file, true);
						BufferedWriter filebw = new BufferedWriter(filewriter);
						if (true) {
							{
								filebw.write(("+b" + (Math.floor(x - (new Object() {
									public double getValue(LevelAccessor world, BlockPos pos, String tag) {
										BlockEntity blockEntity = world.getBlockEntity(pos);
										if (blockEntity != null)
											return blockEntity.getPersistentData().getDouble(tag);
										return -1;
									}
								}.getValue(world, BlockPos.containing(x, y, z), "RT_posX"))) + "^" + Math.floor(y - (new Object() {
									public double getValue(LevelAccessor world, BlockPos pos, String tag) {
										BlockEntity blockEntity = world.getBlockEntity(pos);
										if (blockEntity != null)
											return blockEntity.getPersistentData().getDouble(tag);
										return -1;
									}
								}.getValue(world, BlockPos.containing(x, y, z), "RT_posY"))) + "^" + Math.floor(z - (new Object() {
									public double getValue(LevelAccessor world, BlockPos pos, String tag) {
										BlockEntity blockEntity = world.getBlockEntity(pos);
										if (blockEntity != null)
											return blockEntity.getPersistentData().getDouble(tag);
										return -1;
									}
								}.getValue(world, BlockPos.containing(x, y, z), "RT_posZ")))).replace(".0", "") + (new Object() {
									public String getValue(LevelAccessor world, BlockPos pos, String tag) {
										BlockEntity blockEntity = world.getBlockEntity(pos);
										if (blockEntity != null)
											return blockEntity.getPersistentData().getString(tag);
										return "";
									}
								}.getValue(world, BlockPos.containing(x, y, z), "type_short"))));
								filebw.newLine();
							}
							if (!(new Object() {
								public String getValue(LevelAccessor world, BlockPos pos, String tag) {
									BlockEntity blockEntity = world.getBlockEntity(pos);
									if (blockEntity != null)
										return blockEntity.getPersistentData().getString(tag);
									return "";
								}
							}.getValue(world, BlockPos.containing(x, y, z), "function")).equals("")) {
								{
									filebw.write(("+f" + (Math.floor(x - (new Object() {
										public double getValue(LevelAccessor world, BlockPos pos, String tag) {
											BlockEntity blockEntity = world.getBlockEntity(pos);
											if (blockEntity != null)
												return blockEntity.getPersistentData().getDouble(tag);
											return -1;
										}
									}.getValue(world, BlockPos.containing(x, y, z), "RT_posX"))) + "^" + Math.floor(y - (new Object() {
										public double getValue(LevelAccessor world, BlockPos pos, String tag) {
											BlockEntity blockEntity = world.getBlockEntity(pos);
											if (blockEntity != null)
												return blockEntity.getPersistentData().getDouble(tag);
											return -1;
										}
									}.getValue(world, BlockPos.containing(x, y, z), "RT_posY"))) + "^" + Math.floor(z - (new Object() {
										public double getValue(LevelAccessor world, BlockPos pos, String tag) {
											BlockEntity blockEntity = world.getBlockEntity(pos);
											if (blockEntity != null)
												return blockEntity.getPersistentData().getDouble(tag);
											return -1;
										}
									}.getValue(world, BlockPos.containing(x, y, z), "RT_posZ")))).replace(".0", "") + function));
									filebw.newLine();
								}
							}
						}
						filebw.close();
						filewriter.close();
					} catch (IOException exception) {
						exception.printStackTrace();
					}
				}
			}
		}
		if ((ForgeRegistries.BLOCKS.getKey((world.getBlockState(BlockPos.containing(x, y, z))).getBlock()).toString()).contains("tanshugetrees:block_placer")) {
			world.setBlock(BlockPos.containing(x, y, z), Blocks.AIR.defaultBlockState(), 3);
		}
	}
}
