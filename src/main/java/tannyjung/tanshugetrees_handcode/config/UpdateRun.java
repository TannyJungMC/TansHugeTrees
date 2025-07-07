package tannyjung.tanshugetrees_handcode.config;

import net.minecraft.world.level.LevelAccessor;

import java.io.*;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.concurrent.CompletableFuture;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import tannyjung.tanshugetrees.TanshugetreesMod;
import tannyjung.tanshugetrees_handcode.Handcode;
;
import tannyjung.core.GameUtils;
import tannyjung.core.MiscUtils;

public class UpdateRun {

	public static boolean install_pause_systems = false;

    public static void start (LevelAccessor level) {

		CompletableFuture.runAsync(() -> {

			if (MiscUtils.isConnectedToInternet() == false) {

				GameUtils.misc.sendChatMessage(level, "@a", "red", "THT : Can't update right now, as the mod can't connect to the internet.");

			} else {

				if (checkModVersion(level, "https://raw.githubusercontent.com/TannyJungMC/THT-tree_pack/" + Handcode.tanny_pack_version_name.toLowerCase() + "/version.txt") == true) {

					install_pause_systems = true;

					TanshugetreesMod.queueServerWork(20, () -> {

						GameUtils.misc.sendChatMessage(level, "@a", "white", "");
						GameUtils.misc.sendChatMessage(level, "@a", "gray", "THT : Started the installation, this may take a while.");

						// Delete Old Folders
						{

							if (deleteOldPackFolder(level, Handcode.directory_config + "/custom_packs/TannyJung-Tree-Pack") == false) {
								return;
							}
							if (deleteOldPackFolder(level, Handcode.directory_config + "/custom_packs/[INCOMPATIBLE] TannyJung-Tree-Pack") == false) {
								return;
							}

						}

						// Systems
						{

							if (createZIP(level) == false) {
								return;
							}
							if (createZIP(level) == false) {
								return;
							}
							if (download(level) == false) {
								return;
							}
							if (unzip(level) == false) {
								return;
							}
							if (deleteZIP(level) == false) {
								return;
							}
							if (renameFolder(level) == false) {
								return;
							}

						}

						GameUtils.misc.sendChatMessage(level, "@a", "gray", "THT : Install Completed!");

						ConfigRepairAll.start(level, true);
						ConfigMain.apply(level);

						GameUtils.misc.sendChatMessage(level, "@a", "white", "");
						FileCount.start(level);
						GameUtils.misc.sendChatMessage(level, "@a", "white", "");
						PackMessage.start(level);

						install_pause_systems = false;

					});

				}

			}

		});

	}

	private static boolean checkModVersion (LevelAccessor level, String url) {

		boolean return_logic = true;
		double data_structure_version_url = 0.0;

		// Read URL
		{

			try {

				BufferedReader buffered_reader = new BufferedReader(new InputStreamReader(new URI(url).toURL().openStream()));
				String read_all = "";

				while ((read_all = buffered_reader.readLine()) != null) {

					if (read_all.startsWith("data_structure_version = ")) {

						data_structure_version_url = Double.parseDouble(read_all.replace("data_structure_version = ", ""));

					}

				}
				buffered_reader.close();

			} catch (Exception exception) {

				MiscUtils.exception(new Exception(), exception);

			}

        }

		if (Handcode.data_structure_version_pack > data_structure_version_url) {

			return_logic = false;
			GameUtils.misc.sendChatMessage(level, "@a", "red", "THT : Seems like you update the mod very fast! TannyJung's Tree Pack (" + Handcode.tanny_pack_version_name + ") haven't updated to support this mod version yet, please wait a bit for the update to available.");

		} else if (Handcode.data_structure_version_pack < data_structure_version_url) {

			return_logic = false;
			GameUtils.misc.sendChatMessage(level, "@a", "red", "THT : You're currently using mod version that does not support to new tree pack version, try update the mod and do it again.");

		}

		if (return_logic == false) {

			TanshugetreesMod.LOGGER.info("Your mod data structure version is " + Handcode.data_structure_version_pack + " but the pack is " + data_structure_version_url);

		}

		return return_logic;

	}

	private static boolean deleteOldPackFolder (LevelAccessor level, String path) {

		if (new File(path).exists() == true) {

			try {

				Files.walk(Paths.get(path)).sorted(Comparator.reverseOrder()).forEach(source -> {

					source.toFile().delete();

				});

			} catch (Exception ignored) {

				GameUtils.misc.sendChatMessage(level, "@a", "red", "THT : Something error during installation. Try moving around or lower render distance, then try again.");
				return false;

			}

		}
		
		return true;

	}

	private static boolean createZIP (LevelAccessor level) {

		File file = new File(Handcode.directory_config + "/custom_packs/TannyJung-Tree-Pack.zip");
		ZipOutputStream out = null;

		try {

			out = new ZipOutputStream(new FileOutputStream(file));
			out.close();

		} catch (Exception ignored) {

			GameUtils.misc.sendChatMessage(level, "@a", "red", "THT : Something error during installation. Try moving around or lower render distance, then try again.");
			return false;

		}
		
		return true;

	}

	private static boolean download (LevelAccessor level) {

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

			TanshugetreesMod.LOGGER.info("Downloaded " + download_from);

		} catch (Exception ignored) {

			GameUtils.misc.sendChatMessage(level, "@a", "red", "THT : Something error during installation. Try moving around or lower render distance, then try again.");
			return false;

		}

		return true;

	}

	private static boolean unzip (LevelAccessor level) {

		File unzip = new File(Handcode.directory_config + "/custom_packs/TannyJung-Tree-Pack.zip");
		File unzip_to = new File(Handcode.directory_config + "/custom_packs");
		byte[] buffer = new byte[1024];

		try {

			ZipInputStream zis = new ZipInputStream(new FileInputStream(unzip));
			ZipEntry zipEntry = zis.getNextEntry();

			if (unzip.exists() == true) {

				while (zipEntry != null) {

					File newFile = new File(unzip_to + "/" + zipEntry.getName());

					if (zipEntry.isDirectory()) {

						if (!newFile.isDirectory() && !newFile.mkdirs()) {

							GameUtils.misc.sendChatMessage(level, "@a", "red", "THT : Something error during installation. Try moving around or lower render distance, then try again.");
							return false;

						}

					} else {

						// fix for Windows-created

						File parent = newFile.getParentFile();

						if (!parent.isDirectory() && !parent.mkdirs()) {

							GameUtils.misc.sendChatMessage(level, "@a", "red", "THT : Something error during installation. Try moving around or lower render distance, then try again.");
							return false;

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

		} catch (Exception ignored) {

			GameUtils.misc.sendChatMessage(level, "@a", "red", "THT : Something error during installation. Try moving around or lower render distance, then try again.");
			return false;

		}

		return true;

	}

	private static boolean deleteZIP (LevelAccessor level) {

		File file = new File(Handcode.directory_config + "/custom_packs/TannyJung-Tree-Pack.zip");

		if (file.exists() == true) {

            try {

                Files.walk(file.toPath()).sorted(Comparator.reverseOrder()).forEach(path -> {

                    try {

                        Files.delete(path);

					} catch (Exception exception) {

						MiscUtils.exception(new Exception(), exception);

                    }

                });

			} catch (Exception ignored) {

				GameUtils.misc.sendChatMessage(level, "@a", "red", "THT : Something error during installation. Try moving around or lower render distance, then try again.");
				return false;

			}

		}

		return true;

	}

	private static boolean renameFolder (LevelAccessor level) {

		File rename_from = new File(Handcode.directory_config + "/custom_packs/THT-tree_pack-" + Handcode.tanny_pack_version_name.toLowerCase());
		File rename_to = new File(Handcode.directory_config + "/custom_packs/TannyJung-Tree-Pack");

		if (rename_from.renameTo(rename_to) == false) {

			GameUtils.misc.sendChatMessage(level, "@a", "red", "THT : Something error during installation. Try moving around or lower render distance, then try again.");
			return false;

		}

        return true;

    }

}