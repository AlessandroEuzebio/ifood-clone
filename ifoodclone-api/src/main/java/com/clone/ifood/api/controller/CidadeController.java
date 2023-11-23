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

import com.clone.ifood.api.assembler.CidadeInputDisassembler;
import com.clone.ifood.api.assembler.CidadeModelAssembler;
import com.clone.ifood.api.model.CidadeModel;
import com.clone.ifood.api.model.input.CidadeInput;
import com.clone.ifood.domain.exception.EntidadeNaoEncontradaException;
import com.clone.ifood.domain.exception.EstadoNaoEncontradoException;
import com.clone.ifood.domain.exception.NegocioException;
import com.clone.ifood.domain.model.Cidade;
import com.clone.ifood.domain.repository.CidadeRepository;
import com.clone.ifood.domain.service.CadastroCidadeService;

@RestController
@RequestMapping("/cidades")
public class CidadeController {

	@Autowired
	private CidadeRepository cidadeRepository;
	
	@Autowired
	private CadastroCidadeService cidadeService;
	
	@Autowired
	private CidadeInputDisassembler cidadeInputDisassembler;
	
	@Autowired
	private CidadeModelAssembler cidadeModelAssembler;
	
	@GetMapping
	public List<CidadeModel> listar(){
		List<Cidade> cidades = cidadeRepository.findAll();
		
		return cidadeModelAssembler.toCollectionModel(cidades);
	}
	
	@GetMapping("/{cidadeId}")
	public CidadeModel buscar(@PathVariable Long cidadeId){	
		Cidade cidade = cidadeService.buscarOuFalhar(cidadeId);
		
		return cidadeModelAssembler.toModel(cidade);
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public CidadeModel adicionar(@RequestBody @Valid CidadeInput cidadeInput){		
		try {
			Cidade cidade = cidadeInputDisassembler.toDomainObject(cidadeInput);
			
			cidade = cidadeService.salvar(cidade);
			
			return cidadeModelAssembler.toModel(cidade);
		} catch(EntidadeNaoEncontradaException e) {
			throw new NegocioException(e.getMessage(), e);
		}
		
	}
	
	@PutMapping("/{cidadeId}")
	public CidadeModel atualizar(@RequestBody @Valid CidadeInput cidadeInput, @PathVariable Long cidadeId){
		try {
			Cidade cidadeAtual = cidadeService.buscarOuFalhar(cidadeId);		
//			BeanUtils.copyProperties(cidade, cidadeAtual, "id");
			
			cidadeInputDisassembler.copyToDomainObject(cidadeInput, cidadeAtual);
			
			cidadeAtual = cidadeService.salvar(cidadeAtual);
			
			return  cidadeModelAssembler.toModel(cidadeAtual);
		} catch(EstadoNaoEncontradoException e) {
			throw new NegocioException(e.getMessage(), e); //se não passar o e da ruim no método de captura
		}		
	}
	
	@DeleteMapping("/{cidadeId}")
	public void remover(@PathVariable Long cidadeId) {
		cidadeService.excluir(cidadeId);
	}
}
