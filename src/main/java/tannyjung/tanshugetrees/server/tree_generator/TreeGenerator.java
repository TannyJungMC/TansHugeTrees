package tannyjung.tanshugetrees.server.tree_generator;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import tannyjung.core.game.NBTManager;
import tannyjung.core.game.TXTFunction;
import tannyjung.core.game.Utils;
import tannyjung.tanshugetrees_mcreator.network.TanshugetreesModVariables;
import tannyjung.tanshugetrees.config.ConfigMain;

public class TreeGenerator {

    public static void start (LevelAccessor level_accessor, Entity entity) {

        ServerLevel level_server = (ServerLevel) level_accessor;
        Utils.command.runEntity(entity, "particle composter ~ ~ ~ 0 0 0 0 1 force");

        if (NBTManager.entity.getLogic(entity, "start") == false) {

            NBTManager.entity.setLogic(entity, "start", true);
            beforeRunSystem(level_accessor, level_server, entity);

        } else {

            NBTManager.entity.addNumber(entity, "tree_generator_speed_tick_test", 1);

            if (NBTManager.entity.getNumber(entity, "tree_generator_speed_tick_test") >= NBTManager.entity.getNumber(entity, "tree_generator_speed_tick")) {

                NBTManager.entity.setNumber(entity, "tree_generator_speed_tick_test", 0);
                runSystem(level_accessor, level_server, entity);

            }

        }

    }

    private static void beforeRunSystem (LevelAccessor level_accessor, ServerLevel level_server, Entity entity) {

        Utils.command.runEntity(entity, "kill @e[name=TANSHUGETREES-tree_countdown,distance=..1]");

        if (TanshugetreesModVariables.MapVariables.get(level_accessor).shape_file_converter == false) {

            Utils.command.runEntity(entity, "tp ~ ~" + NBTManager.entity.getNumber(entity, "start_height") + " ~");

        }

        NBTManager.entity.setText(entity, "id", entity.getUUID().toString());
        Utils.command.runEntity(entity, "tag @s add TANSHUGETREES-" + NBTManager.entity.getText(entity, "id"));
        NBTManager.entity.setText(entity, "type", "taproot");
        NBTManager.entity.setText(entity, "step", "summon");
        NBTManager.entity.setNumber(entity, "taproot_count", Mth.nextInt(RandomSource.create(), (int) NBTManager.entity.getNumber(entity, "taproot_count_min"), (int) NBTManager.entity.getNumber(entity, "taproot_count_max")));
        NBTManager.entity.setNumber(entity, "trunk_count", Mth.nextInt(RandomSource.create(), (int) NBTManager.entity.getNumber(entity, "trunk_count_min"), (int) NBTManager.entity.getNumber(entity, "trunk_count_max")));

        // Debug Mode
        {

            if (NBTManager.entity.getLogic(entity, "debug_mode") == true) {

                NBTManager.entity.setText(entity, "taproot_outer", "purple_concrete");
                NBTManager.entity.setText(entity, "taproot_inner", "purple_terracotta");
                NBTManager.entity.setText(entity, "taproot_core", "purple_stained_glass");
                NBTManager.entity.setText(entity, "secondary_root_outer", "magenta_concrete");
                NBTManager.entity.setText(entity, "secondary_root_inner", "magenta_terracotta");
                NBTManager.entity.setText(entity, "secondary_root_core", "magenta_stained_glass");
                NBTManager.entity.setText(entity, "tertiary_root_outer", "pink_concrete");
                NBTManager.entity.setText(entity, "tertiary_root_inner", "pink_terracotta");
                NBTManager.entity.setText(entity, "tertiary_root_core", "pink_stained_glass");
                NBTManager.entity.setText(entity, "fine_root_outer", "light_blue_concrete");
                NBTManager.entity.setText(entity, "fine_root_inner", "light_blue_terracotta");
                NBTManager.entity.setText(entity, "fine_root_core", "light_blue_stained_glass");

                NBTManager.entity.setText(entity, "trunk_outer", "red_concrete");
                NBTManager.entity.setText(entity, "trunk_inner", "red_terracotta");
                NBTManager.entity.setText(entity, "trunk_core", "red_stained_glass");
                NBTManager.entity.setText(entity, "bough_outer", "orange_concrete");
                NBTManager.entity.setText(entity, "bough_inner", "orange_terracotta");
                NBTManager.entity.setText(entity, "bough_core", "orange_stained_glass");
                NBTManager.entity.setText(entity, "branch_outer", "yellow_concrete");
                NBTManager.entity.setText(entity, "branch_inner", "yellow_terracotta");
                NBTManager.entity.setText(entity, "branch_core", "yellow_stained_glass");
                NBTManager.entity.setText(entity, "limb_outer", "lime_concrete");
                NBTManager.entity.setText(entity, "limb_inner", "lime_terracotta");
                NBTManager.entity.setText(entity, "limb_core", "lime_stained_glass");
                NBTManager.entity.setText(entity, "twig_outer", "green_concrete");
                NBTManager.entity.setText(entity, "twig_inner", "green_terracotta");
                NBTManager.entity.setText(entity, "twig_core", "green_stained_glass");
                NBTManager.entity.setText(entity, "sprig_outer", "white_concrete");
                NBTManager.entity.setText(entity, "sprig_inner", "white_terracotta");
                NBTManager.entity.setText(entity, "sprig_core", "white_stained_glass");

                NBTManager.entity.setText(entity, "leaves1", "white_stained_glass");
                NBTManager.entity.setText(entity, "leaves2", "black_stained_glass");

            }

        }

        // No Roots
        {

            if (NBTManager.entity.getNumber(entity, "taproot_count") == 0) {

                NBTManager.entity.setText(entity, "type", "trunk");

            }

        }

        // Summon Status Display
        Utils.command.runEntity(entity, "execute positioned ~ ~1 ~ run " + Utils.command.summonEntity("text_display",  "TANSHUGETREES / TANSHUGETREES-" + NBTManager.entity.getText(entity, "id") + " / TANSHUGETREES-tree_generator_status", "Tree Generator Status", "see_through:1b,alignment:\"left\",brightness:{block:15},line_width:1000,transformation:{left_rotation:[0f,0f,0f,1f],right_rotation:[0f,0f,0f,1f],translation:[0f,0f,0f],scale:[1f,1f,1f]},billboard:vertical,text:'{\"text\":\"In Progress...\",\"color\":\"white\"}'"));

        if (TanshugetreesModVariables.MapVariables.get(level_accessor).shape_file_converter == false) {

            TXTFunction.start(level_accessor, level_server, RandomSource.create(), entity.getBlockX(), entity.getBlockY(), entity.getBlockZ(), NBTManager.entity.getText(entity, "function_start"));

        } else {

            ShapeFileConverter.whenTreeStart(level_server, entity);

        }

    }

    private static void runSystem (LevelAccessor level_accessor, ServerLevel level_server, Entity entity) {

        String id = NBTManager.entity.getText(entity, "id");
        String type = NBTManager.entity.getText(entity, "type");
        String step = NBTManager.entity.getText(entity, "step");
        String[] type_pre_next = new String[0];

        // Status Display
        {

            StringBuilder command = new StringBuilder();

            command
                    .append("Total Processes : ").append((int) NBTManager.entity.getNumber(entity, "total_processes"))
                    .append("\n")
                    .append("Generating : ").append(type)
                    .append("\n")
                    .append("\n")
                    .append("Step : ").append(step)
                    .append("\n")
                    .append("Count : ").append((int) NBTManager.entity.getNumber(entity, type + "_count"))
                    .append("\n")
                    .append("Length : ").append((int) NBTManager.entity.getNumber(entity, type + "_length")).append(" / ").append((int) NBTManager.entity.getNumber(entity, type + "_length_save"))
                    .append("\n")
                    .append("Thickness : ").append(NBTManager.entity.getNumber(entity, type + "_thickness")).append(" / ").append(NBTManager.entity.getNumber(entity, type + "_thickness_start"))
            ;

            Utils.command.runEntity(entity, "execute positioned ~ ~1 ~ run data merge entity @e[tag=TANSHUGETREES-tree_generator_status,distance=..1,limit=1,sort=nearest] {text:'{\"text\":\"" + command + "\",\"color\":\"white\"}'}");

        }

        if (ConfigMain.tree_generator_speed_global == true && NBTManager.entity.getLogic(entity, "tree_generator_speed_global") == true) {

            NBTManager.entity.setNumber(entity, "tree_generator_speed_tick", ConfigMain.tree_generator_speed_tick);
            NBTManager.entity.setNumber(entity, "tree_generator_speed_repeat", ConfigMain.tree_generator_speed_repeat);

        }

        while (true) {

            type = NBTManager.entity.getText(entity, "type");
            step = NBTManager.entity.getText(entity, "step");
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

            NBTManager.entity.addNumber(entity, "total_processes", 1);

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

        if (NBTManager.entity.getNumber(entity, "tree_generator_speed_repeat") != 0) {

            if (NBTManager.entity.getNumber(entity, "tree_generator_speed_repeat_test") < NBTManager.entity.getNumber(entity, "tree_generator_speed_repeat")) {

                NBTManager.entity.addNumber(entity, "tree_generator_speed_repeat_test", 1);

            } else {

                NBTManager.entity.setNumber(entity, "tree_generator_speed_repeat_test", 0);
                return_logic = true;

            }

        }

        return return_logic;

    }

    private static String[] typePreNext (Entity entity) {

        String[] return_text = new String[2];
        String type = NBTManager.entity.getText(entity, "type");

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

            boolean is_taproot_trunk = type.equals("taproot") == true || type.equals("trunk") == true;

            if (type.equals("leaves") == false) {

                // Length and Thickness
                {

                    double length = Mth.nextInt(RandomSource.create(), (int) NBTManager.entity.getNumber(entity, type + "_length_min"), (int) NBTManager.entity.getNumber(entity, type + "_length_max"));
                    length = Math.ceil(length * summonReduction(entity, type, "length_reduce"));
                    NBTManager.entity.setNumber(entity, type + "_length", length);
                    NBTManager.entity.setNumber(entity, type + "_length_save", length);

                    double thickness = NBTManager.entity.getNumber(entity, type + "_thickness_start") - NBTManager.entity.getNumber(entity, type + "_thickness_end");
                    thickness = thickness * summonReduction(entity, type, "thickness_reduce");
                    thickness = NBTManager.entity.getNumber(entity, type + "_thickness_end") + thickness;
                    NBTManager.entity.setNumber(entity, type + "_thickness", thickness);

                }

                if (type_pre_next[1].equals("leaves") == false) {

                    // Next Part Settings
                    {

                        int count = Mth.nextInt(RandomSource.create(), (int) NBTManager.entity.getNumber(entity, type_pre_next[1] + "_count_min"), (int) NBTManager.entity.getNumber(entity, type_pre_next[1] + "_count_max"));
                        count = (int) Math.ceil((double) count * summonReduction(entity, type_pre_next[1], "count_reduce"));
                        NBTManager.entity.setNumber(entity, type_pre_next[1] + "_count", count);
                        NBTManager.entity.setNumber(entity, type_pre_next[1] + "_count_save", count);

                    }

                    // Auto Distance
                    {

                        if (NBTManager.entity.getNumber(entity, type_pre_next[1] + "_count") > 0) {

                            double distance = 0.0;

                            if (NBTManager.entity.getLogic(entity, type_pre_next[1] + "_chance_auto") == true) {

                                double count_available = NBTManager.entity.getNumber(entity, type_pre_next[1] + "_count_save") - NBTManager.entity.getNumber(entity, type_pre_next[1] + "_min_last_count");

                                if (count_available > 0) {

                                    double length_percent = Math.ceil(NBTManager.entity.getNumber(entity, type + "_length_save") * (NBTManager.entity.getNumber(entity, type_pre_next[1] + "_chance_percent") * 0.01));
                                    distance = length_percent / count_available;

                                }

                            }

                            NBTManager.entity.setNumber(entity, type_pre_next[1] + "_chance_auto_distance", distance);
                            NBTManager.entity.setNumber(entity, type_pre_next[1] + "_chance_distance_left", 0);

                        }

                    }

                } else {

                    // Leaves Settings
                    {

                        NBTManager.entity.setNumber(entity, "leaves_count", 1);
                        NBTManager.entity.setNumber(entity, "leaves_length", 1);
                        NBTManager.entity.setNumber(entity, "leaves_length_save", 1);

                        double size = Mth.nextDouble(RandomSource.create(), NBTManager.entity.getNumber(entity, "leaves_size_min"), NBTManager.entity.getNumber(entity, "leaves_size_max"));
                        size = size * summonReduction(entity, "leaves", "size_reduce");
                        NBTManager.entity.setNumber(entity, "leaves_size", size);

                    }

                }

                // Chance of Discontinue
                {

                    if (is_taproot_trunk == false) {

                        double chance = NBTManager.entity.getNumber(entity, type + "_continue_chance");
                        chance = chance * summonReduction(entity, type, "continue_reduce");
                        chance = 1.0 - chance;

                        if (Math.random() < chance) {

                            NBTManager.entity.setNumber(entity, type_pre_next[1] + "_count", 0);

                        }

                    }

                }

            }

            // Summoning
            {

                String at_part = "";
                String positioned = "";

                // Position
                {

                    if (is_taproot_trunk == true) {

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

                                if (NBTManager.entity.getText(entity, type + "_center_direction_from").equals("") == true) {

                                    // Non Center Direction
                                    {

                                        if (NBTManager.entity.getNumber(entity, type + "_min_last_count") > 0 && NBTManager.entity.getNumber(entity, type + "_count") <= 1) {

                                            // Make last part facing same as previous part
                                            forward = 1;

                                        } else {

                                            vertical = NBTManager.entity.getNumber(entity, type + "_start_vertical");
                                            horizontal = NBTManager.entity.getNumber(entity, type + "_start_horizontal");
                                            height = Mth.nextDouble(RandomSource.create(), NBTManager.entity.getNumber(entity, type + "_start_height_min"), NBTManager.entity.getNumber(entity, type + "_start_height_max"));
                                            forward = Mth.nextDouble(RandomSource.create(), NBTManager.entity.getNumber(entity, type + "_start_forward_min"), NBTManager.entity.getNumber(entity, type + "_start_forward_max"));

                                        }

                                    }

                                } else {

                                    // Center Direction
                                    {

                                        int length = (int) NBTManager.entity.getNumber(entity, type_pre_next[0] + "_length");
                                        int length_save = (int) NBTManager.entity.getNumber(entity, type_pre_next[0] + "_length_save");
                                        double center = NBTManager.entity.getNumber(entity, type + "_center_direction_center") * 0.01;
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

                                        vertical = 1.0 + ((NBTManager.entity.getNumber(entity, type + "_center_direction_vertical_" + above_or_below) - 1.0) * percent);
                                        horizontal = 1.0 + ((NBTManager.entity.getNumber(entity, type + "_center_direction_horizontal_" + above_or_below) - 1.0) * percent);
                                        height = NBTManager.entity.getNumber(entity, type + "_center_direction_height_" + above_or_below) * percent;
                                        forward = NBTManager.entity.getNumber(entity, type + "_center_direction_forward_" + above_or_below) * percent;

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

                Utils.command.run(level_server, 0, 0, 0, "execute at @e[tag=TANSHUGETREES-" + id + ",tag=TANSHUGETREES-" + at_part + "] " + positioned + " run " + Utils.command.summonEntity("marker", "TANSHUGETREES / TANSHUGETREES-" + id + " / TANSHUGETREES-generator_" + type, "Tree Generator - Part", ""));

                if (is_taproot_trunk == true) {

                    int direction = Mth.nextInt(RandomSource.create(), (int) NBTManager.entity.getNumber(entity, type + "_start_direction_min"), (int) NBTManager.entity.getNumber(entity, type + "_start_direction_max"));
                    int gravity = Mth.nextInt(RandomSource.create(), (int) NBTManager.entity.getNumber(entity, type + "_start_gravity_max"), (int) NBTManager.entity.getNumber(entity, type + "_start_gravity_min"));
                    Utils.command.run(level_server, 0, 0, 0, "execute as @e[tag=TANSHUGETREES-" + id + ",tag=TANSHUGETREES-generator_" + type + "] at @s run tp @s ~ ~ ~ " + direction + " " + gravity);

                } else {

                    String type_current = "tag=TANSHUGETREES-" + id + ",tag=TANSHUGETREES-generator_" + type;
                    String type_pre = "tag=TANSHUGETREES-" + id + ",tag=TANSHUGETREES-generator_" + type_pre_next[0];
                    Utils.command.run(level_server, 0, 0, 0, "execute as @e[" + type_current + "] at @s facing entity @e[" + type_pre + ",limit=1] feet positioned as @e[" + type_pre + ",limit=1] run tp @s ~ ~ ~ facing ^ ^ ^-1");

                }

            }

            // Next Step
            {

                if (is_taproot_trunk == true) {

                    NBTManager.entity.setText(entity, "step", "build");

                } else {

                    NBTManager.entity.setText(entity, "step", "calculation");

                }

            }

            NBTManager.entity.addNumber(entity, type + "_count", -1);

        }

        private static double summonReduction (Entity entity, String type, String step) {

            double return_number = 1.0;
            String from = NBTManager.entity.getText(entity, type + "_" + step + "_from");

            if (from.equals("") == false) {

                int length = (int) NBTManager.entity.getNumber(entity, from + "_length");
                int length_save = (int) NBTManager.entity.getNumber(entity, from + "_length_save");
                double center = NBTManager.entity.getNumber(entity, type + "_" + step + "_center") * 0.01;
                int length_below = (int) ((double) length_save * center);
                int length_above = length_save - length_below;

                String above_below = "";
                double percent = 0.0;

                if ((1.0 - center) >= ((double) length / (double) length_save)) {

                    // Above
                    above_below = "above";
                    percent = (double) length / (double) length_above;

                } else {

                    // Below
                    above_below = "below";
                    percent = 1.0 - ((double) (length - length_above) / (double) length_below);

                }

                double start = NBTManager.entity.getNumber(entity, type + "_" + step + "_" + above_below + "_start");
                double end = NBTManager.entity.getNumber(entity, type + "_" + step + "_" + above_below + "_end");
                return_number = (start - end) * percent;
                return_number = end + return_number;
                return_number = return_number * 0.01;

            }

            return return_number;
        }

        private static void calculation (ServerLevel level_server, Entity entity, String id, String type, String[] type_pre_next) {

            Utils.command.runEntity(entity, "data modify entity @s ForgeData.previous_pos set from entity @e[tag=TANSHUGETREES-" + id + ",tag=TANSHUGETREES-generator_" + type + ",limit=1] Pos");
            double[] pos = NBTManager.entity.getListNumber(entity, "previous_pos");
            NBTManager.entity.setNumber(entity, "previous_posX", pos[0]);
            NBTManager.entity.setNumber(entity, "previous_posY", pos[1]);
            NBTManager.entity.setNumber(entity, "previous_posZ", pos[2]);

            boolean go_next = false;

            // Test
            {

                if (NBTManager.entity.getNumber(entity, type + "_length") > 0) {

                    if (NBTManager.entity.getNumber(entity, type_pre_next[1] + "_count") > NBTManager.entity.getNumber(entity, type_pre_next[1] + "_min_last_count")) {

                        // Length Range
                        if (NBTManager.entity.getNumber(entity, type + "_length") / NBTManager.entity.getNumber(entity, type + "_length_save") <= NBTManager.entity.getNumber(entity, type_pre_next[1] + "_chance_percent") * 0.01) {

                            if (NBTManager.entity.getNumber(entity, type_pre_next[1] + "_chance_distance_left") > 0) {

                                NBTManager.entity.addNumber(entity, type_pre_next[1] + "_chance_distance_left", -1);

                            } else {

                                NBTManager.entity.setNumber(entity, type_pre_next[1] + "_chance_distance_left", NBTManager.entity.getNumber(entity, type_pre_next[1] + "_chance_auto_distance"));

                                // Chance
                                if (Math.random() < NBTManager.entity.getNumber(entity, type_pre_next[1] + "_chance")) {

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

                    if (type.equals("leaves") == false) {

                        // Curvature
                        {

                            double vertical = NBTManager.entity.getNumber(entity, type + "_curvature_vertical");
                            double horizontal = NBTManager.entity.getNumber(entity, type + "_curvature_horizontal");

                            if (vertical != 0 || horizontal != 0) {

                                vertical = Mth.nextDouble(RandomSource.create(), -(vertical), vertical);
                                horizontal = Mth.nextDouble(RandomSource.create(), -(horizontal), horizontal);
                                Utils.command.run(level_server, 0, 0, 0, "execute as @e[tag=TANSHUGETREES-" + id + ",tag=TANSHUGETREES-generator_" + type + "] at @s run tp @s ~ ~ ~ ~" + horizontal + " ~" + vertical);

                            }

                        }

                        boolean gravity_run = false;

                        // Gravity
                        {

                            int weightiness = (int) NBTManager.entity.getNumber(entity, type + "_gravity_weightiness");

                            if (weightiness != 0) {

                                int min = (int) NBTManager.entity.getNumber(entity, type + "_gravity_min");
                                int max = (int) NBTManager.entity.getNumber(entity, type + "_gravity_max");

                                if (Utils.command.result(level_server, 0, 0, 0, "execute if entity @e[tag=TANSHUGETREES-" + id + ",tag=TANSHUGETREES-generator_" + type + ",x_rotation=" + min + "..90]") == true) {

                                    Utils.command.run(level_server, 0, 0, 0, "execute as @e[tag=TANSHUGETREES-" + id + ",tag=TANSHUGETREES-generator_" + type + "] at @s run tp @s ~ ~ ~ ~ ~-" + weightiness);
                                    gravity_run = true;

                                }

                                if (Utils.command.result(level_server, 0, 0, 0, "execute if entity @e[tag=TANSHUGETREES-" + id + ",tag=TANSHUGETREES-generator_" + type + ",x_rotation=-90.." + max + "]") == true) {

                                    Utils.command.run(level_server, 0, 0, 0, "execute as @e[tag=TANSHUGETREES-" + id + ",tag=TANSHUGETREES-generator_" + type + "] at @s run tp @s ~ ~ ~ ~ ~" + weightiness);
                                    gravity_run = true;

                                }

                            }

                        }

                        if (gravity_run == false) {

                            // Centripetal
                            {

                                double centripetal = NBTManager.entity.getNumber(entity, type + "_centripetal") * 0.01;

                                if (centripetal != 0.0) {

                                    String old_pos = pos[0] + " " + pos[1] + " " + pos[2];
                                    Utils.command.run(level_server, 0, 0, 0, "execute as @e[tag=TANSHUGETREES-" + id + ",tag=TANSHUGETREES-generator_" + type + "] at @s run tp @s ^ ^ ^1");
                                    Utils.command.run(level_server, 0, 0, 0, "execute as @e[tag=TANSHUGETREES-" + id + ",tag=TANSHUGETREES-generator_" + type + "] at @s facing entity @e[tag=TANSHUGETREES-" + id + ",tag=TANSHUGETREES-generator_trunk] feet run tp @s ^ ^ ^-" + centripetal);
                                    Utils.command.run(level_server, 0, 0, 0, "execute as @e[tag=TANSHUGETREES-" + id + ",tag=TANSHUGETREES-generator_" + type + "] at @s facing " + old_pos + " positioned " + old_pos + " run tp @s ~ ~ ~ facing ^ ^ ^-1");

                                }

                            }

                        }

                        double thickness = NBTManager.entity.getNumber(entity, type + "_thickness_start") - NBTManager.entity.getNumber(entity, type + "_thickness_end");
                        double length_percent = NBTManager.entity.getNumber(entity, type + "_length") / NBTManager.entity.getNumber(entity, type + "_length_save");
                        thickness = NBTManager.entity.getNumber(entity, type + "_thickness_end") + (thickness * length_percent);
                        NBTManager.entity.setNumber(entity, type + "_thickness", thickness);

                    }

                    Utils.command.run(level_server, 0, 0, 0, "execute as @e[tag=TANSHUGETREES-" + id + ",tag=TANSHUGETREES-generator_" + type + "] at @s run tp @s ^ ^ ^1");
                    NBTManager.entity.addNumber(entity, type + "_length", -1);
                    NBTManager.entity.setText(entity, "step", "build");

                }

            } else {

                // Next Part
                {

                    if (NBTManager.entity.getNumber(entity, type_pre_next[1] + "_count") > 0) {

                        // Up
                        {

                            NBTManager.entity.setText(entity, "type", type_pre_next[1]);
                            NBTManager.entity.setText(entity, "step", "summon");

                        }

                    } else {

                        // Down
                        {

                            Utils.command.runEntity(entity, "kill @e[tag=TANSHUGETREES-" + id + ",tag=TANSHUGETREES-generator_" + type + "]");

                            if (type.equals("taproot") == true) {

                                if (NBTManager.entity.getNumber(entity, type + "_count") > 0) {

                                    NBTManager.entity.setText(entity, "step", "summon");

                                } else {

                                    if (NBTManager.entity.getNumber(entity, type_pre_next[0] + "_count") > 0) {

                                        NBTManager.entity.setText(entity, "step", "summon");
                                        NBTManager.entity.setText(entity, "type", type_pre_next[0]);

                                    } else {

                                        NBTManager.entity.setText(entity, "step", "end");

                                    }

                                }

                            } else if (type.equals("trunk") == true) {

                                if (NBTManager.entity.getNumber(entity, type + "_count") > 0) {

                                    NBTManager.entity.setText(entity, "step", "summon");

                                } else {

                                    NBTManager.entity.setText(entity, "step", "end");

                                }

                            } else {

                                NBTManager.entity.setText(entity, "type", type_pre_next[0]);

                            }

                        }

                    }

                }

            }

        }

        private static void build (LevelAccessor level_accessor, ServerLevel level_server, Entity entity, String id, String type) {

            double thickness = NBTManager.entity.getNumber(entity, type + "_thickness");
            double size = 0.0;

            // Get Size and Radius
            {

                if (type.equals("leaves") == false) {

                    size = thickness;

                } else {

                    size = NBTManager.entity.getNumber(entity, "leaves_size");

                }

            }

            if (size > 0) {

                size = size - 1.0;

                if (size < 0) {

                    size = 0;

                }

                double radius = size * 0.5;
                double radius_ceil = Math.ceil(radius);
                String generator_type = NBTManager.entity.getText(entity, type + "_generator_type");

                // First Settings
                {

                    if (NBTManager.entity.getLogic(entity, "still_building") == false) {

                        NBTManager.entity.setLogic(entity, "still_building", true);

                        // Get Center Pos
                        {

                            Utils.command.runEntity(entity, "data modify entity @s ForgeData.build_center set from entity @e[tag=TANSHUGETREES-" + id + ",tag=TANSHUGETREES-generator_" + type + ",limit=1] Pos");
                            double[] pos = NBTManager.entity.getListNumber(entity, "build_center");
                            NBTManager.entity.setNumber(entity, "build_centerX", pos[0]);
                            NBTManager.entity.setNumber(entity, "build_centerY", pos[1]);
                            NBTManager.entity.setNumber(entity, "build_centerZ", pos[2]);

                        }

                        // Sphere Zone
                        {

                            if (generator_type.equals("sphere_zone") == true) {

                                Utils.command.runEntity(entity, "data modify entity @s ForgeData.builder_yaw_pitch set from entity @e[tag=TANSHUGETREES-" + id + ",tag=TANSHUGETREES-generator_sprig,limit=1] Rotation");
                                double[] yaw_pitch = NBTManager.entity.getListNumberFloat(entity, "builder_yaw_pitch");
                                double yaw = Math.toRadians(((yaw_pitch[0] + 180) + 90) % 360);
                                double pitch = yaw_pitch[1];

                                // Min Pitch
                                {

                                    int pitch_min = (int) NBTManager.entity.getNumber(entity, type + "_sphere_zone_pitch_min");

                                    if (pitch > pitch_min) {

                                        pitch = pitch_min;

                                    }

                                }

                                pitch = Math.toRadians(pitch);
                                double distance = radius;
                                int sphere_zone_size = (int) NBTManager.entity.getNumber(entity, type + "_sphere_zone_size");

                                NBTManager.entity.setNumber(entity, "sphere_zone_posX", distance * Math.cos(pitch) * Math.cos(yaw));
                                NBTManager.entity.setNumber(entity, "sphere_zone_posY", distance * Math.sin(pitch));
                                NBTManager.entity.setNumber(entity, "sphere_zone_posZ", distance * Math.cos(pitch) * Math.sin(yaw));

                                // Change Build Center
                                {

                                    distance = distance - sphere_zone_size;
                                    NBTManager.entity.addNumber(entity, "build_centerX", distance * Math.cos(pitch) * Math.cos(yaw));
                                    NBTManager.entity.addNumber(entity, "build_centerY", distance * Math.sin(pitch));
                                    NBTManager.entity.addNumber(entity, "build_centerZ", distance * Math.cos(pitch) * Math.sin(yaw));

                                }

                                // Get Area
                                {

                                    double sphere_zone_area = size - sphere_zone_size;

                                    if (sphere_zone_area < 0) {

                                        sphere_zone_area = 0;

                                    }

                                    NBTManager.entity.setNumber(entity, "sphere_zone_area", sphere_zone_area * sphere_zone_area);

                                }

                            }

                        }

                        NBTManager.entity.setNumber(entity, "build_saveX", -(radius));
                        NBTManager.entity.setNumber(entity, "build_saveY", -(radius));
                        NBTManager.entity.setNumber(entity, "build_saveZ", -(radius));

                    }

                }

                double sphere_zone_area = 0.0;
                double[] sphere_zone_pos = new double[0];

                if (generator_type.equals("sphere_zone") == true) {

                    sphere_zone_area = NBTManager.entity.getNumber(entity, "sphere_zone_area");
                    sphere_zone_pos = new double[]{NBTManager.entity.getNumber(entity, "sphere_zone_posX"), NBTManager.entity.getNumber(entity, "sphere_zone_posY"), NBTManager.entity.getNumber(entity, "sphere_zone_posZ")};

                }

                double scan_change = radius / radius_ceil;

                if (radius == 0) {

                    scan_change = 1;

                }

                double[] center_pos = new double[]{NBTManager.entity.getNumber(entity, "build_centerX"), NBTManager.entity.getNumber(entity, "build_centerY"), NBTManager.entity.getNumber(entity, "build_centerZ")};
                boolean replace = NBTManager.entity.getLogic(entity, type + "_replace");
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

                        build_saveX = NBTManager.entity.getNumber(entity, "build_saveX");
                        build_saveY = NBTManager.entity.getNumber(entity, "build_saveY");
                        build_saveZ = NBTManager.entity.getNumber(entity, "build_saveZ");

                        if (build_saveY > scan_end) {

                            break;

                        } else {

                            if (build_saveX > scan_end) {

                                NBTManager.entity.setNumber(entity, "build_saveX", -(scan_start));
                                NBTManager.entity.addNumber(entity, "build_saveY", scan_change);

                            } else {

                                if (build_saveZ > scan_end) {

                                    NBTManager.entity.setNumber(entity, "build_saveZ", -(scan_start));
                                    NBTManager.entity.addNumber(entity, "build_saveX", scan_change);

                                } else {

                                    NBTManager.entity.addNumber(entity, "build_saveZ", scan_change);

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

                                    pos = new BlockPos((int) Math.floor(center_pos[0] + build_saveX), (int) Math.floor(center_pos[1] + build_saveY), (int) Math.floor(center_pos[2] + build_saveZ));

                                    // Place Block
                                    {

                                        if (type.equals("leaves") == false) {

                                            // General
                                            {

                                                if (size < 1 && build_saveX == 0 && build_saveY == 0 && build_saveZ == 0) {

                                                    buildBlockConnector(level_accessor, level_server, entity, center_pos, pos, type, generator_type, radius, build_area, replace);

                                                }

                                                String previous_block = buildGetPreviousBlock(level_accessor, pos, replace);
                                                String block_type = buildGetBlockType(entity, type, previous_block, radius, build_area);

                                                if (buildPlaceBlock(level_accessor, level_server, entity, pos, type, block_type, previous_block) == false) {

                                                    return;

                                                }

                                            }

                                        } else {

                                            // Leaves
                                            {

                                                if (Math.random() < NBTManager.entity.getNumber(entity, "leaves_density") * 0.01) {

                                                    String previous_block = "";
                                                    String block_type = "";
                                                    BlockPos pos_leaves = null;
                                                    int deep = 1;

                                                    if (Math.random() < NBTManager.entity.getNumber(entity, "leaves_straighten_chance")) {

                                                        deep = Mth.nextInt(RandomSource.create(), (int) NBTManager.entity.getNumber(entity, "leaves_straighten_min"), (int) NBTManager.entity.getNumber(entity, "leaves_straighten_max"));

                                                    }

                                                    for (int deep_test = 0; deep_test < deep; deep_test++) {

                                                        pos_leaves = new BlockPos(pos.getX(), pos.getY() - deep_test, pos.getZ());

                                                        if (Utils.block.isTaggedAs(level_accessor.getBlockState(pos_leaves), "tanshugetrees:block_placer_blacklist_leaves") == false && Utils.block.toTextID(level_accessor.getBlockState(pos_leaves)).startsWith("tanshugetrees:block_placer_leaves") == false) {

                                                            previous_block = buildGetPreviousBlock(level_accessor, pos, replace);
                                                            block_type = buildGetBlockType(entity, type, previous_block, radius, build_area);

                                                            if (buildPlaceBlock(level_accessor, level_server, entity, pos, type, block_type, previous_block) == false) {

                                                                return;

                                                            }

                                                        }

                                                    }

                                                    return;

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
            NBTManager.entity.setLogic(entity, "still_building", false);
            NBTManager.entity.setText(entity, "step", "calculation");

        }

        private static void buildBlockConnector (LevelAccessor level_accessor, ServerLevel level_server, Entity entity, double[] center_pos, BlockPos pos, String type, String generator_type, double radius, double build_area, boolean replace) {

            double block_connector_posX = NBTManager.entity.getNumber(entity, "previous_posX");
            double block_connector_posY = NBTManager.entity.getNumber(entity, "previous_posY");
            double block_connector_posZ = NBTManager.entity.getNumber(entity, "previous_posZ");
            int testX = (int) (Math.floor(center_pos[0]) - Math.floor(block_connector_posX));
            int testY = (int) (Math.floor(center_pos[1]) - Math.floor(block_connector_posY));
            int testZ = (int) (Math.floor(center_pos[2]) - Math.floor(block_connector_posZ));

            if (Math.abs(testX) == 1 || Math.abs(testY) == 1 || Math.abs(testZ) == 1) {

                // For X and Z
                {

                    if (Math.abs(testX) == 1 && Math.abs(testZ) == 1) {

                        if (center_pos[0] - block_connector_posX > center_pos[2] - block_connector_posZ) {

                            pos = new BlockPos(pos.getX() - testX, pos.getY(), pos.getZ());

                        } else {

                            pos = new BlockPos(pos.getX(), pos.getY(), pos.getZ() - testZ);

                        }

                        // Place Block
                        {

                            String previous_block = buildGetPreviousBlock(level_accessor, pos, replace);
                            String block_type = buildGetBlockType(entity, type, previous_block, radius, build_area);

                            if (buildPlaceBlock(level_accessor, level_server, entity, pos, type, block_type, previous_block) == false) {

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

                            String previous_block = buildGetPreviousBlock(level_accessor, pos, replace);
                            String block_type = buildGetBlockType(entity, type, previous_block, radius, build_area);

                            if (buildPlaceBlock(level_accessor, level_server, entity, pos, type, block_type, previous_block) == false) {

                                return;

                            }

                        }

                    }

                }

            }

        }

        private static String buildGetBlockType (Entity entity, String type, String previous_block, double radius, double build_area) {

            String block = "";

            if (type.equals("leaves") == false) {

                // General
                {

                    // Get Outer-Inner-Core
                    {

                        double outer_level = NBTManager.entity.getNumber(entity, type + "_outer_level");
                        double inner_level = NBTManager.entity.getNumber(entity, type + "_inner_level");
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

                                }

                            }

                        }

                    }

                    if (previous_block.equals("") == false) {

                        String type_short = type.substring(0, 2);
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

                    if (Math.random() < NBTManager.entity.getNumber(entity, "leaves2_chance")) {

                        block = "2";

                    } else {

                        block = "1";

                    }

                }

            }

            return block;

        }

        private static String buildGetPreviousBlock (LevelAccessor level_accessor, BlockPos pos, boolean replace) {

            String previous_block = "";

            {

                if (TanshugetreesModVariables.MapVariables.get(level_accessor).shape_file_converter == false) {

                    BlockState block = level_accessor.getBlockState(pos);

                    if (replace == true || block.isAir() == false) {

                        previous_block = Utils.block.toTextID(block);

                        if (previous_block.startsWith("tanshugetrees:block_placer_") == false) {

                            previous_block = "";

                        } else {

                            previous_block = previous_block.substring("tanshugetrees:block_placer_".length());
                            String type = previous_block.substring(0, 2);

                            if (previous_block.endsWith("outer") == true) {

                                previous_block = "o";

                            } else if (previous_block.endsWith("inner") == true) {

                                previous_block = "i";

                            } else if (previous_block.endsWith("core") == true) {

                                previous_block = "c";

                            }

                            previous_block = type + previous_block;

                        }

                    }

                } else {

                    String key = "B" + pos.getX() + "/" + pos.getY() + "/" + pos.getZ();
                    previous_block = ShapeFileConverter.export_data.getOrDefault(key, "");

                    if (replace == false && previous_block.equals("") == false) {

                        previous_block = "";

                    }

                }

            }

            return previous_block;

        }

        private static boolean buildPlaceBlock (LevelAccessor level_accessor, ServerLevel level_server, Entity entity, BlockPos pos, String type, String block_type, String previous_block) {

            if (block_type.equals("") == false) {

                String type_short = type.substring(0, 2);
                String block_placer = "";
                String block = "";

                if (type.equals("leaves") == false) {

                    type_short = type_short + block_type.charAt(0);
                    block_placer = type + "_" + block_type;
                    block = block_placer;

                } else {

                    type_short = type_short + block_type;
                    block_placer = "leaves_" + block_type;
                    block = "leaves" + block_type;

                }

                if (NBTManager.entity.getText(entity, block).equals("") == false) {

                    String[] function = buildGetWayFunction(entity, type);
                    boolean replacing = previous_block.equals("") == false;

                    if (TanshugetreesModVariables.MapVariables.get(level_accessor).shape_file_converter == false) {

                        Utils.command.run(level_server, pos.getX(), pos.getY(), pos.getZ(), "particle flash ~ ~ ~ 0 0 0 0 1 force");
                        level_accessor.setBlock(pos, Utils.block.fromText("tanshugetrees:block_placer_" + block_placer), 2);

                        NBTManager.block.setText(level_accessor, pos, "block", NBTManager.entity.getText(entity, block));
                        NBTManager.block.setText(level_accessor, pos, "function", function[1]);
                        NBTManager.block.setText(level_accessor, pos, "function_style", function[2]);

                    } else {

                        String key = pos.getX() + "/" + pos.getY() + "/" + pos.getZ();

                        if (replacing == true) {

                            ShapeFileConverter.export_data.remove("B" + key);

                        }

                        ShapeFileConverter.export_data.put("B" + key, type_short);

                        if (function[0].equals("") == false) {

                            if (replacing == true) {

                                ShapeFileConverter.export_data.remove("F" + key);

                            }

                            ShapeFileConverter.export_data.put("F" + key, function[0]);

                        }

                    }

                    return true;

                }

            }

            return false;

        }

        private static String[] buildGetWayFunction (Entity entity, String type) {

            String[] return_text = new String[]{"", "", ""};
            String function = "";
            String path = "";
            String at_type = "";
            String style = "";

            for (int number = 1; number <= 3; number++) {

                function = "function_way" + number;

                if (Math.random() < NBTManager.entity.getNumber(entity, function + "_chance")) {

                    path = NBTManager.entity.getText(entity, function);
                    at_type = NBTManager.entity.getText(entity, function + "_type");
                    style = NBTManager.entity.getText(entity, function + "_style");

                    if (path.equals("") == false && at_type.equals(type) == true) {

                        if (NBTManager.entity.getNumber(entity, function + "_max") >= 0) {

                            double range_min = NBTManager.entity.getNumber(entity, function + "_range_min") * 0.01;
                            double range_max = NBTManager.entity.getNumber(entity, function + "_range_max") * 0.01;
                            double length_percent = 1.0;

                            if (NBTManager.entity.getNumber(entity, at_type + "_length_save") > 0) {

                                length_percent = 1.0 - (NBTManager.entity.getNumber(entity, at_type + "_length") / NBTManager.entity.getNumber(entity, at_type + "_length_save"));

                            }

                            if (range_min <= length_percent && length_percent <= range_max) {

                                // Count Limitation
                                {

                                    if (NBTManager.entity.getNumber(entity, function + "_max") > 0) {

                                        NBTManager.entity.addNumber(entity, function + "_max", -1);

                                        if (NBTManager.entity.getNumber(entity, function + "_max") == 0) {

                                            NBTManager.entity.setNumber(entity, function + "_max", -1);

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

                TXTFunction.start(level_accessor, level_server, RandomSource.create(), entity.getBlockX(), entity.getBlockY(), entity.getBlockZ(), NBTManager.entity.getText(entity, "function_end"));

            } else {

                ShapeFileConverter.whenTreeEnd(level_accessor, level_server, entity);
                firework_position = "execute at @p run ";

            }

            Utils.command.runEntity(entity, firework_position + "summon firework_rocket ~20 ~10 ~20 {LifeTime:40,FireworksItem:{id:firework_rocket,Count:1,tag:{Fireworks:{Flight:2,Explosions:[{Type:4,Flicker:1,Trail:1,Colors:[I;3887386,4312372],FadeColors:[I;3887386,4312372]}]}}}}");
            Utils.command.runEntity(entity, firework_position + "summon firework_rocket ~20 ~10 ~-20 {LifeTime:40,FireworksItem:{id:firework_rocket,Count:1,tag:{Fireworks:{Flight:2,Explosions:[{Type:4,Flicker:1,Trail:1,Colors:[I;3887386,4312372],FadeColors:[I;3887386,4312372]}]}}}}");
            Utils.command.runEntity(entity, firework_position + "summon firework_rocket ~-20 ~10 ~20 {LifeTime:40,FireworksItem:{id:firework_rocket,Count:1,tag:{Fireworks:{Flight:2,Explosions:[{Type:4,Flicker:1,Trail:1,Colors:[I;3887386,4312372],FadeColors:[I;3887386,4312372]}]}}}}");
            Utils.command.runEntity(entity, firework_position + "summon firework_rocket ~-20 ~10 ~-20 {LifeTime:40,FireworksItem:{id:firework_rocket,Count:1,tag:{Fireworks:{Flight:2,Explosions:[{Type:4,Flicker:1,Trail:1,Colors:[I;3887386,4312372],FadeColors:[I;3887386,4312372]}]}}}}");
            Utils.command.run(level_server, 0, 0, 0, "kill @e[tag=TANSHUGETREES-" + id + "]");

        }

    }

}
