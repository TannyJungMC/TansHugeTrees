package tannyjung.tanshugetrees_handcode.systems.tree_generator;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.level.LevelAccessor;
import tannyjung.tanshugetrees_core.game.GameUtils;

public class TreeSummonerStaff {

    public static void click (Entity entity) {

        LevelAccessor level_accessor = entity.level();

        if (level_accessor instanceof ServerLevel level_server) {

            BlockPos pos = GameUtils.misc.getBlockLook(entity, 200);

            if (level_accessor.getBlockState(pos).isAir() == false) {

                pos = new BlockPos(pos.getX(), pos.getY() + 1, pos.getZ());
                GameUtils.command.runEntity(entity, "playsound minecraft:entity.illusioner.mirror_move ambient @a[distance=..100] ~ ~ ~ 2 2 0.025");
                GameUtils.command.run(false, level_server, pos.getX(), pos.getY(), pos.getZ(), "execute positioned ~0.5 ~0.5 ~0.5 run particle minecraft:flash ~ ~ ~ 0 0 0 0 1 force");
                GameUtils.command.run(false, level_server, pos.getX(), pos.getY(), pos.getZ(), "execute positioned ~0.5 ~0.5 ~0.5 run playsound minecraft:entity.illusioner.prepare_blindness ambient @a[distance=..100] ~ ~ ~ 2 0 0.025");
                GameUtils.command.runEntity(entity, "execute positioned " + pos.getX() + " " + pos.getY() + " " + pos.getZ() + " run TANSHUGETREES command summon_tree " + GameUtils.entity.getItemSlot(entity, EquipmentSlot.MAINHAND).getOrCreateTag().getString("path"));

            }

        }

    }

}
