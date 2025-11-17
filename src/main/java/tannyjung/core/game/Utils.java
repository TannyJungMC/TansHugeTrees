package tannyjung.core.game;

import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.ServerScoreboard;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.scores.Objective;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.server.ServerLifecycleHooks;
import tannyjung.core.OutsideUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Comparator;
import java.util.List;

public class Utils {

    public static final String path_game = FMLPaths.GAMEDIR.get().toString();

	public static class misc {

        public static boolean isModLoaded (String id) {

            return ModList.get().isLoaded(id);

        }

        public static int playerCount () {

            return ServerLifecycleHooks.getCurrentServer().getPlayerCount();

        }

        public static boolean customTestBiome (Holder<Biome> biome_center, String config_value) {

            boolean return_logic = false;

            {

                String biome_centerID = Utils.biome.toID(biome_center);

                for (String split : config_value.split(" / ")) {

                    return_logic = true;

                    for (String split2 : split.split(", ")) {

                        String split_get = split2.replaceAll("[#!]", "");

                        {

                            if (split2.contains("#") == false) {

                                if (biome_centerID.equals(split_get) == false) {

                                    return_logic = false;

                                }

                            } else {

                                if (Utils.biome.isTaggedAs(biome_center, split_get) == false) {

                                    return_logic = false;

                                }

                            }

                            if (split2.contains("!") == true) {

                                return_logic = !return_logic;

                            }

                        }

                        if (return_logic == false) {

                            break;

                        }

                    }

                    if (return_logic == true) {

                        break;

                    }

                }

            }

            return return_logic;

        }

        public static boolean customTestBlock (BlockState test_block, String config_value) {

            boolean return_logic = false;

            {

                for (String split : config_value.split(" / ")) {

                    return_logic = true;

                    for (String split2 : split.split(", ")) {

                        String split_get = split2.replaceAll("[#!]", "");

                        {

                            if (split2.contains("#") == false) {

                                if (ForgeRegistries.BLOCKS.getKey(test_block.getBlock()).toString().equals(split_get) == false) {

                                    return_logic = false;

                                }

                            } else {

                                if (Utils.block.isTaggedAs(test_block, split_get) == false) {

                                    return_logic = false;

                                }

                            }

                            if (split2.contains("!") == true) {

                                return_logic = !return_logic;

                            }

                        }

                        if (return_logic == false) {

                            break;

                        }

                    }

                    if (return_logic == true) {

                        break;

                    }

                }

            }

            return return_logic;

        }

		public static void sendChatMessage (ServerLevel level_server, String target, String color, String text) {

			level_server.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(0, 0, 0), Vec2.ZERO, level_server, 4, "", Component.literal(""), level_server.getServer(), null).withSuppressedOutput(), "tellraw " + target + " [{\"text\":\"" + text + "\",\"color\":\"" + color + "\"}]");

		}

		public static String getCurrentDimensionID (Level level) {

			return level.dimension().location().toString();

		}

        public static String getForgeDataFromGiveFile (String path) {

            String return_text = "";
            File file = new File(path);

            if (file.exists() == true && file.isDirectory() == false) {

                StringBuilder data = new StringBuilder();

                // Read File
                {

                    try { BufferedReader buffered_reader = new BufferedReader(new FileReader(file), 65536); String read_all = ""; while ((read_all = buffered_reader.readLine()) != null) {

                        {

                            data.append(read_all);

                        }

                    } buffered_reader.close(); } catch (Exception exception) { OutsideUtils.exception(new Exception(), exception); }

                }

                return_text = data.substring(data.indexOf("ForgeData"), data.length() - 2);

            }

            return return_text;

        }

	}

	public static class command {

		public static void run (ServerLevel level_server, double posX, double posY, double posZ, String command) {

            level_server.getServer().execute(() -> {

                level_server.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(posX, posY, posZ), Vec2.ZERO, level_server, 4, "", Component.literal(""), level_server.getServer(), null).withSuppressedOutput(), command);

            });

		}

		public static void runEntity (Entity entity, String command) {

			if (entity.level() instanceof ServerLevel level_server) {

                level_server.getServer().execute(() -> {

                    level_server.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, entity.position(), entity.getRotationVector(), level_server, 4, entity.getName().getString(), entity.getDisplayName(), level_server.getServer(), entity), command);

                });

			}

		}

		public static boolean result (ServerLevel level_server, int posX, int posY, int posZ, String command) {

			StringBuilder result = new StringBuilder();

			CommandSource data_consumer = new CommandSource() {

				@Override
				public boolean acceptsSuccess() {
					result.append("pass");
					return true;
				}

				@Override
				public void sendSystemMessage(Component component) {
				}

				@Override
				public boolean acceptsFailure() {
					return false;
				}

				@Override
				public boolean shouldInformAdmins() {
					return false;
				}

			};

			level_server.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(data_consumer, new Vec3(posX, posY, posZ), Vec2.ZERO, level_server, 4, "", Component.literal(""), level_server.getServer(), null), command);
			return result.toString().equals("pass");

		}

		public static boolean resultEntity (Entity entity, String command) {

			StringBuilder result = new StringBuilder();

			CommandSource data_consumer = new CommandSource() {

				@Override
				public boolean acceptsSuccess() {
					result.append("pass");
					return true;
				}

				@Override
				public void sendSystemMessage(Component component) {
				}

				@Override
				public boolean acceptsFailure() {
					return false;
				}

				@Override
				public boolean shouldInformAdmins() {
					return false;
				}

			};

			if (entity.level() instanceof ServerLevel level_server) {

				level_server.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(data_consumer, entity.position(), entity.getRotationVector(), level_server, 4, entity.getName().getString(), entity.getDisplayName(), level_server.getServer(), entity), command);

			}

			return result.toString().equals("pass");

		}

		public static String resultCustom (ServerLevel level_server, int posX, int posY, int posZ, String command) {

			StringBuilder result = new StringBuilder();

			CommandSource data_consumer = new CommandSource() {

				@Override
				public void sendSystemMessage(Component component) {

					result.append(component);

				}

				@Override
				public boolean acceptsSuccess() {
					return true;
				}

				@Override
				public boolean acceptsFailure() {
					return true;
				}

				@Override
				public boolean shouldInformAdmins() {
					return false;
				}

			};

			level_server.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(data_consumer, new Vec3(posX, posY, posZ), Vec2.ZERO, level_server, 4, "", Component.literal(""), level_server.getServer(), null), command);
			return result.toString();

		}

        public static String summonEntity (String id, String tag, String name, String custom) {

            StringBuilder return_text = new StringBuilder();

            return_text
                    .append("summon ")
                    .append(id)
                    .append(" ~ ~ ~ {Tags:[\"TANNYJUNG\",\"")
            ;

            if (tag.equals("") == false) {

                return_text.append(tag.replace(" / ", "\",\""));

            }

            return_text.append("\"]");

            if (name.equals("") == false) {

                return_text
                        .append(",CustomName:'{\"text\":\"")
                        .append(name)
                        .append("\"}'")
                ;

            }

            if (custom.equals("") == false) {

                return_text
                        .append(",")
                        .append(custom)
                ;

            }

            return return_text + "}";

        }

	}

	public static class score {

		public static int get (ServerLevel level_server, String objective, String player) {

			ServerScoreboard score = level_server.getServer().getScoreboard();
			Objective objective_get = score.getObjective(objective);

			return score.getOrCreatePlayerScore(player, objective_get).getScore();

		}

		public static void set (ServerLevel level_server, String objective, String player, int value) {

			ServerScoreboard score = level_server.getServer().getScoreboard();
			Objective objective_get = score.getObjective(objective);

			score.getOrCreatePlayerScore(player, objective_get).setScore(value);

		}

		public static void add (ServerLevel level_server, String objective, String player, int value) {

			ServerScoreboard score = level_server.getServer().getScoreboard();
			Objective objective_get = score.getObjective(objective);
			int old_value = get(level_server, objective, player);

			score.getOrCreatePlayerScore(player, objective_get).setScore(old_value + value);

		}

	}

	public static class biome {

		public static String toID (Holder<Biome> biome) {

			String return_text = biome.toString().replace("Reference{ResourceKey[minecraft:worldgen/biome / ", "");
			return return_text.substring(0, return_text.indexOf("]"));

		}

		public static boolean isTaggedAs (Holder<Biome> biome, String tag) {

			try {

				return biome.is(TagKey.create(Registries.BIOME, ResourceLocation.parse(tag)));

			} catch (Exception exception) {

				OutsideUtils.exception(new Exception(), exception);

			}

			return false;

		}

	}

	public static class block {

		public static boolean isTaggedAs (BlockState block, String tag) {

			try {

				return block.is(BlockTags.create(ResourceLocation.parse(tag)));

			} catch (Exception exception) {

				OutsideUtils.exception(new Exception(), exception);

			}

			return false;

		}

		public static BlockState fromText (String data) {

			BlockState block = Blocks.AIR.defaultBlockState();

			try {

				String id = data;

				// Get Block ID
				{

					if (data.endsWith("}") == true) {

						id = data.substring(0, data.indexOf("{"));

					}

					if (data.endsWith("]") == true) {

						id = data.substring(0, data.indexOf("["));

					}

					block = ForgeRegistries.BLOCKS.getValue(ResourceLocation.parse(id)).defaultBlockState();

				}

				if (data.endsWith("}") == true) {

					data = data.substring(0, data.indexOf("{"));

				}

				if (data.endsWith("]") == true) {

					String[] properties = data.substring(data.indexOf("[") + 1, data.length() - 1).split(",");

					for (String property_data : properties) {

						String[] get = property_data.split("=");
						Property<?> property = block.getBlock().getStateDefinition().getProperty(get[0]);

						if (property instanceof BooleanProperty) {

							block = propertyBooleanSet(block, get[0], Boolean.parseBoolean(get[1]));

						} else if (property instanceof IntegerProperty) {

							block = propertyIntegerSet(block, get[0], Integer.parseInt(get[1]));

						} else if (property instanceof EnumProperty<?>) {

							block = propertyEnumSet(block, get[0], get[1]);

						}

					}

				}

			} catch (Exception exception) {

				OutsideUtils.exception(new Exception(), exception);

			}

			return block;

		}

		public static String toText (BlockState block) {

			return block.toString().replace("Block{", "").replace("}", "");

		}

		public static String toTextID (BlockState block) {

			String return_text = block.getBlock().toString();
			return return_text.substring("Block{".length(), return_text.length() - 1);

		}

		public static String textFromItemText (String text) {

			String id = text.substring(0, text.indexOf("{"));
			String convert = text.substring(text.indexOf("ForgeData"), text.length() - 1);
			return id + "{" + convert;

		}

		public static boolean propertyBooleanGet (BlockState block, String name) {

			Property<?> property = block.getBlock().getStateDefinition().getProperty(name);

			if (property instanceof BooleanProperty) {

				return Boolean.parseBoolean(block.getValue(property).toString());

			}

			return false;

		}

		public static int propertyIntegerGet (BlockState block, String name) {

			Property<?> property = block.getBlock().getStateDefinition().getProperty(name);

			if (property instanceof IntegerProperty) {

				return Integer.parseInt(block.getValue(property).toString());

			}

			return 0;

		}

		public static String propertyEnumGet (BlockState block, String name) {

			Property<?> property = block.getBlock().getStateDefinition().getProperty(name);

			if (property instanceof EnumProperty<?>) {

				return block.getValue(property).toString();

			}

			return "";

		}

		public static BlockState propertyBooleanSet (BlockState block, String name, boolean value) {

			Property<?> property = block.getBlock().getStateDefinition().getProperty(name);

            if (block.hasProperty(property) == true) {

                if (property instanceof BooleanProperty property_instance) {

                    block = block.setValue(property_instance, value);

                }

            }

			return block;

		}

		public static BlockState propertyIntegerSet (BlockState block, String name, int value) {

			Property<?> property = block.getBlock().getStateDefinition().getProperty(name);

            if (block.hasProperty(property) == true) {

                if (property instanceof IntegerProperty property_instance) {

                    block = block.setValue(property_instance, value);

                }

            }

			return block;

		}

		public static BlockState propertyEnumSet (BlockState block, String name, String value) {

			if (name.equals("facing") == true) {

				if (block.hasProperty(DirectionalBlock.FACING) == true) {

					block = block.setValue(DirectionalBlock.FACING, Direction.valueOf(value.toUpperCase()));

				} else if (block.hasProperty(HorizontalDirectionalBlock.FACING) == true) {

					block = block.setValue(HorizontalDirectionalBlock.FACING, Direction.valueOf(value.toUpperCase()));

				}

			}

			return block;

		}


	}

	public static class entity {

		public static List<Entity> getAllAt (Level level, int posX, int posY, int posZ) {

			Vec3 center = new Vec3((posX + 0.5), (posY + 0.5), (posZ + 0.5));
			return level.getEntitiesOfClass(Entity.class, new AABB(center, center).inflate(3 / 2d), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(center))).toList();

		}

		public static boolean isCreativeMode (Entity entity) {

			if (entity instanceof Player player) {

				return player.getAbilities().instabuild;

			}

			return false;

		}

		public static ItemStack itemGet (Entity entity, EquipmentSlot equipment_slot) {

			if (entity instanceof LivingEntity living_entity) {

				return living_entity.getItemBySlot(equipment_slot);

			}

			return ItemStack.EMPTY;

		}

		public static void itemCountSet (Entity entity, EquipmentSlot equipment_slot, int value) {

			if (entity instanceof LivingEntity living_entity) {

				ItemStack item = living_entity.getItemBySlot(equipment_slot);
				item.setCount(value);

			}

		}

		public static void itemCountAdd (Entity entity, EquipmentSlot equipment_slot, int value) {

			if (entity instanceof LivingEntity living_entity) {

				ItemStack item = living_entity.getItemBySlot(equipment_slot);
				item.setCount(item.getCount() + value);

			}

		}

	}

}