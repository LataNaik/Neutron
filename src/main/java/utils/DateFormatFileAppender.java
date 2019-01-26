package utils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.FileAppender;

public class DateFormatFileAppender extends FileAppender {

	public void setFile(String file) {
		super.setFile(file);
	}

	public synchronized void setFile(String fileName, boolean append, boolean bufferedIO, int bufferSize)
			throws IOException {
		Date d = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmssSS");
		fileName = fileName.replaceAll("%timestamp", format.format(d));
		super.setFile(fileName, append, bufferedIO, bufferSize);
	}

}
