package tannyjung.tanshugetrees_handcode.config;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.io.File;
import java.io.FileReader;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;

import tannyjung.tanshugetrees.TanshugetreesMod;
import tannyjung.tanshugetrees.procedures.SendChatMessageProcedure;
import tannyjung.tanshugetrees_handcode.Handcode;
import tannyjung.tanshugetrees_handcode.misc.Misc;

public class CheckUpdateRun {

    public static void start (LevelAccessor level, double x, double y, double z) {

		String url = "";

		if (ConfigMain.wip_version == false) {
			
        	url = "https://raw.githubusercontent.com/TannyJungMC/THT-tree_pack/" + Handcode.tanny_pack_version + "/version.txt";

		} else {

			url = "https://raw.githubusercontent.com/TannyJungMC/THT-tree_pack/wip/version.txt";
			
		}

        File file = new File(Handcode.directory_config + "/custom_packs/THT-tree_pack-main/version.txt");
		int pack_version = 0;


        if (file.exists() == true) {

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

							if (read_all.startsWith("pack_version : ")) {

								url_pack_version = Integer.parseInt(read_all.replace("pack_version : ", ""));

							}

						}

					} buffered_reader.close(); } catch (Exception e) { e.printStackTrace(); }

				}

				if (pack_version == url_pack_version) {

					SendChatMessageProcedure.execute(level, "green", "@a", "THT : TannyJung's Tree Pack is already up to date");
	
	            } else {

					TanshugetreesMod.LOGGER.debug("Pack Version : " + url_pack_version);
					TanshugetreesMod.LOGGER.debug("Your Pack Version : " + pack_version);
	
					if (ConfigMain.auto_check_update == true && ConfigMain.auto_update == true) {

						SendChatMessageProcedure.execute(level, "gold", "@a", "THT : Detected new version for TannyJung's Tree Pack. Starting auto update...");
						UpdateRun.start(level, x, y, z);

					} else {

						SendChatMessageProcedure.execute(level, "gold", "@a", "THT : Detected new version for TannyJung's Tree Pack. Starting auto update...");
						Misc.runCommand(level, 0, 0, 0, "tellraw @a [{\"text\":\"THT : Detected new version for TannyJung's Tree Pack. You can manual update by follow the guide in \",\"color\":\"gold\"},{\"text\":\"GitHub\",\"color\":\"white\",\"underlined\":\"true\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"https://github.com/TannyJungMC/THT-tree_pack\"},\"hoverEvent\":{\"action\":\"show_item\",\"contents\":{\"id\":\"minecraft:air\",\"count\":1,\"tag\":\"{display:{Name:'\\\"https://github.com/TannyJungMC/THT-tree_pack\\\"'}}\"}}},{\"text\":\" or click \",\"color\":\"gold\"},{\"text\":\"[here]\",\"color\":\"white\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/THT tanny_pack update\"}},{\"text\":\" to let the mod do it.\",\"color\":\"gold\"}]");

					}
	                
	            }
	            
	        } catch (Exception e) {

				SendChatMessageProcedure.execute(level, "red", "@a", "THT : Can't check the update right now, try again later.");

            }

        } else {

			Misc.runCommand(level, 0, 0, 0, "tellraw @a [{\"text\":\"THT : Not detected TannyJung's Tree Pack in the config folder. You can manual download and install by follow the guide in \",\"color\":\"gold\"},{\"text\":\"GitHub\",\"color\":\"white\",\"underlined\":\"true\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"https://github.com/TannyJungMC/THT-tree_pack\"},\"hoverEvent\":{\"action\":\"show_item\",\"contents\":{\"id\":\"minecraft:air\",\"count\":1,\"tag\":\"{display:{Name:'\\\"https://github.com/TannyJungMC/THT-tree_pack\\\"'}}\"}}},{\"text\":\" or click \",\"color\":\"gold\"},{\"text\":\"[here]\",\"color\":\"white\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/THT tanny_pack update\"}},{\"text\":\" to let the mod do it.\",\"color\":\"gold\"}]");

        }

    }

}