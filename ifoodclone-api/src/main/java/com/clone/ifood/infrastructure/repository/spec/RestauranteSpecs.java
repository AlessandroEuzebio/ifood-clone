package com.clone.ifood.infrastructure.repository.spec;

import java.math.BigDecimal;

import org.springframework.data.jpa.domain.Specification;

import com.clone.ifood.domain.model.Restaurante;

public class RestauranteSpecs {

	public static Specification<Restaurante> comFreteGratis(){
//		return new RestauranteComFreteGratisSpec();
		//vamos criar uma classe anônima usando expressão lambda, dessa forma não precisaremos mais 
		//da classe RestauranteComFreteGratisSpec - vou deixar só para exemplificar melhor
		return (root, query, builder) -> 
			builder.equal(root.get("taxaFrete"), BigDecimal.ZERO);
		
	}
	
	public static Specification<Restaurante> comNomeSemelhante(String nome){
		return (root, query, builder) ->
			builder.like(root.get("nome"), "%" + nome + "%");
	}
}
