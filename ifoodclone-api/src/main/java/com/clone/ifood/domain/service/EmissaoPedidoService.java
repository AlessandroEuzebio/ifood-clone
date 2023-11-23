package com.clone.ifood.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.clone.ifood.domain.exception.NegocioException;
import com.clone.ifood.domain.exception.PedidoNaoEncontradoException;
import com.clone.ifood.domain.model.Cidade;
import com.clone.ifood.domain.model.FormaPagamento;
import com.clone.ifood.domain.model.Pedido;
import com.clone.ifood.domain.model.Produto;
import com.clone.ifood.domain.model.Restaurante;
import com.clone.ifood.domain.repository.PedidoRepository;

@Service
public class EmissaoPedidoService {

	@Autowired
	private PedidoRepository pedidoRepository;
	
	@Autowired
	private CadastroRestauranteService cadastroRestaurante;
	
	@Autowired
	private CadastroFormaPagamentoService cadastroFormaPagamento;
	
	@Autowired
	private CadastroCidadeService cadastroCidade;
	
	@Autowired
	private CadastroProdutoService cadastroProduto;
	
	@Transactional
	public Pedido emitir(Pedido pedido) {
		validarPedido(pedido);
		validarItens(pedido);
		
		Restaurante restaurante = pedido.getRestaurante();
		
		pedido.setTaxaFrete(restaurante.getTaxaFrete());
		pedido.calcularValorTotal();
		
		return pedidoRepository.save(pedido);
	}
	
	public void validarPedido(Pedido pedido) {
		Long restauranteId = pedido.getRestaurante().getId();
		Restaurante restaurante = cadastroRestaurante.buscarOuFalhar(restauranteId);
		
		Long formaPagamentoId = pedido.getFormaPagamento().getId();
		FormaPagamento formaPagamento = cadastroFormaPagamento.buscarOuFalhar(formaPagamentoId);
		
		Long cidadeId = pedido.getEnderecoEntrega().getCidade().getId();
		Cidade cidade = cadastroCidade.buscarOuFalhar(cidadeId);
		
		pedido.setRestaurante(restaurante);
		pedido.setFormaPagamento(formaPagamento);
		pedido.getEnderecoEntrega().setCidade(cidade);
		
		if(restaurante.naoAceitaFormaPagamento(formaPagamento)) {
			throw new NegocioException(
						String.format("A forma de pagamento %s não é aceita pelo restaurante", formaPagamento.getDescricao()));
		}		
	}
	
	public void validarItens(Pedido pedido) {
		pedido.getItens().forEach(
				item -> {
					Produto produto = cadastroProduto.buscarOuFalhar(item.getProduto().getId(), pedido.getRestaurante().getId());
					
					item.setPedido(pedido);
					item.setProduto(produto);
					item.setPrecoUnitario(produto.getPreco());
				});
	}
	
	public Pedido buscarOuFalhar(String codigoPedido) {
		return pedidoRepository.findByCodigo(codigoPedido)
				.orElseThrow(() -> new PedidoNaoEncontradoException(codigoPedido));
	}
}
