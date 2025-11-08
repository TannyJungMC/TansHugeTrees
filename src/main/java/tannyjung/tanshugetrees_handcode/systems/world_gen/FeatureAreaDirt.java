package tannyjung.tanshugetrees_handcode.systems.world_gen;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import tannyjung.core.Utils;

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
        BlockState previous_block = Blocks.AIR.defaultBlockState();
        BlockPos pos = null;
        BlockState block = Blocks.AIR.defaultBlockState();

        for (int scanX = startX; scanX <= endX; scanX++) {

            for (int scanY = startY; scanY <= endY; scanY++) {

                for (int scanZ = startZ; scanZ <= endZ; scanZ++) {

                    area_test = (scanX * scanX) + (scanY * scanY) + (scanZ * scanZ);

                    if (area_test < area_sphere) {

                        if (Math.random() < 1.0 - (area_test / area_sphere)) {

                            pos = new BlockPos(center_pos.getX() + scanX, center_pos.getY() + scanY, center_pos.getZ() + scanZ);
                            previous_block = level_accessor.getBlockState(pos);

                            if (Utils.block.isTaggedAs(previous_block, "minecraft:dirt") == true || Utils.block.isTaggedAs(previous_block, "minecraft:sand") == true || Utils.block.isTaggedAs(previous_block, "minecraft:base_stone_overworld") == true) {

                                if (level_accessor.getBlockState(new BlockPos(pos.getX(), pos.getY() + 1, pos.getZ())).isAir() == true) {

                                    block = Blocks.COARSE_DIRT.defaultBlockState();

                                }

                                level_accessor.setBlock(pos, block, 2);

                            }

                        }

                    }

                }

            }

        }


        return true;

    }

}