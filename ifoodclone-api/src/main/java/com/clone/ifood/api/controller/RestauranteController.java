package com.clone.ifood.api.controller;

import java.util.List;
import java.util.Set;

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

import com.clone.ifood.api.assembler.RestauranteInputDisassembler;
import com.clone.ifood.api.assembler.RestauranteModelAssembler;
import com.clone.ifood.api.assembler.UsuarioModelAssembler;
import com.clone.ifood.api.model.RestauranteModel;
import com.clone.ifood.api.model.UsuarioModel;
import com.clone.ifood.api.model.input.RestauranteInput;
import com.clone.ifood.api.model.view.RestauranteView;
import com.clone.ifood.domain.exception.CidadeNaoEncontradaException;
import com.clone.ifood.domain.exception.CozinhaNaoEncontradaException;
import com.clone.ifood.domain.exception.NegocioException;
import com.clone.ifood.domain.exception.RestauranteNaoEncontradoException;
import com.clone.ifood.domain.model.Restaurante;
import com.clone.ifood.domain.model.Usuario;
import com.clone.ifood.domain.repository.RestauranteRepository;
import com.clone.ifood.domain.service.CadastroRestauranteService;
import com.fasterxml.jackson.annotation.JsonView;

@RestController
@RequestMapping("/restaurantes")
public class RestauranteController {

	@Autowired
	private RestauranteRepository restauranteRepository;
	
	@Autowired
	private CadastroRestauranteService cadastroRestaurante;
	
	@Autowired
	private RestauranteModelAssembler restauranteModelAssembler;
	
	@Autowired
	private RestauranteInputDisassembler restauranteInputDisassembler;
	
	@Autowired
	private UsuarioModelAssembler usuarioModelAssembler;
	
	@JsonView(RestauranteView.Resumo.class)
	@GetMapping
	public List<RestauranteModel> listar(){
		List<Restaurante> restaurantes = restauranteRepository.findAll();
		
		return restauranteModelAssembler.toCollectionModel(restaurantes);
	}
	
	@JsonView(RestauranteView.ApenasNome.class)
	@GetMapping(params = "projecao=apenas-nome")
	public List<RestauranteModel> listarResumido(){			
		return listar();
	}
	
	@GetMapping("/{restauranteId}")
	public RestauranteModel buscar(@PathVariable Long restauranteId){		
		
		Restaurante restaurante = cadastroRestaurante.buscarOuFalhar(restauranteId);
		
		return restauranteModelAssembler.toModel(restaurante);
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public RestauranteModel adicionar(
			@RequestBody @Valid RestauranteInput restauranteInput){
		try {
			Restaurante restaurante = restauranteInputDisassembler.toDomainObject(restauranteInput);
			
			return restauranteModelAssembler.toModel(cadastroRestaurante.salvar(restaurante));
		} catch(CozinhaNaoEncontradaException | CidadeNaoEncontradaException e) {
			throw new NegocioException(e.getMessage());
		}
	}
	
	@PutMapping("/{restauranteId}")
	public RestauranteModel atualizar(@PathVariable Long restauranteId,
										@RequestBody @Valid RestauranteInput restauranteInput){
		try {			
			Restaurante restauranteAtual = cadastroRestaurante.buscarOuFalhar(restauranteId);
			
			restauranteInputDisassembler.copyToDomainObject(restauranteInput, restauranteAtual);
			
			return restauranteModelAssembler.toModel(cadastroRestaurante.salvar(restauranteAtual));
		} catch(CozinhaNaoEncontradaException | CidadeNaoEncontradaException e) {
			throw new NegocioException(e.getMessage());
		}		
	}
	
	@PutMapping("/{restauranteId}/ativo")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void ativar(@PathVariable Long restauranteId) {
		cadastroRestaurante.ativar(restauranteId);
	}
	
	@DeleteMapping("/{restauranteId}/ativo")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void inativar(@PathVariable Long restauranteId) {
		cadastroRestaurante.inativar(restauranteId);
	}
	
	@PutMapping("/{restauranteId}/abertura")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void abrir(@PathVariable Long restauranteId) {
		cadastroRestaurante.abrir(restauranteId);		
	}
	
	@PutMapping("/{restauranteId}/fechamento")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void fechar(@PathVariable Long restauranteId) {
		cadastroRestaurante.fechar(restauranteId);		
	}
	
	@GetMapping("/{restauranteId}/responsaveis")
	public List<UsuarioModel> listarResponsaveis(@PathVariable Long restauranteId){
		Restaurante restaurante = cadastroRestaurante.buscarOuFalhar(restauranteId);
		Set<Usuario> responsaveis = restaurante.getResponsaveis();
		
		return usuarioModelAssembler.toCollectionModel(responsaveis);
	}
	
	@PutMapping("/{restauranteId}/responsaveis/{responsavelId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void adicionarResponsavel(@PathVariable Long restauranteId, @PathVariable Long responsavelId) {
		cadastroRestaurante.adicionarResponsável(restauranteId, responsavelId);
	}
	
	@DeleteMapping("/{restauranteId}/responsaveis/{responsavelId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void removerResponsavel(@PathVariable Long restauranteId, @PathVariable Long responsavelId) {
		cadastroRestaurante.removerResponsável(restauranteId, responsavelId);
	}
	
	@PutMapping("/ativacoes")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void ativarMultiplos(@RequestBody List<Long> restaurantesIds) {		
		try {
			cadastroRestaurante.ativar(restaurantesIds);
		} catch(RestauranteNaoEncontradoException e) {
			throw new NegocioException(e.getMessage(), e);
		}
	}
	
	@DeleteMapping("/ativacoes")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void desativarMultiplos(@RequestBody List<Long> restaurantesIds) {
		try {
			cadastroRestaurante.inativar(restaurantesIds);
		} catch(RestauranteNaoEncontradoException e) {
			throw new NegocioException(e.getMessage(), e);
		}
	}
	
}
