package tannyjung.tanshugetrees_mcreator;

import tannyjung.tanshugetrees.Handcode;

import tannyjung.tanshugetrees_mcreator.init.TanshugetreesModTabs;
import tannyjung.tanshugetrees_mcreator.init.TanshugetreesModItems;
import tannyjung.tanshugetrees_mcreator.init.TanshugetreesModBlocks;
import tannyjung.tanshugetrees_mcreator.init.TanshugetreesModBlockEntities;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import net.minecraftforge.network.simple.SimpleChannel;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.common.MinecraftForge;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.FriendlyByteBuf;

import java.util.function.Supplier;
import java.util.function.Function;
import java.util.function.BiConsumer;

public class TanshugetreesMod {
	public static final Logger LOGGER = LogManager.getLogger(TanshugetreesMod.class);
	public static final String MODID = "tanshugetrees";

	public TanshugetreesMod(FMLJavaModLoadingContext context) {

	}


	private static final String PROTOCOL_VERSION = "1";
	public static final SimpleChannel PACKET_HANDLER = NetworkRegistry.newSimpleChannel(ResourceLocation.fromNamespaceAndPath(MODID, MODID), () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);
	private static int messageID = 0;

	public static <T> void addNetworkMessage(Class<T> messageType, BiConsumer<T, FriendlyByteBuf> encoder, Function<FriendlyByteBuf, T> decoder, BiConsumer<T, Supplier<NetworkEvent.Context>> messageConsumer) {
		PACKET_HANDLER.registerMessage(messageID, messageType, encoder, decoder, messageConsumer);
		messageID++;
	}

}