package com.imagetracker.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.imagetracker.dto.ResultDTO;
import com.imagetracker.util.FileStorageProperties;

@Service
public class SystemServiceImpl implements SystemService {

	@Autowired
	FileStorageProperties fileStorageProperties;

	private Path fileStorageLocation;

	DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

	@PostConstruct
	public void init() {
		this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir()).toAbsolutePath().normalize();

		try {
			Files.createDirectories(this.fileStorageLocation);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public void addFileToSharedFolder(MultipartFile file) {
		uploadFileToS3(file);
	}

	public void uploadFileToS3(MultipartFile file) {
		// CommonService commonService = new CommonService();
		// credentials object identifying user for authentication
		// user must have AWSConnector and AmazonS3FullAccess for
		// this example to work
		AWSCredentials credentials = new BasicAWSCredentials(CommonConstants.ACCESS_KEY_ID,
				CommonConstants.ACCESS_SEC_KEY);

		// create a client connection based on credentials
		// AmazonS3 s3client = new AmazonS3Client(credentials);

		AmazonS3 s3client = AmazonS3ClientBuilder.standard()
				.withCredentials(new AWSStaticCredentialsProvider(credentials)).withRegion(Regions.US_EAST_2).build();

		// create bucket - name must be unique for all S3 users
		if (!s3client.doesBucketExistV2(CommonConstants.BUCKET_NAME))
			s3client.createBucket(CommonConstants.BUCKET_NAME);

		// upload file to folder and set it to public
		String filePath = CommonConstants.FOLDER_NAME + CommonConstants.SUFFIX + file.getOriginalFilename();
		try {
			s3client.putObject(CommonConstants.BUCKET_NAME, filePath, file.getInputStream(), new ObjectMetadata());
		} catch (SdkClientException | IOException e) {
			e.printStackTrace();
		}
		/*
		 * s3client.putObject( new PutObjectRequest(bucketName, fileName, new
		 * java.io.File(CommonConstants.FILE_NAME))
		 * .withCannedAcl(CannedAccessControlList.PublicRead));
		 */

		System.out.println("Execution Completed");
	}

}
