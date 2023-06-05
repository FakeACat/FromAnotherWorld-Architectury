package acats.fromanotherworld.entity.interfaces;

public interface MaybeThing {
    boolean isThing();
    default boolean isDistinctThing(){
        return this.isThing();
    }
}
