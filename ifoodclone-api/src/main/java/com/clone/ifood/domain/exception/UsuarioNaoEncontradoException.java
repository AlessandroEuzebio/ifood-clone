package com.clone.ifood.domain.exception;

public class UsuarioNaoEncontradoException extends EntidadeNaoEncontradaException{

	private static final long serialVersionUID = 1L;

	public UsuarioNaoEncontradoException(String mensagem) {
		super(mensagem);
	}
	
	public UsuarioNaoEncontradoException(long usuarioId) {
		this(String.format("Não existe cadastro de usuário com o código %d", usuarioId));
	}
}
