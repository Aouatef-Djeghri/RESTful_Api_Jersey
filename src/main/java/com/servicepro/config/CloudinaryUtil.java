package com.servicepro.config;



import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

public class CloudinaryUtil {

	
	public static final Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
			"cloud_name", "",
			"api_key", "",
			"api_secret", ""));
	
	
	public static void deleteImage(String imageURL, String dir ) {
		try {
			String[] parts = imageURL.split("/");
			String fullName = parts[parts.length - 1];
			String name = fullName.split("\\.", -1)[0];
			cloudinary.uploader().destroy(dir+"/"+name,ObjectUtils.asMap("resource_type","image","type","upload"));
		} catch (Exception e1) {
		 	//catch exception
		}
	}
	
}
