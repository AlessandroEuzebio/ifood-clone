package com.clone.ifood.infrastructure.repository;

import java.util.Optional;

import javax.persistence.EntityManager;

import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import com.clone.ifood.domain.repository.CustomJpaRepository;

public class CustomJpaRepositoryImpl<T, ID> extends SimpleJpaRepository<T, ID>
	implements CustomJpaRepository<T, ID>{
	
	private EntityManager manager;

	public CustomJpaRepositoryImpl(JpaEntityInformation<T, ?> entityInformation, 
			EntityManager entityManager) {
		super(entityInformation, entityManager);
		this.manager = entityManager;
	}

	@Override
	public Optional<T> buscarPrimeiro(){
		var jpql = "from " + getDomainClass().getName(); //getDomainClass vem da classe pai que retorna a classe que representa a entidade
	
		T entity = manager.createQuery(jpql, getDomainClass())
			.setMaxResults(1) // o máximo que a consulta retorna é 1
			.getSingleResult(); //só pode retornar um resultado
		
		return Optional.ofNullable(entity); // pode ter um valor nulo dentro do option
	}
	
	@Override
	public void detach(T entity) {
		manager.detach(entity);
	}
}
