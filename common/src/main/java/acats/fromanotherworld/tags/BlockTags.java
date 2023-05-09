package acats.fromanotherworld.tags;

import acats.fromanotherworld.FromAnotherWorld;
import net.minecraft.block.Block;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class BlockTags {
    public static final TagKey<Block> THING_IMMUNE = blockTag("thing_immune");
    public static final TagKey<Block> FREEZES_THINGS = blockTag("freezes_things");

    private static TagKey<Block> blockTag(String id){
        return TagKey.of(RegistryKeys.BLOCK, new Identifier(FromAnotherWorld.MOD_ID, id));
    }
}
