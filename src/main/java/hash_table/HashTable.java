package hash_table;

/**
 * A generic implementation of a hash table using open adressing for collision resolution.
 * This class provides a way to store keys efficiently, allowing for average-case
 * constant time complexity for insertion and search operations.
 *
 * <p>
 * The hash table uses linear probing for collision resolution. Linear probing is a collision resolution method where 
 * each slot in the hash table is checked in a sequential manner until an empty slot is found.
 * </p>
 *
 * <p>
 * The hash table automatically resizes itself when the load factor exceeds {@code loadFactor}. The load factor is
 * defined as the ratio of the number of entries to the number of this.hashTable. When resizing occurs,
 * all existing entries are rehashed and reinserted into the resized hash table.
 * </p>
 *
 * @param <Key> the type of keys maintained by this table
 */
public class HashTable<Key> {
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int DEFAULT_TABLE_SIZE = 37;

    private final float loadFactor;
    private Key[] hashTable;
    private int size;

    /**
     * Constructs a HashTable with the default hash size (37).
     */
    public HashTable() {
        this(DEFAULT_TABLE_SIZE);
    }

    /**
     * Constructs a HashTable with the specified hash size.
     *
     * @param tableSize the number of this.hashTable in the hash table
     */
    public HashTable(int tableSize) {
        this(tableSize, DEFAULT_LOAD_FACTOR);
    }

    /**
     * Constructs a HashTable with the specified hash size and load factor.
     *
     * @param tableSize the size of this hash table
     * @param loadFactor the maximum occupancy rate of this hash table
     */
    @SuppressWarnings("unchecked")
    public HashTable(int tableSize, float loadFactor) {
        this.hashTable = (Key[]) new Object[tableSize];
        this.loadFactor = loadFactor;
    }

    /**
     * Inserts the specified key into the hash table.
     *
     * @param key the key to be inserted
     */
    public void insert(Key key) {
        ensureCapacity(this.size + 1);
        int keyHash = computeHash(key);

        for (; this.hashTable[keyHash] != null; keyHash = circularIncrement(keyHash)) {
            if (key.equals(this.hashTable[keyHash])) return;
        }

        this.hashTable[keyHash] = key;
        size++;
    }

    /**
     * Searches for the key in this hash table.
     *
     * @param key the key to search for
     * @return boolean indicating whether or not this hash table contains this key
     */
    public boolean contains(Key key) {
        for (int i = computeHash(key); this.hashTable[i] != null; i = circularIncrement(i)) {
            if (key.equals(this.hashTable[i])) return true;
        }

        return false;
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
    private int computeHash(Key key) {
        return Math.floorMod(key.hashCode(), this.hashTable.length);
    }

    /**
     * Increments index in circular manner, restrained by the length
     * of this hash table to ensure that no exception occurs.
     */
    private int circularIncrement(int index) {
        return (index + 1) % this.hashTable.length;
    }

    /**
     * Ensures that this hash table has enough capacity without surpassing the load factor.
     * If it doesn't, execute rehash and resizes the hash table.
     */
    private void ensureCapacity(int capacity) {
        float currLoadFactor = capacity / (float) this.hashTable.length;
        if (currLoadFactor >= loadFactor) rehash();
    }

    /**
     * Resizes this hash table to the next prime after 2*{@code currPrime}.
     * Is also responsible for recalculating the hashes of all its keys.
     */
    private void rehash() {
        int nextPrime = getNextPrime(this.hashTable.length);
        HashTable<Key> newHashTable = new HashTable<>(nextPrime);

        for (Key key : this.hashTable) {
            if (key != null) newHashTable.insert(key);
        }

        this.hashTable = newHashTable.hashTable;
    }

    /**
     * Gets the next prime after 2*{@code currPrime}
     * 
     * @param currPrime the current prime
     * @return the next prime after 2*{@code currPrime}
     */
    private int getNextPrime(int currPrime) {
        int possiblePrime = 2 * currPrime + 1;
        while (!isPrime(possiblePrime)) possiblePrime += 2;
        return possiblePrime;
    }

    /**
     * Simplified implementation for primality test, assuming that {@code number} != 1 and is not even.
     * 
     * @param number the number to test for primality
     * @return whether the odd number is prime
     */
    private boolean isPrime(int number) {
        for (int divisor = 3; divisor < Math.sqrt(number); divisor += 2) {
            if (number % divisor == 0) return false;
        }
        return true;
    }
}