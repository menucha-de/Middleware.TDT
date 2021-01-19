package havis.middleware.tdt;


public class ItemDataDecoder {

	private PackedObjectInvestigator packedObjectInvestigator;

	public ItemDataDecoder(PackedObjectInvestigator packedObjectInvestigator) {
		this.packedObjectInvestigator = packedObjectInvestigator;
	}

	public PackedObjectInvestigator getPackedObjectInvestigator() {
		return this.packedObjectInvestigator;
	}

	public ItemData decode(int bank, byte[] data) {
		ItemData result = DataFormat3MessageDecoder.INSTANCE.decode(bank, data);
		if (result == null) {
			byte[] dataToDecode = data;
			if (bank == 1) {
				// remove AFI
				dataToDecode = new byte[data.length - 1];
				System.arraycopy(data, 1, dataToDecode, 0, dataToDecode.length);
			}
			result = this.packedObjectInvestigator.decodePackedObjects(dataToDecode);
		}
		if (result == null)
			throw new UnsupportedOperationException("No data to decode");
		return result;
	}
}
