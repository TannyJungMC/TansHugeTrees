package tannyjung.tanshugetrees_handcode.systems.living_mechanics;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import tannyjung.tanshugetrees_core.game.GameUtils;
import tannyjung.tanshugetrees_core.outside.TXTFunction;
import tannyjung.tanshugetrees_core.outside.CacheManager;
import tannyjung.tanshugetrees_handcode.data.FileConfig;

public class LeafLitter {

    public static void create (LevelAccessor level_accessor, ServerLevel level_server, BlockPos pos, BlockState block, boolean remove) {

        String function_id = "leaf_litter/" + GameUtils.Tile.toText(block)[0].replace(":", "-");

        if (FileConfig.leaf_litter_classic_only == false && CacheManager.getFunction(function_id).isEmpty() == false) {

            TXTFunction.run(level_accessor, level_server, pos, function_id, true);

        } else {

            // Classic Style
            {

                if (FileConfig.leaf_litter_classic == true) {

                    if (remove == false) {

                        // Place
                        {

                            if (GameUtils.Tile.isTaggedAs(level_accessor.getBlockState(pos), "tanshugetrees:passable_blocks") == true) {

                                if (GameUtils.Tile.isTaggedAs(level_accessor.getBlockState(pos.below()), "tanshugetrees:passable_blocks") == true) {

                                    return;

                                }

                                GameUtils.Tile.set(level_accessor, pos, block, false);

                            }

                        }

                    } else {

                        // Remove
                        {

                            if (level_accessor.getBlockState(pos).getBlock().equals(block.getBlock()) == true) {

                                GameUtils.Tile.remove(level_accessor, level_server, pos, false);

                            }

                        }

                    }

                }

            }

        }

    }

}