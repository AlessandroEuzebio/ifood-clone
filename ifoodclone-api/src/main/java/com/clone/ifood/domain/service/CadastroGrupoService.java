package com.clone.ifood.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.clone.ifood.domain.exception.EntidadeEmUsoException;
import com.clone.ifood.domain.exception.GrupoNaoEncontradoException;
import com.clone.ifood.domain.model.Grupo;
import com.clone.ifood.domain.model.Permissao;
import com.clone.ifood.domain.repository.GrupoRepository;

@Service
public class CadastroGrupoService {

	private static final String MSG_GRUPO_EM_USO = "Grupo de código %d não pode ser removido pois está em uso.";
	
	@Autowired
	private GrupoRepository grupoRepository;
	
	@Autowired
	private CadastroPermissaoService cadastroPermissaoService;
	
	@Transactional
	public Grupo salvar(Grupo grupo) {
		return grupoRepository.save(grupo);
	}
	
	@Transactional
	public void associaPermissao(Long grupoId, Long permissaoId) {
		Grupo grupo = buscarOuFalhar(grupoId);
		Permissao permissao = cadastroPermissaoService.buscarOuFalhar(permissaoId);
		
		grupo.adicionarPermissao(permissao);
	}
	
	@Transactional
	public void desassociaPermissao(Long grupoId, Long permissaoId) {
		Grupo grupo = buscarOuFalhar(grupoId);
		Permissao permissao = cadastroPermissaoService.buscarOuFalhar(permissaoId);
		
		grupo.removerPermissao(permissao);
	}
	
	@Transactional
	public void excluir(Long grupoId) {
		try {
			grupoRepository.deleteById(grupoId);
			grupoRepository.flush();
		} catch(EmptyResultDataAccessException e) {
			throw new GrupoNaoEncontradoException(grupoId);			
		} catch(DataIntegrityViolationException e) {
			throw new EntidadeEmUsoException(
					String.format(MSG_GRUPO_EM_USO, grupoId));
		}
	}
	
	public Grupo buscarOuFalhar(Long grupoId) {
		return grupoRepository.findById(grupoId)
				.orElseThrow(() -> new GrupoNaoEncontradoException(grupoId));
	}
}
