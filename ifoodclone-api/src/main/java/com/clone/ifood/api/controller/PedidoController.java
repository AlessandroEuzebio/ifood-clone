package com.clone.ifood.api.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.clone.ifood.api.assembler.PedidoInputDisassembler;
import com.clone.ifood.api.assembler.PedidoModelAssembler;
import com.clone.ifood.api.assembler.PedidoResumoModelAssembler;
import com.clone.ifood.api.model.PedidoModel;
import com.clone.ifood.api.model.PedidoResumoModel;
import com.clone.ifood.api.model.input.PedidoInput;
import com.clone.ifood.domain.exception.EntidadeNaoEncontradaException;
import com.clone.ifood.domain.exception.NegocioException;
import com.clone.ifood.domain.model.Pedido;
import com.clone.ifood.domain.model.Usuario;
import com.clone.ifood.domain.repository.PedidoRepository;
import com.clone.ifood.domain.repository.filter.PedidoFilter;
import com.clone.ifood.domain.service.EmissaoPedidoService;
import com.clone.ifood.infrastructure.repository.spec.PedidoSpecs;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

	@Autowired
	private PedidoRepository pedidoRepository;
	
	@Autowired
	private EmissaoPedidoService emissaoPedidoService;
	
	@Autowired
	private PedidoModelAssembler pedidoModelAssembler;
	
	@Autowired
	private PedidoResumoModelAssembler pedidoResumoModelAssembler;
	
	@Autowired
	private PedidoInputDisassembler pedidoInputDisassembler;
	
	@Autowired
	private EmissaoPedidoService emissaoPedido;
	
	@GetMapping
	public List<PedidoResumoModel> pesquisar(PedidoFilter filtro) {
		List<Pedido> todosPedidos = pedidoRepository.findAll(PedidoSpecs.usandoFiltro(filtro));
		
		return pedidoResumoModelAssembler.toCollectionModel(todosPedidos);
	}
	
	@GetMapping("/{codigoPedido}")
	public PedidoModel buscar(@PathVariable String codigoPedido) {
		Pedido pedido = emissaoPedidoService.buscarOuFalhar(codigoPedido);
		
		return pedidoModelAssembler.toModel(pedido);
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public PedidoModel emitirPedido(@RequestBody @Valid PedidoInput pedidoInput) {
		try {
	        Pedido novoPedido = pedidoInputDisassembler.toDomainObject(pedidoInput);

	        // TODO pegar usuário autenticado
	        novoPedido.setCliente(new Usuario());
	        novoPedido.getCliente().setId(1L);

	        novoPedido = emissaoPedido.emitir(novoPedido);

	        return pedidoModelAssembler.toModel(novoPedido);
	    } catch (EntidadeNaoEncontradaException e) {
	        throw new NegocioException(e.getMessage(), e);
	    }
	}
}
