package tannyjung.tanshugetrees_handcode.systems.tree_generator;

import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import tannyjung.tanshugetrees.network.TanshugetreesModVariables;
import tannyjung.tanshugetrees_core.Core;
import tannyjung.tanshugetrees_core.game.*;
import tannyjung.tanshugetrees_core.outside.OutsideUtils;
import tannyjung.tanshugetrees_core.outside.TXTFunction;
import tannyjung.tanshugetrees_handcode.Handcode;

import java.io.File;

public class TreeGenerator {

    public static Entity create (ServerLevel level_server, ServerPlayer player_server, BlockPos pos, String id) {

        File file = null;
        String path_type = "";

        // Get File
        {

            int index = id.indexOf("/");

            if (index != -1) {

                file = new File(Core.path_config + "/custom_packs/" + id.substring(0, index) + "/tree_blueprints/" + id.substring(index + 1) + ".txt");

                if (file.exists() == true) {

                    path_type = "Uncached";

                } else {

                    file = new File(Core.path_config + "/dev/temporary/tree_blueprints/" + id + ".txt");

                    if (file.exists() == true) {

                        path_type = "Cached";

                    } else {

                        if (player_server != null) {

                            GameUtils.sendChatMessagePrivate(player_server, "Path Not Detected -> " + id + " / red");

                        }

                    }

                }

            }

        }

        if (path_type.isEmpty() == false) {

            Entity entity_summon = EntityManager.summon(level_server, pos.getCenter(), false, "minecraft:marker", "Tree Generator", new String[]{"TANSHUGETREES-tree_generator"}, "{NeoForgeData:{tanshugetrees:{main:" + GameUtils.Data.convertFileToTag(file.getPath()) + "}}}");

            if (entity_summon != null) {

                if (TanshugetreesModVariables.MapVariables.get(level_server).shape_file_generator == true) {

                    NBTManager.Mob.setText(entity_summon, "main", "path_type", path_type, false);

                } else {

                    GameUtils.sendChatMessage(level_server, "Summoned a tree generator at " + pos.getX() + " " + pos.getY() + " " + pos.getZ() + "  / gray | [?] / dark_gray / " + id + " (" + path_type + ")");

                }

                NBTManager.Mob.setText(entity_summon, "main", "name", file.getName().substring(0, file.getName().length() - ".txt".length()), false);
                return entity_summon;

            }

        }

        return null;

    }

    public static void run (ServerLevel level_server, Entity entity) {

        GameUtils.spawnParticle(level_server, entity.position(), 0, 0, 0, 0, 1, "minecraft:composter");

        if (NBTManager.Mob.getLogic(entity, "main", "start") == false) {

            NBTManager.Mob.setLogic(entity, "main", "start", true, false);
            beforeRunSystem(level_server, entity);

        } else {

            NBTManager.Mob.addNumber(entity, "main", "tree_generator_speed_tick_test", 1, false);

            if (NBTManager.Mob.getNumber(entity, "main", "tree_generator_speed_tick_test") >= NBTManager.Mob.getNumber(entity, "main", "tree_generator_speed_tick")) {

                NBTManager.Mob.setNumber(entity, "main", "tree_generator_speed_tick_test", 0, false);
                runSystem(level_server, entity);

            }

        }

    }

    private static void beforeRunSystem (ServerLevel level_server, Entity entity) {

        String id = entity.getStringUUID();
        NBTManager.Mob.setText(entity, "main", "id", id, false);
        entity.addTag("TANSHUGETREES-" + id);

        NBTManager.Mob.setText(entity, "main", "gen_type", "taproot", false);
        NBTManager.Mob.setText(entity, "main", "gen_step", "summon", false);
        NBTManager.Mob.setNumber(entity, "main", "taproot_count", Mth.nextInt(RandomSource.create(), (int) NBTManager.Mob.getNumber(entity, "main", "taproot_count_min"), (int) NBTManager.Mob.getNumber(entity, "main", "taproot_count_max")), false);
        NBTManager.Mob.setNumber(entity, "main", "trunk_count", Mth.nextInt(RandomSource.create(), (int) NBTManager.Mob.getNumber(entity, "main", "trunk_count_min"), (int) NBTManager.Mob.getNumber(entity, "main", "trunk_count_max")), false);

        if (NBTManager.Mob.getNumber(entity, "main", "taproot_count") == 0) {

            NBTManager.Mob.setText(entity, "main", "gen_type", "trunk", false);

        }

        if (TanshugetreesModVariables.MapVariables.get(level_server).shape_file_generator == true) {

            ShapeFileGenerator.whenTreeStart(level_server, entity);

        } else {

            // Debug Mode
            {

                if (NBTManager.Mob.getLogic(entity, "main", "debug_mode") == true) {

                    NBTManager.Mob.setText(entity, "main", "taproot_outer", "purple_concrete", false);
                    NBTManager.Mob.setText(entity, "main", "taproot_inner", "purple_terracotta", false);
                    NBTManager.Mob.setText(entity, "main", "taproot_core", "purple_stained_glass", false);
                    NBTManager.Mob.setText(entity, "main", "secondary_root_outer", "magenta_concrete", false);
                    NBTManager.Mob.setText(entity, "main", "secondary_root_inner", "magenta_terracotta", false);
                    NBTManager.Mob.setText(entity, "main", "secondary_root_core", "magenta_stained_glass", false);
                    NBTManager.Mob.setText(entity, "main", "tertiary_root_outer", "pink_concrete", false);
                    NBTManager.Mob.setText(entity, "main", "tertiary_root_inner", "pink_terracotta", false);
                    NBTManager.Mob.setText(entity, "main", "tertiary_root_core", "pink_stained_glass", false);
                    NBTManager.Mob.setText(entity, "main", "fine_root_outer", "light_blue_concrete", false);
                    NBTManager.Mob.setText(entity, "main", "fine_root_inner", "light_blue_terracotta", false);
                    NBTManager.Mob.setText(entity, "main", "fine_root_core", "light_blue_stained_glass", false);

                    NBTManager.Mob.setText(entity, "main", "trunk_outer", "red_concrete", false);
                    NBTManager.Mob.setText(entity, "main", "trunk_inner", "red_terracotta", false);
                    NBTManager.Mob.setText(entity, "main", "trunk_core", "red_stained_glass", false);
                    NBTManager.Mob.setText(entity, "main", "bough_outer", "orange_concrete", false);
                    NBTManager.Mob.setText(entity, "main", "bough_inner", "orange_terracotta", false);
                    NBTManager.Mob.setText(entity, "main", "bough_core", "orange_stained_glass", false);
                    NBTManager.Mob.setText(entity, "main", "branch_outer", "yellow_concrete", false);
                    NBTManager.Mob.setText(entity, "main", "branch_inner", "yellow_terracotta", false);
                    NBTManager.Mob.setText(entity, "main", "branch_core", "yellow_stained_glass", false);
                    NBTManager.Mob.setText(entity, "main", "limb_outer", "lime_concrete", false);
                    NBTManager.Mob.setText(entity, "main", "limb_inner", "lime_terracotta", false);
                    NBTManager.Mob.setText(entity, "main", "limb_core", "lime_stained_glass", false);
                    NBTManager.Mob.setText(entity, "main", "twig_outer", "green_concrete", false);
                    NBTManager.Mob.setText(entity, "main", "twig_inner", "green_terracotta", false);
                    NBTManager.Mob.setText(entity, "main", "twig_core", "green_stained_glass", false);
                    NBTManager.Mob.setText(entity, "main", "sprig_outer", "white_concrete", false);
                    NBTManager.Mob.setText(entity, "main", "sprig_inner", "white_terracotta", false);
                    NBTManager.Mob.setText(entity, "main", "sprig_core", "white_stained_glass", false);

                    NBTManager.Mob.setText(entity, "main", "leaves1", "white_stained_glass", false);
                    NBTManager.Mob.setText(entity, "main", "leaves2", "black_stained_glass", false);

                }

            }

            Entity entity_countdown = EntityManager.Population.getEverywhereOneStatic(level_server, "", "", new String[]{"TANSHUGETREES-tree_countdown"});

            if (entity_countdown != null) {

                entity_countdown.discard();

            }

            entity.setPos(entity.getX(), entity.getY() + NBTManager.Mob.getNumber(entity, "main", "start_height"), entity.getZ());
            DisplayManager.summonText(level_server, entity.position(), 20, new String[]{"TANSHUGETREES-" + id, "TANSHUGETREES-tree_generator_status"}, "");
            TXTFunction.run(level_server, level_server, entity.blockPosition(), NBTManager.Mob.getText(entity, "main", "function_start"), true);

        }

        EntityManager.summon(level_server, entity.position(), false, "minecraft:marker", "Tree Generator (" + "taproot" + ")", new String[]{"TANSHUGETREES-" + id, "TANSHUGETREES-generator_" + "taproot"}, "");
        EntityManager.summon(level_server, entity.position(), false, "minecraft:marker", "Tree Generator (" + "secondary_root" + ")", new String[]{"TANSHUGETREES-" + id, "TANSHUGETREES-generator_" + "secondary_root"}, "");
        EntityManager.summon(level_server, entity.position(), false, "minecraft:marker", "Tree Generator (" + "tertiary_root" + ")", new String[]{"TANSHUGETREES-" + id, "TANSHUGETREES-generator_" + "tertiary_root"}, "");
        EntityManager.summon(level_server, entity.position(), false, "minecraft:marker", "Tree Generator (" + "fine_root" + ")", new String[]{"TANSHUGETREES-" + id, "TANSHUGETREES-generator_" + "fine_root"}, "");
        EntityManager.summon(level_server, entity.position(), false, "minecraft:marker", "Tree Generator (" + "trunk" + ")", new String[]{"TANSHUGETREES-" + id, "TANSHUGETREES-generator_" + "trunk"}, "");
        EntityManager.summon(level_server, entity.position(), false, "minecraft:marker", "Tree Generator (" + "bough" + ")", new String[]{"TANSHUGETREES-" + id, "TANSHUGETREES-generator_" + "bough"}, "");
        EntityManager.summon(level_server, entity.position(), false, "minecraft:marker", "Tree Generator (" + "branch" + ")", new String[]{"TANSHUGETREES-" + id, "TANSHUGETREES-generator_" + "branch"}, "");
        EntityManager.summon(level_server, entity.position(), false, "minecraft:marker", "Tree Generator (" + "limb" + ")", new String[]{"TANSHUGETREES-" + id, "TANSHUGETREES-generator_" + "limb"}, "");
        EntityManager.summon(level_server, entity.position(), false, "minecraft:marker", "Tree Generator (" + "twig" + ")", new String[]{"TANSHUGETREES-" + id, "TANSHUGETREES-generator_" + "twig"}, "");
        EntityManager.summon(level_server, entity.position(), false, "minecraft:marker", "Tree Generator (" + "sprig" + ")", new String[]{"TANSHUGETREES-" + id, "TANSHUGETREES-generator_" + "sprig"}, "");
        EntityManager.summon(level_server, entity.position(), false, "minecraft:marker", "Tree Generator (" + "leaves" + ")", new String[]{"TANSHUGETREES-" + id, "TANSHUGETREES-generator_" + "leaves"}, "");


    }

    private static void runSystem (ServerLevel level_server, Entity entity) {

        String id = NBTManager.Mob.getText(entity, "main", "id");
        String gen_type = NBTManager.Mob.getText(entity, "main", "gen_type");
        String gen_step = NBTManager.Mob.getText(entity, "main", "gen_step");
        String[] type_pre_next = null;

        // Status Display
        {

            Entity entity_status = EntityManager.Population.getEverywhereOneStatic(level_server, "", "", new String[]{"TANSHUGETREES-" + id, "TANSHUGETREES-tree_generator_status"});

            if (entity_status != null) {

                StringBuilder builder = new StringBuilder();
                builder.append("§b ↓ Generating a tree here ↓ ");
                builder.append("\n");
                builder.append("\n");
                builder.append("§fTotal Process : ").append((int) NBTManager.Mob.getNumber(entity, "main", "total_processes"));
                builder.append("\n");
                builder.append("§fStep : ").append(gen_step);
                builder.append("\n");
                builder.append("§fType : ").append(gen_type);
                builder.append("\n");
                builder.append("\n");
                builder.append("§fCount Left : ").append((int) NBTManager.Mob.getNumber(entity, "main", gen_type + "_count"));
                builder.append("\n");
                builder.append("§fLength : ").append((int) NBTManager.Mob.getNumber(entity, "main", gen_type + "_length")).append(" / ").append((int) NBTManager.Mob.getNumber(entity, "main", gen_type + "_length_save"));
                builder.append("\n");
                builder.append("§fThickness : ").append(OutsideUtils.Mathematic.shorterDouble(NBTManager.Mob.getNumber(entity, "main", gen_type + "_thickness"), 3)).append(" / ").append(NBTManager.Mob.getNumber(entity, "main", gen_type + "_thickness_start"));

                String error = NBTManager.Mob.getText(entity, "main", "error");

                if (error.isEmpty() == false) {

                    builder.append("\n");
                    builder.append("\n");
                    builder.append("§cError : ").append(error);

                }

                double build_centerY = NBTManager.Mob.getNumber(entity, "main", "build_centerY");

                if (build_centerY != 0 && entity_status.getY() - 25 < build_centerY) {

                    entity_status.setPos(entity_status.getX(), build_centerY + 25, entity_status.getZ());

                }

                GameUtils.runCommandEntity(entity_status, "data merge entity @s {text:'{\"text\":\"" + builder + "\",\"color\":\"white\"}'}");

            }

        }

        if (Handcode.Config.tree_generator_speed_global == true && NBTManager.Mob.getLogic(entity, "main", "tree_generator_speed_global") == true) {

            NBTManager.Mob.setNumber(entity, "main", "tree_generator_speed_tick", Handcode.Config.tree_generator_speed_tick, false);
            NBTManager.Mob.setNumber(entity, "main", "tree_generator_speed_repeat", Handcode.Config.tree_generator_speed_repeat, false);
            NBTManager.Mob.setNumber(entity, "main", "tree_generator_tp_limit", Handcode.Config.tree_generator_tp_limit, false);

        }

        NBTManager.Mob.setText(entity, "main", "error", "", false);

        while (true) {

            gen_type = NBTManager.Mob.getText(entity, "main", "gen_type");
            gen_step = NBTManager.Mob.getText(entity, "main", "gen_step");
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

                if (Step.build(level_server, entity, id, gen_type, type_pre_next) == true) {

                    NBTManager.Mob.setLogic(entity, "main", "still_building", false, false);
                    NBTManager.Mob.setText(entity, "main", "gen_step", "calculation", false);

                } else {

                    return;

                }

            } else {

                Step.end(level_server, entity, id);
                return;

            }

            NBTManager.Mob.addNumber(entity, "main", "total_processes", 1, false);

            // Process Break (Normal)
            {

                if (TanshugetreesModVariables.MapVariables.get(level_server).shape_file_generator == false) {

                    if (NBTManager.Mob.getNumber(entity, "main", "tree_generator_speed_repeat") != 0) {

                        if (NBTManager.Mob.getNumber(entity, "main", "tree_generator_speed_repeat_test") < NBTManager.Mob.getNumber(entity, "main", "tree_generator_speed_repeat")) {

                            NBTManager.Mob.addNumber(entity, "main", "tree_generator_speed_repeat_test", 1, false);

                        } else {

                            NBTManager.Mob.setNumber(entity, "main", "tree_generator_speed_repeat_test", 0, false);
                            break;

                        }

                    }

                }

            }

        }

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

                    double length = Mth.nextInt(RandomSource.create(), (int) NBTManager.Mob.getNumber(entity, "main", gen_type + "_length_min"), (int) NBTManager.Mob.getNumber(entity, "main", gen_type + "_length_max"));
                    length = Math.ceil(length * summonReduction(entity, gen_type, "length"));
                    NBTManager.Mob.setNumber(entity, "main", gen_type + "_length", length, false);
                    NBTManager.Mob.setNumber(entity, "main", gen_type + "_length_save", length, false);

                    double thickness = NBTManager.Mob.getNumber(entity, "main", gen_type + "_thickness_start") - NBTManager.Mob.getNumber(entity, "main", gen_type + "_thickness_end");
                    thickness = thickness * summonReduction(entity, gen_type, "thickness");
                    thickness = NBTManager.Mob.getNumber(entity, "main", gen_type + "_thickness_end") + thickness;
                    NBTManager.Mob.setNumber(entity, "main", gen_type + "_thickness", thickness, false);

                }

                if (type_pre_next[1].equals("leaves") == false) {

                    // Next Part Settings
                    {

                        int count = Mth.nextInt(RandomSource.create(), (int) NBTManager.Mob.getNumber(entity, "main", type_pre_next[1] + "_count_min"), (int) NBTManager.Mob.getNumber(entity, "main", type_pre_next[1] + "_count_max"));
                        count = (int) Math.ceil((double) count * summonReduction(entity, type_pre_next[1], "count"));
                        NBTManager.Mob.setNumber(entity, "main", type_pre_next[1] + "_count", count, false);
                        NBTManager.Mob.setNumber(entity, "main", type_pre_next[1] + "_count_save", count, false);

                    }

                    // Auto Distance
                    {

                        if (NBTManager.Mob.getNumber(entity, "main", type_pre_next[1] + "_count") > 0) {

                            double distance = 0.0;

                            if (NBTManager.Mob.getLogic(entity, "main", type_pre_next[1] + "_chance_auto") == true) {

                                double count_available = NBTManager.Mob.getNumber(entity, "main", type_pre_next[1] + "_count_save") - NBTManager.Mob.getNumber(entity, "main", type_pre_next[1] + "_min_last_count");

                                if (count_available > 0) {

                                    double length_percent = Math.ceil(NBTManager.Mob.getNumber(entity, "main", gen_type + "_length_save") * (NBTManager.Mob.getNumber(entity, "main", type_pre_next[1] + "_chance_percent") * 0.01));
                                    distance = length_percent / count_available;

                                }

                            }

                            NBTManager.Mob.setNumber(entity, "main", type_pre_next[1] + "_chance_auto_distance", distance, false);
                            NBTManager.Mob.setNumber(entity, "main", type_pre_next[1] + "_chance_distance_left", 0, false);

                        }

                    }

                } else {

                    // Leaves Settings
                    {

                        NBTManager.Mob.setNumber(entity, "main", "leaves_count", 1, false);
                        NBTManager.Mob.setNumber(entity, "main", "leaves_length", 1, false);
                        NBTManager.Mob.setNumber(entity, "main", "leaves_length_save", 1, false);

                        double size = Mth.nextDouble(RandomSource.create(), NBTManager.Mob.getNumber(entity, "main", "leaves_size_min"), NBTManager.Mob.getNumber(entity, "main", "leaves_size_max"));
                        size = size * summonReduction(entity, "leaves", "size");
                        NBTManager.Mob.setNumber(entity, "main", "leaves_size", size, false);

                    }

                }

                // Chance of Discontinuation
                {

                    if (is_taproot_trunk == false) {

                        double chance = NBTManager.Mob.getNumber(entity, "main", gen_type + "_continue_chance");
                        chance = chance * summonReduction(entity, gen_type, "continue");
                        chance = 1.0 - chance;

                        if (Math.random() < chance) {

                            NBTManager.Mob.setNumber(entity, "main", type_pre_next[1] + "_count", 0, false);

                        }

                    }

                }

            }

            // Summoning
            {

                Entity entity_at = null;
                Vec3 vec3 = null;

                if (is_taproot_trunk == true) {

                    {

                        entity_at = EntityManager.Population.getEverywhereOneStatic(level_server, "", "", new String[]{"TANSHUGETREES-" + id, "TANSHUGETREES-tree_generator"});

                        if (entity_at == null) {

                            NBTManager.Mob.setText(entity, "main", "error", "Not Detected Center Generator", false);
                            return false;

                        }

                        vec3 = entity_at.position();

                    }

                } else {

                    {

                        entity_at = EntityManager.Population.getEverywhereOneStatic(level_server, "", "", new String[]{"TANSHUGETREES-" + id, "TANSHUGETREES-generator_" + type_pre_next[0]});

                        if (entity_at == null) {

                            NBTManager.Mob.setText(entity, "main", "error", "Not Detected Specific Generator", false);
                            return false;

                        }

                        if (gen_type.equals("leaves") == true) {

                            vec3 = entity_at.position();

                        } else {

                            // Custom Position
                            {

                                double vertical = 0.0;
                                double horizontal = 0.0;
                                double forward = 0.0;
                                double height = 0.0;
                                String center_direction_from = NBTManager.Mob.getText(entity, "main", gen_type + "_center_direction_from");

                                if (center_direction_from.isEmpty() == true) {

                                    // Non-Center Direction
                                    {

                                        if (NBTManager.Mob.getNumber(entity, "main", gen_type + "_min_last_count") > 0 && NBTManager.Mob.getNumber(entity, "main", gen_type + "_count") <= 1) {

                                            // Make last part facing same as previous part
                                            forward = 1;

                                        } else {

                                            vertical = NBTManager.Mob.getNumber(entity, "main", gen_type + "_start_vertical");
                                            horizontal = NBTManager.Mob.getNumber(entity, "main", gen_type + "_start_horizontal");
                                            height = Mth.nextDouble(RandomSource.create(), NBTManager.Mob.getNumber(entity, "main", gen_type + "_start_height_min"), NBTManager.Mob.getNumber(entity, "main", gen_type + "_start_height_max"));
                                            forward = Mth.nextDouble(RandomSource.create(), NBTManager.Mob.getNumber(entity, "main", gen_type + "_start_forward_min"), NBTManager.Mob.getNumber(entity, "main", gen_type + "_start_forward_max"));

                                        }

                                    }

                                } else {

                                    // Center Direction
                                    {

                                        int length = (int) NBTManager.Mob.getNumber(entity, "main", type_pre_next[0] + "_length");
                                        int length_save = (int) NBTManager.Mob.getNumber(entity, "main", type_pre_next[0] + "_length_save");
                                        double center = NBTManager.Mob.getNumber(entity, "main", gen_type + "_center_direction_center") * 0.01;
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

                                        vertical = 1.0 + ((NBTManager.Mob.getNumber(entity, "main", gen_type + "_center_direction_vertical_" + above_or_below) - 1.0) * percent);
                                        horizontal = 1.0 + ((NBTManager.Mob.getNumber(entity, "main", gen_type + "_center_direction_horizontal_" + above_or_below) - 1.0) * percent);
                                        height = NBTManager.Mob.getNumber(entity, "main", gen_type + "_center_direction_height_" + above_or_below) * percent;
                                        forward = NBTManager.Mob.getNumber(entity, "main", gen_type + "_center_direction_forward_" + above_or_below) * percent;

                                    }

                                }

                                vertical = Mth.nextDouble(RandomSource.create(), -(vertical), vertical);
                                horizontal = Mth.nextDouble(RandomSource.create(), -(horizontal), horizontal);
                                vec3 = EntityManager.getPosLook(entity_at, horizontal, vertical, forward);
                                vec3 = vec3.add(0, height, 0);

                            }

                        }

                    }

                }

                Entity entity_current = EntityManager.Population.getEverywhereOneStatic(level_server, "", "", new String[]{"TANSHUGETREES-" + id, "TANSHUGETREES-generator_" + gen_type});

                if (entity_current == null) {

                    NBTManager.Mob.setText(entity, "main", "error", "Not Detected Specific Generator", false);
                    return false;

                }

                entity_current.setPos(vec3);

                if (is_taproot_trunk == true) {

                    entity_current.setXRot(Mth.nextInt(RandomSource.create(), (int) NBTManager.Mob.getNumber(entity, "main", gen_type + "_start_gravity_max"), (int) NBTManager.Mob.getNumber(entity, "main", gen_type + "_start_gravity_min")));
                    entity_current.setYRot(Mth.nextInt(RandomSource.create(), (int) NBTManager.Mob.getNumber(entity, "main", gen_type + "_start_direction_min"), (int) NBTManager.Mob.getNumber(entity, "main", gen_type + "_start_direction_max")));

                } else {

                    entity_current.lookAt(EntityAnchorArgument.Anchor.FEET, entity_at.position());
                    entity_current.setPos(entity_at.position());
                    entity_current.lookAt(EntityAnchorArgument.Anchor.FEET, EntityManager.getPosLook(entity_current, 0, 0, -1));

                }

            }

            // Next Step
            {

                if (is_taproot_trunk == true) {

                    NBTManager.Mob.setText(entity, "main", "gen_step", "build", false);

                } else {

                    NBTManager.Mob.setText(entity, "main", "gen_step", "calculation", false);

                }

            }

            NBTManager.Mob.addNumber(entity, "main", gen_type + "_count", -1, false);
            return true;

        }

        private static double summonReduction (Entity entity, String gen_type, String gen_step) {

            gen_step = gen_step + "_reduce";
            String from = NBTManager.Mob.getText(entity, "main", gen_type + "_" + gen_step + "_from");

            if (from.isEmpty() == true) {

                return 1.0;

            }

            double length = NBTManager.Mob.getNumber(entity, "main", from + "_length");
            double length_save = NBTManager.Mob.getNumber(entity, "main", from + "_length_save");
            double center = NBTManager.Mob.getNumber(entity, "main", gen_type + "_" + gen_step + "_center") * 0.01;
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

            double start = NBTManager.Mob.getNumber(entity, "main", gen_type + "_" + gen_step + "_" + above_below + "_start");
            double end = NBTManager.Mob.getNumber(entity, "main", gen_type + "_" + gen_step + "_" + above_below + "_end");
            double reduce = (start - end) * percent;
            reduce = (end + reduce) * 0.01;

            if (Double.isNaN(reduce) == true) {

                reduce = 0;

            }

            return reduce;

        }

        private static boolean calculation (ServerLevel level_server, Entity entity, String id, String gen_type, String[] type_pre_next) {

            Entity entity_current = EntityManager.Population.getEverywhereOneStatic(level_server, "", "", new String[]{"TANSHUGETREES-" + id, "TANSHUGETREES-generator_" + gen_type});

            if (entity_current == null) {

                NBTManager.Mob.setText(entity, "main", "error", "Not Detected Specific Generator", false);
                return false;

            }

            NBTManager.Mob.setNumber(entity, "main", "previous_posX", entity_current.position().x, false);
            NBTManager.Mob.setNumber(entity, "main", "previous_posY", entity_current.position().y, false);
            NBTManager.Mob.setNumber(entity, "main", "previous_posZ", entity_current.position().z, false);
            boolean go_next = false;

            // Test
            {

                if (NBTManager.Mob.getNumber(entity, "main", gen_type + "_length") > 0) {

                    if (NBTManager.Mob.getNumber(entity, "main", type_pre_next[1] + "_count") > NBTManager.Mob.getNumber(entity, "main", type_pre_next[1] + "_min_last_count")) {

                        // Length Range
                        if (NBTManager.Mob.getNumber(entity, "main", gen_type + "_length") / NBTManager.Mob.getNumber(entity, "main", gen_type + "_length_save") <= NBTManager.Mob.getNumber(entity, "main", type_pre_next[1] + "_chance_percent") * 0.01) {

                            if (NBTManager.Mob.getNumber(entity, "main", type_pre_next[1] + "_chance_distance_left") > 0) {

                                NBTManager.Mob.addNumber(entity, "main", type_pre_next[1] + "_chance_distance_left", -1, false);

                            } else {

                                NBTManager.Mob.setNumber(entity, "main", type_pre_next[1] + "_chance_distance_left", NBTManager.Mob.getNumber(entity, "main", type_pre_next[1] + "_chance_auto_distance"), false);

                                // Chance
                                if (Math.random() < NBTManager.Mob.getNumber(entity, "main", type_pre_next[1] + "_chance")) {

                                    go_next = true;

                                }

                            }

                        }

                    }

                } else {

                    go_next = true;

                }

            }

            if (go_next == true) {

                if (NBTManager.Mob.getNumber(entity, "main", type_pre_next[1] + "_count") > 0) {

                    // Up
                    {

                        NBTManager.Mob.setText(entity, "main", "gen_type", type_pre_next[1], false);
                        NBTManager.Mob.setText(entity, "main", "gen_step", "summon", false);

                    }

                } else {

                    // Down
                    {

                        if (gen_type.equals("taproot") == true) {

                            if (NBTManager.Mob.getNumber(entity, "main", gen_type + "_count") > 0) {

                                NBTManager.Mob.setText(entity, "main", "gen_step", "summon", false);

                            } else {

                                if (NBTManager.Mob.getNumber(entity, "main", type_pre_next[0] + "_count") > 0) {

                                    NBTManager.Mob.setText(entity, "main", "gen_step", "summon", false);
                                    NBTManager.Mob.setText(entity, "main", "gen_type", type_pre_next[0], false);

                                } else {

                                    NBTManager.Mob.setText(entity, "main", "gen_step", "end", false);

                                }

                            }

                        } else if (gen_type.equals("trunk") == true) {

                            if (NBTManager.Mob.getNumber(entity, "main", gen_type + "_count") > 0) {

                                NBTManager.Mob.setText(entity, "main", "gen_step", "summon", false);

                            } else {

                                NBTManager.Mob.setText(entity, "main", "gen_step", "end", false);

                            }

                        } else {

                            NBTManager.Mob.setText(entity, "main", "gen_type", type_pre_next[0], false);

                        }

                    }

                }

            } else {

                // Continue
                {

                    if (gen_type.equals("leaves") == false) {

                        // Curvature
                        {

                            float vertical = (float) NBTManager.Mob.getNumber(entity, "main", gen_type + "_curvature_vertical");
                            float horizontal = (float) NBTManager.Mob.getNumber(entity, "main", gen_type + "_curvature_horizontal");
                            entity_current.setXRot(entity_current.getXRot() + Mth.nextFloat(RandomSource.create(), -(vertical), vertical));
                            entity_current.setYRot(entity_current.getYRot() + Mth.nextFloat(RandomSource.create(), -(horizontal), horizontal));

                        }

                        boolean gravity_run = false;

                        // Gravity
                        {

                            float weightiness = (float) NBTManager.Mob.getNumber(entity, "main", gen_type + "_gravity_weightiness");

                            if (weightiness != 0) {

                                float set = 0;

                                if (entity_current.getXRot() >= NBTManager.Mob.getNumber(entity, "main", gen_type + "_gravity_min")) {

                                    set = entity_current.getXRot() - weightiness;

                                    if (set < -90) {

                                        set = -90;

                                    }

                                    entity_current.setXRot(set);
                                    gravity_run = true;

                                }

                                if (entity_current.getXRot() <= NBTManager.Mob.getNumber(entity, "main", gen_type + "_gravity_max")) {

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

                                double centripetal = NBTManager.Mob.getNumber(entity, "main", gen_type + "_centripetal") * 0.01;

                                if (centripetal != 0) {

                                    Entity entity_trunk = EntityManager.Population.getEverywhereOneStatic(level_server, "", "", new String[]{"TANSHUGETREES-" + id, "TANSHUGETREES-generator_trunk"});

                                    if (entity_trunk == null) {

                                        NBTManager.Mob.setText(entity, "main", "error", "Not Detected Specific Generator", false);
                                        return false;

                                    }

                                    Vec3 vec3 = entity_current.position();

                                    entity_current.setPos(EntityManager.getPosLook(entity_current, 0, 0, 1));
                                    entity_current.lookAt(EntityAnchorArgument.Anchor.FEET, entity_trunk.position());
                                    entity_current.setPos(EntityManager.getPosLook(entity_current, 0, 0, -(centripetal)));

                                    entity_current.lookAt(EntityAnchorArgument.Anchor.FEET, vec3);
                                    entity_current.setPos(vec3);
                                    entity_current.lookAt(EntityAnchorArgument.Anchor.FEET, EntityManager.getPosLook(entity_current, 0, 0, -1));

                                }

                            }

                        }

                        double percent = NBTManager.Mob.getNumber(entity, "main", gen_type + "_thickness_start") - NBTManager.Mob.getNumber(entity, "main", gen_type + "_thickness_end");
                        percent = percent / NBTManager.Mob.getNumber(entity, "main", gen_type + "_length_save");
                        double thickness = NBTManager.Mob.getNumber(entity, "main", gen_type + "_thickness");
                        NBTManager.Mob.setNumber(entity, "main", gen_type + "_thickness", thickness - percent, false);

                    }

                    entity_current.setPos(EntityManager.getPosLook(entity_current, 0, 0, 1));
                    NBTManager.Mob.addNumber(entity, "main", gen_type + "_length", -1, false);
                    NBTManager.Mob.setText(entity, "main", "gen_step", "build", false);

                    // Process Break (TP Limit)
                    {

                        if (TanshugetreesModVariables.MapVariables.get(level_server).shape_file_generator == false) {

                            if (NBTManager.Mob.getNumber(entity, "main", "tree_generator_tp_limit") != 0) {

                                if (NBTManager.Mob.getNumber(entity, "main", "tree_generator_tp_limit_test") < NBTManager.Mob.getNumber(entity, "main", "tree_generator_tp_limit")) {

                                    NBTManager.Mob.addNumber(entity, "main", "tree_generator_tp_limit_test", 1, false);

                                } else {

                                    NBTManager.Mob.setNumber(entity, "main", "tree_generator_tp_limit_test", 0, false);
                                    return false;

                                }

                            }

                        }

                    }

                }

            }

            return true;

        }

        private static boolean build (ServerLevel level_server, Entity entity, String id, String gen_type, String[] type_pre_next) {

            double thickness = NBTManager.Mob.getNumber(entity, "main", gen_type + "_thickness");
            double size = 0.0;

            // Get Size and Radius
            {

                if (gen_type.equals("leaves") == false) {

                    size = thickness;

                } else {

                    size = NBTManager.Mob.getNumber(entity, "main", "leaves_size");

                }

                if (size == 0) {

                    return true;

                }

                size = size - 1.0;

                if (size < 0) {

                    size = 0;

                }

            }

            double radius = size * 0.5;
            double radius_ceil = Math.ceil(radius);
            String generator_type = NBTManager.Mob.getText(entity, "main", gen_type + "_generator_type");

            // First Settings
            {

                if (NBTManager.Mob.getLogic(entity, "main", "still_building") == false) {

                    NBTManager.Mob.setLogic(entity, "main", "still_building", true, false);

                    // Get Center Pos
                    {

                        Entity entity_current = EntityManager.Population.getEverywhereOneStatic(level_server, "", "", new String[]{"TANSHUGETREES-" + id, "TANSHUGETREES-generator_" + gen_type});

                        if (entity_current == null) {

                            NBTManager.Mob.setText(entity, "main", "error", "Not Detected Specific Generator", false);
                            return false;

                        }

                        NBTManager.Mob.setNumber(entity, "main", "build_centerX", entity_current.position().x, false);
                        NBTManager.Mob.setNumber(entity, "main", "build_centerY", entity_current.position().y, false);
                        NBTManager.Mob.setNumber(entity, "main", "build_centerZ", entity_current.position().z, false);

                    }

                    // Sphere Zone
                    {

                        if (generator_type.equals("sphere_zone") == true) {

                            Entity entity_pre = EntityManager.Population.getEverywhereOneStatic(level_server, "", "", new String[]{"TANSHUGETREES-" + id, "TANSHUGETREES-generator_" + type_pre_next[0]});

                            if (entity_pre == null) {

                                NBTManager.Mob.setText(entity, "main", "error", "Not Detected Specific Generator", false);
                                return false;

                            }

                            double yaw = Math.toRadians(((entity_pre.getYRot() + 180) + 90) % 360);
                            double pitch = entity_pre.getXRot();

                            // Min Pitch
                            {

                                int pitch_min = (int) NBTManager.Mob.getNumber(entity, "main", gen_type + "_sphere_zone_pitch_min");

                                if (pitch > pitch_min) {

                                    pitch = pitch_min;

                                }

                            }

                            pitch = Math.toRadians(pitch);
                            double distance = radius;
                            int sphere_zone_size = (int) NBTManager.Mob.getNumber(entity, "main", gen_type + "_sphere_zone_size");

                            NBTManager.Mob.setNumber(entity, "main", "sphere_zone_posX", distance * Math.cos(pitch) * Math.cos(yaw), false);
                            NBTManager.Mob.setNumber(entity, "main", "sphere_zone_posY", distance * Math.sin(pitch), false);
                            NBTManager.Mob.setNumber(entity, "main", "sphere_zone_posZ", distance * Math.cos(pitch) * Math.sin(yaw), false);

                            // Change Build Center
                            {

                                distance = distance - sphere_zone_size;
                                NBTManager.Mob.addNumber(entity, "main", "build_centerX", distance * Math.cos(pitch) * Math.cos(yaw), false);
                                NBTManager.Mob.addNumber(entity, "main", "build_centerY", distance * Math.sin(pitch), false);
                                NBTManager.Mob.addNumber(entity, "main", "build_centerZ", distance * Math.cos(pitch) * Math.sin(yaw), false);

                            }

                            // Get Area
                            {

                                double sphere_zone_area = size - sphere_zone_size;

                                if (sphere_zone_area < 0) {

                                    sphere_zone_area = 0;

                                }

                                NBTManager.Mob.setNumber(entity, "main", "sphere_zone_area", sphere_zone_area * sphere_zone_area, false);

                            }

                        }

                    }

                    NBTManager.Mob.setNumber(entity, "main", "build_saveX", -(radius), false);
                    NBTManager.Mob.setNumber(entity, "main", "build_saveY", -(radius), false);
                    NBTManager.Mob.setNumber(entity, "main", "build_saveZ", -(radius), false);

                }

            }

            double sphere_zone_area = 0.0;
            double[] sphere_zone_pos = null;

            if (generator_type.equals("sphere_zone") == true) {

                sphere_zone_area = NBTManager.Mob.getNumber(entity, "main", "sphere_zone_area");
                sphere_zone_pos = new double[]{NBTManager.Mob.getNumber(entity, "main", "sphere_zone_posX"), NBTManager.Mob.getNumber(entity, "main", "sphere_zone_posY"), NBTManager.Mob.getNumber(entity, "main", "sphere_zone_posZ")};

            }

            double build_centerX = NBTManager.Mob.getNumber(entity, "main", "build_centerX");
            double build_centerY = NBTManager.Mob.getNumber(entity, "main", "build_centerY");
            double build_centerZ = NBTManager.Mob.getNumber(entity, "main", "build_centerZ");
            boolean replace = NBTManager.Mob.getLogic(entity, "main", gen_type + "_replace");
            double sphere_area = (radius + 0.35) * (radius + 0.35);

            double scan_change = radius / radius_ceil;

            if (radius == 0) {

                scan_change = 1;

            }

            double scan_start = radius + scan_change;
            double scan_end = radius + scan_change;

            double build_area = 0.0;
            double build_saveX = 0;
            double build_saveY = 0;
            double build_saveZ = 0;
            BlockPos pos = null;

            while (true) {

                // Process Break (Place Block Limit)
                {

                    if (TanshugetreesModVariables.MapVariables.get(level_server).shape_file_generator == false) {

                        if (NBTManager.Mob.getNumber(entity, "main", "tree_generator_speed_repeat") != 0) {

                            if (NBTManager.Mob.getNumber(entity, "main", "tree_generator_speed_repeat_test") < NBTManager.Mob.getNumber(entity, "main", "tree_generator_speed_repeat")) {

                                NBTManager.Mob.addNumber(entity, "main", "tree_generator_speed_repeat_test", 1, false);

                            } else {

                                NBTManager.Mob.setNumber(entity, "main", "tree_generator_speed_repeat_test", 0, false);
                                return false;

                            }

                        }

                    }

                }

                // Building
                {

                    build_saveX = NBTManager.Mob.getNumber(entity, "main", "build_saveX");
                    build_saveY = NBTManager.Mob.getNumber(entity, "main", "build_saveY");
                    build_saveZ = NBTManager.Mob.getNumber(entity, "main", "build_saveZ");

                    if (build_saveY > scan_end) {

                        break;

                    } else {

                        if (build_saveX > scan_end) {

                            NBTManager.Mob.setNumber(entity, "main", "build_saveX", -(scan_start), false);
                            NBTManager.Mob.addNumber(entity, "main", "build_saveY", scan_change, false);

                        } else {

                            if (build_saveZ > scan_end) {

                                NBTManager.Mob.setNumber(entity, "main", "build_saveZ", -(scan_start), false);
                                NBTManager.Mob.addNumber(entity, "main", "build_saveX", scan_change, false);

                            } else {

                                NBTManager.Mob.addNumber(entity, "main", "build_saveZ", scan_change, false);

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

                                pos = new BlockPos((int) (build_centerX + build_saveX), (int) (build_centerY + build_saveY), (int) (build_centerZ + build_saveZ));

                                // Place Block
                                {

                                    if (gen_type.equals("leaves") == true) {

                                        // Leaves
                                        {

                                            if (Math.random() < NBTManager.Mob.getNumber(entity, "main", "leaves_density") * 0.01) {

                                                String previous_block = "";
                                                String block_type = "";
                                                BlockPos pos_leaves = null;
                                                int deep = 0;

                                                if (Math.random() < NBTManager.Mob.getNumber(entity, "main", "leaves_straighten_chance")) {

                                                    deep = Mth.nextInt(RandomSource.create(), (int) NBTManager.Mob.getNumber(entity, "main", "leaves_straighten_min"), (int) NBTManager.Mob.getNumber(entity, "main", "leaves_straighten_max"));

                                                }

                                                for (int deep_test = 0; deep_test <= deep; deep_test++) {

                                                    pos_leaves = pos.below(deep_test);
                                                    previous_block = buildGetPreviousBlock(level_server, pos_leaves);
                                                    block_type = buildGetBlockType(entity, gen_type, previous_block, radius, build_area);

                                                    if (buildPlaceBlock(level_server, entity, pos_leaves, gen_type, block_type, previous_block, replace) == false) {

                                                        break;

                                                    }

                                                }

                                            }

                                        }

                                    } else {

                                        // General
                                        {

                                            if (size < 1 && build_saveX == 0 && build_saveY == 0 && build_saveZ == 0) {

                                                buildBlockConnector(level_server, entity, build_centerX, build_centerY, build_centerZ, pos, gen_type, radius, build_area, replace);

                                            }

                                            String previous_block = buildGetPreviousBlock(level_server, pos);
                                            String block_type = buildGetBlockType(entity, gen_type, previous_block, radius, build_area);
                                            buildPlaceBlock(level_server, entity, pos, gen_type, block_type, previous_block, replace);

                                        }

                                    }

                                }

                            }

                        }

                    }

                }

            }

            // If it builds to the end without any break
            return true;

        }

        private static void buildBlockConnector (ServerLevel level_server, Entity entity, double center_posX, double center_posY, double center_posZ, BlockPos pos, String gen_type, double radius, double build_area, boolean replace) {

            double block_connector_posX = NBTManager.Mob.getNumber(entity, "main", "previous_posX");
            double block_connector_posY = NBTManager.Mob.getNumber(entity, "main", "previous_posY");
            double block_connector_posZ = NBTManager.Mob.getNumber(entity, "main", "previous_posZ");
            int testX = (int) (Math.floor(center_posX) - Math.floor(block_connector_posX));
            int testY = (int) (Math.floor(center_posY) - Math.floor(block_connector_posY));
            int testZ = (int) (Math.floor(center_posZ) - Math.floor(block_connector_posZ));

            if (Math.abs(testX) == 1 || Math.abs(testY) == 1 || Math.abs(testZ) == 1) {

                // For X and Z
                {

                    if (Math.abs(testX) == 1 && Math.abs(testZ) == 1) {

                        if (center_posX - block_connector_posX > center_posZ - block_connector_posZ) {

                            pos = pos.offset(-testX, 0, 0);

                        } else {

                            pos = pos.offset(0, 0, -testZ);

                        }

                        String previous_block = buildGetPreviousBlock(level_server, pos);
                        String block_type = buildGetBlockType(entity, gen_type, previous_block, radius, build_area);

                        if (buildPlaceBlock(level_server, entity, pos, gen_type, block_type, previous_block, replace) == false) {

                            return;

                        }

                    }

                }

                // For Y
                {

                    if ((Math.abs(testX) == 1 || Math.abs(testZ) == 1) && Math.abs(testY) == 1) {

                        pos = pos.offset(0, -testY, 0);
                        String previous_block = buildGetPreviousBlock(level_server, pos);
                        String block_type = buildGetBlockType(entity, gen_type, previous_block, radius, build_area);
                        buildPlaceBlock(level_server, entity, pos, gen_type, block_type, previous_block, replace);

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

                        double outer_level = NBTManager.Mob.getNumber(entity, "main", gen_type + "_outer_level");
                        double inner_level = NBTManager.Mob.getNumber(entity, "main", gen_type + "_inner_level");
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
                        boolean is_same_type = type_short.equals(previous_block_short);
                        boolean is_core = previous_block.endsWith("c");
                        boolean is_blacklist = isBlacklist(type_short, previous_block_short);

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

                    if (Math.random() < NBTManager.Mob.getNumber(entity, "main", "leaves2_chance")) {

                        block = "2";

                    } else {

                        block = "1";

                    }

                }

            }

            return block;

        }

        private static boolean isBlacklist (String type_short, String previous_block_short) {

            if (type_short.equals("se") == true) {

                return "ta".contains(previous_block_short) == true;

            } else if (type_short.equals("te") == true) {

                return "ta/se".contains(previous_block_short) == true;

            } else if (type_short.equals("fi") == true) {

                return "ta/se/te".contains(previous_block_short) == true;

            } else if (type_short.equals("tr") == true) {

                return "ta/se/te/fi".contains(previous_block_short) == true;

            } else if (type_short.equals("bo") == true) {

                return "ta/se/te/fi/tr".contains(previous_block_short) == true;

            } else if (type_short.equals("br") == true) {

                return "ta/se/te/fi/tr/bo".contains(previous_block_short) == true;

            } else if (type_short.equals("li") == true) {

                return "ta/se/te/fi/tr/bo/br".contains(previous_block_short) == true;

            } else if (type_short.equals("tw") == true) {

                return "ta/se/te/fi/tr/bo/br/li".contains(previous_block_short) == true;

            } else if (type_short.equals("sp") == true) {

                return "ta/se/te/fi/tr/bo/br/li/tw".contains(previous_block_short) == true;

            }

            return false;

        }

        private static String buildGetPreviousBlock (ServerLevel level_server, BlockPos pos) {

            String previous_block = "";

            if (TanshugetreesModVariables.MapVariables.get(level_server).shape_file_generator == true) {

                String key = "B" + pos.getX() + "/" + pos.getY() + "/" + pos.getZ();
                previous_block = ShapeFileGenerator.export_data.getOrDefault(key, "");

            } else {

                {

                    BlockState block = level_server.getBlockState(pos);

                    if (block.canBeReplaced() == false) {

                        previous_block = BlockManager.toText(block)[0];

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

            }

            return previous_block;

        }

        private static boolean buildPlaceBlock (ServerLevel level_server, Entity entity, BlockPos pos, String gen_type, String block_type, String previous_block, boolean replace) {

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

                if (NBTManager.Mob.getText(entity, "main", block).isEmpty() == false) {

                    GameUtils.spawnParticle(level_server, pos.getCenter(), 0, 0, 0, 0, 1, "minecraft:flash");

                    // Place
                    {

                        String[] function = buildGetWayFunction(entity, gen_type);

                        if (TanshugetreesModVariables.MapVariables.get(level_server).shape_file_generator == false) {

                            // On World
                            {

                                BlockManager.set(level_server, pos, BlockManager.fromText(level_server, "tanshugetrees:block_placer_" + block_placer), false);
                                NBTManager.Tile.setText(level_server, pos, "main", "block", NBTManager.Mob.getText(entity, "main", block));
                                NBTManager.Tile.setText(level_server, pos, "main", "function", function[1]);
                                NBTManager.Tile.setText(level_server, pos, "main", "function_style", function[2]);

                            }

                        } else {

                            // To Cache
                            {

                                String key = pos.getX() + "/" + pos.getY() + "/" + pos.getZ();

                                if (previous_block.isEmpty() == false && replace == true) {

                                    ShapeFileGenerator.export_data.remove("B" + key);

                                }

                                ShapeFileGenerator.export_data.put("B" + key, type_short);

                                if (function[0].isEmpty() == false) {

                                    if (previous_block.isEmpty() == false && replace == true) {

                                        ShapeFileGenerator.export_data.remove("F" + key);

                                    }

                                    ShapeFileGenerator.export_data.put("F" + key, function[0]);

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

                if (Math.random() < NBTManager.Mob.getNumber(entity, "main", function + "_chance")) {

                    path = NBTManager.Mob.getText(entity, "main", function);
                    at_type = NBTManager.Mob.getText(entity, "main", function + "_type");
                    style = NBTManager.Mob.getText(entity, "main", function + "_style");

                    if (path.isEmpty() == false && at_type.equals(gen_type) == true) {

                        if (NBTManager.Mob.getNumber(entity, "main", function + "_max") >= 0) {

                            double range_min = NBTManager.Mob.getNumber(entity, "main", function + "_range_min") * 0.01;
                            double range_max = NBTManager.Mob.getNumber(entity, "main", function + "_range_max") * 0.01;
                            double length_percent = 1.0;

                            if (NBTManager.Mob.getNumber(entity, "main", at_type + "_length_save") > 0) {

                                length_percent = 1.0 - (NBTManager.Mob.getNumber(entity, "main", at_type + "_length") / NBTManager.Mob.getNumber(entity, "main", at_type + "_length_save"));

                            }

                            if (range_min <= length_percent && length_percent <= range_max) {

                                // Count Limitation
                                {

                                    if (NBTManager.Mob.getNumber(entity, "main", function + "_max") > 0) {

                                        NBTManager.Mob.addNumber(entity, "main", function + "_max", -1, false);

                                        if (NBTManager.Mob.getNumber(entity, "main", function + "_max") == 0) {

                                            NBTManager.Mob.setNumber(entity, "main", function + "_max", -1, false);

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

        private static void end (ServerLevel level_server, Entity entity, String id) {

            if (TanshugetreesModVariables.MapVariables.get(level_server).shape_file_generator == true) {

                ShapeFileGenerator.whenTreeEnd(level_server, entity);

            } else {

                TXTFunction.run(level_server, level_server, entity.blockPosition(), NBTManager.Mob.getText(entity, "main", "function_end"), true);

            }

            EntityManager.summon(level_server, entity.position().add(20.0, 10.0, 20.0), true, "minecraft:firework_rocket", "", new String[]{}, "{LifeTime:40,FireworksItem:{id:firework_rocket,Count:1,tag:{Fireworks:{Flight:2,Explosions:[{Type:4,Flicker:1,Trail:1,Colors:[I;3887386,4312372],FadeColors:[I;3887386,4312372]}]}}}}");
            EntityManager.summon(level_server, entity.position().add(20.0, 10.0, -20.0), true, "minecraft:firework_rocket", "", new String[]{}, "{LifeTime:40,FireworksItem:{id:firework_rocket,Count:1,tag:{Fireworks:{Flight:2,Explosions:[{Type:4,Flicker:1,Trail:1,Colors:[I;3887386,4312372],FadeColors:[I;3887386,4312372]}]}}}}");
            EntityManager.summon(level_server, entity.position().add(-20.0, 10.0, 20.0), true, "minecraft:firework_rocket", "", new String[]{}, "{LifeTime:40,FireworksItem:{id:firework_rocket,Count:1,tag:{Fireworks:{Flight:2,Explosions:[{Type:4,Flicker:1,Trail:1,Colors:[I;3887386,4312372],FadeColors:[I;3887386,4312372]}]}}}}");
            EntityManager.summon(level_server, entity.position().add(-20.0, 10.0, -20.0), true, "minecraft:firework_rocket", "", new String[]{}, "{LifeTime:40,FireworksItem:{id:firework_rocket,Count:1,tag:{Fireworks:{Flight:2,Explosions:[{Type:4,Flicker:1,Trail:1,Colors:[I;3887386,4312372],FadeColors:[I;3887386,4312372]}]}}}}");

            for (Entity scan : EntityManager.Population.getEverywhereStatic(level_server, "", "", new String[]{"TANSHUGETREES-" + id})) {

                scan.discard();

            }

        }

    }

}
