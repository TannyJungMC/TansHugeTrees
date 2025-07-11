package tannyjung.tanshugetrees_handcode.systems.world_gen;

import net.minecraft.world.level.ChunkPos;
import tannyjung.core.FileManager;
import tannyjung.tanshugetrees_handcode.Handcode;
import tannyjung.core.MiscUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class DataFolderCleaner {

    public static void start (String dimension, ChunkPos chunk_pos) {

        String region = (chunk_pos.x >> 5) + "," + (chunk_pos.z >> 5);
        String node = MiscUtils.quardtreeChunkToNode(chunk_pos.x, chunk_pos.z);
        String file_path = dimension + "/" + region + "/" + node + ".txt";

        boolean full_node = false;
        File file_region = new File(Handcode.directory_world_data + "/regions/" + file_path);
        FileManager.writeTXT(file_region.toPath().toString(), "A", true);

        // Read to get text length
        {

            try { BufferedReader buffered_reader = new BufferedReader(new FileReader(file_region)); String read_all = ""; while ((read_all = buffered_reader.readLine()) != null) {

                {

                    if (read_all.length() >= Math.pow(32 >> 2, 2)) {

                        full_node = true;

                    }

                }

            } buffered_reader.close(); } catch (Exception exception) { MiscUtils.exception(new Exception(), exception); }

        }

        if (full_node == true) {

            file_region.delete();

            File file_place = new File(Handcode.directory_world_data + "/place/" + file_path);
            File file_detailed_detection = new File(Handcode.directory_world_data + "/detailed_detection/" + file_path);

            file_place.delete();
            file_detailed_detection.delete();

            // If finish placing all chunks in region
            if (file_place.getParentFile().listFiles() == null) {

                file_place.getParentFile().delete();
                file_detailed_detection.getParentFile().delete();

            }

        }

    }

}
