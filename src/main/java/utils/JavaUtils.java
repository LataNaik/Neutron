package utils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class JavaUtils {

	public static String datetime(String dateFormat) {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		return sdf.format(cal.getTime());
	}

	public static File[] listFiles(String dirPath) {
		File dir = new File(dirPath);
		return dir.listFiles();
	}

	public static String getCaller() {
		StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
		StackTraceElement stackTraceElement = stackTraceElements[2];
		return stackTraceElement.getMethodName();
	}
}
