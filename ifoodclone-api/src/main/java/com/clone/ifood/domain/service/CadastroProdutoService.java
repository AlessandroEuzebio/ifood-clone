package com.clone.ifood.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.clone.ifood.domain.exception.ProdutoNaoEncontradoException;
import com.clone.ifood.domain.model.Produto;
import com.clone.ifood.domain.repository.ProdutoRepository;

@Service
public class CadastroProdutoService {

	@Autowired
	private ProdutoRepository produtoRepository;
	
	@Transactional
	public Produto salvar(Produto produto) {
		return produtoRepository.save(produto);
	}	
	
	public Produto buscarOuFalhar(Long produtoId, Long restauranteId) {
		return produtoRepository.findById(produtoId, restauranteId)
				.orElseThrow(() -> new ProdutoNaoEncontradoException(produtoId, restauranteId));
	}

}
