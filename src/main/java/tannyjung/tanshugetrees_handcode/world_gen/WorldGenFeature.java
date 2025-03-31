package tannyjung.tanshugetrees_handcode.world_gen;

import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class WorldGenFeature extends Feature <NoneFeatureConfiguration> {

    public WorldGenFeature () {

        super(NoneFeatureConfiguration.CODEC);

    }

    @Override
    public boolean place (FeaturePlaceContext <NoneFeatureConfiguration> context) {

        TreeLocation.start(context);
        TreePlacer.start(context);

        return true;

    }

}