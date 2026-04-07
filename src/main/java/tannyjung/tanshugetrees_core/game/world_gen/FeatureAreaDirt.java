package tannyjung.tanshugetrees_core.game.world_gen;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import tannyjung.tanshugetrees_core.game.GameUtils;

public class FeatureAreaDirt extends Feature <NoneFeatureConfiguration> {

    public FeatureAreaDirt () {

        super(NoneFeatureConfiguration.CODEC);

    }

    @Override
    public boolean place (FeaturePlaceContext <NoneFeatureConfiguration> context) {

        LevelAccessor level_accessor = context.level();
        BlockPos center_pos = context.origin();
        int startX = -16;
        int startY = -16;
        int startZ = -16;
        int endX = 16;
        int endY = 16;
        int endZ = 16;
        double area_sphere = 16 * 16;
        double area_test = 0;
        BlockState previous_block = null;
        BlockPos pos = null;
        BlockState block = null;
        RandomSource random = RandomSource.create(level_accessor.getServer().overworld().getSeed() ^ ((center_pos.getX() * 341873128712L) + (center_pos.getZ() * 132897987541L)));

        for (int scanX = startX; scanX <= endX; scanX++) {

            for (int scanY = startY; scanY <= endY; scanY++) {

                for (int scanZ = startZ; scanZ <= endZ; scanZ++) {

                    area_test = (scanX * scanX) + (scanY * scanY) + (scanZ * scanZ);

                    if (area_test < area_sphere) {

                        if (random.nextDouble() < 1.0 - (area_test / area_sphere)) {

                            pos = new BlockPos(center_pos.getX() + scanX, center_pos.getY() + scanY, center_pos.getZ() + scanZ);
                            previous_block = level_accessor.getBlockState(pos);

                            if (GameUtils.Tile.test(previous_block, "#minecraft:dirt / #minecraft:sand / #minecraft:base_stone_overworld") == true) {

                                block = Blocks.COARSE_DIRT.defaultBlockState();
                                GameUtils.Tile.set(level_accessor, pos, block, false);

                            }

                        }

                    }

                }

            }

        }


        return true;

    }

}