package com.clone.ifood.domain.exception;

public class GrupoNaoEncontradoException extends EntidadeNaoEncontradaException{

	private static final long serialVersionUID = 1L;

	public GrupoNaoEncontradoException(String mensagem) {
		super(mensagem);
	}
	
	public GrupoNaoEncontradoException(long grupoId) {
		this(String.format("Não existe um cadastro de grupo com o código %d", grupoId));
	}
}
