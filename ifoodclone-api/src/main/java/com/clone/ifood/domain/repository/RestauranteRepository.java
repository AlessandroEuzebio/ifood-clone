package com.clone.ifood.domain.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.clone.ifood.domain.model.Restaurante;

//@Repository não precisa dessa anotação pq já herda
public interface RestauranteRepository extends CustomJpaRepository<Restaurante, Long>, RestauranteRepositoryQueries, JpaSpecificationExecutor<Restaurante>{

	@Query("from Restaurante r join r.cozinha")
	List<Restaurante> findAll();
	
	//queremos restaurantes com a taxa frete entre a taxaInicial e a taxaFinal
	List<Restaurante> queryByTaxaFreteBetween(BigDecimal taxaInicial, BigDecimal taxaFinal);
	
//	@Query("from Restaurante  where nome like %:nome% and cozinha.id = :id") // query agora no orm.xml
	List<Restaurante> consultarPorNome(String nome, @Param("id") Long cozinha); //se deixarmos o nome das variáveis iguais o bind é automático
	
//	List<Restaurante> findByNomeContainingAndCozinhaId(String nome, Long cozinha);
	
	Optional<Restaurante> findFirstRestauranteByNomeContaining(String nome);
	
	List<Restaurante> findTop2ByNomeContaining(String nome);
	
	int countByCozinhaId(Long cozinhaId);
}
