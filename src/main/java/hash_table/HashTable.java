package hash_table;

import java.util.LinkedList;

/**
 * A generic implementation of a hash table using an array of linked lists for collision resolution.
 * This class provides a way to store keys efficiently, allowing for average-case
 * constant time complexity for insertion, deletion, and search operations.
 *
 * <p>
 * The hash table uses separate chaining for collision resolution. Each bucket in the hash table is a
 * linked list that stores its keys. When a collision occurs (i.e., when two keys hash to the same index),
 * the new key is simply added to the corresponding linked list.
 * </p>
 *
 * <p>
 * The hash table automatically resizes itself when the load factor exceeds {@code loadFactor}. The load factor is
 * defined as the ratio of the number of entries to the number of buckets. When resizing occurs,
 * all existing entries are rehashed and inserted into the new buckets.
 * </p>
 *
 * @param <K> the type of keys maintained by this hash table
 */
public class HashTable<K> {
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int DEFAULT_BUCKET_SIZE = 53;

    private LinkedList<K>[] buckets;
    private final float loadFactor;
    private int size;

    /**
     * Constructs a HashTable with the default hash size (17).
     */
    public HashTable() {
        this(DEFAULT_BUCKET_SIZE);
    }

    /**
     * Constructs a HashTable with the specified hash size.
     *
     * @param bucketSize the number of buckets in the hash table
     */
    public HashTable(int bucketSize) {
        this(bucketSize, DEFAULT_LOAD_FACTOR);
    }

    /**
     * Constructs a HashTable with the specified hash size and load factor.
     *
     * @param bucketSize the number of buckets in the hash table
     * @param loadFactor the  
     */
    public HashTable(int bucketSize, float loadFactor) {
        this.buckets = new LinkedList[bucketSize];
        this.loadFactor = loadFactor;

        // calls clear to initialize the buckets
        this.clear();
    }

    /**
     * Inserts the specified key into the hash table.
     *
     * @param key the key to be inserted
     */
    public void insert(K key) {
        int hash = computeHash(key);
        ensureCapacity(this.size + 1);
        if (!this.buckets[hash].contains(key)) {
            this.buckets[hash].add(key);
            this.size += 1;
        }
    }

    /**
     * Deletes the key from this hash table.
     *
     * @param key the key which should be deleted from the hash table
     * @return boolean indicating if the key was succesfully deleted
     */
    @SuppressWarnings("")
    public boolean delete(K key) {
        int hash = this.computeHash(key);

        if (this.buckets[hash].remove(key)) {
            this.size -= 1;
            return true;
        }

        return false;
    }

    /**
     * Searches for the key in this hash table.
     *
     * @param key the key which should be deleted from the hash table
     * @return boolean indicating whether or not this hash table contains this key
     */
    public boolean contains(K key) {
        int hash = this.computeHash(key);
        return this.buckets[hash].contains(key);
    }

    /**
     * Clears the contents of the hash table by reinitializing each bucket.
     */
    public final void clear() {
        for (int i = 0; i < buckets.length; i++) {
            this.buckets[i] = new LinkedList<>();
        }
        this.size = 0;
    }

    /**
     * Gets the number of keys in the hash table.
     *
     * @return the number of keys in the hash table
     */
    public int size() {
        return this.size;
    }

    /**
     * Computes the hash code for the specified key.
     *
     * @param key the key for which the hash code is to be computed
     * @return the hash code corresponding to the key
     */
    private int computeHash(K key) {
        return Math.floorMod(key.hashCode(), buckets.length);
    }

    /**
     * Ensures that this hash table has enough capacity without surpassing the load factor.
     * If it doesn't, execute rehash and resizes the hash table.
     */
    private void ensureCapacity(int capacity) {
        float currLoadFactor = capacity / (float) buckets.length;
        if (currLoadFactor >= loadFactor) rehash();
    }

    /**
     * Resizes this hash table to the next prime after 2*{@code currPrime}.
     * Is also responsible for recalculating the hashes of all its keys.
     */
    private void rehash() {
        int nextPrime = getNextPrime(buckets.length);
        HashTable<K> newHashTable = new HashTable<>(nextPrime);

        for (LinkedList<K> bucket : buckets) {
            for (K key : bucket) newHashTable.insert(key);
        }

        this.buckets = newHashTable.buckets;
    }

    /**
     * Gets the next prime after 2*{@code currPrime}
     * 
     * @return the next prime after 2*{@code currPrime}
     */
    private int getNextPrime(int currPrime) {
        int possiblePrime = 2 * currPrime + 1;
        while (!isPrime(possiblePrime)) possiblePrime += 2;
        return possiblePrime;
    }

    /**
     * Simplified implementation for primality test, assuming that the number is not even.
     * 
     * @return whether the odd number is prime
     */
    private boolean isPrime(int number) {
        for (int divisor = 3; divisor < Math.sqrt(number); divisor += 2) {
            if (number % divisor == 0) return false;
        }
        return true;
    }
}