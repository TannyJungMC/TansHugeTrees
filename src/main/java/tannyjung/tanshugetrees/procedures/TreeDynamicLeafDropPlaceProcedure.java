package tannyjung.tanshugetrees.procedures;

import tannyjung.tanshugetrees_handcode.config.ConfigMain;

import net.minecraftforge.registries.ForgeRegistries;

import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.tags.BlockTags;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;

public class TreeDynamicLeafDropPlaceProcedure {
	public static void execute(LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		String variable_text = "";
		BlockState block = Blocks.AIR.defaultBlockState();
		BlockState variable_block = Blocks.AIR.defaultBlockState();
		if (ConfigMain.leaves_litter == true) {
			variable_text = (entity.getPersistentData().getString("block")).replace("keep", "");
			if (variable_text.endsWith("]")) {
				variable_text = variable_text.substring(0, variable_text.indexOf("[", 0));
			}
			block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation((variable_text).toLowerCase(java.util.Locale.ENGLISH))).defaultBlockState();
			block = (block.getBlock().getStateDefinition().getProperty("persistent") instanceof BooleanProperty _withbp2 ? block.setValue(_withbp2, true) : block);
			if ((world.getBlockState(BlockPos.containing(entity.getX(), entity.getY() + 2, entity.getZ()))).getBlock() == Blocks.WATER) {
				block = (block.getBlock().getStateDefinition().getProperty("waterlogged") instanceof BooleanProperty _withbp8 ? block.setValue(_withbp8, true) : block);
				world.setBlock(BlockPos.containing(entity.getX(), entity.getY() + 2, entity.getZ()), block, 3);
			} else {
				if ((world.getBlockState(BlockPos.containing(entity.getX(), entity.getY() + 1.5, entity.getZ()))).is(BlockTags.create(new ResourceLocation("tanshugetrees:air_blocks"))) == true || variable_block.getBlock() == Blocks.SNOW) {
					variable_block = (world.getBlockState(BlockPos.containing(entity.getX(), entity.getY() + 1.3, entity.getZ())));
					if (!(variable_block.getBlock() == Blocks.DIRT_PATH)
							&& (!(variable_block.getBlock() instanceof SimpleWaterloggedBlock)
									|| !((variable_block.getBlock().getStateDefinition().getProperty("type") instanceof EnumProperty _getep25 ? variable_block.getValue(_getep25).toString() : "").equals("bottom")
											|| (variable_block.getBlock().getStateDefinition().getProperty("half") instanceof EnumProperty _getep26 ? variable_block.getValue(_getep26).toString() : "").equals("bottom")))
							|| variable_block.is(BlockTags.create(new ResourceLocation("minecraft:stairs"))) || variable_block.is(BlockTags.create(new ResourceLocation("tanshugetrees:passable_blocks")))) {
						world.setBlock(BlockPos.containing(entity.getX(), entity.getY() + 1.5, entity.getZ()), block, 3);
					}
				}
			}
		}
		{
			Entity _ent = entity;
			if (!_ent.level().isClientSide() && _ent.getServer() != null) {
				_ent.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null, 4,
						_ent.getName().getString(), _ent.getDisplayName(), _ent.level().getServer(), _ent), "kill @s");
			}
		}
	}
}
