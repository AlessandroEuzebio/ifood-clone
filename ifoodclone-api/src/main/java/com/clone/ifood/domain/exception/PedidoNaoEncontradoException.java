package com.clone.ifood.domain.exception;

public class PedidoNaoEncontradoException extends EntidadeNaoEncontradaException{

	private static final long serialVersionUID = 1L;
	
	public PedidoNaoEncontradoException(String codigoPedido) {
		super(String.format("Não existe cadastro de pedido com id %d", codigoPedido));
	}
}
