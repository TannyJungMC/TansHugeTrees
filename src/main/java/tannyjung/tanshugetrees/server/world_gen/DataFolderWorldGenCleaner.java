package tannyjung.tanshugetrees.server.world_gen;

import net.minecraft.world.level.ChunkPos;
import tannyjung.core.FileManager;
import tannyjung.tanshugetrees.Handcode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DataFolderWorldGenCleaner {

    public static void start (String dimension, ChunkPos chunk_pos) {

        String region = (chunk_pos.x >> 5) + "," + (chunk_pos.z >> 5);
        String file_path = dimension + "/" + region + ".bin";

        File file_region = new File(Handcode.path_world_data + "/world_gen/#regions/" + file_path);
        List<String> list = new ArrayList<>();
        list.add("b0");
        FileManager.writeBIN(file_region.getPath(), list, true);

        if (FileManager.readBIN(file_region.getPath()).capacity() >= 1024) {

            FileManager.writeBIN(file_region.getPath(), new ArrayList<>(), false);
            new File(Handcode.path_world_data + "/world_gen/place/" + file_path).delete();
            new File(Handcode.path_world_data + "/world_gen/detailed_detection/" + file_path).delete();

        }

    }

}
