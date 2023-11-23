package com.clone.ifood.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.clone.ifood.domain.model.Produto;
import com.clone.ifood.domain.model.Restaurante;

public interface ProdutoRepository extends JpaRepository<Produto, Long>{
	
	@Query("from Produto where id = :produtoId and restaurante.id = :restauranteId") //aparentemente tem que ser restaurante.id e não restaurante_id
	Optional<Produto> findById(Long produtoId, Long restauranteId);

	List<Produto> findTodosByRestaurante(Restaurante restaurante);
	
	@Query("from Produto p where p.ativo = true and p.restaurante = :restaurante")
	List<Produto> findAtivosByRestaurante(Restaurante restaurante);
}
