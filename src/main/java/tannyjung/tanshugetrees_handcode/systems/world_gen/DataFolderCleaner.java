package tannyjung.tanshugetrees_handcode.systems.world_gen;

import net.minecraft.world.level.ChunkPos;
import tannyjung.core.FileManager;
import tannyjung.core.GameUtils;
import tannyjung.tanshugetrees_handcode.Handcode;
import tannyjung.core.OutsideUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class DataFolderCleaner {

    public static void start (String dimension, ChunkPos chunk_pos) {

        String region = (chunk_pos.x >> 5) + "," + (chunk_pos.z >> 5);
        String node = GameUtils.outside.quardtreeChunkToNode(chunk_pos.x, chunk_pos.z);
        String file_path = dimension + "/" + region + "/" + node + ".txt";

        File file_region = new File(Handcode.directory_world_data + "/world_gen/regions/" + file_path);
        FileManager.writeTXT(file_region.toPath().toString(), "A", true);
        boolean full_node = false;

        // Read to get text length
        {

            try {
                BufferedReader buffered_reader = new BufferedReader(new FileReader(file_region));
                String read_all = "";
                while ((read_all = buffered_reader.readLine()) != null) {

                    {

                        if (read_all.length() >= (32 >> 2) * (32 >> 2)) {

                            full_node = true;

                        }

                    }

                }
                buffered_reader.close();
            } catch (Exception exception) {
                OutsideUtils.exception(new Exception(), exception);
            }

        }

        if (full_node == true) {

            File file_place = new File(Handcode.directory_world_data + "/world_gen/place/" + file_path);
            File file_detailed_detection = new File(Handcode.directory_world_data + "/world_gen/detailed_detection/" + file_path);

            file_region.delete();
            file_place.delete();
            file_detailed_detection.delete();

            // TanshugetreesMod.LOGGER.debug("Debugging : Delete Place File -> " + chunk_pos.x + " " + chunk_pos.z);

            // If finish placing all chunks in region
            if (file_place.getParentFile().listFiles().length == 0) {

                file_place.getParentFile().delete();
                file_detailed_detection.getParentFile().delete();

            }

        }

    }

}
