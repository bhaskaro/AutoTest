/**
 * 
 */
package com.oggu.auto.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.logging.log4j.Logger;

import com.oggu.auto.core.common.CommonConstants;
import com.oggu.auto.core.common.CommonUtils;
import com.oggu.auto.core.config.ConfigReader;
import com.oggu.auto.core.excep.AutoRuntimeException;
import com.oggu.auto.core.exec.TestExecutor;
import com.oggu.auto.core.model.RunTests;
import com.oggu.auto.core.model.Test;
import com.oggu.auto.core.sess.SessionUtil;

/**
 * @author bhaskaro
 *
 */
public class TestRunner implements CommonConstants {

	private static Logger logger = CommonUtils.getLogger(TestRunner.class);

	private static boolean isCombo = false;

	/**
	 * @param args
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	public static void main(String[] args) throws InterruptedException, ExecutionException {

		String uuid = CommonConstants.RANDOM_UUID;
		runTests(uuid);
	}

	public static void runTests(String uuid) throws AutoRuntimeException {

		logger.info("Running Tests for UUID : {}", uuid);
		System.out.println(CommonUtils.getBanner("Running Tests.", 10));
		System.out.println(CommonUtils.getBanner("----------------", 15));

		Map<String, Test> configuredTests = ConfigReader.getTests().stream()
				.collect(Collectors.toMap(t -> t.getName(), t -> t));

		logger.debug("All Configured Tests : {}", configuredTests);

		RunTests runTests = ConfigReader.getRunTests();
		String[] tests = runTests.getTestNames();

		try {
			if (tests != null && tests.length > 0) {

				Stream.of(tests).forEach(test -> logger.debug("Configured Test : {}", test));

				isCombo = tests.length > 1 && !runTests.isSequential();

				logger.debug("isCombo is : {}, configuring global tests-duration for all tests : {}", isCombo,
						runTests.getTestsDuration());

				// run tests concurrently
				if (isCombo) {
					ExecutorService executor = Executors.newFixedThreadPool(tests.length);
					List<Future<String>> futures = new ArrayList<>();

					Stream.of(tests).forEach(testName -> {
						Test test = configuredTests.get(testName);
						test.setDuration(runTests.getTestsDuration());
						String testSessName = SessionUtil.createTestSession(test.getName());

						futures.add(executor.submit(new TestExecutor(uuid, testSessName, test)));
						try {
							Thread.sleep(EXCTR_THREAD_DELAY_SECS * 1000);
						} catch (InterruptedException e) {
							logger.error(e);
							throw new AutoRuntimeException(e);
						}
					});

					executor.shutdown();

					// sleep for combo duration (global duration)
					Thread.sleep(runTests.getTestsDuration() * 1000);

					while (!executor.isShutdown()) {
						logger.debug("TestExecutor Executor is still running, sleeping for a min");
						Thread.sleep(1000 * 10);
					}

					futures.stream().forEach(f -> {
						try {
							logger.info("Got ouput from COMBO tests : {}", f.get());
						} catch (InterruptedException | ExecutionException e) {
							logger.error(e);
							throw new AutoRuntimeException(e);
						}
					});

					logger.debug("Ran all configured tests");

				} else {// run sequentially

					Stream.of(tests).forEach(testName -> {

						Test test = configuredTests.get(testName);
						String testSessName = SessionUtil.createTestSession(test.getName());
						FutureTask<String> ft = new FutureTask<>(new TestExecutor(uuid, testSessName, test));
						new Thread(ft).start();

						// sleep for test duration (test duration)
						try {
							Thread.sleep(test.getDuration() * 1000);
							while (!ft.isDone()) {
								logger.debug("Executor is still running, sleeping for 30 secs");
								Thread.sleep(1000 * 10);
							}
							logger.info("got ouput from the task : {}", ft.get());
						} catch (InterruptedException | ExecutionException e) {
							logger.error(e);
							throw new AutoRuntimeException(e);
						}
					});

					logger.info("Ran the configured test.");
				}

			}
		} catch (Exception e) {
			logger.error(e);
			throw new AutoRuntimeException(e);
		}
	}

}
