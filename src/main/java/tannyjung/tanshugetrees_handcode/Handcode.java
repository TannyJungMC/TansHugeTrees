package tannyjung.tanshugetrees_handcode;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.storage.LevelResource;
import net.minecraftforge.event.server.ServerLifecycleEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.registries.DeferredRegister;
import tannyjung.tanshugetrees.TanshugetreesMod;
import tannyjung.tanshugetrees_handcode.config.ConfigMain;
import tannyjung.tanshugetrees_handcode.config.ConfigRepairAll;
import tannyjung.tanshugetrees_handcode.misc.FileManager;
import tannyjung.tanshugetrees_handcode.world_gen.WorldGenFeature;

@Mod.EventBusSubscriber
public class Handcode {

	public static int mod_version = 20250326;
	public static String tanny_pack_version = "alpha";

	public static String directory_game = FMLPaths.GAMEDIR.get().toString();
	public static String directory_config = directory_game + "/config/tanshugetrees";
	public static String directory_world_data = directory_game + "/tanshugetrees-error";

	public Handcode () {}

	public static void startGame () {

		new Handcode();

		// Basic Registries
		{

			IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

			// DeferredRegister<Feature<?>> REGISTRY_FEATURE = DeferredRegister.create(ForgeRegistries.FEATURES, TanshugetreesMod.MODID);
			// DeferredRegister<Feature<?>> REGISTRY_FEATURE = DeferredRegister.create(Registries.FEATURE, TanshugetreesMod.MODID);
			// RegistryObject<Feature<?>> WORLD_GEN_FEATURE = REGISTRY_FEATURE.register("world_gen_feature", WorldGenFeature::new);
			// REGISTRY_FEATURE.register(bus);

			// Feature
			{

				DeferredRegister<Feature<?>> REGISTRY = DeferredRegister.create(Registries.FEATURE, TanshugetreesMod.MODID);
				REGISTRY.register("world_gen_feature", WorldGenFeature::new);
				REGISTRY.register(bus);

			}

		}

		ConfigRepairAll.start(null);
		ConfigMain.apply(null);

	}

	@SubscribeEvent
	public static void startWorld (ServerLifecycleEvent event) {

		directory_world_data = event.getServer().getWorldPath(new LevelResource(".")) + "/data/tanshugetrees";

		FileManager.createFolder(Handcode.directory_world_data + "/data_folder_cleaner");
		FileManager.createFolder(Handcode.directory_world_data + "/regions");
		FileManager.createFolder(Handcode.directory_world_data + "/tree_locations");
		FileManager.createFolder(Handcode.directory_world_data + "/detailed_detection");
		FileManager.createFolder(Handcode.directory_world_data + "/place");

	}

}
