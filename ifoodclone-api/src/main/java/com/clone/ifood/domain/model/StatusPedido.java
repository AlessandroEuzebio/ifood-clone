package com.clone.ifood.domain.model;

import java.util.Arrays;
import java.util.List;

public enum StatusPedido {
	CRIADO("criado"),
	CONFIRMADO("confirmado", CRIADO), //a partir de qual status que podemos chegar ao confirmado? CRIADO!
	ENTREGUE("entregue", CONFIRMADO),
	CANCELADO("cancelado", CRIADO);
	
	private String descricao;
	private List<StatusPedido> statusAnteriores;
	
	StatusPedido(String descricao, StatusPedido... statusAnteriores){// o ... é porque podemos não passar esse parâmetro
		this.descricao = descricao;									//ou mesmo passar mais de um 
		this.statusAnteriores = Arrays.asList(statusAnteriores);
	}
	
	public String getDescricao() {
		return this.descricao;
	}
	
	public boolean naoPodeAlterarPara(StatusPedido novoStatus) {
		return !novoStatus.statusAnteriores.contains(this);
	}
}