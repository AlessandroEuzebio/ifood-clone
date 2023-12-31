package com.clone.ifood.api.assembler;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.clone.ifood.api.model.input.CidadeInput;
import com.clone.ifood.domain.model.Cidade;
import com.clone.ifood.domain.model.Estado;

@Component
public class CidadeInputDisassembler {

	@Autowired
	private ModelMapper modelMapper;
	
	public Cidade toDomainObject(CidadeInput cidadeInput) {
		return modelMapper.map(cidadeInput, Cidade.class);
	}
	
	public void copyToDomainObject(CidadeInput cidadeInput, Cidade cidade) {
		cidade.setEstado(new Estado());
		
		modelMapper.map(cidadeInput, cidade);
	}
}
