package com.cep_service.cep_service.domain;

import jakarta.persistence.*;
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
}
