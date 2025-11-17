package tannyjung.core.game;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.client.event.ScreenEvent;

public class OverlayMaker {

    public static class Manu {

        public static void text (ScreenEvent.Render event, String text, int posX, int posZ, float scaleX, float scaleZ, int color, boolean shadow) {

            posX = (int) (posX / scaleX);
            posZ = (int) (posZ / scaleZ);

            event.getGuiGraphics().pose().pushPose();
            event.getGuiGraphics().pose().scale(scaleX, scaleZ, 1.0f);
            event.getGuiGraphics().drawString(Minecraft.getInstance().font, text, posX, posZ, color, shadow);
            event.getGuiGraphics().pose().popPose();

        }

        public static void image (ScreenEvent.Render event, String path, int posX, int posZ, int sizeX, int sizeZ, int piece_countX, int piece_countZ, int choose) {

            int piece_sizeX = sizeX / piece_countX;
            int piece_sizeZ = sizeZ / piece_countZ;
            int startX = Mth.clamp(choose * piece_sizeX, 0, sizeX - piece_sizeX);
            int startZ = Mth.clamp(choose * piece_sizeZ, 0, sizeZ - piece_sizeZ);
            event.getGuiGraphics().blit(ResourceLocation.parse(path), posX, posZ, startX, startZ, piece_sizeX, piece_sizeZ, sizeX, sizeZ);

        }

    }

    public static class InGame {

        public static void text (RenderGuiEvent event, String text, int posX, int posZ, float scaleX, float scaleZ, int color, boolean shadow) {

            posX = (int) (posX / scaleX);
            posZ = (int) (posZ / scaleZ);

            event.getGuiGraphics().pose().pushPose();
            event.getGuiGraphics().pose().scale(scaleX, scaleZ, 1.0f);
            event.getGuiGraphics().drawString(Minecraft.getInstance().font, text, posX, posZ, color, shadow);
            event.getGuiGraphics().pose().popPose();

        }

        public static void image (RenderGuiEvent event, String path, int posX, int posZ, int sizeX, int sizeZ, int piece_countX, int piece_countZ, int choose) {

            int piece_sizeX = sizeX / piece_countX;
            int piece_sizeZ = sizeZ / piece_countZ;
            int startX = Mth.clamp(choose * piece_sizeX, 0, sizeX - piece_sizeX);
            int startZ = Mth.clamp(choose * piece_sizeZ, 0, sizeZ - piece_sizeZ);
            event.getGuiGraphics().blit(ResourceLocation.parse(path), posX, posZ, startX, startZ, piece_sizeX, piece_sizeZ, sizeX, sizeZ);

        }

    }

}
