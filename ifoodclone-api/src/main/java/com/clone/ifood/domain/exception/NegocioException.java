package com.clone.ifood.domain.exception;

//@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NegocioException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	public NegocioException(String mensagem) {
		super(mensagem);
	}
	
	public NegocioException(String mensagem, Throwable causa) {
		super(mensagem, causa);
	}
}
