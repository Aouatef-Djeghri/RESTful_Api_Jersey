package com.servicepro.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.servicepro.responseModel.ErrorMessage;

@Provider
public class DatabaseExceptionMapper implements ExceptionMapper<DatabaseException>{
	@Override
	public Response toResponse(DatabaseException ex) { 
		ErrorMessage errorMessage = new ErrorMessage(ex.getMessage(), 500, "http://lilac-software-solutions.com");
		return Response.status(Status.INTERNAL_SERVER_ERROR).
				entity(errorMessage).
				build();
	}
}


