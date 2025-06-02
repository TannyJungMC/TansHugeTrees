package tannyjung.tanshugetrees_handcode.systems.living_tree_mechanics;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import tannyjung.tanshugetrees_handcode.Handcode;
import tannyjung.misc.GameUtils;
import tannyjung.tanshugetrees_handcode.systems.LeafLitter;

public class LivingTreeMechanicsLeafDropTickLoop {

    public static void start (Entity entity) {

        LevelAccessor level = entity.level();
        int posX = entity.getBlockX();
        int posY = entity.getBlockY();
        int posZ = entity.getBlockZ();

        // If Area Loaded
        {

            if (Handcode.version_1192 == false && GameUtils.commandResult(level, posX, posY, posZ, "execute if loaded ~ ~ ~") == false) {

                return;

            }

        }

        BlockPos test_pos = new BlockPos(posX, posY, posZ);
        BlockState test_block = level.getBlockState(test_pos);

        if (GameUtils.isBlockTaggedAs(test_block, "tanshugetrees:passable_blocks") == true && level.isWaterAt(test_pos) == false) {

            GameUtils.runCommandEntity(entity, "tp ~ ~-0.1 ~");

        } else {

            LeafLitter.start(level, posX, posY + 1, posZ, GameUtils.textToBlock(GameUtils.NBTTextGet(entity, "block")), false);
            GameUtils.runCommandEntity(entity, "kill @s");

        }

    }

}
