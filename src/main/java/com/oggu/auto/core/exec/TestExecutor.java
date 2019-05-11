package com.oggu.auto.core.exec;

import java.util.concurrent.Callable;

import org.apache.logging.log4j.Logger;

import com.oggu.auto.core.common.CommonConstants;
import com.oggu.auto.core.common.CommonUtils;
import com.oggu.auto.core.model.Test;
import com.oggu.auto.core.sess.SessionUtil;

public class TestExecutor implements CommonConstants, Callable<String> {

	private static final Logger logger = CommonUtils.getLogger(TestExecutor.class);
	private Test test = null;

	public TestExecutor(Test test) {
		this.test = test;
	}

	@Override
	public String call() throws Exception {

		String testSession = SessionUtil.createTestSession(test.getName());

		logger.info("running test (" + test.getName() + ") : with session : " + testSession + " on "
				+ test.getDefaultOatsSrvr() + " with concurrency : " + test.getThreads() + " and duration : "
				+ test.getDuration());

		boolean status = SessionUtil.saveSessionDetails(RANDOM_UUID, testSession, test);

		if (logger.isDebugEnabled()) {
			logger.debug("Saved Session : {}", status);
		}

		// TODO remove later
		Thread.sleep(3 * 1000);
		return "Test (" + test.getName() + ") ran successfully.";
	}

}
