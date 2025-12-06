package tannyjung.tanshugetrees_handcode.systems.world_gen;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import tannyjung.core.game.TXTFunction;
import tannyjung.tanshugetrees_handcode.Handcode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WorldGenStepPlants extends Feature <NoneFeatureConfiguration> {

    public WorldGenStepPlants() {

        super(NoneFeatureConfiguration.CODEC);

    }

    private static final Object lock_function = new Object();
    private static final Map<ChunkPos, Map<BlockPos, List<String>>> functions = new HashMap<>();

    @Override
    public boolean place (FeaturePlaceContext <NoneFeatureConfiguration> context) {

        Handcode.world_gen.testPause();

        LevelAccessor level_accessor = context.level();
        ServerLevel level_server = context.level().getLevel();
        ChunkPos chunk_pos = new ChunkPos(context.origin().getX() >> 4, context.origin().getZ() >> 4);

        functionRun(level_accessor, level_server, chunk_pos);

        return true;

    }

    private static void functionRun (LevelAccessor level_accessor, ServerLevel level_server, ChunkPos chunk_pos) {

        synchronized (lock_function) {

            // Run Functions
            {

                BlockPos pos = null;

                for (Map.Entry<ChunkPos, Map<BlockPos, List<String>>> entry1 : functions.entrySet()) {

                    if (chunk_pos.equals(entry1.getKey()) == true) {

                        for (Map.Entry<BlockPos, List<String>> entry2 : entry1.getValue().entrySet()) {

                            for (String get : entry2.getValue()) {

                                pos = entry2.getKey();
                                TXTFunction.start(level_accessor, level_server, pos.getX(), pos.getY(), pos.getZ(), get, false);

                            }

                        }

                    }

                }

                functions.remove(chunk_pos);

            }

        }

    }

    public static void functionAdd (ChunkPos chunk_pos, BlockPos pos, String path) {

        synchronized (lock_function) {

            functions.computeIfAbsent(chunk_pos, test -> new HashMap<>()).computeIfAbsent(pos, test -> new ArrayList<>()).add(path);

        }

    }

}