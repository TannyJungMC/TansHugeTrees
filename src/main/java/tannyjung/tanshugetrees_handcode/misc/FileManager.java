package tannyjung.tanshugetrees_handcode.misc;

import tannyjung.tanshugetrees.TanshugetreesMod;

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

				try {

					file.createNewFile();

				} catch (Exception e) {

					TanshugetreesMod.LOGGER.error(e.getMessage());

				}

			}

		}

		try {

			Writer writer = new FileWriter(file, append);
			BufferedWriter buffered_writer = new BufferedWriter(writer);

			buffered_writer.write(write);

			buffered_writer.close();
			writer.close();

		} catch (Exception e) {

			TanshugetreesMod.LOGGER.error(e.getMessage());

		}

	}

	public static void writeConfigTXT (String path, String write_get) {

		File file = new File(path);

		// Create a File
		{

			if (file.exists() == false) {

				try {

					file.createNewFile();

				} catch (Exception e) {

					TanshugetreesMod.LOGGER.error(e.getMessage());

				}

			}

		}

		// Test and Write
		{

			StringBuilder write = new StringBuilder();

			// Read New
			{

				try { BufferedReader buffered_reader = new BufferedReader(new StringReader(write_get)); String read_new = ""; while ((read_new = buffered_reader.readLine()) != null) {

					{

						if (read_new.contains(" = ") == true) {

							String test = read_new.substring(0, read_new.indexOf(" = "));
							boolean exists = false;

							// Read Old
							{

								try { BufferedReader buffered_reader2 = new BufferedReader(new FileReader(file)); String read_old = ""; while ((read_old = buffered_reader2.readLine()) != null) {

									{

										if (read_old.startsWith(test + " = ") == true) {

											read_new = read_old;
											exists = true;
											break;

										}

									}

								} buffered_reader2.close(); } catch (Exception e) { TanshugetreesMod.LOGGER.error(e.getMessage()); }

							}

							if (exists == false) {

								System.out.println("Repaired " + file.getName() + " > " + test);

							}

						}

						write.append(read_new);
						write.append("\n");

					}

				} buffered_reader.close(); } catch (Exception e) { TanshugetreesMod.LOGGER.error(e.getMessage()); }

			}

			FileManager.writeTXT(file.toPath().toString(), write.toString(), false);

		}

	}

}