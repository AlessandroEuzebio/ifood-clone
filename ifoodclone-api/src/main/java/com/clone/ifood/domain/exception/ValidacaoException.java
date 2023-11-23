package com.clone.ifood.domain.exception;

import org.springframework.validation.BindingResult;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor //já cria o construtor recebendo bindingResults como parâmetro
@Getter
public class ValidacaoException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	private BindingResult bindingResult;

}
