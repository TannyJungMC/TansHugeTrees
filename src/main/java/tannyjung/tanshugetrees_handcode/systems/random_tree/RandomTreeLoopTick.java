package tannyjung.tanshugetrees_handcode.systems.random_tree;

import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LevelAccessor;
import tannyjung.tanshugetrees.network.TanshugetreesModVariables;
import tannyjung.tanshugetrees_handcode.misc.GameUtils;
import tannyjung.tanshugetrees_handcode.systems.config.ConfigMain;

public class RandomTreeLoopTick {

    public static void run (LevelAccessor level, Entity entity) {

        GameUtils.runCommandEntity(entity, "particle composter ~ ~ ~ 0 0 0 0 1 force");

        if (GameUtils.NBTLogicGet(entity, "start") == false) {

            GameUtils.NBTLogicSet(entity, "start", true);
            start(level, entity);

        } else {

            GameUtils.NBTNumberSet(entity, "generate_speed_test", GameUtils.NBTNumberGet(entity, "generate_speed_test") + 1);

            if (GameUtils.NBTNumberGet(entity, "generate_speed_test") > GameUtils.NBTNumberGet(entity, "generate_speed")) {

                GameUtils.NBTNumberSet(entity, "generate_speed_test", 0);
                runSystem(level, entity);

            }

        }

    }
    
    private static void start (LevelAccessor level, Entity entity) {

        if (TanshugetreesModVariables.MapVariables.get(level).auto_gen == false) {

            GameUtils.runCommandEntity(entity, "tp ~ ~" + GameUtils.NBTNumberGet(entity, "start_height") + " ~");

            if (ConfigMain.global_speed_enable == true && GameUtils.NBTLogicGet(entity, "global_generate_speed") == true) {

                GameUtils.NBTNumberSet(entity, "generate_speed", ConfigMain.global_speed);
                GameUtils.NBTNumberSet(entity, "generate_speed_repeat", ConfigMain.global_speed_repeat);
                GameUtils.NBTNumberSet(entity, "generate_speed_tp", ConfigMain.global_speed_tp);

            }

        }

        GameUtils.runCommandEntity(entity, "kill @e[name=TANSHUGETREES-tree_countdown,distance=..1]");
        GameUtils.NBTTextSet(entity, "ID", entity.getUUID().toString());
        GameUtils.NBTTextSet(entity, "type", "taproot");
        GameUtils.NBTTextSet(entity, "step", "summon");
        GameUtils.NBTNumberSet(entity, "taproot_count", Mth.nextInt(RandomSource.create(), (int) GameUtils.NBTNumberGet(entity, "taproot_count_min"), (int) GameUtils.NBTNumberGet(entity, "taproot_count_max")));
        GameUtils.NBTNumberSet(entity, "trunk_count", Mth.nextInt(RandomSource.create(), (int) GameUtils.NBTNumberGet(entity, "trunk_count_min"), (int) GameUtils.NBTNumberGet(entity, "trunk_count_max")));

        // Debug Mode
        {

            if (GameUtils.NBTLogicGet(entity, "debug_mode") == true) {

                GameUtils.NBTTextSet(entity, "taproot_outer", "red_stained_glass");
                GameUtils.NBTTextSet(entity, "taproot_inner", "red_stained_glass");
                GameUtils.NBTTextSet(entity, "taproot_core", "red_stained_glass");
                GameUtils.NBTTextSet(entity, "secondary_root_outer", "red_stained_glass");
                GameUtils.NBTTextSet(entity, "secondary_root_inner", "red_stained_glass");
                GameUtils.NBTTextSet(entity, "secondary_root_core", "red_stained_glass");
                GameUtils.NBTTextSet(entity, "tertiary_root_outer", "red_stained_glass");
                GameUtils.NBTTextSet(entity, "tertiary_root_inner", "red_stained_glass");
                GameUtils.NBTTextSet(entity, "tertiary_root_core", "red_stained_glass");
                GameUtils.NBTTextSet(entity, "fine_root_outer", "red_stained_glass");
                GameUtils.NBTTextSet(entity, "fine_root_inner", "red_stained_glass");
                GameUtils.NBTTextSet(entity, "fine_root_core", "red_stained_glass");

                GameUtils.NBTTextSet(entity, "trunk_outer", "red_stained_glass");
                GameUtils.NBTTextSet(entity, "trunk_inner", "red_stained_glass");
                GameUtils.NBTTextSet(entity, "trunk_core", "red_stained_glass");
                GameUtils.NBTTextSet(entity, "branch_outer", "red_stained_glass");
                GameUtils.NBTTextSet(entity, "branch_inner", "red_stained_glass");
                GameUtils.NBTTextSet(entity, "branch_core", "red_stained_glass");
                GameUtils.NBTTextSet(entity, "twig_outer", "red_stained_glass");
                GameUtils.NBTTextSet(entity, "twig_inner", "red_stained_glass");
                GameUtils.NBTTextSet(entity, "twig_core", "red_stained_glass");
                GameUtils.NBTTextSet(entity, "leaves_twig_outer", "red_stained_glass");
                GameUtils.NBTTextSet(entity, "leaves_twig_inner", "red_stained_glass");
                GameUtils.NBTTextSet(entity, "leaves_twig_core", "red_stained_glass");

                GameUtils.NBTTextSet(entity, "leaves1", "green_stained_glass");
                GameUtils.NBTTextSet(entity, "leaves2", "lime_stained_glass");

            }

        }

        // Summon Status Display
        {

            GameUtils.runCommandEntity(entity, "execute positioned ~ ~1 ~ run " + GameUtils.summonEntity("text_display", "", "sapling_status", "white", "see_through:1b,alignment:\"left\",brightness:{block:15},line_width:1000,transformation:{left_rotation:[0f,0f,0f,1f],right_rotation:[0f,0f,0f,1f],translation:[0f,0f,0f],scale:[1f,1f,1f]},billboard:vertical,text:'{\"text\":\"In Progress...\",\"color\":\"white\"}'"));

        }

    }

    private static String[] typePreNext (String type) {

        String[] return_text = new String[2];

        if (type.equals("taproot") == true) {

            return_text[0] = "";
            return_text[1] = "secondary_root";

        } else if (type.equals("secondary_root") == true) {

            return_text[0] = "taproot";
            return_text[1] = "tertiary_root";

        } else if (type.equals("tertiary_root") == true) {

            return_text[0] = "secondary_root";
            return_text[1] = "fine_root";

        } else if (type.equals("fine_root") == true) {

            return_text[0] = "tertiary_root";
            return_text[1] = "trunk";

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

        // Status Display
        {

            StringBuilder command = new StringBuilder();
            String type = GameUtils.NBTTextGet(entity, "type");

            command
                    .append("Total Processes : ").append((int) GameUtils.NBTNumberGet(entity, "process"))
                    .append("\\\\n")
                    .append("Generating : ").append(type)
                    .append("\\\\n")
                    .append("\\\\n")
                    .append("Step : ").append(GameUtils.NBTTextGet(entity, "step"))
                    .append("\\\\n")
                    .append("Count : ").append((int) GameUtils.NBTNumberGet(entity, type + "_count"))
                    .append("\\\\n")
                    .append("Length : ").append((int) GameUtils.NBTNumberGet(entity, type + "_length"))
                    .append("\\\\n")
                    .append("Thickness : ").append(GameUtils.NBTNumberGet(entity, type + "_thickness"))
            ;

            GameUtils.runCommandEntity(entity, "execute positioned ~ ~1 ~ run data merge entity @e[name=TANSHUGETREES-sapling_status,distance=..1,limit=1,sort=nearest] {text:'{\"text\":\"" + command + "\",\"color\":\"red\"}'}");

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

            GameUtils.NBTNumberSet(entity, "process", (int) GameUtils.NBTNumberGet(entity, "process") + 1);

            if (GameUtils.NBTTextGet(entity, "step").equals("summon") == true) {

                stepSummon(level, entity);

            } else if (GameUtils.NBTTextGet(entity, "step").equals("tp") == true) {



            } else if (GameUtils.NBTTextGet(entity, "step").equals("build") == true) {



            } else {

                // End

            }

        }

    }

    private static void stepSummon (LevelAccessor level, Entity entity) {



    }

}
