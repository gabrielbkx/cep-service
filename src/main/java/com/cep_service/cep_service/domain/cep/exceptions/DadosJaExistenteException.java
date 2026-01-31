package com.cep_service.cep_service.domain.cep.exceptions;

public class DadosJaExistenteException extends RuntimeException{
    public DadosJaExistenteException(String mensagem) {
        super(mensagem);
    }
}
