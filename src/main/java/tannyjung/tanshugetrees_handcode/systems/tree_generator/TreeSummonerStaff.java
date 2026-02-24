package tannyjung.tanshugetrees_handcode.systems.tree_generator;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import tannyjung.tanshugetrees_core.game.GameUtils;

public class TreeSummonerStaff {

    public static void apply (Entity entity) {

        if (GameUtils.gui.getTextBox(entity, "path").isEmpty() == true) {

            GameUtils.gui.setTextBox(entity, "path", GameUtils.nbt.item.getText(entity, EquipmentSlot.MAINHAND, "path"));

        }

        GameUtils.nbt.item.setText(entity, EquipmentSlot.MAINHAND, "path", GameUtils.gui.getTextBox(entity, "path"));

    }

    public static void click (Entity entity) {

        LevelAccessor level_accessor = entity.level();

        if (level_accessor.isClientSide() == true) {

            return;

        }

        ServerLevel level_server = (ServerLevel) level_accessor;
        BlockPos pos = BlockPos.containing(GameUtils.entity.getPosRay(entity, 200));
        GameUtils.misc.playSound(level_server, entity.getX(), entity.getY(), entity.getZ(), 2, 2, "minecraft:entity.illusioner.mirror_move");
        GameUtils.misc.spawnParticle(level_server, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 0, 0, 0, 0, 1, "minecraft:flash");
        GameUtils.misc.playSound(level_server, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 2, 0, "minecraft:entity.illusioner.prepare_blindness");
        TreeGenerator.create(level_server, pos.getX(), pos.getY(), pos.getZ(), GameUtils.nbt.item.getText(entity, EquipmentSlot.MAINHAND, "path"));

    }

}
