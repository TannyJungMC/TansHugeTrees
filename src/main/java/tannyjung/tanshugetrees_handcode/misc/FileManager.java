package tannyjung.tanshugetrees_handcode.misc;

import tannyjung.tanshugetrees.TanshugetreesMod;
import tannyjung.tanshugetrees_handcode.Handcode;

import java.io.*;

public class FileManager {

	public static String quardtreeChunkToNode (int chunkX, int chunkZ) {

		StringBuilder return_text = new StringBuilder();

		{

			int localX = chunkX & 31;
			int localZ = chunkZ & 31;

			for (int level = 0; level < Handcode.quadtree_level; level++) {

				int size = 32 >> (level + 1);
				int posX = (localX / size) % 2;
				int posZ = (localZ / size) % 2;

				if (posX == 0 && posZ == 0) return_text.append("-NW");
				else if (posX == 1 && posZ == 0) return_text.append("-NE");
				else if (posX == 0 && posZ == 1) return_text.append("-SW");
				else return_text.append("-SE");

			}

		}

		return return_text.substring(1);

	}

	public static int[] textPosConverter (String pos, int rotation, boolean mirrored) {

		int[] return_number = new int[3];

		{

			String[] get = pos.split("\\^");
			int posX = Integer.parseInt(get[0]);
			int posY = Integer.parseInt(get[1]);
			int posZ = Integer.parseInt(get[2]);

			// Rotation & Mirrored
			{

				if (mirrored == true) {

					posX = posX * (-1);

				}

				if (rotation == 2) {

					int posX_save = posX;
					posX = posZ;
					posZ = posX_save * (-1);

				} else if (rotation == 3) {

					posX = posX * (-1);
					posZ = posZ * (-1);

				} else if (rotation == 4) {

					int posX_save = posX;
					int posZ_save = posZ;
					posX = posZ_save * (-1);
					posZ = posX_save;

				}

			}

			return_number[0] = posX;
			return_number[1] = posY;
			return_number[2] = posZ;

		}

		return return_number;

	}

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

					try {
						BufferedReader buffered_reader = new BufferedReader(new FileReader(file));
						String read_all = "";
						while ((read_all = buffered_reader.readLine()) != null) {

							{

								if (read_all.startsWith("|") == false) {

									if (read_all.startsWith(name) == true) {

										return_text = read_all.replace(name, "");
										break;

									}

								}

							}

						}
						buffered_reader.close();
					} catch (Exception e) {
						TanshugetreesMod.LOGGER.error(e.getMessage());
					}

				}

			}

			return return_text;

		}

	}

}