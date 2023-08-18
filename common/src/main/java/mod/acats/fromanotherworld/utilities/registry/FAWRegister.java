package mod.acats.fromanotherworld.utilities.registry;

import java.util.HashMap;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

@Deprecated
public class FAWRegister<T> {
    public FAWRegister(){
    }
    private final HashMap<String, FAWRegistryObject<? extends T>> map = new HashMap<>();
    public <E extends T> FAWRegistryObject<E> register(String id, Supplier<E> supplier){
        FAWRegistryObject<E> fawRegistryObject = new FAWRegistryObject<>(supplier);
        map.put(id, fawRegistryObject);
        return fawRegistryObject;
    }

    public final void registerAll(BiConsumer<String, Supplier<? extends T>> registerer){
        map.forEach((id, registryObject) -> registerer.accept(id, registryObject::build));
    }

    public final void forEach(BiConsumer<String, Supplier<? extends T>> action){
        map.forEach((id, registryObject) -> action.accept(id, registryObject::get));
    }

    public final T get(String id) {
        return this.map.get(id).get();
    }
}
