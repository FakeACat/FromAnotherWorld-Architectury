package mod.acats.fromanotherworld;

import mod.acats.fromanotherlibrary.config.v2.ModConfig;
import mod.acats.fromanotherlibrary.registry.CommonMod;
import mod.acats.fromanotherlibrary.registry.FALRegister;
import mod.acats.fromanotherlibrary.registry.ResourcePackLoader;
import mod.acats.fromanotherlibrary.registry.TabPopulator;
import mod.acats.fromanotherlibrary.registry.client.ClientMod;
import mod.acats.fromanotherworld.config.Config;
import mod.acats.fromanotherworld.registry.*;
import mod.azure.azurelib.AzureLib;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Optional;
import java.util.function.Supplier;

public class FromAnotherWorld implements CommonMod {

    public static final String MOD_ID = "fromanotherworld";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void preRegisterContent() {
        AzureLib.initialize();
        TransformationRegistry.register();
        ItemRegistry.registerSpawnEggs();
        SpawnEntryRegistry.register();
    }

    @Override
    public String getID() {
        return MOD_ID;
    }

    @Override
    public Optional<FALRegister<Item>> getItemRegister() {
        return Optional.of(ItemRegistry.ITEM_REGISTRY);
    }

    @Override
    public Optional<FALRegister<CreativeModeTab>> getTabRegister() {
        return Optional.of(ItemRegistry.TAB_REGISTRY);
    }

    @Override
    public Optional<TabPopulator> getTabPopulator() {
        return Optional.of(ItemRegistry.TAB_POPULATOR);
    }

    @Override
    public Optional<FALRegister<Block>> getBlockRegister() {
        return Optional.of(BlockRegistry.BLOCK_REGISTRY);
    }

    @Override
    public Optional<FALRegister<BlockEntityType<?>>> getBlockEntityRegister() {
        return Optional.of(BlockEntityRegistry.BLOCK_ENTITY_REGISTRY);
    }

    @Override
    public Optional<FALRegister<EntityType<?>>> getEntityRegister() {
        return Optional.of(EntityRegistry.ENTITY_REGISTRY);
    }

    @Override
    public Optional<HashMap<EntityType<? extends LivingEntity>, Supplier<AttributeSupplier.Builder>>> getEntityAttributeRegister() {
        return Optional.of(EntityRegistry.getAttributes());
    }

    @Override
    public Optional<ClientMod> getClientMod() {
        return Optional.of(new FromAnotherWorldClient());
    }

    @Override
    public Optional<FALRegister<ParticleType<?>>> getParticleRegister() {
        return Optional.of(ParticleRegistry.PARTICLE_REGISTRY);
    }

    @Override
    public Optional<FALRegister<SoundEvent>> getSoundEventRegister() {
        return Optional.of(SoundRegistry.SOUND_REGISTRY);
    }

    @Override
    public Optional<FALRegister<MobEffect>> getMobEffectRegister() {
        return Optional.of(StatusEffectRegistry.STATUS_EFFECT_REGISTRY);
    }

    @Override
    public Optional<ResourcePackLoader> getResourcePacks() {
        ResourcePackLoader loader = new ResourcePackLoader();
        DatapackRegistry.register(loader);
        return Optional.of(loader);
    }

    @Override
    public Optional<ModConfig[]> getConfigs() {
        return Optional.of(new ModConfig[] {
                Config.ASSIMILATED_SCULK_CONFIG,
                Config.COMPATIBILITY_CONFIG,
                Config.DIFFICULTY_CONFIG,
                Config.EFFECT_CONFIG,
                Config.EVENT_CONFIG,
                Config.BLOCK_CONFIG,
                Config.SPAWNING_CONFIG,
                Config.WORLD_CONFIG,
                Config.MISC_CONFIG
        });
    }
}
