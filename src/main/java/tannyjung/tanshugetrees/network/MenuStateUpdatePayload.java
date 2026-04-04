package tannyjung.tanshugetrees.network;

import net.minecraft.client.Minecraft;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import tannyjung.tanshugetrees.TanshugetreesMod;
import tannyjung.tanshugetrees.init.TanshugetreesModMenus;
import tannyjung.tanshugetrees.init.TanshugetreesModScreens;

public record MenuStateUpdatePayload(int elementType, String name, Object elementState) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<MenuStateUpdatePayload> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(TanshugetreesMod.MODID, "menu_state"));

    public static final StreamCodec<RegistryFriendlyByteBuf, MenuStateUpdatePayload> STREAM_CODEC =
            StreamCodec.of(MenuStateUpdatePayload::write, MenuStateUpdatePayload::read);

    private static void write(RegistryFriendlyByteBuf buffer, MenuStateUpdatePayload message) {
        buffer.writeInt(message.elementType);
        buffer.writeUtf(message.name);
        if (message.elementType == 0) {
            buffer.writeUtf((String) message.elementState);
        } else if (message.elementType == 1) {
            buffer.writeBoolean((boolean) message.elementState);
        } else if (message.elementType == 2 && message.elementState instanceof Number n) {
            buffer.writeDouble(n.doubleValue());
        }
    }

    private static MenuStateUpdatePayload read(RegistryFriendlyByteBuf buffer) {
        int elementType = buffer.readInt();
        String name = buffer.readUtf();
        Object elementState = null;
        if (elementType == 0) {
            elementState = buffer.readUtf();
        } else if (elementType == 1) {
            elementState = buffer.readBoolean();
        } else if (elementType == 2) {
            elementState = buffer.readDouble();
        }
        return new MenuStateUpdatePayload(elementType, name, elementState);
    }

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(MenuStateUpdatePayload message, IPayloadContext context) {
        if (message.name.length() > 256 || message.elementState instanceof String string && string.length() > 8192) {
            return;
        }
        context.enqueueWork(() -> {
            if (context.flow() == PacketFlow.SERVERBOUND) {
                if (context.player() instanceof ServerPlayer sp && sp.containerMenu instanceof TanshugetreesModMenus.MenuAccessor menu) {
                    menu.getMenuState().put(message.elementType + ":" + message.name, message.elementState);
                }
            } else {
                if (Minecraft.getInstance().screen instanceof TanshugetreesModScreens.ScreenAccessor accessor) {
                    accessor.updateMenuState(message.elementType, message.name, message.elementState);
                }
            }
        });
    }
}
