/**
 * 
 */
package com.oggu.auto.core.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.oggu.auto.core.common.CommonConstants;
import com.oggu.auto.core.common.CommonUtils;
import com.oggu.auto.core.excep.AutoRuntimeException;
import com.oggu.auto.core.model.OatsServer;
import com.oggu.auto.core.model.RunTests;
import com.oggu.auto.core.model.Test;

/**
 * @author bhaskaro
 *
 */
public class ConfigReader implements CommonConstants {

	private static final Logger logger = CommonUtils.getLogger(ConfigReader.class);

	private static RunTests runTests = null;
	private static List<Test> tests = null;
	private static List<OatsServer> oatsServers = null;

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		System.out.println(getOatsServers());
		System.out.println(getTests());
		System.out.println(getRunTests());
	}

	public static RunTests getRunTests() {

		if (runTests == null) {

			runTests = new RunTests();

			JSONParser jsonParser = new JSONParser();
			try {
				JSONObject jsonObject = (JSONObject) jsonParser
						.parse(CommonUtils.getCPResourceAsStream(RUNTESTS_CONFIG_FILE));

				String[] tests = CommonUtils.safeSplit(String.valueOf(jsonObject.get("test-names")), COMMA_STRING);

				runTests.setTestNames(tests);
				runTests.setSequential(CommonUtils.parseBooleanOrDefault(jsonObject.get("sequential"), false));
				runTests.setTestsDuration(CommonUtils.parseIntOrDefault(jsonObject.get("tests-duration"), 0));
				runTests.setTestProperties(String.valueOf(jsonObject.get("test-properties")));
				runTests.setBaselineTps(CommonUtils.parseDoubleOrDefault(jsonObject.get("baseline-tps"), 0));
				runTests.setBaselineFlrs(CommonUtils.parseIntOrDefault(jsonObject.get("baseline-flrs"), 0));
				runTests.setBaselineFlrPerc(CommonUtils.parseDoubleOrDefault(jsonObject.get("baseline-flr-perc"), 0));

			} catch (IOException | ParseException e) {
				logger.error(e);
				throw new AutoRuntimeException(e.getMessage(), e);
			}
		}

		return runTests;
	}

	@SuppressWarnings("unchecked")
	public static List<Test> getTests() {

		if (tests == null) {
			tests = new ArrayList<>();

			JSONParser jsonParser = new JSONParser();
			try {
				JSONObject jsonObject = (JSONObject) jsonParser
						.parse(CommonUtils.getCPResourceAsStream(TESTS_CONFIG_FILE));

				JSONArray array = (JSONArray) jsonObject.get("tests");

				array.forEach(a -> {
					JSONObject obj = (JSONObject) a;
					Test test = new Test();
					test.setName(String.valueOf(obj.get("name")));
					test.setDir(String.valueOf(obj.get("dir")));
					test.setUsecaseName(String.valueOf(obj.get("usecase-name")));
					test.setThreads(CommonUtils.parseIntOrDefault(obj.get("threads"), 0));
					test.setIterations(CommonUtils.parseIntOrDefault(obj.get("iterations"), 0));
					test.setDuration(CommonUtils.parseIntOrDefault(obj.get("duration"), 0));
					test.setScnProps(CommonUtils.convertToMap((JSONArray) obj.get("scnProps")));
					test.setScriptProps(CommonUtils.convertToMap((JSONArray) obj.get("scriptProps")));
					test.setBaselineTps(CommonUtils.parseDoubleOrDefault(obj.get("baseline-tps"), 0));
					test.setBaselineFlrs(CommonUtils.parseIntOrDefault(obj.get("baseline-flrs"), 0));
					test.setBaselineFlrPerc(CommonUtils.parseDoubleOrDefault(obj.get("baseline-flr-perc"), 0));
					test.setDefaultOatsSrvr(String.valueOf(obj.get("default-oats-server")));

					tests.add(test);
				});
			} catch (IOException | ParseException e) {
				logger.error(e);
				throw new AutoRuntimeException(e.getMessage(), e);
			}
		}

		return tests;
	}

	@SuppressWarnings("unchecked")
	public static List<OatsServer> getOatsServers() {

		if (oatsServers == null) {
			oatsServers = new ArrayList<>();

			JSONParser jsonParser = new JSONParser();
			try {
				JSONObject jsonObject = (JSONObject) jsonParser
						.parse(CommonUtils.getCPResourceAsStream(OATS_CONFIG_FILE));

				JSONArray array = (JSONArray) jsonObject.get("oats-servers");

				array.forEach(a -> {
					JSONObject obj = (JSONObject) a;
					OatsServer oatsServer = new OatsServer();
					oatsServer.setName(String.valueOf(obj.get("name")));
					oatsServer.setHost(String.valueOf(obj.get("host")));
					oatsServer.setHomedir(String.valueOf(obj.get("homedir")));
					oatsServer.setAdminUser(String.valueOf(obj.get("admin-user")));
					oatsServer.setAdminPwd(String.valueOf(obj.get("admin-pwd")));
					oatsServer.setVmUser(String.valueOf(obj.get("vm-user")));
					oatsServer.setVmPwd(String.valueOf(obj.get("vm-pwd")));

					oatsServers.add(oatsServer);
				});
			} catch (IOException | ParseException e) {
				logger.error(e);
				throw new AutoRuntimeException(e.getMessage(), e);
			}
		}
		return oatsServers;
	}

}
