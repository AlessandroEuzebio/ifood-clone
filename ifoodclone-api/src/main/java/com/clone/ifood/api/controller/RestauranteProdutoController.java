package com.clone.ifood.api.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.clone.ifood.api.assembler.ProdutoInputDisassembler;
import com.clone.ifood.api.assembler.ProdutoModelAssembler;
import com.clone.ifood.api.model.ProdutoModel;
import com.clone.ifood.api.model.input.ProdutoInput;
import com.clone.ifood.domain.model.Produto;
import com.clone.ifood.domain.model.Restaurante;
import com.clone.ifood.domain.repository.ProdutoRepository;
import com.clone.ifood.domain.service.CadastroProdutoService;
import com.clone.ifood.domain.service.CadastroRestauranteService;

@RestController
@RequestMapping("/restaurantes/{restauranteId}/produtos")
public class RestauranteProdutoController {

	@Autowired
	private ProdutoRepository produtoRepository;	
	
	@Autowired
	private CadastroProdutoService cadastroProdutoService;

	@Autowired
	private CadastroRestauranteService cadastroRestauranteService;
	
	@Autowired
	private ProdutoModelAssembler produtoModelAssembler;
	
	@Autowired
	private ProdutoInputDisassembler produtoInputDisassembler;
	
	
	@GetMapping
	public List<ProdutoModel> listar(@PathVariable Long restauranteId, 
										@RequestParam(required = false) boolean incluirInativos){
		Restaurante restaurante = cadastroRestauranteService.buscarOuFalhar(restauranteId);
		List<Produto> produtos = null;
		
		if(incluirInativos) {
			produtos = produtoRepository.findTodosByRestaurante(restaurante);
		}else {
			produtos = produtoRepository.findAtivosByRestaurante(restaurante);
		}	
		
		return produtoModelAssembler.toCollectionModel(produtos);
	}
	
	@GetMapping("/{produtoId}")
	public ProdutoModel buscar(@PathVariable Long restauranteId, @PathVariable Long produtoId) {
		Produto produto = cadastroProdutoService.buscarOuFalhar(produtoId, restauranteId);
		
		return produtoModelAssembler.toModel(produto);
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ProdutoModel adicionar(@RequestBody @Valid ProdutoInput produtoInput,
									@PathVariable Long restauranteId) {
		Restaurante restaurante = cadastroRestauranteService.buscarOuFalhar(restauranteId);
		Produto produto = produtoInputDisassembler.toDomainObject(produtoInput);
		produto.setRestaurante(restaurante);
		
		produto = cadastroProdutoService.salvar(produto);
		
		return produtoModelAssembler.toModel(produto);		
	}
	
	@PostMapping("/{produtoId}")
	public ProdutoModel atualizar(@RequestBody @Valid ProdutoInput produtoInput,
									@PathVariable Long produtoId, @PathVariable Long restauranteId) {
		Produto produto = cadastroProdutoService.buscarOuFalhar(produtoId, restauranteId);
		produtoInputDisassembler.copyToDomainObject(produtoInput, produto);
		
		produto = cadastroProdutoService.salvar(produto);
		
		return produtoModelAssembler.toModel(produto);
	}
}
