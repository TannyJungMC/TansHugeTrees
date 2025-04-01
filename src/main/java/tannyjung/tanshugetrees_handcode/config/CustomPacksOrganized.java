package tannyjung.tanshugetrees_handcode.config;

import net.minecraftforge.fml.loading.FMLPaths;
import tannyjung.tanshugetrees.TanshugetreesMod;
import tannyjung.tanshugetrees_handcode.Handcode;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Comparator;

public class CustomPacksOrganized {

    public static void start () {

        clearFolder();
        organizing();
        replace();

    }

    public static void clearFolder () {

        Path delete = Path.of(Handcode.directory_config + "/custom_packs/.organized");

        try {

            Files.walk(delete).sorted(Comparator.reverseOrder()).forEach(source -> {

                if (source.toFile().getName().equals(".organized") == false) {

                    try {

                        Files.delete(source);

                    } catch (Exception e) {

                        e.printStackTrace();

                    }

                }

            });

        } catch (Exception e) {

            e.printStackTrace();

        }

    }

    private static void organizing () {

        for (File pack : new File(Handcode.directory_config + "/custom_packs").listFiles()) {

            // Make it scan the main pack first
            {

                if (pack.getName().equals(".organized") == true) {

                    pack = new File(Handcode.directory_config + "/custom_packs/THT-tree_pack-main");

                } else {

                    if (pack.getName().equals("THT-tree_pack-main") == true) {

                        continue;

                    }

                }

            }

            if (pack.exists() == true) {

                for (File category : pack.listFiles()) {

                    // Only These Folders
                    if (category.getName().equals("functions") == true || category.getName().equals("leaf_litter") == true || category.getName().equals("presets") == true || category.getName().equals("world_gen") == true) {

                        // Copying
                        {

                            try {

                                File pack_final = pack;

                                Files.walk(category.toPath()).forEach(source -> {

                                    if (source.toFile().isDirectory() == false) {

                                        // Not in Storage Folder
                                        if (source.getParent().toFile().getName().equals("storage") == false) {

                                            String path = Handcode.directory_config + "/custom_packs/.organized/" + category.getName();

                                            // With Pack Name
                                            if (category.getName().equals("leaf_litter") == false) {

                                                path = path + "/" + pack_final.getName();

                                            }

                                            try {

                                                Path to = Path.of(path);
                                                to = to.resolve(category.toPath().relativize(source));
                                                Files.createDirectories(to.getParent());
                                                Files.copy(source, to, StandardCopyOption.REPLACE_EXISTING);

                                            } catch (Exception e) {

                                                e.printStackTrace();

                                            }

                                        }

                                    }

                                });

                            } catch (Exception e) {

                                e.printStackTrace();

                            }

                        }

                    }

                }

            }

        }

    }

    private static void replace () {

        for (File pack : new File(Handcode.directory_config + "/custom_packs/").listFiles()) {

            if (pack.getName().equals(".organized") == false) {

                File replace = new File(Handcode.directory_config + "/custom_packs/" + pack.getName() + "/replace");

                if (replace.exists() == true) {

                    // Copying
                    {

                        try {

                            Files.walk(replace.toPath()).forEach(source -> {

                                if (source.toFile().isDirectory() == false) {

                                    try {

                                        Path from = Path.of(pack.toPath() + "/replace");
                                        Path to = Path.of(Handcode.directory_config + "/custom_packs/.organized");

                                        Path copy = to.resolve(from.relativize(source));
                                        Files.createDirectories(copy.getParent());
                                        Files.copy(source, copy, StandardCopyOption.REPLACE_EXISTING);

                                    } catch (Exception e) {

                                        e.printStackTrace();

                                    }

                                }

                            });

                        } catch (Exception e) {

                            e.printStackTrace();

                        }

                    }

                }

            }

        }

    }

}