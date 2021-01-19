package havis.middleware.tdt;

public class TdtTranslationException extends Exception {

	private static final long serialVersionUID = 1L;

	private final static String MESSAGE = "TDT-Error : ";

	public TdtTranslationException() {
		super(MESSAGE);
	}

	public TdtTranslationException(String message) {
		super(MESSAGE + message);
	}

	public TdtTranslationException(String message, Exception innerException) {
		super(MESSAGE + message, innerException);
	}

	public TdtTranslationException(Exception innerException) {
		super(MESSAGE, innerException);
	}
}