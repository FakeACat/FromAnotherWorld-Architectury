package mod.acats.fromanotherworld.config;

import mod.acats.fromanotherlibrary.config.FALConfig;

public class WorldConfig extends FALConfig {
    @Override
    protected String name() {
        return "world";
    }

    @Override
    protected int version() {
        return 0;
    }

    public final FALConfigBooleanProperty alienChunkLoading = new FALConfigBooleanProperty(
            "alien_thing_chunk_loading",
            "Should the Alien Thing be able to keep chunks around it loaded?\nMay cause issues with other mods that load chunks.",
            true
    );

    @Override
    protected FALConfigProperty<?>[] properties() {
        return new FALConfigProperty[] {
                this.alienChunkLoading
        };
    }
}
