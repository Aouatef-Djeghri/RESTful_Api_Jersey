package com.servicepro.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.servicepro.responseModel.ErrorMessage;

@Provider
public class EmptyParameterExceptionMapper implements ExceptionMapper<EmptyParameterException> {

	@Override
	public Response toResponse(EmptyParameterException ex) {
		ErrorMessage errorMessage = new ErrorMessage(ex.getMessage(), 406, "http://lilac-software-solutions.com");
		return Response.status(Status.NOT_ACCEPTABLE).
				entity(errorMessage).
				build();
	}

}
