package com.oggu.auto.core.common;

import java.io.InputStreamReader;
import java.io.Reader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CommonUtils implements CommonConstants {

	public static void main(String[] args) {

		System.out.println(parseIntOrDefault(" 5.0 ", 0));

		Map<String, String> scriptProperties = getScriptProperties("hello=world,,,,hello2=world2");

		System.out.println("scriptProperties : " + scriptProperties);
	}

	public static Integer parseIntOrDefault(Object toParse, int defaultValue) {

		try {
			return Integer.parseInt(String.valueOf(toParse).trim());
		} catch (NumberFormatException e) {
			return defaultValue;
		}
	}

	public static double parseDoubleOrDefault(Object toParse, double defaultValue) {

		try {
			return Double.parseDouble(String.valueOf(toParse).trim());
		} catch (NumberFormatException e) {
			return defaultValue;
		}
	}

	public static boolean parseBooleanOrDefault(Object toParse, boolean defaultValue) {

		try {
			return Boolean.parseBoolean(String.valueOf(toParse).trim());
		} catch (Exception e) {
			return defaultValue;
		}
	}

	public static Reader getCPResourceAsStream(String resourceName) {

		return new InputStreamReader(CLASS_LOADER.getResourceAsStream(resourceName));
	}

	public static String[] safeSplit(String str, String delim) {

		String[] tests = null;

		if (StringUtils.isNotBlank(str)) {

			List<String> temp = new ArrayList<String>();

			if (str.contains(delim)) {
				tests = str.split(delim);

				for (String string : tests) {

					if (string != null && !string.trim().isEmpty())
						temp.add(string.trim());
				}
			} else {
				temp.add(str.trim());
			}
			tests = temp.toArray(new String[temp.size()]);
		}
		return tests;
	}

	public static Logger getLogger(Class<?> clazz) {

		return LogManager.getLogger(clazz);
	}

	public static Map<String, String> getScriptProperties(String str) {

		Map<String, String> collect = null;

		if (StringUtils.contains(str, COMMA_STRING)) {

			collect = Stream.of(StringUtils.split(str, COMMA_STRING)).map(s -> StringUtils.split(s, EQUAL_TO_STRING))
					.collect(Collectors.toMap(s -> s[0], s -> s[1]));
		}

		return collect;
	}

	public static String toStringDate(Date date, String patern) {

		return new SimpleDateFormat(patern).format(date);
	}

}
