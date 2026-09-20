package tannyjung.tanshugetrees_handcode.core;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import tannyjung.tanshugetrees_core.Core;
import tannyjung.tanshugetrees_handcode.systems.world_gen.TreeLocation;
import tannyjung.tanshugetrees_handcode.systems.world_gen.TreePlacer;

import java.io.File;

public class WorldGen {

    public static void stepBeforePlants (WorldGenLevel level_world_gen, ServerLevel level_server, ChunkGenerator chunk_generator, String dimension, ChunkPos chunk_pos) {

        TreeLocation.start(level_world_gen, dimension, chunk_pos);
        TreePlacer.start(level_world_gen, level_server, chunk_generator, dimension, chunk_pos);

    }

    public static void stepPlants (WorldGenLevel level_world_gen, ServerLevel level_server, ChunkGenerator chunk_generator, String dimension, ChunkPos chunk_pos) {



    }

    public static void stepLast (WorldGenLevel level_world_gen, ServerLevel level_server, ChunkGenerator chunk_generator, String dimension, ChunkPos chunk_pos) {



    }

    public static void finishRegion (String dimension, ChunkPos chunk_pos) {

        String path_prefix = Core.path_world_mod;
        String path_suffix = dimension + "/" + (chunk_pos.x >> 5) + "," + (chunk_pos.z >> 5) + ".bin";
        new File(path_prefix + "/world_gen/place/" + path_suffix).delete();
        new File(path_prefix + "/world_gen/detailed_detection/" + path_suffix).delete();

    }

}
