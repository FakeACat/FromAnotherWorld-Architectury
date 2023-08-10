package mod.acats.fromanotherworld.fabric.registry;

import mod.acats.fromanotherworld.FromAnotherWorld;
import mod.acats.fromanotherworld.recipe.DamageItemsRecipe;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;

public class RecipeRegistryFabric {
    public static void register(){
        Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, new ResourceLocation(FromAnotherWorld.MOD_ID, DamageItemsRecipe.Serializer.ID), DamageItemsRecipe.Serializer.INSTANCE);
    }
}
