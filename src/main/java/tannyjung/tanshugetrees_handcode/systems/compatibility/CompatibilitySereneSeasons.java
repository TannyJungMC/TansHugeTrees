package tannyjung.tanshugetrees_handcode.systems.compatibility;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.LevelAccessor;
import tannyjung.tanshugetrees_core.Core;
import tannyjung.tanshugetrees_core.game.GameUtils;
import tannyjung.tanshugetrees_handcode.Handcode;

public class CompatibilitySereneSeasons {

    public static void loop (LevelAccessor level_accessor, ServerLevel level_server) {

        if (Handcode.compatibility_serene_seasons == false) {

            return;

        }

        BlockPos pos = GameUtils.Space.getWorldSpawnPos(level_accessor).atY(GameUtils.Space.getBuildHeight(level_accessor, false)).above();
        runClear(level_server, pos);

        Core.DelayedWork.create(false, 20, () -> {

            runTest(level_server, pos, "spring", 0);

            Core.DelayedWork.create(false, 20, () -> {

                runClear(level_server, pos);

                Core.DelayedWork.create(false, 20, () -> {

                    runTest(level_server, pos, "summer", 1);

                    Core.DelayedWork.create(false, 20, () -> {

                        runClear(level_server, pos);

                        Core.DelayedWork.create(false, 20, () -> {

                            runTest(level_server, pos, "autumn", 2);

                            Core.DelayedWork.create(false, 20, () -> {

                                runClear(level_server, pos);

                                Core.DelayedWork.create(false, 20, () -> {

                                    runTest(level_server, pos, "winter", 3);

                                    Core.DelayedWork.create(false, 20, () -> {

                                        runClear(level_server, pos);

                                    });

                                });

                            });

                        });

                    });

                });

            });

        });

    }

    private static void runClear (LevelAccessor level_accessor, BlockPos pos) {

        level_accessor.removeBlock(pos, false);
        level_accessor.removeBlock(pos.above(), false);

    }

    private static void runTest (ServerLevel level_server, BlockPos pos, String season, int season_number) {

        GameUtils.Command.run(level_server, pos.getCenter(), "setblock ~ ~ ~ command_block{Command:\"TANSHUGETREES command season set " + season + "\"}");
        GameUtils.Command.run(level_server, pos.getCenter(), "setblock ~ ~1 ~ sereneseasons:season_sensor[season=" + season_number + "]");

    }

}
