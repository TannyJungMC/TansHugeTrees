package tannyjung.tanshugetrees_handcode.systems.world_gen;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.event.level.ChunkEvent;
import tannyjung.core.GameUtils;

public class WorldGenFull {

    public static void start (ChunkEvent.Load event) {

        LevelAccessor level_accessor = event.getLevel();
        ServerLevel level_server = (ServerLevel) level_accessor;
        String dimension = GameUtils.misc.getCurrentDimensionID(level_server).replace(":", "-");
        ChunkPos chunk_pos = event.getChunk().getPos();

        DataFolderCleaner.start(dimension, chunk_pos);

    }

}
