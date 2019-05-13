/**
 * 
 */
package com.oggu.auto.core.exec;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.logging.log4j.Logger;

import com.oggu.auto.core.common.CommonConstants;
import com.oggu.auto.core.common.CommonUtils;
import com.oggu.auto.core.config.ConfigReader;
import com.oggu.auto.core.excep.AutoRuntimeException;
import com.oggu.auto.core.exec.thrds.TestPreparorWorker;
import com.oggu.auto.core.model.RunTests;
import com.oggu.auto.core.model.Test;

/**
 * @author bhaskaro
 *
 */
public class PrepareTests implements CommonConstants {

	private static Logger logger = CommonUtils.getLogger(PrepareTests.class);

	/**
	 * @param args
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	public static void main(String[] args) throws InterruptedException, ExecutionException {

		String uuid = CommonConstants.RANDOM_UUID;
		prepareTests(uuid);
	}

	public static void prepareTests(String uuid) throws AutoRuntimeException {

		logger.info("Preparing Tests for UUID : {}", uuid);
		System.out.println(CommonUtils.getBanner("Preparing Tests.", 10));
		System.out.println(CommonUtils.getBanner("----------------", 15));

		Map<String, Test> configuredTests = ConfigReader.getTests().stream()
				.collect(Collectors.toMap(t -> t.getName(), t -> t));

		logger.debug("All Configured Tests : {}", configuredTests);

		RunTests runTests = ConfigReader.getRunTests();
		String[] tests = runTests.getTestNames();

		File newRandDir = new File(TEST_EXECUTION_DIR, uuid);
		newRandDir.mkdirs();

		logger.info("New Output folder for this test run :" + newRandDir.getAbsolutePath());

		if (tests != null && tests.length > 0) {
			Stream.of(runTests.getTestNames()).forEach(s -> logger.debug("Configured Test : " + s));

			ExecutorService executor = Executors.newFixedThreadPool(tests.length);
			List<Future<String>> futures = new ArrayList<>();

			for (String testName : tests) {
				Test test = configuredTests.get(testName);
				Future<String> future = executor.submit(new TestPreparorWorker(uuid, test));
				futures.add(future);
			}

			executor.shutdown();

			try {
				while (!executor.isShutdown()) {
					logger.debug("TestPreparor Executor is still running, sleeping for a min");
					Thread.sleep(1000 * 1);
				}
				futures.stream().forEach(f -> {
					try {
						logger.info("Got ouput from TestPreparor : " + f.get());
					} catch (InterruptedException | ExecutionException e) {
						throw new AutoRuntimeException(e.getMessage(), e);
					}
				});
			} catch (Exception e) {
				logger.error(e);
				throw new AutoRuntimeException(e.getMessage(), e);
			}

			logger.debug("Prepared all configured tests");
		}
	}
}
