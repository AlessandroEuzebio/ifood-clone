package com.clone.ifood.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean // significa que a interface não deve ser levada em conta para fim de instanciação de um repositório pelo SDJ
public interface CustomJpaRepository<T, ID> extends JpaRepository<T, ID>{

	Optional<T> buscarPrimeiro();
	
	void detach(T entity);
}
