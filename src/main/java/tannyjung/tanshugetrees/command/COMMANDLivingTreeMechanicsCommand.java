package tannyjung.tanshugetrees.command;

import tannyjung.tanshugetrees.procedures.COMMANDLivingTreeMechanicsMainProcedure;
import tannyjung.tanshugetrees.procedures.COMMANDLivingTreeMechanicsLeafLitterRemoverProcedure;
import tannyjung.tanshugetrees.procedures.COMMANDLivingTreeMechanicsLeafDropProcedure;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.common.util.FakePlayerFactory;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.Direction;
import net.minecraft.commands.Commands;

@Mod.EventBusSubscriber
public class COMMANDLivingTreeMechanicsCommand {
	@SubscribeEvent
	public static void registerCommand(RegisterCommandsEvent event) {
		event.getDispatcher().register(Commands.literal("TANSHUGETREES").requires(s -> s.hasPermission(2)).then(Commands.literal("dev").then(Commands.literal("living_tree_mechanics").then(Commands.literal("main").executes(arguments -> {
			Level world = arguments.getSource().getUnsidedLevel();
			double x = arguments.getSource().getPosition().x();
			double y = arguments.getSource().getPosition().y();
			double z = arguments.getSource().getPosition().z();
			Entity entity = arguments.getSource().getEntity();
			if (entity == null && world instanceof ServerLevel _servLevel)
				entity = FakePlayerFactory.getMinecraft(_servLevel);
			Direction direction = Direction.DOWN;
			if (entity != null)
				direction = entity.getDirection();

			COMMANDLivingTreeMechanicsMainProcedure.execute(entity);
			return 0;
		})).then(Commands.literal("leaf_drop").executes(arguments -> {
			Level world = arguments.getSource().getUnsidedLevel();
			double x = arguments.getSource().getPosition().x();
			double y = arguments.getSource().getPosition().y();
			double z = arguments.getSource().getPosition().z();
			Entity entity = arguments.getSource().getEntity();
			if (entity == null && world instanceof ServerLevel _servLevel)
				entity = FakePlayerFactory.getMinecraft(_servLevel);
			Direction direction = Direction.DOWN;
			if (entity != null)
				direction = entity.getDirection();

			COMMANDLivingTreeMechanicsLeafDropProcedure.execute(entity);
			return 0;
		})).then(Commands.literal("leaf_litter_remover").executes(arguments -> {
			Level world = arguments.getSource().getUnsidedLevel();
			double x = arguments.getSource().getPosition().x();
			double y = arguments.getSource().getPosition().y();
			double z = arguments.getSource().getPosition().z();
			Entity entity = arguments.getSource().getEntity();
			if (entity == null && world instanceof ServerLevel _servLevel)
				entity = FakePlayerFactory.getMinecraft(_servLevel);
			Direction direction = Direction.DOWN;
			if (entity != null)
				direction = entity.getDirection();

			COMMANDLivingTreeMechanicsLeafLitterRemoverProcedure.execute(entity);
			return 0;
		})))));
	}

}