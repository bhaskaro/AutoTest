package com.oggu.auto.core.common;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
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
import org.json.simple.JSONArray;

public class CommonUtils implements CommonConstants {

	public static void main(String[] args) {

		System.out.println(parseIntOrDefault(" 5.0 ", 0));

		Map<String, String> scriptProperties = getScriptRScnProperties("hello=world,,,,hello2=world2");

		System.out.println("scriptProperties : " + scriptProperties);

		System.out.println(CommonUtils.toStringDate(new Date(), "yyyy-MM-dd'T'HH:mm:ss'Z'"));
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

	public static Logger getLogger(Object obj) {

		Logger logger = null;

		if (obj instanceof String) {
			logger = LogManager.getLogger(String.valueOf(obj));
		} else if (obj instanceof Class) {
			logger = LogManager.getLogger((Class<?>) obj);
		} else {
			logger = LogManager.getLogger(obj);
		}
		return logger;
	}

	public static Map<String, String> getScriptRScnProperties(String str) {

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

	/**
	 * Prints the input text as Text Banner.
	 * 
	 * @param bannerTxt
	 * 
	 * @return Text Banner
	 */
	public static String getBanner(String bannerTxt, int size) {

		StringBuilder bannerOut = new StringBuilder();

		// need to adjust for width and height
		BufferedImage image = new BufferedImage(144, 32, BufferedImage.TYPE_INT_RGB);
		Graphics g = image.getGraphics();
		g.setFont(new Font("Dialog", Font.PLAIN, size));
		Graphics2D graphics = (Graphics2D) g;
		graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		// the banner text may affect width and height
		graphics.drawString(bannerTxt, 6, 24);
		// ImageIO.write(image, "png", File.createTempFile("AsciiBanner.png", null));

		// need to adjust for width and height
		for (int y = 0; y < 32; y++) {
			StringBuilder sb = new StringBuilder();
			// need to adjust for width and height
			for (int x = 0; x < 144; x++)
				sb.append(image.getRGB(x, y) == -16777216 ? " " : image.getRGB(x, y) == -1 ? "#" : "*");
			if (sb.toString().trim().isEmpty())
				continue;
			bannerOut.append(sb).append(NEWLINE_STRING);
		}
		return bannerOut.toString();
	}

	@SuppressWarnings("unchecked")
	public static Map<String, String> convertToMap(JSONArray jsonArray) {

		Map<String, String> map = null;

		if (jsonArray != null && !jsonArray.isEmpty()) {

			Stream<String> ss = jsonArray.stream().map(String::valueOf);
			List<String> list = ss.collect(Collectors.toList());

			map = list.stream().collect(Collectors.toList()).stream().filter(s -> s.contains(EQUAL_TO_STRING))
					.map(elem -> elem.split(EQUAL_TO_STRING)).collect(Collectors.toMap(e -> e[0], e -> e[1]));

		}

		return map;
	}

	@SuppressWarnings("unchecked")
	public static Object toJSONArray(Map<String, String> map) {

		final JSONArray jsonArray;

		if (map != null && !map.isEmpty()) {
			jsonArray = new JSONArray();

			map.entrySet().stream()
					.map(e -> new StringBuilder().append(e.getKey()).append("=").append(e.getValue()).toString())
					.forEach(jsonArray::add);
		} else {
			jsonArray = null;
		}

		return jsonArray;
	}
}
