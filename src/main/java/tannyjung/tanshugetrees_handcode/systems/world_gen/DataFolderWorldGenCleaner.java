package tannyjung.tanshugetrees_handcode.systems.world_gen;

import net.minecraft.world.level.ChunkPos;
import tannyjung.core.FileManager;
import tannyjung.tanshugetrees_handcode.Handcode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DataFolderWorldGenCleaner {

    public static void start (String dimension, ChunkPos chunk_pos) {

        String region = (chunk_pos.x >> 5) + "," + (chunk_pos.z >> 5);
        String file_path = dimension + "/" + region;
        File file_region = new File(Handcode.path_world_data + "/world_gen/regions/" + file_path + ".bin");
        File file_place = new File(Handcode.path_world_data + "/world_gen/place/" + file_path + ".txt");
        File file_detailed_detection = new File(Handcode.path_world_data + "/world_gen/detailed_detection/" + file_path + ".txt");

        List<String> list = new ArrayList<>();
        list.add("b0");
        FileManager.writeBIN(file_region.getPath(), list, true);

        if (FileManager.readBIN(file_region.getPath()).capacity() >= 1024) {

            file_region.delete();
            file_place.delete();
            file_detailed_detection.delete();

            System.out.println("Deleted Region -> " + region);

        }

    }

}
