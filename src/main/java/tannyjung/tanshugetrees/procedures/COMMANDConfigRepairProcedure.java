package tannyjung.tanshugetrees.procedures;

import com.mojang.brigadier.context.CommandContext;
import tannyjung.tanshugetrees_handcode.Handcode;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.commands.CommandSourceStack;

public class COMMANDConfigRepairProcedure {

	public static void start (CommandContext<CommandSourceStack> data) {

        LevelAccessor level_accessor = data.getSource().getLevel();
		Handcode.runRestart(level_accessor, false);

	}

}