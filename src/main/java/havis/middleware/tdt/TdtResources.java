package havis.middleware.tdt;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.TextResource;

public interface TdtResources extends ClientBundle {

	public static final TdtResources INSTANCE = GWT.create(TdtResources.class);

	@Source("gdti-96.json")
	public TextResource gdti96();

	@Source("gdti-113.json")
	public TextResource gdti113();

	@Source("giai-64.json")
	public TextResource giai64();

	@Source("giai-96.json")
	public TextResource giai96();

	@Source("giai-202.json")
	public TextResource giai202();

	@Source("gid-96.json")
	public TextResource gid96();

	@Source("grai-64.json")
	public TextResource grai64();

	@Source("grai-96.json")
	public TextResource grai96();

	@Source("grai-170.json")
	public TextResource grai170();

	@Source("gsrn-96.json")
	public TextResource gsrn96();

	@Source("sgln-64.json")
	public TextResource sgln64();

	@Source("sgln-96.json")
	public TextResource sgln96();

	@Source("sgln-195.json")
	public TextResource sgln195();

	@Source("sgtin-64.json")
	public TextResource sgtin64();

	@Source("sgtin-96.json")
	public TextResource sgtin96();

	@Source("sgtin-198.json")
	public TextResource sgtin198();

	@Source("sscc-64.json")
	public TextResource sscc64();

	@Source("sscc-96.json")
	public TextResource sscc96();
	
	@Source("table-for-data.csv")
	public TextResource tableForData();
}