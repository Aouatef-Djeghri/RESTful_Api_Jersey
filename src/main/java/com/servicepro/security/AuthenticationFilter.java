package com.servicepro.security;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;

//@Provider
public class AuthenticationFilter implements ContainerResponseFilter, ContainerRequestFilter {

	@Override
	public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext)
			throws IOException {
		// Not implemented yet
	}

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		// Not implemented yet
	}


}
