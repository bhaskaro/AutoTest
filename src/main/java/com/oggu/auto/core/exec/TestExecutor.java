package com.oggu.auto.core.exec;

import java.util.concurrent.Callable;

import org.apache.logging.log4j.Logger;

import com.oggu.auto.core.common.CommonConstants;
import com.oggu.auto.core.common.CommonUtils;
import com.oggu.auto.core.model.Test;
import com.oggu.auto.core.sess.SessionUtil;

public class TestExecutor implements CommonConstants, Callable<String> {

	String uuid = null;
	String testSessName = null;
	private Test test = null;

	public TestExecutor(String uuid, String testSessName, Test test) {
		this.uuid = uuid;
		this.testSessName = testSessName;
		this.test = test;
	}

	@Override
	public String call() throws Exception {

		Logger logger = CommonUtils.getLogger(test.getName());

		logger.info("running test (" + test.getName() + ") : with session : " + testSessName + " on "
				+ test.getDefaultOatsSrvr() + " with concurrency : " + test.getThreads() + " and duration : "
				+ test.getDuration());

		boolean status = SessionUtil.saveSessionDetails(uuid, testSessName, test);

		if (logger.isDebugEnabled()) {
			logger.debug("Saved Session : {}", status);
		}

		// TODO remove later
		Thread.sleep(3 * 1000);
		return "Test (" + test.getName() + ") ran successfully.";
	}

}
