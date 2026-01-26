package com.cep_service.cep_service.exception;

public class DadosJaExistenteException extends RuntimeException{
    public DadosJaExistenteException(String mensagem) {
        super(mensagem);
    }
}
