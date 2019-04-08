package com.imagetracker.bean;

import java.io.Serializable;

public class File implements Serializable {

	private static final long serialVersionUID = 1L;

	private String url;

	private String name;

	private String labels;

	private double size;

	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getLabels() {
		return labels;
	}

	public void setLabels(String labels) {
		this.labels = labels;
	}

	public double getSize() {
		return size;
	}

	public void setSize(double size) {
		this.size = size;
	}

}