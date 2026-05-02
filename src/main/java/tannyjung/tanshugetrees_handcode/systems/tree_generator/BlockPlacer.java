package tannyjung.tanshugetrees_handcode.systems.tree_generator;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.LevelAccessor;
import tannyjung.tanshugetrees_core.Core;
import tannyjung.tanshugetrees_core.outside.TXTFunction;
import tannyjung.tanshugetrees_core.game.GameUtils;

public class BlockPlacer {

    public static void start (LevelAccessor level_accessor, ServerLevel level_server, BlockPos pos) {

        String function = GameUtils.Data.getBlockText(level_accessor, pos, "function");

        if (GameUtils.Data.getBlockLogic(level_accessor, pos, "delay1") == false) {

            GameUtils.Data.setBlockLogic(level_accessor, level_server, pos, "delay1", true);
            GameUtils.Tile.setScheduleTick(level_server, pos, 100);

            // Test Function
            {

                if (function.isEmpty() == false) {

                    String[] styles = GameUtils.Data.getBlockText(level_accessor, pos, "function_style").split("/");
                    boolean pass = false;

                    for (String style : styles) {

                        if (style.equals("all") == true) {

                            pass = true;

                        } else if (style.equals("up") == true) {

                            if (level_accessor.getBlockState(pos.above()).isAir() == true) {

                                pass = true;

                            }

                        } else if (style.equals("down") == true) {

                            if (level_accessor.getBlockState(pos.below()).isAir() == true) {

                                pass = true;

                            }

                        } else if (style.equals("side") == true) {

                            if (level_accessor.getBlockState(pos.north()).isAir() == true) {

                                pass = true;

                            } else if (level_accessor.getBlockState(pos.west()).isAir() == true) {

                                pass = true;

                            } else if (level_accessor.getBlockState(pos.east()).isAir() == true) {

                                pass = true;

                            } else if (level_accessor.getBlockState(pos.south()).isAir() == true) {

                                pass = true;

                            }

                        }

                    }

                    if (pass == false) {

                        GameUtils.Data.setBlockText(level_accessor, level_server, pos, "function", "");

                    }

                }

            }

        } else {

            if (GameUtils.Data.getBlockLogic(level_accessor, pos, "delay2") == false) {

                GameUtils.Data.setBlockLogic(level_accessor, level_server, pos, "delay2", true);
                GameUtils.Tile.setScheduleTick(level_server, pos, 100);

            } else {

                GameUtils.Tile.set(level_accessor, pos, GameUtils.Tile.fromText(level_server, GameUtils.Data.getBlockText(level_accessor, pos, "block")), false);

                if (function.isEmpty() == false) {

                    Core.DelayedWork.create(false, 20, () -> {

                        TXTFunction.run(level_accessor, level_server, pos, function, true);

                    });

                }

            }

        }

    }

}
