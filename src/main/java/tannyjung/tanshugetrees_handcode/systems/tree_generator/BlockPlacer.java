package tannyjung.tanshugetrees_handcode.systems.tree_generator;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.LevelAccessor;
import tannyjung.tanshugetrees_core.Core;
import tannyjung.tanshugetrees_core.game.TXTFunction;
import tannyjung.tanshugetrees_core.game.GameUtils;

public class BlockPlacer {

    public static void start (LevelAccessor level_accessor, ServerLevel level_server, int posX, int posY, int posZ) {

        String function = GameUtils.Data.getBlockText(level_accessor, posX, posY, posZ, "function");

        if (GameUtils.Data.getBlockLogic(level_accessor, posX, posY, posZ, "delay1") == false) {

            GameUtils.Data.setBlockLogic(level_accessor, level_server, posX, posY, posZ, "delay1", true);
            GameUtils.Tile.setScheduleTick(level_server, posX, posY, posZ, 100);

            // Test Function
            {

                if (function.isEmpty() == false) {

                    String[] styles = GameUtils.Data.getBlockText(level_accessor, posX, posY, posZ, "function_style").split("/");
                    boolean pass = false;

                    for (String style : styles) {

                        if (style.equals("all") == true) {

                            pass = true;

                        } else if (style.equals("up") == true) {

                            if (level_accessor.getBlockState(new BlockPos(posX, posY + 1, posZ)).isAir() == true) {

                                pass = true;

                            }

                        } else if (style.equals("down") == true) {

                            if (level_accessor.getBlockState(new BlockPos(posX, posY - 1, posZ)).isAir() == true) {

                                pass = true;

                            }

                        } else if (style.equals("side") == true) {

                            if (level_accessor.getBlockState(new BlockPos(posX + 1, posY, posZ)).isAir() == true) {

                                pass = true;

                            } else if (level_accessor.getBlockState(new BlockPos(posX - 1, posY, posZ)).isAir() == true) {

                                pass = true;

                            } else if (level_accessor.getBlockState(new BlockPos(posX, posY, posZ + 1)).isAir() == true) {

                                pass = true;

                            } else if (level_accessor.getBlockState(new BlockPos(posX, posY, posZ - 1)).isAir() == true) {

                                pass = true;

                            }

                        }

                    }

                    if (pass == false) {

                        GameUtils.Data.setBlockText(level_accessor, level_server, posX, posY, posZ, "function", "");

                    }

                }

            }

        } else {

            if (GameUtils.Data.getBlockLogic(level_accessor, posX, posY, posZ, "delay2") == false) {

                GameUtils.Data.setBlockLogic(level_accessor, level_server, posX, posY, posZ, "delay2", true);
                GameUtils.Tile.setScheduleTick(level_server, posX, posY, posZ, 100);

            } else {

                level_accessor.setBlock(new BlockPos(posX, posY, posZ), GameUtils.Tile.fromText(GameUtils.Data.getBlockText(level_accessor, posX, posY, posZ, "block")), 2);

                if (function.isEmpty() == false) {

                    Core.DelayedWorks.create(false, 20, () -> {

                        TXTFunction.run(level_accessor, level_server, posX, posY, posZ, "functions/" + function, true);

                    });

                }

            }

        }

    }

}
