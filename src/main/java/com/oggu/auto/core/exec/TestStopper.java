/**
 * 
 */
package com.oggu.auto.core.exec;

import java.util.List;

import org.apache.logging.log4j.Logger;

import com.oggu.auto.core.common.CommonConstants;
import com.oggu.auto.core.common.CommonUtils;
import com.oggu.auto.core.model.Test;
import com.oggu.auto.core.sess.SessionUtil;

/**
 * @author Bhaskar
 *
 */
public class TestStopper {

	private static Logger logger = CommonUtils.getLogger(TestStopper.class);

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		String uuid = CommonConstants.RANDOM_UUID;

		// TODO
		uuid = "78e101dc-c440-4b02-a06a-e58eb73fe324";

		stopTests(uuid);
	}

	private static void stopTests(String uuid) {

		logger.info("Stopping Tests for UUID : {}", uuid);
		System.out.println(CommonUtils.getBanner("Stopping Tests.", 10));
		System.out.println(CommonUtils.getBanner("----------------", 15));

		List<Test> tests = SessionUtil.getTests(uuid);

		if (tests != null && !tests.isEmpty()) {

			tests.stream().forEach(s -> logger.debug("Stopping Test Name : {}, with Test Session : {}", s.getName(),
					s.getSessionName()));

//			Map<String, Test> recentlyRanTests = ConfigReader.getTests().stream()
//					.collect(Collectors.toMap(t -> t.getName(), t -> t));
		}

	}

}
