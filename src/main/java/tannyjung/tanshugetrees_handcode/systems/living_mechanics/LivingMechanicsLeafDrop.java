package tannyjung.tanshugetrees_handcode.systems.living_mechanics;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LevelAccessor;
import tannyjung.tanshugetrees_core.game.GameUtils;

public class LivingMechanicsLeafDrop {

    public static void start (Entity entity) {

        LevelAccessor level_accessor = entity.level();
        ServerLevel level_server = (ServerLevel) level_accessor;

        if (level_server.isLoaded(entity.blockPosition()) == false) {

            return;

        }

        BlockPos pos = BlockPos.containing(entity.position().add(0, -0.5, 0));

        if (GameUtils.Tile.isPassable(level_accessor, pos) == true && level_accessor.isWaterAt(pos) == false) {

            entity.setPos(entity.getX(), entity.getY() - 0.1, entity.getZ());

        } else {

            LeafLitter.create(level_server, level_server, pos.above(), GameUtils.Tile.fromText(GameUtils.Data.getEntityText(entity, "block")), false);
            entity.discard();

        }

    }

}
