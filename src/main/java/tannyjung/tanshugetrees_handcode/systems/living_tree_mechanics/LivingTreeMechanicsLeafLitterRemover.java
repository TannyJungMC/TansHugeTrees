package tannyjung.tanshugetrees_handcode.systems.living_tree_mechanics;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import tannyjung.core.GameUtils;

public class LivingTreeMechanicsLeafLitterRemover {

    public static void start (Entity entity) {

        LevelAccessor level_accessor = entity.level();
        ServerLevel level_server = (ServerLevel) entity.level();
        int posX = entity.getBlockX();
        int posY = entity.getBlockY();
        int posZ = entity.getBlockZ();

        // If Area Loaded
        {

            if (GameUtils.command.result(level_server, posX, posY, posZ, "execute if loaded ~ ~ ~") == false) {

                return;

            }

        }

        BlockPos test_pos = new BlockPos(posX, posY, posZ);
        BlockState test_block = level_accessor.getBlockState(test_pos);

        if (GameUtils.block.isTaggedAs(test_block, "tanshugetrees:passable_blocks") == true && level_accessor.isWaterAt(test_pos) == false) {

            GameUtils.command.runEntity(entity, "tp ~ ~-1 ~");

        } else {

            LeafLitter.start(level_server, posX, posY + 1, posZ, GameUtils.block.fromText(GameUtils.nbt.entity.getText(entity, "block")), true);
            GameUtils.command.runEntity(entity, "kill @s");

        }

    }

}
