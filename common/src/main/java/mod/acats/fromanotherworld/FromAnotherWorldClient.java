package mod.acats.fromanotherworld;

import mod.acats.fromanotherlibrary.registry.client.BlockEntityRendererEntry;
import mod.acats.fromanotherlibrary.registry.client.ClientMod;
import mod.acats.fromanotherworld.block.entity.render.CorpseBlockEntityRenderer;
import mod.acats.fromanotherworld.block.entity.render.TunnelBlockEntityRenderer;
import mod.acats.fromanotherworld.registry.BlockEntityRegistry;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;

public class FromAnotherWorldClient implements ClientMod {
    @Override
    public @Nullable Collection<BlockEntityRendererEntry<?>> getBlockRendererEntries() {
        return List.of(
                new BlockEntityRendererEntry<>(BlockEntityRegistry.CORPSE_BLOCK_ENTITY.get(), p -> new CorpseBlockEntityRenderer()),
                new BlockEntityRendererEntry<>(BlockEntityRegistry.TUNNEL_BLOCK_ENTITY.get(), p -> new TunnelBlockEntityRenderer())
        );
    }
}
