package tannyjung.tanshugetrees_handcode.config;

import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fml.loading.FMLPaths;
import tannyjung.tanshugetrees.TanshugetreesMod;
import tannyjung.tanshugetrees_handcode.Handcode;
import tannyjung.tanshugetrees_handcode.misc.Misc;

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

                } buffered_reader.close(); } catch (Exception e) { TanshugetreesMod.LOGGER.error(e.getMessage()); }

            }

            Misc.runCommand(level, 0, 0, 0, message.toString());

        }

    }

}