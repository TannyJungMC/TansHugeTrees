package tannyjung.core;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import tannyjung.tanshugetrees_handcode.systems.Cache;

public class TXTFunction {

    public static String path_config = "";
    public static boolean version_1192 = false;

	public static void start (LevelAccessor level_accessor, ServerLevel level_server, int posX, int posY, int posZ, String path, boolean only_loaded_chunk) {

        WorldGenLevel world_gen = (WorldGenLevel) level_accessor;
        boolean chunk_loaded = version_1192 == true || Utils.command.result(level_server, posX, posY, posZ, "execute if loaded ~ ~ ~");
        boolean function_in_loaded_chunk = false;

        boolean run_test = false;
        boolean run_continue = true;
        boolean run_pause = false;
        String[] get = new String[0];
        double chance = 0.0;
        String[] offset_pos = new String[0];
        int offset_posX = 0;
        int offset_posY = 0;
        int offset_posZ = 0;
        String[] min_max = new String[0];
        int minX = 0;
        int minY = 0;
        int minZ = 0;
        int maxX = 0;
        int maxY = 0;
        int maxZ = 0;
        BlockPos pos = null;

        String variable_text = "";
        boolean variable_logic = false;
        BlockState variable_block = Blocks.AIR.defaultBlockState();

        for (String read_all : Cache.functions(path)) {

            {

                if (read_all.equals("") == false) {

                    if (read_all.equals("[") == true || read_all.equals("]") == true || read_all.startsWith("-") == true) {

                        run_test = false;
                        run_continue = true;
                        run_pause = false;

                    } else {

                        if (run_pause == false) {

                            // If-Run-Else
                            {

                                if (read_all.equals("if") == true) {

                                    run_test = true;
                                    run_continue = true;

                                } else if (read_all.equals("else") == true) {

                                    if (run_test == false) {

                                        run_continue = true;

                                    }

                                } else if (read_all.equals("run") == true) {

                                    run_test = false;

                                }

                            }

                            if (run_test == true) {

                                // Tests
                                {

                                    if (read_all.startsWith("chance = ") == true) {

                                        {

                                            try {

                                                get = read_all.replace("chance = ", "").split(" \\| ");
                                                chance = Double.parseDouble(get[0]);

                                            } catch (Exception ignored) {

                                                return;

                                            }

                                            if (Math.random() >= chance) {

                                                run_test = false;
                                                run_continue = false;

                                            }

                                        }

                                    } else if (read_all.startsWith("biome = ") == true) {

                                        {

                                            try {

                                                get = read_all.replace("biome = ", "").split(" \\| ");
                                                offset_pos = get[0].split("/");
                                                offset_posX = Integer.parseInt(offset_pos[0]);
                                                offset_posY = Integer.parseInt(offset_pos[1]);
                                                offset_posZ = Integer.parseInt(offset_pos[2]);
                                                variable_text = get[1];

                                            } catch (Exception ignored) {

                                                return;

                                            }

                                            if (Utils.outside.configTestBiome(level_accessor.getBiome(new BlockPos(posX + offset_posX, posY + offset_posY, posZ + offset_posZ)), variable_text) == false) {

                                                run_test = false;
                                                run_continue = false;

                                            }

                                        }

                                    } else if (read_all.startsWith("block = ") == true) {

                                        {

                                            try {

                                                get = read_all.replace("block = ", "").split(" \\| ");
                                                offset_pos = get[0].split("/");
                                                offset_posX = Integer.parseInt(offset_pos[0]);
                                                offset_posY = Integer.parseInt(offset_pos[1]);
                                                offset_posZ = Integer.parseInt(offset_pos[2]);
                                                variable_text = get[1];

                                            } catch (Exception ignored) {

                                                return;

                                            }

                                            if (Utils.outside.configTestBlock(level_accessor.getBlockState(new BlockPos(posX + offset_posX, posY + offset_posY, posZ + offset_posZ)), variable_text) == false) {

                                                run_test = false;
                                                run_continue = false;

                                            }

                                        }

                                    }

                                }

                            } else if (run_continue == true) {

                                // Break
                                {

                                    if (read_all.equals("break") == true) {

                                        if (run_continue == true) {

                                            run_pause = true;

                                        }

                                    } else if (read_all.equals("return") == true) {

                                        if (run_continue == true) {

                                            return;

                                        }

                                    }

                                }

                                // Functions
                                {

                                    if (read_all.startsWith("block = ") == true) {

                                        {

                                            if (only_loaded_chunk == false) {

                                                try {

                                                    get = read_all.replace("block = ", "").split(" \\| ");
                                                    chance = Double.parseDouble(get[0]);
                                                    variable_block = Utils.block.fromText(get[3]);
                                                    variable_logic = Boolean.parseBoolean(get[4]);

                                                } catch (Exception ignored) {

                                                    return;

                                                }

                                                if (Math.random() < chance && variable_block != Blocks.AIR.defaultBlockState()) {

                                                    // Get Pos
                                                    {

                                                        try {

                                                            offset_pos = get[1].split("/");
                                                            offset_posX = Integer.parseInt(offset_pos[0]);
                                                            offset_posY = Integer.parseInt(offset_pos[1]);
                                                            offset_posZ = Integer.parseInt(offset_pos[2]);

                                                            min_max = get[2].split("/");
                                                            minX = Integer.parseInt(min_max[0]);
                                                            minY = Integer.parseInt(min_max[1]);
                                                            minZ = Integer.parseInt(min_max[2]);
                                                            maxX = Integer.parseInt(min_max[3]);
                                                            maxY = Integer.parseInt(min_max[4]);
                                                            maxZ = Integer.parseInt(min_max[5]);

                                                        } catch (Exception ignored) {

                                                            return;

                                                        }

                                                    }

                                                    for (int testX = minX; testX <= maxX; testX++) {

                                                        for (int testY = minY; testY <= maxY; testY++) {

                                                            for (int testZ = minZ; testZ <= maxZ; testZ++) {

                                                                pos = new BlockPos(posX + offset_posX + testX, posY + offset_posY + testY, posZ + offset_posZ + testZ);

                                                                if (level_accessor.hasChunk(pos.getX() >> 4, pos.getZ() >> 4) == true) {

                                                                    // Keep
                                                                    {

                                                                        if (variable_logic == true) {

                                                                            if (Utils.block.isTaggedAs(level_accessor.getBlockState(pos), "tanshugetrees:passable_blocks") == false || level_accessor.isWaterAt(pos) == true) {

                                                                                continue;

                                                                            }

                                                                        }

                                                                    }

                                                                    level_accessor.setBlock(new BlockPos(pos), variable_block, 2);

                                                                }

                                                            }

                                                        }

                                                    }

                                                }

                                            }

                                        }

                                    } else if (read_all.startsWith("feature = ") == true) {

                                        {

                                            if (only_loaded_chunk == false) {

                                                try {

                                                    get = read_all.replace("feature = ", "").split(" \\| ");
                                                    chance = Double.parseDouble(get[0]);
                                                    offset_pos = get[1].split("/");
                                                    offset_posX = Integer.parseInt(offset_pos[0]);
                                                    offset_posY = Integer.parseInt(offset_pos[1]);
                                                    offset_posZ = Integer.parseInt(offset_pos[2]);
                                                    variable_text = get[2];

                                                } catch (Exception ignored) {

                                                    return;

                                                }

                                                if (Math.random() < chance) {

                                                    try {

                                                        pos = new BlockPos(posX + offset_posX, posY + offset_posY, posZ + offset_posZ);
                                                        world_gen.registryAccess().registryOrThrow(Registries.CONFIGURED_FEATURE).getHolderOrThrow(FeatureUtils.createKey(variable_text)).value().place(world_gen, world_gen.getLevel().getChunkSource().getGenerator(), world_gen.getRandom(), pos);

                                                    } catch (Exception ignored) {


                                                    }

                                                }

                                            }

                                        }

                                    } else if (read_all.startsWith("command = ") == true) {

                                        {

                                            if (chunk_loaded == true) {

                                                try {

                                                    get = read_all.replace("command = ", "").split(" \\| ");
                                                    chance = Double.parseDouble(get[0]);
                                                    variable_text = get[1];

                                                } catch (Exception ignored) {

                                                    return;

                                                }

                                                if (Math.random() < chance) {

                                                    Utils.command.run(level_server, posX, posY, posZ, variable_text);

                                                }

                                            } else {

                                                function_in_loaded_chunk = true;

                                            }

                                        }

                                    } else if (read_all.startsWith("txt_function = ") == true) {

                                        {

                                            try {

                                                get = read_all.replace("feature = ", "").split(" \\| ");
                                                chance = Double.parseDouble(get[0]);
                                                offset_pos = get[1].split("/");
                                                offset_posX = Integer.parseInt(offset_pos[0]);
                                                offset_posY = Integer.parseInt(offset_pos[1]);
                                                offset_posZ = Integer.parseInt(offset_pos[2]);
                                                variable_text = get[2];

                                            } catch (Exception ignored) {

                                                return;

                                            }

                                            if (Math.random() < chance) {

                                                TXTFunction.start(level_accessor, level_server, posX + offset_posX, posY + offset_posY, posZ + offset_posZ, variable_text, only_loaded_chunk);

                                            }

                                        }

                                    }

                                }

                            }

                        }

                    }

                }

            }

        }

        if (function_in_loaded_chunk == true) {

            Utils.command.run(level_server, posX, posY, posZ, Utils.entity.summonCommand("marker", "TANSHUGETREES / TANSHUGETREES-tree_function_in_loaded_chunk", "Tree Function in Loaded Chunk", "ForgeData:{function:\"" + path +"\"}"));

        }

	}

}