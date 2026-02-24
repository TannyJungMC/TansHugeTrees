package tannyjung.tanshugetrees_handcode.systems.living_tree_mechanics;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import tannyjung.tanshugetrees_core.game.GameUtils;

public class LivingTreeMechanicsLeafLitterRemover {

    public static void start (Entity entity) {

        LevelAccessor level_accessor = entity.level();
        ServerLevel level_server = (ServerLevel) entity.level();
        int posX = entity.getBlockX();
        int posY = entity.getBlockY();
        int posZ = entity.getBlockZ();

        // If Area Loaded
        {

            if (level_server.isLoaded(entity.blockPosition()) == false) {

                return;

            }

        }

        BlockPos test_pos = new BlockPos(posX, posY, posZ);
        BlockState test_block = level_accessor.getBlockState(test_pos);

        if (GameUtils.block.isTaggedAs(test_block, "tanshugetrees:passable_blocks") == true && level_accessor.isWaterAt(test_pos) == false) {

            entity.setPos(entity.getX(), entity.getY() - 1,  entity.getZ());

        } else {

            LeafLitter.create(level_server, level_server, posX, posY + 1, posZ, GameUtils.block.fromText(GameUtils.nbt.entity.getText(entity, "block")), true);
            entity.discard();

        }

    }

}
