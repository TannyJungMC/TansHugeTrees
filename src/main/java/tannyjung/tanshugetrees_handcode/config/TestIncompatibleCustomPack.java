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
import tannyjung.tanshugetrees.network.TanshugetreesModVariables;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.concurrent.CompletableFuture;

public class TestIncompatibleCustomPack {

    

    // ----------------------------------------------------------------------------------------------------
    // Custom
    // ----------------------------------------------------------------------------------------------------

    

    //    FMLPaths.GAMEDIR.get().toString();
    //    "C:/Users/acer/Desktop/Minecraft Projects/Mod MCreator/THT/THT/run";
    static String game_directory = FMLPaths.GAMEDIR.get().toString();

    static String directory_pack = game_directory + "/config/tanshugetrees/custom_packs";



    // ----------------------------------------------------------------------------------------------------
    // Class Global Variables
    // ----------------------------------------------------------------------------------------------------



    static LevelAccessor world;
    static double x = 0;
    static double y = 0;
    static double z = 0;

    

    // ----------------------------------------------------------------------------------------------------
    // Run System
    // ----------------------------------------------------------------------------------------------------



    public static void start (LevelAccessor import_world, double import_x, double import_y, double import_z) throws Exception {

        world = import_world;
        x = import_x;
        y = import_y;
        z = import_z;

        CompletableFuture.runAsync(() -> {

            try {

                test();

            }  catch (Exception e) {

                TanshugetreesMod.LOGGER.error(e);
                
            }

        });

    }



    // ----------------------------------------------------------------------------------------------------
    // Test
    // ----------------------------------------------------------------------------------------------------



    public static void test () throws Exception {

        File directory = new File(directory_pack);
        File [] list = directory.listFiles();

        if (directory.exists() == true) {

            if (list != null) {

                for (int i = 0; i < list.length; i++) {

                    if (
                            list[i].isDirectory() == true
                            &&
                            list[i].getName().equals(".organized") == false
                    ) {

                        File version_file = new File(directory_pack + "/" + list[i].getName() + "/version.txt");

                        if (version_file.exists() == true) {

                            BufferedReader buffered_reader = new BufferedReader(new FileReader(version_file));
                            String read_all = "";
                            boolean test = false;

                            while ((read_all = buffered_reader.readLine()) != null) {

                                if (read_all.startsWith("mod_version : ")) {

                                    String version = read_all.replace("mod_version : ", "");

                                    if (TanshugetreesModVariables.MapVariables.get(world).mod_version.equals(version) == false) {   //  TanshugetreesModVariables.MapVariables.get(world).mod_version

                                        test = false;

                                        String command = "tellraw @a [{\"text\":\"THT : Detected Incompatible Pack! \",\"color\":\"red\"},{\"text\":\"" + list[i].getName().replace("[INCOMPATIBLE] ", "") + "\",\"color\":\"white\"}]";

                                        if (world instanceof ServerLevel _level) {
                                            _level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL,new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),(command));
                                        }

                                    } else {

                                        test = true;

                                    }

                                }

                            } buffered_reader.close();

                            if (test == false) {

                                if (list[i].getName().startsWith("[INCOMPATIBLE] ") == false) {

                                    File rename_from = new File(directory_pack + "/" + list[i].getName());
                                    File rename_to = new File(directory_pack + "/[INCOMPATIBLE] " + list[i].getName());

                                    rename_from.renameTo(rename_to);

                                }

                            } else {

                                if (list[i].getName().startsWith("[INCOMPATIBLE] ") == true) {

                                    File rename_from = new File(directory_pack + "/" + list[i].getName());
                                    File rename_to = new File(directory_pack + "/" + list[i].getName().replace("[INCOMPATIBLE] ", ""));

                                    rename_from.renameTo(rename_to);

                                }

                            }

                        }

                    }

                }

            }

        } else {

            TanshugetreesMod.LOGGER.error("Error to test files from the directory (Not Found Directory Folder)");

        }

    }

}