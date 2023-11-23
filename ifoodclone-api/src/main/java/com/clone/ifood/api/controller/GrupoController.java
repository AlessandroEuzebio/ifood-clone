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

import com.clone.ifood.api.assembler.GrupoInputDisassembler;
import com.clone.ifood.api.assembler.GrupoModelAssembler;
import com.clone.ifood.api.assembler.PermissaoModelAssembler;
import com.clone.ifood.api.model.GrupoModel;
import com.clone.ifood.api.model.PermissaoModel;
import com.clone.ifood.api.model.input.GrupoInput;
import com.clone.ifood.domain.model.Grupo;
import com.clone.ifood.domain.model.Permissao;
import com.clone.ifood.domain.repository.GrupoRepository;
import com.clone.ifood.domain.service.CadastroGrupoService;

@RestController
@RequestMapping("/grupos")
public class GrupoController {

	@Autowired
	private GrupoRepository grupoRepository;
	
	@Autowired
	private CadastroGrupoService cadastroGrupoService;
	
	@Autowired
	private GrupoModelAssembler grupoModelAssembler;
	
	@Autowired
	private GrupoInputDisassembler grupoInputDisassembler;
	
	@Autowired
	private PermissaoModelAssembler permissaoModelAssembler;
		
	@GetMapping
	public List<GrupoModel> listar(){
		List<Grupo> grupos = grupoRepository.findAll();
		
		return grupoModelAssembler.toCollectionModel(grupos);
	}
	
	@GetMapping("/{grupoId}")
	public GrupoModel buscar(@PathVariable Long grupoId) {
		Grupo grupo = cadastroGrupoService.buscarOuFalhar(grupoId);
		
		return grupoModelAssembler.toModel(grupo);
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public GrupoModel adicionar(@RequestBody @Valid GrupoInput grupoInput) {
		Grupo grupo = grupoInputDisassembler.toDomainObject(grupoInput);
		
		grupo = cadastroGrupoService.salvar(grupo);
		
		return grupoModelAssembler.toModel(grupo);
	}
	
	@PutMapping("/{grupoId}")
	public GrupoModel atualizar(@RequestBody @Valid GrupoInput grupoInput, @PathVariable Long grupoId) {
		Grupo grupoAtual = cadastroGrupoService.buscarOuFalhar(grupoId);
		
		grupoInputDisassembler.copyToDomainObject(grupoInput, grupoAtual);
		
		grupoAtual = cadastroGrupoService.salvar(grupoAtual);
		
		return grupoModelAssembler.toModel(grupoAtual);
	}
	
	@DeleteMapping("/{grupoId}")
	public void excluir(@PathVariable Long grupoId) {
		cadastroGrupoService.excluir(grupoId);
	}
	
	@GetMapping("/{grupoId}/permissoes")
	public List<PermissaoModel> listarPermissoes(@PathVariable Long grupoId){
		Grupo grupo = cadastroGrupoService.buscarOuFalhar(grupoId);
		Set<Permissao> permissoes = grupo.getPermissoes();
		
		return permissaoModelAssembler.toCollectionModel(permissoes);		
	}
	
	@PutMapping("{grupoId}/permissoes/{permissaoId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void associaPermissao(@PathVariable Long grupoId, @PathVariable Long permissaoId) {
		cadastroGrupoService.associaPermissao(grupoId, permissaoId);
	}
	
	@DeleteMapping("{grupoId}/permissoes/{permissaoId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void desassociaPermissao(@PathVariable Long grupoId, @PathVariable Long permissaoId) {
		cadastroGrupoService.desassociaPermissao(grupoId, permissaoId);
	}
}
