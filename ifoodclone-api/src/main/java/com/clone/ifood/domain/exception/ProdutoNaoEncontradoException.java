package com.clone.ifood.domain.exception;

public class ProdutoNaoEncontradoException extends EntidadeNaoEncontradaException{

	private static final long serialVersionUID = 1L;

	public ProdutoNaoEncontradoException(String mensagem) {
		super(mensagem);
	}
	
	public ProdutoNaoEncontradoException(long produtoId, Long restauranteId) {
		this(String.format("Não existe cadastro de produto com o código %d para o restaurante %d", produtoId, restauranteId));
	}

}
