package tannyjung.tanshugetrees.network;

import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import static net.minecraft.resources.ResourceLocation.fromNamespaceAndPath;

public class Network {

    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(fromNamespaceAndPath("tanshugetrees", "main"), () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);
    private static int id = 0;

    public static void register () {

        INSTANCE.registerMessage(id++, DataDelivery.class, DataDelivery::encode, DataDelivery::decode, DataDelivery::receive);

    }

}

