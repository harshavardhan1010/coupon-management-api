package com.monk.ecommerce.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApplicationExceptionHandler {

	@ExceptionHandler(GenericApplicationException.class)
	private ResponseEntity<?> handleException(GenericApplicationException ex) {
		ExceptionResponse exp = ExceptionResponse.builder().error(ex.getSummary())
				.businessErrorDescription(ex.getDetail()).build();
		return ResponseEntity.status(ex.getStatusCode()).body(exp);
	}

	@ExceptionHandler(Exception.class)
	private ResponseEntity<?> handleException(Exception ex) {
		Map<String, String> body = new HashMap<>();
		body.put("summary", "Internal Server error please contact admin");
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
	}
}
