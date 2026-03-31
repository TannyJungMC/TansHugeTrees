package tannyjung.tanshugetrees_core.outside;

import tannyjung.tanshugetrees_core.Core;

import java.util.*;

public class ConfigClassic {

	public static void repair (String path, String original) {

        Map<String, Boolean> should_keep = new HashMap<>();
        Map<String, String> old_values = new HashMap<>();

        // Get Old Values
        {

            List<String> options = new ArrayList<>();
            List<String> values = new ArrayList<>();
            List<String> defaults = new ArrayList<>();

            // Get
            {

                String[] split = new String[0];

                for (String read_all : FileManager.readTXT(path)) {

                    if (read_all.contains(" = ") == true) {

                        split = read_all.split(" = ");
                        options.add(split[0]);
                        values.add(split[1]);
                        old_values.put(split[0], split[1]);

                    } else if (read_all.startsWith("| Default is ") == true) {

                        read_all = read_all.substring("| Default is ".length());
                        read_all = read_all.substring(2, read_all.length() - 2);
                        defaults.addAll(Arrays.asList(read_all.split(" ] \\[ ")));

                        while (values.size() > defaults.size()) {

                            defaults.add("");

                        }

                        while (values.size() < defaults.size()) {

                            values.add("");

                        }

                    }

                }

            }

            // Test Keep
            {

                boolean test = false;

                for (int loop = 0; loop < options.size(); loop++) {

                    if (defaults.get(loop).isEmpty() == true) {

                        test = false;

                    } else {

                        test = values.get(loop).equals(defaults.get(loop)) == false;

                    }

                    should_keep.put(options.get(loop), test);

                }

            }

        }

        StringBuilder write = new StringBuilder();
        write.append("Important Notes");
        write.append("\n");
        write.append("\n");
        write.append("- To apply this config and repair missing values, run this command [ /").append(Core.mod_id_big).append("restart ] or restart the world.");
        write.append("\n");
        write.append("- This config can be automatic repair itself to keep your unchanged values up to date, so don't panic when you see option values change by themselves!");
        write.append("\n");

        // Generate
        {

            String option = "";
            String value = "";
            String[] split = new String[0];
            List<String> written_values = new ArrayList<>();

            for (String read_all : original.split("\\n")) {

                if (read_all.startsWith("|") == false && read_all.contains(" = ") == true) {

                    // Write Option
                    {

                        split = read_all.split(" = ");
                        option = split[0];
                        value = split[1];

                        if (old_values.containsKey(option) == true) {

                            if (should_keep.get(option) == true) {

                                write.append(option).append(" = ").append(old_values.get(option));

                            } else {

                                write.append(read_all);

                            }

                        } else {

                            write.append(read_all);

                        }

                        written_values.add(value);

                    }

                } else if (read_all.isEmpty() == true && written_values.size() > 0) {

                    // Write Default
                    {

                        write.append(read_all).append("| Default is");

                        for (String get : written_values) {

                            write.append(" [ ").append(get).append(" ]");

                        }

                        write.append("\n");
                        written_values.clear();

                    }

                } else {

                    write.append(read_all);

                }

                write.append("\n");

            }

        }

        FileManager.writeTXT(path, write.toString(), false);

	}

    public static Map<String, String> getValues (String path) {

        Map<String, String> data = new HashMap<>();

        // Get Data
        {

            int index = 0;

            for (String read_all : FileManager.readTXT(path)) {

                {

                    if (read_all.isEmpty() == false) {

                        if (read_all.contains(" = ") == true) {

                            index = read_all.indexOf(" = ");
                            data.put(read_all.substring(0, index), read_all.substring(index + 3));

                        }

                    }

                }

            }

        }

        return data;

    }

}