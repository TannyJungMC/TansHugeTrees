package tannyjung.tanshugetrees_handcode.systems.tree_generator;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import tannyjung.tanshugetrees_core.Core;
import tannyjung.tanshugetrees_core.game.EntityManager;
import tannyjung.tanshugetrees_core.game.GameUtils;
import tannyjung.tanshugetrees_core.game.NBTManager;
import tannyjung.tanshugetrees_core.game.screen.GUIManager;
import tannyjung.tanshugetrees_core.outside.FileManager;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TreeSummonerStaff {

    public static void click (ServerLevel level_server, ServerPlayer player_server) {

        if (player_server.isShiftKeyDown() == true) {

            // GUI
            {

                List<String> list = new ArrayList<>();
                File[] packs = new File(Core.path_config + "/custom_packs").listFiles();

                if (packs != null) {

                    Set<String> uncache_blueprints = new HashSet<>();
                    String id = null;

                    for (File pack : packs) {

                        if (pack.getName().endsWith(".zip") == true) {

                            continue;

                        }

                        for (File file : FileManager.getAllFiles(pack.getPath() + "/tree_blueprints")) {

                            id = Path.of(pack.getPath() + "/tree_blueprints").relativize(file.toPath()).toString();
                            id = id.replace("\\", " > ");
                            id = id.substring(0, id.length() - ".txt".length());
                            id = pack.getName() + " > " + id;

                            uncache_blueprints.add(id);
                            list.add("§b" + id);

                        }

                    }

                    for (File file : FileManager.getAllFiles(Core.path_config + "/dev/temporary/tree_blueprints")) {

                        id = Path.of(Core.path_config + "/dev/temporary/tree_blueprints").relativize(file.toPath()).toString();
                        id = id.replace("\\", " > ");
                        id = id.substring(0, id.length() - ".txt".length());

                        if (uncache_blueprints.contains(id) == true) {

                            continue;

                        }

                        list.add("§a" + id);

                    }

                }

                NBTManager.Item.setListAlternate(player_server.getItemBySlot(EquipmentSlot.MAINHAND), "gui", "board_tree_generator_staff", list);
                GUIManager.open(player_server, "item", "main", "tree_generator_staff");

            }

        } else {

            // Summon
            {

                BlockPos pos = BlockPos.containing(EntityManager.getPosRayForward(player_server, 500, false));
                GameUtils.spawnParticle(level_server, pos.getCenter(), 0, 0, 0, 0, 1, "minecraft:flash");
                GameUtils.playSound(level_server, player_server.position(), 2, 2, "minecraft:entity.illusioner.mirror_move");
                GameUtils.playSound(level_server, pos.getCenter(), 2, 0, "minecraft:entity.illusioner.prepare_blindness");

                ItemStack item = player_server.getItemBySlot(EquipmentSlot.MAINHAND);
                List<String> list = NBTManager.Item.getListAsList(item, "gui", "board_tree_generator_staff");
                int select = (int) NBTManager.Item.getNumber(item, "gui", "board_tree_generator_staff_select");

                String id = list.get(select);
                id = id.replace(" > ", "/");
                id = id.substring("§f".length());
                TreeGenerator.create(level_server, player_server, pos, id);

            }

        }

    }

}
