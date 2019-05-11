/**
 * 
 */
package com.oggu.auto.core.config;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * @author bhaskaro
 *
 */
public class TestConfigWriter {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		writeOatsConfig();
		writeTestsConfig();
		writeRunTestsConfig();
	}

	@SuppressWarnings("unchecked")
	private static void writeRunTestsConfig() {
		JSONObject runtests = new JSONObject();

		runtests.put("test-names", "getusers1,getusers2,getusers3");
		runtests.put("sequential", true);
		runtests.put("tests-duration", 3600);
		runtests.put("test-properties", "TENANT-PRX=testtent,BEGIN-TENANT=1");
		runtests.put("baseline-tps", 100.56);
		runtests.put("baseline-flrs", 10);
		runtests.put("baseline-flr-perc", 0.5);

		System.out.println(runtests.toJSONString());
	}

	@SuppressWarnings("unchecked")
	private static void writeTestsConfig() {
		JSONObject tests = new JSONObject();

		JSONArray jsonArray = new JSONArray();

		for (int i = 1; i <= 2; i++) {
			JSONObject jsonObject = new JSONObject();

			jsonObject.put("name", "getusers" + i);
			jsonObject.put("dir", "getusers");
			jsonObject.put("usecase-name", "GET-USERS-" + i);
			jsonObject.put("threads", i * 10);
			jsonObject.put("duration", i * 300);
			jsonObject.put("iterations", i * 10);
			jsonObject.put("baseline-tps", 45.5);
			jsonObject.put("baseline-flrs", i);
			jsonObject.put("baseline-flr-perc", (double) i / 10);
			jsonObject.put("scnProps", "ITERATION-DELAY=" + (i * 2) + ",THINK-TIME=" + i + 10);
			jsonObject.put("scriptProps", "TENANTPFX=TESTTEN" + i + ",BEGIN-TENANT=" + i + 10);
			jsonObject.put("default-oats-server", "default");

			jsonArray.add(jsonObject);
		}

		tests.put("tests", jsonArray);

		System.out.println(tests.toJSONString());
	}

	@SuppressWarnings("unchecked")
	private static void writeOatsConfig() {
		JSONObject oatsServers = new JSONObject();

		JSONArray jsonArray = new JSONArray();

		for (int i = 1; i <= 2; i++) {
			JSONObject jsonObject = new JSONObject();

			jsonObject.put("name", "server" + i);
			jsonObject.put("host", "host" + i);
			jsonObject.put("homedir", "/scratch/dir" + i);
			jsonObject.put("admin-user", "administrator");
			jsonObject.put("admin-pwd", "welcome" + i);
			jsonObject.put("vm-user", "testuser");
			jsonObject.put("vm-pwd", "Passwd" + i);

			jsonArray.add(jsonObject);
		}

		oatsServers.put("oats-servers", jsonArray);

		System.out.println(oatsServers.toJSONString());
	}

}
