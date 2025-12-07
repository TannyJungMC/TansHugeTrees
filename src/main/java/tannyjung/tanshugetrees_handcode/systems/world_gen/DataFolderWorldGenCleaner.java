package tannyjung.tanshugetrees_handcode.systems.world_gen;

import net.minecraft.world.level.ChunkPos;
import tannyjung.tanshugetrees_core.FileManager;
import tannyjung.tanshugetrees_handcode.Handcode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DataFolderWorldGenCleaner {

    public static void start (String dimension, ChunkPos chunk_pos) {

        String file_path = dimension + "/" + (chunk_pos.x >> 5) + "," + (chunk_pos.z >> 5) + ".bin";
        File file_region = new File(Handcode.path_world_data + "/world_gen/#regions/" + file_path);

        List<String> add = new ArrayList<>();
        add.add("b0");
        FileManager.writeBIN(file_region.getPath(), add, true);

        if (FileManager.readBIN(file_region.getPath()).capacity() >= 1024) {

            new File(Handcode.path_world_data + "/world_gen/place/" + file_path).delete();
            new File(Handcode.path_world_data + "/world_gen/detailed_detection/" + file_path).delete();

            List<String> add_end = new ArrayList<>();
            add_end.add("b1");
            FileManager.writeBIN(file_region.getPath(), add_end, false);

        }

    }

}
