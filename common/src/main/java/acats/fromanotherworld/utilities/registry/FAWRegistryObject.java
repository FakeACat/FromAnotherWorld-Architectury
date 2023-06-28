package acats.fromanotherworld.utilities.registry;

import java.util.function.Supplier;

public class FAWRegistryObject<T> {
    public FAWRegistryObject(Supplier<T> supplier){
        this.supplier = supplier;
    }
    private final Supplier<T> supplier;
    private T object;

    public T build(){
        this.object = supplier.get();
        return this.object;
    }
    public T get(){
        return this.object;
    }
}
