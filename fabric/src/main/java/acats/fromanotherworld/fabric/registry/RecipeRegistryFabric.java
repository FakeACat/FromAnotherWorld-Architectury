package acats.fromanotherworld.fabric.registry;

import acats.fromanotherworld.FromAnotherWorld;
import acats.fromanotherworld.recipe.DamageItemsRecipe;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;

public class RecipeRegistryFabric {
    public static void register(){
        Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, new ResourceLocation(FromAnotherWorld.MOD_ID, DamageItemsRecipe.Serializer.ID), DamageItemsRecipe.Serializer.INSTANCE);
    }
}
