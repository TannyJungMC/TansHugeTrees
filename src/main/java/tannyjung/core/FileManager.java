package tannyjung.core;

import java.io.*;

public class FileManager {

	public static void createFolder (String path) {

		File folder = new File(path);

		if (folder.exists() == false) {

			folder.mkdirs();

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

					MiscUtils.exception(new Exception(), exception);

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

			MiscUtils.exception(new Exception(), exception);

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

									} buffered_reader2.close(); } catch (Exception exception) { MiscUtils.exception(new Exception(), exception); }

								}

							}

						}

						write.append(read_new);
						write.append("\n");

					}

				} buffered_reader.close(); } catch (Exception exception) { MiscUtils.exception(new Exception(), exception); }

			}

			writeTXT(file.toPath().toString(), write.toString(), false);

		}

	}

	public static class GetConfigValue {

		public static boolean logic (String path, String name) {

			return Boolean.parseBoolean(find(path, name));

		}

		public static int numberInt (String path, String name) {

			return Integer.parseInt(find(path, name));

		}

		public static double numberDouble (String path, String name) {

			return Double.parseDouble(find(path, name));

		}

		public static String text (String path, String name) {

			return find(path, name);

		}

		public static String find (String path, String name) {

			String return_text = "";
			name = name + " = ";

			{

				File file = new File(path);

				{

					try { BufferedReader buffered_reader = new BufferedReader(new FileReader(file), 65536); String read_all = ""; while ((read_all = buffered_reader.readLine()) != null) {

						{

							if (read_all.startsWith("|") == false) {

								if (read_all.startsWith(name) == true) {

									return_text = read_all.replace(name, "");
									break;

								}

							}

						}

					} buffered_reader.close(); } catch (Exception exception) { MiscUtils.exception(new Exception(), exception); }

				}

			}

			return return_text;

		}

	}

}