package com.oggu.auto.core.util;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Collection;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.oggu.auto.core.common.CommonConstants;
import com.oggu.auto.core.excep.AutoRuntimeException;

public class ZipUtil implements CommonConstants {

	private static final Logger logger = LogManager.getLogger(ZipUtil.class);

	public static void main(String[] args) throws Exception {

		String dir = "OUTPUT";
		String zipFile = "test.zip";

		compressDir(dir, zipFile);
	}

	public static void compressDir(String srcDir, String targetZip) {

		IOFileFilter fileFilter = TrueFileFilter.INSTANCE;

		Collection<File> files = FileUtils.listFilesAndDirs(new File(srcDir), fileFilter, fileFilter);

		files.stream().forEach(f -> logger.debug("Compressing file/folder : " + f.getAbsolutePath()));

		try (ZipOutputStream zipout = new ZipOutputStream(new FileOutputStream(new File(srcDir, targetZip)))) {
			for (File file : files) {

				if (!file.isDirectory()) {
					zipout.putNextEntry(new ZipEntry(file.getPath().replaceAll(srcDir, EMPTY_STRING)));
					zipout.write(FileUtils.readFileToByteArray(file));
					zipout.closeEntry();
				}
			}
			logger.info("successfully created zip file : " + targetZip);
		} catch (Exception e) {
			throw new AutoRuntimeException(e);
		}

	}

}
