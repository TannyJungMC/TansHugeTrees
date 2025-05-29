package tannyjung.tanshugetrees_handcode.systems.world_gen;

import net.minecraft.world.level.ChunkPos;
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

    public static void run (String dimension, ChunkPos chunk_pos) {

        String region = (chunk_pos.x >> 5) + "," + (chunk_pos.z >> 5);
        String node = FileManager.quardtreeChunkToNode(chunk_pos.x, chunk_pos.z);
        String file_path = dimension + "/" + region + "/" + node + ".txt";

        boolean full_node = false;
        File file_region = new File(Handcode.directory_world_data + "/regions/" + file_path);
        FileManager.writeTXT(file_region.toPath().toString(), "A", true);

        // Read to get text length
        {

            try { BufferedReader buffered_reader = new BufferedReader(new FileReader(file_region)); String read_all = ""; while ((read_all = buffered_reader.readLine()) != null) {

                {

                    if (read_all.length() >= Math.pow(32 >> Handcode.quadtree_level, 2)) {

                        full_node = true;

                    }

                }

            } buffered_reader.close(); } catch (Exception e) { TanshugetreesMod.LOGGER.error(e.getMessage()); }

        }

        if (full_node == true) {

            File file_place = new File(Handcode.directory_world_data + "/place/" + file_path);
            File file_detailed_detection = new File(Handcode.directory_world_data + "/detailed_detection/" + file_path);

            file_region.delete();
            file_place.delete();
            file_detailed_detection.delete();

            // If finish placing all chunks in region
            if (false && file_place.getParentFile().listFiles() == null) {

                // file_region.getParentFile().delete();
                // file_place.getParentFile().delete();
                // file_detailed_detection.getParentFile().delete();

                System.out.println("Delete region folders...");
                System.out.println(file_region.getParentFile().delete());
                System.out.println(file_place.getParentFile().delete());
                System.out.println(file_detailed_detection.getParentFile().delete());

            }

        }

    }

}
