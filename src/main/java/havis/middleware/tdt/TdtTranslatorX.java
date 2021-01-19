package havis.middleware.tdt;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class TdtTranslatorX extends TdtTranslator {

	/**
	 * Translates data from input stream
	 * 
	 * @param input
	 *            The input stream
	 * @return The tag info
	 * @throws TdtTranslationException
	 *             If translation failed
	 * @throws IOException
	 *             If reading from stream failed
	 */
	public TdtTagInfo translate(InputStream input)
			throws TdtTranslationException, IOException {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		byte[] bytes = new byte[1024];
		int length = 0;
		while ((length = input.read(bytes)) > 0)
			output.write(bytes, 0, length);
		return translate(output.toByteArray(), null, null, null, null);
	}
}