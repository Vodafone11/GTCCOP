package com.ast.logger;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class UtilityLogger {
	private static Logger logger;
	private static UtilityLogger loggerUtility = null;

	public static UtilityLogger getUtilityLogger(String s) {
		if (loggerUtility == null) {
			loggerUtility = new UtilityLogger();
			if (logger == null) {
				System.out.println(s);
				PropertyConfigurator.configure(s);
				logger = Logger.getLogger("ASTCustomLogger");
			}
		}
		return loggerUtility;
	}

	public void debug(String s) {
		if (logger.isDebugEnabled()) {
			logger.debug(s);
		}
	}

	public void error(String s) {
		logger.error(":::::::::::::ERROR STARTS::::::::::::::::::");
		logger.error(s);
		logger.error("::::::::::::::::::ERROR ENDS::::::::::::::::::\n");
	}

	public void info(String s) {
		if (logger.isInfoEnabled()) {
			logger.info(s);
		}
	}

	public void setStackTrace(Throwable throwable) {
		StringWriter stringWriter = new StringWriter();
		PrintWriter writer = new PrintWriter(stringWriter);
		throwable.printStackTrace(writer);
		logger.error(":::::::::::::::::: Start Stack Trace ::::::::::::::::::");
		logger.error(throwable.getMessage());
		logger.error(stringWriter.toString());
		logger.error(":::::::::::::::::: End Stack Trace ::::::::::::::::::");
		writer = null;
		stringWriter = null;
	}

	public void warn(String s) {
		logger.warn(s);
	}
}
