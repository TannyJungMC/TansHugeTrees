package tannyjung.tanshugetrees_handcode.config;

import net.minecraft.world.level.LevelAccessor;
import tannyjung.misc.MiscUtils;
import tannyjung.tanshugetrees.TanshugetreesMod;
import tannyjung.tanshugetrees_handcode.Handcode;
import tannyjung.misc.GameUtils;
;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class PackMessage {

    public static void start (LevelAccessor level) {

		File file = new File(Handcode.directory_config + "/custom_packs/TannyJung-Tree-Pack/message.txt");
		StringBuilder message = new StringBuilder();

		if (file.exists() == true && file.isDirectory() == false) {

            {

                try { BufferedReader buffered_reader = new BufferedReader(new FileReader(file)); String read_all = ""; while ((read_all = buffered_reader.readLine()) != null) {

                    {

                        message.append(read_all);

                    }

                } buffered_reader.close(); } catch (Exception exception) { MiscUtils.exception(exception); }

            }

            GameUtils.command.run(level, 0, 0, 0, message.toString());

        }

    }

}