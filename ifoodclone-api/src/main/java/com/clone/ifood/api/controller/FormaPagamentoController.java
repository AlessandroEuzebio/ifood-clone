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

import com.clone.ifood.api.assembler.FormaPagamentoInputDisassembler;
import com.clone.ifood.api.assembler.FormaPagamentoModelAssembler;
import com.clone.ifood.api.model.FormaPagamentoModel;
import com.clone.ifood.api.model.input.FormaPagamentoInput;
import com.clone.ifood.domain.model.FormaPagamento;
import com.clone.ifood.domain.repository.FormaPagamentoRepository;
import com.clone.ifood.domain.service.CadastroFormaPagamentoService;

@RestController
@RequestMapping("/formas-pagamento")
public class FormaPagamentoController {

	@Autowired
	FormaPagamentoRepository formaPagamentoRepository;
	
	@Autowired
	CadastroFormaPagamentoService cadastroFormaPagamentoService;
	
	@Autowired
	FormaPagamentoModelAssembler formaPagamentoModelAssembler;
	
	@Autowired
	FormaPagamentoInputDisassembler formaPagamentoInputDisassembler;
	
	@GetMapping
	public List<FormaPagamentoModel> listar(){
		List<FormaPagamento> formasPagamento = formaPagamentoRepository.findAll();
		
		return formaPagamentoModelAssembler.toCollectionModel(formasPagamento);
	}
	
	@GetMapping("/{formaPagamentoId}")
	public FormaPagamentoModel buscar(@PathVariable Long formaPagamentoId) {
		FormaPagamento formaPagamento = cadastroFormaPagamentoService.buscarOuFalhar(formaPagamentoId);
		
		return formaPagamentoModelAssembler.toModel(formaPagamento);
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public FormaPagamentoModel adicionar(@RequestBody @Valid FormaPagamentoInput formaPagamentoInput) {
		FormaPagamento formaPagamento = formaPagamentoInputDisassembler.toDomainObject(formaPagamentoInput);
		formaPagamento = formaPagamentoRepository.save(formaPagamento);
		
		return formaPagamentoModelAssembler.toModel(formaPagamento);		
	}
	
	@PutMapping("/{formaPagamentoId}")
	public FormaPagamentoModel atualizar(@RequestBody @Valid FormaPagamentoInput formaPagamentoInput, 
											@PathVariable Long formaPagamentoId) {
		FormaPagamento formaPagamentoAtual = cadastroFormaPagamentoService.buscarOuFalhar(formaPagamentoId);
				
		formaPagamentoInputDisassembler.copyToDomainObject(formaPagamentoInput, formaPagamentoAtual);
		
		formaPagamentoAtual = cadastroFormaPagamentoService.salvar(formaPagamentoAtual);
		
		return formaPagamentoModelAssembler.toModel(formaPagamentoAtual);		
	}
	
	@DeleteMapping("/{formaPagamentoId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void excluit(@PathVariable Long formaPagamentoId) {
		cadastroFormaPagamentoService.excluir(formaPagamentoId);
	}
}
