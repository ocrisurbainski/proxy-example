package com.urbainski.pessoa.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class PessoaResponseDTO {

    private Integer oid;
    private String nome;
    private String documento;
    private LocalDate nascimento;
    private TipoPessoaDTO tipoPessoa;
    private Boolean agro;

}
