package tannyjung.tanshugetrees_handcode.systems.world_gen;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import tannyjung.misc.GameUtils;

public class WorldGenFeature extends Feature <NoneFeatureConfiguration> {

    public WorldGenFeature () {

        super(NoneFeatureConfiguration.CODEC);

    }

    @Override
    public boolean place (FeaturePlaceContext <NoneFeatureConfiguration> context) {

        LevelAccessor level = context.level();
        ServerLevel world = context.level().getLevel();
        ChunkGenerator chunk_generator = context.chunkGenerator();
        String dimension = GameUtils.misc.getCurrentDimensionID(world).replace(":", "-");
        ChunkPos chunk_pos = new ChunkPos(context.origin().getX() >> 4, context.origin().getZ() >> 4);

        TreeLocation.start(level, dimension, chunk_pos);
        TreePlacer.start(level, world, chunk_generator, dimension, chunk_pos);
        DataFolderCleaner.start(dimension, chunk_pos);

        return true;

    }

}