package mod.acats.fromanotherworld.memory;

public enum Aggression {
    HIDING(10.0F),
    NORMAL(1.0F),
    AGGRESSIVE(0.5F);

    public final float transformChanceMultiplier;

    Aggression(float transformChanceMultiplier) {
        this.transformChanceMultiplier = transformChanceMultiplier;
    }
}
