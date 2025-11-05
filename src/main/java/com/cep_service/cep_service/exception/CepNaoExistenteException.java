package com.cep_service.cep_service.exception;

public class CepNaoExistenteException extends RuntimeException{
    public CepNaoExistenteException(String mensagem) {
        super(mensagem);
    }
}
