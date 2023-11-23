package com.clone.ifood.domain.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.clone.ifood.domain.exception.NegocioException;
import com.clone.ifood.domain.exception.UsuarioNaoEncontradoException;
import com.clone.ifood.domain.model.Grupo;
import com.clone.ifood.domain.model.Usuario;
import com.clone.ifood.domain.repository.UsuarioRepository;

@Service
public class CadastroUsuarioService {
	

	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private CadastroGrupoService cadastroGrupoService;
	
	@Transactional
	public Usuario salvar(Usuario usuario) {
		usuarioRepository.detach(usuario);
		
		Optional<Usuario> usuarioExistente = usuarioRepository.findByEmail(usuario.getEmail());
		
		if(usuarioExistente.isPresent() && usuarioExistente.get().equals(usuario)) { //o && serve para atualização de usuario
			throw new NegocioException(
					String.format("Já existe um usuário cadastrado com o e-mail %s", usuario.getEmail()));
		}
		
		return usuarioRepository.save(usuario);
	}
	
	@Transactional
	public void alterarSenha(Long usuarioId, String senhaAtual, String novaSenha) {
		Usuario usuario = buscarOuFalhar(usuarioId);
		
		if(usuario.senhaNaoCoincideCom(senhaAtual)) {
			throw new NegocioException("Senha atual informada não conicide com a senha do usuário.");
		}
		
		usuario.setSenha(novaSenha);
	}
	
	@Transactional
	public void adicionaGrupo(Long grupoId, Long usuarioId) {
		Usuario usuario = buscarOuFalhar(usuarioId);
		Grupo grupo = cadastroGrupoService.buscarOuFalhar(grupoId);
		
		usuario.adicionarAoGrupo(grupo);
	}
	
	@Transactional
	public void removeDoGrupo(Long grupoId, Long usuarioId) {
		Usuario usuario = buscarOuFalhar(usuarioId);
		Grupo grupo = cadastroGrupoService.buscarOuFalhar(grupoId);
		
		usuario.removerDoGrupo(grupo);
	}
	
	public Usuario buscarOuFalhar(Long usuarioId) {
		return usuarioRepository.findById(usuarioId)
				.orElseThrow(() -> new UsuarioNaoEncontradoException(usuarioId));
	}
	

}
