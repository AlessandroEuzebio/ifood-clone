package com.clone.ifood.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.clone.ifood.domain.exception.EntidadeEmUsoException;
import com.clone.ifood.domain.exception.FormaPagamentoNaoEncontradaException;
import com.clone.ifood.domain.model.FormaPagamento;
import com.clone.ifood.domain.repository.FormaPagamentoRepository;

@Service
public class CadastroFormaPagamentoService {
	
	private static final String MSG_FORMA_PAGAMENTO_EM_USO = "Forma de pagamento de código %d não pode ser removida, pois está em uso";

	@Autowired
	private FormaPagamentoRepository formaPagamentoRepository;
	
	@Transactional
	public FormaPagamento salvar(FormaPagamento formaPagamento) {
		return formaPagamentoRepository.save(formaPagamento);
	}
	
	@Transactional
	public void excluir(long formaPagamentoId) {
		try {
			formaPagamentoRepository.deleteById(formaPagamentoId);
		} catch(EmptyResultDataAccessException e) {
			throw new FormaPagamentoNaoEncontradaException(formaPagamentoId);
		} catch(DataIntegrityViolationException e) {
			throw new EntidadeEmUsoException(MSG_FORMA_PAGAMENTO_EM_USO);
		}
	}
	
	public FormaPagamento buscarOuFalhar(Long formaPagamentoId) {
		return formaPagamentoRepository.findById(formaPagamentoId)
				.orElseThrow(() -> new FormaPagamentoNaoEncontradaException(formaPagamentoId));
	}
}
