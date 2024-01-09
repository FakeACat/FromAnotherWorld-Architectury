package mod.acats.fromanotherworld.config;

import mod.acats.fromanotherlibrary.config.v2.ModConfig;
import mod.acats.fromanotherlibrary.config.v2.properties.BooleanProperty;

public class WorldConfig extends ModConfig {
    @Override
    public String name() {
        return "world";
    }

    @Override
    protected int version() {
        return 0;
    }

    public final BooleanProperty alienChunkLoading = addProperty(new BooleanProperty(
            "alien_thing_chunk_loading",
            "Should the Alien Thing be able to keep chunks around it loaded?\nMay cause issues with other mods that load chunks.",
            true,
            false
    ));
}
