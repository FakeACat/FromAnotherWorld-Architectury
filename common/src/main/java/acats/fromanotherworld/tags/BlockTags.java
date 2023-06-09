package acats.fromanotherworld.tags;

import acats.fromanotherworld.FromAnotherWorld;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class BlockTags {
    public static final TagKey<Block> THING_IMMUNE = blockTag("thing_immune");
    public static final TagKey<Block> FREEZES_THINGS = blockTag("freezes_things");

    private static TagKey<Block> blockTag(String id){
        return TagKey.create(Registries.BLOCK, new ResourceLocation(FromAnotherWorld.MOD_ID, id));
    }
}
