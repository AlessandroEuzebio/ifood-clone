package com.clone.ifood.api.assembler;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.clone.ifood.api.model.input.PedidoInput;
import com.clone.ifood.domain.model.Pedido;

@Component
public class PedidoInputDisassembler {

	@Autowired
	private ModelMapper modelMapper;
	
	public Pedido toDomainObject (PedidoInput pedidoInput) {
		
		Pedido pedido = modelMapper.map(pedidoInput, Pedido.class);
		return pedido;
	}
	
	public void copyToDomainObject(PedidoInput pedidoInput, Pedido pedido) {
		modelMapper.map(pedidoInput, pedido);
	}
}
