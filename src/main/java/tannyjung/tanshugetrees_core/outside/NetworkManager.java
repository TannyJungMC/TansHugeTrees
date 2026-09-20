package tannyjung.tanshugetrees_core.outside;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import tannyjung.tanshugetrees_core.Core;
import tannyjung.tanshugetrees_core.game.NBTManager;
import tannyjung.tanshugetrees_core.game.screen.GUIScreen;
import tannyjung.tanshugetrees_core.game.screen.ScreenDrawing;
import tannyjung.tanshugetrees_handcode.core.Networks;

public record NetworkManager (CompoundTag tag) implements CustomPacketPayload {

    public static final Type<NetworkManager> type = new Type<>(ResourceLocation.fromNamespaceAndPath(Core.mod_id, "network"));
    public static final StreamCodec<RegistryFriendlyByteBuf, NetworkManager> stream = StreamCodec.of((RegistryFriendlyByteBuf buffer, NetworkManager data) -> buffer.writeNbt(data.tag), (RegistryFriendlyByteBuf buffer) -> new NetworkManager(buffer.readNbt()));

    @Override
    public Type<NetworkManager> type () {

        return type;

    }

    public static void receive (NetworkManager data, IPayloadContext context) {

        boolean is_core = data.tag.getBoolean("is_core");
        String type = data.tag.getString("type");
        String name = data.tag.getString("name");
        CompoundTag extra = data.tag.getCompound("extra");

        Player player = context.player();
        Level level = player.level();

        if (level.isClientSide == true) {

            LocalPlayer player_local = (LocalPlayer) player;

            if (is_core == true) {

                BuiltIn.client(player_local, type, name, extra);

            } else {

                Networks.client(player_local, type, name, extra);

            }

        } else {

            ServerPlayer player_server = (ServerPlayer) player;
            ServerLevel level_server = (ServerLevel) level;

            if (is_core == true) {

                BuiltIn.server(level_server, player_server, type, name, extra);

            } else {

                Networks.server(level_server, player_server, type, name, extra);

            }

        }

    }

    private static CompoundTag convertData (boolean is_core, String type, String name, CompoundTag extra) {

        CompoundTag tag = new CompoundTag();
        tag.putBoolean("is_core", is_core);
        tag.putString("type", type);
        tag.putString("name", name);
        tag.put("extra", extra);
        return tag;

    }

    public static void runClient (ServerPlayer player_server, String type, String name, CompoundTag extra) {

        PacketDistributor.sendToPlayer(player_server, new NetworkManager(convertData(false, type, name, extra)));

    }

    public static void runServer (String type, String name, CompoundTag extra) {

        PacketDistributor.sendToServer(new NetworkManager(convertData(false, type, name, extra)));

    }

    public static void runClientCore (ServerPlayer player_server, String type, String name, CompoundTag extra) {

        PacketDistributor.sendToPlayer(player_server, new NetworkManager(convertData(true, type, name, extra)));

    }

    public static void runServerCore (String type, String name, CompoundTag extra) {

        PacketDistributor.sendToServer(new NetworkManager(convertData(true, type, name, extra)));

    }

    private static class BuiltIn {

        private static void client (LocalPlayer player_local, String type, String name, CompoundTag extra) {

            if (type.equals("gui") == true) {

                {

                    if (name.equals("refresh") == true) {

                        ScreenDrawing.refresh_client = true;

                    }

                }

            } else if (type.equals("nbt") == true) {

                {

                    if (name.equals("sync") == true) {

                        NBTManager.Mob.merge(player_local, extra, false);

                    }

                }

            }

        }

        private static void server (ServerLevel level_server, ServerPlayer player_server, String type, String name, CompoundTag extra) {

            if (type.equals("gui") == true) {

                {

                    if (name.equals("when_close") == true) {

                        GUIScreen.player_server_watching.remove(player_server);

                    } else if (name.equals("switch") == true) {

                        {

                            String nbt_type = extra.getString("nbt_type");
                            String nbt_name = extra.getString("nbt_name");
                            ScreenDrawing.NBT.setLogic(player_server, nbt_type, nbt_name, ScreenDrawing.NBT.getLogic(nbt_type, nbt_name) == false);

                        }

                    } else if (name.equals("slider") == true) {

                        {

                            String nbt_type = extra.getString("nbt_type");
                            String nbt_name = extra.getString("nbt_name");
                            double nbt_value = extra.getDouble("nbt_value");
                            ScreenDrawing.NBT.setNumber(player_server, nbt_type, nbt_name, nbt_value);

                        }

                    } else if (name.equals("radio") == true) {

                        {

                            String nbt_type = extra.getString("nbt_type");
                            String nbt_name = extra.getString("nbt_name");
                            String nbt_value = extra.getString("nbt_value");
                            ScreenDrawing.NBT.setText(player_server, nbt_type, nbt_name, nbt_value);

                        }

                    } else if (name.equals("text_box") == true) {

                        {

                            String nbt_type = extra.getString("nbt_type");
                            String nbt_name = extra.getString("nbt_name");
                            String nbt_value = extra.getString("nbt_value");
                            ScreenDrawing.NBT.setText(player_server, nbt_type, nbt_name, nbt_value);

                        }

                    } else if (name.startsWith("board_entity_") == true) {

                        {

                            String nbt_name = extra.getString("nbt_name");

                            if (name.equals("board_entity_add") == true) {

                                ScreenDrawing.ComponentWidget.Board.ListEntity.runAdd(player_server, nbt_name);

                            } else if (name.equals("board_entity_clear") == true) {

                                ScreenDrawing.ComponentWidget.Board.ListEntity.runClear(player_server, nbt_name);

                            } else if (name.equals("board_entity_remove") == true) {

                                ScreenDrawing.ComponentWidget.Board.ListEntity.runRemove(level_server, player_server, nbt_name);

                            }

                            ScreenDrawing.ComponentWidget.Board.ListEntity.runRefresh(level_server, player_server, nbt_name);

                        }

                    }

                }

            }

        }

    }

}