package tannyjung.tanshugetrees_handcode.config;

import net.minecraftforge.fml.loading.FMLPaths;

import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;

public class SendMessageWhenUpdate {

    public static void start (LevelAccessor level, double x, double y, double z) {
		
			

		// ----------------------------------------------------------------------------------------------------
		// Custom
		// ----------------------------------------------------------------------------------------------------

		

		String file_directory = FMLPaths.GAMEDIR.get().toString() + "/config/tanshugetrees/custom_packs/THT-tree_pack-main/message.txt";



		// ----------------------------------------------------------------------------------------------------
		// Variables
		// ----------------------------------------------------------------------------------------------------

		


		File file = new File(file_directory);

        BufferedReader buffered_reader;

        String read_all = "";
        String read = "";



		// ----------------------------------------------------------------------------------------------------
		// End
		// ----------------------------------------------------------------------------------------------------

		
		if (file.exists() == true) {

            try {

                buffered_reader = new BufferedReader(new FileReader(file));

                while ((read = buffered_reader.readLine()) != null) {

                    if (read.equals("") == false) {

                        read_all = read_all + read;

                    }

                }
                buffered_reader.close();

                if (level instanceof ServerLevel _level) {

                    _level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(), (read_all));

                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

}