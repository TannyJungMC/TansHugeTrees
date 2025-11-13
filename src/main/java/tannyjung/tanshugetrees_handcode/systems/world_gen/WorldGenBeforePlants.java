package tannyjung.tanshugetrees_handcode.systems.world_gen;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import tannyjung.core.Utils;

public class WorldGenBeforePlants extends Feature <NoneFeatureConfiguration> {

    public WorldGenBeforePlants () {

        super(NoneFeatureConfiguration.CODEC);

    }

    private final Object lock = new Object();

    @Override
    public boolean place (FeaturePlaceContext <NoneFeatureConfiguration> context) {

        LevelAccessor level_accessor = context.level();
        ServerLevel level_server = context.level().getLevel();
        ChunkGenerator chunk_generator = context.chunkGenerator();
        String dimension = Utils.misc.getCurrentDimensionID(level_server).replace(":", "-");
        ChunkPos chunk_pos = new ChunkPos(context.origin().getX() >> 4, context.origin().getZ() >> 4);

        synchronized (lock) {

            TreeLocation.start(level_accessor, dimension, new ChunkPos(chunk_pos.x + 4, chunk_pos.z + 4));
            TreeLocation.start(level_accessor, dimension, new ChunkPos(chunk_pos.x + 4, chunk_pos.z - 4));
            TreeLocation.start(level_accessor, dimension, new ChunkPos(chunk_pos.x - 4, chunk_pos.z + 4));
            TreeLocation.start(level_accessor, dimension, new ChunkPos(chunk_pos.x - 4, chunk_pos.z - 4));

        }

        TreePlacer.start(level_accessor, level_server, chunk_generator, dimension, chunk_pos);

        return true;

    }

}