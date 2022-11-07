package br.com.qintess.Util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

import javax.management.RuntimeErrorException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.LoggingException;

public class Logger {
	final static org.apache.logging.log4j.Logger log = LogManager.getLogger(Logger.class);

	File logFolder = new File("c:/logs");
	static FileInputStream in;
	static OutputStream out;

	public void getCauseMainError(Throwable error, File file)
			throws LoggingException, IOException, FileNotFoundException {
		if (error.getCause().equals("null")) {
			throw new RuntimeException("Error during execution" + " NullPointer + %s"
					+ String.format("%s", new RuntimeErrorException(null)));

		} else {
			if (file.isDirectory() && file.isFile()) {
				in = new FileInputStream(file.getPath());
				int i = in.read();
				System.out.println((char) i);
			}
			in.close();
		}
	}
}
