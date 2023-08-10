package mod.acats.fromanotherworld.entity.texture;

import mod.acats.fromanotherworld.FromAnotherWorld;
import com.mojang.blaze3d.platform.NativeImage;
import java.awt.*;
import java.util.HashMap;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.Mth;

public class ThingOverlayTexture implements AutoCloseable{
    //Original overlay logic credit to Gigeresque: https://github.com/cybercat-mods/gigeresque
    private DynamicTexture backedTexture;
    public RenderType renderLayer;
    public ThingOverlayTexture(TextureManager textureManager, ResourceManager resourceManager, ResourceLocation entityTexture, ResourceLocation overlayTexture, String prefix){
        backedTexture = new DynamicTexture(64, 64, true);
        Minecraft.getInstance().execute(() -> {
            try {
                NativeImage entityImage = NativeImage.read(resourceManager.getResourceOrThrow(entityTexture).open());
                NativeImage overlayImage = NativeImage.read(resourceManager.getResourceOrThrow(overlayTexture).open());
                int height1 = entityImage.getHeight();
                int width1 = entityImage.getWidth();
                int height2 = overlayImage.getHeight();
                int width2 = overlayImage.getWidth();
                backedTexture = new DynamicTexture(width1, height1, true);
                for (int x = 0; x < width1; x++) {
                    for (int y = 0; y < height1; y++) {
                        int colour = entityImage.getPixelRGBA(x, y);
                        Color colour2 = Color.decode(String.valueOf(colour));
                        if ((colour >> 24 & 255) > 10) {
                            int brightness = (colour2.getRed() + colour2.getGreen() + colour2.getBlue()) / 3;
                            NativeImage image = backedTexture.getPixels();
                            if (image != null) {
                                Color colour3 = Color.decode(String.valueOf(overlayImage.getPixelRGBA(x % width2, y % height2)));
                                float multiplier = Mth.clamp((float)brightness / 255.0F + 0.25F, 0.0F, 1.0F) / 255.0F;
                                colour3 = new Color(colour3.getRed() * multiplier, colour3.getGreen() * multiplier, colour3.getBlue() * multiplier);
                                int colour4 = colour3.hashCode();
                                image.setPixelRGBA(x, y, colour4);
                            }
                        }
                    }
                }

                backedTexture.upload();
                entityImage.close();
            }
            catch (Exception e){
                e.printStackTrace();
            }
        });
        renderLayer = RenderType.entityTranslucent(textureManager.register(prefix + "/" + entityTexture.getPath(), backedTexture));
    }
    @Override
    public void close() {
        backedTexture.close();
    }

    public static final ResourceLocation FLESH_OVERLAY_TEXTURE = new ResourceLocation(FromAnotherWorld.MOD_ID, "textures/entity/thing/flesh_overlay.png");
    public static final ResourceLocation SNOW_OVERLAY_TEXTURE = new ResourceLocation("textures/block/powder_snow.png");
    public static final ResourceLocation INJURED_OVERLAY_TEXTURE = new ResourceLocation(FromAnotherWorld.MOD_ID, "textures/entity/thing/injured_overlay.png");

    private static final HashMap<ResourceLocation, ThingOverlayTexture> FLESH = new HashMap<>();
    private static final HashMap<ResourceLocation, ThingOverlayTexture> SNOW = new HashMap<>();
    private static final HashMap<ResourceLocation, ThingOverlayTexture> INJURED = new HashMap<>();

    public static RenderType getFleshOverlayRenderLayer(ResourceLocation entityTexture){
        return layer(entityTexture, FLESH_OVERLAY_TEXTURE, FLESH, "flesh_overlay");
    }
    public static RenderType getSnowOverlayRenderLayer(ResourceLocation entityTexture){
        return layer(entityTexture, SNOW_OVERLAY_TEXTURE, SNOW, "snow_overlay");
    }
    public static RenderType getInjuredOverlayRenderLayer(ResourceLocation entityTexture){
        return layer(entityTexture, INJURED_OVERLAY_TEXTURE, INJURED, "injured_overlay");
    }

    private static RenderType layer(ResourceLocation entityTexture, ResourceLocation overlayTexture, HashMap<ResourceLocation, ThingOverlayTexture> hashMap, String prefix){
        return hashMap.computeIfAbsent(entityTexture, identifier -> new ThingOverlayTexture(Minecraft.getInstance().getTextureManager(), Minecraft.getInstance().getResourceManager(), entityTexture, overlayTexture, prefix)).renderLayer;
    }
}
