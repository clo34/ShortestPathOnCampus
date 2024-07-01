// == CS400 Spring 2024 File Header Information ==
// Name: Charles Lo
// Email: clo34@wisc.edu
// Lecturer: Gary Dahl
// Notes to Grader: <optional extra notes>

import java.util.LinkedList;
import java.util.NoSuchElementException;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

public class HashtableMap <KeyType, ValueType> implements MapADT<KeyType, ValueType>{
    protected class Pair {
        public KeyType key;
        public ValueType value;

        public Pair(KeyType key, ValueType value) {
            this.key = key;
            this.value = value;
        }

    }

    protected LinkedList<Pair>[] table;
    //step 15
    protected int size;
    protected int capacity;
    protected static final double LOAD_FACTOR_THRESHOLD = 0.8;

    @SuppressWarnings("unchecked")
    public HashtableMap(int capacity) {
        this.capacity = capacity;
        this.size = 0;
        table = (LinkedList<Pair>[]) new LinkedList[capacity];
        for (int i = 0; i < capacity; i++) {
            table[i] = new LinkedList<>();
        }
        //for each index in table:
            //table[index] = new linkedlist
    }
    public HashtableMap() {
        this(64); // default capacity = 64
    }

    //step 13
    /**
     * Computes the hash value for a given key.
     *
     * @param key the key for which to compute the hash value
     * @return the computed hash value
     */
    private int hash(KeyType key) {
        return Math.abs(key.hashCode()) % capacity;
    }

    /**
     * Helper method that resizes the hash table by doubling its capacity and rehashing all existing key-value pairs.
     * This method is called when the load factor of the hash table exceeds the threshold.
     */
    private void resize() {
        int newCapacity = capacity * 2;
        LinkedList<Pair>[] newTable = (LinkedList<Pair>[]) new LinkedList[newCapacity];

        for (LinkedList<Pair> bucket : table) {
            if (bucket != null) {
                for (Pair pair : bucket) {
                    int index = Math.abs(pair.key.hashCode()) % newCapacity;
                    if (newTable[index] == null) {
                        newTable[index] = new LinkedList<>();
                    }
                    newTable[index].add(pair);
                }
            }
        }

        table = newTable;
        capacity = newCapacity;
    }


    //MapADT methods
    @Override
    public void put(KeyType key, ValueType value) throws IllegalArgumentException {
        if (key == null) {
            throw new NullPointerException("Key cannot be null");
        }

        int index = hash(key);
        if (table[index] == null) {
            table[index] = new LinkedList<>();
        } else {
//            for (Pair pair : table[index]) {
//                if (pair.key.equals(key)) {
//                    throw new IllegalArgumentException("Key already exists");
//                }
//            }
        }
        table[index].add(new Pair(key, value));
        size++;


        if ((double)size / capacity >= LOAD_FACTOR_THRESHOLD) {
            resize();
        }
    }

    @Override
    public boolean containsKey(KeyType key) {
        if (key == null) {
            return false;
        }

        int index = hash(key);
        if (table[index] != null) {
            for (Pair pair : table[index]) {
                if (pair.key.equals(key)) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public ValueType get(KeyType key) throws NoSuchElementException {
        if (key == null) {
            throw new NullPointerException("Key cannot be null");
        }

        int index = hash(key);
        if (table[index] != null) {
            for (Pair pair : table[index]) {
                if (pair.key.equals(key)) {
                    return pair.value;
                }
            }
        }

        throw new NoSuchElementException("Key not found");
    }

    @Override
    public ValueType remove(KeyType key) throws NoSuchElementException {
        if (key == null) {
            throw new NullPointerException("Key cannot be null");
        }

        int index = hash(key);
        if (table[index] != null) {
            for (Pair pair : table[index]) {
                if (pair.key.equals(key)) {
                    table[index].remove(pair);
                    size--;
                    return pair.value;
                }
            }
        }

        throw new NoSuchElementException("Key not found");
    }

    @Override
    public void clear() {
        for(int i = 0; i < capacity; i++){
            table[i] = null;
        }
        size = 0;
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public int getCapacity() {
        return capacity;
    }

    // Junit 5 Tests
    /**
     * Test the put and get methods
     * */
    @Test
    void testPutAndGet() {
        HashtableMap<String, Integer> map = new HashtableMap<>();
        map.put("one", 1);
        map.put("two", 2);

        assertEquals(1, map.get("one"));
        assertEquals(2, map.get("two"));
    }

    /**
     * Test containsKey method.
     */
    @Test
    void testContainsKey() {
        HashtableMap<String, Integer> map = new HashtableMap<>();
        map.put("one", 1);
        map.put("two", 2);

        assertTrue(map.containsKey("one"));
        assertFalse(map.containsKey("three"));
    }

    /**
     * Test the remove method
     * */
    @Test
    void testRemove() {
        HashtableMap<String, Integer> map = new HashtableMap<>();
        map.put("one", 1);
        map.put("two", 2);

        assertEquals(1, map.remove("one"));
        assertFalse(map.containsKey("one"));
    }

    /**
     * Test the clear method
     * */
    @Test
    void testClear() {
        HashtableMap<String, Integer> map = new HashtableMap<>();
        map.put("one", 1);
        map.put("two", 2);

        map.clear();

        assertEquals(0, map.getSize());
    }

    /**
     * Test the getSize method
     * */
    @Test
    void testGetSize() {
        HashtableMap<String, Integer> map = new HashtableMap<>();
        map.put("one", 1);
        map.put("two", 2);

        assertEquals(2, map.getSize());
    }

    /**
     * Test the getCapacity method
     * */
    @Test
    void testGetCapacity() {
        HashtableMap<String, Integer> map = new HashtableMap<>(10);

        assertEquals(10, map.getCapacity());
    }
}



