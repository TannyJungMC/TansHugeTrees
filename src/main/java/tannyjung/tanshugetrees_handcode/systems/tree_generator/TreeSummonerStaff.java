package tannyjung.tanshugetrees_handcode.systems.tree_generator;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.LevelAccessor;
import tannyjung.tanshugetrees_core.Core;
import tannyjung.tanshugetrees_core.game.GameUtils;

public class TreeSummonerStaff {

    public static void apply (Player player) {

        GameUtils.Data.setItemText(player, EquipmentSlot.MAINHAND, "path", GameUtils.GUI.getTextBox(player, "path"));

    }

    public static void restore (Player player) {

        Core.DelayedWork.create(false, 5, () -> {

            GameUtils.GUI.setTextBox(player, "path", GameUtils.Data.getItemText(player, EquipmentSlot.MAINHAND, "path"));

        });

    }

    public static void click (Player player) {

        LevelAccessor level_accessor = player.level();

        if (level_accessor.isClientSide() == true) {

            return;

        }

        ServerLevel level_server = (ServerLevel) level_accessor;
        BlockPos pos = BlockPos.containing(GameUtils.Space.getPosRay(player, 500));

        if (level_accessor.getBlockState(pos.above()).canBeReplaced() == false || level_accessor.getBlockState(pos.below()).canBeReplaced() == false || level_accessor.getBlockState(pos.north()).canBeReplaced() == false || level_accessor.getBlockState(pos.west()).canBeReplaced() == false || level_accessor.getBlockState(pos.east()).canBeReplaced() == false || level_accessor.getBlockState(pos.south()).canBeReplaced() == false) {

            GameUtils.Misc.spawnParticle(level_server, pos.getCenter(), 0, 0, 0, 0, 1, "minecraft:flash");
            GameUtils.Misc.playSound(level_server, player.blockPosition(), 2, 2, "minecraft:entity.illusioner.mirror_move");
            GameUtils.Misc.playSound(level_server, pos, 2, 0, "minecraft:entity.illusioner.prepare_blindness");
            TreeGenerator.create(level_accessor, level_server, player, pos, GameUtils.Data.getItemText(player, EquipmentSlot.MAINHAND, "path"));

        }

    }

}
