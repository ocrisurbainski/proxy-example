package com.urbainski.pessoa;

import com.urbainski.pessoa.dto.PessoaRequestDTO;
import com.urbainski.pessoa.dto.PessoaResponseDTO;
import com.urbainski.pessoa.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        return pessoa.map(ResponseEntity::ok).orElseThrow(() -> NotFoundException.ofPessoaNaoEncontrada(documento));
    }

    @PostMapping("/{documento}")
    public ResponseEntity<PessoaResponseDTO> salvar(@PathVariable String documento, @RequestBody PessoaRequestDTO pessoaRequestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pessoaData.salvar(documento, pessoaRequestDTO));
    }

    @PutMapping("/{documento}")
    public ResponseEntity<PessoaResponseDTO> atualizar(@PathVariable String documento, @RequestBody PessoaRequestDTO pessoaRequestDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(pessoaData.atualizar(documento, pessoaRequestDTO));
    }

    @DeleteMapping("/{documento}")
    public ResponseEntity<Void> deletar(@PathVariable String documento) {
        pessoaData.deletar(documento);
        return ResponseEntity.ok().build();
    }

}
