package tannyjung.tanshugetrees_handcode.config;

import net.minecraft.world.level.LevelAccessor;
import tannyjung.core.FileManager;
import tannyjung.core.OutsideUtils;
import tannyjung.tanshugetrees_handcode.Handcode;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Comparator;

public class CustomPackOrganized {

    public static void start (LevelAccessor level_accessor) {

        CustomPackIncompatible.scanMain(level_accessor);
        clearFolder();
        organizing();
        replace();
        CustomPackIncompatible.scanOrganized();

    }

    public static void clearFolder () {

        Path delete = Path.of(Handcode.directory_config + "/.dev/custom_packs_organized");

        try {

            Files.walk(delete).sorted(Comparator.reverseOrder()).forEach(source -> {

                try {

                    Files.delete(source);

                } catch (Exception exception) {

                    OutsideUtils.exception(new Exception(), exception);

                }

            });

        } catch (Exception exception) {

            OutsideUtils.exception(new Exception(), exception);

        }

    }

    private static void organizing () {

        File[] packs = new File(Handcode.directory_config + "/custom_packs").listFiles();
        File[] pack_files = new File[0];

        if (packs != null) {

            boolean copy = false;

            for (File pack : packs) {

                pack_files = pack.listFiles();

                if (pack_files != null) {

                    for (File category : pack_files) {

                        copy = false;

                        // Testing
                        {

                            if (category.getName().equals("functions") == true || category.getName().equals("leaf_litter") == true || category.getName().equals("presets") == true || category.getName().equals("world_gen") == true) {

                                copy = true;

                                // If incompatible, don't copy these folders.
                                {

                                    if (pack.getName().startsWith("[INCOMPATIBLE]") == true) {

                                        if (category.getName().equals("leaf_litter") == true) {

                                            copy = false;

                                        }

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

                                                String path = Handcode.directory_config + "/.dev/custom_packs_organized/" + category.getName();

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

                                                    OutsideUtils.exception(new Exception(), exception);

                                                }

                                            }

                                        }

                                    });

                                } catch (Exception exception) {

                                    OutsideUtils.exception(new Exception(), exception);

                                }

                            }

                        }

                    }

                }

            }

        }

    }

    private static void replace () {

        File[] packs = new File(Handcode.directory_config + "/custom_packs/").listFiles();

        if (packs != null) {

            for (File pack : packs) {

                if (pack.getName().startsWith("[INCOMPATIBLE]") == false) {

                    File replace = new File(Handcode.directory_config + "/custom_packs/" + pack.getName() + "/replace");

                    if (replace.exists() == true) {

                        // Scan
                        {

                            try {

                                Files.walk(replace.toPath()).forEach(source -> {

                                    if (source.toFile().isDirectory() == false) {

                                        try {

                                            replaceEachFile(pack, source);

                                        } catch (Exception exception) {

                                            OutsideUtils.exception(new Exception(), exception);

                                        }

                                    }

                                });

                            } catch (Exception exception) {

                                OutsideUtils.exception(new Exception(), exception);

                            }

                        }

                    }

                }

            }

        }

    }

    private static void replaceEachFile (File pack, Path source) {

        String[] data_new = FileManager.fileToStringArray(source.toString());
        boolean specific = false;

        // Get Mode
        {

            for (String read_all : data_new) {

                if (read_all.equals("# SPECIFIC") == true) {

                    specific = true;

                }

                break;

            }

        }

        File file = Path.of(Handcode.directory_config + "/.dev/custom_packs_organized").resolve(Path.of(pack.toPath() + "/replace").relativize(source)).toFile();
        String[] data_old = FileManager.fileToStringArray(file.getPath());
        String[] data = new String[0];

        if (specific == false) {

            data = data_new;

        } else {

            if (data_old.length == 0) {

                return;

            } else {

                data = data_old;
                int line = 0;
                String name = "";

                for (String read_all : data_new) {

                    if (read_all.equals("") == false) {

                        if (read_all.contains(" = ") == true) {

                            name = read_all.substring(0, read_all.indexOf(" = ") + 3);

                            for (String read_all_old : data) {

                                if (read_all_old.startsWith(name) == true) {

                                    data[line] = read_all;
                                    break;

                                }

                                line = line + 1;

                            }

                        }

                    }

                }

            }

        }

        FileManager.writeTXT(file.getPath(), String.join("\n", data), false);

    }

}