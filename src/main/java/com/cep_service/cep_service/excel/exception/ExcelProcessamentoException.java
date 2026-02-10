package com.cep_service.cep_service.excel.exception;


public class ExcelProcessamentoException extends RuntimeException {
    public ExcelProcessamentoException(String message) {
        super(message);
    }

    public ExcelProcessamentoException(String message, Throwable cause) {
        super(message, cause);
    }
}