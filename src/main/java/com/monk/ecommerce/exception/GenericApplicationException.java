package com.monk.ecommerce.exception;

import org.springframework.http.HttpStatus;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GenericApplicationException extends RuntimeException{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2971944984793827549L;
	private String summary;
	private String detail;
	private HttpStatus statusCode;
}
