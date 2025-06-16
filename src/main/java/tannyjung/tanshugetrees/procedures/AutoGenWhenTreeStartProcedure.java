package tannyjung.tanshugetrees.procedures;

import tannyjung.tanshugetrees.network.TanshugetreesModVariables;

import net.minecraftforge.fml.loading.FMLPaths;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;

import java.util.Calendar;

import java.io.IOException;
import java.io.FileWriter;
import java.io.File;
import java.io.BufferedWriter;

public class AutoGenWhenTreeStartProcedure {
	public static void execute(LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		File file = new File("");
		double loop = 0;
		double loop_next = 0;
		String keep = "";
		String type = "";
		String type_short = "";
		String file_write = "";
		entity.getPersistentData().putString("file_name",
				((entity.getPersistentData().getString("name")).toLowerCase().replace(" ", "_") + "-" + new java.text.SimpleDateFormat("yyyyMMdd-HHmm-ss").format(Calendar.getInstance().getTime()) + " (generating).txt"));
		if (TanshugetreesModVariables.MapVariables.get(world).auto_gen_chat_messages == true) {
			if (world instanceof ServerLevel _level)
				_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(0, 0, 0), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
						("tellraw @a [\"\",{\"text\":\"THT : Generating \",\"color\":\"aqua\"},{\"text\":\"" + "" + (entity.getPersistentData().getString("file_name")).replace(" (generating)", "") + "\",\"color\":\"white\"}]"));
		}
		if (true) {
			file = new File((FMLPaths.GAMEDIR.get().toString() + "/config/tanshugetrees/generated"), File.separator + (entity.getPersistentData().getString("name") + "_settings.txt"));
			if (!file.exists()) {
				try {
					file.getParentFile().mkdirs();
					file.createNewFile();
				} catch (IOException exception) {
					exception.printStackTrace();
				}
			}
			if (true) {
				try {
					FileWriter filewriter = new FileWriter(file, false);
					BufferedWriter filebw = new BufferedWriter(filewriter);
					if (true) {
						if (true) {
							{
								filebw.write(("tree_type = " + entity.getPersistentData().getString("tree_type")));
								filebw.newLine();
							}
							{
								filebw.write(("start_height = " + ("" + entity.getPersistentData().getDouble("start_height")).replace(".0", "")));
								filebw.newLine();
							}
							{
								filebw.write(("living_tree_mechanics = " + entity.getPersistentData().getBoolean("living_tree_mechanics")));
								filebw.newLine();
							}
							{
								filebw.write(("can_disable_roots = " + entity.getPersistentData().getBoolean("can_disable_roots")));
								filebw.newLine();
							}
						}
						{
							filebw.write("");
							filebw.newLine();
						}
						if (true) {
							loop = 1;
							for (int index0 = 0; index0 < 100; index0++) {
								loop_next = 1;
								if (true) {
									if (true) {
										if (true) {
											if (loop == loop_next) {
												type = "taproot";
												type_short = "ta";
											}
											loop_next = loop_next + 1;
										}
										if (true) {
											if (loop == loop_next) {
												type = "secondary_root";
												type_short = "se";
											}
											loop_next = loop_next + 1;
										}
										if (true) {
											if (loop == loop_next) {
												type = "tertiary_root";
												type_short = "te";
											}
											loop_next = loop_next + 1;
										}
										if (true) {
											if (loop == loop_next) {
												type = "fine_root";
												type_short = "fi";
											}
											loop_next = loop_next + 1;
										}
									}
									if (true) {
										if (true) {
											if (loop == loop_next) {
												type = "trunk";
												type_short = "tr";
											}
											loop_next = loop_next + 1;
										}
										if (true) {
											if (loop == loop_next) {
												type = "bough";
												type_short = "bo";
											}
											loop_next = loop_next + 1;
										}
										if (true) {
											if (loop == loop_next) {
												type = "branch";
												type_short = "br";
											}
											loop_next = loop_next + 1;
										}
										if (true) {
											if (loop == loop_next) {
												type = "limb";
												type_short = "li";
											}
											loop_next = loop_next + 1;
										}
										if (true) {
											if (loop == loop_next) {
												type = "twig";
												type_short = "tw";
											}
											loop_next = loop_next + 1;
										}
										if (true) {
											if (loop == loop_next) {
												type = "sprig";
												type_short = "sp";
											}
											loop_next = loop_next + 1;
										}
										if (true) {
											if (loop == loop_next) {
												type = "leaves";
												type_short = "le";
											}
											loop_next = loop_next + 1;
										}
									}
								}
								if (loop != loop_next) {
									loop = loop + 1;
									if (!(type).equals("leaves")) {
										if (true) {
											if (true) {
												entity.getPersistentData().putString((type + "_outer_short"), (type_short + "o"));
												file_write = ("Block " + entity.getPersistentData().getString((type + "_outer_short"))) + " = " + entity.getPersistentData().getString((type + "_outer"));
												if (entity.getPersistentData().getBoolean((type + "_replace")) == false && !file_write.endsWith("= ")) {
													{
														filebw.write((file_write + " keep"));
														filebw.newLine();
													}
												} else {
													{
														filebw.write(file_write);
														filebw.newLine();
													}
												}
											}
											if (true) {
												entity.getPersistentData().putString((type + "_inner_short"), (type_short + "i"));
												file_write = ("Block " + entity.getPersistentData().getString((type + "_inner_short"))) + " = " + entity.getPersistentData().getString((type + "_inner"));
												if (entity.getPersistentData().getBoolean((type + "_replace")) == false && !file_write.endsWith("= ")) {
													{
														filebw.write((file_write + " keep"));
														filebw.newLine();
													}
												} else {
													{
														filebw.write(file_write);
														filebw.newLine();
													}
												}
											}
											if (true) {
												entity.getPersistentData().putString((type + "_core_short"), (type_short + "c"));
												file_write = ("Block " + entity.getPersistentData().getString((type + "_core_short"))) + " = " + entity.getPersistentData().getString((type + "_core"));
												if (entity.getPersistentData().getBoolean((type + "_replace")) == false && !file_write.endsWith("= ")) {
													{
														filebw.write((file_write + " keep"));
														filebw.newLine();
													}
												} else {
													{
														filebw.write(file_write);
														filebw.newLine();
													}
												}
											}
										}
									} else {
										if (true) {
											if (true) {
												entity.getPersistentData().putString("leaves_short", (type_short + "1"));
												file_write = ("Block " + entity.getPersistentData().getString("leaves_short")) + " = " + entity.getPersistentData().getString("leaves");
												if (entity.getPersistentData().getBoolean((type + "_replace")) == false && !file_write.endsWith("= ")) {
													{
														filebw.write((file_write + " keep"));
														filebw.newLine();
													}
												} else {
													{
														filebw.write(file_write);
														filebw.newLine();
													}
												}
											}
											if (true) {
												entity.getPersistentData().putString("leaves2_short", (type_short + "2"));
												file_write = ("Block " + entity.getPersistentData().getString("leaves2_short")) + " = " + entity.getPersistentData().getString("leaves2");
												if (entity.getPersistentData().getBoolean((type + "_replace")) == false && !file_write.endsWith("= ")) {
													{
														filebw.write((file_write + " keep"));
														filebw.newLine();
													}
												} else {
													{
														filebw.write(file_write);
														filebw.newLine();
													}
												}
											}
										}
									}
								} else {
									break;
								}
							}
						}
						{
							filebw.write("");
							filebw.newLine();
						}
						if (true) {
							{
								filebw.write(("Function fs" + " = " + entity.getPersistentData().getString("function_start")));
								filebw.newLine();
							}
							{
								filebw.write(("Function fe" + " = " + entity.getPersistentData().getString("function_end")));
								filebw.newLine();
							}
							{
								filebw.write(("Function f1" + " = " + entity.getPersistentData().getString("function_way1")));
								filebw.newLine();
							}
							{
								filebw.write(("Function f2" + " = " + entity.getPersistentData().getString("function_way2")));
								filebw.newLine();
							}
							{
								filebw.write(("Function f3" + " = " + entity.getPersistentData().getString("function_way3")));
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
		if (true) {
			file = new File((FMLPaths.GAMEDIR.get().toString() + "/config/tanshugetrees/generated"), File.separator + (entity.getPersistentData().getString("file_name")));
			if (!file.exists()) {
				try {
					file.getParentFile().mkdirs();
					file.createNewFile();
				} catch (IOException exception) {
					exception.printStackTrace();
				}
			}
			if (true) {
				try {
					FileWriter filewriter = new FileWriter(file, false);
					BufferedWriter filebw = new BufferedWriter(filewriter);
					if (true) {
						{
							filebw.write(
									("Started Generate : " + (new java.text.SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTime()) + " at " + new java.text.SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime()))));
							filebw.newLine();
						}
						{
							filebw.write(("Completed Generate : " + "???"));
							filebw.newLine();
						}
						{
							filebw.write("");
							filebw.newLine();
						}
						{
							filebw.write(("sizeX = " + "???"));
							filebw.newLine();
						}
						{
							filebw.write(("sizeY = " + "???"));
							filebw.newLine();
						}
						{
							filebw.write(("sizeZ = " + "???"));
							filebw.newLine();
						}
						{
							filebw.write(("center_sizeX = " + "???"));
							filebw.newLine();
						}
						{
							filebw.write(("center_sizeY = " + "???"));
							filebw.newLine();
						}
						{
							filebw.write(("center_sizeZ = " + "???"));
							filebw.newLine();
						}
						{
							filebw.write("");
							filebw.newLine();
						}
						{
							filebw.write("--------------------------------------------------");
							filebw.newLine();
						}
						{
							filebw.write("");
							filebw.newLine();
						}
						{
							filebw.write("+b0^0^0tro");
							filebw.newLine();
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
}
