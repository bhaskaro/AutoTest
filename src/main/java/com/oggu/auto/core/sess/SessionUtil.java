/**
 * 
 */
package com.oggu.auto.core.sess;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.Logger;
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
				throw new AutoRuntimeException(e);
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
	public static boolean saveSessionDetails(String randomUuid, String testSession, Test test) {

		try {
			JSONObject root = (JSONObject) new JSONParser().parse(new FileReader(sessFile));

			JSONObject sessions = (JSONObject) root.get("sessions");

			JSONObject session = (JSONObject) sessions.get(randomUuid);

			if (session == null) {
				logger.info("sess doesnt exist.");

				session = new JSONObject();

				session.put("session-id", randomUuid);

				sessions.put(randomUuid, session);
			} else {
				logger.info("sess exist : {}", session);
			}

			JSONObject details = new JSONObject();
			details.put("name", test.getName());
			details.put("usecase-name", test.getUsecaseName());
			details.put("threads", test.getThreads());
			details.put("dir", test.getDir());
			details.put("duration", test.getDuration());
			details.put("iterations", test.getIterations());
			details.put("default-oats-server", test.getDefaultOatsSrvr());
			details.put("baseline-tps", test.getBaselineTps());
			details.put("baseline-flrs", test.getBaselineFlrs());
			details.put("baseline-flr-perc", test.getBaselineFlrPerc());
			details.put("scriptProps", test.getScriptProps());
			details.put("scnProps", test.getScnProps());

			JSONObject testName = new JSONObject();
			testName.put("details", details);

			session.put(testSession, testName);

			if (logger.isDebugEnabled()) {
				logger.debug("sessions --- : {}", root);
			}

			FileUtils.writeStringToFile(sessFile, root.toJSONString(), UTF8, false);

			// sessions.writeJSONString(new FileWriter(sessFile));
			// sessions.writeJSONString(new PrintWriter(System.out));

			logger.info("sessions : {}", sessions.toJSONString());
		} catch (IOException | ParseException e) {
			logger.error(e);
			throw new AutoRuntimeException(e);
		}

		return true;
	}

	public static String createTestSession(String testName) {

		Date date = new Date();
		String strDate = CommonUtils.toStringDate(date, TEST_SESSION_TIME_FORMAT);
		String testSession = strDate + testName;

		return testSession;
	}

}
