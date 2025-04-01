package tannyjung.tanshugetrees_handcode.config;

import tannyjung.tanshugetrees_handcode.Handcode;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Comparator;

public class CustomPacksOrganizedClear {

    public static void start () {

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

}