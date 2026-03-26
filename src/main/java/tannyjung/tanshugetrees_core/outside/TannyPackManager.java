package tannyjung.tanshugetrees_core.outside;

import net.minecraft.server.level.ServerLevel;
import tannyjung.tanshugetrees_core.Core;
import tannyjung.tanshugetrees_core.game.GameUtils;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class TannyPackManager {

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

    public static String[] testVersion (ServerLevel level_server, File pack) {

        String[] test = new String[]{"", "", ""};
        String url = "https://raw.githubusercontent.com/TannyJungMC/" + Core.github_pack + "/" + Core.tanny_pack_type.toLowerCase() + "/info.txt";

        if (OutsideUtils.isURLAvailable(url) == false) {

            test[0] = "Connection Failed";

        } else {

            String url_data_structure_version = "";
            int url_pack_version = 0;

            // Get Version (Online)
            {

                for (String read_all : OutsideUtils.readOnlineTXT(url)) {

                    if (read_all.startsWith("data_structure_version = ")) {

                        url_data_structure_version = read_all.substring("data_structure_version = ".length());

                    } else if (read_all.startsWith("pack_version = ")) {

                        url_pack_version = Integer.parseInt(read_all.substring("pack_version = ".length()));

                    }

                }

            }

            if (url_data_structure_version.isEmpty() == true || url_pack_version == 0) {

                test[0] = "Version Test Failed";

            } else {

                String version_test_data = OutsideUtils.testVersion(Core.data_structure_version_pack, url_data_structure_version);

                if (version_test_data.equals("early") == true) {

                    test[0] = "Not Support Yet";
                    test[1] = url_data_structure_version;

                } else if (version_test_data.equals("outdated") == true) {

                    test[0] = "Required New Version";
                    test[1] = url_data_structure_version;

                } else if (version_test_data.equals("same") == true) {

                    int pack_version = 0;

                    // Get Version (Your)
                    {

                        for (String read_all : FileManager.readTXT(Core.path_config + "/#dev/temporary/info/" + pack.getName() + ".txt")) {

                            if (read_all.startsWith("pack_version = ")) {

                                pack_version = Integer.parseInt(read_all.substring("pack_version = ".length()));
                                break;

                            }

                        }

                    }

                    if (pack_version > url_pack_version) {

                        test[0] = "Impossible";
                        test[1] = String.valueOf(pack_version);
                        test[2] = String.valueOf(url_pack_version);

                    } else if (pack_version < url_pack_version) {

                        test[0] = "Ready To Update";
                        test[1] = String.valueOf(pack_version);
                        test[2] = String.valueOf(url_pack_version);

                    } else {

                        test[0] = "Up To Date";

                        {

                            if (level_server != null) {

                                GameUtils.Misc.sendChatMessage(level_server, "TannyJung's Main Pack (" + Core.tanny_pack_type + ") for " + Core.mod_name + " mod is already up to date / gray");

                            } else {

                                Core.logger.info("TannyJung's Main Pack ({}) for {} mod is already up to date", Core.tanny_pack_type, Core.mod_name);

                            }

                        }

                    }

                }

            }

        }

        return test;

    }

    public static void runCheckUpdate (ServerLevel level_server) {

        File pack = getCurrentFile();

        if (pack.exists() == false) {

            runUpdate(level_server);

        } else {

            String[] test = testVersion(level_server, pack);

            // Test
            {

                if (test[0].equals("Ready To Update") == true) {

                    {

                        if (level_server != null) {

                            GameUtils.Misc.sendChatMessage(level_server, "Detected a new update of TannyJung's Main Pack (" + Core.tanny_pack_type + ") for " + Core.mod_name + " mod available to download from GitHub. You're currently using " + test[1] + " but there's new " + test[2] + " version. You can manual update by follow the guide in the  / gold | Wiki / white / " + Core.wiki + " |  or click  / gold | here / white / /" + Core.mod_id_big + " tanny_pack update" + " |  to let the mod update it. After the update may causes some issues in your world. If you very care about this world, then I would recommend to do it before start new world instead. / gold");

                        } else {

                            Core.logger.info("Detected a new update of TannyJung's Main Pack ({}) for {} mod available to download from GitHub. You're currently using {} but there's new {} version. You can manual update by follow the guide in the wiki ({}) or use [ /{} tanny_pack update ] command to let the mod update it. After the update may causes some issues in your world. If you very care about this world, then I would recommend to do it before start new world instead.", Core.tanny_pack_type, Core.mod_name, test[1], test[2], Core.wiki, Core.mod_id_big);

                        }

                    }

                } else if (test[0].equals("Impossible") == true) {

                    {

                        if (level_server != null) {

                            GameUtils.Misc.sendChatMessage(level_server, "How is this possible? You're currently using TannyJung's Main Pack (" + Core.tanny_pack_type + ") version " + test[1] + " for " + Core.mod_name + " mod but the pack from GitHub is " + test[2] + " version. You're from the future? Maybe it's just me updating something, or you just joking me. / gold");

                        } else {

                            Core.logger.info("How is this possible? You're currently using TannyJung's Main Pack ({}) version {} for {} mod but the pack from GitHub is {} version. You're from the future?. Maybe it's just me updating something, or you just joking me.", Core.tanny_pack_type, Core.mod_name, test[1], test[2]);

                        }

                    }

                } else if (test[0].equals("Not Support Yet") == true) {

                    {

                        if (level_server != null) {

                            GameUtils.Misc.sendChatMessage(level_server, "Seems like you update the mod very fast! TannyJung's Main Pack (" + Core.tanny_pack_type + ") for " + Core.mod_name + " mod from GitHub haven't updated to support this mod version yet, please wait a bit for the update to be available. You're currently using " + Core.data_structure_version_pack + " but it's still for " + test[1] + " version. / gold");

                        } else {

                            Core.logger.info("Seems like you update the mod very fast! TannyJung's Main Pack ({}) for {} mod from GitHub haven't updated to support this mod version yet, please wait a bit for the update to be available. You're currently using {} but it's still for {} version.", Core.tanny_pack_type, Core.mod_name, Core.data_structure_version_pack, test[1]);

                        }

                    }

                } else if (test[0].equals("Required New Version") == true) {

                    {

                        if (level_server != null) {

                            GameUtils.Misc.sendChatMessage(level_server, "Detected a new update of TannyJung's Main Pack (" + Core.tanny_pack_type + ") for " + Core.mod_name + " mod available to download from GitHub, but it requires new mod version. You're currently using " + Core.data_structure_version_pack + " but requires " + test[1] + " version. Try update the mod first, if you want to update it. / gold");

                        } else {

                            Core.logger.info("Detected a new update of TannyJung's Main Pack ({}) for {} mod available to download from GitHub, but it requires new mod version. You're currently using {} but requires {} version. Please update the mod first, if you want to install it.", Core.tanny_pack_type, Core.mod_name, Core.data_structure_version_pack, test[1]);

                        }

                    }

                } else if (test[0].equals("Connection Failed") == true) {

                    {

                        if (level_server != null) {

                            GameUtils.Misc.sendChatMessage(level_server, "Can't check for new update of TannyJung's Main Pack (" + Core.tanny_pack_type + ") for " + Core.mod_name + " mod from GitHub right now, because the mod can't connect to GitHub. This maybe internet connection problem, the website currently down, your country blocked GitHub, or there's a new mod update. Here is the  / red | Wiki / white / " + Core.wiki + " |  if you want to manual install it. / red");

                        } else {

                            Core.logger.error("Can't check for new update of TannyJung's Main Pack ({}) for {} mod from GitHub right now, because the mod can't connect to GitHub. This maybe internet connection problem, the website currently down, your country blocked GitHub, or there's a new mod update. Here is wiki if you want to manual install it ({}).", Core.tanny_pack_type, Core.mod_name, Core.wiki);

                        }

                    }

                } else if (test[0].equals("Version Test Failed") == true) {

                    {

                        if (level_server != null) {

                            GameUtils.Misc.sendChatMessage(level_server, "Can't check for new update of TannyJung's Main Pack (" + Core.tanny_pack_type + ") for " + Core.mod_name + " mod from GitHub right now, because something went wrong with version testing. This maybe internet connection problem, the website currently down, your country blocked GitHub, or there's a new mod update. Here is the  / red | Wiki / white / " + Core.wiki + " |  if you want to manual install it. / red");

                        } else {

                            Core.logger.error("Can't check for new update of TannyJung's Main Pack ({}) for {} mod from GitHub right now, because something went wrong with version testing. This maybe internet connection problem, the website currently down, your country blocked GitHub, or there's a new mod update. Here is wiki if you want to manual install it ({}).", Core.tanny_pack_type, Core.mod_name, Core.wiki);

                        }

                    }

                }

            }

        }

    }

    public static void runUpdate (ServerLevel level_server) {

        File pack = getCurrentFile();
        boolean pack_exists = pack.exists();

        if (pack_exists == false) {

            // Not Found
            {

                if (level_server != null) {

                    GameUtils.Misc.sendChatMessage(level_server, "Not detected TannyJung's Main Pack (" + Core.tanny_pack_type + ") for " + Core.mod_name + " mod in custom packs folder. Starting auto install... / gold");

                } else {

                    Core.logger.info("Not detected TannyJung's Main Pack ({}) for {} mod in custom packs folder. Starting auto install...", Core.tanny_pack_type, Core.mod_name);

                }

            }

        }

        boolean start = false;
        boolean online = false;
        String[] test = testVersion(level_server, pack);

        // Test
        {

            if (test[0].equals("Ready To Update") == true) {

                start = true;
                online = true;

                if (pack_exists == true) {

                    {

                        if (level_server != null) {

                            GameUtils.Misc.sendChatMessage(level_server, "Updating TannyJung's Main Pack (" + Core.tanny_pack_type + ") for " + Core.mod_name + " mod from " + test[1] + " to " + test[2] + " new version from GitHub. This may take a while. / gray");

                        } else {

                            Core.logger.info("Updating TannyJung's Main Pack ({}) for {} mod from {} to {} new version from GitHub. This may take a while.", Core.tanny_pack_type, Core.mod_name, test[1], test[2]);

                        }

                    }

                } else {

                    {

                        if (level_server != null) {

                            GameUtils.Misc.sendChatMessage(level_server, "Installing TannyJung's Main Pack (" + Core.tanny_pack_type + ") latest version " + test[2] + " for " + Core.mod_name + " mod from GitHub. This may take a while. / gray");

                        } else {

                            Core.logger.info("Installing TannyJung's Main Pack ({}) latest version {} for {} mod from GitHub. This may take a while.", Core.tanny_pack_type, Core.mod_name, test[2]);

                        }

                    }

                }

            } else if (test[0].equals("Impossible") == true) {

                if (pack_exists == false) {

                    start = true;

                }

                {

                    if (level_server != null) {

                        GameUtils.Misc.sendChatMessage(level_server, "No, you can't update TannyJung's Main Pack (" + Core.tanny_pack_type + ") for " + Core.mod_name + " mod to new version from GitHub right now, because you're from the future. You're currently using " + test[1] + " but the pack from GitHub is " + test[2] + " version. Tell me this week lottery and I will update it for you. / red");

                    } else {

                        Core.logger.error("No, you can't update TannyJung's Main Pack ({}) for {} mod to new version from GitHub right now, because you're from the future. You're currently using {} but the pack from GitHub is {} version. Tell me this week lottery and I will update it for you.", Core.tanny_pack_type, Core.mod_name, test[1], test[2]);

                    }

                }

            } else if (test[0].equals("Not Support Yet") == true) {

                if (pack_exists == false) {

                    start = true;

                    {

                        if (level_server != null) {

                            GameUtils.Misc.sendChatMessage(level_server, "Can't install TannyJung's Main Pack (" + Core.tanny_pack_type + ") latest version for " + Core.mod_name + " mod from GitHub, because it haven't updated to support this mod version yet. Please wait a bit for the update to be available. You're currently using " + Core.data_structure_version_pack + " but it's still for " + test[1] + " version. / red");

                        } else {

                            Core.logger.error("Can't install TannyJung's Main Pack ({}) latest version for {} mod from GitHub, because it haven't updated to support this mod version yet. Please wait a bit for the update to be available. You're currently using {} but it's still for {} version.", Core.tanny_pack_type, Core.mod_name, Core.data_structure_version_pack, test[1]);

                        }

                    }

                } else {

                    {

                        if (level_server != null) {

                            GameUtils.Misc.sendChatMessage(level_server, "Can't update TannyJung's Main Pack (" + Core.tanny_pack_type + ") for " + Core.mod_name + " mod to new version from GitHub, because it haven't updated to support this mod version yet. Please wait a bit for the update to be available. You're currently using " + Core.data_structure_version_pack + " but it's still for " + test[1] + " version. / red");

                        } else {

                            Core.logger.error("Can't update TannyJung's Main Pack ({}) for {} mod to new version from GitHub, because it haven't updated to support this mod version yet. Please wait a bit for the update to be available. You're currently using {} but it's still for {} version.", Core.tanny_pack_type, Core.mod_name, Core.data_structure_version_pack, test[1]);

                        }

                    }

                }

            } else if (test[0].equals("Required New Version") == true) {

                if (pack_exists == false) {

                    start = true;

                }

                {

                    if (level_server != null) {

                        GameUtils.Misc.sendChatMessage(level_server, "Can't update TannyJung's Main Pack (" + Core.tanny_pack_type + ") for " + Core.mod_name + " mod to new version from GitHub, because it requires new mod version. You're currently using " + Core.data_structure_version_pack + " but requires " + test[1] + " version. Try update the mod first, if you want to update it. / red");

                    } else {

                        Core.logger.error("Can't update TannyJung's Main Pack ({}) for {} mod to new version from GitHub, because it requires new mod version. You're currently using {} but requires {} version. Please update the mod first, if you want to install it.", Core.tanny_pack_type, Core.mod_name, Core.data_structure_version_pack, test[1]);

                    }

                }

            } else if (test[0].equals("Connection Failed") == true) {

                if (pack_exists == false) {

                    start = true;

                    {

                        if (level_server != null) {

                            GameUtils.Misc.sendChatMessage(level_server, "Can't install TannyJung's Main Pack (" + Core.tanny_pack_type + ") latest version for " + Core.mod_name + " mod from GitHub right now, because the mod can't connect to GitHub. This maybe internet connection problem, the website currently down, your country blocked GitHub, or there's a new mod update. Here is the  / red | Wiki / white / " + Core.wiki + " |  if you want to manual install it. / red");

                        } else {

                            Core.logger.error("Can't install TannyJung's Main Pack ({}) latest version for {} mod from GitHub right now, because the mod can't connect to GitHub. This maybe internet connection problem, the website currently down, your country blocked GitHub, or there's a new mod update. Here is wiki if you want to manual install it ({}).", Core.tanny_pack_type, Core.mod_name, Core.wiki);

                        }

                    }

                } else {

                    {

                        if (level_server != null) {

                            GameUtils.Misc.sendChatMessage(level_server, "Can't update TannyJung's Main Pack (" + Core.tanny_pack_type + ") for " + Core.mod_name + " mod to new version from GitHub right now, because the mod can't connect to GitHub. This maybe internet connection problem, the website currently down, your country blocked GitHub, or there's a new mod update. Here is the  / red | Wiki / white / " + Core.wiki + " |  if you want to manual update it. / red");

                        } else {

                            Core.logger.error("Can't update TannyJung's Main Pack ({}) for {} mod to new version from GitHub right now, because the mod can't connect to GitHub. This maybe internet connection problem, the website currently down, your country blocked GitHub, or there's a new mod update. Here is wiki if you want to manual update it ({}).", Core.tanny_pack_type, Core.mod_name, Core.wiki);

                        }

                    }

                }

            } else if (test[0].equals("Version Test Failed") == true) {

                if (pack_exists == false) {

                    start = true;

                    {

                        if (level_server != null) {

                            GameUtils.Misc.sendChatMessage(level_server, "Can't install TannyJung's Main Pack (" + Core.tanny_pack_type + ") latest version for " + Core.mod_name + " mod from GitHub right now, because something went wrong with version testing. This maybe internet connection problem, the website currently down, your country blocked GitHub, or there's a new mod update. Here is the  / red | Wiki / white / " + Core.wiki + " |  if you want to manual install it. / red");

                        } else {

                            Core.logger.error("Can't install TannyJung's Main Pack ({}) latest version for {} mod from GitHub right now, because something went wrong with version testing. This maybe internet connection problem, the website currently down, your country blocked GitHub, or there's a new mod update. Here is wiki if you want to manual install it ({}).", Core.tanny_pack_type, Core.mod_name, Core.wiki);

                        }

                    }

                } else {

                    {

                        if (level_server != null) {

                            GameUtils.Misc.sendChatMessage(level_server, "Can't update TannyJung's Main Pack (" + Core.tanny_pack_type + ") for " + Core.mod_name + " mod to new version from GitHub right now, because something went wrong with version testing. This maybe internet connection problem, the website currently down, your country blocked GitHub, or there's a new mod update. Here is the  / red | Wiki / white / " + Core.wiki + " |  if you want to manual update it. / red");

                        } else {

                            Core.logger.error("Can't update TannyJung's Main Pack ({}) for {} mod to new version from GitHub right now, because something went wrong with version testing. This maybe internet connection problem, the website currently down, your country blocked GitHub, or there's a new mod update. Here is wiki if you want to manual update it ({}).", Core.tanny_pack_type, Core.mod_name, Core.wiki);

                        }

                    }

                }

            }

        }

        if (start == true) {

            // Updating
            {

                boolean pass = false;

                if (online == true) {

                    // Online
                    {

                        if (OutsideUtils.download("https://github.com/TannyJungMC/" + Core.github_pack + "/archive/refs/heads/" + Core.tanny_pack_type.toLowerCase() + ".zip", Core.path_config + "/custom_packs/#TannyJung-Main-Pack.zip") == true) {

                            FileManager.extractZIP(Core.path_config + "/custom_packs/#TannyJung-Main-Pack.zip", Core.path_config + "/custom_packs/#TannyJung-Main-Pack", true, "");
                            FileManager.delete(Core.path_config + "/custom_packs/#TannyJung-Main-Pack.zip");
                            FileManager.compressZIP(Core.path_config + "/custom_packs/#TannyJung-Main-Pack.zip", new File(Core.path_config + "/custom_packs/#TannyJung-Main-Pack"));
                            FileManager.delete(Core.path_config + "/custom_packs/#TannyJung-Main-Pack");
                            pass = true;

                        }

                    }

                }

                if (pass == false) {

                    // Offline
                    {

                        if (level_server != null) {

                            GameUtils.Misc.sendChatMessage(level_server, "Changed to built-in version instead, which maybe outdated. / gray");

                        } else {

                            Core.logger.info("Changed to built-in version instead, which maybe outdated.");

                        }

                        // Extract From JAR
                        {

                            try {

                                InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream("#TannyJung-Main-Pack.zip");

                                if (stream != null) {

                                    Files.copy(stream, Path.of(Core.path_config + "/custom_packs/#TannyJung-Main-Pack.zip"), StandardCopyOption.REPLACE_EXISTING);
                                    stream.close();

                                }

                            } catch (Exception exception) {

                                OutsideUtils.exception(new Exception(), exception, "");

                            }

                        }

                        pass = true;

                    }

                }

                if (pass == true) {

                    // Completed
                    {

                        if (pack_exists == false) {

                            {

                                if (level_server != null) {

                                    GameUtils.Misc.sendChatMessage(level_server, "Install Completed! / gray");

                                } else {

                                    Core.logger.info("Install Completed!");

                                }

                            }

                        } else {

                            {

                                if (level_server != null) {

                                    GameUtils.Misc.sendChatMessage(level_server, "Update Completed! / gray");

                                } else {

                                    Core.logger.info("Update Completed!");

                                }

                            }

                        }

                    }

                    Core.Restart.run(level_server, true, true);

                }

            }

        }

    }

}