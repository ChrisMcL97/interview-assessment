package com.zinkwork.Atm.exception;

public class AtmException extends RuntimeException{
	
	private static final long serialVersionUID = 3017428362996029475L;

	public AtmException(String error) {
		super(error);
	}
}
