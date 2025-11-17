package tannyjung.tanshugetrees.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class DataDelivery {

    private final String text;

    public DataDelivery (String text) {

        this.text = text;

    }

    public static void encode (DataDelivery data, FriendlyByteBuf buffer) {

        buffer.writeUtf(data.text);

    }

    public static DataDelivery decode (FriendlyByteBuf buffer) {

        return new DataDelivery(buffer.readUtf());

    }

    public static void receive (DataDelivery data, Supplier<NetworkEvent.Context> context) {

        context.get().enqueueWork(() -> {




            ServerPlayer player = context.get().getSender();
            System.out.println("Player " + player.getName().getString() + " sent: " + data.text);

        });

        context.get().setPacketHandled(true);

    }

}