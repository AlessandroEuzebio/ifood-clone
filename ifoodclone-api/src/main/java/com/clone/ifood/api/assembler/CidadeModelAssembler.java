package com.clone.ifood.api.assembler;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.clone.ifood.api.model.CidadeModel;
import com.clone.ifood.domain.model.Cidade;

@Component
public class CidadeModelAssembler {

	@Autowired
	private ModelMapper modelMapper;
	
	public CidadeModel toModel(Cidade cidade) {
		return modelMapper.map(cidade, CidadeModel.class);
	}
	
	public List<CidadeModel> toCollectionModel(List<Cidade> cidades){
		return cidades.stream()
				.map(cidade -> toModel(cidade))
				.collect(Collectors.toList());
	}
}
