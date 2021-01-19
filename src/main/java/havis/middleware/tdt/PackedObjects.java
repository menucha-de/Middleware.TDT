package havis.middleware.tdt;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PackedObjects extends ItemData {

	/**
	 * The data storage format identifier
	 */
	private String dsfid = "89";

	private ObjectInfoFormat objectInfoFormat = ObjectInfoFormat.IDLPO_DEFAULT;

	private Compaction compaction = Compaction.PACKED_OBJECT;

	private PackedObjectInvestigator investigator;

	public PackedObjects(PackedObjectInvestigator investigator) {
		super();
		this.investigator = investigator;
	}

	public PackedObjects(PackedObjectInvestigator investigator, List<Map.Entry<String, String>> dataElements) {
		super(dataElements);
		this.investigator = investigator;
	}

	public PackedObjects(PackedObjectInvestigator investigator, String dsfid, ObjectInfoFormat objectInfoFormat, List<Map.Entry<String, String>> dataElements) {
		super(dataElements);
		this.investigator = investigator;
		if (dsfid != null)
			this.dsfid = dsfid;
		this.objectInfoFormat = objectInfoFormat;
	}

	public String getDsfid() {
		return dsfid;
	}

	public void setDsfid(String dsfid) {
		this.dsfid = dsfid;
	}

	public ObjectInfoFormat getObjectInfoFormat() {
		return objectInfoFormat;
	}

	public void setObjectInfoFormat(ObjectInfoFormat objectInfoFormat) {
		this.objectInfoFormat = objectInfoFormat;
	}

	public Compaction getCompaction() {
		return compaction;
	}

	public void setCompaction(Compaction compaction) {
		this.compaction = compaction;
	}

	@Override
	public byte[] encode() throws EncoderException {
		if (this.investigator == null)
			throw new IllegalStateException("Failed to encode: PackedObjectInvestigator not specified");

		String hex = null;
		try {
			hex = this.investigator.encodePackedObjects(this);
		} catch (Exception e) {
			throw new EncoderException(e.getMessage());
		}

		if (hex != null)
			return DataTypeConverter.hexStringToByteArray(hex);

		return null;
	}

	@Override
	public String toString() {
		return "{ \"dsfid\" : \"" + dsfid + "\", \"objectInfoFormat\" : \"" + objectInfoFormat + "\", \"dataElements\" : " + dataElements.toString() + " }";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((compaction == null) ? 0 : compaction.hashCode());
		result = prime * result + ((dsfid == null) ? 0 : dsfid.hashCode());
		result = prime * result + ((objectInfoFormat == null) ? 0 : objectInfoFormat.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		PackedObjects other = (PackedObjects) obj;
		if (compaction != other.compaction)
			return false;
		if (dsfid == null) {
			if (other.dsfid != null)
				return false;
		} else if (!dsfid.equals(other.dsfid))
			return false;
		if (objectInfoFormat != other.objectInfoFormat)
			return false;
		return true;
	}

	public static enum ObjectInfoFormat {
		IDLPO_DEFAULT, IDLPO_NON_DEFAULT, IDMPO;
	}

	public static enum Compaction {
		PACKED_OBJECT, APPLICATION_DEFINED, COMPACT, UTF_8;
	}

	List<Map.Entry<String, String>> sort(PackedObjectInvestigator investigator) {
		return null;
	}

	@Override
	public PackedObjects clone() {
		PackedObjects clone = new PackedObjects(this.investigator);
		clone.compaction = this.compaction;
		clone.dsfid = this.dsfid;
		clone.numberOfReadBytes = this.numberOfReadBytes;
		clone.objectInfoFormat = this.objectInfoFormat;
		if (this.dataElements != null) {
			clone.dataElements = new ArrayList<>();
			for (Map.Entry<String, String> entry : this.dataElements) {
				clone.dataElements.add(new SimpleEntry<>(entry.getKey(), entry.getValue()));
			}
		}
		return clone;
	}
}