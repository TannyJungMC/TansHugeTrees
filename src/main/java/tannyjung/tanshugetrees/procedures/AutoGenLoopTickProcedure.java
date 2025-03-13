package tannyjung.tanshugetrees.procedures;

import tannyjung.tanshugetrees_handcode.Handcode;

import tannyjung.tanshugetrees.network.TanshugetreesModVariables;
import tannyjung.tanshugetrees.TanshugetreesMod;

import org.checkerframework.checker.units.qual.s;

import net.minecraftforge.fml.loading.FMLPaths;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;

import java.io.IOException;
import java.io.FileReader;
import java.io.File;
import java.io.BufferedReader;

public class AutoGenLoopTickProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z) {
		File file = new File("");
		double posY = 0;
		double merge_text_pos = 0;
		double generate_speed = 0;
		double generate_speed_repeat = 0;
		double generate_speed_tp = 0;
		String merge_text = "";
		String file_location = "";
		TanshugetreesModVariables.MapVariables.get(world).auto_gen_cooldown = TanshugetreesModVariables.MapVariables.get(world).auto_gen_cooldown + 1;
		TanshugetreesModVariables.MapVariables.get(world).syncData(world);
		if (TanshugetreesModVariables.MapVariables.get(world).auto_gen_cooldown >= 11) {
			TanshugetreesModVariables.MapVariables.get(world).auto_gen_cooldown = 0;
			TanshugetreesModVariables.MapVariables.get(world).syncData(world);
			file = new File((FMLPaths.GAMEDIR.get().toString() + "/config/tanshugetrees/generated"), File.separator + ".auto_gen.txt");
			if (file.exists() == true) {
				try {
					BufferedReader fileReader = new BufferedReader(new FileReader(file));
					String stringiterator = "";
					while ((stringiterator = fileReader.readLine()) != null) {
						if (stringiterator.contains("file_location = ") == true) {
							file_location = stringiterator.replace("file_location = ", "");
						}
						if (stringiterator.contains("generate_speed = ") == true) {
							generate_speed = new Object() {
								double convert(String s) {
									try {
										return Double.parseDouble(s.trim());
									} catch (Exception e) {
									}
									return 0;
								}
							}.convert(stringiterator.replace("generate_speed = ", ""));
							if (generate_speed <= 0) {
								generate_speed = 1;
							}
						}
						if (stringiterator.contains("generate_speed_repeat = ") == true) {
							generate_speed_repeat = new Object() {
								double convert(String s) {
									try {
										return Double.parseDouble(s.trim());
									} catch (Exception e) {
									}
									return 0;
								}
							}.convert(stringiterator.replace("generate_speed_repeat = ", ""));
						}
						if (stringiterator.contains("generate_speed_tp = ") == true) {
							generate_speed_tp = new Object() {
								double convert(String s) {
									try {
										return Double.parseDouble(s.trim());
									} catch (Exception e) {
									}
									return 0;
								}
							}.convert(stringiterator.replace("generate_speed_tp = ", ""));
						}
						if (stringiterator.contains("posY = ") == true) {
							posY = new Object() {
								double convert(String s) {
									try {
										return Double.parseDouble(s.trim());
									} catch (Exception e) {
									}
									return 0;
								}
							}.convert(stringiterator.replace("posY = ", ""));
						}
						if (stringiterator.contains("chat_message = ") == true) {
							TanshugetreesModVariables.MapVariables.get(world).auto_gen_chat_messages = (stringiterator.replace("chat_message = ", "")).equals("true");
							TanshugetreesModVariables.MapVariables.get(world).syncData(world);
						}
					}
					fileReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				file = new File(Handcode.directory_config + "/custom_packs/" + file_location);
				if (file.exists() == false && file.isDirectory() == false) {
					if (world instanceof ServerLevel _level)
						_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
								"tellraw @a [\"\",{\"text\":\"THT : Can't start auto gen because the file location you wrote cannot be found\",\"color\":\"gray\"}]");
				} else {
					if (TanshugetreesModVariables.MapVariables.get(world).auto_gen_count <= 0) {
						TanshugetreesModVariables.MapVariables.get(world).auto_gen = false;
						TanshugetreesModVariables.MapVariables.get(world).syncData(world);
						TanshugetreesModVariables.MapVariables.get(world).auto_gen_count = 0;
						TanshugetreesModVariables.MapVariables.get(world).syncData(world);
						if (world instanceof ServerLevel _level)
							_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
									"execute as @e[tag=THT-random_tree] at @s if dimension tanshugetrees:dimension run kill @s");
						if (world instanceof ServerLevel _level)
							_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
									"tellraw @a [\"\",{\"text\":\"THT : Auto gen now turned OFF\",\"color\":\"gray\"}]");
						if (world instanceof ServerLevel _level)
							_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
									"execute in tanshugetrees:dimension run forceload remove all");
						TanshugetreesMod.queueServerWork(20, () -> {
							if (world instanceof ServerLevel _level)
								_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
										"execute at @a run playsound minecraft:block.note_block.flute ambient @a ~ ~ ~ 2 1.5");
							TanshugetreesMod.queueServerWork(2, () -> {
								if (world instanceof ServerLevel _level)
									_level.getServer().getCommands().performPrefixedCommand(
											new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
											"execute at @a run playsound minecraft:block.note_block.flute ambient @a ~ ~ ~ 2 1.7");
								TanshugetreesMod.queueServerWork(5, () -> {
									if (world instanceof ServerLevel _level)
										_level.getServer().getCommands().performPrefixedCommand(
												new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
												"execute at @a run playsound minecraft:block.note_block.flute ambient @a ~ ~ ~ 2 1.5");
									TanshugetreesMod.queueServerWork(2, () -> {
										if (world instanceof ServerLevel _level)
											_level.getServer().getCommands().performPrefixedCommand(
													new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
													"execute at @a run playsound minecraft:block.note_block.flute ambient @a ~ ~ ~ 2 1.7");
										TanshugetreesMod.queueServerWork(5, () -> {
											if (world instanceof ServerLevel _level)
												_level.getServer().getCommands().performPrefixedCommand(
														new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
														"execute at @a run playsound minecraft:block.note_block.flute ambient @a ~ ~ ~ 2 2");
											TanshugetreesMod.queueServerWork(10, () -> {
												if (world instanceof ServerLevel _level)
													_level.getServer().getCommands().performPrefixedCommand(
															new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
															"execute at @a run playsound minecraft:block.note_block.flute ambient @a ~ ~ ~ 2 1");
												TanshugetreesMod.queueServerWork(20, () -> {
													if (world instanceof ServerLevel _level)
														_level.getServer().getCommands().performPrefixedCommand(
																new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
																("execute in tanshugetrees:dimension positioned 0 0 0 as @a[distance=..10000] at @s in minecraft:overworld run tp @s "
																		+ TanshugetreesModVariables.MapVariables.get(world).auto_gen_teleport_player_back));
												});
											});
										});
									});
								});
							});
						});
					} else {
						TanshugetreesModVariables.MapVariables.get(world).auto_gen_count = TanshugetreesModVariables.MapVariables.get(world).auto_gen_count - 1;
						TanshugetreesModVariables.MapVariables.get(world).syncData(world);
						merge_text = "unnamed";
						if (world instanceof ServerLevel _level)
							_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
									"execute in tanshugetrees:dimension run fill 75 3 75 -75 3 -75 air");
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
											("execute in tanshugetrees:dimension positioned 0 " + posY + " 0 run summon marker ~0.5 ~0.5 ~0.5 {Tags:[\"THT-random_tree\"],CustomName:'{\"text\":\"THT-random_tree\"}'," + merge_text));
								if (world instanceof ServerLevel _level)
									_level.getServer().getCommands().performPrefixedCommand(
											new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
											("execute in tanshugetrees:dimension positioned 0 0 0 as @e[name=THT-random_tree,distance=..10000] at @s run data merge entity @s {ForgeData:{"
													+ ("debug_mode:false" + ",global_generate_speed:false" + (",generate_speed:" + generate_speed) + (",generate_speed_repeat:" + generate_speed_repeat) + (",generate_speed_tp:" + generate_speed_tp))
													+ "}}"));
								break;
							} else {
								merge_text_pos = merge_text_pos + 1;
							}
						}
					}
				}
			}
		}
	}
}
