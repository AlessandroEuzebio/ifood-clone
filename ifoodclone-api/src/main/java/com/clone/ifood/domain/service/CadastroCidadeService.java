package com.clone.ifood.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.clone.ifood.domain.exception.CidadeNaoEncontradaException;
import com.clone.ifood.domain.exception.EntidadeEmUsoException;
import com.clone.ifood.domain.model.Cidade;
import com.clone.ifood.domain.model.Estado;
import com.clone.ifood.domain.repository.CidadeRepository;

@Service
public class CadastroCidadeService {

	private static final String MSG_CIDADE_EM_USO = "A cidade de código %d não pode ser removida pois está em uso";

	@Autowired
	private CidadeRepository cidadeRepository;
	
	@Autowired
	private CadastroEstadoService cadastroEstado;	
	
	@Transactional
	public Cidade salvar(Cidade cidade) {
		Long estadoId = cidade.getEstado().getId();
		
		Estado estado = cadastroEstado.buscarOuFalhar(estadoId);
		
		cidade.setEstado(estado);
		return cidadeRepository.save(cidade);
	}
	
	@Transactional
	public void excluir(Long cidadeId) {
		try {
			cidadeRepository.deleteById(cidadeId);
			cidadeRepository.flush();
			
		} catch(EmptyResultDataAccessException e) {
			throw new CidadeNaoEncontradaException(cidadeId);
			
		} catch(DataIntegrityViolationException e) {
			throw new EntidadeEmUsoException(
					String.format(MSG_CIDADE_EM_USO, cidadeId));
		}
	}
	
	public Cidade buscarOuFalhar(Long cidadeId) {
		return cidadeRepository.findById(cidadeId)
				.orElseThrow(() -> new CidadeNaoEncontradaException(cidadeId));
	}
}
