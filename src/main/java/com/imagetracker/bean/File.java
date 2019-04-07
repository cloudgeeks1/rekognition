package com.imagetracker.bean;

import java.io.Serializable;

public class File implements Serializable {

	private static final long serialVersionUID = 1L;

	private String content;

	private String name;

	private String extension;

	private double size;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}

	public double getSize() {
		return size;
	}

	public void setSize(double size) {
		this.size = size;
	}

}