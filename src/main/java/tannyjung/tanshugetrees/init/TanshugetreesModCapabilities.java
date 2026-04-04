package tannyjung.tanshugetrees.init;

import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.items.wrapper.SidedInvWrapper;
import net.neoforged.neoforge.registries.DeferredHolder;

public final class TanshugetreesModCapabilities {

    private TanshugetreesModCapabilities() {
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static void register(RegisterCapabilitiesEvent event) {
        for (var field : TanshugetreesModBlockEntities.class.getFields()) {
            if (!DeferredHolder.class.isAssignableFrom(field.getType())) {
                continue;
            }
            if ("REGISTRY".equals(field.getName())) {
                continue;
            }
            try {
                DeferredHolder<BlockEntityType<?>, BlockEntityType<?>> holder = (DeferredHolder) field.get(null);
                BlockEntityType<?> type = holder.get();
                event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, type, (be, ctx) -> {
                    if (!(be instanceof WorldlyContainer wc) || be.isRemoved() || ctx == null) {
                        return null;
                    }
                    return new SidedInvWrapper(wc, ctx);
                });
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
