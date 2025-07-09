package tannyjung.tanshugetrees_handcode.config;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.LevelAccessor;
import tannyjung.core.MiscUtils;
import tannyjung.tanshugetrees_handcode.Handcode;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Comparator;

public class CustomPackOrganized {

    public static void start (LevelAccessor level_accessor) {

        if (level_accessor instanceof ServerLevel level_server) {

            clearFolder();
            CustomPackIncompatible.scanMain(level_server);
            organizing();
            replace();
            CustomPackIncompatible.scanOrganized(level_server);

        }

    }

    public static void clearFolder () {

        Path delete = Path.of(Handcode.directory_config + "/custom_packs/.organized");

        try {

            Files.walk(delete).sorted(Comparator.reverseOrder()).forEach(source -> {

                if (source.toFile().getName().equals(".organized") == false) {

                    try {

                        Files.delete(source);

                    } catch (Exception exception) {

                        MiscUtils.exception(new Exception(), exception);

                    }

                }

            });

        } catch (Exception exception) {

            MiscUtils.exception(new Exception(), exception);

        }

    }

    private static void organizing () {

        boolean copy = false;

        for (File pack : new File(Handcode.directory_config + "/custom_packs").listFiles()) {

            // Make it scan the main pack first
            {

                if (pack.getName().equals(".organized") == true) {

                    pack = new File(Handcode.directory_config + "/custom_packs/TannyJung-Tree-Pack");

                } else {

                    if (pack.getName().equals("TannyJung-Tree-Pack") == true) {

                        continue;

                    }

                }

            }

            if (pack.exists() == true) {

                for (File category : pack.listFiles()) {

                    copy = false;

                    // Testing
                    {

                        if (category.getName().equals("functions") == true || category.getName().equals("leaf_litter") == true || category.getName().equals("presets") == true || category.getName().equals("world_gen") == true) {

                            copy = true;

                            // If incompatible, don't copy these folders.
                            if (pack.getName().startsWith("[INCOMPATIBLE]") == true) {

                                if (category.getName().equals("leaf_litter") == true) {

                                    copy = false;

                                }

                            }

                        }

                    }

                    if (copy == true) {

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

                                            } catch (Exception exception) {

                                                MiscUtils.exception(new Exception(), exception);

                                            }

                                        }

                                    }

                                });

                            } catch (Exception exception) {

                                MiscUtils.exception(new Exception(), exception);

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

                if (pack.getName().startsWith("[INCOMPATIBLE]") == false) {

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

                                        } catch (Exception exception) {

                                            MiscUtils.exception(new Exception(), exception);

                                        }

                                    }

                                });

                            } catch (Exception exception) {

                                MiscUtils.exception(new Exception(), exception);

                            }

                        }

                    }

                }

            }

        }

    }

}