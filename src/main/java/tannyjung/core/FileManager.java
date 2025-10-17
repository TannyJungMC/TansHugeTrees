package tannyjung.core;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FileManager {

	public static void createFolder (String path) {

		File folder = new File(path);

		if (folder.exists() == false) {

			folder.mkdirs();

			if (folder.getName().startsWith(".") == true) {

				try {

					Runtime.getRuntime().exec("attrib +H \"" + Path.of(path).toAbsolutePath() + "\"");

				} catch (Exception exception) {

					OutsideUtils.exception(new Exception(), exception);

				}

			}

		}

	}

	public static void writeTXT (String path, String write, boolean append) {

		File file = new File(path);

		// Create a File
		{

			if (file.exists() == false) {

				FileManager.createFolder(file.getParent());

				try {

					file.createNewFile();

				} catch (Exception exception) {

					OutsideUtils.exception(new Exception(), exception);

				}

			}

		}

		try {

			Writer writer = new FileWriter(file, append);
			BufferedWriter buffered_writer = new BufferedWriter(writer);

			buffered_writer.write(write);

			buffered_writer.close();
			writer.close();

		} catch (Exception exception) {

			OutsideUtils.exception(new Exception(), exception);

		}

	}

	public static void writeConfigTXT (String path, String write_get) {

		File file = new File(path);
		createFolder(file.getParent());
		boolean old_file_exists = file.exists() == true && file.isDirectory() == false;

		// Test and Write
		{

			StringBuilder write = new StringBuilder();

			// Read New
			{

				try { BufferedReader buffered_reader = new BufferedReader(new StringReader(write_get)); String read_new = ""; while ((read_new = buffered_reader.readLine()) != null) {

					{

						if (read_new.contains(" = ") == true) {

							String test = read_new.substring(0, read_new.indexOf(" = "));

							if (old_file_exists) {

								// Read Old
								{

									try { BufferedReader buffered_reader2 = new BufferedReader(new FileReader(file), 65536); String read_old = ""; while ((read_old = buffered_reader2.readLine()) != null) {

										{

											if (read_old.startsWith(test + " = ") == true) {

												read_new = read_old;
												break;

											}

										}

									} buffered_reader2.close(); } catch (Exception exception) { OutsideUtils.exception(new Exception(), exception); }

								}

							}

						}

						write.append(read_new);
						write.append("\n");

					}

				} buffered_reader.close(); } catch (Exception exception) { OutsideUtils.exception(new Exception(), exception); }

			}

			writeTXT(file.toPath().toString(), write.toString(), false);

		}

	}

	public static String[] readTXT (String path) {

		String[] return_array = new String[0];
		File file = new File(path);

		if (file.exists() == true && file.isDirectory() == false) {

			try {

				return_array = Files.readAllLines(file.toPath()).toArray(new String[0]);

			} catch (Exception exception) {

				OutsideUtils.exception(new Exception(), exception);

			}

		}

		return return_array;

	}

    public static void writeBIN (String path, short[] write, boolean append) {

        try {

            DataOutputStream file_bin = new DataOutputStream(new FileOutputStream(path, append));

            for (short read_all : write) {

                file_bin.writeShort(read_all);

            }

            file_bin.close();

        } catch (Exception exception) {

            OutsideUtils.exception(new Exception(), exception);

        }

    }

    public static short[] readBIN (String path) {

        List<Short> get_list = new ArrayList<>();

        {

            try {

                DataInputStream file_bin = new DataInputStream(new FileInputStream(path));

                while (file_bin.available() > 0) {

                    get_list.add(file_bin.readShort());

                }

                file_bin.close();

            } catch (Exception exception) {

                OutsideUtils.exception(new Exception(), exception);

            }

        }

        return OutsideUtils.shortListToArray(get_list);

    }

}