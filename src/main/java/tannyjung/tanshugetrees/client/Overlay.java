package tannyjung.tanshugetrees.client;

import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.LevelLoadingScreen;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import tannyjung.core.game.OverlayMaker;
import tannyjung.tanshugetrees_mcreator.procedures.OVERLAYWorldGenAnimationProcedure;
import tannyjung.tanshugetrees_mcreator.procedures.OVERLAYWorldGenBarProcedure;
import tannyjung.tanshugetrees.server.world_gen.TreeLocation;

import java.io.File;
import java.nio.file.Files;

@Mod.EventBusSubscriber({Dist.CLIENT})
public class Overlay {

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void eventMenu (ScreenEvent.Render.Post event) {

        OverlayMaker.Manu.image(event, "internet", "aaa" , "https://yt3.googleusercontent.com/lFP2bfvXbKYNoqlLkF6Gz7tN3uvjEEzNNMy8oYkBTlz6HtGDn-xEGoUBGuo_lEZbYVHTAp6a=s900-c-k-c0x00ffffff-no-rj", 10, 50, 100, 100, 1, 1, 1);









        if (event.getScreen() instanceof LevelLoadingScreen) {

            if (TreeLocation.world_gen_overlay_animation != 0) {

                OverlayMaker.Manu.image(event, "inside", "", "tanshugetrees:textures/screens/overlay_region_gen.png", 6, 8, 64, 16, 4, 1, (int) OVERLAYWorldGenAnimationProcedure.execute());
                OverlayMaker.Manu.image(event, "inside", "", "tanshugetrees:textures/screens/overlay_region_gen_bar.png", 22, 8, 17, 16, 17, 1, (int) OVERLAYWorldGenBarProcedure.execute());
                OverlayMaker.Manu.text(event, "Biome : " + TreeLocation.world_gen_overlay_details_biome, 6, 32, 1.0f, 1.0f, -1, false);
                OverlayMaker.Manu.text(event, "Tree : " + TreeLocation.world_gen_overlay_details_tree, 6, 44, 1.0f, 1.0f, -3355444, false);
                OverlayMaker.Manu.text(event, "Generating tree locations. This may take a while.", 6, 64, 1.0f, 1.0f, -3381760, false);

            }

        }

    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void eventInGame (RenderGuiEvent.Post event) {

        if (TreeLocation.world_gen_overlay_animation != 0) {

            OverlayMaker.InGame.image(event, "tanshugetrees:textures/screens/overlay_region_gen.png", 6, 8, 64, 16, 4, 1, (int) OVERLAYWorldGenAnimationProcedure.execute());
            OverlayMaker.InGame.image(event, "tanshugetrees:textures/screens/overlay_region_gen_bar.png", 22, 8, 17, 16, 17, 1, (int) OVERLAYWorldGenBarProcedure.execute());
            OverlayMaker.InGame.text(event, "Biome : " + TreeLocation.world_gen_overlay_details_biome, 6, 32, 1.0f, 0.5f, -10066330, false);
            OverlayMaker.InGame.text(event, "Tree : " + TreeLocation.world_gen_overlay_details_tree, 6, 44, 0.5f, 1.0f, -11908534, false);

        }

    }

}
