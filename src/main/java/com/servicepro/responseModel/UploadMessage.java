package com.servicepro.responseModel;

public class UploadMessage {

	private String imagePath;
	private int code;
	
	
	
	public UploadMessage() {
	
	}



	public UploadMessage(String imagePath, int code) {
		super();
		this.imagePath = imagePath;
		this.code = code;
	}



	public String getImagePath() {
		return imagePath;
	}



	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}



	public int getCode() {
		return code;
	}



	public void setCode(int code) {
		this.code = code;
	}

	
	
}
