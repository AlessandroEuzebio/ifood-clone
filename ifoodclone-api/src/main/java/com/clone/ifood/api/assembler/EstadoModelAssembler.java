package com.clone.ifood.api.assembler;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.clone.ifood.api.model.EstadoModel;
import com.clone.ifood.domain.model.Estado;

@Component
public class EstadoModelAssembler {

	@Autowired
	private ModelMapper modelMapper;
	
	public EstadoModel toModel(Estado estado) {
		return modelMapper.map(estado, EstadoModel.class);
	}
	
	public List<EstadoModel> toCollectionModel(List<Estado> estados){
		return estados.stream()
				.map(estado -> toModel(estado))
				.collect(Collectors.toList());
	}
}
