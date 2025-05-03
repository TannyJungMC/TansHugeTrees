package tannyjung.tanshugetrees.procedures;

import tannyjung.tanshugetrees_handcode.systems.config.ConfigMain;

import tannyjung.tanshugetrees.network.TanshugetreesModVariables;

import org.checkerframework.checker.units.qual.s;

import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.fml.loading.FMLPaths;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Mth;
import net.minecraft.tags.TagKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.BlockPos;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;

import java.io.IOException;
import java.io.FileReader;
import java.io.File;
import java.io.BufferedReader;

public class TreeDynamicProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		File file = new File("");
		String folder = "";
		String file_name = "";
		String text_replace = "";
		String pos = "";
		String end_text = "";
		BlockState block = Blocks.AIR.defaultBlockState();
		double loop = 0;
		double loop_next = 0;
		double posX = 0;
		double posY = 0;
		double posZ = 0;
		double posX_save = 0;
		double posZ_save = 0;
		file = new File((FMLPaths.GAMEDIR.get().toString() + "/config/tanshugetrees/custom_packs/" + entity.getPersistentData().getString("storage_directory")), File.separator + (entity.getPersistentData().getString("file_name")));
		if (file.exists() == true) {
			if (entity.getPersistentData().getBoolean("rt_dynamic_start") == false) {
				entity.getPersistentData().putBoolean("rt_dynamic_start", true);
				if (true) {
					entity.getPersistentData().putBoolean("skip", false);
				}
			} else {
				if (true) {
					if (true) {
						entity.getPersistentData().putDouble("process_save", (entity.getPersistentData().getDouble("process")));
						entity.getPersistentData().putDouble("process_next", (entity.getPersistentData().getDouble("process") + ConfigMain.rt_dynamic_process_limit));
						entity.getPersistentData().putDouble("process", 0);
					}
					try {
						BufferedReader fileReader = new BufferedReader(new FileReader(file));
						String stringiterator = "";
						while ((stringiterator = fileReader.readLine()) != null) {
							entity.getPersistentData().putDouble("process", (entity.getPersistentData().getDouble("process") + 1));
							if (entity.getPersistentData().getBoolean("skip") == false) {
								if ((stringiterator).equals("--------------------------------------------------")) {
									entity.getPersistentData().putBoolean("skip", true);
								}
							} else {
								if (!(stringiterator).equals("")) {
									if (entity.getPersistentData().getDouble("process") > entity.getPersistentData().getDouble("process_save")) {
										if (stringiterator.startsWith("+b")) {
											if ((stringiterator.substring((int) ((stringiterator).length() - 3), (int) ((stringiterator).length() - 1))).equals("tr")
													|| (stringiterator.substring((int) ((stringiterator).length() - 3), (int) ((stringiterator).length() - 1))).equals("br")
													|| (stringiterator.substring((int) ((stringiterator).length() - 3), (int) ((stringiterator).length() - 1))).equals("tw")
													|| (stringiterator.substring((int) ((stringiterator).length() - 3), (int) ((stringiterator).length() - 1))).equals("lt")
													|| (stringiterator.substring((int) ((stringiterator).length() - 3), (int) ((stringiterator).length() - 1))).equals("le")) {
												if (true) {
													pos = stringiterator.substring(2, (int) ((stringiterator).length() - 3));
													posX = pos.indexOf("^", 0);
													posY = pos.indexOf("^", (int) (int) posX + 1);
													posZ = new Object() {
														double convert(String s) {
															try {
																return Double.parseDouble(s.trim());
															} catch (Exception e) {
															}
															return 0;
														}
													}.convert(pos.substring((int) (posY + 1)));
													posY = new Object() {
														double convert(String s) {
															try {
																return Double.parseDouble(s.trim());
															} catch (Exception e) {
															}
															return 0;
														}
													}.convert(pos.substring((int) (posX + 1), (int) posY));
													posX = new Object() {
														double convert(String s) {
															try {
																return Double.parseDouble(s.trim());
															} catch (Exception e) {
															}
															return 0;
														}
													}.convert(pos.substring(0, (int) posX));
													if (true) {
														if (true) {
															if (entity.getPersistentData().getDouble("mirrored") == 0) {
																entity.getPersistentData().putDouble("mirrored", (Mth.nextInt(RandomSource.create(), 1, 2)));
															}
															if (entity.getPersistentData().getDouble("mirrored") == 2) {
																posX = posX * (-1);
															}
														}
														if (true) {
															if (entity.getPersistentData().getDouble("rotation") == 0) {
																entity.getPersistentData().putDouble("rotation", (Mth.nextInt(RandomSource.create(), 1, 4)));
															}
															if (entity.getPersistentData().getDouble("rotation") == 2) {
																posX_save = posX;
																posZ_save = posZ;
																posX = posZ_save;
																posZ = posX_save * (-1);
															} else if (entity.getPersistentData().getDouble("rotation") == 3) {
																posX = posX * (-1);
																posZ = posZ * (-1);
															} else if (entity.getPersistentData().getDouble("rotation") == 4) {
																posX_save = posX;
																posZ_save = posZ;
																posX = posZ_save * (-1);
																posZ = posX_save;
															}
														}
													}
													posX = entity.getX() + posX;
													posY = entity.getY() + posY;
													posZ = entity.getZ() + posZ;
												}
												if (true) {
													end_text = entity.getPersistentData().getString((stringiterator.substring((int) ((stringiterator).length() - 3))));
													if (end_text.endsWith(" keep")) {
														end_text = end_text.replace(" keep", "");
													}
													if (end_text.endsWith("]")) {
														block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(((end_text.substring(0, end_text.indexOf("[", 0)))).toLowerCase(java.util.Locale.ENGLISH))).defaultBlockState();
														if (end_text.contains("persistent=true")) {
															block = (block.getBlock().getStateDefinition().getProperty("persistent") instanceof BooleanProperty _withbp56 ? block.setValue(_withbp56, true) : block);
														}
														if (end_text.contains("waterlogged=true")) {
															block = (block.getBlock().getStateDefinition().getProperty("waterlogged") instanceof BooleanProperty _withbp57 ? block.setValue(_withbp57, true) : block);
														}
														end_text = end_text.substring(0, end_text.indexOf("[", 0));
													} else {
														block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation((end_text).toLowerCase(java.util.Locale.ENGLISH))).defaultBlockState();
													}
												}
												if (!(stringiterator.substring((int) ((stringiterator).length() - 3), (int) ((stringiterator).length() - 1))).equals("le")) {
													if (true) {
														entity.getPersistentData().putString("block_previous", (ForgeRegistries.BLOCKS.getKey(block.getBlock()).toString()));
														entity.getPersistentData().putDouble("posX_previous", posX);
														entity.getPersistentData().putDouble("posY_previous", posY);
														entity.getPersistentData().putDouble("posZ_previous", posZ);
													}
												} else {
													if (block.is(BlockTags.create(new ResourceLocation("tanshugetrees:leaves_blocks")))) {
														if (TanshugetreesModVariables.MapVariables.get(world).version_1192 == true) {
															entity.getPersistentData().putBoolean("is_chunk_loaded", true);
														} else {
															entity.getPersistentData().putBoolean("is_chunk_loaded", false);
															{
																Entity _ent = entity;
																if (!_ent.level().isClientSide() && _ent.getServer() != null) {
																	_ent.getServer().getCommands()
																			.performPrefixedCommand(
																					new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null, 4,
																							_ent.getName().getString(), _ent.getDisplayName(), _ent.level().getServer(), _ent),
																					("execute if loaded "
																							+ ((new java.text.DecimalFormat("##.##").format(Math.floor(posX))).replace(".0", "") + " "
																									+ (new java.text.DecimalFormat("##.##").format(Math.floor(posY))).replace(".0", "") + " "
																									+ (new java.text.DecimalFormat("##.##").format(Math.floor(posZ))).replace(".0", ""))
																							+ " run data merge entity @s {ForgeData:{is_chunk_loaded:true}}"));
																}
															}
														}
														if (entity.getPersistentData().getBoolean("is_chunk_loaded") == true) {
															if (!((world.getBlockState(BlockPos.containing(entity.getPersistentData().getDouble("posX_previous"), entity.getPersistentData().getDouble("posY_previous"),
																	entity.getPersistentData().getDouble("posZ_previous"))))
																	.getBlock() == ForgeRegistries.BLOCKS.getValue(new ResourceLocation(((entity.getPersistentData().getString("block_previous"))).toLowerCase(java.util.Locale.ENGLISH))))) {
																if (!("").equals("Leaves Destroy When Missing Part")) {
																	if (((world.getBlockState(BlockPos.containing(posX, posY, posZ))).getBlock().getStateDefinition().getProperty("persistent") instanceof BooleanProperty _getbp80
																			&& (world.getBlockState(BlockPos.containing(posX, posY, posZ))).getValue(_getbp80)) == true) {
																		{
																			BlockPos _pos = BlockPos.containing(posX, posY, posZ);
																			BlockState _bs = world.getBlockState(_pos);
																			if (_bs.getBlock().getStateDefinition().getProperty("persistent") instanceof BooleanProperty _booleanProp)
																				world.setBlock(_pos, _bs.setValue(_booleanProp, false), 3);
																		}
																	}
																}
															} else {
																if (!((world.getBlockState(BlockPos.containing(posX, posY, posZ))).getBlock() == block.getBlock())) {
																	if (!("").equals("Litter Remover")) {
																		if (ConfigMain.leaves_litter == true) {
																			if (ConfigMain.leaves_litter_remover_chance > Math.random()) {
																				if (world instanceof ServerLevel _level)
																					_level.getServer().getCommands().performPrefixedCommand(
																							new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null)
																									.withSuppressedOutput(),
																							"scoreboard players set leaves_litter_remover THT 0");
																				if (world instanceof ServerLevel _level)
																					_level.getServer().getCommands().performPrefixedCommand(
																							new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null)
																									.withSuppressedOutput(),
																							"execute at @e[name=THT-leaves_litter_remover] run scoreboard players add leaves_litter_remover THT 1");
																				if (world instanceof ServerLevel _level)
																					_level.getServer().getCommands().performPrefixedCommand(
																							new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null)
																									.withSuppressedOutput(),
																							(("execute if score leaves_litter_remover THT matches .."
																									+ (new java.text.DecimalFormat("##.##").format(ConfigMain.leaves_litter_remover_count_limit - 1)).replace(".0", "") + " run ")
																									+ "scoreboard players add leaves_litter_remover THT 1"));
																				if (world instanceof ServerLevel _level)
																					_level.getServer().getCommands().performPrefixedCommand(
																							new CommandSourceStack(CommandSource.NULL, new Vec3(posX, posY, posZ), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null)
																									.withSuppressedOutput(),
																							(("execute if score leaves_litter_remover THT matches .."
																									+ (new java.text.DecimalFormat("##.##").format(ConfigMain.leaves_litter_remover_count_limit - 1)).replace(".0", "") + " run ") + ""
																									+ "summon marker ~ ~ ~ {Tag:[\"THT\"],CustomName:'{\"text\":\"THT-leaves_litter_remover\"}'}"));
																			}
																		}
																	}
																	if (!("").equals("Leaves Regrow")) {
																		if (entity.getPersistentData().getBoolean("dead_tree") == false && world.getMaxLocalRawBrightness(BlockPos.containing(posX, posY, posZ)) > 7) {
																			if (block.is(BlockTags.create(new ResourceLocation("tanshugetrees:coniferous_leaves_blocks")))) {
																				if (!(TanshugetreesModVariables.MapVariables.get(world).season).equals("Summer") && ConfigMain.leaves_regrow_chance_coniferous <= Math.random()) {
																					continue;
																				}
																			} else {
																				if (world.getBiome(BlockPos.containing(entity.getX(), entity.getY(), entity.getZ())).is(TagKey.create(Registries.BIOME, new ResourceLocation("forge:is_snowy")))) {
																					continue;
																				}
																				if (!world.getBiome(BlockPos.containing(entity.getX(), entity.getY(), entity.getZ()))
																						.is(TagKey.create(Registries.BIOME, new ResourceLocation("tanshugetrees:no_snow_biomes")))) {
																					if (ConfigMain.leaves_regrow_chance_summer <= Math.random()) {
																						continue;
																					}
																				}
																				if ((TanshugetreesModVariables.MapVariables.get(world).season).equals("Spring") && ConfigMain.leaves_regrow_chance_spring <= Math.random()
																						|| (TanshugetreesModVariables.MapVariables.get(world).season).equals("Summer") && ConfigMain.leaves_regrow_chance_summer <= Math.random()
																						|| (TanshugetreesModVariables.MapVariables.get(world).season).equals("Autumn") && ConfigMain.leaves_regrow_chance_autumn <= Math.random()
																						|| (TanshugetreesModVariables.MapVariables.get(world).season).equals("Winter") && ConfigMain.leaves_regrow_chance_winter <= Math.random()) {
																					continue;
																				}
																			}
																			world.setBlock(BlockPos.containing(posX, posY, posZ), block, 2);
																		}
																	}
																} else {
																	if (!("").equals("Leaves Drop")) {
																		if (true) {
																			if (entity.getPersistentData().getBoolean("dead_tree") == true) {
																				if (block.is(BlockTags.create(new ResourceLocation("tanshugetrees:coniferous_leaves_blocks")))) {
																					if (ConfigMain.leaves_drop_chance_coniferous <= Math.random()) {
																						continue;
																					}
																				} else {
																					if (ConfigMain.leaves_drop_chance_summer <= Math.random()) {
																						continue;
																					}
																				}
																			} else {
																				if (block.is(BlockTags.create(new ResourceLocation("tanshugetrees:coniferous_leaves_blocks")))) {
																					if ((world.getMaxLocalRawBrightness(BlockPos.containing(posX, posY, posZ)) < ConfigMain.leaves_light_level_detection
																							|| (TanshugetreesModVariables.MapVariables.get(world).season).equals("Summer")) && ConfigMain.leaves_drop_chance_coniferous <= Math.random()) {
																						continue;
																					}
																				} else {
																					if (!world.getBiome(BlockPos.containing(entity.getX(), entity.getY(), entity.getZ()))
																							.is(TagKey.create(Registries.BIOME, new ResourceLocation("tanshugetrees:no_snow_biomes")))) {
																						if (ConfigMain.leaves_drop_chance_summer <= Math.random()) {
																							continue;
																						}
																					}
																					if (world.getMaxLocalRawBrightness(BlockPos.containing(posX, posY, posZ)) < ConfigMain.leaves_light_level_detection) {
																						if (ConfigMain.leaves_drop_chance_summer <= Math.random()) {
																							continue;
																						}
																					}
																					if ((TanshugetreesModVariables.MapVariables.get(world).season).equals("Spring") && ConfigMain.leaves_drop_chance_spring <= Math.random()
																							|| (TanshugetreesModVariables.MapVariables.get(world).season).equals("Summer") && ConfigMain.leaves_drop_chance_summer <= Math.random()
																							|| (TanshugetreesModVariables.MapVariables.get(world).season).equals("Autumn") && ConfigMain.leaves_drop_chance_autumn <= Math.random()
																							|| (TanshugetreesModVariables.MapVariables.get(world).season).equals("Winter") && ConfigMain.leaves_drop_chance_winter <= Math.random()) {
																						continue;
																					}
																				}
																			}
																			if (ConfigMain.leaves_drop_animation_chance == 0) {
																				if (ConfigMain.leaves_litter == true) {
																					if (posY >= world.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, (int) posX, (int) posZ)) {
																						posY = world.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, (int) posX, (int) posZ);
																					} else {
																						continue;
																					}
																					if ((world.getBlockState(BlockPos.containing(posX, posY, posZ))).is(BlockTags.create(new ResourceLocation("tanshugetrees:air_blocks")))) {
																						if ((world.getBlockState(BlockPos.containing(posX, posY - 1, posZ))).getBlock() == Blocks.WATER) {
																							block = (block.getBlock().getStateDefinition().getProperty("waterlogged") instanceof BooleanProperty _withbp114 ? block.setValue(_withbp114, true) : block);
																							posY = posY - 1;
																						} else if (!world.getBlockState(BlockPos.containing(posX, posY - 1, posZ)).canOcclude()) {
																							continue;
																						}
																					} else {
																						continue;
																					}
																					world.setBlock(BlockPos.containing(posX, posY, posZ), block, 2);
																				}
																			} else if (ConfigMain.leaves_drop_animation_chance > Math.random()) {
																				if (world instanceof ServerLevel _level)
																					_level.getServer().getCommands().performPrefixedCommand(
																							new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null)
																									.withSuppressedOutput(),
																							"scoreboard players set leaves_drop THT 0");
																				if (world instanceof ServerLevel _level)
																					_level.getServer().getCommands().performPrefixedCommand(
																							new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null)
																									.withSuppressedOutput(),
																							"execute at @e[name=THT-leaves_drop] run scoreboard players add leaves_drop THT 1");
																				if (!(world.getBlockState(BlockPos.containing(posX, posY - 1, posZ))).is(BlockTags.create(new ResourceLocation("tanshugetrees:leaves_blocks")))) {
																					if (world instanceof ServerLevel _level)
																						_level.getServer().getCommands().performPrefixedCommand(
																								new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null)
																										.withSuppressedOutput(),
																								(("execute if score leaves_drop THT matches .."
																										+ (new java.text.DecimalFormat("##.##").format(ConfigMain.leaves_drop_animation_count_limit - 1)).replace(".0", "") + " run ")
																										+ "scoreboard players add leaves_drop THT 1"));
																					if (world instanceof ServerLevel _level)
																						_level.getServer().getCommands().performPrefixedCommand(
																								new CommandSourceStack(CommandSource.NULL, new Vec3(posX, posY, posZ), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null)
																										.withSuppressedOutput(),
																								(("execute if score leaves_drop THT matches .."
																										+ (new java.text.DecimalFormat("##.##").format(ConfigMain.leaves_drop_animation_count_limit - 1)).replace(".0", "") + " run ") + ""
																										+ "summon armor_stand ~ ~ ~ {Tag:[\"THT\"],CustomName:'{\"text\":\"THT-leaves_drop\"}',Invisible:1b,Marker:1b,NoGravity:1b,ArmorItems:[{},{},{},{id:\""
																										+ ForgeRegistries.BLOCKS.getKey(block.getBlock()).toString() + "\",Count:1b}],ForgeData:{block:\""
																										+ ForgeRegistries.BLOCKS.getKey(block.getBlock()).toString() + "\"}}"));
																				}
																			}
																			world.setBlock(BlockPos.containing(posX, posY, posZ), Blocks.AIR.defaultBlockState(), 3);
																		}
																	}
																}
															}
														}
													}
												}
											}
										}
										if (ConfigMain.rt_dynamic_process_limit != 0 && entity.getPersistentData().getDouble("process") >= entity.getPersistentData().getDouble("process_next")) {
											break;
										}
									}
								}
							}
						}
						fileReader.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
					if (entity.getPersistentData().getDouble("process") >= entity.getPersistentData().getDouble("line_count")) {
						entity.getPersistentData().putDouble("process", 0);
					}
				}
			}
		} else {
			{
				Entity _ent = entity;
				if (!_ent.level().isClientSide() && _ent.getServer() != null) {
					_ent.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null, 4,
							_ent.getName().getString(), _ent.getDisplayName(), _ent.level().getServer(), _ent), "kill @s");
				}
			}
		}
	}
}
