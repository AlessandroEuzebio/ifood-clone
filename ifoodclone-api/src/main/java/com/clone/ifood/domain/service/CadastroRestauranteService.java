package com.clone.ifood.domain.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.clone.ifood.domain.exception.RestauranteNaoEncontradoException;
import com.clone.ifood.domain.model.Cidade;
import com.clone.ifood.domain.model.Cozinha;
import com.clone.ifood.domain.model.FormaPagamento;
import com.clone.ifood.domain.model.Restaurante;
import com.clone.ifood.domain.model.Usuario;
import com.clone.ifood.domain.repository.RestauranteRepository;

@Service
public class CadastroRestauranteService {

	@Autowired
	private RestauranteRepository restauranteRepository;
	
	@Autowired
	private CadastroCozinhaService cadastroCozinha;
	
	@Autowired
	private CadastroCidadeService cadastroCidade;
	
	@Autowired
	private CadastroFormaPagamentoService cadastroFormaPagamento;
	
	@Autowired
	private CadastroUsuarioService	cadastroUsuario;
		
	@Transactional
	public Restaurante salvar(Restaurante restaurante) {
		Long cozinhaId = restaurante.getCozinha().getId();
		Long cidadeId = restaurante.getEndereco().getCidade().getId();

		Cozinha cozinha = cadastroCozinha.buscarOuFalhar(cozinhaId);
		Cidade cidade = cadastroCidade.buscarOuFalhar(cidadeId);
		
		restaurante.setCozinha(cozinha);
		restaurante.getEndereco().setCidade(cidade);
		
		return restauranteRepository.save(restaurante);
	}
	
	@Transactional
	public void ativar(Long RestauranteId) {
		Restaurante restauranteAtual = buscarOuFalhar(RestauranteId);
		
		restauranteAtual.ativar();
		//restauranteAtual.setAtivo(true);
		//não precisamos fazer um save no repositório porque quando buscamos o restaurante no repositório
		//a instância do restaurante fica em um estado gerenciado pelo contexto de persistência do JPA
		//qualquer modificação feita no objeto restauranteAtual dentro dessa transação, será sincronizada
		//depois com a base.
	}
	
	@Transactional
	public void inativar(Long RestauranteId) {
		Restaurante restauranteAtual = buscarOuFalhar(RestauranteId);
		
		restauranteAtual.inativar();
		//restauranteAtual.setAtivo(false);
	}
	
	@Transactional
	public void desassociarFormaPagamento(Long restauranteId, Long formaPagamentoId) {
		Restaurante restaurante = buscarOuFalhar(restauranteId);
		FormaPagamento formaPagamento = cadastroFormaPagamento.buscarOuFalhar(formaPagamentoId);
		
		restaurante.removerFormaPagamento(formaPagamento);
//		restaurante.getFormasPagamento().add(formaPagamento);
	}
	
	@Transactional
	public void associarFormaPagamento(Long restauranteId, Long formaPagamentoId) {
		Restaurante restaurante = buscarOuFalhar(restauranteId);
		FormaPagamento formaPagamento = cadastroFormaPagamento.buscarOuFalhar(formaPagamentoId);
		
		restaurante.adicionarFormaPagamento(formaPagamento);
	}
	
	@Transactional
	public void abrir(Long restauranteId) {
		Restaurante restaurante = buscarOuFalhar(restauranteId);		
		restaurante.abrir();
	}
	
	@Transactional
	public void fechar(Long restauranteId) {
		Restaurante restaurante = buscarOuFalhar(restauranteId);		
		restaurante.fechar();
	}
	
	@Transactional
	public void adicionarResponsável(Long restauranteId, Long usuarioId) {
		Restaurante restaurante = buscarOuFalhar(restauranteId);
		Usuario usuario = cadastroUsuario.buscarOuFalhar(usuarioId);
		
		restaurante.adicionarResponsavel(usuario);				
	}
	
	@Transactional
	public void removerResponsável(Long restauranteId, Long usuarioId) {
		Restaurante restaurante = buscarOuFalhar(restauranteId);
		Usuario usuario = cadastroUsuario.buscarOuFalhar(usuarioId);
		
		restaurante.removerResponsavel(usuario);				
	}
	
	@Transactional
	public void ativar(List<Long> restaurantesIds){
	  restaurantesIds.forEach(this::ativar);
	}

	@Transactional
	public void inativar(List<Long> restaurantesIds){
	  restaurantesIds.forEach(this::inativar);
	}
	
	public Restaurante buscarOuFalhar(Long restauranteId) {
		return restauranteRepository.findById(restauranteId)
				.orElseThrow(() -> new RestauranteNaoEncontradoException(restauranteId));
	}
}
