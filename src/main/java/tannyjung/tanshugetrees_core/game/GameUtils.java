package tannyjung.tanshugetrees_core.game;

import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.TagParser;
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
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.scores.Objective;
import tannyjung.tanshugetrees_core.outside.OutsideUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Comparator;
import java.util.List;

/*
(Forge)
import net.minecraftforge.fml.ModList;
import net.minecraftforge.server.ServerLifecycleHooks;
import net.minecraft.world.level.chunk.ChunkStatus;
(NeoForge)
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import net.minecraft.world.level.chunk.status.ChunkStatus;
*/
import net.minecraftforge.fml.ModList;
import net.minecraftforge.server.ServerLifecycleHooks;
import net.minecraft.world.level.chunk.ChunkStatus;

public class GameUtils {

	public static class misc {

        public static boolean isModLoaded (String id) {

            return ModList.get().isLoaded(id);

        }

        public static int getPlayerCount () {

            return ServerLifecycleHooks.getCurrentServer().getPlayerCount();

        }

        public static boolean testCustomBiome (Holder<Biome> biome_center, String config_value) {

            boolean return_logic = false;

            if (config_value.equals("all") == true) {

                return_logic = true;

            } else {

                String biome_centerID = GameUtils.biome.toID(biome_center);

                for (String split : config_value.split(" / ")) {

                    return_logic = true;

                    for (String split2 : split.split(", ")) {

                        String split_get = split2.replaceAll("[#!]", "");

                        {

                            if (split2.startsWith("#") == true || split2.startsWith("!#") == true) {

                                if (GameUtils.biome.isTaggedAs(biome_center, split_get) == false) {

                                    return_logic = false;

                                }

                            } else {

                                if (biome_centerID.equals(split_get) == false) {

                                    return_logic = false;

                                }

                            }

                            if (split2.startsWith("!") == true) {

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

        public static boolean testCustomBlock (BlockState test_block, String config_value) {

            boolean return_logic = false;

            if (config_value.equals("all") == true) {

                return_logic = true;

            } else {

                String value = "";

                for (String split : config_value.split(" / ")) {

                    return_logic = true;

                    for (String split2 : split.split(", ")) {

                        value = split2.replaceAll("[#!]", "");

                        {

                            if (split2.startsWith("#") == true || split2.startsWith("!#") == true) {

                                if (GameUtils.block.isTaggedAs(test_block, value) == false) {

                                    return_logic = false;

                                }

                            } else {

                                if (test_block.equals(block.fromText(value)) == false) {

                                    return_logic = false;

                                }

                            }

                            if (split2.startsWith("!") == true) {

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

		public static void sendChatMessage (ServerLevel level_server, Entity entity, String target, String color, String text) {

            command.run(false, level_server, 0, 0, 0, "tellraw " + target + " [{\"text\":\"" + text + "\",\"color\":\"" + color + "\"}]");

        }

        public static void sendChatMessagePrivate (Entity entity, String color, String text) {

            command.runEntity(entity, "tellraw @s [{\"text\":\"" + text + "\",\"color\":\"" + color + "\"}]");

        }

		public static String getCurrentDimensionID (Level level) {

			/*
			(Forge)
			return level.dimension().location().toString();
			(NeoForge)
			return level.dimension().identifier().toString();
			*/
			return level.dimension().location().toString();

		}

        public static String getForgeDataFromFile (String path) {

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

                    } buffered_reader.close(); } catch (Exception exception) { OutsideUtils.exception(new Exception(), exception, ""); }

                }

                return_text = data.substring(data.indexOf("ForgeData"), data.length() - 2);

            }

            return return_text;

        }

	}

	public static class command {

		public static void run (boolean safe_mode, ServerLevel level_server, double posX, double posY, double posZ, String command) {

			/*
			(Forge)
			Runnable runnable = () -> level_server.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(posX, posY, posZ), Vec2.ZERO, level_server, 4, "", Component.literal(""), level_server.getServer(), null).withSuppressedOutput(), command);
			(NeoForge)
			Runnable runnable = () -> level_server.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(posX, posY, posZ), Vec2.ZERO, level_server, PermissionSet.ALL_PERMISSIONS, "", Component.literal(""), level_server.getServer(), null).withSuppressedOutput(), command);
			*/
            Runnable runnable = () -> level_server.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(posX, posY, posZ), Vec2.ZERO, level_server, 4, "", Component.literal(""), level_server.getServer(), null).withSuppressedOutput(), command);

            if (safe_mode == true) {

                level_server.getServer().execute(runnable);

            } else {

                runnable.run();

            }

		}

		public static void runEntity (Entity entity, String command) {

            if (entity.level() instanceof ServerLevel level_server) {

				/*
				(Forge)
				level_server.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, entity.position(), entity.getRotationVector(), level_server, 4, entity.getName().getString(), entity.getDisplayName(), level_server.getServer(), entity), command);
				(NeoForge)
				level_server.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, entity.position(), entity.getRotationVector(), level_server, PermissionSet.ALL_PERMISSIONS, entity.getName().getString(), entity.getDisplayName(), level_server.getServer(), entity), command);
				*/
                level_server.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, entity.position(), entity.getRotationVector(), level_server, 4, entity.getName().getString(), entity.getDisplayName(), level_server.getServer(), entity), command);

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

			/*
			(Forge)
			level_server.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(data_consumer, new Vec3(posX, posY, posZ), Vec2.ZERO, level_server, 4, "", Component.literal(""), level_server.getServer(), null), command);
			(NeoForge)
			level_server.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(data_consumer, new Vec3(posX, posY, posZ), Vec2.ZERO, level_server, PermissionSet.ALL_PERMISSIONS, "", Component.literal(""), level_server.getServer(), null), command);
			*/
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

            if (tag.isEmpty() == false) {

                return_text.append(tag.replace(" / ", "\",\""));

            }

            return_text.append("\"]");

            if (name.isEmpty() == false) {

                return_text
                        .append(",CustomName:'{\"text\":\"")
                        .append(name)
                        .append("\"}'")
                ;

            }

            if (custom.isEmpty() == false) {

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

			/*
			(Forge)
			return score.getOrCreatePlayerScore(player, objective_get).getScore();
			(NeoForge)
			return score.getOrCreatePlayerScore(ScoreHolder.forNameOnly(player), objective_get, false).get();
			*/
			return score.getOrCreatePlayerScore(player, objective_get).getScore();

		}

		public static void set (ServerLevel level_server, String objective, String player, int value) {

			ServerScoreboard score = level_server.getServer().getScoreboard();
			Objective objective_get = score.getObjective(objective);

			/*
			(Forge)
			score.getOrCreatePlayerScore(player, objective_get).setScore(value);
			(NeoForge)
			score.getOrCreatePlayerScore(ScoreHolder.forNameOnly(player), objective_get, false).set(value);
			*/
			score.getOrCreatePlayerScore(player, objective_get).setScore(value);

		}

		public static void add (ServerLevel level_server, String objective, String player, int value) {

			ServerScoreboard score = level_server.getServer().getScoreboard();
			Objective objective_get = score.getObjective(objective);
			int old_value = get(level_server, objective, player);

			/*
			(Forge)
			score.getOrCreatePlayerScore(player, objective_get).setScore(old_value + value);
			(NeoForge)
			score.getOrCreatePlayerScore(ScoreHolder.forNameOnly(player), objective_get, false).set(old_value + value);
			*/
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

				OutsideUtils.exception(new Exception(), exception, "");

			}

			return false;

		}

	}

	public static class block {

		public static boolean isTaggedAs (BlockState block, String tag) {

			try {

				return block.is(BlockTags.create(ResourceLocation.parse(tag)));

			} catch (Exception exception) {

				OutsideUtils.exception(new Exception(), exception, "");

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

					/*
					(Forge)
					block = BuiltInRegistries.BLOCK.get(ResourceLocation.parse(id)).defaultBlockState();
					(NeoForge)
					block = BuiltInRegistries.BLOCK.get(Identifier.parse(id)).get().value().defaultBlockState();
					*/
					block = BuiltInRegistries.BLOCK.get(ResourceLocation.parse(id)).defaultBlockState();

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

				OutsideUtils.exception(new Exception(), exception, "");

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

			} else if (name.equals("type") == true) {

                if (block.hasProperty(SlabBlock.TYPE) == true) {

                    block = block.setValue(SlabBlock.TYPE, SlabType.valueOf(value.toUpperCase()));

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

		public static ItemStack getItemSlot (Entity entity, EquipmentSlot equipment_slot) {

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

	public static class nbt {

		public static class entity {

			public static String getText (Entity entity, String name) {

				/*
				(Forge)
				return entity.getPersistentData().getString(name);
				(NeoForge)
				return entity.getPersistentData().getString(name).get();
				*/
				return entity.getPersistentData().getString(name);

			}

			public static Boolean getLogic (Entity entity, String name) {

				/*
				(Forge)
				return entity.getPersistentData().getBoolean(name);
				(NeoForge)
				return entity.getPersistentData().getBoolean(name).get();
				*/
				return entity.getPersistentData().getBoolean(name);

			}

			public static double getNumber (Entity entity, String name) {

				/*
				(Forge)
				return entity.getPersistentData().getDouble(name);
				(NeoForge)
				return entity.getPersistentData().getDouble(name).get();
				*/
				return entity.getPersistentData().getDouble(name);

			}

			public static double[] getListNumber (Entity entity, String name) {

				/*
				(Forge)
				ListTag list = entity.getPersistentData().getList(name, Tag.TAG_DOUBLE);
				(NeoForge)
				ListTag list = entity.getPersistentData().getList(name).get();
				*/
				ListTag list = entity.getPersistentData().getList(name, Tag.TAG_DOUBLE);

				double[] convert = new double[list.size()];

				for (int count = 0; count <= list.size() - 1; count++) {

					/*
					(Forge)
					convert[count] = list.getDouble(count);
					(NeoForge)
					convert[count] = list.getDouble(count).get();
					*/
					convert[count] = list.getDouble(count);

				}

				return convert;

			}

			public static double[] getListNumberFloat (Entity entity, String name) {

				/*
				(Forge)
				ListTag list = entity.getPersistentData().getList(name, Tag.TAG_FLOAT);
				(NeoForge)
				ListTag list = entity.getPersistentData().getList(name).get();
				*/
				ListTag list = entity.getPersistentData().getList(name, Tag.TAG_FLOAT);

				double[] convert = new double[list.size()];

				for (int count = 0; count <= list.size() - 1; count++) {

					/*
					(Forge)
					convert[count] = list.getFloat(count);
					(NeoForge)
					convert[count] = list.getFloat(count).get();
					*/
					convert[count] = list.getFloat(count);

				}

				return convert;

			}

			public static void setText (Entity entity, String name, String value) {

				entity.getPersistentData().putString(name, value);

			}

			public static void setLogic (Entity entity, String name, boolean value) {

				entity.getPersistentData().putBoolean(name, value);

			}

			public static void setNumber (Entity entity, String name, double value) {

				entity.getPersistentData().putDouble(name, value);

			}

			public static void addNumber (Entity entity, String name, double value) {

				/*
				(Forge)
				entity.getPersistentData().putDouble(name, entity.getPersistentData().getDouble(name) + value);
				(NeoForge)
				entity.getPersistentData().putDouble(name, entity.getPersistentData().getDouble(name).get() + value);
				*/
				entity.getPersistentData().putDouble(name, entity.getPersistentData().getDouble(name) + value);

			}

		}

		public static class block {

			public static String getText (LevelAccessor level_accessor, BlockPos pos, String name) {

				return new Object() {

					public String getValue (LevelAccessor level_accessor, BlockPos pos, String name) {

						BlockEntity blockEntity = level_accessor.getBlockEntity(pos);

						if (blockEntity != null) {

							/*
							(Forge)
							return blockEntity.getPersistentData().getString(name);
							(NeoForge)
							return blockEntity.getPersistentData().getString(name).get();
							*/
							return blockEntity.getPersistentData().getString(name);

						}

						return "";

					}

				}.getValue(level_accessor, pos, name);

			}

			public static double getNumber (LevelAccessor level_accessor, BlockPos pos, String name) {

				return new Object() {

					public double getValue (LevelAccessor level_accessor, BlockPos pos, String name) {

						BlockEntity blockEntity = level_accessor.getBlockEntity(pos);

						if (blockEntity != null) {

							/*
							(Forge)
							return blockEntity.getPersistentData().getDouble(name);
							(NeoForge)
							return blockEntity.getPersistentData().getDouble(name).get();
							*/
							return blockEntity.getPersistentData().getDouble(name);

						}

						return 0.0;

					}

				}.getValue(level_accessor, pos, name);

			}

			public static boolean getLogic (LevelAccessor level_accessor, BlockPos pos, String name) {

				return new Object() {

					public boolean getValue (LevelAccessor level_accessor, BlockPos pos, String name) {

						BlockEntity blockEntity = level_accessor.getBlockEntity(pos);

						if (blockEntity != null) {

							/*
							(Forge)
							return blockEntity.getPersistentData().getBoolean(name);
							(NeoForge)
							return blockEntity.getPersistentData().getBoolean(name).get();
							*/
							return blockEntity.getPersistentData().getBoolean(name);

						}

						return false;

					}

				}.getValue(level_accessor, pos, name);

			}

			public static void setText (LevelAccessor level_accessor, BlockPos pos, String name, String value) {

				BlockEntity block_entity = level_accessor.getBlockEntity(pos);

				if (block_entity != null) {

					block_entity.getPersistentData().putString(name, value);
					BlockState block = level_accessor.getBlockState(pos);

					if (level_accessor instanceof ServerLevel level_server) {

						level_server.sendBlockUpdated(pos, block, block, 2);

					}

				}

			}

			public static void setNumber (LevelAccessor level_accessor, BlockPos pos, String name, double value) {

				BlockEntity block_entity = level_accessor.getBlockEntity(pos);

				if (block_entity != null) {

					block_entity.getPersistentData().putDouble(name, value);
					BlockState block = level_accessor.getBlockState(pos);

					if (level_accessor instanceof ServerLevel level_server) {

						level_server.sendBlockUpdated(pos, block, block, 2);

					}

				}

			}

			public static void setLogic (LevelAccessor level_accessor, BlockPos pos, String name, boolean value) {

				BlockEntity block_entity = level_accessor.getBlockEntity(pos);

				if (block_entity != null) {

					block_entity.getPersistentData().putBoolean(name, value);
					BlockState block = level_accessor.getBlockState(pos);

					if (level_accessor instanceof ServerLevel level_server) {

						level_server.sendBlockUpdated(pos, block, block, 2);

					}

				}

			}

			public static void addNumber (LevelAccessor level_accessor, BlockPos pos, String name, double value) {

				BlockEntity block_entity = level_accessor.getBlockEntity(pos);

				if (block_entity != null) {

					/*
					(Forge)
					block_entity.getPersistentData().putDouble(name, block_entity.getPersistentData().getDouble(name) + value);
					(NeoForge)
					block_entity.getPersistentData().putDouble(name, block_entity.getPersistentData().getDouble(name).get() + value);
					*/
					block_entity.getPersistentData().putDouble(name, block_entity.getPersistentData().getDouble(name) + value);
					BlockState block = level_accessor.getBlockState(pos);

					if (level_accessor instanceof ServerLevel level_server) {

						level_server.sendBlockUpdated(pos, block, block, 2);

					}

				}

			}

		}

		public static class item {

			public static String getText (Entity entity, EquipmentSlot slot, String name) {

				/*
				(Forge)
				return GameUtils.entity.getItemSlot(entity, slot).getOrCreateTag().getString(name);
				(NeoForge)
				return GameUtils.entity.getItemSlot(entity, slot).getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getString("path"));
				*/
				return GameUtils.entity.getItemSlot(entity, slot).getOrCreateTag().getString(name);

			}

		}

		public static CompoundTag textToCompoundTag (String id) {

			CompoundTag return_NBT = new CompoundTag();

			{

				try {

					/*
					(Forge)
					return_NBT = TagParser.parseTag(id.substring(id.indexOf("{")));
					(NeoForge)
					return_NBT = TagParser.parseCompoundFully(id.substring(id.indexOf("{")));
					*/
					return_NBT = TagParser.parseTag(id.substring(id.indexOf("{")));

				} catch (Exception ignored) {



				}

			}

			return return_NBT;

		}

	}

	public static class space {

		public static int[] getWorldSpawnPos (LevelAccessor level_accessor) {

			/*
			(Forge)
			return new int[]{level_accessor.getLevelData().getXSpawn(), level_accessor.getLevelData().getZSpawn()};
			(NeoForge)
			return new int[]{level_accessor.getLevelData().getSpawnPos().getX(), level_accessor.getLevelData().getSpawnPos().getZ()};
			*/
			return new int[]{level_accessor.getLevelData().getXSpawn(), level_accessor.getLevelData().getZSpawn()};

		}

		public static BlockPos getBlockLook (Entity entity, double distance) {

			return entity.level().clip(new ClipContext(entity.getEyePosition(1f), entity.getEyePosition(1f).add(entity.getViewVector(1f).scale(distance)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, entity)).getBlockPos();

		}

		public static int getBuildHeight (LevelAccessor level_accessor, boolean highest) {

			int height = 0;

			if (highest == true) {

				/*
				(Forge)
				height = level_accessor.getMaxBuildHeight();
				(NeoForge)
				height = level_accessor.getMaxY();
				*/
				height = level_accessor.getMaxBuildHeight();

			} else {

				/*
				(Forge)
				height = level_accessor.getMinBuildHeight();
				(NeoForge)
				height = level_accessor.getMinY();
				*/
				height = level_accessor.getMinBuildHeight();

			}

			return height;

		}
		
		public static boolean testChunkStatus (LevelAccessor level_accessor, int chunkX, int chunkZ, String status) {

			if (level_accessor.hasChunk(chunkX, chunkZ) == true) {

				ChunkStatus status_convert = ChunkStatus.FULL;

				if (status.equals("STRUCTURE_REFERENCES") == true) {

					status_convert = ChunkStatus.STRUCTURE_REFERENCES;

				} else if (status.equals("SURFACE") == true) {

					status_convert = ChunkStatus.SURFACE;

				} else if (status.equals("FEATURES") == true) {

					status_convert = ChunkStatus.FEATURES;

				}

                return level_accessor.getChunk(chunkX, chunkZ).getStatus().isOrAfter(status_convert);

			}

			return false;

		}

		public static Holder<Biome> getBiome (LevelAccessor level_accessor, int posX, int posY, int posZ) {

			if (level_accessor.hasChunk(posX >> 4, posZ >> 4) == true) {

				return level_accessor.getBiome(new BlockPos(posX, posY, posZ));

			} else {

				return level_accessor.getUncachedNoiseBiome(posX, posY, posZ);

			}

		}

		public static void placeFeature (WorldGenLevel level_world_gen, int posX, int posY, int posZ, String id) {

			/*
			(Forge)
			level_world_gen.registryAccess().registryOrThrow(Registries.CONFIGURED_FEATURE).getHolderOrThrow(FeatureUtils.createKey(id)).value().place(level_world_gen, level_world_gen.getLevel().getChunkSource().getGenerator(), level_world_gen.getRandom(), new BlockPos(posX, posY, posZ));
			(NeoForge)
			level_world_gen.registryAccess().lookupOrThrow(Registries.CONFIGURED_FEATURE).getValueOrThrow(FeatureUtils.createKey(id)).place(level_world_gen, level_world_gen.getLevel().getChunkSource().getGenerator(), level_world_gen.getRandom(), new BlockPos(posX, posY, posZ));
			*/
			level_world_gen.registryAccess().registryOrThrow(Registries.CONFIGURED_FEATURE).getHolderOrThrow(FeatureUtils.createKey(id)).value().place(level_world_gen, level_world_gen.getLevel().getChunkSource().getGenerator(), level_world_gen.getRandom(), new BlockPos(posX, posY, posZ));

		}

	}

}