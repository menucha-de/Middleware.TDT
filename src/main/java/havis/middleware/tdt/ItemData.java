package havis.middleware.tdt;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class ItemData {

	protected int numberOfReadBytes = 0;
	protected List<Map.Entry<String, String>> dataElements;

	public ItemData() {
		super();
	}

	public ItemData(List<Map.Entry<String, String>> dataElements) {
		this();
		this.dataElements = dataElements;
	}

	/**
	 * @return the data elements
	 */
	public List<Map.Entry<String, String>> getDataElements() {
		if (dataElements == null)
			dataElements = new ArrayList<Map.Entry<String, String>>();
		return dataElements;
	}

	/**
	 * @param dataElements
	 *            the data elements
	 */
	public void setDataElements(List<Map.Entry<String, String>> dataElements) {
		this.dataElements = dataElements;
	}

	/**
	 * @param numberOfReadBytes
	 *            set the number of bytes that have been read
	 */
	protected void setNumberOfReadBytes(int numberOfReadBytes) {
		this.numberOfReadBytes = numberOfReadBytes;
	}

	/**
	 * @return The number of bytes that have been read
	 */
	public int getNumberOfReadBytes() {
		return numberOfReadBytes;
	}

	/**
	 * @return the encoded data
	 * @throws EncoderException
	 *             if encoding fails
	 */
	public abstract byte[] encode() throws EncoderException;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dataElements == null) ? 0 : dataElements.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ItemData other = (ItemData) obj;
		if (dataElements == null) {
			if (other.dataElements != null)
				return false;
		} else if (!dataElements.equals(other.dataElements))
			return false;
		return true;
	}

	public abstract ItemData clone();

}