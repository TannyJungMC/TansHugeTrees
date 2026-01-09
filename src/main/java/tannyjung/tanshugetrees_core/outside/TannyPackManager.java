package tannyjung.tanshugetrees_core.outside;

import net.minecraft.server.level.ServerLevel;
import tannyjung.tanshugetrees_core.Core;
import tannyjung.tanshugetrees_core.game.GameUtils;

import java.io.File;

public class TannyPackManager {

    public static boolean checkUpdate (ServerLevel level_server, String pack_link, String wiki, boolean auto_update, String command_update) {

        String url = "https://raw.githubusercontent.com/" + pack_link + "/" + Core.tanny_pack_type.toLowerCase() + "/info.txt";

        if (OutsideUtils.isURLAvailable(url) == false) {

            if (level_server != null) {

                GameUtils.command.run(false, level_server, 0, 0, 0, "tellraw @a [{\"text\":\"" + Core.mod_id_short + " : Can't check for update right now, because the mod can't connect to GitHub. This maybe the website is currently down or there's a new mod update. The most serious case is your country blocked GitHub website, that will need to manual install with VPN enabled. Here is \",\"color\":\"red\"},{\"text\":\"Installation Wiki\",\"color\":\"white\",\"underlined\":\"true\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"" + wiki + "\"},\"hoverEvent\":{\"action\":\"show_item\",\"contents\":{\"id\":\"minecraft:diamond\",\"count\":1,\"tag\":\"{display:{Name:'\\\"" + wiki + "\\\"'}}\"}}},{\"text\":\" if you want to manual install.\",\"color\":\"red\"}]");

            } else {

                Core.logger.error("Can't check for update right now, because the mod can't connect to GitHub. This maybe the website is currently down or there's a new mod update. The most serious case is your country blocked GitHub website, that will need to manual install with VPN enabled. Here is installation wiki if you want to manual install ({}).", wiki);

            }

        } else {

            int url_pack_version = 0;
            int url_data_structure_version = 0;
            int pack_version = 0;
            File file_pack = getCurrentFile();

            // Get Version Offline
            {

                File file = new File(Core.path_config + "/#dev/temporary/info/" + file_pack.getName() + ".txt");

                if (file.exists() == true && file.isDirectory() == false) {

                    for (String read_all : FileManager.readTXT(file.getPath())) {

                        {

                            if (read_all.startsWith("pack_version = ")) {

                                pack_version = Integer.parseInt(read_all.substring("pack_version = ".length()));
                                break;

                            }

                        }

                    }

                }

            }

            // Get Version Online
            {

                for (String read_all : OutsideUtils.readOnlineTXT(url)) {

                    {

                        if (read_all.startsWith("data_structure_version = ")) {

                            url_data_structure_version = Integer.parseInt(read_all.substring("data_structure_version = ".length()));

                        } else if (read_all.startsWith("pack_version = ")) {

                            url_pack_version = Integer.parseInt(read_all.substring("pack_version = ".length()));
                            break;

                        }

                    }

                }

            }

            if (url_data_structure_version == 0) {

                if (level_server != null) {

                    GameUtils.command.run(false, level_server, 0, 0, 0, "tellraw @a [{\"text\":\"" + Core.mod_id_short + " : Something went wrong with version testing. This maybe the website is currently down or there's a new mod update. The most serious case is your country blocked GitHub website, that will need to manual install with VPN enabled. Here is \",\"color\":\"red\"},{\"text\":\"Installation Wiki\",\"color\":\"white\",\"underlined\":\"true\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"" + wiki + "\"},\"hoverEvent\":{\"action\":\"show_item\",\"contents\":{\"id\":\"minecraft:diamond\",\"count\":1,\"tag\":\"{display:{Name:'\\\"" + wiki + "\\\"'}}\"}}},{\"text\":\" if you want to manual install.\",\"color\":\"red\"}]");

                } else {

                    Core.logger.error("Something went wrong with version testing. This maybe the website is currently down or there's a new mod update. The most serious case is your country blocked GitHub website, that will need to manual install with VPN enabled. Here is installation wiki if you want to manual install ({}).", wiki);

                }

            } else {

                if (pack_version == url_pack_version) {

                    message(level_server, "info", "TannyJung's Main Pack (" + Core.tanny_pack_type + ") is up to date");

                } else if (Core.data_structure_version > url_data_structure_version) {

                    message(level_server, "error", "Seems like you update the mod very fast! TannyJung's Main Pack (" + Core.tanny_pack_type + ") haven't updated to support this mod version yet, please wait a bit for the update to be available.");

                } else if (Core.data_structure_version < url_data_structure_version) {

                    message(level_server, "error", "Detected new version of TannyJung's Main Pack (" + Core.tanny_pack_type + "), but it requires new mod version. Please update the mod if you want to install it.");

                } else if (Core.data_structure_version == url_data_structure_version) {

                    if (file_pack.exists() == true) {

                        // Detected New Version
                        {

                            if (auto_update == true) {

                                message(level_server, "info", "Detected new version of TannyJung's Main Pack (" + Core.tanny_pack_type + "). Starting auto update...");

                                if (level_server != null) {

                                    GameUtils.command.run(false, level_server, 0, 0, 0, command_update);

                                } else {

                                    return true;

                                }

                            } else {

                                if (level_server != null) {

                                    GameUtils.command.run(false, level_server, 0, 0, 0, "tellraw @a [{\"text\":\"" + Core.mod_id_short + " : Detected new version of TannyJung's Main Pack (" + Core.tanny_pack_type + "). You can manual update by follow the guide in \",\"color\":\"gold\"},{\"text\":\"Wiki\",\"color\":\"white\",\"underlined\":\"true\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"" + wiki + "\"},\"hoverEvent\":{\"action\":\"show_item\",\"contents\":{\"id\":\"minecraft:diamond\",\"count\":1,\"tag\":\"{display:{Name:'\\\"" + wiki + "\\\"'}}\"}}},{\"text\":\" or click \",\"color\":\"gold\"},{\"text\":\"[here]\",\"color\":\"white\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/" + command_update + "\"}},{\"text\":\" to let the mod install it.\",\"color\":\"gold\"}]");

                                } else {

                                    Core.logger.info("Detected new version for TannyJung's Main Pack ({}). You can manual update by follow the guide in wiki ({}) or join a world and let the mod install it.", Core.tanny_pack_type, wiki);

                                }

                            }

                        }

                    } else {

                        // Not Found
                        {

                            if (level_server != null) {

                                GameUtils.command.run(false, level_server, 0, 0, 0, "tellraw @a [{\"text\":\"" + Core.mod_id_short + " : Not detected TannyJung's Main Pack (" + Core.tanny_pack_type + ") in custom packs folder. Starting auto install...\",\"color\":\"gold\"}]");
                                GameUtils.command.run(false, level_server, 0, 0, 0, command_update);

                            } else {

                                Core.logger.info("Not detected TannyJung's Main Pack ({}) in custom packs folder. Starting auto install...", Core.tanny_pack_type);
                                return true;

                            }

                        }

                    }

                }

            }

            Core.logger.info("Data Structure Version -> Mod {} GitHub {} / Pack Version -> Mod {} GitHub {}", Core.data_structure_version, url_data_structure_version, pack_version, url_pack_version);

        }

        return false;

    }

    public static boolean reinstall (ServerLevel level_server, String pack_link, String wiki) {

        String url = "https://github.com/" + pack_link + "/archive/refs/heads/" + Core.tanny_pack_type.toLowerCase() + ".zip";

        if (level_server != null) {

            GameUtils.command.run(false, level_server, 0, 0, 0, "tellraw @a [{\"text\":\"" + Core.mod_id_short + " : Installing ZIP from GitHub. This may take a while. \",\"color\":\"gray\"},{\"text\":\"URL\",\"color\":\"white\",\"underlined\":\"true\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"" + url + "\"},\"hoverEvent\":{\"action\":\"show_item\",\"contents\":{\"id\":\"minecraft:diamond\",\"count\":1,\"tag\":\"{display:{Name:'\\\"" + url + "\\\"'}}\"}}}]");

        } else {

            Core.logger.info("Installing ZIP from GitHub. This may take a while. ({})", url);

        }

        String path = Core.path_config + "/custom_packs/#TannyJung-Main-Pack";
        FileManager.delete(Core.path_config + "/custom_packs/#TannyJung-Main-Pack");
        FileManager.delete(Core.path_config + "/custom_packs/[INCOMPATIBLE] #TannyJung-Main-Pack");
        FileManager.delete(Core.path_config + "/custom_packs/#TannyJung-Main-Pack.zip");
        FileManager.delete(Core.path_config + "/custom_packs/[INCOMPATIBLE] #TannyJung-Main-Pack.zip");

        if (OutsideUtils.download(url, path + ".zip") == true) {

            FileManager.extractZIP(path + ".zip", path, true, "");
            FileManager.delete(path + ".zip");
            FileManager.compressZIP(path + ".zip", new File(path));
            FileManager.delete(path);

            message(level_server, "info", "Install Completed!");
            return true;

        } else {

            if (level_server != null) {

                GameUtils.command.run(false, level_server, 0, 0, 0, "tellraw @a [{\"text\":\"" + Core.mod_id_short + " : Can't install right now, because the mod can't connect to GitHub. This maybe the website is currently down or there's a new mod update. The most serious case is your country blocked GitHub website, that will need to manual install with VPN enabled. Here is \",\"color\":\"red\"},{\"text\":\"Installation Wiki\",\"color\":\"white\",\"underlined\":\"true\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"" + wiki + "\"},\"hoverEvent\":{\"action\":\"show_item\",\"contents\":{\"id\":\"minecraft:diamond\",\"count\":1,\"tag\":\"{display:{Name:'\\\"" + wiki + "\\\"'}}\"}}},{\"text\":\" if you want to manual install.\",\"color\":\"red\"}]");

            } else {

                Core.logger.error("Can't install right now, because the mod can't connect to GitHub. This maybe the website is currently down or there's a new mod update. The most serious case is your country blocked GitHub website, that will need to manual install with VPN enabled. Here is installation wiki if you want to manual install ({}).", wiki);

            }

        }

        return false;

    }

    public static File getCurrentFile () {

        File file = new File(Core.path_config + "/custom_packs/#TannyJung-Main-Pack.zip");

        if (file.exists() == false) {

            file = new File(Core.path_config + "/custom_packs/[INCOMPATIBLE] #TannyJung-Main-Pack.zip");

        }

        if (file.exists() == false) {

            file = new File(Core.path_config + "/custom_packs/#TannyJung-Main-Pack");

        }

        if (file.exists() == false) {

            file = new File(Core.path_config + "/custom_packs/[INCOMPATIBLE] #TannyJung-Main-Pack");

        }

        return file;

    }

    private static void message (ServerLevel level_server, String type, String message) {

        if (level_server != null) {

            if (message.isEmpty() == false) {

                String color = "";

                if (type.equals("info") == true) {

                    color = "gray";

                } else if (type.equals("error") == true) {

                    color = "red";

                }

                GameUtils.misc.sendChatMessage(level_server, null, "@a", color, Core.mod_id_short + " : " + message);

            } else {

                GameUtils.misc.sendChatMessage(level_server, null, "@a", "white", "");

            }

        } else {

            if (type.equals("info") == true) {

                Core.logger.info(message);

            } else if (type.equals("error") == true) {

                Core.logger.error(message);

            }

        }

    }

}