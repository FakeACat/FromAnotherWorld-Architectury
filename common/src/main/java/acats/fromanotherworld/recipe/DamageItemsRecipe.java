package acats.fromanotherworld.recipe;

import acats.fromanotherworld.utilities.ItemUtilities;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class DamageItemsRecipe implements CraftingRecipe {
    private final ResourceLocation id;
    private final ItemStack output;
    private final NonNullList<Ingredient> ingredients;
    private final CraftingBookCategory category;
    private final String group;

    public DamageItemsRecipe(ResourceLocation id,
                             String group,
                             CraftingBookCategory category,
                             ItemStack output,
                             NonNullList<Ingredient> ingredients){
        this.id = id;
        this.output = output;
        this.ingredients = ingredients;
        this.category = category;
        this.group = group;
    }

    @Override
    public @NotNull CraftingBookCategory category() {
        return this.category;
    }

    @Override
    public boolean matches(CraftingContainer container, Level level) {
        StackedContents stackedContents = new StackedContents();
        int i = 0;

        for(int j = 0; j < container.getContainerSize(); ++j) {
            ItemStack itemStack = container.getItem(j);
            if (!itemStack.isEmpty()) {
                ++i;
                stackedContents.accountStack(itemStack, 1);
            }
        }

        return i == this.ingredients.size() && stackedContents.canCraft(this, null);
    }

    @Override
    public @NotNull ItemStack assemble(CraftingContainer container, RegistryAccess registryAccess) {
        return this.output.copy();
    }

    @Override
    public boolean canCraftInDimensions(int i, int j) {
        return i * j >= this.ingredients.size();
    }

    @Override
    public @NotNull ItemStack getResultItem(RegistryAccess registryAccess) {
        return this.output;
    }

    @Override
    public @NotNull ResourceLocation getId() {
        return this.id;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public @NotNull String getGroup() {
        return this.group;
    }

    @Override
    public @NotNull NonNullList<Ingredient> getIngredients() {
        return this.ingredients;
    }

    @Override
    public @NotNull NonNullList<ItemStack> getRemainingItems(CraftingContainer container) {
        final NonNullList<ItemStack> nonNullList = CraftingRecipe.super.getRemainingItems(container);

        for(int i = 0; i < nonNullList.size(); ++i) {
            ItemStack stack = container.getItem(i);
            if (stack.isDamageableItem()){
                nonNullList.set(i, ItemUtilities.damage(stack, 1).copy());
            }
        }
        return nonNullList;
    }

    public static class Serializer implements RecipeSerializer<DamageItemsRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final String ID = "damage_items";
        public Serializer() {
        }

        public @NotNull DamageItemsRecipe fromJson(ResourceLocation resourceLocation, JsonObject jsonObject) {
            String string = GsonHelper.getAsString(jsonObject, "group", "");
            CraftingBookCategory craftingBookCategory = CraftingBookCategory.CODEC.byName(GsonHelper.getAsString(jsonObject, "category", null), CraftingBookCategory.MISC);
            NonNullList<Ingredient> nonNullList = itemsFromJson(GsonHelper.getAsJsonArray(jsonObject, "ingredients"));
            if (nonNullList.isEmpty()) {
                throw new JsonParseException("No ingredients for damage item recipe");
            } else if (nonNullList.size() > 9) {
                throw new JsonParseException("Too many ingredients for damage item recipe");
            } else {
                ItemStack itemStack = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(jsonObject, "result"));
                return new DamageItemsRecipe(resourceLocation, string, craftingBookCategory, itemStack, nonNullList);
            }
        }

        private static NonNullList<Ingredient> itemsFromJson(JsonArray jsonArray) {
            NonNullList<Ingredient> nonNullList = NonNullList.create();

            for(int i = 0; i < jsonArray.size(); ++i) {
                Ingredient ingredient = Ingredient.fromJson(jsonArray.get(i), false);
                if (!ingredient.isEmpty()) {
                    nonNullList.add(ingredient);
                }
            }

            return nonNullList;
        }

        public @NotNull DamageItemsRecipe fromNetwork(ResourceLocation resourceLocation, FriendlyByteBuf friendlyByteBuf) {
            String string = friendlyByteBuf.readUtf();
            CraftingBookCategory craftingBookCategory = friendlyByteBuf.readEnum(CraftingBookCategory.class);
            int i = friendlyByteBuf.readVarInt();
            NonNullList<Ingredient> nonNullList = NonNullList.withSize(i, Ingredient.EMPTY);

            nonNullList.replaceAll(ignored -> Ingredient.fromNetwork(friendlyByteBuf));

            ItemStack itemStack = friendlyByteBuf.readItem();
            return new DamageItemsRecipe(resourceLocation, string, craftingBookCategory, itemStack, nonNullList);
        }

        public void toNetwork(FriendlyByteBuf friendlyByteBuf, DamageItemsRecipe damageItemsRecipe) {
            friendlyByteBuf.writeUtf(damageItemsRecipe.group);
            friendlyByteBuf.writeEnum(damageItemsRecipe.category);
            friendlyByteBuf.writeVarInt(damageItemsRecipe.ingredients.size());

            for (Ingredient ingredient : damageItemsRecipe.ingredients) {
                ingredient.toNetwork(friendlyByteBuf);
            }

            friendlyByteBuf.writeItem(damageItemsRecipe.output);
        }
    }
}
