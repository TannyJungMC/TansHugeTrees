package tannyjung.tanshugetrees_handcode.systems.tree_generator;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import tannyjung.misc.GameUtils;
import tannyjung.tanshugetrees.network.TanshugetreesModVariables;
import tannyjung.tanshugetrees_handcode.config.ConfigMain;

public class TreeGenerator {

    public static void start (LevelAccessor level, Entity entity) {

        GameUtils.command.runEntity(entity, "particle composter ~ ~ ~ 0 0 0 0 1 force");

        if (GameUtils.NBT.entity.getLogic(entity, "start") == false) {

            GameUtils.NBT.entity.setLogic(entity, "start", true);
            beforeRunSystem(level, entity);

        } else {

            GameUtils.NBT.entity.setNumber(entity, "generate_speed_test", GameUtils.NBT.entity.getNumber(entity, "generate_speed_test") + 1);

            if (GameUtils.NBT.entity.getNumber(entity, "generate_speed_test") > GameUtils.NBT.entity.getNumber(entity, "generate_speed")) {

                GameUtils.NBT.entity.setNumber(entity, "generate_speed_test", 0);
                runSystem(level, entity);

            }

        }

    }

    private static void beforeRunSystem (LevelAccessor level, Entity entity) {

        GameUtils.command.runEntity(entity, "kill @e[name=TANSHUGETREES-tree_countdown,distance=..1]");

        if (TanshugetreesModVariables.MapVariables.get(level).auto_gen == false) {

            GameUtils.command.runEntity(entity, "tp ~ ~" + GameUtils.NBT.entity.getNumber(entity, "start_height") + " ~");

        }

        GameUtils.NBT.entity.setText(entity, "id", entity.getUUID().toString());
        GameUtils.command.runEntity(entity, "tag @s add TANSHUGETREES-" + GameUtils.NBT.entity.getText(entity, "id"));
        GameUtils.NBT.entity.setText(entity, "type", "taproot");
        GameUtils.NBT.entity.setText(entity, "step", "summon");
        GameUtils.NBT.entity.setNumber(entity, "taproot_count", Mth.nextInt(RandomSource.create(), (int) GameUtils.NBT.entity.getNumber(entity, "taproot_count_min"), (int) GameUtils.NBT.entity.getNumber(entity, "taproot_count_max")));
        GameUtils.NBT.entity.setNumber(entity, "trunk_count", Mth.nextInt(RandomSource.create(), (int) GameUtils.NBT.entity.getNumber(entity, "trunk_count_min"), (int) GameUtils.NBT.entity.getNumber(entity, "trunk_count_max")));
        GameUtils.NBT.entity.setNumber(entity, "leaves_count_min", 1);
        GameUtils.NBT.entity.setNumber(entity, "leaves_count_max", 1);

        // No Roots
        {

            if (GameUtils.NBT.entity.getNumber(entity, "taproot_count") == 0) {

                GameUtils.NBT.entity.setText(entity, "type", "trunk");

            }

        }

        // Debug Mode
        {

            if (GameUtils.NBT.entity.getLogic(entity, "debug_mode") == true) {

                GameUtils.NBT.entity.setText(entity, "taproot_outer", "purple_concrete");
                GameUtils.NBT.entity.setText(entity, "taproot_inner", "purple_terracotta");
                GameUtils.NBT.entity.setText(entity, "taproot_core", "purple_stained_glass");
                GameUtils.NBT.entity.setText(entity, "secondary_root_outer", "magenta_concrete");
                GameUtils.NBT.entity.setText(entity, "secondary_root_inner", "magenta_terracotta");
                GameUtils.NBT.entity.setText(entity, "secondary_root_core", "magenta_stained_glass");
                GameUtils.NBT.entity.setText(entity, "tertiary_root_outer", "pink_concrete");
                GameUtils.NBT.entity.setText(entity, "tertiary_root_inner", "pink_terracotta");
                GameUtils.NBT.entity.setText(entity, "tertiary_root_core", "pink_stained_glass");
                GameUtils.NBT.entity.setText(entity, "fine_root_outer", "light_blue_concrete");
                GameUtils.NBT.entity.setText(entity, "fine_root_inner", "light_blue_terracotta");
                GameUtils.NBT.entity.setText(entity, "fine_root_core", "light_blue_stained_glass");

                GameUtils.NBT.entity.setText(entity, "trunk_outer", "red_concrete");
                GameUtils.NBT.entity.setText(entity, "trunk_inner", "red_terracotta");
                GameUtils.NBT.entity.setText(entity, "trunk_core", "red_stained_glass");
                GameUtils.NBT.entity.setText(entity, "branch_outer", "orange_concrete");
                GameUtils.NBT.entity.setText(entity, "branch_inner", "orange_terracotta");
                GameUtils.NBT.entity.setText(entity, "branch_core", "orange_stained_glass");
                GameUtils.NBT.entity.setText(entity, "twig_outer", "yellow_concrete");
                GameUtils.NBT.entity.setText(entity, "twig_inner", "yellow_terracotta");
                GameUtils.NBT.entity.setText(entity, "twig_core", "yellow_stained_glass");
                GameUtils.NBT.entity.setText(entity, "sprig_outer", "lime_concrete");
                GameUtils.NBT.entity.setText(entity, "sprig_inner", "lime_terracotta");
                GameUtils.NBT.entity.setText(entity, "sprig_core", "lime_stained_glass");

                GameUtils.NBT.entity.setText(entity, "leaves1", "lime_stained_glass");
                GameUtils.NBT.entity.setText(entity, "leaves2", "green_stained_glass");

            }

        }

        // Summon Status Display
        GameUtils.command.runEntity(entity, "execute positioned ~ ~1 ~ run " + GameUtils.misc.summonEntity("text_display", GameUtils.NBT.entity.getText(entity, "id") + " / tree_generator_status", "Tree Generator Status", "see_through:1b,alignment:\"left\",brightness:{block:15},line_width:1000,transformation:{left_rotation:[0f,0f,0f,1f],right_rotation:[0f,0f,0f,1f],translation:[0f,0f,0f],scale:[1f,1f,1f]},billboard:vertical,text:'{\"text\":\"In Progress...\",\"color\":\"white\"}'"));

    }

    private static void runSystem (LevelAccessor level, Entity entity) {

        String id = GameUtils.NBT.entity.getText(entity, "id");
        String type = GameUtils.NBT.entity.getText(entity, "type");
        String step = GameUtils.NBT.entity.getText(entity, "step");
        String[] type_pre_next = new String[0];

        // Status Display
        {

            StringBuilder command = new StringBuilder();

            command
                    .append("Total Processes : ").append((int) GameUtils.NBT.entity.getNumber(entity, "total_processes"))
                    .append("\\\\n")
                    .append("Generating : ").append(type)
                    .append("\\\\n")
                    .append("\\\\n")
                    .append("Step : ").append(step)
                    .append("\\\\n")
                    .append("Count : ").append((int) GameUtils.NBT.entity.getNumber(entity, type + "_count"))
                    .append("\\\\n")
                    .append("Length : ").append((int) GameUtils.NBT.entity.getNumber(entity, type + "_length")).append(" / ").append((int) GameUtils.NBT.entity.getNumber(entity, type + "_length_save"))
                    .append("\\\\n")
                    .append("Thickness : ").append(GameUtils.NBT.entity.getNumber(entity, type + "_thickness")).append(" / ").append(GameUtils.NBT.entity.getNumber(entity, type + "_thickness_max"))
            ;

            GameUtils.command.runEntity(entity, "execute positioned ~ ~1 ~ run data merge entity @e[tag=TANSHUGETREES-tree_generator_status,distance=..1,limit=1,sort=nearest] {text:'{\"text\":\"" + command + "\",\"color\":\"red\"}'}");

        }

        if (ConfigMain.global_speed_enable == true && GameUtils.NBT.entity.getLogic(entity, "global_generate_speed") == true) {

            GameUtils.NBT.entity.setNumber(entity, "generate_speed", ConfigMain.global_speed);
            GameUtils.NBT.entity.setNumber(entity, "generate_speed_repeat", ConfigMain.global_speed_repeat);
            GameUtils.NBT.entity.setNumber(entity, "generate_speed_tp", ConfigMain.global_speed_tp);

        }

        while (true) {

            type = GameUtils.NBT.entity.getText(entity, "type");
            step = GameUtils.NBT.entity.getText(entity, "step");
            type_pre_next = typePreNext(entity);

            if (step.equals("summon") == true) {

                Step.summon(level, entity, id, type, type_pre_next);

            } else if (step.equals("calculation") == true) {

                Step.calculation(level, entity, id, type, type_pre_next);

            } else if (step.equals("build") == true) {

                Step.build(level, entity, id, type);

            } else {

                Step.end(level, entity, id);
                break;

            }

            // Break Out
            {

                if (processBreak(entity) == true) {

                    break;

                }

                if (GameUtils.NBT.entity.getNumber(entity, "generate_speed_tp") != 0) {

                    if (GameUtils.NBT.entity.getNumber(entity, "generate_speed_tp_test") < GameUtils.NBT.entity.getNumber(entity, "generate_speed_tp")) {

                        GameUtils.NBT.entity.setNumber(entity, "generate_speed_tp_test", 0);
                        break;

                    }

                }

            }

        }

    }

    private static boolean processBreak (Entity entity) {

        boolean return_logic = false;
        GameUtils.NBT.entity.addNumber(entity, "total_processes", 1);

        if (GameUtils.NBT.entity.getNumber(entity, "generate_speed_repeat") != 0) {

            if (GameUtils.NBT.entity.getNumber(entity, "generate_speed_repeat_test") < GameUtils.NBT.entity.getNumber(entity, "generate_speed_repeat")) {

                GameUtils.NBT.entity.addNumber(entity, "generate_speed_repeat_test", 1);

            } else {

                GameUtils.NBT.entity.setNumber(entity, "generate_speed_repeat_test", 0);
                return_logic = true;

            }

        }

        return return_logic;

    }

    private static String[] typePreNext (Entity entity) {

        String[] return_text = new String[2];
        String type = GameUtils.NBT.entity.getText(entity, "type");

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

        private static void summon (LevelAccessor level, Entity entity, String id, String type, String[] type_pre_next) {

            boolean taproot_trunk = type.equals("taproot") == true || type.equals("trunk") == true;
            GameUtils.NBT.entity.addNumber(entity, type + "_count", -1);

            // Start Settings
            {

                if (type.equals("leaves") == false) {

                    // General
                    {

                        GameUtils.NBT.entity.setNumber(entity, type_pre_next[1] + "_count", Mth.nextInt(RandomSource.create(), (int) GameUtils.NBT.entity.getNumber(entity, type_pre_next[1] + "_count_min"), (int) GameUtils.NBT.entity.getNumber(entity, type_pre_next[1] + "_count_max")));
                        GameUtils.NBT.entity.setNumber(entity, type + "_length", Mth.nextInt(RandomSource.create(), (int) GameUtils.NBT.entity.getNumber(entity, type + "_length_min"), (int) GameUtils.NBT.entity.getNumber(entity, type + "_length_max")));
                        GameUtils.NBT.entity.setNumber(entity, type + "_thickness", GameUtils.NBT.entity.getNumber(entity, type + "_thickness_max") - 1);

                        // stepSummonReduceFrom...

                        GameUtils.NBT.entity.setNumber(entity, type_pre_next[1] + "_count_save", GameUtils.NBT.entity.getNumber(entity, type_pre_next[1] + "_count"));
                        GameUtils.NBT.entity.setNumber(entity, type + "_length_save", GameUtils.NBT.entity.getNumber(entity, type + "_length"));

                    }

                    if (type.equals("sprig") == true) {

                        GameUtils.NBT.entity.setNumber(entity, "leaves_count", 1);
                        GameUtils.NBT.entity.setNumber(entity, "leaves_length", 1);

                    }

                } else {

                    // Leaves
                    {

                        GameUtils.NBT.entity.setNumber(entity, "leaves_thickness", Mth.nextDouble(RandomSource.create(), GameUtils.NBT.entity.getNumber(entity, "leaves_size_min"), GameUtils.NBT.entity.getNumber(entity, "leaves_size_max")));








                        int reduce_from = (int) GameUtils.NBT.entity.getNumber(entity, GameUtils.NBT.entity.getText(entity, "leaves_size_reduce_from") + "_length");
                        int reduce_from_save = (int) GameUtils.NBT.entity.getNumber(entity, GameUtils.NBT.entity.getText(entity, "leaves_size_reduce_from") + "_length_save");
                        double reduce_center = 1 - (GameUtils.NBT.entity.getNumber(entity, "leaves_size_reduce_center") * 0.01);

                        if (reduce_from > reduce_from_save * reduce_center) {



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

                    if (taproot_trunk == true) {

                        at_part = "tree_generator";
                        positioned = "positioned ~ ~ ~";

                    } else {

                        at_part = "generator_" + type_pre_next[0];

                        if (type.equals("leaves") == false) {

                            {

                                if (GameUtils.NBT.entity.getLogic(entity, type + "_center_direction") == false) {

                                    // Non Center Direction
                                    {

                                        double vertical = Mth.nextDouble(RandomSource.create(), -(GameUtils.NBT.entity.getNumber(entity, type + "_start_vertical")), GameUtils.NBT.entity.getNumber(entity, type + "_start_vertical"));
                                        double horizontal = Mth.nextDouble(RandomSource.create(), -(GameUtils.NBT.entity.getNumber(entity, type + "_start_horizontal")), GameUtils.NBT.entity.getNumber(entity, type + "_start_horizontal"));
                                        double forward = Mth.nextDouble(RandomSource.create(), GameUtils.NBT.entity.getNumber(entity, type + "_start_forward_min"), GameUtils.NBT.entity.getNumber(entity, type + "_start_forward_max"));
                                        double height = Mth.nextDouble(RandomSource.create(), GameUtils.NBT.entity.getNumber(entity, type + "_start_height_min"), GameUtils.NBT.entity.getNumber(entity, type + "_start_height_max"));
                                        vertical = Double.parseDouble(String.format("%.2f", vertical));
                                        horizontal = Double.parseDouble(String.format("%.2f", horizontal));
                                        forward = Double.parseDouble(String.format("%.2f", forward));
                                        height = Double.parseDouble(String.format("%.2f", height));

                                        positioned = "positioned ^" + horizontal + " ^" + vertical + " ^" + forward + " positioned ~ ~" + height + " ~";

                                    }

                                } else {

                                    // Center Direction
                                    {


                                    }

                                }

                            }

                        } else {

                            positioned = "positioned ~ ~ ~";

                        }

                    }

                }

                GameUtils.command.run(level, 0, 0, 0, "execute as @e[tag=TANSHUGETREES-" + id + ",tag=TANSHUGETREES-" + at_part + "] at @s " + positioned + " run " + GameUtils.misc.summonEntity("marker", id + " / generator_" + type, "Tree Generator - Part", ""));
                GameUtils.command.run(level, 0, 0, 0, "execute as @e[tag=TANSHUGETREES-" + id + ",tag=TANSHUGETREES-" + at_part + "] at @s " + positioned + " run particle minecraft:firework ~ ~ ~ 0 0 0 0.1 20 force");

                if (taproot_trunk == true) {

                    int direction = Mth.nextInt(RandomSource.create(), (int) GameUtils.NBT.entity.getNumber(entity, type + "_start_direction_min"), (int) GameUtils.NBT.entity.getNumber(entity, type + "_start_direction_max"));
                    int gravity = Mth.nextInt(RandomSource.create(), (int) GameUtils.NBT.entity.getNumber(entity, type + "_start_gravity_max"), (int) GameUtils.NBT.entity.getNumber(entity, type + "_start_gravity_min"));
                    GameUtils.command.run(level, 0, 0, 0, "execute as @e[tag=TANSHUGETREES-" + id + ",tag=TANSHUGETREES-generator_" + type + "] at @s run tp @s ~ ~ ~ " + direction + " " + gravity);

                } else {

                    String type_current = "tag=TANSHUGETREES-" + id + ",tag=TANSHUGETREES-generator_" + type;
                    String type_pre = "tag=TANSHUGETREES-" + id + ",tag=TANSHUGETREES-generator_" + type_pre_next[0];
                    GameUtils.command.run(level, 0, 0, 0, "execute as @e[" + type_current + "] at @s facing entity @e[" + type_pre + ",limit=1] feet positioned as @e[" + type_pre + ",limit=1] run tp @s ~ ~ ~ facing ^ ^ ^-1");

                }

            }

            // Next Step
            {

                if (GameUtils.NBT.entity.getNumber(entity, type + "_length") == 0) {

                    GameUtils.NBT.entity.setText(entity, "step", "calculation");

                } else {

                    GameUtils.NBT.entity.setText(entity, "step", "build");

                }

            }

        }

        private static void summonReduceFrom (Entity entity, String type, String step, String type_next) {

            String type_test = "";

            if (step.equals("count") == true) {

                type_test = type_next;

            } else {

                type_test = type;

            }

            String reduce_from = GameUtils.NBT.entity.getText(entity, type_test + "_" + step + "_reduce_from");
            int length_save = (int) GameUtils.NBT.entity.getNumber(entity, reduce_from + "_length_save");

            if (length_save > 0) {

                if (step.equals("count") == true) {

                    if (type.equals("taproot") == false && type.equals("fine_root") == false && type.equals("trunk") == false && type.equals("sprig") == false) {


                    }


                }

            }

        }

        private static void calculation (LevelAccessor level, Entity entity, String id, String type, String[] type_pre_next) {

            if (GameUtils.NBT.entity.getNumber(entity, type + "_length") > 0) {

                // Continue
                {

                    GameUtils.NBT.entity.setText(entity, "step", "build");
                    GameUtils.NBT.entity.addNumber(entity, type + "_length", -1);

                    if (type.equals("leaves") == false) {

                        // Curvature
                        {

                            double vertical = GameUtils.NBT.entity.getNumber(entity, type + "_curvature_vertical");
                            double horizontal = GameUtils.NBT.entity.getNumber(entity, type + "_curvature_horizontal");

                            if (vertical != 0 || horizontal != 0) {

                                vertical = Mth.nextDouble(RandomSource.create(), -(vertical), vertical);
                                horizontal = Mth.nextDouble(RandomSource.create(), -(horizontal), horizontal);
                                GameUtils.command.run(level, 0, 0, 0, "execute as @e[tag=TANSHUGETREES-" + id + ",tag=TANSHUGETREES-generator_" + type + "] at @s run tp @s ~ ~ ~ ~" + horizontal + " ~" + vertical);

                            }

                        }

                        // Gravity
                        {

                            int weightiness = (int) GameUtils.NBT.entity.getNumber(entity, type + "_gravity_weightiness");

                            if (weightiness != 0) {

                                int min = (int) GameUtils.NBT.entity.getNumber(entity, type + "_gravity_min");
                                int max = (int) GameUtils.NBT.entity.getNumber(entity, type + "_gravity_max");
                                GameUtils.command.run(level, 0, 0, 0, "execute as @e[tag=TANSHUGETREES-" + id + ",tag=TANSHUGETREES-generator_" + type + ",x_rotation=" + min + "..90] at @s run tp @s ~ ~ ~ ~ ~-" + weightiness);
                                GameUtils.command.run(level, 0, 0, 0, "execute as @e[tag=TANSHUGETREES-" + id + ",tag=TANSHUGETREES-generator_" + type + ",x_rotation=-90.." + max + "] at @s run tp @s ~ ~ ~ ~ ~" + weightiness);

                            }

                        }

                        double percent = GameUtils.NBT.entity.getNumber(entity, type + "_length") / GameUtils.NBT.entity.getNumber(entity, type + "_length_save");
                        double thickness = GameUtils.NBT.entity.getNumber(entity, type + "_thickness_max") - GameUtils.NBT.entity.getNumber(entity, type + "_thickness_min");
                        thickness = (GameUtils.NBT.entity.getNumber(entity, type + "_thickness_min") - 1) + (thickness * percent);
                        thickness = Math.round(thickness);
                        GameUtils.NBT.entity.setNumber(entity, type + "_thickness", Double.parseDouble(String.format("%.2f", thickness)));
                        GameUtils.command.run(level, 0, 0, 0, "execute as @e[tag=TANSHUGETREES-" + id + ",tag=TANSHUGETREES-generator_" + type + "] at @s run tp @s ^ ^ ^1");

                    }

                }

            } else {

                // Next Part
                {

                    if (GameUtils.NBT.entity.getNumber(entity, type_pre_next[1] + "_count") > 0) {

                        // Up
                        {

                            GameUtils.NBT.entity.setText(entity, "type", type_pre_next[1]);
                            GameUtils.NBT.entity.setText(entity, "step", "summon");

                        }

                    } else {

                        // Down
                        {

                            GameUtils.command.runEntity(entity, "kill @e[tag=TANSHUGETREES-" + id + ",tag=TANSHUGETREES-generator_" + type + "]");

                            if (type.equals("taproot") == true) {

                                if (GameUtils.NBT.entity.getNumber(entity, type + "_count") <= 0 && GameUtils.NBT.entity.getNumber(entity, type + "_length") <= 0) {

                                    GameUtils.NBT.entity.setText(entity, "type", type_pre_next[0]);

                                }

                                GameUtils.NBT.entity.setText(entity, "step", "summon");

                            } else if (type.equals("trunk") == true) {

                                if (GameUtils.NBT.entity.getNumber(entity, type + "_count") <= 0 && GameUtils.NBT.entity.getNumber(entity, type + "_length") <= 0) {

                                    GameUtils.NBT.entity.setText(entity, "step", "end");

                                } else {

                                    GameUtils.NBT.entity.setText(entity, "step", "summon");

                                }

                            } else {

                                GameUtils.NBT.entity.setText(entity, "type", type_pre_next[0]);

                            }

                        }

                    }

                }

            }

        }

        private static void build (LevelAccessor level, Entity entity, String id, String type) {

            double thickness = GameUtils.NBT.entity.getNumber(entity, type + "_thickness");
            double half_thickness = thickness * 0.5;
            String generator_type = GameUtils.NBT.entity.getText(entity, type + "_generator_type");

            // Start Settings
            {

                if (GameUtils.NBT.entity.getLogic(entity, "still_building") == false) {

                    GameUtils.NBT.entity.setLogic(entity, "still_building", true);
                    GameUtils.NBT.entity.setNumber(entity, "build_saveX", -(half_thickness));
                    GameUtils.NBT.entity.setNumber(entity, "build_saveY", -(half_thickness));
                    GameUtils.NBT.entity.setNumber(entity, "build_saveZ", -(half_thickness));

                    // Get Center Pos
                    {

                        GameUtils.command.runEntity(entity, "data modify entity @s ForgeData.builder_pos set from entity @e[tag=TANSHUGETREES-" + id + ",tag=TANSHUGETREES-generator_" + type + ",limit=1] Pos");
                        double[] center_pos = GameUtils.NBT.entity.getListNumber(entity, "builder_pos");
                        GameUtils.NBT.entity.setNumber(entity, "build_centerX", center_pos[0]);
                        GameUtils.NBT.entity.setNumber(entity, "build_centerY", center_pos[1]);
                        GameUtils.NBT.entity.setNumber(entity, "build_centerZ", center_pos[2]);

                    }

                    if (generator_type.equals("sphere_zone") == true) {

                        GameUtils.command.runEntity(entity, "data modify entity @s ForgeData.builder_yaw_pitch set from entity @e[tag=TANSHUGETREES-" + id + ",tag=TANSHUGETREES-generator_sprig,limit=1] Rotation");
                        double[] yaw_pitch = GameUtils.NBT.entity.getListNumberFloat(entity, "builder_yaw_pitch");
                        double yaw = Math.toRadians(((yaw_pitch[0] + 180) + 90) % 360);
                        double pitch = yaw_pitch[1];

                        // Min Pitch
                        {

                            int pitch_min = (int) GameUtils.NBT.entity.getNumber(entity, type + "_sphere_zone_pitch_min");

                            if (pitch > pitch_min) {

                                pitch = pitch_min;

                            }

                        }

                        pitch = Math.toRadians(pitch);
                        double distance = half_thickness;
                        int size = (int) GameUtils.NBT.entity.getNumber(entity, type + "_sphere_zone_size");

                        GameUtils.NBT.entity.setNumber(entity, "sphere_zone_posX", distance * Math.cos(pitch) * Math.cos(yaw));
                        GameUtils.NBT.entity.setNumber(entity, "sphere_zone_posY", distance * Math.sin(pitch));
                        GameUtils.NBT.entity.setNumber(entity, "sphere_zone_posZ", distance * Math.cos(pitch) * Math.sin(yaw));

                        // Change Build Center
                        {

                            distance = distance - size;
                            GameUtils.NBT.entity.addNumber(entity, "build_centerX", distance * Math.cos(pitch) * Math.cos(yaw));
                            GameUtils.NBT.entity.addNumber(entity, "build_centerY", distance * Math.sin(pitch));
                            GameUtils.NBT.entity.addNumber(entity, "build_centerZ", distance * Math.cos(pitch) * Math.sin(yaw));

                        }

                        // Get Area
                        {

                            double sphere_zone_area = thickness - size;

                            if (sphere_zone_area < 0) {

                                sphere_zone_area = 0;

                            }

                            GameUtils.NBT.entity.setNumber(entity, "sphere_zone_area", sphere_zone_area * sphere_zone_area);

                        }

                    }

                }

            }

            double[] center_pos = new double[]{GameUtils.NBT.entity.getNumber(entity, "build_centerX"), GameUtils.NBT.entity.getNumber(entity, "build_centerY"), GameUtils.NBT.entity.getNumber(entity, "build_centerZ")};
            boolean replace = GameUtils.NBT.entity.getLogic(entity, type + "_replace");
            double sphere_area = half_thickness * half_thickness;
            double sphere_zone_area = 0.0;
            double[] sphere_zone_pos = new double[0];

            if (generator_type.equals("sphere_zone") == true) {

                sphere_zone_area = GameUtils.NBT.entity.getNumber(entity, "sphere_zone_area");
                sphere_zone_pos = new double[]{GameUtils.NBT.entity.getNumber(entity, "sphere_zone_posX"), GameUtils.NBT.entity.getNumber(entity, "sphere_zone_posY"), GameUtils.NBT.entity.getNumber(entity, "sphere_zone_posZ")};

            }

            while (true) {

                {

                double build_saveX = GameUtils.NBT.entity.getNumber(entity, "build_saveX");
                double build_saveY = GameUtils.NBT.entity.getNumber(entity, "build_saveY");
                double build_saveZ = GameUtils.NBT.entity.getNumber(entity, "build_saveZ");

                if (build_saveY <= half_thickness) {

                    if (build_saveX <= half_thickness) {

                        if (build_saveZ <= half_thickness) {

                            GameUtils.NBT.entity.addNumber(entity, "build_saveZ", 1);

                            // Place Block
                            {

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

                                BlockPos pos = new BlockPos((int) (center_pos[0] + build_saveX), (int) (center_pos[1] + build_saveY), (int) (center_pos[2] + build_saveZ));
                                String block = "";

                                // Get Block
                                {

                                    if (type.equals("leaves") == false) {

                                        {

                                            if (generator_type.equals("sphere") == true) {

                                                block = buildOuterInnerCore(level, entity, type, half_thickness, pos, build_area);

                                            } else {

                                                block = "outer";

                                            }

                                        }

                                    } else {

                                        {

                                            if (Math.random() < GameUtils.NBT.entity.getNumber(entity, "leaves_density") * 0.01) {

                                                if (Math.random() < GameUtils.NBT.entity.getNumber(entity, "leaves2_chance")) {

                                                    block = "leaves_2";

                                                } else {

                                                    block = "leaves_1";

                                                }

                                            }

                                        }

                                    }

                                }

                                if (block.equals("") == false && buildTestKeep(level, pos, replace) == true) {

                                    buildPlaceBlock(level, entity, type, pos, block);
                                    return;

                                }

                            }

                        } else {

                            GameUtils.NBT.entity.setNumber(entity, "build_saveZ", -(half_thickness));
                            GameUtils.NBT.entity.addNumber(entity, "build_saveX", 1);

                        }

                    } else {

                        GameUtils.NBT.entity.setNumber(entity, "build_saveX", -(half_thickness));
                        GameUtils.NBT.entity.addNumber(entity, "build_saveY", 1);

                    }

                } else {

                    break;

                }

                }

            }

            // If it builds to the end without any break
            GameUtils.NBT.entity.setLogic(entity, "still_building", false);
            GameUtils.NBT.entity.setText(entity, "step", "calculation");

        }

        private static String buildOuterInnerCore (LevelAccessor level, Entity entity, String type, double half_thickness, BlockPos pos, double build_area) {

            Block previous_block = level.getBlockState(pos).getBlock();
            String block = "";

            if (GameUtils.block.isTaggedAs(previous_block.defaultBlockState(), "tanshugetrees:block_placer_blacklist_" + type) == false) {

                // Get Type
                {

                    double outer_level = GameUtils.NBT.entity.getNumber(entity, type + "_outer_level");
                    double inner_level = GameUtils.NBT.entity.getNumber(entity, type + "_inner_level");
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

        private static boolean buildTestKeep (LevelAccessor level, BlockPos pos, boolean replace) {

            if (replace == false && GameUtils.block.isTaggedAs(level.getBlockState(pos), "tanshugetrees:passable_blocks") == false) {

                return false;

            }

            return true;

        }

        private static void buildPlaceBlock (LevelAccessor level, Entity entity, String type, BlockPos pos, String block) {

            String block_placer = "";

            if (type.equals("leaves") == false) {

                block_placer = type + "_" + block;
                block = block_placer;

            } else {

                block_placer = block;
                block = block_placer.replace("_", "");

            }

            level.setBlock(pos, GameUtils.block.fromText("tanshugetrees:block_placer_" + block_placer), 2);
            GameUtils.NBT.block.setText(level, pos, "block", GameUtils.NBT.entity.getText(entity, block));
            GameUtils.command.run(level, pos.getX(), pos.getY(), pos.getZ(), "particle flash ~ ~ ~ 0 0 0 0 1 force");

        }

        private static void end (LevelAccessor level, Entity entity, String id) {

            GameUtils.command.runEntity(entity, "summon firework_rocket ~20 ~10 ~20 {LifeTime:40,FireworksItem:{id:firework_rocket,Count:1,tag:{Fireworks:{Flight:2,Explosions:[{Type:4,Flicker:1,Trail:1,Colors:[I;3887386,4312372],FadeColors:[I;3887386,4312372]}]}}}}");
            GameUtils.command.runEntity(entity, "summon firework_rocket ~20 ~10 ~-20 {LifeTime:40,FireworksItem:{id:firework_rocket,Count:1,tag:{Fireworks:{Flight:2,Explosions:[{Type:4,Flicker:1,Trail:1,Colors:[I;3887386,4312372],FadeColors:[I;3887386,4312372]}]}}}}");
            GameUtils.command.runEntity(entity, "summon firework_rocket ~-20 ~10 ~20 {LifeTime:40,FireworksItem:{id:firework_rocket,Count:1,tag:{Fireworks:{Flight:2,Explosions:[{Type:4,Flicker:1,Trail:1,Colors:[I;3887386,4312372],FadeColors:[I;3887386,4312372]}]}}}}");
            GameUtils.command.runEntity(entity, "summon firework_rocket ~-20 ~10 ~-20 {LifeTime:40,FireworksItem:{id:firework_rocket,Count:1,tag:{Fireworks:{Flight:2,Explosions:[{Type:4,Flicker:1,Trail:1,Colors:[I;3887386,4312372],FadeColors:[I;3887386,4312372]}]}}}}");
            GameUtils.command.run(level, 0, 0, 0, "kill @e[tag=TANSHUGETREES-" + id + "]");

        }

    }

}
