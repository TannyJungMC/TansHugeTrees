package tannyjung.tanshugetrees.config;

import net.minecraft.server.level.ServerLevel;

import java.io.*;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import net.minecraft.world.level.LevelAccessor;
import tannyjung.core.OutsideUtils;
import tannyjung.core.game.Utils;
import tannyjung.tanshugetrees.Handcode;
;

public class PackUpdate {

    public static void start (LevelAccessor level_accessor) {

        Handcode.thread_main.submit(() -> {

            if (level_accessor instanceof ServerLevel level_server) {

                if (OutsideUtils.isConnectedToInternet() == false) {

                    Utils.misc.sendChatMessage(level_server, "@a", "red", "THT : Can't update right now, as the mod can't connect to the internet.");

                } else {

                    if (checkModVersion(level_server, "https://raw.githubusercontent.com/TannyJungMC/THT-tree_pack/" + Handcode.tanny_pack_version_name.toLowerCase() + "/version.txt") == true) {

                        Utils.misc.sendChatMessage(level_server, "@a", "white", "");
                        Utils.misc.sendChatMessage(level_server, "@a", "gray", "THT : Started the installation, this may take a while.");

                        Handcode.system_pause = true;
                        run(level_accessor, level_server);

                    }

                }

            }

        });

	}

    private static void run (LevelAccessor level_accessor, ServerLevel level_server) {

        // Delete Old Folders
        {

            if (deleteOldPackFolder(level_server, Handcode.path_config + "/custom_packs/#TannyJung-Main-Pack") == false) {
                return;
            }
            if (deleteOldPackFolder(level_server, Handcode.path_config + "/custom_packs/[INCOMPATIBLE] #TannyJung-Main-Pack") == false) {
                return;
            }

        }

        // Systems
        {

            if (createZIP(level_server) == false) {
                return;
            }
            if (createZIP(level_server) == false) {
                return;
            }
            if (download(level_server) == false) {
                return;
            }
            if (unzip(level_server) == false) {
                return;
            }
            if (deleteZIP(level_server) == false) {
                return;
            }
            if (renameFolder(level_server) == false) {
                return;
            }

        }

        Utils.misc.sendChatMessage(level_server, "@a", "gray", "THT : Install Completed!");
        Utils.misc.sendChatMessage(level_server, "@a", "white", "");
        CustomPackFileCount.start(level_server);
        Utils.misc.sendChatMessage(level_server, "@a", "white", "");
        message(level_server);

        Handcode.runRestart(level_accessor, false);

    }

	public static void message (ServerLevel level_server) {

		File file = new File(Handcode.path_config + "/custom_packs/#TannyJung-Main-Pack/message.txt");
		StringBuilder message = new StringBuilder();

		if (file.exists() == true && file.isDirectory() == false) {

			{

				try { BufferedReader buffered_reader = new BufferedReader(new FileReader(file), 65536); String read_all = ""; while ((read_all = buffered_reader.readLine()) != null) {

					{

						message.append(read_all);

					}

				} buffered_reader.close(); } catch (Exception exception) { OutsideUtils.exception(new Exception(), exception); }

			}

			Utils.command.run(level_server, 0, 0, 0, message.toString());

		}

	}

	private static boolean checkModVersion (ServerLevel level_server, String url) {

		boolean return_logic = true;
		int data_structure_version_url = 0;

		// Read URL
		{

			try { BufferedReader buffered_reader = new BufferedReader(new InputStreamReader(new URI(url).toURL().openStream()), 65536); String read_all = ""; while ((read_all = buffered_reader.readLine()) != null) {

				{

					if (read_all.startsWith("data_structure_version = ")) {

						data_structure_version_url = Integer.parseInt(read_all.replace("data_structure_version = ", ""));

					}

				}

			} buffered_reader.close(); } catch (Exception exception) { OutsideUtils.exception(new Exception(), exception); }

        }

		if (Handcode.DATA_STRUCTURE_VERSION > data_structure_version_url) {

			return_logic = false;
			Utils.misc.sendChatMessage(level_server, "@a", "red", "THT : Seems like you update the mod very fast! TannyJung's Main Pack (" + Handcode.tanny_pack_version_name + ") haven't updated to support this mod version yet, please wait a bit for the update to be available.");

		} else if (Handcode.DATA_STRUCTURE_VERSION < data_structure_version_url) {

			return_logic = false;
			Utils.misc.sendChatMessage(level_server, "@a", "red", "THT : You're currently using mod version that does not support to new version of the pack. Try update the mod and do it again.");

		}

		if (return_logic == false) {

			Handcode.logger.info("Your mod data structure version is " + Handcode.DATA_STRUCTURE_VERSION + " but the pack is " + data_structure_version_url);

		}

		return return_logic;

	}

	private static boolean deleteOldPackFolder (ServerLevel level_server, String path) {

		if (new File(path).exists() == true) {

			try {

				Files.walk(Path.of(path)).sorted(Comparator.reverseOrder()).forEach(source -> {

					source.toFile().delete();

				});

			} catch (Exception ignored) {

				Utils.misc.sendChatMessage(level_server, "@a", "red", "THT : Something error during installation. Try moving around or lower render distance, then try again.");
				return false;

			}

		}
		
		return true;

	}

	private static boolean createZIP (ServerLevel level_server) {

		File file = new File(Handcode.path_config + "/custom_packs/#TannyJung-Main-Pack.zip");
		ZipOutputStream out = null;

		try {

			out = new ZipOutputStream(new FileOutputStream(file));
			out.close();

		} catch (Exception ignored) {

			Utils.misc.sendChatMessage(level_server, "@a", "red", "THT : Something error during installation. Try moving around or lower render distance, then try again.");
			return false;

		}
		
		return true;

	}

	private static boolean download (ServerLevel level_server) {

		String download_from = "https://github.com/TannyJungMC/THT-tree_pack/archive/refs/heads/" + Handcode.tanny_pack_version_name.toLowerCase() + ".zip";
		String download_to = Handcode.path_config + "/custom_packs/#TannyJung-Main-Pack.zip";

		try {

			Handcode.logger.info("Downloading ZIP from " + download_from);
			BufferedInputStream URL_read_all = new BufferedInputStream(new URI(download_from).toURL().openStream());

			try (FileOutputStream fileOutputStream = new FileOutputStream(download_to)) {

				byte dataBuffer[] = new byte[1024];
				int bytesRead;

				while ((bytesRead = URL_read_all.read(dataBuffer, 0, 1024)) != -1) {

					fileOutputStream.write(dataBuffer, 0, bytesRead);

				}

			}

			Handcode.logger.info("Download Completed!");

		} catch (Exception ignored) {

			Utils.misc.sendChatMessage(level_server, "@a", "red", "THT : Something error during installation. Try moving around or lower render distance, then try again.");
			return false;

		}

		return true;

	}

	private static boolean unzip (ServerLevel level_server) {

		File unzip = new File(Handcode.path_config + "/custom_packs/#TannyJung-Main-Pack.zip");
		File unzip_to = new File(Handcode.path_config + "/custom_packs");
		byte[] buffer = new byte[1024];

		try {

			ZipInputStream zis = new ZipInputStream(new FileInputStream(unzip));
			ZipEntry zipEntry = zis.getNextEntry();

			if (unzip.exists() == true) {

				while (zipEntry != null) {

					File newFile = new File(unzip_to + "/" + zipEntry.getName());

					if (zipEntry.isDirectory()) {

						if (!newFile.isDirectory() && !newFile.mkdirs()) {

							Utils.misc.sendChatMessage(level_server, "@a", "red", "THT : Something error during installation. Try moving around or lower render distance, then try again.");
							return false;

						}

					} else {

						// fix for Windows-created

						File parent = newFile.getParentFile();

						if (!parent.isDirectory() && !parent.mkdirs()) {

							Utils.misc.sendChatMessage(level_server, "@a", "red", "THT : Something error during installation. Try moving around or lower render distance, then try again.");
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

			Utils.misc.sendChatMessage(level_server, "@a", "red", "THT : Something error during installation. Try moving around or lower render distance, then try again.");
			return false;

		}

		return true;

	}

	private static boolean deleteZIP (ServerLevel level_server) {

		File file = new File(Handcode.path_config + "/custom_packs/#TannyJung-Main-Pack.zip");

		if (file.exists() == true) {

            try {

                Files.walk(file.toPath()).sorted(Comparator.reverseOrder()).forEach(path -> {

                    try {

                        Files.delete(path);

					} catch (Exception exception) {

						OutsideUtils.exception(new Exception(), exception);

                    }

                });

			} catch (Exception ignored) {

				Utils.misc.sendChatMessage(level_server, "@a", "red", "THT : Something error during installation. Try moving around or lower render distance, then try again.");
				return false;

			}

		}

		return true;

	}

	private static boolean renameFolder (ServerLevel level_server) {

		File rename_from = new File(Handcode.path_config + "/custom_packs/THT-tree_pack-" + Handcode.tanny_pack_version_name.toLowerCase());
		File rename_to = new File(Handcode.path_config + "/custom_packs/#TannyJung-Main-Pack");

		if (rename_from.renameTo(rename_to) == false) {

			Utils.misc.sendChatMessage(level_server, "@a", "red", "THT : Something error during installation. Try moving around or lower render distance, then try again.");
			return false;

		}

        return true;

    }

}