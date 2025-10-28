package tannyjung.tanshugetrees_handcode.systems.living_tree_mechanics;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.level.levelgen.Heightmap;
import tannyjung.core.OutsideUtils;
import tannyjung.tanshugetrees.network.TanshugetreesModVariables;
import tannyjung.tanshugetrees_handcode.Handcode;
import tannyjung.core.GameUtils;
import tannyjung.tanshugetrees_handcode.config.ConfigMain;
import tannyjung.tanshugetrees_handcode.systems.Cache;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LivingTreeMechanics {

    public static void start (Entity entity) {

        if (ConfigMain.developer_mode == true) {

            GameUtils.command.runEntity(entity, "particle flash ~ ~ ~ 0 0 0 0 1 force");
            GameUtils.command.runEntity(entity, "particle totem_of_undying ~ ~100 ~ 0 25 0 0 300 force");

        }

        LevelAccessor level_accessor = entity.level();
        ServerLevel level_server = (ServerLevel) entity.level();
        Map<String, BlockState> map_block = new HashMap<>();
        int[] leaves_type = new int[2];
        boolean can_leaves_decay = false;
        boolean can_leaves_drop = false;
        boolean can_leaves_regrow = false;

        // Read Settings
        {

            File file = new File(Handcode.directory_config + "/#dev/custom_packs_organized/presets/" + GameUtils.nbt.entity.getText(entity, "tree_settings") + "_settings.txt");

            if (file.exists() == true && file.isDirectory() == false) {

                String get_short = "";
                String get = "";

                {

                    try { BufferedReader buffered_reader = new BufferedReader(new FileReader(file), 65536); String read_all = ""; while ((read_all = buffered_reader.readLine()) != null) {

                        {

                            if (read_all.startsWith("can_leaves_decay = ") == true) {

                                can_leaves_decay = Boolean.parseBoolean(read_all.replace("can_leaves_decay = ", ""));

                            } else if (read_all.startsWith("can_leaves_drop = ") == true) {

                                can_leaves_drop = Boolean.parseBoolean(read_all.replace("can_leaves_drop = ", ""));

                            } else if (read_all.startsWith("can_leaves_regrow = ") == true) {

                                can_leaves_regrow = Boolean.parseBoolean(read_all.replace("can_leaves_regrow = ", ""));

                            } else if (read_all.startsWith("Block ") == true) {

                                {

                                    get_short = read_all.substring(("Block ### ").length(), ("Block ### ####").length());
                                    get = read_all.substring(("Block ### #### = ").length());

                                    if (get.endsWith(" keep") == true) {

                                        get = get.substring(0, get.length() - (" keep").length());

                                    }

                                    map_block.put(get_short, GameUtils.block.fromText(get));

                                    if (get_short.startsWith("120") == true) {

                                        // Leaves Types
                                        {

                                            if (get.endsWith("]") == true) {

                                                get = get.substring(0, get.indexOf("["));

                                            }

                                            int number = Integer.parseInt(get_short.substring(3)) - 1;

                                            if (ConfigMain.deciduous_leaves_list.contains(get) == true) {

                                                leaves_type[number] = 1;

                                            } else if (ConfigMain.coniferous_leaves_list.contains(get) == true) {

                                                leaves_type[number] = 2;

                                            }

                                        }

                                    }

                                }

                            }

                        }

                    } buffered_reader.close(); } catch (Exception exception) { OutsideUtils.exception(new Exception(), exception); }

                }

            } else {

                return;

            }

        }

        String[] file_path_data = GameUtils.nbt.entity.getText(entity, "file").split("\\|");
        String path_storage = file_path_data[0];
        String chosen = file_path_data[1];
        File file = new File(Handcode.directory_config + "/custom_packs/" + path_storage.replace("/", "/presets/") + "/storage/" + chosen);

        if (file.exists() == true && file.isDirectory() == false) {

            BlockPos center_pos = new BlockPos(entity.getBlockX(), entity.getBlockY(), entity.getBlockZ());
            boolean have_center_block = level_accessor.getBlockState(center_pos).isAir() == false;
            int rotation = (int) GameUtils.nbt.entity.getNumber(entity, "rotation");
            boolean mirrored = GameUtils.nbt.entity.getLogic(entity, "mirrored");
            int biome_type = 0;

            // Biome Type Test
            {

                if (GameUtils.biome.isTaggedAs(level_accessor.getBiome(center_pos), "forge:is_snowy") == true) {

                    biome_type = 1;

                } else if (GameUtils.biome.isTaggedAs(level_accessor.getBiome(center_pos), "tanshugetrees:tropical_biomes") == true) {

                    biome_type = 2;

                }

            }

            int process = 0;
            int loop = 0;
            String type = "";
            int posX = 0;
            int posY = 0;
            int posZ = 0;
            int[] pos_converted = new int[0];
            String get = "";
            BlockPos pre_pos = new BlockPos(0, 0, 0);
            BlockState pre_block = Blocks.AIR.defaultBlockState();
            BlockPos pos = new BlockPos(0, 0, 0);
            BlockState block = Blocks.AIR.defaultBlockState();
            String[] pre_block_data = new String[0];

            for (short read_all : Cache.tree_shape(path_storage + "/" + chosen)) {

                // Loop and Get Data
                {

                    loop = loop + 1;

                    if (loop > 4) {

                        loop = 1;

                    }

                    if (loop == 1) {

                        type = String.valueOf(read_all);

                    } else if (loop == 2) {

                        posX = read_all;

                    } else if (loop == 3) {

                        posY = read_all;

                    } else {

                        posZ = read_all;

                    }

                }

                if (loop == 4) {

                    process = process + 1;

                    // Skipping Conditions
                    {

                        // Out of Save
                        {

                            if (process < GameUtils.nbt.entity.getNumber(entity, "process_save")) {

                                continue;

                            }

                        }

                        // Out of Process Limit
                        {

                            if (ConfigMain.living_tree_mechanics_process_limit > 0) {

                                if (GameUtils.nbt.entity.getNumber(entity, "process_save") + ConfigMain.living_tree_mechanics_process_limit <= process) {

                                    GameUtils.nbt.entity.setNumber(entity, "process_save", process);
                                    return;

                                }

                            }

                        }

                    }

                    if (type.startsWith("1") == true) {

                        if (type.startsWith("120") == false) {

                            GameUtils.nbt.entity.setText(entity, "pre_block", type + "/" + posX + "/" + posY + "/" + posZ);

                        } else {

                            // Get Previous Block Data
                            {

                                pre_block_data = GameUtils.nbt.entity.getText(entity, "pre_block").split("/");
                                pos_converted = GameUtils.outside.posRotationMirrored(Integer.parseInt(pre_block_data[1]), Integer.parseInt(pre_block_data[3]), rotation, mirrored);
                                pre_pos = new BlockPos(entity.getBlockX() + pos_converted[0], entity.getBlockY() + Integer.parseInt(pre_block_data[2]), entity.getBlockZ() + pos_converted[1]);

                                // Only Loaded Chunks
                                {

                                    if (level_server.isLoaded(pre_pos) == false) {

                                        return;

                                    }

                                }

                                pre_block = map_block.getOrDefault(pre_block_data[0], Blocks.AIR.defaultBlockState());

                            }

                            pos_converted = GameUtils.outside.posRotationMirrored(posX, posZ, rotation, mirrored);
                            pos = new BlockPos(entity.getBlockX() + pos_converted[0], entity.getBlockY() + posY, entity.getBlockZ() + pos_converted[1]);
                            block = map_block.get(type);

                            // Only Loaded Chunks
                            {

                                if (level_server.isLoaded(pos) == false) {

                                    return;

                                }

                            }

                            if (level_accessor.getBlockState(pre_pos).getBlock() != pre_block.getBlock()) {

                                // Missing Twig
                                {

                                    if (can_leaves_decay == true) {

                                        if (level_accessor.getBlockState(pos).getBlock() == block.getBlock()) {

                                            block = GameUtils.block.propertyBooleanSet(block, "persistent", false);
                                            level_accessor.setBlock(pos, block, 2);

                                        }

                                    }

                                }

                            } else {

                                if (can_leaves_drop == true || can_leaves_regrow == true) {

                                    updating(level_accessor, level_server, entity, pos, map_block, block, leaves_type[Integer.parseInt(type.substring(3)) - 1], biome_type, have_center_block, can_leaves_drop, can_leaves_regrow);

                                }

                            }

                        }

                    }

                }

            }

            // At the end of the file
            {

                GameUtils.nbt.entity.setNumber(entity, "process_save", 0);

                if (GameUtils.nbt.entity.getLogic(entity, "dead_tree") == true) {

                    level_server.getServer().execute(() -> {

                        GameUtils.command.runEntity(entity, "kill @s");

                    });

                } else if (GameUtils.nbt.entity.getLogic(entity, "still_alive") == true) {

                    GameUtils.nbt.entity.setLogic(entity, "still_alive", false);
                    GameUtils.nbt.entity.setLogic(entity, "have_leaves", false);

                } else if (GameUtils.nbt.entity.getLogic(entity, "have_leaves") == false) {

                    if (leaves_type[0] == 1 || leaves_type[1] == 1) {

                        String current_season = TanshugetreesModVariables.MapVariables.get(level_accessor).season;

                        if (current_season.equals("Spring") == true || current_season.equals("Autumn") == true || current_season.equals("Winter") == true) {

                            GameUtils.nbt.entity.setLogic(entity, "dormancy", true);

                        }

                    }

                } else {

                    if (Math.random() < 0.1) {

                        GameUtils.nbt.entity.setLogic(entity, "dead_tree", true);

                    }

                }

            }

        }

    }

    private static void updating (LevelAccessor level_accessor, ServerLevel level_server, Entity entity, BlockPos pos, Map<String, BlockState> map_block, BlockState block, int leaves_type, int biome_type, boolean have_center_block, boolean can_leaves_drop, boolean can_leaves_regrow) {

        boolean is_leaves = level_accessor.getBlockState(pos).getBlock() == block.getBlock();
        String current_season = TanshugetreesModVariables.MapVariables.get(level_accessor).season;
        boolean straighten = false;
        boolean can_pos_photosynthesis = false;

        // Leaves Straighten Test
        {

            if ((GameUtils.nbt.entity.getNumber(entity, "straighten_highestX") != pos.getX() || GameUtils.nbt.entity.getNumber(entity, "straighten_highestY") < pos.getY() || GameUtils.nbt.entity.getNumber(entity, "straighten_highestZ") != pos.getZ())) {

                GameUtils.nbt.entity.setNumber(entity, "straighten_highestX", pos.getX());
                GameUtils.nbt.entity.setNumber(entity, "straighten_highestY", pos.getY());
                GameUtils.nbt.entity.setNumber(entity, "straighten_highestZ", pos.getZ());

            } else {

                straighten = true;

            }

        }

        // Light Level Detection
        {

            if (ConfigMain.leaf_light_level_detection <= level_accessor.getBrightness(LightLayer.SKY, pos) + 1) {

                can_pos_photosynthesis = true;

            }

        }

        if (is_leaves == true || have_center_block == false) {

            // Leaf Drop
            {

                if (can_leaves_drop == true) {

                    double chance = 0.0;

                    if (straighten == true) {

                        BlockState test = level_accessor.getBlockState(new BlockPos(pos.getX(), (int) GameUtils.nbt.entity.getNumber(entity, "straighten_highestY"), pos.getZ()));

                        if (map_block.get("1201").getBlock() != test.getBlock() && map_block.get("1202").getBlock() != test.getBlock()) {

                            chance = 1.0;

                        }

                    } else if (can_pos_photosynthesis == false) {

                        chance = ConfigMain.leaf_light_level_detection_drop_chance;

                    } else {

                        if (leaves_type == 1) {

                            if (biome_type == 0) {

                                // By Seasons
                                {

                                    chance = switch (current_season) {
                                        case "Spring" -> ConfigMain.leaf_drop_chance_spring;
                                        case "Summer" -> ConfigMain.leaf_drop_chance_summer;
                                        case "Autumn" -> ConfigMain.leaf_drop_chance_autumn;
                                        case "Winter" -> ConfigMain.leaf_drop_chance_winter;
                                        default -> chance;
                                    };

                                }

                            } else if (biome_type == 1) {

                                chance = ConfigMain.leaf_drop_chance_winter;

                            } else if (biome_type == 2) {

                                chance = ConfigMain.leaf_drop_chance_summer;

                            }

                        } else if (leaves_type == 2) {

                            // Only drop coniferous leaves in summer
                            {

                                if (current_season.equals("Summer")) {

                                    chance = ConfigMain.leaf_drop_chance_coniferous;

                                }

                            }

                        } else {

                            chance = ConfigMain.leaf_drop_chance_summer;

                        }

                    }

                    if (Math.random() < chance) {

                        level_accessor.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);

                        if (Math.random() < ConfigMain.leaf_drop_animation_chance) {

                            // Animation
                            {

                                if (GameUtils.score.get(level_server, "TANSHUGETREES", "leaf_drop") < ConfigMain.leaf_drop_animation_count_limit) {

                                    // Don't create animation, if there's a block below.
                                    if (GameUtils.block.isTaggedAs(level_accessor.getBlockState(new BlockPos(pos.getX(), pos.getY() - 1, pos.getZ())), "tanshugetrees:passable_blocks") == true) {

                                        GameUtils.score.add(level_server, "TANSHUGETREES", "leaf_drop", 1);
                                        String command = GameUtils.entity.summonCommand("block_display", "TANSHUGETREES / TANSHUGETREES-leaf_drop", "Falling Leaf", "transformation:{left_rotation:[0f,0f,0f,1f],right_rotation:[0f,0f,0f,1f],translation:[0f,0f,0f],scale:[1.0f,1.0f,1.0f]},block_state:{Name:\"" + GameUtils.block.toTextID(block) + "\"},ForgeData:{block:\"" + GameUtils.block.toText(block) + "\"}");
                                        GameUtils.command.run(level_server, pos.getX(), pos.getY(), pos.getZ(), command);

                                    }

                                }

                            }

                        } else {

                            // No Animation
                            {

                                int height_motion = level_accessor.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, pos.getX(), pos.getZ());

                                if (height_motion != level_accessor.getMinBuildHeight() && height_motion < pos.getY()) {

                                    LeafLitter.start(level_accessor, pos.getX(), height_motion, pos.getZ(), block, false);

                                }

                            }

                        }

                    }

                }

            }

        } else if (level_accessor.getBlockState(pos).isAir() == true) {

            // Leaf Regrowth
            {

                if (can_leaves_regrow == true) {

                    double chance = 0.0;

                    if (straighten == true) {

                        // Cancel By Straighten (if no block above)
                        {

                            BlockState test = level_accessor.getBlockState(new BlockPos(pos.getX(), pos.getY() + 1, pos.getZ()));

                            if (map_block.get("1201").getBlock() != test.getBlock() && map_block.get("1202").getBlock() != test.getBlock()) {

                                return;

                            } else {

                                chance = 1.0;

                            }

                        }

                    } else if (can_pos_photosynthesis == true) {

                        if (leaves_type == 1) {

                            if (biome_type == 0) {

                                // By Seasons
                                {

                                    chance = switch (current_season) {
                                        case "Spring" -> ConfigMain.leaf_regrowth_chance_spring;
                                        case "Summer" -> ConfigMain.leaf_regrowth_chance_summer;
                                        case "Autumn" -> ConfigMain.leaf_regrowth_chance_autumn;
                                        case "Winter" -> ConfigMain.leaf_regrowth_chance_winter;
                                        default -> chance;
                                    };

                                }

                            } else if (biome_type == 1) {

                                chance = ConfigMain.leaf_regrowth_chance_winter;

                            } else if (biome_type == 2) {

                                chance = ConfigMain.leaf_regrowth_chance_summer;

                            }

                        } else if (leaves_type == 2) {

                            chance = ConfigMain.leaf_regrowth_chance_coniferous;

                        } else {

                            chance = ConfigMain.leaf_regrowth_chance_summer;

                        }

                    }

                    if (Math.random() < chance) {

                        GameUtils.nbt.entity.setLogic(entity, "dormancy", false);
                        block = GameUtils.block.propertyBooleanSet(block, "persistent", true);
                        level_accessor.setBlock(pos, block, 2);

                    }

                }

            }

        }

        // Leaf Litter Remover
        {

            if (Math.random() < ConfigMain.leaf_litter_remover_chance) {

                if (GameUtils.score.get(level_server, "TANSHUGETREES", "leaf_litter_remover") < ConfigMain.leaf_litter_remover_count_limit) {

                    GameUtils.score.add(level_server, "TANSHUGETREES", "leaf_litter_remover", 1);
                    String command = GameUtils.entity.summonCommand("marker", "TANSHUGETREES / TANSHUGETREES-leaf_litter_remover", "Leaf Litter Remover", "ForgeData:{block:\"" + GameUtils.block.toText(block) + "\"}");
                    GameUtils.command.run(level_server, pos.getX(), pos.getY(), pos.getZ(), command);

                }

            }

        }

        // Still Alive
        {

            if (GameUtils.nbt.entity.getLogic(entity, "still_alive") == false) {

                if (is_leaves == true) {

                    GameUtils.nbt.entity.setLogic(entity, "still_alive", true);
                    GameUtils.nbt.entity.setLogic(entity, "have_leaves", true);

                } else if (leaves_type == 1 && biome_type == 1) {

                    GameUtils.nbt.entity.setLogic(entity, "still_alive", true);

                } else if (GameUtils.nbt.entity.getLogic(entity, "dormancy") == true) {

                    GameUtils.nbt.entity.setLogic(entity, "still_alive", true);

                }

            }

        }

    }

}