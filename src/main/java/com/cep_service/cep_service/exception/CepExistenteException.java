package com.cep_service.cep_service.exception;

public class CepExistenteException extends RuntimeException{
    public CepExistenteException(String mensagem) {
        super(mensagem);
    }
}
