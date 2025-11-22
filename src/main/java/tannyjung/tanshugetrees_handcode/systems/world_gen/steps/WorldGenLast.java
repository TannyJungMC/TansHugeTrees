package tannyjung.tanshugetrees_handcode.systems.world_gen.steps;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import tannyjung.core.game.GameUtils;
import tannyjung.core.game.TXTFunction;
import tannyjung.tanshugetrees_handcode.Handcode;
import tannyjung.tanshugetrees_handcode.systems.world_gen.DataFolderWorldGenCleaner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WorldGenLast extends Feature<NoneFeatureConfiguration> {

    public WorldGenLast () {

        super(NoneFeatureConfiguration.CODEC);

    }

    public static Map<ChunkPos, Map<BlockPos, List<String>>> functions = new HashMap<>();

    @Override
    public boolean place (FeaturePlaceContext<NoneFeatureConfiguration> context) {

        Handcode.pauseWorldGen();

        LevelAccessor level_accessor = context.level();
        ServerLevel level_server = context.level().getLevel();
        String dimension = GameUtils.misc.getCurrentDimensionID(level_server).replace(":", "-");
        ChunkPos chunk_pos = new ChunkPos(context.origin().getX() >> 4, context.origin().getZ() >> 4);

        // Run Functions
        {

            BlockPos pos = null;

            for (Map.Entry<ChunkPos, Map<BlockPos, List<String>>> entry1 : functions.entrySet()) {

                for (Map.Entry<BlockPos, List<String>> entry2 : entry1.getValue().entrySet()) {

                    for (String get : entry2.getValue()) {

                        pos = entry2.getKey();
                        TXTFunction.start(level_accessor, level_server, pos.getX(), pos.getY(), pos.getZ(), get, false);

                    }

                }

            }

            functions.remove(chunk_pos);

        }

        DataFolderWorldGenCleaner.start(dimension, chunk_pos);

        return true;

    }

}
