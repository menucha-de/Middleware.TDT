package havis.middleware.tdt;

import java.util.Map;

public class SimpleEntry<K, V> implements Map.Entry<K, V> {

	private K key;
	private V value;
	
	public SimpleEntry(K key, V value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public K getKey() {
		return key;
	}

	@Override
	public V getValue() {
		return value;
	}
	
	/**
     * Replaces the key corresponding to this entry with the specified
     * key.
     *
     * @param key new key to be stored in this entry
     * @return the old key corresponding to the entry
     */
	public K setKey(K key) {
		K oldKey = this.key;
        this.key = key;
        return oldKey;
	}

	/**
     * Replaces the value corresponding to this entry with the specified
     * value.
     *
     * @param value new value to be stored in this entry
     * @return the old value corresponding to the entry
     */
	@Override
	public V setValue(V value) {
		V oldValue = this.value;
        this.value = value;
        return oldValue;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((key == null) ? 0 : key.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof SimpleEntry))
			return false;
		@SuppressWarnings("rawtypes")
		SimpleEntry other = (SimpleEntry) obj;
		if (key == null) {
			if (other.key != null)
				return false;
		} else if (!key.equals(other.key))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "{ \"key\" : \"" + key + "\", \"value\" : " + value + "\" }";
	}
}
