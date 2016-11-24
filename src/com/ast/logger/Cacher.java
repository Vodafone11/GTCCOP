package com.ast.logger;

import java.io.FileInputStream;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;

public class Cacher {
	private static HashMap<String, String> Cacher = null;

	static {
		Cacher = new HashMap();
		//cacheLoader("D:/CAPWorkspace/GTC_POC/files/OIMProp.properties");
		cacheLoader("./files/OIMProp.properties");
	}

	private static void cacheLoader(String fileName) {
		String key = null;
		String value = null;

		Properties properties = null;
		try {
			properties = new Properties();
			properties.load(new FileInputStream(fileName));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		Enumeration keys = properties.keys();
		while (keys.hasMoreElements()) {
			key = (String)keys.nextElement();
			value = properties.getProperty(key);
			if ((key != null) && (value != null)) {
				Cacher.put(key, value);
			}
			key = null;
			value = null;
		}
		keys = null;
		properties.clear();
		properties = null;
	}

	public static String getproperty(String key) {
		return (String)Cacher.get(key);
	}

	public static void closeCache() {
		if (Cacher != null) {
			Cacher.clear();
			Cacher = null;
		}
	}
}
