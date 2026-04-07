package tannyjung.tanshugetrees_core.outside;

import tannyjung.tanshugetrees_core.Core;

import java.util.*;

public class CacheManager {

    public static String clear () {

        int size = 0;
        size = size + DataLogic.clear();
        size = size + DataText.clear();
        size = size + DataShort.clear();
        size = size + DataInt.clear();

        if (size < 1024) {

            return size + " B";

        } else if (size < 1048576) {

            return String.format(Locale.US, "%.2f", (double) size / 1024.0) + " KB";

        } else {

            return String.format(Locale.US, "%.2f", (double) size / 1048576.0) + " MB";

        }

    }

    public static class DataLogic {

        private static final Map<String, Map<String, Boolean>> normal = new HashMap<>();
        private static final Map<String, Map<String, boolean[]>> array = new HashMap<>();
        private static final Map<String, Map<String, List<Boolean>>> list = new HashMap<>();
        private static final Map<String, Map<String, Map<String, Boolean>>> map = new HashMap<>();
        private static final Object lock_normal = new Object();
        private static final Object lock_array = new Object();
        private static final Object lock_list = new Object();
        private static final Object lock_map = new Object();

        private static int clear () {

            int size = 0;

            // Normal
            {

                synchronized (lock_normal) {

                    for (Map.Entry<String, Map<String, Boolean>> entry : normal.entrySet()) {

                        size = size + entry.getValue().size();

                    }

                    normal.clear();

                }

            }

            // Array
            {

                synchronized (lock_array) {

                    for (Map.Entry<String, Map<String, boolean[]>> entry1 : array.entrySet()) {

                        for (Map.Entry<String, boolean[]> entry2 : entry1.getValue().entrySet()) {

                            size = size + entry2.getValue().length;

                        }

                    }

                    array.clear();

                }

            }

            // List
            {

                synchronized (lock_list) {

                    for (Map.Entry<String, Map<String, List<Boolean>>> entry1 : list.entrySet()) {

                        for (Map.Entry<String, List<Boolean>> entry2 : entry1.getValue().entrySet()) {

                            size = size + entry2.getValue().size();

                        }

                    }

                    list.clear();

                }

            }

            // Map
            {

                synchronized (lock_map) {

                    for (Map.Entry<String, Map<String, Map<String, Boolean>>> entry1 : map.entrySet()) {

                        for (Map.Entry<String, Map<String, Boolean>> entry2 : entry1.getValue().entrySet()) {

                            size = size + entry2.getValue().size();

                        }

                    }

                    map.clear();

                }

            }

            return size;

        }

        public static boolean existNormal (String name, String key) {

            synchronized (lock_normal) {

                return normal.getOrDefault(name, new HashMap<>()).containsKey(key);

            }

        }

        public static Map<String, Boolean> getNormal (String name) {

            synchronized (lock_normal) {

                return normal.getOrDefault(name, new HashMap<>());

            }

        }

        public static void setNormal (String name, String key, boolean value) {

            synchronized (lock_normal) {

                if (key == null) {

                    normal.put(name, new HashMap<>());

                } else {

                    normal.computeIfAbsent(name, create -> new HashMap<>()).put(key, value);

                }

            }

        }

        public static Map<String, boolean[]> getArray (String name) {

            synchronized (lock_array) {

                return array.getOrDefault(name, new HashMap<>());

            }

        }

        public static void setArray (String name, String key, boolean[] value) {

            synchronized (lock_array) {

                if (key == null) {

                    array.put(name, new HashMap<>());

                } else {

                    array.computeIfAbsent(name, create -> new HashMap<>()).put(key, value);

                }

            }

        }

        public static Map<String, List<Boolean>> getList (String name) {

            synchronized (lock_list) {

                return list.getOrDefault(name, new HashMap<>());

            }

        }

        public static void setList (String name, String key, List<Boolean> value) {

            synchronized (lock_list) {

                if (key == null) {

                    list.put(name, new HashMap<>());

                } else {

                    list.computeIfAbsent(name, create -> new HashMap<>()).put(key, value);

                }

            }

        }

        public static Map<String, Map<String, Boolean>> getMap (String name) {

            synchronized (lock_map) {

                return map.getOrDefault(name, new HashMap<>());

            }

        }

        public static void setMap (String name, String key, Map<String, Boolean> value) {

            synchronized (lock_map) {

                if (key == null) {

                    map.put(name, new HashMap<>());

                } else {

                    map.computeIfAbsent(name, create -> new HashMap<>()).put(key, value);

                }

            }

        }

    }

    public static class DataText {

        private static final Map<String, Map<String, String>> normal = new HashMap<>();
        private static final Map<String, Map<String, String[]>> array = new HashMap<>();
        private static final Map<String, Map<String, Set<String>>> set = new HashMap<>();
        private static final Map<String, Map<String, List<String>>> list = new HashMap<>();
        private static final Map<String, Map<String, Map<String, String>>> map = new HashMap<>();
        private static final Object lock_normal = new Object();
        private static final Object lock_array = new Object();
        private static final Object lock_set = new Object();
        private static final Object lock_list = new Object();
        private static final Object lock_map = new Object();

        private static int clear () {

            int size = 0;

            // Normal
            {

                synchronized (lock_normal) {

                    for (Map.Entry<String, Map<String, String>> entry1 : normal.entrySet()) {

                        for (Map.Entry<String, String> entry2 : entry1.getValue().entrySet()) {

                            size = size + (entry2.getValue().length() * Character.BYTES);

                        }

                    }

                    normal.clear();

                }

            }

            // Array
            {

                synchronized (lock_array) {

                    for (Map.Entry<String, Map<String, String[]>> entry1 : array.entrySet()) {

                        for (Map.Entry<String, String[]> entry2 : entry1.getValue().entrySet()) {

                            for (String scan : entry2.getValue()) {

                                size = size + (scan.length() * Character.BYTES);

                            }

                        }

                    }

                    array.clear();

                }

            }

            // Set
            {

                synchronized (lock_set) {

                    for (Map.Entry<String, Map<String, Set<String>>> entry1 : set.entrySet()) {

                        for (Map.Entry<String, Set<String>> entry2 : entry1.getValue().entrySet()) {

                            for (String scan : entry2.getValue()) {

                                size = size + (scan.length() * Character.BYTES);

                            }

                        }

                    }

                    set.clear();

                }

            }

            // List
            {

                synchronized (lock_list) {

                    for (Map.Entry<String, Map<String, List<String>>> entry1 : list.entrySet()) {

                        for (Map.Entry<String, List<String>> entry2 : entry1.getValue().entrySet()) {

                            for (String scan : entry2.getValue()) {

                                size = size + (scan.length() * Character.BYTES);

                            }

                        }

                    }

                    list.clear();

                }

            }

            // Map
            {

                synchronized (lock_map) {

                    for (Map.Entry<String, Map<String, Map<String, String>>> entry1 : map.entrySet()) {

                        for (Map.Entry<String, Map<String, String>> entry2 : entry1.getValue().entrySet()) {

                            for (Map.Entry<String, String> entry3 : entry2.getValue().entrySet()) {

                                size = size + (entry3.getValue().length() * Character.BYTES);

                            }

                        }

                    }

                    map.clear();

                }

            }

            return size;

        }

        public static boolean existNormal (String name, String key) {

            synchronized (lock_normal) {

                return normal.getOrDefault(name, new HashMap<>()).containsKey(key);

            }

        }

        public static Map<String, String> getNormal (String name) {

            synchronized (lock_normal) {

                return normal.getOrDefault(name, new HashMap<>());

            }

        }

        public static void setNormal (String name, String key, String value) {

            synchronized (lock_normal) {

                if (key == null) {

                    normal.put(name, new HashMap<>());

                } else {

                    normal.computeIfAbsent(name, create -> new HashMap<>()).put(key, value);

                }

            }

        }

        public static Map<String, String[]> getArray (String name) {

            synchronized (lock_array) {

                return array.getOrDefault(name, new HashMap<>());

            }

        }

        public static void setArray (String name, String key, String[] value) {

            synchronized (lock_array) {

                if (key == null) {

                    array.put(name, new HashMap<>());

                } else {

                    array.computeIfAbsent(name, create -> new HashMap<>()).put(key, value);

                }

            }

        }

        public static Map<String, Set<String>> getSet (String name) {

            synchronized (lock_set) {

                return set.getOrDefault(name, new HashMap<>());

            }

        }

        public static void setSet (String name, String key, Set<String> value) {

            synchronized (lock_set) {

                if (key == null) {

                    set.put(name, new HashMap<>());

                } else {

                    set.computeIfAbsent(name, create -> new HashMap<>()).put(key, value);

                }

            }

        }

        public static Map<String, List<String>> getList (String name) {

            synchronized (lock_list) {

                return list.getOrDefault(name, new HashMap<>());

            }

        }

        public static void setList (String name, String key, List<String> value) {

            synchronized (lock_list) {

                if (key == null) {

                    list.put(name, new HashMap<>());

                } else {

                    list.computeIfAbsent(name, create -> new HashMap<>()).put(key, value);

                }

            }

        }

        public static Map<String, Map<String, String>> getMap (String name) {

            synchronized (lock_map) {

                return map.getOrDefault(name, new HashMap<>());

            }

        }

        public static void setMap (String name, String key, Map<String, String> value) {

            synchronized (lock_map) {

                if (key == null) {

                    map.put(name, new HashMap<>());

                } else {

                    map.computeIfAbsent(name, create -> new HashMap<>()).put(key, value);

                }

            }

        }

    }

    public static class DataShort {

        private static final Map<String, Map<String, Short>> normal = new HashMap<>();
        private static final Map<String, Map<String, short[]>> array = new HashMap<>();
        private static final Map<String, Map<String, Set<Short>>> set = new HashMap<>();
        private static final Map<String, Map<String, List<Short>>> list = new HashMap<>();
        private static final Map<String, Map<String, Map<String, Short>>> map = new HashMap<>();
        private static final Object lock_normal = new Object();
        private static final Object lock_array = new Object();
        private static final Object lock_set = new Object();
        private static final Object lock_list = new Object();
        private static final Object lock_map = new Object();

        private static int clear () {

            int size = 0;

            // Normal
            {

                synchronized (lock_normal) {

                    for (Map.Entry<String, Map<String, Short>> entry : normal.entrySet()) {

                        size = size + (entry.getValue().size() * Short.BYTES);

                    }

                    normal.clear();

                }

            }

            // Array
            {

                synchronized (lock_array) {

                    for (Map.Entry<String, Map<String, short[]>> entry1 : array.entrySet()) {

                        for (Map.Entry<String, short[]> entry2 : entry1.getValue().entrySet()) {

                            size = size + (entry2.getValue().length * Short.BYTES);

                        }

                    }

                    array.clear();

                }

            }

            // Set
            {

                synchronized (lock_set) {

                    for (Map.Entry<String, Map<String, Set<Short>>> entry1 : set.entrySet()) {

                        for (Map.Entry<String, Set<Short>> entry2 : entry1.getValue().entrySet()) {

                            size = size + (entry2.getValue().size() * Short.BYTES);

                        }

                    }

                    set.clear();

                }

            }

            // List
            {

                synchronized (lock_list) {

                    for (Map.Entry<String, Map<String, List<Short>>> entry1 : list.entrySet()) {

                        for (Map.Entry<String, List<Short>> entry2 : entry1.getValue().entrySet()) {

                            size = size + (entry2.getValue().size() * Short.BYTES);

                        }

                    }

                    list.clear();

                }

            }

            // Map
            {

                synchronized (lock_map) {

                    for (Map.Entry<String, Map<String, Map<String, Short>>> entry1 : map.entrySet()) {

                        for (Map.Entry<String, Map<String, Short>> entry2 : entry1.getValue().entrySet()) {

                            size = size + (entry2.getValue().size() * Short.BYTES);

                        }

                    }

                    map.clear();

                }

            }

            return size;

        }

        public static boolean existNormal (String name) {

            synchronized (lock_normal) {

                return normal.containsKey(name);

            }

        }

        public static Map<String, Short> getNormal (String name) {

            synchronized (lock_normal) {

                return normal.getOrDefault(name, new HashMap<>());

            }

        }

        public static void setNormal (String name, String key, short value) {

            synchronized (lock_normal) {

                if (key == null) {

                    normal.put(name, new HashMap<>());

                } else {

                    normal.computeIfAbsent(name, create -> new HashMap<>()).put(key, value);

                }

            }

        }

        public static Map<String, short[]> getArray (String name) {

            synchronized (lock_array) {

                return array.getOrDefault(name, new HashMap<>());

            }

        }

        public static void setArray (String name, String key, short[] value) {

            synchronized (lock_array) {

                if (key == null) {

                    array.put(name, new HashMap<>());

                } else {

                    array.computeIfAbsent(name, create -> new HashMap<>()).put(key, value);

                }

            }

        }

        public static Map<String, Set<Short>> getSet (String name) {

            synchronized (lock_set) {

                return set.getOrDefault(name, new HashMap<>());

            }

        }

        public static void setSet (String name, String key, Set<Short> value) {

            synchronized (lock_set) {

                if (key == null) {

                    set.put(name, new HashMap<>());

                } else {

                    set.computeIfAbsent(name, create -> new HashMap<>()).put(key, value);

                }

            }

        }

        public static Map<String, List<Short>> getList (String name) {

            synchronized (lock_list) {

                return list.getOrDefault(name, new HashMap<>());

            }

        }

        public static void setList (String name, String key, List<Short> value) {

            synchronized (lock_list) {

                if (key == null) {

                    list.put(name, new HashMap<>());

                } else {

                    list.computeIfAbsent(name, create -> new HashMap<>()).put(key, value);

                }

            }

        }

        public static Map<String, Map<String, Short>> getMap (String name) {

            synchronized (lock_map) {

                return map.getOrDefault(name, new HashMap<>());

            }

        }

        public static void setMap (String name, String key, Map<String, Short> value) {

            synchronized (lock_map) {

                if (key == null) {

                    map.put(name, new HashMap<>());

                } else {

                    map.computeIfAbsent(name, create -> new HashMap<>()).put(key, value);

                }

            }

        }

    }

    public static class DataInt {

        private static final Map<String, Map<String, Integer>> normal = new HashMap<>();
        private static final Map<String, Map<String, int[]>> array = new HashMap<>();
        private static final Map<String, Map<String, Set<Integer>>> set = new HashMap<>();
        private static final Map<String, Map<String, List<Integer>>> list = new HashMap<>();
        private static final Map<String, Map<String, Map<String, Integer>>> map = new HashMap<>();
        private static final Object lock_normal = new Object();
        private static final Object lock_array = new Object();
        private static final Object lock_set = new Object();
        private static final Object lock_list = new Object();
        private static final Object lock_map = new Object();

        private static int clear () {

            int size = 0;

            // Normal
            {

                synchronized (lock_normal) {

                    for (Map.Entry<String, Map<String, Integer>> entry : normal.entrySet()) {

                        size = size + (entry.getValue().size() * Integer.BYTES);

                    }

                    normal.clear();

                }

            }

            // Array
            {

                synchronized (lock_array) {

                    for (Map.Entry<String, Map<String, int[]>> entry1 : array.entrySet()) {

                        for (Map.Entry<String, int[]> entry2 : entry1.getValue().entrySet()) {

                            size = size + (entry2.getValue().length * Integer.BYTES);

                        }

                    }

                    array.clear();

                }

            }

            // Set
            {

                synchronized (lock_set) {

                    for (Map.Entry<String, Map<String, Set<Integer>>> entry1 : set.entrySet()) {

                        for (Map.Entry<String, Set<Integer>> entry2 : entry1.getValue().entrySet()) {

                            size = size + (entry2.getValue().size() * Integer.BYTES);

                        }

                    }

                    set.clear();

                }

            }

            // List
            {

                synchronized (lock_list) {

                    for (Map.Entry<String, Map<String, List<Integer>>> entry1 : list.entrySet()) {

                        for (Map.Entry<String, List<Integer>> entry2 : entry1.getValue().entrySet()) {

                            size = size + (entry2.getValue().size() * Integer.BYTES);

                        }

                    }

                    list.clear();

                }

            }

            // Map
            {

                synchronized (lock_map) {

                    for (Map.Entry<String, Map<String, Map<String, Integer>>> entry1 : map.entrySet()) {

                        for (Map.Entry<String, Map<String, Integer>> entry2 : entry1.getValue().entrySet()) {

                            size = size + (entry2.getValue().size() * Integer.BYTES);

                        }

                    }

                    map.clear();

                }

            }

            return size;

        }

        public static boolean existNormal (String name) {

            synchronized (lock_normal) {

                return normal.containsKey(name);

            }

        }

        public static Map<String, Integer> getNormal (String name) {

            synchronized (lock_normal) {

                return normal.getOrDefault(name, new HashMap<>());

            }

        }

        public static void setNormal (String name, String key, int value) {

            synchronized (lock_normal) {

                if (key == null) {

                    normal.put(name, new HashMap<>());

                } else {

                    normal.computeIfAbsent(name, create -> new HashMap<>()).put(key, value);

                }

            }

        }

        public static Map<String, int[]> getArray (String name) {

            synchronized (lock_array) {

                return array.getOrDefault(name, new HashMap<>());

            }

        }

        public static void setArray (String name, String key, int[] value) {

            synchronized (lock_array) {

                if (key == null) {

                    array.put(name, new HashMap<>());

                } else {

                    array.computeIfAbsent(name, create -> new HashMap<>()).put(key, value);

                }

            }

        }

        public static Map<String, Set<Integer>> getSet (String name) {

            synchronized (lock_set) {

                return set.getOrDefault(name, new HashMap<>());

            }

        }

        public static void setSet (String name, String key, Set<Integer> value) {

            synchronized (lock_set) {

                if (key == null) {

                    set.put(name, new HashMap<>());

                } else {

                    set.computeIfAbsent(name, create -> new HashMap<>()).put(key, value);

                }

            }

        }

        public static Map<String, List<Integer>> getList (String name) {

            synchronized (lock_list) {

                return list.getOrDefault(name, new HashMap<>());

            }

        }

        public static void setList (String name, String key, List<Integer> value) {

            synchronized (lock_list) {

                if (key == null) {

                    list.put(name, new HashMap<>());

                } else {

                    list.computeIfAbsent(name, create -> new HashMap<>()).put(key, value);

                }

            }

        }

        public static Map<String, Map<String, Integer>> getMap (String name) {

            synchronized (lock_map) {

                return map.getOrDefault(name, new HashMap<>());

            }

        }

        public static void setMap (String name, String key, Map<String, Integer> value) {

            synchronized (lock_map) {

                if (key == null) {

                    map.put(name, new HashMap<>());

                } else {

                    map.computeIfAbsent(name, create -> new HashMap<>()).put(key, value);

                }

            }

        }

    }

    public static String[] getFunction (String path) {

        String[] data = DataText.getArray("functions").get(path);

        if (data == null) {

            data = FileManager.readTXT(Core.path_config + "/dev/temporary/" + path + ".txt");
            DataText.setArray("functions", path, data);

        }

        return data;

    }

    public static String getDictionary (String key, boolean is_number) {

        String get = DataText.getNormal("dictionary").get(key);

        if (get == null) {

            // Write New
            {

                String value_id = "";
                String value_text = "";
                String path = Core.path_world_mod + "/dictionary.txt";
                String[] data = FileManager.readTXT(path);

                for (String scan : data) {

                    if (is_number == true) {

                        if (scan.startsWith(key + "|") == true) {

                            value_id = key;
                            value_text = scan.substring(scan.indexOf("|") + 1);
                            break;

                        }

                    } else {

                        if (scan.endsWith("|" + key) == true) {

                            value_id = scan.substring(0, scan.indexOf("|"));
                            value_text = key;
                            break;

                        }

                    }

                }

                if (value_id.isEmpty() == true && value_text.isEmpty() == true) {

                    if (is_number == false) {

                        value_text = key;

                    }

                    if (value_text.isEmpty() == false) {

                        value_id = String.valueOf(data.length + 1);
                        FileManager.writeTXT(path, value_id + "|" + value_text + "\n", true);

                    }

                }

                DataText.setNormal("dictionary", value_id, value_text);
                DataText.setNormal("dictionary", value_text, value_id);

                if (is_number == true) {

                    get = value_text;

                } else {

                    get = value_id;

                }

            }

        }

        return get;

    }

}
