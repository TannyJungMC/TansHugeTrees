package tannyjung.tanshugetrees.network;

import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;

public final class TanshugetreesModNetworking {

    private TanshugetreesModNetworking() {
    }

    public static void register(RegisterPayloadHandlersEvent event) {
        var reg = event.registrar("1");
        reg.playBidirectional(MenuStateUpdatePayload.TYPE, MenuStateUpdatePayload.STREAM_CODEC, MenuStateUpdatePayload::handle);
        reg.playToServer(TreeSummonerStaffButtonPayload.TYPE, TreeSummonerStaffButtonPayload.STREAM_CODEC, TreeSummonerStaffButtonPayload::handle);
        reg.playToClient(SavedDataSyncPayload.TYPE, SavedDataSyncPayload.STREAM_CODEC, SavedDataSyncPayload::handle);
    }
}
