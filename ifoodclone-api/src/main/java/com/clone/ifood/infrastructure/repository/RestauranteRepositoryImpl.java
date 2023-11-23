package com.clone.ifood.infrastructure.repository;

import static com.clone.ifood.infrastructure.repository.spec.RestauranteSpecs.comFreteGratis;
import static com.clone.ifood.infrastructure.repository.spec.RestauranteSpecs.comNomeSemelhante;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.clone.ifood.domain.model.Restaurante;
import com.clone.ifood.domain.repository.RestauranteRepository;
import com.clone.ifood.domain.repository.RestauranteRepositoryQueries;

@Repository
public class RestauranteRepositoryImpl implements RestauranteRepositoryQueries {

	@PersistenceContext
	private EntityManager manager;
	
	@Autowired @Lazy // lazy serve para evitar a dependencia circular, só vai instanciar a dependencia no momento em que for preciso
	private RestauranteRepository restauranteRepository;
	
	@Override
	public List<Restaurante> find(String nome, BigDecimal taxaFreteInicial, BigDecimal taxaFreteFinal){
		
		//para obter a instância de uma criteriaQuery precisamos da instância de uma CriteriaBuilder
		CriteriaBuilder builder = manager.getCriteriaBuilder(); // também é uma interface e funciona como uma fábrica para construir elementos necessários para fazer consultas como critérios da consulta e a própria CriteriaQuery
		
		CriteriaQuery<Restaurante> criteria = builder.createQuery(Restaurante.class); //é uma interface responsável por montar a estrutura de uma query, é um construtor de cláusulas	
		Root<Restaurante> root = criteria.from(Restaurante.class); // mesmo que o from Restaurante e o root é o restaurante
		
		var predicates = new ArrayList<Predicate>();
		
		if(StringUtils.hasText(nome)) {
			predicates.add(builder.like(root.get("nome"), "%" + nome + "%"));
		}
		if(taxaFreteInicial != null) {
			predicates.add(builder.greaterThanOrEqualTo(root.get("taxaFrete"), taxaFreteInicial));
		}
		if(taxaFreteFinal != null) {
			predicates.add(builder.lessThanOrEqualTo(root.get("taxaFrete"), taxaFreteFinal));
		}
		
		criteria.where(predicates.toArray(new Predicate[0]));
		
		TypedQuery<Restaurante> query = manager.createQuery(criteria);
		return query.getResultList();
	}

	@Override
	public List<Restaurante> findComFreteGratis(String nome) {
		
		return restauranteRepository.findAll(comFreteGratis()
				.and(comNomeSemelhante(nome)));
	}
}
