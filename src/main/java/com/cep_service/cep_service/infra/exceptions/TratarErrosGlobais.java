package com.cep_service.cep_service.infra.exceptions;

import com.cep_service.cep_service.excel.exception.ExcelProcessamentoException;
import com.cep_service.cep_service.infra.exceptions.dto.DadosErro;
import com.cep_service.cep_service.infra.exceptions.dto.DadosErroValidacao;
import com.cep_service.cep_service.infra.security.exceptions.TokenInvalidoException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class TratarErrosGlobais {


    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<DadosErro> tratarErro404(EntityNotFoundException e) {
        String mensagem = e.getMessage();
        DadosErro dadosErro = new DadosErro(mensagem, 404);
        return ResponseEntity.status(dadosErro.status()).body(dadosErro);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<DadosErro> tratarErro500(Exception e) {

        // Detalhe ligeiro galera:  Usar essa mensagem faz com que nosso codigo nao exiba informações sensiveis como
        // tabelas de banco de dados, etc para o frontend ou quem esteja consumindo nossa API. Se eu fizesse como esta
        // no metodo acima os logs da classe Exception ia expor demais nossa plicaçao
        String mensagem = "Ocorreu um erro interno no servidor.";

        DadosErro dadosErro = new DadosErro(mensagem, 500);
        return ResponseEntity.status(dadosErro.status()).body(dadosErro);
    }

    // Esse metodo trata os erros de validação dos dados de entrada (400 Bad Request) do bean validation
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<DadosErroValidacao>> tratarErro400(MethodArgumentNotValidException e) {

        var erros = e.getFieldErrors()
                .stream()
                .map(error -> new DadosErroValidacao(error.getField(), error.getDefaultMessage()))
                .collect(Collectors.toList());

        return ResponseEntity.status(400).body(erros);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<DadosErro> tratarErro403(AccessDeniedException e) {

        String mensagem = "Você não tem permissão para acessar este recurso.";
        DadosErro dadosErro = new DadosErro(mensagem, HttpStatus.FORBIDDEN.value());
        return ResponseEntity.status(dadosErro.status()).body(dadosErro);
    }

    @ExceptionHandler(TokenInvalidoException.class) // Ou JWTVerificationException.class
    public ResponseEntity<DadosErro> tratarErroTokenInvalido(TokenInvalidoException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED) // 401
                .body(new DadosErro("Token inválido ou expirado", HttpStatus.UNAUTHORIZED.value()));
    }


    //excel handlers
    @ExceptionHandler(ExcelProcessamentoException.class)
    public ResponseEntity tratarErroDeProcessamentoExcel(ExcelProcessamentoException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao gerar a planilha Excel: "
                + ex.getMessage());
    }
}