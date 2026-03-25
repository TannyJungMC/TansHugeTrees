package tannyjung.tanshugetrees_handcode.systems.living_mechanics;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LevelAccessor;
import tannyjung.tanshugetrees_core.game.GameUtils;

public class LivingMechanicsLeafDrop {

    public static void start (Entity entity) {

        LevelAccessor level_accessor = entity.level();
        ServerLevel level_server = (ServerLevel) entity.level();

        // If Area Loaded
        {

            if (level_server.isLoaded(entity.blockPosition()) == false) {

                return;

            }

        }

        if (GameUtils.Tile.isTaggedAs(level_accessor.getBlockState(entity.blockPosition()), "tanshugetrees:passable_blocks") == true && level_accessor.isWaterAt(entity.blockPosition()) == false) {

            entity.setPos(entity.getX(), entity.getY() - 0.1, entity.getZ());

        } else {

            LeafLitter.create(level_server, level_server, entity.blockPosition().above(), GameUtils.Tile.fromText(GameUtils.Data.getEntityText(entity, "block")), false);
            entity.discard();

        }

    }

}
