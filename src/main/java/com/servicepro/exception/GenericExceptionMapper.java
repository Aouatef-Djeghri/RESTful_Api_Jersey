package com.servicepro.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.servicepro.responseModel.ErrorMessage;


//@Provider
public class GenericExceptionMapper implements ExceptionMapper<Exception>{

	@Override
	public Response toResponse(Exception ex) {
		ErrorMessage errorMessage = new ErrorMessage("Uri doesn't exist \n(or might be something else , this is a genericException message it is still under construction)", 500, "http://lilac-software-solutions.com");
		return Response.status(Status.INTERNAL_SERVER_ERROR).
				entity(errorMessage).
				build();
	}

}