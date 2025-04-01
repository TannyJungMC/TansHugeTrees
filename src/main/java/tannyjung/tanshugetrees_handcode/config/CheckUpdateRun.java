package tannyjung.tanshugetrees_handcode.config;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.io.File;
import java.io.FileReader;

import net.minecraft.world.level.LevelAccessor;

import tannyjung.tanshugetrees_handcode.Handcode;
import tannyjung.tanshugetrees_handcode.misc.Misc;
import tannyjung.tanshugetrees_handcode.misc.MiscOutside;

public class CheckUpdateRun {

    public static void start (LevelAccessor level) {

		if (MiscOutside.isConnectedToInternet() == false) {

			Misc.sendChatMessage(level, "@a", "red", "THT : Can't check for update right now, as no internet connection.");

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
			File file_incompatible = new File(Handcode.directory_config + "/custom_packs/[INCOMPATIBLE] TannyJung-Tree-Pack/version.txt");
			int pack_version = 0;

			if ((file.exists() == true && file.isDirectory() == false) || (file_incompatible.exists() == true && file_incompatible.isDirectory() == false)) {

				// Get Your Pack Version
				{

					try { BufferedReader buffered_reader = new BufferedReader(new FileReader(file)); String read_all = ""; while ((read_all = buffered_reader.readLine()) != null) {

						{

							if (read_all.startsWith("pack_version = ")) {

								pack_version = Integer.parseInt(read_all.replace("pack_version = ", ""));
								break;

							}

						}

					} buffered_reader.close(); } catch (Exception e) { e.printStackTrace(); }

				}

				try {

					URL url_convert = new URI(url).toURL();
					int url_pack_version = 0;

					// Read File From GitHub
					{

						try { BufferedReader buffered_reader = new BufferedReader(new InputStreamReader(url_convert.openStream())); String read_all = ""; while ((read_all = buffered_reader.readLine()) != null) {

							{

								if (read_all.startsWith("pack_version = ")) {

									url_pack_version = Integer.parseInt(read_all.replace("pack_version = ", ""));

								}

							}

						} buffered_reader.close(); } catch (Exception e) { e.printStackTrace(); }

					}

					if (pack_version == url_pack_version) {

						Misc.sendChatMessage(level, "@a", "gray", "THT : TannyJung's Tree Pack (" + Handcode.tanny_pack_version_name + ") is already up to date");

					} else {

						if (ConfigMain.auto_check_update == true && ConfigMain.auto_update == true) {

							Misc.sendChatMessage(level, "@a", "gold", "THT : Detected new version for TannyJung's Tree Pack (" + Handcode.tanny_pack_version_name + "). Starting auto update...");
							UpdateRun.start(level);

						} else {

							Misc.runCommand(level, 0, 0, 0, "tellraw @a [{\"text\":\"THT : Detected new version for TannyJung's Tree Pack (" + Handcode.tanny_pack_version_name + "). You can manual update by follow the guide in \",\"color\":\"gold\"},{\"text\":\"GitHub\",\"color\":\"white\",\"underlined\":\"true\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"" + github + "\"},\"hoverEvent\":{\"action\":\"show_item\",\"contents\":{\"id\":\"minecraft:air\",\"count\":1,\"tag\":\"{display:{Name:'\\\"" + github + "\\\"'}}\"}}},{\"text\":\" or click \",\"color\":\"gold\"},{\"text\":\"[here]\",\"color\":\"white\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/tanshugetrees tanny_pack update\"}},{\"text\":\" to let the mod do it.\",\"color\":\"gold\"}]");

						}

					}

				} catch (Exception e) {

					Misc.sendChatMessage(level, "@a", "red", "THT : Can't check the update right now, try again later.");

				}

			} else {

				Misc.runCommand(level, 0, 0, 0, "tellraw @a [{\"text\":\"THT : Not detected TannyJung's Tree Pack (" + Handcode.tanny_pack_version_name + ") in the custom packs folder. You can manual download and install by follow the guide in \",\"color\":\"gold\"},{\"text\":\"GitHub\",\"color\":\"white\",\"underlined\":\"true\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"" + github + "\"},\"hoverEvent\":{\"action\":\"show_item\",\"contents\":{\"id\":\"minecraft:air\",\"count\":1,\"tag\":\"{display:{Name:'\\\"" + github + "\\\"'}}\"}}},{\"text\":\" or click \",\"color\":\"gold\"},{\"text\":\"[here]\",\"color\":\"white\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/tanshugetrees tanny_pack update\"}},{\"text\":\" to let the mod do it.\",\"color\":\"gold\"}]");

			}

		}

    }

}