package tannyjung.tanshugetrees_handcode.systems.tree_generator;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import tannyjung.core.GameUtils;
import tannyjung.tanshugetrees.network.TanshugetreesModVariables;
import tannyjung.tanshugetrees_handcode.config.ConfigMain;

public class TreeGenerator {

    public static void start (LevelAccessor level_accessor, Entity entity) {

        ServerLevel level_server = (ServerLevel) level_accessor;
        GameUtils.command.runEntity(entity, "particle composter ~ ~ ~ 0 0 0 0 1 force");

        if (GameUtils.nbt.entity.getLogic(entity, "start") == false) {

            GameUtils.nbt.entity.setLogic(entity, "start", true);
            beforeRunSystem(level_accessor, level_server, entity);

        } else {

            GameUtils.nbt.entity.addNumber(entity, "generate_speed_test", 1);

            if (GameUtils.nbt.entity.getNumber(entity, "generate_speed_test") >= GameUtils.nbt.entity.getNumber(entity, "generate_speed_tick")) {

                GameUtils.nbt.entity.setNumber(entity, "generate_speed_test", 0);
                runSystem(level_accessor, level_server, entity);

            }

        }

    }

    private static void beforeRunSystem (LevelAccessor level_accessor, ServerLevel level_server, Entity entity) {

        GameUtils.command.runEntity(entity, "kill @e[name=TANSHUGETREES-tree_countdown,distance=..1]");

        if (TanshugetreesModVariables.MapVariables.get(level_accessor).shape_file_converter == false) {

            GameUtils.command.runEntity(entity, "tp ~ ~" + GameUtils.nbt.entity.getNumber(entity, "start_height") + " ~");

        }

        GameUtils.nbt.entity.setText(entity, "id", entity.getUUID().toString());
        GameUtils.command.runEntity(entity, "tag @s add TANSHUGETREES-" + GameUtils.nbt.entity.getText(entity, "id"));
        GameUtils.nbt.entity.setText(entity, "type", "taproot");
        GameUtils.nbt.entity.setText(entity, "step", "summon");
        GameUtils.nbt.entity.setNumber(entity, "taproot_count", Mth.nextInt(RandomSource.create(), (int) GameUtils.nbt.entity.getNumber(entity, "taproot_count_min"), (int) GameUtils.nbt.entity.getNumber(entity, "taproot_count_max")));
        GameUtils.nbt.entity.setNumber(entity, "trunk_count", Mth.nextInt(RandomSource.create(), (int) GameUtils.nbt.entity.getNumber(entity, "trunk_count_min"), (int) GameUtils.nbt.entity.getNumber(entity, "trunk_count_max")));
        GameUtils.nbt.entity.setNumber(entity, "leaves_count_min", 1);
        GameUtils.nbt.entity.setNumber(entity, "leaves_count_max", 1);

        // Debug Mode
        {

            if (GameUtils.nbt.entity.getLogic(entity, "debug_mode") == true) {

                GameUtils.nbt.entity.setText(entity, "taproot_outer", "purple_concrete");
                GameUtils.nbt.entity.setText(entity, "taproot_inner", "purple_terracotta");
                GameUtils.nbt.entity.setText(entity, "taproot_core", "purple_stained_glass");
                GameUtils.nbt.entity.setText(entity, "secondary_root_outer", "magenta_concrete");
                GameUtils.nbt.entity.setText(entity, "secondary_root_inner", "magenta_terracotta");
                GameUtils.nbt.entity.setText(entity, "secondary_root_core", "magenta_stained_glass");
                GameUtils.nbt.entity.setText(entity, "tertiary_root_outer", "pink_concrete");
                GameUtils.nbt.entity.setText(entity, "tertiary_root_inner", "pink_terracotta");
                GameUtils.nbt.entity.setText(entity, "tertiary_root_core", "pink_stained_glass");
                GameUtils.nbt.entity.setText(entity, "fine_root_outer", "light_blue_concrete");
                GameUtils.nbt.entity.setText(entity, "fine_root_inner", "light_blue_terracotta");
                GameUtils.nbt.entity.setText(entity, "fine_root_core", "light_blue_stained_glass");

                GameUtils.nbt.entity.setText(entity, "trunk_outer", "red_concrete");
                GameUtils.nbt.entity.setText(entity, "trunk_inner", "red_terracotta");
                GameUtils.nbt.entity.setText(entity, "trunk_core", "red_stained_glass");
                GameUtils.nbt.entity.setText(entity, "bough_outer", "orange_concrete");
                GameUtils.nbt.entity.setText(entity, "bough_inner", "orange_terracotta");
                GameUtils.nbt.entity.setText(entity, "bough_core", "orange_stained_glass");
                GameUtils.nbt.entity.setText(entity, "branch_outer", "yellow_concrete");
                GameUtils.nbt.entity.setText(entity, "branch_inner", "yellow_terracotta");
                GameUtils.nbt.entity.setText(entity, "branch_core", "yellow_stained_glass");
                GameUtils.nbt.entity.setText(entity, "limb_outer", "lime_concrete");
                GameUtils.nbt.entity.setText(entity, "limb_inner", "lime_terracotta");
                GameUtils.nbt.entity.setText(entity, "limb_core", "lime_stained_glass");
                GameUtils.nbt.entity.setText(entity, "twig_outer", "green_concrete");
                GameUtils.nbt.entity.setText(entity, "twig_inner", "green_terracotta");
                GameUtils.nbt.entity.setText(entity, "twig_core", "green_stained_glass");
                GameUtils.nbt.entity.setText(entity, "sprig_outer", "white_concrete");
                GameUtils.nbt.entity.setText(entity, "sprig_inner", "white_terracotta");
                GameUtils.nbt.entity.setText(entity, "sprig_core", "white_stained_glass");

                GameUtils.nbt.entity.setText(entity, "leaves1", "white_stained_glass");
                GameUtils.nbt.entity.setText(entity, "leaves2", "black_stained_glass");

            }

        }

        // No Roots
        {

            if (GameUtils.nbt.entity.getNumber(entity, "taproot_count") == 0) {

                GameUtils.nbt.entity.setText(entity, "type", "trunk");

            }

        }

        // Summon Status Display
        GameUtils.command.runEntity(entity, "execute positioned ~ ~1 ~ run " + GameUtils.misc.summonEntity("text_display",  "TANSHUGETREES / TANSHUGETREES-" + GameUtils.nbt.entity.getText(entity, "id") + " / TANSHUGETREES-tree_generator_status", "Tree Generator Status", "see_through:1b,alignment:\"left\",brightness:{block:15},line_width:1000,transformation:{left_rotation:[0f,0f,0f,1f],right_rotation:[0f,0f,0f,1f],translation:[0f,0f,0f],scale:[1f,1f,1f]},billboard:vertical,text:'{\"text\":\"In Progress...\",\"color\":\"white\"}'"));

        if (TanshugetreesModVariables.MapVariables.get(level_accessor).shape_file_converter == false) {

            if (Math.random() < GameUtils.nbt.entity.getNumber(entity, "function_start_chance")) {

                TreeFunction.start(level_accessor, level_server, entity.getBlockX(), entity.getBlockY(), entity.getBlockZ(), GameUtils.nbt.entity.getText(entity, "function_start"), false);

            }

        } else {

            ShapeFileConverter.whenTreeStart(level_server, entity);

        }

    }

    private static void runSystem (LevelAccessor level_accessor, ServerLevel level_server, Entity entity) {

        String id = GameUtils.nbt.entity.getText(entity, "id");
        String type = GameUtils.nbt.entity.getText(entity, "type");
        String step = GameUtils.nbt.entity.getText(entity, "step");
        String[] type_pre_next = new String[0];

        // Status Display
        {

            StringBuilder command = new StringBuilder();

            command
                    .append("Total Processes : ").append((int) GameUtils.nbt.entity.getNumber(entity, "total_processes"))
                    .append("\\\\n")
                    .append("Generating : ").append(type)
                    .append("\\\\n")
                    .append("\\\\n")
                    .append("Step : ").append(step)
                    .append("\\\\n")
                    .append("Count : ").append((int) GameUtils.nbt.entity.getNumber(entity, type + "_count"))
                    .append("\\\\n")
                    .append("Length : ").append((int) GameUtils.nbt.entity.getNumber(entity, type + "_length")).append(" / ").append((int) GameUtils.nbt.entity.getNumber(entity, type + "_length_save"))
                    .append("\\\\n")
                    .append("Thickness : ").append(GameUtils.nbt.entity.getNumber(entity, type + "_thickness")).append(" / ").append(GameUtils.nbt.entity.getNumber(entity, type + "_thickness_max"))
            ;

            GameUtils.command.runEntity(entity, "execute positioned ~ ~1 ~ run data merge entity @e[tag=TANSHUGETREES-tree_generator_status,distance=..1,limit=1,sort=nearest] {text:'{\"text\":\"" + command + "\",\"color\":\"red\"}'}");

        }

        if (ConfigMain.global_speed_enable == true && GameUtils.nbt.entity.getLogic(entity, "global_generate_speed") == true) {

            GameUtils.nbt.entity.setNumber(entity, "generate_speed_tick", ConfigMain.global_speed);
            GameUtils.nbt.entity.setNumber(entity, "generate_speed_repeat", ConfigMain.global_speed_repeat);
            GameUtils.nbt.entity.setNumber(entity, "generate_speed_tp", ConfigMain.global_speed_tp);

        }

        while (true) {

            type = GameUtils.nbt.entity.getText(entity, "type");
            step = GameUtils.nbt.entity.getText(entity, "step");
            type_pre_next = typePreNext(entity);

            if (step.equals("summon") == true) {

                Step.summon(level_server, entity, id, type, type_pre_next);

            } else if (step.equals("calculation") == true) {

                Step.calculation(level_server, entity, id, type, type_pre_next);

            } else if (step.equals("build") == true) {

                Step.build(level_accessor, level_server, entity, id, type);

            } else {

                Step.end(level_accessor, level_server, entity, id);
                break;

            }

            GameUtils.nbt.entity.addNumber(entity, "total_processes", 1);

            // Break Out
            {

                if (processBreak(entity) == true) {

                    break;

                }

                if (GameUtils.nbt.entity.getNumber(entity, "generate_speed_tp") != 0) {

                    if (GameUtils.nbt.entity.getNumber(entity, "generate_speed_tp_test") < GameUtils.nbt.entity.getNumber(entity, "generate_speed_tp")) {

                        GameUtils.nbt.entity.setNumber(entity, "generate_speed_tp_test", 0);
                        break;

                    }

                }

            }

        }

    }

    private static boolean processBreak (Entity entity) {

        boolean return_logic = false;

        if (GameUtils.nbt.entity.getNumber(entity, "generate_speed_repeat") != 0) {

            if (GameUtils.nbt.entity.getNumber(entity, "generate_speed_repeat_test") < GameUtils.nbt.entity.getNumber(entity, "generate_speed_repeat")) {

                GameUtils.nbt.entity.addNumber(entity, "generate_speed_repeat_test", 1);

            } else {

                GameUtils.nbt.entity.setNumber(entity, "generate_speed_repeat_test", 0);
                return_logic = true;

            }

        }

        return return_logic;

    }

    private static String[] typePreNext (Entity entity) {

        String[] return_text = new String[2];
        String type = GameUtils.nbt.entity.getText(entity, "type");

        // Roots
        {

            if (type.equals("taproot") == true) {

                return_text[0] = "trunk";
                return_text[1] = "secondary_root";

            } else if (type.equals("secondary_root") == true) {

                return_text[0] = "taproot";
                return_text[1] = "tertiary_root";

            } else if (type.equals("tertiary_root") == true) {

                return_text[0] = "secondary_root";
                return_text[1] = "fine_root";

            } else if (type.equals("fine_root") == true) {

                return_text[0] = "tertiary_root";
                return_text[1] = "";

            }

        }

        // Body
        {

            if (type.equals("trunk") == true) {

                return_text[0] = "";
                return_text[1] = "bough";

            } else if (type.equals("bough") == true) {

                return_text[0] = "trunk";
                return_text[1] = "branch";

            } else if (type.equals("branch") == true) {

                return_text[0] = "bough";
                return_text[1] = "limb";

            } else if (type.equals("limb") == true) {

                return_text[0] = "branch";
                return_text[1] = "twig";

            } else if (type.equals("twig") == true) {

                return_text[0] = "limb";
                return_text[1] = "sprig";

            } else if (type.equals("sprig") == true) {

                return_text[0] = "twig";
                return_text[1] = "leaves";

            } else if (type.equals("leaves") == true) {

                return_text[0] = "sprig";
                return_text[1] = "";

            }

        }

        return return_text;

    }

    private static class Step {

        private static void summon (ServerLevel level_server, Entity entity, String id, String type, String[] type_pre_next) {

            boolean taproot_trunk = type.equals("taproot") == true || type.equals("trunk") == true;
            GameUtils.nbt.entity.addNumber(entity, type + "_count", -1);

            // Start Settings
            {

                if (type.equals("leaves") == false) {

                    // General
                    {

                        GameUtils.nbt.entity.setNumber(entity, type_pre_next[1] + "_count", Mth.nextInt(RandomSource.create(), (int) GameUtils.nbt.entity.getNumber(entity, type_pre_next[1] + "_count_min"), (int) GameUtils.nbt.entity.getNumber(entity, type_pre_next[1] + "_count_max")));
                        GameUtils.nbt.entity.setNumber(entity, type + "_length", Mth.nextInt(RandomSource.create(), (int) GameUtils.nbt.entity.getNumber(entity, type + "_length_min"), (int) GameUtils.nbt.entity.getNumber(entity, type + "_length_max")));
                        GameUtils.nbt.entity.setNumber(entity, type + "_thickness", GameUtils.nbt.entity.getNumber(entity, type + "_thickness_max") - 1);

                        if (GameUtils.nbt.entity.getNumber(entity, type + "_length") > 0) {

                            // Reduce From - To
                            {

                                summonReduction(entity, "count", type, type_pre_next[1]);
                                summonReduction(entity, "length", type, type_pre_next[1]);
                                summonReduction(entity, "thickness", type, type_pre_next[1]);

                            }

                        }

                        GameUtils.nbt.entity.setNumber(entity, type_pre_next[1] + "_count_save", GameUtils.nbt.entity.getNumber(entity, type_pre_next[1] + "_count"));
                        GameUtils.nbt.entity.setNumber(entity, type + "_length_save", GameUtils.nbt.entity.getNumber(entity, type + "_length"));

                        // Auto Distance
                        {

                            int auto_distance = 0;

                            if (GameUtils.nbt.entity.getLogic(entity, type_pre_next[1] + "_random_auto") == true) {

                                auto_distance = (int) Math.ceil(GameUtils.nbt.entity.getNumber(entity, type + "_length_save") * (GameUtils.nbt.entity.getNumber(entity, type_pre_next[1] + "_random_percent") * 0.01));
                                auto_distance = (int) Math.ceil(auto_distance / GameUtils.nbt.entity.getNumber(entity, type_pre_next[1] + "_count_save"));

                            }

                            GameUtils.nbt.entity.setNumber(entity, type_pre_next[1] + "_random_auto_distance", auto_distance);

                        }

                        GameUtils.nbt.entity.setNumber(entity, type_pre_next[1] + "_random_distance_left", 0);

                    }

                    if (type.equals("sprig") == true) {

                        GameUtils.nbt.entity.setNumber(entity, "leaves_count", 1);
                        GameUtils.nbt.entity.setNumber(entity, "leaves_length", 1);

                    }

                } else {

                    // Leaves
                    {

                        GameUtils.nbt.entity.setNumber(entity, "leaves_size", Mth.nextDouble(RandomSource.create(), GameUtils.nbt.entity.getNumber(entity, "leaves_size_min"), GameUtils.nbt.entity.getNumber(entity, "leaves_size_max")));
                        summonReduction(entity, "size", type, type_pre_next[1]);

                    }

                }

            }

            // Summoning
            {

                String at_part = "";
                String positioned = "";

                // Position
                {

                    if (taproot_trunk == true) {

                        at_part = "tree_generator";
                        positioned = "positioned ~ ~ ~";

                    } else {

                        at_part = "generator_" + type_pre_next[0];

                        if (type.equals("leaves") == false) {

                            {

                                double vertical = 0.0;
                                double horizontal = 0.0;
                                double forward = 0.0;
                                double height = 0.0;

                                if (GameUtils.nbt.entity.getText(entity, type + "_center_direction_from").equals("") == true) {

                                    // Non Center Direction
                                    {

                                        vertical = GameUtils.nbt.entity.getNumber(entity, type + "_start_vertical");
                                        horizontal = GameUtils.nbt.entity.getNumber(entity, type + "_start_horizontal");
                                        height = Mth.nextDouble(RandomSource.create(), GameUtils.nbt.entity.getNumber(entity, type + "_start_height_min"), GameUtils.nbt.entity.getNumber(entity, type + "_start_height_max"));
                                        forward = Mth.nextDouble(RandomSource.create(), GameUtils.nbt.entity.getNumber(entity, type + "_start_forward_min"), GameUtils.nbt.entity.getNumber(entity, type + "_start_forward_max"));

                                    }

                                } else {

                                    // Center Direction
                                    {

                                        int length = (int) GameUtils.nbt.entity.getNumber(entity, type_pre_next[0] + "_length");
                                        int length_save = (int) GameUtils.nbt.entity.getNumber(entity, type_pre_next[0] + "_length_save");
                                        double center = GameUtils.nbt.entity.getNumber(entity, type + "_center_direction_center") * 0.01;
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

                                        vertical = 1.0 + ((GameUtils.nbt.entity.getNumber(entity, type + "_center_direction_vertical_" + above_or_below) - 1.0) * percent);
                                        horizontal = 1.0 + ((GameUtils.nbt.entity.getNumber(entity, type + "_center_direction_horizontal_" + above_or_below) - 1.0) * percent);
                                        height = GameUtils.nbt.entity.getNumber(entity, type + "_center_direction_height_" + above_or_below) * percent;
                                        forward = GameUtils.nbt.entity.getNumber(entity, type + "_center_direction_forward_" + above_or_below) * percent;

                                    }

                                }

                                vertical = Double.parseDouble(String.format("%.2f", Mth.nextDouble(RandomSource.create(), -(vertical), vertical)));
                                horizontal = Double.parseDouble(String.format("%.2f", Mth.nextDouble(RandomSource.create(), -(horizontal), horizontal)));
                                height = Double.parseDouble(String.format("%.2f", height));
                                forward = Double.parseDouble(String.format("%.2f", forward));
                                positioned = "positioned ^" + horizontal + " ^" + vertical + " ^" + forward + " positioned ~ ~" + height + " ~";

                            }

                        } else {

                            positioned = "positioned ~ ~ ~";

                        }

                    }

                }

                GameUtils.command.run(level_server, 0, 0, 0, "execute at @e[tag=TANSHUGETREES-" + id + ",tag=TANSHUGETREES-" + at_part + "] " + positioned + " run " + GameUtils.misc.summonEntity("marker", "TANSHUGETREES / TANSHUGETREES-" + id + " / TANSHUGETREES-generator_" + type, "Tree Generator - Part", ""));
                GameUtils.command.run(level_server, 0, 0, 0, "execute at @e[tag=TANSHUGETREES-" + id + ",tag=TANSHUGETREES-" + at_part + "] " + positioned + " run particle minecraft:firework ~ ~ ~ 0 0 0 0.1 20 force");

                if (taproot_trunk == true) {

                    int direction = Mth.nextInt(RandomSource.create(), (int) GameUtils.nbt.entity.getNumber(entity, type + "_start_direction_min"), (int) GameUtils.nbt.entity.getNumber(entity, type + "_start_direction_max"));
                    int gravity = Mth.nextInt(RandomSource.create(), (int) GameUtils.nbt.entity.getNumber(entity, type + "_start_gravity_max"), (int) GameUtils.nbt.entity.getNumber(entity, type + "_start_gravity_min"));
                    GameUtils.command.run(level_server, 0, 0, 0, "execute as @e[tag=TANSHUGETREES-" + id + ",tag=TANSHUGETREES-generator_" + type + "] at @s run tp @s ~ ~ ~ " + direction + " " + gravity);

                } else {

                    String type_current = "tag=TANSHUGETREES-" + id + ",tag=TANSHUGETREES-generator_" + type;
                    String type_pre = "tag=TANSHUGETREES-" + id + ",tag=TANSHUGETREES-generator_" + type_pre_next[0];
                    GameUtils.command.run(level_server, 0, 0, 0, "execute as @e[" + type_current + "] at @s facing entity @e[" + type_pre + ",limit=1] feet positioned as @e[" + type_pre + ",limit=1] run tp @s ~ ~ ~ facing ^ ^ ^-1");

                }

            }

            // Next Step
            {

                if (GameUtils.nbt.entity.getNumber(entity, type + "_length") == 0) {

                    GameUtils.nbt.entity.setText(entity, "step", "calculation");

                } else {

                    GameUtils.nbt.entity.setText(entity, "step", "build");

                }

            }

        }

        private static void summonReduction (Entity entity, String step, String type, String type_next) {

            // No Reduction
            {

                if (step.equals("count") == true) {

                    if (type.equals("taproot") == false && type.equals("fine_root") == false && type.equals("trunk") == false && type.equals("sprig") == false) {

                        return;

                    }

                }

            }

            String reduce_from = "";

            // Reduce From
            {

                if (step.equals("count") == true) {

                    reduce_from = type_next;

                } else {

                    reduce_from = type;

                }

                reduce_from = GameUtils.nbt.entity.getText(entity, reduce_from + "_" + step + "_reduce_from");

            }

            if (reduce_from.equals("") == false) {

                int length = (int) GameUtils.nbt.entity.getNumber(entity, reduce_from + "_length");
                int length_save = (int) GameUtils.nbt.entity.getNumber(entity, reduce_from + "_length_save");
                double center = GameUtils.nbt.entity.getNumber(entity, type + "_" + step + "_reduce_center") * 0.01;
                int length_below = (int) (length_save * center);
                int length_above = length_save - length_below;
                double percent = 0.0;

                if ((1.0 - center) >= ((double) length / (double) length_save)) {

                    // Above
                    percent = (double) length / (double) length_above;

                } else {

                    // Below
                    percent = 1.0 - ((double) (length - length_above) / (double) length_below);

                }

                double value = GameUtils.nbt.entity.getNumber(entity, type + "_" + step) * (1.0 - (GameUtils.nbt.entity.getNumber(entity, type + "_" + step + "_reduce_below") * 0.01));
                GameUtils.nbt.entity.setNumber(entity, type + "_" + step, value * percent);

            }

        }

        private static void calculation (ServerLevel level_server, Entity entity, String id, String type, String[] type_pre_next) {

            boolean go_next = false;

            // Test
            {

                if (GameUtils.nbt.entity.getNumber(entity, type + "_length") > 0) {

                    if (GameUtils.nbt.entity.getNumber(entity, type_pre_next[1] + "_count") > 1) {

                        if (GameUtils.nbt.entity.getNumber(entity, type_pre_next[1] + "_random_distance_left") > 0) {

                            GameUtils.nbt.entity.addNumber(entity, type_pre_next[1] + "_random_distance_left", -1);

                        } else {

                            // Length Range
                            if (GameUtils.nbt.entity.getNumber(entity, type + "_length") / GameUtils.nbt.entity.getNumber(entity, type + "_length_save") <= GameUtils.nbt.entity.getNumber(entity, type_pre_next[1] + "_random_percent") * 0.01) {

                                GameUtils.nbt.entity.setNumber(entity, type_pre_next[1] + "_random_distance_left", GameUtils.nbt.entity.getNumber(entity, type_pre_next[1] + "_random_auto_distance"));

                                // Chance
                                if (Math.random() < GameUtils.nbt.entity.getNumber(entity, type_pre_next[1] + "_random_chance")) {

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

                    GameUtils.nbt.entity.setText(entity, "step", "build");
                    GameUtils.nbt.entity.addNumber(entity, type + "_length", -1);

                    if (type.equals("leaves") == false) {

                        // Curvature
                        {

                            double vertical = GameUtils.nbt.entity.getNumber(entity, type + "_curvature_vertical");
                            double horizontal = GameUtils.nbt.entity.getNumber(entity, type + "_curvature_horizontal");

                            if (vertical != 0 || horizontal != 0) {

                                vertical = Mth.nextDouble(RandomSource.create(), -(vertical), vertical);
                                horizontal = Mth.nextDouble(RandomSource.create(), -(horizontal), horizontal);
                                GameUtils.command.run(level_server, 0, 0, 0, "execute as @e[tag=TANSHUGETREES-" + id + ",tag=TANSHUGETREES-generator_" + type + "] at @s run tp @s ~ ~ ~ ~" + horizontal + " ~" + vertical);

                            }

                        }

                        // Gravity
                        {

                            int weightiness = (int) GameUtils.nbt.entity.getNumber(entity, type + "_gravity_weightiness");

                            if (weightiness != 0) {

                                int min = (int) GameUtils.nbt.entity.getNumber(entity, type + "_gravity_min");
                                int max = (int) GameUtils.nbt.entity.getNumber(entity, type + "_gravity_max");
                                GameUtils.command.run(level_server, 0, 0, 0, "execute as @e[tag=TANSHUGETREES-" + id + ",tag=TANSHUGETREES-generator_" + type + ",x_rotation=" + min + "..90] at @s run tp @s ~ ~ ~ ~ ~-" + weightiness);
                                GameUtils.command.run(level_server, 0, 0, 0, "execute as @e[tag=TANSHUGETREES-" + id + ",tag=TANSHUGETREES-generator_" + type + ",x_rotation=-90.." + max + "] at @s run tp @s ~ ~ ~ ~ ~" + weightiness);

                            }

                        }

                        double percent = GameUtils.nbt.entity.getNumber(entity, type + "_length") / GameUtils.nbt.entity.getNumber(entity, type + "_length_save");
                        double thickness = GameUtils.nbt.entity.getNumber(entity, type + "_thickness_max") - GameUtils.nbt.entity.getNumber(entity, type + "_thickness_min");
                        thickness = (GameUtils.nbt.entity.getNumber(entity, type + "_thickness_min") - 1) + (thickness * percent);
                        thickness = Math.round(thickness);
                        GameUtils.nbt.entity.setNumber(entity, type + "_thickness", Double.parseDouble(String.format("%.2f", thickness)));
                        GameUtils.command.run(level_server, 0, 0, 0, "execute as @e[tag=TANSHUGETREES-" + id + ",tag=TANSHUGETREES-generator_" + type + "] at @s run tp @s ^ ^ ^1");

                    }

                }

            } else {

                // Next Part
                {

                    if (GameUtils.nbt.entity.getNumber(entity, type_pre_next[1] + "_count") > 0) {

                        // Up
                        {

                            GameUtils.nbt.entity.setText(entity, "type", type_pre_next[1]);
                            GameUtils.nbt.entity.setText(entity, "step", "summon");

                        }

                    } else {

                        // Down
                        {

                            GameUtils.command.runEntity(entity, "kill @e[tag=TANSHUGETREES-" + id + ",tag=TANSHUGETREES-generator_" + type + "]");

                            if (type.equals("taproot") == true) {

                                if (GameUtils.nbt.entity.getNumber(entity, type + "_count") <= 0 && GameUtils.nbt.entity.getNumber(entity, type + "_length") <= 0) {

                                    GameUtils.nbt.entity.setText(entity, "type", type_pre_next[0]);

                                }

                                GameUtils.nbt.entity.setText(entity, "step", "summon");

                            } else if (type.equals("trunk") == true) {

                                if (GameUtils.nbt.entity.getNumber(entity, type + "_count") <= 0 && GameUtils.nbt.entity.getNumber(entity, type + "_length") <= 0) {

                                    GameUtils.nbt.entity.setText(entity, "step", "end");

                                } else {

                                    GameUtils.nbt.entity.setText(entity, "step", "summon");

                                }

                            } else {

                                GameUtils.nbt.entity.setText(entity, "type", type_pre_next[0]);

                            }

                        }

                    }

                }

            }

        }

        private static void build (LevelAccessor level_accessor, ServerLevel level_server, Entity entity, String id, String type) {

            double thickness = 0.0;

            if (type.equals("leaves") == false) {

                thickness = GameUtils.nbt.entity.getNumber(entity, type + "_thickness");

            } else {

                thickness = GameUtils.nbt.entity.getNumber(entity, "leaves_size");

            }

            double half_thickness = thickness * 0.5;
            String generator_type = GameUtils.nbt.entity.getText(entity, type + "_generator_type");

            // Start Settings
            {

                if (GameUtils.nbt.entity.getLogic(entity, "still_building") == false) {

                    GameUtils.nbt.entity.setLogic(entity, "still_building", true);

                    // Get Center Pos
                    {

                        GameUtils.command.runEntity(entity, "data modify entity @s ForgeData.builder_pos set from entity @e[tag=TANSHUGETREES-" + id + ",tag=TANSHUGETREES-generator_" + type + ",limit=1] Pos");
                        double[] center_pos = GameUtils.nbt.entity.getListNumber(entity, "builder_pos");
                        GameUtils.nbt.entity.setNumber(entity, "build_centerX", center_pos[0]);
                        GameUtils.nbt.entity.setNumber(entity, "build_centerY", center_pos[1]);
                        GameUtils.nbt.entity.setNumber(entity, "build_centerZ", center_pos[2]);

                    }

                    // Sphere Zone
                    {

                        if (generator_type.equals("sphere_zone") == true) {

                            GameUtils.command.runEntity(entity, "data modify entity @s ForgeData.builder_yaw_pitch set from entity @e[tag=TANSHUGETREES-" + id + ",tag=TANSHUGETREES-generator_sprig,limit=1] Rotation");
                            double[] yaw_pitch = GameUtils.nbt.entity.getListNumberFloat(entity, "builder_yaw_pitch");
                            double yaw = Math.toRadians(((yaw_pitch[0] + 180) + 90) % 360);
                            double pitch = yaw_pitch[1];

                            // Min Pitch
                            {

                                int pitch_min = (int) GameUtils.nbt.entity.getNumber(entity, type + "_sphere_zone_pitch_min");

                                if (pitch > pitch_min) {

                                    pitch = pitch_min;

                                }

                            }

                            pitch = Math.toRadians(pitch);
                            double distance = half_thickness;
                            int size = (int) GameUtils.nbt.entity.getNumber(entity, type + "_sphere_zone_size");

                            GameUtils.nbt.entity.setNumber(entity, "sphere_zone_posX", distance * Math.cos(pitch) * Math.cos(yaw));
                            GameUtils.nbt.entity.setNumber(entity, "sphere_zone_posY", distance * Math.sin(pitch));
                            GameUtils.nbt.entity.setNumber(entity, "sphere_zone_posZ", distance * Math.cos(pitch) * Math.sin(yaw));

                            // Change Build Center
                            {

                                distance = distance - size;
                                GameUtils.nbt.entity.addNumber(entity, "build_centerX", distance * Math.cos(pitch) * Math.cos(yaw));
                                GameUtils.nbt.entity.addNumber(entity, "build_centerY", distance * Math.sin(pitch));
                                GameUtils.nbt.entity.addNumber(entity, "build_centerZ", distance * Math.cos(pitch) * Math.sin(yaw));

                            }

                            // Get Area
                            {

                                double sphere_zone_area = thickness - size;

                                if (sphere_zone_area < 0) {

                                    sphere_zone_area = 0;

                                }

                                GameUtils.nbt.entity.setNumber(entity, "sphere_zone_area", sphere_zone_area * sphere_zone_area);

                            }

                        }

                    }

                    GameUtils.nbt.entity.setNumber(entity, "build_saveX", -(half_thickness));
                    GameUtils.nbt.entity.setNumber(entity, "build_saveY", -(half_thickness));
                    GameUtils.nbt.entity.setNumber(entity, "build_saveZ", -(half_thickness));

                }

            }

            double[] center_pos = new double[]{GameUtils.nbt.entity.getNumber(entity, "build_centerX"), GameUtils.nbt.entity.getNumber(entity, "build_centerY"), GameUtils.nbt.entity.getNumber(entity, "build_centerZ")};
            boolean replace = GameUtils.nbt.entity.getLogic(entity, type + "_replace");
            double sphere_area = half_thickness * half_thickness;
            double sphere_zone_area = 0.0;
            double[] sphere_zone_pos = new double[0];

            // Get Some Saved Data
            {

                if (generator_type.equals("sphere_zone") == true) {

                    sphere_zone_area = GameUtils.nbt.entity.getNumber(entity, "sphere_zone_area");
                    sphere_zone_pos = new double[]{GameUtils.nbt.entity.getNumber(entity, "sphere_zone_posX"), GameUtils.nbt.entity.getNumber(entity, "sphere_zone_posY"), GameUtils.nbt.entity.getNumber(entity, "sphere_zone_posZ")};

                }

            }

            while (true) {

                // Building
                {

                    double build_saveX = GameUtils.nbt.entity.getNumber(entity, "build_saveX");
                    double build_saveY = GameUtils.nbt.entity.getNumber(entity, "build_saveY");
                    double build_saveZ = GameUtils.nbt.entity.getNumber(entity, "build_saveZ");

                    if (build_saveY <= half_thickness) {

                        if (build_saveX <= half_thickness) {

                            if (build_saveZ <= half_thickness) {

                                GameUtils.nbt.entity.addNumber(entity, "build_saveZ", 1);
                                double build_area = 0.0;

                                // Shaping
                                {

                                    if (generator_type.startsWith("sphere") == true) {

                                        {

                                            build_area = (build_saveX * build_saveX) + (build_saveY * build_saveY) + (build_saveZ * build_saveZ);

                                            if ((thickness > 1) && (build_area > sphere_area)) {

                                                continue;

                                            }

                                        }

                                    }

                                    if (generator_type.equals("sphere_zone") == true) {

                                        {

                                            if (Math.pow(sphere_zone_pos[0] - build_saveX, 2) + Math.pow(sphere_zone_pos[1] - build_saveY, 2) + Math.pow(sphere_zone_pos[2] - build_saveZ, 2) < sphere_zone_area) {

                                                continue;

                                            }

                                        }

                                    }

                                }

                                BlockPos pos = new BlockPos((int) Math.floor(center_pos[0] + build_saveX), (int) Math.floor(center_pos[1] + build_saveY), (int) Math.floor(center_pos[2] + build_saveZ));

                                // Place Block
                                {

                                    if (type.equals("leaves") == false) {

                                        // General
                                        {

                                            String block_type = "";

                                            // Get Block
                                            {

                                                if (generator_type.equals("sphere") == true) {

                                                    block_type = buildOuterInnerCore(level_accessor, entity, type, half_thickness, pos, build_area);

                                                } else {

                                                    block_type = "outer";

                                                }

                                            }

                                            if (thickness <= 1) {

                                                buildBlockConnector(level_accessor, level_server, entity, center_pos, pos, type, block_type, replace);

                                            }

                                            if (block_type.equals("") == false) {

                                                if (buildTestKeep(level_accessor, pos, replace) == true) {

                                                    buildPlaceBlock(level_accessor, level_server, entity, pos, type, block_type);
                                                    return;

                                                }

                                            }

                                        }

                                    } else {

                                        // Leaves
                                        {

                                            if (Math.random() < GameUtils.nbt.entity.getNumber(entity, "leaves_density") * 0.01) {

                                                BlockPos pos_leaves = null;
                                                String block_type = "";
                                                int deep = 1;

                                                if (Math.random() < GameUtils.nbt.entity.getNumber(entity, "leaves_straighten_chance")) {

                                                    deep = Mth.nextInt(RandomSource.create(), (int) GameUtils.nbt.entity.getNumber(entity, "leaves_straighten_min"), (int) GameUtils.nbt.entity.getNumber(entity, "leaves_straighten_max"));

                                                }

                                                for (int deep_test = 0; deep_test < deep; deep_test++) {

                                                    // Get Block
                                                    {

                                                        if (Math.random() < GameUtils.nbt.entity.getNumber(entity, "leaves2_chance")) {

                                                            block_type = "2";

                                                        } else {

                                                            block_type = "1";

                                                        }

                                                    }

                                                    pos_leaves = new BlockPos(pos.getX(), pos.getY() - deep_test, pos.getZ());

                                                    if (GameUtils.block.isTaggedAs(level_accessor.getBlockState(pos_leaves), "tanshugetrees:block_placer_blacklist_leaves") == false) {

                                                        if (block_type.equals("") == false && buildTestKeep(level_accessor, pos_leaves, replace) == true) {

                                                            buildPlaceBlock(level_accessor, level_server, entity, pos_leaves, type, block_type);

                                                        }

                                                    }

                                                }

                                                return;

                                            }

                                        }

                                    }

                                }

                            } else {

                                GameUtils.nbt.entity.setNumber(entity, "build_saveZ", -(half_thickness));
                                GameUtils.nbt.entity.addNumber(entity, "build_saveX", 1);

                            }

                        } else {

                            GameUtils.nbt.entity.setNumber(entity, "build_saveX", -(half_thickness));
                            GameUtils.nbt.entity.addNumber(entity, "build_saveY", 1);

                        }

                    } else {

                        break;

                    }

                }

            }

            // If it builds to the end without any break
            GameUtils.nbt.entity.setLogic(entity, "still_building", false);
            GameUtils.nbt.entity.setText(entity, "step", "calculation");

        }

        private static void buildBlockConnector (LevelAccessor level_accessor, ServerLevel level_server, Entity entity, double[] center_pos, BlockPos pos, String type, String block_type, boolean replace) {

            double block_connector_posX = GameUtils.nbt.entity.getNumber(entity, "block_connector_posX");
            double block_connector_posY = GameUtils.nbt.entity.getNumber(entity, "block_connector_posY");
            double block_connector_posZ = GameUtils.nbt.entity.getNumber(entity, "block_connector_posZ");
            GameUtils.nbt.entity.setNumber(entity, "block_connector_posX", center_pos[0]);
            GameUtils.nbt.entity.setNumber(entity, "block_connector_posY", center_pos[1]);
            GameUtils.nbt.entity.setNumber(entity, "block_connector_posZ", center_pos[2]);

            if (block_type.equals("") == false) {

                int testX = (int) Math.floor(center_pos[0]) - (int) Math.floor(block_connector_posX);
                int testY = (int) Math.floor(center_pos[1]) - (int) Math.floor(block_connector_posY);
                int testZ = (int) Math.floor(center_pos[2]) - (int) Math.floor(block_connector_posZ);
                BlockPos pos_connect = pos;

                if (Math.abs(testX) == 1 || Math.abs(testY) == 1 || Math.abs(testZ) == 1) {

                    if (Math.abs(testX) == 1 && Math.abs(testZ) == 1) {

                        // For X and Z
                        {

                            if (center_pos[0] - block_connector_posX > center_pos[2] - block_connector_posZ) {

                                pos_connect = new BlockPos(pos_connect.getX() - testX, pos_connect.getY(), pos_connect.getZ());

                            } else {

                                pos_connect = new BlockPos(pos_connect.getX(), pos_connect.getY(), pos_connect.getZ() - testZ);

                            }

                            // Place Block
                            {

                                if (buildTestKeep(level_accessor, pos_connect, replace) == true) {

                                    buildPlaceBlock(level_accessor, level_server, entity, pos_connect, type, block_type);

                                }

                            }

                        }

                    }

                    if ((Math.abs(testX) == 1 || Math.abs(testZ) == 1) && Math.abs(testY) == 1) {

                        // For Y
                        {

                            pos_connect = new BlockPos(pos_connect.getX(), pos_connect.getY() - testY, pos_connect.getZ());

                            // Place Block
                            {

                                if (buildTestKeep(level_accessor, pos_connect, replace) == true) {

                                    buildPlaceBlock(level_accessor, level_server, entity, pos_connect, type, block_type);

                                }

                            }

                        }

                    }

                }

            }

        }

        private static String buildOuterInnerCore (LevelAccessor level_accessor, Entity entity, String type, double half_thickness, BlockPos pos, double build_area) {

            Block previous_block = level_accessor.getBlockState(pos).getBlock();
            String block = "";

            if (GameUtils.block.isTaggedAs(previous_block.defaultBlockState(), "tanshugetrees:block_placer_blacklist_" + type) == false) {

                // Get Type
                {

                    double outer_level = GameUtils.nbt.entity.getNumber(entity, type + "_outer_level");
                    double inner_level = GameUtils.nbt.entity.getNumber(entity, type + "_inner_level");
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

                    double outer_area = half_thickness - outer_level_area;
                    double inner_area = outer_area - inner_level_area;

                    // Applying
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

                            // Make inner and core blend. Do not make inner become hollow when chance is false, but swap to use core instead.
                            block = "core";

                        }

                    } else {

                        if (outer_level >= 1 || Math.random() < outer_level) {

                            block = "outer";

                        }

                    }

                }

                // Replace
                {

                    Block block_outer = GameUtils.block.fromText("tanshugetrees:block_placer_" + type + "_outer").getBlock();
                    Block block_inner = GameUtils.block.fromText("tanshugetrees:block_placer_" + type + "_inner").getBlock();
                    Block block_core = GameUtils.block.fromText("tanshugetrees:block_placer_" + type + "_core").getBlock();

                    if (block.equals("outer") == true) {

                        if (previous_block == block_outer || previous_block == block_inner || previous_block == block_core) {

                            block = "";

                        }

                    } else if (block.equals("inner") == true) {

                        if (previous_block == block_inner || previous_block == block_core) {

                            block = "";

                        }

                    } else if (block.equals("core") == true) {

                        if (previous_block == block_core) {

                            block = "";

                        }

                    }

                }

            }

            return block;

        }

        private static boolean buildTestKeep (LevelAccessor level_accessor, BlockPos pos, boolean replace) {

            if (replace == false && GameUtils.block.isTaggedAs(level_accessor.getBlockState(pos), "tanshugetrees:passable_blocks") == false) {

                return false;

            }

            return true;

        }

        private static void buildPlaceBlock (LevelAccessor level_accessor, ServerLevel level_server, Entity entity, BlockPos pos, String type, String block_type) {

            String type_short = type.substring(0, 2);
            String block_placer = "";
            String block = "";

            if (type.equals("leaves") == false) {

                type_short = type_short + block_type.substring(0, 1);
                block_placer = type + "_" + block_type;
                block = block_placer;

            } else {

                type_short = type_short + block_type;
                block_placer = "leaves_" + block_type;
                block = "leaves" + block_type;

            }

            level_accessor.setBlock(pos, GameUtils.block.fromText("tanshugetrees:block_placer_" + block_placer), 2);
            GameUtils.command.run(level_server, pos.getX(), pos.getY(), pos.getZ(), "particle flash ~ ~ ~ 0 0 0 0 1 force");

            String[] function = buildGetWayFunction(entity, type);
            GameUtils.nbt.block.setText(level_accessor, pos, "function", function[1]);
            GameUtils.nbt.block.setText(level_accessor, pos, "block", GameUtils.nbt.entity.getText(entity, block));

            if (TanshugetreesModVariables.MapVariables.get(level_accessor).shape_file_converter == true) {

                GameUtils.nbt.block.setText(level_accessor, pos, "export_file_name", GameUtils.nbt.entity.getText(entity, "export_file_name"));
                GameUtils.nbt.block.setNumber(level_accessor, pos, "center_posX", entity.getBlockX());
                GameUtils.nbt.block.setNumber(level_accessor, pos, "center_posY", entity.getBlockY());
                GameUtils.nbt.block.setNumber(level_accessor, pos, "center_posZ", entity.getBlockZ());
                GameUtils.nbt.block.setText(level_accessor, pos, "type_short", type_short);
                GameUtils.nbt.block.setText(level_accessor, pos, "function_short", function[0]);

            }

        }

        private static String[] buildGetWayFunction (Entity entity, String type) {

            String[] return_text = new String[]{"", ""};

            for (int number = 1; number <= 3; number++) {

                String function = "function_way" + number;
                String function_path = GameUtils.nbt.entity.getText(entity, function);
                String at_type = GameUtils.nbt.entity.getText(entity, function + "_type");

                if (function_path.equals("") == false && at_type.equals(type)) {

                    if (Math.random() < GameUtils.nbt.entity.getNumber(entity, function + "_chance")) {

                        if (GameUtils.nbt.entity.getNumber(entity, function + "_max") >= 0) {

                            double range_min = GameUtils.nbt.entity.getNumber(entity, function + "_range_min") * 0.01;
                            double range_max = GameUtils.nbt.entity.getNumber(entity, function + "_range_max") * 0.01;
                            double length_percent = 1.0;

                            if (GameUtils.nbt.entity.getNumber(entity, at_type + "_length_save") > 0) {

                                length_percent = 1.0 - (GameUtils.nbt.entity.getNumber(entity, at_type + "_length") / GameUtils.nbt.entity.getNumber(entity, at_type + "_length_save"));

                            }

                            if (range_min <= length_percent && length_percent <= range_max) {

                                // Count Limitation
                                {

                                    if (GameUtils.nbt.entity.getNumber(entity, function + "_max") > 0) {

                                        GameUtils.nbt.entity.addNumber(entity, function + "_max", -1);

                                        if (GameUtils.nbt.entity.getNumber(entity, function + "_max") == 0) {

                                            GameUtils.nbt.entity.setNumber(entity, function + "_max", -1);

                                        }

                                    }

                                }

                                return_text[0] = "f" + number;
                                return_text[1] = function_path;
                                break;

                            }

                        }

                    }

                }

            }

            return return_text;

        }

        private static void end (LevelAccessor level_accessor, ServerLevel level_server, Entity entity, String id) {

            GameUtils.command.runEntity(entity, "summon firework_rocket ~20 ~10 ~20 {LifeTime:40,FireworksItem:{id:firework_rocket,Count:1,tag:{Fireworks:{Flight:2,Explosions:[{Type:4,Flicker:1,Trail:1,Colors:[I;3887386,4312372],FadeColors:[I;3887386,4312372]}]}}}}");
            GameUtils.command.runEntity(entity, "summon firework_rocket ~20 ~10 ~-20 {LifeTime:40,FireworksItem:{id:firework_rocket,Count:1,tag:{Fireworks:{Flight:2,Explosions:[{Type:4,Flicker:1,Trail:1,Colors:[I;3887386,4312372],FadeColors:[I;3887386,4312372]}]}}}}");
            GameUtils.command.runEntity(entity, "summon firework_rocket ~-20 ~10 ~20 {LifeTime:40,FireworksItem:{id:firework_rocket,Count:1,tag:{Fireworks:{Flight:2,Explosions:[{Type:4,Flicker:1,Trail:1,Colors:[I;3887386,4312372],FadeColors:[I;3887386,4312372]}]}}}}");
            GameUtils.command.runEntity(entity, "summon firework_rocket ~-20 ~10 ~-20 {LifeTime:40,FireworksItem:{id:firework_rocket,Count:1,tag:{Fireworks:{Flight:2,Explosions:[{Type:4,Flicker:1,Trail:1,Colors:[I;3887386,4312372],FadeColors:[I;3887386,4312372]}]}}}}");

            if (TanshugetreesModVariables.MapVariables.get(level_accessor).shape_file_converter == false) {

                if (Math.random() < GameUtils.nbt.entity.getNumber(entity, "function_end_chance")) {

                    TreeFunction.start(level_accessor, level_server, entity.getBlockX(), entity.getBlockY(), entity.getBlockZ(), GameUtils.nbt.entity.getText(entity, "function_end"), false);

                }

            } else {

                ShapeFileConverter.whenTreeEnd(level_accessor, level_server, entity);

            }

            GameUtils.command.run(level_server, 0, 0, 0, "kill @e[tag=TANSHUGETREES-" + id + "]");

        }

    }

}
