package tannyjung.tanshugetrees_handcode.systems.world_gen;

import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import tannyjung.tanshugetrees.TanshugetreesMod;
import tannyjung.tanshugetrees_handcode.Handcode;
import tannyjung.tanshugetrees_handcode.misc.FileManager;
import tannyjung.tanshugetrees_handcode.misc.GameUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class DataFolderCleaner {

    public static void start (FeaturePlaceContext<NoneFeatureConfiguration> context) {

        String dimension = GameUtils.getCurrentDimensionID(context.level()).replace(":", "-");
        String name = (context.origin().getX() >> 9) + "," + (context.origin().getZ() >> 9);

        File file = new File(Handcode.directory_world_data + "/data_folder_cleaner/" + dimension + "/" + name + ".txt");
        FileManager.createFolder(file.getParent());

        // Write
        {

            StringBuilder write = new StringBuilder();

            {

                write.append("A");

            }

            FileManager.writeTXT(file.toPath().toString(), write.toString(), true);

        }

        boolean region_full = false;

        // Read to get text length
        {

            try { BufferedReader buffered_reader = new BufferedReader(new FileReader(file)); String read_all = ""; while ((read_all = buffered_reader.readLine()) != null) {

                {

                    if (read_all.length() >= 1024) {

                        region_full = true;

                    }

                }

            } buffered_reader.close(); } catch (Exception e) { TanshugetreesMod.LOGGER.error(e.getMessage()); }

        }

        if (region_full == true) {

            file.delete();
            new File(Handcode.directory_world_data + "/place/" + dimension + "/" + name).delete();
            new File(Handcode.directory_world_data + "/detailed_detection/" + dimension + "/" + name).delete();

        }

    }

}
