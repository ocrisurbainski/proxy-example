package com.urbainski.pessoa;

import com.urbainski.pessoa.dto.PessoaResponseDTO;
import com.urbainski.pessoa.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pessoa")
public class PessoaController {

    private final PessoaData pessoaData;

    @Autowired
    public PessoaController(final PessoaData pessoaData) {
        this.pessoaData = pessoaData;
    }

    @GetMapping("/{documento}")
    public ResponseEntity<PessoaResponseDTO> buscarPessoaPorDocumento(@PathVariable String documento) {
        final var pessoa = pessoaData.buscarPessoaPorDocumento(documento);
        return pessoa.map(ResponseEntity::ok).orElseThrow(() -> new NotFoundException(String.format("Pessoa com documento '%s' não encontrada!", documento)));
    }

}
