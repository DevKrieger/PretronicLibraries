package net.pretronic.libraries.utility.map;

import java.util.HashMap;
import java.util.Map;

public final class Maps {

    public static <K, V> Map<K, V> of(K key, V value) {
        Map<K, V> map = new HashMap<>();
        map.put(key, value);
        return map;
    }

    @SafeVarargs
    public static <K, V> Map<K, V> of(Pair<K, V>... values) {
        Map<K, V> map = new HashMap<>();
        for (Pair<K, V> value : values) {
            map.put(value.getKey(), value.getValue());
        }
        return map;
    }
}
