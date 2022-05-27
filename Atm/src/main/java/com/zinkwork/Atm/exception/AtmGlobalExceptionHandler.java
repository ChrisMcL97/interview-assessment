package com.zinkwork.Atm.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AtmGlobalExceptionHandler {
	@ExceptionHandler(value = { AtmException.class })
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	ErrorResponse badRequest(AtmException ex) {
		return new ErrorResponse(400, ex.getMessage());
	}
}
