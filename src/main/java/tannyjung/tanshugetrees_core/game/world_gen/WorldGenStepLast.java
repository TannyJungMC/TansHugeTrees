package tannyjung.tanshugetrees_core.game.world_gen;

import net.minecraft.world.level.ChunkPos;
import tannyjung.tanshugetrees_core.Core;
import tannyjung.tanshugetrees_core.outside.FileManager;
import tannyjung.tanshugetrees_handcode.systems.world_gen.WorldGen;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class WorldGenStepLast {

    public static void start (String dimension, ChunkPos chunk_pos) {

        // World Gen Folder Cleaner
        {

            WorldGen.stepLast(dimension, chunk_pos);
            String path_prefix = Core.path_world_core + "/" + Core.data_structure_version_core;
            String path_suffix = dimension + "/" + (chunk_pos.x >> 5) + "," + (chunk_pos.z >> 5) + ".bin";

            if (WorldGenStepLast.testWorldGenFolderCleaner(path_prefix + "/world_gen/#regions/" + path_suffix) == true) {

                new File(path_prefix + "/world_gen/blacklist_chunks/" + path_suffix).delete();

            }

            new File(path_prefix + "/world_gen/pre_location_biome.bin").delete();

        }

    }

    public static boolean testWorldGenFolderCleaner (String path) {

        File file = new File(path);
        List<String> add = new ArrayList<>();
        add.add("b0");
        FileManager.writeBIN(file.getPath(), add, true);

        if (FileManager.readBIN(file.getPath()).capacity() >= 1024) {

            List<String> add_end = new ArrayList<>();
            add_end.add("b1");
            FileManager.writeBIN(file.getPath(), add_end, false);

            return true;

        }

        return false;

    }

}