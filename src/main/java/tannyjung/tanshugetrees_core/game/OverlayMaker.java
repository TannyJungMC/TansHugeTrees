package tannyjung.tanshugetrees_core.game;

import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import tannyjung.tanshugetrees_core.outside.OutsideUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class OverlayMaker {

    private static final Map<String, Integer> online_image_id = new HashMap<>();
    private static final Map<String, Boolean> image_available = new HashMap<>();
    private static int online_image_count = 0;

    public static void createText (GuiGraphics graphic, int screen_width, int screen_height, String pos_style, int posX, int posZ, double scale, int color, boolean shadow, String text) {

        int[] pos = convertPos(screen_width, screen_height, posX, posZ, pos_style, scale);
        posX = pos[0];
        posZ = pos[1];

        /*
        (1.20.1) (1.21.1)
        graphic.pose().pushPose();
        graphic.pose().scale((float) scale, (float) scale, 1.0f);
        graphic.drawString(Minecraft.getInstance().font, text, posX, posZ, color, shadow);
        graphic.pose().popPose();
        (1.21.8)
        graphic.pose().pushMatrix();
        graphic.pose().scale((float) scale, (float) scale);
        graphic.drawString(Minecraft.getInstance().font, text, posX, posZ, color, shadow);
        graphic.pose().popMatrix();
        */
        graphic.pose().pushPose();
        graphic.pose().scale((float) scale, (float) scale, 1.0f);
        graphic.drawString(Minecraft.getInstance().font, text, posX, posZ, color, shadow);
        graphic.pose().popPose();

    }

    public static void createImage (GuiGraphics graphic, boolean internet, String path, String path_fail, int posX, int posZ, int sizeX, int sizeZ, int piece_countX, int piece_countZ, int choose) {

        String name = "";

        if (internet == true) {

            if (online_image_id.containsKey(path) == false) {

                online_image_count = online_image_count + 1;
                online_image_id.put(path, online_image_count);

            }

            name = "tannyjung:online_image_" + online_image_id.get(path) + ".png";

        } else {

            name = path;

        }

        ResourceLocation location = null;

        if (image_available.containsKey(name) == false) {

            boolean pass = false;

            if (internet == true) {

                {

                    if (OutsideUtils.isURLAvailable(path) == true) {

                        try {

                            BufferedImage buffer = ImageIO.read(URI.create(path).toURL());
                            NativeImage native_image = new NativeImage(buffer.getWidth(), buffer.getHeight(), false);

                            // Color Convert
                            {

                                int argb = 0;
                                int a = 0;
                                int r = 0;
                                int g = 0;
                                int b = 0;
                                int abgr = 0;

                                for (int scanY = 0; scanY < buffer.getHeight(); scanY++) {

                                    for (int scanX = 0; scanX < buffer.getWidth(); scanX++) {

                                        argb = buffer.getRGB(scanX, scanY);
                                        a = (argb >>> 24) & 0xFF;
                                        r = (argb >>> 16) & 0xFF;
                                        g = (argb >>> 8) & 0xFF;
                                        b = (argb) & 0xFF;
                                        abgr = (a << 24) | (b << 16) | (g << 8) | r;

                                        /*
                                        (1.20.1) (1.21.1)
                                        native_image.setPixelRGBA(scanX, scanY, abgr);
                                        (1.21.8)
                                        native_image.setPixelABGR(scanX, scanY, abgr);
                                        */
                                        native_image.setPixelRGBA(scanX, scanY, abgr);

                                    }

                                }

                            }

                            /*
                            (1.20.1) (1.21.1)
                            Minecraft.getInstance().getTextureManager().register(location, new DynamicTexture(native_image));
                            (1.21.8)
                            Minecraft.getInstance().getTextureManager().register(location, new DynamicTexture(() -> "test", native_image));
                            */
                            Minecraft.getInstance().getTextureManager().register(ResourceLocation.parse(name), new DynamicTexture(native_image));
                            pass = true;

                        } catch (Exception exception) {

                            OutsideUtils.exception(new Exception(), exception, "");

                        }

                    }

                }

            } else {

                {

                    AbstractTexture texture = Minecraft.getInstance().getTextureManager().getTexture(ResourceLocation.parse(name));

                    if (texture.getId() != -1) {

                        pass = true;

                    }

                }

            }

            image_available.put(name, pass);

        }

        if (image_available.get(name) == true) {

            location = ResourceLocation.parse(name);

        } else {

            if (path_fail.isEmpty() == true) {

                return;

            }

            location = ResourceLocation.parse(path_fail);

        }

        int piece_sizeX = sizeX / piece_countX;
        int piece_sizeZ = sizeZ / piece_countZ;
        int startX = Mth.clamp(choose * piece_sizeX, 0, sizeX - piece_sizeX);
        int startZ = Mth.clamp(choose * piece_sizeZ, 0, sizeZ - piece_sizeZ);

        graphic.blit(location, posX, posZ, startX, startZ, piece_sizeX, piece_sizeZ, sizeX, sizeZ);

    }

    private static int[] convertPos (int screen_width, int screen_height, int posX, int posZ, String pos_style, double scale) {

        int[] pos = new int[2];

        {

            if (pos_style.startsWith("top-") == true) {

                pos[1] = posZ;

            } else if (pos_style.startsWith("bottom-") == true) {

                pos[1] = screen_height - posZ;

            }

            if (pos_style.endsWith("-left") == true) {

                pos[0] = posX;

            } else if (pos_style.endsWith("-right") == true) {

                pos[0] = screen_width - posX;

            }

        }

        pos[0] = (int) (pos[0] / scale);
        pos[1] = (int) (pos[1] / scale);
        return pos;

    }

}
