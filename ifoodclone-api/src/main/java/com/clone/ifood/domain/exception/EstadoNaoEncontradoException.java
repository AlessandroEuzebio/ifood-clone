package com.clone.ifood.domain.exception;

//@ResponseStatus(HttpStatus.NOT_FOUND) não precisa pois EntidadeNaoEncontradaException já é NOT_FOUND
public class EstadoNaoEncontradoException extends EntidadeNaoEncontradaException{

	private static final long serialVersionUID = 1L;
	
	public EstadoNaoEncontradoException(String mensagem) {
		super(mensagem);
	}
	
	public EstadoNaoEncontradoException(long estadoId) {
		this(String.format("Não existe um cadastro de estado com código %d", estadoId));
	}
}
