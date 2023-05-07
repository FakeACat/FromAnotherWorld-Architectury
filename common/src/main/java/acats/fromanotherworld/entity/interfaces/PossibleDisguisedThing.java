package acats.fromanotherworld.entity.interfaces;

public interface PossibleDisguisedThing {
    int getTimeUntilFinishedRevealing();
    void setTimeUntilFinishedRevealing(int t);
    int getRevealMaximum();
    float getSupercellConcentration();
    void setSupercellConcentration(float i);
    boolean isAssimilated();
    void setAssimilated(boolean assimilated);
    boolean isSleeper();
    void setSleeper(boolean sleeper);
    int getRevealTimer();
    void setRevealTimer(int revealTimer);
    int getRevealed();
    void setRevealed(int t);
    void setRevealedMax(int i);
    default boolean isRevealed(){
        return getTimeUntilFinishedRevealing() > 0;
    }
}
