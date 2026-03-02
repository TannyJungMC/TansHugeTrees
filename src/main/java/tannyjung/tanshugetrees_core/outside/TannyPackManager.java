package tannyjung.tanshugetrees_core.outside;

import net.minecraft.server.level.ServerLevel;
import tannyjung.tanshugetrees_core.Core;
import tannyjung.tanshugetrees_core.game.GameUtils;
import tannyjung.tanshugetrees_handcode.data.FileConfig;

import java.io.File;

public class TannyPackManager {

    public static void checkUpdate (ServerLevel level_server) {

        String url = "https://raw.githubusercontent.com/TannyJungMC/" + Core.github_pack + "/" + Core.tanny_pack_type.toLowerCase() + "/info.txt";

        if (OutsideUtils.isURLAvailable(url) == false) {

            if (level_server != null) {

                GameUtils.Misc.sendChatMessage(level_server, "@a", "Can't check for update right now, because the mod can't connect to GitHub. This maybe the website is currently down or there's a new mod update. The most serious case is your country blocked GitHub website, that will need to manual install with VPN enabled. Here is / red | Wiki / white / " + Core.wiki + " |  if you want to manual install. / red");

            } else {

                Core.logger.error("Can't check for update right now, because the mod can't connect to GitHub. This maybe the website is currently down or there's a new mod update. The most serious case is your country blocked GitHub website, that will need to manual install with VPN enabled. Here is installation wiki if you want to manual install ({}).", Core.wiki);

            }

        } else {

            String url_pack_version = "";
            String url_data_structure_version = "";
            String pack_version = "";
            File file_pack = getCurrentFile();

            // Get Version Offline
            {

                File file = new File(Core.path_config + "/#dev/#temporary/info/" + file_pack.getName() + ".txt");

                if (file.exists() == true && file.isDirectory() == false) {

                    for (String read_all : FileManager.readTXT(file.getPath())) {

                        {

                            if (read_all.startsWith("pack_version = ")) {

                                pack_version = read_all.substring("pack_version = ".length());
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

                            url_data_structure_version = read_all.substring("data_structure_version = ".length());

                        } else if (read_all.startsWith("pack_version = ")) {

                            url_pack_version = read_all.substring("pack_version = ".length());
                            break;

                        }

                    }

                }

            }

            if (url_data_structure_version.isEmpty() == true) {

                if (level_server != null) {

                    GameUtils.Misc.sendChatMessage(level_server, "@a", "Something went wrong with version testing. This maybe the website is currently down or there's a new mod update. The most serious case is your country blocked GitHub website, that will need to manual install with VPN enabled. Here is / red | Wiki / white / " + Core.wiki + " |  if you want to manual install. / red");

                } else {

                    Core.logger.error("Something went wrong with version testing. This maybe the website is currently down or there's a new mod update. The most serious case is your country blocked GitHub website, that will need to manual install with VPN enabled. Here is installation wiki if you want to manual install ({}).", Core.wiki);

                }

            } else {

                if (pack_version.equals(url_pack_version) == true) {

                    if (level_server != null) {

                        GameUtils.Misc.sendChatMessage(level_server, "@a", "TannyJung's Main Pack (" + Core.tanny_pack_type + ") is up to date / gray");

                    } else {

                        Core.logger.info("TannyJung's Main Pack ({}) is up to date", Core.tanny_pack_type);

                    }

                } else {

                    String test = OutsideUtils.testVersion(Core.data_structure_version_mod, url_data_structure_version);

                    if (test.equals("early") == true) {

                        if (level_server != null) {

                            GameUtils.Misc.sendChatMessage(level_server, "@a", "Seems like you update the mod very fast! TannyJung's Main Pack (" + Core.tanny_pack_type + ") haven't updated to support this mod version yet, please wait a bit for the update to be available. / gold");

                        } else {

                            Core.logger.info("Seems like you update the mod very fast! TannyJung's Main Pack ({}) haven't updated to support this mod version yet, please wait a bit for the update to be available.", Core.tanny_pack_type);

                        }

                    } else if (test.equals("outdated") == true) {

                        if (level_server != null) {

                            GameUtils.Misc.sendChatMessage(level_server, "@a", "Detected new version of TannyJung's Main Pack (" + Core.tanny_pack_type + "), but it requires new mod version. Please update the mod if you want to install it. / gold");

                        } else {

                            Core.logger.info("Detected new version of TannyJung's Main Pack ({}), but it requires new mod version. Please update the mod if you want to install it.", Core.tanny_pack_type);

                        }

                    } else if (test.equals("same") == true) {

                        if (file_pack.exists() == true) {

                            // Detected New Version
                            {

                                if (FileConfig.auto_update == true) {

                                    if (level_server != null) {

                                        GameUtils.Misc.sendChatMessage(level_server, "@a", "Detected new version of TannyJung's Main Pack (" + Core.tanny_pack_type + "). Starting auto update... / gray");

                                    } else {

                                        Core.logger.info("Detected new version of TannyJung's Main Pack (" + Core.tanny_pack_type + "). Starting auto update...", Core.tanny_pack_type);

                                    }

                                    reinstall(level_server);

                                } else {

                                    if (level_server != null) {

                                        GameUtils.Misc.sendChatMessage(level_server, "@a", "Detected new version of TannyJung's Main Pack (" + Core.tanny_pack_type + "). You can manual update by follow  / gold | Installation Guide / white / " + Core.wiki + " |  or click  / gold | here / white / /" + Core.mod_id_big + " tanny_pack update" + " |  to let the mod do it automatically. / gold");

                                    } else {

                                        Core.logger.info("Detected new version for TannyJung's Main Pack ({}). You can manual update by follow the guide in wiki ({}) or join a world and let the mod install it.", Core.tanny_pack_type, Core.wiki);

                                    }

                                }

                            }

                        } else {

                            // Not Found
                            {

                                if (level_server != null) {

                                    GameUtils.Misc.sendChatMessage(level_server, "@a", "Not detected TannyJung's Main Pack (" + Core.tanny_pack_type + ") in custom packs folder. Starting auto install... / gold");

                                } else {

                                    Core.logger.info("Not detected TannyJung's Main Pack ({}) in custom packs folder. Starting auto install...", Core.tanny_pack_type);

                                }

                                reinstall(level_server);

                            }

                        }

                    }

                }

            }

            Core.logger.info("Data Structure Version -> Mod {} GitHub {} / Pack Version -> Mod {} GitHub {}", Core.data_structure_version_mod, url_data_structure_version, pack_version, url_pack_version);

        }

    }

    public static void reinstall (ServerLevel level_server) {

        Runnable runnable = () -> {

            String url = "https://github.com/TannyJungMC/" + Core.github_pack + "/archive/refs/heads/" + Core.tanny_pack_type.toLowerCase() + ".zip";

            if (level_server != null) {

                GameUtils.Misc.sendChatMessage(level_server, "@a", "Installing ZIP from GitHub. This may take a while. / gray");

            } else {

                Core.logger.info("Installing ZIP from GitHub. This may take a while.");

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

                if (level_server != null) {

                    GameUtils.Misc.sendChatMessage(level_server, "@a", "Install Completed! / gray");

                } else {

                    Core.logger.info("Install Completed!");

                }

                Core.Restart.run(level_server, "config / world", true);

            } else {

                if (level_server != null) {

                    GameUtils.Misc.sendChatMessage(level_server, "@a", "Can't install right now, because the mod can't connect to GitHub. This maybe the website is currently down or there's a new mod update. The most serious case is your country blocked GitHub website, that will need to manual install with VPN enabled. Here is / red | Installation Guide / white / " + Core.wiki + " |  if you want to manual install. / red");

                } else {

                    Core.logger.error("Can't install right now, because the mod can't connect to GitHub. This maybe the website is currently down or there's a new mod update. The most serious case is your country blocked GitHub website, that will need to manual install with VPN enabled. Here is installation wiki if you want to manual install ({}).", Core.wiki);

                }

            }

        };

        if (level_server != null) {

            Core.Restart.testLock();
            Core.thread_main.submit(runnable);

        } else {

            runnable.run();

        }

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

}