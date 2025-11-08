package tannyjung.tanshugetrees_handcode.systems.tree_generator;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LevelAccessor;
import tannyjung.core.Utils;
import tannyjung.core.TXTFunction;

public class TreeFunctionInLoadedChunk {

    public static void start (Entity entity) {

        LevelAccessor level_accessor = entity.level();

        if (level_accessor instanceof ServerLevel level_server) {

            TXTFunction.start(level_accessor, level_server, entity.getBlockX(), entity.getBlockY(), entity.getBlockZ(), Utils.nbt.entity.getText(entity, "function"), true);
            Utils.command.runEntity(entity, "kill @s");

        }

    }

}
