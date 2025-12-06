package tannyjung.tanshugetrees_handcode.config;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.io.File;
import java.io.FileReader;

import net.minecraft.server.level.ServerLevel;

import net.minecraft.world.level.LevelAccessor;
import tannyjung.core.OutsideUtils;
import tannyjung.core.game.GameUtils;
import tannyjung.tanshugetrees.TanshugetreesMod;
import tannyjung.tanshugetrees_handcode.Handcode;
import tannyjung.tanshugetrees_handcode.data.FileConfig;
import tannyjung.tanshugetrees_handcode.data.TannyPack;
;

public class PackCheckUpdate {

    public static void start (ServerLevel level_server) {

        String wiki = "https://sites.google.com/view/tannyjung/minecraft-projects/tans-huge-trees/installation";
        String url = "https://raw.githubusercontent.com/TannyJungMC/THT-tree_pack/" + Handcode.tanny_pack_type.toLowerCase() + "/version.txt";

        if (OutsideUtils.isURLAvailable(url) == false) {

            message(level_server, "error", "Can't check for update right now, as the mod can't connect to GitHub.");

        } else {

            File file = null;

            // Get Info File
            {

                file = new File(Handcode.path_config + "/#dev/temporary/info/#TannyJung-Main-Pack.zip.txt");

                if (file.exists() == false) {

                    file = new File(Handcode.path_config + "/#dev/temporary/info/[INCOMPATIBLE] #TannyJung-Main-Pack.zip.txt");

                }

                if (file.exists() == false) {

                    file = new File(Handcode.path_config + "/#dev/temporary/info/#TannyJung-Main-Pack.txt");

                }

                if (file.exists() == false) {

                    file = new File(Handcode.path_config + "/#dev/temporary/info/[INCOMPATIBLE] #TannyJung-Main-Pack.txt");

                }

            }

            if (file.exists() == true && file.isDirectory() == false) {

                // Test URL
                {

                    try {

                        URL url_convert = new URI(url).toURL();
                        int url_pack_version = 0;
                        int url_data_structure_version = 0;
                        int pack_version = 0;

                        // Get Your Version
                        {

                            try {
                                BufferedReader buffered_reader = new BufferedReader(new FileReader(file), 65536);
                                String read_all = "";
                                while ((read_all = buffered_reader.readLine()) != null) {

                                    {

                                        if (read_all.startsWith("pack_version = ")) {

                                            pack_version = Integer.parseInt(read_all.substring("pack_version = ".length()));

                                        }

                                    }

                                }
                                buffered_reader.close();
                            } catch (Exception exception) {
                                OutsideUtils.exception(new Exception(), exception);
                            }

                        }

                        // Read File From GitHub
                        {

                            try {
                                BufferedReader buffered_reader = new BufferedReader(new InputStreamReader(url_convert.openStream()), 65536);
                                String read_all = "";
                                while ((read_all = buffered_reader.readLine()) != null) {

                                    {

                                        if (read_all.startsWith("data_structure_version = ")) {

                                            url_data_structure_version = Integer.parseInt(read_all.substring("data_structure_version = ".length()));

                                        } else if (read_all.startsWith("pack_version = ")) {

                                            url_pack_version = Integer.parseInt(read_all.substring("pack_version = ".length()));

                                        }

                                    }

                                }
                                buffered_reader.close();
                            } catch (Exception exception) {
                                OutsideUtils.exception(new Exception(), exception);
                            }

                        }

                        if (pack_version == url_pack_version) {

                            message(level_server, "info", "TannyJung's Main Pack (" + Handcode.tanny_pack_type + ") is up to date");

                        } else {

                            if (url_data_structure_version == 0) {

                                message(level_server, "error", "Something went wrong with version testing. Maybe website is down or there's a new mod update.");

                            } else if (Handcode.data_structure_version > url_data_structure_version) {

                                message(level_server, "error", "Seems like you update the mod very fast! TannyJung's Main Pack (" + Handcode.tanny_pack_type + ") haven't updated to support this mod version yet, please wait a bit for the update to be available.");

                            } else {

                                // Detected New Version
                                {

                                    if (FileConfig.auto_check_update == true) {

                                        if (FileConfig.auto_update == true) {

                                            message(level_server, "info", "Detected new version for TannyJung's Main Pack (" + Handcode.tanny_pack_type + "). Starting auto update...");
                                            TannyPack.start(level_server);

                                        } else {

                                            if (level_server != null) {

                                                GameUtils.command.run(level_server, 0, 0, 0, "tellraw @a [{\"text\":\"THT : Detected new version for TannyJung's Main Pack (" + Handcode.tanny_pack_type + "). You can manual update by follow the guide in \",\"color\":\"gold\"},{\"text\":\"Wiki\",\"color\":\"white\",\"underlined\":\"true\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"" + wiki + "\"},\"hoverEvent\":{\"action\":\"show_item\",\"contents\":{\"id\":\"minecraft:air\",\"count\":1,\"tag\":\"{display:{Name:'\\\"" + wiki + "\\\"'}}\"}}},{\"text\":\" or click \",\"color\":\"gold\"},{\"text\":\"[here]\",\"color\":\"white\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/TANSHUGETREES custom_pack update_main\"}},{\"text\":\" to let the mod install it.\",\"color\":\"gold\"}]");

                                            } else {

                                                TanshugetreesMod.LOGGER.info("Detected new version for TannyJung's Main Pack (" + Handcode.tanny_pack_type + "). You can manual update by follow the guide in wiki (" + wiki + ") or join a world and let the mod install it.");

                                            }

                                        }

                                    }

                                }

                            }

                            TanshugetreesMod.LOGGER.info("Data Structure Version   ->   Mod {} GitHub {}", Handcode.data_structure_version, url_data_structure_version);
                            TanshugetreesMod.LOGGER.info("Pack Version   ->   Mod {} GitHub {}", pack_version, url_pack_version);

                        }

                    } catch (Exception ignored) {

                        message(level_server, "error", "Can't check the update right now, try again later.");

                    }

                }

            } else {

                // Not Detect Pack
                {

                    if (level_server != null) {

                        GameUtils.command.run(level_server, 0, 0, 0, "tellraw @a [{\"text\":\"THT : Not detected TannyJung's Main Pack (" + Handcode.tanny_pack_type + ") in the custom packs folder. You can manual install by follow the guide in \",\"color\":\"gold\"},{\"text\":\"GitHub\",\"color\":\"white\",\"underlined\":\"true\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"" + wiki + "\"},\"hoverEvent\":{\"action\":\"show_item\",\"contents\":{\"id\":\"minecraft:air\",\"count\":1,\"tag\":\"{display:{Name:'\\\"" + wiki + "\\\"'}}\"}}},{\"text\":\" or click \",\"color\":\"gold\"},{\"text\":\"[here]\",\"color\":\"white\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/TANSHUGETREES custom_pack update_main\"}},{\"text\":\" to let the mod install it.\",\"color\":\"gold\"}]");

                    } else {

                        TanshugetreesMod.LOGGER.info("Not detected TannyJung's Main Pack (" + Handcode.tanny_pack_type + "). You can manual update by follow the guide in wiki (" + wiki + ") or join a world and let the mod install it.");

                    }

                }

                TannyPack.reinstall(null);

            }

        }

    }

    private static void message (ServerLevel level_server, String type, String message) {

        if (level_server != null) {

            String color = "";

            if (type.equals("info") == true) {

                color = "gray";

            } else if (type.equals("error") == true) {

                color = "red";

            }

            GameUtils.misc.sendChatMessage(level_server, "@a", color, "THT : " + message);

        } else {

            if (type.equals("info") == true) {

                TanshugetreesMod.LOGGER.info(message);

            } else if (type.equals("error") == true) {

                TanshugetreesMod.LOGGER.error(message);

            }

        }

    }

}