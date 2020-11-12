package com.servicepro.services;


import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Map;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.apache.commons.io.IOUtils;
import org.glassfish.jersey.media.multipart.FormDataParam;
import com.cloudinary.utils.ObjectUtils;
import com.google.gson.Gson;
import com.servicepro.config.CloudinaryUtil;
import com.servicepro.exception.DataNotFoundException;
import com.servicepro.exception.DatabaseException;
import com.servicepro.responseModel.ErrorMessage;
import com.servicepro.responseModel.UploadMessage;


/*
 * Aouatef Djeghri
 * Year 2020
 */


@Path("/upload")
public class UploadResources {

	ArrayList<String> imageUrls = new ArrayList<>();
	
	
	@POST
	@Path("/image/{dir}/{imageId}")
	@Consumes({ MediaType.MULTIPART_FORM_DATA })
	@Produces(MediaType.APPLICATION_JSON)
	public Response uploadImage(@FormDataParam("file") InputStream fileInputStream,
			@PathParam("dir") String dir,@PathParam("imageId") String imageId) throws Exception {

			//test if dir is not correct
			if (!dir.equals("ids") && !dir.equals("category") && !dir.equals("avatar")) {
				throw new DatabaseException("Image Directory is not correct, it must me either 'ids' or 'category' or 'avatar'! ");
			}
			
			//test if imageId is null
			if (imageId == null || imageId.trim().equals("") ) {
				throw new DataNotFoundException("Image Id  cannot be null  ! ");
			}
		
			File file = File.createTempFile("temp", null);

			try(OutputStream outputStream = new FileOutputStream(file)){
			    IOUtils.copy(fileInputStream, outputStream);
			    
				Map uploadResult = CloudinaryUtil.cloudinary.uploader().upload(file.getAbsolutePath(), ObjectUtils.asMap("resource_type", "auto",
						"public_id",imageId,
						"folder",dir));
						String imageUrl = uploadResult.get("secure_url").toString();
						UploadMessage response = new UploadMessage(imageUrl, 200);
						file.deleteOnExit();
						return Response.status(Status.OK).entity(response).build();
			    
			} catch (Exception e) {
			    // handle exception here
				ErrorMessage errorMessage = new ErrorMessage("Image didn't upload !!\nException: " + e.getMessage(), 400,
						"http://lilac-software-solutions.com");
				return Response.status(Status.BAD_REQUEST).entity(errorMessage).build();
			} 
	}

	@POST
	@Path("/workImages")
	@Consumes({ MediaType.MULTIPART_FORM_DATA })
	@Produces(MediaType.APPLICATION_JSON)
	public Response uploadImages(@FormDataParam("ImageOne") InputStream fileInputStream1,
			@FormDataParam("ImageTwo") InputStream fileInputStream2,
			@FormDataParam("ImageThree") InputStream fileInputStream3) throws Exception {

		if (fileInputStream1 != null) {
			uploadImage( fileInputStream1);
		}
		if (fileInputStream2 != null) {
			uploadImage( fileInputStream2);
		}
		if (fileInputStream3 != null) {
			uploadImage( fileInputStream3);
		}

		String imagesLinks = saveURL(imageUrls);
		UploadMessage response = new UploadMessage(imagesLinks, 200);
		return Response.status(Status.OK).entity(response).build();
	}
	
	  public String saveURL(ArrayList<String> list){
	        Gson gson = new Gson();
	        return gson.toJson(list);
	    }
	  

	public Boolean uploadImage(InputStream fileInputStream) throws Exception {

		// save uploaded file to new location
		
		File file = File.createTempFile("temp", null);

		try(OutputStream outputStream = new FileOutputStream(file)){
		    IOUtils.copy(fileInputStream, outputStream);
			Map uploadResult = CloudinaryUtil.cloudinary.uploader().upload(file.getAbsolutePath(), ObjectUtils.asMap("resource_type", "auto",
					"use_filename",true,
					"folder","work"));
					imageUrls.add(uploadResult.get("secure_url").toString());
					file.deleteOnExit();
					return true;
		    
		} catch (Exception e) {
			throw new DatabaseException(e.getMessage());
		} 

	}


}