package havis.middleware.misc;

import havis.middleware.tdt.EpcTagDataTranslation;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import com.fasterxml.jackson.databind.ObjectMapper;

public class TdtGenerator {

	private static Unmarshaller unmarshaller;

	static {
		try {
			JAXBContext context = JAXBContext
					.newInstance(EpcTagDataTranslation.class);
			unmarshaller = context.createUnmarshaller();
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}
	private final static FilenameFilter filter = new FilenameFilter() {
		@Override
		public boolean accept(File file, String filename) {
			return filename.matches(".*\\.xml");
		}
	};

	private static void process(File target, File source) throws IOException,
			JAXBException {
		ObjectMapper mapper = new ObjectMapper();
		File[] files = source.listFiles(filter);
		if (files != null) {
			for (File file : files) {
				EpcTagDataTranslation epcTagDataTranslation = unmarshaller
						.unmarshal(new StreamSource(file),
								EpcTagDataTranslation.class).getValue();
				epcTagDataTranslation.setDate(null);
				String filename = target.getAbsolutePath()
						+ File.separator
						+ file.getName()
								.substring(0, file.getName().length() - 4)
								.toLowerCase() + ".json";
				FileOutputStream stream = new FileOutputStream(filename);
				try {
					mapper.writeValue(stream, epcTagDataTranslation);
				} finally {
					stream.close();
				}
			}
		}
	}

	public static void main(String[] args) throws IOException, JAXBException {
		List<String> list = Arrays.asList(args);
		Iterator<String> iter = list.iterator();
		File target = new File(".");
		while (iter.hasNext()) {
			String value = iter.next();
			switch (value) {
			case "-t":
				if (iter.hasNext()) {
					target = new File(iter.next());
					if (!target.isDirectory()) {
						System.out.println("Target path '" + target
								+ "' must be a directory");
						return;
					}
				} else {
					System.out.println("Missing target path");
					return;
				}
				break;
			default:
				do {
					File file = new File(value);
					if (file.isDirectory()) {
						process(target, file);
					} else {
						System.out.println("Source path must be a directory");
						return;
					}
				} while (iter.hasNext());
			}
		}
	}
}