package tannyjung.core.game;

import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import tannyjung.core.OutsideUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.nio.file.Files;

public class OverlayMaker {

    public static void text (GuiGraphics graphic, String text, int posX, int posZ, float scaleX, float scaleZ, int color, boolean shadow) {

        posX = (int) (posX / scaleX);
        posZ = (int) (posZ / scaleZ);

        graphic.pose().pushPose();
        graphic.pose().scale(scaleX, scaleZ, 1.0f);
        graphic.drawString(Minecraft.getInstance().font, text, posX, posZ, color, shadow);
        graphic.pose().popPose();

    }

    public static void image (GuiGraphics graphic, String type, String name, String path, int posX, int posZ, int sizeX, int sizeZ, int piece_countX, int piece_countZ, int choose) {

        if (name.equals("") == true) {

            name = path;

        } else {

            name = "tannyjung:" + name + ".png";

        }

        ResourceLocation location = ResourceLocation.parse(name);
        AbstractTexture texture = null;

        // Test Get Texture
        {

            try {

                texture = Minecraft.getInstance().getTextureManager().getTexture(location);

            } catch (Exception ignored) {


            }

        }

        if (texture instanceof SimpleTexture) {

            if (type.equals("internet") == true) {

                {

                    if (OutsideUtils.isURLAvailable(path) == true) {

                        try {

                            BufferedImage buffer = ImageIO.read(new URL(path));
                            NativeImage native_image = new NativeImage(buffer.getWidth(), buffer.getHeight(), false);

                            // Color Convert
                            {

                                for (int scanY = 0; scanY < buffer.getHeight(); scanY++) {

                                    for (int scanX = 0; scanX < buffer.getWidth(); scanX++) {

                                        int argb = buffer.getRGB(scanX, scanY);
                                        int a = (argb >>> 24) & 0xFF;
                                        int r = (argb >>> 16) & 0xFF;
                                        int g = (argb >>> 8) & 0xFF;
                                        int b = (argb) & 0xFF;
                                        int abgr = (a << 24) | (b << 16) | (g << 8) | r;
                                        native_image.setPixelRGBA(scanX, scanY, abgr);

                                    }

                                }

                            }

                            Minecraft.getInstance().getTextureManager().register(location, new DynamicTexture(native_image));

                        } catch (Exception exception) {

                            OutsideUtils.exception(new Exception(), exception);

                        }

                    }

                }

            } else if (type.equals("outside") == true) {

                {

                    File file = new File(path);

                    if (file.exists() == true && file.isDirectory() == false) {

                        try {

                            NativeImage image = NativeImage.read(Files.newInputStream(file.toPath()));
                            Minecraft.getInstance().getTextureManager().register(name, new DynamicTexture(image));

                        } catch (Exception exception) {

                            OutsideUtils.exception(new Exception(), exception);

                        }

                    }

                }

            }

        }

        int piece_sizeX = sizeX / piece_countX;
        int piece_sizeZ = sizeZ / piece_countZ;
        int startX = Mth.clamp(choose * piece_sizeX, 0, sizeX - piece_sizeX);
        int startZ = Mth.clamp(choose * piece_sizeZ, 0, sizeZ - piece_sizeZ);

        graphic.blit(location, posX, posZ, startX, startZ, piece_sizeX, piece_sizeZ, sizeX, sizeZ);

    }

}
