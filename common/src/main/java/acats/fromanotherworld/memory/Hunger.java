package acats.fromanotherworld.memory;

public enum Hunger {
    VERY_HUNGRY(0.8F, 0.6F, true, 2.0F, 2.0F),
    HUNGRY(0.9F, 0.8F, false, 1.5F, 1.5F),
    NORMAL(1.0F, 1.0F, false, 1.0F, 1.0F),
    SATISFIED(1.1F, 1.2F, false, 0.5F, 1.0F);

    public final float revealCooldownMultiplier;
    public final float transformChanceMultiplier;
    public final boolean activateHibernating;
    public final float wanderIntervalMultiplier;
    public final float wanderDistMultiplier;

    Hunger(float revealCooldownMultiplier, float transformChanceMultiplier, boolean activateHibernating, float wanderIntervalMultiplier, float wanderDistMultiplier) {
        this.revealCooldownMultiplier = revealCooldownMultiplier;
        this.transformChanceMultiplier = transformChanceMultiplier;
        this.activateHibernating = activateHibernating;
        this.wanderIntervalMultiplier = wanderIntervalMultiplier;
        this.wanderDistMultiplier = wanderDistMultiplier;
    }
}
