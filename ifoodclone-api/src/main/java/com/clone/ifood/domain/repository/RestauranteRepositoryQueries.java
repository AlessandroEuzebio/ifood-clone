package com.clone.ifood.domain.repository;

import java.math.BigDecimal;
import java.util.List;

import com.clone.ifood.domain.model.Restaurante;

public interface RestauranteRepositoryQueries {

	List<Restaurante> find(String nome, 
			BigDecimal taxaFreteInicial, BigDecimal taxaFreteFinal);

	List<Restaurante> findComFreteGratis(String nome);
}