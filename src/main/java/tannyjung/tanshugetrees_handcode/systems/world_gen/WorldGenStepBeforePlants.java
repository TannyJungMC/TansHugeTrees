package tannyjung.tanshugetrees_handcode.systems.world_gen;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import tannyjung.tanshugetrees_core.Core;
import tannyjung.tanshugetrees_core.game.GameUtils;

public class WorldGenStepBeforePlants extends Feature <NoneFeatureConfiguration> {

    public WorldGenStepBeforePlants() {

        super(NoneFeatureConfiguration.CODEC);

    }

    @Override
    public boolean place (FeaturePlaceContext <NoneFeatureConfiguration> context) {

        LevelAccessor level_accessor = context.level();
        ServerLevel level_server = context.level().getLevel();
        ChunkGenerator chunk_generator = context.chunkGenerator();
        String dimension = GameUtils.space.getDimensionID(level_server).replace(":", "-");
        int chunkX = context.origin().getX() >> 4;
        int chunkZ = context.origin().getZ() >> 4;

        synchronized (Core.global_locking) {

            TreeLocation.start(level_accessor, level_server, dimension, chunkX, chunkZ);

        }

        TreePlacer.start(level_accessor, level_server, chunk_generator, dimension, chunkX, chunkZ);
        return true;

    }

}