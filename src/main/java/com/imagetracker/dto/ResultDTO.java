package com.imagetracker.dto;

public class ResultDTO {

	public int code;
	public String message;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public ResultDTO() {
		super();
	}

	public ResultDTO(int code, String message) {
		super();
		this.code = code;
		this.message = message;
	}

}