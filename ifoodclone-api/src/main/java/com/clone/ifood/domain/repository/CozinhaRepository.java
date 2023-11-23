package com.clone.ifood.domain.repository;

import java.util.List;
import java.util.Optional;

import com.clone.ifood.domain.model.Cozinha;

//@Repository
public interface CozinhaRepository extends CustomJpaRepository<Cozinha, Long>{
	
	List<Cozinha> findTodasByNomeContaining(String nome);
	
	Optional<Cozinha> findByNome(String nome);
	
	boolean existsByNome(String nome);
	
}
