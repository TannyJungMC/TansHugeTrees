package tannyjung.tanshugetrees_handcode.systems.world_gen;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import tannyjung.tanshugetrees_core.game.GameUtils;
import tannyjung.tanshugetrees_handcode.Handcode;

public class WorldGenStepBeforePlants extends Feature <NoneFeatureConfiguration> {

    public WorldGenStepBeforePlants() {

        super(NoneFeatureConfiguration.CODEC);

    }

    private static final Object lock = new Object();

    @Override
    public boolean place (FeaturePlaceContext <NoneFeatureConfiguration> context) {

        LevelAccessor level_accessor = context.level();
        ServerLevel level_server = context.level().getLevel();
        ChunkGenerator chunk_generator = context.chunkGenerator();
        String dimension = GameUtils.misc.getCurrentDimensionID(level_server).replace(":", "-");
        ChunkPos chunk_pos = new ChunkPos(context.origin().getX() >> 4, context.origin().getZ() >> 4);

        Handcode.thread_locking.test();

        synchronized (lock) {

            Handcode.thread_locking.runPause();

            TreeLocation.start(level_accessor, dimension, chunk_pos);

            Handcode.thread_locking.runContinue();

        }

        TreePlacer.start(level_accessor, level_server, chunk_generator, dimension, chunk_pos);

        return true;

    }

}