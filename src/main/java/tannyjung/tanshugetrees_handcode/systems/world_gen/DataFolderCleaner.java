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

    public static void run (FeaturePlaceContext<NoneFeatureConfiguration> context) {

        String dimension = GameUtils.getCurrentDimensionID(context.level().getLevel()).replace(":", "-");
        int chunk_posX = context.origin().getX() >> 4;
        int chunk_posZ = context.origin().getZ() >> 4;
        String name = (chunk_posX >> 5) + "," + (chunk_posZ >> 5) + "/" + FileManager.quardtreeChunkToNode(chunk_posX, chunk_posZ) + ".txt";

        File file = new File(Handcode.directory_world_data + "/data_folder_cleaner/" + dimension + "/" + name);

        // Write
        {

            StringBuilder write = new StringBuilder();

            {

                write.append("A");

            }

            FileManager.writeTXT(file.toPath().toString(), write.toString(), true);

        }

        boolean full = false;

        // Read to get text length
        {

            try { BufferedReader buffered_reader = new BufferedReader(new FileReader(file)); String read_all = ""; while ((read_all = buffered_reader.readLine()) != null) {

                {

                    if (read_all.length() >= Math.pow(32 >> Handcode.quadtree_level, 2)) {

                        full = true;

                    }

                }

            } buffered_reader.close(); } catch (Exception e) { TanshugetreesMod.LOGGER.error(e.getMessage()); }

        }

        if (full == true) {

            file.delete();
            new File(Handcode.directory_world_data + "/place/" + dimension + "/" + name).delete();
            new File(Handcode.directory_world_data + "/detailed_detection/" + dimension + "/" + name).delete();

        }

    }

}
