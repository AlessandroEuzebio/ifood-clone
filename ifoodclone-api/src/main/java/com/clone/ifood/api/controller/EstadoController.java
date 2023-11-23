package com.clone.ifood.api.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.clone.ifood.api.assembler.EstadoInputDisassembler;
import com.clone.ifood.api.assembler.EstadoModelAssembler;
import com.clone.ifood.api.model.EstadoModel;
import com.clone.ifood.api.model.input.EstadoInput;
import com.clone.ifood.domain.model.Estado;
import com.clone.ifood.domain.repository.EstadoRepository;
import com.clone.ifood.domain.service.CadastroEstadoService;

@RestController
@RequestMapping("/estados")
public class EstadoController {

	@Autowired
	private EstadoRepository estadoRepository;
	
	@Autowired
	private CadastroEstadoService estadoService;
	
	@Autowired
	private EstadoInputDisassembler estadoInputDisassembler;
	
	@Autowired
	private EstadoModelAssembler estadoModelAssembler;
	
	@GetMapping
	public List<EstadoModel> listar(){
		List<Estado> estados = estadoRepository.findAll();
		
		return estadoModelAssembler.toCollectionModel(estados);
	}
	
	@GetMapping("/{estadoId}")
	public EstadoModel buscar(@PathVariable Long estadoId){
		Estado estado = estadoService.buscarOuFalhar(estadoId);
		
		return estadoModelAssembler.toModel(estado);
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public EstadoModel adicionar(@RequestBody @Valid EstadoInput estadoInput) {
		Estado estado = estadoInputDisassembler.toDomainObject(estadoInput);
		
		estado = estadoService.salvar(estado);
		
		return estadoModelAssembler.toModel(estado);
	}
	
	@PutMapping("/{estadoId}")
	public EstadoModel atualizar(@RequestBody @Valid EstadoInput estadoInput, @PathVariable Long estadoId){		
		Estado estadoAtual = estadoService.buscarOuFalhar(estadoId);		
		estadoInputDisassembler.copyToDomainObject(estadoInput, estadoAtual);
		
		estadoAtual = estadoService.salvar(estadoAtual);
		
		return estadoModelAssembler.toModel(estadoAtual);
	}
	
	@DeleteMapping("/{estadoId}")
	public void remover(@PathVariable Long estadoId){		
		estadoService.excluir(estadoId);
	}
}
