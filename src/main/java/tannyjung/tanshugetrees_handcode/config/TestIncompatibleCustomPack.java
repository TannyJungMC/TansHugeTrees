package tannyjung.tanshugetrees_handcode.config;

import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
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

    public static void start () {

        // Scan Main Folder
        {

            File file = new File(Handcode.directory_config + "/custom_packs");

            for (File pack : file.listFiles()) {

                if (pack.getName().equals(".organized") == false) {

                    testVersion(new File(pack.toPath() + "/version.txt").toPath());

                }

            }

        }

        // Scan Organized Folder
        {

            File file = new File(Handcode.directory_config + "/custom_packs/.organized");

            {

                try {

                    Files.walk(file.toPath()).forEach(source -> {

                        if (source.toFile().isDirectory() == false) {

                            if (source.getParent().getParent().toFile().getName().equals("world_gen") == true) {

                                testTreeSettings(source);

                            }

                        }

                    });

                } catch (Exception e) {

                    e.printStackTrace();

                }

            }

        }

    }

    private static void testVersion (Path source) {

        File file = new File(source.toFile().getPath());

        if (file.exists() == true && file.isDirectory() == false) {

            {

                try { BufferedReader buffered_reader = new BufferedReader(new FileReader(file)); String read_all = ""; while ((read_all = buffered_reader.readLine()) != null) {

                    {

                        if (read_all.startsWith("mod_version = ")) {

                            int version = Integer.parseInt(read_all.replace("mod_version = ", ""));

                            if (version != Handcode.mod_version) {

                                rename(new File(source.getParent().getParent().toFile().getPath() + "/.organized/" + source.getParent().toFile().getName()));
                                break;

                            }

                        }

                    }

                } buffered_reader.close(); } catch (Exception e) { e.printStackTrace(); }

            }

        } else {

            rename(new File(source.getParent().getParent().toFile().getPath() + "/.organized/" + source.getParent().toFile().getName()));

        }

    }

    private static void testTreeSettings (Path source) {

        String tree_settings = "";

        // Read "World Gen" File
        {

            try { BufferedReader buffered_reader = new BufferedReader(new FileReader(source.toFile())); String read_all = ""; while ((read_all = buffered_reader.readLine()) != null) {

                {

                    if (read_all.startsWith("tree_settings = ")) {

                        tree_settings = read_all.replace("tree_settings = ", "");
                        break;

                    }

                }

            } buffered_reader.close(); } catch (Exception e) { e.printStackTrace(); }

        }

        File file = new File(Handcode.directory_config + "/custom_packs/.organized/" + tree_settings);

        if (file.exists() == true && file.isDirectory() == false) {

            // Read "Tree Settings" File
            {

                try { BufferedReader buffered_reader = new BufferedReader(new FileReader(file)); String read_all = ""; while ((read_all = buffered_reader.readLine()) != null) {

                    {

                        if (read_all.startsWith("Block ")) {

                            String id = read_all.substring(read_all.indexOf(" = ") + 3).replace(" keep", "");

                            if (id.equals("") == false) {

                                System.out.println(read_all + "   >   " + id + "   >   " + Misc.textToBlock(id).getBlock());

                                if (Misc.textToBlock(id).getBlock() == Blocks.AIR) {

                                    rename(source.toFile());
                                    break;

                                }

                            }

                        }

                    }

                } buffered_reader.close(); } catch (Exception e) { e.printStackTrace(); }

            }

        } else {

            rename(source.toFile());

        }

    }

    private static void rename (File file) {

        if (file.getName().startsWith("[INCOMPATIBLE]") == false) {

            file.renameTo(new File(file.getParentFile().toPath() + "/[INCOMPATIBLE] " + file.getName()));

        }

    }

}