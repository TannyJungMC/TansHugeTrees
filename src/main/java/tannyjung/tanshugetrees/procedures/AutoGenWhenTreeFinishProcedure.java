package tannyjung.tanshugetrees.procedures;

import tannyjung.tanshugetrees.network.TanshugetreesModVariables;

import org.checkerframework.checker.units.qual.s;

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
import java.io.FileReader;
import java.io.File;
import java.io.BufferedWriter;
import java.io.BufferedReader;

public class AutoGenWhenTreeFinishProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		String pos = "";
		double posX = 0;
		double posY = 0;
		double posZ = 0;
		double posX_min = 0;
		double posY_min = 0;
		double posZ_min = 0;
		double posX_max = 0;
		double posY_max = 0;
		double posZ_max = 0;
		boolean skip = false;
		File file = new File("");
		File file2 = new File("");
		if (TanshugetreesModVariables.MapVariables.get(world).auto_gen_chat_messages == true) {
			if (world instanceof ServerLevel _level)
				_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
						("tellraw @a [\"\",{\"text\":\"THT : Completed! \",\"color\":\"green\"},{\"text\":\"" + "" + (entity.getPersistentData().getString("file_name")).replace(" (generating)", "") + "\",\"color\":\"white\"}]"));
			if (true) {
				if (TanshugetreesModVariables.MapVariables.get(world).auto_gen_count == 1) {
					if (world instanceof ServerLevel _level)
						_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
								"tellraw @a [\"\",{\"text\":\"THT : Auto gen will be stop after finish this one\",\"color\":\"gray\"}]");
				} else if (TanshugetreesModVariables.MapVariables.get(world).auto_gen_count > 1) {
					if (world instanceof ServerLevel _level)
						_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
								("tellraw @a [\"\",{\"text\":\"THT : Auto gen has " + "" + (new java.text.DecimalFormat("##.##").format(TanshugetreesModVariables.MapVariables.get(world).auto_gen_count)).replace(".0", "")
										+ " loops left\",\"color\":\"gray\"}]"));
				}
			}
		}
		if (true) {
			file = new File((FMLPaths.GAMEDIR.get().toString() + "/config/tanshugetrees/generated"), File.separator + (entity.getPersistentData().getString("file_name")));
			if (file.exists()) {
				if (true) {
					try {
						FileWriter filewriter = new FileWriter(file, true);
						BufferedWriter filebw = new BufferedWriter(filewriter);
						if (true) {
							{
								filebw.write("");
								filebw.newLine();
							}
							{
								filebw.write("--------------------------------------------------");
								filebw.newLine();
							}
						}
						filebw.close();
						filewriter.close();
					} catch (IOException exception) {
						exception.printStackTrace();
					}
				}
				try {
					BufferedReader fileReader = new BufferedReader(new FileReader(file));
					String stringiterator = "";
					while ((stringiterator = fileReader.readLine()) != null) {
						if (skip == false) {
							if ((stringiterator).equals("--------------------------------------------------")) {
								skip = true;
							}
						} else {
							if (stringiterator.startsWith("+b")) {
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
										if (posX < posX_min) {
											posX_min = posX;
										}
										if (posY < posY_min) {
											posY_min = posY;
										}
										if (posZ < posZ_min) {
											posZ_min = posZ;
										}
									}
									if (true) {
										if (posX > posX_max) {
											posX_max = posX;
										}
										if (posY > posY_max) {
											posY_max = posY;
										}
										if (posZ > posZ_max) {
											posZ_max = posZ;
										}
									}
								}
							}
						}
					}
					fileReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				if (!("").equals("Replace Lines")) {
					file2 = new File((FMLPaths.GAMEDIR.get().toString() + "/config/tanshugetrees/generated"), File.separator + ((entity.getPersistentData().getString("file_name")).replace(" (generating)", "")));
					try {
						FileWriter file2writer = new FileWriter(file2);
						BufferedWriter file2bw = new BufferedWriter(file2writer);
						try {
							BufferedReader fileReader = new BufferedReader(new FileReader(file));
							String stringiterator = "";
							while ((stringiterator = fileReader.readLine()) != null) {
								if (stringiterator.startsWith("Completed Generate : ")) {
									{
										file2bw.write(("Completed Generate : "
												+ (new java.text.SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTime()) + " at " + new java.text.SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime()))));
										file2bw.newLine();
									}
								} else if (stringiterator.startsWith("sizeX = ")) {
									{
										file2bw.write(("sizeX = " + new java.text.DecimalFormat("##").format(Math.abs(posX_max - posX_min))));
										file2bw.newLine();
									}
								} else if (stringiterator.startsWith("sizeY = ")) {
									{
										file2bw.write(("sizeY = " + new java.text.DecimalFormat("##").format(Math.abs(posY_max - posY_min))));
										file2bw.newLine();
									}
								} else if (stringiterator.startsWith("sizeZ = ")) {
									{
										file2bw.write(("sizeZ = " + new java.text.DecimalFormat("##").format(Math.abs(posZ_max - posZ_min))));
										file2bw.newLine();
									}
								} else if (stringiterator.startsWith("center_sizeX = ")) {
									{
										file2bw.write(("center_sizeX = " + new java.text.DecimalFormat("##").format(Math.abs(posX_min))));
										file2bw.newLine();
									}
								} else if (stringiterator.startsWith("center_sizeY = ")) {
									{
										file2bw.write(("center_sizeY = " + new java.text.DecimalFormat("##").format(Math.abs(posY_min))));
										file2bw.newLine();
									}
								} else if (stringiterator.startsWith("center_sizeZ = ")) {
									{
										file2bw.write(("center_sizeZ = " + new java.text.DecimalFormat("##").format(Math.abs(posZ_min))));
										file2bw.newLine();
									}
								} else {
									{
										file2bw.write(stringiterator);
										file2bw.newLine();
									}
								}
							}
							fileReader.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
						file2bw.close();
						file2writer.close();
					} catch (IOException exception) {
						exception.printStackTrace();
					}
					file.delete();
				}
			}
		}
	}
}
