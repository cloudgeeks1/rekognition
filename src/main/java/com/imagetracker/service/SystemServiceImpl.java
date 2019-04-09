package com.imagetracker.service;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.AmazonRekognitionClientBuilder;
import com.amazonaws.services.rekognition.model.AmazonRekognitionException;
import com.amazonaws.services.rekognition.model.DetectLabelsRequest;
import com.amazonaws.services.rekognition.model.DetectLabelsResult;
import com.amazonaws.services.rekognition.model.Image;
import com.amazonaws.services.rekognition.model.Label;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.imagetracker.dto.ConnectionDao;
import com.imagetracker.util.FileStorageProperties;


@Service
public class SystemServiceImpl implements SystemService {

	@Autowired
	FileStorageProperties fileStorageProperties;

	private Path fileStorageLocation;

	DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	private URL url;

	private PutObjectResult putObject;
	@PostConstruct
	public void init() {
		this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir()).toAbsolutePath().normalize();

		try {
			Files.createDirectories(this.fileStorageLocation);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	

	public void uploadFileToS3(MultipartFile file,String userName,int userId,ConnectionDao dao) {
		// CommonService commonService = new CommonService();
		// credentials object identifying user for authentication
		// user must have AWSConnector and AmazonS3FullAccess for
		// this example to work
		AmazonS3 s3client = gets3ClientObject();

		// create bucket - name must be unique for all S3 users
		if (!s3client.doesBucketExistV2(CommonConstants.BUCKET_NAME))
			s3client.createBucket(CommonConstants.BUCKET_NAME);

		// upload file to folder and set it to public
		String filePath = CommonConstants.FOLDER_NAME + CommonConstants.SUFFIX + file.getOriginalFilename();
		try {
//			s3client.putObject(CommonConstants.BUCKET_NAME, filePath, file.getInputStream(), new ObjectMetadata());
			putObject = s3client.putObject(
					   new PutObjectRequest(CommonConstants.BUCKET_NAME, filePath, file.getInputStream(), new ObjectMetadata())
					      .withCannedAcl(CannedAccessControlList.PublicRead));
			
			String objectURL = getObjectURL(file.getOriginalFilename());
			String imageLables = rekognitionImage(file.getOriginalFilename());
			
			dao.insertUploadedImageInfo(dao.RetriveConnection(), userId, userName, file.getOriginalFilename(), objectURL, imageLables);
			
			
			
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

	private AmazonS3 gets3ClientObject() {
		AWSCredentials credentials = new BasicAWSCredentials(CommonConstants.ACCESS_KEY_ID,
				CommonConstants.ACCESS_SEC_KEY);

		// create a client connection based on credentials
		// AmazonS3 s3client = new AmazonS3Client(credentials);

		AmazonS3 s3client = AmazonS3ClientBuilder.standard()
				.withCredentials(new AWSStaticCredentialsProvider(credentials)).withRegion(Regions.US_EAST_2).build();
		return s3client;
	}
	
	public String getObjectURL(String fileName) {
		AmazonS3 s3client = gets3ClientObject();
		String bucketName = CommonConstants.BUCKET_NAME;

		try {
			ListObjectsRequest listObjectsRequest =new ListObjectsRequest() .withBucketName(bucketName).withPrefix("upload"+ "/"+fileName);
			ObjectListing objects = s3client.listObjects(listObjectsRequest);
			List<S3ObjectSummary> list = objects.getObjectSummaries();
			for(S3ObjectSummary image: list) {
			    S3Object s3object = s3client.getObject(bucketName, image.getKey());
			    url = s3client.getUrl(bucketName, s3object.getKey());
			    System.out.println("url : "+url);
			    return url.toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
	
	public String rekognitionImage(String fileName) {
		String lableList = "";
		String filePath = CommonConstants.FOLDER_NAME + CommonConstants.SUFFIX + fileName;
		 S3Object s3object = gets3ClientObject().getObject(CommonConstants.BUCKET_NAME, filePath);
		  AWSCredentials credentials = new BasicAWSCredentials(CommonConstants.ACCESS_KEY_ID,CommonConstants.ACCESS_SEC_KEY);
	      AmazonRekognition rekognitionClient = AmazonRekognitionClientBuilder.standard().withRegion(Regions.US_EAST_2).withCredentials(new AWSStaticCredentialsProvider(credentials)).build();
        DetectLabelsRequest request = new DetectLabelsRequest()
                .withImage(new Image().withS3Object(new com.amazonaws.services.rekognition.model.S3Object().withName(s3object.getKey()).withBucket( CommonConstants.BUCKET_NAME)))
                .withMaxLabels(10).withMinConfidence(95F);
        try {
            DetectLabelsResult result = rekognitionClient.detectLabels(request);
            List<Label> labels = result.getLabels();
            for (Label label : labels) {
				lableList+=label.getName()+",";
			}
            return lableList;
        }catch (AmazonRekognitionException e) {
            e.printStackTrace();
        } 
        return "";
    }

	@Override
	public void removeDuplicateLables(Set<String> lables, String[] split) {
		// TODO Auto-generated method stub
		for (String label : split) {
			lables.add(label);
		}
		
	}
}
