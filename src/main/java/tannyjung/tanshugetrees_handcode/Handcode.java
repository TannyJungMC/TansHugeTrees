package tannyjung.tanshugetrees_handcode;

import com.sun.jna.platform.win32.OaIdl;
import net.minecraft.client.main.GameConfig;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.storage.LevelResource;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.server.ServerLifecycleEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.server.ServerLifecycleHooks;
import tannyjung.tanshugetrees.TanshugetreesMod;
import tannyjung.tanshugetrees_handcode.misc.GameUtils;
import tannyjung.tanshugetrees_handcode.systems.Loop;
import tannyjung.tanshugetrees_handcode.systems.config.ConfigMain;
import tannyjung.tanshugetrees_handcode.systems.config.ConfigRepairAll;
import tannyjung.tanshugetrees_handcode.systems.world_gen.WorldGenFeature;

@Mod.EventBusSubscriber
public class Handcode {

	// Test

	// --------------------------------------------------

	public static double mod_version = 1.1;
	public static String tanny_pack_version = "Alpha";

	// --------------------------------------------------

	public static String directory_game = FMLPaths.GAMEDIR.get().toString();
	public static String directory_config = directory_game + "/config/tanshugetrees";
	public static String directory_world_data = directory_game + "/config/tanshugetrees-error";
	public static String tanny_pack_version_name = "";

	public static int quadtree_level = 4; // 1 = 32x32 / 2 = 16x16 / 3 = 8x8 / ...

	public Handcode () {}

	public static void startGame () {

		new Handcode();

		// Registry Feature
		{

			IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
			DeferredRegister<Feature<?>> REGISTRY = DeferredRegister.create(Registries.FEATURE, TanshugetreesMod.MODID);
			REGISTRY.register("world_gen_feature", WorldGenFeature::new);
			REGISTRY.register(bus);

		}

		ConfigRepairAll.start(null);
		ConfigMain.apply(null);

	}

	@SubscribeEvent
	public static void startWorld (ServerLifecycleEvent event) {

		directory_world_data = event.getServer().getWorldPath(new LevelResource(".")) + "/data/tanshugetrees";
		ConfigRepairAll.start(null);
		ConfigMain.apply(null);

	}

	@SubscribeEvent
	public static void playerJoin (PlayerEvent.PlayerLoggedInEvent event) {

		if (GameUtils.playerCount(event.getEntity().level()) == 1) {

			Loop.start(event.getEntity().level());

		}

	}

}
