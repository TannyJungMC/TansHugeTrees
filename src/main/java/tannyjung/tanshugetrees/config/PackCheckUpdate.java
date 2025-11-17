package tannyjung.tanshugetrees.config;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.io.File;
import java.io.FileReader;

import net.minecraft.server.level.ServerLevel;

import net.minecraft.world.level.LevelAccessor;
import tannyjung.core.OutsideUtils;
import tannyjung.core.game.Utils;
import tannyjung.tanshugetrees.Handcode;
;

public class PackCheckUpdate {

    public static void start (LevelAccessor level_accessor, boolean up_to_date_message) {

		if (level_accessor instanceof ServerLevel level_server) {

			if (OutsideUtils.Misc.isConnectedToInternet() == false) {

				Utils.misc.sendChatMessage(level_server, "@a", "red", "THT : Can't check for update right now, as the mod can't connect to the internet.");

			} else {

				String github = "";
				String url = "";

				if (ConfigMain.wip_version == false) {

					github = "https://github.com/TannyJungMC/THT-tree_pack/" + Handcode.tanny_pack_version_name.toLowerCase();
					url = "https://raw.githubusercontent.com/TannyJungMC/THT-tree_pack/" + Handcode.tanny_pack_version_name.toLowerCase() + "/version.txt";

				} else {

					github = "https://github.com/TannyJungMC/THT-tree_pack/tree/wip";
					url = "https://raw.githubusercontent.com/TannyJungMC/THT-tree_pack/wip/version.txt";

				}

				File file = new File(Handcode.path_config + "/custom_packs/#TannyJung-Main-Pack/version.txt");

				if (file.exists() == false) {

					file = new File(Handcode.path_config + "/custom_packs/[INCOMPATIBLE] #TannyJung-Main-Pack/version.txt");

				}

				if (file.exists() == true && file.isDirectory() == false) {

					try {

						URL url_convert = new URI(url).toURL();
						int url_pack_version = 0;
						int url_data_structure_version = 0;
						int pack_version = 0;

						// Get Your Version
						{

							try { BufferedReader buffered_reader = new BufferedReader(new FileReader(file), 65536); String read_all = ""; while ((read_all = buffered_reader.readLine()) != null) {

								{

									if (read_all.startsWith("pack_version = ")) {

										pack_version = Integer.parseInt(read_all.replace("pack_version = ", ""));

									}

								}

							} buffered_reader.close(); } catch (Exception exception) { OutsideUtils.exception(new Exception(), exception); }

						}

						// Read File From GitHub
						{

							try { BufferedReader buffered_reader = new BufferedReader(new InputStreamReader(url_convert.openStream()), 65536); String read_all = ""; while ((read_all = buffered_reader.readLine()) != null) {

								{

									if (read_all.startsWith("data_structure_version = ")) {

										url_data_structure_version = Integer.parseInt(read_all.replace("data_structure_version = ", ""));

									} else if (read_all.startsWith("pack_version = ")) {

										url_pack_version = Integer.parseInt(read_all.replace("pack_version = ", ""));

									}

								}

							} buffered_reader.close(); } catch (Exception exception) { OutsideUtils.exception(new Exception(), exception); }

						}

						if (pack_version == url_pack_version) {

							if (up_to_date_message == true) {

								Utils.misc.sendChatMessage(level_server, "@a", "gray", "THT : TannyJung's Main Pack (" + Handcode.tanny_pack_version_name + ") is already up to date");

							}

						} else {

							if (Handcode.DATA_STRUCTURE_VERSION <= url_data_structure_version) {

								if (ConfigMain.auto_check_update == true) {

									if (ConfigMain.auto_update == true) {

										Utils.misc.sendChatMessage(level_server, "@a", "gold", "THT : Detected new version for TannyJung's Main Pack (" + Handcode.tanny_pack_version_name + "). Starting auto update...");
										PackUpdate.start(level_server);

									} else {

										Utils.command.run(level_server, 0, 0, 0, "tellraw @a [{\"text\":\"THT : Detected new version for TannyJung's Main Pack (" + Handcode.tanny_pack_version_name + "). You can manual update by follow the guide in \",\"color\":\"gold\"},{\"text\":\"GitHub\",\"color\":\"white\",\"underlined\":\"true\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"" + github + "\"},\"hoverEvent\":{\"action\":\"show_item\",\"contents\":{\"id\":\"minecraft:air\",\"count\":1,\"tag\":\"{display:{Name:'\\\"" + github + "\\\"'}}\"}}},{\"text\":\" or click \",\"color\":\"gold\"},{\"text\":\"[here]\",\"color\":\"white\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/TANSHUGETREES tanny_pack update\"}},{\"text\":\" to let the mod install it.\",\"color\":\"gold\"}]");

									}

								}

							} else {

								Utils.misc.sendChatMessage(level_server, "@a", "red", "THT : Seems like you update the mod very fast! TannyJung's Main Pack (" + Handcode.tanny_pack_version_name + ") haven't updated to support this mod version yet, please wait a bit for the update to be available.");
								Handcode.logger.info("Your mod data structure version is " + Handcode.DATA_STRUCTURE_VERSION + " but the pack is " + url_data_structure_version);

							}

						}

					} catch (Exception ignored) {

						Utils.misc.sendChatMessage(level_server, "@a", "red", "THT : Can't check the update right now, try again later.");

					}

				} else {

					Utils.command.run(level_server, 0, 0, 0, "tellraw @a [{\"text\":\"THT : Not detected TannyJung's Main Pack (" + Handcode.tanny_pack_version_name + ") in the custom packs folder. You can manual install by follow the guide in \",\"color\":\"gold\"},{\"text\":\"GitHub\",\"color\":\"white\",\"underlined\":\"true\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"" + github + "\"},\"hoverEvent\":{\"action\":\"show_item\",\"contents\":{\"id\":\"minecraft:air\",\"count\":1,\"tag\":\"{display:{Name:'\\\"" + github + "\\\"'}}\"}}},{\"text\":\" or click \",\"color\":\"gold\"},{\"text\":\"[here]\",\"color\":\"white\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/TANSHUGETREES tanny_pack update\"}},{\"text\":\" to let the mod install it.\",\"color\":\"gold\"}]");

				}

			}

		}

    }

}