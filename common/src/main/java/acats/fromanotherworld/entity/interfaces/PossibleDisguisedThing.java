package acats.fromanotherworld.entity.interfaces;

public interface PossibleDisguisedThing {
    int faw$getTimeUntilFinishedRevealing();
    void faw$setTimeUntilFinishedRevealing(int t);
    int faw$getRevealMaximum();
    float faw$getSupercellConcentration();
    void faw$setSupercellConcentration(float i);
    boolean faw$isAssimilated();
    void faw$setAssimilated(boolean assimilated);
    boolean faw$isSleeper();
    void faw$setSleeper(boolean sleeper);
    int faw$getRevealTimer();
    void faw$setRevealTimer(int revealTimer);
    int faw$getRevealed();
    void faw$setRevealed(int t);
    void faw$setRevealedMax(int i);
    default boolean faw$isRevealed(){
        return faw$getTimeUntilFinishedRevealing() > 0;
    }
}
