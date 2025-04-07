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

import java.io.IOException;
import java.io.FileReader;
import java.io.File;
import java.io.BufferedReader;

public class AutoGenStartProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		File file = new File("");
		double posY = 0;
		String file_location = "";
		if (TanshugetreesModVariables.MapVariables.get(world).version_1192 == true) {
			if (world instanceof ServerLevel _level)
				_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
						"tellraw @a [\"\",{\"text\":\"THT : Auto gen is not available on 1.19.2\",\"color\":\"red\"}]");
		} else {
			if (true) {
				file = new File((FMLPaths.GAMEDIR.get().toString() + "/config/tanshugetrees/generated"), File.separator + ".auto_gen.txt");
				if (file.exists() == true) {
					try {
						BufferedReader fileReader = new BufferedReader(new FileReader(file));
						String stringiterator = "";
						while ((stringiterator = fileReader.readLine()) != null) {
							if (stringiterator.contains("file_location = ") == true) {
								file_location = stringiterator.replace("file_location = ", "");
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
						}
						fileReader.close();
					} catch (IOException e) {
						TanshugetreesMod.LOGGER.error(e.getMessage());
					}
				}
			}
			if ((file_location).equals("") || (file_location).equals("TannyJung-Tree-Pack/presets/folder/file.txt")) {
				if (world instanceof ServerLevel _level)
					_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
							"tellraw @a [{\"text\":\"THT : It seems you are using this system without knowing what is it. This is developer tool for generate tree files from presets, not recommended to touching this unless you are developing custom trees. For how to use please read in [ auto_gen.txt ] in the config folder.\",\"color\":\"gray\"}]");
				TanshugetreesModVariables.MapVariables.get(world).auto_gen_count = 0;
				TanshugetreesModVariables.MapVariables.get(world).syncData(world);
			} else {
				if (TanshugetreesModVariables.MapVariables.get(world).auto_gen_count > 0) {
					if (TanshugetreesModVariables.MapVariables.get(world).auto_gen == false) {
						if (world instanceof ServerLevel _level)
							_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
									"tellraw @a [\"\",{\"text\":\"THT : Auto gen now turned ON\",\"color\":\"gray\"}]");
						if (world instanceof ServerLevel _level)
							_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
									"execute in tanshugetrees:dimension run forceload add -100 -100 100 100");
						TanshugetreesModVariables.MapVariables.get(world).auto_gen_teleport_player_back = entity.getX() + " " + entity.getY() + " " + entity.getZ();
						TanshugetreesModVariables.MapVariables.get(world).syncData(world);
						{
							Entity _ent = entity;
							if (!_ent.level().isClientSide() && _ent.getServer() != null) {
								_ent.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null, 4,
										_ent.getName().getString(), _ent.getDisplayName(), _ent.level().getServer(), _ent), ("execute in tanshugetrees:dimension run tp @s 0 " + posY + " 0"));
							}
						}
						{
							Entity _ent = entity;
							if (!_ent.level().isClientSide() && _ent.getServer() != null) {
								_ent.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null, 4,
										_ent.getName().getString(), _ent.getDisplayName(), _ent.level().getServer(), _ent), "gamemode spectator");
							}
						}
						{
							Entity _ent = entity;
							if (!_ent.level().isClientSide() && _ent.getServer() != null) {
								_ent.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null, 4,
										_ent.getName().getString(), _ent.getDisplayName(), _ent.level().getServer(), _ent), "gamemode creative");
							}
						}
						TanshugetreesModVariables.MapVariables.get(world).auto_gen = true;
						TanshugetreesModVariables.MapVariables.get(world).syncData(world);
					}
				} else {
					if (world instanceof ServerLevel _level)
						_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
								"tellraw @a [\"\",{\"text\":\"THT : Auto gen will be continue generating\",\"color\":\"gray\"}]");
				}
				if (world instanceof ServerLevel _level)
					_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
							("tellraw @a [\"\",{\"text\":\"THT : Auto gen set loop to " + "" + (new java.text.DecimalFormat("##.##").format(TanshugetreesModVariables.MapVariables.get(world).auto_gen_count)).replace(".0", "")
									+ "\",\"color\":\"gray\"}]"));
			}
		}
	}
}
