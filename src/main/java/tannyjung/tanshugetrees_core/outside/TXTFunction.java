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

import java.util.ArrayList;
import java.util.List;

public class TXTFunction {

    public static List<Entity> list_delayed_command = new ArrayList<>();

	public static void run (LevelAccessor level_accessor, ServerLevel level_server, BlockPos pos, String path, boolean randomly) {

        boolean chunk_loaded = level_server.isPositionEntityTicking(pos) == true;
        RandomSource random = null;

        if (randomly == true) {

            random = RandomSource.create();

        } else {

            random = RandomSource.create(level_accessor.getServer().overworld().getSeed() ^ ((pos.getX() * 341873128712L) + (pos.getZ() * 132897987541L) + pos.getY()));

        }
        
        StringBuilder export_command = new StringBuilder();
        boolean run_test = false;
        boolean run_test_result = true;
        boolean run_skip = false;
        boolean run_break = false;

        String[] split = null;
        double chance = 0.0;
        String[] offset_pos = null;
        int offset_posX = 0;
        int offset_posY = 0;
        int offset_posZ = 0;
        String[] min_max = null;
        int minX = 0;
        int minY = 0;
        int minZ = 0;
        int maxX = 0;
        int maxY = 0;
        int maxZ = 0;
        BlockPos pos_convert = null;
        BlockState block = null;
        String variable_text = "";

        for (String scan : CacheManager.getFunction(path)) {

            {

                if (scan.isEmpty() == false) {

                    if (scan.startsWith("# ") == false) {

                        if (scan.equals("[") == true || scan.equals("]") == true) {

                            run_test = false;
                            run_test_result = true;
                            run_skip = false;
                            run_break = false;

                        } else if (scan.startsWith("-") == true) {

                            run_test = false;
                            run_test_result = true;
                            run_skip = false;

                        } else {

                            if (run_break == false) {

                                {

                                    if (scan.startsWith("debug = ") == true) {

                                        {

                                            try {

                                                split = scan.substring("debug = ".length()).split(" \\| ");
                                                chance = Double.parseDouble(split[0]);
                                                variable_text = split[1];

                                            } catch (Exception ignored) {

                                                return;

                                            }

                                            if (random.nextDouble() < chance) {

                                                Core.logger.info("{}   |   Testing > {}   |   Result > {}   |   Skip > {}   |   Break > {}", variable_text, run_test, run_test_result, run_skip, run_break);

                                            }

                                        }

                                    } else {

                                        if (scan.equals("if") == true) {

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

                                        } else if (scan.equals("else") == true) {

                                            {

                                                run_test_result = !run_test_result;

                                            }

                                        } else if (scan.equals("run") == true) {

                                            {

                                                run_test = false;
                                                run_skip = run_test_result == false;

                                            }

                                        } else if (scan.equals("break") == true) {

                                            {

                                                if (run_skip == false) {

                                                    run_break = true;

                                                }

                                            }

                                        } else if (scan.equals("return") == true) {

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

                                                        if (scan.startsWith("chance = ") == true) {

                                                            {

                                                                try {

                                                                    split = scan.substring("chance = ".length()).split(" \\| ");
                                                                    chance = Double.parseDouble(split[0]);

                                                                } catch (Exception ignored) {

                                                                    return;

                                                                }

                                                                if (random.nextDouble() < chance) {

                                                                    continue;

                                                                }

                                                            }

                                                        } else if (scan.startsWith("biome = ") == true) {

                                                            {

                                                                try {

                                                                    split = scan.substring("biome = ".length()).split(" \\| ");

                                                                    offset_pos = split[0].split("/");
                                                                    offset_posX = Integer.parseInt(offset_pos[0]);
                                                                    offset_posY = Integer.parseInt(offset_pos[1]);
                                                                    offset_posZ = Integer.parseInt(offset_pos[2]);

                                                                    variable_text = split[1];

                                                                } catch (Exception ignored) {

                                                                    return;

                                                                }

                                                                pos_convert = pos.offset(offset_posX, offset_posY, offset_posZ);

                                                                if (GameUtils.Environment.test(GameUtils.Environment.getAt(level_accessor, pos_convert), variable_text) == true) {

                                                                    continue;

                                                                }

                                                            }

                                                        } else if (scan.startsWith("block = ") == true) {

                                                            {

                                                                try {

                                                                    split = scan.substring("block = ".length()).split(" \\| ");

                                                                    offset_pos = split[0].split("/");
                                                                    offset_posX = Integer.parseInt(offset_pos[0]);
                                                                    offset_posY = Integer.parseInt(offset_pos[1]);
                                                                    offset_posZ = Integer.parseInt(offset_pos[2]);

                                                                    variable_text = split[1];

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

                                                        if (scan.startsWith("block = ") == true) {

                                                            {

                                                                try {

                                                                    split = scan.substring("block = ".length()).split(" \\| ");
                                                                    chance = Double.parseDouble(split[0]);

                                                                    offset_pos = split[1].split("/");
                                                                    offset_posX = Integer.parseInt(offset_pos[0]);
                                                                    offset_posY = Integer.parseInt(offset_pos[1]);
                                                                    offset_posZ = Integer.parseInt(offset_pos[2]);

                                                                    min_max = split[2].split("/");
                                                                    minX = Integer.parseInt(min_max[0]);
                                                                    minY = Integer.parseInt(min_max[1]);
                                                                    minZ = Integer.parseInt(min_max[2]);
                                                                    maxX = Integer.parseInt(min_max[3]);
                                                                    maxY = Integer.parseInt(min_max[4]);
                                                                    maxZ = Integer.parseInt(min_max[5]);

                                                                    block = GameUtils.Tile.fromText(split[3]);
                                                                    variable_text = split[4];

                                                                } catch (Exception ignored) {

                                                                    return;

                                                                }

                                                                if (random.nextDouble() < chance) {

                                                                    if (block != Blocks.AIR.defaultBlockState()) {

                                                                        for (int testX = minX; testX <= maxX; testX++) {

                                                                            for (int testY = minY; testY <= maxY; testY++) {

                                                                                for (int testZ = minZ; testZ <= maxZ; testZ++) {

                                                                                    pos_convert = pos.offset(offset_posX + testX, offset_posY + testY, offset_posZ + testZ);

                                                                                    if (GameUtils.Tile.test(level_accessor.getBlockState(pos_convert), variable_text) == false) {

                                                                                        continue;

                                                                                    }

                                                                                    GameUtils.Tile.set(level_accessor, pos_convert, block, false);

                                                                                }

                                                                            }

                                                                        }

                                                                    }

                                                                }

                                                            }

                                                        } else if (scan.startsWith("feature = ") == true) {

                                                            {

                                                                try {

                                                                    split = scan.substring("feature = ".length()).split(" \\| ");
                                                                    chance = Double.parseDouble(split[0]);

                                                                    offset_pos = split[1].split("/");
                                                                    offset_posX = Integer.parseInt(offset_pos[0]);
                                                                    offset_posY = Integer.parseInt(offset_pos[1]);
                                                                    offset_posZ = Integer.parseInt(offset_pos[2]);

                                                                    variable_text = split[2];

                                                                } catch (Exception ignored) {

                                                                    return;

                                                                }

                                                                if (random.nextDouble() < chance) {

                                                                    pos_convert = pos.offset(offset_posX, offset_posY, offset_posZ);
                                                                    GameUtils.Space.placeFeature(level_accessor, pos_convert, variable_text);

                                                                }

                                                            }

                                                        } else if (scan.startsWith("function = ") == true) {

                                                            {

                                                                try {

                                                                    split = scan.substring("function = ".length()).split(" \\| ");
                                                                    chance = Double.parseDouble(split[0]);

                                                                    offset_pos = split[1].split("/");
                                                                    offset_posX = Integer.parseInt(offset_pos[0]);
                                                                    offset_posY = Integer.parseInt(offset_pos[1]);
                                                                    offset_posZ = Integer.parseInt(offset_pos[2]);

                                                                    variable_text = split[2];

                                                                } catch (Exception ignored) {

                                                                    return;

                                                                }

                                                                if (random.nextDouble() < chance) {

                                                                    pos_convert = pos.offset(offset_posX, offset_posY, offset_posZ);
                                                                    TXTFunction.run(level_accessor, level_server, pos_convert, variable_text, false);

                                                                }

                                                            }

                                                        } else if (scan.startsWith("command = ") == true) {

                                                            {

                                                                try {

                                                                    split = scan.substring("command = ".length()).split(" \\| ");
                                                                    chance = Double.parseDouble(split[0]);
                                                                    variable_text = split[1];

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
                GameUtils.Mob.summonWorldGen(level_server, pos.getCenter(), "marker", "Delayed Command", "TANNYJUNG-delayed_command", "{NeoForgeData:{" + Core.mod_id + ":{command:\"" + command_final + "\"}}}");
                
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

    public static void loop (ServerLevel level_server) {

        list_delayed_command = GameUtils.Mob.getAtEverywhere(level_server, "minecraft:marker", "TANNYJUNG-delayed_command");

        for (Entity entity : list_delayed_command) {

            TXTFunction.runDelayedCommand(level_server, entity);

        }

    }

}