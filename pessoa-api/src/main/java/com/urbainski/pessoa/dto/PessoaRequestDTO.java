package com.urbainski.pessoa.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class PessoaRequestDTO {

    private Integer oid;
    private String nome;
    private LocalDate nascimento;
    private TipoPessoaDTO tipoPessoa;
    private Boolean agro;

}
