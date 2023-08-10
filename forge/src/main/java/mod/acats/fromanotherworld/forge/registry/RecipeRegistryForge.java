package mod.acats.fromanotherworld.forge.registry;

import mod.acats.fromanotherworld.FromAnotherWorld;
import mod.acats.fromanotherworld.recipe.DamageItemsRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class RecipeRegistryForge {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, FromAnotherWorld.MOD_ID);

    public static void register(IEventBus eventBus){
        SERIALIZERS.register(DamageItemsRecipe.Serializer.ID, () -> DamageItemsRecipe.Serializer.INSTANCE);

        SERIALIZERS.register(eventBus);
    }
}
