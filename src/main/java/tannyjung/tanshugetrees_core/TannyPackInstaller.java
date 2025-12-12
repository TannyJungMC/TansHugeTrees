package tannyjung.tanshugetrees_core;

import net.minecraft.server.level.ServerLevel;
import org.apache.logging.log4j.Logger;
import tannyjung.tanshugetrees_core.game.GameUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.net.URI;

public class TannyPackInstaller {

    public static boolean checkUpdate (ServerLevel level_server, Logger logger, String path_config, String id, String pack_link, String branch, String wiki, int data_structure_version_config, boolean auto_update, String command_update) {

        File file = null;

        // Get Path
        {

            file = new File(path_config + "/custom_packs/#TannyJung-Main-Pack.zip");

            if (file.exists() == false) {

                file = new File(path_config + "/custom_packs/[INCOMPATIBLE] #TannyJung-Main-Pack.zip");

            }

            if (file.exists() == false) {

                file = new File(path_config + "/custom_packs/#TannyJung-Main-Pack");

            }

            if (file.exists() == false) {

                file = new File(path_config + "/custom_packs/[INCOMPATIBLE] #TannyJung-Main-Pack");

            }

        }

        if (file.exists() == true) {

            file = new File(path_config + "/#dev/temporary/info/" + file.getName() + ".txt");

            // Test URL
            {

                String url = "https://raw.githubusercontent.com/" + pack_link + "/" + branch.toLowerCase() + "/info.txt";

                if (OutsideUtils.isURLAvailable(url) == false) {

                    message(level_server, logger, id, "error", "Can't check for update right now, as the mod can't connect to GitHub.");

                } else {

                    try {

                        int url_pack_version = 0;
                        int url_data_structure_version = 0;
                        int pack_version = 0;

                        // Get Your Version
                        {

                            try { BufferedReader buffered_reader = new BufferedReader(new FileReader(file), 65536); String read_all = ""; while ((read_all = buffered_reader.readLine()) != null) {

                                {

                                    if (read_all.startsWith("pack_version = ")) {

                                        pack_version = Integer.parseInt(read_all.substring("pack_version = ".length()));
                                        break;

                                    }

                                }

                            } buffered_reader.close(); } catch (Exception exception) { OutsideUtils.exception(new Exception(), exception); }

                        }

                        // Read File From GitHub
                        {

                            try { BufferedReader buffered_reader = new BufferedReader(new InputStreamReader(new URI(url).toURL().openStream()), 65536); String read_all = ""; while ((read_all = buffered_reader.readLine()) != null) {

                                {

                                    if (read_all.startsWith("data_structure_version = ")) {

                                        url_data_structure_version = Integer.parseInt(read_all.substring("data_structure_version = ".length()));

                                    } else if (read_all.startsWith("pack_version = ")) {

                                        url_pack_version = Integer.parseInt(read_all.substring("pack_version = ".length()));
                                        break;

                                    }

                                }

                            } buffered_reader.close(); } catch (Exception exception) { OutsideUtils.exception(new Exception(), exception); }

                        }

                        if (pack_version == url_pack_version) {

                            message(level_server, logger, id, "info", "TannyJung's Main Pack (" + branch + ") is up to date");

                        } else {

                            if (url_data_structure_version == 0) {

                                message(level_server, logger, id, "error", "Something went wrong with version testing. Maybe website is down or there's a new mod update.");

                            } else if (data_structure_version_config > url_data_structure_version) {

                                message(level_server, logger, id, "error", "Seems like you update the mod very fast! TannyJung's Main Pack (" + branch + ") haven't updated to support this mod version yet, please wait a bit for the update to be available.");

                            } else if (data_structure_version_config < url_data_structure_version) {

                                message(level_server, logger, id, "error", "Detected new version of TannyJung's Main Pack (" + branch + "), but it requires new mod version. Please update the mod if you want to install it.");

                            } else if (data_structure_version_config == url_data_structure_version) {

                                // Detected New Version
                                {

                                    if (auto_update == true) {

                                        message(level_server, logger, id, "info", "Detected new version of TannyJung's Main Pack (" + branch + "). Starting auto update...");

                                        if (level_server != null) {

                                            GameUtils.command.run(level_server, 0, 0, 0, command_update);

                                        } else {

                                            return true;

                                        }

                                    } else {

                                        if (level_server != null) {

                                            GameUtils.command.run(level_server, 0, 0, 0, "tellraw @a [{\"text\":\"" + id + " : Detected new version of TannyJung's Main Pack (" + branch + "). You can manual update by follow the guide in \",\"color\":\"gold\"},{\"text\":\"Wiki\",\"color\":\"white\",\"underlined\":\"true\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"" + wiki + "\"},\"hoverEvent\":{\"action\":\"show_item\",\"contents\":{\"id\":\"minecraft:diamond\",\"count\":1,\"tag\":\"{display:{Name:'\\\"" + wiki + "\\\"'}}\"}}},{\"text\":\" or click \",\"color\":\"gold\"},{\"text\":\"[here]\",\"color\":\"white\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/" + command_update + "\"}},{\"text\":\" to let the mod install it.\",\"color\":\"gold\"}]");

                                        } else {

                                            logger.info("Detected new version for TannyJung's Main Pack (" + branch + "). You can manual update by follow the guide in wiki (" + wiki + ") or join a world and let the mod install it.");

                                        }

                                    }

                                }

                            }

                        }

                        logger.info("Data Structure Version -> Mod {} GitHub {} / Pack Version -> Mod {} GitHub {}", data_structure_version_config, url_data_structure_version, pack_version, url_pack_version);

                    } catch (Exception ignored) {

                        message(level_server, logger, id, "error", "Can't check the update right now, try again later.");

                    }

                }

            }

        } else {

            // Not Found
            {

                if (level_server != null) {

                    GameUtils.command.run(level_server, 0, 0, 0, "tellraw @a [{\"text\":\"" + id + " : Not detected TannyJung's Main Pack (" + branch + ") in custom packs folder. Starting auto install...\",\"color\":\"gold\"}]");
                    GameUtils.command.run(level_server, 0, 0, 0, command_update);

                } else {

                    logger.info("Not detected TannyJung's Main Pack ({}) in custom packs folder. Starting auto install...", branch);
                    return true;

                }

            }

        }

        return false;

    }

    public static boolean reinstall (ServerLevel level_server, Logger logger, String path_config, String id, String pack_link, String branch) {

        String url = "https://github.com/" + pack_link + "/archive/refs/heads/" + branch.toLowerCase() + ".zip";

        if (OutsideUtils.isURLAvailable(url) == true) {

            if (level_server != null) {

                GameUtils.command.run(level_server, 0, 0, 0, "tellraw @a [{\"text\":\"" + id + " : Installing ZIP from GitHub. This may take a while. \",\"color\":\"gray\"},{\"text\":\"URL\",\"color\":\"white\",\"underlined\":\"true\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"" + url + "\"},\"hoverEvent\":{\"action\":\"show_item\",\"contents\":{\"id\":\"minecraft:diamond\",\"count\":1,\"tag\":\"{display:{Name:'\\\"" + url + "\\\"'}}\"}}}]");

            } else {

                logger.info("Installing ZIP from GitHub. This may take a while. ({})", url);

            }

            // Download and Extract
            {

                String path = path_config + "/custom_packs/#TannyJung-Main-Pack";

                FileManager.delete(path_config + "/custom_packs/#TannyJung-Main-Pack");
                FileManager.delete(path_config + "/custom_packs/[INCOMPATIBLE] #TannyJung-Main-Pack");
                FileManager.delete(path_config + "/custom_packs/#TannyJung-Main-Pack.zip");
                FileManager.delete(path_config + "/custom_packs/[INCOMPATIBLE] #TannyJung-Main-Pack.zip");

                OutsideUtils.download(url, path + ".zip");
                FileManager.extractZIP(path + ".zip", path, true, "");
                FileManager.delete(path + ".zip");

                // If a config about "ZIP main pack" is true. ZIP is good for lesser space, but might slower to get data.
                FileManager.compressZIP(path + ".zip", new File(path));
                FileManager.delete(path);

            }

            message(level_server, logger, id, "info", "Install Completed!");
            messageNews(level_server, logger, id, pack_link, branch);
            return true;

        } else {

            message(level_server, logger, id, "error", "Can't update right now, as the mod can't connect to GitHub.");

        }

        return false;

    }

    public static void messageNews (ServerLevel level_server, Logger logger, String id, String pack_link, String branch) {

        String url = "https://raw.githubusercontent.com/" + pack_link + "/" + branch.toLowerCase() + "/info.txt";

        if (OutsideUtils.isURLAvailable(url) == false) {

            String message = "";

            // Read File From GitHub
            {

                try { BufferedReader buffered_reader = new BufferedReader(new InputStreamReader(new URI(url).toURL().openStream()), 65536); String read_all = ""; while ((read_all = buffered_reader.readLine()) != null) {

                    {

                        if (read_all.startsWith("message = ")) {

                            message = read_all.substring("message = ".length());
                            break;

                        }

                    }

                } buffered_reader.close(); } catch (Exception exception) { OutsideUtils.exception(new Exception(), exception); }

            }

            if (message.equals("") == false) {

                message(level_server, logger, id, "info", "");
                GameUtils.command.run(level_server, 0, 0, 0, message);
                message(level_server, logger, id, "info", "");

            }

        }

    }

    private static void message (ServerLevel level_server, Logger logger, String id, String type, String message) {

        if (level_server != null) {

            if (message.equals("") == false) {

                String color = "";

                if (type.equals("info") == true) {

                    color = "gray";

                } else if (type.equals("error") == true) {

                    color = "red";

                }

                GameUtils.misc.sendChatMessage(level_server, null, "@a", color, id + " : " + message);

            } else {

                GameUtils.misc.sendChatMessage(level_server, null, "@a", "white", "");

            }

        } else {

            if (type.equals("info") == true) {

                logger.info(message);

            } else if (type.equals("error") == true) {

                logger.error(message);

            }

        }

    }

}