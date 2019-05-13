/**
 * 
 */
package com.oggu.auto.core.sess;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.oggu.auto.core.common.CommonConstants;
import com.oggu.auto.core.common.CommonUtils;
import com.oggu.auto.core.excep.AutoRuntimeException;
import com.oggu.auto.core.model.Test;

/**
 * @author bhaskaro
 *
 */
public class SessionUtil implements CommonConstants {

	private static final Logger logger = CommonUtils.getLogger(SessionUtil.class);

	private static File sessFile = null;

	static {
		sessFile = new File(TEST_EXECUTION_DIR, SESSIONS_FILE);

		if (!sessFile.exists()) {
			try {
				logger.info("file doesnt exist in static");
				FileUtils.writeStringToFile(sessFile, "{\"sessions\":{}}", UTF8);
			} catch (IOException e) {
				logger.error(e);
				throw new AutoRuntimeException(e.getMessage(), e);
			}
		}
	}

	/**
	 * @param args
	 * @throws ParseException
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public static void main(String[] args) throws FileNotFoundException, IOException, ParseException {

		Test test = new Test();
		test.setBaselineFlrPerc(0.444);
		test.setBaselineFlrs(5);
		test.setBaselineTps(45);
		test.setDefaultOatsSrvr("default");
		test.setDir("getusers1");
		test.setDuration(30);
		test.setIterations(5);
		test.setName("getusers1");
		test.setThreads(10);
		test.setUsecaseName("GETUSERS");
		saveSessionDetails(RANDOM_UUID, "12234556-" + test.getUsecaseName(), test);

		test = new Test();
		test.setBaselineFlrPerc(0.444);
		test.setBaselineFlrs(5);
		test.setBaselineTps(45);
		test.setDefaultOatsSrvr("default");
		test.setDir("getusers2");
		test.setDuration(30);
		test.setIterations(5);
		test.setName("getusers2");
		test.setThreads(10);
		test.setUsecaseName("GETUSERS");
		saveSessionDetails(RANDOM_UUID, "45561223-" + test.getUsecaseName(), test);

	}

	@SuppressWarnings("unchecked")
	public static boolean saveSessionDetails(String uuid, String testSession, Test test) {

		try {

			logger.debug("=================== reading sessions file : {}", sessFile.getAbsolutePath());
			JSONObject root = (JSONObject) new JSONParser().parse(new FileReader(sessFile));

			JSONObject sessions = (JSONObject) root.get("sessions");

			JSONObject session = (JSONObject) sessions.get(uuid);

			if (session == null) {
				logger.info("sess doesnt exist.");

				session = new JSONObject();

				session.put("session-id", uuid);
				session.put("created-on", CommonUtils.toStringDate(new Date(), TESTS_SESSION_TIME_FORMAT));

				sessions.put(uuid, session);
			} else {
				logger.info("sess exist : {}", session);
			}

			JSONObject testDtls = new JSONObject();
			testDtls.put("name", test.getName());
			testDtls.put("usecase-name", test.getUsecaseName());
			testDtls.put("threads", test.getThreads());
			testDtls.put("dir", test.getDir());
			testDtls.put("duration", test.getDuration());
			testDtls.put("iterations", test.getIterations());
			testDtls.put("default-oats-server", test.getDefaultOatsSrvr());
			testDtls.put("baseline-tps", test.getBaselineTps());
			testDtls.put("baseline-flrs", test.getBaselineFlrs());
			testDtls.put("baseline-flr-perc", test.getBaselineFlrPerc());
			testDtls.put("scriptProps", CommonUtils.toJSONArray(test.getScriptProps()));
			testDtls.put("scnProps", CommonUtils.toJSONArray(test.getScnProps()));
			testDtls.put("session-name", testSession);

			JSONObject testName = new JSONObject();
			testName.put("test", testDtls);

			JSONArray tests = (JSONArray) session.get("tests");

			if (tests == null || tests.isEmpty()) {
				tests = new JSONArray();

				tests.add(0, testDtls);
			} else {
				tests.add(tests.size(), testDtls);
			}

			session.put("tests", tests);

			if (logger.isDebugEnabled()) {
				logger.debug("sessions --- : {}", root.toJSONString());
			}

			FileUtils.writeStringToFile(sessFile, root.toJSONString(), UTF8, false);

			logger.info("sessions : {}", sessions.toJSONString());
		} catch (IOException | ParseException e) {
			logger.error(e.getMessage(), e);
			throw new AutoRuntimeException(e.getMessage(), e);
		}

		return true;
	}

	public static String createTestSession(String testName) {

		Date date = new Date();
		String strDate = CommonUtils.toStringDate(date, TEST_SESSION_TIME_FORMAT);
		String testSession = strDate + testName;

		return testSession;
	}

	@SuppressWarnings("unchecked")
	public static List<Test> getTests(String uuid) {

		final List<Test> tests;

		logger.debug("=================== reading sessions file : {}", sessFile.getAbsolutePath());
		JSONObject root;

		try {
			root = (JSONObject) new JSONParser().parse(new FileReader(sessFile));
			JSONObject sessions = (JSONObject) root.get("sessions");

			JSONObject session = (JSONObject) sessions.get(uuid);

			if (session != null && session.get("tests") instanceof JSONArray) {

				JSONArray jsonTests = (JSONArray) session.get("tests");

				logger.debug("jsonTests is not empty, size is : {}", jsonTests.size());
				tests = new ArrayList<Test>(jsonTests.size());

				jsonTests.stream().forEach(jsonTest -> {

					JSONObject jsonObject = (JSONObject) jsonTest;

					Test test = new Test();
					test.setBaselineFlrPerc(CommonUtils.parseDoubleOrDefault(jsonObject.get("baseline-flr-perc"), 0.0));
					test.setBaselineFlrs(CommonUtils.parseIntOrDefault(jsonObject.get("baseline-flrs"), 0));

					test.setBaselineTps(CommonUtils.parseDoubleOrDefault(jsonObject.get("baseline-tps"), 0.0));
					test.setDefaultOatsSrvr(String.valueOf(jsonObject.get("default-oats-server")));

					test.setDir(String.valueOf(jsonObject.get("dir")));
					test.setDuration(CommonUtils.parseIntOrDefault(jsonObject.get("duration"), 0));

					test.setIterations(CommonUtils.parseIntOrDefault(jsonObject.get("iterations"), 0));
					test.setName(String.valueOf(jsonObject.get("name")));

					if (jsonObject.get("scnProps") != null) {
						test.setScnProps(CommonUtils.convertToMap((JSONArray) jsonObject.get("scnProps")));
					}

					if (jsonObject.get("scriptProps") != null) {
						test.setScriptProps(CommonUtils.convertToMap((JSONArray) jsonObject.get("scriptProps")));
					}

					test.setSessionName(String.valueOf(jsonObject.get("session-name")));
					test.setThreads(CommonUtils.parseIntOrDefault(jsonObject.get("threads"), 0));
					test.setUsecaseName(String.valueOf(jsonObject.get("usecase-name")));

					tests.add(test);
				});
			} else {
				logger.error("jsonTests is empty.");
				tests = null;
			}
		} catch (IOException | ParseException e) {
			logger.error(e);
			throw new AutoRuntimeException(e.getMessage(), e);
		}

		return tests;
	}

}