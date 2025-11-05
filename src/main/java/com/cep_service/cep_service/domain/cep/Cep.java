package com.cep_service.cep_service.domain.cep;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.*;

@Entity
@Table(name = "ceps")
@EqualsAndHashCode(of = "id")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
public class Cep {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
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

    public void atualizarInformacoes(@Valid DadosatualizarCep dados) {
        if (dados.numeroCep()!= null) {
            this.numeroCep = dados.numeroCep();
        }
        if (dados.logradouro() != null) {
            this.logradouro = dados.logradouro();
    }
        if (dados.cidade() != null) {
            this.cidade = dados.cidade();
        }
    }

}
