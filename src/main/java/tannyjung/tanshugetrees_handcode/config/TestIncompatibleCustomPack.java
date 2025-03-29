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
import tannyjung.tanshugetrees_handcode.Handcode;
import tannyjung.tanshugetrees_handcode.misc.Misc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.concurrent.CompletableFuture;

public class TestIncompatibleCustomPack {

    public static void start (LevelAccessor level) {

        test(level);

    }

    public static void test (LevelAccessor level) {

        File file = new File(Handcode.directory_config + "/custom_packs");

        if (file.exists() == true) {

            for (File pack : file.listFiles()) {

                if (pack.isDirectory() == true && pack.getName().equals(".organized") == false) {

                    File file_version = new File(Handcode.directory_config + "/custom_packs/" + pack.getName() + "/version.txt");
                    boolean same_version = false;

                    if (file_version.exists() == true && file_version.isDirectory() == false) {

                        try { BufferedReader buffered_reader = new BufferedReader(new FileReader(file_version)); String read_all = ""; while ((read_all = buffered_reader.readLine()) != null) {

                            {

                                if (read_all.startsWith("mod_version = ")) {

                                    same_version = Handcode.mod_version == Integer.parseInt(read_all.replace("mod_version = ", ""));

                                }

                            }

                        } buffered_reader.close(); } catch (Exception e) { e.printStackTrace(); }

                    }

                    if (same_version == true) {

                        if (pack.getName().startsWith("[INCOMPATIBLE]") == true) {

                            pack.renameTo(new File(Handcode.directory_config + "/custom_packs/" + pack.getName().replace("[INCOMPATIBLE] ", "")));

                        }

                    } else {

                        Misc.runCommand(level, 0, 0, 0, "tellraw @a [{\"text\":\"THT : Detected Incompatible Pack! \",\"color\":\"red\"},{\"text\":\"" + pack.getName().replace("[INCOMPATIBLE] ", "") + "\",\"color\":\"white\"}]");

                        if (pack.getName().startsWith("[INCOMPATIBLE]") == false) {

                            pack.renameTo(new File(Handcode.directory_config + "/custom_packs/[INCOMPATIBLE] " + pack.getName()));

                        }

                    }

                }

            }

        }

    }

}