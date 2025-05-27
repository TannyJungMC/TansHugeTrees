package tannyjung.tanshugetrees_handcode.systems.config;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.io.File;
import java.io.FileReader;

import net.minecraft.world.level.LevelAccessor;

import tannyjung.tanshugetrees.TanshugetreesMod;
import tannyjung.tanshugetrees_handcode.Handcode;
;
import tannyjung.tanshugetrees_handcode.misc.GameUtils;
import tannyjung.tanshugetrees_handcode.misc.OutsideUtils;

public class CheckUpdateRun {

    public static void run (LevelAccessor level) {

		if (OutsideUtils.isConnectedToInternet() == false) {

			GameUtils.sendChatMessage(level, "@a", "red", "THT : Can't check for update right now, as no internet connection.");

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

			File file = new File(Handcode.directory_config + "/custom_packs/TannyJung-Tree-Pack/version.txt");

			if (file.exists() == false) {

				file = new File(Handcode.directory_config + "/custom_packs/[INCOMPATIBLE] TannyJung-Tree-Pack/version.txt");

			}

			if (file.exists() == true && file.isDirectory() == false) {

				try {

					URL url_convert = new URI(url).toURL();
					int url_pack_version = 0;
					double url_data_structure_version = 0.0;
					int pack_version = 0;

					// Get Your Version
					{

						try { BufferedReader buffered_reader = new BufferedReader(new FileReader(file)); String read_all = ""; while ((read_all = buffered_reader.readLine()) != null) {

							{

								if (read_all.startsWith("pack_version = ")) {

									pack_version = Integer.parseInt(read_all.replace("pack_version = ", ""));

								}

							}

						} buffered_reader.close(); } catch (Exception e) { TanshugetreesMod.LOGGER.error(e.getMessage()); }

					}

					// Read File From GitHub
					{

						try { BufferedReader buffered_reader = new BufferedReader(new InputStreamReader(url_convert.openStream())); String read_all = ""; while ((read_all = buffered_reader.readLine()) != null) {

							{

								if (read_all.startsWith("data_structure_version = ")) {

									url_data_structure_version = Double.parseDouble(read_all.replace("data_structure_version = ", ""));

								} else if (read_all.startsWith("pack_version = ")) {

									url_pack_version = Integer.parseInt(read_all.replace("pack_version = ", ""));

								}

							}

						} buffered_reader.close(); } catch (Exception e) { TanshugetreesMod.LOGGER.error(e.getMessage()); }

					}

					if (pack_version == url_pack_version) {

						GameUtils.sendChatMessage(level, "@a", "gray", "THT : TannyJung's Tree Pack (" + Handcode.tanny_pack_version_name + ") is already up to date");

					} else {

						if (Handcode.data_structure_version_pack <= url_data_structure_version) {

							if (ConfigMain.auto_check_update == true) {

								if (ConfigMain.auto_update == true) {

									GameUtils.sendChatMessage(level, "@a", "gold", "THT : Detected new version for TannyJung's Tree Pack (" + Handcode.tanny_pack_version_name + "). Starting auto update...");
									UpdateRun.run(level);

								} else {

									GameUtils.runCommand(level, 0, 0, 0, "tellraw @a [{\"text\":\"THT : Detected new version for TannyJung's Tree Pack (" + Handcode.tanny_pack_version_name + "). You can manual update by follow the guide in \",\"color\":\"gold\"},{\"text\":\"GitHub\",\"color\":\"white\",\"underlined\":\"true\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"" + github + "\"},\"hoverEvent\":{\"action\":\"show_item\",\"contents\":{\"id\":\"minecraft:air\",\"count\":1,\"tag\":\"{display:{Name:'\\\"" + github + "\\\"'}}\"}}},{\"text\":\" or click \",\"color\":\"gold\"},{\"text\":\"[here]\",\"color\":\"white\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/TANSHUGETREES tanny_pack update\"}},{\"text\":\" to let the mod do it.\",\"color\":\"gold\"}]");

								}

							}

						} else {

							GameUtils.sendChatMessage(level, "@a", "red", "THT : Seems like you update the mod very fast! TannyJung's Tree Pack (" + Handcode.tanny_pack_version_name + ") haven't updated to support this mod version yet, please wait a bit for the update to available.");

						}

					}

				} catch (Exception e) {

					GameUtils.sendChatMessage(level, "@a", "red", "THT : Can't check the update right now, try again later.");

				}

			} else {

				GameUtils.runCommand(level, 0, 0, 0, "tellraw @a [{\"text\":\"THT : Not detected TannyJung's Tree Pack (" + Handcode.tanny_pack_version_name + ") in the config folder. You can manual install by follow the guide in \",\"color\":\"gold\"},{\"text\":\"GitHub\",\"color\":\"white\",\"underlined\":\"true\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"" + github + "\"},\"hoverEvent\":{\"action\":\"show_item\",\"contents\":{\"id\":\"minecraft:air\",\"count\":1,\"tag\":\"{display:{Name:'\\\"" + github + "\\\"'}}\"}}},{\"text\":\" or click \",\"color\":\"gold\"},{\"text\":\"[here]\",\"color\":\"white\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/TANSHUGETREES tanny_pack update\"}},{\"text\":\" to let the mod do it.\",\"color\":\"gold\"}]");

			}

		}

    }

}