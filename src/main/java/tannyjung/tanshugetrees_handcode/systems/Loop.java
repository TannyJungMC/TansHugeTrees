package tannyjung.tanshugetrees_handcode.systems;

import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.server.ServerLifecycleHooks;
import tannyjung.tanshugetrees.TanshugetreesMod;
import tannyjung.tanshugetrees_handcode.misc.GameUtils;

import java.util.concurrent.CompletableFuture;

public class Loop {

    public static void start (LevelAccessor level) {

        tick(level);
        second(level);

    }

    private static void tick (LevelAccessor level) {

        // Looping
        {

            TanshugetreesMod.queueServerWork(1, () -> {

                if (GameUtils.playerCount(level) > 0) {

                    tick(level);

                }

            });

        }



    }

    private static void second (LevelAccessor level) {

        // Looping
        {

            TanshugetreesMod.queueServerWork(20, () -> {

                if (GameUtils.playerCount(level) > 0) {

                    second(level);

                }

            });

        }

        // Living Tree Mechanics
        {

            if (GameUtils.commandResult(level, 0, 0, 0, "execute if entity @e[name=TANSHUGETREES-leaf_drop]") == true) {

                GameUtils.runCommand(level, 0, 0, 0, "scoreboard players set leaf_drop_count TANSHUGETREES 0");
                GameUtils.runCommand(level, 0, 0, 0, "execute at @e[name=TANSHUGETREES-leaf_drop] run scoreboard players add leaf_drop_count TANSHUGETREES 1");

            }

        }

    }

}
