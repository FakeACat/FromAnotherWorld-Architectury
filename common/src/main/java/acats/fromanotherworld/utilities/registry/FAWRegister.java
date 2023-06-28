package acats.fromanotherworld.utilities.registry;

import org.apache.logging.log4j.util.TriConsumer;

import java.util.HashMap;
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

    public final void registerAll(TriConsumer<String, ? super FAWRegistryObject<? extends T>, Supplier<? extends T>> registerer){
        map.forEach((id, registryObject) -> registerer.accept(id, registryObject, registryObject::build));
    }
}
