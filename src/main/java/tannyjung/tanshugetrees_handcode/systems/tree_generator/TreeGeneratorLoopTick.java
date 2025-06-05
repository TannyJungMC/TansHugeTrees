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
import tannyjung.tanshugetrees_handcode.config.ConfigMain;

public class TreeGeneratorLoopTick {

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
                GameUtils.NBT.entity.setText(entity, "taproot_core", "purple_wool");
                GameUtils.NBT.entity.setText(entity, "secondary_root_outer", "magenta_concrete");
                GameUtils.NBT.entity.setText(entity, "secondary_root_inner", "magenta_terracotta");
                GameUtils.NBT.entity.setText(entity, "secondary_root_core", "magenta_wool");
                GameUtils.NBT.entity.setText(entity, "tertiary_root_outer", "pink_concrete");
                GameUtils.NBT.entity.setText(entity, "tertiary_root_inner", "pink_terracotta");
                GameUtils.NBT.entity.setText(entity, "tertiary_root_core", "pink_wool");
                GameUtils.NBT.entity.setText(entity, "fine_root_outer", "light_blue_concrete");
                GameUtils.NBT.entity.setText(entity, "fine_root_inner", "light_blue_terracotta");
                GameUtils.NBT.entity.setText(entity, "fine_root_core", "light_blue_wool");

                GameUtils.NBT.entity.setText(entity, "trunk_outer", "red_concrete");
                GameUtils.NBT.entity.setText(entity, "trunk_inner", "red_terracotta");
                GameUtils.NBT.entity.setText(entity, "trunk_core", "red_wool");
                GameUtils.NBT.entity.setText(entity, "branch_outer", "orange_concrete");
                GameUtils.NBT.entity.setText(entity, "branch_inner", "orange_terracotta");
                GameUtils.NBT.entity.setText(entity, "branch_core", "orange_wool");
                GameUtils.NBT.entity.setText(entity, "twig_outer", "yellow_concrete");
                GameUtils.NBT.entity.setText(entity, "twig_inner", "yellow_terracotta");
                GameUtils.NBT.entity.setText(entity, "twig_core", "yellow_wool");
                GameUtils.NBT.entity.setText(entity, "leaves_twig_outer", "lime_concrete");
                GameUtils.NBT.entity.setText(entity, "leaves_twig_inner", "lime_terracotta");
                GameUtils.NBT.entity.setText(entity, "leaves_twig_core", "lime_wool");

                GameUtils.NBT.entity.setText(entity, "leaves1", "lime_stained_glass");
                GameUtils.NBT.entity.setText(entity, "leaves2", "green_stained_glass");

            }

        }

        // Summon Status Display
        GameUtils.command.runEntity(entity, "execute positioned ~ ~1 ~ run " + GameUtils.misc.summonEntity("text_display", GameUtils.NBT.entity.getText(entity, "id") + " / tree_generator_status", "Tree Generator Status", "see_through:1b,alignment:\"left\",brightness:{block:15},line_width:1000,transformation:{left_rotation:[0f,0f,0f,1f],right_rotation:[0f,0f,0f,1f],translation:[0f,0f,0f],scale:[1f,1f,1f]},billboard:vertical,text:'{\"text\":\"In Progress...\",\"color\":\"white\"}'"));

    }

    private static String[] typePreNext (Entity entity) {

        String[] return_text = new String[2];
        String type = GameUtils.NBT.entity.getText(entity, "type");

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
                    .append("Thickness : ").append(GameUtils.NBT.entity.getNumber(entity, type + "_thickness")).append(" / ").append((int) GameUtils.NBT.entity.getNumber(entity, type + "_thickness_max"))
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

                stepSummon(level, entity, id, type, type_pre_next, step);

            } else if (step.equals("calculation") == true) {

                stepCalculation(level, entity, id, type, type_pre_next, step);

            } else if (step.equals("build") == true) {

                stepBuild(level, entity, id, type);

            } else {

                stepEnd(level, entity, id);
                break;

            }

            // Break Out
            {

                if (process_break(entity) == true) {

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

    private static boolean process_break (Entity entity) {

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

    private static void stepSummon (LevelAccessor level, Entity entity, String id, String type, String[] type_pre_next, String step) {

        GameUtils.NBT.entity.setText(entity, "step", "build");
        GameUtils.NBT.entity.addNumber(entity, type + "_count", -1);
        boolean taproot_trunk = type.equals("taproot") == true || type.equals("trunk") == true;

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

            } else {

                // Leaves
                {

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

            if (taproot_trunk == true) {

                at_part = "tree_generator";
                positioned = "positioned ~ ~ ~";

            } else {

                at_part = "generator_" + type_pre_next[0];

                if (GameUtils.NBT.entity.getLogic(entity, type + "_center_direction") == false) {

                    // Non Center Direction
                    {

                        double vertical = Mth.nextDouble(RandomSource.create(), -(GameUtils.NBT.entity.getNumber(entity, type + "_start_vertical")), GameUtils.NBT.entity.getNumber(entity, type + "_start_vertical"));
                        double horizontal = Mth.nextDouble(RandomSource.create(), -(GameUtils.NBT.entity.getNumber(entity, type + "_start_horizontal")), GameUtils.NBT.entity.getNumber(entity, type + "_start_horizontal"));
                        double forward = Mth.nextDouble(RandomSource.create(), GameUtils.NBT.entity.getNumber(entity, type + "_start_forward_min"), GameUtils.NBT.entity.getNumber(entity, type + "_start_forward_max"));
                        double height = Mth.nextDouble(RandomSource.create(), GameUtils.NBT.entity.getNumber(entity, type + "_start_height_min"), GameUtils.NBT.entity.getNumber(entity, type + "_start_height_max"));
                        vertical = Double.parseDouble(String.format("%.2f", vertical));
                        horizontal = Double.parseDouble(String.format("%.2f",horizontal));
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

    }

    private static void stepSummonReduceFrom (Entity entity, String id, String type, String step, String type_next) {

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

                if (type.equals("taproot") == false && type.equals("fine_root") == false && type.equals("trunk") == false && type.equals("leaves_twig") == false) {



                }



            }

        }

    }

    private static void stepCalculation (LevelAccessor level, Entity entity, String id, String type, String[] type_pre_next, String step) {

        if (GameUtils.NBT.entity.getNumber(entity, type + "_length") > 0) {

            // Continue
            {

                GameUtils.NBT.entity.setText(entity, "step", "build");
                GameUtils.NBT.entity.addNumber(entity, type + "_length", -1);

                // Direction and Gravity
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

                double thickness = GameUtils.NBT.entity.getNumber(entity, type + "_length") / GameUtils.NBT.entity.getNumber(entity, type + "_length_save");
                thickness = thickness * (GameUtils.NBT.entity.getNumber(entity, type + "_thickness_max") - GameUtils.NBT.entity.getNumber(entity, type + "_thickness_min"));
                thickness = (GameUtils.NBT.entity.getNumber(entity, type + "_thickness_min") + thickness) - 1;
                GameUtils.NBT.entity.setNumber(entity, type + "_thickness", Double.parseDouble(String.format("%.2f", thickness)));




                GameUtils.command.run(level, 0, 0, 0, "execute as @e[tag=TANSHUGETREES-" + id + ",tag=TANSHUGETREES-generator_" + type + "] at @s run tp @s ^ ^ ^1");

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

    private static void stepBuild (LevelAccessor level, Entity entity, String id, String type) {

        String generator_type = GameUtils.NBT.entity.getText(entity, type + "_generator_type");
        double thickness = GameUtils.NBT.entity.getNumber(entity, type + "_thickness");
        double half_thickness = thickness * 0.5;

        if (GameUtils.NBT.entity.getLogic(entity, "still_building") == false) {

            GameUtils.NBT.entity.setLogic(entity, "still_building", true);

            // Get Builder Pos
            {

                GameUtils.command.runEntity(entity, "data modify entity @s ForgeData.create_pos set from entity @e[tag=TANSHUGETREES-" + id + ",tag=TANSHUGETREES-generator_" + type + ",limit=1] Pos");
                ListTag get = GameUtils.NBT.entity.getListNumber(entity, "create_pos");
                GameUtils.NBT.entity.setNumber(entity, "build_centerX", get.getDouble(0));
                GameUtils.NBT.entity.setNumber(entity, "build_centerY", get.getDouble(1));
                GameUtils.NBT.entity.setNumber(entity, "build_centerZ", get.getDouble(2));

            }

            // Start Settings
            {

                if (generator_type.equals("square") == true) {

                    GameUtils.NBT.entity.setNumber(entity, "build_saveX", -(half_thickness));
                    GameUtils.NBT.entity.setNumber(entity, "build_saveY", -(half_thickness));
                    GameUtils.NBT.entity.setNumber(entity, "build_saveZ", -(half_thickness));


                }

            }

        }

        int center_posX = (int) GameUtils.NBT.entity.getNumber(entity, "build_centerX");
        int center_posY = (int) GameUtils.NBT.entity.getNumber(entity, "build_centerY");
        int center_posZ = (int) GameUtils.NBT.entity.getNumber(entity, "build_centerZ");
        String block = GameUtils.NBT.entity.getText(entity, type + "_outer");

        if (type.equals("leaves") == false) {

            if (thickness <= 0.5) {

                // One Block Thickness
                {

                    placeBlock(level, new BlockPos(center_posX, center_posY, center_posZ), block, type);
                    GameUtils.command.run(level, center_posX, center_posY, center_posZ, "particle flash ~ ~ ~ 0 0 0 0 1 force");

                }

            } else {

                if (generator_type.equals("square") == true) {

                    {

                        while (true) {

                            double build_saveX = GameUtils.NBT.entity.getNumber(entity, "build_saveX");
                            double build_saveY = GameUtils.NBT.entity.getNumber(entity, "build_saveY");
                            double build_saveZ = GameUtils.NBT.entity.getNumber(entity, "build_saveZ");

                            if (build_saveY < Math.ceil(half_thickness)) {

                                if (build_saveX < Math.ceil(half_thickness)) {

                                    if (build_saveZ < Math.ceil(half_thickness)) {

                                        // Place Block
                                        {

                                            BlockPos pos = new BlockPos((int) (center_posX + build_saveX), (int) (center_posY + build_saveY), (int) (center_posZ + build_saveZ));

                                            if (level.getBlockState(pos).isAir() == true) {

                                                placeBlock(level, pos, block, type);
                                                GameUtils.command.run(level, pos.getX(), pos.getY(), pos.getZ(), "particle flash ~ ~ ~ 0 0 0 0 1 force");
                                                return;

                                            }

                                        }

                                        GameUtils.NBT.entity.addNumber(entity, "build_saveZ", 1);

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

                } else if (generator_type.equals("sphere") == true) {


                }

            }

        } else {

            // Leaves

        }

        // If it builds to the end without any break
        GameUtils.NBT.entity.setLogic(entity, "still_building", false);
        GameUtils.NBT.entity.setText(entity, "step", "calculation");

    }

    private static void blockOuterInnerCore () {



    }

    private static void placeBlock (LevelAccessor level, BlockPos pos, String block, String type) {

        BlockState block_placer = GameUtils.block.fromText("tanshugetrees:block_placer_" + type + "_" + "outer");
        level.setBlock(pos, block_placer, 2);
        GameUtils.NBT.block.setText(level, pos, "block", block);

    }

    private static void stepEnd (LevelAccessor level, Entity entity, String id) {

        GameUtils.command.runEntity(entity, "summon firework_rocket ~20 ~10 ~20 {LifeTime:40,FireworksItem:{id:firework_rocket,Count:1,tag:{Fireworks:{Flight:2,Explosions:[{Type:4,Flicker:1,Trail:1,Colors:[I;3887386,4312372],FadeColors:[I;3887386,4312372]}]}}}}");
        GameUtils.command.runEntity(entity, "summon firework_rocket ~20 ~10 ~-20 {LifeTime:40,FireworksItem:{id:firework_rocket,Count:1,tag:{Fireworks:{Flight:2,Explosions:[{Type:4,Flicker:1,Trail:1,Colors:[I;3887386,4312372],FadeColors:[I;3887386,4312372]}]}}}}");
        GameUtils.command.runEntity(entity, "summon firework_rocket ~-20 ~10 ~20 {LifeTime:40,FireworksItem:{id:firework_rocket,Count:1,tag:{Fireworks:{Flight:2,Explosions:[{Type:4,Flicker:1,Trail:1,Colors:[I;3887386,4312372],FadeColors:[I;3887386,4312372]}]}}}}");
        GameUtils.command.runEntity(entity, "summon firework_rocket ~-20 ~10 ~-20 {LifeTime:40,FireworksItem:{id:firework_rocket,Count:1,tag:{Fireworks:{Flight:2,Explosions:[{Type:4,Flicker:1,Trail:1,Colors:[I;3887386,4312372],FadeColors:[I;3887386,4312372]}]}}}}");
        GameUtils.command.run(level, 0, 0, 0, "kill @e[tag=TANSHUGETREES-" + id + "]");

    }

}
