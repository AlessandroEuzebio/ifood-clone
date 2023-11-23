package com.clone.ifood.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.clone.ifood.domain.model.FormaPagamento;

public interface FormaPagamentoRepository extends JpaRepository<FormaPagamento, Long>{
	
}
