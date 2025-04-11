package tannyjung.tanshugetrees_handcode.config;

import net.minecraft.world.level.LevelAccessor;

import java.io.*;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import tannyjung.tanshugetrees.TanshugetreesMod;
import tannyjung.tanshugetrees_handcode.Handcode;
import tannyjung.tanshugetrees_handcode.misc.Misc;
import tannyjung.tanshugetrees_handcode.misc.MiscOutside;

public class UpdateRun {

	private static String error = "";

    public static void start (LevelAccessor level) {

		String url = "https://raw.githubusercontent.com/TannyJungMC/THT-tree_pack/" + Handcode.tanny_pack_version_name.toLowerCase() + "/version.txt";

		if (MiscOutside.isConnectedToInternet() == false) {

			Misc.sendChatMessage(level, "@a", "red", "THT : Can't update the tree pack right now, as no internet connection.");

		} else {

			if (checkModVersion(level, url) == true) {

				Misc.sendChatMessage(level, "@a", "white", "");
				Misc.sendChatMessage(level, "@a", "gray", "THT : Started the installation, this may take a while.");
				error = "";

				// Delete Old Folders
				{

					deleteOldPackFolder(Handcode.directory_config + "/custom_packs/TannyJung-Tree-Pack");
					deleteOldPackFolder(Handcode.directory_config + "/custom_packs/[INCOMPATIBLE] TannyJung-Tree-Pack");

				}

				if (error.equals("") == true) {createZIP();}
				if (error.equals("") == true) {download();}
				if (error.equals("") == true) {unzip();}
				if (error.equals("") == true) {deleteZIP();}
				if (error.equals("") == true) {renameFolder();}

				if (error.equals("") == false) {

					Misc.sendChatMessage(level, "@a", "red", "THT : Something error during installation, please wait and try again. For some info, it caused by [ " + error + " ].");

				} else {

					Misc.sendChatMessage(level, "@a", "gray", "THT : Install Completed!");
					ConfigRepairAll.start(null);
					ConfigMain.apply(null);

					Misc.sendChatMessage(level, "@a", "white", "");
					FileCount.start(level, 0, 0, 0);
					Misc.sendChatMessage(level, "@a", "white", "");
					PackMessage.start(level);

				}

			}

		}




	}

	private static boolean checkModVersion (LevelAccessor level, String url) {

		boolean return_logic = true;
		double mod_version_url = 0.0;

		// Read URL
		{

			try {

				BufferedReader buffered_reader = new BufferedReader(new InputStreamReader(new URI(url).toURL().openStream()));
				String read_all = "";

				while ((read_all = buffered_reader.readLine()) != null) {

					if (read_all.startsWith("mod_version = ")) {

						mod_version_url = Double.parseDouble(read_all.replace("mod_version = ", ""));

					}

				}
				buffered_reader.close();

			} catch (Exception e) {

				TanshugetreesMod.LOGGER.error(e.getMessage());

			}

        }

		if (Handcode.mod_version != mod_version_url) {

			Misc.sendChatMessage(level, "@a", "red", "THT : You're currently using mod version that does not support to new tree pack version, try update the mod and do it again.");
			TanshugetreesMod.LOGGER.info("Your version is " + Handcode.mod_version + " but tree pack needed " + mod_version_url);

			return_logic = false;

		}

		return return_logic;

	}

	private static void deleteOldPackFolder (String path) {

		if (new File(path).exists() == true) {

			try {

				Files.walk(Paths.get(path)).sorted(Comparator.reverseOrder()).forEach(source -> {

					try {

						Files.delete(source);

					} catch (Exception e) {

						error = "Deleting Old Pack Folder";

					}

				});

			} catch (Exception e) {

				error = "Deleting Old Pack Folder > File Walk Path";

			}

		}

	}

	private static void createZIP () {

		File file = new File(Handcode.directory_config + "/custom_packs/TannyJung-Tree-Pack.zip");
		ZipOutputStream out = null;

		try {

			out = new ZipOutputStream(new FileOutputStream(file));
			out.close();

		} catch (Exception e) {

			error = "Creating ZIP";

		}

	}

	private static void download () {

		String download_from = "https://github.com/TannyJungMC/THT-tree_pack/archive/refs/heads/" + Handcode.tanny_pack_version_name.toLowerCase() + ".zip";
		String download_to = Handcode.directory_config + "/custom_packs/TannyJung-Tree-Pack.zip";

		try {

			BufferedInputStream URL_read_all = new BufferedInputStream(new URI(download_from).toURL().openStream());

			try (FileOutputStream fileOutputStream = new FileOutputStream(download_to)) {

				byte dataBuffer[] = new byte[1024];
				int bytesRead;

				while ((bytesRead = URL_read_all.read(dataBuffer, 0, 1024)) != -1) {

					fileOutputStream.write(dataBuffer, 0, bytesRead);

				}

			}

			TanshugetreesMod.LOGGER.info("THT : Downloaded " + download_from);

		} catch (Exception e) {

			error = "Downloading The Pack From " + download_from;

		}

	}

	private static void unzip () {

		File unzip = new File(Handcode.directory_config + "/custom_packs/TannyJung-Tree-Pack.zip");
		File unzip_to = new File(Handcode.directory_config + "/custom_packs");
		byte[] buffer = new byte[1024];

		try {

			ZipInputStream zis = new ZipInputStream(new FileInputStream(unzip));
			ZipEntry zipEntry = zis.getNextEntry();

			if (unzip.exists() == true) {

				while (zipEntry != null) {

					File newFile = unzip2(unzip_to, zipEntry);

					if (zipEntry.isDirectory()) {

						if (!newFile.isDirectory() && !newFile.mkdirs()) {

							error = "Create Directory " + newFile;

						}

					} else {

						// fix for Windows-created

						File parent = newFile.getParentFile();

						if (!parent.isDirectory() && !parent.mkdirs()) {

							error = "Create Directory " + parent;

						}

						// write file content

						FileOutputStream fos = new FileOutputStream(newFile);
						int len = 0;

						while ((len = zis.read(buffer)) > 0) {

							fos.write(buffer, 0, len);

						}
						fos.close();

					}

					zipEntry = zis.getNextEntry();

				}
				zis.closeEntry();
				zis.close();

			}

		} catch (Exception e) {

			error = "Unzipping";

		}

	}

	public static File unzip2 (File destinationDir, ZipEntry zipEntry) {

		File destFile;
		String destDirPath;
		String destFilePath;
		destFile = new File(destinationDir, zipEntry.getName());

		try {

			destDirPath = destinationDir.getCanonicalPath();
			destFilePath = destFile.getCanonicalPath();

			if (!destFilePath.startsWith(destDirPath + File.separator)) {

				error = "Entry is outside of the target dir " + zipEntry.getName();

			}

		} catch (Exception e) {

			TanshugetreesMod.LOGGER.error(e.getMessage());

		}

		return destFile;

	}

	private static void deleteZIP () {

		File file = new File(Handcode.directory_config + "/custom_packs/TannyJung-Tree-Pack.zip");

		if (file.exists() == true) {

            try {

                Files.walk(file.toPath()).sorted(Comparator.reverseOrder()).forEach(path -> {

                    try {

                        Files.delete(path);

                    } catch (Exception e) {

						error = "Deleting ZIP";

                    }

                });

            } catch (Exception e) {

				error = "Deleting ZIP > File Walk Path";

            }

		}

	}

	private static void renameFolder () {

		File rename_from = new File(Handcode.directory_config + "/custom_packs/THT-tree_pack-" + Handcode.tanny_pack_version_name.toLowerCase());
		File rename_to = new File(Handcode.directory_config + "/custom_packs/TannyJung-Tree-Pack");

		if (rename_from.renameTo(rename_to) == false) {

			error = "Renaming The Folder From " + rename_from + " to " + rename_to;

		}

	}

}