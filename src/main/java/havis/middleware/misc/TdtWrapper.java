package havis.middleware.misc;

import havis.middleware.misc.TdtInitiator.SCHEME;
import havis.middleware.tdt.ItemDataDecoder;
import havis.middleware.tdt.PackedObjectInvestigator;
import havis.middleware.tdt.TdtTranslator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TdtWrapper {

	private final static Logger log = Logger.getLogger(TdtWrapper.class.getName());

	private static TdtTranslator tdt = new TdtTranslator();

	private static ItemDataDecoder itemDataDecoder;

	static {
		init();
	}

	private static void init() {
		for (SCHEME scheme : SCHEME.values()) {
			try {
				log.log(Level.FINE, "Loading TDT scheme {0}", scheme);
				tdt.getTdtDefinitions().add(TdtInitiator.get(scheme));
			} catch (IOException e) {
				log.log(Level.SEVERE, "Failed to load TDT scheme " + scheme, e);
			}
		}
		// loads internal data
		PackedObjectInvestigator packedObjectInvestigator = null;
		log.log(Level.FINE, "Loading internal data for PackedObjectInvestigator");
		try {
			packedObjectInvestigator = new PackedObjectInvestigator(readPackedObjectData());
		} catch (Exception e) {
			log.log(Level.SEVERE, "Failed to load internal data for PackedObjectInvestigator", e);
		}
		itemDataDecoder = new ItemDataDecoder(packedObjectInvestigator);
	}

	private static String[] readPackedObjectData() {
		List<String> data = new ArrayList<String>();
		InputStream stream = PackedObjectInvestigator.class.getResourceAsStream("table-for-data.csv");
		if (stream != null) {
			BufferedReader reader = null;
			try {
				reader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
				String line = "";
				while ((line = reader.readLine()) != null) {
					data.add(line);
				}

			} catch (IOException e) {
				throw new IllegalStateException("Failed to read internal data", e);
			} finally {
				try {
					if (reader != null) {
						reader.close();
					}
				} catch (IOException e) {
					// ignore
				}
			}
		}
		return data.toArray(new String[data.size()]);
	}

	static void reset() {
		tdt.getTdtDefinitions().getDefinitions().clear();
		init();
	}

	public static TdtTranslator getTdt() {
		return tdt;
	}

	public static ItemDataDecoder getItemDataDecoder() {
		return itemDataDecoder;
	}
}