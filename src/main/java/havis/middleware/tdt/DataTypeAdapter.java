package havis.middleware.tdt;

import java.util.Calendar;
import java.util.Date;

import javax.xml.bind.DatatypeConverter;

public class DataTypeAdapter {

	public static Date parseDate(String string) {
		return DatatypeConverter.parseDate(string).getTime();
	}

	public static String printDate(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return DatatypeConverter.printDate(calendar);
	}
}