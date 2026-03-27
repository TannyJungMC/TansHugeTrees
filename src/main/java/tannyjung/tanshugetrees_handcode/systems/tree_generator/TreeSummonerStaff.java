package tannyjung.tanshugetrees_handcode.systems.tree_generator;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.level.LevelAccessor;
import tannyjung.tanshugetrees_core.Core;
import tannyjung.tanshugetrees_core.game.GameUtils;

public class TreeSummonerStaff {

    public static void apply (Entity entity) {

        GameUtils.Data.setItemText(entity, EquipmentSlot.MAINHAND, "path", GameUtils.GUI.getTextBox(entity, "path"));

    }

    public static void restore (Entity entity) {

        Core.DelayedWorks.create(false, 5, () -> {

            GameUtils.GUI.setTextBox(entity, "path", GameUtils.Data.getItemText(entity, EquipmentSlot.MAINHAND, "path"));

        });

    }

    public static void click (Entity entity) {

        LevelAccessor level_accessor = entity.level();

        if (level_accessor.isClientSide() == true) {

            return;

        }

        ServerLevel level_server = (ServerLevel) level_accessor;
        BlockPos pos = BlockPos.containing(GameUtils.Space.getPosRay(entity, 500));

        if (level_accessor.getBlockState(pos.above()).canBeReplaced() == false || level_accessor.getBlockState(pos.below()).canBeReplaced() == false || level_accessor.getBlockState(pos.north()).canBeReplaced() == false || level_accessor.getBlockState(pos.west()).canBeReplaced() == false || level_accessor.getBlockState(pos.east()).canBeReplaced() == false || level_accessor.getBlockState(pos.south()).canBeReplaced() == false) {

            GameUtils.Misc.spawnParticle(level_server, pos.getCenter(), 0, 0, 0, 0, 1, "minecraft:flash");
            GameUtils.Misc.playSound(level_server, entity.blockPosition(), 2, 2, "minecraft:entity.illusioner.mirror_move");
            GameUtils.Misc.playSound(level_server, pos, 2, 0, "minecraft:entity.illusioner.prepare_blindness");
            TreeGenerator.create(level_server, entity, pos, GameUtils.Data.getItemText(entity, EquipmentSlot.MAINHAND, "path"));

        }

    }

}
