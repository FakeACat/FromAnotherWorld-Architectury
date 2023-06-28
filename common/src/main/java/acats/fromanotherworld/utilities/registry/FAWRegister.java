package acats.fromanotherworld.utilities.registry;

import java.util.HashMap;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class FAWRegister<T> {
    public FAWRegister(){
    }
    private final HashMap<String, FAWRegistryObject<? extends T>> map = new HashMap<>();
    public <E extends T> FAWRegistryObject<E> register(String id, Supplier<E> supplier){
        FAWRegistryObject<E> fawRegistryObject = new FAWRegistryObject<>(supplier);
        map.put(id, fawRegistryObject);
        return fawRegistryObject;
    }

    public final void registerAll(BiConsumer<String, ? super FAWRegistryObject<? extends T>> registerer){
        map.forEach(registerer);
    }
}
