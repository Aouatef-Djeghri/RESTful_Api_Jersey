package com.servicepro.exception;

import javax.ws.rs.core.*;
import javax.ws.rs.core.Response.*;
import javax.ws.rs.ext.*;

import com.servicepro.responseModel.ErrorMessage;


@Provider // @Provider declares that the class is of interest to the JAX-RS runtime
public class DataNotFoundExceptionMapper implements ExceptionMapper<DataNotFoundException>{

	@Override
	public Response toResponse(DataNotFoundException ex) {
		ErrorMessage errorMessage = new ErrorMessage(ex.getMessage(), 404, "http://lilac-software-solutions.com");
		return Response.status(Status.NOT_FOUND).
				entity(errorMessage).
				build();
	}

}
