package com.urbainski.pessoa;

import com.urbainski.pessoa.dto.PessoaRequestDTO;
import com.urbainski.pessoa.dto.PessoaResponseDTO;
import com.urbainski.pessoa.dto.TipoPessoaDTO;
import com.urbainski.pessoa.exception.DataDuplicatedException;
import com.urbainski.pessoa.exception.NotFoundException;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.Month;
import java.util.LinkedHashMap;
import java.util.Objects;
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

    public PessoaResponseDTO salvar(String documento, PessoaRequestDTO pessoaRequestDTO) {
        if (this.data.containsKey(documento)) {
            throw DataDuplicatedException.ofPessoaDuplicata(documento);
        }
        this.data.put(documento, toPessoaResponseDTO(documento, pessoaRequestDTO));
        return this.data.get(documento);
    }

    public PessoaResponseDTO atualizar(String documento, PessoaRequestDTO pessoaRequestDTO) {
        if (this.data.containsKey(documento)) {
            this.data.put(documento, toPessoaResponseDTO(documento, pessoaRequestDTO));
            return this.data.get(documento);
        }
        throw NotFoundException.ofPessoaNaoEncontrada(documento);
    }

    public void deletar(String documento) {
        if (this.data.containsKey(documento)) {
            this.data.remove(documento);
            return;
        }
        throw NotFoundException.ofPessoaNaoEncontrada(documento);
    }

    private PessoaResponseDTO toPessoaResponseDTO(String documento, PessoaRequestDTO pessoaRequestDTO) {
        return PessoaResponseDTO.builder()
                .oid(pessoaRequestDTO.getOid())
                .nome(pessoaRequestDTO.getNome())
                .documento(documento)
                .nascimento(pessoaRequestDTO.getNascimento())
                .tipoPessoa(pessoaRequestDTO.getTipoPessoa())
                .agro(pessoaRequestDTO.getAgro())
                .build();
    }

}
