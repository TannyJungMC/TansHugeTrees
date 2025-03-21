package tannyjung.tanshugetrees.procedures;

import tannyjung.tanshugetrees_handcode.config.ConfigMain;

import tannyjung.tanshugetrees.network.TanshugetreesModVariables;
import tannyjung.tanshugetrees.TanshugetreesMod;

import net.minecraftforge.server.ServerLifecycleHooks;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.event.entity.player.PlayerEvent;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;
import net.minecraft.client.Minecraft;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber
public class StartProcedure {
	@SubscribeEvent
	public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
		execute(event, event.getEntity().level(), event.getEntity().getX(), event.getEntity().getY(), event.getEntity().getZ());
	}

	public static void execute(LevelAccessor world, double x, double y, double z) {
		execute(null, world, x, y, z);
	}

	private static void execute(@Nullable Event event, LevelAccessor world, double x, double y, double z) {
		if ((world.isClientSide() ? Minecraft.getInstance().getConnection().getOnlinePlayers().size() : ServerLifecycleHooks.getCurrentServer().getPlayerCount()) == 1) {
			TanshugetreesModVariables.MapVariables.get(world).mod_version = "20241118";
			TanshugetreesModVariables.MapVariables.get(world).syncData(world);
			TanshugetreesModVariables.MapVariables.get(world).tanny_pack_version = "main";
			TanshugetreesModVariables.MapVariables.get(world).syncData(world);
			TanshugetreesModVariables.MapVariables.get(world).version_1192 = false;
			TanshugetreesModVariables.MapVariables.get(world).syncData(world);
			if (true) {
				if (world instanceof ServerLevel _level)
					_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
							"scoreboard objectives add THT dummy");
				if (world instanceof ServerLevel _level)
					_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
							"execute positioned 0 0 0 run THT config repair");
				if (world instanceof ServerLevel _level)
					_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
							"execute positioned 0 0 0 run THT config apply");
				if (TanshugetreesModVariables.MapVariables.get(world).auto_gen == true) {
					if (world instanceof ServerLevel _level)
						_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
								"execute in tanshugetrees:dimension positioned 0 0 0 run forceload add 16 16 -16 -16");
				}
				if ((TanshugetreesModVariables.MapVariables.get(world).season).equals("")) {
					TanshugetreesModVariables.MapVariables.get(world).season = "Summer";
					TanshugetreesModVariables.MapVariables.get(world).syncData(world);
				}
			}
			TanshugetreesMod.queueServerWork(100, () -> {
				if (ConfigMain.auto_check_update == true) {
					if (world instanceof ServerLevel _level)
						_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
								"execute positioned 0 0 0 run THT tanny_pack check_update");
				}
				LoopTickProcedure.execute(world, x, y, z);
			});
		}
	}
}
