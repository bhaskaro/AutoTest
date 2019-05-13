package com.oggu.auto.core.exec.thrds;

import java.io.File;
import java.io.FilenameFilter;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.oggu.auto.core.common.CommonConstants;
import com.oggu.auto.core.model.Test;
import com.oggu.auto.core.util.ZipUtil;

public class TestPreparorWorker implements CommonConstants, Callable<String> {

	String uuid = null;
	private Test test = null;

	public TestPreparorWorker(String uuid, Test test) {
		this.uuid = uuid;
		this.test = test;
	}

	@Override
	public String call() throws Exception {

		// Get logger and add appender
		Logger logger = LogManager.getLogger(test.getName());

		logger.info("Preparting test ({}) : found oats server : {} with concurrency : {} and duration : {}",
				test.getName(), test.getDefaultOatsSrvr(), test.getThreads(), test.getDuration());

		File srcDir = new File(TEST_SOURCE_DIR, test.getDir());
		File destDir = new File(new File(TEST_EXECUTION_DIR, uuid), test.getName());
		FileUtils.copyDirectory(srcDir, destDir);

		File[] javaFiles = destDir.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.toLowerCase().endsWith(".java");
			}
		});

		// replace script properties
		Map<String, String> scriptProps = test.getScriptProps();

		if (MapUtils.isNotEmpty(scriptProps)) {

			for (File file : javaFiles) {

				String content = FileUtils.readFileToString(file, UTF8);

				for (String key : scriptProps.keySet()) {

					if (StringUtils.contains(content, key)) {
						content = Pattern.compile(key).matcher(content).replaceAll(scriptProps.get(key));
					}
				}
				FileUtils.writeStringToFile(file, content, UTF8);
			}
		}

		List<String> listOfJavaFiles = Stream.of(javaFiles).map(s -> s.getAbsolutePath()).collect(Collectors.toList());

		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		int compilationResult = compiler.run(null, null, null,
				listOfJavaFiles.toArray(new String[listOfJavaFiles.size()]));

		if (compilationResult == 0) {
			logger.info("Compilation is successful : {}", listOfJavaFiles);
		} else {
			logger.error("Compilation Failed : {}", listOfJavaFiles);
		}

		ZipUtil.compressDir(destDir.getPath(), test.getName() + ".zip");

		// TODO remove later
		// Thread.sleep(1 * 1000);
		return "Test (" + test.getName() + ") Prepared successfully.";
	}

}
