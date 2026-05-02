package tannyjung.tanshugetrees_core.outside;

import tannyjung.tanshugetrees_core.Core;
import tannyjung.tanshugetrees_handcode.Handcode;

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

                String[] split = null;

                for (String scan : FileManager.readTXT(path)) {

                    if (scan.contains(" = ") == true) {

                        split = scan.split(" = ");
                        options.add(split[0]);
                        values.add(split[1]);
                        old_values.put(split[0], split[1]);

                    } else if (scan.startsWith("| Default is ") == true) {

                        scan = scan.substring("| Default is ".length());
                        scan = scan.substring(2, scan.length() - 2);
                        defaults.addAll(Arrays.asList(scan.split(" ] \\[ ")));

                        while (defaults.size() > values.size()) {

                            options.add("");
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
            String[] split = null;
            List<String> written_values = new ArrayList<>();

            for (String scan : original.split("\\n")) {

                if (scan.startsWith("|") == false && scan.contains(" = ") == true) {

                    // Write Option
                    {

                        split = scan.split(" = ");
                        option = split[0];
                        value = split[1];

                        if (old_values.containsKey(option) == true) {

                            if (should_keep.get(option) == true) {

                                write.append(option).append(" = ").append(old_values.get(option));

                            } else {

                                write.append(scan);

                            }

                        } else {

                            write.append(scan);

                        }

                        written_values.add(value);

                    }

                } else if (scan.isEmpty() == true && written_values.size() > 0) {

                    // Write Default
                    {

                        write.append(scan).append("| Default is");

                        for (String get : written_values) {

                            write.append(" [ ").append(get).append(" ]");

                        }

                        write.append("\n");
                        written_values.clear();

                    }

                } else {

                    write.append(scan);

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

            for (String scan : FileManager.readTXT(path)) {

                {

                    if (scan.isEmpty() == false) {

                        if (scan.contains(" = ") == true) {

                            index = scan.indexOf(" = ");
                            data.put(scan.substring(0, index), scan.substring(index + 3));

                        }

                    }

                }

            }

        }

        return data;

    }

}