package tannyjung.tanshugetrees_handcode.config;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.LevelAccessor;
import tannyjung.core.OutsideUtils;
import tannyjung.tanshugetrees_handcode.Handcode;
import tannyjung.core.GameUtils;

import java.io.File;
import java.nio.file.Files;

public class CustomPackFileCount {

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

            file_size = Double.parseDouble(String.format("%.2f", file_size / (1024.0 * 1024.0)));

            GameUtils.command.run(level_server, 0, 0, 0, "tellraw @a [{\"text\":\"There are now \",\"color\":\"white\"},{\"text\":\"" + count_variation + "\",\"color\":\"yellow\"},{\"text\":\" variation of species from all installed packs, and \",\"color\":\"white\"},{\"text\":\"" + count_shape + "\",\"color\":\"yellow\"},{\"text\":\" different shapes in total! Used about \",\"color\":\"white\"},{\"text\":\"" + String.format("%.1f", file_size) + " MB\",\"color\":\"yellow\"},{\"text\":\" of the space.\",\"color\":\"white\"}]");

        }

    }

    private static void fileSize () {

        File file = new File(Handcode.directory_config + "/custom_packs");

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

        File file = new File(Handcode.directory_config + "/.dev/custom_packs_organized/world_gen");

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

        File file = new File(Handcode.directory_config + "/custom_packs/");

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