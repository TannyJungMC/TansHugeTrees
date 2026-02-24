package tannyjung.tanshugetrees_handcode.systems.tree_generator;

import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.Vec3;
import tannyjung.tanshugetrees_core.Core;
import tannyjung.tanshugetrees_core.game.TXTFunction;
import tannyjung.tanshugetrees_core.game.GameUtils;
import tannyjung.tanshugetrees.network.TanshugetreesModVariables;
import tannyjung.tanshugetrees_handcode.data.FileConfig;

import java.io.File;
import java.util.Locale;

public class TreeGenerator {

    public static void create (ServerLevel level_server, int posX, int posY, int posZ, String path) {

        String[] split = path.split("/");

        try {

            path = Core.path_config + "/#dev/#temporary/presets/" + split[0] + "/" + split[1] + "/" + split[1] + ".txt";

        } catch (Exception ignored) {



        }

        File file = new File(path);

        if (file.exists() == true && file.isDirectory() == false) {

            GameUtils.entity.summon(level_server, posX + 0.5, posY + 0.5, posZ + 0.5, "minecraft:marker", "Tree Generator", "TANSHUGETREES-tree_generator", GameUtils.nbt.convertFileToForgeData(path));

        } else {

            GameUtils.misc.sendChatMessage(level_server, "@s", "Path Not Found / red");

        }

    }

    public static void run (LevelAccessor level_accessor, Entity entity) {

        ServerLevel level_server = (ServerLevel) level_accessor;
        GameUtils.misc.spawnParticle(level_server, entity.getX(), entity.getY(), entity.getZ(), 0, 0, 0, 0, 1, "minecraft:composter");

        if (GameUtils.nbt.entity.getLogic(entity, "start") == false) {

            GameUtils.nbt.entity.setLogic(entity, "start", true);
            beforeRunSystem(level_accessor, level_server, entity);

        } else {

            GameUtils.nbt.entity.addNumber(entity, "tree_generator_speed_tick_test", 1);

            if (GameUtils.nbt.entity.getNumber(entity, "tree_generator_speed_tick_test") >= GameUtils.nbt.entity.getNumber(entity, "tree_generator_speed_tick")) {

                GameUtils.nbt.entity.setNumber(entity, "tree_generator_speed_tick_test", 0);
                runSystem(level_accessor, level_server, entity);

            }

        }

    }

    private static void beforeRunSystem (LevelAccessor level_accessor, ServerLevel level_server, Entity entity) {

        GameUtils.nbt.entity.setText(entity, "id", entity.getUUID().toString());
        entity.addTag("TANSHUGETREES-" + GameUtils.nbt.entity.getText(entity, "id"));
        GameUtils.nbt.entity.setText(entity, "type", "taproot");
        GameUtils.nbt.entity.setText(entity, "step", "summon");
        GameUtils.nbt.entity.setNumber(entity, "taproot_count", Mth.nextInt(RandomSource.create(), (int) GameUtils.nbt.entity.getNumber(entity, "taproot_count_min"), (int) GameUtils.nbt.entity.getNumber(entity, "taproot_count_max")));
        GameUtils.nbt.entity.setNumber(entity, "trunk_count", Mth.nextInt(RandomSource.create(), (int) GameUtils.nbt.entity.getNumber(entity, "trunk_count_min"), (int) GameUtils.nbt.entity.getNumber(entity, "trunk_count_max")));

        if (GameUtils.nbt.entity.getNumber(entity, "taproot_count") == 0) {

            GameUtils.nbt.entity.setText(entity, "type", "trunk");

        }

        if (TanshugetreesModVariables.MapVariables.get(level_accessor).shape_file_converter == false) {

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

            Entity entity_countdown = GameUtils.entity.getAtAreaOne(level_server, entity.getX(), entity.getY() + 1, entity.getZ(), 1, true, "minecraft:text_display", "TANSHUGETREES-tree_countdown");

            if (entity_countdown != null) {

                entity_countdown.discard();

            }

            entity.setPos(entity.getX(), entity.getY() + GameUtils.nbt.entity.getNumber(entity, "start_height"), entity.getZ());
            GameUtils.entity.summon(level_server, entity.getX(), entity.getY() + 1, entity.getZ(), "text_display", "Tree Generator Status", "TANSHUGETREES-" + GameUtils.nbt.entity.getText(entity, "id") + " / TANSHUGETREES-tree_generator_status", "{see_through:1b,alignment:\"left\",brightness:{block:15, sky:15},line_width:1000,transformation:{left_rotation:[0f,0f,0f,1f],right_rotation:[0f,0f,0f,1f],translation:[0f,0f,0f],scale:[1f,1f,1f]},billboard:vertical,text:'{\"text\":\"In Progress...\",\"color\":\"white\"}'}");
            TXTFunction.run(level_accessor, level_server, entity.getBlockX(), entity.getBlockY(), entity.getBlockZ(), "functions/" + GameUtils.nbt.entity.getText(entity, "function_start"), true);

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
            command.append("Total Processes : ").append((int) GameUtils.nbt.entity.getNumber(entity, "total_processes"));
            command.append("\n");
            command.append("Generating : ").append(type);
            command.append("\n");
            command.append("\n");
            command.append("Step : ").append(step);
            command.append("\n");
            command.append("Count : ").append((int) GameUtils.nbt.entity.getNumber(entity, type + "_count"));
            command.append("\n");
            command.append("Length : ").append((int) GameUtils.nbt.entity.getNumber(entity, type + "_length")).append(" / ").append((int) GameUtils.nbt.entity.getNumber(entity, type + "_length_save"));
            command.append("\n");
            command.append("Thickness : ").append(GameUtils.nbt.entity.getNumber(entity, type + "_thickness")).append(" / ").append(GameUtils.nbt.entity.getNumber(entity, type + "_thickness_start"));
            GameUtils.command.runEntity(entity, "execute positioned ~ ~1 ~ run data merge entity @e[tag=TANSHUGETREES-tree_generator_status,distance=..1,limit=1] {text:'{\"text\":\"" + command + "\",\"color\":\"white\"}'}");

        }

        if (FileConfig.tree_generator_speed_global == true && GameUtils.nbt.entity.getLogic(entity, "tree_generator_speed_global") == true) {

            GameUtils.nbt.entity.setNumber(entity, "tree_generator_speed_tick", FileConfig.tree_generator_speed_tick);
            GameUtils.nbt.entity.setNumber(entity, "tree_generator_speed_repeat", FileConfig.tree_generator_speed_repeat);

        }

        while (true) {

            GameUtils.nbt.entity.addNumber(entity, "total_processes", 1);
            type = GameUtils.nbt.entity.getText(entity, "type");
            step = GameUtils.nbt.entity.getText(entity, "step");
            type_pre_next = getTypePreNext(type);

            if (step.equals("summon") == true) {

                Step.summon(level_server, entity, id, type, type_pre_next);

            } else if (step.equals("calculation") == true) {

                Step.calculation(level_server, entity, id, type, type_pre_next);

            } else if (step.equals("build") == true) {

                Step.build(level_accessor, level_server, entity, id, type, type_pre_next);

            } else {

                Step.end(level_accessor, level_server, entity, id);
                break;

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

        if (GameUtils.nbt.entity.getNumber(entity, "tree_generator_speed_repeat") != 0) {

            if (GameUtils.nbt.entity.getNumber(entity, "tree_generator_speed_repeat_test") < GameUtils.nbt.entity.getNumber(entity, "tree_generator_speed_repeat")) {

                GameUtils.nbt.entity.addNumber(entity, "tree_generator_speed_repeat_test", 1);

            } else {

                GameUtils.nbt.entity.setNumber(entity, "tree_generator_speed_repeat_test", 0);
                return_logic = true;

            }

        }

        return return_logic;

    }

    private static String[] getTypePreNext (String type) {

        String[] return_text = new String[2];

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

                    double length = Mth.nextInt(RandomSource.create(), (int) GameUtils.nbt.entity.getNumber(entity, type + "_length_min"), (int) GameUtils.nbt.entity.getNumber(entity, type + "_length_max"));
                    length = Math.ceil(length * summonReduction(entity, type, "length_reduce"));
                    GameUtils.nbt.entity.setNumber(entity, type + "_length", length);
                    GameUtils.nbt.entity.setNumber(entity, type + "_length_save", length);

                    double thickness = GameUtils.nbt.entity.getNumber(entity, type + "_thickness_start") - GameUtils.nbt.entity.getNumber(entity, type + "_thickness_end");
                    thickness = thickness * summonReduction(entity, type, "thickness_reduce");
                    thickness = GameUtils.nbt.entity.getNumber(entity, type + "_thickness_end") + thickness;
                    GameUtils.nbt.entity.setNumber(entity, type + "_thickness", thickness);

                }

                if (type_pre_next[1].equals("leaves") == false) {

                    // Next Part Settings
                    {

                        int count = Mth.nextInt(RandomSource.create(), (int) GameUtils.nbt.entity.getNumber(entity, type_pre_next[1] + "_count_min"), (int) GameUtils.nbt.entity.getNumber(entity, type_pre_next[1] + "_count_max"));
                        count = (int) Math.ceil((double) count * summonReduction(entity, type_pre_next[1], "count_reduce"));
                        GameUtils.nbt.entity.setNumber(entity, type_pre_next[1] + "_count", count);
                        GameUtils.nbt.entity.setNumber(entity, type_pre_next[1] + "_count_save", count);

                    }

                    // Auto Distance
                    {

                        if (GameUtils.nbt.entity.getNumber(entity, type_pre_next[1] + "_count") > 0) {

                            double distance = 0.0;

                            if (GameUtils.nbt.entity.getLogic(entity, type_pre_next[1] + "_chance_auto") == true) {

                                double count_available = GameUtils.nbt.entity.getNumber(entity, type_pre_next[1] + "_count_save") - GameUtils.nbt.entity.getNumber(entity, type_pre_next[1] + "_min_last_count");

                                if (count_available > 0) {

                                    double length_percent = Math.ceil(GameUtils.nbt.entity.getNumber(entity, type + "_length_save") * (GameUtils.nbt.entity.getNumber(entity, type_pre_next[1] + "_chance_percent") * 0.01));
                                    distance = length_percent / count_available;

                                }

                            }

                            GameUtils.nbt.entity.setNumber(entity, type_pre_next[1] + "_chance_auto_distance", distance);
                            GameUtils.nbt.entity.setNumber(entity, type_pre_next[1] + "_chance_distance_left", 0);

                        }

                    }

                } else {

                    // Leaves Settings
                    {

                        GameUtils.nbt.entity.setNumber(entity, "leaves_count", 1);
                        GameUtils.nbt.entity.setNumber(entity, "leaves_length", 1);
                        GameUtils.nbt.entity.setNumber(entity, "leaves_length_save", 1);

                        double size = Mth.nextDouble(RandomSource.create(), GameUtils.nbt.entity.getNumber(entity, "leaves_size_min"), GameUtils.nbt.entity.getNumber(entity, "leaves_size_max"));
                        size = size * summonReduction(entity, "leaves", "size_reduce");
                        GameUtils.nbt.entity.setNumber(entity, "leaves_size", size);

                    }

                }

                // Chance of Discontinuation
                {

                    if (is_taproot_trunk == false) {

                        double chance = GameUtils.nbt.entity.getNumber(entity, type + "_continue_chance");
                        chance = chance * summonReduction(entity, type, "continue_reduce");
                        chance = 1.0 - chance;

                        if (Math.random() < chance) {

                            GameUtils.nbt.entity.setNumber(entity, type_pre_next[1] + "_count", 0);

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

                        if (type.equals("leaves") == false) {

                            if (GameUtils.nbt.entity.getText(entity, type + "_center_direction_from").isEmpty() == true) {

                                // Non-Center Direction
                                {

                                    if (GameUtils.nbt.entity.getNumber(entity, type + "_min_last_count") > 0 && GameUtils.nbt.entity.getNumber(entity, type + "_count") <= 1) {

                                        // Make last part facing same as previous part
                                        forward = 1;

                                    } else {

                                        vertical = GameUtils.nbt.entity.getNumber(entity, type + "_start_vertical");
                                        horizontal = GameUtils.nbt.entity.getNumber(entity, type + "_start_horizontal");
                                        height = Mth.nextDouble(RandomSource.create(), GameUtils.nbt.entity.getNumber(entity, type + "_start_height_min"), GameUtils.nbt.entity.getNumber(entity, type + "_start_height_max"));
                                        forward = Mth.nextDouble(RandomSource.create(), GameUtils.nbt.entity.getNumber(entity, type + "_start_forward_min"), GameUtils.nbt.entity.getNumber(entity, type + "_start_forward_max"));

                                    }

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

                            vertical = Mth.nextDouble(RandomSource.create(), -(vertical), vertical);
                            horizontal = Mth.nextDouble(RandomSource.create(), -(horizontal), horizontal);

                        }

                    }

                }

                Entity entity_at = GameUtils.entity.getAtEverywhereOne(level_server, "minecraft:marker", "TANSHUGETREES-" + id + " / TANSHUGETREES-" + at_part);

                if (entity_at != null) {

                    Vec3 vec3 = GameUtils.entity.getPosLook(entity_at, horizontal, vertical, forward);
                    Entity entity_summon = GameUtils.entity.summon(level_server, vec3.x, vec3.y + height, vec3.z, "minecraft:marker", "Tree Generator (" + type + ")", "TANSHUGETREES-" + id + " / TANSHUGETREES-generator_" + type, "");

                    if (is_taproot_trunk == true) {

                        entity_summon.setXRot(Mth.nextInt(RandomSource.create(), (int) GameUtils.nbt.entity.getNumber(entity, type + "_start_gravity_max"), (int) GameUtils.nbt.entity.getNumber(entity, type + "_start_gravity_min")));
                        entity_summon.setYRot(Mth.nextInt(RandomSource.create(), (int) GameUtils.nbt.entity.getNumber(entity, type + "_start_direction_min"), (int) GameUtils.nbt.entity.getNumber(entity, type + "_start_direction_max")));

                    } else {

                        Entity entity_pre = GameUtils.entity.getAtEverywhereOne(level_server, "minecraft:marker", "TANSHUGETREES-" + id + " / TANSHUGETREES-generator_" + type_pre_next[0]);

                        if (entity_pre != null) {

                            entity_summon.lookAt(EntityAnchorArgument.Anchor.FEET, entity_pre.position());
                            entity_summon.setPos(entity_pre.position());
                            entity_summon.lookAt(EntityAnchorArgument.Anchor.FEET, GameUtils.entity.getPosLook(entity_summon, 0, 0, -1));

                        }

                    }

                }

            }

            // Next Step
            {

                if (is_taproot_trunk == true) {

                    GameUtils.nbt.entity.setText(entity, "step", "build");

                } else {

                    GameUtils.nbt.entity.setText(entity, "step", "calculation");

                }

            }

            GameUtils.nbt.entity.addNumber(entity, type + "_count", -1);

        }

        private static double summonReduction (Entity entity, String type, String step) {

            double return_number = 1.0;
            String from = GameUtils.nbt.entity.getText(entity, type + "_" + step + "_from");

            if (from.isEmpty() == false) {

                int length = (int) GameUtils.nbt.entity.getNumber(entity, from + "_length");
                int length_save = (int) GameUtils.nbt.entity.getNumber(entity, from + "_length_save");
                double center = GameUtils.nbt.entity.getNumber(entity, type + "_" + step + "_center") * 0.01;
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

                double start = GameUtils.nbt.entity.getNumber(entity, type + "_" + step + "_" + above_below + "_start");
                double end = GameUtils.nbt.entity.getNumber(entity, type + "_" + step + "_" + above_below + "_end");
                return_number = (start - end) * percent;
                return_number = end + return_number;
                return_number = return_number * 0.01;

            }

            return return_number;
        }

        private static void calculation (ServerLevel level_server, Entity entity, String id, String type, String[] type_pre_next) {

            Entity entity_current = GameUtils.entity.getAtEverywhereOne(level_server, "minecraft:marker", "TANSHUGETREES-" + id + " / TANSHUGETREES-generator_" + type);

            if (entity_current == null) {

                return;

            }

            GameUtils.nbt.entity.setNumber(entity, "previous_posX", entity_current.position().x);
            GameUtils.nbt.entity.setNumber(entity, "previous_posY", entity_current.position().y);
            GameUtils.nbt.entity.setNumber(entity, "previous_posZ", entity_current.position().z);
            boolean go_next = false;

            // Test
            {

                if (GameUtils.nbt.entity.getNumber(entity, type + "_length") > 0) {

                    if (GameUtils.nbt.entity.getNumber(entity, type_pre_next[1] + "_count") > GameUtils.nbt.entity.getNumber(entity, type_pre_next[1] + "_min_last_count")) {

                        // Length Range
                        if (GameUtils.nbt.entity.getNumber(entity, type + "_length") / GameUtils.nbt.entity.getNumber(entity, type + "_length_save") <= GameUtils.nbt.entity.getNumber(entity, type_pre_next[1] + "_chance_percent") * 0.01) {

                            if (GameUtils.nbt.entity.getNumber(entity, type_pre_next[1] + "_chance_distance_left") > 0) {

                                GameUtils.nbt.entity.addNumber(entity, type_pre_next[1] + "_chance_distance_left", -1);

                            } else {

                                GameUtils.nbt.entity.setNumber(entity, type_pre_next[1] + "_chance_distance_left", GameUtils.nbt.entity.getNumber(entity, type_pre_next[1] + "_chance_auto_distance"));

                                // Chance
                                if (Math.random() < GameUtils.nbt.entity.getNumber(entity, type_pre_next[1] + "_chance")) {

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

                            float vertical = (float) GameUtils.nbt.entity.getNumber(entity, type + "_curvature_vertical");
                            float horizontal = (float) GameUtils.nbt.entity.getNumber(entity, type + "_curvature_horizontal");
                            entity_current.setXRot(entity_current.getXRot() + Mth.nextFloat(RandomSource.create(), -(vertical), vertical));
                            entity_current.setYRot(entity_current.getYRot() + Mth.nextFloat(RandomSource.create(), -(horizontal), horizontal));

                        }

                        boolean gravity_run = false;

                        // Gravity
                        {

                            float weightiness = (float) GameUtils.nbt.entity.getNumber(entity, type + "_gravity_weightiness");

                            if (weightiness != 0) {

                                if (entity_current.getXRot() >= GameUtils.nbt.entity.getNumber(entity, type + "_gravity_min")) {

                                    entity_current.setXRot(entity_current.getXRot() - weightiness);
                                    gravity_run = true;

                                }

                                if (entity_current.getXRot() <= GameUtils.nbt.entity.getNumber(entity, type + "_gravity_max")) {

                                    entity_current.setXRot(entity_current.getXRot() + weightiness);
                                    gravity_run = true;

                                }

                            }

                        }

                        if (gravity_run == false) {

                            // Centripetal
                            {

                                double centripetal = GameUtils.nbt.entity.getNumber(entity, type + "_centripetal") * 0.01;

                                if (centripetal != 0) {

                                    Entity entity_trunk = GameUtils.entity.getAtEverywhereOne(level_server, "minecraft:marker", "TANSHUGETREES-" + id + " / TANSHUGETREES-generator_trunk");

                                    if (entity_trunk != null) {

                                        Vec3 vec3 = entity_current.position();

                                        entity_current.setPos(GameUtils.entity.getPosLook(entity_current, 0, 0, 1));
                                        entity_current.lookAt(EntityAnchorArgument.Anchor.FEET, entity_trunk.position());
                                        entity_current.setPos(GameUtils.entity.getPosLook(entity_current, 0, 0, -(centripetal)));

                                        entity_current.lookAt(EntityAnchorArgument.Anchor.FEET, vec3);
                                        entity_current.setPos(vec3);
                                        entity_current.lookAt(EntityAnchorArgument.Anchor.FEET, GameUtils.entity.getPosLook(entity_current, 0, 0, -1));

                                    }

                                }

                            }

                        }

                        double thickness = GameUtils.nbt.entity.getNumber(entity, type + "_thickness_start") - GameUtils.nbt.entity.getNumber(entity, type + "_thickness_end");
                        double length_percent = GameUtils.nbt.entity.getNumber(entity, type + "_length") / GameUtils.nbt.entity.getNumber(entity, type + "_length_save");
                        thickness = GameUtils.nbt.entity.getNumber(entity, type + "_thickness_end") + (thickness * length_percent);
                        GameUtils.nbt.entity.setNumber(entity, type + "_thickness", thickness);

                    }

                    entity_current.setPos(GameUtils.entity.getPosLook(entity_current, 0, 0, 1));
                    GameUtils.nbt.entity.addNumber(entity, type + "_length", -1);
                    GameUtils.nbt.entity.setText(entity, "step", "build");

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

                            entity_current.discard();

                            if (type.equals("taproot") == true) {

                                if (GameUtils.nbt.entity.getNumber(entity, type + "_count") > 0) {

                                    GameUtils.nbt.entity.setText(entity, "step", "summon");

                                } else {

                                    if (GameUtils.nbt.entity.getNumber(entity, type_pre_next[0] + "_count") > 0) {

                                        GameUtils.nbt.entity.setText(entity, "step", "summon");
                                        GameUtils.nbt.entity.setText(entity, "type", type_pre_next[0]);

                                    } else {

                                        GameUtils.nbt.entity.setText(entity, "step", "end");

                                    }

                                }

                            } else if (type.equals("trunk") == true) {

                                if (GameUtils.nbt.entity.getNumber(entity, type + "_count") > 0) {

                                    GameUtils.nbt.entity.setText(entity, "step", "summon");

                                } else {

                                    GameUtils.nbt.entity.setText(entity, "step", "end");

                                }

                            } else {

                                GameUtils.nbt.entity.setText(entity, "type", type_pre_next[0]);

                            }

                        }

                    }

                }

            }

        }

        private static void build (LevelAccessor level_accessor, ServerLevel level_server, Entity entity, String id, String type, String[] type_pre_next) {

            double thickness = GameUtils.nbt.entity.getNumber(entity, type + "_thickness");
            double size = 0.0;

            // Get Size and Radius
            {

                if (type.equals("leaves") == false) {

                    size = thickness;

                } else {

                    size = GameUtils.nbt.entity.getNumber(entity, "leaves_size");

                }

            }

            if (size > 0) {

                size = size - 1.0;

                if (size < 0) {

                    size = 0;

                }

                double radius = size * 0.5;
                double radius_ceil = Math.ceil(radius);
                String generator_type = GameUtils.nbt.entity.getText(entity, type + "_generator_type");

                // First Settings
                {

                    if (GameUtils.nbt.entity.getLogic(entity, "still_building") == false) {

                        GameUtils.nbt.entity.setLogic(entity, "still_building", true);

                        // Get Center Pos
                        {

                            Entity entity_current = GameUtils.entity.getAtEverywhereOne(level_server, "minecraft:marker", "TANSHUGETREES-" + id + " / TANSHUGETREES-generator_" + type);

                            if (entity_current == null) {

                                return;

                            }

                            GameUtils.nbt.entity.setNumber(entity, "build_centerX", entity_current.position().x);
                            GameUtils.nbt.entity.setNumber(entity, "build_centerY", entity_current.position().y);
                            GameUtils.nbt.entity.setNumber(entity, "build_centerZ", entity_current.position().z);

                        }

                        // Sphere Zone
                        {

                            if (generator_type.equals("sphere_zone") == true) {

                                Entity entity_pre = GameUtils.entity.getAtEverywhereOne(level_server, "minecraft:marker", "TANSHUGETREES-" + id + " / TANSHUGETREES-generator_" + type_pre_next[0]);

                                if (entity_pre == null) {

                                    return;

                                }

                                double yaw = Math.toRadians(((entity_pre.getYRot() + 180) + 90) % 360);
                                double pitch = entity_pre.getXRot();

                                // Min Pitch
                                {

                                    int pitch_min = (int) GameUtils.nbt.entity.getNumber(entity, type + "_sphere_zone_pitch_min");

                                    if (pitch > pitch_min) {

                                        pitch = pitch_min;

                                    }

                                }

                                pitch = Math.toRadians(pitch);
                                double distance = radius;
                                int sphere_zone_size = (int) GameUtils.nbt.entity.getNumber(entity, type + "_sphere_zone_size");

                                GameUtils.nbt.entity.setNumber(entity, "sphere_zone_posX", distance * Math.cos(pitch) * Math.cos(yaw));
                                GameUtils.nbt.entity.setNumber(entity, "sphere_zone_posY", distance * Math.sin(pitch));
                                GameUtils.nbt.entity.setNumber(entity, "sphere_zone_posZ", distance * Math.cos(pitch) * Math.sin(yaw));

                                // Change Build Center
                                {

                                    distance = distance - sphere_zone_size;
                                    GameUtils.nbt.entity.addNumber(entity, "build_centerX", distance * Math.cos(pitch) * Math.cos(yaw));
                                    GameUtils.nbt.entity.addNumber(entity, "build_centerY", distance * Math.sin(pitch));
                                    GameUtils.nbt.entity.addNumber(entity, "build_centerZ", distance * Math.cos(pitch) * Math.sin(yaw));

                                }

                                // Get Area
                                {

                                    double sphere_zone_area = size - sphere_zone_size;

                                    if (sphere_zone_area < 0) {

                                        sphere_zone_area = 0;

                                    }

                                    GameUtils.nbt.entity.setNumber(entity, "sphere_zone_area", sphere_zone_area * sphere_zone_area);

                                }

                            }

                        }

                        GameUtils.nbt.entity.setNumber(entity, "build_saveX", -(radius));
                        GameUtils.nbt.entity.setNumber(entity, "build_saveY", -(radius));
                        GameUtils.nbt.entity.setNumber(entity, "build_saveZ", -(radius));

                    }

                }

                double sphere_zone_area = 0.0;
                double[] sphere_zone_pos = new double[0];

                if (generator_type.equals("sphere_zone") == true) {

                    sphere_zone_area = GameUtils.nbt.entity.getNumber(entity, "sphere_zone_area");
                    sphere_zone_pos = new double[]{GameUtils.nbt.entity.getNumber(entity, "sphere_zone_posX"), GameUtils.nbt.entity.getNumber(entity, "sphere_zone_posY"), GameUtils.nbt.entity.getNumber(entity, "sphere_zone_posZ")};

                }

                double scan_change = radius / radius_ceil;

                if (radius == 0) {

                    scan_change = 1;

                }

                double center_posX = GameUtils.nbt.entity.getNumber(entity, "build_centerX");
                double center_posY = GameUtils.nbt.entity.getNumber(entity, "build_centerY");
                double center_posZ = GameUtils.nbt.entity.getNumber(entity, "build_centerZ");
                boolean replace = GameUtils.nbt.entity.getLogic(entity, type + "_replace");
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

                        build_saveX = GameUtils.nbt.entity.getNumber(entity, "build_saveX");
                        build_saveY = GameUtils.nbt.entity.getNumber(entity, "build_saveY");
                        build_saveZ = GameUtils.nbt.entity.getNumber(entity, "build_saveZ");

                        if (build_saveY > scan_end) {

                            break;

                        } else {

                            if (build_saveX > scan_end) {

                                GameUtils.nbt.entity.setNumber(entity, "build_saveX", -(scan_start));
                                GameUtils.nbt.entity.addNumber(entity, "build_saveY", scan_change);

                            } else {

                                if (build_saveZ > scan_end) {

                                    GameUtils.nbt.entity.setNumber(entity, "build_saveZ", -(scan_start));
                                    GameUtils.nbt.entity.addNumber(entity, "build_saveX", scan_change);

                                } else {

                                    GameUtils.nbt.entity.addNumber(entity, "build_saveZ", scan_change);

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

                                        if (type.equals("leaves") == false) {

                                            // General
                                            {

                                                if (size < 1 && build_saveX == 0 && build_saveY == 0 && build_saveZ == 0) {

                                                    buildBlockConnector(level_accessor, level_server, entity, center_posX, center_posY, center_posZ, pos, type, generator_type, radius, build_area, replace);

                                                }

                                                String previous_block = buildGetPreviousBlock(level_accessor, pos);
                                                String block_type = buildGetBlockType(entity, type, previous_block, radius, build_area);

                                                if (buildPlaceBlock(level_accessor, level_server, entity, pos, type, block_type, previous_block, replace) == false) {

                                                    return;

                                                }

                                            }

                                        } else {

                                            // Leaves
                                            {

                                                if (Math.random() < GameUtils.nbt.entity.getNumber(entity, "leaves_density") * 0.01) {

                                                    String previous_block = "";
                                                    String block_type = "";
                                                    BlockPos pos_leaves = null;
                                                    int deep = 0;

                                                    if (Math.random() < GameUtils.nbt.entity.getNumber(entity, "leaves_straighten_chance")) {

                                                        deep = Mth.nextInt(RandomSource.create(), (int) GameUtils.nbt.entity.getNumber(entity, "leaves_straighten_min"), (int) GameUtils.nbt.entity.getNumber(entity, "leaves_straighten_max"));

                                                    }

                                                    for (int deep_test = 0; deep_test <= deep; deep_test++) {

                                                        pos_leaves = new BlockPos(pos.getX(), pos.getY() - deep_test, pos.getZ());
                                                        previous_block = buildGetPreviousBlock(level_accessor, pos_leaves);
                                                        block_type = buildGetBlockType(entity, type, previous_block, radius, build_area);

                                                        if (buildPlaceBlock(level_accessor, level_server, entity, pos_leaves, type, block_type, previous_block, replace) == false) {

                                                            return;

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
            GameUtils.nbt.entity.setLogic(entity, "still_building", false);
            GameUtils.nbt.entity.setText(entity, "step", "calculation");

        }

        private static void buildBlockConnector (LevelAccessor level_accessor, ServerLevel level_server, Entity entity, double center_posX, double center_posY, double center_posZ, BlockPos pos, String type, String generator_type, double radius, double build_area, boolean replace) {

            double block_connector_posX = GameUtils.nbt.entity.getNumber(entity, "previous_posX");
            double block_connector_posY = GameUtils.nbt.entity.getNumber(entity, "previous_posY");
            double block_connector_posZ = GameUtils.nbt.entity.getNumber(entity, "previous_posZ");
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
                            String block_type = buildGetBlockType(entity, type, previous_block, radius, build_area);

                            if (buildPlaceBlock(level_accessor, level_server, entity, pos, type, block_type, previous_block, replace) == false) {

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
                            String block_type = buildGetBlockType(entity, type, previous_block, radius, build_area);

                            if (buildPlaceBlock(level_accessor, level_server, entity, pos, type, block_type, previous_block, replace) == false) {

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

                    if (Math.random() < GameUtils.nbt.entity.getNumber(entity, "leaves2_chance")) {

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

                        previous_block = GameUtils.block.toTextID(level_accessor.getBlockState(pos));

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

                }

            }

            return previous_block;

        }

        private static boolean buildPlaceBlock (LevelAccessor level_accessor, ServerLevel level_server, Entity entity, BlockPos pos, String type, String block_type, String previous_block, boolean replace) {

            if (block_type.isEmpty() == false) {

                boolean remove_then_add = false;

                if (previous_block.isEmpty() == false) {

                    if (replace == false) {

                        return false;

                    } else {

                        remove_then_add = true;

                    }

                }

                String type_short = type.substring(0, 2) + block_type.charAt(0);
                String block_placer = type + "_" + block_type;
                String block = block_placer;

                if (type.equals("leaves") == true) {

                    block = "leaves" + block_type;

                }

                if (GameUtils.nbt.entity.getText(entity, block).isEmpty() == false) {

                    // Place
                    {

                        String[] function = buildGetWayFunction(entity, type);

                        if (TanshugetreesModVariables.MapVariables.get(level_accessor).shape_file_converter == false) {

                            GameUtils.misc.spawnParticle(level_server, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 0, 0, 0, 0, 1, "minecraft:flash");
                            level_accessor.setBlock(pos, GameUtils.block.fromText("tanshugetrees:block_placer_" + block_placer), 2);

                            GameUtils.nbt.block.setText(level_accessor, level_server, pos.getX(), pos.getY(), pos.getZ(), "block", GameUtils.nbt.entity.getText(entity, block));
                            GameUtils.nbt.block.setText(level_accessor, level_server, pos.getX(), pos.getY(), pos.getZ(), "function", function[1]);
                            GameUtils.nbt.block.setText(level_accessor, level_server, pos.getX(), pos.getY(), pos.getZ(), "function_style", function[2]);

                        } else {

                            String key = pos.getX() + "/" + pos.getY() + "/" + pos.getZ();

                            if (remove_then_add == true) {

                                ShapeFileConverter.export_data.remove("B" + key);

                            }

                            ShapeFileConverter.export_data.put("B" + key, type_short);

                            if (function[0].isEmpty() == false) {

                                if (remove_then_add == true) {

                                    ShapeFileConverter.export_data.remove("F" + key);

                                }

                                ShapeFileConverter.export_data.put("F" + key, function[0]);

                            }

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

            for (int number = 1; number <= 9; number++) {

                function = "function_way" + number;

                if (Math.random() < GameUtils.nbt.entity.getNumber(entity, function + "_chance")) {

                    path = GameUtils.nbt.entity.getText(entity, function);
                    at_type = GameUtils.nbt.entity.getText(entity, function + "_type");
                    style = GameUtils.nbt.entity.getText(entity, function + "_style");

                    if (path.isEmpty() == false && at_type.equals(type) == true) {

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

                TXTFunction.run(level_accessor, level_server, entity.getBlockX(), entity.getBlockY(), entity.getBlockZ(), "functions/" + GameUtils.nbt.entity.getText(entity, "function_end"), true);

            } else {

                ShapeFileConverter.whenTreeEnd(level_accessor, level_server, entity);
                firework_position = "execute at @p run ";

            }

            GameUtils.entity.summon(level_server, entity.getX() + 20, entity.getY() + 10, entity.getZ() + 20, "minecraft:firework_rocket", "", "", "{LifeTime:40,FireworksItem:{id:firework_rocket,Count:1,tag:{Fireworks:{Flight:2,Explosions:[{Type:4,Flicker:1,Trail:1,Colors:[I;3887386,4312372],FadeColors:[I;3887386,4312372]}]}}}}");
            GameUtils.entity.summon(level_server, entity.getX() + 20, entity.getY() + 10, entity.getZ() - 20, "minecraft:firework_rocket", "", "", "{LifeTime:40,FireworksItem:{id:firework_rocket,Count:1,tag:{Fireworks:{Flight:2,Explosions:[{Type:4,Flicker:1,Trail:1,Colors:[I;3887386,4312372],FadeColors:[I;3887386,4312372]}]}}}}");
            GameUtils.entity.summon(level_server, entity.getX() - 20, entity.getY() + 10, entity.getZ() + 20, "minecraft:firework_rocket", "", "", "{LifeTime:40,FireworksItem:{id:firework_rocket,Count:1,tag:{Fireworks:{Flight:2,Explosions:[{Type:4,Flicker:1,Trail:1,Colors:[I;3887386,4312372],FadeColors:[I;3887386,4312372]}]}}}}");
            GameUtils.entity.summon(level_server, entity.getX() - 20, entity.getY() + 10, entity.getZ() - 20, "minecraft:firework_rocket", "", "", "{LifeTime:40,FireworksItem:{id:firework_rocket,Count:1,tag:{Fireworks:{Flight:2,Explosions:[{Type:4,Flicker:1,Trail:1,Colors:[I;3887386,4312372],FadeColors:[I;3887386,4312372]}]}}}}");

            for (Entity entity_import : GameUtils.entity.getAtEverywhere(level_server, "", "TANSHUGETREES-" + id)) {

                entity_import.discard();

            }

        }

    }

}
