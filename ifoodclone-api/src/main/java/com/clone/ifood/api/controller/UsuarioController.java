package com.clone.ifood.api.controller;

import java.util.List;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.clone.ifood.api.assembler.GrupoModelAssembler;
import com.clone.ifood.api.assembler.UsuarioInputDisassembler;
import com.clone.ifood.api.assembler.UsuarioModelAssembler;
import com.clone.ifood.api.model.GrupoModel;
import com.clone.ifood.api.model.UsuarioModel;
import com.clone.ifood.api.model.input.SenhaInput;
import com.clone.ifood.api.model.input.UsuarioComSenhaInput;
import com.clone.ifood.api.model.input.UsuarioInput;
import com.clone.ifood.domain.model.Grupo;
import com.clone.ifood.domain.model.Usuario;
import com.clone.ifood.domain.repository.UsuarioRepository;
import com.clone.ifood.domain.service.CadastroUsuarioService;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private CadastroUsuarioService cadastroUsuarioService;
	
	@Autowired
	private UsuarioModelAssembler usuarioModelAssembler;
	
	@Autowired
	private UsuarioInputDisassembler usuarioInputDisassembler;
	
	@Autowired
	private GrupoModelAssembler grupoModelAssembler;
	
	@GetMapping
	public List<UsuarioModel> listar() {
		List<Usuario> usuarios = usuarioRepository.findAll();
		
		return usuarioModelAssembler.toCollectionModel(usuarios);
	}
	
	@GetMapping("/{usuarioId}")
	public UsuarioModel buscar(@PathVariable Long usuarioId) {
		Usuario usuario = cadastroUsuarioService.buscarOuFalhar(usuarioId);
		
		return usuarioModelAssembler.toModel(usuario);
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public UsuarioModel adicionar(@RequestBody @Valid UsuarioComSenhaInput usuariocomSenha) {
		Usuario usuario = usuarioInputDisassembler.toDomainObject(usuariocomSenha);
		
		usuario = cadastroUsuarioService.salvar(usuario);
		
		return usuarioModelAssembler.toModel(usuario);
	}
	
	@PutMapping("/{usuarioId}")
	public UsuarioModel atualizar(@PathVariable Long usuarioId, @RequestBody @Valid UsuarioInput usuarioInput) {
		Usuario usuarioAtual = cadastroUsuarioService.buscarOuFalhar(usuarioId);
		
		usuarioInputDisassembler.copyToDomainObject(usuarioInput, usuarioAtual);
		
		usuarioAtual = cadastroUsuarioService.salvar(usuarioAtual);
		
		return usuarioModelAssembler.toModel(usuarioAtual);
	}
	
	@PutMapping("/{usuarioId}/senha")
	public void alterarSenha(@PathVariable Long usuarioId, @RequestBody @Valid SenhaInput senhaInput) {
		cadastroUsuarioService.alterarSenha(usuarioId, senhaInput.getSenhaAtual(), senhaInput.getNovaSenha());
	}
	
	@GetMapping("/{usuarioId}/grupos")
	public List<GrupoModel> listarGrupos(@PathVariable Long usuarioId){
		Usuario usuario = cadastroUsuarioService.buscarOuFalhar(usuarioId);
		Set<Grupo> grupos = usuario.getGrupos();
		
		return grupoModelAssembler.toCollectionModel(grupos);
	}
	
	@PutMapping("/{usuarioId}/grupos/{grupoId}")
	public void adicionarGrupo(@PathVariable Long usuarioId, @PathVariable Long grupoId) {
		cadastroUsuarioService.adicionaGrupo(grupoId, usuarioId);
	}
	
	@DeleteMapping("/{usuarioId}/grupos/{grupoId}")
	public void removerGrupo(@PathVariable Long usuarioId, @PathVariable Long grupoId) {
		cadastroUsuarioService.removeDoGrupo(grupoId, usuarioId);
	}	
}
