package acats.fromanotherworld.entity.interfaces;

public interface MaybeThing {
    boolean faw$isThing();
    default boolean faw$isDistinctThing(){
        return this.faw$isThing();
    }
}
