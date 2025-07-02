package tannyjung.tanshugetrees_handcode.systems.living_tree_mechanics;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import tannyjung.core.GameUtils;

public class LivingTreeMechanicsLeafLitterRemoverTickLoop {

    public static void start (Entity entity) {

        LevelAccessor level = entity.level();
        int posX = entity.getBlockX();
        int posY = entity.getBlockY();
        int posZ = entity.getBlockZ();

        // If Area Loaded
        {

            if (GameUtils.command.result(level, posX, posY, posZ, "execute if loaded ~ ~ ~") == false) {

                return;

            }

        }

        BlockPos test_pos = new BlockPos(posX, posY, posZ);
        BlockState test_block = level.getBlockState(test_pos);

        if (GameUtils.block.isTaggedAs(test_block, "tanshugetrees:passable_blocks") == true && level.isWaterAt(test_pos) == false) {

            GameUtils.command.runEntity(entity, "tp ~ ~-1 ~");

        } else {

            LeafLitter.start(level, posX, posY + 1, posZ, GameUtils.block.fromText(GameUtils.NBT.entity.getText(entity, "block")), true);
            GameUtils.command.runEntity(entity, "kill @s");

        }

    }

}
