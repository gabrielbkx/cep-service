package com.cep_service.cep_service.domain.cep.exceptions;

public class CepNaoExistenteException extends RuntimeException{
    public CepNaoExistenteException(String mensagem) {
        super(mensagem);
    }
}
