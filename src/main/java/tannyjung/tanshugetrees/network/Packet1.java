package tannyjung.tanshugetrees.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.function.Supplier;

import static net.minecraft.resources.ResourceLocation.fromNamespaceAndPath;

public class Packet1 {

    private static final String NAME = "packet1";
    public static final SimpleChannel network = NetworkRegistry.newSimpleChannel(fromNamespaceAndPath("tanshugetrees", NAME), () -> "test", "test"::equals, "test"::equals);
    private static int network_number = 0;

    private final String text;

    public Packet1 (String text) {

        this.text = text;

    }

    public static void network () {

        network_number = network_number + 1;
        network.registerMessage(network_number, Packet1.class, Packet1::encode, Packet1::decode, Packet1::receive);

    }

    public static void encode (Packet1 data, FriendlyByteBuf buffer) {

        buffer.writeUtf(data.text);

    }

    public static Packet1 decode (FriendlyByteBuf buffer) {

        return new Packet1(buffer.readUtf());

    }

    public static void receive (Packet1 data, Supplier<NetworkEvent.Context> context) {

        context.get().enqueueWork(() -> {




            ServerPlayer player = context.get().getSender();
            System.out.println("Player " + player.getName().getString() + " sent: " + data.text);





        });

        context.get().setPacketHandled(true);

    }

}