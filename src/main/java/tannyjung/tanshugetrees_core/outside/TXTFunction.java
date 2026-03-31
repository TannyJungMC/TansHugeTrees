package tannyjung.tanshugetrees_core.outside;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import tannyjung.tanshugetrees_core.Core;
import tannyjung.tanshugetrees_core.game.GameUtils;

public class TXTFunction {

	public static void run (LevelAccessor level_accessor, ServerLevel level_server, BlockPos pos, String path, boolean randomly) {

        RandomSource random = null;

        if (randomly == true) {

            random = RandomSource.create();

        } else {

            random = RandomSource.create(level_server.getSeed() ^ ((pos.getX() * 341873128712L) + pos.getY() + (pos.getZ() * 132897987541L)));

        }

        boolean chunk_loaded = level_server.isPositionEntityTicking(pos) == true;
        StringBuilder export_command = new StringBuilder();
        boolean run_test = false;
        boolean run_test_result = true;
        boolean run_skip = false;
        boolean run_break = false;

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
        BlockPos pos_convert = null;
        String variable_text = "";
        BlockState variable_block = Blocks.AIR.defaultBlockState();

        for (String read_all : CacheManager.getFunction(path)) {

            {

                if (read_all.isEmpty() == false) {

                    if (read_all.startsWith("# ") == false) {

                        if (read_all.equals("[") == true || read_all.equals("]") == true) {

                            run_test = false;
                            run_test_result = true;
                            run_skip = false;
                            run_break = false;

                        } else if (read_all.startsWith("-") == true) {

                            run_test = false;
                            run_test_result = true;
                            run_skip = false;

                        } else {

                            if (run_break == false) {

                                {

                                    if (read_all.startsWith("debug = ") == true) {

                                        {

                                            try {

                                                get = read_all.substring("debug = ".length()).split(" \\| ");
                                                chance = Double.parseDouble(get[0]);
                                                variable_text = get[1];

                                            } catch (Exception ignored) {

                                                return;

                                            }

                                            if (random.nextDouble() < chance) {

                                                Core.logger.info("{}   |   Testing > {}   |   Result > {}   |   Skip > {}   |   Break > {}", variable_text, run_test, run_test_result, run_skip, run_break);

                                            }

                                        }

                                    } else {

                                        if (read_all.equals("if") == true) {

                                            {

                                                if (run_test == false) {

                                                    run_test = true;
                                                    run_test_result = true;
                                                    run_skip = false;

                                                } else {

                                                    if (run_skip == false) {

                                                        if (run_test_result == false) {

                                                            run_test_result = true;

                                                        } else {

                                                            run_skip = true;

                                                        }

                                                    }

                                                }

                                            }

                                        } else if (read_all.equals("else") == true) {

                                            {

                                                run_test_result = !run_test_result;

                                            }

                                        } else if (read_all.equals("run") == true) {

                                            {

                                                run_test = false;
                                                run_skip = run_test_result == false;

                                            }

                                        } else if (read_all.equals("break") == true) {

                                            {

                                                if (run_skip == false) {

                                                    run_break = true;

                                                }

                                            }

                                        } else if (read_all.equals("return") == true) {

                                            {

                                                if (run_skip == false) {

                                                    return;

                                                }

                                            }

                                        } else {

                                            if (run_skip == false) {

                                                if (run_test == true) {

                                                    // Tests
                                                    {

                                                        if (read_all.startsWith("chance = ") == true) {

                                                            {

                                                                try {

                                                                    get = read_all.substring("chance = ".length()).split(" \\| ");
                                                                    chance = Double.parseDouble(get[0]);

                                                                } catch (Exception ignored) {

                                                                    return;

                                                                }

                                                                if (random.nextDouble() < chance) {

                                                                    continue;

                                                                }

                                                            }

                                                        } else if (read_all.startsWith("biome = ") == true) {

                                                            {

                                                                try {

                                                                    get = read_all.substring("biome = ".length()).split(" \\| ");
                                                                    offset_pos = get[0].split("/");
                                                                    offset_posX = Integer.parseInt(offset_pos[0]);
                                                                    offset_posY = Integer.parseInt(offset_pos[1]);
                                                                    offset_posZ = Integer.parseInt(offset_pos[2]);
                                                                    variable_text = get[1];

                                                                } catch (Exception ignored) {

                                                                    return;

                                                                }

                                                                pos_convert = pos.offset(offset_posX, offset_posY, offset_posZ);

                                                                if (GameUtils.Environment.test(GameUtils.Environment.getAt(level_accessor, pos_convert), variable_text) == true) {

                                                                    continue;

                                                                }

                                                            }

                                                        } else if (read_all.startsWith("block = ") == true) {

                                                            {

                                                                try {

                                                                    get = read_all.substring("block = ".length()).split(" \\| ");
                                                                    offset_pos = get[0].split("/");
                                                                    offset_posX = Integer.parseInt(offset_pos[0]);
                                                                    offset_posY = Integer.parseInt(offset_pos[1]);
                                                                    offset_posZ = Integer.parseInt(offset_pos[2]);
                                                                    variable_text = get[1];

                                                                } catch (Exception ignored) {

                                                                    return;

                                                                }

                                                                pos_convert = pos.offset(offset_posX, offset_posY, offset_posZ);

                                                                if (GameUtils.Space.testChunkStatus(level_accessor, new ChunkPos(pos_convert), "surface") == true) {

                                                                    if (GameUtils.Tile.test(level_accessor.getBlockState(pos_convert), variable_text) == true) {

                                                                        continue;

                                                                    }

                                                                }

                                                            }

                                                        }

                                                        run_test_result = false;

                                                    }

                                                } else {

                                                    // Run
                                                    {

                                                        if (read_all.startsWith("block = ") == true) {

                                                            {

                                                                try {

                                                                    get = read_all.substring("block = ".length()).split(" \\| ");
                                                                    chance = Double.parseDouble(get[0]);
                                                                    variable_block = GameUtils.Tile.fromText(get[3]);
                                                                    variable_text = get[4];

                                                                } catch (Exception ignored) {

                                                                    return;

                                                                }

                                                                if (random.nextDouble() < chance) {

                                                                    if (variable_block != Blocks.AIR.defaultBlockState()) {

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

                                                                                    pos_convert = pos.offset(offset_posX + testX, offset_posY + testY, offset_posZ + testZ);

                                                                                    if (level_accessor.hasChunk(pos_convert.getX() >> 4, pos_convert.getZ() >> 4) == true) {

                                                                                        if (GameUtils.Tile.test(level_accessor.getBlockState(pos_convert), variable_text) == false) {

                                                                                            continue;

                                                                                        }

                                                                                        GameUtils.Tile.set(level_accessor, pos_convert, variable_block, false);

                                                                                    }

                                                                                }

                                                                            }

                                                                        }

                                                                    }

                                                                }

                                                            }

                                                        } else if (read_all.startsWith("feature = ") == true) {

                                                            {

                                                                try {

                                                                    get = read_all.substring("feature = ".length()).split(" \\| ");
                                                                    chance = Double.parseDouble(get[0]);
                                                                    offset_pos = get[1].split("/");
                                                                    offset_posX = Integer.parseInt(offset_pos[0]);
                                                                    offset_posY = Integer.parseInt(offset_pos[1]);
                                                                    offset_posZ = Integer.parseInt(offset_pos[2]);
                                                                    variable_text = get[2];

                                                                } catch (Exception ignored) {

                                                                    return;

                                                                }

                                                                if (random.nextDouble() < chance) {

                                                                    pos_convert = pos.offset(offset_posX, offset_posY, offset_posZ);

                                                                    try {

                                                                        GameUtils.Space.placeFeature(level_accessor, pos_convert, variable_text);

                                                                    } catch (Exception ignored) {

                                                                        return;

                                                                    }

                                                                }

                                                            }

                                                        } else if (read_all.startsWith("function = ") == true) {

                                                            {

                                                                try {

                                                                    get = read_all.substring("function = ".length()).split(" \\| ");
                                                                    chance = Double.parseDouble(get[0]);
                                                                    offset_pos = get[1].split("/");
                                                                    offset_posX = Integer.parseInt(offset_pos[0]);
                                                                    offset_posY = Integer.parseInt(offset_pos[1]);
                                                                    offset_posZ = Integer.parseInt(offset_pos[2]);
                                                                    variable_text = get[2];

                                                                } catch (Exception ignored) {

                                                                    return;

                                                                }

                                                                if (random.nextDouble() < chance) {

                                                                    pos_convert = pos.offset(offset_posX, offset_posY, offset_posZ);
                                                                    TXTFunction.run(level_accessor, level_server, pos_convert, variable_text, false);

                                                                }

                                                            }

                                                        } else if (read_all.startsWith("command = ") == true) {

                                                            {

                                                                try {

                                                                    get = read_all.substring("command = ".length()).split(" \\| ");
                                                                    chance = Double.parseDouble(get[0]);
                                                                    variable_text = get[1];

                                                                } catch (Exception ignored) {

                                                                    return;

                                                                }

                                                                if (random.nextDouble() < chance) {

                                                                    if (chunk_loaded == true) {

                                                                        String variable_text_final = variable_text;

                                                                        level_server.getServer().execute(() -> {

                                                                            GameUtils.Command.run(level_server, pos.getCenter(), variable_text_final);

                                                                        });

                                                                    } else {

                                                                        export_command.append("|").append(variable_text);

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

                            }

                        }

                    }

                }

            }

        }

        // Export Command
        {

            if (export_command.isEmpty() == false) {

                String command = export_command.toString();

                if (command.startsWith("|") == true) {

                    command = command.substring(1);

                }

                String command_final = command.replace("'", "*").replace("\"", "$");
                GameUtils.Mob.summonWorldGen(level_server, pos.getCenter(), "marker", "Delayed Command", "tanshugetrees-delayed_command", "{ForgeData:{" + Core.mod_id + ":{command:\"" + command_final + "\"}}}");
                
            }

        }

    }

    public static void runDelayedCommand (ServerLevel level_server, Entity entity) {

        if (level_server.isPositionEntityTicking(entity.blockPosition()) == true) {

            for (String command : GameUtils.Data.getEntityText(entity, "command").replace("*", "'").replace("$", "\"").split("\\|")) {

                level_server.getServer().execute(() -> {

                    GameUtils.Command.run(level_server, entity.position(), command);

                });

            }

            entity.discard();

        }

    }

}