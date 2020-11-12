package com.servicepro.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.servicepro.responseModel.ErrorMessage;


@Provider // @Provider declares that the class is of interest to the JAX-RS runtime
public class DataAlreadyExistsExceptionMapper implements ExceptionMapper<DataAlreadyExistsException>{

	@Override
	public Response toResponse(DataAlreadyExistsException ex) {
		ErrorMessage errorMessage = new ErrorMessage(ex.getMessage(), 400, "http://lilac-software-solutions.com");
		return Response.status(Status.BAD_REQUEST).
				entity(errorMessage).
				build();
	}

}
