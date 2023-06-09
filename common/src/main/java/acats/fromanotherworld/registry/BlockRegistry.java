package acats.fromanotherworld.registry;

import acats.fromanotherworld.block.ThingGore;
import java.util.HashMap;
import java.util.function.Supplier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class BlockRegistry {
    public static final HashMap<String, FAWBlock> BLOCK_REGISTRY = new HashMap<>();
    public static class FAWBlock{
        public FAWBlock(Supplier<Block> blockSupplier){
            this.blockSupplier = blockSupplier;
        }
        private final Supplier<Block> blockSupplier;
        private Block block;
        public Block build(){
            this.block = this.blockSupplier.get();
            return this.block;
        }
        public Block get(){
            return this.block;
        }
    }
    public static final FAWBlock THING_GORE = registerBlock("thing_gore", () -> new ThingGore(BlockBehaviour.Properties.of().noCollission().noOcclusion().randomTicks()));

    private static FAWBlock registerBlock(String id, Supplier<Block> blockSupplier){
        FAWBlock fawBlock = new FAWBlock(blockSupplier);
        BLOCK_REGISTRY.put(id, fawBlock);
        return fawBlock;
    }
}
