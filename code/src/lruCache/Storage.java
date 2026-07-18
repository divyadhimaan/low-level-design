import java.util.HashMap;
import java.util.Map;

public class Storage {
    private int capacity;
    private Map<Integer, String> storage;

    public Storage(int capacity)
    {
        this.capacity = capacity;
        storage = new HashMap<>();
        System.out.println("Cache Storage set to " +  this.capacity);
    }

    public void addKey(int key, String value, EvictionEngine evictionEngine)
    {
        if(storage.containsKey(key)) {
            storage.remove(key);
        }
        if(storage.size() == capacity) {
            int evictedKey = evictionEngine.EvictKey();
            System.out.println("Evicting key: " + evictedKey);
            storage.remove(evictedKey);
        }
        storage.put(key, value);
    }
    public String getKey(int key)
    {
        if(storage.containsKey(key))
            return storage.get(key);
        return "null";
    }

}
