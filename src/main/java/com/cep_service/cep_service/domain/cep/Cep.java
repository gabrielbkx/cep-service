package com.cep_service.cep_service.domain.cep;

import com.cep_service.cep_service.domain.cep.dto.DadosDetalharCep;
import com.cep_service.cep_service.domain.cep.dto.DadosSalvarCep;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ceps")
@EqualsAndHashCode(of = "id")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Cep {

    // vou deixar o jpa criar as colunas conforme escrito nos atributos
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "numero_cep")
    private String numeroCep;
    private String logradouro;
    private String cidade;


    public Cep(DadosSalvarCep dados) {
        this.numeroCep = dados.numeroCep();
        this.logradouro = dados.logradouro();
        this.cidade = dados.cidade();
    }


    public Cep(DadosDetalharCep cep) {
        this.id = cep.id();
        this.numeroCep = cep.numeroCep();
        this.logradouro = cep.logradouro();
        this.cidade = cep.cidade();
    }

    public Cep(String numeroCep, String Logradouro, String Cidade) {
        this.numeroCep = numeroCep;
        this.logradouro = Logradouro;
        this.cidade = Cidade;
    }
}
