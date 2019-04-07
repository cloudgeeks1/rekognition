package com.imagetracker.service;

import org.springframework.web.multipart.MultipartFile;

import com.imagetracker.dto.ResultDTO;

public interface SystemService {

	public void addFileToSharedFolder(MultipartFile file);

	public void getObjDetails();
}
