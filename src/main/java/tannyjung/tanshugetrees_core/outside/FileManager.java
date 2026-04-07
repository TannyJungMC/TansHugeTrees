package tannyjung.tanshugetrees_core.outside;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class FileManager {

	public static void createEmptyFile (String path, boolean is_directory) {

        File file = new File(path);

		if (file.exists() == false) {

            try {

                if (is_directory == true) {

                    file.mkdirs();

                } else {

                    file.getParentFile().mkdirs();
                    file.createNewFile();

                }

            } catch (Exception exception) {

                OutsideUtils.exception(new Exception(), exception, "");

            }

		}

	}

    public static List<File> getAllFiles (String path) {

        List<File> files = new ArrayList<>();

        try {

            Files.walk(Path.of(path)).forEach(source -> {

                if (source.toFile().isDirectory() == false) {

                    files.add(source.toFile());

                }

            });

        } catch (Exception exception) {

            OutsideUtils.exception(new Exception(), exception, "");

        }

        return files;

    }

    public static void rename (String path, String new_name) {

        File file = new File(path);
        file.renameTo(new File(file.getParentFile().getPath() + "/" + new_name));

    }

    public static void copy (String from, String to, boolean is_directory) {

        Path path_from = Path.of(from);
        Path path_to = Path.of(to);

        if (is_directory == false) {

            createEmptyFile(path_to.getParent().toString(), true);

            try {

                Files.copy(path_from, path_to, StandardCopyOption.REPLACE_EXISTING);

            } catch (Exception exception) {

                OutsideUtils.exception(new Exception(), exception, "");

            }

        } else {

            try {

                Files.walk(path_from).forEach(source -> {

                    if (source.toFile().isDirectory() == false) {

                        Path path_new = path_to.resolve(path_from.relativize(source));
                        createEmptyFile(path_new.getParent().toString(), true);

                        try {

                            Files.copy(source, path_new, StandardCopyOption.REPLACE_EXISTING);

                        } catch (Exception exception) {

                            OutsideUtils.exception(new Exception(), exception, "");

                        }

                    }

                });

            } catch (Exception exception) {

                OutsideUtils.exception(new Exception(), exception, "");

            }

        }

    }

    public static void delete (String path) {

        File file = new File(path);

        if (file.exists() == true) {

            try {

                Files.walk(file.toPath()).sorted(Comparator.reverseOrder()).forEach(source -> {

                    source.toFile().delete();

                });

            } catch (Exception exception) {

                OutsideUtils.exception(new Exception(), exception, "");

            }

        }

    }

	public static void writeTXT (String path, String write, boolean append) {

		File file = new File(path);
        createEmptyFile(file.getPath(), false);

		try {

			Writer writer = new FileWriter(file, append);
			BufferedWriter buffered_writer = new BufferedWriter(writer);

			buffered_writer.write(write);

			buffered_writer.close();
			writer.close();

		} catch (Exception exception) {

			OutsideUtils.exception(new Exception(), exception, "");

		}

	}

	public static String[] readTXT (String path) {

		File file = new File(path);

		if (file.exists() == true) {

			try {

				return Files.readAllLines(file.toPath()).toArray(new String[0]);

			} catch (Exception exception) {

				OutsideUtils.exception(new Exception(), exception, "");

			}

		}

		return new String[0];

	}

    public static void writeBIN (String path, List<String> write, boolean append) {

        createEmptyFile(path, false);

        if (write.size() > 0) {

            try {

                DataOutputStream file_bin = new DataOutputStream(new FileOutputStream(path, append));

                {

                    String type = "";
                    String value = "";

                    for (String scan : write) {

                        type = scan.substring(0, 1);
                        value = scan.substring(1);

                        if (type.equals("b") == true) {

                            file_bin.writeByte(Byte.parseByte(value));

                        } else if (type.equals("s") == true) {

                            file_bin.writeShort(Short.parseShort(value));

                        } else if (type.equals("i") == true) {

                            file_bin.writeInt(Integer.parseInt(value));

                        } else if (type.equals("l") == true) {

                            file_bin.writeBoolean(Boolean.parseBoolean(value));

                        }

                    }

                }

                file_bin.close();

            } catch (Exception exception) {

                OutsideUtils.exception(new Exception(), exception, "");

            }

        }

    }

    public static ByteBuffer readBIN (String path) {

        File file = new File(path);

        if (file.exists() == true) {

            try {

                return ByteBuffer.wrap(Files.readAllBytes(file.toPath())).order(ByteOrder.BIG_ENDIAN);

            } catch (Exception exception) {

                OutsideUtils.exception(new Exception(), exception, "");

            }

        }

        return ByteBuffer.allocate(0);

    }

    public static void extractZIP (String path_zip, String path_folder, boolean skip_first_folder, String filter) {

        createEmptyFile(path_folder, true);

        try {

            ZipInputStream stream_input = new ZipInputStream(new FileInputStream(path_zip));
            ZipEntry entry = stream_input.getNextEntry();
            FileOutputStream stream_output = null;
            byte[] bytes = new byte[1024];
            int length = 0;
            String entry_path = "";
            int first_folder = 0;

            while (entry != null) {

                if (skip_first_folder == true) {

                    if (first_folder == 0) {

                        first_folder = entry.getName().length();

                    }

                }

                entry_path = entry.getName().substring(first_folder);

                if (entry_path.contains(filter) == true) {

                    if (entry.isDirectory() == true) {

                        createEmptyFile(path_folder + "/" + entry_path, true);

                    } else {

                        createEmptyFile(path_folder + "/" + entry_path, false);
                        stream_output = new FileOutputStream(path_folder + "/" + entry_path);

                        while ((length = stream_input.read(bytes)) > 0) {

                            stream_output.write(bytes, 0, length);

                        }

                        stream_output.close();

                    }

                }

                entry = stream_input.getNextEntry();

            }

            stream_input.closeEntry();
            stream_input.close();

        } catch (Exception exception) {

            OutsideUtils.exception(new Exception(), exception, "");

        }

    }

    public static void compressZIP (String path_zip, File file) {

        createEmptyFile(path_zip, false);

        try {

            ZipOutputStream stream_output = new ZipOutputStream(new FileOutputStream(path_zip));
            byte[] bytes = new byte[1024];

            if (file.isDirectory() == false) {

                FileInputStream stream_input = new FileInputStream(file);
                stream_output.putNextEntry(new ZipEntry(file.getName()));
                int length = 0;

                while ((length = stream_input.read(bytes)) >= 0) {

                    stream_output.write(bytes, 0, length);

                }

                stream_input.close();

            } else {

                Files.walk(file.toPath()).forEach(source -> {

                    if (source.toFile().isDirectory() == false) {

                        try {

                            FileInputStream stream_input = new FileInputStream(source.toFile());
                            stream_output.putNextEntry(new ZipEntry(file.toPath().relativize(source).toString()));
                            int length = 0;

                            while ((length = stream_input.read(bytes)) >= 0) {

                                stream_output.write(bytes, 0, length);

                            }

                            stream_input.close();

                        } catch (Exception exception) {

                            OutsideUtils.exception(new Exception(), exception, "");

                        }

                    }

                });

            }

            stream_output.close();

        } catch (Exception exception) {

            OutsideUtils.exception(new Exception(), exception, "");

        }

    }

    public static void mergeTXT (File file_from, File file_to) {

        if (file_to.exists() == false) {

            copy(file_from.getPath(), file_to.getPath(), false);

        } else {

            String[] data_new = FileManager.readTXT(file_from.getPath());

            if (data_new[0].equals("# REPLACE") == true) {

                StringBuilder write = new StringBuilder();
                boolean skip = true;

                for (String scan : data_new) {

                    if (skip == true) {

                        skip = false;
                        continue;

                    }

                    write.append(scan).append("\n");

                }

                FileManager.writeTXT(file_to.getPath(), write.toString(), false);

            } else {

                String[] data_old = FileManager.readTXT(file_to.getPath());
                Map<String, String> data = new HashMap<>();
                String[] split = null;
                String[] split2 = null;

                // Get Old Data
                {

                    for (String scan : data_old) {

                        if (scan.isEmpty() == false) {

                            if (scan.startsWith("#") == false) {

                                if (scan.contains(" = ") == true) {

                                    split = scan.split(" = ");
                                    data.put(split[0], split[1]);

                                }

                            }

                        }

                    }

                }

                List<String> write_add = new ArrayList<>();

                // Get Data
                {

                    StringBuilder merge = new StringBuilder();

                    for (String scan : data_new) {

                        if (scan.isEmpty() == false) {

                            if (scan.contains(" = ") == true) {

                                if (scan.startsWith("# MERGE") == true) {

                                    {

                                        split = scan.substring(scan.indexOf(" -> ") + 4).split(" = ");

                                        if (data.containsKey(split[0]) == false) {

                                            write_add.add(split[0] + " = " + split[1]);

                                        } else {

                                            merge.setLength(0);
                                            merge.append("|").append(data.get(split[0]).replace(" / ", "| / |")).append("|");

                                            for (String scan_list : split[1].split(" / ")) {

                                                if (merge.toString().contains("|" + scan_list + "|") == false) {

                                                    merge.append(" / ").append(scan_list);

                                                }

                                            }

                                            data.put(split[0], merge.toString().replace("|", ""));

                                        }

                                    }

                                } else if (scan.startsWith("# REPLACE") == true) {

                                    {

                                        split = scan.substring(scan.indexOf(" -> ") + 4).split(" = ");

                                        if (data.containsKey(split[0]) == true) {

                                            split2 = data.get(split[1]).split(" >>> ");
                                            write_add.add(split[0] + " = " + data.get(split[0]).replace(split2[0], split2[1]));

                                        }

                                    }


                                } else {

                                    split = scan.split(" = ");

                                    if (data.containsKey(split[0]) == true) {

                                        data.replace(split[0], split[1]);

                                    } else {

                                        write_add.add(scan);

                                    }

                                }

                            }

                        }

                    }

                }

                // Write
                {

                    List<String> write = new ArrayList<>();

                    for (String scan : data_old) {

                        if (scan.contains(" = ") == false) {

                            write.add(scan);

                        } else {

                            if (scan.startsWith("# MERGE") == true) {

                                scan = scan.substring(scan.indexOf(" -> ") + 4);

                            }

                            split = scan.split(" = ");
                            write.add(split[0] + " = " + data.get(split[0]));

                        }

                    }

                    write.addAll(write_add);
                    FileManager.writeTXT(file_to.getPath(), String.join("\n", write), false);

                }

            }

        }

    }

}