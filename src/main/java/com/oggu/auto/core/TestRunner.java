/**
 * 
 */
package com.oggu.auto.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
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

		runTests();
	}

	public static void runTests() throws AutoRuntimeException {

		Map<String, Test> configuredTests = ConfigReader.getTests().stream()
				.collect(Collectors.toMap(t -> t.getName(), t -> t));

		logger.debug("All Configured Tests : " + configuredTests);

		RunTests runTests = ConfigReader.getRunTests();
		String[] tests = runTests.getTestNames();

		try {
			if (tests != null && tests.length > 0) {

				Stream.of(runTests.getTestNames()).forEach(s -> logger.debug("Configured Test : {}", s));

				if (tests.length > 1 && !runTests.isSequential()) {
					isCombo = true;
				}

				logger.debug("isCombo is : " + isCombo + ", configuring global tests-duration for all tests : {}",
						runTests.getTestsDuration());

				// run tests concurrently
				if (isCombo) {
					ExecutorService executor = Executors.newFixedThreadPool(tests.length);
					List<Future<String>> futures = new ArrayList<>();

					for (String test : tests) {
						Test testConfig = configuredTests.get(test);
						testConfig.setDuration(runTests.getTestsDuration());
						futures.add(executor.submit(new TestExecutor(testConfig)));
						Thread.sleep(EXCTR_THREAD_DELAY_SECS * 1000);
					}

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
							throw new AutoRuntimeException(e);
						}
					});

					logger.debug("Ran all configured tests");

				} else {// run sequentially

					for (String test : tests) {
						Callable<String> te = new TestExecutor(configuredTests.get(test));

						FutureTask<String> ft = new FutureTask<>(te);
						new Thread(ft).start();

						// sleep for test duration (test duration)
						Thread.sleep(configuredTests.get(test).getDuration() * 1000);

						while (!ft.isDone()) {
							logger.debug("Executor is still running, sleeping for 30 secs");
							Thread.sleep(1000 * 10);
						}

						logger.info("got ouput from the task : {}", ft.get());
					}

					logger.debug("Ran the configured test.");
				}

			}
		} catch (Exception e) {
			throw new AutoRuntimeException(e);
		}
	}

}
