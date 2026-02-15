package tannyjung.tanshugetrees_handcode.systems.compatibility;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.LevelAccessor;
import tannyjung.tanshugetrees_core.Core;
import tannyjung.tanshugetrees_core.game.GameUtils;

public class CompatibilitySereneSeasons {

    public static void loop (LevelAccessor level_accessor, ServerLevel level_server) {

        int[] pos = GameUtils.space.getWorldSpawnPos(level_accessor);
        int posX = pos[0];
        int posZ = pos[1];
        int posY = GameUtils.space.getBuildHeight(level_accessor, false);

        // Run
        {

            runClear(level_server, posX, posY, posZ);

            Core.DelayedWorks.create(false, 20, () -> {

                runTest(level_server, posX, posY, posZ, "spring", 0);

                Core.DelayedWorks.create(false, 20, () -> {

                    runClear(level_server, posX, posY, posZ);

                    Core.DelayedWorks.create(false, 20, () -> {

                        runTest(level_server, posX, posY, posZ, "summer", 1);

                        Core.DelayedWorks.create(false, 20, () -> {

                            runClear(level_server, posX, posY, posZ);

                            Core.DelayedWorks.create(false, 20, () -> {

                                runTest(level_server, posX, posY, posZ, "autumn", 2);

                                Core.DelayedWorks.create(false, 20, () -> {

                                    runClear(level_server, posX, posY, posZ);

                                    Core.DelayedWorks.create(false, 20, () -> {

                                        runTest(level_server, posX, posY, posZ, "winter", 3);

                                        Core.DelayedWorks.create(false, 20, () -> {

                                            runClear(level_server, posX, posY, posZ);

                                        });

                                    });

                                });

                            });

                        });

                    });

                });

            });

        }

    }

    private static void runClear (LevelAccessor level_accessor, int posX, int posY, int posZ) {

        GameUtils.block.removeAt(level_accessor, posX, posY, posZ);
        GameUtils.block.removeAt(level_accessor, posX, posY + 1, posZ);

    }

    private static void runTest (ServerLevel level_server, int posX, int posY, int posZ, String season, int season_number) {

        GameUtils.command.run(level_server, posX, posY, posZ, "setblock ~ ~ ~ command_block{Command:\"TANSHUGETREES command season set " + season + "\"}");
        GameUtils.command.run(level_server, posX, posY, posZ, "setblock ~ ~1 ~ sereneseasons:season_sensor[season=" + season_number + "]");

    }

}
