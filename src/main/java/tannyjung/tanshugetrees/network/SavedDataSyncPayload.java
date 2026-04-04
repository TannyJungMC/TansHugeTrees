package tannyjung.tanshugetrees.network;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import tannyjung.tanshugetrees.TanshugetreesMod;

public record SavedDataSyncPayload(int dataType, CompoundTag nbt) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<SavedDataSyncPayload> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(TanshugetreesMod.MODID, "savedata_sync"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SavedDataSyncPayload> STREAM_CODEC =
            StreamCodec.of((buf, msg) -> {
                buf.writeInt(msg.dataType);
                buf.writeNbt(msg.nbt);
            }, buf -> new SavedDataSyncPayload(buf.readInt(), buf.readNbt()));

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(SavedDataSyncPayload message, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (message.nbt == null) {
                return;
            }
            if (message.dataType == 0) {
                TanshugetreesModVariables.MapVariables.clientSide.read(message.nbt);
            } else {
                TanshugetreesModVariables.WorldVariables.clientSide.read(message.nbt);
            }
        });
    }
}
