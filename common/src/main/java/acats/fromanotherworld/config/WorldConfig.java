package acats.fromanotherworld.config;

public class WorldConfig extends FAWConfig {
    @Override
    String name() {
        return "world";
    }

    @Override
    int version() {
        return 0;
    }

    public final FAWConfigBooleanProperty alienChunkLoading = new FAWConfigBooleanProperty(
            "alien_thing_chunk_loading",
            "Should the Alien Thing be able to keep chunks around it loaded?",
            true
    );

    @Override
    FAWConfigProperty<?>[] properties() {
        return new FAWConfigProperty[] {
                this.alienChunkLoading
        };
    }
}
