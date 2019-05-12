package com.oggu.auto.core.common;

import java.util.UUID;

import com.oggu.auto.core.config.ConfigReader;

public interface CommonConstants {

	String EMPTY_STRING = "";
	String NEWLINE_STRING = "\n";
	String COMMA_STRING = ",";
	String EQUAL_TO_STRING = "=";
	String UTF8 = "UTF-8";

	int EXCTR_THREAD_DELAY_SECS = 5;

	ClassLoader CLASS_LOADER = ConfigReader.class.getClassLoader();

	String OATS_CONFIG_FILE = "oatssrvrs.json";
	String TESTS_CONFIG_FILE = "tests.json";
	String RUNTESTS_CONFIG_FILE = "runtests.json";

	String TEST_SOURCE_DIR = "RUNTESTS";
	String TEST_EXECUTION_DIR = "OUTPUT";

	String RANDOM_UUID = UUID.randomUUID().toString();

	String TEST_SESSION_TIME_FORMAT = "yyyy-mm-dd-hh-MM-";
	String TESTS_SESSION_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
	String SESSIONS_FILE = "sessions.txt";
}
