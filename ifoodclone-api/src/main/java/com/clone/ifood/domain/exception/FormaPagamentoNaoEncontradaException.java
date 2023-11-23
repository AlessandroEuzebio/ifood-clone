package com.clone.ifood.domain.exception;

public class FormaPagamentoNaoEncontradaException extends EntidadeNaoEncontradaException{

	private static final long serialVersionUID = 1L;

	public FormaPagamentoNaoEncontradaException(String mensagem) {
		super(mensagem);
	}
	
	public FormaPagamentoNaoEncontradaException(long formaPagamentoId) {
		this(String.format("Não existe uma forma de pagamento o com código %d", formaPagamentoId));
	}

}
