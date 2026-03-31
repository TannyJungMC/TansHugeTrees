package tannyjung.tanshugetrees_handcode.systems.tree_generator;

import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import tannyjung.tanshugetrees_core.Core;
import tannyjung.tanshugetrees_core.outside.TXTFunction;
import tannyjung.tanshugetrees_core.game.GameUtils;
import tannyjung.tanshugetrees.network.TanshugetreesModVariables;
import tannyjung.tanshugetrees_handcode.Handcode;

import java.io.File;

public class TreeGenerator {

    public static void create (ServerLevel level_server, Entity entity, BlockPos pos, String path) {

        String name_pack = "";
        String name_tree = "";

        try {

            String[] split = path.split("/");
            name_pack = split[0];
            name_tree = split[1];

        } catch (Exception ignored) {



        }

        File file = new File(Core.path_config + "/custom_packs/" + name_pack + "/presets/" + name_tree + "/" + name_tree + ".txt");

        if (file.exists() == true) {

            GameUtils.Mob.summon(level_server, pos.getCenter(), "minecraft:marker", "Tree Generator", "TANSHUGETREES-tree_generator", GameUtils.Data.convertFileToForgeData(file.getPath()));
            GameUtils.Misc.sendChatMessage(level_server, "Summoned a tree generator at " + pos.getX() + " " + pos.getY() + " " + pos.getZ() + "  / gray | [?] / dark_gray / " + path + " (Extracted)");

        } else {

            file = new File(Core.path_config + "/dev/temporary/presets/" + name_pack + "/" + name_tree + "/" + name_tree + ".txt");

            if (file.exists() == true) {

                GameUtils.Misc.sendChatMessage(level_server, "Summoned a tree generator at " + pos.getX() + " " + pos.getY() + " " + pos.getZ() + "  / gray | [?] / dark_gray / " + path + " (Unextracted)");
                GameUtils.Mob.summon(level_server, pos.getCenter(), "minecraft:marker", "Tree Generator", "TANSHUGETREES-tree_generator", GameUtils.Data.convertFileToForgeData(file.getPath()));

            } else {

                GameUtils.Misc.sendChatMessagePrivate(entity, "Path Not Found / red");

            }

        }

    }

    public static void run (LevelAccessor level_accessor, Entity entity) {

        ServerLevel level_server = (ServerLevel) level_accessor;
        GameUtils.Misc.spawnParticle(level_server, entity.position(), 0, 0, 0, 0, 1, "minecraft:composter");

        if (GameUtils.Data.getEntityLogic(entity, "start") == false) {

            GameUtils.Data.setEntityLogic(entity, "start", true);
            beforeRunSystem(level_accessor, level_server, entity);

        } else {

            GameUtils.Data.addEntityNumber(entity, "tree_generator_speed_tick_test", 1);

            if (GameUtils.Data.getEntityNumber(entity, "tree_generator_speed_tick_test") >= GameUtils.Data.getEntityNumber(entity, "tree_generator_speed_tick")) {

                GameUtils.Data.setEntityNumber(entity, "tree_generator_speed_tick_test", 0);
                runSystem(level_accessor, level_server, entity);

            }

        }

    }

    private static void beforeRunSystem (LevelAccessor level_accessor, ServerLevel level_server, Entity entity) {

        GameUtils.Data.setEntityText(entity, "id", entity.getUUID().toString());
        entity.addTag("TANSHUGETREES-" + GameUtils.Data.getEntityText(entity, "id"));
        GameUtils.Data.setEntityText(entity, "gen_type", "taproot");
        GameUtils.Data.setEntityText(entity, "gen_step", "summon");
        GameUtils.Data.setEntityNumber(entity, "taproot_count", Mth.nextInt(RandomSource.create(), (int) GameUtils.Data.getEntityNumber(entity, "taproot_count_min"), (int) GameUtils.Data.getEntityNumber(entity, "taproot_count_max")));
        GameUtils.Data.setEntityNumber(entity, "trunk_count", Mth.nextInt(RandomSource.create(), (int) GameUtils.Data.getEntityNumber(entity, "trunk_count_min"), (int) GameUtils.Data.getEntityNumber(entity, "trunk_count_max")));

        if (GameUtils.Data.getEntityNumber(entity, "taproot_count") == 0) {

            GameUtils.Data.setEntityText(entity, "gen_type", "trunk");

        }

        if (TanshugetreesModVariables.MapVariables.get(level_accessor).shape_file_converter == false) {

            // Debug Mode
            {

                if (GameUtils.Data.getEntityLogic(entity, "debug_mode") == true) {

                    GameUtils.Data.setEntityText(entity, "taproot_outer", "purple_concrete");
                    GameUtils.Data.setEntityText(entity, "taproot_inner", "purple_terracotta");
                    GameUtils.Data.setEntityText(entity, "taproot_core", "purple_stained_glass");
                    GameUtils.Data.setEntityText(entity, "secondary_root_outer", "magenta_concrete");
                    GameUtils.Data.setEntityText(entity, "secondary_root_inner", "magenta_terracotta");
                    GameUtils.Data.setEntityText(entity, "secondary_root_core", "magenta_stained_glass");
                    GameUtils.Data.setEntityText(entity, "tertiary_root_outer", "pink_concrete");
                    GameUtils.Data.setEntityText(entity, "tertiary_root_inner", "pink_terracotta");
                    GameUtils.Data.setEntityText(entity, "tertiary_root_core", "pink_stained_glass");
                    GameUtils.Data.setEntityText(entity, "fine_root_outer", "light_blue_concrete");
                    GameUtils.Data.setEntityText(entity, "fine_root_inner", "light_blue_terracotta");
                    GameUtils.Data.setEntityText(entity, "fine_root_core", "light_blue_stained_glass");

                    GameUtils.Data.setEntityText(entity, "trunk_outer", "red_concrete");
                    GameUtils.Data.setEntityText(entity, "trunk_inner", "red_terracotta");
                    GameUtils.Data.setEntityText(entity, "trunk_core", "red_stained_glass");
                    GameUtils.Data.setEntityText(entity, "bough_outer", "orange_concrete");
                    GameUtils.Data.setEntityText(entity, "bough_inner", "orange_terracotta");
                    GameUtils.Data.setEntityText(entity, "bough_core", "orange_stained_glass");
                    GameUtils.Data.setEntityText(entity, "branch_outer", "yellow_concrete");
                    GameUtils.Data.setEntityText(entity, "branch_inner", "yellow_terracotta");
                    GameUtils.Data.setEntityText(entity, "branch_core", "yellow_stained_glass");
                    GameUtils.Data.setEntityText(entity, "limb_outer", "lime_concrete");
                    GameUtils.Data.setEntityText(entity, "limb_inner", "lime_terracotta");
                    GameUtils.Data.setEntityText(entity, "limb_core", "lime_stained_glass");
                    GameUtils.Data.setEntityText(entity, "twig_outer", "green_concrete");
                    GameUtils.Data.setEntityText(entity, "twig_inner", "green_terracotta");
                    GameUtils.Data.setEntityText(entity, "twig_core", "green_stained_glass");
                    GameUtils.Data.setEntityText(entity, "sprig_outer", "white_concrete");
                    GameUtils.Data.setEntityText(entity, "sprig_inner", "white_terracotta");
                    GameUtils.Data.setEntityText(entity, "sprig_core", "white_stained_glass");

                    GameUtils.Data.setEntityText(entity, "leaves1", "white_stained_glass");
                    GameUtils.Data.setEntityText(entity, "leaves2", "black_stained_glass");

                }

            }

            Entity entity_countdown = GameUtils.Mob.getAtAreaOne(level_server, entity.position().add(0, 1, 0), 1, true, "minecraft:text_display", "TANSHUGETREES-tree_countdown");

            if (entity_countdown != null) {

                entity_countdown.discard();

            }

            entity.setPos(entity.getX(), entity.getY() + GameUtils.Data.getEntityNumber(entity, "start_height"), entity.getZ());
            GameUtils.Mob.summon(level_server, entity.position().add(0, 1, 0), "text_display", "Tree Generator Status", "TANSHUGETREES-" + GameUtils.Data.getEntityText(entity, "id") + " / TANSHUGETREES-tree_generator_status", "{see_through:1b,alignment:\"left\",brightness:{block:15, sky:15},line_width:1000,transformation:{left_rotation:[0f,0f,0f,1f],right_rotation:[0f,0f,0f,1f],translation:[0f,0f,0f],scale:[1f,1f,1f]},billboard:vertical,text:'{\"text\":\"In Progress\",\"color\":\"white\"}'}");
            TXTFunction.run(level_accessor, level_server, entity.blockPosition(), "functions/" + GameUtils.Data.getEntityText(entity, "function_start"), true);

        } else {

            ShapeFileConverter.whenTreeStart(level_server, entity);

        }

    }

    private static void runSystem (LevelAccessor level_accessor, ServerLevel level_server, Entity entity) {

        String id = GameUtils.Data.getEntityText(entity, "id");
        String gen_type = GameUtils.Data.getEntityText(entity, "gen_type");
        String gen_step = GameUtils.Data.getEntityText(entity, "gen_step");
        String[] type_pre_next = new String[0];

        // Status Display
        {

            StringBuilder command = new StringBuilder();
            command.append("Total Processes : ").append((int) GameUtils.Data.getEntityNumber(entity, "total_processes"));
            command.append("\n");
            command.append("Generating : ").append(gen_type);
            command.append("\n");
            command.append("\n");
            command.append("Step : ").append(gen_step);
            command.append("\n");
            command.append("Count : ").append((int) GameUtils.Data.getEntityNumber(entity, gen_type + "_count"));
            command.append("\n");
            command.append("Length : ").append((int) GameUtils.Data.getEntityNumber(entity, gen_type + "_length")).append(" / ").append((int) GameUtils.Data.getEntityNumber(entity, gen_type + "_length_save"));
            command.append("\n");
            command.append("Thickness : ").append(GameUtils.Data.getEntityNumber(entity, gen_type + "_thickness")).append(" / ").append(GameUtils.Data.getEntityNumber(entity, gen_type + "_thickness_start"));
            GameUtils.Command.runEntity(entity, "execute positioned ~ ~1 ~ run data merge entity @e[tag=TANSHUGETREES-tree_generator_status,distance=..1,limit=1] {text:'{\"text\":\"" + command + "\",\"color\":\"white\"}'}");

        }

        if (Handcode.Config.tree_generator_speed_global == true && GameUtils.Data.getEntityLogic(entity, "tree_generator_speed_global") == true) {

            GameUtils.Data.setEntityNumber(entity, "tree_generator_speed_tick", Handcode.Config.tree_generator_speed_tick);
            GameUtils.Data.setEntityNumber(entity, "tree_generator_speed_repeat", Handcode.Config.tree_generator_speed_repeat);

        }

        while (true) {

            GameUtils.Data.addEntityNumber(entity, "total_processes", 1);
            gen_type = GameUtils.Data.getEntityText(entity, "gen_type");
            gen_step = GameUtils.Data.getEntityText(entity, "gen_step");
            type_pre_next = getTypePreNext(gen_type);

            if (gen_step.equals("summon") == true) {

                if (Step.summon(level_server, entity, id, gen_type, type_pre_next) == false) {

                    return;

                }

            } else if (gen_step.equals("calculation") == true) {

                if (Step.calculation(level_server, entity, id, gen_type, type_pre_next) == false) {

                    return;

                }

            } else if (gen_step.equals("build") == true) {

                if (Step.build(level_accessor, level_server, entity, id, gen_type, type_pre_next) == false) {

                    return;

                }

            } else {

                Step.end(level_accessor, level_server, entity, id);
                return;

            }

            // Break Out
            {

                if (processBreak(entity) == true) {

                    break;

                }

            }

        }

    }

    private static boolean processBreak (Entity entity) {

        boolean return_logic = false;

        if (GameUtils.Data.getEntityNumber(entity, "tree_generator_speed_repeat") != 0) {

            if (GameUtils.Data.getEntityNumber(entity, "tree_generator_speed_repeat_test") < GameUtils.Data.getEntityNumber(entity, "tree_generator_speed_repeat")) {

                GameUtils.Data.addEntityNumber(entity, "tree_generator_speed_repeat_test", 1);

            } else {

                GameUtils.Data.setEntityNumber(entity, "tree_generator_speed_repeat_test", 0);
                return_logic = true;

            }

        }

        return return_logic;

    }

    private static String[] getTypePreNext (String gen_type) {

        String[] return_text = new String[2];

        // Roots
        {

            if (gen_type.equals("taproot") == true) {

                return_text[0] = "trunk";
                return_text[1] = "secondary_root";

            } else if (gen_type.equals("secondary_root") == true) {

                return_text[0] = "taproot";
                return_text[1] = "tertiary_root";

            } else if (gen_type.equals("tertiary_root") == true) {

                return_text[0] = "secondary_root";
                return_text[1] = "fine_root";

            } else if (gen_type.equals("fine_root") == true) {

                return_text[0] = "tertiary_root";
                return_text[1] = "";

            }

        }

        // Body
        {

            if (gen_type.equals("trunk") == true) {

                return_text[0] = "";
                return_text[1] = "bough";

            } else if (gen_type.equals("bough") == true) {

                return_text[0] = "trunk";
                return_text[1] = "branch";

            } else if (gen_type.equals("branch") == true) {

                return_text[0] = "bough";
                return_text[1] = "limb";

            } else if (gen_type.equals("limb") == true) {

                return_text[0] = "branch";
                return_text[1] = "twig";

            } else if (gen_type.equals("twig") == true) {

                return_text[0] = "limb";
                return_text[1] = "sprig";

            } else if (gen_type.equals("sprig") == true) {

                return_text[0] = "twig";
                return_text[1] = "leaves";

            } else if (gen_type.equals("leaves") == true) {

                return_text[0] = "sprig";
                return_text[1] = "";

            }

        }

        return return_text;

    }

    private static class Step {

        private static boolean summon (ServerLevel level_server, Entity entity, String id, String gen_type, String[] type_pre_next) {

            boolean is_taproot_trunk = gen_type.equals("taproot") == true || gen_type.equals("trunk") == true;

            if (gen_type.equals("leaves") == false) {

                // Length and Thickness
                {

                    double length = Mth.nextInt(RandomSource.create(), (int) GameUtils.Data.getEntityNumber(entity, gen_type + "_length_min"), (int) GameUtils.Data.getEntityNumber(entity, gen_type + "_length_max"));
                    length = Math.ceil(length * summonReduction(entity, gen_type, "length_reduce"));
                    GameUtils.Data.setEntityNumber(entity, gen_type + "_length", length);
                    GameUtils.Data.setEntityNumber(entity, gen_type + "_length_save", length);

                    double thickness = GameUtils.Data.getEntityNumber(entity, gen_type + "_thickness_start") - GameUtils.Data.getEntityNumber(entity, gen_type + "_thickness_end");
                    thickness = thickness * summonReduction(entity, gen_type, "thickness_reduce");
                    thickness = GameUtils.Data.getEntityNumber(entity, gen_type + "_thickness_end") + thickness;
                    GameUtils.Data.setEntityNumber(entity, gen_type + "_thickness", thickness);

                }

                if (type_pre_next[1].equals("leaves") == false) {

                    // Next Part Settings
                    {

                        int count = Mth.nextInt(RandomSource.create(), (int) GameUtils.Data.getEntityNumber(entity, type_pre_next[1] + "_count_min"), (int) GameUtils.Data.getEntityNumber(entity, type_pre_next[1] + "_count_max"));
                        count = (int) Math.ceil((double) count * summonReduction(entity, type_pre_next[1], "count_reduce"));
                        GameUtils.Data.setEntityNumber(entity, type_pre_next[1] + "_count", count);
                        GameUtils.Data.setEntityNumber(entity, type_pre_next[1] + "_count_save", count);

                    }

                    // Auto Distance
                    {

                        if (GameUtils.Data.getEntityNumber(entity, type_pre_next[1] + "_count") > 0) {

                            double distance = 0.0;

                            if (GameUtils.Data.getEntityLogic(entity, type_pre_next[1] + "_chance_auto") == true) {

                                double count_available = GameUtils.Data.getEntityNumber(entity, type_pre_next[1] + "_count_save") - GameUtils.Data.getEntityNumber(entity, type_pre_next[1] + "_min_last_count");

                                if (count_available > 0) {

                                    double length_percent = Math.ceil(GameUtils.Data.getEntityNumber(entity, gen_type + "_length_save") * (GameUtils.Data.getEntityNumber(entity, type_pre_next[1] + "_chance_percent") * 0.01));
                                    distance = length_percent / count_available;

                                }

                            }

                            GameUtils.Data.setEntityNumber(entity, type_pre_next[1] + "_chance_auto_distance", distance);
                            GameUtils.Data.setEntityNumber(entity, type_pre_next[1] + "_chance_distance_left", 0);

                        }

                    }

                } else {

                    // Leaves Settings
                    {

                        GameUtils.Data.setEntityNumber(entity, "leaves_count", 1);
                        GameUtils.Data.setEntityNumber(entity, "leaves_length", 1);
                        GameUtils.Data.setEntityNumber(entity, "leaves_length_save", 1);

                        double size = Mth.nextDouble(RandomSource.create(), GameUtils.Data.getEntityNumber(entity, "leaves_size_min"), GameUtils.Data.getEntityNumber(entity, "leaves_size_max"));
                        size = size * summonReduction(entity, "leaves", "size_reduce");
                        GameUtils.Data.setEntityNumber(entity, "leaves_size", size);

                    }

                }

                // Chance of Discontinuation
                {

                    if (is_taproot_trunk == false) {

                        double chance = GameUtils.Data.getEntityNumber(entity, gen_type + "_continue_chance");
                        chance = chance * summonReduction(entity, gen_type, "continue_reduce");
                        chance = 1.0 - chance;

                        if (Math.random() < chance) {

                            GameUtils.Data.setEntityNumber(entity, type_pre_next[1] + "_count", 0);

                        }

                    }

                }

            }

            // Summoning
            {

                String at_part = "tree_generator";
                double vertical = 0.0;
                double horizontal = 0.0;
                double forward = 0.0;
                double height = 0.0;

                // Position
                {

                    if (is_taproot_trunk == false) {

                        at_part = "generator_" + type_pre_next[0];

                        if (gen_type.equals("leaves") == false) {

                            if (GameUtils.Data.getEntityText(entity, gen_type + "_center_direction_from").isEmpty() == true) {

                                // Non-Center Direction
                                {

                                    if (GameUtils.Data.getEntityNumber(entity, gen_type + "_min_last_count") > 0 && GameUtils.Data.getEntityNumber(entity, gen_type + "_count") <= 1) {

                                        // Make last part facing same as previous part
                                        forward = 1;

                                    } else {

                                        vertical = GameUtils.Data.getEntityNumber(entity, gen_type + "_start_vertical");
                                        horizontal = GameUtils.Data.getEntityNumber(entity, gen_type + "_start_horizontal");
                                        height = Mth.nextDouble(RandomSource.create(), GameUtils.Data.getEntityNumber(entity, gen_type + "_start_height_min"), GameUtils.Data.getEntityNumber(entity, gen_type + "_start_height_max"));
                                        forward = Mth.nextDouble(RandomSource.create(), GameUtils.Data.getEntityNumber(entity, gen_type + "_start_forward_min"), GameUtils.Data.getEntityNumber(entity, gen_type + "_start_forward_max"));

                                    }

                                }

                            } else {

                                // Center Direction
                                {

                                    int length = (int) GameUtils.Data.getEntityNumber(entity, type_pre_next[0] + "_length");
                                    int length_save = (int) GameUtils.Data.getEntityNumber(entity, type_pre_next[0] + "_length_save");
                                    double center = GameUtils.Data.getEntityNumber(entity, gen_type + "_center_direction_center") * 0.01;
                                    int length_below = (int) (length_save * center);
                                    int length_above = length_save - length_below;
                                    double percent = 0.0;
                                    String above_or_below = "";

                                    if ((1.0 - center) >= ((double) length / (double) length_save)) {

                                        // Above
                                        above_or_below = "above";
                                        percent = 1.0 - ((double) length / (double) length_above);

                                    } else {

                                        // Below
                                        above_or_below = "below";
                                        percent = (double) (length - length_above) / (double) length_below;

                                    }

                                    vertical = 1.0 + ((GameUtils.Data.getEntityNumber(entity, gen_type + "_center_direction_vertical_" + above_or_below) - 1.0) * percent);
                                    horizontal = 1.0 + ((GameUtils.Data.getEntityNumber(entity, gen_type + "_center_direction_horizontal_" + above_or_below) - 1.0) * percent);
                                    height = GameUtils.Data.getEntityNumber(entity, gen_type + "_center_direction_height_" + above_or_below) * percent;
                                    forward = GameUtils.Data.getEntityNumber(entity, gen_type + "_center_direction_forward_" + above_or_below) * percent;

                                }

                            }

                            vertical = Mth.nextDouble(RandomSource.create(), -(vertical), vertical);
                            horizontal = Mth.nextDouble(RandomSource.create(), -(horizontal), horizontal);

                        }

                    }

                }

                Entity entity_at = GameUtils.Mob.getAtEverywhereOne(level_server, "minecraft:marker", "TANSHUGETREES-" + id + " / TANSHUGETREES-" + at_part);

                if (entity_at == null) {

                    return false;

                }

                Vec3 vec3 = GameUtils.Space.getPosLook(entity_at, horizontal, vertical, forward);
                Entity entity_summon = GameUtils.Mob.summon(level_server, vec3.add(0, height, 0), "minecraft:marker", "Tree Generator (" + gen_type + ")", "TANSHUGETREES-" + id + " / TANSHUGETREES-generator_" + gen_type, "");

                if (is_taproot_trunk == true) {

                    entity_summon.setXRot(Mth.nextInt(RandomSource.create(), (int) GameUtils.Data.getEntityNumber(entity, gen_type + "_start_gravity_max"), (int) GameUtils.Data.getEntityNumber(entity, gen_type + "_start_gravity_min")));
                    entity_summon.setYRot(Mth.nextInt(RandomSource.create(), (int) GameUtils.Data.getEntityNumber(entity, gen_type + "_start_direction_min"), (int) GameUtils.Data.getEntityNumber(entity, gen_type + "_start_direction_max")));

                } else {

                    Entity entity_pre = GameUtils.Mob.getAtEverywhereOne(level_server, "minecraft:marker", "TANSHUGETREES-" + id + " / TANSHUGETREES-generator_" + type_pre_next[0]);

                    if (entity_pre == null) {

                        return false;

                    }

                    entity_summon.lookAt(EntityAnchorArgument.Anchor.FEET, entity_pre.position());
                    entity_summon.setPos(entity_pre.position());
                    entity_summon.lookAt(EntityAnchorArgument.Anchor.FEET, GameUtils.Space.getPosLook(entity_summon, 0, 0, -1));

                }

            }

            // Next Step
            {

                if (is_taproot_trunk == true) {

                    GameUtils.Data.setEntityText(entity, "gen_step", "build");

                } else {

                    GameUtils.Data.setEntityText(entity, "gen_step", "calculation");

                }

            }

            GameUtils.Data.addEntityNumber(entity, gen_type + "_count", -1);
            return true;

        }

        private static double summonReduction (Entity entity, String gen_type, String gen_step) {

            double return_number = 1.0;
            String from = GameUtils.Data.getEntityText(entity, gen_type + "_" + gen_step + "_from");

            if (from.isEmpty() == false) {

                double length = GameUtils.Data.getEntityNumber(entity, from + "_length");
                double length_save = GameUtils.Data.getEntityNumber(entity, from + "_length_save");
                double center = GameUtils.Data.getEntityNumber(entity, gen_type + "_" + gen_step + "_center") * 0.01;
                double length_below = length_save * center;
                double length_above = length_save - length_below;

                String above_below = "";
                double percent = 0.0;

                if ((1.0 - center) >= (length / length_save)) {

                    above_below = "above";
                    percent = length / length_above;

                } else {

                    above_below = "below";
                    percent = 1.0 - ((length - length_above) / length_below);

                }

                double start = GameUtils.Data.getEntityNumber(entity, gen_type + "_" + gen_step + "_" + above_below + "_start");
                double end = GameUtils.Data.getEntityNumber(entity, gen_type + "_" + gen_step + "_" + above_below + "_end");
                return_number = (start - end) * percent;
                return_number = end + return_number;
                return_number = return_number * 0.01;

            }

            return return_number;
        }

        private static boolean calculation (ServerLevel level_server, Entity entity, String id, String gen_type, String[] type_pre_next) {

            Entity entity_current = GameUtils.Mob.getAtEverywhereOne(level_server, "minecraft:marker", "TANSHUGETREES-" + id + " / TANSHUGETREES-generator_" + gen_type);

            if (entity_current == null) {

                return false;

            }

            GameUtils.Data.setEntityNumber(entity, "previous_posX", entity_current.position().x);
            GameUtils.Data.setEntityNumber(entity, "previous_posY", entity_current.position().y);
            GameUtils.Data.setEntityNumber(entity, "previous_posZ", entity_current.position().z);
            boolean go_next = false;

            // Test
            {

                if (GameUtils.Data.getEntityNumber(entity, gen_type + "_length") > 0) {

                    if (GameUtils.Data.getEntityNumber(entity, type_pre_next[1] + "_count") > GameUtils.Data.getEntityNumber(entity, type_pre_next[1] + "_min_last_count")) {

                        // Length Range
                        if (GameUtils.Data.getEntityNumber(entity, gen_type + "_length") / GameUtils.Data.getEntityNumber(entity, gen_type + "_length_save") <= GameUtils.Data.getEntityNumber(entity, type_pre_next[1] + "_chance_percent") * 0.01) {

                            if (GameUtils.Data.getEntityNumber(entity, type_pre_next[1] + "_chance_distance_left") > 0) {

                                GameUtils.Data.addEntityNumber(entity, type_pre_next[1] + "_chance_distance_left", -1);

                            } else {

                                GameUtils.Data.setEntityNumber(entity, type_pre_next[1] + "_chance_distance_left", GameUtils.Data.getEntityNumber(entity, type_pre_next[1] + "_chance_auto_distance"));

                                // Chance
                                if (Math.random() < GameUtils.Data.getEntityNumber(entity, type_pre_next[1] + "_chance")) {

                                    go_next = true;

                                }

                            }

                        }

                    }

                } else {

                    go_next = true;

                }

            }

            if (go_next == false) {

                // Continue
                {

                    if (gen_type.equals("leaves") == false) {

                        // Curvature
                        {

                            float vertical = (float) GameUtils.Data.getEntityNumber(entity, gen_type + "_curvature_vertical");
                            float horizontal = (float) GameUtils.Data.getEntityNumber(entity, gen_type + "_curvature_horizontal");
                            entity_current.setXRot(entity_current.getXRot() + Mth.nextFloat(RandomSource.create(), -(vertical), vertical));
                            entity_current.setYRot(entity_current.getYRot() + Mth.nextFloat(RandomSource.create(), -(horizontal), horizontal));

                        }

                        boolean gravity_run = false;

                        // Gravity
                        {

                            float weightiness = (float) GameUtils.Data.getEntityNumber(entity, gen_type + "_gravity_weightiness");

                            if (weightiness != 0) {

                                float set = 0;

                                if (entity_current.getXRot() >= GameUtils.Data.getEntityNumber(entity, gen_type + "_gravity_min")) {

                                    set = entity_current.getXRot() - weightiness;

                                    if (set < -90) {

                                        set = -90;

                                    }

                                    entity_current.setXRot(set);
                                    gravity_run = true;

                                }

                                if (entity_current.getXRot() <= GameUtils.Data.getEntityNumber(entity, gen_type + "_gravity_max")) {

                                    set = entity_current.getXRot() + weightiness;

                                    if (set > 90) {

                                        set = 90;

                                    }

                                    entity_current.setXRot(set);
                                    gravity_run = true;

                                }

                            }

                        }

                        if (gravity_run == false) {

                            // Centripetal
                            {

                                double centripetal = GameUtils.Data.getEntityNumber(entity, gen_type + "_centripetal") * 0.01;

                                if (centripetal != 0) {

                                    Entity entity_trunk = GameUtils.Mob.getAtEverywhereOne(level_server, "minecraft:marker", "TANSHUGETREES-" + id + " / TANSHUGETREES-generator_trunk");

                                    if (entity_trunk == null) {

                                        return false;

                                    }

                                    Vec3 vec3 = entity_current.position();

                                    entity_current.setPos(GameUtils.Space.getPosLook(entity_current, 0, 0, 1));
                                    entity_current.lookAt(EntityAnchorArgument.Anchor.FEET, entity_trunk.position());
                                    entity_current.setPos(GameUtils.Space.getPosLook(entity_current, 0, 0, -(centripetal)));

                                    entity_current.lookAt(EntityAnchorArgument.Anchor.FEET, vec3);
                                    entity_current.setPos(vec3);
                                    entity_current.lookAt(EntityAnchorArgument.Anchor.FEET, GameUtils.Space.getPosLook(entity_current, 0, 0, -1));

                                }

                            }

                        }

                        double thickness = GameUtils.Data.getEntityNumber(entity, gen_type + "_thickness_start") - GameUtils.Data.getEntityNumber(entity, gen_type + "_thickness_end");
                        double length_percent = GameUtils.Data.getEntityNumber(entity, gen_type + "_length") / GameUtils.Data.getEntityNumber(entity, gen_type + "_length_save");
                        thickness = GameUtils.Data.getEntityNumber(entity, gen_type + "_thickness_end") + (thickness * length_percent);
                        GameUtils.Data.setEntityNumber(entity, gen_type + "_thickness", thickness);

                    }

                    entity_current.setPos(GameUtils.Space.getPosLook(entity_current, 0, 0, 1));
                    GameUtils.Data.addEntityNumber(entity, gen_type + "_length", -1);
                    GameUtils.Data.setEntityText(entity, "gen_step", "build");

                }

            } else {

                // Next Part
                {

                    if (GameUtils.Data.getEntityNumber(entity, type_pre_next[1] + "_count") > 0) {

                        // Up
                        {

                            GameUtils.Data.setEntityText(entity, "gen_type", type_pre_next[1]);
                            GameUtils.Data.setEntityText(entity, "gen_step", "summon");

                        }

                    } else {

                        // Down
                        {

                            entity_current.discard();

                            if (gen_type.equals("taproot") == true) {

                                if (GameUtils.Data.getEntityNumber(entity, gen_type + "_count") > 0) {

                                    GameUtils.Data.setEntityText(entity, "gen_step", "summon");

                                } else {

                                    if (GameUtils.Data.getEntityNumber(entity, type_pre_next[0] + "_count") > 0) {

                                        GameUtils.Data.setEntityText(entity, "gen_step", "summon");
                                        GameUtils.Data.setEntityText(entity, "gen_type", type_pre_next[0]);

                                    } else {

                                        GameUtils.Data.setEntityText(entity, "gen_step", "end");

                                    }

                                }

                            } else if (gen_type.equals("trunk") == true) {

                                if (GameUtils.Data.getEntityNumber(entity, gen_type + "_count") > 0) {

                                    GameUtils.Data.setEntityText(entity, "gen_step", "summon");

                                } else {

                                    GameUtils.Data.setEntityText(entity, "gen_step", "end");

                                }

                            } else {

                                GameUtils.Data.setEntityText(entity, "gen_type", type_pre_next[0]);

                            }

                        }

                    }

                }

            }

            return true;

        }

        private static boolean build (LevelAccessor level_accessor, ServerLevel level_server, Entity entity, String id, String gen_type, String[] type_pre_next) {

            double thickness = GameUtils.Data.getEntityNumber(entity, gen_type + "_thickness");
            double size = 0.0;

            // Get Size and Radius
            {

                if (gen_type.equals("leaves") == false) {

                    size = thickness;

                } else {

                    size = GameUtils.Data.getEntityNumber(entity, "leaves_size");

                }

            }

            if (size > 0) {

                size = size - 1.0;

                if (size < 0) {

                    size = 0;

                }

                double radius = size * 0.5;
                double radius_ceil = Math.ceil(radius);
                String generator_type = GameUtils.Data.getEntityText(entity, gen_type + "_generator_type");

                // First Settings
                {

                    if (GameUtils.Data.getEntityLogic(entity, "still_building") == false) {

                        GameUtils.Data.setEntityLogic(entity, "still_building", true);

                        // Get Center Pos
                        {

                            Entity entity_current = GameUtils.Mob.getAtEverywhereOne(level_server, "minecraft:marker", "TANSHUGETREES-" + id + " / TANSHUGETREES-generator_" + gen_type);

                            if (entity_current == null) {

                                return false;

                            }

                            GameUtils.Data.setEntityNumber(entity, "build_centerX", entity_current.position().x);
                            GameUtils.Data.setEntityNumber(entity, "build_centerY", entity_current.position().y);
                            GameUtils.Data.setEntityNumber(entity, "build_centerZ", entity_current.position().z);

                        }

                        // Sphere Zone
                        {

                            if (generator_type.equals("sphere_zone") == true) {

                                Entity entity_pre = GameUtils.Mob.getAtEverywhereOne(level_server, "minecraft:marker", "TANSHUGETREES-" + id + " / TANSHUGETREES-generator_" + type_pre_next[0]);

                                if (entity_pre == null) {

                                    return false;

                                }

                                double yaw = Math.toRadians(((entity_pre.getYRot() + 180) + 90) % 360);
                                double pitch = entity_pre.getXRot();

                                // Min Pitch
                                {

                                    int pitch_min = (int) GameUtils.Data.getEntityNumber(entity, gen_type + "_sphere_zone_pitch_min");

                                    if (pitch > pitch_min) {

                                        pitch = pitch_min;

                                    }

                                }

                                pitch = Math.toRadians(pitch);
                                double distance = radius;
                                int sphere_zone_size = (int) GameUtils.Data.getEntityNumber(entity, gen_type + "_sphere_zone_size");

                                GameUtils.Data.setEntityNumber(entity, "sphere_zone_posX", distance * Math.cos(pitch) * Math.cos(yaw));
                                GameUtils.Data.setEntityNumber(entity, "sphere_zone_posY", distance * Math.sin(pitch));
                                GameUtils.Data.setEntityNumber(entity, "sphere_zone_posZ", distance * Math.cos(pitch) * Math.sin(yaw));

                                // Change Build Center
                                {

                                    distance = distance - sphere_zone_size;
                                    GameUtils.Data.addEntityNumber(entity, "build_centerX", distance * Math.cos(pitch) * Math.cos(yaw));
                                    GameUtils.Data.addEntityNumber(entity, "build_centerY", distance * Math.sin(pitch));
                                    GameUtils.Data.addEntityNumber(entity, "build_centerZ", distance * Math.cos(pitch) * Math.sin(yaw));

                                }

                                // Get Area
                                {

                                    double sphere_zone_area = size - sphere_zone_size;

                                    if (sphere_zone_area < 0) {

                                        sphere_zone_area = 0;

                                    }

                                    GameUtils.Data.setEntityNumber(entity, "sphere_zone_area", sphere_zone_area * sphere_zone_area);

                                }

                            }

                        }

                        GameUtils.Data.setEntityNumber(entity, "build_saveX", -(radius));
                        GameUtils.Data.setEntityNumber(entity, "build_saveY", -(radius));
                        GameUtils.Data.setEntityNumber(entity, "build_saveZ", -(radius));

                    }

                }

                double sphere_zone_area = 0.0;
                double[] sphere_zone_pos = new double[0];

                if (generator_type.equals("sphere_zone") == true) {

                    sphere_zone_area = GameUtils.Data.getEntityNumber(entity, "sphere_zone_area");
                    sphere_zone_pos = new double[]{GameUtils.Data.getEntityNumber(entity, "sphere_zone_posX"), GameUtils.Data.getEntityNumber(entity, "sphere_zone_posY"), GameUtils.Data.getEntityNumber(entity, "sphere_zone_posZ")};

                }

                double scan_change = radius / radius_ceil;

                if (radius == 0) {

                    scan_change = 1;

                }

                double center_posX = GameUtils.Data.getEntityNumber(entity, "build_centerX");
                double center_posY = GameUtils.Data.getEntityNumber(entity, "build_centerY");
                double center_posZ = GameUtils.Data.getEntityNumber(entity, "build_centerZ");
                boolean replace = GameUtils.Data.getEntityLogic(entity, gen_type + "_replace");
                double sphere_area = (radius + 0.35) * (radius + 0.35);
                double build_area = 0.0;
                double build_saveX = 0;
                double build_saveY = 0;
                double build_saveZ = 0;
                BlockPos pos = null;
                double scan_start = radius + scan_change;
                double scan_end = radius + scan_change;

                while (true) {

                    // Building
                    {

                        build_saveX = GameUtils.Data.getEntityNumber(entity, "build_saveX");
                        build_saveY = GameUtils.Data.getEntityNumber(entity, "build_saveY");
                        build_saveZ = GameUtils.Data.getEntityNumber(entity, "build_saveZ");

                        if (build_saveY > scan_end) {

                            break;

                        } else {

                            if (build_saveX > scan_end) {

                                GameUtils.Data.setEntityNumber(entity, "build_saveX", -(scan_start));
                                GameUtils.Data.addEntityNumber(entity, "build_saveY", scan_change);

                            } else {

                                if (build_saveZ > scan_end) {

                                    GameUtils.Data.setEntityNumber(entity, "build_saveZ", -(scan_start));
                                    GameUtils.Data.addEntityNumber(entity, "build_saveX", scan_change);

                                } else {

                                    GameUtils.Data.addEntityNumber(entity, "build_saveZ", scan_change);

                                    // Shaping
                                    {

                                        build_area = (build_saveX * build_saveX) + (build_saveY * build_saveY) + (build_saveZ * build_saveZ);

                                        if (build_area > sphere_area) {

                                            continue;

                                        }

                                        if (generator_type.equals("sphere_zone") == true) {

                                            {

                                                if (Math.pow(sphere_zone_pos[0] - build_saveX, 2) + Math.pow(sphere_zone_pos[1] - build_saveY, 2) + Math.pow(sphere_zone_pos[2] - build_saveZ, 2) < sphere_zone_area) {

                                                    continue;

                                                }

                                            }

                                        }

                                    }

                                    pos = new BlockPos((int) Math.floor(center_posX + build_saveX), (int) Math.floor(center_posY + build_saveY), (int) Math.floor(center_posZ + build_saveZ));

                                    // Place Block
                                    {

                                        if (gen_type.equals("leaves") == false) {

                                            // General
                                            {

                                                if (size < 1 && build_saveX == 0 && build_saveY == 0 && build_saveZ == 0) {

                                                    buildBlockConnector(level_accessor, level_server, entity, center_posX, center_posY, center_posZ, pos, gen_type, radius, build_area, replace);

                                                }

                                                String previous_block = buildGetPreviousBlock(level_accessor, pos);
                                                String block_type = buildGetBlockType(entity, gen_type, previous_block, radius, build_area);
                                                buildPlaceBlock(level_accessor, level_server, entity, pos, gen_type, block_type, previous_block, replace);

                                            }

                                        } else {

                                            // Leaves
                                            {

                                                if (Math.random() < GameUtils.Data.getEntityNumber(entity, "leaves_density") * 0.01) {

                                                    String previous_block = "";
                                                    String block_type = "";
                                                    BlockPos pos_leaves = null;
                                                    int deep = 0;

                                                    if (Math.random() < GameUtils.Data.getEntityNumber(entity, "leaves_straighten_chance")) {

                                                        deep = Mth.nextInt(RandomSource.create(), (int) GameUtils.Data.getEntityNumber(entity, "leaves_straighten_min"), (int) GameUtils.Data.getEntityNumber(entity, "leaves_straighten_max"));

                                                    }

                                                    for (int deep_test = 0; deep_test <= deep; deep_test++) {

                                                        pos_leaves = new BlockPos(pos.getX(), pos.getY() - deep_test, pos.getZ());
                                                        previous_block = buildGetPreviousBlock(level_accessor, pos_leaves);
                                                        block_type = buildGetBlockType(entity, gen_type, previous_block, radius, build_area);

                                                        if (buildPlaceBlock(level_accessor, level_server, entity, pos_leaves, gen_type, block_type, previous_block, replace) == false) {

                                                            break;

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

            // If it builds to the end without any break
            GameUtils.Data.setEntityLogic(entity, "still_building", false);
            GameUtils.Data.setEntityText(entity, "gen_step", "calculation");
            return true;

        }

        private static void buildBlockConnector (LevelAccessor level_accessor, ServerLevel level_server, Entity entity, double center_posX, double center_posY, double center_posZ, BlockPos pos, String gen_type, double radius, double build_area, boolean replace) {

            double block_connector_posX = GameUtils.Data.getEntityNumber(entity, "previous_posX");
            double block_connector_posY = GameUtils.Data.getEntityNumber(entity, "previous_posY");
            double block_connector_posZ = GameUtils.Data.getEntityNumber(entity, "previous_posZ");
            int testX = (int) (Math.floor(center_posX) - Math.floor(block_connector_posX));
            int testY = (int) (Math.floor(center_posY) - Math.floor(block_connector_posY));
            int testZ = (int) (Math.floor(center_posZ) - Math.floor(block_connector_posZ));

            if (Math.abs(testX) == 1 || Math.abs(testY) == 1 || Math.abs(testZ) == 1) {

                // For X and Z
                {

                    if (Math.abs(testX) == 1 && Math.abs(testZ) == 1) {

                        if (center_posX - block_connector_posX > center_posZ - block_connector_posZ) {

                            pos = new BlockPos(pos.getX() - testX, pos.getY(), pos.getZ());

                        } else {

                            pos = new BlockPos(pos.getX(), pos.getY(), pos.getZ() - testZ);

                        }

                        // Place Block
                        {

                            String previous_block = buildGetPreviousBlock(level_accessor, pos);
                            String block_type = buildGetBlockType(entity, gen_type, previous_block, radius, build_area);

                            if (buildPlaceBlock(level_accessor, level_server, entity, pos, gen_type, block_type, previous_block, replace) == false) {

                                return;

                            }

                        }

                    }

                }

                // For Y
                {

                    if ((Math.abs(testX) == 1 || Math.abs(testZ) == 1) && Math.abs(testY) == 1) {

                        pos = new BlockPos(pos.getX(), pos.getY() - testY, pos.getZ());

                        // Place Block
                        {

                            String previous_block = buildGetPreviousBlock(level_accessor, pos);
                            String block_type = buildGetBlockType(entity, gen_type, previous_block, radius, build_area);

                            if (buildPlaceBlock(level_accessor, level_server, entity, pos, gen_type, block_type, previous_block, replace) == false) {

                                return;

                            }

                        }

                    }

                }

            }

        }

        private static String buildGetBlockType (Entity entity, String gen_type, String previous_block, double radius, double build_area) {

            String block = "";

            if (gen_type.equals("leaves") == false) {

                // General
                {

                    // Get Outer-Inner-Core
                    {

                        double outer_level = GameUtils.Data.getEntityNumber(entity, gen_type + "_outer_level");
                        double inner_level = GameUtils.Data.getEntityNumber(entity, gen_type + "_inner_level");
                        double outer_level_area = outer_level;
                        double inner_level_area = inner_level;

                        // Outer and inner thickness must not lower than 1
                        {

                            if (outer_level_area < 1) {

                                outer_level_area = 1;

                            }

                            if (inner_level_area < 1) {

                                inner_level_area = 1;

                            }

                        }

                        double outer_area = radius - outer_level_area;
                        double inner_area = outer_area - inner_level_area;

                        // Area Calculation
                        {

                            if (outer_area > 0) {

                                outer_area = outer_area * outer_area;

                            } else {

                                outer_area = 0;

                            }

                            if (inner_area > 0) {

                                inner_area = inner_area * inner_area;

                            } else {

                                inner_area = 0;

                            }

                        }

                        if (build_area < inner_area) {

                            block = "core";

                        } else if (build_area < outer_area) {

                            if (inner_level >= 1 || Math.random() < inner_level) {

                                block = "inner";

                            } else {

                                block = "core";

                            }

                        } else {

                            if (outer_level >= 1 || Math.random() < outer_level) {

                                block = "outer";

                            } else {

                                if (inner_level >= 1 || Math.random() < inner_level) {

                                    block = "inner";

                                } else {

                                    block = "core";

                                }

                            }

                        }

                    }

                    if (previous_block.isEmpty() == false) {

                        String type_short = gen_type.substring(0, 2);
                        String previous_block_short = previous_block.substring(0, 2);
                        boolean is_blacklist = false;

                        // Test Blacklist
                        {

                            if (type_short.equals("se") == true) {

                                if ("ta".contains(previous_block_short) == true) {

                                    is_blacklist = true;

                                }

                            } else if (type_short.equals("te") == true) {

                                if ("ta/se".contains(previous_block_short) == true) {

                                    is_blacklist = true;

                                }

                            } else if (type_short.equals("fi") == true) {

                                if ("ta/se/te".contains(previous_block_short) == true) {

                                    is_blacklist = true;

                                }

                            } else if (type_short.equals("tr") == true) {

                                if ("ta/se/te/fi".contains(previous_block_short) == true) {

                                    is_blacklist = true;

                                }

                            } else if (type_short.equals("bo") == true) {

                                if ("ta/se/te/fi/tr".contains(previous_block_short) == true) {

                                    is_blacklist = true;

                                }

                            } else if (type_short.equals("br") == true) {

                                if ("ta/se/te/fi/tr/bo".contains(previous_block_short) == true) {

                                    is_blacklist = true;

                                }

                            } else if (type_short.equals("li") == true) {

                                if ("ta/se/te/fi/tr/bo/br".contains(previous_block_short) == true) {

                                    is_blacklist = true;

                                }

                            } else if (type_short.equals("tw") == true) {

                                if ("ta/se/te/fi/tr/bo/br/li".contains(previous_block_short) == true) {

                                    is_blacklist = true;

                                }

                            } else if (type_short.equals("sp") == true) {

                                if ("ta/se/te/fi/tr/bo/br/li/tw".contains(previous_block_short) == true) {

                                    is_blacklist = true;

                                }

                            }

                        }

                        boolean is_same_type = type_short.equals(previous_block_short);
                        boolean is_core = previous_block.endsWith("c");

                        if (block.equals("core") == true) {

                            // Core
                            {

                                if (is_core == true) {

                                    if (is_same_type == true) {

                                        block = "";

                                    } else if (is_blacklist == true) {

                                        block = "";

                                    }

                                }

                            }

                        } else {

                            // Outer and Inner
                            {

                                if (is_same_type == true) {

                                    if (is_core == true) {

                                        block = "";

                                    } else {

                                        {

                                            boolean is_same_type_outer = previous_block.endsWith("o");
                                            boolean is_same_type_inner = previous_block.endsWith("i");

                                            if (block.equals("outer") == true) {

                                                if (is_same_type_outer == true || is_same_type_inner == true) {

                                                    block = "";

                                                }

                                            } else if (block.equals("inner") == true) {

                                                if (is_same_type_inner == true) {

                                                    block = "";

                                                }

                                            }

                                        }

                                    }

                                } else {

                                    if (is_blacklist == true) {

                                        block = "";

                                    }

                                }

                            }

                        }

                    }

                }

            } else {

                // Leaves
                {

                    if (Math.random() < GameUtils.Data.getEntityNumber(entity, "leaves2_chance")) {

                        block = "2";

                    } else {

                        block = "1";

                    }

                }

            }

            return block;

        }

        private static String buildGetPreviousBlock (LevelAccessor level_accessor, BlockPos pos) {

            String previous_block = "";

            {

                if (TanshugetreesModVariables.MapVariables.get(level_accessor).shape_file_converter == false) {

                    {

                        BlockState block = level_accessor.getBlockState(pos);

                        if (block.canBeReplaced() == false) {

                            previous_block = GameUtils.Tile.toText(block)[0];

                            if (previous_block.startsWith("tanshugetrees:block_placer_") == true) {

                                {

                                    previous_block = previous_block.substring("tanshugetrees:block_placer_".length());
                                    String gen_type = previous_block.substring(0, 2);

                                    if (previous_block.endsWith("outer") == true) {

                                        previous_block = "o";

                                    } else if (previous_block.endsWith("inner") == true) {

                                        previous_block = "i";

                                    } else if (previous_block.endsWith("core") == true) {

                                        previous_block = "c";

                                    }

                                    previous_block = gen_type + previous_block;

                                }

                            }

                        }

                    }

                } else {

                    String key = "B" + pos.getX() + "/" + pos.getY() + "/" + pos.getZ();
                    previous_block = ShapeFileConverter.export_data.getOrDefault(key, "");

                }

            }

            return previous_block;

        }

        private static boolean buildPlaceBlock (LevelAccessor level_accessor, ServerLevel level_server, Entity entity, BlockPos pos, String gen_type, String block_type, String previous_block, boolean replace) {

            if (block_type.isEmpty() == false) {

                if (previous_block.isEmpty() == false && replace == false) {

                    return false;

                }

                String type_short = gen_type.substring(0, 2) + block_type.charAt(0);
                String block_placer = gen_type + "_" + block_type;
                String block = block_placer;

                if (gen_type.equals("leaves") == true) {

                    block = "leaves" + block_type;

                }

                if (GameUtils.Data.getEntityText(entity, block).isEmpty() == false) {

                    // Place
                    {

                        String[] function = buildGetWayFunction(entity, gen_type);

                        if (TanshugetreesModVariables.MapVariables.get(level_accessor).shape_file_converter == false) {

                            // On World
                            {

                                GameUtils.Misc.spawnParticle(level_server, pos.getCenter(), 0, 0, 0, 0, 1, "minecraft:flash");
                                GameUtils.Tile.set(level_accessor, pos, GameUtils.Tile.fromText("tanshugetrees:block_placer_" + block_placer), false);
                                GameUtils.Data.setBlockText(level_accessor, level_server, pos, "block", GameUtils.Data.getEntityText(entity, block));
                                GameUtils.Data.setBlockText(level_accessor, level_server, pos, "function", function[1]);
                                GameUtils.Data.setBlockText(level_accessor, level_server, pos, "function_style", function[2]);

                            }

                        } else {

                            // To Cache
                            {

                                String key = pos.getX() + "/" + pos.getY() + "/" + pos.getZ();

                                if (previous_block.isEmpty() == false && replace == true) {

                                    ShapeFileConverter.export_data.remove("B" + key);

                                }

                                ShapeFileConverter.export_data.put("B" + key, type_short);

                                if (function[0].isEmpty() == false) {

                                    if (previous_block.isEmpty() == false && replace == true) {

                                        ShapeFileConverter.export_data.remove("F" + key);

                                    }

                                    ShapeFileConverter.export_data.put("F" + key, function[0]);

                                }

                            }

                        }

                    }

                    return true;

                }

            }

            return false;

        }

        private static String[] buildGetWayFunction (Entity entity, String gen_type) {

            String[] return_text = new String[]{"", "", ""};
            String function = "";
            String path = "";
            String at_type = "";
            String style = "";

            for (int number = 1; number <= 9; number++) {

                function = "function_way" + number;

                if (Math.random() < GameUtils.Data.getEntityNumber(entity, function + "_chance")) {

                    path = GameUtils.Data.getEntityText(entity, function);
                    at_type = GameUtils.Data.getEntityText(entity, function + "_type");
                    style = GameUtils.Data.getEntityText(entity, function + "_style");

                    if (path.isEmpty() == false && at_type.equals(gen_type) == true) {

                        if (GameUtils.Data.getEntityNumber(entity, function + "_max") >= 0) {

                            double range_min = GameUtils.Data.getEntityNumber(entity, function + "_range_min") * 0.01;
                            double range_max = GameUtils.Data.getEntityNumber(entity, function + "_range_max") * 0.01;
                            double length_percent = 1.0;

                            if (GameUtils.Data.getEntityNumber(entity, at_type + "_length_save") > 0) {

                                length_percent = 1.0 - (GameUtils.Data.getEntityNumber(entity, at_type + "_length") / GameUtils.Data.getEntityNumber(entity, at_type + "_length_save"));

                            }

                            if (range_min <= length_percent && length_percent <= range_max) {

                                // Count Limitation
                                {

                                    if (GameUtils.Data.getEntityNumber(entity, function + "_max") > 0) {

                                        GameUtils.Data.addEntityNumber(entity, function + "_max", -1);

                                        if (GameUtils.Data.getEntityNumber(entity, function + "_max") == 0) {

                                            GameUtils.Data.setEntityNumber(entity, function + "_max", -1);

                                        }

                                    }

                                }

                                return_text[0] = "f" + number;
                                return_text[1] = path;
                                return_text[2] = style;
                                break;

                            }

                        }

                    }

                }

            }

            return return_text;

        }

        private static void end (LevelAccessor level_accessor, ServerLevel level_server, Entity entity, String id) {

            String firework_position = "";

            if (TanshugetreesModVariables.MapVariables.get(level_accessor).shape_file_converter == false) {

                TXTFunction.run(level_accessor, level_server, entity.blockPosition(), "functions/" + GameUtils.Data.getEntityText(entity, "function_end"), true);

            } else {

                ShapeFileConverter.whenTreeEnd(level_accessor, level_server, entity);
                firework_position = "execute at @p run ";

            }

            GameUtils.Mob.summon(level_server, entity.position().add(20.0, 10.0, 20.0), "minecraft:firework_rocket", "", "", "{LifeTime:40,FireworksItem:{id:firework_rocket,Count:1,tag:{Fireworks:{Flight:2,Explosions:[{Type:4,Flicker:1,Trail:1,Colors:[I;3887386,4312372],FadeColors:[I;3887386,4312372]}]}}}}");
            GameUtils.Mob.summon(level_server, entity.position().add(20.0, 10.0, -20.0), "minecraft:firework_rocket", "", "", "{LifeTime:40,FireworksItem:{id:firework_rocket,Count:1,tag:{Fireworks:{Flight:2,Explosions:[{Type:4,Flicker:1,Trail:1,Colors:[I;3887386,4312372],FadeColors:[I;3887386,4312372]}]}}}}");
            GameUtils.Mob.summon(level_server, entity.position().add(-20.0, 10.0, 20.0), "minecraft:firework_rocket", "", "", "{LifeTime:40,FireworksItem:{id:firework_rocket,Count:1,tag:{Fireworks:{Flight:2,Explosions:[{Type:4,Flicker:1,Trail:1,Colors:[I;3887386,4312372],FadeColors:[I;3887386,4312372]}]}}}}");
            GameUtils.Mob.summon(level_server, entity.position().add(-20.0, 10.0, -20.0), "minecraft:firework_rocket", "", "", "{LifeTime:40,FireworksItem:{id:firework_rocket,Count:1,tag:{Fireworks:{Flight:2,Explosions:[{Type:4,Flicker:1,Trail:1,Colors:[I;3887386,4312372],FadeColors:[I;3887386,4312372]}]}}}}");

            for (Entity entity_import : GameUtils.Mob.getAtEverywhere(level_server, "", "TANSHUGETREES-" + id)) {

                entity_import.discard();

            }

        }

    }

}
