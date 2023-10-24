package com.urbainski.pessoa;

import com.urbainski.pessoa.dto.PessoaResponseDTO;
import com.urbainski.pessoa.dto.TipoPessoaDTO;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.Month;
import java.util.LinkedHashMap;
import java.util.Optional;

@Component
public class PessoaData {

    public LinkedHashMap<String, PessoaResponseDTO> data;

    @PostConstruct
    public void init() {
        final var p1 = PessoaResponseDTO.builder()
                .oid(25636)
                .nome("Cristian Elder Urbainski")
                .documento("08789761910")
                .tipoPessoa(TipoPessoaDTO.FISICA)
                .agro(Boolean.TRUE)
                .nascimento(LocalDate.of(1992, Month.DECEMBER, 24))
                .build();

        this.data = new LinkedHashMap<>();
        this.data.put(p1.getDocumento(), p1);
    }

    public Optional<PessoaResponseDTO> buscarPessoaPorDocumento(String documento) {
        return Optional.ofNullable(this.data.get(documento));
    }

}
