package tannyjung.tanshugetrees_handcode.data;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.LevelAccessor;
import tannyjung.core.FileManager;
import tannyjung.core.OutsideUtils;
import tannyjung.core.game.GameUtils;
import tannyjung.tanshugetrees_handcode.Handcode;
import tannyjung.tanshugetrees_handcode.config.*;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Comparator;

public class DataRepair {

    public static void start (LevelAccessor level_accessor) {

        FileManager.createFolder(Handcode.path_config + "/#dev");
        FileManager.createFolder(Handcode.path_config + "/#dev/custom_packs_organized");
        FileManager.createFolder(Handcode.path_config + "/#dev/shape_file_converter");
        FileManager.createFolder(Handcode.path_config + "/custom_packs");

        DataMigration.start();

        custom_pack_organizing.start(level_accessor);
        FileConfig.repair();
        FileConfigWorldGen.start();
        FileShapeConverter.start();

        FileConfig.apply();

    }

    public static class custom_pack_organizing {

        public static void start (LevelAccessor level_accessor) {

            CustomPackIncompatible.scanMain(level_accessor);
            clearFolder();
            organizing();
            replace();
            CustomPackIncompatible.scanWorldGenFile();

        }

        public static void clearFolder () {

            Path delete = Path.of(Handcode.path_config + "/#dev/custom_packs_organized");

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

            File[] packs = new File(Handcode.path_config + "/custom_packs").listFiles();
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

                                                    String path = Handcode.path_config + "/#dev/custom_packs_organized/" + category.getName();

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

            File[] packs = new File(Handcode.path_config + "/custom_packs/").listFiles();

            if (packs != null) {

                for (File pack : packs) {

                    if (pack.getName().startsWith("[INCOMPATIBLE]") == false) {

                        File replace = new File(Handcode.path_config + "/custom_packs/" + pack.getName() + "/replace");

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

            String[] data_new = FileManager.readTXT(source.toString());
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

            File file = Path.of(Handcode.path_config + "/#dev/custom_packs_organized").resolve(Path.of(pack.toPath() + "/replace").relativize(source)).toFile();
            String[] data_old = FileManager.readTXT(file.getPath());
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

    public static class custom_pack_file_count {

        private static double file_size = 0.0;
        private static int count_shape = 0;
        private static int count_variation = 0;

        public static void start (LevelAccessor level_accessor) {

            if (level_accessor instanceof ServerLevel level_server) {

                file_size = 0.0;
                count_shape = 0;
                count_variation = 0;

                fileSize();
                countShape();
                countVariation();

                file_size = Double.parseDouble(String.format("%.2f", file_size / (1024 * 1024)));
                GameUtils.command.run(level_server, 0, 0, 0, "tellraw @a [{\"text\":\"There are now \",\"color\":\"white\"},{\"text\":\"" + count_variation + "\",\"color\":\"yellow\"},{\"text\":\" variation of species from all installed packs, and \",\"color\":\"white\"},{\"text\":\"" + count_shape + "\",\"color\":\"yellow\"},{\"text\":\" different shapes in total! Used about \",\"color\":\"white\"},{\"text\":\"" + file_size + " MB\",\"color\":\"yellow\"},{\"text\":\" of the space.\",\"color\":\"white\"}]");

            }

        }

        private static void fileSize () {

            File file = new File(Handcode.path_config + "/custom_packs");

            if (file.exists() == true) {

                {

                    try {

                        Files.walk(file.toPath()).forEach(source -> {

                            if (source.toFile().isDirectory() == false) {

                                file_size = file_size + source.toFile().length();

                            }

                        });

                    } catch (Exception exception) {

                        OutsideUtils.exception(new Exception(), exception);

                    }

                }

            }

        }

        private static void countVariation () {

            File file = new File(Handcode.path_config + "/#dev/custom_packs_organized/world_gen");

            if (file.exists() == true) {

                {

                    try {

                        Files.walk(file.toPath()).forEach(source -> {

                            if (source.toFile().isDirectory() == false) {

                                count_variation = count_variation + 1;

                            }

                        });

                    } catch (Exception exception) {

                        OutsideUtils.exception(new Exception(), exception);

                    }

                }

            }

        }

        private static void countShape () {

            File file = new File(Handcode.path_config + "/custom_packs/");

            if (file.exists() == true) {

                {

                    try {

                        Files.walk(file.toPath()).forEach(source -> {

                            if (source.toFile().isDirectory() == false && source.toString().contains("\\storage\\") == true) {

                                count_shape = count_shape + 1;

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
