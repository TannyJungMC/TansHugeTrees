package tannyjung.core.game;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import tannyjung.core.OutsideUtils;
import tannyjung.tanshugetrees.server.Cache;

public class TXTFunction {

	public static void start (LevelAccessor level_accessor, ServerLevel level_server, RandomSource random, int posX, int posY, int posZ, String path) {

        WorldGenLevel world_gen = (WorldGenLevel) level_accessor;
        boolean chunk_loaded = level_server.isPositionEntityTicking(new BlockPos(posX, posY, posZ));

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
        BlockPos pos = null;

        String variable_text = "";
        boolean variable_logic = false;
        BlockState variable_block = Blocks.AIR.defaultBlockState();
        StringBuilder export_command = new StringBuilder();

        for (String read_all : Cache.getFunction(path)) {

            {

                if (read_all.equals("") == false) {

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

                                            get = read_all.replace("debug = ", "").split(" \\| ");
                                            chance = Double.parseDouble(get[0]);
                                            variable_text = get[1];

                                        } catch (Exception ignored) {

                                            return;

                                        }

                                        if (random.nextDouble() < chance) {

                                            OutsideUtils.logger.info(variable_text + "   |   Testing > " + run_test + "   |   Result > " + run_test_result + "   |   Skip > " + run_skip + "   |   Break > " + run_break);

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

                                                                get = read_all.replace("chance = ", "").split(" \\| ");
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

                                                                get = read_all.replace("biome = ", "").split(" \\| ");
                                                                offset_pos = get[0].split("/");
                                                                offset_posX = Integer.parseInt(offset_pos[0]);
                                                                offset_posY = Integer.parseInt(offset_pos[1]);
                                                                offset_posZ = Integer.parseInt(offset_pos[2]);
                                                                variable_text = get[1];

                                                            } catch (Exception ignored) {

                                                                return;

                                                            }

                                                            if (Utils.misc.testCustomBiome(level_accessor.getBiome(new BlockPos(posX + offset_posX, posY + offset_posY, posZ + offset_posZ)), variable_text) == true) {

                                                                continue;

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

                                                            if (Utils.misc.testCustomBlock(level_accessor.getBlockState(new BlockPos(posX + offset_posX, posY + offset_posY, posZ + offset_posZ)), variable_text) == true) {

                                                                continue;

                                                            }

                                                        }

                                                    }

                                                    run_test_result = false;

                                                }

                                            } else {

                                                // Functions
                                                {

                                                    if (read_all.startsWith("block = ") == true) {

                                                        {

                                                            try {

                                                                get = read_all.replace("block = ", "").split(" \\| ");
                                                                chance = Double.parseDouble(get[0]);
                                                                variable_block = Utils.block.fromText(get[3]);
                                                                variable_logic = Boolean.parseBoolean(get[4]);

                                                            } catch (Exception ignored) {

                                                                return;

                                                            }

                                                            if (random.nextDouble() < chance && variable_block != Blocks.AIR.defaultBlockState()) {

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

                                                    } else if (read_all.startsWith("block = ") == true) {

                                                        {

                                                            try {

                                                                get = read_all.replace("block = ", "").split(" \\| ");
                                                                chance = Double.parseDouble(get[0]);
                                                                variable_block = Utils.block.fromText(get[3]);
                                                                variable_logic = Boolean.parseBoolean(get[4]);

                                                            } catch (Exception ignored) {

                                                                return;

                                                            }

                                                            if (random.nextDouble() < chance && variable_block != Blocks.AIR.defaultBlockState()) {

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

                                                    } else if (read_all.startsWith("feature = ") == true) {

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

                                                            if (random.nextDouble() < chance) {

                                                                try {

                                                                    pos = new BlockPos(posX + offset_posX, posY + offset_posY, posZ + offset_posZ);
                                                                    world_gen.registryAccess().registryOrThrow(Registries.CONFIGURED_FEATURE).getHolderOrThrow(FeatureUtils.createKey(variable_text)).value().place(world_gen, world_gen.getLevel().getChunkSource().getGenerator(), world_gen.getRandom(), pos);

                                                                } catch (Exception ignored) {

                                                                    return;

                                                                }

                                                            }

                                                        }

                                                    } else if (read_all.startsWith("function = ") == true) {

                                                        {

                                                            try {

                                                                get = read_all.replace("function = ", "").split(" \\| ");
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

                                                                TXTFunction.start(level_accessor, level_server, random, posX + offset_posX, posY + offset_posY, posZ + offset_posZ, variable_text);

                                                            }

                                                        }

                                                    } else if (read_all.startsWith("command = ") == true) {

                                                        {

                                                            try {

                                                                get = read_all.replace("command = ", "").split(" \\| ");
                                                                chance = Double.parseDouble(get[0]);
                                                                variable_text = get[1];

                                                            } catch (Exception ignored) {

                                                                return;

                                                            }

                                                            if (random.nextDouble() < chance) {

                                                                if (chunk_loaded == true) {

                                                                    Utils.command.run(level_server, posX, posY, posZ, variable_text);

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

        // Export Command
        {

            if (export_command.isEmpty() == false) {

                String command = export_command.toString();

                if (command.startsWith("|") == true) {

                    command = command.substring(1);

                }

                command = command.replace("'", "*").replace("\"", "$");
                Utils.command.run(level_server, posX + 0.5, posY + 0.5, posZ + 0.5, Utils.command.summonEntity("marker", "TANNYJUNG / TANNYJUNG-delayed_command", "Delayed Command", "ForgeData:{command:\"" + command + "\"}"));

            }

        }

	}

    public static void runDelayedCommand (Entity entity) {

        LevelAccessor level_accessor = entity.level();

        if (level_accessor instanceof ServerLevel level_server) {

            if (level_server.isPositionEntityTicking(entity.blockPosition()) == true) {

                for (String command : NBTManager.Entity.getText(entity, "command").replace("*", "'").replace("$", "\"").split("\\|")) {

                    Utils.command.run(level_server, entity.getBlockX(), entity.getBlockY(), entity.getBlockZ(), command);


                }

                Utils.command.runEntity(entity, "kill @s");

            }

        }

    }

}