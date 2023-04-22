package acats.fromanotherworld.entity;

public interface DisguisedThing {
    int getTimeUntilFinishedRevealing();
    void setTimeUntilFinishedRevealing(int t);
    int getRevealMaximum();
    float getSupercellConcentration();
    void setSupercellConcentration(float i);
    boolean isAssimilated();
    void setAssimilated();
    default boolean isRevealed(){
        return getTimeUntilFinishedRevealing() > 0;
    }
}
