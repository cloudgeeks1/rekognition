package com.imagetracker.service;

import java.io.File;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.S3ObjectSummary;

public class CommonService {

	public void getObj(AmazonS3 s3client) {
		String bucketName = CommonConstants.BUCKET_NAME;
		String objectName = CommonConstants.BUCKET_FILE_PATH;

		try {
			S3Object s3object = s3client.getObject(bucketName, objectName);
			S3ObjectInputStream inputStream = s3object.getObjectContent();
			FileUtils.copyInputStreamToFile(inputStream, new File(CommonConstants.LOCAL_DOWNLOAD_PATH));

			System.out.println("file copied to destination.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method first deletes all the files in given folder and than the folder
	 * itself
	 */

	public static void deleteFolder(String bucketName, String folderName, AmazonS3 client) {
		List<S3ObjectSummary> fileList = client.listObjects(bucketName, folderName).getObjectSummaries();
		for (Object object : fileList) {
			S3ObjectSummary file = (S3ObjectSummary) object;
			client.deleteObject(bucketName, file.getKey());
		}
		client.deleteObject(bucketName, folderName);
		System.out.println("Folder Deleted");
	}

}
