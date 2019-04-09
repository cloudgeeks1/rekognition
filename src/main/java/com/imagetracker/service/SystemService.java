package com.imagetracker.service;

import java.util.Set;

import org.springframework.web.multipart.MultipartFile;

import com.imagetracker.dto.ConnectionDao;

public interface SystemService {

	public void uploadFileToS3(MultipartFile file,String userName,int userId,ConnectionDao dao);

	public String getObjectURL(String fileName);
	
	public String rekognitionImage(String fileName);

	public void removeDuplicateLables(Set<String> lables, String[] split);
}
