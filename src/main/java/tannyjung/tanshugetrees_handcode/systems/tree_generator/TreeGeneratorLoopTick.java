package tannyjung.tanshugetrees_handcode.systems.tree_generator;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.ListTag;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import tannyjung.tanshugetrees.network.TanshugetreesModVariables;
import tannyjung.misc.GameUtils;
import tannyjung.tanshugetrees_handcode.systems.config.ConfigMain;

import java.util.Arrays;

public class TreeGeneratorLoopTick {

    public static void start (LevelAccessor level, Entity entity) {

        GameUtils.runCommandEntity(entity, "particle composter ~ ~ ~ 0 0 0 0 1 force");

        if (GameUtils.NBTLogicGet(entity, "start") == false) {

            GameUtils.NBTLogicSet(entity, "start", true);
            beforeRunSystem(level, entity);

        } else {

            GameUtils.NBTNumberSet(entity, "generate_speed_test", GameUtils.NBTNumberGet(entity, "generate_speed_test") + 1);

            if (GameUtils.NBTNumberGet(entity, "generate_speed_test") > GameUtils.NBTNumberGet(entity, "generate_speed")) {

                GameUtils.NBTNumberSet(entity, "generate_speed_test", 0);
                runSystem(level, entity);

            }

        }

    }

    private static void beforeRunSystem (LevelAccessor level, Entity entity) {

        GameUtils.runCommandEntity(entity, "kill @e[name=TANSHUGETREES-tree_countdown,distance=..1]");

        if (TanshugetreesModVariables.MapVariables.get(level).auto_gen == false) {

            GameUtils.runCommandEntity(entity, "tp ~ ~" + GameUtils.NBTNumberGet(entity, "start_height") + " ~");

        }

        GameUtils.NBTTextSet(entity, "id", entity.getUUID().toString());
        GameUtils.runCommandEntity(entity, "tag @s add TANSHUGETREES-" + GameUtils.NBTTextGet(entity, "id"));
        GameUtils.NBTTextSet(entity, "type", "taproot");
        GameUtils.NBTTextSet(entity, "step", "summon");
        GameUtils.NBTNumberSet(entity, "taproot_count", Mth.nextInt(RandomSource.create(), (int) GameUtils.NBTNumberGet(entity, "taproot_count_min"), (int) GameUtils.NBTNumberGet(entity, "taproot_count_max")));
        GameUtils.NBTNumberSet(entity, "trunk_count", Mth.nextInt(RandomSource.create(), (int) GameUtils.NBTNumberGet(entity, "trunk_count_min"), (int) GameUtils.NBTNumberGet(entity, "trunk_count_max")));

        // No Roots
        {

            if (GameUtils.NBTNumberGet(entity, "taproot_count") == 0) {

                GameUtils.NBTTextSet(entity, "type", "trunk");

            }

        }

        // Debug Mode
        {

            if (GameUtils.NBTLogicGet(entity, "debug_mode") == true) {

                GameUtils.NBTTextSet(entity, "taproot_outer", "purple_concrete");
                GameUtils.NBTTextSet(entity, "taproot_inner", "purple_concrete");
                GameUtils.NBTTextSet(entity, "taproot_core", "purple_concrete");
                GameUtils.NBTTextSet(entity, "secondary_root_outer", "magenta_concrete");
                GameUtils.NBTTextSet(entity, "secondary_root_inner", "magenta_concrete");
                GameUtils.NBTTextSet(entity, "secondary_root_core", "magenta_concrete");
                GameUtils.NBTTextSet(entity, "tertiary_root_outer", "pink_concrete");
                GameUtils.NBTTextSet(entity, "tertiary_root_inner", "pink_concrete");
                GameUtils.NBTTextSet(entity, "tertiary_root_core", "pink_concrete");
                GameUtils.NBTTextSet(entity, "fine_root_outer", "light_blue_concrete");
                GameUtils.NBTTextSet(entity, "fine_root_inner", "light_blue_concrete");
                GameUtils.NBTTextSet(entity, "fine_root_core", "light_blue_concrete");

                GameUtils.NBTTextSet(entity, "trunk_outer", "red_concrete");
                GameUtils.NBTTextSet(entity, "trunk_inner", "red_concrete");
                GameUtils.NBTTextSet(entity, "trunk_core", "red_concrete");
                GameUtils.NBTTextSet(entity, "branch_outer", "orange_concrete");
                GameUtils.NBTTextSet(entity, "branch_inner", "orange_concrete");
                GameUtils.NBTTextSet(entity, "branch_core", "orange_concrete");
                GameUtils.NBTTextSet(entity, "twig_outer", "yellow_concrete");
                GameUtils.NBTTextSet(entity, "twig_inner", "yellow_concrete");
                GameUtils.NBTTextSet(entity, "twig_core", "yellow_concrete");
                GameUtils.NBTTextSet(entity, "leaves_twig_outer", "lime_concrete");
                GameUtils.NBTTextSet(entity, "leaves_twig_inner", "lime_concrete");
                GameUtils.NBTTextSet(entity, "leaves_twig_core", "lime_concrete");

                GameUtils.NBTTextSet(entity, "leaves1", "lime_stained_glass");
                GameUtils.NBTTextSet(entity, "leaves2", "green_stained_glass");

            }

        }

        // Summon Status Display
        GameUtils.runCommandEntity(entity, "execute positioned ~ ~1 ~ run " + GameUtils.summonEntity("text_display", GameUtils.NBTTextGet(entity, "id") + " / tree_generator_status", "Tree Generator Status", "see_through:1b,alignment:\"left\",brightness:{block:15},line_width:1000,transformation:{left_rotation:[0f,0f,0f,1f],right_rotation:[0f,0f,0f,1f],translation:[0f,0f,0f],scale:[1f,1f,1f]},billboard:vertical,text:'{\"text\":\"In Progress...\",\"color\":\"white\"}'"));

    }

    private static String[] typePreNext (Entity entity) {

        String[] return_text = new String[2];
        String type = GameUtils.NBTTextGet(entity, "type");

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

        } else if (type.equals("trunk") == true) {

            return_text[0] = "";
            return_text[1] = "branch";

        } else if (type.equals("branch") == true) {

            return_text[0] = "trunk";
            return_text[1] = "twig";

        } else if (type.equals("twig") == true) {

            return_text[0] = "branch";
            return_text[1] = "leaves_twig";

        } else if (type.equals("leaves_twig") == true) {

            return_text[0] = "twig";
            return_text[1] = "";

        }

        return return_text;

    }

    private static void runSystem (LevelAccessor level, Entity entity) {

        String id = GameUtils.NBTTextGet(entity, "id");
        String type = GameUtils.NBTTextGet(entity, "type");
        String step = GameUtils.NBTTextGet(entity, "step");
        String[] type_pre_next = new String[0];

        // Status Display
        {

            StringBuilder command = new StringBuilder();

            command
                    .append("Total Processes : ").append((int) GameUtils.NBTNumberGet(entity, "process"))
                    .append("\\\\n")
                    .append("Generating : ").append(type)
                    .append("\\\\n")
                    .append("\\\\n")
                    .append("Step : ").append(step)
                    .append("\\\\n")
                    .append("Count : ").append((int) GameUtils.NBTNumberGet(entity, type + "_count"))
                    .append("\\\\n")
                    .append("Length : ").append((int) GameUtils.NBTNumberGet(entity, type + "_length")).append(" / ").append((int) GameUtils.NBTNumberGet(entity, type + "_length_save"))
                    .append("\\\\n")
                    .append("Thickness : ").append(GameUtils.NBTNumberGet(entity, type + "_thickness")).append(" / ").append((int) GameUtils.NBTNumberGet(entity, type + "_thickness_max"))
            ;

            GameUtils.runCommandEntity(entity, "execute positioned ~ ~1 ~ run data merge entity @e[tag=TANSHUGETREES-tree_generator_status,distance=..1,limit=1,sort=nearest] {text:'{\"text\":\"" + command + "\",\"color\":\"red\"}'}");

        }

        if (ConfigMain.global_speed_enable == true && GameUtils.NBTLogicGet(entity, "global_generate_speed") == true) {

            GameUtils.NBTNumberSet(entity, "generate_speed", ConfigMain.global_speed);
            GameUtils.NBTNumberSet(entity, "generate_speed_repeat", ConfigMain.global_speed_repeat);
            GameUtils.NBTNumberSet(entity, "generate_speed_tp", ConfigMain.global_speed_tp);

        }

        while (true) {

            // Break Out
            {

                if (GameUtils.NBTNumberGet(entity, "generate_speed_repeat") != 0) {

                    if (GameUtils.NBTNumberGet(entity, "generate_speed_repeat_test") < GameUtils.NBTNumberGet(entity, "generate_speed_repeat")) {

                        GameUtils.NBTNumberSet(entity, "generate_speed_repeat_test", GameUtils.NBTNumberGet(entity, "generate_speed_repeat_test") + 1);

                    } else {

                        GameUtils.NBTNumberSet(entity, "generate_speed_repeat_test", 0);
                        break;

                    }

                }

                if (GameUtils.NBTNumberGet(entity, "generate_speed_tp") != 0) {

                    if (GameUtils.NBTNumberGet(entity, "generate_speed_tp_test") < GameUtils.NBTNumberGet(entity, "generate_speed_tp")) {

                        GameUtils.NBTNumberSet(entity, "generate_speed_tp_test", 0);
                        break;

                    }

                }

            }

            GameUtils.NBTNumberAdd(entity, "process", 1);
            type = GameUtils.NBTTextGet(entity, "type");
            step = GameUtils.NBTTextGet(entity, "step");
            type_pre_next = typePreNext(entity);

            if (step.equals("summon") == true) {

                stepSummon(level, entity, id, type, type_pre_next, step);

            } else if (step.equals("calculation") == true) {

                stepCalculation(level, entity, id, type, type_pre_next, step);

            } else if (step.equals("build") == true) {

                stepBuild(level, entity, id, type);

            } else {

                stepEnd(level, entity, id);
                break;

            }

        }

    }

    private static void stepSummon (LevelAccessor level, Entity entity, String id, String type, String[] type_pre_next, String step) {

        GameUtils.NBTTextSet(entity, "step", "build");
        GameUtils.NBTNumberAdd(entity, type + "_count", -1);
        boolean taproot_trunk = type.equals("taproot") == true || type.equals("trunk") == true;

        // Start Settings
        {

            if (type.equals("leaves") == false) {

                // General
                {

                    GameUtils.NBTNumberSet(entity, type_pre_next[1] + "_count", Mth.nextInt(RandomSource.create(), (int) GameUtils.NBTNumberGet(entity, type_pre_next[1] + "_count_min"), (int) GameUtils.NBTNumberGet(entity, type_pre_next[1] + "_count_max")));
                    GameUtils.NBTNumberSet(entity, type + "_length", Mth.nextInt(RandomSource.create(), (int) GameUtils.NBTNumberGet(entity, type + "_length_min"), (int) GameUtils.NBTNumberGet(entity, type + "_length_max")));
                    GameUtils.NBTNumberSet(entity, type + "_thickness", GameUtils.NBTNumberGet(entity, type + "_thickness_max") - 1);

                    // stepSummonReduceFrom...

                    GameUtils.NBTNumberSet(entity, type_pre_next[1] + "_count_save", GameUtils.NBTNumberGet(entity, type_pre_next[1] + "_count"));
                    GameUtils.NBTNumberSet(entity, type + "_length_save", GameUtils.NBTNumberGet(entity, type + "_length"));

                }

            } else {

                // Leaves
                {

                    int reduce_from = (int) GameUtils.NBTNumberGet(entity, GameUtils.NBTTextGet(entity, "leaves_size_reduce_from") + "_length");
                    int reduce_from_save = (int) GameUtils.NBTNumberGet(entity, GameUtils.NBTTextGet(entity, "leaves_size_reduce_from") + "_length_save");
                    double reduce_center = 1 - (GameUtils.NBTNumberGet(entity, "leaves_size_reduce_center") * 0.01);

                    if (reduce_from > reduce_from_save * reduce_center) {


                    }

                }

            }

        }

        // Summoning
        {

            String at_part = "";
            String positioned = "";

            if (taproot_trunk == true) {

                at_part = "tree_generator";
                positioned = "positioned ~ ~ ~";

            } else {

                at_part = "generator_" + type_pre_next[0];

                if (GameUtils.NBTLogicGet(entity, type + "_center_direction") == false) {

                    // Non Center Direction
                    {

                        double vertical = GameUtils.NBTNumberGet(entity, type + "_start_vertical");
                        double horizontal = GameUtils.NBTNumberGet(entity, type + "_start_horizontal");
                        vertical = Mth.nextDouble(RandomSource.create(), -(vertical), vertical);
                        horizontal = Mth.nextDouble(RandomSource.create(), -(horizontal), horizontal);
                        double forward = Mth.nextDouble(RandomSource.create(), GameUtils.NBTNumberGet(entity, type + "_start_forward_min"), GameUtils.NBTNumberGet(entity, type + "_start_forward_max"));
                        double height = Mth.nextDouble(RandomSource.create(), GameUtils.NBTNumberGet(entity, type + "_start_height_min"), GameUtils.NBTNumberGet(entity, type + "_start_height_max"));
                        positioned = "positioned ^" + horizontal + " ^" + vertical + " ^" + forward + " positioned ~ ~" + height + " ~";

                    }

                } else {

                    // Center Direction
                    {


                    }

                }

            }

            GameUtils.runCommand(level, 0, 0, 0, "execute as @e[tag=TANSHUGETREES-" + id + ",tag=TANSHUGETREES-" + at_part + "] at @s " + positioned + " run " + GameUtils.summonEntity("marker", id + " / generator_" + type, "Tree Generator - Part", ""));
            GameUtils.runCommand(level, 0, 0, 0, "execute as @e[tag=TANSHUGETREES-" + id + ",tag=TANSHUGETREES-" + at_part + "] at @s " + positioned + " run particle minecraft:firework ~ ~ ~ 0 0 0 0.1 20 force");

            if (taproot_trunk == true) {

                int direction = Mth.nextInt(RandomSource.create(), (int) GameUtils.NBTNumberGet(entity, type + "_start_direction_min"), (int) GameUtils.NBTNumberGet(entity, type + "_start_direction_max"));
                int gravity = Mth.nextInt(RandomSource.create(), (int) GameUtils.NBTNumberGet(entity, type + "_start_gravity_max"), (int) GameUtils.NBTNumberGet(entity, type + "_start_gravity_min"));
                GameUtils.runCommand(level, 0, 0, 0, "execute as @e[tag=TANSHUGETREES-" + id + ",tag=TANSHUGETREES-generator_" + type + "] at @s run tp @s ~ ~ ~ " + direction + " " + gravity);

            } else {

                String type_current = "tag=TANSHUGETREES-" + id + ",tag=TANSHUGETREES-generator_" + type;
                String type_pre = "tag=TANSHUGETREES-" + id + ",tag=TANSHUGETREES-generator_" + type_pre_next[0];
                GameUtils.runCommand(level, 0, 0, 0, "execute as @e[" + type_current + "] at @s facing entity @e[" + type_pre + ",limit=1] feet positioned as @e[" + type_pre + ",limit=1] run tp @s ~ ~ ~ facing ^ ^ ^-1");

            }

        }

    }

    private static void stepSummonReduceFrom (Entity entity, String id, String type, String step, String type_next) {

        String type_test = "";

        if (step.equals("count") == true) {

            type_test = type_next;

        } else {

            type_test = type;

        }

        String reduce_from = GameUtils.NBTTextGet(entity, type_test + "_" + step + "_reduce_from");
        int length_save = (int) GameUtils.NBTNumberGet(entity, reduce_from + "_length_save");

        if (length_save > 0) {

            if (step.equals("count") == true) {

                if (type.equals("taproot") == false && type.equals("fine_root") == false && type.equals("trunk") == false && type.equals("leaves_twig") == false) {



                }



            }

        }

    }

    private static void stepCalculation (LevelAccessor level, Entity entity, String id, String type, String[] type_pre_next, String step) {

        if (GameUtils.NBTNumberGet(entity, type + "_length") > 0) {

            // Continue
            {

                GameUtils.NBTTextSet(entity, "step", "build");
                GameUtils.NBTNumberAdd(entity, type + "_length", -1);

                // Direction and Gravity
                {

                    double vertical = GameUtils.NBTNumberGet(entity, type + "_curvature_vertical");
                    double horizontal = GameUtils.NBTNumberGet(entity, type + "_curvature_horizontal");

                    if (vertical != 0 || horizontal != 0) {

                        vertical = Mth.nextDouble(RandomSource.create(), -(vertical), vertical);
                        horizontal = Mth.nextDouble(RandomSource.create(), -(horizontal), horizontal);
                        GameUtils.runCommand(level, 0, 0, 0, "execute as @e[tag=TANSHUGETREES-" + id + ",tag=TANSHUGETREES-generator_" + type + "] at @s run tp @s ~ ~ ~ ~" + horizontal + " ~" + vertical);

                    }

                }

                // Gravity
                {

                    int weightiness = (int) GameUtils.NBTNumberGet(entity, type + "_gravity_weightiness");

                    if (weightiness != 0) {

                        int min = (int) GameUtils.NBTNumberGet(entity, type + "_gravity_min");
                        int max = (int) GameUtils.NBTNumberGet(entity, type + "_gravity_max");
                        GameUtils.runCommand(level, 0, 0, 0, "execute as @e[tag=TANSHUGETREES-" + id + ",tag=TANSHUGETREES-generator_" + type + ",x_rotation=" + min + "..90] at @s run tp @s ~ ~ ~ ~ ~-" + weightiness);
                        GameUtils.runCommand(level, 0, 0, 0, "execute as @e[tag=TANSHUGETREES-" + id + ",tag=TANSHUGETREES-generator_" + type + ",x_rotation=-90.." + max + "] at @s run tp @s ~ ~ ~ ~ ~" + weightiness);

                    }

                }

                double thickness = Double.parseDouble(String.format("%.2f", GameUtils.NBTNumberGet(entity, type + "_length") / GameUtils.NBTNumberGet(entity, type + "_length_save")));
                thickness = thickness * (GameUtils.NBTNumberGet(entity, type + "_thickness_max") - GameUtils.NBTNumberGet(entity, type + "_thickness_min"));
                GameUtils.NBTNumberSet(entity, type + "_thickness", GameUtils.NBTNumberGet(entity, type + "_thickness_max") - thickness - 1);




                GameUtils.runCommand(level, 0, 0, 0, "execute as @e[tag=TANSHUGETREES-" + id + ",tag=TANSHUGETREES-generator_" + type + "] at @s run tp @s ^ ^ ^1");
                GameUtils.runCommand(level, 0, 0, 0, "execute as @e[tag=TANSHUGETREES-" + id + ",tag=TANSHUGETREES-generator_" + type + "] at @s run particle flame ~ ~ ~ 0 0 0 0 1 force");

            }

        } else {

            // Next Part
            {

                if (GameUtils.NBTNumberGet(entity, type_pre_next[1] + "_count") > 0) {

                    // Up
                    {

                        GameUtils.NBTTextSet(entity, "type", type_pre_next[1]);
                        GameUtils.NBTTextSet(entity, "step", "summon");

                    }

                } else {

                    // Down
                    {

                        GameUtils.runCommandEntity(entity, "kill @e[tag=TANSHUGETREES-" + id + ",tag=TANSHUGETREES-generator_" + type + "]");

                        if (type.equals("taproot") == true) {

                            if (GameUtils.NBTNumberGet(entity, type + "_count") <= 0 && GameUtils.NBTNumberGet(entity, type + "_length") <= 0) {

                                GameUtils.NBTTextSet(entity, "type", type_pre_next[0]);

                            }

                            GameUtils.NBTTextSet(entity, "step", "summon");

                        } else if (type.equals("trunk") == true) {

                            if (GameUtils.NBTNumberGet(entity, type + "_count") <= 0 && GameUtils.NBTNumberGet(entity, type + "_length") <= 0) {

                                GameUtils.NBTTextSet(entity, "step", "end");

                            } else {

                                GameUtils.NBTTextSet(entity, "step", "summon");

                            }

                        } else {

                            GameUtils.NBTTextSet(entity, "type", type_pre_next[0]);

                        }

                    }

                }

            }

        }

    }

    private static void stepBuild (LevelAccessor level, Entity entity, String id, String type) {

        double center_posX = 0.0;
        double center_posY = 0.0;
        double center_posZ = 0.0;

        // Get Create Pos
        {

            GameUtils.runCommandEntity(entity, "data modify entity @s ForgeData.create_pos set from entity @e[tag=TANSHUGETREES-" + id + ",tag=TANSHUGETREES-generator_" + type + ",limit=1] Pos");
            ListTag get = GameUtils.NBTListNumberGet(entity, "create_pos");
            center_posX = get.getDouble(0);
            center_posY = get.getDouble(1);
            center_posZ = get.getDouble(2);

        }

        double thickness = GameUtils.NBTNumberGet(entity, type + "_thickness");


        BlockState block = GameUtils.textToBlock(GameUtils.NBTTextGet(entity, type + "_outer"));
        GameUtils.runCommand(level, center_posX, center_posY, center_posZ, "particle flash ~ ~ ~ 0 0 0 0 1 force");






        if (GameUtils.NBTTextGet(entity, type + "_generator_type").equals("square") == true) {

            double half_thickness = thickness * 0.5;

            for (double scanX = center_posX - half_thickness; scanX <= center_posX + half_thickness; scanX++) {

                for (double scanY = center_posY - half_thickness; scanY <= center_posY + half_thickness; scanY++) {

                    for (double scanZ = center_posZ - half_thickness; scanZ <= center_posZ + half_thickness; scanZ++) {

                        level.setBlock(new BlockPos((int) scanX, (int) scanY, (int) scanZ), block, 2);

                    }

                }

            }





            // thickness = thickness * 0.5;
            // String from_to = "~" + -(thickness) + " ~" + -(thickness) + " ~" + -(thickness) + " ~" + thickness + " ~" + thickness + " ~" + thickness;
            // GameUtils.runCommand(level, 0, 0, 0, "execute at @e[tag=TANSHUGETREES-" + id + ",tag=TANSHUGETREES-generator_" + type + "] run fill " + from_to + " " + block);

        }




        GameUtils.NBTTextSet(entity, "step", "calculation");

    }

    private static void stepEnd (LevelAccessor level, Entity entity, String id) {

        GameUtils.runCommandEntity(entity, "summon firework_rocket ~20 ~10 ~20 {LifeTime:40,FireworksItem:{id:firework_rocket,Count:1,tag:{Fireworks:{Flight:2,Explosions:[{Type:4,Flicker:1,Trail:1,Colors:[I;3887386,4312372],FadeColors:[I;3887386,4312372]}]}}}}");
        GameUtils.runCommandEntity(entity, "summon firework_rocket ~20 ~10 ~-20 {LifeTime:40,FireworksItem:{id:firework_rocket,Count:1,tag:{Fireworks:{Flight:2,Explosions:[{Type:4,Flicker:1,Trail:1,Colors:[I;3887386,4312372],FadeColors:[I;3887386,4312372]}]}}}}");
        GameUtils.runCommandEntity(entity, "summon firework_rocket ~-20 ~10 ~20 {LifeTime:40,FireworksItem:{id:firework_rocket,Count:1,tag:{Fireworks:{Flight:2,Explosions:[{Type:4,Flicker:1,Trail:1,Colors:[I;3887386,4312372],FadeColors:[I;3887386,4312372]}]}}}}");
        GameUtils.runCommandEntity(entity, "summon firework_rocket ~-20 ~10 ~-20 {LifeTime:40,FireworksItem:{id:firework_rocket,Count:1,tag:{Fireworks:{Flight:2,Explosions:[{Type:4,Flicker:1,Trail:1,Colors:[I;3887386,4312372],FadeColors:[I;3887386,4312372]}]}}}}");
        GameUtils.runCommand(level, 0, 0, 0, "kill @e[tag=TANSHUGETREES-" + id + "]");

    }

}
