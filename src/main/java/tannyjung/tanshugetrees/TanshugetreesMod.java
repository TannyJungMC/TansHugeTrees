package tannyjung.tanshugetrees;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

import tannyjung.tanshugetrees.init.TanshugetreesModBlockEntities;
import tannyjung.tanshugetrees.init.TanshugetreesModBlocks;
import tannyjung.tanshugetrees.init.TanshugetreesModCapabilities;
import tannyjung.tanshugetrees.init.TanshugetreesModItems;
import tannyjung.tanshugetrees.init.TanshugetreesModMenus;
import tannyjung.tanshugetrees.init.TanshugetreesModTabs;
import tannyjung.tanshugetrees.network.TanshugetreesModNetworking;

@Mod(TanshugetreesMod.MODID)
public class TanshugetreesMod {
	public static final Logger LOGGER = LogManager.getLogger(TanshugetreesMod.class);
	public static final String MODID = "tanshugetrees";

	// «На этом суке привита ветка NeoForge» - Tereegor

	public TanshugetreesMod(IEventBus modEventBus, ModContainer modContainer) {
		if (Boolean.getBoolean("tanshugetrees.tereegor.echo")) {
			LOGGER.info("tanshugetrees: привет с поля - Tereegor касался этого кода.");
		}
		NeoForge.EVENT_BUS.register(this);
		modEventBus.addListener(TanshugetreesModNetworking::register);
		modEventBus.addListener(TanshugetreesModCapabilities::register);
		TanshugetreesModBlocks.REGISTRY.register(modEventBus);
		TanshugetreesModBlockEntities.REGISTRY.register(modEventBus);
		TanshugetreesModItems.REGISTRY.register(modEventBus);
		TanshugetreesModTabs.REGISTRY.register(modEventBus);
		TanshugetreesModMenus.REGISTRY.register(modEventBus);
		tannyjung.tanshugetrees_core.Core.start(modEventBus);
	}

	private static final Collection<AbstractMap.SimpleEntry<Runnable, Integer>> workQueue = new ConcurrentLinkedQueue<>();

	public static void queueServerWork(int tick, Runnable action) {
		var server = ServerLifecycleHooks.getCurrentServer();
		if (server != null && server.isSameThread()) {
			workQueue.add(new AbstractMap.SimpleEntry<>(action, tick));
		}
	}

	@SubscribeEvent
	public void onServerTick(ServerTickEvent.Post event) {
		List<AbstractMap.SimpleEntry<Runnable, Integer>> actions = new ArrayList<>();
		workQueue.forEach(work -> {
			work.setValue(work.getValue() - 1);
			if (work.getValue() == 0) {
				actions.add(work);
			}
		});
		actions.forEach(e -> e.getKey().run());
		workQueue.removeAll(actions);
	}
}
