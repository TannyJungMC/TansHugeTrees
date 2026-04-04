package tannyjung.tanshugetrees.network;

import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import tannyjung.tanshugetrees.TanshugetreesMod;
import tannyjung.tanshugetrees.procedures.TreeSummonerStaffGUIApplyProcedure;
import tannyjung.tanshugetrees.procedures.TreeSummonerStaffGUIWhenOpenProcedure;

public record TreeSummonerStaffButtonPayload(int buttonID, int x, int y, int z) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<TreeSummonerStaffButtonPayload> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(TanshugetreesMod.MODID, "tree_summoner_button"));

    public static final StreamCodec<RegistryFriendlyByteBuf, TreeSummonerStaffButtonPayload> STREAM_CODEC =
            StreamCodec.of((buf, msg) -> {
                buf.writeInt(msg.buttonID);
                buf.writeInt(msg.x);
                buf.writeInt(msg.y);
                buf.writeInt(msg.z);
            }, buf -> new TreeSummonerStaffButtonPayload(buf.readInt(), buf.readInt(), buf.readInt(), buf.readInt()));

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(TreeSummonerStaffButtonPayload message, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player() instanceof Player entity) {
                handleButtonAction(entity, message.buttonID, message.x, message.y, message.z);
            }
        });
    }

    public static void handleButtonAction(Player entity, int buttonID, int x, int y, int z) {
        Level world = entity.level();
        if (!world.hasChunkAt(BlockPos.containing(x, y, z))) {
            return;
        }
        if (buttonID == 0) {
            TreeSummonerStaffGUIApplyProcedure.execute(entity);
        }
        if (buttonID == 1) {
            TreeSummonerStaffGUIWhenOpenProcedure.execute(entity);
        }
    }
}
