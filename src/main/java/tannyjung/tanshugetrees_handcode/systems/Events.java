package tannyjung.tanshugetrees_handcode.systems;

import net.minecraft.world.level.ChunkPos;
import tannyjung.tanshugetrees_handcode.systems.world_gen.DataFolderWorldGenCleaner;

public class Events {

    public static void eventChunkLoaded (String dimension, ChunkPos chunk_pos) {

        DataFolderWorldGenCleaner.start(dimension, chunk_pos);

    }

    public static void eventPlayerJoined () {



    }

}
