package acats.fromanotherworld.entity.texture;

import acats.fromanotherworld.FromAnotherWorld;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

import java.awt.*;
import java.util.HashMap;

public class ThingOverlayTexture implements AutoCloseable{
    //Original overlay logic credit to Gigeresque: https://github.com/cybercat-mods/gigeresque
    private NativeImageBackedTexture backedTexture;
    public RenderLayer renderLayer;
    public ThingOverlayTexture(TextureManager textureManager, ResourceManager resourceManager, Identifier entityTexture, Identifier overlayTexture, String prefix){
        backedTexture = new NativeImageBackedTexture(64, 64, true);
        MinecraftClient.getInstance().execute(() -> {
            try {
                NativeImage entityImage = NativeImage.read(resourceManager.getResourceOrThrow(entityTexture).getInputStream());
                NativeImage overlayImage = NativeImage.read(resourceManager.getResourceOrThrow(overlayTexture).getInputStream());
                int height1 = entityImage.getHeight();
                int width1 = entityImage.getWidth();
                int height2 = overlayImage.getHeight();
                int width2 = overlayImage.getWidth();
                backedTexture = new NativeImageBackedTexture(width1, height1, true);
                for (int x = 0; x < width1; x++) {
                    for (int y = 0; y < height1; y++) {
                        int colour = entityImage.getColor(x, y);
                        Color colour2 = Color.decode(String.valueOf(colour));
                        if ((colour >> 24 & 255) > 10) {
                            int brightness = (colour2.getRed() + colour2.getGreen() + colour2.getBlue()) / 3;
                            NativeImage image = backedTexture.getImage();
                            if (image != null) {
                                Color colour3 = Color.decode(String.valueOf(overlayImage.getColor(x % width2, y % height2)));
                                float multiplier = MathHelper.clamp((float)brightness / 255.0F + 0.25F, 0.0F, 1.0F) / 255.0F;
                                colour3 = new Color(colour3.getRed() * multiplier, colour3.getGreen() * multiplier, colour3.getBlue() * multiplier);
                                int colour4 = colour3.hashCode();
                                image.setColor(x, y, colour4);
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
        renderLayer = RenderLayer.getEntityTranslucent(textureManager.registerDynamicTexture(prefix + "/" + entityTexture.getPath(), backedTexture));
    }
    @Override
    public void close() {
        backedTexture.close();
    }

    public static final Identifier FLESH_OVERLAY_TEXTURE = new Identifier(FromAnotherWorld.MOD_ID, "textures/entity/flesh_overlay.png");
    public static final Identifier SNOW_OVERLAY_TEXTURE = new Identifier("textures/block/powder_snow.png");
    public static final Identifier INJURED_OVERLAY_TEXTURE = new Identifier(FromAnotherWorld.MOD_ID, "textures/entity/injured_overlay.png");

    private static final HashMap<Identifier, ThingOverlayTexture> FLESH = new HashMap<>();
    private static final HashMap<Identifier, ThingOverlayTexture> SNOW = new HashMap<>();
    private static final HashMap<Identifier, ThingOverlayTexture> INJURED = new HashMap<>();

    public static RenderLayer getFleshOverlayRenderLayer(Identifier entityTexture){
        return layer(entityTexture, FLESH_OVERLAY_TEXTURE, FLESH, "flesh_overlay");
    }
    public static RenderLayer getSnowOverlayRenderLayer(Identifier entityTexture){
        return layer(entityTexture, SNOW_OVERLAY_TEXTURE, SNOW, "snow_overlay");
    }
    public static RenderLayer getInjuredOverlayRenderLayer(Identifier entityTexture){
        return layer(entityTexture, INJURED_OVERLAY_TEXTURE, INJURED, "injured_overlay");
    }

    private static RenderLayer layer(Identifier entityTexture, Identifier overlayTexture, HashMap<Identifier, ThingOverlayTexture> hashMap, String prefix){
        return hashMap.computeIfAbsent(entityTexture, identifier -> new ThingOverlayTexture(MinecraftClient.getInstance().getTextureManager(), MinecraftClient.getInstance().getResourceManager(), entityTexture, overlayTexture, prefix)).renderLayer;
    }
}
